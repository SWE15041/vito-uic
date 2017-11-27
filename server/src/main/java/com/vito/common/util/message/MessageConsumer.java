package com.vito.common.util.message;
/**
 * 等级：A
 * Created by zhaixm on 2015/12/28.
 */

/**
 * Describe: 消息消费者
 * CreateUser: zhaixm
 * CreateTime: 2015/12/28 16:21
 */
public interface MessageConsumer<T> {

    void consume(T message);

}
