package com.android.yawei.jhoa.bean;

/**
 * 选附件上边不同格式文件
 */
public class SreachSDFileBean {

	private int number;//加入序列号  
	private String bookName;//加入名称  
	private String path;// 加入路径  
	private long size;// 加入文件大小 
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	
	
}
