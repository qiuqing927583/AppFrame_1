package com.android.yawei.jhoa.mobile;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.cordova.webview.CordovaWebview;
import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.bean.UserCheckCode;
import com.android.yawei.jhoa.bean.UserStatusBean;
import com.android.yawei.jhoa.dialog.LoginModelActivity;
import com.android.yawei.jhoa.facescan.DetectLoginActivity;
import com.android.yawei.jhoa.factory.ResolveXML;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.fingerprint.FingerprintActivity;
import com.android.yawei.jhoa.ui.CustomProgressDialog;
import com.android.yawei.jhoa.utils.AppUtils;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.DownloadUpdate;
import com.android.yawei.jhoa.utils.FileUtils;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.NetworkUtils;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.utils.SysExitUtil;
import com.android.yawei.jhoa.utils.UnzipFromAssets;
import com.android.yawei.jhoa.utils.UpdateThread;
import com.android.yawei.jhoa.webservice.WebService.Callback;
import com.android.yawei.jhoa.webview.MainViewWebview;
import com.hxyl.Elink;
import com.yawei.android.appframework.utils.DensityUtil;
import com.yawei.jhoa.mobile.R;

import java.io.File;


/**********************
 *
 * TODO 登录
 * @author Yusz
 *
 *
 **********************/
public class JhoaLoginActivity extends BaseActivity implements OnClickListener, Elink.IEventHandler {

	private EditText editUserName;//用户名
	private EditText editUserPwd;//用户密码
	private ScrollView mScrollView;//界面scollview
	private Button butLogin;//登录按钮
	private TextView txt_loginMode;//登录方式
	private CustomProgressDialog progressDialog = null;//dialog提示信息
	private String strUserName, strUserPwd;//记录全局变量用户输入的用户名和密码
	public MyHandler handler = new MyHandler();

	private AlertDialog dialogNewApp; //下载新版本时的对话框
	private EditText dialogtip;//版本提示下载更新，更新的内容
	private Dialog dialog_accesscode; //下载dialog
	private ProgressBar progtessBar;//下载进度
	private TextView textProleght;//下载比例

