package com.android.yawei.jhoa.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.yawei.jhoa.utils.StatusBarUtil;
import com.yawei.jhoa.mobile.R;

public class PersonalizedSettingActivity extends Activity implements View.OnClickListener {
    private LinearLayout LinTopBack;
    private TextView redcolor,bluecolor,yellowcolor;
    private static int sTheme;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (sTheme!=0 ) {
            //设置主题
            setTheme(sTheme);
        }
        setContentView(R.layout.activity_personalized_setting);
        try{

            initView();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initView(){
        LinTopBack = (LinearLayout)findViewById(R.id.LinTopBack);
        LinTopBack.setOnClickListener(this);
        redcolor = (TextView) findViewById(R.id.redcolor);
        redcolor.setOnClickListener(this);
        bluecolor = (TextView)findViewById(R.id.bluecolor);
        bluecolor.setOnClickListener(this);
        yellowcolor = (TextView)findViewById(R.id.yellowcolor);
        yellowcolor.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.LinTopBack:
                finish();
                break;
            case R.id.redcolor:
                StatusBarUtil.setStatusBarColor1(PersonalizedSettingActivity.this,R.color.red);
                break;
            case R.id.bluecolor:
                StatusBarUtil.setStatusBarColor1(PersonalizedSettingActivity.this,R.color.blue);

                break;
            case R.id.yellowcolor:

                //将选中的主题资源id保存到静态变量中
                sTheme = R.color.yellow;
                //重构Activity才能应用新的主题
                recreate();
//                StatusBarUtil.setStatusBarColor(PersonalizedSettingActivity.this,R.color.yellow);
//                mToolbar.setBackground(getResources().getDrawable(R.color.yellow));
                break;
        }
    }

}
