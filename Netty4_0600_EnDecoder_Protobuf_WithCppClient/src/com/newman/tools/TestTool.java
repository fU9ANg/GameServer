package com.newman.tools;

import java.util.List;

import org.dom4j.DocumentException;

public class TestTool {
	public static void main(String[] args) throws DocumentException {
		Protocol protocol = new Protocol();
		boolean bool = protocol.loadObjectFromXml("src/protocol.xml");
		if (bool) {
			if (protocol.checkValid())
				System.out.println(protocol);
		}

		// DEBUG protocol.
		System.out.println("=========== protocol ===========");
		System.out.println("comment: " + protocol.getComment());
		System.out.println("name: " + protocol.getName());
		System.out.println("type: " + protocol.getType());
		System.out.println("packagename: " + protocol.getPackagename());
		System.out.println("java_package: " + protocol.getJava_package());
		System.out.println("classname: " + protocol.getClassname());

		List<Struct> sl = protocol.getList();
		for (int i = 0; i < sl.size(); i++) {
			System.out.println("\t=========== struct ===========");
			System.out.println("\tcomment: " + sl.get(i).getComment());
			System.out.println("\ttype: " + sl.get(i).getType());
			System.out.println("\tname: " + sl.get(i).getName());
			System.out.println("\tmainmessage: " + sl.get(i).getMainMessage());

			List<Field> fl = sl.get(i).getFields();
			for (int j = 0; j < fl.size(); j++) {
				System.out.println("\t\t=========== Field ===========");
				System.out.println("\t\tcomment: " + fl.get(j).getComment());
				System.out.println("\t\tmodifier: " + fl.get(j).getModifier());
				System.out.println("\t\ttype: " + fl.get(j).getType());
				System.out.println("\t\tname: " + fl.get(j).getName());
				System.out.println("\t\tvalue: " + fl.get(j).getValue());
			}
		}
	}
}