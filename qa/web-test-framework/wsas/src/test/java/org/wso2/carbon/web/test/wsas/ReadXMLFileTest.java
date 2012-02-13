package org.wso2.carbon.web.test.wsas;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.*;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/*
 * Created by IntelliJ IDEA.
 * User: Suminda Chamara
 * Date: Mar 24, 2009
 * Time: 2:07:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReadXMLFileTest {
    String FilePath;
    String fileName;

    public ReadXMLFileTest(String getFileName) {
        fileName = getFileName;
    }

    public String ReadConfig(String childTag, String parentTag) throws Exception {


        try {

            File file = new File("src"+File.separator+"test"+File.separator+"java"+File.separator+"org"+File.separator+"wso2"+File.separator+"carbon"+File.separator+"web"+File.separator+"test"+File.separator+"wsas"+File.separator +fileName.substring(0,fileName.indexOf('.'))+".XML");

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
           // Document doc = docBuilder.parse(new File("src"+File.separator+"test"+File.separator+"java"+File.separator+"org"+File.separator+"wso2"+File.separator+"carbon"+File.separator+"web"+File.separator+"test"+File.separator+"wsas"+File.separator + fileName));
             Document doc = docBuilder.parse(file);
            // normalize text representation
            doc.getDocumentElement().normalize();
            //  System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());


            NodeList listOfMainTag = doc.getElementsByTagName(parentTag);
        //    int totalMainTags = listOfMainTag.getLength();               // getting  number of main tags
            //   System.out.println("Total no of main tags : " + totalMainTags);

            for (int s = 0; s < listOfMainTag.getLength(); s++) {


                Node firstTagNode = listOfMainTag.item(s);
                if (firstTagNode.getNodeType() == Node.ELEMENT_NODE) {


                    Element firstPersonElement = (Element) firstTagNode;

                    //-------
                    NodeList firstNameList = firstPersonElement.getElementsByTagName(childTag);
                    Element firstNameElement = (Element) firstNameList.item(0);

                    NodeList textFNList = firstNameElement.getChildNodes();
                    FilePath = ((Node) textFNList.item(0)).getNodeValue();
                    // System.out.println(FilePath);


                }//end of if clause


            }//end of for loop with s var


        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());

        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();

        } catch (Throwable t) {
            t.printStackTrace();
        }
        //System.exit (0);

        
        //end of main
        return FilePath;


    }
}
