package com.android.yawei.jhoa.utils;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*********************
 * 
 * @author Yusz
 * 创建时间：2015-6-25
 * 功能：获取内置和外置sdcard
 *
 */
public class SDCardUtil {

	/**
     * 内置
     */
    static String SDCARD_INTERNAL = "internal";

    /**
     * 外置
     */
    static String SDCARD_EXTERNAL = "external";

    /**
     * API14以下通过读取Linux的vold.fstab文件来获取SDCard信息
     * 
     * @return
     */
    public static HashMap<String, SDCardInfo> getSDCardInfoBelow14(){
        HashMap<String, SDCardInfo> sdCardInfos = new HashMap<String, SDCardInfo>();
        BufferedReader bufferedReader = null;
        List<String> dev_mountStrs = null;
        try {
            // API14以下通过读取Linux的vold.fstab文件来获取SDCard信息
            bufferedReader = new BufferedReader(new FileReader(Environment.getRootDirectory().getAbsoluteFile()
                    + File.separator + "etc" + File.separator + "vold.fstab"));
            dev_mountStrs = new ArrayList<String>();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("dev_mount")) {
                    dev_mountStrs.add(line);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; dev_mountStrs != null && i < dev_mountStrs.size(); i++) {
            SDCardInfo sdCardInfo = new SDCardInfo();
            String[] infoStr = dev_mountStrs.get(i).split(" ");
            sdCardInfo.setLabel(infoStr[1]);
            sdCardInfo.setMountPoint(infoStr[2]);
            if (sdCardInfo.getMountPoint().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                sdCardInfo.setMounted(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
                sdCardInfos.put(SDCARD_INTERNAL, sdCardInfo);
            } else if (sdCardInfo.getMountPoint().startsWith("/mnt")
                    && !sdCardInfo.getMountPoint().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                File file = new File(sdCardInfo.getMountPoint() + File.separator + "temp");
                if (file.exists()) {
                    sdCardInfo.setMounted(true);
                } else {
                    if (file.mkdir()) {
                        file.delete();
                        sdCardInfo.setMounted(true);
                    } else {
                        sdCardInfo.setMounted(false);
                    }
                }
                sdCardInfos.put(SDCARD_EXTERNAL, sdCardInfo);
            }
        }
        return sdCardInfos;
    }

    // API14以上包括14通过改方法获取
    public static List<SDCardInfo> getSDCardInfo(Context context)throws Exception {
        List<SDCardInfo> sdCardInfos = new ArrayList<SDCardInfo>();
        String[] storagePathList = null;
        try {
            StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Method getVolumePaths = storageManager.getClass().getMethod("getVolumePaths");
            storagePathList = (String[]) getVolumePaths.invoke(storageManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (storagePathList != null && storagePathList.length > 0) {
        	for(int i=0;i<storagePathList.length;i++){
        		String mSDCardPath = storagePathList[i];
                SDCardInfo internalDevInfo = new SDCardInfo();
                internalDevInfo.setMountPoint(mSDCardPath);
                internalDevInfo.setMounted(checkSDCardMount14(context, mSDCardPath));
                sdCardInfos.add(internalDevInfo);
        	}
        }
        return sdCardInfos;
    }

    /**
     * @Description:判断SDCard是否挂载上,返回值为true证明挂载上了，否则未挂载
     * @Date 2013-11-12
     * @param context 上下文
     * @param mountPoint 挂载点
     */
    protected static boolean checkSDCardMount14(Context context, String mountPoint)throws Exception {
        if (mountPoint == null) {
            return false;
        }
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Method getVolumeState = storageManager.getClass().getMethod("getVolumeState", String.class);
            String state = (String) getVolumeState.invoke(storageManager, mountPoint);
            return Environment.MEDIA_MOUNTED.equals(state);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

