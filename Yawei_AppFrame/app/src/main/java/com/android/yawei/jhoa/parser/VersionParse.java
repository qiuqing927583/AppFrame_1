package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.ClientVersionInfo;
import com.android.yawei.jhoa.bean.DataSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/***************************
 * 解析版本信息
 * @author Yusz
 *
 */
public class VersionParse extends DefaultHandler {
	private List<ClientVersionInfo> dataSet = null;
	private ClientVersionInfo clientVersionInfo = null;
	private String text = null;

	public VersionParse(List<ClientVersionInfo> dataSet){
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

		}else if(localName.equals("root")){
			clientVersionInfo = new ClientVersionInfo();
		}else if(localName.equals("item")){
			clientVersionInfo = null;
			clientVersionInfo = new ClientVersionInfo();
			clientVersionInfo.setName(attributes.getValue("name")) ;
			clientVersionInfo.setVersion(attributes.getValue("version"));
            clientVersionInfo.setLength(attributes.getValue("filelength"));
			clientVersionInfo.setUrl(attributes.getValue("url"));
			clientVersionInfo.setDebugversion(attributes.getValue("debugversion"));
            clientVersionInfo.setDebuglength(attributes.getValue("debugfilelength"));
			clientVersionInfo.setDebugurl(attributes.getValue("debugurl"));
			clientVersionInfo.setUpmessage(attributes.getValue("upinfomation"));
			clientVersionInfo.setWwwversion(attributes.getValue("wwwversion"));
			clientVersionInfo.setWwwurl(attributes.getValue("wwwurl"));
			clientVersionInfo.setWwwfilelength(attributes.getValue("wwwfilelength"));
			dataSet.add(clientVersionInfo);
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

		}else if (localName.equals("name")){
			clientVersionInfo.setName(text) ;
		}else if (localName.equals("version")){
			clientVersionInfo.setVersion(text);
		}else if (localName.equals("filelength")){
			clientVersionInfo.setLength(text);
		}else if (localName.equals("url")){
			clientVersionInfo.setUrl(text);
		}else if (localName.equals("upinfomation")){
			clientVersionInfo.setUpmessage(text);
		}else if (localName.equals("wwwversion")){
			clientVersionInfo.setWwwversion(text);
		}else if (localName.equals("wwwurl")){
			clientVersionInfo.setWwwurl(text);
		}else if (localName.equals("wwwfilelength")){
			clientVersionInfo.setWwwfilelength(text);
		}else if(localName.equals("items")){
			dataSet.add(clientVersionInfo);
		}
		
		text = "";
	}

}

