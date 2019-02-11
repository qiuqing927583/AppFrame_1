package com.android.yawei.jhoa.bean;

import java.io.Serializable;

/**
 * 流程意见bean
 */
@SuppressWarnings("serial")
public class FlowOpinion implements Serializable{
	
	private String guid;
	private String employeeName;
	private String endTime;
	private String content;
	
	
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
