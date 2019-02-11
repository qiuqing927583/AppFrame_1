package com.android.yawei.jhoa.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*************
 * 记事本数据库
 * @author Yusz
 *
 */
public class DatabaseNotepadHelper extends SQLiteOpenHelper{

	private final static String db_name = "notepad.db";
	public DatabaseNotepadHelper(Context context) {
		super(context, db_name, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql_Sendermsg = "create table " + "notepad" + "(id integer primary key autoincrement," 
				+ "guid" +" TEXT,"+"n_title" + " TEXT,"
				+ "n_content" + " TEXT,"
				+ "n_time" + " TEXT"+ ");";
		db.execSQL(sql_Sendermsg);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
		if(currentVersion>oldVersion){
			AddFieldToTable(db);
		}
	}
	//数据库升级往表中插入一个字段
	private void AddFieldToTable(SQLiteDatabase db){
		db.execSQL("ALTER TABLE notepad ADD  guid TEXT;");
	}
}
