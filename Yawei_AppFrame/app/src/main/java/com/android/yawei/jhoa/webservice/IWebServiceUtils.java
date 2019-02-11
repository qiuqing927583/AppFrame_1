package com.android.yawei.jhoa.webservice;

import java.util.Map;

/**
 * WebService的接口
 * 
 * @author sn
 * 
 */
public interface IWebServiceUtils{
	/**
	 * 同步调用
	 * 
	 * @param nameSpace
	 * @param methodName
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String invoke(String nameSpace, String methodName, String url, Map<String, Object> params) throws Exception;
	public boolean invokeBoolean(String nameSpace, String methodName, String url, Map<String, Object> params) throws Exception;

	/**
	 * 异步调用
	 * 
	 * @param nameSpace
	 * @param methodName
	 * @param url
	 * @param params
	 * @param callback
	 * @throws Exception
	 */
	public void asyncInvoke(String nameSpace, String methodName, String url, Map<String, Object> params, WebService.Callback callback) throws Exception;
}

