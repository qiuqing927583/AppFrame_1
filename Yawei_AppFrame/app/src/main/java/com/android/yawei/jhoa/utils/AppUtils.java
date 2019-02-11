package com.android.yawei.jhoa.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 获取APP信息辅助类
 * Created by Yusz on 2018-3-6.
 */

public class AppUtils {
    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context){
        try{
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return "办公";
    }
}
