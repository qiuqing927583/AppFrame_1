package com.android.yawei.jhoa.bean;

public class RecvMessage {
	private String conent;
	private String userName;
	private String msgTime;
	private String guid;
	private boolean isRootUser;
	private String msg_size;
	private String title;
	private String status;

	public void SetConent(String conent){
		this.conent = conent;
	}
	public void SetUserName(String userName){
		this.userName = userName;
	}
	public void SetMsgTime(String msgTime){
		this.msgTime = msgTime;
	}
	public void SetIsRootUset(boolean isRootUser){
		this.isRootUser = isRootUser;
	}
	public void SetRecvUserGuid(String guid){
		this.guid = guid;
	}
	public void SetMsgSize(String msg_size){
		this.msg_size = msg_size;
	}
	public void SetTitle(String title){
		this.title = title;
	}
	public void SetStatus(String status){
		this.status = status;
	}
	public String GetStatus(){
		return status;
	}
	public String GetConent(){
		return conent;
	}
	public String GetUserName(){
		return userName;
	}
	public String GetMsgTime(){
		return msgTime;
	}
	public String GetRecvUserGuid(){
		return guid;
	}
	public boolean GetIsRootUset(){
		return isRootUser;
	}
	public String GetMsgSize(){
		return msg_size;
	}
	public String GetTitle(){
		return title;
	}
	
}
