package com.android.yawei.jhoa.bean;

public class ProcessBean {

	private String moduletype;
	private String flowid;
	private String modulename;
	private String flowname;
	private String sendtype;
	private String handletype;
	private String visitsign;
	
	public void Setmoduletype(String moduletype){
		this.moduletype = moduletype;
	}
	public String Getmoduletype(){
		return moduletype;
	}
	
	public void Setflowid(String flowid){
		this.flowid = flowid;
	}
	public String Getflowid(){
		return flowid;
	}
	
	public void Setmodulename(String modulename){
		this.modulename = modulename;
	}
	public String Getmodulename(){
		return modulename;
	}
	
	public void Setflowname(String flowname){
		this.flowname = flowname;
	}
	public String Getflowname(){
		return flowname;
	}
	
	public void Setsendtype(String sendtype){
		this.sendtype = sendtype;
	}
	public String Getsendtype(){
		return sendtype;
	}
	
	public void Sethandletype(String handletype){
		this.handletype = handletype;
	}
	public String Gethandletype(){
		return handletype;
	}
	public void Setvisitsign(String visitsign){
		this.visitsign = visitsign;
	}
	public String Getvisitsign(){
		return visitsign;
	}
}
