package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.DBJ;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

//解析邮件列表
public class EmailListParser extends DefaultHandler{
	
	private List<DBJ> dataSet = null;
	private DBJ dbj = null;
	private String text = null;

	public EmailListParser(List<DBJ> dataSet){
		if (dataSet == null){
			throw new NullPointerException();
		}
		this.dataSet = dataSet;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException{
		text = text + new String(ch, start, length);
	}
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
		text = "";
		if (localName == null){}
		else if (localName.equals("root")){}
		else if (localName.equals("item")){
			dbj = new DBJ();
			dbj.setGuid(attributes.getValue("guid"));
			dbj.setFlowguid(attributes.getValue("flowguid"));
			dbj.setSenddate(attributes.getValue("senddate"));
			dbj.setTitle(attributes.getValue("title"));
			dbj.setReceivepersonguid(attributes.getValue("receivepersonguid"));
			dbj.setSendperson(attributes.getValue("sendperson"));
			dbj.setIsnew(attributes.getValue("isnew"));
			dbj.setSnum(attributes.getValue("snum"));
			dbj.setType(attributes.getValue("type"));
			dbj.setFileguid(attributes.getValue("fileguid"));
			dbj.setIsreply(attributes.getValue("isreply"));
			dbj.setIsout(attributes.getValue("isout"));
            dbj.setIstop(attributes.getValue("istop"));
            dbj.setOutsysurl(attributes.getValue("outsysserviceurl"));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		if (text != null && !"".equals(text)){
			text = text.trim();
		}
		if (localName == null){

		}else if(localName.equals("ionicparam")){
			dbj.setIonicparam(text);
		}else if(localName.equals("isqianpi")){
			dbj.setIsqianpi(text);
		}else if(localName.equals("ionicurl")){
			dbj.setIonicurl(text);
		}else if (localName.equals("item")){
			dataSet.add(dbj);
		}
		text = "";
	}
}
