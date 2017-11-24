package com.vito.common.util.message.kafka;

import com.vito.common.util.bean.SerializeUtil;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2015/12/29 17:50
 */
public class KafkaObjectDeserializer implements Deserializer<Object> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        try {
            if (data == null)
                return null;
            else
                return SerializeUtil.convertBytes2Obj(data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerializationException("反序列化对象出错，请检查消息对象是否有继承Serializable", e);
        }
    }

    @Override
    public void close() {

    }

}
