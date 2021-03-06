package com.newman.tools;

import java.util.ArrayList;
import java.util.List;

public class Struct {
	private String type;
	private String name;
	private String mainMessage;
	private String comment;

	private List<Field> fields = new ArrayList<Field>();

	private int nameIsExist(String name) {
		int child1size = this.fields.size();
		int num = 0;
		for (int j = 0; j < child1size; j++) {
			if (this.fields.get(j).getName().equals(name)) {
				num++;
			}
		}
		return num;
	}

	private int valueIsExist(String value) {
		int child1size = this.fields.size();
		int num = 0;
		for (int j = 0; j < child1size; j++) {
			if (this.fields.get(j).getValue().equals(value)) {
				num++;
			}
		}
		return num;
	}

	public String getFieldByName(String name) {
		List<Field> fl = this.getFields();
		for (Object o : fl) {
			Field f = (Field) o;
			if (f.getName().equals(name)) {
				return name;
			}
		}
		return null;
	}

	public String getMainMessage() {
		return mainMessage;
	}

	public void setMainMessage(String mainMessage) {
		this.mainMessage = mainMessage;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public boolean addField(Field f) {
		return fields.add(f);
	}

	public boolean delField(Field f) {
		return fields.remove(f);
	}

	public boolean checkNameValid() {
		int child1size = this.fields.size();
		int num = 0;
		for (int j = 0; j < child1size; j++) {
			num = this.nameIsExist(this.fields.get(j).getName());
			if (num > 1) {
				System.out.println("Error: [" + this.getName() + ", " + this.fields.get(j).getName()
						+ "]: 名称重复定义，请检查xml文件!!!");
				return false;
			}
		}
		return true;
	}

	public boolean checkValueValid() {
		int child1size = this.fields.size();
		int num = 0;
		for (int j = 0; j < child1size; j++) {
			num = this.valueIsExist(this.fields.get(j).getValue());
			if (num > 1) {
				System.out.println("Error: [" + this.getName() + ", " + this.fields.get(j).getName()
						+ "]: 值重复定义，请检查xml文件!!!");
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "\t[type=" + type + ", name=" + name + ", mainMessage=" + mainMessage + ", comment="
				+ comment + ", fields=" + fields + "]\n";
	}
}