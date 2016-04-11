package com.newman.tools;

import java.util.ArrayList;
import java.util.List;

public class Protocol {

	private String name;
	private String type;
	private String packagename;
	private String java_package;
	private String classname;

	private List<Struct> list = new ArrayList<Struct>();

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

	@Override
	public String toString() {
		return "Protocol [name=" + name + ", type=" + type + ", packagename=" + packagename + ", java_package="
				+ java_package + ", classname=" + classname + ", list=" + list + "]";
	}

}
