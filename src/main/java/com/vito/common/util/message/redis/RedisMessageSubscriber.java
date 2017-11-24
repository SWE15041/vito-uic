package com.vito.common.util.message.redis;

import com.vito.common.util.bean.SerializeUtil;
import com.vito.common.util.cache.RedisCacheUtil;
import com.vito.common.util.message.MessageConsumer;
import com.vito.common.util.message.MessageSubscriber;
import com.vito.common.util.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Describe: redis订阅器基础类
 * CreateUser: zhaixm
 * CreateTime: 2015/12/17 11:03
 */
public class RedisMessageSubscriber extends MessageSubscriber {

    private RedisCacheUtil redisCacheUtil;
    private SubscribersRunner subscribersRunner;

    public RedisMessageSubscriber(String host, Integer port) {
        redisCacheUtil = new RedisCacheUtil(host, port);
    }

    @Override
    public void doSubscribe() {
        try {
            if (subscribersRunner == null) {
                subscribersRunner = new SubscribersRunner();
                new Thread(subscribersRunner).start();
            } else {
                subscribersRunner.reset();
            }
        } catch (Exception e) {
            throw new RuntimeException("redis订阅出错", e);
        }
    }

    @Override
    public void destroy() {
        subscribersRunner.stop();
        redisCacheUtil.closePool();
    }

    /**
     * 订阅主执行器
     */
    public class SubscribersRunner implements Runnable {

        private Logger logger = LoggerFactory.getLogger(this.getClass());
        private Jedis jedis;
        private BinaryJedisPubSub pubSub = null;
        private boolean resetFlag = false;
        private boolean stopFlag = false;

        /**
         * 订阅器异步执行线程池
         */
        private ExecutorService subscriberExecutorService = Executors.newFixedThreadPool(10);

        public SubscribersRunner() {
            this.jedis = redisCacheUtil.getJedis();
        }

        @Override
        public void run() {
            pubSub = new BinaryJedisPubSub() {
                @Override
                public void onMessage(byte[] channel, byte[] message) {
                    String topic = StringUtil.getStr(channel);
                    List<MessageConsumer> consumers = getConsumers(topic);
                    for (MessageConsumer consumer : consumers) {
                        Object target = SerializeUtil.convertBytes2Obj(message);
                        logger.debug("收到消息:" + target);
                        subscriberExecutorService.execute(new SubscriberRunner(consumer, target));
                    }
                }
            };
            byte[][] topics = getTopicBytes();
            //该方法具有阻断性
            jedis.subscribe(pubSub, topics);

            //重置订阅器
            while (!stopFlag) {
                if (resetFlag) {
                    logger.debug("订阅器已重置，重新订阅");
                    resetFlag = false;
                    run();
                }
            }
        }

        private byte[][] getTopicBytes() {
            Set<String> topics = getTopics();
            logger.debug("当前订阅topic:" + topics);
            byte[][] topicBytes = new byte[topics.size()][];
            int i = 0;
            for(String topic : topics) {
                topicBytes[i++] = StringUtil.getBytes(topic);
            }
            return topicBytes;
        }

        public void reset() {
            try {
                //如果pubSub监听器已创建则需要取消监听并设置flag，不能在此处直接再订阅否则会阻断调用者
                if (pubSub != null) {
                    logger.debug("加入新订阅主题，取消原订阅器");
                    pubSub.unsubscribe();
                    resetFlag = true;
                }
            } catch (Exception e) {
                logger.error("重置redis订阅器失败", e);
            }
        }

        public void stop() {
            logger.debug("停止订阅线程begin...");
            stopFlag = true;
            if (pubSub != null) {
                logger.debug("取消redis订阅器");
                pubSub.unsubscribe();
            }
            if (jedis != null) {
                logger.debug("返回jedis连接");
                redisCacheUtil.closeResource(jedis, false);
            }
            subscriberExecutorService.shutdown();
            logger.debug("停止订阅线程end");
        }

        /**
         * 消费者执行器
         */
        public class SubscriberRunner implements Runnable {
            private MessageConsumer consumer;
            private Object message;

            public SubscriberRunner(MessageConsumer consumer, Object message) {
                this.consumer = consumer;
                this.message = message;
            }

            @Override
            public void run() {
                consumer.consume(message);
            }
        }
    }
}
