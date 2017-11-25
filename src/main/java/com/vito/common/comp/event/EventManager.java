package com.vito.common.comp.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Describe: 事件管理器
 * CreateUser: zhaixm
 * CreateTime: 2015/10/21 11:49
 */
public class EventManager<L extends EventListener> {

    private static final Logger LOG = LoggerFactory.getLogger(EventManager.class);

    private boolean throwException = false;

    private Set<L> eventListeners = new HashSet<>();

    public void fire(EventObject event) {
        for (EventListener listener : eventListeners) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                if (throwException) {
                    throw new EventException("调用事件监听器处理出错", e);
                }
                LOG.error("调用事件监听器处理出错", e);
            }
        }
    }

    public boolean isThrowException() {
        return throwException;
    }

    public void setThrowException(boolean throwException) {
        this.throwException = throwException;
    }

    public Set<L> getEventListeners() {
        return eventListeners;
    }

    public void setEventListeners(Set<L> entityEventListeners) {
        this.eventListeners = entityEventListeners;
    }

    public void addEventListener(L listener) {
        this.eventListeners.add(listener);
    }
}
