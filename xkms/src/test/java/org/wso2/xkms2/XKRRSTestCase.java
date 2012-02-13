/*
 * Copyright 2001-2004 The Apache Software Foundation.
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
package org.wso2.xkms2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.impl.dom.DOOMAbstractFactory;

public class XKRRSTestCase extends TestCase {

    protected String baseDir = System.getProperty("basedir");

    protected String testResourceDir = "src" + File.separator + "test"
            + File.separator + "resources" + File.separator + "XKRRS";

    public XKRRSTestCase(String name) {
        super(name);
        if (baseDir == null) {
            baseDir = (String) new File(".").getAbsolutePath();
        }
    }

    public InputStream getResource(String name) {
        String filePath = new File(testResourceDir, name).getAbsolutePath();

        try {
            FileInputStream fis = new FileInputStream(filePath);
            return fis;
        } catch (FileNotFoundException e) {
            fail("Cannot get resource: " + e.getMessage());
            throw new RuntimeException();
        }
    }

    public OMElement getResourceAsElement(String name) {
        try {
            InputStream in = getResource(name);
            
            XMLStreamReader reader  = XMLInputFactory.newInstance().createXMLStreamReader(in);
            StAXOMBuilder builder = new StAXOMBuilder(OMAbstractFactory.getOMFactory(), reader);
            OMElement element = builder.getDocumentElement();
            element.build();
            
            StAXOMBuilder builder2 = new StAXOMBuilder(DOOMAbstractFactory.getOMFactory(), element.getXMLStreamReader());
            OMElement element2 = builder2.getDocumentElement();
            return element2;

        } catch (Exception e) {
            fail("Cannot get resource: " + e.getMessage());
            throw new RuntimeException();
        }
    }
}
