package com.android.yawei.jhoa.utils;

public class Constants {
	//TODO  域空间
	private static final String nameSpace ="http://tempuri.org/";
	//TODO  域空间去掉空格
	public static final String NAMESPACE = nameSpace.trim();
	//TODO 获取用户信息和状态
	public static String WEB_GETUSERINFO_SERVICE = "";
	//TODO 版本检测
	public static String WEB_APPDOWNLOAD_URL = "";
	//TODO 动态取Webservice.asmx地址，办公接口,专网地址
	public static String WEB_SERVICE_URL = "";
	//TODO 重置密码
	public static String RSETPASSWORD = "";
	//TODO 版本型号（0测试版，1正式版）
	public static final String VERSIONTYPE="1";
	//TODO 正式版本号
	public static String VERSION_ID="";
	//TODO 测试版本号
	public static String VERSIONDEBUG_ID="";
	//TODO 登录方式 账号密码、人脸、指纹
	public static final String LOGINMODLE = "loginmodle";
	//TODO 下载
	public static  String PACKAGE_NAME=null;
	//TODO 用户登录账号,切换
	public static final String CUTOVER_AD_LOGNAME="cutoveruserName";
	//TODO 用户的guid,切换
	public static final String CUTOVER_AD_GUID = "cutoveradGuid";
	///TODO 用户交换号,切换
	public static final String CUTOVER_EXCHANGE_ID = "cutoverexchangeId";
	//TODO 用户昵称(名称),切换
	public static final String CUTOVER_DISPLAY_NAME="cutoverdisplayName";
	//TODO 用户的信息（部门）,切换
	public static final String CUTOVER_AD_DEPART="cutoverdepart";
	//TODO 用户的新旧系统表示,切换
	public static final String CUTOVER_AD_SYSFLAG="cutoversysflag";//0新系统1旧系统
	//根root
	//TODO 用户登录账号
	public static final String AD_LOGNAME="userName";
	//TODO 用户的guid
	public static final String AD_GUID = "adGuid";
	//TODO 用户交换号
	public static final String EXCHANGE_ID = "exchangeId";
	//TODO 用户昵称(名称)
	public static final String DISPLAY_NAME="displayName";
	//TODO 用户的信息（部门）
	public static final String AD_DEPART="depart";
	//TODO 用户的新旧系统表示，0新系统1旧系统
	public static final String AD_SYSFLAG="sysflag";
	//TODO asses资源文件是否要更新,版本号
	public static final String ASSESZIPVER ="asseszipver";
	//TODO 写邮件选人，判断是否点击确定按钮,false不是点击的确定，true点击确定
    public static boolean IsOkSelectPerson=false;
	//TODO 联系人，分组人一次获取的条数
    public static final String PAGE_SIZE="20";
	//TODO 什么品牌手机+手机的型号，比如：三星 + GT2000
  	public static String mobileName=null;
	//TODO 密码
  	public static String PASSWORD="";
	//TODO 是否有新版本
  	public static Boolean IsVerSion=false;
	//TODO 设备唯一标示
  	public static String MobileIMEI = "";
	//TODO 应用安全管控标示
	public static String APPGUID = "1001";
	//TODO 即时通讯是否初始化成功
	public static boolean ChatIsInit = true;
	//TODO 是否开启即时通讯
	public static boolean OpenChat = true;
	//TODO 是否开启百度语音
	public static boolean OpenBaiduViceo = true;
	//TODO 是否可以使用腾讯的文件打开
	public static boolean TencentOpenfile = false;
	//TODO 获取Access Token
	public static String ACCESS_TOKEN="";
}
