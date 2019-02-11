package com.android.yawei.jhoa.bean;

/**
 * 新版轻应用
 */
public class MoreAppBean {

	private String appName;//名称
	private int appDrawable = 0;//图标
	private String imgUrl;//网络图标
	private String guid;//应用guid
	private String linkUrl;//轻应用连接
	private String isTrans;//轻应用是否授权 0不需要认证，1需要认证
	private String isnew = "0";//正式版如果有最新测试版提示
	private String type;//授权类型 0部门 1个人 2公开
	private String moduletype; //模块打开类型  0：H5应用； 1：混合应用；2：独立应用；3：自带应用
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public int getAppDrawable() {
		return appDrawable;
	}
	public void setAppDrawable(int appDrawable) {
		this.appDrawable = appDrawable;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getIsTrans() {
		return isTrans;
	}
	public void setIsTrans(String isTrans) {
		this.isTrans = isTrans;
	}
	public String getIsnew() {
		return isnew;
	}
	public void setIsnew(String isnew) {
		this.isnew = isnew;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModuletype() {
		return moduletype;
	}

	public void setModuletype(String moduletype) {
		this.moduletype = moduletype;
	}
}
