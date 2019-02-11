package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.GroupBean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 * 解析组列表
 * @author wangyx
 * @version 1.0.0 2013-3-22
 */
public class GroupParser extends DefaultHandler {
	private List<GroupBean> dataSet = null;
	private GroupBean group = null;
	private String text = null;

	public GroupParser(List<GroupBean> dataSet){
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
			group = null;
			group = new GroupBean();
			group.setName(attributes.getValue(0));
			group.setCommonName(attributes.getValue(1));
			group.setGuid(attributes.getValue(2));
			group.setType(attributes.getValue(3));
			dataSet.add(group);
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

