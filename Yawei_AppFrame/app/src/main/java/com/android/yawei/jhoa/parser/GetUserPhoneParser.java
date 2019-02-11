package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.GetUserPhoneBean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GetUserPhoneParser extends DefaultHandler{

	private GetUserPhoneBean dataSet = null;
	private String text = null;

	public GetUserPhoneParser(GetUserPhoneBean dataSet) {
		if (dataSet == null) {
			throw new NullPointerException();
		}
		this.dataSet = dataSet;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		text = text + new String(ch, start, length);
	}

	String totalCount;

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		text = "";

		if (localName == null) {

		} else if (localName.equals("root")) {
		} else {
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (text != null && !"".equals(text)) {
			text = text.trim();
		}

		if (localName == null) {

		} else if (localName.equals("displayName")) {
			dataSet.setDisplayName(text);
		} else if (localName.equals("path")) {
			dataSet.setPath(text);
		} else if (localName.equals("depart")) {
			dataSet.setDepart(text);
		}else if (localName.equals("adGuid")) {
			dataSet.setAdGuid(text);
		}else if (localName.equals("exchangeID")) {
			dataSet.setExchangeID(text);
		}else if (localName.equals("sysflag")) {
			dataSet.setSysflag(text);
		}else if (localName.equals("usertel")) {
			dataSet.setUsertel(text);
		}else if(localName.equals("accountname")) {
			dataSet.setAccountname(text);
		}
		text = "";
	}
	
}
