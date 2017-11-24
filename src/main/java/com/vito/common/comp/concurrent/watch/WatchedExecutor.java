/**   
 * 文件名: AbsWatchedCallable.java   
 * 作者: zhaixm  
 * 版本: 2013-11-22 下午12:42:35 v1.0   
 * 日期: 2013-11-22    
 * 描述: 
 */
package com.vito.common.comp.concurrent.watch;

import java.util.concurrent.Callable;

/**     
 * 作者: zhaixm
 * 日期: 2013-11-22 下午12:42:35 
 * 描述: 可检测的线程任务
 */
public interface WatchedExecutor<V> extends Callable<V> {
	
	/**
	 * 作者: zhaixm
	 * 版本: 2013-11-22 下午2:21:00
	 * 日期: 2013-11-22  
	 * 参数: 
	 * 返回: void
	 * 描述: 关闭线程（即强制停止）
	 */
	void shutDown();
	
	/**
	 * 作者: zhaixm
	 * 版本: 2013-11-22 下午2:20:45
	 * 日期: 2013-11-22  
	 * 参数: @return
	 * 返回: ThreadRunState
	 * 描述: 获取线程运行状态
	 */
	ThreadRunState runState();
	
	/**
	 * 作者: zhaixm
	 * 版本: 2013-11-14 下午3:46:34
	 * 日期: 2013-11-14  
	 * 参数: @return
	 * 返回: String
	 * 描述: 调度任务名称
	 */   
	String name();
	
	/**
	 * 最大运行时间（毫秒）
	 * 
	 * @return
	 */
	long limitRunTime();
	
}
