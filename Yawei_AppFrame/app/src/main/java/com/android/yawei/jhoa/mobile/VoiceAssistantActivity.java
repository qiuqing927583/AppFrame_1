package com.android.yawei.jhoa.mobile;

import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.android.yawei.jhoa.adapter.BaseActivity;
import com.android.yawei.jhoa.adapter.VoiceSayAdapter;
import com.android.yawei.jhoa.addressbook.GroupFragmentActivity;
import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.voice.baidu.control.MyRecognizer;
import com.android.yawei.jhoa.voice.baidu.control.MyWakeup;
import com.android.yawei.jhoa.voice.baidu.ui.BaiduASRDialogTheme;
import com.android.yawei.jhoa.voice.baidu.ui.ColorFilterGenerator;
import com.android.yawei.jhoa.voice.baidu.ui.SDKAnimationView;
import com.android.yawei.jhoa.voice.baidu.voice.IStatus;
import com.android.yawei.jhoa.voice.baidu.voice.MessageStatusRecogListener;
import com.android.yawei.jhoa.voice.baidu.voice.StatusRecogListener;
import com.android.yawei.jhoa.voice.baidu.voiceutil.VoiceBean;
import com.android.yawei.jhoa.voice.baidu.wakeup.IWakeupListener;
import com.android.yawei.jhoa.voice.baidu.wakeup.RecogWakeupListener;
import com.baidu.speech.asr.SpeechConstant;
import com.yawei.jhoa.mobile.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO 语音唤醒
 * class description: VoiceAssistantActivity
 * author: Yusz
 * Date: 2018-8-8
 */

public class VoiceAssistantActivity extends BaseActivity implements IStatus {

