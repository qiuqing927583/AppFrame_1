package com.android.yawei.jhoa.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.SpUtils;
import com.yawei.jhoa.mobile.R;

/***********
 * TODO 功能：个人中心
 * @author Yusz
 * 创建时间：2015-7-24
 *
 *
 ***********/
public class MyPersonalCenterActivity extends BaseActivity implements OnClickListener{

	private Intent intent;
	private LinearLayout linBack;
	private TextView userName;//用户名
	private TextView userLoginName;//用户登录名
	private TextView userDepartment;//用户部门
	private LinearLayout linwddlrz;//登录日志
	private LinearLayout loginmodel;//多种登录方式
	private LinearLayout linqhyh;//切换用户
	private LinearLayout lingxhsz;//个性化设置
	private LinearLayout screenDirection;//横竖屏切换
	private TextView txtCacheSize;//显示缓存大小
	private LinearLayout linqchc;//点击清除缓存
	private LinearLayout linbbxx;//版本信息
	private LinearLayout linabout;//关于
	private Button user_logout;//注销登录

	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypersonalcenter);

		try {

			InitView();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void InitView() throws Exception{

		linBack = (LinearLayout)findViewById(R.id.LinTopBack);
		linBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		user_logout = (Button) findViewById(R.id.user_logout);
		user_logout.setOnClickListener(this);

		userName = (TextView)findViewById(R.id.userName);
		userLoginName = (TextView)findViewById(R.id.userLoginName);
		userDepartment = (TextView)findViewById(R.id.userDepartment);
		userName.setText(SpUtils.getString(getApplicationContext(), Constants.CUTOVER_DISPLAY_NAME, ""));
		userLoginName.setText("账号："+SpUtils.getString(getApplicationContext(), Constants.CUTOVER_AD_LOGNAME, ""));
		userDepartment.setText("部门："+SpUtils.getString(getApplicationContext(), Constants.CUTOVER_AD_DEPART, ""));

		txtCacheSize = (TextView)findViewById(R.id.txtCacheSize);
		linqchc = (LinearLayout)findViewById(R.id.linqchc);
		linqchc.setOnClickListener(this);
		linbbxx = (LinearLayout)findViewById(R.id.linbbxx);
		linbbxx.setOnClickListener(this);
		loginmodel = (LinearLayout)findViewById(R.id.loginmodel);
		loginmodel.setOnClickListener(this);
		linqhyh = (LinearLayout)findViewById(R.id.linqhyh);
		linqhyh.setOnClickListener(this);
		linabout = (LinearLayout)findViewById(R.id.linabout);
		linabout.setOnClickListener(this);
		screenDirection = (LinearLayout)findViewById(R.id.screenDirection);
		screenDirection.setOnClickListener(this);
		lingxhsz = (LinearLayout)findViewById(R.id.lingxhsz);
		lingxhsz.setOnClickListener(this);
		linwddlrz = (LinearLayout)findViewById(R.id.linwddlrz);
		linwddlrz.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id){
			case R.id.user_logout://注销登录
				intent = new Intent(MyPersonalCenterActivity.this,MainActivityV2.class);
				intent.putExtra(MainActivityV2.TAG_EXIT, true);
				startActivity(intent);
				break;
			case R.id.linqchc://清除缓存
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {

							CacheDataManager.clearAllCache(MyPersonalCenterActivity.this);

							//Thread.sleep(3000);

							if (CacheDataManager.getTotalCacheSize(MyPersonalCenterActivity.this).startsWith("0")) {

								handler.sendEmptyMessage(0);

							}

						} catch (Exception e) {

							return;

						}
					}
				}).start();
				break;
			case R.id.linbbxx://版本信息
				intent = new Intent(MyPersonalCenterActivity.this,AppVersionActivity.class);
				startActivity(intent);
				break;
			case R.id.loginmodel://多种方式登录
				intent = new Intent(MyPersonalCenterActivity.this,SettingModelActivity.class);
				startActivity(intent);
				break;
			case R.id.linqhyh://切换用户
				intent = new Intent(MyPersonalCenterActivity.this,MainActivityV2.class);
				intent.putExtra(MainActivityV2.TAG_EXIT, true);
				startActivity(intent);
				break;
			case R.id.linabout://关于
				intent = new Intent(MyPersonalCenterActivity.this,AboutActivity.class);
				startActivity(intent);
				break;
			case R.id.screenDirection://横竖屏切换
				intent = new Intent(MyPersonalCenterActivity.this,ChangeScreenActivity.class);
				startActivity(intent);
				break;
			case R.id.lingxhsz://个性化设置
				intent = new Intent(MyPersonalCenterActivity.this,PersonalizedSettingActivity.class);
				startActivity(intent);
				break;
			case R.id.linwddlrz://登录日志
				intent = new Intent(MyPersonalCenterActivity.this,ConLogActivity.class);
				startActivity(intent);
				break;
		}
	}

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

				case 0:

					Toast.makeText(MyPersonalCenterActivity.this,"清理完成",Toast.LENGTH_SHORT).show();

					try {

						txtCacheSize.setText(CacheDataManager.getTotalCacheSize(MyPersonalCenterActivity.this));

					} catch (Exception e) {

						e.printStackTrace();

					}

			}

		}

	};

}
