package com.vito.common.comp.concurrent.watch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 多线程池监控线程
 * 
 * 轮询控制每个线程的执行时间，超时则执行强制退出
 * 
 * @author zhaixm
 *
 */
@SuppressWarnings("rawtypes")
public class ConcurrentRunnerWatcher implements Runnable {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConcurrentRunnerWatcher.class);

	/**
	 * 线程队列
	 */
	private Queue<WatchedExecutorModel> threadQueue = new LinkedBlockingQueue<>();

	@Override
	public void run() {
		while (true) {
			try {
				while (!threadQueue.isEmpty()) {
					WatchedExecutorModel watchedExecutorModel = threadQueue.poll();
					WatchedExecutor watchableThread = watchedExecutorModel.getWatchedExecutor();
					switch (watchableThread.runState()) {
					case WAITING:
						// 线程处于等待状态还未开始运行，不进行超时判断，放回监控线程池
						threadQueue.offer(watchedExecutorModel);
						break;
					case RUNNING:
						// 线程处于运行状态，进行超时判断
						Future threadFuture = watchedExecutorModel.getFuture();
						try {
                            //TODO 此方法不会被阻塞，可以考虑将超时判断改成isDone判断，解决调用get导致线程被阻塞从而造成超时判断不够准确的问题
//						    threadFuture.isDone()
							threadFuture.get(watchableThread.limitRunTime(), TimeUnit.MILLISECONDS);
							LOG.info("线程：" + watchableThread.name() + "执行结束");
						} catch (TimeoutException e) {
							LOG.error("线程：" + watchableThread.name() + "超时强制退出", e);
							watchableThread.shutDown();
							threadFuture.cancel(true);
						} catch (Exception e) {
							LOG.error("线程：" + watchableThread.name() + "监控到异常，强制结束", e);
							watchableThread.shutDown();
							threadFuture.cancel(true);
						}
						break;
					default:
						// 线程处于打断或者完成状态，不做处理
						break;
					}
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOG.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 添加被监控的线程
	 * 
	 * @param watchedCallable 实现WatchedCallable接口的线程类
	 * @param future WatchedCallable的Future期望对象
	 */
	public void add(WatchedExecutor watchedCallable, Future future) {
		this.threadQueue.offer(new WatchedExecutorModel(watchedCallable, future));
	}
	
}
