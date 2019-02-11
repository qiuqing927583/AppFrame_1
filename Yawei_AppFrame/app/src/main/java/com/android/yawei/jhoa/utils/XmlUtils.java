package com.android.yawei.jhoa.utils;





import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;


import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;


import java.io.StringReader;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Xml工具
 * 
 * @author sn
 * 
 */
public final class XmlUtils {

	private XmlUtils()
	{
	}

	public static void saxParse(String xml, DefaultHandler handler) throws Exception
	{
		if (xml == null || handler == null)
		{
			throw new NullPointerException();
		}
		// 构建SAXParser
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		// 构建InputSource
		InputSource is = new InputSource(new StringReader(xml));
		// 调用parse()方法
		parser.parse(is, handler);
	}
//	public static JSONObject xml2JSON(byte[] xml) throws JDOMException, IOException {
//		JSONObject json = new JSONObject();
//		InputStream is = new ByteArrayInputStream(xml);
//		SAXBuilder sb = new SAXBuilder();
//		org.jdom2.Document doc = sb.build(is);
//		Element root = doc.getRootElement();
//		json.put(root.getName(), iterateElement(root));
//		return json;
//	}
//
//	private static JSONObject iterateElement(Element element) {
//		List node = element.getChildren();
//		Element et = null;
//		JSONObject obj = new JSONObject();
//		List list = null;
//		for (int i = 0; i < node.size(); i++) {
//			list = new LinkedList();
//			et = (Element) node.get(i);
//			if (et.getTextTrim().equals("")) {
//				if (et.getChildren().size() == 0)
//					continue;
//				if (obj.containsKey(et.getName())) {
//					list = (List) obj.get(et.getName());
//				}
//				list.add(iterateElement(et));
//				obj.put(et.getName(), list);
//			} else {
//				if (obj.containsKey(et.getName())) {
//					list = (List) obj.get(et.getName());
//				}
//				list.add(et.getTextTrim());
//				obj.put(et.getName(), list);
//			}
//		}
//		return obj;
//	}
	public static JSON ConvertXMLtoJSON(String xml)  {

		XMLSerializer xmlSerializer = new XMLSerializer();
		JSON json = xmlSerializer.read(xml);
		return json;

	}
}
