package com.android.yawei.jhoa.factory;

import android.content.Context;

import com.android.yawei.jhoa.bean.A_DBJ;
import com.android.yawei.jhoa.bean.Attachment;
import com.android.yawei.jhoa.bean.ClientVersionInfo;
import com.android.yawei.jhoa.bean.DataSet;
import com.android.yawei.jhoa.bean.FileRollBack;
import com.android.yawei.jhoa.bean.FlowOpinion;
import com.android.yawei.jhoa.bean.GroupBean;
import com.android.yawei.jhoa.bean.InboxDetail;
import com.android.yawei.jhoa.bean.InboxItem;
import com.android.yawei.jhoa.bean.RootUserBean;
import com.android.yawei.jhoa.bean.User;
import com.android.yawei.jhoa.bean.UserCheckCode;
import com.android.yawei.jhoa.bean.UserLog;
import com.android.yawei.jhoa.bean.UserStatusBean;
import com.android.yawei.jhoa.parser.A_DBJ_Parse;
import com.android.yawei.jhoa.parser.FileRollBackParse;
import com.android.yawei.jhoa.parser.FlowParser;
import com.android.yawei.jhoa.parser.GPersonParser;
import com.android.yawei.jhoa.parser.GroupParser;
import com.android.yawei.jhoa.parser.InboxDetailParse;
import com.android.yawei.jhoa.parser.InboxItemParse;
import com.android.yawei.jhoa.parser.LogParser;
import com.android.yawei.jhoa.parser.LookAttachmentParse;
import com.android.yawei.jhoa.parser.RootUserParser;
import com.android.yawei.jhoa.parser.SwitchUserParse;
import com.android.yawei.jhoa.parser.UserCheckCodeParser;
import com.android.yawei.jhoa.parser.UserCheckConfig;
import com.android.yawei.jhoa.parser.VersionParse;
import com.android.yawei.jhoa.uploadfile.UpLoadFileBean;
import com.android.yawei.jhoa.uploadfile.UpLoadFileParse;
import com.android.yawei.jhoa.utils.XmlUtils;

import java.util.ArrayList;
import java.util.List;


