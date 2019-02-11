package com.android.yawei.jhoa.voice.baidu.voice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.android.yawei.jhoa.voice.RecordView;
import com.android.yawei.jhoa.voice.RecordVoiceActivity;
import com.android.yawei.jhoa.voice.baidu.control.MyRecognizer;
import com.android.yawei.jhoa.voice.baidu.offline.OfflineRecogParams;
import com.android.yawei.jhoa.voice.baidu.voiceutil.AutoCheck;
import com.android.yawei.jhoa.voice.baidu.voiceutil.VoiceBean;
import com.baidu.speech.asr.SpeechConstant;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * 识别的基类Activity。 ActivityCommon定义了通用的UI部分
 * 封装了识别的大部分逻辑，包括MyRecognizer的初始化，资源释放
 * <p>
 * <p>
 * 集成流程代码，只需要一句： myRecognizer.start(params);具体示例代码参见startRough()
 * =》.实例化 myRecognizer   new MyRecognizer(this, listener);
 * =》 实例化 listener  new MessageStatusRecogListener(null);
 * </p>
 * 集成文档： http://ai.baidu.com/docs#/ASR-Android-SDK/top 集成指南一节
 * demo目录下doc_integration_DOCUMENT
 * ASR-INTEGRATION-helloworld  ASR集成指南-集成到helloworld中 对应 ActivityMiniRecog
 * ASR-INTEGRATION-TTS-DEMO ASR集成指南-集成到合成DEMO中 对应 ActivityRecog
 * <p>
 * 大致流程为
 * 1. 实例化MyRecognizer ,调用release方法前不可以实例化第二个。参数中需要开发者自行填写语音识别事件的回调类，实现开发者自身的业务逻辑
 * 2. 如果使用离线命令词功能，需要调用loadOfflineEngine。在线功能不需要。
 * 3. 根据识别的参数文档，或者demo中测试出的参数，组成json格式的字符串。调用 start 方法
 * 4. 在合适的时候，调用release释放资源。
 * <p>
 */

public abstract class ActivityRecog extends RecordVoiceActivity implements IStatus,View.OnTouchListener {

    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    protected MyRecognizer myRecognizer;
    /*
     * Api的参数类，仅仅用于生成调用START的json字符串，本身与SDK的调用无关
     */
    protected CommonRecogParams apiParams;
    /*
     * 本Activity中是否需要调用离线命令词功能。根据此参数，判断是否需要调用SDK的ASR_KWS_LOAD_ENGINE事件
     */
    protected boolean enableOffline = false;
    /**
     * 控制UI按钮的状态
     */
    protected int status;
    /**
     * 在onCreate中调用。初始化识别控制类MyRecognizer
     */
    protected void initRecog() {
        but_voice.setOnTouchListener(this);
        InitChildView();
        StatusRecogListener listener = new MessageStatusRecogListener(mhandler);
        myRecognizer = new MyRecognizer(this, listener);
        apiParams = getApiParams();
        status = STATUS_NONE;
        if (enableOffline) {
            myRecognizer.loadOfflineEngine(OfflineRecogParams.fetchOfflineParams());
        }
    }

    /**
     * 开始录音，点击“开始”按钮后调用。
     */
    protected void start() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ActivityRecog.this);
        //  上面的获取是为了生成下面的Map， 自己集成时可以忽略
        // / 集成时不需要上面的代码，只需要params参数。
        final Map<String, Object> params = apiParams.fetch(sp);
        // 复制此段可以自动检测错误
        (new AutoCheck(getApplicationContext(), new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
//                        txtLog.append(message + "\n");
                    }
                }
            }
        },enableOffline)).checkAsr(params);

        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
        myRecognizer.start(params);
    }

    /**
     * 测试demo成功后可以修改这个方法
     * 粗略测试，将原来的start方法注释，这个方法改为start即可。
     * 点击开始按钮使用，注意此时与本demo的UI已经解绑，UI上不会显示，请自行看logcat日志
     */
    protected void startRough() {
        // initRecog中已经初始化，这里释放。不需要集成到您的代码中
        myRecognizer.release();
        myRecognizer = null;
        // 上面不需要集成到您的代码中

        /*********************************************/
        // 1. 确定识别参数
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // 具体的params的值在 测试demo成功后，myRecognizer.start(params);中打印

        // 2. 初始化IRecogListener
        StatusRecogListener listener = new MessageStatusRecogListener(null);
        // 日志显示在logcat里，UI界面上是没有的。需要显示在界面上， 这里设置为handler

        // 3 初始化 MyRecognizer
        myRecognizer = new MyRecognizer(this, listener);

        // 4. 启动识别
        myRecognizer.start(params);
        // 日志显示在logcat里，UI界面上是没有的。

        // 5 识别结束了别忘了释放。

        // 需要离线识别过程，需要加上 myRecognizer.loadOfflineEngine(OfflineRecogParams.fetchOfflineParams());
        // 注意这个loadOfflineEngine是异步的， 不能连着调用 start
    }

    /**
     * 销毁时需要释放识别资源。
     */
    @Override
    protected void onDestroy() {
        myRecognizer.release();
        super.onDestroy();
    }

    /**
     * 开始录音后，手动停止录音。SDK会识别在此过程中的录音。点击“停止”按钮后调用。
     */
    protected void stop() {
        myRecognizer.stop();
    }

    /**
     * 开始录音后，取消这次录音。SDK会取消本次识别，回到原始状态。点击“取消”按钮后调用。
     */
    private void cancel() {
        myRecognizer.cancel();
    }

    /**
     * @return
     */
    protected abstract CommonRecogParams getApiParams();
    protected abstract void InitChildView();
    protected abstract void GetVoiceMessage(String message);
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            mRecorfView.start();
            start();
            but_voice.setText("松开结束说话");
            status = STATUS_WAITING_READY;
            timeTimer.schedule(timeTask = new TimerTask() {
                public void run() {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }, 20, 20);
            mRecorfView.setOnCountDownListener(new RecordView.OnCountDownListener() {
                @Override
                public void onCountDown() {
                    but_voice.setText("按住开始说话");
                    stop();
                    status = STATUS_NONE; // 识别结束，回到初始状态
//                    finish();
                    //Toast.makeText(RecordVoiceActivity.this,"录音结束",Toast.LENGTH_SHORT).show();
                }
            });
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            mRecorfView.cancel();
            but_voice.setText("按住开始说话");
            stop();
            status = STATUS_NONE; // 识别结束，回到初始状态
        }
        return false;
    }

    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
        switch (msg.what) { // 处理MessageStatusRecogListener中的状态回调
            case STATUS_FINISHED:
                if (msg.arg2 == 1) {
                    mRecorfView.cancel();
                    VoiceBean bean = JSON.parseObject(msg.obj.toString(),VoiceBean.class);
                    if(!"".equals(bean.getErrorCode())&&bean.getErrorCode() != null){
                        txtvoice.setText("错误码："+bean.getErrorCode()+";"+bean.getError());
                    }else{
                        GetVoiceMessage(bean.getSuccess());
                    }
                }
                status = msg.what;
                break;
            case STATUS_NONE:
            case STATUS_READY:
            case STATUS_SPEAKING:
            case STATUS_RECOGNITION:
                status = msg.what;
                break;
            default:
                break;
        }
    }
}
