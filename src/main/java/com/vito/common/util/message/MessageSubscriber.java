package com.vito.common.util.message;

import com.vito.common.util.validate.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2015/12/28 18:02
 */
public abstract class MessageSubscriber {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String, List<MessageConsumer>> consumerMap = new HashMap<>();

    public void subscribe(String topic, MessageConsumer consumer) {
        logger.debug("消费者[" + consumer + "]订阅topic:" + topic);
        addConsumer(topic, consumer);
        doSubscribe();
    }

    public abstract void destroy();

    protected abstract void doSubscribe();

    protected void addConsumer(String topic, MessageConsumer consumer) {
        synchronized (MessageSubscriber.class) {
            List<MessageConsumer> consumers = consumerMap.get(topic);
            if (Validator.isNull(consumers)) {
                consumers = new ArrayList<>();
            }
            consumers.add(consumer);
            consumerMap.put(topic, consumers);
        }
    }

    protected List<MessageConsumer> getConsumers(String topic) {
        return consumerMap.get(topic);
    }

    protected Set<String> getTopics() {
        return consumerMap.keySet();
    }

}
