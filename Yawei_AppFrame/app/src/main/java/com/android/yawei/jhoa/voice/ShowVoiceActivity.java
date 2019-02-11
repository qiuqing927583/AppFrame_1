package com.android.yawei.jhoa.voice;

import android.Manifest;
import android.content.Intent;

import com.android.yawei.jhoa.utils.VerifyPermissions;
import com.android.yawei.jhoa.voice.baidu.params.NluRecogParams;
import com.android.yawei.jhoa.voice.baidu.voice.ActivityRecog;
import com.android.yawei.jhoa.voice.baidu.voice.CommonRecogParams;

public class ShowVoiceActivity extends ActivityRecog{
    {
        enableOffline = true; // 请确认不使用离线命令词功能后，改为false
        // 改为false后需要勾选“本地语义文件”选项，同时可以勾选”扩展词条选项“
    }

    String voiceMessage;

    @Override
    protected CommonRecogParams getApiParams() {
        return new NluRecogParams(this);
    }

    @Override
    protected void InitChildView() {
        setFinishOnTouchOutside(false);
    }

    @Override
    protected void GetVoiceMessage(String message) {
        Intent intent = new Intent();
        intent.putExtra( "voice", message);
        setResult(RESULT_OK, intent);
        finish();
    }

    public ShowVoiceActivity(){
        super();
    }
}
