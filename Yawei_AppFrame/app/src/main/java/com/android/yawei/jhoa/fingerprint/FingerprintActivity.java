package com.android.yawei.jhoa.fingerprint;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.db.AppModuleDatabase;
import com.android.yawei.jhoa.mobile.JhoaLoginActivity;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.utils.MyApplication;
import com.android.yawei.jhoa.utils.SpUtils;
import com.yawei.jhoa.mobile.R;

/**
 * TODO 指纹识别
 * class : FingerprintActivity
 * author: Yusz
 * Date: 2018-8-30
 */

public class FingerprintActivity extends BaseActivity{

    private TextView mResultInfo = null;

    private FingerprintManagerCompat fingerprintManager = null;
    private MyAuthCallback myAuthCallback = null;
    private CancellationSignal cancellationSignal = null;
    private String sign;//1登录，2注册
    private AppModuleDatabase dbHelper;
    private Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        try {
            sign = getIntent().getStringExtra("sign");
            MyApplication application = (MyApplication) getApplication();
            dbHelper = application.GetModuleSQLHepler();
            initHandler();
            isRightFingerPrinter();
            initView();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void isRightFingerPrinter() {
        fingerprintManager = FingerprintManagerCompat.from(this);

        if (!fingerprintManager.isHardwareDetected()) {//是否支持指纹识别
            // 没有指纹传感器
            String message = getString(R.string.no_sensor_dialog_message);
            ShowDialog(message);
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {//是否已注册指纹
            // 没有指纹录入
            ShowDialog("设备未录入指纹信息，请到系统“设置”中找到指纹设置，设置指纹信息。");
        } else {
            try {
                myAuthCallback = new MyAuthCallback(handler);
                StartFinger();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case FingerprintConstant.MSG_AUTH_SUCCESS:
                        setResultInfo(R.string.fingerprint_success);
                        cancellationSignal = null;
                        break;
                    case FingerprintConstant.MSG_AUTH_FAILED:
                        setResultInfo(R.string.fingerprint_not_recognized);
                        cancellationSignal = null;
                        break;
                    case FingerprintConstant.MSG_AUTH_ERROR:
                        handleErrorCode(msg.arg1);
                        break;
                    case FingerprintConstant.MSG_AUTH_HELP:
                        handleHelpCode(msg.arg1);
                        break;
                }
            }
        };
    }
    private void initView() {
        mResultInfo = (TextView) this.findViewById(R.id.fingerprint_status);
        LinearLayout LinTopBack = (LinearLayout)findViewById(R.id.LinTopBack);
        LinTopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                finish();
            }
        });
    }
    private void ShowDialog(String message){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_dialog, (ViewGroup) findViewById(R.id.dialog));
        android.app.AlertDialog alertDialog;
        TextView dialogtitleL = (TextView) layout.findViewById(R.id.dialogtitle);
        dialogtitleL.setText("提示");
        TextView dialogtipL = (TextView) layout.findViewById(R.id.dialogtip);
        dialogtipL.setText(message);
        alertDialog = new android.app.AlertDialog.Builder(FingerprintActivity.this).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("userinfo", "-1");
                setResult(101, intent);
                finish();
            }
        }).create();
        alertDialog.setView(layout, -1, -1, -1, -1);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
    private void StartFinger(){
        try {
            CryptoObjectHelper cryptoObjectHelper = new CryptoObjectHelper();
            if (cancellationSignal == null) {
                cancellationSignal = new CancellationSignal();
            }
            fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(), 0,
                    cancellationSignal, myAuthCallback, null);
            // set button state.
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(FingerprintActivity.this, "Fingerprint init failed! Try again!", Toast.LENGTH_SHORT).show();
        }
    }
    private void handleHelpCode(int code) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ACQUIRED_GOOD:
                setResultInfo(R.string.AcquiredGood_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:
                setResultInfo(R.string.AcquiredImageDirty_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT:
                setResultInfo(R.string.AcquiredInsufficient_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:
                setResultInfo(R.string.AcquiredPartial_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST:
                setResultInfo(R.string.AcquiredTooFast_warning);
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW:
                setResultInfo(R.string.AcquiredToSlow_warning);
                break;
        }
    }
    private void handleErrorCode(int code) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
                setResultInfo(R.string.ErrorCanceled_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE:
                setResultInfo(R.string.ErrorHwUnavailable_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
                setResultInfo(R.string.ErrorLockout_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_NO_SPACE:
                setResultInfo(R.string.ErrorNoSpace_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_TIMEOUT:
                setResultInfo(R.string.ErrorTimeout_warning);
                break;
            case FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS:
                setResultInfo(R.string.ErrorUnableToProcess_warning);
                break;
        }
    }
    /**
     * 设置识别结果
     *
     * @param stringId 识别结果提示内容
     */
    private void setResultInfo(int stringId) {
        if (mResultInfo != null) {
            if (stringId == R.string.fingerprint_success) {
                //指纹识别成功
                mResultInfo.setTextColor(getResources().getColor(R.color.blue));
                if("1".equals(sign) || sign==null){
                    String usermsg = dbHelper.GetFingerUser();
                    if(!TextUtils.isEmpty(usermsg)){
                        Log.d("usermsg",usermsg);
//                        Intent intent = new Intent();
//                        intent.putExtra("userinfo", usermsg);
//                        setResult(101, intent);
                        String strName = "";
                        String strPwd = "";
                        if (usermsg.split(";").length<1){
                            strName = "";
                        }else{
                            strName = usermsg.split(";")[0];
                        }

                        if (usermsg.split(";").length<=1){
                           strPwd = "";
                        }else{
                            strPwd = usermsg.split(";")[1];
                        }
                        JhoaLoginActivity.activity.GetUserInfoFromMobile(strName,strPwd);

                       finish();
                    }else{
                        ShowDialog("未绑定任何账号，请登录到个人中心进行账号绑定！");
                    }
                }else if("2".equals(sign)){
                    String username = SpUtils.getString(getApplicationContext(), Constants.CUTOVER_AD_LOGNAME,"");
                    dbHelper.InsertUserMsgToTable(username,Constants.PASSWORD);
                    Toast.makeText(FingerprintActivity.this,"账号绑定成功",Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {
                //指纹识别失败
                mResultInfo.setTextColor(getResources().getColor(R.color.red));
            }
            mResultInfo.setText(stringId);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cancellationSignal != null) {
            cancellationSignal.cancel();
        }
    }


}
