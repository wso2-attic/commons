package org.wso2.carbon.web.test.esb;

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

import com.thoughtworks.selenium.*;
import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

public class ESBManageLocalEntriesTest extends TestCase {
    Selenium selenium;

    public ESBManageLocalEntriesTest(Selenium _browser){
		selenium = _browser;
    }

    /*
    This method will add a Source URL Entry
     */
    public void addSourceUrlEntry(String name, String value) throws Exception{
		selenium.click("link=Local Entries");
        selenium.waitForPageToLoad("30000");

        boolean localEntry = selenium.isTextPresent(name);

        if (localEntry){
            //Do nothing
        }else {
            selenium.click("link=Add Source URL Entry");
            selenium.waitForPageToLoad("30000");
            selenium.type("Name", name);
            selenium.type("Value", value);
            selenium.click("//input[@value='Save']");
            selenium.waitForPageToLoad("30000");
        }
    }

    /*
    This method will add a Source URL Entry
     */
    public void addInlineXmlEntry(String name, String xmlString) throws Exception{
		selenium.click("link=Local Entries");
        selenium.waitForPageToLoad("30000");

        boolean localEntry = selenium.isTextPresent(name);

        if (localEntry){
            //Do nothing
        }else {
            selenium.click("link=Add In-lined XML Entry");
            selenium.waitForPageToLoad("30000");
            selenium.type("Name", name);
            selenium.type("entry_value", xmlString);
            selenium.click("//input[@value='Save']");
            selenium.waitForPageToLoad("30000");
        }
    }

    /*
    This method will add a Source URL Entry
     */
    public void addInlineTextEntry(String name, String value) throws Exception{
		selenium.click("link=Local Entries");
        selenium.waitForPageToLoad("30000");

        boolean localEntry = selenium.isTextPresent(name);

        if (localEntry){
            //Do nothing
        }else {
            selenium.click("link=Add In-lined Text Entry");
            selenium.waitForPageToLoad("30000");
            selenium.type("Name", name);
            selenium.type("Value", value);
            selenium.click("//input[@value='Save']");
            selenium.waitForPageToLoad("30000");
        }
    }

