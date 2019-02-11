package com.android.yawei.jhoa.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.adapter.SelectGroupAdapter;
import com.android.yawei.jhoa.adapter.SelectPersonAdapter;
import com.android.yawei.jhoa.bean.GroupBean;
import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.factory.ResolveXML;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.ui.CustomProgressDialog;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.utils.SysExitUtil;
import com.android.yawei.jhoa.webservice.WebService;
import com.android.yawei.jhoa.webservice.WebService.Callback;
import com.yawei.android.appframework.ui.RefreshLayoutListView;
import com.yawei.android.appframework.ui.RefreshLayoutListView.OnLoadMore;
import com.yawei.jhoa.mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 选人
 */
public class SelectPersonListActivity extends BaseActivity implements OnClickListener{

	private String adGuid, exchageId;//用户的唯一标示；用户的交换号
	private RefreshLayoutListView pullRefreshList;//页面加载人员listview
	private SwipeRefreshLayout swip;//google自带下拉刷新
	private List<GroupBean> seldataSet; //下拉框选项数据源
	private int pageNow =1;    //当前页
	private static final int SET_STATUS = 1;
	private String gname,gguid,gtype,parameter;
	private TextView group_tip;//组名称
	private SelectPersonAdapter adapter = null;//选中组中包含的人适配器
	private static CustomProgressDialog progressDialog; //加载提示
	public static SelectPersonListActivity selectPerson;
	private List<User> dataSet;//成员数据
	private LinearLayout linBack;//返回
//	private boolean isRefresh = false,isCheckAll=false; // 判断是否是上拉刷新
	private Button butOksearch;//搜索按钮
	private EditText editSearch;//搜索输入框
	private ImageButton cancelSearch;//清除搜索输入框
	private boolean isSearch = false;//判断是否是搜索状态
	private MyApplication myapplication;//全局
	private ImageView imgSelect;//选择组按钮
	private LinearLayout LinParent;//下拉框依附组件
	private boolean flag = false;//是否初始化完成选组标志  
	private ListView SelListView = null; //下拉悬浮窗口中组列表
	private SelectGroupAdapter optionsAdapter = null;//下拉组适配器
	private int pwidth; //下拉框依附组件宽度，也将作为下拉框的宽度
	private PopupWindow selectPopupWindow= null;//PopupWindow,悬浮窗口对象
	private LinearLayout linDetermine;//确定按钮
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectpersonlist);
		progressDialog = CustomProgressDialog.createDialog(SelectPersonListActivity.this);
		progressDialog.setMessage("正在加载中,请稍后...");
		progressDialog.setCancelable(true);
		progressDialog.show();
		SysExitUtil.AddActivity(this);
		selectPerson = SelectPersonListActivity.this;
		myapplication = (MyApplication) getApplication();
		adGuid = SpUtils.getString(getApplicationContext(), Constants.CUTOVER_AD_GUID, "");//用户唯一标示
		exchageId = SpUtils.getString(getApplicationContext(), Constants.CUTOVER_EXCHANGE_ID, "");//用户交换号
		dataSet = new ArrayList<User>();
		
		InitView();
		initDatas();
	}
	private void InitView(){
		group_tip=(TextView)findViewById(R.id.group_tip);
		linBack  =(LinearLayout)findViewById(R.id.LinTopBack);
		linBack.setOnClickListener(this);
		pullRefreshList = (RefreshLayoutListView)findViewById(R.id.listView);
		pullRefreshList.setOnItemClickListener(onItemClickListener);
		butOksearch = (Button)findViewById(R.id.butOksearch);
		butOksearch.setOnClickListener(this);
		editSearch = (EditText)findViewById(R.id.et_search);
		cancelSearch = (ImageButton)findViewById(R.id.cancel_search);
		cancelSearch.setOnClickListener(this);
		imgSelect = (ImageView)findViewById(R.id.btn_select);
		imgSelect.setOnClickListener(this);
		LinParent = (LinearLayout)findViewById(R.id.LinParent);
		linDetermine = (LinearLayout)findViewById(R.id.LinOk);
		linDetermine.setOnClickListener(this);
		
		//下拉刷新控件
		swip = (SwipeRefreshLayout)findViewById(R.id.swip_index);
		//下拉刷新出现的四个颜色
		swip.setColorSchemeResources( R.color.swiperefresh_color1, R.color.swiperefresh_color2,
	            R.color.swiperefresh_color3, R.color.swiperefresh_color4);
		//刷新
		swip.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				if(isSearch && !editSearch.getText().toString().equals("")){//搜索刷新
					WebServiceNetworkAccess.GetUserListByQuery(editSearch.getText().toString(), new WebService.Callback() {
						@Override
						public void handle(String result) {
							Message msg = new Message();
							msg.what = 2;
							msg.obj = result;
//							isRefresh=true;
							handler.sendMessage(msg);
						}
					});
				}else{//普通刷新
					//下拉拉刷新
					pageNow =1;    //当前页
					// 首次获得的10条信息
		    		WebServiceNetworkAccess.GetUsersInGroupByPageNo(parameter, exchageId, gtype, pageNow+"",new WebService.Callback() {
				       @Override
						public void handle(String result) {
							Message msg = new Message();
							msg.what = 2;
							msg.obj = result;
							handler.sendMessage(msg);
						}
					});
				}
			}
		});
		//加载更多
		pullRefreshList.setLoadMoreListen(new OnLoadMore() {
			@Override
			public void loadMore() {
				//加载更多
				pageNow++;
//				isCheckAll=false;
				WebServiceNetworkAccess.GetUsersInGroupByPageNo(parameter, exchageId, gtype, pageNow+"", new Callback() {
					@Override
					public void handle(String result) {
						Message msg = new Message();
						msg.what = SET_STATUS;
						msg.obj = result;
						handler.sendMessage(msg);
					}
				});
			}
		});
	}

	// 初始化填充Adapter所用List数据
	private void initDatas(){
	    WebServiceNetworkAccess.GetUserGroups(adGuid, exchageId,new Callback() {
			@Override
			public void handle(String result) {
				// TODO 异步获取组信息（公共组和个人组）
				Message msg = new Message();
				msg.what = 13;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		});
	}
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//TODO 消息handler
			try {
				switch (msg.what) {
				case SET_STATUS:
					//加载更多
					if(swip.isShown()){
						swip.setRefreshing(false);
					}
					if (msg.obj != null&& !msg.obj.equals("anyType") && !msg.obj.toString().equals("")&& !msg.obj.toString().equals("<root />")) {
						setInboxItem((String) msg.obj);
					}else if(msg.obj != null && (msg.obj.toString().equals("")|| msg.obj.toString().equals("<root />"))){
						if(progressDialog != null && progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						if(msg.obj.toString().equals("") && pageNow>1){
							pullRefreshList.onLoadComplete(true);//设置准许刷新
						}else{
							if(adapter!=null){
							  adapter.clear();
							  adapter.notifyDataSetChanged();
							}
							pullRefreshList.onLoadComplete(true);//设置准许刷新
							Toast.makeText(SelectPersonListActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
						}
					}else{
						if(progressDialog != null && progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						pullRefreshList.onLoadComplete(true);//设置准许刷新
						Toast.makeText(SelectPersonListActivity.this, getResources().getString(R.string.abnormal), Toast.LENGTH_SHORT).show();
					}
					break;
				case 2:
					//刷新
					if(swip.isShown()){
						swip.setRefreshing(false);
					}
					if (msg.obj != null&& !msg.obj.equals("anyType") && !msg.obj.toString().equals("")&& !msg.obj.toString().equals("<root />")) {
						dataSet.clear();
						if(adapter != null){//刷新清空所有数据重新加载
							adapter.clear();
							adapter.checkedMap.clear();
						}
						setInboxItem((String) msg.obj);
					}else if(msg.obj != null && (msg.obj.toString().equals("")|| msg.obj.toString().equals("<root />"))){
						if(progressDialog != null && progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						if(adapter!=null){
						  adapter.clear();
						  adapter.notifyDataSetChanged();
						}
						pullRefreshList.onLoadComplete(true);//设置准许刷新
						Toast.makeText(SelectPersonListActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
					}else{
						if(progressDialog != null && progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						pullRefreshList.onLoadComplete(true);//设置准许刷新
						Toast.makeText(SelectPersonListActivity.this, getResources().getString(R.string.abnormal), Toast.LENGTH_SHORT).show();
					}
					break;
				case 10:
					//选中某一个组，接收到消息去获取改组下的成员
					pageNow=1;
					editSearch.setText("");
					if(seldataSet!=null){
						progressDialog = CustomProgressDialog.createDialog(SelectPersonListActivity.this);
						progressDialog.setMessage("正在加载中,请稍后...");
						progressDialog.setCancelable(true);
						progressDialog.show();
						if(adapter!=null){
							adapter.checkedMap.clear();
						}
						group_tip.setText(seldataSet.get(msg.arg1).getName());
						gname = seldataSet.get(msg.arg1).getName();
						gguid = seldataSet.get(msg.arg1).getGuid();
						gtype = seldataSet.get(msg.arg1).getType();
						if(gname!=null&&!"".equals(gname)){
							group_tip.setText(gname);
						}
						if(gtype!=null && gtype.equals("1")){//公共組
							parameter=gname;
						}else{
							parameter=gguid;
						}
	//					isRefresh = true;
						WebServiceNetworkAccess.GetUsersInGroupByPageNo(parameter, exchageId, gtype, pageNow+"", new WebService.Callback() {
							@Override
							public void handle(String result) {
								dataSet.clear();
								if(adapter != null){//刷新清空所有数据重新加载
									adapter.checkedMap.clear();
									adapter.clear();
								}
								Message msg_ = new Message();
								msg_.what = SET_STATUS;
								msg_.obj = result;
								handler.sendMessage(msg_);

							}
						});
					}
					pullRefreshList.setSelection(0);
					selectPopupWindow.dismiss();
					break;
				case 11:
					//用户更多的信息
					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.activity_dialog,(ViewGroup) findViewById(R.id.dialog));
				    TextView dialogtitle=(TextView)layout.findViewById(R.id.dialogtitle);
				    dialogtitle.setText("用户详情");
				    TextView dialogtip=(TextView)layout.findViewById(R.id.dialogtip);
				    dialogtip.setText(msg.obj+"");
				    AlertDialog  alertDialog = new AlertDialog.Builder(SelectPersonListActivity.this)
				    .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
		                @Override
		                public void onClick(DialogInterface dialog, int which) {
		                	 dialog.dismiss();
		                }
		            }).create();
				    alertDialog.setView(layout,-1,-1,-1,-1);
					alertDialog.show();
					break;
				case 13:
					String retXml=(String)msg.obj;
				    if (retXml != null&& !retXml.equals("anyType") && !retXml.equals("")&& !retXml.equals("<root />")) {
			        	seldataSet = ResolveXML.parseGroup(retXml);//解析分组
			        	if(seldataSet!=null){
			    	    	gname = seldataSet.get(0).getName();
			    			gguid = seldataSet.get(0).getGuid();
			    			gtype = seldataSet.get(0).getType();
			    			if(gname!=null&&!"".equals(gname)){
			    				group_tip.setText(gname);
			    			}
			    			if(gtype!=null && gtype.equals("1")){
			    				//公共組
			    				parameter=gname;
			    			}else{
			    				parameter=gguid;
			    			}
				    		// 首次获得的10条信息
				    		WebServiceNetworkAccess.GetUsersInGroupByPageNo(parameter, exchageId, gtype, pageNow+"",new WebService.Callback() {
						       @Override
								public void handle(String result) {
								   Message msg13 = new Message();
								   msg13.what = 2;
								   msg13.obj = result;
								   handler.sendMessage(msg13);
								}
							});
			    		}else{
			    			if(progressDialog != null && progressDialog.isShowing()){
			    				progressDialog.dismiss();
			    			}
			    		}
					}else if(retXml.equals("")|| retXml.equals("<root />")){
						if(progressDialog != null && progressDialog.isShowing()){
		    				progressDialog.dismiss();
		    			}
						Toast.makeText(SelectPersonListActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
					}else{
						if(progressDialog != null && progressDialog.isShowing()){
		    				progressDialog.dismiss();
		    			}
						Toast.makeText(SelectPersonListActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
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
	 // 解析组成员
	private void setInboxItem(String result)throws Exception{

		String xml = null;
		if (result == null || !(result instanceof String)){
			xml = "";
		}else{
			xml = (String) result;
		}
		dataSet = ResolveXML.parseGPerson(xml);

		int datasize=0;
		if(dataSet!=null){
			datasize=dataSet.size();
		}
		if(datasize<Integer.parseInt(Constants.PAGE_SIZE)){
			pullRefreshList.onLoadComplete(true);
		}else{
			pullRefreshList.onLoadComplete(false);
		}
		//设定初始化组人员的适配器
		if (datasize != 0){
			if (adapter == null){
				adapter = new SelectPersonAdapter(SelectPersonListActivity.this,dataSet);
				pullRefreshList.setAdapter(adapter);
			}else {
				adapter.append(dataSet);
				adapter.notifyDataSetChanged();
			}
		}
	    if(progressDialog.isShowing()){
			progressDialog.dismiss();
	    }
	}
	private OnItemClickListener onItemClickListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, final View view, final int position, long id){
			try {
				User u;
				ImageView okImg=(ImageView)view.findViewById(R.id.okImage);
				TextView isCheck=(TextView)view.findViewById(R.id.ischeck);
				String strCheck=isCheck.getText()+"";
				if(strCheck.equals("true")){
					 okImg.setImageResource(R.drawable.del);
				     adapter.checkedMap.put(position-1,-11);
				     isCheck.setText("false");
				     u = (User) adapter.getItem(position);
				     myapplication.removeUser(u);
				}else{
					  okImg.setImageResource(R.drawable.add);
					  adapter.checkedMap.put(position-1,position-1);
					  isCheck.setText("true");
					  u = (User) adapter.getItem(position);
					  if(!myapplication.isContainsUser(u)){
						  myapplication.addUser(u);
					  }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.LinTopBack:
				//返回
				finish();
				break;
			case R.id.butOksearch:
				if(adapter!=null){
					adapter.checkedMap.clear();
				}
	            String strEditText=editSearch.getText().toString();
				if(!"".equals(strEditText)){
					progressDialog = CustomProgressDialog.createDialog(SelectPersonListActivity.this);
					progressDialog.setMessage("正在加载中,请稍后...");
					progressDialog.setCancelable(true);
					progressDialog.setCanceledOnTouchOutside(false);
					progressDialog.show();
					isSearch = true;
					WebServiceNetworkAccess.GetUserListByQuery(strEditText, new Callback() {
						@Override
						public void handle(String result) {
							Message msg = new Message();
							msg.what = 2;
							msg.obj = result;
	//						isRefresh=true;
							handler.sendMessage(msg);
						}
					});
				}else{
					Toast.makeText(SelectPersonListActivity.this, "请输入搜索姓名", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.cancel_search:
				//清空输入框
				if(!isSearch || editSearch.getText().toString().equals("")){
					editSearch.setText("");
					return;
				}
				editSearch.setText("");
				progressDialog = CustomProgressDialog.createDialog(SelectPersonListActivity.this);
				progressDialog.setMessage("正在加载中,请稍后...");
				progressDialog.setCancelable(true);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
				isSearch = false;
				WebServiceNetworkAccess.GetUserGroups(adGuid, exchageId,new Callback() {
					@Override
					public void handle(String result) {
						Message msg = new Message();
						msg.what = 13;
						msg.obj = result;
						handler.sendMessage(msg);
					}
				});
				break;
			case R.id.btn_select:
				 //设置点击下拉箭头图片事件，点击弹出PopupWindow浮动下拉框
				if(flag){
						myapplication.removeAllUser();
					if(adapter!=null){
						adapter.checkedMap.clear();
						adapter.notifyDataSetChanged();
					}
					//显示PopupWindow窗口
					imgSelect.setBackgroundResource(R.drawable.selectup);
					popupWindwShowing();
				}
				break;
			case R.id.LinOk:
				//写邮件选人，判断是否点击确定按钮,false不是点击的确定，true点击确定
				Constants.IsOkSelectPerson=true;
				finish();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
     * 没有在onCreate方法中调用initWedget()，而是在onWindowFocusChanged方法中调用，
     * 是因为initWedget()中需要获取PopupWindow浮动下拉框依附的组件宽度，在onCreate方法中是无法获取到该宽度的
     */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		try {
			while(!flag){
				initWedget();
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 初始化悬浮窗口界面控件
	private void initWedget()throws Exception{
		if(seldataSet!=null){
			//获取下拉框依附的组件宽度
	        int width = LinParent.getWidth();
	        pwidth = width;
	        //初始化PopupWindow
	        initPopuWindow();
		}
	}
	//初始化PopupWindow
    @SuppressWarnings("deprecation")
	private void initPopuWindow()throws Exception{ 
    	//PopupWindow浮动下拉框布局
        View selwindow = (View)this.getLayoutInflater().inflate(R.layout.selectgrouplist, null); 
        SelListView = (ListView) selwindow.findViewById(R.id.selectGroup_list); 
        //设置自定义Adapter
        optionsAdapter = new SelectGroupAdapter(SelectPersonListActivity.this, seldataSet,handler);
        SelListView.setAdapter(optionsAdapter); 
        selectPopupWindow = new PopupWindow(selwindow, pwidth, LayoutParams.MATCH_PARENT, true);
        selectPopupWindow.setOutsideTouchable(true); 
        //这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
        selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());  
        selectPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				
				imgSelect.setBackgroundResource(R.drawable.selectdown);
			}
		});
    } 
	// 显示PopupWindow窗口
    public void popupWindwShowing()throws Exception{ 
       //将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
       //这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
       //（是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
       selectPopupWindow.showAsDropDown(LinParent,0,-3);
    } 
	protected void onDestroy(){
		 super.onDestroy();
		 SysExitUtil.RemoveActivity(this);
	}
}
