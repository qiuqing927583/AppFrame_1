package com.android.yawei.jhoa.adapter;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.PreferenceHelper;
import com.android.yawei.jhoa.utils.SpUtils;
import com.yawei.jhoa.mobile.R;

/**
 * class description: BaseFragmentActivity
 * author: Yusz
 * Date: 2018-8-8
 */

public class BaseFragmentActivity extends FragmentActivity{
    public int mTheme = R.style.AppTheme_Default;
    public int portrait = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //preference中去获取到主题,并且设置给 activity
        if (savedInstanceState == null) {
            mTheme = PreferenceHelper.getTheme(this);
        } else {
            mTheme = savedInstanceState.getInt("theme");
        }

        setTheme(mTheme);
        super.onCreate(savedInstanceState);

    }
    //检查主题 是否已经改变? 如果已经改变了,就重新加载activity ,否则没有动作
    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (mTheme != PreferenceHelper.getTheme(this)) {
                reload();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", mTheme);
    }
    //reload就是finish当前 activity ,再启动当前activity
    protected void reload() {
        try {
            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
    }

}
