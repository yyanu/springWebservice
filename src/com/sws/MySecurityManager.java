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
	 * 权限拦截器
	 *
	 */
	public Object around(ProceedingJoinPoint  joinPoint) throws Throwable{  
		Object ret = null;

		String targetClass = joinPoint.getTarget().getClass().getName();
		Namespace namespace=Namespace.getNamespace("tns", "http://www.ispring.com/ws/hello/schemas");
		
		//扫描invoke开头的方法
		if (targetClass.indexOf("com.sws") != -1) {
					System.out.println(joinPoint.toString());
			        DOMSource domSource = null;
					Object[] args = joinPoint.getArgs();
				   //通过分析aop监听参数分析出request等信息   
			        for (int i = 0; i < args.length; i++) {   
			           if (args[i] instanceof DOMSource) {   
			        	   System.out.println(args[i].toString());
			            	domSource = (DOMSource) args[i];   
			            	break;
			            }   
			        }   
			        
					//从请求参数中解析出调用者以及安全密钥
					Node node = domSource.getNode();
					String responseName = node.getLocalName().replaceAll("Request", "Response");
					
					// 取得header节点，并从中解析出invoker以及secretKey
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
						desc.setText("未授权, 拒绝访问!");
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
