package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.UserCheckCode;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class UserCheckCodeParser extends DefaultHandler {
	private UserCheckCode userCheckCode = null;
	private String text = null;

	public UserCheckCodeParser(UserCheckCode dataSet){
		if (dataSet == null){
			throw new NullPointerException();
		}

		this.userCheckCode = dataSet;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException{
		text = text + new String(ch, start, length);
	}
	String totalCount ;
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
		text = "";

		if (localName == null){

		}else if (localName.equals("root")){
		}
		else{
			// do nothing
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		if (text != null && !"".equals(text)){
			text = text.trim();
		}

		if (localName == null){

		}else if (localName.equals("root")){
		}else if (localName.equals("deviceimei")){
			userCheckCode.setDeviceimei(text);
		}else if (localName.equals("devicename")){
			userCheckCode.setDevicename(text);
		}else if (localName.equals("devicesn")){
			userCheckCode.setDevicesn(text);
		}else if(localName.equalsIgnoreCase("devicemeid")){
			userCheckCode.setDevicemeid(text);
		}else if (localName.equals("registercode")){
			userCheckCode.setRegistercode(text);
		}else if (localName.equals("signflag")){
			userCheckCode.setSignflag(text);
		}
	
		text = "";
	}

}

