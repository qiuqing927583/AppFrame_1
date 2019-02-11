package com.android.yawei.jhoa.factory;

import com.android.yawei.jhoa.utils.Constants;
import com.android.yawei.jhoa.webservice.WebService;
import com.android.yawei.jhoa.webservice.WebServiceUtils;

import java.util.HashMap;
import java.util.Map;

/****************************************
 * 所有外网和办公（装网）webservice接口                            *
 * 登录、待办、已办、草稿、已发、联系人、更多界面webservice*
 * *
 ****************************************/
public class WebServiceNetworkAccess {

	/**********************
	 * 获取登录日志
	 * @param userGuid
	 * @return
	 */
	public static void  GetUserLoginInfo(String userGuid,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userGuid", userGuid);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetUserLoginInfo", Constants.WEB_GETUSERINFO_SERVICE, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * 同步方法
	 * 获取版本信息，判断是否是最新版本
	 */
	public static String getVersion(String clientName){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appGuid", clientName);
		String xml = "";
		try{
			xml=WebServiceUtils.invoke(Constants.NAMESPACE, "GetVersion", Constants.WEB_APPDOWNLOAD_URL, params);

		}catch (Exception e){
			e.printStackTrace();
			xml ="";
		}
		return xml;
	}
	/*
	 * 异步方法
	 * 获取版本信息，判断是否是最新版本
	 */
	public static void  getVersion(String clientName,WebService.Callback  callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appGuid", clientName);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetVersion", Constants.WEB_APPDOWNLOAD_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 新开发更多界面获取应用
	 * @param userGuid
	 * @param exchangeCode
	 * @param appGuid
	 * @param callback
	 */
	public static void  GetModuleInfoByUserGuid(String userGuid ,String exchangeCode,String appGuid,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userGuid", userGuid);
		params.put("exchangeCode", exchangeCode);
		params.put("appGuid", appGuid);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetModuleInfoByUserGuid", Constants.WEB_GETUSERINFO_SERVICE, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/********************************************
	 * 信件webservice
	 ********************************************/
	/**
	 * 异步获得待办件列表
	 * @param pageSize 获取多少条数据；
	 * @param splitValue 分也标示(最后一条的时间)；
	 * @param userGuid 用户唯一标识(guid)；
	 * @param sign 邮件的标示(0待办1已办)
	 * @param dbName 用户交换号；newOrOld标示是否第一次(1标示第一次；0标示已经加载过)
	 * @param typeCode 0全部  1待批 2待阅 3邮件
	 */
	public static void getNewestToDealList(String pageSize,String splitValue, String userGuid, String sign,String dbName,String newOrOld, String typeCode,
			WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pageSize", pageSize);
		params.put("splitValue", splitValue);
		params.put("userGuid", userGuid);
		params.put("sign", sign);
		params.put("dbName", dbName);
		params.put("newOrOld", newOrOld);
		params.put("typeCode", typeCode);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetNewestToDealList", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/***************************
	 * 异步获取组信息（公共组和个人组）
	 * @param userGuid 用户唯一标示
	 * @param dbName 交换号
	 */
	public static void GetUserGroups(String userGuid,String dbName, WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userGuid",userGuid);
		params.put("dbName",dbName);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetUserGroups", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 异步获取组下的人员信息
	 * @param groupGuid 组唯一标示
	 * @param dbName
	 * @param type
	 * @param pageNow
	 * @param callback
	 */
	public static void  GetUsersInGroupByPageNo(String groupGuid,String dbName,String type,String pageNow, WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("groupGuid", groupGuid);
		params.put("dbName", dbName);
		params.put("type", type);
		params.put("pageNo", pageNow);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetUsersInGroupByPageNo", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/*****************
	 * 异步搜索用户
	 * @param username 搜索人的名字
	 */
	public static void GetUserListByQuery(String username, WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", username);
	
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetUserListByQuery", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/*****************
	 * 附件id,根据id删除附件
	 * @param blobGuid
	 * @return
	 *****************/
	public static void  DeleteAttachmentByBlobGuid(String blobGuid,String exchangeid,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("blobGuid", blobGuid);
		params.put("dbName", exchangeid);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "DeleteAttachmentByBlobGuid", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public static String  DeleteAttachmentByBlobGuid(String blobGuid,String exchangeid){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("blobGuid", blobGuid);
		params.put("dbName", exchangeid);
		String xml = "";
		try{
			xml = WebServiceUtils.invoke(Constants.NAMESPACE, "DeleteAttachmentByBlobGuid", Constants.WEB_SERVICE_URL, params);
		}catch (Exception e){
			xml = "";
			e.printStackTrace();
		}
		return xml;
	}

	/******
	 * 新增修改便笺方法
	 * @param mailXml
	 * @param operatorXml
	 * @return
	 */
	public static void UpdateNotePaperForBigAttach(String mailXml,String operatorXml,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("MailXml", mailXml);
		params.put("operatorXml", operatorXml);
		String xml = "";
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "UpdateNotePaperForBigAttach", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/*********
	 * 新增邮件保存方法
	 * @param MailXml
	 * @param operatorXml
	 * @return
	 */
	public static void AddNotePaperForBigAttach(String MailXml, String operatorXml,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("MailXml", MailXml);
		params.put("operatorXml", operatorXml);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "AddNotePaperForBigAttach",  Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/*****************
	 * 发送便笺
	 * @param strGuid 保存便笺返回的id
	 * @dbName 用户交换号
	 * @operatorXml 拼凑用户的信息
	 * @return
	 *****************/
	public static void SendNotePaper(String strGuid,String dbName,String operatorXml,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("strGuid", strGuid);
		params.put("dbName", dbName);
		params.put("operatorXml", operatorXml);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "SendNotePaper", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 异步获得便笺列表
	 * @param pageSize 每页显示条目
	 * @param splitValue 取那个时间段数据标识（日期格式的字符串）
	 * @param userGuid 当前用户GUID
	 * @param status 状态（0是草稿箱，2是发件箱）
	 * @param dbName 当前用户的交换号（exchangeID）
	 * @param newOrOld 获取最新的还是后续的（1是最新，0是后续）
	 * @param callback 回调
	 */
	public static void GetNewestNotePaperList(String pageSize,String splitValue, String userGuid, String status,String dbName,String newOrOld, WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pageSize", pageSize);
		params.put("splitValue", splitValue);
		params.put("userGuid", userGuid);
		params.put("status", status);
		params.put("dbName", dbName);
		params.put("newOrOld", newOrOld);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetNewestNotePaperList", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/*******************
	 * 将邮件修改阅读状态为已阅读
	 * @param strGuid  待办Guid
	 * @param dbName 用户exchangeID
	 * 返回：成功返回1，失败返回0
	 * @param callback
	 */
	public static void UpdateIsNew(String strGuid,String dbName,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("strGuid", strGuid);
		params.put("dbName", dbName);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "UpdateIsNew", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/*******************
	 * 异步获得某个待办件的详情	
	 * @param strGuid 待办件的唯一标示
	 * @param exChangeId 用户交换号
	 * @param clientName 设备名称（比如Android系统参数是：“android”）
	 * @param outsysurl  跨系统webservice地址，如果不为空，获取的是跨系统的信息
	 */
	public static void GetTodealMutiInfoByGUID(String strGuid,String exChangeId,String clientName,String outsysurl,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("strGuid", strGuid);
		params.put("exChangeId", exChangeId);
		params.put("clientName", clientName);
		
		try{
			if(outsysurl != null && !outsysurl.equals("")){
				WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetTodealMutiInfoByGUID", outsysurl, params,callback);
			}else{
				WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetTodealMutiInfoByGUID", Constants.WEB_SERVICE_URL, params,callback);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	
	}
	
	/****************
	 * 获取流程详细信息
	 * @param flowGuid 流程件唯一唯一标示
	 */
	public static void GetFlowOpinionInfo(String flowGuid ,String outsysurl,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flowGuid", flowGuid);
		try{
			if(outsysurl != null && !outsysurl.equals("")){
				WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetFlowOpinionInfo", outsysurl, params,callback);
			}else{
				WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetFlowOpinionInfo", Constants.WEB_SERVICE_URL, params,callback);
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/*******************************
	 * 添加流程意见
	 * @param flowGuid 流程唯一标识
	 * @param opinion 意见内容
	 * @param operatorXml 添加一些扩展信息
	 * @param outsysurl 跨系统webservice地址，如果不为空是跨系统的流程件添加意见
	 * @param callback
	 */
	public static void InsertFlowOpinion(String flowGuid,String opinion,String operatorXml,String outsysurl,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flowGuid", flowGuid);
		params.put("opinion", opinion);
		params.put("operatorXml", operatorXml);
		try{
			if(outsysurl != null && !outsysurl.equals("")){
				WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "InsertFlowOpinion", outsysurl, params,callback);
			}else{
				WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "InsertFlowOpinion", Constants.WEB_SERVICE_URL, params,callback);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/*****************
	 * 更新流程意见 
	 * @param flowGuid 流程唯一标识
	 * @param opinion 意见内容
	 * @param operatorXml 扩展信息
	 * @param outsysurl 跨系统webservice地址，如果不为空是跨系统，更新流程意见
	 *****************/
	public static void UpdateFlowOpinion(String flowGuid,String opinion,String operatorXml,String outsysurl,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flowGuid", flowGuid);
		params.put("opinion", opinion);
		params.put("operatorXml", operatorXml);
		try{
			if(outsysurl != null && !outsysurl.equals("")){
				WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "UpdateFlowOpinion", outsysurl, params,callback);
			}else{
				WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "UpdateFlowOpinion", Constants.WEB_SERVICE_URL, params,callback);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/*********************
	 * 4、	回复待办件
	 * @param replyXml
	 *  <Reply>
 	    <Guid>待办件Guid</Guid>     
 	    <Content>内容</Content>      
 		<exchangeId>用户exchangeID</exchangeId>
 		</Reply>
	 * @param callback
	 */
	public static void submitReply(String replyXml,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("replyXml", replyXml);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "SubmitReply", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/******************************
	 * 流程处理结束
	 * @param flowGuid 流程唯一标识
	 * @param docType 是获取待办详细信息中的type节点的值
	 * @param operatorXml 扩展信息
	 * @param outsysurl 跨系统webservice地址，如果不为空是跨系统，处理结束
	 * @return
	 ******************************/
	public static void FinishFlowStep(String flowGuid,String docType,String operatorXml,String outsysurl,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flowGuid", flowGuid);
		params.put("docType", docType);
		params.put("operatorXml", operatorXml);
		try{
			if(outsysurl != null && !outsysurl.equals("")){
				WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "FinishFlowStep", outsysurl, params,callback);
			}else{
				WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "FinishFlowStep", Constants.WEB_SERVICE_URL, params,callback);
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/************************
	 * 	删除待办
	 * @param strGuid 待办件唯一标示
	 * @param dbName 用户交换号
	 * @param operatorXml 扩展信息
	 */
	public static void DeleteToDealByGuid(String strGuid,String dbName,String operatorXml,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("strGuid", strGuid);
		params.put("dbName", dbName);
		params.put("operatorXml", operatorXml);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "DeleteToDealByGuid", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}	
	}
	/*********************
	 * 更改待办信息状态到已办件 
	 * @param strGuid 待办件唯一标示
	 * @param dbName 用户交换号
	 * @param operatorXml 扩展信息
	 */
	public static void UpdateFinishDealList(String strGuid,String dbName,String operatorXml,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("strGuid", strGuid);
		params.put("dbName", dbName);
		params.put("operatorXml", operatorXml);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "UpdateFinishDealList", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public static String UpdateFinishDealList(String strGuid,String dbName,String operatorXml){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("strGuid", strGuid);
		params.put("dbName", dbName);
		params.put("operatorXml", operatorXml);
		String xml = "";
		try{
			xml = WebServiceUtils.invoke(Constants.NAMESPACE, "UpdateFinishDealList", Constants.WEB_SERVICE_URL, params);
		}catch (Exception e){
			e.printStackTrace();
		}
		return  xml;
	}
	/**********************
	 * 将待办置顶 标记为急要件
	 * @param strGuid 待办唯一标识 如果是多个guid 之间以英文分号分割
	 * @param isTop 置顶标志 0表示不置顶 1表示置顶
	 * @param operatorXml 扩展信息
	 */
	public static void UpdateIsTop(String strGuid,String isTop,String operatorXml,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("strGuid", strGuid);
		params.put("isTop", isTop);
		params.put("operatorXml", operatorXml);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "UpdateIsTop", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}	
	}
	/***************************
	 * 得到某条便笺信息(已发和草稿)
	 * @param strGuid 便笺唯一标示
	 * @param exchangeID 用户交换号
	 * @param userName 用户姓名
	 * @param userGuid 用户唯一标示
	 */
	public static void GetNotePaperInfoByGUID(String strGuid,String exchangeID,String userGuid,String userName, WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("strGuid", strGuid);
		params.put("exChangeId", exchangeID);
		params.put("userName", userName);
		params.put("userGuid", userGuid);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetNotePaperInfoByGUID", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/*******************
	 * 异步获得回复列表
	 * @param strGuid 唯一GUID
	 * @param userGuid 当前用户GUID
	 * @param callback 回调
	 *******************/
	public static void GetRelationReNotePaperByGuid(String strGuid, String userGuid, WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("strGuid", strGuid);
		params.put("userGuid", userGuid);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetRelationReNotePaperByGuid",  Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**********
	 * 删除收件箱的便笺（查回复）
	 * @param strGuid 便笺的唯一标示
	 * @param dbName 当前用户的交换号
	 * @return
	 **********/
	public static void DeleteRevNotePaperByGuid(String strGuid,String dbName,String userGuid,String operatorXml,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("strGuid", strGuid);
		params.put("dbName", dbName);
		params.put("userGuid", userGuid);
		params.put("operatorXml", operatorXml);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "DeleteRevNotePaperByGuid", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/***************
	 * 1、	转发便笺
	 * @param preGuid 转发的便笺guid
	 * @param dispatchPerson 发送人
	 * @param dispatchPersonGuid 发送人guid
	 * @param dbName 用户交换号
	 * @param operatorXml 扩展信息
	 ***************/
	public static void RepeatNotePaper(String preGuid,String dispatchPerson,String dispatchPersonGuid,String dbName,String operatorXml,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("preGuid", preGuid);
		params.put("dispatchPerson", dispatchPerson);
		params.put("dispatchPersonGuid", dispatchPersonGuid);
		params.put("dbName", dbName);
		params.put("operatorXml", operatorXml);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "RepeatNotePaper", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/********
	 * 删除已办和草稿便笺件
	 * @param strGuid 便笺Guid
	 * @param dbName 用户exchangeID
	 * @param userGuid 当前用户的guid
	 * @param operatorXml 扩展信息
	 * @return
	 */
	public static void DeleteMyNotePaperByGuid(String strGuid,String dbName,String userGuid,String operatorXml,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("strGuid", strGuid);
		params.put("dbName", dbName);
		params.put("userGuid", userGuid);
		params.put("operatorXml", operatorXml);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "DeleteMyNotePaperByGuid", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}	
	}
	/*********************
	 * 获取某个文件的回执简略信息
	 * @param strGuid 文档唯一标识
	 * @param rollbackStatus 回执类型 0：文件回执；1：流程回执；
	 * @param docType 文档模块 如便笺模块是290；
	 *********************/
	public static void GetFileRollbackSimpleInfo (String strGuid,String rollbackStatus, String docType, WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("strGuid", strGuid);
		params.put("rollbackStatus", rollbackStatus);
		params.put("docType", docType);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetFileRollbackSimpleInfo", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**************
	 * 2.	根据发送人获取待办列表，相关件
	 * @param sendGuid 发送人唯一标示
	 * @param pageSize 一页显示的条目数
	 * @param splitValue 当前最新条目的标识值
	 * @param userGuid 当前用户唯一标示
	 * @param sign 待办或已办
	 * @param dbName 当前用户的数据库链接
	 * @param newOrOld 获取最新的还是后续的
	 */
	public static void GetToDealListBySendGuid(String sendGuid,String pageSize,String splitValue, String userGuid, String sign,String dbName,String newOrOld, WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sendGuid", sendGuid);
		params.put("pageSize", pageSize);
		params.put("splitValue", splitValue);
		params.put("userGuid", userGuid);
		params.put("sign", sign);
		params.put("dbName", dbName);
		params.put("newOrOld", newOrOld);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetToDealListBySendGuid", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**********************
	 * 设置默认组
	 * @param userGuid 用户唯一标识
	 * @param groupGuid 要添加的默认组唯一标识
	 * @param dbName 数据库连接串
	 * @param callback
	 */
	public static void AddDefaultCommonGroup(String userGuid, String groupGuid, String dbName,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userGuid", userGuid);
		params.put("groupGuid", groupGuid);
		params.put("dbName", dbName);
		try{
			 WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "AddDefaultCommonGroup", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取个人组信息
	 * @param userGuid 当前用户唯一标识
	 * @param dbName 用户交换号
	 */
	public static void GetPersonalGroups(String userGuid, String dbName, WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userGuid", userGuid);
		params.put("dbName", dbName);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetPersonalGroups", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/****************
	 * 删除个人组
	 * @param guidStr 组唯一标示，多个用分号”;”分隔
	 * @param dbName 用户交换号
	 * @param operatorXml
	 * @return
	 */
	public static void DeletePersonalGroupList(String guidStr, String dbName,String operatorXml,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("guidStr", guidStr);
		params.put("dbName", dbName);
		params.put("operatorXml", operatorXml);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "DeletePersonalGroupList", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}	
	}
	/***************
	 * 添加个人组
	 * @param groupInfoXml
	 * @param dbName
	 * @return
	 */
	public static void AddPersonalGroup(String groupInfoXml, String dbName,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("groupInfoXml", groupInfoXml);
		params.put("dbName", dbName);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "AddPersonalGroup", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}	
	}
	/*********************
	 * 自定义组删除成员
	 * @param userGuidStr 用户唯一标示，多个用分号”;”分隔
	 * @param groupGuid 个人组唯一标示
	 * @param dbName 数据库连接串
	 * @param operatorXml
	 */
	public static void DeletePersonsInPersonalGroup(String userGuidStr,String groupGuid,String dbName,String operatorXml,WebService.Callback callback){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userGuidStr", userGuidStr);
		params.put("groupGuid", groupGuid);
		params.put("dbName", dbName);
		params.put("operatorXml", operatorXml);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "DeletePersonsInPersonalGroup", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加人到个人组用户
	 * @param usersinfoXml 用户登录名
	 * @param groupGuid 用户组guid
	 * @param dbName 用户信息交换码
	 * @return
	 */
	public static void AddPersonalGroupPerson(String usersinfoXml,String groupGuid, String dbName,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usersinfoXml", usersinfoXml);
		params.put("groupGuid", groupGuid);
		params.put("dbName", dbName);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "AddPersonalGroupPerson",  Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/****************
	 * 获取急要件
	 * @param pageSize
	 * @param splitValue
	 * @param userGuid
	 * @param newOrOld
	 * @param isTop
	 * @param callback
	 */
	public static void GetToDealListByTop(String pageSize,String splitValue, String userGuid, String newOrOld,String isTop, WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pageSize", pageSize);
		params.put("splitValue", splitValue);
		params.put("userGuid", userGuid);
		params.put("newOrOld", newOrOld);
		params.put("isTop", isTop);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetToDealListByTop", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/***********
	 * 获取待办件的排序
	 * @param guid 用户唯一标示
	 * @return
	 */
	public static void GetUserTodealOrder(String guid,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userGuid", guid);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetUserTodealOrder", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/******************
	 * 上传待办件设置的排序
	 * @param guid
	 * @param username
	 * @param orderFlag
	 * @return
	 */
	public static void SetUserTodealOrder(String guid,String username,String orderFlag,WebService.Callback callback){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userGuid", guid);
		params.put("username", username);
		params.put("orderFlag", orderFlag);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "SetUserTodealOrder", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/***************
	 * 根据待办件标题搜索获取最新的待办
	 * @param title
	 * @param pageSize
	 * @param splitValue
	 * @param userGuid
	 * @param sign
	 * @param dbName
	 * @param newOrOld
	 * @param callback
	 */
	public static void GetSearchToDealList(String title,String pageSize,String splitValue, String userGuid, String sign,String dbName,String newOrOld,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("title", title);
		params.put("pageSize", pageSize);
		params.put("splitValue", splitValue);
		params.put("userGuid", userGuid);
		params.put("sign", sign);
		params.put("dbName", dbName);
		params.put("newOrOld", newOrOld);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetSearchToDealList", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 异步根据便笺标题搜索已发或者草稿信息
	 * @param pageSize
	 * @param splitValue
	 * @param userGuid
	 * @param status
	 * @param dbName
	 * @param newOrOld
	 * @param callback
	 */
	public static void GetSearchNotePaperList(String title, String pageSize,String splitValue, String userGuid, String status,String dbName,String newOrOld, WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("title", title);
		params.put("pageSize", pageSize);
		params.put("splitValue", splitValue);
		params.put("userGuid", userGuid);
		params.put("status", status);
		params.put("dbName", dbName);
		params.put("newOrOld", newOrOld);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetSearchNotePaperList", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 最新开发用户校验，VPN也用此方法
	 * @param appID 应用标示
	 * @param accountName
	 * @param strPassword
	 * @param extInfo
	 * @param callback
	 */
	public static void GetUserInfoFromMobile(String appID, String accountName, String strPassword, String extInfo,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appID", appID);
		params.put("accountName", accountName);
		params.put("strPassword", strPassword);
		params.put("extInfo", extInfo);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetUserInfoFromMobile", Constants.WEB_GETUSERINFO_SERVICE, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/***************************
	 * 联系人，查看某一个联系人的详细信息，该方法现在只用到手机号
	 * @param userGuid 用户唯一标识
	 */
	public static void GetUserInfoByGuid(String userGuid,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userGuid",userGuid);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetUserInfoByGuid", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/****************************
	 * 根据待办唯一标识获取外链流程件的系统接口地址
	 * @param todealGuid  待办件唯一标示
	 * @param callback
	 */
	public static void GetTodealOutServiceUrl(String todealGuid,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("todealGuid",todealGuid);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetTodealOutServiceUrl", Constants.WEB_SERVICE_URL, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/*********************
	 * 外系统获取流程件详细信息
	 * @param fileGuid  文档唯一标识
	 * @param docType 邮件类型(290便签件；10收文；20发文；40地方信息；60会议)
	 * @param userName 用户名
	 * @param userGuid 用户表示
	 */
	public static void GetFlowFileInfo(String fileGuid, String docType, String userName, String userGuid ,String outsysurl ,WebService.Callback callback){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fileGuid",fileGuid);
		params.put("docType",docType);
		params.put("userName",userName);
		params.put("userGuid",userGuid);
		
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetFlowFileInfo", outsysurl, params, callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/************
	 * 获取流程件的流程环节
	 * @param docType 流程标示
	 ************/
	public static void GetMoudleFlowStepInfo(String docType,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("docType", docType);		
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetMoudleFlowStepInfo", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/************
	 * 发文流转
	 * @param flowCenterGuid 流程唯一标示
	 * @param docGuid
	 * @param docType 邮件类型
	 * @param flowType 流转标示
	 * @param flowTypeName 流转名称
	 * @param strFlowUser
	 * @param sendPersonName
	 * @param sendPersonGuid
	 * @return
	 */
	public static void SendProcessEmail(String flowCenterGuid, String docGuid, String docType, String flowType,
			String flowTypeName, String strFlowUser, String sendPersonName, String sendPersonGuid,WebService.Callback callback){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flowCenterGuid", flowCenterGuid);
		params.put("docGuid", docGuid);
		params.put("docType", docType);
		params.put("flowType", flowType);
		params.put("flowTypeName", flowTypeName);
		params.put("strFlowUser", strFlowUser);
		params.put("sendPersonName", sendPersonName);
		params.put("sendPersonGuid", sendPersonGuid);
		
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "CreateAndSendFlowStepProcess", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**********************
	 * 通过模块发送类名返回上级流程(返上级流程)
	 * @param flowGuid 流程唯一标识
	 * @param flowSendType 模块发送类名称
	 * @param opinion 返上级流程意见内容
	 */
	public static void BackUpFlowProcessBySendType (String flowGuid, String flowSendType, String opinion,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flowGuid", flowGuid);
		params.put("flowSendType", flowSendType);
		params.put("opinion", opinion);
		
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "BackUpFlowProcessBySendType", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/********************
	 * 根据登陆用户的唯一标识获取授权用户信息接口，获取当前可以切换的用户
	 * @param userGuid 用户唯一标识
	 * @param callback
	 */
	
	public static void GetAuthorizedInfo(String userGuid,WebService.Callback callback){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userGuid", userGuid);
		
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetAuthorizedInfo", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/*****
	 * 设备认证
	 * @param deviceSN
	 * @param callback
	 */
	public static void  GetMachineCodeInfo(String deviceSN,WebService.Callback callback){
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceSN", deviceSN);
		try{
			 WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetMachineCodeInfo", Constants.WEB_GETUSERINFO_SERVICE, params,callback);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	/***************
	 * 签批上传接口
	 * */
	public static void UpDataPdfAttachment(String attachInfoXml,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("attachInfoXml", attachInfoXml);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "UpdateAttachmentInfo", Constants.WEB_SERVICE_URL, params,callback);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void UpdateUserPassword(String userName,String oldPassword,String newPassword,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName", userName);
		params.put("oldPassword", oldPassword);
		params.put("newPassword", newPassword);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "UpdateUserPassword", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/*******
	 *  获取未读个数
	 * @param isNew 0是已读 1未读
	 * @param sign 0表示待办 1表示已办
	 * @param userGuid
	 * @param dbName
	 * @param callback
	 */
	public static void GetToDealCountByStatus(String isNew,String sign,String userGuid,String dbName,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("isNew", isNew);
		params.put("sign", sign);
		params.put("userGuid", userGuid);
		params.put("dbName", dbName);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetToDealCountByStatus", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/***********
	 * 获取除了签批单以外的附件
	 * @param strGuid
	 * @param dbName
	 * @param callback
	 */
	public static void GetSomeAttachmentByGuid(String strGuid, String dbName,String outUrl,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("strGuid", strGuid);
		params.put("dbName", dbName);

		try{if(outUrl != null && !outUrl.equals("")){
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE,"GetSomeAttachmentByGuid", outUrl, params,callback);
		}else {
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetSomeAttachmentByGuid", Constants.WEB_SERVICE_URL, params, callback);
		}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public static String SaveBlobAttach(String fileName,String fileGuid ,String blobGuid,String fileData,String extendInfo,String outurl){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fileName", fileName);
		params.put("fileGuid", fileGuid);
		params.put("blobGuid", blobGuid);
		params.put("fileData", fileData);
		params.put("extendInfo", extendInfo);
		String result = "";
		try{
			if(outurl != null && !outurl.equals("")){
				result = WebServiceUtils.invoke(Constants.NAMESPACE, "SaveBlobAttach",  outurl, params);
			}else{
				result = WebServiceUtils.invoke(Constants.NAMESPACE, "SaveBlobAttach",  Constants.WEB_SERVICE_URL, params);
			}
		}catch (Exception e){
			result = "";
			e.printStackTrace();
		}
		return result;
	}
	/*********
	 * 新增签批单上传方法可以上传大附件
	 * @param attachInfoXml
	 * @param callback
	 */
	public static void UpdateAttachmentInfoForBigAttach(String attachInfoXml,String outUrl,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("attachInfoXml", attachInfoXml);
		try{
			if(outUrl != null && !outUrl.equals("")){
				WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "UpdateAttachmentInfoForBigAttach",outUrl, params,callback);
			}else{
				WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "UpdateAttachmentInfoForBigAttach",Constants.WEB_SERVICE_URL, params,callback);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	/******************
	 * 新移动办公添加意见处理结束方法
	 * @param flowGuid 当前流程标识
	 * @param docType 模块号
	 * @param opinion 意见内容。手写签批传入“签批意见”
	 * @param operatorXml 操作用户xml
	 * @param returnType 普通添加意见0，手写签批意见5
	 */
	public static void EndFlowWithFlowOpinion(String flowGuid, String docType, String opinion, String operatorXml,String returnType,String outUrl,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flowGuid", flowGuid);
		params.put("docType", docType);
		params.put("opinion", opinion);
		params.put("operatorXml", operatorXml);
		params.put("returnType", returnType);
		params.put("isOutFlow", "0");
		try{
			if(outUrl != null&& !outUrl.equals("")){
				WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "EndFlowWithFlowOpinion", outUrl, params,callback);
			}else{
				WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "EndFlowWithFlowOpinion", Constants.WEB_SERVICE_URL, params,callback);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**********
	 * 外系统流程获取，外系统的接口地址
	 * @param guid
	 * @param url
	 * @param isout
	 * @param callback
	 */
	public static void GetSignUrl(String guid, String url, String isout,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("guid", guid);
		params.put("url", url);
		params.put("isout", isout);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetSignUrl",  Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/********************
	 * 便笺转收文接口
	 * @param strGuid 便笺唯一标识
	 * @param operatorXml 操作人信息XML
	 * <root>
	<userguid><![CDATA[用户唯一标识]]></userguid>
	<username><![CDATA[用户名]]></username>
	<devicename><![CDATA[设备名]]></ devicename>
	<title><![CDATA[标题]]></title>
	</root>
	 */
	public static void NotePaperToReceiveFile(String strGuid,String operatorXml,WebService.Callback callback){

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("strGuid", strGuid);
		params.put("operatorXml", operatorXml);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "NotePaperToReceiveFile", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 附件转成ofd
	 * @param blobGuid 附件blobGuid
	 * @param callback
	 */
	public static void GetOFDFile(String blobGuid ,String fileURL,WebService.Callback callback){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bolbGuid", blobGuid);
		params.put("fileURL", fileURL);
		try{
			WebServiceUtils.asyncInvoke(Constants.NAMESPACE, "GetOFDFile", Constants.WEB_SERVICE_URL, params,callback);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
