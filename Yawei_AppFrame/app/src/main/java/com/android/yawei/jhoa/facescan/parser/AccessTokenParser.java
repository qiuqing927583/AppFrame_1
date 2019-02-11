/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.android.yawei.jhoa.facescan.parser;

import com.android.yawei.jhoa.facescan.exception.FaceError;
import com.android.yawei.jhoa.facescan.model.AccessToken;
import com.android.yawei.jhoa.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class AccessTokenParser implements Parser<AccessToken> {
    @Override
    public AccessToken parse(String json) throws FaceError {
        try {
            AccessToken accessToken = new AccessToken();
            accessToken.setJson(json);
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject != null) {

                accessToken.setAccessToken(jsonObject.optString("access_token"));
                accessToken.setExpiresIn(jsonObject.optInt("expires_in"));
                Constants.ACCESS_TOKEN = jsonObject.optString("access_token");
                return accessToken;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            FaceError error = new FaceError(FaceError.ErrorCode.JSON_PARSE_ERROR, "Json parse error", e);
            throw error;
        }
        return null;
    }
}
