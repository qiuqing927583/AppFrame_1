package com.android.yawei.jhoa.webservice;

import com.android.yawei.jhoa.webservice.soap.SoapWebServiceUtils;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * WebService工具
 * 
 * @author sn
 * 
 */

public final class WebServiceUtils {
	private WebServiceUtils(){
	}

	public static IWebServiceUtils instance = null;

	static{
		newInstance();
	}

	private static void newInstance(){
		
		/**
		 * 通过修改配置文件来创建全局的WebService工具
		 */
//		try{
//			ResourceBundle rb = ResourceBundle.getBundle("com.android.yawei.jhoa.webservice.WebServiceUitls",Locale.getDefault());
//			String className = rb.getString("WebServiceUtils");
//			instance = (IWebServiceUtils) Class.forName(className).newInstance();
//		}catch (Exception e){
//			e.printStackTrace();
//			instance = new SoapWebServiceUtils();
//		}
		try {
			instance = new SoapWebServiceUtils();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static String invoke(String nameSpace, String methodName, String url, Map<String, Object> params) throws Exception{
		return instance.invoke(nameSpace, methodName, url, params);
	}
	public static boolean invokeBoolean(String nameSpace, String methodName, String url, Map<String, Object> params) throws Exception{
		return instance.invokeBoolean(nameSpace, methodName, url, params);
	}

	public static void asyncInvoke(String nameSpace, String methodName, String url, Map<String, Object> params, WebService.Callback callback) throws Exception{
		instance.asyncInvoke(nameSpace, methodName, url, params, callback);
	}
}
