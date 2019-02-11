package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.A_DBJ;
import com.android.yawei.jhoa.bean.Attachment;
import com.android.yawei.jhoa.bean.DataSet;
import com.android.yawei.jhoa.bean.FlowOpinion;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class A_DBJ_Parse extends DefaultHandler {
	private List<A_DBJ> dataSet = null;
	private DataSet<Attachment> dataSetAttachment = null;
	private DataSet<FlowOpinion> dataSetFlowOpinion=null;
	private A_DBJ a_DBJ = null;
	private Attachment attachment=null;
	private FlowOpinion flowOpinion=null;
	private String text = null;

	public A_DBJ_Parse(List<A_DBJ> dataSet){
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

		}

		else if (localName.equals("root")){
			a_DBJ = new A_DBJ();
		}else if (localName.equals("AttachmentList")){
			dataSetAttachment=new DataSet<Attachment>();
		}else if (localName.equals("Attachment")){
		     attachment=new Attachment();
		     attachment.setAttGuidString(attributes.getValue("guid"));
		     attachment.setAttFileNameString(attributes.getValue("fileName"));
		     attachment.setAttFileLengthString(attributes.getValue("fileLength"));
		     attachment.setAttFileExt(attributes.getValue("fileExt"));
		     attachment.setAttUrl(attributes.getValue("url"));
		     attachment.setAttUrlOffice(attributes.getValue("urloffice"));
		     attachment.setImageCount(attributes.getValue("imgcount"));
		     attachment.setImageUrl(attributes.getValue("imgurl"));
			 attachment.setIspreview(attributes.getValue("ispreview"));
		     dataSetAttachment.add(attachment);
		     a_DBJ.setAttachment(dataSetAttachment);
	
		}else if (localName.equals("FlowList")) {
			dataSetFlowOpinion=new DataSet<FlowOpinion>();
		}else if(localName.equals("Flow")){
			flowOpinion=new FlowOpinion();
			flowOpinion.setGuid(attributes.getValue("guid"));
			flowOpinion.setEmployeeName(attributes.getValue("employeeName"));
			flowOpinion.setEndTime(attributes.getValue("endTime"));
			flowOpinion.setContent(attributes.getValue("content"));
			dataSetFlowOpinion.add(flowOpinion);
			a_DBJ.setFlowopinion(dataSetFlowOpinion);
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

		}else if (localName.equals("root")){
			dataSet.add(a_DBJ);
		}else if (localName.equals("guid")){ 
			a_DBJ.setGuid(text);
		}else if (localName.equals("title")){
			a_DBJ.setTitle(text);
		}else if (localName.equals("receivePerson")){
			a_DBJ.setReceivePerson(text);
		}else if (localName.equals("sendPerson")){
			a_DBJ.setSendPerson(text);
		}else if (localName.equals("receivepersonguid")){
			a_DBJ.setReceivepersonguid(text);
		}else if (localName.equals("sendpersonGuid")){
			a_DBJ.setSendpersonGuid(text);
		}else if (localName.equals("sendDate")){
			a_DBJ.setSendDate(text);
		}else if (localName.equals("type")){
			a_DBJ.setType(text);
		}else if (localName.equals("fileGUID")){
			a_DBJ.setFileGUID(text);
		}else if (localName.equals("flowGUID")){
			a_DBJ.setFlowGUID(text);
		}else if (localName.equals("isnew")){
			a_DBJ.setIsnew(text);
		}else if (localName.equals("sign")){
			a_DBJ.setSign(text);
		}else if (localName.equals("content")){
			a_DBJ.setContent(text);
		}else if (localName.equals("sendflag")){
			a_DBJ.setSendflag(text);
		}else if(localName.equals("flowsign")){
			a_DBJ.setFlowsign(text);
		}else if(localName.equals("parentguid")){
			a_DBJ.setParentguid(text);
		}else if(localName.equals("sysflag")){
			a_DBJ.setSysflag(text);
		}else if(localName.equals("istop")){
			a_DBJ.setIstop(text);
		}else if(localName.equals("isqianpi")){
			a_DBJ.setIsqianpi(text);
		}else if(localName.equals("qianpiurl")){
			a_DBJ.setQianpiurl(text);
		}else if(localName.equals("qianpiguid")){
			a_DBJ.setQianpiguid(text);
		}
		text = "";
	}
}

