package com.android.yawei.jhoa.bean;

/**
 * 通讯录获取人的所有信息
 */
public class GetUserPhoneBean {

	private String displayName;//用户名
	private String path;//用户路径
	private String adGuid;//用户唯一标示
	private String depart;//用户部门
	private String exchangeID;//用户交换号
	private String sysflag;//新旧系统标示
	private String usertel;//手机号
	private String accountname;//账号
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
	public String getAdGuid() {
		return adGuid;
	}
	public void setAdGuid(String adGuid) {
		this.adGuid = adGuid;
	}
	public String getDepart() {
		return depart;
	}
	public void setDepart(String depart) {
		this.depart = depart;
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
	public String getUsertel() {
		return usertel;
	}
	public void setUsertel(String usertel) {
		this.usertel = usertel;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}
}
