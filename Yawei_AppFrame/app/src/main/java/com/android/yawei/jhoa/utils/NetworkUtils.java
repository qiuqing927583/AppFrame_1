package com.android.yawei.jhoa.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

public class NetworkUtils {
	private static final int TIME_OUT = 5000;

	/**
	 * 判断网络是否连接正常
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context activity){
		Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager == null){
            return false;
        }else{
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0){
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
		
	}
	
	/**
	 * 判断wifi是否可用
	 * 
	 * @return "true",wifi可用
	 * @return "false",wifi不可用
	 */
	private static boolean isWifiAvailable(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable() && ni.getType() == ConnectivityManager.TYPE_WIFI){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 获取文字
	 * 
	 * @param webSiteUrl
	 *            网页的地址
	 */
	public static String getString(String webSiteUrl){
		try{
			URL url = new URL(webSiteUrl);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setConnectTimeout(TIME_OUT);

			BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null){
				sb.append("\n");
				sb.append(line);
			}

			String text = sb.toString();

			if (text != null && !text.equals("")){
				text = text.substring(1);
			}

			br.close();
			http.disconnect();
			return text;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获取HttpURLConnection
	 * 
	 * @param url
	 * @return 连接成功返回coon，否则返回null
	 */
	@SuppressWarnings("deprecation")
	public static HttpURLConnection getConnection(Context context, URL url){
		HttpURLConnection conn = null;
		if (isWifiAvailable(context)){
			// 判断wifi是否可用
			try{
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(100000);// 连接超时时间
				conn.setReadTimeout(100000);
			}catch (IOException e){
				e.printStackTrace();
				conn = null;
				return conn;
			}
			return conn;
		}
		if ((android.net.Proxy.getDefaultHost()) != null){// wap需要代理
		
			try{
				java.net.Proxy p = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(android.net.Proxy.getDefaultHost(), android.net.Proxy.getDefaultPort()));
				conn = (HttpURLConnection) url.openConnection(p);
				conn.setConnectTimeout(100000);
				conn.setReadTimeout(100000);
			}catch (Exception e){
				conn = null;
			}
			return conn;
		}
		if ((android.net.Proxy.getDefaultHost()) == null){// net不需要代理
			try{
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(100000);
			}catch (Exception e){
				conn = null;
			}
			return conn;
		}
		return conn;
	}

}
