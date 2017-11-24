package com.vito.common.util.cmd;

import org.apache.log4j.Logger;

import java.io.*;

public class StreamGobbler extends Thread {
	
	private Logger logger = Logger.getLogger(StreamGobbler.class);

	InputStream is;
	StreamGobblerType type;
	OutputStream os;
	StringBuilder sb;

	public StreamGobbler(InputStream is, StreamGobblerType type) {
		this.is = is;
		this.type = type;
	}

	public StreamGobbler(InputStream is, StreamGobblerType type, OutputStream redirect) {
		this.is = is;
		this.type = type;
		this.os = redirect;
	}
	
	public StreamGobbler(InputStream is, StreamGobblerType type, StringBuilder sb) {
		this.is = is;
		this.type = type;
		this.sb = sb;
	}

	public void run() {
		try {
			PrintWriter pw = null;
			if (os != null)
				pw = new PrintWriter(os);

			InputStreamReader isr = new InputStreamReader(is, "gbk");
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				if (pw != null){
					pw.println(line);
				}
				if(sb != null){
					sb.append(line);
					sb.append("\r\n");
				}
//				System.out.println(type.getName() + ">" + line);
				switch (type) {
				case ERROR:
					logger.error(line);
					break;
				case OUTPUT:
					logger.info(line);
				default:
					break;
				}
			}
			if (pw != null)
				pw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
