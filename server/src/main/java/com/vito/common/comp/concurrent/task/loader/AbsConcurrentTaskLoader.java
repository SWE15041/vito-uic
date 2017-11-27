package com.vito.common.comp.concurrent.task.loader;

import com.vito.common.util.validate.Validator;
import com.vito.common.comp.concurrent.task.ConcurrentTask;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 作者: zhaixm
 * 日期: 2017/3/17 14:24
 * 描述: 本地任务加载器
 */
public abstract class AbsConcurrentTaskLoader<T extends ConcurrentTask> implements ConcurrentTaskLoader<T> {

    private LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>();

    @Override
    public void loadTaskToQueue() {
        List<T> tasks = loadWaitingTasks();
        if (Validator.isNotNull(tasks)) {
            for (T task : tasks) {
                push(task);
            }
        }
    }

    @Override
    public List<T> consumeTask(int fetchNum) {
        return pull(fetchNum);
    }

    protected abstract void push(T task);

    protected abstract List<T> pull(int pullNum);

    protected abstract List<T> loadWaitingTasks();
}
