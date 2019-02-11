package com.android.yawei.jhoa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.yawei.jhoa.bean.AppOptionBean;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.SpUtils;
import com.yawei.jhoa.mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yusz on 2018-2-22.
 */

public class AppModuleDatabase {

    private AppModuleDBHelper dbHelper;
    private Context context;

    public AppModuleDatabase(Context context){
        this.context = context;
        this.dbHelper = new AppModuleDBHelper(context);
    }
    /**
     * 自定义应用：往总表中插入应用，先清除后插入数据
     */
    public void ClearAndInsertIntoAppModule(List<AppOptionBean> array){
        String userguid = SpUtils.getString(context, Constants.CUTOVER_AD_GUID, "");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("delete from " +AppModuleDBHelper.ALL_MOUDLE_TABLE);
            for(AppOptionBean bean:array){
                String imageURL = String.valueOf(bean.getImageId());
                if(imageURL ==null || imageURL.equals("")){
                    imageURL = bean.getImageurl();
                }
                String modulename = bean.getName();
                if(modulename==null || modulename.equals("")){
                    modulename = bean.getModulename();
                }
                db.execSQL("INSERT INTO "+AppModuleDBHelper.ALL_MOUDLE_TABLE+"("
                                +AppModuleDBHelper.module_Name+ ","
                                +AppModuleDBHelper.module_EN+ ","
                                +AppModuleDBHelper.module_Url+","
                                +AppModuleDBHelper.module_type + ","
                                + AppModuleDBHelper.module_ImageUrl
                                +")"+ "values(?,?,?,?,?)",
                        new Object[]{bean.getModulename(),bean.getEnName(),bean.getModuleurl(),bean.getModuletype(),imageURL});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }
    /**
     * 自定义应用：往总表中插入应用，不清除直接插入数据
     */
    public void InsertIntoAppModule(List<AppOptionBean> array){
        String userguid = SpUtils.getString(context, Constants.CUTOVER_AD_GUID, "");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();  //开始事务
        try {
            for(AppOptionBean bean:array){
                String imageURL =  bean.getImageurl();
                if(imageURL ==null || imageURL.equals("")){
                    imageURL = String.valueOf(bean.getImageId());
                }
                String modulename = bean.getName();
                if(modulename==null || modulename.equals("")){
                    modulename = bean.getModulename();
                }
                db.execSQL("INSERT INTO "+AppModuleDBHelper.USER_MOUDLE_TABLE+"("
                                +AppModuleDBHelper.userGuid+ ","
                                +AppModuleDBHelper.module_Name+ ","
                                +AppModuleDBHelper.module_EN+ ","
                                +AppModuleDBHelper.module_Url+","
                                +AppModuleDBHelper.module_type + ","
                                + AppModuleDBHelper.module_ImageUrl
                                +")"+ "values(?,?,?,?,?,?)",
                        new Object[]{userguid,modulename,bean.getEnName(),bean.getModuleurl(),bean.getModuletype(),imageURL});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }
    /**
     * 自定义应用：往总表中插入应用，不清除直接插入数据
     */
    public void InsertIntoAppModuleForDelete(List<AppOptionBean> array){
        String userguid = SpUtils.getString(context, Constants.CUTOVER_AD_GUID, "");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("delete from " +AppModuleDBHelper.USER_MOUDLE_TABLE);
            for(AppOptionBean bean:array){
                String imageURL =  bean.getImageurl();
                if(imageURL ==null || imageURL.equals("")){
                    imageURL = String.valueOf(bean.getImageId());
                }
                String modulename = bean.getName();
                if(modulename==null || modulename.equals("")){
                    modulename = bean.getModulename();
                }
                db.execSQL("INSERT INTO "+AppModuleDBHelper.USER_MOUDLE_TABLE+"("
                                +AppModuleDBHelper.userGuid+ ","
                                +AppModuleDBHelper.module_Name+ ","
                                +AppModuleDBHelper.module_EN+ ","
                                +AppModuleDBHelper.module_Url+","
                                +AppModuleDBHelper.module_type + ","
                                + AppModuleDBHelper.module_ImageUrl
                                +")"+ "values(?,?,?,?,?,?)",
                        new Object[]{userguid,modulename,bean.getEnName(),bean.getModuleurl(),bean.getModuletype(),imageURL});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }
    /*************
     * 从个人自定义表中获取数据
     * @return
     */
    public List<AppOptionBean> GetAppByTable()throws Exception{
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        List<AppOptionBean> appInfo = new ArrayList<AppOptionBean>();
        String userguid = SpUtils.getString(context, Constants.CUTOVER_AD_GUID, "");
        Cursor cv = db.rawQuery("SELECT * FROM "+AppModuleDBHelper.USER_MOUDLE_TABLE+" WHERE "+AppModuleDBHelper.userGuid + " = ?" , new String[]{userguid});
        while(cv.moveToNext()){
            AppOptionBean info = new AppOptionBean();
            String imagePath =cv.getString(cv.getColumnIndex(AppModuleDBHelper.module_ImageUrl));
            if(imagePath.contains("http://")){
                info.setImageurl(imagePath);
            }else{
                info.setImageId(Integer.valueOf(imagePath));
            }
            info.setName(cv.getString(cv.getColumnIndex(AppModuleDBHelper.module_Name)));
            info.setModulename(cv.getString(cv.getColumnIndex(AppModuleDBHelper.module_Name)));
            info.setModuletype(cv.getString(cv.getColumnIndex(AppModuleDBHelper.module_type)));
            info.setModuleurl(cv.getString(cv.getColumnIndex(AppModuleDBHelper.module_Url)));
            info.setEnName(cv.getString(cv.getColumnIndex(AppModuleDBHelper.module_EN)));
            appInfo.add(info);
        }
        cv.close();
//        if(appInfo.size()>1){
            for(AppOptionBean bean: appInfo){
                if(bean.getEnName() != null && bean.getEnName().equals("more")){
                    appInfo.remove(bean);
                    appInfo.add(bean);
                    break;
                }
            }
//        }
        for(AppOptionBean bean: appInfo){
            if(bean.getEnName() != null && bean.getEnName().equals("daiban")){
                //待办
                bean.setImageId(R.drawable.ic_daiban);
            }else if(bean.getEnName() != null && bean.getEnName().equals("yiban")){
                //已办
                bean.setImageId(R.drawable.ic_yiban);
            }else if(bean.getEnName() != null && bean.getEnName().equals("caogao")){
                //草稿
                bean.setImageId(R.drawable.ic_caogao);
            }else if(bean.getEnName() != null && bean.getEnName().equals("yifa")){
                //已发
                bean.setImageId(R.drawable.ic_yifa);
            }else if(bean.getEnName() != null && bean.getEnName().equals("important")){
                //急要件
                bean.setImageId(R.drawable.ic_important_email);
            }else if(bean.getEnName() != null && bean.getEnName().equals("contact")){
                //通讯录
                bean.setImageId(R.drawable.ic_tongxunlu);
            }else if(bean.getEnName() != null && bean.getEnName().equals("chart")){
                //聊天
                bean.setImageId(R.drawable.ic_goutong);
            }else if(bean.getEnName() != null && bean.getEnName().equals("note")){
                //记事本
                bean.setImageId(R.drawable.ic_jishiben);
            }else if(bean.getEnName() != null && bean.getEnName().equals("self")){
                //我的
                bean.setImageId(R.drawable.ic_wode);
            }else if(bean.getEnName() != null && bean.getEnName().equals("more")){
                //更多
                bean.setImageId(R.drawable.ic_moremoudle);
            }
        }
        return appInfo;
    }

    /*****
     * 根据名称判断table中是否有
     * @param tableName
     * @param moudleName
     * @return
     * @throws Exception
     */
    public boolean GetAppByTableIsReadly(String tableName,String moudleName)throws Exception{
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        List<AppOptionBean> appInfo = new ArrayList<AppOptionBean>();
        String userguid = SpUtils.getString(context, Constants.CUTOVER_AD_GUID, "");
        Cursor cv = db.rawQuery("SELECT * FROM "+tableName+" WHERE "+AppModuleDBHelper.userGuid+" = ?" +" AND " + AppModuleDBHelper.module_Name + " = ? ",
                new String[]{userguid,moudleName});
        while(cv.moveToNext()){
            cv.close();
            return  true;
        }
        cv.close();
        return false;
    }

    /***
     * 根据名称删除表中对应数据
     * @param tableName
     * @param moudleName
     * @throws Exception
     */
    public void DelAppMoudleToTable(String tableName,String moudleName)throws Exception{
        String userguid = SpUtils.getString(context, Constants.CUTOVER_AD_GUID, "");
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        db.execSQL("delete from "+tableName+" where "+AppModuleDBHelper.userGuid+" = ?" +" AND " + AppModuleDBHelper.module_Name + " = ? ",
                new Object[] {userguid,moudleName});
    }
    public void InsertDataToUserappTable(AppOptionBean bean){
        String userguid = SpUtils.getString(context, Constants.CUTOVER_AD_GUID, "");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();  //开始事务
        try {
            String imageURL =  bean.getImageurl();
            if(imageURL ==null || imageURL.equals("")){
                imageURL = String.valueOf(bean.getImageId());
            }
            String modulename = bean.getName();
            if(modulename==null || modulename.equals("")){
                modulename = bean.getModulename();
            }
            db.execSQL("INSERT INTO "+AppModuleDBHelper.USER_MOUDLE_TABLE+"("
                            +AppModuleDBHelper.userGuid+ ","
                            +AppModuleDBHelper.module_Name+ ","
                            +AppModuleDBHelper.module_EN+ ","
                            +AppModuleDBHelper.module_Url+","
                            +AppModuleDBHelper.module_type + ","
                            + AppModuleDBHelper.module_ImageUrl
                            +")"+ "values(?,?,?,?,?,?)",
                    new Object[]{userguid,modulename,bean.getEnName(),bean.getModuleurl(),bean.getModuletype(),imageURL});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * 指纹识别，注册绑定用户
     * @param loginname
     * @param pwd
     */
    public void InsertUserMsgToTable(String loginname,String pwd){
        int amount=0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cv = db.rawQuery("select * from "+ AppModuleDBHelper.USER_MSG_TABLE, null);
        amount=cv.getCount();
        if(amount==0){
            try {
                db.beginTransaction();  //开始事务
                db.execSQL("INSERT INTO "+AppModuleDBHelper.USER_MSG_TABLE+"("
                                +AppModuleDBHelper.user_loginname+ ","
                                +AppModuleDBHelper.user_pwd
                                +")"+ "values(?,?)",
                        new Object[]{loginname,pwd});
                db.setTransactionSuccessful();  //设置事务成功完成
            } finally {
                db.endTransaction();    //结束事务
            }
        }else {
            while(cv.moveToNext()){
                String id = cv.getString(cv.getColumnIndex("id"));
                ContentValues cnt = new ContentValues();
                cnt.put(AppModuleDBHelper.user_loginname, loginname);
                cnt.put(AppModuleDBHelper.user_pwd, pwd);
                db.update(AppModuleDBHelper.USER_MSG_TABLE, cnt, "id = ?", new String[]{id});
            }
        }
        cv.close();
    }

    /**
     * 指纹登录获取登录信息
     * @return
     */
    public String GetFingerUser(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        int amount=0;
        Cursor cv = db.rawQuery("select * from "+ AppModuleDBHelper.USER_MSG_TABLE, null);
        amount=cv.getCount();
        String usermsg = "";
        if(amount!=0){
            while(cv.moveToNext()){
                String loginName =  cv.getString(cv.getColumnIndex(AppModuleDBHelper.user_loginname));
                String pwd = cv.getString(cv.getColumnIndex(AppModuleDBHelper.user_pwd));
                usermsg = loginName+";"+pwd;
            }
        }
        return usermsg;
    }
}
