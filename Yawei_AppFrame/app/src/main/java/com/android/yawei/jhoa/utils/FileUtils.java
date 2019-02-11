package com.android.yawei.jhoa.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.yawei.jhoa.Interface.UploadBigFileCallback;
import com.android.yawei.jhoa.bean.Attachment;
import com.android.yawei.jhoa.bean.FileTobase;
import com.android.yawei.jhoa.factory.ResolveXML;
import com.android.yawei.jhoa.factory.WebServiceNetworkAccess;
import com.android.yawei.jhoa.mobile.OpenAttachmentActivity;
import com.android.yawei.jhoa.uploadfile.BigBase64Util;
import com.android.yawei.jhoa.uploadfile.BigRandomAccessFile;
import com.android.yawei.jhoa.uploadfile.UpLoadFileBean;
import com.yawei.jhoa.mobile.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileUtils {
	private String SDCardRoot;
	private String SDStateString;
	public FileUtils() {
		// 得到当前外部存储设备的目录
		SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
		// 获取扩展SD卡设备状态
		SDStateString = Environment.getExternalStorageState();
	}

	/***
	 * 获取手机sd卡路径
	 * @return
	 */
	public static String GetSDPath(){
		String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
		return SDPath;
	}
	public static String GetSystemFilePath(){
		String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"YaweiAppFrame/";
		return SDPath;
	}
	/**
	 * 在SD卡上创建文件
	 * @param dir 目录路径
	 * @param fileName 文件名称
	 * @return
	 * @throws IOException
	 */
	public File createFileInSDCard(String dir, String fileName)throws IOException {
		File file = new File(SDCardRoot + dir + File.separator + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * @param dir 目录路径
	 * @return
	 */
	public File creatSDDir(String dir) {
		File dirFile = new File(SDCardRoot + dir + File.separator);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return dirFile;
	}
	public File creatSDDirs(String dirPath) {
		File dirFile = new File(dirPath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return dirFile;
	}
	/**
	 * 判断SD卡上的文件夹是否存在
	 * @param dir 目录路径
	 * @param fileName 文件名称
	 * @return
	 */
	public boolean isFileExist(String dir, String fileName) {
		File file = new File(SDCardRoot + dir + File.separator + fileName);
		return file.exists();
	}

	/**
	 * 获取文件的路径
	 * @param dir 目录路径
	 * @param fileName 文件名称
	 * @return
	 */
	public String getFilePath(String dir, String fileName) {
		return SDCardRoot + dir + File.separator + fileName;
	}

	/**
	 * 获取SD卡的剩余容量,单位是Byte
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public long getSDAvailableSize() {
		if (SDStateString.equals(Environment.MEDIA_MOUNTED)) {
			// 取得sdcard文件路径
			File pathFile = Environment.getExternalStorageDirectory();
			android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
			// 获取SDCard上每个block的SIZE
			long nBlocSize = statfs.getBlockSize();
			// 获取可供程序使用的Block的数量
			long nAvailaBlock = statfs.getAvailableBlocks();
			// 计算 SDCard 剩余大小Byte
			long nSDFreeSize = nAvailaBlock * nBlocSize;
			return nSDFreeSize;
		}
		return 0;
	}

	/**
	 * 把try e转成字符串
	 * @param e
	 * @return
	 */
	public static String getErrorInfoFromException(Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e2) {
			return "bad getErrorInfoFromException";
		}
	}
	/**
	 * 将一个字节数组数据写入到SD卡中
	 * @param dir 目录路径
	 * @param fileName 文件名称
	 * @param bytes 字节数组数
	 * @return
	 */
	public boolean writeSD(String dir, String fileName, byte[] bytes) {
		if (bytes == null) {
			return false;
		}
		OutputStream output = null;
		try {
			// 拥有可读可写权限，并且有足够的容量
			if (SDStateString.equals(Environment.MEDIA_MOUNTED)
					&& bytes.length < getSDAvailableSize()) {
				File file = null;
				creatSDDir(dir);
				file = createFileInSDCard(dir, fileName);
				output = new BufferedOutputStream(new FileOutputStream(file));
				output.write(bytes);
				output.flush();
				return true;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 从sd卡中读取文件，并且以字节流返回
	 * @param dir 目录路径
	 * @param fileName 文件名称
	 * @return
	 */
	public byte[] readFromSD(String dir, String fileName) {
		File file = new File(SDCardRoot + dir + File.separator + fileName);
		if (!file.exists()) {
			return null;
		}
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			byte[] data = new byte[inputStream.available()];
			inputStream.read(data);
			return data;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中 ,从网络上读取图片
	 * @param dir  目录路径
	 * @param fileName 文件名称
	 * @param input
	 * @return
	 */
	public File writeSDFromInput(String dir, String fileName, InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			int size = input.available();
			// 拥有可读可写权限，并且有足够的容量
			if (SDStateString.equals(Environment.MEDIA_MOUNTED)
					&& size < getSDAvailableSize()) {
				creatSDDir(dir);
				file = createFileInSDCard(dir, fileName);
				output = new BufferedOutputStream(new FileOutputStream(file));
				byte buffer[] = new byte[4 * 1024];
				int temp;
				while ((temp = input.read(buffer)) != -1) {
					output.write(buffer, 0, temp);
				}
				output.flush();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

  /**
   *删除文件
   * 
   */
    public boolean deleteFile(File file) {
    	boolean ret=false;
	    if (file.exists()) { // 判断文件是否存在
	        if (file.isFile()&&file.exists()) { // 判断是否是文件
	            file.delete(); // delete()方法 你应该知道 是删除的意思;
	        } else if (file.isDirectory()&&file.exists()) { // 否则如果它是一个目录
	            File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
	            if(files != null){
					for (int i = 0; i<files.length; i++) { // 遍历目录下所有的文件
						ret= this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
					}
				}
	        }
	        ret= file.delete();
	    }else{
	    	ret=true;
	    }
    	return ret;
    }

    /****
     * 把文件拷贝到指定目录
     * @param myContext
     * @param ASSETS_NAME
     * @param savePath
     * @param saveName
     */
    public static void copyToPath(Context myContext, String ASSETS_NAME,String savePath, String saveName) {
        String filename = savePath + "/" + saveName;
        File dir = new File(savePath);
        // 如果目录不中存在，创建这个目录
        if (!dir.exists())
            dir.mkdirs();
        try {
            if (!(new File(filename)).exists()) {
                InputStream is = myContext.getResources().getAssets().open(ASSETS_NAME);
                FileOutputStream fos = new FileOutputStream(filename);
                byte[] buffer = new byte[7168];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/******
	 * 把 byte src中真实数据拷贝到另外的byte中
	 * @param src
	 * @param begin
	 * @param count
	 * @return
	 */
	public static byte[] CopyBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		/**
		 * src:源数组；	begin:源数组要复制的起始位置；
		 bs:目的数组；	destPos:目的数组放置的起始位置；	count:复制的长度。
		 注意：src and dest都必须是同类型或者可以进行转换类型的数组．
		 */
		System.arraycopy(src, begin, bs, 0, count);
		return bs;

	}
	public static byte[] CopyBytesToByte(List<byte[]> src,int count) {
		byte[]bs = new  byte[count];
		int dest=0;//目的数组；	destPos:目的数组放置的起始位置；	length:复制的长度。
		for(int i=0;i<src.size();i++){
			System.arraycopy(src.get(i), 0, bs, dest, src.get(i).length);
			dest = dest+src.get(i).length;
		}
		return bs;

	}
	/********
	 * 获取一个txt文件路径
	 * @return
	 */
	public static String GetOneFileName(){
		String fileName = GetSystemFilePath()+"Log/"+GetSystemTime.GetSystemTimeYear2()+".txt";
		SysExitUtil.createPath(GetSystemFilePath()+"Log/");
		return fileName;
	}
	/**
	 * 写日志
	 * @param fileName
	 * @param message
	 * @throws Exception
	 */
	public static void writeFileData(String fileName, String message) {

		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(fileName);

			try {
				if( !file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fout = new FileOutputStream(file, true);
				byte[] bytes = message.getBytes();
				fout.write(bytes);
				fout.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 *  只是附件下载
	 * @param fileUrl
	 * @param filePath
	 * @param fileExists 是否需要判断文件存在,true无论本地有没有都下载此文件
	 * @return
	 * @throws Exception
	 */

	public static String DownloadFile(String fileUrl,String filePath,boolean fileExists)throws Exception{
		if(fileUrl.substring(fileUrl.lastIndexOf(".")+1, fileUrl.length()).toLowerCase().equals("pdf")){
			fileUrl = fileUrl.substring(0, fileUrl.lastIndexOf("."))+fileUrl.substring(fileUrl.lastIndexOf("."), fileUrl.length()).toLowerCase();
		}
		String serviceFileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1, fileUrl.length());
		if(fileExists || !new File(filePath+serviceFileName).exists()){
			if(!(new File(filePath)).exists()){
				File temp_file =  new File(filePath);
				temp_file.mkdirs();
			}
			// 文件名转码 .防止http出现403
			try {
				String service_name_encode = java.net.URLEncoder.encode(serviceFileName,"utf-8");
				service_name_encode = service_name_encode.replaceAll("\\+", "%20").replaceAll("%3A", ":").replaceAll("%2F", "/");
				String service_UrlSplited = fileUrl.substring(0, fileUrl.lastIndexOf("/") + 1);
				// 真正可以下载的url
				String downloadFileUrl = service_UrlSplited + service_name_encode;

				URL myURL = new URL(downloadFileUrl);
				HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
				conn.setDoInput(true);
				// 设定传送的内容类型是可序列化的java对象 EOFEXception
				conn.setRequestProperty("Content-type","application/x-java-serialized-object");
				conn.connect();
				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return "";
				}
				InputStream is = conn.getInputStream();
				if (is == null) {
					return "";
				}
				FileOutputStream fos = new FileOutputStream(filePath + serviceFileName);
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

			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				return "0";
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return "0";
			}catch (Exception e){
				e.printStackTrace();
				return "0";
			}
		}
		return filePath + serviceFileName;
	}
	/**
	 * 打开文件
	 *
	 * @param file
	 */
	public static void openFile(Context context,File file)throws Exception{
		try {
			String fileName = file.getName();
			String filesuffix = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
			if(filesuffix!=null&&filesuffix.length()>0&&filesuffix.toLowerCase().equals("pdf")){
				Intent intent = new Intent(context, OpenAttachmentActivity.class);
				intent.putExtra("type","1");
				intent.putExtra("filePath",file.getPath());
				context.startActivity(intent);
			}else if(filesuffix!=null&&filesuffix.length()>0&&(filesuffix.toLowerCase().equals("jpg")||filesuffix.toLowerCase().equals("jpeg")
					||filesuffix.toLowerCase().equals("gif")||filesuffix.toLowerCase().equals("png"))){
				Intent intent = new Intent(context, OpenAttachmentActivity.class);
				intent.putExtra("type","0");
				intent.putExtra("filePath",file.getPath());
				context.startActivity(intent);
			}else if(Constants.TencentOpenfile&&CanOpenfile(fileName)){
				Intent intent = new Intent(context, OpenAttachmentActivity.class);
				intent.putExtra("type","2");
				intent.putExtra("filePath",file.getPath());
				context.startActivity(intent);
			}else {
				String[] WpsArray = { ".doc", ".docx", ".xls", ".xlsx", ".ppt",".pptx", ".txt" };
				boolean IsOpenWps = false;
				String strWpsActivityName = "";
				String strWpsPkgName = "";
				Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
				mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
				List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(mainIntent, 0);
				// 调用系统排序 ， 根据name排序
				// 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
				Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(packageManager));
				for (ResolveInfo reInfo : resolveInfos) {
					String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
					String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
					if (pkgName.contains("cn.wps.moffice")) {
						strWpsActivityName = activityName;
						strWpsPkgName = pkgName;
						break;
					}
				}
				// 获取后缀名前的分隔符"."在fName中的位置。
				int dotIndex = file.getName().lastIndexOf(".");
				String endDox = file.getName().substring(dotIndex, file.getName().length()).toLowerCase();
				IsOpenWps = Arrays.asList(WpsArray).contains(endDox);
				if (IsOpenWps) {
					WpsOpenFile(context,file, strWpsPkgName, strWpsActivityName);
				} else {
					DefaultOpenFile(context,file);
				}
			}
		}catch (Exception e){
			DefaultOpenFile(context,file);
		}
	}

	// 默认金山Wps打开附件
	private static void WpsOpenFile(Context context,File file, String strWpsPkgName, String strWpsActivityName)throws Exception{
		Bundle bundle = new Bundle();
		bundle.putString("OpenMode", "Normal");
		bundle.putBoolean("SendCloseBroad", true);
		bundle.putBoolean("ClearBuffer", true);
		bundle.putBoolean("ClearTrace", true);
		bundle.putBoolean("ClearFile", true);
		Intent intent = new Intent();
		// 设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
		intent.setClassName(strWpsPkgName, strWpsActivityName + "2");
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
		intent.setData(data);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	private static void DefaultOpenFile(Context context,File file)throws Exception{
		try {
			// 设置intent的data和Type属性。
			Intent intent = new Intent();
			// 设置intent的Action属性
			intent.setAction(Intent.ACTION_VIEW);
			// 获取文件file的MIME类型
			String type = getMIMEType(file);
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
			// 设置intent的data和Type属性。
			intent.setDataAndType(data, type);
			// 跳转
			context.startActivity(intent);
		}catch (Exception e){
			e.printStackTrace();
			Toast.makeText(context,"文件打开失败",Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * 判断文件夹是否存在
	 *
	 * @param path
	 * @return
	 */
	public static boolean ExistsFile(String path) {
		File file = new File(path);
		return file.exists();
	}
	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 *
	 * @param file
	 */
	public static String getMIMEType(File file) {
		String type = "*/*";
		String fName = file.getName();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (end == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}
	/**
	 * 大附件或者多个文件上传
	 * @param attachmentList
	 * @param sign 用来返回区分操作，默认传0（例如：写便笺，发送和保存到草稿箱0保存草稿 1保存发送）
	 *             部分情况可能无用
	 */
	public static void UpLoadBigFile(final List<Attachment> attachmentList , final String sign, final UploadBigFileCallback callback) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String fileGuid= "";
					for (Attachment attachlocal : attachmentList) {
						if(attachlocal.getAttGuidString()==null || "".equals(attachlocal.getAttGuidString())){
							String blobGuid = "";
							BigRandomAccessFile bigRandomAccessFile = new BigRandomAccessFile(attachlocal.getAttUrl(), 0);
							long startPos = 0L;
							Long length = bigRandomAccessFile.getFileLength();//得到文件的长度
							int mBufferSize = 1024*1024; //每次处理1M字节
							byte[] buffer = new byte[mBufferSize];//创建一个mBufferSize大小的缓存数组
							BigRandomAccessFile.Detail fileDetail;//文件的详情类
							long nRead = 0l;//读取文件的当前长度
							String fileName = new File(attachlocal.getAttUrl()).getName(); //分配一个文件名
							long fileLength = new File(attachlocal.getAttUrl()).length();
							int startAllFrequency =0 ;//总共需要上传几次
							if(fileLength%(1024*1024)==0){
								startAllFrequency = (int) (fileLength/(1024*1024));
							}else{
								startAllFrequency = (int) fileLength/(1024*1024)+1;
							}
							int NowsFrequency = 0;//当前上传的第几次
							long nStart = startPos;//开始读的位置
							while (nStart < length) {//当开始都的位置比长度小的时候
								NowsFrequency = NowsFrequency+1;
								String extendInfo = "<root>"
										+"<total>"+startAllFrequency+"</total>"
										+"<nownum>"+NowsFrequency+"</nownum>"
										+"<datalen>本次传入文件流长度</datalen>"
										+"</root>";
								fileDetail = bigRandomAccessFile.getContent(startPos,mBufferSize);//开始读取文件
								nRead = fileDetail.length;//读取的文件的长度
								buffer = fileDetail.b;//读取文件的缓存
								byte[] strBuffer = FileUtils.CopyBytes(buffer, 0, (int)nRead);
								String result = WebServiceNetworkAccess.SaveBlobAttach(fileName, fileGuid, blobGuid, BigBase64Util.getBase64String(strBuffer),extendInfo,"");
								if(result != null && !result.equals("") && !result.equals("anyType")){
									UpLoadFileBean strBean = ResolveXML.parseFileUp(result);
									fileGuid = strBean.getFileguid();
									blobGuid = strBean.getBlobguid();
								}else{
									callback.BigFileUpError("附件上传失败",sign);
									return;
								}
								nStart += nRead;//下一次从哪里开始读取
								startPos = nStart;
							}
						}
					}
					callback.BigFileUpSucceed("附件上传成功",fileGuid,sign);
				} catch (Exception e) {
					callback.BigFileUpError("附件上传失败",sign);
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	public static void FileToBase(final List<FileTobase> filePathArr,final UploadBigFileCallback callback){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<FileTobase> filebasearr = new ArrayList<>();
					String fileGuid= "";
					for (FileTobase fileinfo : filePathArr) {
						FileTobase filebean = new FileTobase();
						BigRandomAccessFile bigRandomAccessFile = new BigRandomAccessFile(fileinfo.getAttUrl(), 0);
						long startPos = 0L;
						Long length = bigRandomAccessFile.getFileLength();//得到文件的长度
						int mBufferSize = 1024*1024; //每次处理1M字节
						byte[] buffer = new byte[mBufferSize];//创建一个mBufferSize大小的缓存数组
						BigRandomAccessFile.Detail fileDetail;//文件的详情类
						long nRead = 0l;//读取文件的当前长度
						long nStart = startPos;//开始读的位置
						String filebase = "";
						List<byte[]> byteArr = new ArrayList<>();
						while (nStart < length) {//当开始都的位置比长度小的时候
							fileDetail = bigRandomAccessFile.getContent(startPos,mBufferSize);//开始读取文件
							nRead = fileDetail.length;//读取的文件的长度
							buffer = fileDetail.b;//读取文件的缓存
							byte[] strBuffer = FileUtils.CopyBytes(buffer, 0, (int)nRead);
							byteArr.add(strBuffer);
							nStart += nRead;//下一次从哪里开始读取
							startPos = nStart;
						}
						byte[] strBuffer = FileUtils.CopyBytesToByte(byteArr,(int)startPos);
						filebase = BigBase64Util.getBase64String(strBuffer);
						filebean.setFileData(filebase);
						filebean.setAttFileNameString(new File(fileinfo.getAttUrl()).getName());
						filebean.setAttUrl(fileinfo.getAttUrl());
						filebean.setFileSize(String.valueOf(new File(fileinfo.getAttUrl()).length()));
						filebasearr.add(filebean);
					}
					String fileJson = JSON.toJSONString(filebasearr);
					callback.BigFileUpSucceed(fileJson,"","");
				} catch (Exception e) {
					callback.BigFileUpError("形成文件流失败","");
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	/**
	 * 判断可以打开的文件,用在腾讯tbs判断
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static boolean CanOpenfile(String fileName)throws Exception{
		String fileSuffix = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		boolean ret=false;
		if(fileSuffix.equals("doc")||fileSuffix.equals("docx")||fileSuffix.equals("xls")||fileSuffix.equals("xlsx")||fileSuffix.equals("pdf")
				||fileSuffix.equals("txt")||fileSuffix.equals("bmp")||fileSuffix.equals("jpg")||fileSuffix.equals("png")||fileSuffix.equals("pptx")
				||fileSuffix.equals("ppt")||fileSuffix.equals("jpeg")||fileSuffix.equals("gif")||fileSuffix.equals("xml")||fileSuffix.equals("html")||fileSuffix.equals("htm")
				||fileSuffix.equals("js")){
			ret= true;
		}
		return ret;
	}
	public static final String[][] MIME_MapTable = {
			// {后缀名， MIME类型}
			{ ".3gp", "video/3gpp" },
			{ ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" },
			{ ".avi", "video/x-msvideo" },
			{ ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" },
			{ ".c", "text/plain" },
			{ ".class", "application/octet-stream" },
			{ ".conf", "text/plain" },
			{ ".cpp", "text/plain" },
			{ ".doc", "application/msword" },
			{ ".docx", "application/msword" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx", "application/vnd.ms-excel" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pptx", "application/vnd.ms-powerpoint" },
			{ ".exe", "application/octet-stream" },
			{ ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" },
			{ ".h", "text/plain" },
			{ ".htm", "text/html" },
			{ ".html", "text/html" },
			{ ".jar", "application/java-archive" },
			{ ".java", "text/plain" },
			{ ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" },
			{ ".js", "application/x-javascript" },
			{ ".log", "text/plain" },
			{ ".m3u", "audio/x-mpegurl" },
			{ ".m4a", "audio/mp4a-latm" },
			{ ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" },
			{ ".m4u", "video/vnd.mpegurl" },
			{ ".m4v", "video/x-m4v" },
			{ ".mov", "video/quicktime" },
			{ ".mp2", "audio/x-mpeg" },
			{ ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" },
			{ ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" },
			{ ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" },
			{ ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" },
			{ ".png", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".prop", "text/plain" },
			{ ".rc", "text/plain" },
			{ ".rmvb", "audio/x-pn-realaudio" },
			{ ".rtf", "application/rtf" },
			{ ".sh", "text/plain" },
			{ ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" },
			{ ".txt", "text/plain" },
			{ ".wav", "audio/x-wav" },
			{ ".wma", "audio/x-ms-wma" },
			{ ".wmv", "audio/x-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" },
			{ ".xml", "text/plain" },
			{ ".ofd", "application/octet-stream" }
	};

}
