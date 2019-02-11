package com.android.yawei.jhoa.voice;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yawei.jhoa.voice.baidu.inputstream.InFileStream;
import com.android.yawei.jhoa.voice.baidu.params.NluRecogParams;
import com.android.yawei.jhoa.voice.baidu.voice.ActivityRecog;
import com.android.yawei.jhoa.voice.baidu.voice.CommonRecogParams;
import com.yawei.jhoa.mobile.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 录音
 */

public abstract class RecordVoiceActivity extends Activity {

    protected Button but_voice;
    protected RecordView mRecorfView;
    protected TextView txtvoice;
    protected Handler mhandler;
//    private int nowModel = RecordView.MODEL_RECORD;

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_recordvoice);
        InFileStream.setContext(this);
        InitView();
        InitData();
        initPermission();
        initRecog();
    }
    private void InitView(){
        but_voice = (Button)findViewById(R.id.recordButton);
        mRecorfView = (RecordView) findViewById(R.id.recordView);
        mRecorfView.setCountdownTime(9);
        mRecorfView.setModel(RecordView.MODEL_RECORD);
        txtvoice = (TextView)findViewById(R.id.txtvoice);
    }
    private void InitData(){
        mhandler = new Handler() {

            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handleMsg(msg);
            }

        };
    }
    protected void handleMsg(Message msg) {

    }
    protected abstract void initRecog();

    protected TimerTask timeTask;
    protected Timer timeTimer = new Timer(true);
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int db = (int) (Math.random()*100);
            mRecorfView.setVolume(db);
        }
    };

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }
}
