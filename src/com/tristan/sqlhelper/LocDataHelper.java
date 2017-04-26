package com.tristan.sqlhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


//这个类帮助打开放在data/data中的localization数据库文件
public class LocDataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "localization.db";
    private static final int DATABASE_VERSION = 1;
	
    
    public LocDataHelper(Context context){
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
