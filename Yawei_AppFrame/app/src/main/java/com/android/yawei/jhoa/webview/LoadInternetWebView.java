package com.android.yawei.jhoa.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.bean.Attachment;
import com.android.yawei.jhoa.bean.DynamicButBean;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.mobile.SDFileListActivity;
import com.android.yawei.jhoa.parser.DynamicButParser;
import com.android.yawei.jhoa.ui.CustomProgressDialog;
import com.android.yawei.jhoa.ui.MyWebChromeClient;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.FileUtils;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.utils.SysExitUtil;
import com.android.yawei.jhoa.utils.XmlUtils;
import com.android.yawei.jhoa.webservice.WebService.Callback;
import com.lidroid.xutils.BitmapUtils;
import com.yawei.android.appframework.utils.DensityUtil;
import com.yawei.jhoa.mobile.R;

import org.kobjects.base64.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/***********************
 * 
 * @author Yusz
 * 创建时间：2015-6-29
 * 功能：轻应用加载页面
 *
 */
public class LoadInternetWebView extends BaseActivity implements OnClickListener{
	
	private WebView webview;
	private String loadUrl;//链接url
	private LinearLayout back;
	private String title;//标题名称
	private CustomProgressDialog progressDialog; // 进度条
	private boolean isloading = false;
	private TextView text_title;//标题text
	private LinearLayout linExit;//首页
	private List<DynamicButBean> listButName;//存储button名称
	private LinearLayout pubilcBut;//动态创建button的父
	private boolean htmCanTransfer = false;//是否调用html返回，如果是false调用Android  webview返回
	private boolean isSelFile = false;
	private ByteArrayOutputStream baos;//文件输出流
	private MyApplication application;
	public static LoadInternetWebView rootActivity;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loadinternetwebview);
		
		SysExitUtil.AddActivity(LoadInternetWebView.this);
		
		try {
			Intent intent = getIntent();
			loadUrl = intent.getStringExtra("loadurl");
			title = intent.getStringExtra("title");
			listButName = new ArrayList<DynamicButBean>();
			application = (MyApplication) getApplication();
			rootActivity = LoadInternetWebView.this;
			
			InitView();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("static-access")
	private void InitView()throws Exception{
		
		back = (LinearLayout)findViewById(R.id.LinTopBack);
		back.setOnClickListener(this);
		text_title = (TextView)findViewById(R.id.webTitle);
		text_title.setText(title);
		pubilcBut = (LinearLayout)findViewById(R.id.pubilcBut);
		
		linExit = (LinearLayout)findViewById(R.id.LinHome);
		linExit.setOnClickListener(this);
		progressDialog = CustomProgressDialog.createDialog(LoadInternetWebView.this);
		progressDialog.setMessage("正在加载中,请稍后...");
		progressDialog.setCancelable(true);
		
		//实例化WebView对象  
		webview = (WebView)findViewById(R.id.loadwebview);
		webview.setWebViewClient(new WebViewClient(){
			@Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
				
				try {
					final String downloadUrl = URLDecoder.decode(url, "UTF-8");
					String subUrl = downloadUrl.substring(downloadUrl.lastIndexOf("/")+1, downloadUrl.length());
					String urlFilePath = downloadUrl.substring(0, downloadUrl.lastIndexOf("/")-1);
					final String filePath = urlFilePath.substring(urlFilePath.lastIndexOf("/")+1, urlFilePath.length());
					
					if(subUrl.toLowerCase().contains(".doc")||subUrl.toLowerCase().contains(".xls")||subUrl.toLowerCase().contains(".txt")||subUrl.toLowerCase().contains(".pdf")||subUrl.toLowerCase().contains(".png")
							||subUrl.toLowerCase().contains(".jpg")||subUrl.toLowerCase().contains(".jpeg")||subUrl.toLowerCase().contains(".bmp")||subUrl.toLowerCase().contains(".zip")||subUrl.toLowerCase().contains(".rar")
							||subUrl.toLowerCase().contains(".ppt")){
						//判断是否有sd卡
						if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
							progressDialog.setMessage("正在下载中,请稍后...");
							progressDialog.setCancelable(true);
							progressDialog.setCanceledOnTouchOutside(false);
							progressDialog.show();
							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										SysExitUtil.createPath(FileUtils.GetSDPath()+"jhoaMobile/temp/"+filePath);
										String pathPath = FileUtils.DownloadFile(downloadUrl, FileUtils.GetSDPath()+"jhoaMobile/temp/"+filePath+"/",false);
										if(!TextUtils.isEmpty(pathPath)&&!"0".equals(pathPath)){
											Message msg = new Message();
											msg.what = 7;
											msg.obj = pathPath;
											handler.sendMessage(msg);
										}else{
											Message msg = new Message();
											msg.what = 2;
											msg.obj = "下载失败";
											handler.sendMessage(msg);
										}
									}catch (Exception e){
										Message msg = new Message();
										msg.what = 2;
										msg.obj = "下载失败";
										handler.sendMessage(msg);
									}
								}
							}).start();
							
						}else{
							Toast.makeText(LoadInternetWebView.this,"请插入SD卡",Toast.LENGTH_SHORT).show();
						}
						return true;
					}
					
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				webview.loadUrl(url);
                return true;
            }
			
			@Override  
		    public void onPageFinished(WebView view, String url) {  
				 super.onPageFinished(view, url);
				 webview.loadUrl("javascript:InitIndexForAndroid()");
				 if(progressDialog != null && progressDialog.isShowing())
					 progressDialog.dismiss();
				 
		    }
		    @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
               super.onPageStarted(view, url, favicon);
               if(!isloading){
            	   progressDialog.show();
            	   isloading = true;
               }
           }
		});
        //设置WebView属性，能够执行Javascript脚本  
        webview.getSettings().setJavaScriptEnabled(true);  
        //支持缩放
        webview.getSettings().setSupportZoom(false);
        webview.getSettings().setDisplayZoomControls(false);
       //解决缓存问题 (不使用缓存，只从网络获取数据.)
        webview.getSettings().setCacheMode(webview.getSettings().LOAD_NO_CACHE);
        //设置总动适应屏幕宽度
        webview.getSettings().setUseWideViewPort(true);
