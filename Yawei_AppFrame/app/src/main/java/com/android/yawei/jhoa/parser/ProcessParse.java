package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.ProcessBean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class ProcessParse extends DefaultHandler{
	private List<ProcessBean> dataSet = null;
	private ProcessBean processMsg = null;
	private String text = null;
	public ProcessParse( List<ProcessBean> dataSet){
		
		if (dataSet == null){
			throw new NullPointerException();
		}

		this.dataSet = dataSet;
	}
	

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException{
		text = text + new String(ch, start, length);
	}
	String totalCount ;
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
		text = "";
		try {
			if (localName == null){
	
			}
			else if (localName.equals("item")){
				processMsg = new ProcessBean();
				processMsg.Setmoduletype(attributes.getValue("moduletype"));
				processMsg.Setflowid(attributes.getValue("flowid"));
				processMsg.Setmodulename(attributes.getValue("modulename"));
				processMsg.Setflowname(attributes.getValue("flowname"));
				processMsg.Setsendtype(attributes.getValue("sendtype"));
				processMsg.Sethandletype(attributes.getValue("handletype"));
				processMsg.Setvisitsign(attributes.getValue("visitsign"));
				dataSet.add(processMsg);
			}
			else{
				// do nothing
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		if (text != null && !"".equals(text)){
			text = text.trim();
		}

		if (localName == null){

		}
		
//		else if (localName.equals("status")){
//		}
//		else if (localName.equals("id")){
//		}
//		else if (localName.equals("name")){
//		}
//		else if(localName.equalsIgnoreCase("flag")){
//		}
//		else if (localName.equals("exchangeid")){
//		}
//		else if(localName.equals("passwordstrong")){
//		}
//		else if (localName.equals("vpnip")){
//		}
//		else if(localName.equals("oaurl")){
//		}
//		else if(localName.equals("passwordurl")){
//		}
//		else if(localName.equals("logontime")){
//		}
//		else if (localName.equals("root")){
//		}
//		text = "";
	}
}
