package com.android.yawei.jhoa.mobile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.ui.CustomProgressDialog;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.utils.SysExitUtil;
import com.android.yawei.jhoa.webservice.WebService.Callback;

import com.yawei.jhoa.mobile.R;

/****************
 * TODO 添加个人组
 * @author Yusz
 */
public class AddMyGroupActivity extends BaseActivity implements OnClickListener{

	private LinearLayout linBack;//返回
	private LinearLayout linAdd;//添加
	private EditText editGroupName;//添加组名称
	private CustomProgressDialog progressDialog;
	private MyHandler handler = new MyHandler();
	
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmygroup);
		
		try {
			SysExitUtil.AddActivity(AddMyGroupActivity.this);
			InitView();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void InitView()throws Exception{
		linBack = (LinearLayout)findViewById(R.id.LinTopBack);
		linBack.setOnClickListener(this);
		linAdd = (LinearLayout)findViewById(R.id.LinAdd);
		linAdd.setOnClickListener(this);
		editGroupName = (EditText)findViewById(R.id.EtGroupName);
	}
	//添加自定义组
	private void AddCustomGrop()throws Exception{
		if(!editGroupName.getText().toString().equals("")){
			
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage("正在添加中,请稍后...");
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			
			String userXml="<root>";
			userXml+="<item groupName=\""+editGroupName.getText()+"\"";
			userXml+=" creator=\""+SpUtils.getString(getApplicationContext(), Constants.CUTOVER_DISPLAY_NAME, "")+"\" ";
			userXml+=" creatorguid=\""+SpUtils.getString(getApplicationContext(), Constants.CUTOVER_AD_GUID, "")+"\"/>";
			userXml+="</root>";
			
			WebServiceNetworkAccess.AddPersonalGroup(userXml,SpUtils.getString(getApplicationContext(), Constants.CUTOVER_EXCHANGE_ID, ""),new Callback() {
				@Override
				public void handle(String result) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = result;
					handler.sendMessage(msg);
				}
			});
		 }else{
			  Toast.makeText(AddMyGroupActivity.this, "群组名称不能为空", Toast.LENGTH_SHORT).show();
		  }
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.LinTopBack:
				finish();
				break;
			case R.id.LinAdd:
				AddCustomGrop();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
					case 0:
						if(progressDialog != null && progressDialog.isShowing())
							progressDialog.dismiss();
						String strAddXml = (String)msg.obj;
						if (strAddXml != null && "1".equals(strAddXml)) {
							Toast.makeText(AddMyGroupActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
							finish();
							Message msg_ = new Message();
							msg_.what = 4;
							PersonGroupListActivity.Activity_EntityActivity.handler.sendMessage(msg_);
							finish();
						}else{
							Toast.makeText(AddMyGroupActivity.this, "添加失败，请重新添加", Toast.LENGTH_SHORT).show();
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
	}
	public void onDestroy(){
		super.onDestroy();
		SysExitUtil.RemoveActivity(this);
	}
}
