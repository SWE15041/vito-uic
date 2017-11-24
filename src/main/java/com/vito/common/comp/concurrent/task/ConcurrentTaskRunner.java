package com.vito.common.comp.concurrent.task;

import com.vito.common.comp.concurrent.task.loader.ConcurrentTaskLoader;
import com.vito.common.comp.concurrent.watch.ConcurrentRunnerWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 作者: zhaixm
 * 日期: 2017/3/17 23:32
 * 描述: 任务运行线程
 */
public abstract class ConcurrentTaskRunner<T extends ConcurrentTask> implements Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ConcurrentTaskLoader<T> taskLoader;
    private ExecutorService executorService;
    private int consumeBatchNum;

    public ConcurrentTaskRunner(ConcurrentTaskLoader taskLoader, int threadPoolSize, int consumeBatchSize) {
        this.taskLoader = taskLoader;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.consumeBatchNum = consumeBatchSize;
    }

    @Override
    public void run() {
        // 创建监控线程，避免线程假死无限占用线程池资源
        ConcurrentRunnerWatcher executorWatcher = new ConcurrentRunnerWatcher();
        new Thread(executorWatcher).start();
        while (true) {
            try {
                // 从数据加载任务到执行队列
                taskLoader.loadTaskToQueue();
                // 查询待执行的任务
                List<T> tasks = taskLoader.consumeTask(consumeBatchNum);
                logger.debug("从队列中获取到待消费的任务数：{}", tasks.size());
                // 遍历创建执行分发的任务执行线程
                tasks.forEach(task -> {
                    ConcurrentTaskExecutor executor = getExecutor(task);
                    beforeSubmit(task);
                    Future<Boolean> future = executorService.submit(executor);
                    executorWatcher.add(executor, future);
                });
                // 避免过度频繁的查询
                Thread.sleep(5000);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 任务提交前拦截器
     * @param task
     */
    protected void beforeSubmit(T task) {

    }

    protected abstract ConcurrentTaskExecutor getExecutor(T task);

}