    /*
    Verifying whether the Source URL entry is successfully added
     */
    public void verifyAddedLocalEntry(String name, String localEntryType) throws Exception{
		selenium.click("link=Local Entries");
        selenium.waitForPageToLoad("30000");
        
        String readLocalEntry = "";
        String user = name;
        int i = 1;
        while (!user.equals(readLocalEntry)) {
            readLocalEntry = selenium.getText("//table[@id='myTable']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i - 1;
        assertEquals(name, selenium.getText("//table[@id='myTable']/tbody/tr[" + Integer.toString(i) + "]/td[1]"));
        assertEquals(localEntryType, selenium.getText("//table[@id='myTable']/tbody/tr[" + Integer.toString(i) + "]/td[2]"));
    }

    /*
    Verifying whether the values added when creating the local entry are persisted proeperly
     */
    public void verifySourceInlineTextValues(String name, String value) throws Exception{
		selenium.click("link=Local Entries");
        selenium.waitForPageToLoad("30000");

        boolean localEntry = selenium.isTextPresent(name);

        if (localEntry){
            String readLocalEntry = "";
            String user = name;
            int i = 1;
            while (!user.equals(readLocalEntry)) {
                readLocalEntry = selenium.getText("//table[@id='myTable']/tbody/tr[" + Integer.toString(i) + "]/td");
                i = i + 1;
            }
            i = i - 1;
            selenium.click("//table[@id='myTable']/tbody/tr[" + Integer.toString(i) + "]/td[3]/a[1]");
            selenium.waitForPageToLoad("30000");
            assertEquals(name, selenium.getValue("Name"));
            assertEquals(value, selenium.getValue("Value"));            
        } else {
            System.out.println("The local entry does not exist!!!");
        }
    }

    /*
    Verifying whether the values added when creating the local entry are persisted proeperly
     */
    public void verifyInlineXmlValues(String name, String value) throws Exception{
		selenium.click("link=Local Entries");
        selenium.waitForPageToLoad("30000");

        boolean localEntry = selenium.isTextPresent(name);

        if (localEntry){
            String readLocalEntry = "";
            String user = name;
            int i = 1;
            while (!user.equals(readLocalEntry)) {
                readLocalEntry = selenium.getText("//table[@id='myTable']/tbody/tr[" + Integer.toString(i) + "]/td");
                i = i + 1;
            }
            i = i - 1;
            selenium.click("//table[@id='myTable']/tbody/tr[" + Integer.toString(i) + "]/td[3]/a[1]");
            selenium.waitForPageToLoad("30000");
            assertEquals(name, selenium.getValue("Name"));
            assertEquals(value, selenium.getValue("entry_value"));
        } else {
            System.out.println("The local entry does not exist!!!");
        }
    }

    /*
    Method to edit Source URL Entry/Inline Text Entry
     */
    public void editSourceurlInlinetextEntry(String name, String value) throws Exception{
		selenium.click("link=Local Entries");
        selenium.waitForPageToLoad("30000");

        boolean localEntry = selenium.isTextPresent(name);

        if (localEntry){
            String readLocalEntry = "";
            String user = name;
            int i = 1;
            while (!user.equals(readLocalEntry)) {
                readLocalEntry = selenium.getText("//table[@id='myTable']/tbody/tr[" + Integer.toString(i) + "]/td");
                i = i + 1;
            }
            i = i - 1;
            selenium.click("//table[@id='myTable']/tbody/tr[" + Integer.toString(i) + "]/td[3]/a[1]");
            selenium.waitForPageToLoad("30000");

            //Specifying new values to verify editing of local entries
            selenium.type("Value", value);
            selenium.click("//input[@value='Save']");
            selenium.waitForPageToLoad("30000");
        } else {
            System.out.println("The local entry does not exist!!!");
        }
    }

    /*
    Method to edit Inline Xml Entry
     */
    public void editInlineXmlEntry(String name, String value) throws Exception{
		selenium.click("link=Local Entries");
        selenium.waitForPageToLoad("30000");

        boolean localEntry = selenium.isTextPresent(name);

        if (localEntry){
            String readLocalEntry = "";
            String user = name;
            int i = 1;
            while (!user.equals(readLocalEntry)) {
                readLocalEntry = selenium.getText("//table[@id='myTable']/tbody/tr[" + Integer.toString(i) + "]/td");
                i = i + 1;
            }
            i = i - 1;
            selenium.click("//table[@id='myTable']/tbody/tr[" + Integer.toString(i) + "]/td[3]/a[1]");
            selenium.waitForPageToLoad("30000");

            //Specifying new values to verify editing of local entries
            selenium.type("entry_value", value);
            selenium.click("//input[@value='Save']");
            selenium.waitForPageToLoad("30000");
        } else {
            System.out.println("The local entry does not exist!!!");
        }
    }

    /*
    Deleting Local Entries
     */
    public void deleteLocalEntry(String name) throws Exception{
		selenium.click("link=Local Entries");
        selenium.waitForPageToLoad("30000");

        boolean localEntry = selenium.isTextPresent(name);

        if (localEntry){
            String readLocalEntry = "";
            String user = name;
            int i = 1;
            while (!user.equals(readLocalEntry)) {
                readLocalEntry = selenium.getText("//table[@id='myTable']/tbody/tr[" + Integer.toString(i) + "]/td");
                i = i + 1;
            }
            i = i - 1;
            selenium.click("//table[@id='myTable']/tbody/tr[" + Integer.toString(i) + "]/td[3]/a[2]");
            assertTrue(selenium.isTextPresent("Do you want to delete the entry ?"));
            selenium.click("//button[@type='button']");
            selenium.waitForPageToLoad("30000");
        } else {
            System.out.println("The local entry does not exist!!!");
        }
    }

    /*
    Method to verify whether the entry has been deleted successfully
     */
    public void verifyLocalEntryDeletion(String name) throws Exception{
		selenium.click("link=Local Entries");
        selenium.waitForPageToLoad("30000");

        boolean entryExists = selenium.isTextPresent(name);
        if (entryExists){
            assertTrue(selenium.isTextPresent("The Local Entry Exists"));            
        }
    }

    /*
    Method to add local entries without value
     */
    public void addEntryWithoutValue(String type, String name) {
        selenium.click("link=Local Entries");
        selenium.waitForPageToLoad("30000");
        selectType(type);
        selenium.type("Name", name);
        selenium.click("//input[@value='Save']");
        assertTrue(selenium.isTextPresent("Value field cannot be empty"));
        selenium.click("//button[@type='button']");
    }


    /*
    Method used to find local entry in list
     */
    public void clickEditLocalEntry(String entryName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        int row_no=1;
        esbCommon.viewLocalEntries();
        Thread.sleep(2000);
        while (selenium.isElementPresent("//table[@id='myTable']/tbody/tr["+row_no+"]/td[1]")){
            if(selenium.getText("//table[@id='myTable']/tbody/tr["+row_no+"]/td[1]").equals(entryName)){
                selenium.click("//table[@id='myTable']/tbody/tr["+row_no+"]/td[3]/a[1]");
                break;
            }
            row_no++;
         }
    }

    public void addExistingEntry(String type, String name, String value) {
        selenium.click("link=Local Entries");
        selenium.waitForPageToLoad("30000");
        selectType(type);
        selenium.type("Name", name);
        selenium.type("Value", value);
        selenium.click("//input[@value='Save']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("An Entry with key " + name + " is already used within the configuration"));
        selenium.click("//button[@type='button']");
    }
    public void selectType(String type) {

        if (type == "source_url") {
             selenium.click("link=Add Source URL Entry");
        } else if (type == "inline_text") {
             selenium.click("link=Add In-lined Text Entry");
        } else if (type == "inline_xml") {
             selenium.click("link=Add In-lined XML Entry");
        }

        selenium.waitForPageToLoad("30000");
    }

}
