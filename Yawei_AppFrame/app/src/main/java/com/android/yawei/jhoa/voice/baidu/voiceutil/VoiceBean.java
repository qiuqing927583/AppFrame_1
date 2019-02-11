package com.android.yawei.jhoa.voice.baidu.voiceutil;

/**
 * 自定义语音识别类
 */
public class VoiceBean {

    String errorCode;//错误码
    String error;//错误信息
    String success;//成功信息
    String diffTime;//耗时
    String successJson;//语音解析成功的原始json
    int type;//自己加的，只有在唤醒才有用，0个人语音输入，1助手给的提示

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getDiffTime() {
        return diffTime;
    }

    public void setDiffTime(String diffTime) {
        this.diffTime = diffTime;
    }

    public String getSuccessJson() {
        return successJson;
    }

    public void setSuccessJson(String successJson) {
        this.successJson = successJson;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
