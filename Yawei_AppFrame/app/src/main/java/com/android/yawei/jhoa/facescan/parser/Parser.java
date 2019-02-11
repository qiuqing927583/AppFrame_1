/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.android.yawei.jhoa.facescan.parser;


import com.android.yawei.jhoa.facescan.exception.FaceError;

/**
 * JSON解析
 * @param <T>
 */
public interface Parser<T> {
    T parse(String json) throws FaceError;
}
