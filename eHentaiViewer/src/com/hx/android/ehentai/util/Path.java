package com.hx.android.ehentai.util;

public class Path {
	public final static String APP_STORAGE = android.os.Environment
			.getExternalStorageDirectory().toString();

	public final static String LAZY_LOAD_PATH = String.format("%s%s", APP_STORAGE, "/eHentaiViewer/LazyLoad/");
	public final static String CACHE_PATH = String.format("%s%s", APP_STORAGE, "/eHentaiViewer/Comic/");
}
