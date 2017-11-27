package com.vito.common.util.file;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileUtil {
	
	public static void copyFile(String srcFilePath, String destFilePath) {
		try {
			File srcFile = new File(srcFilePath);
			File destFile = new File(destFilePath);
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void deleFile(String filePath){
		File file = new File(filePath);  
	    // 判断目录或文件是否存在  
		if (file.exists()) {  
	        file.delete();  
		}
	}

}
