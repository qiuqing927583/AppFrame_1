package com.android.yawei.jhoa.factory;

import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.webservice.WebService;
import com.android.yawei.jhoa.webservice.WebServiceUtils;

import java.util.HashMap;
import java.util.Map;

/*******************
 * 
 * @author Yusz
 * 收文管理模块webservice
 * 
 *******************/
public class ReceivWebServiceAccess {
	
	/********************
	 * 获取收文管理列表
	 * @param pageNo 当前页码
	 * @param pageSize 每页显示条数
	 * @param userGuid 当前用户唯一标识
	 * @param status 查询列表状态 0:待办 1:在办 2:已办 4:所有
	 * @param titleKey 模糊查询标题关键字
	 */
	public static void GetReceiveFileList(String pageNo, String pageSize, String userGuid, String status, String titleKey,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pageNo", pageNo);
		params.put("pageSize", pageSize);
		params.put("userGuid", userGuid);
		params.put("status", status);
		params.put("titleKey", titleKey);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetReceiveFileList", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**************
	 * 获取收文详细信息接口
	 * @param guid 发文件唯一标示
	 * @param userName 用户登录名
	 * @param userGuid 用户唯一标示
	 **************/
	public static void GetReceiveFileInfoByGuid(String guid, String userName, String userGuid,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("guid", guid);
		params.put("userName", userName);
		params.put("userGuid", userGuid);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetReceiveFileInfoByGuid", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/*************************
	 * 获取会议室列表信息
	 * @param callback
	 */
	public static void GetAllRoomList(WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetAllRoomList", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/********************
	 * 获取某个会议室下预定会议室（使用时间不晚于当前日期）的列表数据信息
	 * @param roomGuid 会议室唯一标示
	 */
	public static void  GetApplyRoomList (String roomGuid,WebService.Callback callback){
		

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roomGuid", roomGuid);
		
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetApplyRoomList", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/************************************
	 * 获取某个预定会议室详细信息
	 * @param applyGuid  预定会议室唯一标识
	 * @param callback
	 */
	public static void GetApplyRoomInfo (String applyGuid,WebService.Callback callback){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("applyGuid", applyGuid);
		
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetApplyRoomInfo", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	/****************
	 * 添加会议室申请信息
	 * @param applyRoomXml 会议室申请xml
	 * <root>
	 *	<roomguid><![CDATA[会议室唯一标识]]></roomguid>
	 *	<usedept><![CDATA[使用部门]]></usedept>
	 *	<linkman><![CDATA[联系人]]></linkman>
	 *	<conferencedate><![CDATA[开始日期]]></conferencedate>
	 *	<starthour><![CDATA[开始小时]]></starthour>
	 *	<startminute><![CDATA[开始分钟]]></startminute>
	 *	<endhour><![CDATA[结束小时]]></endhour>
	 *	<endminute><![CDATA[结束分钟]]></endminute>
	 *	<content><![CDATA[事项]]></content>
	 *	<remark><![CDATA[具体要求]]></remark>
	 *	<joinorg><![CDATA[参加部门]]></joinorg>
	 *	</root>
	 * @param operatorXml 操作人信息xml
	 * <root>
	 *  <userguid><![CDATA[用户唯一标识]]></userguid>
	 *	<username><![CDATA[用户名]]></username>
	 *	<devicename><![CDATA[设备名]]></ devicename>
	 *	<title><![CDATA[标题]]></title>
	 *	</root>
	 */
	public static void AddApplyRoom (String applyRoomXml, String operatorXml,WebService.Callback callback){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("applyRoomXml", applyRoomXml);
		params.put("operatorXml", operatorXml);
		
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "AddApplyRoom", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/********************
	 * 获取地方信息列表接口
	 * @param searchStr 模糊查询标题关键字
	 * @param userGuid 当前用户唯一标识
	 * @param status 查询列表状态 0:信息拟稿(信息上报栏目) 1:上报信息(信息上报栏目) 2:上报信息(本地信息栏目) 3:采用信息(本地信息栏目) 4:全部信息(信息上报栏目) 5:全部信息(本地信息栏目)
	 * @param pageIndex 当前页码
	 * @param pageSize 每页显示条数
	 * @param callback
	 */
	public static void GetInfoEntryList(String searchStr, String userGuid, String status, String pageIndex, String pageSize,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("searchStr", searchStr);
		params.put("userGuid", userGuid);
		params.put("status", status);
		params.put("pageIndex", pageIndex);
		params.put("pageSize", pageSize);
		
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetInfoEntryList", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/*****************
	 * 获取地方信息接口
	 * @param entryGuid 地方信息唯一标识
	 * @param callback
	 */
	public static void GetInfoEntry(String entryGuid,WebService.Callback callback){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("entryGuid", entryGuid);
		
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetInfoEntry", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	/****************
	 * 地方信息上报接口 
	 * @param guid 地方上报信息主键
	 * @param callback
	 */
	public static void SendInfoEntry(String guid,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("guid", guid);
		
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "SendInfoEntry", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/****************
	 * 地方信息添加接口
	 * @param entryInfoXml 地方信息添加XML<root>
	   <title><![CDATA[地方信息标题]]></title>
	   <infoentryorig><![CDATA[信息来源]]></infoentryorig>
	   <filesort><![CDATA[信息类别]]></filesort>
	   <content><![CDATA[地方信息内容]]></content>
	   <zsorg><![CDATA[接收人的中文名称 不同用户间用;分割]]></zsorg>
	   <zsorgadguid><![CDATA[接收人的唯一标识 不同用户间用;分割]]></zsorgadguid>
	   <systemflag><![CDATA[接收人新老系统标识 不同用户间用;分割]]></systemflag>
	   <serverflag><![CDATA[接收人信息交换码 不同用户间用;分割]]></serverflag>
	   </root>
	 * @param operatorXml 操作信息XML
	   <root>
	  <userguid><![CDATA[用户唯一标识]]></userguid>
	  <username><![CDATA[用户名]]></username>
	  <devicename><![CDATA[设备名]]></ devicename>
	  <title><![CDATA[标题]]></title>
	  </root>
	 * @param callback
	 */
	public static void AddInfoEntry(String entryInfoXml, String operatorXml,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("entryInfoXml", entryInfoXml);
		params.put("operatorXml", operatorXml);
		
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "AddInfoEntry", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/******************
	 * 上报信息入刊物接口
	 * @param entryGuid 上报信息唯一标识
	 * @param publicationGuid 刊物期号唯一标识
	 * @param operatorXml 操作信息XML
	 * <root>
	   <userguid><![CDATA[用户唯一标识]]></userguid>
	   <username><![CDATA[用户名]]></username>
	   <devicename><![CDATA[设备名]]></ devicename>
	   <title><![CDATA[标题]]></title>
	   </root>
	 * @param callback
	 */
	public static void InfoEntryToPublication(String entryGuid, String publicationGuid, String operatorXml,WebService.Callback callback){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("entryGuid", entryGuid);
		params.put("publicationGuid", publicationGuid);
		params.put("operatorXml", operatorXml);
		
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "InfoEntryToPublication", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	/**************
	 * 获取地方信息类别列表接口
	 * @param callback
	 */
	public static void GetInfoEntryFilesortList(WebService.Callback callback){
		
		Map<String, Object> params = new HashMap<String, Object>();
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetInfoEntryFilesortList", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/*****************
	 * 获取刊物期号信息接口
	 * @param userGuid 用户唯一标识
	 * @param callback
	 */
	public static void GetEntryPublicationList (String userGuid,WebService.Callback callback){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userGuid", userGuid);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetEntryPublicationList", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/*****************
	 * 
	 * @param notePaperXml
	 * <root>
		<item>
		<baseguid>便签唯一标识</baseguid>   fileguid
		<title>便签标题</title>
		<tousername>督查人用户名</tousername>
		<touserguid>督查人唯一标示</touserguid>
		</item>
		</root>
	 * @param operatorXml
	 * <root>
		<userguid><![CDATA[用户唯一标识]]></userguid>
		<username><![CDATA[用户名]]></username>
		<devicename><![CDATA[设备名]]></ devicename>
		<title><![CDATA[标题]]></title>
		</root>
	 */
	public static void NotePaperSendToSuperviseFile(String notePaperXml, String operatorXml,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("notePaperXml", notePaperXml);
		params.put("operatorXml", operatorXml);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "NotePaperSendToSuperviseFile", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
