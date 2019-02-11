package com.android.yawei.jhoa.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/************
 * 下载附件
 * @author Yusz
 *
 */
public class SysExitUtil {
	//建立一个public static的list用来放activity 
	public static List<Activity> activityList = new ArrayList<Activity>(); 
    /*******
     * 添加activity
     * @param activity
     */
    public  static void AddActivity(Activity activity){
    	int size=activityList.size(); 
    	boolean   isNotSeam = true;
    	if(size>0){
	         for(int i=0;i<size;i++){        
	            if(activityList.get(i).getLocalClassName().equals(activity.getLocalClassName())){     //返回类的名称删除软件包的前缀。       
	            	isNotSeam = false;
	            	break;
	            }
	         }
	         if(isNotSeam)
	        	 activityList.add(activity);
    	}else {
        	 activityList.add(activity);    
		}
    }
	/******
	 * finish所有list中的activity
	 */
	public static void FinishActivity(){
		int siz=activityList.size();
		for(int i=0;i<siz;i++){
			if(activityList.get(i)!=null){
				((Activity) activityList.get(i)).finish();
			}
		}
		activityList.clear();
	}
	//判断activity是否已经finish
	public static void RemoveActivity(Activity activity){
		for(int i=0;i<activityList.size();i++){
			if(activityList.get(i).getLocalClassName().equals(activity.getLocalClassName())){
				activityList.remove(i);
				break;
			}
		}
	}

   /**************
    * 去出空格，记事本使用
    * @param str
    * @return
    */
    public static String replaceBlankTrem(String str) {  
        String dest = "";  
        if (str!=null) {  
            Pattern p = Pattern.compile("\t");  
            Matcher m = p.matcher(str);  
            dest = m.replaceAll("");  
        }  
        return dest;  
    }
    /**
     * 判断是否是空字符串 null和"" 都返回 true
     *
     * @author Robin Chang
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (s != null && !s.equals("")) {
            return false;
        }
        return true;
    }


	/**
	 * 创建目录
	 * 
	 * @param path
	 */
	public static void createPath(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}


	/******
	 * 传入包名根据报名判断应用是否安装
	 * @param uri
	 * @param context
	 * @return
	 */
	public static boolean isAppInstalled(String uri,Context context) {
		PackageManager pm = context.getPackageManager();
		boolean installed =false;
		try {
			pm.getPackageInfo(uri,PackageManager.GET_ACTIVITIES);
			installed =true;
		} catch(PackageManager.NameNotFoundException e) {
			installed =false;
		}catch (Exception e){
			installed = false;
			e.printStackTrace();
		}
		return installed;
	}
	/*******
	 * 去出换行和空格，记事本使用
	 * @param str
	 * @return
	 */
    public static String replaceBlank(String str) {  
        String dest = "";  
        if (str!=null) {  
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");  
            Matcher m = p.matcher(str);  
            dest = m.replaceAll("");  
        }  
        return dest;  
    } 

	/********防止按钮多次点击*******/
	private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 800) {   
            return true;   
        }   
        lastClickTime = time;   
        return false;   
    }
    /**
     * 程序是否在前台运行
     * 
     * @return 
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
                return false;
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
	/**
	 * 加载本地图片
	 * 图片转bitmap
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获取设备的密度
	 * @param context
	 * @return
	 */
	public static boolean GetDeviceDensity(Context context ,int dpi){
		DisplayMetrics metric =  context.getResources().getDisplayMetrics();
		int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240,一般高密度是320，2K屏）
		if(dpi==0){
			if(densityDpi>=240){
				return true;
			}else{
				return false;
			}
		}else{
			if(densityDpi>=dpi){
				return true;
			}else{
				return false;
			}
		}
	}
}
