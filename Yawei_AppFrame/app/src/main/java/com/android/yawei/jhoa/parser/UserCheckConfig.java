package com.android.yawei.jhoa.parser;

import com.android.yawei.jhoa.bean.UserStatusBean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/****************
 * 2015-9-9
 * @author Yusz
 * 登录用户状态
 *
 */
public class UserCheckConfig extends DefaultHandler{

	private String text = null;
	private UserStatusBean dataSet;
	
	public UserCheckConfig(UserStatusBean dataSet){
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

		if (localName.equals("root")){
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException{
		
		if (text != null && !"".equals(text)){
			text = text.trim();
		}

		if (localName.equals("status")){
			dataSet.setStatus(text);
		}else if (localName.equals("accountname")){ 
			dataSet.setAccountname(text);
		}else if (localName.equals("userguid")|| localName.equals("id")){
			dataSet.setUserguid(text);
		}else if (localName.equals("exchangeid")){
			dataSet.setExchangeid(text);
		}else if (localName.equals("vpnip")){
			dataSet.setVpnip(text);
		}else if (localName.equals("personip")){
			dataSet.setOaurl(text);
		}else if (localName.equals("name")){
			dataSet.setName(text);
		}else if (localName.equals("flag")){
			dataSet.setFlag(text);
		}else if (localName.equals("depart")){
			dataSet.setDepart(text);
		}else if (localName.equals("oaurl")||localName.equals("webserviceurl")){
			dataSet.setOaurl(text);
		}else if (localName.equals("homeurl")){
			dataSet.setHomeurl(text);
		}else if (localName.equals("errorflag")){
			dataSet.setErrorflag(text);
		}
		text = "";
	}
	
}
