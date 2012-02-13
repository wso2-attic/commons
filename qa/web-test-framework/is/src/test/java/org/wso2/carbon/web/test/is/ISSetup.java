package org.wso2.carbon.web.test.is;

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

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import com.thoughtworks.selenium.*;
import junit.framework.TestCase;


public class ISSetup extends CommonSetup {

    public ISSetup(String text) {
        super(text);
    }


    //Test XACML Engine
    public static void testXACML_Engine() throws Exception{
        selenium.click("link=Policies");
		selenium.waitForPageToLoad("30000");
        selenium.click("link=Add New Entitlement Policy");
		selenium.waitForPageToLoad("30000");
		selenium.click("save-policy");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Entitlement policy added successfully"));
		selenium.click("//button[@type='button']");

        selenium.click("link=Evaluate Entitlement Policies");
		selenium.waitForPageToLoad("30000");
		selenium.type("policy", "<Request xmlns=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  \n    <Subject>  \n     <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\"  \n          DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n      <AttributeValue>admin</AttributeValue>  \n      </Attribute>  \n     <Attribute AttributeId=\"group\"  \n          DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n      <AttributeValue>admin</AttributeValue>  \n    </Attribute>  \n   </Subject>  \n   <Resource>  \n    <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n     <AttributeValue>http://localhost:8280/services/echo/echoString</AttributeValue>  \n    </Attribute>  \n   </Resource>  \n   <Action>  \n    <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n     <AttributeValue>read</AttributeValue>  \n    </Attribute>  \n   </Action>  \n   <Environment/>  \n   </Request>");
		selenium.click("//input[@value='Evaluate']");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Permit"));
		selenium.click("//button[@type='button']");

        selenium.click("link=Policies");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=exact:urn:sample:xacml:2.0:samplepolicy");
		selenium.waitForPageToLoad("30000");
    }

}