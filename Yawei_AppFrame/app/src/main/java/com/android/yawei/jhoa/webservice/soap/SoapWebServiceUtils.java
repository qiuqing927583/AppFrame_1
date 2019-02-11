package com.android.yawei.jhoa.webservice.soap;

import com.android.yawei.jhoa.webservice.IWebServiceUtils;
import com.android.yawei.jhoa.webservice.WebService.Callback;

import java.util.Map;


/**
 * 采用SOAP方式的WebService工具
 * 
 * @author sn
 * 
 */
public class SoapWebServiceUtils implements IWebServiceUtils {
	@Override
	public String invoke(String nameSpace, String methodName, String url, Map<String, Object> params) throws Exception{
		SoapWebService soap = new SoapWebService(nameSpace, methodName, url, params);
		return soap.invoke();
	}

	@Override
	public void asyncInvoke(String nameSpace, String methodName, String url, Map<String, Object> params, Callback callback) throws Exception{
		SoapWebService soap = new SoapWebService(nameSpace, methodName, url, params);
		soap.asyncInvoke(callback);
	}

	@Override
	public boolean invokeBoolean(String nameSpace, String methodName,String url, Map<String, Object> params) throws Exception {
		return false;
	}
}

