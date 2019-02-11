package com.android.yawei.jhoa.Interface;

/**
 * 附件上传回调
 * Created by Yusz on 2018-2-2.
 */

public interface UploadBigFileCallback {
    /**
     *
     * @param message 成功消息
     * @param fileGuid 成功的fileguid后续操作使用
     */
    public void BigFileUpSucceed(String message,String fileGuid,String sign);
    public void BigFileUpError(String message,String sign);
}
