package com.android.yawei.jhoa.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.android.yawei.jhoa.bean.ClientVersionInfo;
import com.android.yawei.jhoa.bean.DataSet;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.parser.VersionParse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * 访问服务器端，判断是否是最新版本
 */
public class UpdateThread extends Thread {

	public static List<ClientVersionInfo> versionInfo;
	public static final int ISNEW = 100;//当前安装的APP是最新版本
	public static final int ERROR = 101;//版本检测失败提示
	public static final int RESOURCES = 102;//资源更新handler接收提示
	public static final int APPUPVERSION = 103;//有最新版本，APP更新提示handler接收

	private Handler handler=null;
	private int SDGXType;
	private Context context;

	public UpdateThread(Handler hdl,Context context,int SDGXType) {
		this.handler = hdl;
		this.SDGXType=SDGXType;
		this.context=context;
	}
	@Override
	public void run() {
		String str = WebServiceNetworkAccess.getVersion(Constants.APPGUID);
		Log.d("version_app",str);
		if(str == null || str.equals("") || "anyType".equals(str)){
			//版本检测失败
			handler.sendEmptyMessage(ERROR);
		}else{
			versionInfo = parseVersion(str);
			if(SDGXType==-1){
				//自动更新
				if(Constants.VERSIONTYPE.equals("1")){
					String resver = SpUtils.getString(context,Constants.ASSESZIPVER,"");
					//自动更新正式版
					 if(!Constants.VERSION_ID.equals(versionInfo.get(0).getVersion())){
						 handler.sendEmptyMessage(APPUPVERSION);  //是否下载最新版本
	   		         }else if(!TextUtils.isEmpty(versionInfo.get(0).getWwwversion())&& (!versionInfo.get(0).getWwwversion().equals(resver)||!new File(FileUtils.GetSystemFilePath()+"html/").exists())){
						 String carurl = versionInfo.get(0).getWwwurl();
						 if(carurl != null && !carurl.equals("") ){
							 carurl = carurl.substring(carurl.lastIndexOf("/")+1,carurl.length());
							 if(carurl != null && !carurl.equals("")){
								 handler.sendEmptyMessage(RESOURCES);
							 }
						 }
					 }else{
						 handler.sendEmptyMessage(ISNEW);
					 }
	   		         if(SpUtils.getString(context, Constants.EXCHANGE_ID, "").equals("00010000000000000058")){
	   			         if(!Constants.VERSIONDEBUG_ID.equals(versionInfo.get(0).getDebugversion())){ 
	   			        	 Constants.IsVerSion=true;
	   			         }
	   		         }
				}else {
					String resver = SpUtils.getString(context,Constants.ASSESZIPVER,"");
					if(!Constants.VERSION_ID.equals(versionInfo.get(0).getVersion())){
						 handler.sendEmptyMessage(APPUPVERSION);  //是否下载最新版本
	   		        }else if(TextUtils.isEmpty(versionInfo.get(0).getWwwversion())&& (!versionInfo.get(0).getWwwversion().equals(resver)||!new File(FileUtils.GetSystemFilePath()+"html/").exists())){
						String carurl = versionInfo.get(0).getWwwurl();
						if(carurl != null && !carurl.equals("") ){
							carurl = carurl.substring(carurl.lastIndexOf("/")+1,carurl.length());
							if(carurl != null && !carurl.equals("")){
									handler.sendEmptyMessage(RESOURCES);
							}
						}
					}else{
						handler.sendEmptyMessage(ISNEW);
					}
					if(SpUtils.getString(context, Constants.EXCHANGE_ID, "").equals("00010000000000000058")){
	   			         if(!Constants.VERSIONDEBUG_ID.equals(versionInfo.get(0).getDebugversion())){ 
	   			        	 Constants.IsVerSion=true;
	   			         }
	   		         }
				}
			}else {
				switch (SDGXType) {
				case 0://还原
					handler.sendEmptyMessage(12);  //是否下载最新版本
					break;
                case 1://正式版
                	if((Constants.VERSION_ID).equals(versionInfo.get(0).getVersion())){
						//已经是最新版本
						handler.sendEmptyMessage(11);
					}else{
						handler.sendEmptyMessage(APPUPVERSION);  //是否下载最新版本
			   		}
					break;
                case 2://测试版
                	if((Constants.VERSIONDEBUG_ID).equals(versionInfo.get(0).getDebugversion())){
						//已经是最新版本
						handler.sendEmptyMessage(11);
					}else {
						handler.sendEmptyMessage(APPUPVERSION);  //是否下载最新版本
			   		}
					break;
				default:
					break;
				}
			}
		}
		super.run();
	}
	//解析版本信息
	public static List<ClientVersionInfo> parseVersion(String xml){
		if (xml == null){
			throw new NullPointerException();
		}
		List<ClientVersionInfo> dataSet = new ArrayList<>();
		VersionParse bp = new VersionParse(dataSet);
		try{
			XmlUtils.saxParse(xml, bp);
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}
}
