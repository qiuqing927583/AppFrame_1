package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.RootUserBean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RootUserParser extends DefaultHandler{
	private RootUserBean dataSet = null;
	private String text = null;

	public RootUserParser(RootUserBean dataSet){
		if (dataSet == null){
			throw new NullPointerException();
		}

		this.dataSet = dataSet;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException{
		text = text + new String(ch, start, length);
	}
	String totalCount ;
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) 
			throws SAXException{
		text = "";
		if (localName == null)
		{}
		else if (localName.equals("root"))
		{}
		else
		{}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		if (text != null && !"".equals(text)){
			text = text.trim();
		}
		if (localName == null){

		}else if (localName.equals("root")){
		}else if (localName.equals("userName")){
			dataSet.setUserName(text);
		}else if (localName.equals("displayName")){
			dataSet.setDisplayName(text);
		}else if (localName.equals("path")){
			dataSet.setPath(text);
		}else if(localName.equalsIgnoreCase("depart")){
			dataSet.setDepart(text);
		}else if (localName.equals("adGuid")||localName.equals("userGuid")){
			dataSet.setAdGuid(text);
		}else if (localName.equals("exchangeID")){
			dataSet.setExchangeID(text);
		}else if (localName.equals("webserviceurl")){
			dataSet.setWebserviceurl(text);
		}else if (localName.equals("sysflag")){
			dataSet.setSysflag(text);
		}
		text = "";
	}
}
