package com.vito.common.comp.concurrent.task;

import com.vito.common.comp.concurrent.watch.AbsWatchedExecutor;

/**
 * 作者: zhaixm
 * 日期: 2017/3/18 0:00
 * 描述: 任务执行器
 */
public abstract class ConcurrentTaskExecutor<T extends ConcurrentTask, F> extends AbsWatchedExecutor<F> {

    protected T task;

    public ConcurrentTaskExecutor(T task) {
        this.task = task;
    }

}
