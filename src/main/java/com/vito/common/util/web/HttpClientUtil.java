package com.vito.common.util.web;

import com.vito.common.util.string.EncodeUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HttpClientUtil {
	private static MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();

	private static int connectionTimeOut = 20000;

	private static int socketTimeOut = 10000;

	private static int maxConnectionPerHost = 5;

	private static int maxTotalConnections = 40;

	private static boolean initialed = false;

	public static void setPara() {
		manager.getParams().setConnectionTimeout(connectionTimeOut);
		manager.getParams().setSoTimeout(socketTimeOut);
		manager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
		manager.getParams().setMaxTotalConnections(maxTotalConnections);
		initialed = true;
	}
	
	@SuppressWarnings("finally")
	public static String getGetResponse(String url, String encode) {
		if (initialed) {
			HttpClientUtil.setPara();
		}
		HttpClient client = new HttpClient(manager);
		GetMethod getMethod = new GetMethod(url);
		getMethod.setFollowRedirects(true);

		String result = null;
		StringBuffer resultBuffer = new StringBuffer();

		try {

			client.executeMethod(getMethod);
			// String strGetResponseBody = post.getResponseBodyAsString();
			BufferedReader in = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(),
					getMethod.getResponseCharSet()));

			String inputLine = null;

			while ((inputLine = in.readLine()) != null) {
				resultBuffer.append(inputLine);
				resultBuffer.append("\n");
			}

			in.close();

			result = resultBuffer.toString();

			// iso-8859-1 is the default reading encode
			result = EncodeUtil.convertStrEncode(resultBuffer.toString(), getMethod.getResponseCharSet(), encode);
		} catch (Exception e) {
			e.printStackTrace();

			result = "";
		} finally {
			getMethod.releaseConnection();
			return result;
		}
	}

	@SuppressWarnings("finally")
	public static String getPostResponse(String url, String encode) {
		HttpClient client = new HttpClient(manager);

		if (initialed) {
			HttpClientUtil.setPara();
		}

		PostMethod post = new PostMethod(url);
		post.setFollowRedirects(false);

		StringBuffer resultBuffer = new StringBuffer();

		String result = null;

		try {
			client.executeMethod(post);

			BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(), post
					.getResponseCharSet()));
			String inputLine = null;

			while ((inputLine = in.readLine()) != null) {
				resultBuffer.append(inputLine);
				resultBuffer.append("\n");
			}

			in.close();

			// iso-8859-1 is the default reading encode
			result = EncodeUtil.convertStrEncode(resultBuffer.toString(), post.getResponseCharSet(), encode);
		} catch (Exception e) {
			e.printStackTrace();
			result = "";
		} finally {
			post.releaseConnection();
			return result;
		}
	}

	@SuppressWarnings("finally")
	public static String getPostResponse(String url, String encode, NameValuePair[] nameValuePair) {
		HttpClient client = new HttpClient(manager);

		if (initialed) {
			HttpClientUtil.setPara();
		}

		PostMethod post = new PostMethod(url);
		post.setRequestBody(nameValuePair);
		post.setFollowRedirects(false);

		String result = null;
		StringBuffer resultBuffer = new StringBuffer();

		try {
			client.executeMethod(post);
			BufferedReader in = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(), post
					.getResponseCharSet()));
			String inputLine = null;

			while ((inputLine = in.readLine()) != null) {
				resultBuffer.append(inputLine);
				resultBuffer.append("\n");
			}

			in.close();

			// iso-8859-1 is the default reading encode
			result = EncodeUtil.convertStrEncode(resultBuffer.toString(), post.getResponseCharSet(), encode);
		} catch (Exception e) {
			e.printStackTrace();
			result = "";
		} finally {
			post.releaseConnection();
			return result;
		}
	}

}
