package com.vito.common.util.bean;

import com.vito.common.util.validate.Validator;
import org.apache.commons.codec.binary.Base64;

import java.io.*;

public class SerializeUtil {

	/**
	 * 将对象存储在一个文件中
	 * 
	 * @param obj
	 * @param fileName
	 * @author:Administrator
	 */
	public static void convertObj2File(Object obj, String fileName) {
		try {
			FileOutputStream fs = new FileOutputStream(fileName);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(obj);
			os.close();
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将对象从一个文件中读出
	 * 
	 * @param fileName
	 * @return
	 * @author:Administrator
	 */
	public static Object convertFile2Obj(String fileName) {
		Object obj = null;
		try {
			FileInputStream fi = new FileInputStream(fileName);
			ObjectInputStream oi = new ObjectInputStream(fi);
			obj = oi.readObject();
			fi.close();
			oi.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return obj;
	}

	/**
	 * 将对象序列化
	 * @param obj
	 * @return
	 */
	public static byte[] convertObj2Bytes(Object obj) {
		try {
			if (Validator.isNull(obj)) {
				return null;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			return baos.toByteArray();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 将对象序列化并使用base64转为字符串
	 * 
	 * @param obj
	 * @return
	 * @author:zhaixm
	 */
	public static String convertObj2String(Object obj) {
		try {
			if (Validator.isNull(obj)) {
				return null;
			}
			return encodeBytes(convertObj2Bytes(obj));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static Object convertBytes2Obj(byte[] bytes) {
		try {
			if (Validator.isNull(bytes)) {
				return null;
			}
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将字符串转换为字节并反序列化为对象
	 * 
	 * @param streamStr
	 * @return
	 * @author:Administrator
	 */
	public static Object convertString2Obj(String streamStr) {
		try {
			if (Validator.isNull(streamStr)) {
				return null;
			}
			return convertBytes2Obj(decodeBytes(streamStr));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 克隆对象
	 * 
	 * @param source
	 * @return
	 * @author:Administrator
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(T source) {
		T target = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(source);
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bis);
			target = (T)ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return target;
	}
	
	/**
	 * 将图片序列化
	 * 
	 * @param imageFile
	 * @return
	 * @author:zhaixm
	 */
	public static String image2Stream(File imageFile) {
		String result = null;
		try {
			FileInputStream inputStream = new FileInputStream(imageFile);
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			result = encodeBytes(bytes);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	private static String encodeBytes(byte[] bytes) {
		return new String(Base64.encodeBase64(bytes));
	}

	private static byte[] decodeBytes(String str) {
		return Base64.decodeBase64(str.getBytes());
	}

	/**
	 * 反序列化图片
	 * 
	 * @param streamStr
	 * @return
	 * @author:Administrator
	 */
	public static void stream2Image(File destImageFile, String streamStr) {
		try {
			FileOutputStream outputStream = new FileOutputStream(destImageFile);
			outputStream.write(decodeBytes(streamStr));
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws Exception {
//		String stream = file2Stream(new File("F:\\logo.png"));
//		stream2File(new File("F:\\logo1.png"), stream);
//		
//		String stream1 = convertObj2String(new File("F:\\logo.png"));
//		File file = (File)convertString2Obj(stream1);
//		FileInputStream inputStream = new FileInputStream(file);
//		byte[] bytes = new byte[inputStream.available()];
//		inputStream.read(bytes);
//		FileOutputStream outputStream = new FileOutputStream(new File("F:\\logo2.png"));
//		outputStream.write(bytes);
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("F:\\a.txt")));
		StringBuilder sb = new StringBuilder();
		String s = null;
		while ((s = reader.readLine())!=null) {
			sb.append(s);
		}
		stream2Image(new File("F:\\logo3.png"), sb.toString());
	}
}
