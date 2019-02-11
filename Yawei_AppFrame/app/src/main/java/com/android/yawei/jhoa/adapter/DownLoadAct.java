package com.android.yawei.jhoa.adapter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import com.android.yawei.jhoa.utils.FileUtils;
import com.android.yawei.jhoa.utils.SysExitUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 下载
 * 
 * @author tql 2013-4-8
 */

public class DownLoadAct extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

//	protected int download(String urlStr, final String pathStr)throws Exception {
//		int index = urlStr.lastIndexOf("/");
//		// 获取文件名
//		final String service_name = urlStr.substring(index + 1, urlStr.length());
//		if (!ExistsFile(pathStr + service_name)) {
//			// 文件名转码 .防止http出现403
//			String service_name_encode = null;
//			try {
//				service_name_encode = java.net.URLEncoder.encode(service_name,"utf-8");
//				service_name_encode = service_name_encode
//						.replaceAll("\\+", "%20").replaceAll("%3A", ":")
//						.replaceAll("%2F", "/");
//			} catch (UnsupportedEncodingException e1) {
//				e1.printStackTrace();
//			}
//			String service_UrlSplited = urlStr.substring(0, index + 1);
//			// 真正可以下载的url
//			urlStr = service_UrlSplited + service_name_encode;
//			final String url = urlStr;
//			final String path = pathStr;
//			try {
//				URL myURL = new URL(url);
//				HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
//				// conn.setDoOutput(true);//是否需要在url上追加参数
//				conn.setDoInput(true);
//				// 设定传送的内容类型是可序列化的java对象 EOFEXception
//				conn.setRequestProperty("Content-type","application/x-java-serialized-object");
//				conn.connect();
//				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
//					return 0;
//				}
//				InputStream is = conn.getInputStream();
//				if (is == null) {
//					return 0;
//				}
//				FileOutputStream fos = new FileOutputStream(path + service_name);
//				// 把数据存入路径+文件名
//				byte buf[] = new byte[1024];
//				do {
//					// 循环读取
//					int numread = is.read(buf);
//					if (numread == -1) {
//						break;
//					}
//					fos.write(buf, 0, numread);
//				} while (true);
//				is.close();
//				fos.close();
//				try {
//					File file = new File(pathStr + service_name);
//					FileUtils.openFile(this,file);
//				} catch (Exception ex) {
//					return 2;
//				}
//			}  catch (IOException e) {
//				e.printStackTrace();
//				return 0;
//			}
//		} else {
//			try {
//				File file = new File(pathStr + service_name);
//				FileUtils.openFile(this,file);
//			} catch (Exception ex) {
//				return 2;
//			}
//		}
//		return 1;
//	}
	
	
	protected int Print(String urlStr, final String pathStr)throws Exception{
		int index = urlStr.lastIndexOf("/");
		// 获取文件名
		final String service_name = urlStr.substring(index + 1, urlStr.length());
		if (!ExistsFile(pathStr + service_name)) {
			// 文件名转码 .防止http出现403
			String service_name_encode = null;
			try {
				service_name_encode = java.net.URLEncoder.encode(service_name,"utf-8");
				service_name_encode = service_name_encode.replaceAll("\\+", "%20").replaceAll("%3A", ":").replaceAll("%2F", "/");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			String service_UrlSplited = urlStr.substring(0, index + 1);
			// 真正可以下载的url
			urlStr = service_UrlSplited + service_name_encode;
			final String url = urlStr;
			final String path = pathStr;
			try {
				URL myURL = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
				// conn.setDoOutput(true);//是否需要在url上追加参数
				conn.setDoInput(true);
				// 设定传送的内容类型是可序列化的java对象 EOFEXception
				conn.setRequestProperty("Content-type","application/x-java-serialized-object");
				conn.connect();
				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return 0;
				}
				InputStream is = conn.getInputStream();
				if (is == null) {
					return 0;
				}
				FileOutputStream fos = new FileOutputStream(path + service_name);
				// 把数据存入路径+文件名
				byte buf[] = new byte[1024];
				do {
					// 循环读取
					int numread = is.read(buf);
					if (numread == -1) {
						break;
					}
					fos.write(buf, 0, numread);
				} while (true);
				is.close();
				fos.close();
				try {
					File file = new File(pathStr + service_name);
					PrintFile(file);
				} catch (Exception ex) {
					return 2;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return 0;
			}
		  } else {
			try {
				File file = new File(pathStr + service_name);
				PrintFile(file);
			} catch (Exception ex) {
				return 2;
			}
		}
		return 1;
	}

	/**
	 * 创建目录
	 * 
	 * @param path
	 */
	protected void createPath(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 判断文件夹是否存在
	 * 
	 * @param path
	 * @return
	 */
	protected boolean ExistsFile(String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 打开文件
	 * 
	 * @param file
	 */
//	private void openFile(File file)throws Exception{
//		String[] WpsArray = { ".doc", ".docx", ".xls", ".xlsx", ".ppt",".pptx", ".txt" };
//		boolean IsOpenWps = false;
//		String strWpsActivityName = "";
//		String strWpsPkgName = "";
//		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//		final PackageManager packageManager = this.getPackageManager();// 获取packagemanager
//		List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(mainIntent, 0);
//		// 调用系统排序 ， 根据name排序
//		// 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
//		Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(packageManager));
//		for (ResolveInfo reInfo : resolveInfos) {
//			String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
//			String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
//			if (pkgName.contains("cn.wps.moffice")) {
//				strWpsActivityName = activityName;
//				strWpsPkgName = pkgName;
//				break;
//			}
//		}
//		// 获取后缀名前的分隔符"."在fName中的位置。
//		int dotIndex = file.getName().lastIndexOf(".");
//		if(dotIndex>0){
//			String endDox = file.getName().substring(dotIndex, file.getName().length()).toLowerCase();
//			IsOpenWps = Arrays.asList(WpsArray).contains(endDox);
//			if (IsOpenWps) {
//				if(SysExitUtil.isAppInstalled(strWpsPkgName,this)){
//					WpsOpenFile(file, strWpsPkgName, strWpsActivityName);
//				}else{
//					DefaultOpenFile(file);
//				}
//			} else {
//				DefaultOpenFile(file);
//			}
//		}else{
//			DefaultOpenFile(file);
//		}
//	}

	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 *
	 * @param file
	 */
//	private String getMIMEType(File file) {
//		String type = "*/*";
//		String fName = file.getName();
//		// 获取后缀名前的分隔符"."在fName中的位置。
//		int dotIndex = fName.lastIndexOf(".");
//		if (dotIndex < 0) {
//			return type;
//		}
//		/* 获取文件的后缀名 */
//		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
//		if (end == "")
//			return type;
//		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
//		for (int i = 0; i < FileUtils.MIME_MapTable.length; i++) {
//			if (end.equals(FileUtils.MIME_MapTable[i][0]))
//				type = FileUtils.MIME_MapTable[i][1];
//		}
//		return type;
//	}
	// 默认金山Wps打开附件
//	private void WpsOpenFile(File file, String strWpsPkgName,String strWpsActivityName)throws Exception {
//		Bundle bundle = new Bundle();
//		bundle.putString("OpenMode", "Normal");
//		bundle.putBoolean("SendCloseBroad", true);
//		bundle.putBoolean("ClearBuffer", true);
//		bundle.putBoolean("ClearTrace", true);
//		bundle.putBoolean("ClearFile", true);
//		Intent intent = new Intent();
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		// 设置intent的Action属性
//		intent.setAction(Intent.ACTION_VIEW);
//		intent.setClassName(strWpsPkgName, strWpsActivityName + "2");
//		intent.setData(Uri.fromFile(file));
//		intent.putExtras(bundle);
//		startActivity(intent);
//	}

//	private void DefaultOpenFile(File file)throws Exception {
//		// 设置intent的data和Type属性。
//		Intent intent = new Intent();
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		// 设置intent的Action属性
//		intent.setAction(Intent.ACTION_VIEW);
//		// 获取文件file的MIME类型
//		String type = getMIMEType(file);
//		// 设置intent的data和Type属性。
//		intent.setDataAndType(Uri.fromFile(file), type);
//		// 跳转
//		startActivity(intent);
//
//	}

	
	private void PrintFile(File file)throws Exception{
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
		//旧版本调用
//		intent.setPackage("com.dynamixsoftware.printershare");
		//最新的调用打印软件
		intent.setPackage("com.dynamixsoftware.printershare.amazon");
		intent.setData(Uri.fromFile(file));
		// 获取文件file的MIME类型
		String type = FileUtils.getMIMEType(file);
		intent.setDataAndType(Uri.fromFile(file), type);
		startActivity(intent);
	}

}
