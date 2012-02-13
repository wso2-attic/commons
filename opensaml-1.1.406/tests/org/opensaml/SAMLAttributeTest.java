/*
 *  Copyright 2001-2005 Internet2
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import junit.framework.TestCase;

/**
 * @author Scott Cantor
 *
 */
public class SAMLAttributeTest extends TestCase
{
    private String xmlpath = "data/org/opensaml/SAMLAttributeTest.xml";
    
    public SAMLAttributeTest(String arg0)
    {
        super(arg0);
		Logger.getRootLogger().setLevel(Level.OFF);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(SAMLAttributeTest.class);
    }

    public void testSAMLAttribute() throws Exception
    {
        SAMLAttribute obj = new SAMLAttribute(new FileInputStream(xmlpath));
        obj.addValue("Bar");
        obj.setType(new QName(XML.XSD_NS,"string"));
        //obj.toStream(System.err);
        
        SAMLAttribute obj2 = new SAMLAttribute(new ByteArrayInputStream(obj.toString().getBytes()));
        Iterator values = obj2.getValues();
        assertEquals(values.next().toString(),"");
        assertEquals(values.next().toString(),"Bar");
        assertEquals(obj2.getType(),new QName(XML.XSD_NS,"string"));
    }
}
