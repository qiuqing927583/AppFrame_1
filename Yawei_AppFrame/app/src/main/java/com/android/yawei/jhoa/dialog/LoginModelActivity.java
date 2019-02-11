package com.android.yawei.jhoa.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.yawei.jhoa.facescan.Config;
import com.android.yawei.jhoa.facescan.DetectLoginActivity;
import com.android.yawei.jhoa.facescan.exception.FaceError;
import com.android.yawei.jhoa.facescan.faceuntil.APIService;
import com.android.yawei.jhoa.facescan.faceuntil.OnResultListener;
import com.android.yawei.jhoa.facescan.model.AccessToken;
import com.android.yawei.jhoa.fingerprint.FingerprintActivity;
import com.android.yawei.jhoa.mobile.JhoaLoginActivity;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.SpUtils;
import com.baidu.aip.FaceEnvironment;
import com.baidu.aip.FaceSDKManager;
import com.baidu.idl.facesdk.FaceTracker;
import com.yawei.jhoa.mobile.R;

/**
 * class description: LoginModelActivity
 * author: Yusz
 * Date: 2018-8-10
 */

public class LoginModelActivity extends AppCompatActivity {

    private RadioButton radiobutton1;//账号密码登录
    private RadioButton radiobutton2;//人脸识别
    private RadioButton radiobutton3;//指纹登录

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_dialog_loginmodel);
        setFinishOnTouchOutside(true);
        InitView();
    }
    private void InitView(){
        radiobutton1 =(RadioButton)findViewById(R.id.radiobutton1);
        radiobutton2 =(RadioButton)findViewById(R.id.radiobutton2);
        radiobutton3 =(RadioButton)findViewById(R.id.radiobutton3);
        String loginmodel = SpUtils.getString(getApplicationContext(),Constants.LOGINMODLE,"");
        if(TextUtils.isEmpty(loginmodel)||"1".equals(loginmodel)){
            radiobutton1.setChecked(true);
        }else if("2".equals(loginmodel)){
            radiobutton2.setChecked(true);
        }else if("3".equals(loginmodel)){
            radiobutton3.setChecked(true);
        }
        radiobutton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SpUtils.setString(getApplicationContext(), Constants.LOGINMODLE,"1");
                    finish();
                }
            }
        });
        radiobutton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SpUtils.setString(getApplicationContext(), Constants.LOGINMODLE,"2");
                    //初始化人脸扫描
                    InitBaiduFaceScan();
                }
            }
        });
        radiobutton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SpUtils.setString(getApplicationContext(), Constants.LOGINMODLE,"3");
                    Intent intent = new Intent(LoginModelActivity.this, FingerprintActivity.class);
                    startActivityForResult(intent,1);
                    finish();
                }
            }
        });
    }
    //初始化人脸扫描
    private void InitBaiduFaceScan(){
        initFaceLib();
        APIService.getInstance().init(this);
        APIService.getInstance().setGroupId(Config.groupID);
        // 用ak，sk获取token, 调用在线api，如：注册、识别等。为了ak、sk安全，建议放您的服务器，
        APIService.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
//                Log.i("wtf", "AccessToken->" + result.getAccessToken());
                Intent intent = new Intent(LoginModelActivity.this, DetectLoginActivity.class);
                startActivityForResult(intent,1);
                finish();
            }
            @Override
            public void onError(FaceError error) {
//                Log.e("xx", "AccessTokenError:" + error);
                Toast.makeText(LoginModelActivity.this,"人脸认证错误："+error,Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }, getApplicationContext(), Config.apiKey, Config.secretKey);
    }
    /**
     * 初始化SDK
     */
    private void initFaceLib() {
        // 为了android和ios 区分授权，appId=appname_face_android ,其中appname为申请sdk时的应用名
        // 应用上下文
        // 申请License取得的APPID
        // assets目录下License文件名
        FaceSDKManager.getInstance().init(this, Config.licenseID, Config.licenseFileName);
        setFaceConfig();
    }
    private void setFaceConfig() {
        FaceTracker tracker = FaceSDKManager.getInstance().getFaceTracker(this);  //.getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整

        // 模糊度范围 (0-1) 推荐小于0.7
        tracker.set_blur_thr(FaceEnvironment.VALUE_BLURNESS);
        // 光照范围 (0-1) 推荐大于40
        tracker.set_illum_thr(FaceEnvironment.VALUE_BRIGHTNESS);
        // 裁剪人脸大小
        tracker.set_cropFaceSize(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        // 人脸yaw,pitch,row 角度，范围（-45，45），推荐-15-15
        tracker.set_eulur_angle_thr(FaceEnvironment.VALUE_HEAD_PITCH, FaceEnvironment.VALUE_HEAD_ROLL,
                FaceEnvironment.VALUE_HEAD_YAW);

        // 最小检测人脸（在图片人脸能够被检测到最小值）80-200， 越小越耗性能，推荐120-200
        tracker.set_min_face_size(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        //
        tracker.set_notFace_thr(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        // 人脸遮挡范围 （0-1） 推荐小于0.5
        tracker.set_occlu_thr(FaceEnvironment.VALUE_OCCLUSION);
        // 是否进行质量检测
        tracker.set_isCheckQuality(true);
        // 是否进行活体校验
        tracker.set_isVerifyLive(false);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (resultCode){
                case 100:
//                    Intent intent1 = new Intent();
//                    intent1.putExtra("userinfo", data.getStringExtra("userinfo"));
                    setResult(100, data);
                    finish();
                    break;
                case 101:
                    String str101 = data.getStringExtra("userinfo");
                    if("-1".equals(str101)||TextUtils.isEmpty(str101)){
                        finish();
                    }else if(!TextUtils.isEmpty(str101)){
                        setResult(101, data);
                        finish();

                    }
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
