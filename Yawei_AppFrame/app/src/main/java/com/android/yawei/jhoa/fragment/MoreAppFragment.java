//package com.android.yawei.jhoa.fragment;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout.LayoutParams;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.android.yawei.cordova.webview.CordovaWebview;
//import com.android.yawei.jhoa.adapter.MoreAppGridAdapter;
//import com.android.yawei.jhoa.adapter.ViewPagerAdapter;
//import com.android.yawei.jhoa.bean.AppOptionBean;
//import com.android.yawei.jhoa.bean.MoreAppBean;
//import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
//import com.android.yawei.jhoa.mobile.LoginLogActivity;
//import com.android.yawei.jhoa.mobile.NotepadActivity;
//import com.android.yawei.jhoa.ui.CustomProgressDialog;
//import com.android.yawei.jhoa.utils.Constants;
//import com.android.yawei.jhoa.utils.FileUtils;
//import com.android.yawei.jhoa.utils.SpUtils;
//import com.android.yawei.jhoa.webservice.WebService;
//import com.android.yawei.jhoa.webview.LoadHtmlWebview;
//import com.android.yawei.jhoa.webview.LoadInternetWebView;
//import com.hxyl.Elink;
//import com.yawei.jhoa.mobile.R;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MoreAppFragment extends Fragment {//implements Elink.IEventHandler{
//
//	private ViewPager viewPager;//viewpager
//	private List<MoreAppBean> listData;//gridview数据
//	private MoreAppGridAdapter adapter;//gridview适配器
//	private ArrayList<View> pageViews;//viewpager数据
//	private String userGuid;//用户唯一标示
//	private String userId;//用户交换号
//	private int current;//记录viewpager滑动到当前的第几页
//	private List<MoreAppGridAdapter> listAdapters;//存储多页girdview的适配器
//	private static CustomProgressDialog progressDialog;
//	private MoreAppBean clickBean;//记录被点击的应用
//	private LinearLayout layout_point;// 游标显示布局
//	private ArrayList<ImageView> pointViews;//小点数据
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//
//		if(progressDialog == null || !progressDialog.isShowing()){//判断progressDialog是否显示，如果没显示创建
//			progressDialog = CustomProgressDialog.createDialog(getActivity());
//			progressDialog.setMessage("正在加载中,请稍后...");
//			progressDialog.setCancelable(true);
//			progressDialog.show();
//		}
//		View rootView = inflater.inflate(R.layout.fragment_moreapp, container, false);
//		View newView = InitView(rootView);
////		Elink.registerHandler(this);
//
//		return newView;
//	}
//	private View InitView(View rootView) {
//
//		listData = new ArrayList<MoreAppBean>();
//		pageViews = new ArrayList<View>();
//		listAdapters = new ArrayList<MoreAppGridAdapter>();
//		userGuid = SpUtils.getString(getActivity(), Constants.CUTOVER_AD_GUID, "");
//		userId = SpUtils.getString(getActivity(), Constants.CUTOVER_EXCHANGE_ID, "");
//
//		viewPager = (ViewPager)rootView.findViewById(R.id.viewpager);
//		layout_point = (LinearLayout) rootView.findViewById(R.id.iv_image);
//
//		MoreAppBean bean = new MoreAppBean();
//		/*bean.setAppName("急要件");
//		bean.setAppDrawable(R.drawable.image_myaojian);
//		listData.add(bean);
//		bean = new MoreAppBean();
//		bean.setAppName("反馈意见");
//		bean.setAppDrawable(R.drawable.image_fkyj);
//		listData.add(bean);
//		bean = new MoreAppBean();
//		bean.setAppName("使用说明");
//		bean.setAppDrawable(R.drawable.image_sysm);
//		listData.add(bean);*/
//		/*bean = new MoreAppBean();
//		bean.setAppName("登录日志");
//		bean.setAppDrawable(R.drawable.image_dlrz);
//		listData.add(bean);*/
//		bean = new MoreAppBean();
//		bean.setAppName("记事本");
//		bean.setAppDrawable(R.drawable.ic_jishiben);
//		listData.add(bean);
//		bean = new MoreAppBean();
//		bean.setAppName("急要件");
//		bean.setAppDrawable(R.drawable.ic_important_email);
//		listData.add(bean);
//		/*bean = new MoreAppBean();
//		bean.setAppName("金宏网盘");
//		bean.setAppDrawable(R.drawable.image_skgfile);
//		listData.add(bean);*/
//
//		WebServiceNetworkAccess.GetModuleInfoByUserGuid(userGuid,userId,Constants.APPGUID, new WebService.Callback() {
//			@Override
//			public void handle(String result) {
//				Message msg = new Message();
//				msg.what = 0;
//				msg.obj = result;
//				handler.sendMessage(msg);
//			}
//		});
//		return rootView;
//	}
//	//TODO　分页
//	private List<List<MoreAppBean>> GetPagerPaging() throws Exception{
//		List<List<MoreAppBean>> listBean = new ArrayList<List<MoreAppBean>>();
//		int pageCount = (int) Math.ceil(listData.size() / 20 + 0.1);
//
//		for (int i = 0; i < pageCount; i++) {
//			listBean.add(getData(i));
//		}
//		return listBean;
//	}
//	//TODO　分离数据，降所有的数据按照每20个分离
//	private List<MoreAppBean> getData(int page) {
//		int startIndex = page * 20;
//		int endIndex = startIndex + 20;
//		//判断添加的数据是否越狱，如果越狱重新赋值
//		if (endIndex > listData.size()) {
//			endIndex = listData.size();
//		}
//		// 截取listData中数据添加到list中
//		List<MoreAppBean> list = new ArrayList<MoreAppBean>();
//		list.addAll(listData.subList(startIndex, endIndex));
//
//		return list;
//	}
//	//TODO  item点击
//	private OnItemClickListener onItemClick = new OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View view, int postion, long arg3) {
//			try {
//				clickBean = (MoreAppBean) listAdapters.get(current).getItem(postion);
//				Intent intent = null;
//				if(clickBean.getLinkUrl() == null || clickBean.getLinkUrl().isEmpty()){
//					if(clickBean.getAppName().equals("登录日志")){
//						intent= new Intent(getActivity(), LoginLogActivity.class);
//		         		startActivity(intent);
//					}else if(clickBean.getAppName().equals("记事本")){
//						intent = new Intent(getActivity(), NotepadActivity.class);
//		         		startActivity(intent);
//					}else if(clickBean.getAppName().equals("聊天")){
//						if(Constants.ChatIsInit){
////							startActivity(new Intent(getActivity(),ChatSecondActivity.class));
//							ArrayList tabList = new ArrayList();
//							tabList.add(Elink.TAB_MESSAGE);
//							tabList.add(Elink.TAB_ORGNIZATION);
//							tabList.add(Elink.TAB_CONTACT);
//							Elink.showMainPage(getActivity(),tabList);
//						}else{
////							progressDialog = CustomProgressDialog.createDialog(getActivity());
////							progressDialog.setMessage("正在加载中,请稍后...");
////							progressDialog.setCancelable(true);
////							progressDialog.show();
////							String portStr = "9000";
////							String httpPortStr = "9090";
////							String ip = "49.4.1.212";
////							int port = Integer.parseInt(portStr);
////							int httpPort = Integer.parseInt(httpPortStr);
////							Elink.init(ip,port,httpPort);
////							handler.sendEmptyMessageDelayed(1000,1000);
////							Elink.login("caoxiaohua","a12345678");
////							intent = new Intent(getActivity(), ChatMainActivity.class);
////							startActivity(intent);
//							Toast.makeText(getActivity(),"聊天初始化失败，请重新登录在进入聊天",Toast.LENGTH_LONG).show();
//						}
//
//					}/*else if(clickBean.getAppName().equals("设置")){
//						intent = new Intent(getActivity(), MyPersonalCenterActivity.class);
//						startActivity(intent);
//					}*/else if(clickBean.getAppName().equals("退出")){
//						ExitApp();
//					}
//				}else{
//					//0：H5应用； 1：混合应用；2：独立应用；3：自带应用
//					if(clickBean.getModuletype().equals("0")){
//						intent = new Intent(getActivity(), LoadHtmlWebview.class);
//						String []data =  clickBean.getLinkUrl().split("[?]");
//						if(data.length>1){
//							intent.putExtra("loadurl", data[0]);
//							intent.putExtra("sign", data[1]);
//						}else{
//							intent.putExtra("loadurl",  clickBean.getLinkUrl());
//						}
//						startActivity(intent);
//					}else if(clickBean.getModuletype().equals("1")){
//						String mUrl = Constants.WEB_SERVICE_URL;
//						mUrl = mUrl.substring(0, mUrl.lastIndexOf("/"));
//						mUrl = mUrl.substring(0, mUrl.lastIndexOf("/")+1);
//						String []data = clickBean.getLinkUrl().split("[|]");
//						String paraJson = "";
//						if(data.length>1){
//							String []parameter = data[1].split(";");
//							for(int i=0;i<parameter.length;i++){
//								String[] strpara = parameter[i].split("=");
//								paraJson += "\""+ strpara[0]+"\""+":"+"\""+strpara[1]+"\""+",";
//							}
//							paraJson = paraJson.substring(0,paraJson.lastIndexOf(","));
//							paraJson = "{"+paraJson+"}";
//						}
//						intent = new Intent(getActivity(), CordovaWebview.class);
////                intent.putExtra("loadurl", bean.getModuleurl());
//						intent.putExtra("loadurl", "file://"+ FileUtils.GetSDPath()+"jhoamobile/html/"+data[0]);
//						intent.putExtra("murl", mUrl);
//						intent.putExtra("parameter",paraJson);
//						intent.putExtra("appfrom", "");
//						startActivity(intent);
//					}else  if(clickBean.getModuletype().equals("2")){
//						intent = getActivity().getPackageManager().getLaunchIntentForPackage(clickBean.getLinkUrl());
//						startActivity(intent);
//					}else if(clickBean.getModuletype().equals("3")){
//						if(clickBean.getLinkUrl().equals("chart")){
//							try {
//								if(Constants.ChatIsInit){
//									ArrayList tabList = new ArrayList();
//									tabList.add(Elink.TAB_MESSAGE);
//									tabList.add(Elink.TAB_ORGNIZATION);
//									tabList.add(Elink.TAB_CONTACT);
//									Elink.showMainPage(getActivity(),tabList);
//								}else{
//									Toast.makeText(getActivity(),"沟通环境未初始化成功请稍后再试",Toast.LENGTH_LONG).show();
//								}
//							}catch (Exception e){
//								e.printStackTrace();
//							}
//						}else {
//							try {
//								String []data = clickBean.getLinkUrl().split("[|]");
//								intent = new Intent(getActivity(),Class.forName(data[0]));
//								if(data.length>1){
//									String []parameter = data[1].split(";");
//									for(int i=0;i<parameter.length;i++){
//										String[] strpara = parameter[i].split("=");
//										intent.putExtra(strpara[0],strpara[1]);
//									}
//								}
//								startActivity(intent);
//							} catch (ClassNotFoundException e) {
//								Toast.makeText(getActivity(),"启动页面错误，请检查配置模块地址是否正确",Toast.LENGTH_LONG).show();
//								e.printStackTrace();
//							}
//						}
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		}
//	};
//
//	public Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			//TODO 消息handler
//			try {
//				switch (msg.what) {
//				case 2:
//					if(progressDialog != null && progressDialog.isShowing())
//			 			progressDialog.dismiss();
//
//					String lightxml = (String)msg.obj;
//					if(lightxml != null && !lightxml.equals("") && !lightxml.equals("anyType")){
//						if(clickBean.getLinkUrl().toLowerCase().contains("http")){
//							String murl = clickBean.getLinkUrl()+"?syscode="+lightxml;
//						 	Intent intent = new Intent(getActivity(), LoadInternetWebView.class);
//							intent.putExtra("LOADURL", murl);
//							intent.putExtra("TITLE", clickBean.getAppName());
//							startActivity(intent);
//						}else{
//							String mUrl = Constants.WEB_SERVICE_URL;
//							mUrl = mUrl.substring(0, mUrl.lastIndexOf("/"));
//							mUrl = mUrl.substring(0, mUrl.lastIndexOf("/")+1);
//							Intent intent = new Intent(getActivity(), CordovaWebview.class);
//							intent.putExtra("murl", mUrl);
//							intent.putExtra("appfrom", clickBean.getGuid());
//							startActivity(intent);
//						}
//					}else{
//						Toast.makeText(getActivity(), "认证失败", Toast.LENGTH_SHORT).show();
//					}
//					break;
//				case 3:
//					if(progressDialog != null && progressDialog.isShowing())
//			 			progressDialog.dismiss();
//					String strPath = (String)msg.obj;
//					FileUtils.openFile(getActivity(),new File(strPath));
//					break;
//				case 4:
//					if(progressDialog != null && progressDialog.isShowing())
//			 			progressDialog.dismiss();
//					Toast.makeText(getActivity(), (String)msg.obj, Toast.LENGTH_SHORT).show();
//					break;
//				case 0:
//					if(progressDialog != null && progressDialog.isShowing())
//			 			progressDialog.dismiss();
//					String xmlCode = (String) msg.obj;
//					if(xmlCode != null && !xmlCode.equals("") && !xmlCode.equals("anyType")){
//						try {
//							JSONArray jsonArray = JSONArray.parseArray(xmlCode);
//							for (int i=0;i<jsonArray.size();i++){
//								JSONObject jsonObject = jsonArray.getJSONObject(i);
//								JSONArray jsonArray1 = jsonObject.getJSONArray("moduleinfo");
//								List<AppOptionBean> strlistArr = new ArrayList<>();
//								for (int j=0;j<jsonArray1.size();j++){
//									JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
//									MoreAppBean bean = new MoreAppBean();
//									bean.setAppName(jsonObject1.getString("modulename"));
//									bean.setImgUrl(jsonObject1.getString("imageurl"));
//									bean.setLinkUrl(jsonObject1.getString("moduleurl"));
//									bean.setModuletype(jsonObject1.getString("moduletype"));
//									bean.setType(jsonObject1.getString("moduletype"));
//									listData.add(bean);
//								}
//							}
//						}catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//					handler.sendEmptyMessage(1);
//					break;
//				case 1:
//					MoreAppBean bean = new MoreAppBean();
//					/*bean.setAppName("设置");
//					bean.setAppDrawable(R.drawable.image_mshezhi);
//					if(Constants.IsVerSion)
//						bean.setIsnew("1");
//					listData.add(bean);
//					bean = new MoreAppBean();
//					bean.setAppName("退出");
//					bean.setAppDrawable(R.drawable.image_mexit);
//					listData.add(bean);*/
//					try {
//						//动态创建gridview数据
//						List<List<MoreAppBean>> listBean = new ArrayList<List<MoreAppBean>>();
//						listBean = GetPagerPaging();
//						for (int i = 0; i < listBean.size(); i++) {
//							//解决当listData.size()刚好是20的倍数时会多出一页,同时防止不必要的bug产生
//							if(listBean.get(i).size()>0){
//								LinearLayout liner = new LinearLayout(getActivity());
//								LinearLayout.LayoutParams  linserLay = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//								liner.setLayoutParams(linserLay);
//
////								LineGridView view = new LineGridView(getActivity());//重写的gridview，四周有边框，分割线
//								GridView view = new GridView(getActivity());//标准的gridview
//								adapter = new MoreAppGridAdapter(listBean.get(i),getActivity());
//								view.setAdapter(adapter);
//								listAdapters.add(adapter);
//
//								view.setOnItemClickListener(onItemClick);
//								view.setNumColumns(4);//设置GridView一行显示几个图标
//								view.setBackgroundColor(Color.TRANSPARENT);
//								view.setHorizontalSpacing(5);//列间距
//								view.setVerticalSpacing(5);//行间距
//								view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
////								view.setVerticalSpacing(2);//水平线
////								view.setHorizontalSpacing(2);//垂直线
//								view.setCacheColorHint(0);//设置滚动时头部出现黑色条
//								view.setPadding(0, 0, 0, 2);
//								view.setSelector(new ColorDrawable(Color.TRANSPARENT));//点中gridview背景为透明，不是默认黄色
//
//								LinearLayout.LayoutParams gridlay = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//								gridlay.setMargins(0, 20, 0, 0);
//								view.setLayoutParams(gridlay);
//								liner.addView(view);
//								pageViews.add(liner);
//							}
//						}
//
//						viewPager.setAdapter(new ViewPagerAdapter(pageViews));
//						viewPager.setOnPageChangeListener(new OnPageChangeListener() {
//							@Override
//							public void onPageSelected(int postion) {
//								try {
//									current = postion;
//									if(pageViews.size()>1){
//										draw_Point(postion);
//									}
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//							}
//							@Override
//							public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//							}
//							@Override
//							public void onPageScrollStateChanged(int arg0) {
//
//							}
//						});
//						if(pageViews.size()>1){
//							Init_Point();
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//					break;
//				default:
//					break;
//				}
//			} catch (Exception e) {
//
//			}
//			super.handleMessage(msg);
//		}
//	};
//	//TODO 初始化下面小点
//  	private void Init_Point() throws Exception{
//
//  		pointViews = new ArrayList<ImageView>();
//  		ImageView imageView;
//  		for (int i = 0; i < pageViews.size(); i++) {
//  			imageView = new ImageView(getActivity());
//  			imageView.setBackgroundResource(R.drawable.logodot2);
//  			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//  					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
//  			layoutParams.leftMargin = 10;
//  			layoutParams.rightMargin = 10;
//  			layoutParams.width = 8;
//  			layoutParams.height = 8;
//  			layout_point.addView(imageView, layoutParams);
//  			if (pageViews.size()>0&& i == 0) {
//  				imageView.setBackgroundResource(R.drawable.logodot1);
//  			}
//  			pointViews.add(imageView);
//  		}
//  	}
//    //TODO 页面切换重新画小点
//  	public void draw_Point(int index) throws Exception{
//  		for (int i = 0; i < pointViews.size(); i++) {
//  			if (index == i) {
//  				pointViews.get(i).setBackgroundResource(R.drawable.logodot1);
//  			} else {
//  				pointViews.get(i).setBackgroundResource(R.drawable.logodot2);
//  			}
//  		}
//  	}
//  	//TODO 退出
//  	private void ExitApp() throws Exception{
//    	LayoutInflater inflater = getActivity().getLayoutInflater();
//		View layout = inflater.inflate(R.layout.activity_dialog,(ViewGroup)getActivity().findViewById(R.id.dialog));
//		AlertDialog  alertDialog;
//	    TextView dialogtitleL=(TextView)layout.findViewById(R.id.dialogtitle);
//	    dialogtitleL.setText("退出登录");
//	    TextView dialogtipL=(TextView)layout.findViewById(R.id.dialogtip);
//	    dialogtipL.setText("确定要退出登录吗？");
//	    alertDialog = new AlertDialog.Builder(getActivity()).setPositiveButton("确定",new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog,int which) {
//
//				dialog.dismiss();
//	            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//					File file = new File(FileUtils.GetSDPath()+"jhoaMobile/temp");
//				    new FileUtils().deleteFile(file);
//	            }
//	            Constants.isLogin=false;
//	            android.os.Process.killProcess(android.os.Process.myPid());
//	            //关闭聊天的后台接受消息的service
//	            System.exit(0);
//	            Intent startMain = new Intent(Intent.ACTION_MAIN);
//                startMain.addCategory(Intent.CATEGORY_HOME);
//                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(startMain);
//			}
//		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            	 dialog.dismiss();
//            }
//        }).create();
//	    alertDialog.setView(layout,-1,-1,-1,-1);
//	    alertDialog.setCanceledOnTouchOutside(true);
//		alertDialog.show();
//    }
////	final int NET_NOTFOUND = 0xFF01;
////	final int NET_DISCONNECT = 0xFF02;
////	final int NET_HEARTBEAT_TIMEOUT = 0xFF03;
////	@Override
////	public void handlerEvent(int command, Object data) {
////		switch (command){
////			case NET_NOTFOUND:
////				Toast.makeText(getActivity(),(String) data,Toast.LENGTH_SHORT).show();
////				break;
////			case NET_DISCONNECT:
////				Toast.makeText(getActivity(),(String) data,Toast.LENGTH_SHORT).show();
////				break;
////			case NET_HEARTBEAT_TIMEOUT:
////				Toast.makeText(getActivity(),(String) data,Toast.LENGTH_SHORT).show();
////				break;
////			case 0x0102:
////				if(progressDialog != null && progressDialog.isShowing())
////					progressDialog.dismiss();
////				Log.e("Xup ",this+" command:"+Integer.toHexString(command)+",data:"+data);
////				if(data == null){
////					startActivity(new Intent(getActivity(),ChatSecondActivity.class));
////				}else{
////					String msg = (String) data;
////					Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
////				}
////				break;
////			default:
////				break;
////		}
////	}
//}
