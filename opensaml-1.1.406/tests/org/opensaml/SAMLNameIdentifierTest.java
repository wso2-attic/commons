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

import java.io.FileInputStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import junit.framework.TestCase;

/**
 * @author Scott Cantor
 *
 */
public class SAMLNameIdentifierTest extends TestCase
{
    private String xmlpath = "data/org/opensaml/SAMLNameIdentifierTest.xml";
    
    public SAMLNameIdentifierTest(String arg0)
    {
        super(arg0);
		Logger.getRootLogger().setLevel(Level.OFF);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(SAMLNameIdentifierTest.class);
    }

    public void testSAMLNameIdentifier() throws Exception {
        SAMLNameIdentifier n = SAMLNameIdentifier.getInstance(new FileInputStream(xmlpath));
        assertTrue("NameIdentifier value is wrong",n.getName().startsWith("uid=By-Tor"));
        testSAMLNameIdentifier(n);
    }
    
    public static void testSAMLNameIdentifier(SAMLNameIdentifier n) throws Exception {
        assertEquals("NameIdentifier Format is wrong", n.getFormat(), SAMLNameIdentifier.FORMAT_X509);
        n.setName("uid=Snow Dog");
        
        SAMLNameIdentifier n2 = (SAMLNameIdentifier)n.clone();
        assertEquals("NameIdentifier value is wrong",n2.getName(),"uid=Snow Dog");
        assertEquals("names do not serialize equal",n.toString(),n2.toString());
    }
}
