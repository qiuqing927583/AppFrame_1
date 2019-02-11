package com.android.yawei.jhoa.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yawei.jhoa.mobile.R;

public class AppVersionActivity extends Activity {
    private TextView version_current;
    private LinearLayout LinTopBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versioninfo);
        try{
            InitView();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private  void InitView()throws Exception{
        version_current = (TextView) findViewById(R.id.version_current);
        version_current.setText("V "+AppUtils.getVersionName(this));
        LinTopBack = (LinearLayout)findViewById(R.id.LinTopBack);
        LinTopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
