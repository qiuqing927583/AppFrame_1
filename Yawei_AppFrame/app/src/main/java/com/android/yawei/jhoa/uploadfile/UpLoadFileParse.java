package com.android.yawei.jhoa.uploadfile;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class UpLoadFileParse extends DefaultHandler{
	
	private UpLoadFileBean data;
	private String text = null;
	
	public UpLoadFileParse(UpLoadFileBean data){
		if (data == null){
			throw new NullPointerException();
		}
		this.data = data;
	}
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException{
		text = text + new String(ch, start, length);
	}
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
		text = "";
		if (localName.equals("item")){

		}
	}
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException {
		if (text != null && !"".equals(text)){
			text = text.trim();
		}

		if (localName.equals("fileguid")){
			data.setFileguid(text);
		}else if(localName.equals("blobguid")){
			data.setBlobguid(text);
		}
		text = "";
	}

}
