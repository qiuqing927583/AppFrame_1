package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.RootUserBean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class SwitchUserParse extends DefaultHandler{
	private List<RootUserBean> dataSet = null;
	private RootUserBean data = null;
	private String text = null;

	public SwitchUserParse(List<RootUserBean> dataSet)
	{
		if (dataSet == null)
		{
			throw new NullPointerException();
		}

		this.dataSet = dataSet;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException{
		text = text + new String(ch, start, length);
	}
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		text = "";
		if (localName == null){

		}
		else if (localName.equals("root")){
			
		}
		else if (localName.equals("item")){
			data = new RootUserBean();
		}
	}
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if (text != null && !"".equals(text)){
			text = text.trim();
		}

		if (localName.equals("userName")) {
			data.setUserName(text);
		}else if (localName.equals("displayName")) {
			data.setDisplayName(text);
		}else if(localName.equals("path")){
			data.setPath(text);
		}else if (localName.equals("depart")) {
			data.setPath(text);
		}else if (localName.equals("adGuid")) {
			data.setAdGuid(text);
		}else if (localName.equals("exchangeID")) {
			data.setExchangeID(text);
		}else if (localName.equals("sysflag")) {
			data.setSysflag(text);
		}else if (localName.equals("webserviceurl")) {
			data.setWebserviceurl(text);
		}else if(localName.equals("isCompanyUser")){
//			if(text != null &&(text.equals("0")||text.equals("2"))){
//				data.setIsshow(true);
//			}else{
//				data.setIsshow(false);
//			}
		}else if(localName.equals("item")){
			dataSet.add(data);
		}
	}
}
