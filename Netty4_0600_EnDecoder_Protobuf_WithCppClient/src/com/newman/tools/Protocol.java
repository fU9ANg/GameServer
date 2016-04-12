package com.newman.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Protocol {
	private String name;
	private String type;
	private String packagename;
	private String java_package;
	private String classname;
	private List<Struct> list = new ArrayList<Struct>();
	private Map<Integer, String> valueMap = new HashMap<Integer, String>();

	private List<String> baseDataTypeList = new ArrayList<String>();

	public Protocol() {
		super();
		baseDataTypeList.add("double");
		baseDataTypeList.add("float");
		baseDataTypeList.add("int32");
		baseDataTypeList.add("int64");
		baseDataTypeList.add("uint32");
		baseDataTypeList.add("uint64");
		baseDataTypeList.add("sint32");
		baseDataTypeList.add("sint64");
		baseDataTypeList.add("fixed32");
		baseDataTypeList.add("fixed64");
		baseDataTypeList.add("sfixed32");
		baseDataTypeList.add("sfixed64");
		baseDataTypeList.add("bool");
		baseDataTypeList.add("string");
		baseDataTypeList.add("bytes");
	}

	private String getElementValue(Element ele, String sName) {

		Map m = new HashMap();
		String value = "";
		int iCount = 0;

		Attribute root_attr_name = ele.attribute(sName);
		if (root_attr_name != null) {
			m.put("root_attr_name", root_attr_name);
		}
		Element root_ele_name = ele.element(sName);
		if (root_ele_name != null) {
			if (!root_ele_name.getTextTrim().equals(""))
				m.put("root_ele_name", root_ele_name);

			Attribute root_ele_attr_name = root_ele_name.attribute("value");
			if (root_ele_attr_name != null) {
				m.put("root_ele_attr_name", root_ele_attr_name);
			}
		}

		if (m.size() == 0) {
			value = ele.getName();
		}

		else if (m.size() == 1) {

			Iterator<Map.Entry> entries = m.entrySet().iterator();
			while (entries.hasNext()) {

				Map.Entry entry = entries.next();

				if (entry.getKey().equals("root_ele_name")) {
					value = ((Element) entry.getValue()).getTextTrim();
				} else {
					value = ((Attribute) entry.getValue()).getValue();
				}

			}
		} else {
			System.out.println("Error: [" + ele.getName() + ", " + sName + "]: 多处定义相同的值!!!");
			return null;
		}

		return value;
	}

	private boolean checkTypeIsExist(String typeName) {

		// check base data type
		for (int i = 0; i < baseDataTypeList.size(); i++) {
			if (baseDataTypeList.get(i).equals(typeName))
				return true;
		}

		// check user custom data type
		int size = this.list.size();
		for (int i = 0; i < size; i++) {
			if (list.get(i).getName().equals(typeName)) {
				return true;
			}
		}

		return false;
	}

	private Integer getFieldIndex(String sindex) {
		int index = Integer.parseInt(sindex);
		if (index == 0) {
			Iterator<Entry<Integer, String>> entries = valueMap.entrySet().iterator();
			while (entries.hasNext()) {

				Map.Entry entry = entries.next();
				if (entry.getValue().equals("false")) {
					valueMap.put((Integer) entry.getKey(), "true");
					return (Integer) entry.getKey();
				}
			}
		} else {
			if (valueMap.get(index).equals("true")) {
				return 0;
			} else {
				valueMap.put(index, "true");
				return index;
			}
		}

		return 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public String getJava_package() {
		return java_package;
	}

	public void setJava_package(String java_package) {
		this.java_package = java_package;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public List<Struct> getList() {
		return list;
	}

	public void setList(List<Struct> list) {
		this.list = list;
	}

	public boolean addStruct(Struct s) {
		return list.add(s);
	}

	public boolean delStruct(Struct s) {
		return list.remove(s);
	}

	public boolean loadObjectFromXml(String sXmlFile) throws DocumentException {

		SAXReader sr = new SAXReader();
		Document doc = sr.read(sXmlFile);

		Element root = doc.getRootElement();

		String sValue;
		// Name
		sValue = getElementValue(root, "name");
		if (sValue == null) {
			return false;
		}
		this.setName(sValue);

		// Type
		sValue = getElementValue(root, "type");
		if (sValue == null) {
			return false;
		}
		this.setType(sValue);

		// packagename
		sValue = getElementValue(root, "packagename");
		if (sValue == null) {
			return false;
		}
		this.setPackagename(sValue);

		// java_package
		sValue = getElementValue(root, "java_package");
		if (sValue == null) {
			return false;
		}
		this.setJava_package(sValue);

		// classname
		sValue = getElementValue(root, "classname");
		if (sValue == null) {
			return false;
		}
		this.setClassname(sValue);

		List list;
		// enum
		list = root.elements("enum");
		for (Object obj : list) {

			Element ele = (Element) obj;

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
			this.addStruct(struct);
		}
		// message
		list = root.elements("message");
		for (Object obj : list) {

			Element ele = (Element) obj;
			// auto value index

			valueMap.clear();
			for (int ii = 0; ii < ele.elements("field").size(); ii++) {
				valueMap.put(ii + 1, "false");
			}
			// reserve index
			for (int jj = 0; jj < ele.elements("field").size(); jj++) {
				Element fieldElement = (Element) ele.elements("field").get(jj);
				Attribute fieldValueAttri = fieldElement.attribute("value");
				if (fieldValueAttri != null) {
					valueMap.put(Integer.parseInt(fieldValueAttri.getValue()), "reserve");
				}
			}
			String structName = ele.attributeValue("name");
			List fieldList = ele.elements("field");
			Struct struct = new Struct();
			struct.setType("message");
			struct.setName(structName);
			for (Object fieldObj : fieldList) {
				Element fieldEle = (Element) fieldObj;
				Field field = new Field();
				field.setModifier(fieldEle.attributeValue("modifier"));
				field.setType(fieldEle.attributeValue("type"));
				field.setName(fieldEle.attributeValue("name"));

				// auto index value
				Attribute fieldEleValueAttr = fieldEle.attribute("value");
				int autoIndex;
				if (fieldEleValueAttr != null) {
					autoIndex = getFieldIndex(fieldEleValueAttr.getValue());
				} else {
					autoIndex = getFieldIndex("0");
				}
				if (autoIndex == 0) {
					System.out.println("Error: [" + structName + ", " + fieldEle.attributeValue("name")
							+ "]: 定义的value值已存在，请检查xml文件!!!");
					return false;
				}
				field.setValue(String.valueOf(autoIndex));

				struct.addField(field);
			}
			this.addStruct(struct);
		}

		return true;
	}

	public boolean checkValid() {
		int size = this.list.size();
		for (int i = 0; i < size; i++) {
			Struct s = list.get(i);

			// check message struct is valid?
			if (s.getType().equals("message")) {

				List<Field> listField = s.getFields();
				int child1size = listField.size();
				for (int j = 0; j < child1size; j++) {

					String typeName = listField.get(j).getType();
					boolean isExist = this.checkTypeIsExist(typeName);
					if (!isExist) {
						System.out.println("Error: [" + s.getName() + ", " + typeName + "]: 定义不存在，请检查xml文件!!!");
						return false;
					}
				}

			}

			// check value/name of (enum or message) struct is valid?

			// check field name is same
			if (s.checkNameValid() == false) {
				return false;
			}
			// check field value is same
			if (s.checkValueValid() == false) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "Protocol [name=" + name + ", type=" + type + ", packagename=" + packagename + ", java_package="
				+ java_package + ", classname=" + classname + ", list=\n" + list + "]\n";
	}
}