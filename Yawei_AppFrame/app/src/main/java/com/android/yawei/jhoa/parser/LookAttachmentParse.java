package com.android.yawei.jhoa.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.android.yawei.jhoa.bean.Attachment;
import com.android.yawei.jhoa.bean.DataSet;

import java.util.List;

public class LookAttachmentParse extends DefaultHandler{
	private List<Attachment> dataSetAttachment = null;
	private Attachment attachment=null;
	private String text = null;

	public LookAttachmentParse(List<Attachment> dataSet){
		if (dataSet == null){
			throw new NullPointerException();
		}

		this.dataSetAttachment = dataSet;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException{
		text = text + new String(ch, start, length);
	}
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
		text = "";
		if (localName.equals("Attachment")){
			//附件
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
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		
	}

}
