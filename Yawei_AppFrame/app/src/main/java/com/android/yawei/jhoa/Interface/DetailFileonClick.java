package com.android.yawei.jhoa.Interface;

import com.android.yawei.jhoa.bean.Attachment;

/**
 * TODO
 * class : DetailFileonClick
 * author: Yusz
 * Date: 2018-11-23
 */

public interface DetailFileonClick {
    //点击附件名称
    public void onClickFileName(Attachment attachment);
    //点击签批
    public void onClickFileHandWrite(Attachment attachment);
}
