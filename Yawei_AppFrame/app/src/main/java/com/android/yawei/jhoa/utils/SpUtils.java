package com.android.yawei.jhoa.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * 配置属性文件工具类
 * 
 * @author sn
 * 
 */
public class SpUtils {

	public static final String SETTING = "sp_setting";
	
	public static String getString(Context context, String key, String def){
		SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
		return sp.getString(key, def);
	}

	public static void setString(Context context, String key, String val){
		SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
		SharedPreferences.Editor e = sp.edit();
		e.putString(key, val);
		e.commit();
	}

	public static boolean getBoolean(Context context, String key, boolean def){
		SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
		return sp.getBoolean(key, def);
	}

	public static void setBoolean(Context context, String key, boolean val){
		SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
		SharedPreferences.Editor e = sp.edit();
		e.putBoolean(key, val);
		e.commit();
	}

	public static int getInteger(Context context, String key, int def){
		SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
		return sp.getInt(key, def);
	}

	public static void setInteger(Context context, String key, int val){
		SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
		SharedPreferences.Editor e = sp.edit();
		e.putInt(key, val);
		e.commit();
	}
	
	public static long getLong(Context context, String key, long def){
		SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
		return sp.getLong(key, def);
	}

	public static void setLong(Context context, String key, long val){
		SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
		SharedPreferences.Editor e = sp.edit();
		e.putLong(key, val);
		e.commit();
	}

	/**
	 * 用户自定义存储自定义的bitmap
	 * @param context
	 * @param bitmap
	 */
	public static void setBitmap(Context context,Bitmap bitmap,String account){
		SharedPreferences sharedPreferences=context.getSharedPreferences(account, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
		String headPicBase64=new String(Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT));
		editor.putString(account,headPicBase64);
		editor.commit();
	}
	/**获取用户自定义存储的头像*/
	public static Bitmap getBitmap(Context context,String account) {
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences(account, Context.MODE_PRIVATE);
			String headPic = sharedPreferences.getString(account, "");
			Bitmap bitmap = null;
			if (headPic != "") {
				byte[] bytes = Base64.decode(headPic.getBytes(), 1);
				bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			}
			return bitmap;
		}catch (Exception e){
			return  null;
		}
	}
	/**只清除存在的bitmap**/
	public static void PreferencesClear(Context context,String account){
		try {
			SharedPreferences preferences = context.getSharedPreferences(account, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = preferences.edit();
			editor.clear();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
