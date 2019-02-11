package com.android.yawei.jhoa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.yawei.jhoa.bean.RecvMessage;
import com.android.yawei.jhoa.utils.GetSystemTime;

import java.util.ArrayList;
import java.util.List;


/*****************
 * 记事本数据库
 * @author Yusz
 *
 */
public class SQLiteNotepadHelper {
	
	private DatabaseNotepadHelper dbHelp;
	public SQLiteNotepadHelper(Context context) {
		this.dbHelp = new DatabaseNotepadHelper(context);
	}
	
	public void InsetNewNotepad(String guid,String title,String content,String time)throws Exception{
		SQLiteDatabase db=dbHelp.getWritableDatabase();
		db.beginTransaction();  //开始事务
		try {
			db.execSQL("INSERT INTO "+"notepad"+"("
					    +"guid"+""+","
						+"n_title"+""+ ","
						+"n_content" +""+ ","
						+"n_time"+")"+ "values(?,?,?,?)",
					new Object[]{guid,title,content,time});
			db.setTransactionSuccessful();  //设置事务成功完成    
		} finally {  
	          db.endTransaction();    //结束事务  
	      }
	}

	/**
	 * 根据用户唯一标示取出记事本内容
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	public List<RecvMessage> GetNotepadMsg(String guid)throws Exception{
		SQLiteDatabase db=dbHelp.getWritableDatabase();
		Cursor cv = db.rawQuery("SELECT * FROM notepad where guid = ? ", new String[]{guid});
		List<RecvMessage> listMsg = new ArrayList<RecvMessage>();
		while(cv.moveToNext()){
			RecvMessage recvmsg = new RecvMessage();
			recvmsg.SetRecvUserGuid(cv.getString(cv.getColumnIndex("id")));
			recvmsg.SetTitle(cv.getString(cv.getColumnIndex("n_title")));
			recvmsg.SetConent(cv.getString(cv.getColumnIndex("n_content")));
			recvmsg.SetMsgTime(GetSystemTime.GetSystemTimeDay(cv.getString(cv.getColumnIndex("n_time"))));
			listMsg.add(recvmsg);
		}
		return listMsg;
	}
	public void DelMsgById(String id)throws Exception{
		SQLiteDatabase db=dbHelp.getWritableDatabase();
		String[] arg = {id};
		db.delete("notepad", "id=?", arg);
	}
	public RecvMessage GetMsgById(String id)throws Exception{
		SQLiteDatabase db=dbHelp.getWritableDatabase();
		String[] arg = {id};
		Cursor cv = db.rawQuery("SELECT * FROM notepad"+" WHERE id = ? ", arg);
		RecvMessage recvmsg = new RecvMessage();
		while(cv.moveToNext()){
			recvmsg.SetTitle(cv.getString(cv.getColumnIndex("n_title")));
			recvmsg.SetConent(cv.getString(cv.getColumnIndex("n_content")));
			recvmsg.SetMsgTime(cv.getString(cv.getColumnIndex("n_time")));
		}
		return recvmsg;
	}
	public String GetMsgByTime(String time){
		SQLiteDatabase db=dbHelp.getWritableDatabase();
		String[] arg = {time};
		Cursor cv = db.rawQuery("SELECT * FROM notepad"+" WHERE n_time = ? ", arg);
		String id = "";
		while(cv.moveToNext()){
			id = cv.getString(cv.getColumnIndex("id"));
		}
		return id;
	}
	public void UpDataById(String id,String title,String Content,String time)throws Exception{
		SQLiteDatabase db=dbHelp.getWritableDatabase();
		ContentValues cnt = new ContentValues();
		cnt.put("n_title", title);
		cnt.put("n_content", Content);
		cnt.put("n_time", time);
		db.update("notepad", cnt, "id = ?", new String[]{id}); 
	}

	/**
	 * 记事本搜索
	 * @param guid 用户唯一标示
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public List<RecvMessage> SreachData(String guid,String data)throws Exception{
		SQLiteDatabase db=dbHelp.getWritableDatabase();
		List<RecvMessage> listData = new ArrayList<RecvMessage>();
		String current_sql_sel = "SELECT  * FROM notepad"+" where  (n_title like '%"+data+"%' or  n_content like '%"+data+"%' )"+" and guid='"+guid+"'";
		Cursor cv = db.rawQuery(current_sql_sel, null);
		while(cv.moveToNext()){
			RecvMessage re_data = new RecvMessage();
			re_data.SetRecvUserGuid(cv.getString(cv.getColumnIndex("id")));
			re_data.SetTitle(cv.getString(cv.getColumnIndex("n_title")));
			re_data.SetConent(cv.getString(cv.getColumnIndex("n_content")));
			re_data.SetMsgTime(GetSystemTime.GetSystemTimeDay(cv.getString(cv.getColumnIndex("n_time"))));
			listData.add(re_data);
		}
		return listData;
	}
}
