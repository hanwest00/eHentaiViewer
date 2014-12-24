package com.hx.android.ehentai.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private final static String DB_NAME = "ehentai";
	private final static String COMIC_TABLE_QUERY = "create table comic (title varchar(255), keywords varchar(255), coverPath varchar(255), pages integer, rating integer, firstPageUrl varchar(255) primary key)";

	public final static String TABLE_COMIC = "comic";
	
	public DatabaseHelper(Context context, int version)
	{
		this(context, DB_NAME, null, version);
	}
	
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(COMIC_TABLE_QUERY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
