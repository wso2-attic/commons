package org.wso2.carbon.common.test.utils;

/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class ConfigHelper {
    public static Properties getProperties(String filepath) {

        File filePath = new File("./");
        String relativePath = null;
        try {
            relativePath = filePath.getCanonicalPath();

            File findFile = new File(relativePath + filepath);
            if (!findFile.isFile()) {
                filePath = new File("./../");
                relativePath = filePath.getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        filepath = relativePath + filepath;
        Properties prop = new Properties();
        FileInputStream fReader = null;
        try {
            fReader = new FileInputStream(filepath);
            prop.load(fReader);
            fReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    public static String getXMLConfig(String filepath) {

        File filePath = new File("./");
        String relativePath = null;
        try {
            relativePath = filePath.getCanonicalPath();

            File findFile = new File(relativePath + filepath);
            if (!findFile.isFile()) {
                filePath = new File("./../");
                relativePath = filePath.getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        filepath = relativePath + filepath;
        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = null;
        String configString = "";
        File SourceFile = new File(filepath);

        if (SourceFile.exists()) {
            try {
                scanner = new Scanner(new File(filepath));
                while (scanner.hasNextLine()) {
                    text.append(scanner.nextLine()).append(NL);
                }
                configString = text.toString();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (scanner != null) {
                    scanner.close();
                }
            }

        }
        return configString;
    }

    public static String readXML(String value, String XML) {
        int start = XML.indexOf(value + "=\"") + 2 + value.length();
        int end = -1;
        if (start != -1) {
            end = XML.indexOf("\"", start);
        }
        if (end != -1) {
            return XML.substring(start, end);
        }
        return "";

    }

    public static OMElement createOMElement(String filePath) {

        //if file location =null it taking from the test data directory
        OMElement documentElement = null;
        FileInputStream inputStream;
        File file = new File(filePath);
        if (file.exists()) {
            try {
                inputStream = new FileInputStream(filePath);
                XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
                //create the builder
                StAXOMBuilder builder = new StAXOMBuilder(parser);
                //get the root element (in this case the envelope)
                documentElement = builder.getDocumentElement();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }


        }

        return documentElement;

    }

}