//        webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);//设置总动适应屏幕宽度
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setBuiltInZoomControls(true);//设置是否出现缩放工具
        webview.setSaveEnabled(false);//回收webview的时候不保存状态,如果设置为true时回收的时候会保存状态，如果再次启动的时候会带入所保存的状态，当调用js桥中的方法时，会查找原来的js桥对象
        //加载需要显示的网页  
        webview.loadUrl(loadUrl);
//        webview.setWebChromeClient(new WebChromeClient());//处理解析，渲染网页等浏览器做的事情，系统默认
        webview.setWebChromeClient(new MyWebChromeClient());//处理解析，渲染网页等浏览器做的事情，自己封装
      //初始化JavaScript调用webview中方法 
        webview.addJavascriptInterface(new JavaScriptObj(), "stub"); 
        
        final ImageView butimg = new ImageView(LoadInternetWebView.this);
		butimg.setLayoutParams(new LinearLayout.LayoutParams(DensityUtil.dip2px(LoadInternetWebView.this, 20),
				DensityUtil.dip2px(LoadInternetWebView.this, getResources().getDimension(R.dimen.loaddynamicbutwidth)), 0f));
		//加载网络图标
		butimg.setBackgroundResource(R.drawable.shouye);
		linExit.addView(butimg);
		
		final TextView butText = new TextView(LoadInternetWebView.this);
		butText.setTextColor(Color.WHITE);
		butText.setTextSize(14);
		butText.setPadding(0, DensityUtil.px2dip(LoadInternetWebView.this, 8), 0,0);
		butText.setText("首页");
		butText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0f));
		linExit.addView(butText);
	}
	//JavaScript 调用webview中方法
	class JavaScriptObj {
		//添加该属性解决无法回调的问题
		 @JavascriptInterface
	     public void GoBack() {
	    	  finish();
	     }
		 //传回标题创建的button xml
		 @JavascriptInterface
		 public void OnLoadButton(String butXML){
			 try {
				 if(butXML != null && !butXML.equals("")){
					 listButName = ParserXML(butXML);
					 handler.sendEmptyMessage(1);
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
			 
		 }
		 //htm调用该方法通知，webview，返回方式是调用htm
		 @JavascriptInterface
		 public void HtmlCanTransfer(){
			 htmCanTransfer = true;
		 }
		 //网页需要当前登录人的用户名称，和用户标识
		 @JavascriptInterface
		 public void TransferUserValue(){
			handler.sendEmptyMessage(3);
		 }
		 //选取附件
		 @JavascriptInterface
		 public void SeleceDeviceFile(){
			 isSelFile = true;
			 handler.sendEmptyMessage(4);
		 }
		 //流转
		 @JavascriptInterface
		 public void onClickProcess(String docType,String flowCenterGuid,String docGuid){
			 try {
				 Message msg = new Message();
				 msg.what = 5;
				 Bundle bundle = new Bundle();
				 bundle.putString("docType", docType);
				 bundle.putString("flowCenterGuid", flowCenterGuid);
				 bundle.putString("docGuid", docGuid);
				 msg.setData(bundle);
				 handler.sendMessage(msg);
			 } catch (Exception e) {
				e.printStackTrace();
			}
		 }
		 //添加意见
		 @JavascriptInterface
		 public void AddProcessOpinions(String sender,String flowGuid,String docType,String type){
			 Message msg = new Message();
			 msg.what = 8;
			 Bundle bundle = new Bundle();
			 bundle.putString("sender", sender);
			 bundle.putString("flowGuid", flowGuid);
			 bundle.putString("docType", docType);
			 bundle.putString("type", type);
			 msg.setData(bundle);
			 handler.sendMessage(msg);
			 
		 }
		 //办结
		 @JavascriptInterface
		 public void ProcessEnd(String emailTile,String flowGuid,String docType,String type){
			 Message msg = new Message();
			 msg.what = 9;
			 Bundle bundle = new Bundle();
			 bundle.putString("emailTile", emailTile);
			 bundle.putString("flowGuid", flowGuid);
			 bundle.putString("docType", docType);
			 bundle.putString("type", type);
			 msg.setData(bundle);
			 handler.sendMessage(msg);
		 }
		 //返上级流程
		 @JavascriptInterface
		 public void MobileBackUpFlowProcessBySendType(String flowGuid, String flowSendType,String type) {
			 Message msg = new Message();
			 msg.what = 12;
			 Bundle bundle = new Bundle();
			 bundle.putString("flowGuid", flowGuid);
			 bundle.putString("flowSendType", flowSendType);
			 bundle.putString("type", type);
			 msg.setData(bundle);
			 handler.sendMessage(msg);
		 }
		 @JavascriptInterface
		 public String onLoadFile(String filePath){
			 try {
				 FileInputStream fis = new FileInputStream(filePath);
				 baos = new ByteArrayOutputStream();  
				 byte[] buffer = new byte[1024]; 
				 int count = 0;
				 while((count = fis.read(buffer)) >= 0){  
	                baos.write(buffer, 0, count);
		         }
	             fis.close();
				 String uploadBuffer = new String(Base64.encode(baos.toByteArray()));  //进行Base64编码
				 if(baos != null){
					baos.reset();
					baos.close();
					baos = null;
				 }
				 return uploadBuffer;
			 } catch (Exception e) {
				 
				 Message msg = new Message();
				 msg.what = 7;
				 msg.obj = "文件上传失败，可能文件过大！";
				 handler.sendMessage(msg);
				 e.printStackTrace();
				 
				 return "";
			 }
		 }
	}
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.LinTopBack:
			if(htmCanTransfer){
				webview.loadUrl("javascript:webgoback()");
			}else{
				if(webview.canGoBack()){
					webview.goBack();
					return;
				}
				finish();
			}
			break;
		case R.id.LinHome:
			finish();
			break;
		default:
			break;
		}
	}
	
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case 1:
					pubilcBut.removeAllViews();
					DynamicCreateButton();
					break;
				case 2:
					if(progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
					Toast.makeText(LoadInternetWebView.this, (String)msg.obj, Toast.LENGTH_LONG).show();
					break;
				case 3:
					//网页获取当前用户信息
					 String userDisplayName = SpUtils.getString(LoadInternetWebView.this, Constants.CUTOVER_DISPLAY_NAME, "");
					 String userGuid = SpUtils.getString(LoadInternetWebView.this, Constants.CUTOVER_AD_GUID, "");
					 
					 webview.loadUrl("javascript:TransferValue('"+userDisplayName+"|"+userGuid+"|"+ Constants.MobileIMEI+"|"+SpUtils.getString(LoadInternetWebView.this, Constants.DISPLAY_NAME, "")+"')");
					break;
				case 4:
					//选择附件
					Intent intent = new Intent(LoadInternetWebView.this, SDFileListActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
					break;
				case 6:
					//选择附件
					Bundle bundle2 = msg.getData();
					webview.loadUrl("javascript:addFilesInfo('"+bundle2.getString("fileName")+"|"+bundle2.getString("suffix")+"|"+bundle2.getString("length")+"|"+bundle2.getString("filePath")+"')");
					application.removeAllAttachment(); //清除附件缓存
					break;
				case 7:
					String path = (String) msg.obj;
					FileUtils.openFile(LoadInternetWebView.this,new File(path));
					break;
				case 9:
					//办结
					EmailSetEnd(msg.getData());
					break;
				case 10:
					//办结之后界面处理
					Bundle t_bundle = msg.getData();
					String zyb = t_bundle.getString("result");
					if ("1".equals(zyb)) {
						Toast.makeText(LoadInternetWebView.this,"办结成功",Toast.LENGTH_SHORT).show();
						Message m_msg = new Message();
						m_msg.what = 11;
						m_msg.obj = t_bundle.getString("type");
						handler.sendMessage(m_msg);
					} else {
						Toast.makeText(LoadInternetWebView.this,"办结失败",Toast.LENGTH_SHORT).show();
					}
					break;
				case 11:
					//返回给html
					String backType = (String) msg.obj;
					if(backType != null && !backType.equals("")){
						webview.loadUrl("javascript:FlowAfterApk('"+backType+"')");
					}
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		//返回上一个web,主页返回
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
//            webview.goBack(); //goBack()表示返回WebView的上一页面  
        	if(progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
            return false;  
        }  
//        finish();
        return super.onKeyDown(keyCode, event);
	}
	
	public void onDestroy(){
		super.onDestroy();
		try {
			SysExitUtil.RemoveActivity(LoadInternetWebView.this);
			application.removeAllAttachment(); //清除附件缓存
			rootActivity = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void onRestart(){
		super.onRestart();
		if(isSelFile){
			if(application.getAttachmentList() != null && application.getAttachmentList().size()>0){
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
		isSelFile = false;
		}
	}
	
	private void EmailSetEnd(Bundle bundle)throws Exception{
		String userXml="<root>";
		userXml+="<userguid>"+SpUtils.getString(LoadInternetWebView.this, Constants.CUTOVER_AD_GUID, "")+"</userguid>";
		userXml+="<username>"+SpUtils.getString(LoadInternetWebView.this, Constants.CUTOVER_DISPLAY_NAME, "")+"</username>";
		userXml+="<devicename>"+ Constants.mobileName+"</devicename>";
		userXml+="<title>"+bundle.getString("emailTile")+"</title>";
		userXml+="</root>";
		final String type = bundle.getString("type");
		WebServiceNetworkAccess.FinishFlowStep(bundle.getString("flowGuid"),bundle.getString("docType"), userXml,"", new Callback() {
			@Override
			public void handle(String result) {
				Bundle bundle = new Bundle();
				bundle.putString("type", type);
				bundle.putString("result", result);
				Message msg = new Message();
				msg.what = 10;
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		});
	}
	
	//动态创建头部button
	private void DynamicCreateButton() throws Exception{
		try {
			for(int i=0;i<listButName.size();i++){
				final int type = i;
				LinearLayout lLayout=new LinearLayout(LoadInternetWebView.this);
				LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
				lLayout.setOrientation(LinearLayout.VERTICAL);
				lLayout.setGravity(Gravity.CENTER);
				lp.rightMargin = DensityUtil.dip2px(LoadInternetWebView.this, 10);
				lLayout.setBackgroundResource(R.drawable.image_linearlout);
				lLayout.setLayoutParams(lp);   
				
				final ImageView butimg = new ImageView(LoadInternetWebView.this);
//				butimg.setPadding(
//						DensityUtil.px2dip(LoadInternetWebView.this, 20),
//						0, 0,DensityUtil.px2dip(LoadInternetWebView.this, 10));
//				butimg.setLayoutParams(new LinearLayout.LayoutParams(25, 25, 0f));
				butimg.setLayoutParams(new LinearLayout.LayoutParams(DensityUtil.dip2px(LoadInternetWebView.this, getResources().getDimension(R.dimen.loaddynamicbutwidth)),
						DensityUtil.dip2px(LoadInternetWebView.this, getResources().getDimension(R.dimen.loaddynamicbutwidth)), 0f));
				//加载网络图标
				BitmapUtils bitmapUtils = new BitmapUtils(LoadInternetWebView.this);
				bitmapUtils.configMemoryCacheEnabled(false);//内存缓存
        		bitmapUtils.configDiskCacheEnabled(false);//磁盘缓存
				bitmapUtils.display(butimg, listButName.get(type).getIconurl());
				lLayout.addView(butimg);
				
				final TextView butText = new TextView(LoadInternetWebView.this);
				butText.setTextColor(Color.WHITE);
				butText.setTextSize(14);
				butText.setPadding(0, DensityUtil.px2dip(LoadInternetWebView.this, 8), 0,0);
				butText.setText(listButName.get(i).getName());
				butText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0f));
				lLayout.addView(butText);
				lLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					 if (Clickflag) {  
						 setFlag();
						 webview.loadUrl("javascript:"+listButName.get(type).getClickname());
						 new TimeThread().start();  
					  }    
					}
				});
				pubilcBut.addView(lLayout);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private boolean Clickflag = true;  
	private synchronized void setFlag() {  
		Clickflag = false;  
    }  
	 /**  
     * 计时线程（防止在一定时间段内重复点击按钮）  
     */   
    public class TimeThread extends Thread {    
       public void run() {  
            try {  
                Thread.sleep(1000);  
                Clickflag = true;  
           } catch (Exception e) {  
                e.printStackTrace();  
            }  
       }  
    } 
	//解析动态button
	private List<DynamicButBean> ParserXML(String xml)throws Exception{
		if (xml == null){
			throw new NullPointerException();
		}
		List<DynamicButBean> dataSet = new ArrayList<DynamicButBean>();
		DynamicButParser bp = new DynamicButParser(dataSet);
		try{
			XmlUtils.saxParse(xml, bp);
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}

}
