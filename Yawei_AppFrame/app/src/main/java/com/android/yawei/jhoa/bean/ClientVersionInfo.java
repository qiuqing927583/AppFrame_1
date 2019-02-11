package com.android.yawei.jhoa.bean;

/***************
 * 
 * @author Yusz
 * app获取版本信息类
 *
 */
public class ClientVersionInfo {
	
	private String name=null;
	private String version=null;
	private String url=null;
	private String length=null;
	private String debugversion=null;
	private String debugurl=null;
	private String debuglength=null;
	private String upmessage = null;
	private String wwwversion = null;
	private String wwwfilelength = null;
	private String wwwurl = null;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getDebugversion() {
		return debugversion;
	}
	public void setDebugversion(String debugversion) {
		this.debugversion = debugversion;
	}
	public String getDebugurl() {
		return debugurl;
	}
	public void setDebugurl(String debugurl) {
		this.debugurl = debugurl;
	}
	public String getDebuglength() {
		return debuglength;
	}
	public void setDebuglength(String debuglength) {
		this.debuglength = debuglength;
	}
	public String getUpmessage() {
		return upmessage;
	}
	public void setUpmessage(String upmessage) {
		this.upmessage = upmessage;
	}

	public String getWwwversion() {
		return wwwversion;
	}

	public void setWwwversion(String wwwversion) {
		this.wwwversion = wwwversion;
	}

	public String getWwwfilelength() {
		return wwwfilelength;
	}

	public void setWwwfilelength(String wwwfilelength) {
		this.wwwfilelength = wwwfilelength;
	}

	public String getWwwurl() {
		return wwwurl;
	}

	public void setWwwurl(String wwwurl) {
		this.wwwurl = wwwurl;
	}
}
