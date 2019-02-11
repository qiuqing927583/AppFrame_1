package com.android.yawei.jhoa.mobile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.adapter.CommonGroupAdapter;
import com.android.yawei.jhoa.bean.GroupBean;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.parser.GroupParser;
import com.android.yawei.jhoa.ui.CustomProgressDialog;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.utils.SysExitUtil;
import com.android.yawei.jhoa.utils.XmlUtils;
import com.android.yawei.jhoa.webservice.WebService;
import com.android.yawei.jhoa.webservice.WebService.Callback;
import com.yawei.android.appframework.ui.RefreshLayoutListView;

import java.util.ArrayList;
import java.util.List;

import com.yawei.jhoa.mobile.R;

/*********
 * TODO 设置默认组
 * @author Yusz
 * 创建时间：2015-7-7
 */
public class CommonGroupListActivity extends BaseActivity implements OnClickListener{

	private LinearLayout linBack;
	private RefreshLayoutListView pullRefreshList;//整个页面listview
	private SwipeRefreshLayout swip;//google自带下拉刷新
	private String adGuid;//用户唯一标识
	private String exchageId;//用户交换号
	private List<GroupBean> dataSet;//数据
	private CustomProgressDialog progressDialog;
	private CommonGroupAdapter adapter = null;//适配器
	private LinearLayout linMRZ;//确定
	private MyHandler handler = new MyHandler();
	
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commongrouplist);
		
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.setMessage("正在加载中,请稍后...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		adGuid = SpUtils.getString(getApplicationContext(), Constants.CUTOVER_AD_GUID, "");
		exchageId = SpUtils.getString(getApplicationContext(), Constants.CUTOVER_EXCHANGE_ID, "");
		dataSet = new ArrayList<GroupBean>();
		SysExitUtil.AddActivity(CommonGroupListActivity.this);
		
		try {
			InitView();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		WebServiceNetworkAccess.GetUserGroups(adGuid,exchageId,new WebService.Callback() {
	       @Override
			public void handle(String result) {
	    	   //异步获取组信息（公共组和个人组）
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
		pullRefreshList.onLoadComplete(true);//设置只准许刷新
		swip = (SwipeRefreshLayout)findViewById(R.id.swip_index);
		pullRefreshList.setOnItemClickListener(onItemClickListener);
		swip.setColorSchemeResources( R.color.swiperefresh_color1, R.color.swiperefresh_color2,
	            R.color.swiperefresh_color3, R.color.swiperefresh_color4);
		linMRZ = (LinearLayout)findViewById(R.id.LinMRZ);
		linMRZ.setOnClickListener(this);
		//刷新
		swip.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				WebServiceNetworkAccess.GetUserGroups(adGuid,exchageId,new WebService.Callback() {
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
	//TODO 消息handler
	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
					case 0:
						if(swip.isShown()){
							swip.setRefreshing(false);
						}
						if (msg.obj != null&& !msg.obj.equals("anyType") && !msg.obj.toString().equals("")&& !msg.obj.toString().equals("<root />")) {
							List<GroupBean> bean = setInboxItem((String) msg.obj);
							dataSet.clear();//该页面只有刷新没有加载更多，所以每次刷新或者是第一次都是先清空在加载
							dataSet.addAll(bean);

							if(adapter == null){
								adapter = new CommonGroupAdapter(CommonGroupListActivity.this, dataSet);
								pullRefreshList.setAdapter(adapter);
							}else{
								adapter.Updata(dataSet);
								adapter.notifyDataSetChanged();
							}

						}else if(msg.obj.toString().equals("")||msg.obj.toString().equals("<root />")){
							if(progressDialog.isShowing()){
								progressDialog.dismiss();
							}
							Toast.makeText(CommonGroupListActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
						}else{
							if(progressDialog.isShowing()){
								progressDialog.dismiss();
							}
							Toast.makeText(CommonGroupListActivity.this, getResources().getString(R.string.abnormal), Toast.LENGTH_SHORT).show();
						}
						if(progressDialog != null && progressDialog.isShowing())
							progressDialog.dismiss();
						break;
					case 1:
						if(progressDialog != null && progressDialog.isShowing())
							progressDialog.dismiss();

						Toast.makeText(CommonGroupListActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.handleMessage(msg);
		}
	}
	private List<GroupBean> setInboxItem(String result)throws Exception{

		if(result == null || "".equals(result) || "anyType".equalsIgnoreCase(result)){
			return null;
		}
		List<GroupBean> bean = new ArrayList<GroupBean>();
		GroupParser gp = new GroupParser(bean);
		XmlUtils.saxParse(result, gp);
		return bean;
	}
	
	//確定
	private void Definite()throws Exception{
		String strDefaultGroupGuid="";
		int addCount=0;
		if(adapter!=null&&adapter.checkedMap!=null){
			for (int i = 0; i < adapter.checkedMap.size(); i++) {
				if (adapter.checkedMap.get(i)!=-11) {
					strDefaultGroupGuid=adapter.getItem(adapter.checkedMap.get(i)).getGuid();
					addCount++;
				}
			 }
		}
		if(addCount==0){
			Toast.makeText(CommonGroupListActivity.this, "请选择默认组", Toast.LENGTH_SHORT).show();
		}else if(addCount==1){
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage("正在设置中,请稍后...");
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			 WebServiceNetworkAccess.AddDefaultCommonGroup(adGuid,strDefaultGroupGuid,exchageId,new Callback() {
				@Override
				public void handle(String result) {
					if(result != null && "1".equals(result)){
						Message msg = new Message();
						msg.what = 1;
						msg.obj = "设置成功";
						handler.sendMessage(msg);
					}else{
						Message msg = new Message();
						msg.what = 1;
						msg.obj = "设置失败";
						handler.sendMessage(msg);
					}
				}
			});
		}else{
			Toast.makeText(CommonGroupListActivity.this, "默认组只能设置一个", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.LinTopBack:
				finish();
				break;
			case R.id.LinMRZ:
				//设置默认组确定
				Definite();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void onDestroy(){
		super.onDestroy();
		SysExitUtil.RemoveActivity(CommonGroupListActivity.this);
	}
}
