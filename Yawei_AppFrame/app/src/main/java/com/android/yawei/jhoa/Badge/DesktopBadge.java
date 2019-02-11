package com.android.yawei.jhoa.Badge;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.yawei.jhoa.mobile.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Yusz on 2018-5-4.
 */

public class DesktopBadge {

    private static String OSName=null;
    private static int NOTIFI_ID = 0;
    private static String SYSTEM_XIAOMI="XIAOMI";
    private static String SYSTEM_SAMSUNG="SAMSUNG";
    private static String SYSTEM_HUAWEI_HONOR="HONOR";
    private static String SYSTEM_HUAWEI="HUAWEI";
    private static String SYSTEM_NOVA="NOVA";
    private static String SYSTEM_SONY="SONY";
    private static String SYSTEM_VIVO="VIVO";
    private static String SYSTEM_OPPO="OPPO";
    private static String SYSTEM_LG="LG";
    private static String SYSTEM_ZUK="ZUK";
    private static String SYSTEM_HTC="HTC";
    private static String SYSTEM_EBEN="EBEN";

    //显示Bubble
    public static void showBubble(Context context,int num) {
        OSName = android.os.Build.BRAND.trim().toUpperCase();
        if (num < 0) num = 0;
        if (num > 99) num = 99;
        if (OSName != null) {
            Notification.Builder builder = new Notification.Builder(context).setContentTitle("待办").setContentText("您有"+num+"个未读件").setSmallIcon(R.drawable.android_desktop);
            Notification notification = builder.build();
            if (OSName.equals(SYSTEM_XIAOMI)) {
                //小米
                setBadgeOfXiaomi(context,notification, num);
            } else if (OSName.equals(SYSTEM_SAMSUNG) || OSName.equals(SYSTEM_LG)) {
                //三星和LG
                setBadgeOfSamsung(context, notification, num);
            } else if (OSName.equals(SYSTEM_HUAWEI_HONOR) || OSName.equals(SYSTEM_HUAWEI)) {
                //华为荣耀和华为
                setBadgeOfHuaWei(context, notification, num);
            } else if (OSName.equals(SYSTEM_SONY)) {
                //索尼
                setBadgeOfSony(context, notification, num);
            } else if (OSName.equals(SYSTEM_VIVO)) {
                //VIVO
                setBadgeOfVIVO(context, notification, num);
            } else if (OSName.equals(SYSTEM_OPPO)) {
                //OPPO
                setBadgeOfOPPO(context, notification, num);
            } else if (OSName.equals(SYSTEM_ZUK)) {
                //ZUK
                setBadgeOfZUK(context, notification, num);
            } else if (OSName.equals(SYSTEM_HTC)) {
                //HTC
                setBadgeOfHTC(context, notification, num);
            } else if (OSName.equals(SYSTEM_NOVA)) {
                //NOVA
                setBadgeOfNOVA(context, notification, num);
            } else if(OSName.contains(SYSTEM_EBEN)){
                setBadgeOfEBEN(context,num);
            }else {
                //其他的
                setBadgeOfDefault(context, notification, num);
            }
        }
    }
    //小米
    private static void setBadgeOfXiaomi(final Context context, Notification notification,int num){
        try {

            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, num);
            NotificationManager notifyMgr = (NotificationManager)(context.getSystemService(context.NOTIFICATION_SERVICE));
            if(num!=0)
                notifyMgr.notify(NOTIFI_ID, notification);
            else
                notifyMgr.cancel(NOTIFI_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void setBadgeOfVIVO(Context context, Notification notification, int num){
//        Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
//        intent.putExtra("packageName", context.getPackageName());
//        intent.putExtra("className", "");
//        intent.putExtra("notificationNum", num);
//        context.sendBroadcast(intent);
        try {
            Intent localIntent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            localIntent.putExtra("packageName", context.getPackageName());
            localIntent.putExtra("className", getLauncherClassName(context));
            localIntent.putExtra("notificationNum", num);
            context.sendBroadcast(localIntent);

            NotificationManager notifyMgr = (NotificationManager)(context.getSystemService(context.NOTIFICATION_SERVICE));
            if(num!=0)
                notifyMgr.notify(NOTIFI_ID, notification);
            else
                notifyMgr.cancel(NOTIFI_ID);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //三星
    private static void setBadgeOfSamsung(Context context,Notification notification,int num) {
        // 获取你当前的应用
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }

        try {
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", num);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", launcherClassName);
            context.sendBroadcast(intent);

            NotificationManager notifyMgr = (NotificationManager)(context.getSystemService(NOTIFICATION_SERVICE));
            if(num!=0)
                notifyMgr.notify(NOTIFI_ID, notification);
            else
                notifyMgr.cancel(NOTIFI_ID);


        }catch (Exception e){
            e.printStackTrace();
            Log.e("SAMSUNG" + " Badge error", "set Badge failed");
        }
    }
    //华为 荣耀系列
    private static void setBadgeOfHuaWei(Context context, Notification notification,int num) {


        //检测EMUI版本是否支持
//        boolean isSupportedBade=false;
//        try {
//            PackageManager manager = context.getPackageManager();
//            PackageInfo info = manager.getPackageInfo("com.huawei.android.launcher", 0);
//            if(info.versionCode>=63029){
//                isSupportedBade = true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if(!isSupportedBade){
//            Log.i("badgedemo", "not supported badge!");
//            return;
//        }

        try {
            Bundle localBundle = new Bundle();
            localBundle.putString("package", context.getPackageName());
            localBundle.putString("class", getLauncherClassName(context));
            localBundle.putInt("badgenumber", num);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, localBundle);

            NotificationManager notifyMgr = (NotificationManager) (context.getSystemService(NOTIFICATION_SERVICE));
            if (num != 0) notifyMgr.notify(NOTIFI_ID, notification);
            else notifyMgr.cancel(NOTIFI_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //OPPO *只支持部分机型
    private static void setBadgeOfOPPO(Context context, Notification notification,int num){
        try {

            if (num == 0) {
                num = -1;
            }

            Intent intent = new Intent("com.oppo.unsettledevent");
            intent.putExtra("pakeageName", context.getPackageName());
            intent.putExtra("number", num);
            intent.putExtra("upgradeNumber", num);
            if (canResolveBroadcast(context, intent)) {
                context.sendBroadcast(intent);
            } else {

                try {
                    Bundle extras = new Bundle();
                    extras.putInt("app_badge_count", num);
                    context.getContentResolver().call(Uri.parse("content://com.android.badge/badge"), "setAppBadgeCount", null, extras);

                    NotificationManager notifyMgr = (NotificationManager)(context.getSystemService(NOTIFICATION_SERVICE));
                    if(num!=0)
                        notifyMgr.notify(NOTIFI_ID, notification);
                    else
                        notifyMgr.cancel(NOTIFI_ID);
                } catch (Throwable th) {
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //索尼
    private static void setBadgeOfSony(Context context,Notification notification, int num){
        String numString="";
        String activityName = getLauncherClassName(context);
        if (activityName == null){
            return;
        }
        Intent localIntent = new Intent();
        boolean isShow = true;
        if (num < 1){
            numString = "";
            isShow = false;
        }else if (num > 99){
            numString = "99";
        }
        try {
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);
            localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", activityName);
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", numString);
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());
            context.sendBroadcast(localIntent);

            NotificationManager notifyMgr = (NotificationManager)(context.getSystemService(NOTIFICATION_SERVICE));
            if(num!=0)
                notifyMgr.notify(NOTIFI_ID, notification);
            else
                notifyMgr.cancel(NOTIFI_ID);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //ZUK
    private static void setBadgeOfZUK(Context context,Notification notification, int num){
        final Uri CONTENT_URI = Uri.parse("content://com.android.badge/badge");
        try {
            Bundle extra = new Bundle();
            extra.putInt("app_badge_count", num);
            context.getContentResolver().call(CONTENT_URI, "setAppBadgeCount", null, extra);

            NotificationManager notifyMgr = (NotificationManager)(context.getSystemService(NOTIFICATION_SERVICE));
            if(num!=0)
                notifyMgr.notify(NOTIFI_ID, notification);
            else
                notifyMgr.cancel(NOTIFI_ID);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //HTC
    private static void setBadgeOfHTC(Context context,Notification notification,int num){

        try {
            Intent intent1 = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
            intent1.putExtra("com.htc.launcher.extra.COMPONENT", context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().flattenToShortString());
            intent1.putExtra("com.htc.launcher.extra.COUNT", num);

            Intent intent = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
            intent.putExtra("packagename", context.getPackageName());
            intent.putExtra("count", num);

            if (canResolveBroadcast(context, intent1) || canResolveBroadcast(context, intent)) {
                context.sendBroadcast(intent1);
                context.sendBroadcast(intent);
            } else {
                Log.e("HTC" + " Badge error", "unable to resolve intent: " + intent.toString());
            }

            NotificationManager notifyMgr = (NotificationManager)(context.getSystemService(NOTIFICATION_SERVICE));
            if(num!=0)
                notifyMgr.notify(NOTIFI_ID, notification);
            else
                notifyMgr.cancel(NOTIFI_ID);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //NOVA
    private static void setBadgeOfNOVA(Context context,Notification notification,int num){
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put("tag", context.getPackageName()+ "/" +getLauncherClassName(context));
            contentValues.put("count", num);
            context.getContentResolver().insert(Uri.parse("content://com.teslacoilsw.notifier/unread_count"), contentValues);

            NotificationManager notifyMgr = (NotificationManager)(context.getSystemService(NOTIFICATION_SERVICE));
            if(num!=0)
                notifyMgr.notify(NOTIFI_ID, notification);
            else
                notifyMgr.cancel(NOTIFI_ID);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void setBadgeOfEBEN(Context context,int num){
        try{
            Intent intent = new Intent("ebensz.intent.action.ACTION_UPDATE_UNREADITEMS");
            intent.putExtra("name", getLauncherClassName(context));
            intent.putExtra("count", num);
            context.sendBroadcast(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //其他
    private static void setBadgeOfDefault(Context context,Notification notification,int num){

        try {
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", num);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", getLauncherClassName(context));
            if (canResolveBroadcast(context, intent)) {
                context.sendBroadcast(intent);
            } else {
                Log.e("Default" + " Badge error", "unable to resolve intent: " + intent.toString());
            }

            NotificationManager notifyMgr = (NotificationManager)(context.getSystemService(NOTIFICATION_SERVICE));
            if(num!=0)
                notifyMgr.notify(NOTIFI_ID, notification);
            else
                notifyMgr.cancel(NOTIFI_ID);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Default" + " Badge error", "set Badge failed");
        }

    }
    //获取类名,入口类
    private static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }
        return info.activityInfo.name;
    }
    //广播
    private static boolean canResolveBroadcast(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
        return receivers != null && receivers.size() > 0;
    }
}
