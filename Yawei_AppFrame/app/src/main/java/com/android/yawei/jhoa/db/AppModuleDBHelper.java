package com.android.yawei.jhoa.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yusz on 2018-2-7.
 */

public class AppModuleDBHelper extends SQLiteOpenHelper{

    public final static String module_db = "yaweioa.db";
    public final static int version = 2;
    public final static String ALL_MOUDLE_TABLE = "all_moudle";
    public final static String USER_MOUDLE_TABLE = "user_moudle";
    public final static String USER_MSG_TABLE = "user_msg";
    public final static String userGuid = "userGuid";//
    public final static String module_Name = "moduleName";//中文名
    public final static String module_EN = "moduleEN";//英文名
    public final static String module_Url = "moduleUrl";//链接地址，如果是本地activity，为空
    public final static String module_ImageUrl = "moduleImageUrl";
    public final static String module_type = "moduleType";//模块打开类型  0：H5应用； 1：混合应用；2：独立应用；3：自带应用
    public final static String user_loginname = "loginname";
    public final static String user_pwd = "password";


    public AppModuleDBHelper(Context context) {
        super(context, module_db, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql_all_moudle = "create table " + ALL_MOUDLE_TABLE + "(id integer primary key autoincrement,"
                + module_Name + " TEXT,"
                + module_EN + " TEXT,"
                + module_Url + " TEXT,"
                + module_ImageUrl + " TEXT,"
                + module_type + " TEXT"+ ");";
        sqLiteDatabase.execSQL(sql_all_moudle);

        String sql_user_moudle = "create table " + USER_MOUDLE_TABLE + "(id integer primary key autoincrement,"
                + userGuid + " TEXT,"
                + module_EN + " TEXT,"
                + module_Name + " TEXT,"
                + module_Url + " TEXT,"
                + module_ImageUrl + " TEXT,"
                + module_type + " TEXT"+ ");";
        sqLiteDatabase.execSQL(sql_user_moudle);

        String sql_user_msg = "create table " + USER_MSG_TABLE + "(id integer primary key autoincrement,"
                + user_loginname + " TEXT,"
                + user_pwd + " TEXT"+ ");";
        sqLiteDatabase.execSQL(sql_user_msg);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(newVersion>oldVersion){
            UpdbData(sqLiteDatabase);
        }
    }
    private void UpdbData(SQLiteDatabase sqLiteDatabase){
        String sql_user_msg = "create table " + USER_MSG_TABLE + "(id integer primary key autoincrement,"
                + user_loginname + " TEXT,"
                + user_pwd + " TEXT"+ ");";
        sqLiteDatabase.execSQL(sql_user_msg);
    }
}
