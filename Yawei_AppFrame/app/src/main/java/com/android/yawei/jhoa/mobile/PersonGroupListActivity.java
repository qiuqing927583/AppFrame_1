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
import com.android.yawei.jhoa.adapter.PersonGroupAdapter;
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
import com.yawei.android.appframework.network.NetWorkUtil;
import com.yawei.android.appframework.ui.RefreshLayoutListView;

import java.util.ArrayList;
import java.util.List;

import com.yawei.jhoa.mobile.R;

/******************
 * TODO 功能：自定组
 * @author Yusz
 * 创建时间：2017-7-7
 */
public class PersonGroupListActivity extends BaseActivity implements OnClickListener{

	private LinearLayout linBack;//返回
	private LinearLayout linAdd;//添加组
	private LinearLayout linDelete;//删除组
	private LinearLayout linMRZ;//设默认组
	private RefreshLayoutListView pullRefreshList;//整个页面listview
	private SwipeRefreshLayout swip;//google自带下拉刷新
	private String adGuid;//当前用户唯一标识
	private String exchageId;//用户交换号
	private PersonGroupAdapter adapter = null;
	private List<GroupBean> dataSet;
	private CustomProgressDialog progressDialog;
	public static PersonGroupListActivity Activity_EntityActivity;
	
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_persongrouplist);
		
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.setMessage("正在加载中,请稍后...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		SysExitUtil.AddActivity(PersonGroupListActivity.this);
		try {
		
			InitView();
			
			adGuid = SpUtils.getString(getApplicationContext(), Constants.CUTOVER_AD_GUID, "");
			exchageId = SpUtils.getString(getApplicationContext(), Constants.CUTOVER_EXCHANGE_ID, "");
			dataSet = new ArrayList<GroupBean>();
			Activity_EntityActivity = this;
			
			WebServiceNetworkAccess.GetPersonalGroups(adGuid, exchageId,new WebService.Callback() {
		       @Override
				public void handle(String result) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = result;
					handler.sendMessage(msg);

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void InitView()throws Exception{

		linBack = (LinearLayout)findViewById(R.id.LinTopBack);
		linBack.setOnClickListener(this);
		linAdd = (LinearLayout) findViewById(R.id.LinAdd);
		linAdd.setOnClickListener(this);
		linDelete=(LinearLayout)findViewById(R.id.LinDelete);
		linDelete.setOnClickListener(this);
		linMRZ=(LinearLayout)findViewById(R.id.LinMRZ);
		linMRZ.setOnClickListener(this);
		pullRefreshList = (RefreshLayoutListView)findViewById(R.id.listView);
		pullRefreshList.onLoadComplete(true);
		swip = (SwipeRefreshLayout)findViewById(R.id.swip_index);
		pullRefreshList.setOnItemClickListener(onItemClickListener);
		swip.setColorSchemeResources( R.color.swiperefresh_color1, R.color.swiperefresh_color2,
	            R.color.swiperefresh_color3, R.color.swiperefresh_color4);
		//刷新
		swip.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				//下拉拉刷新
				WebServiceNetworkAccess.GetPersonalGroups(adGuid, exchageId,new WebService.Callback() {
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
	//解析组数据
	private List<GroupBean> setInboxItem(String result)throws Exception{
		if(result == null || "".equals(result) || "anyType".equalsIgnoreCase(result)){
			return null;
		}

		List<GroupBean> dataSet = new ArrayList<GroupBean>();

		GroupParser gp = new GroupParser(dataSet);
		try{
			XmlUtils.saxParse(result, gp);
		}catch (Exception e){
			e.printStackTrace();
		}

		return dataSet;
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

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//TODO 消息handler
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
							adapter = new PersonGroupAdapter(PersonGroupListActivity.this, dataSet);
							pullRefreshList.setAdapter(adapter);
						}else{
							adapter.UpData(dataSet);
							adapter.notifyDataSetChanged();
						}

					}else if(msg.obj.toString().equals("")||msg.obj.toString().equals("<root />")){
						if(progressDialog != null &&progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						dataSet.clear();//该页面只有刷新没有加载更多，所以每次刷新或者是第一次都是先清空在加载
						if(adapter == null){
							adapter = new PersonGroupAdapter(PersonGroupListActivity.this, dataSet);
							pullRefreshList.setAdapter(adapter);
						}else{
							adapter.UpData(dataSet);
							adapter.notifyDataSetChanged();
						}
						Toast.makeText(PersonGroupListActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
					}else{
						if(progressDialog != null &&progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						Toast.makeText(PersonGroupListActivity.this, getResources().getString(R.string.abnormal), Toast.LENGTH_SHORT).show();
					}
					if(progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
					break;
				case 1:
					if(progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();

					Toast.makeText(PersonGroupListActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
					break;
				case 2:
					//删除个人组
					if(progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
					String strDel = (String) msg.obj;
					if(strDel != null && !strDel.equals("")&& !strDel.equalsIgnoreCase("anyType")){
						if(strDel.equals("1")){
		                	if(adapter!=null){
								adapter.checkedMap.clear();//清空数据
							}
		                	Toast.makeText(PersonGroupListActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
		                	GetGroup();
						}else{
							Toast.makeText(PersonGroupListActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
						}
	                }else{
	                	Toast.makeText(PersonGroupListActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
	                }
					break;
				case 3:
					//设置默认组
					String strDefault = (String)msg.obj;
					if(strDefault != null && "1".equals(strDefault)){
						Toast.makeText(PersonGroupListActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
					}else{
	                	Toast.makeText(PersonGroupListActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
					}
					break;
				case 4:
					GetGroup();
					break;
				case 11:
					//更多
					Bundle bundleData=msg.getData();
					Intent intent=new Intent(PersonGroupListActivity.this,MyPersonListActivity.class);
					intent.putExtra("gname",bundleData.getString("gname"));
					intent.putExtra("gguid",bundleData.getString("gguid"));
					intent.putExtra("gtype",bundleData.getString("gtype"));
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

	@Override
	public void onClick(View v) {
		//TODO  点击事件
		try {

			switch (v.getId()) {
			case R.id.LinTopBack:
				finish();
				break;
			case R.id.LinAdd:
				//添加组
				Intent AddMyGroup=new Intent(PersonGroupListActivity.this,AddMyGroupActivity.class);
				startActivity(AddMyGroup);
				break;
			case R.id.LinDelete:
				//删除组
				if(NetWorkUtil.checkNet(getApplicationContext())){
					DelGroup();
				}else{
					Toast.makeText(PersonGroupListActivity.this, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.LinMRZ:
				//设默认组
				SetGropDefault();
				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//删除组
	private void DelGroup()throws Exception{
		String strGroupGuid ="";
		boolean IsDelGroup=true;
		if(adapter!=null&&adapter.checkedMap!=null){
			for (int i = 0; i < adapter.checkedMap.size(); i++) {
				if (adapter.checkedMap.get(i)!=-11) {
					strGroupGuid+=adapter.getItem(adapter.checkedMap.get(i)).getGuid()+";";
					if(adapter.getItem(adapter.checkedMap.get(i)).getType().equals("1")){
						IsDelGroup=false;
					}
				}
			 }
		}
		if(IsDelGroup){
			if(strGroupGuid!=null&&!strGroupGuid.equals("")){
				final String DelGroupGuid=strGroupGuid.substring(0,strGroupGuid.length()-1);
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.activity_dialog,(ViewGroup) findViewById(R.id.dialog));
			    TextView dialogtitle=(TextView)layout.findViewById(R.id.dialogtitle);
			    dialogtitle.setText("删除自定组");
			    TextView dialogtip=(TextView)layout.findViewById(R.id.dialogtip);
			    dialogtip.setText("删除自定组必且会把该组的人员一起删除，确认删除吗？");
			    AlertDialog  alertDialog = new AlertDialog.Builder(PersonGroupListActivity.this).setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                	progressDialog = CustomProgressDialog.createDialog(PersonGroupListActivity.this);
	            		progressDialog.setMessage("正在删除,请稍后...");
	            		progressDialog.setCancelable(true);
	            		progressDialog.setCanceledOnTouchOutside(false);
	            		progressDialog.show();
	                	String userXml="<root>";
						userXml+="<userguid>"+ SpUtils.getString(getApplicationContext(), Constants.CUTOVER_AD_GUID, "")+"</userguid>";
						userXml+="<username>"+SpUtils.getString(getApplicationContext(), Constants.CUTOVER_DISPLAY_NAME, "")+"</username>";
						userXml+="<devicename>"+ Constants.mobileName+"</devicename>";
						userXml+="<title>"+DelGroupGuid+"</title>";
						userXml+="</root>";
		                WebServiceNetworkAccess.DeletePersonalGroupList(DelGroupGuid,exchageId,userXml,new Callback() {
							@Override
							public void handle(String result) {
								// TODO 删除个人组
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
				Toast.makeText(PersonGroupListActivity.this, "请选择组", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(PersonGroupListActivity.this, "只能删除自定组", Toast.LENGTH_SHORT).show();
		}
	}

	//获取组
	private void GetGroup()throws Exception{
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.setMessage("正在加载中,请稍后...");
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();

		WebServiceNetworkAccess.GetPersonalGroups(adGuid, exchageId,new WebService.Callback() {
	       @Override
			public void handle(String result) {
				Message msg = new Message();
				msg.what = 0;
				msg.obj = result;
				handler.sendMessage(msg);

			}
		});
	}
	//设置默认组
	private void SetGropDefault()throws Exception{
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
			Toast.makeText(PersonGroupListActivity.this, "请选择默认组", Toast.LENGTH_SHORT).show();
		}else if(addCount==1){
			  WebServiceNetworkAccess.AddDefaultCommonGroup(adGuid,strDefaultGroupGuid,exchageId, new Callback() {
				@Override
				public void handle(String result) {
					Message msg = new Message();
					msg.what = 3;
					msg.obj = result;
					handler.sendMessage(msg);
				}
			});
		}else{
			Toast.makeText(PersonGroupListActivity.this, "默认组只能设置一个", Toast.LENGTH_SHORT).show();
		}
	}
	public void onDestroy(){
		super.onDestroy();
		SysExitUtil.RemoveActivity(PersonGroupListActivity.this);
	}
}
