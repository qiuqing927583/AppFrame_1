package com.android.yawei.jhoa.bean;

import java.io.Serializable;
/**
 * 草稿已发列表
 */
public class InboxItem implements Serializable {
	/**
	 * guid便笺唯一标示
	 */
	private String guid;
	/**
	 * flowguid流程件唯一标示
	 */
	private String flowguid;
	/**
	 * senddate便笺发送时间
	 */
	private String senddate;
	/**
	 * title便笺标题
	 */
	private String title;
	/**
	 * sendperson便笺发送人
	 */
	private String sendperson;
	/**
	 * snum便笺排序
	 */
	private String snum;
	
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getFlowguid() {
		return flowguid;
	}
	public void setFlowguid(String flowguid) {
		this.flowguid = flowguid;
	}
	public String getSenddate() {
		return senddate;
	}
	public void setSenddate(String senddate) {
		this.senddate = senddate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSendperson() {
		return sendperson;
	}
	public void setSendperson(String sendperson) {
		this.sendperson = sendperson;
	}
	public String getSnum() {
		return snum;
	}
	public void setSnum(String snum) {
		this.snum = snum;
	}

}