	private UserStatusBean userBean;
	public static JhoaLoginActivity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			finish();
			return;
		}
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		setContentView(R.layout.activity_login);

		try {
			activity = this;
			Constants.PACKAGE_NAME = AppUtils.getAppName(this);
			InitView();
			String loginmodel = SpUtils.getString(getApplicationContext(),Constants.LOGINMODLE,"");
			if(TextUtils.isEmpty(loginmodel)||"1".equals(loginmodel)){

			}else if("2".equals(loginmodel)){
				Intent intent = new Intent(JhoaLoginActivity.this, DetectLoginActivity.class);
				startActivityForResult(intent,1);
			}else if("3".equals(loginmodel)){
				Intent intent = new Intent(JhoaLoginActivity.this, FingerprintActivity.class);
				intent.putExtra("sign","1");
				startActivityForResult(intent,1);
			}
			if (NetworkUtils.isConnected(getApplicationContext())) {//获取网络状态
//				InitAppDownDialog();
//				UpdateThread ut = new UpdateThread(handler, getApplicationContext(), -1);
//				ut.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void InitView() throws Exception {
		mScrollView = (ScrollView) findViewById(R.id.scollview);
		editUserName = (EditText) findViewById(R.id.userName);
		editUserPwd = (EditText) findViewById(R.id.userPwd);
		String logName = SpUtils.getString(getApplicationContext(), Constants.AD_LOGNAME, "");
		if (logName != null && !"".equals(logName)) {
			editUserName.setText(logName);
		}
		editUserName.setOnTouchListener(OnTouchListener);
		editUserPwd.setOnTouchListener(OnTouchListener);
		txt_loginMode = (TextView)findViewById(R.id.txt_loginMode);
		txt_loginMode.setOnClickListener(this);
		butLogin = (Button) findViewById(R.id.login);
		butLogin.setOnClickListener(this);
	}
    /*edit 点击选中监听*/
	private OnTouchListener OnTouchListener = new OnTouchListener() {
		int vId = 0;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			vId = v.getId();
			if (event.getAction() == MotionEvent.ACTION_UP) {
				//这里必须要给一个延迟，如果不加延迟则没有效果。我现在还没想明白为什么
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						//将ScrollView滚动到底
						mScrollView.fullScroll(View.FOCUS_DOWN);
					}
				}, 300);
			}
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (editUserName.getId() == vId) {
						editUserPwd.clearFocus();//取消焦点
						editUserName.requestFocus();//获取焦点
					} else {
						editUserName.clearFocus();
						editUserPwd.requestFocus();
					}
				}
			}, 300);
			return false;
		}
	};
	public class  MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
					case 0:
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						GetUserInfoFromMobile(strUserName,strUserPwd);
						break;
					case 1:
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						if(!TextUtils.isEmpty((String)msg.obj))
							Toast.makeText(JhoaLoginActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
						break;
					case 2://关闭加载窗口
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						finish();
						break;
					case 7:
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						Toast.makeText(JhoaLoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
						break;
					case 8:
						Bundle bundle8 = msg.getData();
						InitUserInfo(bundle8);
						break;
					case UpdateThread.ERROR:
						Toast.makeText(JhoaLoginActivity.this, "获取版本信息失败！", Toast.LENGTH_SHORT).show();
						break;
					case DownloadUpdate.ERROR:
						if (dialog_accesscode != null && dialog_accesscode.isShowing()) {
							dialog_accesscode.dismiss();
						}
						Toast.makeText(JhoaLoginActivity.this, "下载失败！", Toast.LENGTH_SHORT).show();
						handler.sendEmptyMessage(1);
						break;
					case UpdateThread.APPUPVERSION:
						if (UpdateThread.versionInfo != null && UpdateThread.versionInfo.size() > 0 && UpdateThread.versionInfo.get(0).getUpmessage() != null) {
							//xml返回的换行符实现不了换行
							String information = UpdateThread.versionInfo.get(0).getUpmessage().replace("|", "\n");
							dialogtip.setText(information);
						}
						dialogNewApp.show();
						break;
					case DownloadUpdate.DOWNLOADPRO:
						// 下载的时候更新Notification
						Bundle b = msg.getData();
						// 得到进度条的当前值
						int progressCount = b.getInt("progressValue");
						// 下载未完成，显示进度条
						if (progressCount < 100) {
							if (dialog_accesscode != null) {
								textProleght.setText(progressCount + "%");
								progtessBar.setProgress(progressCount);
							}
						} else {// 下载完成，显示下载完成
							if (dialog_accesscode != null && dialog_accesscode.isShowing()) {
								dialog_accesscode.dismiss();
							}
						}
						break;
					case UpdateThread.RESOURCES:
						//下载资源文件
						InitDilogProgress("办公资源更新");//初始化dialog进度条
						String name = UpdateThread.versionInfo.get(0).getWwwurl();
						name = name.substring(name.lastIndexOf("/") + 1, name.length());
						DownloadUpdate dw = new DownloadUpdate(name, UpdateThread.versionInfo.get(0).getWwwurl(), UpdateThread.versionInfo.get(0).getWwwfilelength(), JhoaLoginActivity.this, handler);
						dw.start();
						break;
					case DownloadUpdate.UNZIP:
						ActicinUnzip((String) msg.obj);
						break;
					default:
						break;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			super.handleMessage(msg);
		}
	}
	@Override
	public void onClick(View v) {
		//TODO 点击事件
		try {
			if (SysExitUtil.isFastDoubleClick())
				return;
			switch (v.getId()) {
				case R.id.login:
					//登录
					if (editUserName.getText().toString().equals("")) {
						Toast.makeText(JhoaLoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
						return;
					} else if (editUserPwd.getText().toString().equals("")) {
						Toast.makeText(JhoaLoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
						return;
					}

					strUserName = editUserName.getText().toString();
					strUserPwd = editUserPwd.getText().toString();
					Constants.PASSWORD=strUserPwd;
					if (NetworkUtils.isConnected(getApplicationContext())) {//获取网络状态
						handler.sendEmptyMessage(0);
					} else {
						Message msg = new Message();
						msg.obj = "网络异常，请检查网络！";
						msg.what = 1;
						handler.sendMessage(msg);
					}
					break;
				case R.id.txt_loginMode:
					Intent intentmode = new Intent(JhoaLoginActivity.this,LoginModelActivity.class);
					startActivityForResult(intentmode,1);
					break;
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//TODO 新开发获取用户状态或信息接口
	public void GetUserInfoFromMobile(String strUserName,String strUserPwd) {
		progressDialog = CustomProgressDialog.createDialog(JhoaLoginActivity.this);
		progressDialog.setMessage("正在验证身份,请稍后...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		String devinfo = "<root>";
		devinfo += "<clientip>IP</clientip>";
		devinfo += "<clientmac>mac</clientmac>";
		devinfo += "<mobilebrand>" + android.os.Build.BRAND + "</mobilebrand>";
		devinfo += "<mobilemodel>" + android.os.Build.MODEL + "</mobilemodel>";
		devinfo += "<mobileversion>Android " + android.os.Build.VERSION.RELEASE + "</mobileversion>";
		devinfo += "<mobileimei>" + Constants.MobileIMEI + "</mobileimei>";
		devinfo += "<mobiletype>" + 2 + "</mobiletype>";
		devinfo += "</root>";
		WebServiceNetworkAccess.GetUserInfoFromMobile(Constants.APPGUID, strUserName, strUserPwd, devinfo, new Callback() {
			@Override
			public void handle(String result) {
				try {
					if (result != null && !result.equals("") && !result.equals("anyType")) {
						UserStatusBean statusBean = ResolveXML.parseUserCheck(result);
						if (statusBean.getStatus().equals("0")) {
							Bundle bundle = new Bundle();
							bundle.putSerializable("userinfo", statusBean);
							Message msg = new Message();
							msg.what = 8;
							msg.setData(bundle);
							handler.sendMessage(msg);
						} else if (statusBean.getStatus().contains("1^")) {
							String[] strarr = statusBean.getStatus().split("\\^");
							Message msg = new Message();
							msg.what = 1;
							msg.obj = "账号密码错误，还可以输入" + strarr[1] + "次";
							handler.sendMessage(msg);
						} else if (statusBean.getStatus().equals("2") || statusBean.getStatus().equals("3")) {
							Message msg = new Message();
							msg.what = 1;
							msg.obj = statusBean.getErrorflag();
							handler.sendMessage(msg);
						} else if (statusBean.getStatus().equals("4")) {
							Message msg = new Message();
							msg.what = 1;
							msg.obj = "该用户被禁用";
							handler.sendMessage(msg);
						} else if (statusBean.getStatus().equals("5")) {
							Message msg = new Message();
							msg.what = 1;
							msg.obj = "不存在该用户";
							handler.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = 1;
						msg.obj = "网络异常，请检查网络重新登录";
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = 1;
					msg.obj = "获取信息失败！";
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		});
	}
	//初始化APP下载提示框
	private void InitAppDownDialog() {
		//提示是否下载新版本的提示框
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.activity_dialog, (ViewGroup) findViewById(R.id.dialog));
		TextView dialogtitle = (TextView) layout.findViewById(R.id.dialogtitle);
		dialogtitle.setText("新版本提醒");
		final String appName = AppUtils.getAppName(this)+".apk";
		dialogtip = (EditText) layout.findViewById(R.id.dialogtip);
		dialogNewApp = new AlertDialog.Builder(JhoaLoginActivity.this).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				try {
					InitDilogProgress("办公更新");//初始化dialog进度条
					if (Constants.VERSIONTYPE.equals("1")) {
						DownloadUpdate dw = new DownloadUpdate(appName, UpdateThread.versionInfo.get(0).getUrl(), UpdateThread.versionInfo.get(0).getLength(), JhoaLoginActivity.this, handler);
						dw.start();
					} else if (!Constants.VERSION_ID.equals(UpdateThread.versionInfo.get(0).getVersion())) {
						//如果当前装的是测试版本，有正式版本发布时
						DownloadUpdate dw = new DownloadUpdate(appName, UpdateThread.versionInfo.get(0).getUrl(), UpdateThread.versionInfo.get(0).getLength(), JhoaLoginActivity.this, handler);
						dw.start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).setCancelable(false).create();
		dialogNewApp.setView(layout, -1, -1, -1, -1);
	}
	/*********
	 * 下载进度
	 */
	private void InitDilogProgress(String strHint) throws Exception {
		dialog_accesscode = new AlertDialog.Builder(JhoaLoginActivity.this).create();
		LayoutInflater layou = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View vView = layou.inflate(R.layout.dialog_version, null);
		progtessBar = (ProgressBar) vView.findViewById(R.id.progtessBar);
		textProleght = (TextView) vView.findViewById(R.id.proleght);
		LinearLayout lindialog = (LinearLayout) vView.findViewById(R.id.lindialog);
		TextView titlehint = (TextView) vView.findViewById(R.id.titlehint);
		titlehint.setText(strHint);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		int screenHeigh = dm.heightPixels;
		int screenwight = dm.widthPixels;
		ViewGroup.LayoutParams layoutParams = lindialog.getLayoutParams();
		layoutParams.width = screenwight - DensityUtil.dip2px(getApplicationContext(), 40);
		layoutParams.height = DensityUtil.dip2px(getApplicationContext(), 130);
		lindialog.setLayoutParams(layoutParams);

		textProleght = (TextView) vView.findViewById(R.id.proleght);
		textProleght.setText("0%");
		progtessBar.setMax(100);
		dialog_accesscode.show();
		dialog_accesscode.setCanceledOnTouchOutside(false);
		dialog_accesscode.setCancelable(false);//设置进度条是否可以按退回键取消
		dialog_accesscode.setContentView(vView);
	}
	//解压文件
	private void ActicinUnzip(final String zip) {
		progressDialog = CustomProgressDialog.createDialog(JhoaLoginActivity.this);
		progressDialog.setMessage("正在解压中,请稍后...");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String root = FileUtils.GetSystemFilePath();
					new FileUtils().deleteFile(new File(root + "html/"));
					UnzipFromAssets.unzip(new File(root + "zip/" + zip), root + "html/");
					Message msg = new Message();
					msg.obj = "解压成功！";
					msg.what = 1;
					handler.sendMessage(msg);
					SpUtils.setString(getApplicationContext(), Constants.ASSESZIPVER, UpdateThread.versionInfo.get(0).getWwwversion());
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.obj = "解压失败！";
					msg.what = 1;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}
	//初始化即时通讯
	private void InitELinkChat() {
		Elink.registerHandler(this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String portStr = "9000";
				String httpPortStr = "9090";
				String ip = "49.4.1.43";
				int port = Integer.parseInt(portStr);
				int httpPort = Integer.parseInt(httpPortStr);
				Elink.init(ip, port, httpPort);
				Elink.login(strUserName, strUserPwd);
			}
		}).start();
	}
	//用户信息
	public void InitUserInfo(Bundle Bundle){
		try {
			userBean = (UserStatusBean) Bundle.getSerializable("userinfo");
			//保存可以切换的用户
			SpUtils.setString(getApplicationContext(), Constants.CUTOVER_AD_LOGNAME, userBean.getAccountname());
			SpUtils.setString(getApplicationContext(), Constants.CUTOVER_DISPLAY_NAME, userBean.getName());
			SpUtils.setString(getApplicationContext(), Constants.CUTOVER_AD_DEPART, userBean.getDepart());
			SpUtils.setString(getApplicationContext(), Constants.CUTOVER_AD_GUID, userBean.getUserguid());
			SpUtils.setString(getApplicationContext(), Constants.CUTOVER_EXCHANGE_ID, userBean.getExchangeid());
			SpUtils.setString(getApplicationContext(), Constants.CUTOVER_AD_SYSFLAG, userBean.getFlag());

			//根用户，一直保持不变
			SpUtils.setString(getApplicationContext(), Constants.AD_LOGNAME, userBean.getAccountname());
			SpUtils.setString(getApplicationContext(), Constants.DISPLAY_NAME, userBean.getName());
			SpUtils.setString(getApplicationContext(), Constants.AD_DEPART, userBean.getDepart());
			SpUtils.setString(getApplicationContext(), Constants.AD_GUID, userBean.getUserguid());
			SpUtils.setString(getApplicationContext(), Constants.EXCHANGE_ID, userBean.getExchangeid());
			SpUtils.setString(getApplicationContext(), Constants.AD_SYSFLAG, userBean.getFlag());
			Log.d("账号123",Constants.CUTOVER_AD_LOGNAME);
			if (!userBean.getOaurl().equals("")) {
				if (Constants.OpenChat) {
					InitELinkChat();
				} else {
					GotoActivity();
				}
			} else {
				Message msg_ = new Message();
				msg_.what = 1;
				msg_.obj = "服务器移动办公配置错误";
				handler.sendMessage(msg_);
			}
		}catch (Exception e){
			Message msg_ = new Message();
			msg_.what = 1;
			msg_.obj = "登陆失败";
			handler.sendMessage(msg_);
			e.printStackTrace();
		}
	}
	boolean gotomian = false;
	public void GotoActivity() {
		gotomian = true;
		//TODO 赋值办公地址
		Constants.WEB_SERVICE_URL = userBean.getOaurl() + "mobileInfoService.asmx";
		//Constants.WEB_SERVICE_URL="http://172.18.1.32/mamsservice/webservice/MobileInfoService.asmx";
		Log.d("WEB_SERVICE_URL",Constants.WEB_SERVICE_URL);
		if (userBean.getHomeurl() != null && !userBean.getHomeurl().equals("")) {
			if (userBean.getHomeurl().contains("http://")) {
				Intent intent = new Intent(JhoaLoginActivity.this, MainViewWebview.class);
				String []data = userBean.getHomeurl().split("[?]");
				if(data.length>1){
					intent.putExtra("loadurl", data[0]);
					intent.putExtra("sign", data[1]);
				}else{
					intent.putExtra("loadurl", userBean.getHomeurl());
				}
				startActivity(intent);
			} else {
				Intent intent = new Intent(JhoaLoginActivity.this, CordovaWebview.class);
				intent.putExtra("loadurl", userBean.getHomeurl());
				startActivity(intent);
			}
		} else {
			Intent intent = new Intent(JhoaLoginActivity.this, MainActivityV2.class);
			startActivity(intent);
		}
		handler.sendEmptyMessage(2);//关闭加载窗口
	}
	final int NET_NOTFOUND = 0xFF01;
	final int NET_DISCONNECT = 0xFF02;
	final int NET_HEARTBEAT_TIMEOUT = 0xFF03;
	@Override
	public void handlerEvent(int command, Object data) {
		try {
			switch (command) {
				case NET_NOTFOUND:
					//Toast.makeText(this, (String) data, Toast.LENGTH_SHORT).show();
					FileUtils.writeFileData(FileUtils.GetOneFileName(), "即时通讯错误信息：" + (String) data + "\n");
					break;
				case NET_DISCONNECT:
					//Toast.makeText(this, (String) data, Toast.LENGTH_SHORT).show();
					FileUtils.writeFileData(FileUtils.GetOneFileName(), "即时通讯错误信息：" + (String) data + "\n");
					break;
				case NET_HEARTBEAT_TIMEOUT:
					FileUtils.writeFileData(FileUtils.GetOneFileName(), "即时通讯错误信息：" + (String) data + "\n");
					break;
				case 0x0102:
					String infomsg = (String) data;
					if (infomsg == null) {
						//聊天初始化成功
						Constants.ChatIsInit = true;
						Elink.unRegisterHandler(this);
					}
					break;
				default:
					break;
			}
			if (!gotomian) GotoActivity();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void onBackPressed(){
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File file = new File(FileUtils.GetSystemFilePath() + "temp");
			new FileUtils().deleteFile(file);
		}
		finish();
		System.exit(0);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			switch (resultCode){
				case 100:
					String strmsg10 = data.getStringExtra("userinfo");
					String[] arr10 = strmsg10.split(";");
					strUserName = arr10[0];
					strUserPwd = arr10[1];
					Constants.PASSWORD=strUserPwd;
					handler.sendEmptyMessage(5);
					break;
				case 101:
					String strmsg11 = data.getStringExtra("userinfo");
					if(!TextUtils.isEmpty(strmsg11)&&!"-1".equals(strmsg11)){
						String[] arr11 = strmsg11.split(";");
						strUserName = arr11[0];
						strUserPwd = arr11[1];
						Constants.PASSWORD=strUserPwd;
						handler.sendEmptyMessage(5);
					}
					break;
				default:
					break;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public void onDestroy(){
		super.onDestroy();
		activity = null;
	}
}
