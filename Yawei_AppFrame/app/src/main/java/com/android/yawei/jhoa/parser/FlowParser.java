package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.FlowOpinion;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class FlowParser extends DefaultHandler {
	private List<FlowOpinion> dataSet = null;
	private String text = null;
	private FlowOpinion flowOpinion;

	public FlowParser(List<FlowOpinion> dataSet) {
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
			flowOpinion = new FlowOpinion();
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

		} else if (localName.equals("root")) {
			dataSet.add(flowOpinion);
		} else if (localName.equals("guid")) {
			flowOpinion.setGuid(text);
		} else if (localName.equals("content")) {
			flowOpinion.setContent(text);
		}

		text = "";
	}

}