//解析所有xml工厂
public class ResolveXML {
	/******************************
	 * 解析用户登录成功之后，用户的信息
	 *****************************/
	public static RootUserBean ParseUser(String xml){
		if (xml == null){
			throw new NullPointerException();
		}
		RootUserBean dataSet = new RootUserBean();//实例化类工厂
		RootUserParser bp = new RootUserParser(dataSet);//类工厂传入，获取出解析的数据
		try{
			XmlUtils.saxParse(xml, bp);
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}
	/************用户切换获取授权的用户*****************/
	public static List<RootUserBean>ParseSwitchUser(String xml){
		if (xml == null){
			throw new NullPointerException();
		}
		List<RootUserBean> dataSet = new ArrayList<RootUserBean>();//实例化类工厂
		SwitchUserParse bp = new SwitchUserParse(dataSet);//类工厂传入，获取出解析的数据
		try{
			XmlUtils.saxParse(xml, bp);
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}
	/**************************************
	 * 选择人联系组
	 * @param xml
	 * @return
	 **************************************/
	public static List<GroupBean> parseGroup(String xml){
		if(xml == null || "".equals(xml) || "anyType".equalsIgnoreCase(xml)){
			return null;
		}
		List<GroupBean> dataSet = new ArrayList<GroupBean>();
		GroupParser gp = new GroupParser(dataSet);
		try{
			XmlUtils.saxParse(xml, gp);
	
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}
	/**
	 * 获取组下成员数据集
	 * @param xml
	 * @return
	 */
	public static List<User> parseGPerson(String xml){
		if(xml == null || "".equals(xml) || "anyType".equalsIgnoreCase(xml)){
			return null;
		}
		List<User> dataSet = new ArrayList<User>();
		GPersonParser gp = new GPersonParser(dataSet);
		try{
			XmlUtils.saxParse(xml, gp);
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}
	/**
	 * 构造DataSet数据用户展现
	 * @param xml 
	 * @param context
	 * @return
	 */
	public static List<InboxItem> parseInbox(String xml, Context context){
		if (xml == null){
			throw new NullPointerException();
		}
		List<InboxItem> dataSet = new ArrayList<InboxItem>();
		InboxItemParse bp = new InboxItemParse(dataSet);
		try{
			XmlUtils.saxParse(xml, bp);
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}
	/****************
	 * 解析待办件
	 * @param xml
	 * @return
	 */
	public static List<A_DBJ> parse_A_DBJ(String xml){
		if (xml == null){
			throw new NullPointerException();
		}
		List<A_DBJ> dataSet = new ArrayList<A_DBJ>();

		A_DBJ_Parse bp = new A_DBJ_Parse(dataSet);
		try{
			XmlUtils.saxParse(xml, bp);
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}
	/**************
	 * 解析添加的流程意见
	 * @param xml
	 * @return
	 */
	public static List<FlowOpinion> parseFlow(String xml){
		if (xml == null){
			throw new NullPointerException();
		}
		List<FlowOpinion> dataSet = new ArrayList<FlowOpinion>();
		FlowParser flowParser = new FlowParser(dataSet);
		try{
			XmlUtils.saxParse(xml, flowParser);
	
		}catch (Exception e){
			e.printStackTrace();
		}

		return dataSet;
	}
	/************************
	 * 解析草稿或者
	 * @param xml
	 * @return
	 */
	public static List<InboxDetail> parseInbox(String xml){
		if (xml == null){
			throw new NullPointerException();
		}

		List<InboxDetail> dataSet = new ArrayList<InboxDetail>();

		InboxDetailParse bp = new InboxDetailParse(dataSet);

		try{
			XmlUtils.saxParse(xml, bp);
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}
	
	/**************
	 * 解析查回复
	 * @param xml
	 * @return
	 */
	public static  List<InboxItem> RichardReply(String xml){
		
		if (xml == null){
			throw new NullPointerException();
		}

		List<InboxItem> dataSet = new ArrayList<InboxItem>();

		InboxItemParse bp = new InboxItemParse(dataSet);

		try{
			XmlUtils.saxParse(xml, bp);
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}
	/******
	 * 解析已发件查回执
	 */
	public static List<FileRollBack> SetInboxItem(String xml){

		if (xml == null){
			throw new NullPointerException();
		}

		List<FileRollBack> dataSet = new ArrayList<FileRollBack>();
		FileRollBackParse bp = new FileRollBackParse(dataSet);

		try{
			XmlUtils.saxParse(xml, bp);
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;

	}
	/*********
	 * 解析登录日志
	 * @param xml
	 * @return
	 */
	public static DataSet<UserLog> parseLog(String xml){
		if (xml == null){
			throw new NullPointerException();
		}

		DataSet<UserLog> dataSet = new DataSet<UserLog>();

		LogParser bp = new LogParser(dataSet);
		try{
			XmlUtils.saxParse(xml, bp);
	
		}catch (Exception e){
			e.printStackTrace();
		}

		return dataSet;
	}
	/*************
	 * 解析登录用户校验
	 * @param xml
	 * @return
	 */
	public static UserStatusBean parseUserCheck(String xml){
		if (xml == null){
			throw new NullPointerException();
		}

		UserStatusBean dataSet = new UserStatusBean();

		UserCheckConfig bp = new UserCheckConfig(dataSet);
		try{
			XmlUtils.saxParse(xml, bp);
	
		}catch (Exception e){
			e.printStackTrace();
		}

		return dataSet;
	}
	
	public static UserCheckCode UserCheckCodeParser(String xml){
		if (xml == null){
			throw new NullPointerException();
		}

		UserCheckCode dataSet = new UserCheckCode();

		UserCheckCodeParser bp = new UserCheckCodeParser(dataSet);
		try{
			XmlUtils.saxParse(xml, bp);
	
		}catch (Exception e){
			e.printStackTrace();
		}

		return dataSet;
	}

	/**
	 * 解析大附件上传
	 * @param xml
	 * @return
	 */
	public static UpLoadFileBean parseFileUp(String xml){
		if(xml == null || "".equals(xml) || "anyType".equalsIgnoreCase(xml)){
			return null;
		}
		UpLoadFileBean dataSet = new UpLoadFileBean();
		UpLoadFileParse gp = new UpLoadFileParse(dataSet);
		try{
			XmlUtils.saxParse(xml, gp);
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}

	/**
	 * 解析版本信息
	 * @param xml
	 * @return
	 */
	public static List<ClientVersionInfo> parseVersion(String xml){
		if (xml == null){
			throw new NullPointerException();
		}
		List<ClientVersionInfo> dataSet = new ArrayList<>();
		VersionParse bp = new VersionParse(dataSet);
		try{
			XmlUtils.saxParse(xml, bp);
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}

	/**
	 * 解析附件
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static List<Attachment> Mod_AllDBJMsg(String xml)throws Exception{
		if (xml == null){
			throw new NullPointerException();
		}
		List<Attachment> dataSet = new ArrayList<Attachment>();

		LookAttachmentParse bp = new LookAttachmentParse(dataSet);
		try{
			XmlUtils.saxParse(xml, bp);
		}catch (Exception e){
			e.printStackTrace();
		}
		return dataSet;
	}
}
