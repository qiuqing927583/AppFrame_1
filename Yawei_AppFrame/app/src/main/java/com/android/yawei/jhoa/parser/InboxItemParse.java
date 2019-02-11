package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.InboxItem;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class InboxItemParse extends DefaultHandler {
	private List<InboxItem> dataSet = null;
	private InboxItem inboxItem = null;
	private String text = null;

	public InboxItemParse(List<InboxItem> dataSet){
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
			
		}else if (localName.equals("item")){
			inboxItem = new InboxItem();
			inboxItem.setGuid(attributes.getValue("guid"));
			inboxItem.setFlowguid(attributes.getValue("flowguid"));
			inboxItem.setSenddate(attributes.getValue("senddate"));
			inboxItem.setTitle(attributes.getValue("title"));
			inboxItem.setSendperson(attributes.getValue("sendperson"));
			inboxItem.setSnum(attributes.getValue("snum"));
			dataSet.add(inboxItem);
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

		}
		text = "";
	}

	@Override
	public void characters(char[] ch, int start, int length)throws SAXException {
		text = text + new String(ch, start, length);
	}

}
