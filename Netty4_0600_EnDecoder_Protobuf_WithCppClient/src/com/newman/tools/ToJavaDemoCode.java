package com.newman.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.dom4j.DocumentException;

public class ToJavaDemoCode {

	private static int _DEBUG = 0;

	public static boolean strIsNull(String str) {
		if (str == null || str.equals("")) {
			return true;
		}
		return false;
	}

	public static String loopProcessMessage(String mainMessageName, Protocol proto, Field f,
			boolean repeated) {
		String str = "";

		String dataType = f.getType();
		if (proto.checkIsBaseType(dataType)) {
			str += "\tSystem.out.println(" + mainMessageName + ".get" + f.getName() + "());\n";
		} else {
			Struct s = proto.getStructByName(dataType);
			List<Field> fieldList = s.getFields();
			for (Object obj : fieldList) {
				Field ff = (Field) obj;
				boolean ffRepeated = ff.getModifier().equals("repeated");
				if (ffRepeated) {
					str += "\tfor (int index=0; index<" + mainMessageName + ".get" + f.getName()
							+ ".get" + ff.getName() + ".size(), index++) {\n";
					str += loopProcessMessage(mainMessageName + ".get" + f.getName(), proto, ff,
							ffRepeated);
					str += "\t}\n";
				} else {

					if (repeated) {
						str += loopProcessMessage(
								mainMessageName + ".get" + f.getName() + "(index)", proto, ff,
								ffRepeated);
					} else {
						str += loopProcessMessage(mainMessageName + ".get" + f.getName(), proto, ff,
								ffRepeated);
					}
				}
			}
		}

		return str;
	}

	public static boolean writeFile(String fileName, Protocol proto) {
		File file = new File(fileName + proto.getName() + "_DemoCode.java");

		FileWriter fw;
		BufferedWriter bw;

		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);

			List<Struct> structList = proto.getList();
			Struct enumStruct = proto.getStructByType("enum");
			Struct mainMessageStruct = proto.getStructByMainMessage();
			List<Field> fieldList = mainMessageStruct.getFields();
			for (Object obj : fieldList) {
				Field f = (Field) obj;
				if (f.getRefEnumValue() == null) {
					continue;
				}
				bw.write("case " + proto.getClassname() + "." + enumStruct.getName() + "."
						+ f.getRefEnumValue() + "_VALUE:\n");

				bw.write(loopProcessMessage("message", proto, f,
						f.getModifier().equals("repeated")));
				bw.write("\tbreak;\n");
			}
			bw.write("default:\n\tSystem.out.println(\"No support message type!\");\n");
			bw.write("\tbreak;");

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

		return true;
	}

	public static void main(String[] args) {
		Protocol protocol = new Protocol();
		boolean bool = false;
		try {
			bool = protocol.loadObjectFromXml("src/protocol.xml");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!bool) {
			System.out.println("Error: loadObjectFromXml() function!");
			return;
		}

		if (!protocol.checkValid()) {
			System.out.println("Error: checkValid() function!");
			return;
		}

		writeFile("/home/newman/", protocol);
	}

}
