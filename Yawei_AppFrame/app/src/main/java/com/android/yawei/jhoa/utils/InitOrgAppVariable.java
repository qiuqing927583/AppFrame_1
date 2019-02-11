package com.android.yawei.jhoa.utils;

/******
 * 动态设置地址
 * @author Yusz
 *
 */
public class InitOrgAppVariable {

	public InitOrgAppVariable(){
		/**版本检测****/
		//Constants.WEB_APPDOWNLOAD_URL = "http://49.4.1.190/appDown/WebService/MobileInfoService.asmx";
		Constants.WEB_APPDOWNLOAD_URL = "http://172.18.1.32/appDown/WebService/MobileInfoService.asmx";
		/*********动态取Webservice.asmx地址,***/
		Constants.WEB_SERVICE_URL = "";
		/**获取用户信息和状态**/
		//Constants.WEB_GETUSERINFO_SERVICE = "http://49.4.1.190/mamsservice/webservice/datainfoservice.asmx";
		Constants.WEB_GETUSERINFO_SERVICE = "http://172.18.1.32/mamsservice/webservice/datainfoservice.asmx";
	}
}
