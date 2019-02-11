package com.android.yawei.jhoa.webservice;

import java.util.Map;

/**
 * WebService
 * 
 * @author sn
 * 
 */
public abstract class WebService{
	// 命名空间
	protected String nameSpace = null;
	// 调用的方法名称
	protected String methodName = null;
	// URL
	protected String url = null;
	// 参数
	protected Map<String, Object> params = null;

	protected WebService(){
	}

	public WebService(String nameSpace, String methodName, String url, Map<String, Object> params){
		// 验证合法性
		if (methodName == null || url == null || params == null){
			throw new NullPointerException();
		}

		// 初始化
		this.nameSpace = nameSpace;
		this.methodName = methodName;
		this.url = url;
		this.params = params;
	}

	/**
	 * 异步调用
	 * 
	 * @param callback
	 */
	public void asyncInvoke(Callback callback){
//		Thread thread = new Thread(new AsyncTask(callback));
//		thread.start();
		new WebserviceThread(callback).start();
	}
	class WebserviceThread extends Thread{
		private Callback callback = null;
		protected WebserviceThread(Callback callback){
			this.callback = callback;
		}
		@Override
		public void run(){
			String result = invoke();
			if (callback != null){
				callback.handle(result);
			}
		}
	}
	/**
	 * 异步任务
	 * 
	 * @author sn
	 * 
	 */
	public class AsyncTask implements Runnable{
		private Callback callback = null;

		protected AsyncTask(Callback callback){
			this.callback = callback;
		}
		@Override
		public void run(){
			String result = invoke();
			if (callback != null){
				callback.handle(result);
			}
		}
	}

	/**
	 * 回调
	 * 
	 * @author sn
	 * 
	 */
	public interface Callback{
		public void handle(String result);
	}

	/**
	 * 调用
	 * 
	 * @return
	 */
	public abstract String invoke();
}
