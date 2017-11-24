package com.vito.common.comp.concurrent.watch;

import java.util.concurrent.Future;

@SuppressWarnings("rawtypes")
public class WatchedExecutorModel {
	
	/**
	 * 可监控线程
	 */
	private WatchedExecutor watchedExecutor;
	
	/**
	 * 线程运行期望对象
	 */
	private Future future;
	
	public WatchedExecutorModel(WatchedExecutor watchedExecutor, Future future) {
		this.watchedExecutor = watchedExecutor;
		this.future = future;
	}

	public WatchedExecutor getWatchedExecutor() {
		return watchedExecutor;
	}

	public void setWatchedExecutor(WatchedExecutor watchedExecutor) {
		this.watchedExecutor = watchedExecutor;
	}

	public Future getFuture() {
		return future;
	}

	public void setFuture(Future future) {
		this.future = future;
	}

}
