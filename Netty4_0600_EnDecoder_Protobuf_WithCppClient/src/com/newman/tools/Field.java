package com.newman.tools;

public class Field {

	private String modifier;
	private String type;
	private String name;
	private String value;

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Field [modifier=" + modifier + ", type=" + type + ", name=" + name + ", value=" + value + "]";
	}

}
