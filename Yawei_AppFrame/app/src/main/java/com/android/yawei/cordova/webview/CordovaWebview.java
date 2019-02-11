package com.android.yawei.cordova.webview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.yawei.jhoa.Interface.UploadBigFileCallback;
import com.android.yawei.jhoa.bean.Attachment;
import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.mobile.SDFileListActivity;
import com.android.yawei.jhoa.ui.CustomProgressDialog;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.FileUtils;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.utils.SysExitUtil;
import com.android.yawei.jhoa.utils.VerifyPermissions;
import com.yawei.cordova.plugins.ionic.MyPlugin;
import com.yawei.jhoa.mobile.R;
import com.yawei.zxing.client.QRCodeIntent;

import org.apache.cordova.CordovaActivity;
import org.kobjects.base64.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class CordovaWebview extends CordovaActivity implements UploadBigFileCallback {

	public static boolean isSelFile = false;
	private MyApplication application;
	private ByteArrayOutputStream baos;//文件输出流
	private WebView webview;
	private CustomProgressDialog progressDialog;
	public String oaUrl;
	public String parameter;
	private String corUrl;
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        try {
        	application = (MyApplication) getApplication();
            oaUrl =CordovaWebview.this.getIntent().getStringExtra("murl");
            corUrl = getIntent().getStringExtra("loadurl");//加载地址
			parameter = getIntent().getStringExtra("parameter");
            if(corUrl != null && !corUrl.equals("")){
            	loadUrl(corUrl);
            }else{
            	loadUrl(launchUrl);
            }
            webview = (WebView) getView();
            webview.addJavascriptInterface(new JavaScriptObj(), "stub");
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }

	//JavaScript 调用webview中方法
	class JavaScriptObj {
		//添加该属性解决无法回调的问题
		 @JavascriptInterface
	     public void GoBack() {
			 try {
				 finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
	     }
		 //返回用户信息
		 @JavascriptInterface
		 public String  TransferUserValue(){
			 
			 String userDisplayName = SpUtils.getString(CordovaWebview.this, Constants.CUTOVER_DISPLAY_NAME, "");
			 String userGuid = SpUtils.getString(CordovaWebview.this, Constants.CUTOVER_AD_GUID, "");
			 
			 return userDisplayName+"|"+userGuid+"|"+ Constants.MobileIMEI;
		 }
		 //返回办公截取地址
		 @JavascriptInterface
		 public String  GetMobileLoadUrl(){
			 return CordovaWebview.this.getIntent().getStringExtra("murl");
//			 String url = "http://35.1.11.15/jhoamobile/";
//			 return url;
		 }
		 //选取附件
		 @JavascriptInterface
		 public void SeleceDeviceFile(){
			 handler.sendEmptyMessage(4);
		 }
		 //下载附件
		 @JavascriptInterface
		 public void DownLoadFile(String fileUrl){
			 Message msg = new Message();
			 msg.what= 1;
			 msg.obj = fileUrl;
			 handler.sendMessage(msg);
		 }
		 //上传文件数据流
		 @JavascriptInterface
		 public String onLoadFile(String filePath){
			 String uploadBuffer = "";
			try {
				FileInputStream fis = new FileInputStream(filePath);
				 baos = new ByteArrayOutputStream();  
				 byte[] buffer = new byte[1024]; 
				 int count = 0;
				 while((count = fis.read(buffer)) >= 0){  
	                baos.write(buffer, 0, count);
		         }
				 fis.close();
				 uploadBuffer = new String(Base64.encode(baos.toByteArray()));  //进行Base64编码
				 if(baos != null){
//					baos.close();
					baos.reset();
//					baos = null;
				 }
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return "";
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
			return uploadBuffer;
		 }
	}
	public void ScanQRCode(){
		VerifyPermissions.RequestPermissionZxing(CordovaWebview.this,10);
	}
	@Override
	public void BigFileUpSucceed(String message, String fileGuid, String sign) {
		if(MyPlugin.mcallbackContext != null) {
			MyPlugin.mcallbackContext.success(message);
		}
	}

	@Override
	public void BigFileUpError(String message, String sign) {
		if(MyPlugin.mcallbackContext != null) {
			MyPlugin.mcallbackContext.error(message);
		}
	}
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case 2:
					if(progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
					if(progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
					Toast.makeText(CordovaWebview.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
					break;
				case 3:					 
					break;
				case 4:
					//选择附件
					if(!isSelFile){//出现点击多次问题
						isSelFile = true;
						Intent intent = new Intent(CordovaWebview.this, SDFileListActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
					}
					break;
				case 5:
					FileUtils.openFile(CordovaWebview.this,new File((String)msg.obj));
					break;
				case 6:
					//选择附件
					Bundle bundle2 = msg.getData();
					webview.loadUrl("javascript:addFilesInfo('"+bundle2.getString("fileName")+"|"+bundle2.getString("suffix")+"|"+bundle2.getString("length")+"|"+bundle2.getString("filePath")+"')");
					application.removeAllAttachment(); //清除附件缓存
					break;
				case 7:
					//提示错误
					Toast.makeText(CordovaWebview.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.handleMessage(msg);
		}
	};
	
	protected void onRestart(){
		super.onRestart();
		try {
			if(Constants.IsOkSelectPerson ){
				Constants.IsOkSelectPerson = false;
				if(application.getSelectedUsers() != null&& application.getSelectedUsers().size()>0){
					List<User> useArr = new ArrayList<>();
					useArr.addAll(application.getSelectedUsers());
					application.removeAllUser();
					String userjson = JSONObject.toJSONString(useArr);
					MyPlugin.mcallbackContext.success(userjson);
					return;
				}
			}
		} catch (Exception e) {
			MyPlugin.mcallbackContext.success("选人失败!");
			e.printStackTrace();
		}
		if(isSelFile){
			isSelFile = false;
			if(application.getAttachmentList() != null && application.getAttachmentList().size()>0){
				if(MyPlugin.mcallbackContext != null){
					try {
						List<Attachment> attArr = new ArrayList<>();
						attArr.addAll(application.getAttachmentList());
						String filejson = JSON.toJSONString(attArr);
						MyPlugin.mcallbackContext.success(filejson);
						application.removeAllAttachment();
					} catch (Exception e) {
						MyPlugin.mcallbackContext.error("选附件失败！");
						e.printStackTrace();
					}
					return;
				}
				//查询缓存中是否含有附件
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							if(application.getAttachmentList() != null && application.getAttachmentList().size()>0){
								List<Attachment> attachmentList = application.getAttachmentList();
								String filePath = attachmentList.get(attachmentList.size()-1).getAttUrl();
								String fileName = new File(filePath).getName();
								
								if(new File(filePath).length() >3*1024*1024){
									Message msg = new Message();
									msg.what = 7;
									msg.obj = "文件过大，上传文件不能超过3M";
									handler.sendMessage(msg);
									return ;
								}
									
								Message msg = new Message();
								Bundle bundle = new Bundle();
								bundle.putString("fileName", fileName);
								bundle.putString("suffix", fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
								bundle.putString("length", String.valueOf(new File(filePath).length()));
								bundle.putString("filePath", filePath);
								msg.setData(bundle);
								msg.what = 6;
								handler.sendMessage(msg);
							}
						} catch (Exception e) {
							Message msg = new Message();
							msg.what = 7;
							msg.obj = "文件上传失败，可能文件过大！";
							handler.sendMessage(msg);
							e.printStackTrace();
						}
					}
					
				}).start();
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		try {
			switch(resultCode){
				case 1000:
					//手写签批
					Bundle b = data.getExtras();
					String filePath = b.getString("filePath");
					MyPlugin.mcallbackContext.success(filePath);
					break;
				default:
					if(requestCode == 10){
						if (data != null && !TextUtils.isEmpty(data.getStringExtra(QRCodeIntent.SCAN_RESULT))) {
							String msg = data.getStringExtra(QRCodeIntent.SCAN_RESULT);
							MyPlugin.mcallbackContext.success(msg);
						}
					}
					break;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
