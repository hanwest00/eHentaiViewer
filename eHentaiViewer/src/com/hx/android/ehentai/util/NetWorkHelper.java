package com.hx.android.ehentai.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public final class NetWorkHelper {

	private static String cookieStr;

	public enum HttpRequestMethod {
		POST, GET
	}

	public static boolean checkNetwork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService("connectivity");
		NetworkInfo moblie = connectivityManager.getNetworkInfo(0);
		NetworkInfo wifi = connectivityManager.getNetworkInfo(1);
		return (moblie != null && moblie.isConnectedOrConnecting())
				|| (wifi != null && wifi.isConnectedOrConnecting());
	}

	public static void downloadFile(String url, File file) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpGet request = new HttpGet(url);

			HttpResponse response = httpClient.execute(request);

			byte[] buffer = EntityUtils.toByteArray(response.getEntity());

			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(buffer);
			outStream.close();
		} catch (IOException e) {
			// covers:
			// ClientProtocolException
			// ConnectTimeoutException
			// ConnectionPoolTimeoutException
			// SocketTimeoutException
			e.printStackTrace();
		}
	}

	public static byte[] httpRequestFile(String url) {
		if (!(url.startsWith("http://") || url.startsWith("https://")
				|| url.startsWith("HTTP://") || url.startsWith("HTTPs://"))) {
			url = "http://" + url;
		}
		HttpURLConnection conn = null;

		try {
			URL getIPUrl = new URL(url);
			conn = (HttpURLConnection) getIPUrl.openConnection();
			conn.setRequestMethod(HttpRequestMethod.GET.toString());
			conn.setUseCaches(false);
			conn.setConnectTimeout(3000);

			InputStream inputStream = conn.getInputStream();
			java.io.BufferedInputStream buffered = new java.io.BufferedInputStream(
					inputStream);
			int len = buffered.available();
			byte[] buffer = new byte[len];
			buffered.read(buffer);
			buffered.close();
			return buffer;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			conn.disconnect();
		}
	}

	public static boolean httpRequestFile(String url, File file) {
		if (!(url.startsWith("http://") || url.startsWith("https://")
				|| url.startsWith("HTTP://") || url.startsWith("HTTPs://"))) {
			url = "http://" + url;
		}
		HttpURLConnection conn = null;

		try {
			URL getIPUrl = new URL(url);
			conn = (HttpURLConnection) getIPUrl.openConnection();
			conn.setRequestMethod(HttpRequestMethod.GET.toString());
			conn.setUseCaches(false);
			conn.setConnectTimeout(3000);

			InputStream inputStream = conn.getInputStream();
			byte[] buffer = new byte[65500];
			int currLen = 0;

			FileOutputStream outStream = new FileOutputStream(file);

			while ((currLen = inputStream.read(buffer, 0, buffer.length)) > 0) {
				outStream.write(buffer, 0, currLen);
			}

			inputStream.close();
			outStream.close();
			return true;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			conn.disconnect();
		}
	}

	public static File httpRequestFile(String url, String cachePath) {
		File ret = FileManager.createFile(cachePath);
		httpRequestFile(url, ret);
		return ret;
	}

	public static void clearCookies() {
		cookieStr = "";
	}

	public static String getCookies() {
		return cookieStr;
	}

	public static String httpClientRequest(String urlStr,
			HttpRequestMethod method, String params, boolean openCookie) {

		String ret = "";
		HttpClient client = new DefaultHttpClient();

		HttpResponse response = null;

		try {
			if (method == HttpRequestMethod.GET) {
				HttpGet httpGet = new HttpGet(urlStr);
				response = client.execute(httpGet);
			} else {
				HttpPost httpPost = new HttpPost(urlStr);
				if (params != null && !"".equals(params)) {
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					for (String s : params.split("&")) {
						int pos = s.indexOf("=");
						if (pos < 1)
							continue;
						nvps.add(new BasicNameValuePair(s.substring(0, pos), s
								.substring(pos + 1)));
					}
					httpPost.setEntity(new UrlEncodedFormEntity(nvps,
							HTTP.UTF_8));
				}

				response = client.execute(httpPost);
			}

			if (openCookie) {
				HttpClientParams
						.setCookiePolicy(
								client.getParams(),
								org.apache.http.client.params.CookiePolicy.BROWSER_COMPATIBILITY);
			}

			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {

				HttpEntity entity = response.getEntity();
				if (entity != null)
					ret = EntityUtils.toString(entity, "utf-8");

				// 读取cookie
				List<Cookie> cookies = ((AbstractHttpClient) client)
						.getCookieStore().getCookies();
				if (!cookies.isEmpty()) {
					StringBuilder sb1 = new StringBuilder();
					for (int i = 0; i < cookies.size(); i++) {
						Cookie cookie = cookies.get(i);
						sb1.append("; "
								+ String.format("%s=%s", cookie.getName(),
										cookie.getValue()));
					}
					cookieStr = sb1.length() > 0 ? sb1.toString().substring(2)
							: "";
				}
			}

			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 某些机型无法获得cookie, 可以使用httpCilentRequest方法
	@SuppressLint("NewApi")
	public static String httpRequest(String urlStr, HttpRequestMethod method,
			String params, boolean openCookie) {
		HttpURLConnection conn = null;

		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(urlStr);

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method.toString());
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(2000);
			conn.setReadTimeout(6000);

			CookieManager cookieManager = null;
			if (openCookie) {

				conn.setRequestProperty("Cookie", cookieStr);

				cookieManager = new CookieManager();
				cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
				CookieHandler.setDefault(cookieManager);
				CookieStore cookieStore = cookieManager.getCookieStore();
				HttpCookie cookie = new HttpCookie("test", "");
				cookieStore.add(URI.create(urlStr), cookie);

			}

			if (method == HttpRequestMethod.POST) {
				conn.setDoOutput(true);

				OutputStream outStream = conn.getOutputStream();

				outStream.write(params.getBytes());
				outStream.flush();
				outStream.close();
			} else {
				conn.setDoOutput(false);
			}

			InputStream inputStream = conn.getInputStream();

			if (openCookie) {
				StringBuilder sb1 = new StringBuilder();
				List<HttpCookie> cookies = cookieManager.getCookieStore()
						.getCookies();
				for (HttpCookie cookie1 : cookies) {
					if (cookie1.toString().startsWith("test"))
						continue;
					sb1.append("; " + cookie1.toString());
				}
				cookieStr = sb1.length() > 0 ? sb1.toString().substring(2) : "";
			}

			byte[] buffer = new byte[65500];
			int currLen = 0;

			while ((currLen = inputStream.read(buffer, 0, buffer.length)) > 0) {
				sb.append(new String(buffer, 0, currLen, "utf-8"));
			}

			inputStream.close();
			return sb.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} finally {
			conn.disconnect();
		}

	}

	public static String httpRequest(String urlStr, HttpRequestMethod method,
			String params) {
		return httpRequest(urlStr, method, params, false);
	}
}
