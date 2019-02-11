package com.android.yawei.jhoa.bean;

//登录成功之后获取用户信息
public class RootUserBean {
	private String userName;//登录的用户名
	private String displayName;//用户的名称(昵称)
	private String path;//用户的长路径，代表在域的路径
	private String depart;//用户部门
	private String adGuid;//用户guid唯一标识
	private String exchangeID;//用户的交换号
	private String sysflag;//用户的新旧系统标示
	private String webserviceurl;//用户的使用的业务地址
	private String oaUrl;//办公地址
	private String orginuserName;//原始登录人的登录名
	private String orgindisplayName;//原始登录人的
	private String orginadGuid;//原始登录人的
	private String parameter;//交互初始化所需要参数
	private String deveInfo;//设备信息
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
	public String getWebserviceurl() {
		return webserviceurl;
	}
	public void setWebserviceurl(String webserviceurl) {
		this.webserviceurl = webserviceurl;
	}

	public String getOrginuserName() {
		return orginuserName;
	}

	public void setOrginuserName(String orginuserName) {
		this.orginuserName = orginuserName;
	}

	public String getOrgindisplayName() {
		return orgindisplayName;
	}

	public void setOrgindisplayName(String orgindisplayName) {
		this.orgindisplayName = orgindisplayName;
	}

	public String getOrginadGuid() {
		return orginadGuid;
	}

	public void setOrginadGuid(String orginadGuid) {
		this.orginadGuid = orginadGuid;
	}

	public String getOaUrl() {
		return oaUrl;
	}

	public void setOaUrl(String oaUrl) {
		this.oaUrl = oaUrl;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getDeveInfo() {
		return deveInfo;
	}

	public void setDeveInfo(String deveInfo) {
		this.deveInfo = deveInfo;
	}
}
