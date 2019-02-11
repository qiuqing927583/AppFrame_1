package com.android.yawei.jhoa.bean;

/**
 * 用户切换
 */
public class SwitchUserBean {

	private String userName;//登录用户名
	private String displayName;//用户名称
	private String path;
	private String depart;//部门
	private String adGuid;//用户guid
	private String exchangeID;//用户交换号
	private String sysflag;//新老旧系统表示
	private String webserviceurl;//webservice
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
	
	
}
