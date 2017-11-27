package com.vito.common.util.message.kafka;

import com.vito.common.util.message.MessageConsumer;
import com.vito.common.util.message.MessageSubscriber;
import com.vito.common.util.validate.Validator;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2015/12/29 14:45
 */
public class KafkaMessageSubscriber extends MessageSubscriber {

    private SubscribersRunner subscribersRunner;

    private KafkaConsumer<String, Object> subscriber;

    public KafkaMessageSubscriber(String group, String host, Integer port) {
        this(group, host + ":" + port);
    }

    public KafkaMessageSubscriber(String group, String... replica) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String repl : replica) {
            sb.append(repl);
            if (++i < replica.length) {
                sb.append(",");
            }
        }
        Properties props = new Properties();
        props.put("bootstrap.servers", sb.toString());
        props.put("group.id", group);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "com.ravan.common.util.message.kafka.KafkaObjectDeserializer");
        subscriber = new KafkaConsumer<>(props);
    }

    @Override
    protected void doSubscribe() {
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
        logger.info("停止订阅线程end");
        subscriber.close();
    }

    /**
     * 订阅主执行器
     */
    public class SubscribersRunner implements Runnable {

        private Logger logger = LoggerFactory.getLogger(this.getClass());

        private boolean resetFlag = false;

        private boolean stopFlag = false;

        /**
         * 订阅器异步执行线程池
         */
        private ExecutorService subscriberExecutorService = Executors.newFixedThreadPool(10);

        public SubscribersRunner() {
        }

        @Override
        public void run() {
            List<String> topicList = new ArrayList<>(getTopics());
            logger.info("订阅主题:" + topicList);
            subscriber.subscribe(topicList);
            while (!stopFlag) {
                ConsumerRecords<String, Object> records = subscriber.poll(100);
                logger.debug("拉取消息，接收到" + records.count() + "个");
                if (Validator.isNotNull(records) && records.count()>0) {
                    for (ConsumerRecord<String, Object> record : records) {
                        logger.debug("收到消息 key:" + record.key() + " value:" + record.value());
                        List<MessageConsumer> consumers = getConsumers(record.topic());
                        for (MessageConsumer consumer : consumers) {
                            subscriberExecutorService.execute(new SubscriberRunner(consumer, record.value()));
                        }
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        logger.error("kafka订阅执行器休眠异常", e);
                    }
                }
                if (resetFlag) {
                    resetFlag = false;
                    logger.debug("取消订阅");
                    subscriber.unsubscribe();
                    logger.debug("重新订阅");
                    run();
                }
            }
        }

        public void reset() {
            logger.info("重置订阅器");
            resetFlag = true;
        }


        public void stop() {
            logger.info("停止订阅线程begin...");
            stopFlag = true;
            try {
                subscriber.unsubscribe();
            } catch (Exception e) {
                logger.error("停止订阅失败");
            }
            subscriberExecutorService.shutdown();
            logger.info("停止订阅线程end");
        }

        /**
         * 消费者执行器
         */
        public class SubscriberRunner implements Runnable {
            private Logger logger = LoggerFactory.getLogger(this.getClass());

            private MessageConsumer consumer;
            private Object message;

            public SubscriberRunner(MessageConsumer consumer, Object message) {
                this.consumer = consumer;
                this.message = message;
            }

            @Override
            public void run() {
                logger.debug("消费者:"+ consumer + " 消费消息:" + message);
                consumer.consume(message);
            }
        }
    }

}
