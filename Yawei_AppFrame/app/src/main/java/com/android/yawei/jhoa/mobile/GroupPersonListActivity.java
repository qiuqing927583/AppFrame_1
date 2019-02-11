package com.android.yawei.jhoa.mobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import com.android.yawei.jhoa.adapter.GroupPersonAdapter;
import com.android.yawei.jhoa.adapter.SelectGroupAdapter;
import com.android.yawei.jhoa.bean.GetUserPhoneBean;
import com.android.yawei.jhoa.bean.GroupBean;
import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.factory.ResolveXML;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.parser.GetUserPhoneParser;
import com.android.yawei.jhoa.ui.CustomProgressDialog;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.utils.SysExitUtil;
import com.android.yawei.jhoa.utils.XmlUtils;
import com.android.yawei.jhoa.webservice.WebService;
import com.android.yawei.jhoa.webservice.WebService.Callback;
import com.yawei.android.appframework.ui.RefreshLayoutListView;
import com.yawei.android.appframework.ui.RefreshLayoutListView.OnLoadMore;
import com.yawei.jhoa.mobile.R;

import java.util.ArrayList;
import java.util.List;

/**************
 * 
 * TODO 联系人
 * @author Yusz
 *
 */
public class GroupPersonListActivity extends BaseActivity implements OnClickListener{

