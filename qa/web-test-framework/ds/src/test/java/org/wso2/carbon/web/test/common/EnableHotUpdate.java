/*
 *  Copyright (c) 2005-2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.web.test.common;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.File;
import java.io.*;
import java.util.Properties;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Oct 29, 2009
 * Time: 3:59:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class EnableHotUpdate implements ErrorHandler{

    public void warning(SAXParseException e) throws SAXException{
        show("Warning", e);

    }

    public void error(SAXParseException e) throws SAXException {
        show("Error", e);

    }

    public void fatalError(SAXParseException e) throws SAXException {
        show("Fatal Error", e);

    }

    private void show(String type, SAXParseException e) {
        System.out.println(type + ": " + e.getMessage());
        System.out.println("Line " + e.getLineNumber() + " Column " + e.getColumnNumber());
        System.out.println("System ID: " + e.getSystemId());
    }

    Properties properties = new Properties();

    public void changeHotUpdate(String towrite) throws Exception {
        FileInputStream file= new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(file);
        file.close();

        String xmlFile = properties.getProperty("carbon.home")+File.separator+"conf"+File.separator+"axis2.xml";

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        dbf.setNamespaceAware(true);
        dbf.setIgnoringElementContentWhitespace(true);

        Document doc = null;
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            builder.setErrorHandler(this);
            InputSource is = new InputSource(xmlFile);
            doc = builder.parse(is);


        } catch (Exception e) {
            System.err.println(e);
        }


        Element root = doc.getDocumentElement();

        NodeList nameElements =root.getElementsByTagName("parameter");
        // NodeList nameElements = root.getElementsByTagName("parameter");
        for(int i=0; i<nameElements.getLength(); i++) {

            String s=nameElements.item(i).getAttributes().item(0).toString();
            if(s.equals("name=\"hotupdate\"")){
                // Element name = (Element)nameElements.item(i);
                System.out.println(nameElements.item(i).getTextContent());
                nameElements.item(i).setTextContent(towrite);

            }

        }


        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();

        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        String xmlString = sw.toString();

        OutputStream f0;
        byte buf[] = xmlString.getBytes();
        f0 = new FileOutputStream(xmlFile);
        for(int i=0;i<buf .length;i++) {
            f0.write(buf[i]);
        }
        f0.close();
        buf = null;

    }


}