    private LinearLayout LinTopBack;
    private SDKAnimationView mVoiceWaveView;
    private ListView listview;
    private List<VoiceBean> voiceData;
    private VoiceSayAdapter adapter;
    private int mTheme = 0;
    private boolean recognizerIsRun;//判断录音是否在运行

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voiceassistant);
        try {
            recognizerIsRun = false;
            voiceData = new ArrayList<>();
//            VerifyPermissions.VerifyStoragePermissions(VoiceAssistantActivity1.this);
            InitView();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void InitView(){
        mVoiceWaveView = (SDKAnimationView)findViewById(R.id.waveview);
        mVoiceWaveView.setThemeStyle(mTheme);
        mVoiceWaveView.setVisibility(View.INVISIBLE);
        adjustThemeColor();
        mVoiceWaveView.setVisibility(View.VISIBLE);
        mVoiceWaveView.startInitializingAnimation();
        mVoiceWaveView.startPreparingAnimation();
        listview = (ListView)findViewById(R.id.listview);
        adapter = new VoiceSayAdapter(VoiceAssistantActivity.this,voiceData);
        listview.setAdapter(adapter);
        LinTopBack = (LinearLayout)findViewById(R.id.LinTopBack);
        LinTopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //TODO 百度语音
    // 识别控制器，使用MyRecognizer控制识别的流程
    protected MyRecognizer myRecognizer;
    /**
     * 0: 方案1， 唤醒词说完后，直接接句子，中间没有停顿。
     * >0 : 方案2： 唤醒词说完后，中间有停顿，然后接句子。推荐4个字 1500ms
     * <p>
     * backTrackInMs 最大 15000，即15s
     */
    private int backTrackInMs = 3500;
    protected MyWakeup myWakeup;
    private void InitVoice()throws Exception{
        // 初始化识别引擎
        StatusRecogListener recogListener = new MessageStatusRecogListener(handler);
        // 改为 SimpleWakeupListener 后，不依赖handler，但将不会在UI界面上显示
        myRecognizer = new MyRecognizer(VoiceAssistantActivity.this, recogListener);

        IWakeupListener listener = new RecogWakeupListener(handler);
        myWakeup = new MyWakeup(VoiceAssistantActivity.this.getApplicationContext(), listener);
    }
    private void start() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下

        // params.put(SpeechConstant.ACCEPT_AUDIO_DATA,true);
        // params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME,true);
        // params.put(SpeechConstant.IN_FILE,"res:///com/baidu/android/voicedemo/wakeup.pcm");
        // params里 "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        if(myWakeup != null)
            myWakeup.start(params);
    }
    private void stop() {
        if(myWakeup != null) {
            myWakeup.stop();
        }
    }
    /**
     * 根据选定的主题，设置色调
     */
    private void adjustThemeColor() {
        float hue = 0;
        switch (mTheme) {
            case BaiduASRDialogTheme.THEME_BLUE_LIGHTBG:
                hue = 0;
                break;
            case BaiduASRDialogTheme.THEME_BLUE_DEEPBG:
                hue = 0;
                break;
            case BaiduASRDialogTheme.THEME_GREEN_LIGHTBG:
                hue = -108;
                break;
            case BaiduASRDialogTheme.THEME_GREEN_DEEPBG:
                hue = -109;
                break;
            case BaiduASRDialogTheme.THEME_RED_LIGHTBG:
                hue = 148;
                break;
            case BaiduASRDialogTheme.THEME_RED_DEEPBG:
                hue = 151;
                break;
            case BaiduASRDialogTheme.THEME_ORANGE_LIGHTBG:
                hue = -178;
                break;
            case BaiduASRDialogTheme.THEME_ORANGE_DEEPBG:
                hue = -178;
                break;
            default:
                break;
        }
        ColorMatrix cm = new ColorMatrix();
        ColorFilterGenerator.adjustColor(cm, 0, 0, 0, hue);
        ColorFilter filter = new ColorMatrixColorFilter(cm);
        mVoiceWaveView.setHsvFilter(filter);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what){
                    case -1:
                        listview.setSelection(listview.getBottom());
                        break;
                    case STATUS_FINISHED:
                        String strjs = (String)msg.obj;
                        VoiceBean bean = JSON.parseObject(strjs,VoiceBean.class);
                        if(bean.getSuccess().contains("写邮件")){
                            bean.setSuccess(bean.getSuccess().replace("协同办公",""));
                            bean.setType(1);
                            voiceData.add(bean);
                            adapter.Updata(voiceData);
                            adapter.notifyDataSetChanged();
//                            Intent intent = new Intent(VoiceAssistantActivity.this, WriteEmailActivity.class);
//                            startActivity(intent);
                            //唤醒写邮件
                        }else if(bean.getSuccess().contains("通讯录")){
                            bean.setSuccess(bean.getSuccess().replace("协同办公",""));
                            bean.setType(1);
                            voiceData.add(bean);
                            adapter.Updata(voiceData);
                            adapter.notifyDataSetChanged();
                            Intent intent = new Intent(VoiceAssistantActivity.this, GroupFragmentActivity.class);
                            intent.putExtra("sign","3");
                            startActivity(intent);
                        }else if(bean.getSuccess().equals("协同办公")){
                            bean.setSuccess("您已唤醒了助手，但是未告诉助手帮你做什么，如需继续请重新唤醒。");
                            bean.setType(0);
                            voiceData.add(bean);
                            adapter.Updata(voiceData);
                            adapter.notifyDataSetChanged();
                        }else{
                            bean.setSuccess("您说的是："+bean.getSuccess().replace("协同办公","")+",助手没明白你要做的事情，请重新唤醒并说出你想做的事情。");
                            bean.setType(0);
                            voiceData.add(bean);
                            adapter.Updata(voiceData);
                            adapter.notifyDataSetChanged();
                        }
                        handler.sendEmptyMessageDelayed(-1,500);
                        break;

                    case STATUS_WAKEUP_SUCCESS:
                        // 此处 开始正常识别流程
                        Map<String, Object> params = new LinkedHashMap<String, Object>();
                        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
                        params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
                        // 如识别短句，不需要需要逗号，使用1536搜索模型。其它PID参数请看文档
                        params.put(SpeechConstant.PID, 1536);
                        if (backTrackInMs > 0) { // 方案1， 唤醒词说完后，直接接句子，中间没有停顿。
                            params.put(SpeechConstant.AUDIO_MILLS, System.currentTimeMillis() - backTrackInMs);

                        }
                        myRecognizer.cancel();
                        myRecognizer.start(params);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    };

    public void onStart(){
        super.onStart();
    }
    public void onResume(){
        super.onResume();
        if(!recognizerIsRun){
            if(Constants.OpenBaiduViceo){
                try {
                    mTheme = 0;
                    InitVoice();
                    start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            recognizerIsRun = true;
        }
    }
    public void onPause(){
        super.onPause();
        if(Constants.OpenBaiduViceo){
            if(myWakeup != null){
                stop();
                myWakeup.release();
            }
            if(myRecognizer != null){
                myRecognizer.stop();
                myRecognizer.release();
            }
            recognizerIsRun = false;
        }
    }
//    public void onRestart(){
//        super.onRestart();
//        if(!recognizerIsRun){
//            if(Constants.OpenBaiduViceo){
//                try {
//                    mTheme = 0;
//                    InitVoice();
//                    start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            recognizerIsRun = true;
//        }
//    }
}