	private LinearLayout linBack;//返回
	private LinearLayout linXYJ;//写便签
	private LinearLayout linXGJ;//相关件
	private LinearLayout linSMRZ;//设默认组
	private LinearLayout lintopGroup;//自定组
	private LinearLayout linParent;////下拉框依附的view
	private String adGuid;//用户唯一标示
	private String exchageId;//用户交换号
	private String gname;//组名称
	private String gguid;//组唯一标识
	private String gtype;//
	private String parameter;//
	private List<GroupBean> seldataSet; //下拉框组选项数据源
	private TextView group_tip;//组名
	private EditText editSearch;//搜索输入框
	private ImageButton cancelSearch;//清除搜索输入框
	private Button butSearch;//搜索按钮
	private CustomProgressDialog progressDialog;//加载提示
	private int pageNow =1;//listview翻页的页数
	private SwipeRefreshLayout swip;//google自带下拉刷新
	private RefreshLayoutListView pullRefreshList;//页面加载人员listview
	private  List<User> dataSet;//组成员
	private boolean isSearch = false;//判断是否是搜索状态
	private ImageView imgSelect;//选组按钮
	private GroupPersonAdapter adapter = null;//组员适配器
	private boolean flag = false;//是否初始化完成标志  
	private ListView SelListView = null; //展示所有下拉选项的ListView
	private int pwidth; //下拉框依附组件宽度，也将作为下拉框的宽度
	private SelectGroupAdapter optionsAdapter = null;//组适配器
	private PopupWindow selectPopupWindow= null;//PopupWindow,悬浮窗口对象
	public static GroupPersonListActivity Activity_EntityActivity = null;
	private MyApplication application;//全局变量
	private User strUser;//点击查看用户详细信息

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouppersonlist);
		try {
			progressDialog = CustomProgressDialog.createDialog(GroupPersonListActivity.this);
			progressDialog.setMessage("正在加载中,请稍后...");
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			
			adGuid = SpUtils.getString(getApplicationContext(), Constants.CUTOVER_AD_GUID, "");
			exchageId = SpUtils.getString(getApplicationContext(), Constants.CUTOVER_EXCHANGE_ID, "");
			dataSet = new ArrayList<User>();
			application = (MyApplication) getApplication();
			Activity_EntityActivity = GroupPersonListActivity.this;
			SysExitUtil.AddActivity(GroupPersonListActivity.this);
			
			InitView();
			//通知主界面，跳转到待办
//			if(MainActivity.mainAct != null){
//				MainActivity.mainAct.handler.sendEmptyMessageDelayed(0, 500);
//			}

			WebServiceNetworkAccess.GetUserGroups(adGuid, exchageId,new WebService.Callback() {
				@Override
				public void handle(String result) {
					// TODO 异步获取组信息（公共组和个人组）
					Message msg_ = new Message();
					msg_.what = 13;
					msg_.obj = result;
					handler.sendMessage(msg_);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//初始化UI
	private void InitView() throws Exception{
		linBack = (LinearLayout)findViewById(R.id.LinTopBack);
		linBack.setOnClickListener(this);
		linXYJ = (LinearLayout)findViewById(R.id.LinXYJ);
		linXYJ.setOnClickListener(this);
		linXGJ = (LinearLayout)findViewById(R.id.LinXGJ);
		linXGJ.setOnClickListener(this);
		linSMRZ = (LinearLayout)findViewById(R.id.LinSMRZ);
		linSMRZ.setOnClickListener(this);
		lintopGroup = (LinearLayout)findViewById(R.id.LintopGroup);
		lintopGroup.setOnClickListener(this);
		group_tip = (TextView)findViewById(R.id.group_tip);
		editSearch = (EditText)findViewById(R.id.et_search);
		cancelSearch = (ImageButton)findViewById(R.id.cancel_search);
		cancelSearch.setOnClickListener(this);
		butSearch = (Button)findViewById(R.id.butOksearch);
		butSearch.setOnClickListener(this);
		imgSelect = (ImageView)findViewById(R.id.btn_select);
		imgSelect.setOnClickListener(this);
		linParent = (LinearLayout)findViewById(R.id.LinParent);
		swip = (SwipeRefreshLayout)findViewById(R.id.swip_index);
		pullRefreshList = (RefreshLayoutListView)findViewById(R.id.listView);
		pullRefreshList.setOnItemClickListener(onItemClickListener);

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
				WebServiceNetworkAccess.GetUsersInGroupByPageNo(parameter, exchageId, gtype, pageNow+"", new Callback() {
					@Override
					public void handle(String result) {
						Message msg = new Message();
						msg.what = 1;
						msg.obj = result;
						handler.sendMessage(msg);
					}
				});
			}
		});
	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case 1:
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
							Toast.makeText(GroupPersonListActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
						}
					}else{
						if(progressDialog != null && progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						pullRefreshList.onLoadComplete(true);//设置准许刷新
						Toast.makeText(GroupPersonListActivity.this, getResources().getString(R.string.abnormal), Toast.LENGTH_SHORT).show();
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
					}
					else if(msg.obj != null && (msg.obj.toString().equals("")|| msg.obj.toString().equals("<root />"))){
						if(progressDialog != null && progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						if(adapter!=null){
						  adapter.clear();
						  adapter.notifyDataSetChanged();
						}
						pullRefreshList.onLoadComplete(true);//设置准许刷新
						Toast.makeText(GroupPersonListActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
					}else{
						if(progressDialog != null && progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						pullRefreshList.onLoadComplete(true);//设置准许刷新
						Toast.makeText(GroupPersonListActivity.this, getResources().getString(R.string.abnormal), Toast.LENGTH_SHORT).show();
					}
					break;
				case 3:
					//获取用户手机号
					if(progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
					String strUserPhone = (String) msg.obj;
					GetUserPhoneBean bean = null;
					if(strUserPhone != null && !strUserPhone.equals("anyType")&&!strUserPhone.equals("")){
						bean = ParseXMl(strUserPhone);
					}
					DisplayInformation(strUser,bean);
					break;
				case 10:
					//选中某一组，获取改组下成员
					pageNow=1;
					editSearch.setText("");
					if(seldataSet!=null){
						progressDialog = CustomProgressDialog.createDialog(GroupPersonListActivity.this);
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
						WebServiceNetworkAccess.GetUsersInGroupByPageNo(parameter, exchageId, gtype, pageNow+"", new WebService.Callback() {
							@Override
							public void handle(String result) {
								Message msg_ = new Message();
								msg_.what = 2;
								msg_.obj = result;
								handler.sendMessage(msg_);

							}
						});
					}
					pullRefreshList.setSelection(0);
					selectPopupWindow.dismiss();
					break;
				case 11:
					//用户信息
					Bundle bundle = msg.getData();
					strUser = (User) bundle.get("user");
					GetUserPhone(strUser);
					break;
				case 13:
					//初始获取组数据
					String retXml=(String)msg.obj;
				    if (retXml != null&& !retXml.equals("anyType") && !retXml.equals("")&& !retXml.equals("<root />")) {
			        	seldataSet = ResolveXML.parseGroup(retXml);//解析组
			        	if(seldataSet!=null){
			    	    	gname = seldataSet.get(0).getName();
			    			gguid = seldataSet.get(0).getGuid();
			    			gtype = seldataSet.get(0).getType();

			    			if(gname!=null&&!"".equals(gname)){
			    				group_tip.setText(gname);
			    			}
			    			//判断是否是公共组，不是去guid获取组成员
			    			if(gtype!=null && gtype.equals("1")){//公共組
			    				parameter=gname;
			    			}else{
			    				parameter=gguid;
			    			}
			    			//获取组成员
			    			WebServiceNetworkAccess.GetUsersInGroupByPageNo(parameter, exchageId, gtype, pageNow+"",new WebService.Callback() {
						       @Override
								public void handle(String result) {
									Message msg = new Message();
									msg.what = 2;
									msg.obj = result;
									handler.sendMessage(msg);
								}
							});
			    		}else{
			    			if(progressDialog != null && progressDialog.isShowing()){
			    				progressDialog.dismiss();
			    			}
			    		}
					}else if( retXml != null && (retXml.equals("")|| retXml.equals("<root />"))){
						if(progressDialog != null && progressDialog.isShowing()){
		    				progressDialog.dismiss();
		    			}
						Toast.makeText(GroupPersonListActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
					}else{
						if(progressDialog != null && progressDialog.isShowing()){
		    				progressDialog.dismiss();
		    			}
						Toast.makeText(GroupPersonListActivity.this, getResources().getString(R.string.abnormal), Toast.LENGTH_SHORT).show();
					}
					break;
				case 14:
					//发短信
					String smsPhoneNum = (String) msg.obj;
					Uri smsToUri = Uri.parse("smsto:"+smsPhoneNum);
					Intent intentsms = new Intent(Intent.ACTION_SENDTO, smsToUri);
					intentsms.setAction(Intent.ACTION_SENDTO);
					intentsms.putExtra("sms_body", "");//默认发送的内容
					startActivity(intentsms);
					break;
				case 15:
					//打电话
					String strCellPhoneNum = (String) msg.obj;
					Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+strCellPhoneNum));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
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

	private OnItemClickListener onItemClickListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, final View view, final int position, long id){
			try {
				ImageView okImg=(ImageView)view.findViewById(R.id.okImage);
				TextView isCheck=(TextView)view.findViewById(R.id.ischeck);
				String strCheck=isCheck.getText()+"";
				if(strCheck.equals("true")){
					 okImg.setImageResource(R.drawable.del);
				     adapter.checkedMap.put(position,-11);
				     isCheck.setText("false");
				}else{
					 okImg.setImageResource(R.drawable.add);
					 adapter.checkedMap.put(position,position);
					 isCheck.setText("true");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

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
	/**
	 * 初始化界面控件
	 */
	private void initWedget()throws Exception{
		try {
			if(seldataSet!=null){
				//获取下拉框依附的组件宽度
		        int width = linParent.getWidth();
		        pwidth = width;
		        //设置点击下拉箭头图片事件，点击弹出PopupWindow浮动下拉框
		        imgSelect.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(flag){
							try {
								//显示PopupWindow窗口
								imgSelect.setBackgroundResource(R.drawable.selectup);
								popupWindwShowing();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
		        //初始化PopupWindow
		        initPopuWindow();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 /**
     * 初始化PopupWindow
     */
    @SuppressWarnings("deprecation")
	private void initPopuWindow() throws Exception{

    	//PopupWindow浮动下拉框布局
        View selwindow = (View)this.getLayoutInflater().inflate(R.layout.selectgrouplist, null);
        SelListView = (ListView) selwindow.findViewById(R.id.selectGroup_list);

        //设置自定义Adapter
        optionsAdapter = new SelectGroupAdapter(this, seldataSet,handler);
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
	/**
     * 显示PopupWindow窗口
     *
     */
    public void popupWindwShowing() throws Exception{
       //将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
       //这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
       //（是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
    	if(selectPopupWindow != null){
    		selectPopupWindow.showAsDropDown(linParent,0,-10);
    	}
    }
	 // 解析组成员
	private void setInboxItem(String result) throws Exception{
		try {
			String xml = null;
			if (result == null || !(result instanceof String)){
				xml = "";
			}else{
				xml = (String) result;
			}
			List<User> data = ResolveXML.parseGPerson(xml);
			int datasize=0;
			if(dataSet!=null){
				datasize=data.size();
			}
			if(datasize<Integer.parseInt(Constants.PAGE_SIZE)){
				pullRefreshList.onLoadComplete(true);
			}else{
				pullRefreshList.onLoadComplete(false);
			}
			dataSet.addAll(data);
			//设定初始化组人员的适配器
			if (datasize != 0){
				if (adapter == null){
					adapter = new GroupPersonAdapter(GroupPersonListActivity.this,dataSet);
					pullRefreshList.setAdapter(adapter);
				}else {
					adapter.UpData(dataSet);
					adapter.notifyDataSetChanged();
				}
			}
		  if(progressDialog != null && progressDialog.isShowing()){
				progressDialog.dismiss();
		  }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	//显示用户信息
	private void DisplayInformation(User Information, GetUserPhoneBean bean) throws Exception{
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog_personmore,(ViewGroup) findViewById(R.id.dialog));
	    TextView dialogtitle=(TextView)layout.findViewById(R.id.dialogtitle);
	    dialogtitle.setText("用户详情");
	    final User userMsg = Information;

	    TextView dialogtip=(TextView)layout.findViewById(R.id.dialogtip);
	    dialogtip.setText(userMsg.getPath()+"");

	    final TextView phonenum = (TextView)layout.findViewById(R.id.phonenum);

	    if(bean != null && bean.getUsertel() != null){
	    	phonenum.setText(bean.getUsertel());
	    }
	    LinearLayout linsmsmessage = (LinearLayout)layout.findViewById(R.id.linsmsmessage);
	    linsmsmessage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!phonenum.getText().toString().equals("")){
					Message msg =  new Message();
					msg.obj = phonenum.getText().toString();
					msg.what = 14;
					handler.sendMessage(msg);
				}else{
					Toast.makeText(GroupPersonListActivity.this, "联系人电话为空不能发短信！", Toast.LENGTH_SHORT).show();
				}

			}
		});
	    LinearLayout lincell = (LinearLayout)layout.findViewById(R.id.lincell);
	    lincell.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!phonenum.getText().toString().equals("")){
					Message msg =  new Message();
					msg.obj = phonenum.getText().toString();
					msg.what = 15;
					handler.sendMessage(msg);
				}else{
					Toast.makeText(GroupPersonListActivity.this, "联系人电话为空不能打电话！", Toast.LENGTH_SHORT).show();
				}
			}
		});
	    LinearLayout linwriteE = (LinearLayout)layout.findViewById(R.id.linwriteE);
	    linwriteE.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
//					application.addUser(userMsg);
//					Intent intent = new Intent(GroupPersonListActivity.this, WriteEmailActivity.class);
//					intent.putExtra("IsSelPerson", true);
//					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	    AlertDialog  alertDialog = new AlertDialog.Builder(GroupPersonListActivity.this).create();
	    alertDialog.setCanceledOnTouchOutside(true);
	    alertDialog.setView(layout,-1,-1,-1,-1);
		alertDialog.show();
	}
	@Override
	public void onClick(View v) {
		Intent intent = null;
		try {
			switch (v.getId()) {
			case R.id.LinTopBack:
				finish();
				break;
			case R.id.LinXYJ:
				try {
					//写便签
					//先清除信息，在缓存防止意外发生
					application.removeAllUser();
					User u = null;
					if(adapter!=null && adapter.checkedMap!=null){
					for (int i = 0; i < adapter.checkedMap.size(); i++) {
						if (adapter.checkedMap.get(i)!=-11) {
							u = new User();
							u.setDisplayName(adapter.getItem(adapter.checkedMap.get(i)).getDisplayName());
							u.setAdGuid(adapter.getItem(adapter.checkedMap.get(i)).getAdGuid());
							u.setSysflag(adapter.getItem(adapter.checkedMap.get(i)).getSysflag());
							u.setExchangeID(adapter.getItem(adapter.checkedMap.get(i)).getExchangeID());
							application.addUser(u);
						}
					 }
					}
//					intent = new Intent(GroupPersonListActivity.this, WriteEmailActivity.class);
//					intent.putExtra("IsSelPerson", true);
//					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.LinXGJ:
				//相关件
				try {
					if(adapter!=null&&adapter.checkedMap!=null){
						int y=0;
						int checkPosition=0;
						for (int i = 0; i < adapter.checkedMap.size(); i++) {
							if (adapter.checkedMap.get(i)!=-11) {
							 y++;
							 checkPosition=adapter.checkedMap.get(i);
							}
						 }
						if(y==0){
							Toast.makeText(GroupPersonListActivity.this, "请选择用户", Toast.LENGTH_SHORT).show();
						}else if(y>1){
							Toast.makeText(GroupPersonListActivity.this, "查看该用户相关件只能选中一个用户", Toast.LENGTH_SHORT).show();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.LinSMRZ:
				//设默认组
				intent=new Intent(GroupPersonListActivity.this,CommonGroupListActivity.class);
				startActivity(intent);
				break;
			case R.id.LintopGroup:
				//自定组
				intent=new Intent(GroupPersonListActivity.this,PersonGroupListActivity.class);
				startActivity(intent);
				break;
			case R.id.cancel_search:
				//清空搜索框
				if(!isSearch || editSearch.getText().toString().equals("")){
					editSearch.setText("");
					return;
				}
				editSearch.setText("");
				progressDialog = CustomProgressDialog.createDialog(GroupPersonListActivity.this);
				progressDialog.setMessage("正在加载中,请稍后...");
				progressDialog.setCancelable(true);
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
			case R.id.butOksearch:
				//点击搜索按钮
				try {
					if(adapter!=null){
						adapter.checkedMap.clear();
					}
		            String strEditText=editSearch.getText().toString();
					if(!"".equals(strEditText)){
						progressDialog = CustomProgressDialog.createDialog(GroupPersonListActivity.this);
						progressDialog.setMessage("正在加载中,请稍后...");
						progressDialog.setCancelable(true);
						progressDialog.show();
						isSearch = true;
						WebServiceNetworkAccess.GetUserListByQuery(strEditText, new WebService.Callback() {
							@Override
							public void handle(String result) {
								Message msg = new Message();
								msg.what = 2;
								msg.obj = result;
								handler.sendMessage(msg);
							}
						});
					}else{
						Toast.makeText(GroupPersonListActivity.this, "请输入搜索姓名", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.btn_select:
				//选组控件
				try {
					if(flag){
						//显示PopupWindow窗口
						imgSelect.setBackgroundResource(R.drawable.selectup);
						popupWindwShowing();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void GetUserPhone(User user)throws Exception{
		progressDialog = CustomProgressDialog.createDialog(GroupPersonListActivity.this);
		progressDialog.setMessage("正在加载中,请稍后...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		WebServiceNetworkAccess.GetUserInfoByGuid(user.getAdGuid(), new Callback() {
			@Override
			public void handle(String result) {
				Message msg = new Message();
				msg.what = 3;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		});
	}

	//解析手机号列表数据
	public GetUserPhoneBean ParseXMl(String xml)throws Exception{
		if (xml == null){
			throw new NullPointerException();
		}
		GetUserPhoneBean dataSet = new GetUserPhoneBean();
		GetUserPhoneParser bp = new GetUserPhoneParser(dataSet);
		try{
			XmlUtils.saxParse(xml, bp);
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}
	
	public void onDestroy(){
		super.onDestroy();
		SysExitUtil.RemoveActivity(GroupPersonListActivity.this);
	}
}
