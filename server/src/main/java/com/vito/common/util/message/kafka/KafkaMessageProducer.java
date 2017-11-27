package com.vito.common.util.message.kafka;

import com.vito.common.util.message.MessageProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2015/12/29 14:24
 */
public class KafkaMessageProducer implements MessageProducer {

    private Producer<String, Object> producer;

    public KafkaMessageProducer(String host, Integer port) {
        this(host + ":" +port);
    }

    public KafkaMessageProducer(String... replica) {
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
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "com.ravan.common.util.message.kafka.KafkaObjectSerializer");
        producer = new KafkaProducer(props);
    }

    @Override
    public void publish(String topic, Object message) {
        producer.send(new ProducerRecord<String, Object>(topic, message.toString(), message));
//        producer.flush();
    }

    @Override
    public void destroy() {
        producer.close();

    }
}
