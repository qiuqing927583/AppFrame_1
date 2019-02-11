package com.android.yawei.jhoa.bean;

import java.io.Serializable;

//选择联系人，每一个部门中的人员
@SuppressWarnings("serial")
public class User implements Serializable{
	private String userName=null; //用户登录的用户名
	private String displayName=null; //用户的真实姓名
	private String path=null; //服务器端用户存在的实际位置
	private String depart=null;//部门
	private String adGuid=null; //用户唯一标示
	private String exchangeID=null; //用户的交换号
	private String sysflag=null;//新老系统标示
	private int checked; // 0 未选中 1选中
	private String webseriveurl=null;//webservice地址
	private String groupGuid=null;//默认组GUID
	private String groupName = null;//默认组名称
	private String groupType=null;//默认组类型
	private String fulldisplayName=null; //单位简称
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDepart() {
		return depart;
	}
	public void setDepart(String depart) {
		this.depart = depart;
	}
	public String getAdGuid() {
		return adGuid;
	}
	public void setAdGuid(String adGuid) {
		this.adGuid = adGuid;
	}
	public String getExchangeID() {
		return exchangeID;
	}
	public void setExchangeID(String exchangeID) {
		this.exchangeID = exchangeID;
	}
	public String getSysflag() {
		return sysflag;
	}
	public void setSysflag(String sysflag) {
		this.sysflag = sysflag;
	}

	public int getChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked;
	}
	public String getWebseriveurl() {
		return webseriveurl;
	}
	public void setWebseriveurl(String webseriveurl) {
		this.webseriveurl = webseriveurl;
	}
	public String getGroupGuid() {
		return groupGuid;
	}
	public void setGroupGuid(String groupGuid) {
		this.groupGuid = groupGuid;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getFulldisplayName() {
		return fulldisplayName;
	}
	public void setFulldisplayName(String fulldisplayName) {
		this.fulldisplayName = fulldisplayName;
	}
	
}
