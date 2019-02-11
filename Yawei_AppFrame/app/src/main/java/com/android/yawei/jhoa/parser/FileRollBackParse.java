package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.FileRollBack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class FileRollBackParse extends DefaultHandler {
	private List<FileRollBack> dataSet = null;
	private FileRollBack FileRollBack = null;
	private String text = null;

	public FileRollBackParse(List<FileRollBack> dataSet){
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

		if (localName == null){

		}else if(localName.equals("item")){
			FileRollBack = null;
			FileRollBack = new FileRollBack();
			FileRollBack.setUsername(attributes.getValue("username"));
			FileRollBack.setStatus(attributes.getValue("status"));
			FileRollBack.setReceivetime(attributes.getValue("receivetime"));
			FileRollBack.setViewtime(attributes.getValue("viewtime"));
			dataSet.add(FileRollBack);
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

