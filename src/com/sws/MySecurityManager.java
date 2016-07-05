package com.sws;

import javax.xml.transform.dom.DOMSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.transform.JDOMSource;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MySecurityManager {
	/**
	 * 
	 * Ȩ��������
	 *
	 */
	public Object around(ProceedingJoinPoint  joinPoint) throws Throwable{  
		Object ret = null;

		String targetClass = joinPoint.getTarget().getClass().getName();
		Namespace namespace=Namespace.getNamespace("tns", "http://www.ispring.com/ws/hello/schemas");
		
		//ɨ��invoke��ͷ�ķ���
		if (targetClass.indexOf("com.sws") != -1) {
					System.out.println(joinPoint.toString());
			        DOMSource domSource = null;
					Object[] args = joinPoint.getArgs();
				   //ͨ������aop��������������request����Ϣ   
			        for (int i = 0; i < args.length; i++) {   
			           if (args[i] instanceof DOMSource) {   
			        	   System.out.println(args[i].toString());
			            	domSource = (DOMSource) args[i];   
			            	break;
			            }   
			        }   
			        
					//����������н������������Լ���ȫ��Կ
					Node node = domSource.getNode();
					String responseName = node.getLocalName().replaceAll("Request", "Response");
					
					// ȡ��header�ڵ㣬�����н�����invoker�Լ�secretKey
					NodeList elements = node.getChildNodes();
					Node headNode = null;
					for (int i = 0; i < elements.getLength(); i++) {
						headNode = elements.item(i);
						if (null != headNode.getLocalName() && headNode.getLocalName().equalsIgnoreCase("header")) {
							break;
						}
					}
					
					String invoker = "";
					String secretKey = "";
					NodeList headElements = headNode.getChildNodes();
					for (int i = 0; i < headElements.getLength(); i++) {
						headNode = headElements.item(i);
						
						if (null != headNode.getLocalName() && headNode.getLocalName().equalsIgnoreCase("invoker")) {
							invoker = headNode.getFirstChild().getNodeValue();
							System.out.println(invoker);
						}
						if (null != headNode.getLocalName() && headNode.getLocalName().equalsIgnoreCase("secretKey")) {
							secretKey = headNode.getFirstChild().getNodeValue();
							System.out.println(secretKey);
						}
					}
					
					
					if (!invoker.equals("admin") || !secretKey.equals("yy")) {
						
						Element response =new Element(responseName, namespace);
						
						Element returnFlag = new Element("returnFlag" ,namespace);
						Element code=new Element("code" ,namespace);
						code.setText("0001");
						returnFlag.addContent(code);
						
						Element desc=new Element("desc" ,namespace);
						desc.setText("δ��Ȩ, �ܾ�����!");
						returnFlag.addContent(desc);
						
						response.addContent(returnFlag);
						
						JDOMSource source =  new JDOMSource(response);
						
						return source;
					}
					
		}
		ret = joinPoint.proceed();
		return ret;
	  }
}
