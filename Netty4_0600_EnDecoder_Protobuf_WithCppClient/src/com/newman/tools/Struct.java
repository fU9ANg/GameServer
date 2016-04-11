package com.newman.tools;

import java.util.ArrayList;
import java.util.List;

public class Struct {

	private String type;
	private String name;
	private List<Field> fields = new ArrayList<Field>();

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

	@Override
	public String toString() {
		return "Struct [type=" + type + ", name=" + name + ", fields=" + fields + "]";
	}


}
