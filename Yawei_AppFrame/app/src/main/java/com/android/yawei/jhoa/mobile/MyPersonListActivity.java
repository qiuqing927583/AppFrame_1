package com.android.yawei.jhoa.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.adapter.MyPersonAdapter;
import com.android.yawei.jhoa.addressbook.GroupFragmentActivity;
import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.parser.GPersonParser;
import com.android.yawei.jhoa.ui.CustomProgressDialog;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.utils.SysExitUtil;
import com.android.yawei.jhoa.utils.XmlUtils;
import com.android.yawei.jhoa.webservice.WebService.Callback;
import com.yawei.android.appframework.ui.RefreshLayoutListView;
import com.yawei.android.appframework.ui.RefreshLayoutListView.OnLoadMore;

import java.util.ArrayList;
import java.util.List;

import com.yawei.jhoa.mobile.R;

/************************
 * TODO 功能：联系人自定组，添加人
 * @author Yusz
 * 创建时间：2015-7-8
 */
public class MyPersonListActivity extends BaseActivity implements OnClickListener{

	private RefreshLayoutListView pullRefreshList;//整个页面listview
	private SwipeRefreshLayout swip;//google自带下拉刷新
	private LinearLayout linBack;
	private LinearLayout LinAdd;//添加
	private LinearLayout LinDelete;//删除
	private String exchageId;//用户唯一标示
	private String gguid;
	private String gtype;
	private int pageNow =1;    //当前页
	private List<User> listData;//获取的数据
	private MyPersonAdapter adapter = null;
	public static MyPersonListActivity Activity_EntityActivity = null;
	private  CustomProgressDialog progressDialog;

	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypersonlist);

		progressDialog = CustomProgressDialog.createDialog(MyPersonListActivity.this);
		progressDialog.setMessage("正在加载中,请稍后...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();

		exchageId = SpUtils.getString(getApplicationContext(), Constants.CUTOVER_EXCHANGE_ID, "");
		gguid = getIntent().getStringExtra("gguid");
		gtype = getIntent().getStringExtra("gtype");
		Activity_EntityActivity = MyPersonListActivity.this;
		listData = new ArrayList<User>();
		SysExitUtil.AddActivity(MyPersonListActivity.this);

		try {
			InitView();
		} catch (Exception e) {
			e.printStackTrace();
		}

		WebServiceNetworkAccess.GetUsersInGroupByPageNo(gguid, exchageId, gtype, String.valueOf(pageNow),new Callback() {
	       @Override
			public void handle(String result) {
				Message msg = new Message();
				msg.what = 0;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		});
	}

	private void InitView()throws Exception{
		linBack = (LinearLayout)findViewById(R.id.LinTopBack);
		linBack.setOnClickListener(this);
		pullRefreshList = (RefreshLayoutListView)findViewById(R.id.listView);
		swip = (SwipeRefreshLayout)findViewById(R.id.swip_index);
		LinAdd=(LinearLayout)findViewById(R.id.LinAdd);
		LinAdd.setOnClickListener(this);
		LinDelete=(LinearLayout)findViewById(R.id.LinDelete);
		LinDelete.setOnClickListener(this);
		pullRefreshList.setOnItemClickListener(onItemClickListener);
//		pullRefreshList.setOnItemLongClickListener(onItemLongClick);
		swip.setColorSchemeResources( R.color.swiperefresh_color1, R.color.swiperefresh_color2,
	            R.color.swiperefresh_color3, R.color.swiperefresh_color4);
		//刷新
		swip.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				//下拉拉刷新
				pageNow=1;
				WebServiceNetworkAccess.GetUsersInGroupByPageNo(gguid, exchageId, gtype, String.valueOf(pageNow),new Callback() {
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
		//加载更多
		pullRefreshList.setLoadMoreListen(new OnLoadMore() {
			@Override
			public void loadMore() {
				// 加载更多
				pageNow++;
				WebServiceNetworkAccess.GetUsersInGroupByPageNo(gguid, exchageId, gtype, String.valueOf(pageNow),new Callback() {
			       @Override
					public void handle(String result) {
						Message msg = new Message();
						msg.what = 0;
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
			//TODO 消息handler
			try {
				switch (msg.what) {
				case 0:
					if(progressDialog != null && progressDialog.isShowing()){
						progressDialog.dismiss();
					}
					if (msg.obj != null&& !msg.obj.equals("anyType") && !msg.obj.toString().equals("")&& !msg.obj.toString().equals("<root />")) {
						List<User> bean = setInboxItem((String) msg.obj);
						if(bean.size()>=20){//判断获取的数据是否是15条，如果不满15条不可以下拉
							pullRefreshList.onLoadComplete(false);//设置既可以上拉也可以下拉
						}else{
							pullRefreshList.onLoadComplete(true);//设置只准许刷新
						}

						listData.addAll(bean);
						if(adapter ==  null){
							adapter = new MyPersonAdapter(MyPersonListActivity.this, listData);
							pullRefreshList.setAdapter(adapter);
						}else{
							adapter.UpData(listData);
							adapter.notifyDataSetChanged();
						}

					}else if(msg.obj != null && (msg.obj.toString().equals("")|| msg.obj.toString().equals("<root />"))){
						if(progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						if(progressDialog != null && progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						if(listData.size()==0){
							pullRefreshList.onLoadComplete(true);//设置准许刷新
							Toast.makeText(MyPersonListActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
						}else{
							pullRefreshList.onLoadComplete(true);//设置准许刷新
							Toast.makeText(MyPersonListActivity.this, "没有更多的数据", Toast.LENGTH_SHORT).show();
						}
						Toast.makeText(MyPersonListActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
					}else{
						if(progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						Toast.makeText(MyPersonListActivity.this, getResources().getString(R.string.abnormal), Toast.LENGTH_SHORT).show();
					}
					if(progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
					break;
				case 1:
					if(swip.isShown()){
						swip.setRefreshing(false);
					}
					if (msg.obj != null&& !msg.obj.equals("anyType") && !msg.obj.toString().equals("")&& !msg.obj.toString().equals("<root />")) {
						List<User> bean = setInboxItem((String) msg.obj);
						if(bean.size()>=20){//判断获取的数据是否是15条，如果不满15条不可以下拉
							pullRefreshList.onLoadComplete(false);//设置既可以上拉也可以下拉
						}else{
							pullRefreshList.onLoadComplete(true);//设置只准许刷新
						}
						listData.clear();
						listData.addAll(bean);
						if(adapter ==  null){
							adapter = new MyPersonAdapter(MyPersonListActivity.this, listData);
							pullRefreshList.setAdapter(adapter);
						}else{
							adapter.UpData(listData);
							adapter.notifyDataSetChanged();
						}

					}else if(msg.obj != null && (msg.obj.toString().equals("")|| msg.obj.toString().equals("<root />"))){
						if(progressDialog != null && progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						if(msg.obj.toString().equals("")|| msg.obj.toString().equals("<root />")){
							listData.clear();
							if(adapter == null){
								adapter = new MyPersonAdapter(MyPersonListActivity.this, listData);
								pullRefreshList.setAdapter(adapter);
							}else{
								adapter.UpData(listData);
								adapter.notifyDataSetChanged();
							}
						}
						if(listData.size()==0){
							pullRefreshList.onLoadComplete(true);//设置准许刷新
							Toast.makeText(MyPersonListActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
						}else{
							pullRefreshList.onLoadComplete(true);//设置准许刷新
						}

					}else{
						if(progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						Toast.makeText(MyPersonListActivity.this, getResources().getString(R.string.abnormal), Toast.LENGTH_SHORT).show();
					}
					if(progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
					break;
				case 11:
					ShowUserDialog((String)msg.obj);
					break;
				case 2:
					//删除组成员
					if(progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
					String strDelUser = (String) msg.obj;
					if(adapter != null){//刷新清空所有数据重新加载
						adapter.checkedMap.clear();
					}
					if(strDelUser != null && "1".equals(strDelUser)){
						GetNewData();
					}else{
						Toast.makeText(MyPersonListActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
					}
					break;
				case 3:
					//添加人到个人组中
					if(progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
					String strAddUser = (String)msg.obj;
					if(strAddUser != null && "1".equals(strAddUser)){//如果是1说明添加成功重新刷新数据
						MyApplication application = (MyApplication) getApplication();
						application.removeAllUser();      //清除已选用户缓存
					    Toast.makeText(MyPersonListActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
					    GetNewData();
					}else{
						Toast.makeText(MyPersonListActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
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

	private OnItemClickListener onItemClickListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, final View view, final int position, long id){
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
		}
	};
	//显示人员信息
	private void ShowUserDialog(String title){
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.activity_dialog,
        (ViewGroup) findViewById(R.id.dialog));
	    TextView dialogtitle=(TextView)layout.findViewById(R.id.dialogtitle);
	    dialogtitle.setText("用户详情");
	    TextView dialogtip=(TextView)layout.findViewById(R.id.dialogtip);
	    dialogtip.setText(title);
	    AlertDialog  alertDialog = new AlertDialog.Builder(MyPersonListActivity.this)
	    .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	 dialog.dismiss();
            }
        }).create();
	    alertDialog.setView(layout,-1,-1,-1,-1);
		alertDialog.show();
	}
	//删除成员重新获取数据
	private void GetNewData(){
		pageNow=1;
		progressDialog = CustomProgressDialog.createDialog(MyPersonListActivity.this);
		progressDialog.setMessage("正在加载中,请稍后...");
		progressDialog.setCancelable(true);
		progressDialog.show();
		if(gguid!=null&&!gguid.equals("")){
		  WebServiceNetworkAccess.GetUsersInGroupByPageNo(gguid, exchageId, gtype, pageNow+"", new Callback() {
			@Override
			public void handle(String result) {
				Message msg_ = new Message();
				msg_.what = 1;
				msg_.obj = result;
				handler.sendMessage(msg_);
			}
		  });
		}else{
			if(adapter!=null){
				adapter.clear();
				adapter.notifyDataSetChanged();
			}
			if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}
		}
	}
	//自定义组删除用户
	private void DelUser(){
		String strUserGuid ="";
		if(gtype.equals("2")){
			if(adapter!=null&&adapter.checkedMap!=null){

				for (int i = 0; i < adapter.checkedMap.size(); i++) {
					if (adapter.checkedMap.get(i)!=-11) {
						strUserGuid+=adapter.getItem(adapter.checkedMap.get(i)).getAdGuid()+";";
					}
				 }
			}

			if(strUserGuid!=null&&!strUserGuid.equals("")){
			    final String DelUserGuid=strUserGuid.substring(0,strUserGuid.length()-1);
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.activity_dialog,
		        (ViewGroup) findViewById(R.id.dialog));
			    TextView dialogtitle=(TextView)layout.findViewById(R.id.dialogtitle);
			    dialogtitle.setText("删除用户");
			    TextView dialogtip=(TextView)layout.findViewById(R.id.dialogtip);
			    dialogtip.setText("确认删除吗？");
			    AlertDialog  alertDialog = new AlertDialog.Builder(MyPersonListActivity.this)
			    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {

	                	progressDialog = CustomProgressDialog.createDialog(MyPersonListActivity.this);
	            		progressDialog.setMessage("正在加载中,请稍后...");
	            		progressDialog.setCancelable(true);
	            		progressDialog.setCanceledOnTouchOutside(false);
	            		progressDialog.show();

		        	    String userXml="<root>";
						userXml+="<userguid>"+ SpUtils.getString(getApplicationContext(), Constants.CUTOVER_AD_GUID, "")+"</userguid>";
						userXml+="<username>"+SpUtils.getString(getApplicationContext(), Constants.CUTOVER_DISPLAY_NAME, "")+"</username>";
						userXml+="<devicename>"+ Constants.mobileName+"</devicename>";
						userXml+="<title>"+DelUserGuid+"</title>";
						userXml+="</root>";
		                WebServiceNetworkAccess.DeletePersonsInPersonalGroup(DelUserGuid,gguid,exchageId,userXml,new Callback() {

							@Override
							public void handle(String result) {
								Message msg = new Message();
								msg.what = 2;
								msg.obj = result;
								handler.sendMessage(msg);
							}
						});

		                dialog.dismiss();
	                }
	            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                	 dialog.dismiss();
	                }
	            }).create();
			    alertDialog.setView(layout,-1,-1,-1,-1);
				alertDialog.show();
			}else{
				Toast.makeText(MyPersonListActivity.this, "请选择用户", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(MyPersonListActivity.this, "只有自定组的用户才可以删除", Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * 解析返回的结果
	 * @param xml
	 */
	private List<User> setInboxItem(String xml){
		if(xml == null || "".equals(xml) || "anyType".equalsIgnoreCase(xml)){
			return null;
		}

		List<User> bean = new ArrayList<User>();

		GPersonParser gp = new GPersonParser(bean);
		try{
			XmlUtils.saxParse(xml, gp);
		}catch (Exception e){
			e.printStackTrace();
		}

		return bean;
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.LinTopBack:
				finish();
				break;
			case R.id.LinAdd:
				//添加
				MyApplication application = (MyApplication) getApplication();
				application.removeAllUser();
//				Intent intent = new Intent(MyPersonListActivity.this, SelectPersonListActivity.class);
//				startActivity(intent);
				Intent intent = new Intent(MyPersonListActivity.this, GroupFragmentActivity.class);
				intent.putExtra("sign","4");
				startActivity(intent);
				break;
			case R.id.LinDelete:
				//删除
				DelUser();
				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		try {
			if(Constants.IsOkSelectPerson){
				//选择添加人
				Constants.IsOkSelectPerson=false;
				MyApplication application = (MyApplication) getApplication();
				//从缓存中获取以选中收件人
				String postInfo="";
				for (User u : application.getSelectedUsers()) {
					postInfo +="<item adGuid=\""+u.getAdGuid()+"\" displayName=\""+u.getDisplayName()+"\"";
				    postInfo +=" userpath=\""+u.getPath()+"\"  usertype=\""+u.getSysflag()+"\"";
				    postInfo +=" exchangeid=\""+u.getExchangeID()+"\" creator=\""+SpUtils.getString(getApplicationContext(), Constants.CUTOVER_DISPLAY_NAME, "")+"\"";
				    postInfo +=" creatorguid=\""+SpUtils.getString(getApplicationContext(), Constants.CUTOVER_AD_GUID, "")+"\"/>";
				}
				//将选中的人添加到组中
				if(!"".equals(postInfo)){
					progressDialog = CustomProgressDialog.createDialog(MyPersonListActivity.this);
					progressDialog.setMessage("正在加载中,请稍后...");
					progressDialog.setCancelable(true);
					progressDialog.setCanceledOnTouchOutside(false);
					progressDialog.show();
					WebServiceNetworkAccess.AddPersonalGroupPerson("<root>"+postInfo+"</root>", gguid, exchageId,new Callback() {

						@Override
						public void handle(String result) {
							// TODO 添加人到个人组用户
							Message msg = new Message();
							msg.what = 3;
							msg.obj = result;
							handler.sendMessage(msg);
						}
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void onDestroy(){
		super.onDestroy();
		SysExitUtil.RemoveActivity(MyPersonListActivity.this);
	}
}
