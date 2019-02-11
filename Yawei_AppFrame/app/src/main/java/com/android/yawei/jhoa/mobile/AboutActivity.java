package com.android.yawei.jhoa.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.yawei.jhoa.utils.Constants;
import com.yawei.jhoa.mobile.R;

public class AboutActivity extends Activity {
    private TextView brand,model,sign;
    private LinearLayout LinTopBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        try{
            initView();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void initView() throws Exception{
        brand = (TextView)findViewById(R.id.brand);
        brand.setText(android.os.Build.BRAND);
        model = (TextView)findViewById(R.id.model);
        model.setText(android.os.Build.MODEL);
        sign = (TextView)findViewById(R.id.sign);
        sign.setText(Constants.MobileIMEI);
        LinTopBack = (LinearLayout)findViewById(R.id.LinTopBack);
        LinTopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
