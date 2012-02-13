package org.wso2.carbon.web.test.is;

import com.thoughtworks.selenium.Selenium;

import java.io.File;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

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


public class ISXACML extends CommonSetup {

    public ISXACML(String text) {
        super(text);
    }


    /* Tests Profile Management UI */
    public static void entitlementPolicyUI() throws Exception {
        selenium.click("link=Policies");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("link=Add New Entitlement Policy"));
        assertTrue(selenium.isElementPresent("link=Import New Entitlement Policy"));
        assertTrue(selenium.isElementPresent("link=Evaluate Entitlement Policies"));
        assertTrue(selenium.isTextPresent("User Entitlement"));

    }


    public static void addNewPolicy() throws Exception {
        selenium.click("link=Policies");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Add New Entitlement Policy");
        selenium.waitForPageToLoad("30000");
        assertEquals("Source View", selenium.getText("//div[@id='editor-canvas']/ul/li[1]/a/em"));

        selenium.click("//div[@id='editor-canvas']/ul/li[2]/a/em");
        assertEquals("Design View", selenium.getText("//div[@id='editor-canvas']/ul/li[2]/a/em"));
        selenium.click("//span[@id='policytree-button-panel']/input[1]");
        selenium.waitForPageToLoad("30000");
        assertEquals("Entitlement policy added successfully", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");

    }

    public static void cancelAddNewPolicy() throws Exception {
        selenium.click("link=Add New Entitlement Policy");
        selenium.waitForPageToLoad("30000");
//        selenium.click("//input[@value='Cancel']");
        selenium.click("go-back");
        selenium.waitForPageToLoad("30000");
    }


    public static void importPolicy() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(selenium);
        selenium.click("link=Policies");
        selenium.waitForPageToLoad("30000");

        selenium.click("link=Import New Entitlement Policy");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Import New Entitlement Policy"));
        File resourcePath = new File("." + File.separator + "lib" + File.separator + "samplePolicy.xml");
        InstSeleniumTestBase.SetFileBrowse("entpolicy", resourcePath.getCanonicalPath());
        selenium.click("//input[@value='Upload']");
        selenium.waitForPageToLoad("30000");
        assertEquals("Entitlement policy imported successfully", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");
        assertTrue(selenium.isTextPresent("exact:urn:sample:xacml:2.0:samplepolicy"));
    }


    public static void cancelPolicyImport() throws Exception {
        selenium.click("link=Policies");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Import New Entitlement Policy");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Import New Entitlement Policy"));
        selenium.click("//input[@value='Cancel']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("User Entitlement"));
    }


    public static void policyEvaluation() throws Exception {
        selenium.click("link=Policies");
        selenium.waitForPageToLoad("30000");

        selenium.click("link=Evaluate Entitlement Policies");
        selenium.waitForPageToLoad("30000");
        selenium.type("policy", "<Request xmlns=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  \n   <Subject>  \n   <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\"  \n          DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n      <AttributeValue>admin</AttributeValue>  \n     </Attribute>  \n     <Attribute AttributeId=\"group\"  \n          DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n      <AttributeValue>admin</AttributeValue>  \n    </Attribute>  \n   </Subject>  \n   <Resource>  \n    <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n     <AttributeValue>http://localhost:8280/services/echo/echoString</AttributeValue>  \n    </Attribute>  \n   </Resource>  \n   <Action>  \n    <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n     <AttributeValue>read</AttributeValue>  \n    </Attribute>  \n   </Action>  \n   <Environment/>  \n   </Request>");
        selenium.click("//input[@value='Evaluate']");
        selenium.waitForPageToLoad("30000");
        //    assertEquals("NotApplicable", selenium.getText("messagebox-info"));
        assertEquals("Permit", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");
        selenium.type("policy", "<Request xmlns=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  \n   <Subject>  \n   <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\"  \n          DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n      <AttributeValue>admin</AttributeValue>  \n     </Attribute>  \n     <Attribute AttributeId=\"group\"  \n          DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n      <AttributeValue>admin</AttributeValue>  \n    </Attribute>  \n   </Subject>  \n   <Resource>  \n    <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n     <AttributeValue>http://10.100.1.98:8280/services/echo/echoString</AttributeValue>  \n    </Attribute>  \n   </Resource>  \n   <Action>  \n    <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n     <AttributeValue>read</AttributeValue>  \n    </Attribute>  \n   </Action>  \n   <Environment/>  \n   </Request>");
        selenium.click("//input[@value='Evaluate']");
        selenium.waitForPageToLoad("30000");
        //  assertEquals("Permit", selenium.getText("messagebox-info"));
        assertEquals("NotApplicable", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");
        selenium.type("policy", "<Request xmlns=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  \n   <Subject>  \n   <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\"  \n          DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n      <AttributeValue>admin1</AttributeValue>  \n     </Attribute>  \n     <Attribute AttributeId=\"group\"  \n          DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n      <AttributeValue>admin1</AttributeValue>  \n    </Attribute>  \n   </Subject>  \n   <Resource>  \n    <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n     <AttributeValue>http://10.100.1.98:8280/services/echo/echoString</AttributeValue>  \n    </Attribute>  \n   </Resource>  \n   <Action>  \n    <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">  \n     <AttributeValue>read</AttributeValue>  \n    </Attribute>  \n   </Action>  \n   <Environment/>  \n   </Request>");
        selenium.click("//input[@value='Evaluate']");
        selenium.waitForPageToLoad("30000");
        //   assertEquals("Deny", selenium.getText("messagebox-info"));
        assertEquals("NotApplicable", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");
    }


    public static void deletePolicy(String PolicyName) throws Exception {
        selenium.click("link=Policies");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(PolicyName));
        selenium.click("//a[@onclick=\"remove('" + PolicyName + "');return false;\"]");
        assertTrue(selenium.isTextPresent("You are about to remove " + PolicyName + ". Do you want to proceed?"));
        //     assertTrue(selenium.getText("messagebox-confirm").matches("^exact:You are about to remove " + PolicyName + ". Do you want to proceed[\\s\\S]$"));
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
    }


    /* CSHelp */
    public static void testCSHelp() throws Exception{
        String expectedForCSHelp="https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/carbon/entitlement/docs/userguide.html";
        selenium.click("link=Policies");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Help");
        String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(helpwinid);
        Thread.sleep(10000);
        assertTrue(selenium.isTextPresent("Entitlement Management"));
        String actualForCSHelp = selenium.getLocation();
        if(actualForCSHelp.equals(expectedForCSHelp))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    }

}