package com.android.yawei.jhoa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.jhoa.mobile.AboutActivity;
import com.android.yawei.jhoa.mobile.AppVersionActivity;
import com.android.yawei.jhoa.mobile.CacheDataManager;
import com.android.yawei.jhoa.mobile.ChangeScreenActivity;
import com.android.yawei.jhoa.mobile.ConLogActivity;
import com.android.yawei.jhoa.mobile.MainActivityV2;
import com.android.yawei.jhoa.mobile.PersonalizedSettingActivity;
import com.android.yawei.jhoa.mobile.SettingModelActivity;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.SpUtils;
import com.yawei.jhoa.mobile.R;

/**
 * 个人中心
 * Created by Yusz on 2018-6-12.
 */

public class SelfSentreFragment extends Fragment implements View.OnClickListener {

    private Intent intent;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_mypersonalcenter, container, false);
        View newView = InitView(rootView);
        try {

            txtCacheSize.setText(CacheDataManager.getTotalCacheSize(getContext()));

        } catch (Exception e) {

            e.printStackTrace();

        }
        return newView;
    }
    private View InitView(View view){
        RelativeLayout relatTop = (RelativeLayout)view.findViewById(R.id.relatTop);
        relatTop.setVisibility(View.GONE);
        user_logout = (Button) view.findViewById(R.id.user_logout);
        user_logout.setOnClickListener(this);

        userName = (TextView)view.findViewById(R.id.userName);
        userLoginName = (TextView)view.findViewById(R.id.userLoginName);
        userDepartment = (TextView)view.findViewById(R.id.userDepartment);
        userName.setText(SpUtils.getString(getContext(), Constants.CUTOVER_DISPLAY_NAME, ""));
        userLoginName.setText("账号："+SpUtils.getString(getContext(), Constants.CUTOVER_AD_LOGNAME, ""));
        userDepartment.setText("部门："+SpUtils.getString(getContext(), Constants.CUTOVER_AD_DEPART, ""));
        txtCacheSize = (TextView)view.findViewById(R.id.txtCacheSize);
        linqchc = (LinearLayout)view.findViewById(R.id.linqchc);
        linqchc.setOnClickListener(this);
        linbbxx = (LinearLayout)view.findViewById(R.id.linbbxx);
        linbbxx.setOnClickListener(this);
        loginmodel = (LinearLayout)view.findViewById(R.id.loginmodel);
        loginmodel.setOnClickListener(this);
        linqhyh = (LinearLayout)view.findViewById(R.id.linqhyh);
        linqhyh.setOnClickListener(this);
        linabout = (LinearLayout)view.findViewById(R.id.linabout);
        linabout.setOnClickListener(this);
        screenDirection = (LinearLayout)view.findViewById(R.id.screenDirection);
        screenDirection.setOnClickListener(this);
        lingxhsz = (LinearLayout)view.findViewById(R.id.lingxhsz);
        lingxhsz.setOnClickListener(this);
        linwddlrz = (LinearLayout)view.findViewById(R.id.linwddlrz);
        linwddlrz.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.user_logout://注销登录
                intent = new Intent(getContext(),MainActivityV2.class);
                intent.putExtra(MainActivityV2.TAG_EXIT, true);
                startActivity(intent);
                break;
            case R.id.linqchc://清除缓存
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            CacheDataManager.clearAllCache(getContext());

                            //Thread.sleep(3000);

                            if (CacheDataManager.getTotalCacheSize(getContext()).startsWith("0")) {

                                handler.sendEmptyMessage(0);

                            }

                        } catch (Exception e) {

                            return;

                        }
                    }
                }).start();
                break;
            case R.id.linbbxx://版本信息
                intent = new Intent(getContext(),AppVersionActivity.class);
                startActivity(intent);
                break;
            case R.id.loginmodel://多种方式登录
                intent = new Intent(getContext(),SettingModelActivity.class);
                startActivity(intent);
                break;
            case R.id.linqhyh://切换用户
                intent = new Intent(getContext(),MainActivityV2.class);
                intent.putExtra(MainActivityV2.TAG_EXIT, true);
                startActivity(intent);
                break;
            case R.id.linabout://关于
                intent = new Intent(getContext(),AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.screenDirection://横竖屏切换
                intent = new Intent(getContext(),ChangeScreenActivity.class);
                startActivity(intent);
                break;
            case R.id.lingxhsz://个性化设置
                intent = new Intent(getContext(),PersonalizedSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.linwddlrz://登录日志
                intent = new Intent(getContext(),ConLogActivity.class);
                startActivity(intent);
                break;
        }
    }

        private Handler handler = new Handler() {

            public void handleMessage(android.os.Message msg) {

                switch (msg.what) {

                    case 0:

                        Toast.makeText(getContext(),"清理完成",Toast.LENGTH_SHORT).show();

                        try {

                            txtCacheSize.setText(CacheDataManager.getTotalCacheSize(getContext()));

                        } catch (Exception e) {

                            e.printStackTrace();

                        }

                }

            }

        };
}
