package com.android.yawei.jhoa.webservice.soap;

import com.android.yawei.jhoa.utils.FileUtils;
import com.android.yawei.jhoa.webservice.WebService;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 采用SOAP方式的WebService
 * 
 * @author sn
 * 
 */
public class SoapWebService extends WebService{
	public SoapWebService(String nameSpace, String methodName, String url, Map<String, Object> params){
		// 验证合法性
		if (nameSpace == null || methodName == null || url == null || params == null){
			throw new NullPointerException();
		}

		// 初始化
		this.nameSpace = nameSpace;
		this.methodName = methodName;
		this.url = url;
		this.params = params;
	}

	/**
	 * 调用
	 * 
	 * @return
	 */
	int i = 0;
	@Override
	public String invoke(){
		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);

		// 设置需调用WebService接口需要传入的参数
		for (Map.Entry<String, Object> entry : params.entrySet()){
			rpc.addProperty(entry.getKey(), entry.getValue());
		}

		// 生成调用WebService方法的SOAP请求信息，并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// 等价于envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);

		// 调用WebService
		HttpTransportSE transport = new HttpTransportSE(url, 30000);
		 //4.构建传输对象 
//		 int timeout = 5000;
//		 MyAndroidHttpTransport transport = new MyAndroidHttpTransport(url, timeout);  
//        transport.debug = true;  
//        try {
//			transport.getServiceConnection();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//        
		try{
			String soapAction = nameSpace + methodName;
			// 调用WebService
			transport.call(soapAction, envelope);
		}catch (Exception e){
			e.printStackTrace();
			FileUtils.writeFileData(FileUtils.GetOneFileName(), "webservice--" +methodName+"  错误信息："+ FileUtils.getErrorInfoFromException(e) + "\n");
			if(e instanceof SocketException){
				return "anyType";
			}
			if( e instanceof SocketTimeoutException){
				return "anyType";
			}
			if( e instanceof TimeoutException){
				return "anyType";
			}
			if (e instanceof IOException){
				try{
					return "anyType";
				}catch (Exception error){
					return "anyType";
				}
			}else if (e instanceof XmlPullParserException){
				return "anyType";
			}else{
				return "anyType";
			}
		}
		// 获取返回的数据
		SoapObject object;
		try{
			object = (SoapObject) envelope.bodyIn;
		}catch (Exception e){
			e.printStackTrace();
			FileUtils.writeFileData(FileUtils.GetOneFileName(), "webservice--" +methodName+"  返回数据错误信息："+ FileUtils.getErrorInfoFromException(e) + "\n");
			return "anyType";
		}
		// 获取返回的结果
	    int retCount=object.getPropertyCount();
	    String result=null;
		if(retCount!=0){
		  result = object.getPropertyAsString(0);
		  if("anyType{}".equals(result)){
			result = "";
		  }
		}
		return result;
	}

}