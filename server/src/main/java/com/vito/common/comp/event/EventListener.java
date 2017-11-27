package com.vito.common.comp.event;

/**
 * 事件监听器
 * @param <E>
 */
public interface EventListener<E extends EventObject> {

    void onEvent(E event);

}