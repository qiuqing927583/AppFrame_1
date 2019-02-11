package com.android.yawei.jhoa.bean;

public class A_DBJ {
	private String guid=null; //某条待办件的唯一标示
	private String title=null; //某条代办事件的标题
	private String receivePerson=null; //接受者的名字
	private String sendPerson=null; //发送者的名字
	private String receivepersonguid=null; //接受者的唯一标示
	private String sendpersonGuid=null; //发送者的唯一标示
	private String sendDate=null; //发送的时间
	private String type=null; //待办事件的类型
	private String fileGUID=null; //附件的唯一标示
	private String flowGUID=null; //流程文件的唯一标示
	private String isnew=null; //是否查看，1是未查看，0是已查看
	private String sign=null; //标志
	private String content=null; //待办事件的内容
	private DataSet<Attachment> attachment=new DataSet<Attachment>();
	private DataSet<FlowOpinion> flowopinion=new DataSet<FlowOpinion>();//流程意见
	private String sendflag=null;//发送人交换号
	private String flowsign=null;//等于1有添加意见
	private String parentguid="";//便笺关联件GUID
	private String sysflag;//服务器标示
	private String istop;//是否是要件
	private String isqianpi;//是否有签批单
	private String qianpiurl;//签批单路径
	private String qianpiguid;//
	public DataSet<FlowOpinion> getFlowopinion() {
		return flowopinion;
	}
	public void setFlowopinion(DataSet<FlowOpinion> flowopinion) {
		this.flowopinion = flowopinion;
	}
	public DataSet<Attachment> getAttachment() {
		return attachment;
	}
	public void setAttachment(DataSet<Attachment> attachment) {
		this.attachment = attachment;
	}
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
	public String getReceivePerson() {
		return receivePerson;
	}
	public void setReceivePerson(String receivePerson) {
		this.receivePerson = receivePerson;
	}
	public String getSendPerson() {
		return sendPerson;
	}
	public void setSendPerson(String sendPerson) {
		this.sendPerson = sendPerson;
	}
	public String getReceivepersonguid() {
		return receivepersonguid;
	}
	public void setReceivepersonguid(String receivepersonguid) {
		this.receivepersonguid = receivepersonguid;
	}
	public String getSendpersonGuid() {
		return sendpersonGuid;
	}
	public void setSendpersonGuid(String sendpersonGuid) {
		this.sendpersonGuid = sendpersonGuid;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFileGUID() {
		return fileGUID;
	}
	public void setFileGUID(String fileGUID) {
		this.fileGUID = fileGUID;
	}
	public String getFlowGUID() {
		return flowGUID;
	}
	public void setFlowGUID(String flowGUID) {
		this.flowGUID = flowGUID;
	}
	public String getIsnew() {
		return isnew;
	}
	public void setIsnew(String isnew) {
		this.isnew = isnew;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSendflag() {
		return sendflag;
	}
	public void setSendflag(String sendflag) {
		this.sendflag = sendflag;
	}
	public String getFlowsign() {
		return flowsign;
	}
	public void setFlowsign(String flowsign) {
		this.flowsign = flowsign;
	}
	
	public String getParentguid() {
		return parentguid;
	}
	public void setParentguid(String parentguid) {
		this.parentguid = parentguid;
	}
	public String getSysflag() {
		return sysflag;
	}
	public void setSysflag(String sysflag) {
		this.sysflag = sysflag;
	}
	public String getIstop() {
		return istop;
	}
	public void setIstop(String istop) {
		this.istop = istop;
	}

	public String getIsqianpi() {
		return isqianpi;
	}

	public void setIsqianpi(String isqianpi) {
		this.isqianpi = isqianpi;
	}

	public String getQianpiurl() {
		return qianpiurl;
	}

	public void setQianpiurl(String qianpiurl) {
		this.qianpiurl = qianpiurl;
	}

	public String getQianpiguid() {
		return qianpiguid;
	}

	public void setQianpiguid(String qianpiguid) {
		this.qianpiguid = qianpiguid;
	}
}
