package com.love.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "alarms";
	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_NAME + "(" + AlarmInfo.ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + AlarmInfo.DESCRIPTION + " STRING," + AlarmInfo.TIME + " INTEGER,"
				+ AlarmInfo.FREQUENCY + " INTEGER," + AlarmInfo.DAYS_OF_WEEK
				+ " INTEGER," + AlarmInfo.ENABLE + " STRING"+ ")");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
		onCreate(db);
	}


}
