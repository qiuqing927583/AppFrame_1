package com.android.yawei.jhoa.bean;

import java.io.Serializable;

//附件的信息
public class Attachment  implements Serializable {
	
	private String attUrl=null;//待办事件的链接 ,或者//附件的本地路径
	private String  attFileNameString=null; //待办事件的文件名称，或者添加本地附件名称
	private String attGuidString=null; //待办事件附件的唯一标示
	private String AttachmentList=null; //待办事件的信息
	private String attFileExt=null;//待办事件的文件的后缀名
	private String attFileLengthString=null; //待办事件的文件大小
	private String attUrlOffice=null;//webofficeUrl
	private String imageCount=null;//图片数
	private String imageUrl=null;//图片连接url
	private String fileSize = null;//文件大小
	private String ispreview;//是否可以在线预览
	public String getAttUrl() {
		return attUrl;
	}
	public void setAttUrl(String attUrl) {
		this.attUrl = attUrl;
	}
	public String getAttFileNameString() {
		return attFileNameString;
	}
	public void setAttFileNameString(String attFileNameString) {
		this.attFileNameString = attFileNameString;
	}
	public String getAttGuidString() {
		return attGuidString;
	}
	public void setAttGuidString(String attGuidString) {
		this.attGuidString = attGuidString;
	}
	public String getAttachmentList() {
		return AttachmentList;
	}
	public void setAttachmentList(String attachmentList) {
		AttachmentList = attachmentList;
	}
	public String getAttFileExt() {
		return attFileExt;
	}
	public void setAttFileExt(String attFileExt) {
		this.attFileExt = attFileExt;
	}
	public String getAttFileLengthString() {
		return attFileLengthString;
	}
	public void setAttFileLengthString(String attFileLengthString) {

		this.attFileLengthString = attFileLengthString;

	}
	public String getAttUrlOffice() {
		return attUrlOffice;
	}
	public void setAttUrlOffice(String attUrlOffice) {
		this.attUrlOffice = attUrlOffice;
	}
	public String getImageCount() {
		return imageCount;
	}
	public void setImageCount(String imageCount) {
		this.imageCount = imageCount;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getIspreview() {
		return ispreview;
	}
	public void setIspreview(String ispreview) {
		this.ispreview = ispreview;
	}
}
