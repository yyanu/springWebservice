package com.sws;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class TestXmlParse {
	public static void main(String[] args) throws Exception {
	  Document document2 = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("D:\\workspace\\spring-ws-server\\webapp\\WEB-INF\\test.xml"));
      DOMImplementationLS domImplLS1 = (DOMImplementationLS) document2
                .getImplementation();
      LSSerializer serializer1 = domImplLS1.createLSSerializer();
      String str1 = serializer1.writeToString(document2.getDocumentElement()); 
      System.out.println(str1);
      
      NodeList children = document2.getDocumentElement().getChildNodes();
      Text requestText = null;
      System.out.println(children.getLength());
      for(int i=0; i<children.getLength(); i++){
    	  System.out.println(children.item(i).getNodeName());
    	  System.out.println(children.item(i).getNodeType());
          if(children.item(i).getNodeType() == Node.TEXT_NODE){
              requestText = (Text) children.item(i);
          }
      }
      System.out.println(requestText);
      
	}
}
