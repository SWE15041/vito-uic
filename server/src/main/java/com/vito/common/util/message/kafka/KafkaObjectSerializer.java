package com.vito.common.util.message.kafka;

import com.vito.common.util.bean.SerializeUtil;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2015/12/29 17:44
 */
public class KafkaObjectSerializer implements Serializer<Object> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Object data) {
        try {
            if (data == null)
                return null;
            else
                return SerializeUtil.convertObj2Bytes(data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerializationException("序列化对象出错，请检查消息对象是否有继承Serializable", e);
        }
    }

    @Override
    public void close() {

    }

}
