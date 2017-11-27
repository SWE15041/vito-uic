package com.vito.common.util.message;
/**
 * 等级：A
 * Created by zhaixm on 2015/12/28.
 */

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2015/12/28 16:10
 */
public interface MessageProducer {

    void publish(String topic, Object message);

    void destroy();

}
