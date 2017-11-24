package com.vito.common.util.message.redis;

import com.vito.common.util.bean.SerializeUtil;
import com.vito.common.util.cache.RedisCacheUtil;
import com.vito.common.util.message.MessageProducer;
import com.vito.common.util.string.StringUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2015/12/28 17:12
 */
public class RedisMessageProducer implements MessageProducer {

    private RedisCacheUtil redisCacheUtil;

    public RedisMessageProducer(String host, Integer port) {
        redisCacheUtil = new RedisCacheUtil(host, port);
    }

    @Override
    public void publish(String topic, Object message) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = redisCacheUtil.getJedis();
            jedis.publish(StringUtil.getBytes(topic), SerializeUtil.convertObj2Bytes(message));
        } catch (JedisException e) {
            broken = redisCacheUtil.handleJedisException(e);
            throw new RuntimeException("redis操作出错", e);
        } finally {
            redisCacheUtil.closeResource(jedis, broken);
        }
    }

    @Override
    public void destroy() {
        redisCacheUtil.closePool();
    }
}
