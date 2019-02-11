package com.android.yawei.jhoa.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.yawei.jhoa.mobile.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**************
 * 下载更新新版本
 * @author Yusz
 *
 */
public class DownloadUpdate extends Thread implements Runnable{

	public static final int ERROR = 14;
	public static final int DOWNLOADPRO = 15;
	public static final int UNZIP = 17;

	private String filename;
	private String httpUrl;
	private String apkLength;
	private static String SDCardRoot;
	static Context context;
	private static Handler handler=null;

	@SuppressWarnings("static-access")
	public DownloadUpdate(String filename, String httpUrl, String apkLength, Context context,Handler hdl) {
		super();
		this.filename = filename;
		this.httpUrl = httpUrl;
		this.apkLength = apkLength;
		this.context = context;
		this.handler = hdl;
		if(filename.contains(".zip")){
			SDCardRoot = FileUtils.GetSDPath()+"jhoaMobile/zip/";
		}else{
			SDCardRoot = FileUtils.GetSDPath()+"jhoaMobile/version/";
		}

	}

	@Override
	public void run() {
		int i = downLoadFile(filename, httpUrl, apkLength);
		if(i == 1){
			setup(context, filename);
		}else{
			handler.sendEmptyMessage(ERROR);
		}
	}

	@SuppressWarnings("deprecation")
	public static int downLoadFile(String filename, String httpUrl, String apkLength){
		File jhoa = null;
		if ((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))){
			jhoa = new File(SDCardRoot);
			if (!jhoa.exists()){
				jhoa.mkdirs(); //创建目录
			}
		}

		File file = null;
		long length = 0;
		long tempFileLength = 0;

		if ((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))){
			file = new File(SDCardRoot + filename);
			length=Long.parseLong(apkLength);
			if (file.exists() && file.length() > 0){
				if(jhoa.exists()){
					File[]files=jhoa.listFiles();
					for(int i=0;i<files.length;i++){
						 File Deletefile = files[i];
						 Deletefile.delete();  //之前下载过其他的版本，将其  删除
					}
				}
			}
		}
		if(file==null||!file.exists()){
			//file为空，内存卡不存在，下载到内存；!file.exists()标示以前存在旧版本的情况,删除之后，重新下载到内存卡上
			try{
				String appdownname  = null;
				try {
					appdownname = java.net.URLEncoder.encode(httpUrl.substring(httpUrl.lastIndexOf("/") + 1, httpUrl.length()),"utf-8");
					appdownname = appdownname.replaceAll("\\+", "%20").replaceAll("%3A", ":").replaceAll("%2F", "/");
				} catch (UnsupportedEncodingException e) {
					handler.sendEmptyMessage(ERROR);
					e.printStackTrace();
					return 0;
				}
				String downloadUrl = httpUrl.substring(0, httpUrl.lastIndexOf("/") + 1)+appdownname;
				URL url = new URL(downloadUrl);
				try{
					HttpURLConnection conn = NetworkUtils.getConnection(context, url);
					if (conn == null){
						return 0;
					}
					conn.setDoInput(true);
					conn.connect();
					length = Long.parseLong(apkLength);
					if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
						InputStream is = conn.getInputStream();
						FileOutputStream fos = null;
						if (!(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))){
							// 内存卡不存在
							fos = context.openFileOutput(filename, Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
						}else{
							fos = new FileOutputStream(file);
						}
						byte[] bt = new byte[1024];
						int i = 0;
						int progresCount = 0;
						int times = 0;
						while ((i = is.read(bt)) > 0){
							fos.write(bt, 0, i);
							tempFileLength += i;
							// if((times == 256)||(tempFileLength == length)){
							if (times == 8){
								// 更新通知中的进度条
								int progress = (int) (tempFileLength * 100 / length);
								if (progresCount < progress){
									progresCount = progress > 100 ? 100 : progress;
									// 下载未完成，利用Handler在主线程中更新
									Message msg = new Message();
									msg.what =DOWNLOADPRO;
									Bundle b = new Bundle();
									b.putInt("progressValue", progresCount);
									b.putString("apkName", filename);
									msg.setData(b);
									handler.sendMessage(msg);
								}
								times = 0;
								continue;
							}
							times++;
						}// while
						/*
						 * 防止恰巧文件大小是256的倍数 已经发送过文件下载完成的消息
						 */
						if (times != 0){
							// 下载完成，利用Handler在主线程中更新
							Message msg = new Message();
							msg.what = DOWNLOADPRO;
							Bundle b = new Bundle();
							b.putInt("progressValue", 100);
							b.putString("apkName", filename);
							msg.setData(b);
							handler.sendMessage(msg);
						}// if
						fos.flush();
						fos.close();
						is.close();
						conn.disconnect();
					}
				}catch (IOException e){
					e.printStackTrace();
					return 0;
				}
			}catch (MalformedURLException e){
				e.printStackTrace();
				return 0;
			}
		}
		if (file == null && tempFileLength != length){
			// 小于改成不等于的情况
			return 0;
		}
		return 1;
	}

	public static int setup(Context context, String apkname){ // SD卡上程序安装
		//故意添加有的时候到了99%进度条不消失
		Message strmsg = new Message();
		strmsg.what = DOWNLOADPRO;
		Bundle b = new Bundle();
		b.putInt("progressValue", 100);
		b.putString("apkName", "");
		strmsg.setData(b);
		handler.sendMessage(strmsg);

		if(apkname.contains(".zip")){
			Message msg = new Message();
			msg.what=UNZIP;
			msg.obj=apkname;
			handler.sendMessage(msg);
			return 1;
		}
		File file = null;
		if ((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))){
			file = new File(SDCardRoot + apkname);
			if (file.exists()){
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				Uri data;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					/**Android 7.0以上的方式**/
					String fileprovider = context.getResources().getString(R.string.file_provider_name);
					data = FileProvider.getUriForFile(context, fileprovider, file);
					context.grantUriPermission("com.yawei.jhoa.mobile", data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, data);
                /*mime类型为  application/x-freemind*/
					intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				} else {
					data = Uri.fromFile(file);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				}
//				Uri uri = Uri.fromFile(file);
				intent.setDataAndType(data, "application/vnd.android.package-archive");
				context.startActivity(intent);
				return 1;
			}
		}else{
			file = new File("/data/data/"+ Constants.PACKAGE_NAME+"/files/" + apkname);
			if(file.exists()){
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
//		        Uri uri =  Uri.fromFile(file);
				Uri data;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					/**Android 7.0以上的方式**/
					String fileprovider = context.getResources().getString(R.string.file_provider_name);
					data = FileProvider.getUriForFile(context, fileprovider, file);
					context.grantUriPermission("com.yawei.jhoa.mobile", data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, data);
                /*mime类型为  application/x-freemind*/
					intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				} else {
					data = Uri.fromFile(file);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				}
		        intent.setDataAndType(data,"application/vnd.android.package-archive");
		        context.startActivity(intent);
		        return 1;
			}
		}
		return 0;
	}

}
