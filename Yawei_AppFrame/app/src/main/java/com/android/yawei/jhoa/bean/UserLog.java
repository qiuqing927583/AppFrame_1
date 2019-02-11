package com.android.yawei.jhoa.bean;

/**
 * 登录日志
 */
public class UserLog {
	private String clientip=null;//登录ip
	private String logontime=null;//登录时间
	public String getClientip() {
		return clientip;
	}
	public void setClientip(String clientip) {
		this.clientip = clientip;
	}
	public String getLogontime() {
		return logontime;
	}
	public void setLogontime(String logontime) {
		this.logontime = logontime;
	}
}
