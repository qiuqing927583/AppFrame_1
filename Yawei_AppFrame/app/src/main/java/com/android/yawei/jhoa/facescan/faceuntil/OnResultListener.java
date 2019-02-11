/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.android.yawei.jhoa.facescan.faceuntil;


import com.android.yawei.jhoa.facescan.exception.FaceError;

public interface OnResultListener<T> {
    void onResult(T result);

    void onError(FaceError error);
}
