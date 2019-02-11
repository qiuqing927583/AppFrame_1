package com.android.yawei.jhoa.bean;

import java.io.Serializable;

/**
 * 待办已办列表
 */

@SuppressWarnings("serial")
public class DBJ implements Serializable{

	private String guid;//邮件的唯一标识(guid)
	private String flowguid;//是否是流程
	private String senddate;//发送时间
	private String title;//邮件标题
	private String receivepersonguid;//收件人唯一标示
	private String sendperson;//发件人
	private String isnew;//是否已经读过(1未读0已读)
	private String snum;//自增列
	private String fileguid;//便签唯一标示
	private String type;//邮件类型(290便签件；10收文；20发文；40地方信息；60会议)
	private String isreply;//是否是回复件(0不是；1是)
	private String isout;//是否是外链件（0不是，1是）是提示外联件打不开
	private String istop;//是否是急要件(0不是1是)
	private String sign;//0待办，1已办
	private String outsysserviceurl;//跨系统，如果该字段不为空，说明是跨系统的流程件
	private String isqianpi;//是否有签批单，0不是签批单1只是有可能含有签批单进行签批
	private String ionicparam;//是否要往ionic上跳转如果为null或者是“”不跳，调到ionic前提没有签批单
	private String ionicurl;//ionic html 本地存放路径
	
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
	public String getReceivepersonguid() {
		return receivepersonguid;
	}
	public void setReceivepersonguid(String receivepersonguid) {
		this.receivepersonguid = receivepersonguid;
	}
	public String getSendperson() {
		return sendperson;
	}
	public void setSendperson(String sendperson) {
		this.sendperson = sendperson;
	}
	public String getIsnew() {
		return isnew;
	}
	public void setIsnew(String isnew) {
		this.isnew = isnew;
	}
	public String getSnum() {
		return snum;
	}
	public void setSnum(String snum) {
		this.snum = snum;
	}
	public String getFileguid() {
		return fileguid;
	}
	public void setFileguid(String fileguid) {
		this.fileguid = fileguid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsreply() {
		return isreply;
	}
	public void setIsreply(String isreply) {
		this.isreply = isreply;
	}
	public String getIsout() {
		return isout;
	}
	public void setIsout(String isout) {
		this.isout = isout;
	}
	public String getIstop() {
		return istop;
	}
	public void setIstop(String istop) {
		this.istop = istop;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getOutsysurl() {
		return outsysserviceurl;
	}
	public void setOutsysurl(String outsysserviceurl) {
		this.outsysserviceurl = outsysserviceurl;
	}

	public String getIsqianpi() {
		return isqianpi;
	}

	public void setIsqianpi(String isqianpi) {
		this.isqianpi = isqianpi;
	}

	public String getIonicparam() {
		return ionicparam;
	}

	public void setIonicparam(String ionicparam) {
		this.ionicparam = ionicparam;
	}

	public String getIonicurl() {
		return ionicurl;
	}

	public void setIonicurl(String ionicurl) {
		this.ionicurl = ionicurl;
	}
}
