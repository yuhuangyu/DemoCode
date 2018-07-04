package com.api.libgoogle;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//
//import com.joyreach.download.common.ProxyDownConfig;

/**
 * 文件下载工具类
 * 
 * @author JOJO 2018年6月12日上午9:47:59
 *
 */
public class HttpUtil {
	/**
	 * 连接超时时间
	 */
	public static int connectTimeout = 15000;
	/**
	 * 读取超时时间
	 */
	public static int readTimeout = 30000;
	/**
	 * 相应成功状态码
	 */
	public static final int SUCCESS_STATUS = 200;
	/**
	 * 请求头-cookie参数key
	 */
	public static final String REQUEST_HEADER_COOKIE_KEY = "Cookie";
	/**
	 * 请求头-User-Agent参数key
	 */
	public static final String REQUEST_HEADER_USER_AGENT_KEY = "User-Agent";
	/**
	 * 请求头-User-Agent参数value
	 */
	public static final String REQUEST_HEADER_USER_AGENT_VALUE = "AndroidDownloadManager/4.1.1 (Linux; U; Android 4.1.1; Nexus S Build/JRO03E)";
	/**
	 * 请求成功返回值
	 */
	public static final int CONN_RETURN_SUCCESS = 1;
	/**
	 * 请求失败返回值(未知原因)
	 */
	public static final int CONN_RETURN_FAIL = -1;
	/**
	 * 请求超时返回值
	 */
	public static final int CONN_RETURN_OUT_TIME = 2;
	/**
	 * 超时重新请求次数
	 */
	public static final int CONN_OUT_TIME_RETRY_COUNT = 2;

//	private static Logger logger = org.slf4j.LoggerFactory.getLogger(HttpUtil.class);

	public static int proxyGetMethodDown(String url) {
		String proxy = "149.129.218.75";
		int port = 8888;
		final String user = "jojo_1";
		final String password = "123456";

		int status = CONN_RETURN_FAIL;
		HttpClient httpClient = new HttpClient();
		InputStream responseInputStream = null;
		FileOutputStream fileOutputStream = null;
		BufferedInputStream bis = null;
		GetMethod getMethod = null;
		try {
			httpClient.getHostConfiguration().setProxy(proxy, port);
			httpClient.getParams().setAuthenticationPreemptive(true);
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
			// 如果代理需要密码验证，这里设置用户名密码
			httpClient.getState().setProxyCredentials(AuthScope.ANY,
					new UsernamePasswordCredentials(user, password));
			getMethod = new GetMethod(url);
			getMethod.addRequestHeader(REQUEST_HEADER_USER_AGENT_KEY, REQUEST_HEADER_USER_AGENT_VALUE);
//			if (StringUtils.isNotBlank(config.getCookie())) {
//				getMethod.addRequestHeader(REQUEST_HEADER_COOKIE_KEY, config.getCookie());
//			}
			httpClient.executeMethod(getMethod);
			System.out.println("===== "+getMethod.getStatusCode());
			if (!(getMethod.getStatusCode() == SUCCESS_STATUS)) {
				return status;
			}
			System.out.println("=== "+getMethod.getStatusCode());
//			new File("D:\\test", pkg+".apk")
			fileOutputStream = new FileOutputStream("D:\\test");
			responseInputStream = getMethod.getResponseBodyAsStream();
			bis = new BufferedInputStream(responseInputStream);
			int len = 2048;
			byte[] b = new byte[len];
			while ((len = bis.read(b)) != -1) {
				fileOutputStream.write(b, 0, len);
			}
			status = CONN_RETURN_SUCCESS;
		} catch (ConnectTimeoutException e) {
			status = CONN_RETURN_OUT_TIME;
		} catch (SocketTimeoutException e) {
			status = CONN_RETURN_OUT_TIME;
		} catch (Exception e) {
//			logger.error("proxy down fail: HttpClientProxy_proxyGetMethodDown", e);
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
			}
			try {
				if (responseInputStream != null) {
					responseInputStream.close();
				}
			} catch (IOException e) {
			}
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
			}
			try {
				if (getMethod != null) {
					getMethod.releaseConnection();
				}
			} catch (Exception e) {
			}
		}
		return status;
	}

	/*public static int httpMethodDown(ProxyDownConfig config) {
		int status = CONN_RETURN_SUCCESS;
		FileOutputStream fileOutputStream = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		try {
			fileOutputStream = new FileOutputStream(config.getFileNamePath());
			URL fileUrl = new URL(config.getDownUrl());
			httpUrl = (HttpURLConnection) fileUrl.openConnection();
			httpUrl.setRequestProperty(REQUEST_HEADER_USER_AGENT_KEY, REQUEST_HEADER_USER_AGENT_VALUE);
//			if (StringUtils.isNotBlank(config.getCookie())) {
//				httpUrl.setRequestProperty(REQUEST_HEADER_COOKIE_KEY, config.getCookie());
//			}
			httpUrl.setConnectTimeout(connectTimeout);
			httpUrl.setReadTimeout(readTimeout);
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());
			int len = 2048;
			byte[] b = new byte[len];
			while ((len = bis.read(b)) != -1) {
				fileOutputStream.write(b, 0, len);
			}
			status = CONN_RETURN_SUCCESS;
		} catch (ConnectTimeoutException e) {
			status = CONN_RETURN_OUT_TIME;
		} catch (SocketTimeoutException e) {
			status = CONN_RETURN_OUT_TIME;
		} catch (Exception e) {
//			logger.error("http conn fail: HttpClientProxy_httpMethodDown", e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (Exception e) {
				}
			}
			if (httpUrl != null) {
				try {
					httpUrl.disconnect();
				} catch (Exception e) {
				}
			}
		}
		return status;
	}*/
}
