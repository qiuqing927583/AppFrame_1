package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.DataSet;
import com.android.yawei.jhoa.bean.UserLog;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LogParser extends DefaultHandler {
	private DataSet<UserLog> dataSet = null;
	private UserLog log = null;
	private String text = null;

	public LogParser(DataSet<UserLog> dataSet)
	{
		if (dataSet == null)
		{
			throw new NullPointerException();
		}

		this.dataSet = dataSet;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		text = text + new String(ch, start, length);
	}
	//String totalCount ;
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		text = "";

		if (localName == null){

		}else if ("root".equals(localName)) {
			
		}else if(localName.equals("item")){
			log = null;
			log = new UserLog();

		}else{
			// do nothing
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (text != null && !"".equals(text)){
			text = text.trim();
		}

		if (localName == null){

		}else if(localName.equals("mobilebrand")){
			log.setClientip(text);
		}else if(localName.equals("logontime")){
			log.setLogontime(text);
		}else if(localName.equals("item")){
			dataSet.add(log);
		}
		text = "";
	}


}

