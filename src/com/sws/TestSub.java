package com.sws;

import java.io.File;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class TestSub {
	public static void main(String[] args) throws Exception {
		SAXBuilder builder = new SAXBuilder(); 
		Document build = builder.build(new File("D:\\workspace\\spring-ws-server\\webapp\\WEB-INF\\test.xml"));
		Element requestElement = build.getRootElement();
		
		Namespace namespace  = Namespace.getNamespace("sch", "http://www.ispring.com/ws/hello/schemas");
//		Element sayHello = requestElement.getChild("sayHelloRequest", namespace);
		System.out.println(requestElement.getChild("name",namespace).getTextTrim());
		
	}
	
	
}
