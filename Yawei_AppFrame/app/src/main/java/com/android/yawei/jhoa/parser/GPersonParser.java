package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.User;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 * 解析组下的成员列表
 * @author wangyx
 * @version 1.0.0 2013-3-22
 */
public class GPersonParser extends DefaultHandler {
	private List<User> dataSet = null;
	private User user = null;
	private String text = null;

	public GPersonParser(List<User> dataSet){
		if (dataSet == null){
			throw new NullPointerException();
		}
		this.dataSet = dataSet;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException{
		text = text + new String(ch, start, length);
	}
	//String totalCount ;
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
		text = "";
		if (localName == null){

		}else if ("root".equals(localName)){
			
		}else if(localName.equals("item")){
			user = null;
			user = new User();
			user.setAdGuid(attributes.getValue("adGuid"));
			user.setDisplayName(attributes.getValue("displayName"));
			user.setPath(attributes.getValue("path"));
			user.setSysflag(attributes.getValue("sysflag"));
			user.setExchangeID(attributes.getValue("exchangeid"));
			user.setFulldisplayName(attributes.getValue("fulldisplayName"));
			user.setUserName(attributes.getValue("accountname"));
			dataSet.add(user);
		}else{
			// do nothing
		}
	}
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		if (text != null && !"".equals(text)){
			text = text.trim();
		}
		if (localName == null){

		}
		text = "";
	}

}

