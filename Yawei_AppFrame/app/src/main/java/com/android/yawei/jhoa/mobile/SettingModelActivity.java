package com.android.yawei.jhoa.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.android.yawei.jhoa.facescan.RegisterActivity;
import com.android.yawei.jhoa.fingerprint.FingerprintActivity;
import com.yawei.jhoa.mobile.R;

/**
 * TODO 设置登录方式
 * class : SettingModelActivity
 * author: Yusz
 * Date: 2018-8-24
 */

public class SettingModelActivity extends Activity{
    private RadioButton radiobutton2;//人脸识别
    private RadioButton radiobutton3;//指纹登录
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_settingmodel);
        InitView();
    }
    private void InitView(){
        radiobutton2 =(RadioButton)findViewById(R.id.radiobutton2);
        radiobutton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Intent intent = new Intent(SettingModelActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        radiobutton3 =(RadioButton)findViewById(R.id.radiobutton3);
        radiobutton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Intent intent = new Intent(SettingModelActivity.this, FingerprintActivity.class);
                    intent.putExtra("sign","2");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
