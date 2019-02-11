package com.android.yawei.jhoa.mobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.yawei.jhoa.USBHelper.UsbHelper;
import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.bean.DevicesInfoBean;
import com.android.yawei.jhoa.facescan.Config;
import com.android.yawei.jhoa.facescan.exception.FaceError;
import com.android.yawei.jhoa.facescan.faceuntil.APIService;
import com.android.yawei.jhoa.facescan.faceuntil.OnResultListener;
import com.android.yawei.jhoa.facescan.model.AccessToken;
import com.android.yawei.jhoa.ui.CustomProgressDialog;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.FileUtils;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.SpUtils;
import com.android.yawei.jhoa.utils.VerifyPermissions;
import com.baidu.aip.FaceEnvironment;
import com.baidu.aip.FaceSDKManager;
import com.baidu.idl.facesdk.FaceTracker;
import com.hxyl.Elink;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.pizidea.imagepicker.BuildConfig;
import com.yawei.jhoa.mobile.R;

import java.io.File;

/**
 * TODO 启动动画
 * class description: LaunchSreenActivity
 * author: Yusz
 * Date: 2018-8-10
 */

public class LaunchSreenActivity extends BaseActivity{



    private CustomProgressDialog progressDialog;
    DevicesInfoBean deveInfo;
    MyApplication application;

    private MyHandler handler = new MyHandler();

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_launchsreen);
        try {
            application = (MyApplication) getApplication();

            deveInfo = application.getDeveInfo();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            boolean permissions= VerifyPermissions.VerifyStoragePermissions(LaunchSreenActivity.this,0);
//        }
            if(permissions){
                handler.sendEmptyMessage(3);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void InitData(){
        ApplicationThreadRun();
        OtherInit();
        //初始化人脸扫描
        String loginmodel = SpUtils.getString(getApplicationContext(),Constants.LOGINMODLE,"");
        Log.d("loginmodel",loginmodel);
        if(!TextUtils.isEmpty(loginmodel)&&loginmodel.equals("2")){
            InitBaiduFaceScan();
        }
        handler.sendEmptyMessageDelayed(1,3000);
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
            }
            @Override
            public void onError(FaceError error) {
//                Log.e("xx", "AccessTokenError:" + error);
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
    //app中一些启动创建耗时的操作再次线程中操作
    private void ApplicationThreadRun(){
        new Thread(){
            public void run(){
                deveInfo.setDeve_brand(android.os.Build.BRAND);
                deveInfo.setDeve_model(android.os.Build.MODEL);
                deveInfo.setDeve_version("Android " + Build.VERSION.RELEASE);
                deveInfo.setDeve_imei(Constants.MobileIMEI);
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    String root = FileUtils.GetSystemFilePath();
                    File file = new File(root+"temp");
                    new FileUtils().deleteFile(file);
                    new FileUtils().creatSDDirs(root+"temp");
                    new FileUtils().creatSDDirs(root+"upload");
                    //ysz 记录错误日志目录
                    new FileUtils().creatSDDirs(root+"Log");
                    new FileUtils().creatSDDirs(root+"zip");
                    new FileUtils().creatSDDirs(root+"plugin");
                }
                UsbHelper.GetMobileUSB(getApplicationContext());
                if(Constants.OpenChat) Elink.attach(getApplicationContext());

            }
        }.start();
    }
    //APP 启动时一些需要初始化的操作
    private void OtherInit(){
        if(!ImageLoader.getInstance().isInited()){
            ImageLoaderConfiguration config = null;
            if(BuildConfig.DEBUG){
                config = new ImageLoaderConfiguration.Builder(this)
                        .threadPriority(Thread.NORM_PRIORITY - 1)
                        .tasksProcessingOrder(QueueProcessingType.LIFO)
                        .memoryCacheSizePercentage(13)
                        .diskCacheSize(500 * 1024 * 1024)
                        .build();
            }else{
                config = new ImageLoaderConfiguration.Builder(this)
                        .threadPriority(Thread.NORM_PRIORITY - 2)
                        .diskCacheSize(500 * 1024 * 1024)
                        .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        .tasksProcessingOrder(QueueProcessingType.LIFO).build();
            }
            ImageLoader.getInstance().init(config);
        }
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            try {
                Intent intent;
                switch (msg.what){
                    case 0:
                        if(progressDialog != null &&progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(LaunchSreenActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        intent = new Intent(LaunchSreenActivity.this,JhoaLoginActivity.class);
                        startActivity(intent);
                        handler.sendEmptyMessageDelayed(2,500);
                        break;
                    case 2:
                        finish();
                        break;
                    case 3:
                        InitData();
                        break;
                    default:
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case VerifyPermissions.mrequestCode:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    Constants.MobileIMEI = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    MyApplication application = (MyApplication) getApplication();
                    DevicesInfoBean infoBean = application.getDeveInfo();
                    infoBean.setDeve_imei(Constants.MobileIMEI);
                }
                handler.sendEmptyMessageDelayed(3,300);
                break;
            default:
        }
    }
}
