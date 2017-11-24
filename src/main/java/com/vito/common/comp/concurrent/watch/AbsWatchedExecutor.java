/**   
 * 文件名: AbsWatchedCallable.java   
 * 作者: zhaixm  
 * 版本: 2013-11-22 下午12:42:35 v1.0   
 * 日期: 2013-11-22    
 * 描述: 
 */
package com.vito.common.comp.concurrent.watch;

/**
 * 作者: zhaixm
 * 日期: 2013-11-22 下午12:42:35 
 * 描述: 
 */
public abstract class AbsWatchedExecutor<V> implements WatchedExecutor<V> {
	
	private volatile ThreadRunState runState = ThreadRunState.WAITING;
	
	/**
	 * 作者: zhaixm
	 * 版本: 2013-11-22 下午2:21:00
	 * 日期: 2013-11-22  
	 * 参数: 
	 * 返回: void
	 * 描述: 关闭线程（即强制停止）
	 */
	public void shutDown() {
		beforeStop();
		runState = ThreadRunState.INTERRUPT;
	}
	
	/**
	 * 作者: zhaixm
	 * 版本: 2013-11-22 下午2:21:25
	 * 日期: 2013-11-22  
	 * 参数: 
	 * 返回: void
	 * 描述: 线程停止前动作
	 */
	protected void beforeStop() {
	}
	
	protected boolean isShutdown() {
		return runState == ThreadRunState.INTERRUPT;
	}
	
	/**
	 * 作者: zhaixm
	 * 版本: 2013-11-22 下午2:20:45
	 * 日期: 2013-11-22  
	 * 参数: @return
	 * 返回: ThreadRunState
	 * 描述: 获取线程运行状态
	 */
	public ThreadRunState runState() {
		return runState;
	}
	
	@Override
	public V call() throws Exception {
		runState = ThreadRunState.RUNNING;
		V result = run();
		runState = ThreadRunState.END;
		return result;
	}
	
	/**
	 * 作者: zhaixm
	 * 版本: 2013-11-22 下午2:21:39
	 * 日期: 2013-11-22  
	 * 参数: @return
	 * 参数: @throws Exception
	 * 返回: V
	 * 描述: 线程真正运行过程，此方法执行前运行状态会改为RUNNING，结束后状态会改为END
	 */
	protected abstract V run() throws Exception;
	
	/**
	 * 作者: zhaixm
	 * 版本: 2013-11-14 下午3:46:34
	 * 日期: 2013-11-14  
	 * 参数: @return
	 * 返回: String
	 * 描述: 调度任务名称
	 */
	public abstract String name();
	
	/**
	 * 最大运行时间（毫秒）
	 * 
	 * @return
	 */
	public abstract long limitRunTime();
	
}
