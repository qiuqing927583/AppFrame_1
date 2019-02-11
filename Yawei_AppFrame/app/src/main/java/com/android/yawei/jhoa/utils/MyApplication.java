package com.android.yawei.jhoa.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.android.yawei.jhoa.bean.A_DBJ;
import com.android.yawei.jhoa.bean.Attachment;
import com.android.yawei.jhoa.bean.DevicesInfoBean;
import com.android.yawei.jhoa.bean.InboxDetail;
import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.db.AppModuleDatabase;
import com.tencent.smtt.sdk.QbSdk;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/*********************
 *
 * @author Yusz
 * 功能：全局
 *
 */
public class MyApplication extends MultiDexApplication {

	private List<Attachment> attachmentList;//缓存已选的附件信息
	private ArrayList<User> selectedUsers; //已选收件人集合的缓存
	private InboxDetail inboxDetail;//页面传值,便笺详情
	private A_DBJ a_dbj;
	private AppModuleDatabase dbHelper;
	private DevicesInfoBean deveInfo;

	public void onCreate() {
		super.onCreate();
		//初始化
		try {
			attachmentList = new ArrayList<Attachment>();
			selectedUsers = new ArrayList<User>();
			Constants.VERSION_ID = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			Constants.mobileName = android.os.Build.BRAND + "  " + android.os.Build.MODEL;
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
				Constants.MobileIMEI = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
			}
			new InitOrgAppVariable();
//       	CatchHandler.getInstance().init(this);
			dbHelper = new AppModuleDatabase(getApplicationContext());
			deveInfo = new DevicesInfoBean();
			//初始化腾讯内核
			InitTencent();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	//初始化腾讯内核
	private void InitTencent()throws Exception{
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

			@Override
			public void onViewInitFinished(boolean arg0) {
				//x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
				if(arg0){
					Constants.TencentOpenfile = true;
				}
			}
			@Override
			public void onCoreInitFinished() {
			}
		};
		//x5内核初始化接口
		QbSdk.initX5Environment(getApplicationContext(),  cb);
	}

	/***
	 * 获取自定义数据库对操作对象
	 * @return
	 * @throws Exception
	 */
	public AppModuleDatabase GetModuleSQLHepler()throws Exception{
		if(dbHelper == null)
			dbHelper = new AppModuleDatabase(getApplicationContext());
		return dbHelper;
	}
	/**************
	 * 根据服务名判断服务是否在运行
	 * @param serviceClassName  服务名称
	 * @return
	 */
	private Boolean getStartService(String serviceClassName){
		Boolean retBoolean=false;
		ActivityManager  activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);  
		List<ActivityManager.RunningServiceInfo>serviceList= activityManager.getRunningServices(30);
		  for(int i = 0; i < serviceList.size(); i ++){ 
              if(serviceClassName.equals(serviceList.get(i).service.getClassName())){ 
            	  retBoolean=true;
            	  return retBoolean; 
             } 
		  }
	   return retBoolean;
	 }
	@Override  
    protected void attachBaseContext(Context base) {  
        super.attachBaseContext(base);  
        MultiDex.install(this);  
    } 
	/************************
	 * 添加附件
	 ************************/
	public void addAttachment(Attachment attachment)throws Exception{
		attachmentList.add(attachment);
	}
	/*************************
	 * 删除指定附件
	 ************************/
	public void removeAttachment(int position){
		attachmentList.remove(position);
	}
	/**************************
	 * 清空所有附件缓存
	 *************************/
	public void removeAllAttachment()throws Exception{
		attachmentList.clear();
	}
	/***************************
	 * 获取附件列表
	 **************************/
	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}
	/****************************
	 * 删除已选收件人的缓存
	 * @param user
	 ****************************/
	public boolean removeUser(User user)throws Exception{
		for (int i = 0; i < selectedUsers.size(); i++) {
			if(user.getAdGuid().equals(selectedUsers.get(i).getAdGuid())){
				selectedUsers.remove(i);
				return true;
			}
		}
		return false;
	}
	/**
	 * 已选收件人缓存中是否包含该收件人
	 * @param user
	 */
	public boolean isContainsUser(User user)throws Exception{
		for (User u : selectedUsers) {
			if(user.getAdGuid().equals(u.getAdGuid())){
				return true;
			}
		}
		return false;
	}
	/**
	 * 增加已选收件人的缓存
	 * @param user
	 */
	public void addUser(User user)throws Exception{
		selectedUsers.add(user);
	}
	/**************
	 * 清除选中发送的人
	 */
	public void removeAllUser()throws Exception{
		selectedUsers.clear();
	}
	/**
	 * 获取全部的已选收件人的缓存
	 * @return
	 */
	public ArrayList<User> getSelectedUsers()throws Exception {
		return selectedUsers;
	}
	/***************
	 * 删除指定的发送人
	 *  postion
	 */
	public void RemoveSelectedUsers(int index)throws Exception{
		if(selectedUsers != null){
			selectedUsers.remove(index);
		}
	}
	public void setInboxDetail(InboxDetail inboxDetail)throws Exception {
		this.inboxDetail = inboxDetail;
	}
	public void setADBJDetail(A_DBJ ADBJDetail)throws Exception {
		this.a_dbj = ADBJDetail;
	}
	public InboxDetail getInboxDetail()throws Exception {
		return inboxDetail;
	}
	/*********
	 * 待办信息
	 * @return
	 */
	public A_DBJ GetADBJDetail()throws Exception {
		return a_dbj;
	}

	/******
	 * 获取e本sn号
	 * 
	 */
	public String getEbEnSnm()throws Exception{
		String PSerialNo="";
		String DeviceModel=getAndroidOsSystemProperties("ro.product.model");
		if (DeviceModel.equals("EBEN T6")) {
			PSerialNo = getAndroidOsSystemProperties("ro.product.serialno");
		}else{
			PSerialNo = getAndroidOsSystemProperties("gsm.scril.sn");
		}
		return PSerialNo;
	}
	public String getAndroidOsSystemProperties(String key)throws Exception {
		Method systemProperties_get = null;
		String str;
		try {
			systemProperties_get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
		    if ((str = (String) systemProperties_get.invoke(null, key)) != null)
		    	return str;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
		return "";
	}

	public DevicesInfoBean getDeveInfo() {
		return deveInfo;
	}

	public void setDeveInfo(DevicesInfoBean deveInfo) {
		this.deveInfo = deveInfo;
	}
}
