package com.android.yawei.jhoa.bean;

public class AppOptionBean {

	private int imageId;
	private String name;
	private String enName;
	private String emailSize;
	private String modulename;
	private String moduleurl;
	private String type;//授权类型 0部门 1个人 2公开
	private String moduletype; //模块打开类型  0：H5应用； 1：混合应用；2：独立应用；3：自带应用
	private String imageurl;//网络图片

	public int getImageId() {

		return imageId;
	}
	public void setImageId(int imageId) {

		this.imageId = imageId;
	}
	public String getName() {

		return name;
	}
	public void setName(String name) {

		this.name = name;
	}
	public String getEnName() {

		return enName;
	}
	public void setEnName(String enName) {

		this.enName = enName;
	}
	public String getEmailSize() {

		return emailSize;
	}
	public void setEmailSize(String emailSize) {

		this.emailSize = emailSize;
	}

	public String getModulename() {

		return modulename;
	}

	public void setModulename(String modulename) {

		this.modulename = modulename;
	}

	public String getModuleurl() {

		return moduleurl;
	}

	public void setModuleurl(String moduleurl) {

		this.moduleurl = moduleurl;
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

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
}
