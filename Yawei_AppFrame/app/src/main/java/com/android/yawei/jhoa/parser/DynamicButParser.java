package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.DynamicButBean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class DynamicButParser extends DefaultHandler{

	private List<DynamicButBean> dataSet = null;
	private DynamicButBean data = null;
	private String text = null;

	public DynamicButParser(List<DynamicButBean> dataSet){
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
			data = new DynamicButBean();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (text != null && !"".equals(text)){
			text = text.trim();
		}

		if (localName == null){

		}else if(localName.equals("id")){
			data.setId(text);
		}else if(localName.equals("name")){
			data.setName(text);
		}else if(localName.equals("iconurl")){
			data.setIconurl(text);
		}else if(localName.equals("clickname")){
			data.setClickname(text);
		}else if(localName.equals("item")){
			dataSet.add(data);
		}
		text = "";
	}

	@Override
	public void characters(char[] ch, int start, int length)throws SAXException {
		text = text + new String(ch, start, length);
	}
}
