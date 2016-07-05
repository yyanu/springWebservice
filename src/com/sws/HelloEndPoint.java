package com.sws;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom.input.SAXBuilder;
import org.springframework.util.Assert;
import org.springframework.ws.server.endpoint.AbstractDomPayloadEndpoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class HelloEndPoint extends AbstractDomPayloadEndpoint{
    public static final String NAMESPACE_URI = "http://www.ispring.com/ws/hello/schemas";
    public static final String HELLO_REQUEST_LOCAL_NAME = "sayHelloRequest";
    public static final String HELLO_RESPONSE_LOCAL_NAME = "sayHelloResponse";
    
    private HelloService helloService;
    
    @Override
    protected Element invokeInternal(Element requestElement, Document document)throws Exception {
        Assert.isTrue(NAMESPACE_URI.equals(requestElement.getNamespaceURI()), "Invalid namespace");
        Assert.isTrue(HELLO_REQUEST_LOCAL_NAME.equals(requestElement.getLocalName()), "Invalid local name");
        
        Document document1 = requestElement.getOwnerDocument();
        DOMImplementationLS domImplLS = (DOMImplementationLS) document1
                .getImplementation();
      LSSerializer serializer = domImplLS.createLSSerializer();
      String str = serializer.writeToString(requestElement); 
      System.out.println(str);
        
        
        Document document2 = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("D:\\workspace\\spring-ws-server\\webapp\\WEB-INF\\test.xml"));
//        Document document1 = requestElement.getOwnerDocument();
        DOMImplementationLS domImplLS1 = (DOMImplementationLS) document2
                  .getImplementation();
        LSSerializer serializer1 = domImplLS1.createLSSerializer();
        String str1 = serializer1.writeToString(document2.getDocumentElement()); 
        System.out.println(str1);
        
        NodeList children = requestElement.getChildNodes();
        Text requestText = null;
        for(int i=0; i<children.getLength(); i++){
            if(children.item(i).getNodeType() == Node.TEXT_NODE){
                requestText = (Text) children.item(i);
            }
        }
        
        if(requestText == null){
            throw new IllegalArgumentException("Could not find request text node");
        }
        
        String response = helloService.sayHello(requestText.getNodeValue());
        
        Element responseElement = document.createElementNS(NAMESPACE_URI, HELLO_RESPONSE_LOCAL_NAME);
        Text responseText = document.createTextNode(response);
        responseElement.appendChild(responseText);
        return responseElement;
    }
    public HelloService getHelloService() {
        return helloService;
    }
    public void setHelloService(HelloService helloService) {
        this.helloService = helloService;
    }

}
