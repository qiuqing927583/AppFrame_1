package com.android.yawei.jhoa.bean;

import java.io.Serializable;
import java.util.List;

//联系人组数据
public class GroupBean implements Serializable{
	private String name;//组名和commonName是一样的，显示的时候用commonName
	private String commonName;//组名和name是一样的
	private String guid;//组唯一标示
	private String type;//1表示域上的组，2表示数据库中的个人组
	private List<User> users; //组下的人员
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	
	
}
