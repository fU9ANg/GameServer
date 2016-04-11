package com.newman.tools;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class TestTool {

	public static void main(String[] args) throws DocumentException {
		Protocol protocol = new Protocol();

		SAXReader sr = new SAXReader();
		Document doc = sr.read("src/protocol.xml");
		Element root = doc.getRootElement();

		Attribute root_name_attr = root.attribute("name");
		Element root_name_ele = root.element("name");
		System.out.println(root_name_attr);
		System.out.println(root_name_ele);
		// xml protocol name
		if (root.attribute("name") == null && root.element("name") == null) {
			System.out.println("11111111");
			protocol.setName(root.getName());
		} else if (root.attribute("name") != null && root.element("name") == null) {
			System.out.println("2222222222222222");
			protocol.setName(root.attribute("name").getValue());
		} else if (root.attribute("name") == null && root.element("name") != null) {
			System.out.println("3333333333333" + root.element("name").getTextTrim());
			protocol.setName(root.element("name").getTextTrim());
		} else if (root.attribute("name") != null && root.element("name") != null) {
			System.out.println(root.element("name").getTextTrim());
			System.out.println(root.attribute("name").getValue());
			if (root.element("name").getTextTrim().equals(root.attribute("name").getValue())) {
				System.out.println("444444444444");

				protocol.setName(root.element("name").getTextTrim());
			} else {
				System.out.println("协议名称定义错误！");
				return;
			}
		}

	
		// if (root.element("name") == null) {
		// protocol.setName(root.getName());
		// } else {
		// protocol.setName(root.element("name").getStringValue());
		// }

		if (root.attribute("name") == null) {
			protocol.setName(root.getName());
		} else {
			protocol.setName(root.attribute("name").getValue());
		}

		if (root.attribute("type") == null) {
			protocol.setType("protobuf");
		} else {
			protocol.setType(root.attribute("type").getValue());
		}

		List list = root.elements();
		for (Object obj : list) {
			Element ele = (Element) obj;

			if ("packagename".equals(ele.getName())) {
				protocol.setPackagename(ele.getTextTrim());
			} else if ("java_package".equals(ele.getName())) {
				protocol.setJava_package(ele.getTextTrim());
			} else if ("classname".equals(ele.getName())) {
				protocol.setClassname(ele.getTextTrim());
			}

			else if ("message".equals(ele.getName())) {
				String structName = ele.attributeValue("name");
				List fieldList = ele.elements("field");
				Struct struct = new Struct();
				struct.setType("message");
				struct.setName(structName);

				int fieldindex = 1;
				for (Object fieldObj : fieldList) {
					Element fieldEle = (Element) fieldObj;
					Field field = new Field();
					field.setModifier(fieldEle.attributeValue("modifier"));
					field.setType(fieldEle.attributeValue("type"));
					field.setName(fieldEle.attributeValue("name"));
					field.setValue("" + fieldindex);
					fieldindex++;
					struct.addField(field);
				}
				protocol.addStruct(struct);
			} else if ("enum".equals(ele.getName())) {

				String structName = ele.attributeValue("name");
				List fieldList = ele.elements("type");
				Struct struct = new Struct();
				struct.setType("enum");
				struct.setName(structName);

				for (Object fieldObj : fieldList) {
					Element fieldEle = (Element) fieldObj;
					Field field = new Field();
					field.setName(fieldEle.attributeValue("name"));
					field.setValue(fieldEle.attributeValue("value"));
					struct.addField(field);
				}
				protocol.addStruct(struct);

			}
		}
		System.out.println(protocol);
	}
}
