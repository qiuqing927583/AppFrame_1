package com.android.yawei.jhoa.bean;

import java.io.Serializable;

/**
 * 登录用户的相关信息
 */
public class UserStatusBean implements Serializable{
	/****别表示0 用户校验码错误；1用户不存在；2该用户未开通移动办公；3管理员锁定；4个人锁定；5临时锁定；6移动办公开通正常
	当且仅当status值为6时，才有其他四个节点
	*/
	private String status;
	private String userguid;//用户为标示
	private String exchangeid;//交换号
	private String vpnip;//用户对用的vpn
	private String oaurl;//业务地址，获取用户信息地址
	private String accountname;//用户登录名
	private String errorflag;//如果status为2或者是3时错误信息
	private String flag;//新旧系统标示
	private String depart;//用户部门
	private String name;//中文名称
	private String homeurl;//自定义主界面

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserguid() {
		return userguid;
	}
	public void setUserguid(String userguid) {
		this.userguid = userguid;
	}
	public String getExchangeid() {
		return exchangeid;
	}
	public void setExchangeid(String exchangeid) {
		this.exchangeid = exchangeid;
	}
	public String getVpnip() {
		return vpnip;
	}
	public void setVpnip(String vpnip) {
		this.vpnip = vpnip;
	}
	public String getOaurl() {
		return oaurl;
	}
	public void setOaurl(String oaurl) {
		this.oaurl = oaurl;
	}
	public String getAccountname() {
		return accountname;
	}
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public String getErrorflag() {
		return errorflag;
	}

	public void setErrorflag(String errorflag) {
		this.errorflag = errorflag;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHomeurl() {
		return homeurl;
	}

	public void setHomeurl(String homeurl) {
		this.homeurl = homeurl;
	}
}
