package com.sws;

import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.ws.server.endpoint.AbstractJDomPayloadEndpoint;

public class HelloEndPointJDom extends AbstractJDomPayloadEndpoint{
	public HelloEndPointJDom(){}
	
	private HelloService helloService;
	
	Namespace namespace = Namespace.getNamespace("tns", "http://www.ispring.com/ws/hello/schemas");
	
	@Override
	protected Element invokeInternal(Element request) throws Exception {
		System.out.println(request);
		
		Element header = request.getChild("header",namespace);
		System.out.println(header.getChild("invoker", namespace).getTextTrim());
		System.out.println(header.getChild("secretKey", namespace).getTextTrim());
		
		String name = request.getChild("name",namespace).getTextTrim();
		String sayHello = helloService.sayHello(name);
		
		Element response = new Element("sayHelloResponse", namespace);
		Element returnFlag = new Element("returnFlag", namespace);
		Element code = new Element("code", namespace);
		code.setText("0");
		returnFlag.addContent(code);

		Element desc = new Element("desc", namespace);
		desc.setText(sayHello);
		returnFlag.addContent(desc);
		response.addContent(returnFlag);
		
		return response;
	}

	public HelloService getHelloService() {
		return helloService;
	}
	public void setHelloService(HelloService helloService) {
		this.helloService = helloService;
	}
}
