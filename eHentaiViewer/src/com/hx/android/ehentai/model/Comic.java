package com.hx.android.ehentai.model;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public class Comic implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private ContentValues mContentValues;

	public String title;
	public String keywords;
	public String coverPath;
	public int pages;
	public int rating;
	public String firstPageUrl;

	public ContentValues convertTo() {
		if (mContentValues == null)
			mContentValues = new ContentValues();

		mContentValues.put("title", title);
		mContentValues.put("keywords", keywords);
		mContentValues.put("coverPath", coverPath);
		mContentValues.put("pages", pages);
		mContentValues.put("rating", rating);
		mContentValues.put("firstPageUrl", firstPageUrl);
		return mContentValues;
	}

	public static Comic convertFrom(Cursor c) {
		Comic ret = new Comic();
		for (int i = 0; i < c.getColumnCount(); i++) {
			try {
				Field field = Comic.class.getDeclaredField(c.getColumnName(i));
				if(field.getType().equals(Integer.class))
					field.set(ret, c.getInt(i));
				else if (field.getType().equals(String.class))
					field.set(ret, c.getString(i));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} 
		}
		return ret;
	}
}
