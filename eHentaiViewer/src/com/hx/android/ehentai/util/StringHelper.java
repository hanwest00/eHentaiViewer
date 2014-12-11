package com.hx.android.ehentai.util;

import java.lang.reflect.Field;

import org.json.*;

import android.util.Base64;

public final class StringHelper {
	public static String trim(String str, String str1) {
		if (str.startsWith(str1))
			str = str.substring(str1.length());

		if (str.endsWith(str1))
			str = str.substring(0, str.length() - str1.length());

		return str;
	}

	public static String startTrim(String str, String str1) {
		if (str.startsWith(str1))
			str = str.substring(str1.length());
		return str;
	}

	public static String endTrim(String str, String str1) {
		if (str.endsWith(str1))
			str = str.substring(0, str.length() - str1.length());
		return str;
	}

	public static <T> void simpleJSONDecode(T obj, String json) {
		try {
			JSONObject jsonObj = new JSONObject(json);
			if (jsonObj != null) {
				Field[] fields = obj.getClass().getDeclaredFields();

				for (Field f : fields) {
					try {
						f.setAccessible(true);
						Object v = jsonObj.get(f.getName());
						if (v != null) {
							f.set(obj, v);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static <T> String simpleJSONEncode(T obj) {
		JSONObject jsonObj = new JSONObject();
		if (obj != null) {
			Field[] fields = obj.getClass().getDeclaredFields();

			for (Field f : fields) {
				try {
					f.setAccessible(true);
					jsonObj.put(f.getName(), f.get(obj));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}

			}
			return jsonObj.toString();
		}

		return "";
	}

	public static String HXEncrypt(String encryptString) {
		if (encryptString == null || "".equals(encryptString.trim()))
			return "";

		try {
			String tmp;

			byte[] tmpBuffer = encryptString.getBytes("UTF-8");
			byte[] buffer = new byte[tmpBuffer.length];
			for (int i = 0; i < tmpBuffer.length; i++)
				buffer[i] = tmpBuffer[tmpBuffer.length - i - 1];
			
			tmp = Base64.encodeToString(buffer, Base64.DEFAULT);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < tmp.length(); i += 4) {
				if (i + 4 < tmp.length())
					sb.append(String.format("%s%s", tmp.substring(i, i + 4), "HX"));
				else
					sb.append(String.format("%s%s", tmp.substring(i), "HX"));
			}
			return Base64.encodeToString(sb.toString().getBytes("UTF-8"), Base64.DEFAULT);
		} catch (Exception e) {
			return "";
		}
	}

	public static String HXDecrypt(String decryptString) {
		try {
			if (decryptString == null || "".equals(decryptString.trim()))
				return "";

			String tmp = new String(Base64.decode(decryptString, Base64.DEFAULT), "UTF-8");

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < tmp.length(); i += 4) {
				if (i + 5 < tmp.length()) {

					if (!(tmp.charAt(i + 4) == 'H' && tmp.charAt(i + 5) == 'X'))
						continue;
					sb.append(tmp.substring(i, i + 4));
					i += 2;
				} else
					sb.append(tmp.substring(i));
			}
			
			byte[] tmpBuffer = Base64.decode(sb.toString(), Base64.DEFAULT);
			byte[] buffer = new byte[tmpBuffer.length];
			for (int i = 0; i < tmpBuffer.length; i++)
				buffer[i] = tmpBuffer[tmpBuffer.length - i - 1];

			return new String(buffer, "UTF-8");
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String getUrlParam(String url, String name) {
		if (url.length() > name.length()) {
			StringBuilder sb = new StringBuilder();
			try {
				if(url.indexOf(name) < 0) return "";
				for (int i = url.indexOf(name) + name.length() + 1; i < url.length(); i++) {
					if ("&".equals(url.charAt(i)))
						break;
					sb.append(url.charAt(i));
				}
				return sb.toString().trim();
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		return "";
	}
}
