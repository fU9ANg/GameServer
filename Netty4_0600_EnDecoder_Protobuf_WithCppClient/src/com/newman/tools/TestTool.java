package com.newman.tools;

import org.dom4j.DocumentException;

public class TestTool {

	public static void main(String[] args) throws DocumentException {
		Protocol protocol = new Protocol();
		boolean bool = protocol.loadObjectFromXml("src/protocol.xml");
		if (bool) {
			if (protocol.checkValid())
				System.out.println(protocol);
		}
	}
}
