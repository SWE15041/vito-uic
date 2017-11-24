package com.vito.common.comp.concurrent.task.loader;

import com.vito.common.comp.concurrent.task.ConcurrentTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 作者: zhaixm
 * 日期: 2017/3/17 14:24
 * 描述: 本地任务加载器
 */
public abstract class ConcurrentTaskLoaderLocal<T extends ConcurrentTask> extends AbsConcurrentTaskLoader<T> {

    private Queue<T> queue = new LinkedBlockingQueue<>();

    @Override
    protected void push(T task) {
        // 注意此处要用offer 不能用push，因为push在队列满时会阻塞这样会导致channelHandler无法正常处理接收到的数据 最终造成bytebuf数据堆积
        boolean result = queue.offer(task);
        if (!result) {
            //TODO 发送邮件报警 数据处理出现异常导致队列数据溢出
            throw new RuntimeException("任务队列溢出");
        }
    }

    @Override
    protected List<T> pull(int pullNum) {
        List<T> tasks = new ArrayList<>();
        T task = queue.poll();
        while (task != null && pullNum-- > 0) {
            tasks.add(task);
            task = queue.poll();
        }
        return tasks;
    }
}
