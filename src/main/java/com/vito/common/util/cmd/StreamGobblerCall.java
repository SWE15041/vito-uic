package com.vito.common.util.cmd;

import com.vito.common.util.string.EncodeUtil;

import java.io.*;
import java.util.concurrent.Callable;

public class StreamGobblerCall implements Callable<String> {

//	private Logger logger = Logger.getLogger(StreamGobblerCall.class);

	InputStream is;
	StreamGobblerType type;
	OutputStream os;

	public StreamGobblerCall(InputStream is, StreamGobblerType type) {
		this(is, type, null);
	}

	public StreamGobblerCall(InputStream is, StreamGobblerType type,
			OutputStream redirect) {
		this.is = is;
		this.type = type;
		this.os = redirect;
	}

	public String call() throws Exception {
		StringBuilder sb = new StringBuilder();
		try {
			PrintWriter pw = null;
			if (os != null) {
				pw = new PrintWriter(os);
			}
			InputStreamReader isr = new InputStreamReader(is, EncodeUtil.GBK);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				if (pw != null) {
					pw.println(line);
				}
				sb.append(type.getCode() + ">>" + line);
				sb.append("\r\n");
			}
//			switch (type) {
//			case ERROR:
//				logger.error(sb.toString());
//				break;
//			case OUTPUT:
//				logger.info(sb.toString());
//			default:
//				break;
//			}
			if (pw != null) {
				pw.flush();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new RuntimeException(ioe);
		}
		return sb.toString();
	}
}
