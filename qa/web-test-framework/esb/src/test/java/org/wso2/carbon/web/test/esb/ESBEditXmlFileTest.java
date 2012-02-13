package org.wso2.carbon.web.test.esb;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.StringWriter;
import java.io.OutputStream;
import java.io.FileOutputStream;

public class ESBEditXmlFileTest extends CommonSetup{
    public ESBEditXmlFileTest(String text) {
        super(text);
    }


    public void editCarbonXmlFile(String element,String elementText) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        Element versionElement=null;
        String carbonXmlPath=esbCommon.getCarbonHome()+File.separator+"conf"+File.separator+"carbon.xml";
        Document doc = parseXmlDocument(carbonXmlPath);
        //Getting the xml root element
        Element root = doc.getDocumentElement();

        //NodeList nodeList = root.getElementsByTagName("Server");
        NodeList nodeList = root.getChildNodes();
        for(int i=0;i<nodeList.getLength();i++){
            String nd=nodeList.item(i).getNodeName();
            if(nd.equals("Version")){
                versionElement = (Element) nodeList.item(i);
                break;
            }
        }
        Node protectedTokens = doc.createElement(element);
        protectedTokens.setTextContent(elementText);
        root.insertBefore(protectedTokens,versionElement);

        //save the xml content to file
        saveXmlToFile(carbonXmlPath,doc);
  }

   public void editPasswordTransportsXmlFile(String elelementTag,String attribute,String element,String editEleText) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        String transportsXmlPath=esbCommon.getCarbonHome()+File.separator+"conf"+File.separator+"transports.xml";
        Document doc = parseXmlDocument(transportsXmlPath);
        //Getting the xml root element
        Element root = doc.getDocumentElement();

        NodeList nodeList = root.getElementsByTagName(elelementTag);
        for(int i=0; i<nodeList.getLength(); i++) {
           String attrib=nodeList.item(i).getAttributes().item(0).toString();
           if(attrib.equals(attribute+"=\""+ element +"\"")){
               // Element name = (Element)nameElements.item(i);
               System.out.println(nodeList.item(i).getTextContent());
               //versionElement = (Element) nodeList.item(i);
               nodeList.item(i).setTextContent(editEleText);
               break;
           }
        }


       //save the xml content to file
       saveXmlToFile(transportsXmlPath,doc);
   }


  public void  addNewAttributeToTransportXmlFile(String elelementTag,String attribute,String element,String newAttribute,String attributeValue) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        String transportsXmlPath=esbCommon.getCarbonHome()+File.separator+"conf"+File.separator+"transports.xml";
        Document doc = parseXmlDocument(transportsXmlPath);

        //Getting the xml root element
        Element root = doc.getDocumentElement();

        NodeList nodeList = root.getElementsByTagName(elelementTag);
        for(int i=0; i<nodeList.getLength(); i++) {
           String attrib=nodeList.item(i).getAttributes().item(1).toString();
           if(attrib.equals(attribute+"=\""+ element +"\"")){
               Element ele = (Element)nodeList.item(i);
               // Add an attribute
              ele.setAttribute(newAttribute, attributeValue);
              break;
           }
        }
      //save the xml content to file
      saveXmlToFile(transportsXmlPath,doc);
 }

public void addNewNodesToTransportXmlFile(String elelementTag,String newAttribute,String attributeValue,String newEle,String elementText) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        String transportsXmlPath=esbCommon.getCarbonHome()+File.separator+"conf"+File.separator+"transports.xml";
        Document doc = parseXmlDocument(transportsXmlPath);

        //Getting the xml root element
        Element root = doc.getDocumentElement();

        Node n1=root.getElementsByTagName(elelementTag).item(0);
        System.out.println(n1.getNodeName());

        Element parameter = doc.createElement(newEle);
        parameter.setTextContent(elementText);
        parameter.setAttribute(newAttribute, attributeValue);

        n1.appendChild(parameter);
        
       //save the xml content to file
       saveXmlToFile(transportsXmlPath,doc);
}

  public Document parseXmlDocument(String fileNm) throws Exception{
      File file = new File(fileNm);
      Element versionElement=null;

      //Create instance of DocumentBuilderFactory
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

      //Get the DocumentBuilder
      DocumentBuilder docBuilder = factory.newDocumentBuilder();
      //Parsing XML Document
      Document doc = docBuilder.parse(file);

      return doc;
  }

  public void saveXmlToFile(String Filename,Document doc) throws Exception{
         File f1 = new File(Filename);
         //setting up a transformer
         TransformerFactory transfac = TransformerFactory.newInstance();
         Transformer trans = transfac.newTransformer();

         //generating string from xml tree
         StringWriter sw = new StringWriter();
         StreamResult result = new StreamResult(sw);
         DOMSource source = new DOMSource(doc);
         trans.transform(source, result);
         String xmlString = sw.toString();

         //Saving the XML content to File
         OutputStream f0;
         byte buf[] = xmlString.getBytes();
         f0 = new FileOutputStream(f1);
         for(int i=0;i<buf .length;i++) {
         f0.write(buf[i]);
         }
         f0.close();
         buf = null;
  }

}


