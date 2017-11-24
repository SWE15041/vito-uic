package com.vito.common.comp.concurrent.task.loader;

import com.vito.common.comp.concurrent.task.ConcurrentTask;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/3/17 14:19
 * 描述: 任务加载器
 */
public interface ConcurrentTaskLoader<T extends ConcurrentTask> {

    /**
     * 加载更多任务到队列中
     *
     * @return
     */
    void loadTaskToQueue();

    /**
     * 返回指定数量的任务
     *
     * @param fetchNum
     * @return
     */
    List<T> consumeTask(int fetchNum);

}
