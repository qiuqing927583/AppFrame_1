package com.android.yawei.jhoa.utils;

import android.content.Context;
import android.content.pm.PackageInfo;

import java.util.List;
import java.util.Map;

/********************
 * 获取系统安装的所有的app包名
 * @author Yusz
 *
 */
public class PakageInfoProvider {
    Map<String, Object> item;
    private Context context;
    
    public PakageInfoProvider(Context context){
    	this.context = context;
    }
    /******
     * 返回所有的系统Android的app包名用分号分割
     * @return
     */
    public String listPackages() {
    	String apps = getInstalledApps(false); 
        
        return apps;
    }

    private String getInstalledApps(boolean getSysPackages) {
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        String packname = "";
        for (int i = 0; i < packs.size(); i++) {
        	packname += packs.get(i).packageName+";";
        }
        return packname;
    }

}
