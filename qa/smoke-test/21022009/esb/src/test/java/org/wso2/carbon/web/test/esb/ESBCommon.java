package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

public class ESBCommon extends TestCase {
    Selenium selenium;

    public ESBCommon(Selenium _browser){
        selenium=_browser;
    }

    /*
	 * This method will be used when updating mediators
	 */
    public void testMediatorUpdate() throws Exception {
        Thread.sleep(2000);
        selenium.click("//input[@value='Update']");
        Thread.sleep(5000);
    }

    /*
	 * This method will be used when clicking on the Save button of the Sequence editor
	 */
    public void testSequenceSave() throws Exception {
        Thread.sleep(2000);
        selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");
    }

    /*
	 * This method will be used when clicking on the Apply button of the Sequence editor
	 */
    public void testSequenceApply() throws Exception {
        Thread.sleep(2000);        
        selenium.click("applyButton");
		selenium.waitForPageToLoad("30000");
    }

    /*
	 * This method will be used when clicking on the Cancel button of the Sequence editor
	 */
    public void testSequenceCancel() throws Exception {
        Thread.sleep(2000);        
        selenium.click("//input[@value='Cancel']");
		selenium.waitForPageToLoad("30000");        
    }

    /*
	 * This method will be used when adding a new Sequence
	 */
    public void testAddSequence(String sequenceName){
        selenium.click("link=Sequences");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add Sequence");
		selenium.waitForPageToLoad("30000");
		selenium.type("sequence.name", sequenceName);
    }

    /*
	 * This method will verify the Help links of the mediators
	 */
     public void testMediatorHelp(String mediatorName) throws Exception {
        //Commenting out the method till the issue is resolved

        //Verifying the Help link
//        assertEquals("Help", selenium.getText("//div[@id='mediatorDesign']/div/div/a"));
//		selenium.click("//div[@id='mediatorDesign']/div/div/a");
//        Thread.sleep(1000);
//
//        String popupidhelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
//        selenium.selectWindow(popupidhelp);
//
//        String a = selenium.getText("//h1");
//
//        assertEquals(mediatorName+" Mediator", selenium.getText("//h1"));
//        selenium.close();
//
//        // Bringing control back to main window.
//        selenium.selectWindow("");
    }

    /*
	 * This method will verify the docs links of the Management Console
	 */
     public void testDocLinks(String linkName, String verifyText, String headerLevel) throws Exception {
        //verifying the document links
        assertEquals(linkName, selenium.getText("link="+linkName));
		assertTrue(selenium.isElementPresent("link="+linkName));
		selenium.click("link="+linkName);
        Thread.sleep(25000);

        String popupidaboute = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(popupidaboute);

        if (verifyText!=null && headerLevel!=null){
            assertEquals(verifyText, selenium.getText(headerLevel));
        }
        selenium.close();

        // Bringing control back to main window.
        selenium.selectWindow("");
    }

    /*
	 * This method will select the resources from the Registry Browser
	 */
    public void testSelectResource(String resourceType, String resourceName) throws Exception {
        Thread.sleep(5000);
        String popupid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(popupid);

        selenium.select("local-registry-keys-selection", "label=["+resourceType+"]-"+resourceName);
//        selenium.click("//input[@value='OK']");
		selenium.click("link=X");        
    }


    /*
     * This method will be used to add namespaces through the namespace editor
     */
    public void testAddNamespace(String prefix, String uri) throws Exception{
        Thread.sleep(1000);
        selenium.click("addNSButton");
		selenium.type("prefix0", prefix);
		selenium.type("uri0", uri);
		selenium.click("saveNSButton");
    }

    /*
     * A method used to delete mediators from the Sequence tree
     */
    public void testDeleteMediator(String mediatorName) throws Exception{
		//Deleting the relevant mediator
        selenium.click("link="+mediatorName);
		selenium.click("link=Delete");
		selenium.click("//button[@type='button']");
		selenium.waitForPageToLoad("30000");

        //Verifying the confirmation message
        assertTrue(selenium.isTextPresent("Do you want to delete the selected mediator?"));
		selenium.click("//button[@type='button']");
		selenium.waitForPageToLoad("30000");        
    }

    /*
	 * This method will be used when adding a new Sequence
	 */
    public void testAddEndpoint() throws Exception{
        selenium.click("link=Endpoints");
        selenium.waitForPageToLoad("30000");
    }

    /*
	 * This method will verify the Help links of each component
	 */
     public void testComponentHelp(String compName) throws Exception {

        //Verifying the Help link
		assertEquals("Help", selenium.getText("link=Help"));
		selenium.click("link=Help");
        Thread.sleep(1000);

        String popupidhelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(popupidhelp);

        String a = selenium.getText("//h1");

		assertEquals(compName, selenium.getText("//h1"));
        selenium.close();

        // Bringing control back to main window.
        selenium.selectWindow("");
    }


    /*
    This method will add child mediators to the root level of the sequence tree
    nodeType will always be wither Add Child or Add Sibling
     */
    public void testAddRootLevelChildren(String nodeType, String mediatorCategory, String mediatorName) throws Exception{
        Thread.sleep(1000);
		if (nodeType!=null){
            selenium.click("link="+nodeType);
        }
        Thread.sleep(1000);
        if (mediatorCategory!=null){
            selenium.click("link="+mediatorCategory);
        }

        if (mediatorName.equals("Filter")){
            Thread.sleep(2000);
            selenium.click("//li[@id='filter']/a");
            Thread.sleep(2000);
        } else {
            Thread.sleep(1000);
            selenium.click("link="+mediatorName);
            Thread.sleep(1000);
        }
    }

    /*
     * This method will be used to add mediators to the second level of the sequence tree
     */
    public void testAddChildMediators(String childLevel, String mediatorCategory, String mediatorName) throws Exception{

        Thread.sleep(1000);
		if (childLevel!=null){
            selenium.click("//div[@id='mediator-"+childLevel+"']/div/div[1]/a");
        }
        Thread.sleep(2000);
        if (mediatorCategory!=null){
            selenium.click("link="+mediatorCategory);
        }

        if (mediatorName.equals("Filter")){
            Thread.sleep(2000);
            selenium.click("//li[@id='filter']/a");
            Thread.sleep(2000);
        } else {
            Thread.sleep(2000);
            selenium.click("link="+mediatorName);
            Thread.sleep(2000);
        }
    }

    /*
    This method will be used to Logout and login again if tests fail
     */

    public void testLogoutLogin() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        String content = null;
        boolean login = selenium.isTextPresent("Sign-out");

        if (login) {
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin", "admin");        
    }
}


