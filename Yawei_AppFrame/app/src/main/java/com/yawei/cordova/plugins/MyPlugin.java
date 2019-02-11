package com.yawei.cordova.plugins;

import android.content.Intent;
import android.os.Message;

import com.android.yawei.cordova.webview.CordovaWebview;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.SpUtils;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyPlugin extends CordovaPlugin {
	
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
	}

	public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
            throws JSONException {
		try {
			CordovaWebview activity = (CordovaWebview) this.cordova.getActivity();
	        JSONObject obj =  args.getJSONObject(0);
	        String method = obj.getString("method");
	        if (method.equals("back")){
	        	callbackContext.success("");//成功给的信息
	 	    	callbackContext.error("");//失败
	        	activity.finish();
	            return true;
	        }else if(method.equals("usermsg")){
	        	String userDisplayName = SpUtils.getString(activity, Constants.CUTOVER_DISPLAY_NAME, "");
				String userGuid = SpUtils.getString(activity, Constants.CUTOVER_AD_GUID, "");
	        	callbackContext.success(userDisplayName+"|"+userGuid+"|"+ Constants.MobileIMEI+"|"+SpUtils.getString(activity, Constants.DISPLAY_NAME, ""));//成功给的信息
				callbackContext.error("获取用户信息失败");//失败
	        	return true;
	        }else if(method.equals("oaurl")){
	        	String url = activity.oaUrl;
	        	callbackContext.success(url);//成功给的信息
	 	    	callbackContext.error("获取办公地址失败");//失败
	        	return true;
	        }else if(method.equals("mobilescreen")){
	        	callbackContext.success("3");//成功给的信息
	 	    	callbackContext.error("获取栏目条数失败");//失败
	        	return true;
	        }else if(method.equals("openattach")){
	        	//下载附件
	        	 Message msg = new Message();
				 msg.what= 1;
				 msg.obj = obj.getString("url");
				 activity.handler.sendMessage(msg);
	        	return true;
	        }else if(method.equals("userguid")){
	        	String userGuid = SpUtils.getString(activity, Constants.CUTOVER_AD_GUID, "");
	        	callbackContext.success(userGuid);//成功给的信息
	 	    	callbackContext.error("获取用户信息失败");//失败
	        	return true;
	        }else if(method.equals("modifypasswordurl")){
//	        	callbackContext.success(Constants.MODIFYPASSWORD);//成功给的信息
	 	    	callbackContext.error("");//失败
	 	    	return true;
	        }else if(method.equals("resetpasswordurl")){
	        	callbackContext.success(Constants.RSETPASSWORD);//成功给的信息
	 	    	callbackContext.error("");//失败
	 	    	return true;
	        }
	        return false;
		} catch (Exception e) {
			return false;
		}
    }
}
