package com.vito.common.util.cmd;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 系统命令运行工具类
 * 
 * @author Administrator
 *
 */
public class SystemCommandUtil {
	
	/**
	 * 运行系统命令
	 * 
	 * @param cmd
	 * @return
	 */
	public static String runCmd(String cmd) {
		String execInfo = "";
		// 取得程序的运行结果，必须使用线程否则会导致程序挂起
		ExecutorService pool = Executors.newFixedThreadPool(2);
		execInfo = runCmd(cmd, pool);
		pool.shutdown();
		return execInfo;
	}
	
	/**
	 * 运行系统命令
	 * 
	 * @param cmd
	 * @return
	 */
	public static String runCmd(String cmd, ExecutorService pool) {
		StringBuilder execInfo = new StringBuilder();
		Runtime rt = Runtime.getRuntime();
		try {
			Process proc = rt.exec(cmd);
			// 取得程序的运行结果，必须使用线程否则会导致程序挂起
			Callable<String> outputCallable = new StreamGobblerCall(proc
					.getInputStream(), StreamGobblerType.OUTPUT);
			Callable<String> errorCallable = new StreamGobblerCall(proc
					.getErrorStream(), StreamGobblerType.ERROR);
			Future<String> outputFuture = pool.submit(outputCallable);
			Future<String> errorFuture = pool.submit(errorCallable);
			proc.getOutputStream().close();
			proc.waitFor();
			proc.exitValue();
			execInfo.append(errorFuture.get());
			execInfo.append("\r\n");
			execInfo.append(outputFuture.get());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return execInfo.toString();
	}
}
