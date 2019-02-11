package com.android.yawei.jhoa.bean;

/**
 * 草稿已发查看页
 */
public class InboxDetail {
	private String guid;//guid便笺唯一标示
	private String title;//title便笺标题
	private String zsorg;//zsorg便笺收件人
	private String zsorgadguid;//zsorgadguid便笺收件人guid
	private String dispatchtime;//dispatchtime发送时间
	private String content;//content便笺内容
	private String status;//status便笺状态
	private String dispatchperson;//dispatchperson 发件人
	private String dispatchpersonguid;//dispatchpersonguid 发件人GUID
	private DataSet<Attachment> attachment = null;
	private String sysflag;//收件人新老系统标示
	private String serverflag;// 收件人服务器标示
	private String isrollback ;//查回执
	private String sendflag;//发件人服务器标示
	private String sendsysflag;// 发件人新老系统标示
	private String parentguid;
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getZsorg() {
		return zsorg;
	}
	public void setZsorg(String zsorg) {
		this.zsorg = zsorg;
	}
	public String getZsorgadguid() {
		return zsorgadguid;
	}
	public void setZsorgadguid(String zsorgadguid) {
		this.zsorgadguid = zsorgadguid;
	}
	public String getDispatchtime() {
		return dispatchtime;
	}
	public void setDispatchtime(String dispatchtime) {
		this.dispatchtime = dispatchtime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDispatchperson() {
		return dispatchperson;
	}
	public void setDispatchperson(String dispatchperson) {
		this.dispatchperson = dispatchperson;
	}
	public String getDispatchpersonguid() {
		return dispatchpersonguid;
	}
	public void setDispatchpersonguid(String dispatchpersonguid) {
		this.dispatchpersonguid = dispatchpersonguid;
	}
	public DataSet<Attachment> getAttachment() {
		return attachment;
	}
	public void setAttachment(DataSet<Attachment> attachment) {
		this.attachment = attachment;
	}
	public String getSysflag() {
		return sysflag;
	}
	public void setSysflag(String sysflag) {
		this.sysflag = sysflag;
	}
	public String getServerflag() {
		return serverflag;
	}
	public void setServerflag(String serverflag) {
		this.serverflag = serverflag;
	}
	public String getIsrollback() {
		return isrollback;
	}
	public void setIsrollback(String isrollback) {
		this.isrollback = isrollback;
	}
	public String getSendflag() {
		return sendflag;
	}
	public void setSendflag(String sendflag) {
		this.sendflag = sendflag;
	}
	public String getSendsysflag() {
		return sendsysflag;
	}
	public void setSendsysflag(String sendsysflag) {
		this.sendsysflag = sendsysflag;
	}
	public String getParentguid() {
		return parentguid;
	}
	public void setParentguid(String parentguid) {
		this.parentguid = parentguid;
	}
 }
