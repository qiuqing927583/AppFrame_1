package com.android.yawei.jhoa.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.yawei.zxing.client.CaptureActivity;
import com.yawei.zxing.client.QRCodeIntent;

import java.util.ArrayList;

/**
 * 手动获取授权
 * Created by Yusz on 2018-1-2.
 */

public class VerifyPermissions {

    public final static int mrequestCode = 123;

    private static String[] PERMISSIONS1 = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
//            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static String[] PERMISSIONS2 = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            };

    /**
     * 检测权限是否开启
     * @param activity
     * @param requestCode 可以为0用默认的是mrequestCode：123,如果传入了值获取权限回调使用传入的值
     * @return true所有需要的权限都已经授权，false有权限未授权
     */
    public static boolean VerifyStoragePermissions(Activity activity,int requestCode) {
        try {
            int m_requestCode ;
            if(requestCode==0){
                m_requestCode = mrequestCode;
            }else{
                m_requestCode = requestCode;
            }
            ArrayList<String> toApplyList = new ArrayList<String>();
            if(Constants.OpenBaiduViceo){
                for (String perm : PERMISSIONS1) {
                    if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(activity, perm)) {
                        // 进入到这里代表没有权限.
                        toApplyList.add(perm);
                    }
                }
            }else{
                for (String perm : PERMISSIONS2) {
                    if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(activity, perm)) {
                        // 进入到这里代表没有权限.
                        toApplyList.add(perm);
                    }
                }
            }
            String[] tmpList = new String[toApplyList.size()];
            if (!toApplyList.isEmpty()) {
                ActivityCompat.requestPermissions(activity, toApplyList.toArray(tmpList), m_requestCode);
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *根据传进来的数组获取数组中权限
     * @param activity
     * @param permissions
     * @param requestCode 可以为0用默认的是mrequestCode：123,如果传入了值获取权限回调使用传入的值
     */
    public static void VerifyPermissions(Activity activity,String[] permissions,int requestCode) {
        try {
            int m_requestCode ;
            if(requestCode==0){
                m_requestCode = mrequestCode;
            }else{
                m_requestCode = requestCode;
            }
            //检测是否有写的权限
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                        Toast.makeText(activity, "您已禁止所需要权限，需要重新开启",Toast.LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions(activity, permissions, m_requestCode);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 二维码
     * 权限申请,相机，存储，蜂鸣
     * @param mactivity
     * @param REQUEST_CODE
     */
	public static void RequestPermissionZxing(Activity mactivity , int REQUEST_CODE) {
		String[] permission = new String[]{
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.VIBRATE,
				Manifest.permission.CAMERA,
		};
		boolean flag = true;
		for (String aPermission : permission) {
			if (ActivityCompat.checkSelfPermission(mactivity, aPermission) != PackageManager.PERMISSION_GRANTED) {
				flag = false;
				if (ActivityCompat.shouldShowRequestPermissionRationale(mactivity, aPermission)) {
					Toast.makeText(mactivity, "您已禁止所需要权限，需要重新开启",Toast.LENGTH_LONG).show();
				} else {
					ActivityCompat.requestPermissions(mactivity, permission, REQUEST_CODE);
				}
			} else {
				flag &= flag;
			}
		}
		if (flag) {
			Intent intent = new Intent(mactivity, CaptureActivity.class);
			//预览框的宽高
			intent.putExtra(QRCodeIntent.FRAME_WIDTH, 250);
			intent.putExtra(QRCodeIntent.FRAME_HEIGHT, 250);
			intent.putExtra(QRCodeIntent.SET_RESULT, true);
			mactivity.startActivityForResult(intent, REQUEST_CODE);
		}
	}
}
