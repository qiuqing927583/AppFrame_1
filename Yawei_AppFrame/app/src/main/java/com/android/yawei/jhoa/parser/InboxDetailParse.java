package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.Attachment;
import com.android.yawei.jhoa.bean.DataSet;
import com.android.yawei.jhoa.bean.InboxDetail;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class InboxDetailParse extends DefaultHandler {
	private List<InboxDetail> dataSet = null;
	private InboxDetail inboxDeail = null;
	private String text = null;
	private DataSet<Attachment> dataSetAttachment = null;
	private Attachment attachment=null;
	public InboxDetailParse(List<InboxDetail> dataSet){
		if (dataSet == null){
			throw new NullPointerException();
		}

		this.dataSet = dataSet;
	}


	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		text = "";

		if (localName == null){

		}else if (localName.equals("root")){
			inboxDeail = new InboxDetail();
		}else if (localName.equals("AttachmentList")){
			dataSetAttachment=new DataSet<Attachment>();
		}else if (localName.equals("Attachment")){
			attachment=new Attachment();
			attachment.setAttGuidString(attributes.getValue("guid"));
			attachment.setAttFileNameString(attributes.getValue("fileName"));
			attachment.setAttFileLengthString(attributes.getValue("fileLength"));
			attachment.setAttFileExt(attributes.getValue("fileExt"));
			attachment.setAttUrl(attributes.getValue("url"));
			attachment.setAttUrlOffice(attributes.getValue("urloffice"));
			attachment.setImageCount(attributes.getValue("imgcount"));
			attachment.setImageUrl(attributes.getValue("imgurl"));
			attachment.setIspreview(attributes.getValue("ispreview"));
			dataSetAttachment.add(attachment);
			inboxDeail.setAttachment(dataSetAttachment);
		}else{
			// do nothing
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
		if (text != null && !"".equals(text)){
			text = text.trim();
		}

		if (localName == null){

		}else if (localName.equals("root")){
			dataSet.add(inboxDeail);
		}else if (localName.toLowerCase().equals("guid")){
			inboxDeail.setGuid(text);
		}else if (localName.toLowerCase().equals("title")){
			inboxDeail.setTitle(text);
		}else if (localName.toLowerCase().equals("zsorg")){
			inboxDeail.setZsorg(text);
		}else if (localName.toLowerCase().equals("zsorgadguid")){
			inboxDeail.setZsorgadguid(text);
		}else if (localName.toLowerCase().equals("dispatchtime")){
			inboxDeail.setDispatchtime(text);
		}else if (localName.toLowerCase().equals("content")){
			inboxDeail.setContent(text);
		}else if (localName.toLowerCase().equals("status")){
			inboxDeail.setStatus(text);
		}else if (localName.toLowerCase().equals("dispatchperson")){
			inboxDeail.setDispatchperson(text);
		}else if (localName.toLowerCase().equals("dispatchpersonguid")){
			inboxDeail.setDispatchpersonguid(text);
		}else if (localName.toLowerCase().equals("sysflag")){
			inboxDeail.setSysflag(text);
		}else if (localName.toLowerCase().equals("serverflag")){
			inboxDeail.setServerflag(text);
		}else if(localName.toLowerCase().equals("isrollback")){
			inboxDeail.setIsrollback(text);
		}else if(localName.toLowerCase().equals("sendflag")){
			inboxDeail.setSendflag(text);
		}else if(localName.toLowerCase().equals("sendsysflag")){
			inboxDeail.setSendsysflag(text);
		}else if(localName.toLowerCase().equals("parentguid")){
			inboxDeail.setParentguid(text);
		}
		text = "";
	}

	@Override
	public void characters(char[] ch, int start, int length)throws SAXException {
		text = text + new String(ch, start, length);
	}

}
