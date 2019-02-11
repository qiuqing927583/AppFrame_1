package com.android.yawei.jhoa.bean;

/************
 * 轻应用，头部动态button
 * @author Yusz
 *
 */
public class DynamicButBean {
	
	private String id;//
	private String name;//button 名称
	private String iconurl;//button 图标的网络路径
//	private String clicktype;//点击类型0标示Android调用js方法；1标示js调用Android方法
	private String clickname;//如果是Android调用js方法，该字段是标示方法名称
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIconurl() {
		return iconurl;
	}
	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}
	public String getClickname() {
		return clickname;
	}
	public void setClickname(String clickname) {
		this.clickname = clickname;
	}

	
}
