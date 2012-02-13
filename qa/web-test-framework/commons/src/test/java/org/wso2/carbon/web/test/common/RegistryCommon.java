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


import com.thoughtworks.selenium.*;
import junit.framework.TestCase;


import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RegistryCommon extends TestCase {
    Selenium selenium;
    String Curspeed;
    String NewSpeed;


    public RegistryCommon(Selenium _browser) {
        selenium = _browser;
        Curspeed = selenium.getSpeed();
        NewSpeed = "200";
    }

    public boolean rateResource(String resourcePath, int ratingValue, double expectedAverageRating)
            throws InterruptedException {

        boolean value = false;
        //selenium.click("//img[@onclick=\"setRating('/testRatingTable', 4)\"]");
        selenium.click("//img[@onclick=\"setRating(\'" + resourcePath + "\', " + ratingValue + ")\"]");
        assertTrue(selenium.isTextPresent("Ratings"));
        Thread.sleep(1000);
        String ratingSt = String.valueOf(ratingValue);
        String ratingAvg = String.valueOf(expectedAverageRating);
        if (selenium.isTextPresent(ratingSt) || selenium.isTextPresent(ratingAvg)) {
            value = true;
            return value;
        }
        return value;
    }

    public void addTextResourceToRoot(String resourcePath) throws InterruptedException {

        boolean value = false;

        selenium.click("link=Browse");
        selenium.waitForPageToLoad("50000");
        String resourceName = removeChar(resourcePath, '/');
        //assertTrue(selenium.isTextPresent("Resources"));
        assertTrue(selenium.isTextPresent("Browse"));
        selenium.click("link=Add Resource");
        assertTrue(selenium.isTextPresent("Add Resource"));
        Thread.sleep(1000);
        selenium.select("addMethodSelector", "label=Text content");
        selenium.click("//option[@value='text']");
        selenium.type("trFileName", resourceName);
        selenium.type("trDescription", "resourceNameDesc");
        selenium.click("//input[@value='Add' and @type='button' and @onclick='whileUpload();submitTextContentForm();']");
        Thread.sleep(2000);
        resourcetextContentMsg();
//        assertTrue("resource doesn't exists at the registry", resourceExists(resourceName));
    }

    public void addTextResource(String resourcePath) throws InterruptedException {

        boolean value = false;
        String resourceName = removeChar(resourcePath, '/');
        assertTrue(selenium.isTextPresent("Browse"));
        selenium.click("link=Add Resource");
        assertTrue(selenium.isTextPresent("Add Resource"));
        Thread.sleep(1000);
        selenium.select("addMethodSelector", "label=Text content");
        selenium.click("//option[@value='text']");
        selenium.type("trFileName", resourceName);
        selenium.type("trDescription", "resourceNameDesc");
        selenium.type("trPlainContent", "ContentContent");
        selenium.click("//input[@value='Add' and @type='button' and @onclick='whileUpload();submitTextContentForm();']");
        resourcetextContentMsg();
        Thread.sleep(1000);
        assertTrue("resource doesn't exists at the registry", resourceExists(resourceName));
    }

    public void addTextResourceWithContent(String resourcePath, String content, String description) throws InterruptedException {

        boolean value = false;
        String resourceName = removeChar(resourcePath, '/');
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Browse"));
        selenium.click("link=Add Resource");
        selenium.select("addMethodSelector", "label=Text content");
        selenium.click("//option[@value='text']");
        selenium.type("trFileName", resourceName);
        selenium.type("trDescription", description);
        selenium.click("//input[@name='richText' and @value='plain']");
        selenium.type("trPlainContent", content);
        selenium.click("//input[@value='Add' and @type='button' and @onclick='whileUpload();submitTextContentForm();']");
        resourcetextContentMsg();
        Thread.sleep(1000);
        assertTrue("resource doesn't exists at the registry", resourceExists(resourceName));
    }

    public void addLinkResource(String resourcePath, String url) throws InterruptedException {

        boolean value = false;
        String resourceName = removeChar(resourcePath, '/');
        assertTrue(selenium.isTextPresent("Browse"));
        selenium.click("link=Add Resource");
        assertTrue(selenium.isTextPresent("Add Resource"));


        selenium.select("addMethodSelector", "label=Text content");
        selenium.click("//option[@value='text']");
        selenium.type("trFileName", resourcePath);
        selenium.type("trDescription", "Extenal Link description");
        selenium.click("//input[@name='richText' and @value='plain']");
        selenium.type("trPlainContent", url);
        selenium.type("trMediaType", "");
        selenium.type("trMediaType", "application/vnd.external");
        selenium.click("//input[@value='Add' and @type='button' and @onclick='whileUpload();submitTextContentForm();']");
        //selenium.click("//button[@type='button']");

        resourcetextContentMsg();
        Thread.sleep(1000);
        assertTrue("resource doesn't exists at the registry", resourceExists(resourceName));
    }

    public boolean deleteResource(String resourcePath, int ActionLinkId) throws InterruptedException {

        boolean value = false;
        String resourceName = removeChar(resourcePath, '/');
        Thread.sleep(1000);
        selenium.click("actionLink" + ActionLinkId);
        Thread.sleep(1000);
        selenium.click("//a[@onclick=\"hideOthers(" + ActionLinkId + ",'del');deleteResource(\'" +
                resourcePath + "\', '/')\"]");

//        selenium.click("//a[@onclick=\"hideOthers(1,'del');deleteResource('/symlinkName', '/')\"]");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Are you sure you want to delete"));
        selenium.click("//button[@type='button']");

        Thread.sleep(1000);
        if ((selenium.isTextPresent(resourceName))) {
            value = false;
            return value;
        } else {
            value = true;
        }
        return value;
    }

    public boolean deleteCollection(String resourcePath, int ActionLinkId) throws InterruptedException {

        boolean value = false;

        String resourceName = removeChar(resourcePath, '/');
        resourceName = "/" + resourceName;
        System.out.println(resourceName);

        gotoResourcePage();

        if (ActionLinkId == 1) {
            selenium.click("actionLink1");
            selenium.click("link=Delete");
            selenium.click("//button[@type='button']");
        } else {
            Thread.sleep(1000);
            selenium.click("actionLink" + ActionLinkId);
            Thread.sleep(1000);
            selenium.click("//a[@onclick=\"hideOthers(" + ActionLinkId + ",'del');deleteResource(\'" + resourceName + "\', '/');\"]");
            assertTrue(selenium.isTextPresent("Are you sure you want to delete"));
            Thread.sleep(1000);
            selenium.click("//button[@type='button']");
        }

        Thread.sleep(1000);
        if ((selenium.isTextPresent(resourceName))) {
            value = false;
            return value;
        } else {
            value = true;
        }
        return value;
    }

    public boolean clickRootIcon() {

        boolean value = false;

        selenium.click("//div[@id='workArea']/div/table/tbody/tr/td[1]/a/img");
        selenium.waitForPageToLoad("120000");
        if ((selenium.isTextPresent("root"))) {
            value = true;
            return value;
        }
        return value;
    }

    public boolean addProperty(String key, String protValue) {

        boolean value = false;
        selenium.click("propertiesIconMinimized");
        selenium.click("link=Add New Property");
        selenium.type("propName", key);
        selenium.type("propValue", protValue);
        selenium.click("//input[@value='Add']");
        if (selenium.isTextPresent(key) && (selenium.isTextPresent(protValue))) {
            value = true;
            return value;
        }
        return value;
    }

    public boolean editProperty(String key, String protValue, int panelPlace) throws InterruptedException {

        boolean value = false;
        selenium.click("propertiesIconMinimized");
        assertTrue(selenium.isTextPresent("Edit"));
        selenium.click("//a[@onclick=\"showHideCommon('propViewPanel_" + panelPlace + "');" +
                "showHideCommon('propEditPanel_" + panelPlace + "');$('propName_" + panelPlace + "').focus();\"]");
        Thread.sleep(1000);
        selenium.type("propName_0", key);
        selenium.type("propValue_0", protValue);
        Thread.sleep(1000);
        selenium.click("propSaveButton_0");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(key));
        assertTrue(selenium.isTextPresent(protValue));
        assertTrue(selenium.isTextPresent("Edit"));
        Thread.sleep(1000);
        if (selenium.isTextPresent(key) && (selenium.isTextPresent(protValue))) {
            value = true;
            return value;
        }
        return value;
    }

    public boolean deleteProperty(String protValue, String key) throws InterruptedException {

        boolean value;

        selenium.click("link=Delete");
        assertTrue(selenium.isTextPresent("Are you sure you want to delete"));
        selenium.click("//button[@type='button']");
        selenium.click("//div[@id='workArea']/div/table/tbody/tr/td[1]/a/img");
        Thread.sleep(1000);

        if (selenium.isTextPresent(key) && (selenium.isTextPresent(protValue))) {
            value = false;
            return value;
        } else {
            value = true;
        }
        return value;
    }

    public boolean addCollectionToRoot(String collectionName, String mediaType, String colDescription) throws InterruptedException {

        boolean value;
        Thread.sleep(2000);
        selenium.click("link=Add Collection");
        selenium.type("collectionName", collectionName);
        selenium.type("mediaType", mediaType);
        selenium.type("colDesc", colDescription);
        Thread.sleep(2000);
        selenium.click("//input[@value='Add' and @type='button' and @onclick='submitCollectionAddForm()']");
        Thread.sleep(1000);
        collectionCreateMsg();

        if (resourceExists(collectionName)) {
            value = false;
        } else {
            value = true;
            return value;
        }
        return value;
    }

    public boolean verifyPropertyCount(int count, String label) throws InterruptedException {

        boolean value;

        selenium.click("propertiesIconMinimized");
        Thread.sleep(1000);
        String propertyCount = selenium.getText("propertiesSum");
        if (count > 1) {
            assertTrue(propertyCount.equalsIgnoreCase(count + " " + label));
            value = true;
            return value;
        } else {
            assertTrue(propertyCount.equalsIgnoreCase(count + " " + label));
            value = false;
        }
        return value;

    }

    public boolean gotoResourcePage() {
        boolean value;

        selenium.click("link=Browse");
        selenium.waitForPageToLoad("30000");

        if (selenium.isTextPresent("root") && (selenium.isTextPresent("Browse"))) {
            value = false;
        } else {
            value = true;
            return value;
        }
        return value;
    }

    public boolean searchProproties(String key, String value, String resourcePath, String userName)
            throws InterruptedException {

        boolean _value = false;

        selenium.click("link=Search");
        selenium.waitForPageToLoad("30000");
        selenium.type("#_propertyName", key);
        selenium.type("#_propertyValue", value);
        selenium.click("#_0");
        //String resourceName = removeChar(resourcePath, '/');
        Thread.sleep(1000);
        if ((selenium.isTextPresent(resourcePath)) && (selenium.isTextPresent("Search Results") &&
                (selenium.isTextPresent(userName)))) {
            Thread.sleep(1000);
            _value = true;
            return _value;
        }
        return _value;
    }

    public boolean addTags(String tagName, String tagId) throws InterruptedException {

        boolean value = false;

        selenium.click("tagsIconMinimized");
        selenium.click("link=Add New Tag");
        selenium.type("id=" + tagId, tagName);
        selenium.focus("id=" + tagId);
        Thread.sleep(1000);
        selenium.keyPressNative("" + KeyEvent.VK_ENTER);
        Thread.sleep(1000);

        if (selenium.isTextPresent(tagName) && (selenium.isTextPresent("Tags"))) {
            return value = true;
        }
        return value;
    }

    public boolean tagLinkNavigation(String tagName, int tagCount, String resourceName) throws InterruptedException {

        boolean value = false;

        selenium.click("link=" + tagName);
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(tagName + " " + "(" + tagCount + ")"));
        assertTrue(selenium.isTextPresent("/"));
        if (selenium.isTextPresent(tagName) && (selenium.isTextPresent("Tags")) && selenium.isTextPresent(resourceName)) {
            return value = true;
        }
        return value;
    }

    public boolean basicTagSearch(String resourcePath, String tagValue) throws InterruptedException {

        boolean value = false;

        selenium.type("criteria", tagValue);
        selenium.click("//input[@value='Search']");
        selenium.waitForPageToLoad("30000");
        Thread.sleep(1000);
        if ((selenium.isTextPresent(tagValue + " (1)") && selenium.isTextPresent(resourcePath))) {
            value = true;
            return value;
        }
        return value;
    }

    public boolean addComment(String commentValue, String commentedByUser, String resourceName) throws InterruptedException {

        boolean value = false;

        selenium.click("commentsIconMinimized");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Add Comment"));
        Thread.sleep(1000);
        selenium.click("link=Add Comment");
        selenium.type("comment", commentValue);
        selenium.click("//input[@value='Add' and @type='button' and @onclick=\"addComment(\'" + resourceName + "\');\"]");
        Thread.sleep(1000);

        if (selenium.isTextPresent(commentValue) && selenium.isTextPresent(commentedByUser)) {
            value = true;
            return value;
        }

        return value;
    }

    public boolean searchSimpleCommet(String path, String comment) throws Exception {

        boolean value = false;
        selenium.click("link=Search");
        selenium.waitForPageToLoad("30000");
        selenium.type("#_comments", comment);
        Thread.sleep(1000);
        selenium.click("#_0");
        Thread.sleep(1000);

        if ((selenium.isTextPresent("Search Results") && selenium.isTextPresent(path) && selenium.isTextPresent("Search Again"))) {
            value = true;
        }

        return value;
    }

    public boolean searchSimpleTag(String path, String tag) throws Exception {

        boolean value = false;
        selenium.click("link=Search");
        selenium.waitForPageToLoad("30000");
        selenium.type("#_tags", tag);
        selenium.click("#_0");
        Thread.sleep(1000);

        if ((selenium.isTextPresent("Search Results") && selenium.isTextPresent(path) && selenium.isTextPresent("Search Again"))) {
            value = true;
        }

        return value;
    }

    public void addtResourceFromFileSystem(String path, String resourceName) throws Exception {

        selenium.click("link=Add Resource");
        selenium.type("uResourceFile", path);
        Thread.sleep(10000);
        selenium.type("uResourceName", resourceName);
        selenium.click("//input[@value='Add' and @type='button' and @onclick='whileUpload();submitUploadContentForm();']");
        selenium.waitForPageToLoad("150000");
        resourceUploadMsg();
        assertTrue("resource doesn't exists at the registry", resourceExists(resourceName));
    }

    public void addtResourceFromUrl(String resourceUrl, String resourceName, int rowCount) throws Exception {

        boolean value = false;

        selenium.click("link=Add Resource");
        selenium.select("addMethodSelector", "label=Import content from URL");
        selenium.type("irFetchURL", resourceUrl);
        selenium.type("irDescription", "test");
        selenium.click("//input[@value='Add' and @type='button' and @onclick='whileUpload();submitImportContentForm();']");
        Thread.sleep(10000);
        resourceUploadUrlMsg();
        Thread.sleep(1000);
        resourceExists(resourceName);
    }

    public boolean searchResources(String path) throws Exception {

        boolean value = false;
        String resourceName = removeChar(path, '/');

        selenium.click("link=Search");
        selenium.waitForPageToLoad("30000");
        selenium.type("#_resourceName", resourceName);
        Thread.sleep(1000);
        selenium.click("#_0");
        Thread.sleep(1000);

        if ((selenium.isTextPresent("Search Results") && selenium.isTextPresent(path) && selenium.isTextPresent
                ("Search Again"))) {
            value = true;
        }
        return value;
    }

    public void importWsldMexTool(String mediatype, String wsdlUrl, String wsdlName, String owner,
                                  String telephone, String email, String wsdlDescription) throws InterruptedException {

        selenium.click("link=Add Resource");
        Thread.sleep(1000);
        selenium.select("addMethodSelector", "label=Create custom content");
        assertTrue(selenium.isTextPresent("Create Custom Content"));
        assertEquals("Create Content", selenium.getValue("//input[@value='Create Content']"));
        selenium.type("customMediaTypeID", mediatype);
        selenium.click("//input[@value='Create Content']");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("WSDL Service Import Tool"));
        assertTrue(selenium.isTextPresent("WSDL URL*"));
        assertTrue(selenium.isTextPresent("Owner"));
        assertTrue(selenium.isTextPresent("Owner e-mail"));
        assertTrue(selenium.isTextPresent("Owner telephone"));
        assertTrue(selenium.isTextPresent("Description"));
        Thread.sleep(1000);
        selenium.type("wsdlURL", wsdlUrl);
        selenium.type("ownerName", owner);
        selenium.type("ownerEmail", email);
        selenium.type("ownerTelephone", telephone);
        selenium.type("description", wsdlDescription);
        Thread.sleep(1000);
        selenium.click("//input[@value='Import Service Details']");
        selenium.waitForPageToLoad("30000");
        System.out.println("WSDL Name :" + wsdlName);

        assertTrue("resource doesn't exists at the registry", resourceExists(wsdlName + "Service"));

    }

    public String removeChar(String s, char c) {

        String r = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) r += s.charAt(i);
        }
        return r;
    }

    public String removeTruncateCharacter(String s) {

        int index1 = s.indexOf("..");
        String r = s;
        if (index1 != -1) {
            int beginChar = s.length() - 2;
            r = s.substring(0, beginChar);
        }
        return r.trim();
    }


    public String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);

    }

    public boolean resourceExists(String resourceName) throws InterruptedException {

        boolean value = false;
        resourceName = removeChar(resourceName, '/');
        String readResource = "";
        int i = 1;

        while (selenium.isElementPresent("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]")) {
            Thread.sleep(1000);
            if (!readResource.equals(resourceName)) {

                readResource = selenium.getTable("//td[@id='actionPaneHelper" + i + "']/table.0.0");
                System.out.println("Test Read resoruce: " + readResource);
                readResource = removeTruncateCharacter(readResource);
                System.out.println(readResource);
                System.out.println(resourceName);
                int index1 = resourceName.indexOf(readResource);

                if (index1 != -1) {
                    value = true;
                    break;
                } else {
                    i = i + 1;
                    continue;
                }
            }
            assertFalse("Resource can not be found in registry browser", value);
            break;
        }
        return value;
    }

    public int getActionID(String resourceName) throws InterruptedException {

        int actionID = 0;
        boolean value = false;
        resourceName = removeChar(resourceName, '/');
        String readResource = "";
        int i = 1;

        while (!readResource.equals(resourceName)) {

            if (selenium.isElementPresent("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]")) {

                Thread.sleep(1000);
                readResource = selenium.getTable("//td[@id='actionPaneHelper" + i + "']/table.0.0");
                System.out.println("Test Read resoruce: " + readResource);
                readResource = removeTruncateCharacter(readResource);
                System.out.println(readResource);
                System.out.println(resourceName);
                int index1 = resourceName.indexOf(readResource);

                if (index1 != -1) {
                    actionID = i;
                    value = true;
                    break;
                } else {
                    i = i + 1;
                    continue;
                }
            }
            assertTrue("Resource can not be found in registry browser", value);
            break;
        }
        return actionID;
    }


    public int getEditedActionID(String resourceName) throws InterruptedException {

        int actionID = 0;
        boolean value = false;

        String readResource = "";

        int i = 1;

        while (!readResource.equals(resourceName)) {

            Thread.sleep(1000);
            readResource = selenium.getTable("//td[@id='actionPaneHelper" + i + "']/table.0.1");
            System.out.println("Test Read resoruce" + readResource);
            readResource = removeTruncateCharacter(readResource);
            System.out.println(readResource);
            System.out.println(resourceName);
            int index1 = resourceName.indexOf(readResource);

            if (index1 != -1) {
                actionID = i;
                value = true;
                break;
            }

            int a = i + 1;
            if (selenium.getTable("//td[@id='actionPaneHelper" + a + "']/table.0.1") != null) {
                i = i + 1;
            } else {
                actionID = 0;
                assertTrue("Resource can not be found in registry browser", value);
                break;
            }

        }
        return actionID;
    }

    public void gotoResource(String resourceName) throws InterruptedException {

        int actionID = getActionID(resourceName);

        selenium.click("resourceView" + actionID);
        selenium.waitForPageToLoad("30000");
    }

    public void createCheckpoint() throws InterruptedException {

        selenium.click("//a[contains(text(),'Create\n                                Checkpoint')]");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Check point created successfully"));
        selenium.click("//button[@type='button']");
    }

    public void checkResourceVersions(String resourceName, String userName) {

        selenium.click("link=View versions");
        selenium.waitForPageToLoad("30000");

        assertTrue(selenium.isTextPresent(resourceName));
        assertTrue(selenium.isTextPresent("Version"));
        assertTrue(selenium.isTextPresent("Restore"));
        assertTrue(selenium.isTextPresent("Details"));
        assertTrue(selenium.isTextPresent(userName));

        String actionscolumn = selenium.getText("//table[@id='versionsTable']/thead/tr/th[4]");
        System.out.println("action Column: " + actionscolumn);
    }

    public void checkCollectionVersions(String resourceName, String userName) {

        selenium.click("link=View versions");
        selenium.waitForPageToLoad("30000");

        assertTrue(selenium.isTextPresent(resourceName));
//        assertTrue(selenium.isTextPresent("No versions available for this resource or collection."));
        //No versions available for this resource or collection.
        String actionscolumn = selenium.getText("//table[@id='versionsTable']/thead/tr/th[4]");
        System.out.println("action Column: " + actionscolumn);
    }

    public void signOut() throws InterruptedException, IOException {

       if (selenium.isElementPresent("link=Sign-out") || selenium.isTextPresent("@")) {

            selenium.click("link=Sign-out");
            selenium.waitForPageToLoad("30000");
            for (int second = 0; ; second++) {
                if (second >= 120) fail("Signout fail due to timeout");
                try {
                    if (selenium.isTextPresent("Sign-in")) break;
                } catch (Exception e) {
                }
                Thread.sleep(1000);
            }

        }
    }



    public void copyResource(int actionId, String resourcePath, String destinationPath) throws InterruptedException {

        if (actionId == 1) {
            selenium.click("actionLink" + actionId);
            selenium.click("link=Copy");
            selenium.type("copy_destination_path" + actionId, destinationPath);
            selenium.click("//input[@value='Copy']");
        } else {

            selenium.click("actionLink" + actionId);
            selenium.click("//a[@onclick=\"showHideCommon('copy_panel" + actionId + "');hideOthers(" + actionId + ",'copy');\"]");
            Thread.sleep(1000);
            selenium.type("copy_destination_path" + actionId, destinationPath);
            Thread.sleep(1000);
            String resourceNewPath = removeChar(resourcePath, '/');
            selenium.click("//input[@value='Copy' and @type='button' and @onclick=\"this.disabled = true; copyResource('/', '" + resourcePath + "'," +
                    "'copy_destination_path" + actionId + "','" + resourceNewPath + "',1); this.disabled = false;\"]");
            Thread.sleep(1000);
        }
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("messagebox-info")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//button[@type='button']");
    }

    public void moveResource
            (
                    int actionId, String
                    resourcePath, String
                    destinationPath) throws InterruptedException {

        if (actionId == 1) {
            selenium.click("actionLink" + actionId);
            selenium.click("link=Move");
            selenium.type("move_destination_path" + actionId, destinationPath);
            selenium.click("//input[@value='Move']");

        } else {

            selenium.click("actionLink" + actionId);
            selenium.click("//a[@onclick=\"showHideCommon('move_panel" + actionId + "');hideOthers(" + actionId + ",'move');\"]");
            Thread.sleep(1000);
            selenium.type("move_destination_path" + actionId, destinationPath);
            Thread.sleep(1000);
            String resourceNewPath = removeChar(resourcePath, '/');
            selenium.click("//input[@value='Move' and @type='button' and @onclick=\"this.disabled = true; moveResource('/', '" + resourcePath + "'," +
                    "'move_destination_path" + actionId + "','" + resourceNewPath + "',1); this.disabled = false;\"]");
        }
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("//button[@type='button']")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//button[@type='button']");

    }

    public void editResource
            (
                    int actionId, String
                    path, String
                    newResourceName) throws InterruptedException {

        selenium.click("actionLink" + actionId);

        selenium.click("//a[@onclick=\"javascript:showHideCommon('rename_panel" + actionId + "');hideOthers(" +
                actionId + ",'rename');if($('rename_panel" + actionId + "').style.display!='none')$('resourceEdit"
                + actionId + "').focus();\"]");

        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Editing resource name"));
        assertTrue(selenium.isTextPresent("New Resource Name *"));
        System.out.println(actionId);
        System.out.println("resourceEdit" + actionId);
//            selenium.click("//a[@onclick=\"javascript:showHideCommon('rename_panel3');hideOthers(3,'rename');if($('rename_panel3').style.display!='none')$('resourceEdit3').focus();\"]");
        selenium.type("resourceEdit" + actionId, newResourceName);
        Thread.sleep(1000);
        selenium.click("//input[@value='Rename']");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("//button[@type='button']")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//button[@type='button']");
/*
        if (actionId==1)
            selenium.click("//input[@value='Rename']");
        else
            selenium.click("//input[@value='Rename' and @type='button' and @onclick=\"this.disabled = true; renameResource('"+path+"', '"+path+newResourceName+"', 'resourceEdit"+actionId+"',1, 'resource'); this.disabled = false;\"]");
        Thread.sleep(1000);
        selenium.click("//button[@type='button']");*/

//        selenium.open("/carbon/resources/resource.jsp?region=region3&item=resource_browser_menu");
    }

    public void editCollection
            (
                    int actionId, String
                    destinationPath, String
                    resourceName) throws InterruptedException {

        selenium.click("actionLink" + actionId);
        selenium.click("//a[@onclick=\"javascript:showHideCommon('rename_panel" + actionId + "');hideOthers(" +
                actionId + ",'rename');if($('rename_panel" + actionId + "').style.display!='none')$('resourceEdit"
                + actionId + "').focus();\"]");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Editing collection name"));
        assertTrue(selenium.isTextPresent("New Collection Name *"));
        System.out.println(actionId);
        System.out.println("resourceEdit" + actionId);
        //selenium.click("//a[@onclick=\"javascript:showHideCommon('rename_panel3');hideOthers(3,'rename');if($('rename_panel3').style.display!='none')$('resourceEdit3').focus();\"]");
        selenium.type("resourceEdit" + actionId, destinationPath);
        Thread.sleep(1000);
        selenium.click("//input[@value='Rename']");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("//button[@type='button']")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//button[@type='button']");
//        selenium.click("//input[@value='Rename' and @type='button' and @onclick=\"renameResource('/', '" + resourceName + "', " +
//        "'resourceEdit" + actionId + "',1, 'collection');\"]");
    }

    public void resourceUploadMsg
            () {
        setWindowfocus();
        assertTrue(selenium.isTextPresent("Successfully uploaded content."));
        selenium.click("//button[@type='button']");
    }

    public void resourceUploadUrlMsg
            () throws InterruptedException {
        setWindowfocus();
        assertTrue(selenium.isTextPresent("Successfully uploaded the resource from the given URL."));
        Thread.sleep(1000);
        selenium.click("//button[@type='button']");
    }

    public void resourcetextContentMsg
            () throws InterruptedException {
        setWindowfocus();
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Successfully added Text content."));
        Thread.sleep(1000);
        selenium.click("//button[@type='button']");
    }

    public void collectionCreateMsg
            () throws InterruptedException {
        setWindowfocus();
        assertTrue(selenium.isTextPresent("Successfully added new collection."));
        Thread.sleep(1000);
        selenium.click("//button[@type='button']");
    }

    public void symlinkCreateMsg
            () throws InterruptedException {
        setWindowfocus();
        assertTrue(selenium.isTextPresent("Successfully created Symbolic Link."));
        Thread.sleep(1000);
        selenium.click("//button[@type='button']");
    }

    public void viewVersions
            () {
        setWindowfocus();
        selenium.click("link=View versions");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Restore"));

    }

    public boolean gotoServicePage
            () throws InterruptedException {
        boolean value = false;

        selenium.click("link=Service");
        selenium.waitForPageToLoad("30000");

        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isTextPresent("Service Operations")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        return value;

    }

    public boolean gotoPolicyPage
            () throws InterruptedException {
        boolean value = false;

        selenium.click("link=Policy");
        selenium.waitForPageToLoad("30000");


        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isTextPresent("Policy Operations")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        return value;

    }

    public boolean gotoWSDLPage
            () throws InterruptedException {
        //   boolean value = false;

        selenium.click("link=WSDL");
        //selenium.waitForPageToLoad("30000");
        //  Thread.sleep(1000);
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isTextPresent("WSDL Operations")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        return true;

    }

    public boolean gotoSchemaPage
            () throws InterruptedException {
        boolean value = false;

        selenium.click("link=Schema");
        selenium.waitForPageToLoad("30000");

        if (selenium.isTextPresent("Schema Operations"))
            value = false;
        else
            value = true;


        return value;

    }

    public boolean gotoServicesPage
            () throws InterruptedException {
        boolean value = false;
        Thread.sleep(1000);
        selenium.click("link=Services");
        selenium.waitForPageToLoad("120000");
        if (selenium.isTextPresent("Service List"))
            value = false;
        else
            value = true;
        return value;

    }

    public boolean gotoPoliciesPage
            () throws InterruptedException {
        boolean value = false;
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("Policies".equals(selenium.getText("link=Policies"))) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("link=Policies");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("Policy List".equals(selenium.getText("//div[@id='middle']/h2"))) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        Thread.sleep(1000);
        if (selenium.isTextPresent("Policy List"))
            value = false;
        else
            value = true;

        return value;

    }

    public boolean gotoWSDLsPage
            () throws InterruptedException {
        boolean value = false;

        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("link=WSDLs")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        selenium.click("link=WSDLs");

        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("WSDL List".equals(selenium.getText("//div[@id='middle']/h2"))) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        Thread.sleep(2000);

        if (selenium.isTextPresent("WSDL List"))
            value = false;
        else
            value = true;


        return value;

    }

    public boolean gotoSchemasPage
            () throws InterruptedException {
        boolean value = false;

        selenium.click("link=Schemas");
        selenium.waitForPageToLoad("120000");

        if (selenium.isTextPresent("SService List"))
            value = false;
        else
            value = true;


        return value;

    }

    public boolean gotoMyprofile
            () throws InterruptedException {
        boolean value = false;
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("link=My Profiles")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        Thread.sleep(1000);
        selenium.click("link=My Profiles");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("My Profiles".equals(selenium.getText("//div[@id='middle']/h2"))) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        Thread.sleep(1000);
        if (selenium.isTextPresent("My Profiles"))
            value = false;
        else
            value = true;


        return value;

    }

    public void createSymlinktoRootResource
            (String
                    symResourceName, String
                    resourceName) throws InterruptedException {

        String description = "symlinkDescription";

        selenium.click("link=Add Resource");
        selenium.select("addMethodSelector", "label=Add Symbolic Link");
        String resourceNameSym = removeChar(symResourceName, '/');
        selenium.type("srFileName", resourceNameSym);
        selenium.click("//input[@value='..']");
        Thread.sleep(1000);
        selenium.click("plus_root");
        Thread.sleep(1000);

        selenium.click("//a[@onclick=\"pickPath('" + resourceName + "','srPath');\"]");
        Thread.sleep(1000);
        selenium.click("//input[@value='OK']");
        selenium.click("//input[@value='Add' and @type='button' and @onclick='whileUpload();submitSymlinkContentForm();']");
        Thread.sleep(1000);
        //symlinkCreateMsg();
        selenium.click("//button[@type='button']");
    }

    public boolean checkProperties
            (String
                    key, String
                    propertyValue) throws InterruptedException {

        boolean value = false;

        selenium.click("propertiesIconMinimized");
        Thread.sleep(1000);

        if (selenium.isTextPresent(key) && (selenium.isTextPresent(propertyValue))) {
            value = true;
            return value;
        }
        return value;
    }

    public boolean checkComments
            (String
                    commentValue, String
                    commentedByUser) throws InterruptedException {

        boolean value = false;

        selenium.click("commentsIconMinimized");
        Thread.sleep(1000);

        if (selenium.isTextPresent(commentValue) && selenium.isTextPresent(commentedByUser)) {
            value = true;
            return value;
        }
        return value;
    }

    public boolean checkTags
            (String
                    tagValue) throws InterruptedException {

        boolean value = false;

        selenium.click("tagsIconMinimized");
        Thread.sleep(1000);

        if (selenium.isTextPresent(tagValue) && (selenium.isTextPresent("Tags"))) {
            value = true;
            return value;
        }
        return value;
    }

    public boolean checkRating
            (
                    int ratingValue,
                    double averageRating) throws InterruptedException {

        boolean value = false;

        Thread.sleep(1000);
        String ratingSt = String.valueOf(ratingValue);
        String ratingAvg = String.valueOf(averageRating);
        if (selenium.isTextPresent(ratingSt) || selenium.isTextPresent(ratingAvg)) {
            value = true;
            return value;
        }
        return value;
    }

    public void addAssociationResource
            (String
                    assoType, String
                    resourceName) throws InterruptedException {

        boolean value = false;

        selenium.click("associationsIconMinimized");
        selenium.click("//a[contains(text(),'Add\n                Association')]");
        selenium.type("type", assoType);
        selenium.click("//input[@value='..' and @type='button' and @onclick=\"showResourceTree('associationPaths');\"]");
        Thread.sleep(1000);
        setWindowfocus();
        selenium.click("plus_root");
        Thread.sleep(1000);

        selenium.click("//a[@onclick=\"pickPath('" + resourceName + "','associationPaths');\"]");
        Thread.sleep(1000);
        selenium.click("//input[@value='OK']");
        Thread.sleep(1000);
        selenium.click("//input[@value='Add' and @type='button' and @onclick=\"addAssociation('assoForm');\"]");
        Thread.sleep(1000);

        String truncatedResourceName = resourceName.substring(0, 10);
        truncatedResourceName = truncatedResourceName + "..";
        System.out.println("Truncated" + truncatedResourceName);


        if ((selenium.isTextPresent(assoType) && (selenium.isTextPresent(truncatedResourceName)) || (selenium.isTextPresent(resourceName)))) {
            value = true;
        }
        selenium.click("associationsIconExpanded");
        assertTrue("Association couldn't be added to the given resource", value);
    }

    public void navigateAssociationUrls
            (String
                    resourceName, String
                    verifyText) throws InterruptedException {

        boolean value = false;

        selenium.click("associationsIconMinimized");

        String readResource = "";

        int i = 0;

        while (!readResource.equals(resourceName)) {

            Thread.sleep(1000);
            readResource = selenium.getTable("//div[@id='associationsList']/table." + i + ".0");
            System.out.println("Test Read resoruce" + readResource);
            readResource = removeTruncateCharacter(readResource);
            System.out.println(readResource);
            System.out.println(resourceName);
            int index1 = resourceName.indexOf(readResource);

            if (index1 != -1) {
                value = true;
                break;
            }

            i = i + 1;

            if (i >= 10) {
                break;
            }
            selenium.click("associationsIconExpanded");
        }

        if (value) {
            assertTrue(selenium.isTextPresent(readResource));
            readResource = readResource + "..";
            selenium.click("link=" + readResource);
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium.isTextPresent(verifyText));
        }
    }

    public void navigateAssociationLinks
            (String
                    resourceName) throws InterruptedException {

        boolean value = false;

        selenium.click("associationsIconMinimized");

        String readResource = "";
        String readResource2 = "";

        int i = 1;

        while (!readResource.equals(resourceName)) {

            Thread.sleep(1000);
            readResource = selenium.getTable("//div[@id='associationsList']/table." + i + ".0");
            System.out.println("Test Read resoruce" + readResource);
            readResource2 = removeTruncateCharacter(readResource);
            System.out.println(readResource);
            System.out.println(resourceName);
            int index1 = resourceName.indexOf(readResource2);

            if (index1 != -1) {
                value = true;
                break;
            }

            i = i + 1;

            if (i >= 10) {
                break;
            }

        }

        if (value) {
            if (readResource.contains("..")) {
//                assertTrue(selenium.isTextPresent(readResource));
                selenium.click("link=" + readResource);
                selenium.waitForPageToLoad("30000");
                assertTrue(selenium.isTextPresent(resourceName));
            } else {
//                assertTrue(selenium.isTextPresent(resourceName));
                selenium.click("link=" + resourceName);
                selenium.waitForPageToLoad("30000");
                assertTrue(selenium.isTextPresent(resourceName));
            }
        }
        selenium.click("associationsIconExpanded");
    }

    public void clickAssociationTree
            () throws InterruptedException {

        selenium.click("associationsIconMinimized");
        selenium.click("link=Association Tree");
        setWindowfocus();
        Thread.sleep(1000);
    }

    public boolean checkAssociationTree
            (String
                    resourceName) throws InterruptedException {

        boolean value = false;
        if (selenium.isTextPresent(resourceName)) {
            value = true;

        }
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        return value;
    }

    public boolean checkAssociationTable
            (String
                    resourceName) throws InterruptedException {

        selenium.click("associationsIconMinimized");
        boolean value = false;

        String readResource = "";

        int i = 1;

        while (!readResource.equals(resourceName)) {

            if (selenium.isElementPresent("//div[@id='associationsList']/table/tbody/tr[" + i + "]/td[1]")) {

                Thread.sleep(1000);
                readResource = selenium.getTable("//div[@id='associationsList']/table." + i + ".0");
                System.out.println("Test Read resoruce" + readResource);
                readResource = removeTruncateCharacter(readResource);
                System.out.println(readResource);
                System.out.println(resourceName);
                int index1 = resourceName.indexOf(readResource);

                if (index1 != -1) {
                    value = true;
                    break;
                } else {
                    i = i + 1;
                    continue;
                }
            }
            break;
        }
        selenium.click("associationsIconExpanded");
        return value;
    }

    public void clickAssocitionTreelinks
            (String
                    resourceName) throws InterruptedException {

        if (selenium.isTextPresent(resourceName)) {
            String linkstore = selenium.getText("link=" + resourceName);
            System.out.println("The Link store :" + linkstore);
            System.out.println("inside if");
            Thread.sleep(1000);
            selenium.click("link=" + resourceName);
            selenium.waitForPageToLoad("30000");
        } else {
            assertFalse("Resource name not found", false);
        }
    }

    public void addExtenalLinkAssociation
            (String
                    url, String
                    type) throws InterruptedException {

        boolean value = false;
        selenium.click("associationsIconMinimized");
        selenium.click("//a[contains(text(),'Add\n                Association')]");
        selenium.type("type", type);
        selenium.type("associationPaths", url);
        selenium.click("//input[@value='Add' and @type='button' and @onclick=\"addAssociation('assoForm');\"]");
        Thread.sleep(1000);

        String truncatedResourceName = url.substring(0, 10);
        truncatedResourceName = truncatedResourceName + "..";
        System.out.println("Truncated" + truncatedResourceName);


        if ((selenium.isTextPresent(type) && (selenium.isTextPresent(truncatedResourceName)))) {
            value = true;
        }
        Thread.sleep(1000);
        selenium.click("associationsIconExpanded");
        assertTrue("extenal association couldn't be added to the given resource", value);

    }

    public void removeAssociation
            (String
                    resourceName, String
                    type, String
                    associationLink) throws InterruptedException {

        selenium.click("associationsIconMinimized");

        String readResource = "";

        int i = 1;

        while (!readResource.equals(resourceName)) {

            if (selenium.isElementPresent("//div[@id='associationsList']/table/tbody/tr[" + i + "]/td[1]")) {

                Thread.sleep(1000);
                readResource = selenium.getTable("//div[@id='associationsList']/table." + i + ".0");
                System.out.println("Test Read resoruce" + readResource);
                readResource = removeTruncateCharacter(readResource);
                System.out.println(readResource);
                System.out.println(associationLink);
                int index1 = associationLink.indexOf(readResource);

                if (index1 != -1) {

                    selenium.click("//a[@onclick=\"removeAssociation('" + resourceName + "','" + associationLink + "','" + type + "','associationsDiv');\"]");
                    assertTrue(selenium.isTextPresent("Are you sure you want to delete"));
                    Thread.sleep(1000);
                    selenium.click("//button[@type='button']");
                    break;
                }
            } else {
                break;
            }
            i = i + 1;


        }
        Thread.sleep(1000);
        selenium.click("associationsIconExpanded");
    }

    public boolean AddSubscribtionsFromPanel
            (String
                    eventList, String
                    notificationMethodList, String
                    resourceLocation) throws InterruptedException {
        boolean value = false;
        selenium.click("subscriptionIconMinimized");
        selenium.select("eventList", "label=" + eventList);
        selenium.select("notificationMethodList", "label=" + notificationMethodList);
        if (notificationMethodList.equals("SOAP"))
            selenium.type("subscriptionURL", resourceLocation);
        else if (notificationMethodList.equals("HTML/Plain-Text")) {
            selenium.type("subscriptionURL", resourceLocation);
            notificationMethodList = "Web Service";
        } else if (notificationMethodList.equals("E-mail"))
            selenium.type("subscriptionEmail", resourceLocation);
        else if (notificationMethodList.equals("User Profile"))
            selenium.type("subscriptionUserProfile", resourceLocation);

        selenium.click("subscribeButton");

        if (selenium.isTextPresent(eventList + " Event via " + notificationMethodList))
            return true;
        else
            return false;
    }

    public boolean AddPolicy
            (String
                    url, String
                    name) throws InterruptedException {

        gotoPolicyPage();
        selenium.type("irFetchURL", url);
        Thread.sleep(1000);
        if (name.equals(""))
            name = selenium.getValue("irResourceName");
        selenium.click("//input[@value='Add']");
        for (int second = 0; ; second++) {
            if (second >= 120) fail("timeout");
            try {
                if (!selenium.isTextPresent("Please wait ....")) break;
                    // Sometimes the 'please wait' text may be hidden from the user, but present in
                    // the html source.
                else if (selenium.isTextPresent("The Policy URL is not a valid URL.")) {
                    selenium.click("//button[@type='button']");
                    return false;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        if (selenium.isTextPresent("The Policy URL is not a valid URL."))
            selenium.click("//button[@type='button']");
        else if (selenium.isTextPresent(name))
            return true;
        return false;

    }

    public boolean AddWSDL
            (String
                    url, String
                    name) throws InterruptedException {

        gotoWSDLPage();
        selenium.type("irFetchURL", url);
        if (name.equals(""))
            name = selenium.getValue("irResourceName");
        selenium.click("//input[@value='Add']");
//        while(selenium.isTextPresent("Please wait until the WSDL is validated and added..."))
//            Thread.sleep(2000);
//
//        Thread.sleep(1000);
        for (int second = 0; ; second++) {
            if (second >= 240) fail("timeout");
            try {
                if (!selenium.isTextPresent("Please wait until the WSDL is validated and added...")) break;
                    // Sometimes the 'please wait' text may be hidden from the user, but present in
                    // the html source.
                else if (selenium.isTextPresent("The WSDL URL is not a valid URL.") ||
                        selenium.isTextPresent("Failed to import resource " + name +
                                " to the parent collection") ||
                        selenium.isTextPresent("undefined") ||
                        selenium.isTextPresent("Failed to import resource")) {
                    selenium.click("//button[@type='button']");
                    return false;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        if (selenium.isTextPresent("The WSDL URL is not a valid URL.") || selenium.isTextPresent("Failed to import resource " + name +
                " to the parent collection") ||
                selenium.isTextPresent("undefined") || selenium.isTextPresent("Failed to import resource"))
            selenium.click("//button[@type='button']");
        else if (selenium.isTextPresent(name))
            return true;
        return false;
    }

    public boolean checkWSDL_Exsits
            (String
                    name) throws InterruptedException {
        gotoWSDLsPage();
        Thread.sleep(500);
        if (selenium.isTextPresent(name))
            return true;
        else
            return false;
    }

    public boolean checkService_Exsits
            (String
                    name) throws InterruptedException {
        gotoServicesPage();
        Thread.sleep(500);
        if (selenium.isTextPresent(name))
            return true;
        else
            return false;
    }

    public boolean checkSchema_Exsits
            (String
                    name) throws InterruptedException {
        gotoSchemasPage();
        Thread.sleep(500);
        if (selenium.isTextPresent(name))
            return true;
        else
            return false;
    }

    public boolean checkPolicy_Exsits
            (String
                    name) throws InterruptedException {
        gotoPoliciesPage();
        Thread.sleep(500);
        if (selenium.isTextPresent(name))
            return true;
        else
            return false;
    }

    public boolean addSchema
            (String
                    url, String
                    name) throws InterruptedException {
        gotoSchemaPage();
        selenium.type("irFetchURL", url);
        if (name.equals(null))
            name = selenium.getValue("irResourceName");
        selenium.click("//input[@value='Add']");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (!selenium.isElementPresent("//tr[@id='waitMessage']/td/div")) break;
                    // Sometimes the 'please wait' text may be hidden from the user, but present in
                    // the html source.
                else if (selenium.isTextPresent("The Schema URL is not a valid URL.")) {
                    selenium.click("//button[@type='button']");
                    return false;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        if (selenium.isTextPresent("The Schema URL is not a valid URL."))
            selenium.click("//button[@type='button']");
        else if (selenium.isTextPresent(name))
            return true;
        return false;
    }

    public boolean checkContentDisplay
            (String
                    key) throws InterruptedException {
        // check the wsdl's or other meta data content window
        boolean value = false;
        if (!selenium.isTextPresent("Display as text"))
            selenium.click("entriesIconMinimized");
        selenium.click("link=Display as text");
        if (selenium.isTextPresent(key))
            value = true;
        selenium.click("cancelContentButtonID");
        return value;
    }

    public boolean fillProfile
            (String
                    name, String
                    fName, String
                    lName, String
                    org,
                           String
                                   add, String
                    country, String
                    email, String
                    tPhone, String
                    mobileNo,
                            String
                                    iM, String
                    url) throws InterruptedException {
        String button = "updateprofile";
        if (!selenium.getText("//div[@id='workArea']/form/table/tbody/tr[1]/td/table/tbody/tr[1]/td[2]").equals("default")) {
            selenium.type("profile", name);
            button = "addprofile";
        }
        selenium.type("http://wso2.org/claims/givenname", fName);
        selenium.type("http://wso2.org/claims/lastname", lName);
        selenium.type("http://wso2.org/claims/organization", org);
        selenium.type("http://wso2.org/claims/streetaddress", add);
        selenium.type("http://wso2.org/claims/country", country);
        selenium.type("http://wso2.org/claims/emailaddress", email);
        selenium.type("http://wso2.org/claims/telephone", tPhone);
        selenium.type("http://wso2.org/claims/mobile", mobileNo);
        selenium.type("http://wso2.org/claims/im", iM);
        selenium.type("http://wso2.org/claims/url", url);

        selenium.click(button);
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("//button[@type='button']")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        if (selenium.isTextPresent("User profile updated successfully") || (selenium.isTextPresent("User profile added successfully"))) {
            selenium.click("//button[@type='button']");
            Thread.sleep(1000);
            return true;
        } else
            return false;
//        for (int second = 0; ; second++) {
//            if (second >= 120) fail("timeout");
//            try {
//                if (selenium.isTextPresent("User profile added successfully")||(selenium.isTextPresent("User profile added successfully"))) break;
//            } catch (Exception e) {
//
//            }
//          Thread.sleep(1000);
//        }
//        if (selenium.isTextPresent("My Profiles"))
//            return true;
//        else
//            return false;
    }

    public boolean deletePolicy
            (String
                    resourceName) throws InterruptedException {

        gotoPoliciesPage();
        String readResource = "";
        String prev = "";
        int i = 2;
        if (selenium.isElementPresent("//table[@id='customTable']/tbody/tr[1]/td[1]"))
            readResource = selenium.getTable("customTable.1.0");
        // System.out.println("Test Read resoruce\"" + readResource+"\"");
        if (readResource.equals(resourceName)) {
            selenium.click("link=Delete");
            selenium.click("//button[@type='button']");
            return true;
        }
        while (selenium.isElementPresent("//table[@id='customTable']/tbody/tr[" + i + "]/td[1]")) {
            Thread.sleep(1000);
            readResource = selenium.getTable("customTable." + i + ".0");
            System.out.println(resourceName);
            if (readResource.equals(resourceName)) {
                selenium.click("link=" + resourceName);
                selenium.waitForPageToLoad("30000");
                String path = selenium.getTable("//div[@id='workArea']/div/table.0.1");
                path = truncateDirectoryPath(path.substring(0,
                        path.indexOf(selenium.getText("//div[@id='workArea']/div/table/tbody/tr/td[2]/script")))).trim();
                gotoPoliciesPage();
                selenium.click("//a[@onclick=\"deleteService('" + path + "','/','../list/policy.jsp')\"]");
                selenium.click("//button[@type='button']");

                return true;
            }
            i++;

            prev = readResource;
        }
        return false;
    }


    public boolean deleteWSDL
            (String
                    resourceName) throws InterruptedException {
        String path = "";
        gotoWSDLsPage();
        String readResource = "";
        int i = 2;
        if (selenium.isElementPresent("//table[@id='customTable']/tbody/tr[1]/td[1]"))
            readResource = selenium.getTable("customTable.1.0");
        if (readResource.equals(resourceName)) {
            selenium.click("link=Delete");
            selenium.click("//button[@type='button']");
            return true;
        }
//        selenium.setSpeed(NewSpeed);
        while (selenium.isElementPresent("//table[@id='customTable']/tbody/tr[" + i + "]/td[1]")) {
            // Thread.sleep(1000);
            selenium.setSpeed(Curspeed);
            readResource = selenium.getTable("customTable." + i + ".0");
            if (readResource.equals(resourceName)) {
                selenium.click("link=" + resourceName);
                selenium.waitForPageToLoad("30000");
                path = selenium.getTable("//div[@id='workArea']/div/table.0.1");
                path = truncateDirectoryPath(path.substring(0,
                        path.indexOf(selenium.getText("//div[@id='workArea']/div/table/tbody/tr/td[2]/script")))).trim();
                gotoWSDLsPage();
                selenium.click("//a[@onclick=\"deleteService('" + path + "','/','../list/wsdl.jsp')\"]");
                selenium.click("//button[@type='button']");
                return true;
            }
            i++;
        }
        return false;
    }

    public boolean deleteService
            (String
                    resourceName) throws InterruptedException {  // not completed

        String readResource = "";
        String path = "";
        gotoServicesPage();
        int i = 2;
        readResource = selenium.getTable("customTable.1.0");
        if (readResource.equals(resourceName)) {
            selenium.click("link=Delete");
            selenium.click("//button[@type='button']");
            return true;
        }
//        selenium.setSpeed(NewSpeed);
        while (selenium.isElementPresent("//table[@id='customTable']/tbody/tr[" + i + "]/td[1]")) {
            //Thread.sleep(1000);
            selenium.setSpeed(Curspeed);
            readResource = selenium.getTable("customTable." + i + ".0");
            // System.out.println(resourceName);
            if (readResource.equals(resourceName)) {
                selenium.click("link=" + resourceName);
                selenium.waitForPageToLoad("30000");
                path = selenium.getTable("//div[@id='workArea']/div/table.0.1");
                path = truncateDirectoryPath(path.substring(0,
                        path.indexOf(selenium.getText("//div[@id='workArea']/div/table/tbody/tr/td[2]/script")))).trim();
                gotoServicesPage();
                selenium.click("//a[@onclick=\"deleteService('" + path + "','/','../list/service.jsp')\"]");
                selenium.click("//button[@type='button']");
                return true;
            }
            i++;
        }
        return false;
    }

    public boolean deleteSchema
            (String
                    resourceName) throws InterruptedException { // not completed
        gotoSchemasPage();
        String readResource = "";
        String path = "";
        int i = 2;
        if (selenium.isElementPresent("//table[@id='customTable']/tbody/tr[1]/td[1]"))
            readResource = selenium.getTable("customTable.1.0");
        System.out.println("Test Read resoruce\"" + readResource + "\"");
        if (readResource.equals(resourceName)) {
            selenium.click("link=Delete");
            selenium.click("//button[@type='button']");
            return true;
        }
//        selenium.setSpeed(NewSpeed);
        while (selenium.isElementPresent("//table[@id='customTable']/tbody/tr[" + i + "]/td[1]")) {
            // Thread.sleep(1000);
            selenium.setSpeed(Curspeed);
            readResource = selenium.getTable("customTable." + i + ".0");
            System.out.println(resourceName);
            if (readResource.equals(resourceName)) {
                selenium.click("link=" + resourceName);
                selenium.waitForPageToLoad("30000");
                path = selenium.getTable("//div[@id='workArea']/div/table.0.1");
                path = truncateDirectoryPath(path.substring(0,
                        path.indexOf(selenium.getText("//div[@id='workArea']/div/table/tbody/tr/td[2]/script")))).trim();
                gotoSchemasPage();
                selenium.click("//a[@onclick=\"deleteService('" + path + "','/','../list/schema.jsp')\"]");
                selenium.click("//button[@type='button']");
                return true;
            }
            i++;
        }
        return false;
    }

    public String truncateDirectoryPath
            (String
                    url) throws InterruptedException {


        return url.replace("\n", "");
    }

    public void addDependency
            (String
                    resourceName) throws InterruptedException {

        boolean value = false;

        selenium.click("dependenciesIconMinimized");
        selenium.click("//a[contains(text(),'Add\n                Dependency')]");

        selenium.click("//input[@value='..' and @type='button' and @onclick=\"showResourceTree('depPaths');\"]");
        Thread.sleep(1000);
        setWindowfocus();
        selenium.click("plus_root");
        Thread.sleep(1000);

        selenium.click("//a[@onclick=\"pickPath('" + resourceName + "','depPaths');\"]");
        Thread.sleep(1000);
        selenium.click("//input[@value='OK']");
        Thread.sleep(1000);
        selenium.click("//input[@value='Add' and @type='button' and @onclick=\"addAssociation('depForm');\"]");
        Thread.sleep(1000);

        String truncatedResourceName = TruncateResourceName(resourceName);

        truncatedResourceName = truncatedResourceName + "..";
        System.out.println("Truncated Name" + truncatedResourceName);


        if (((selenium.isTextPresent(truncatedResourceName)) || (selenium.isTextPresent(resourceName)))) {
            value = true;
        }

        Thread.sleep(1000);
        selenium.click("dependenciesIconExpanded");
    }

    public void navigateDependencyLinks
            (String
                    resourceName) throws InterruptedException {

        boolean value = false;
        Thread.sleep(1000);
        selenium.click("dependenciesIconMinimized");

        String readResource = "";
        String readResource2 = "";

        int i = 1;

        while (!readResource.equals(resourceName)) {

            Thread.sleep(1000);
            readResource = selenium.getTable("//div[@id='dependenciesList']/table." + i + ".0");

            System.out.println("Test Read resoruce befor Truncate" + readResource);
            readResource2 = removeTruncateCharacter(readResource);
            System.out.println("ReadResource2: " + readResource2);
            System.out.println("ReadResourceName: " + resourceName);
            int index1 = resourceName.indexOf(readResource2);

            if (index1 != -1) {
                value = true;
                break;
            }

            i = i + 1;

            if (i >= 10) {
                break;
            }
        }

        if (value) {
            if (readResource.contains("..")) {

                Thread.sleep(1000);
//                assertTrue(selenium.isTextPresent(readResource));
                selenium.click("link=" + readResource);
                selenium.waitForPageToLoad("30000");
                assertTrue(selenium.isTextPresent(resourceName));
            } else {
                Thread.sleep(1000);
//                assertTrue(selenium.isTextPresent(resourceName));
                selenium.click("link=" + resourceName);
                selenium.waitForPageToLoad("30000");
                assertTrue(selenium.isTextPresent(resourceName));
            }
        }
    }

    public void clickDependencyTree
            () throws InterruptedException {

        selenium.click("dependenciesIconMinimized");
        selenium.click("link=Dependency Tree");
        setWindowfocus();
        Thread.sleep(1000);
    }

    public boolean checkDependencyTree
            (String
                    resourceName) throws InterruptedException {

        boolean value = false;
        if (selenium.isTextPresent(resourceName)) {
            value = true;

        }
        Thread.sleep(2000);
        selenium.click("//button[@type='button']");
        return value;
    }

    public boolean checkDependencyTable
            (String
                    resourceName) throws InterruptedException {

        selenium.click("dependenciesIconMinimized");
        boolean value = false;

        String readResource = "";

        int i = 1;

        while (!readResource.equals(resourceName)) {

            if (selenium.isElementPresent("//div[@id='dependenciesList']/table/tbody/tr[" + i + "]/td[1]")) {

                Thread.sleep(1000);
                readResource = selenium.getTable("//div[@id='dependenciesList']/table." + i + ".0");
                System.out.println("Test Read resoruce" + readResource);
                readResource = removeTruncateCharacter(readResource);
                System.out.println(readResource);
                System.out.println(resourceName);
                int index1 = resourceName.indexOf(readResource);

                if (index1 != -1) {
                    value = true;
                    break;
                } else {
                    i = i + 1;
                    continue;
                }
            }
            break;
        }
        Thread.sleep(1000);
        selenium.click("dependenciesIconExpanded");
        return value;
    }

    public void clickDependencyTreelinks
            (String
                    resourceName) throws InterruptedException {


        Thread.sleep(1000);
        if (selenium.isTextPresent(resourceName)) {
            String linkstore = selenium.getText("link=" + resourceName);
            System.out.println("The Link store :" + linkstore);
            System.out.println("inside if");
            Thread.sleep(1000);
            selenium.click("link=" + resourceName);
            selenium.waitForPageToLoad("30000");
        } else {
            assertFalse("Resource name not found", true);
        }
    }

    public void removeDependency
            (String
                    sourceResource, String
                    resourceName) throws InterruptedException {

        selenium.click("dependenciesIconMinimized");

        String readResource = "";

        int i = 1;

        while (!readResource.equals(resourceName)) {

            if (selenium.isElementPresent("//div[@id='dependenciesList']/table/tbody/tr[" + i + "]/td[1]")) {

                Thread.sleep(1000);
                readResource = selenium.getTable("//div[@id='dependenciesList']/table." + i + ".0");
                System.out.println("Test Read resoruce" + readResource);
                readResource = removeTruncateCharacter(readResource);
                System.out.println(readResource);
                System.out.println(resourceName);
                int index1 = resourceName.indexOf(readResource);

                if (index1 != -1) {
                    selenium.click("//a[@onclick=\"removeAssociation('" + sourceResource + "','" + resourceName + "'," +
                            "'depends','dependenciesDiv');\"]");
                    assertTrue(selenium.isTextPresent("Are you sure you want to delete"));
                    Thread.sleep(1000);
                    selenium.click("//button[@type='button']");
                    break;
                }
            } else {
                break;
            }
            i = i + 1;
        }
    }

    public String TruncateResourceName
            (String
                    s) {

        int index1 = s.indexOf("..");
        String r = s;
        if (index1 != -1) {
            int beginChar = s.length() - 2;
            r = s.substring(0, beginChar);
        }
        return r;
    }

    public boolean gotoBrowsePage
            () throws InterruptedException {
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("link=Browse")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        Thread.sleep(1000);
        selenium.click("link=Browse");
        selenium.waitForPageToLoad("600000");
//        for (int second = 0; ; second++) {
//            if (second >= 60) fail("timeout");
//            try {
//                if ("> Browse".equals(selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[3]"))) break;
//            } catch (Exception e) {
//            }
//            Thread.sleep(1000);
//        }
        Thread.sleep(4000);
        if (selenium.isTextPresent("Browse"))
            return true;
        else
            return false;

    }

    public boolean deleteAllSchemas
            () throws InterruptedException { // not completed

        gotoSchemasPage();
        while (selenium.isElementPresent("//table[@id='customTable']/tbody/tr[1]/td[1]")) {

            selenium.click("link=Delete");
            selenium.click("//button[@type='button']");
            Thread.sleep(2000);
        }
        return true;


    }

    public boolean deleteAllServices
            () throws InterruptedException { // not completed

        gotoServicesPage();
        while (selenium.isElementPresent("//table[@id='customTable']/tbody/tr[1]/td[1]")) {

            selenium.click("link=Delete");
            selenium.click("//button[@type='button']");
            Thread.sleep(2000);
        }
        return true;


    }

    public boolean deleteAllWsdls
            () throws InterruptedException { // not completed

        gotoWSDLsPage();
        while (selenium.isElementPresent("//table[@id='customTable']/tbody/tr[1]/td[1]")) {

            selenium.click("link=Delete");
            selenium.click("//button[@type='button']");
            Thread.sleep(2000);
        }
        return true;


    }

    public boolean deleteAllPolicies
            () throws InterruptedException { // not completed

        gotoPoliciesPage();
        while (selenium.isElementPresent("//table[@id='customTable']/tbody/tr[1]/td[1]")) {

            selenium.click("link=Delete");
            selenium.click("//button[@type='button']");
            Thread.sleep(2000);
        }
        return true;
    }

    public void setWindowfocus
            () {
        String tryitwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(tryitwinid);
    }

    public int countDependencies
            () throws InterruptedException {
        selenium.click("dependenciesIconMinimized");
        String noOfDependencies = selenium.getText("//div[@id='dependenciesDiv']/div[1]");
        if (selenium.isTextPresent("No dependencies on this entry yet."))
            noOfDependencies = "0";
        else
            noOfDependencies = noOfDependencies.substring(0, noOfDependencies.indexOf(' '));
        selenium.click("dependenciesIconExpanded");
        return Integer.parseInt(noOfDependencies);
    }

    public int countAssociations
            () throws InterruptedException {
        selenium.click("associationsIconMinimized");
        String noOfAssociations = selenium.getText("//div[@id='associationsDiv']/div[1]");
        if (selenium.isTextPresent("No associations on this entry yet."))
            noOfAssociations = "0";
        else
            noOfAssociations = noOfAssociations.substring(0, noOfAssociations.indexOf(' '));
        if (selenium.isElementPresent("associationsIconExpanded"))
            selenium.click("associationsIconExpanded");
        return Integer.parseInt(noOfAssociations);
    }

    public boolean addLifeCycle
            (String
                    lifeCycleName) throws InterruptedException {
        if (selenium.isElementPresent("lifecycleIconMinimized"))
            selenium.click("lifecycleIconMinimized");
        Thread.sleep(1000);
        if (!selenium.isTextPresent("No lifecycles on this entry yet."))
            return false;
        selenium.click("link=Add Lifecycle");
        selenium.select("aspect", "label=" + lifeCycleName);
        selenium.click("//input[@value='Add' and @type='button' and @onclick='addAspect();']");
//        for (int second = 0;; second++) {
//            if (second >= 60) fail("timeout");
//            try { if (!"No lifecycles on this entry yet.".equals(selenium.getText("lifecyclesSummary"))) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
        if (selenium.isElementPresent("lifecycleIconExpanded"))
            selenium.click("lifecycleIconExpanded");

        return true;
    }

    public boolean tickCheckList
            () throws InterruptedException {
        int i = 0;                // this will check the phases of the life cycyle
        if (selenium.isElementPresent("lifecycleIconMinimized"))
            selenium.click("lifecycleIconMinimized");
        if (selenium.isTextPresent("No lifecycles on this entry yet.")) {
            selenium.click("lifecycleIconExpanded");
            return false;
        }
        while (selenium.isElementPresent("option" + i)) {
            if (selenium.getValue("option" + i).equals("off")) {
                selenium.click("option" + i);
                break;
            }
            i++;
        }
        promoteLC();
        if (selenium.isElementPresent("lifecycleIconExpanded"))
            selenium.click("lifecycleIconExpanded");
        return true;

    }

    public boolean promoteLC
            () throws InterruptedException {
        if (selenium.isElementPresent("lifecycleIconMinimized"))
            selenium.click("lifecycleIconMinimized");
        Thread.sleep(1000);
        if (selenium.isElementPresent("//input[@value='promote']")) {
            selenium.click("//input[@value='promote']");
            if (selenium.isTextPresent("Successfully promoted!")) {
                selenium.click("//button[@type='button']");
                return true;
            }
        }
        return false;
    }

    public boolean demoteLC
            () throws InterruptedException {
        if (selenium.isElementPresent("lifecycleIconMinimized"))
            selenium.click("lifecycleIconMinimized");
        Thread.sleep(1000);
        if (selenium.isElementPresent("//input[@value='demote']")) {
            selenium.click("//input[@value='demote']");
            if (selenium.isTextPresent("Successfully demoted!")) {
                selenium.click("//button[@type='button']");
                return true;
            }
        }
        return false;
    }

    public boolean deleteLifeCycle
            () throws InterruptedException {
        if (selenium.isElementPresent("lifecycleIconMinimized"))
            selenium.click("lifecycleIconMinimized");           // this will delete life cycle the particular window has..
        // so u maust open the resource or wsdl before calling this function
        if (selenium.isTextPresent("No lifecycles on this entry yet")) {
            selenium.click("lifecycleIconExpanded");
            return false;
        }
        selenium.click("//a[@onclick='removeAspect();']");
        if (selenium.isTextPresent("Are you sure you want to delete"))
            selenium.click("//button[@type='button']");
        if (selenium.isElementPresent("lifecycleIconExpanded"))
            selenium.click("lifecycleIconExpanded");
        return true;
    }

    public boolean registerProfile
            (String
                    userName, String
                    mediaType) throws InterruptedException {
        gotoBrowsePage();
        Thread.sleep(500);
        selenium.click("link=Add Resource");
        selenium.select("addMethodSelector", "label=Create custom content");
        selenium.select("customMediaTypeID", "label=" + mediaType);
        selenium.click("//input[@value='Create Content']");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("username")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.type("username", userName);
        selenium.click("//input[@value='Submit']");
        selenium.waitForPageToLoad("120000");
//        selenium.wait(1000);
        Thread.sleep(4000);
        if (selenium.isTextPresent("System Error Occurred"))
            return false;
        else
            return true;
    }

    public boolean deleteColletion
            (String
                    path, String
                    name) throws InterruptedException {
        gotoBrowsePage();                // this will delete the registered prfile specially or if u want u can
        // delete other folders also. eg path ="/system/uesrs/" name ="admin"
        Thread.sleep(1000);
        int index = 0;
        int a, b;
        int id = 1;
        String path1 = path;
        if (!path.equals("/"))
            path = path.concat("/");

        do {
            a = path.indexOf("/", index);
            b = path.indexOf("/", a + 1);
//            System.out.println("a ->"+a);
//            System.out.println("b ->"+b);
            if (a == -1 || b == -1)
                break;
//            System.out.println(path.substring(a+1,b));
            id = getId(path.substring(a + 1, b));
            if (id == -1)
                return false;
            else {

                Thread.sleep(1000);
                selenium.click("resourceView" + id);
                Thread.sleep(4000);
            }
            index = b;

        } while (a != -1 && b != -1);
        id = getId(name);
        if (id == -1)
            return false;

        Thread.sleep(5000);
        selenium.click("actionLink" + id);
//        System.out.println(id) ;
        Thread.sleep(3000);

        if (id == 1) {
            selenium.click("link=Delete");
        } else {
            selenium.click("//a[@onclick=\"hideOthers(" + id + ",'del');deleteResource('" + path + name + "', '" + path1 + "');\"]");
        }
        if (selenium.isTextPresent("Are you sure you want to delete ")) {
            selenium.click("//button[@type='button']");
        }
        if (selenium.isTextPresent("Successfully deleted '/carbon/security'."))
            selenium.click("//button[@type='button']");
        return true;
    }

    public void checkProfileRegister
            (String
                    path, String
                    profileName, String
                    fName, String
                    lName, String
                    email) throws InterruptedException {
        gotoBrowsePage();               // this function will check the details are existing in the profile
        // for the path provide the username also eg "/system/users/admin"
        Thread.sleep(3000);
        int index = 0;
        int a, b;
        int id = 0;
        String curPath = "";
        do {
            a = path.indexOf("/", index);
            b = path.indexOf("/", a + 1);
//            System.out.println("a ->"+a);
//            System.out.println("b ->"+b);
            if (a == -1 || b == -1)
                break;
//            System.out.println(path.substring(a+1,b));
            curPath += path.substring(a, b);
//            System.out.println("cur path:"+curPath);
            id = getId(path.substring(a + 1, b));
            if (id == -1)
                return;
            else {
                Thread.sleep(500);

                selenium.click("resourceView" + id);
                selenium.waitForPageToLoad("120000");
                if (curPath.equals(selenium.getTable("//div[@id='workArea']/div/table.0.1").replace("\n", "")))
                    break;
            }
            index = b;

        } while (a != -1 && b != -1);
        Thread.sleep(3000);
        id = getId("profiles");
        if (id == -1)
            return;
        selenium.click("resourceView" + id);
        selenium.waitForPageToLoad("120000");
        Thread.sleep(3000);

        assertTrue(selenium.isTextPresent(profileName));
        assertTrue(selenium.isTextPresent(email));
        assertTrue(selenium.isTextPresent(fName));
        assertTrue(selenium.isTextPresent(lName));
//      assertTrue(selenium.isTextPresent());   // if u want to add more details change this
    }

    public boolean checkPropertiesOfWSDL
            (String
                    propertyName, String
                    propertyValue) throws InterruptedException {

        String name = "";

        int i = 0;
        selenium.click("propertiesIconMinimized");
        while (selenium.isElementPresent("//tr[@id='propViewPanel_" + i + "']/td[1]")) {
            name = selenium.getText("//tr[@id='propViewPanel_" + i + "']/td[1]/span[1]");
            if (name.equals(propertyName)) {
                selenium.click("propertiesIconExpanded");
                return selenium.getText("//tr[@id='propViewPanel_" + i + "']/td[2]/span[1]").equals(propertyValue);
            }
            i++;

        }
        selenium.click("propertiesIconExpanded");
        return false;
    }

    public boolean deleteProfile
            (String
                    profileName, String
                    user) throws InterruptedException {
        gotoMyprofile();
        String first = selenium.getText("//div[@id='workArea']/table/tbody/tr[1]/td[1]");
        if (profileName.equals(first)) {
            selenium.click("link=Delete");
            for (int second = 0; ; second++) {
                if (second >= 60) fail("timeout");
                try {
                    if ("WSO2 Carbon".equals(selenium.getText("ui-dialog-title-dialog"))) break;
                } catch (Exception e) {
                }
                Thread.sleep(1000);
            }
            selenium.click("//button[@type='button']");
            selenium.waitForPageToLoad("30000");
            selenium.click("//button[@type='button']");
            return true;
        } else
        if (selenium.isElementPresent("//a[@onclick=\"remove('" + user + "','" + profileName + "');return false;\"]")) {
            selenium.click("//a[@onclick=\"remove('" + user + "','" + profileName + "');return false;\"]");
            for (int second = 0; ; second++) {
                if (second >= 60) fail("timeout");
                try {
                    if ("WSO2 Carbon".equals(selenium.getText("ui-dialog-title-dialog"))) break;
                } catch (Exception e) {
                }
                Thread.sleep(1000);
            }
            selenium.click("//button[@type='button']");
            selenium.waitForPageToLoad("30000");
            selenium.click("//button[@type='button']");
            return true;
        } else
            return false;
    }

    public boolean gotoOtherUsersProfiles
            (String
                    username) throws InterruptedException {
        int i = 1;
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("150000");
        selenium.click("link=Users");
        selenium.waitForPageToLoad("150000");
        while (selenium.isElementPresent("//table[@id='userTable']/tbody/tr[" + i + "]/td[1]")) {
            if (username.equals(selenium.getText("//table[@id='userTable']/tbody/tr[" + i + "]/td[1]"))) {
                if (i == 1) {
                    selenium.click("link=User Profile");
                    selenium.waitForPageToLoad("30000");

                } else {
                    selenium.click("//table[@id='userTable']/tbody/tr[" + i + "]/td[2]/a[2]");
                    selenium.waitForPageToLoad("30000");
                }
                break;
            }
            i++;
        }

        return (selenium.getText("//div[@id='middle']/h2").equals("User Profiles : " + username));
    }

    public int getId
            (String
                    resourceName) throws InterruptedException {
        int i = 1;
        while (selenium.isElementPresent("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]")) {
            if (selenium.getText("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]").replace(" ..", "").equals(resourceName))
                return i;
            i++;
        }

        return -1;
    }

    public boolean addNotificationFromAdminConsole
            (String
                    subPath, String
                    eventList, String
                    notificationMethodList, String
                    subcriptionInput) throws InterruptedException {  // before callin this method the resource hasto be ceated in
        //  the browser

        selenium.click("link=Notifications");
        selenium.waitForPageToLoad("50000");
        selenium.click("link=Add Subscription to Resource/Collection");
        selenium.waitForPageToLoad("50000");
//        Thread.sleep(2000) ;
        assertTrue(selectPathFromResourceTree(subPath));
//        selenium.setSpeed(NewSpeed);
        selenium.select("eventList", "label=" + eventList);
//        if (eventList.equals("Check LC Item"))
//            selenium.click("//option[@value='CheckListItemChecked']");
//        else if (eventList.equals("Create Child"))
//            selenium.click("//option[@value='ChildCreated']");
//        else if (eventList.equals("Delete Child"))
//            selenium.click("//option[@value='ChildDeleted']");
//        else if (eventList.equals("Change LC State"))
//            selenium.click("//option[@value='LifeCycleStateChanged']");
//        else if (eventList.equals("Update"))
//            selenium.click("//option[@value='CollectionUpdated']");
//        else
//            return false;

        selenium.select("notificationMethodList", "label=" + notificationMethodList);
        if (notificationMethodList.equals("SOAP"))
            selenium.type("subscriptionInput", subcriptionInput);
        else if (notificationMethodList.equals("HTML/Plain-Text")) {
            selenium.type("subscriptionInput", subcriptionInput);
            notificationMethodList = "Web Service";
        } else if (notificationMethodList.equals("E-mail"))
            selenium.type("subscriptionInput", subcriptionInput);
        else if (notificationMethodList.equals("User Profile"))
            selenium.type("subscriptionInput", subcriptionInput);
        selenium.setSpeed(Curspeed);
        selenium.click("subscribeButton");
        Thread.sleep(4000);
        if (selenium.isTextPresent("You are only allowed to subscribe to your profile")) {
            selenium.click("//button[@type='button']");
            return false;
        }
        return true;
    }

    public boolean selectPathFromResourceTree
            (String
                    path) throws InterruptedException { // this metod works oly in the
        // root levelresource add .. and this has to be improved further
        int level = 0;
        int i = 0;
        String currentPath = "father_root_";
        selenium.click("//input[@value='..']");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("plus_root")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        selenium.click("plus_root");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("father_root_0")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        Thread.sleep(1000);
//        selenium.setSpeed(NewSpeed);
        while (selenium.isElementPresent(currentPath + i)) {
            selenium.setSpeed(Curspeed);
            if (selenium.getText(currentPath + i).equals(path)) {
                selenium.click("link=" + selenium.getText(currentPath + i));
                Thread.sleep(500);
                selenium.click("//input[@value='OK']");
                Thread.sleep(500);
                return true;
            }
            i++;

        }
        selenium.click("link=X");
        return false;
    }

    public boolean fillServiceOverview
            (String
                    name, String
                    nameSpace, String
                    states, String
                    description) { // fill the
        selenium.type("name", name);
        selenium.type("namespace", nameSpace);
        if (!states.equals(""))
            selenium.select("states", "label=" + states);
        selenium.type("description", description);
        return true;
    }

    public boolean fillServiceContacts
            (String
                    contact1, String
                    contacttype1, String
                    contact2,
                                  String
                                          contacttype2, String
                    contact3, String
                    contacttype3) {

        if (!contact1.equals("")) {
            selenium.type("contact1", contact1);
            selenium.select("contacttype1", "label=" + contact1);
        }

        if (!contact2.equals("")) {
            selenium.type("contact2", contact2);
            selenium.select("contacttype2", "label=" + contact2);
        }
        if (!contact3.equals("")) {
            selenium.type("contact3", contact3);
            selenium.select("contacttype3", "label=" + contact3);
        }
        return true;

    }

    public boolean fillServiceInterface
            (String
                    wsdlUrl, String
                    transportProtocol, String
                    messageFormat, String
                    messageExchangePattern) {
        selenium.type("wsdlURL", wsdlUrl);
        selenium.select("transportprotocol", "label=" + transportProtocol);
        selenium.select("messageformat", "label=" + messageFormat);
        selenium.select("messageexchangepattern", "label=" + messageExchangePattern);
        return true;
    }

    public boolean fillServiceSecurity
            (String
                    authenticationplatform, String
                    authenticationmechanism, String
                    authorizationPlatform,
                                             String
                                                     messageIntegrity, String
                    messageEncryption, String
                    comments) {
        selenium.select("authenticationplatform", "label=" + authenticationplatform);
        selenium.select("authenticationmechanism", "label=" + authenticationmechanism);
        selenium.select("authorizationPlatform", "label=" + authorizationPlatform);
        selenium.select("messageIntegrity", "label=" + messageIntegrity);
        selenium.select("messageEncryption", "label=" + messageEncryption);
        selenium.type("comments", comments);
        return true;
    }

    public boolean fillServiceEndpoint
            (String
                    enviroment, String
                    url) {
        int i = 1;
        selenium.click("link=Add Endpoint");
        while (selenium.isElementPresent("endpoint" + i))
            i++;
        selenium.select("endpoint" + 1, "label=" + enviroment);
        selenium.type(i + "", url);
        return true;
    }

    public boolean fillServiceDocLinks
            (String
                    doctype1, String
                    url1, String
                    comment1,
                          String
                                  doctype2, String
                    url2, String
                    comment2, String
                    doctype3, String
                    url3, String
                    comment3) {
        selenium.type("doctype1", doctype1);
        selenium.type("url1", url1);
        selenium.type("comment1", comment1);
        selenium.type("doctype2", doctype2);
        selenium.type("url2", url2);
        selenium.type("comment2", comment2);
        selenium.type("doctype3", doctype3);
        selenium.type("url3", url3);
        selenium.type("comment3", comment3);
        return true;

    }

    public boolean saveService
            () throws InterruptedException {
        selenium.click("//input[@value='Save']");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("Please wait until the Service is importing to the Registry...".equals(selenium.getText("//tr[@id='waitMessage']/td/div")))
                    break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        return true;
    }

    public boolean addNewRole
            (String
                    roleName, String
                    configuration) throws InterruptedException {
        String[] options = {"manage-configuration", "manage-security", "upload-services", "manage-services", "manage-lc-configuration", "manage-mediation"
                , "monitor-system", "delegate-identity"};         // the configuration string should be long atleast  9
        int i = 1;
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("150000");
        selenium.click("link=Roles");
        selenium.waitForPageToLoad("150000");
        selenium.click("link=Add New Role");
        selenium.waitForPageToLoad("150000");
        selenium.type("roleName", roleName);
        selenium.click("//input[@value='Next >']");
        selenium.waitForPageToLoad("30000");

        if (configuration.equals(""))
            return false;
        if (configuration.charAt(0) == ('1'))
            selenium.click("selectedPermissions");
        while (configuration.length() > i && options.length > i) {
            if (configuration.charAt(i) == ('1'))
                selenium.click("//input[@name='selectedPermissions' and @value='" + options[i - 1] + "']");
            i++;
        }

        selenium.click("//input[@value='Finish']");
        selenium.waitForPageToLoad("30000");

        return selenium.isTextPresent("ewe");
    }

    public boolean addUserToRole
            (String
                    roleName, String
                    userName) throws Exception {
        int i = 1;
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Roles");
        selenium.waitForPageToLoad("30000");
//        selenium.setSpeed(NewSpeed );
        while (selenium.isElementPresent("//table[@id='roleTable']/tbody/tr[" + i + "]/td[1]")) {
//            selenium.setSpeed(Curspeed);
            if (selenium.getText("//table[@id='roleTable']/tbody/tr[" + i + "]/td[1]").equals(roleName)) {
                selenium.click("//table[@id='roleTable']/tbody/tr[" + i + "]/td[2]/a[2]");
                selenium.waitForPageToLoad("100000");
                selenium.type("org.wso2.usermgt.role.edit.filter", "*");
                selenium.click("//input[@value='Search']");
                selenium.waitForPageToLoad("30000");
                selenium.click("//input[@name='selectedUsers' and @value='" + userName + "']");
                selenium.click("//input[@value='Update']");
                selenium.waitForPageToLoad("30000");
                assertTrue(selenium.isTextPresent("updated successfully."));
                selenium.click("//button[@type='button']");
                return true;
            }
            i++;
        }
        return false;
    }
//    public  boolean setUserPermissions()

    public boolean setRolePermissions
            (String
                    roleName, String
                    authorize, boolean allow) throws InterruptedException {
        boolean value = false;
        gotoBrowsePage();
        Thread.sleep(1000);
        selenium.click("perIconMinimized");
        try {
            selenium.select("roleToAuthorize", "label=" + roleName);
            selenium.select("roleActionToAuthorize", "label=" + authorize);
        }
        catch (Exception e) {
            assertTrue(false);
        }
        if (allow)
            selenium.click("rolePermissionAllow");
        selenium.click("//input[@value='Add Permission' and @type='button' and @onclick=\"addRolePermission('/')\"]");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("messagebox-info")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        if (selenium.isTextPresent("Permission applied successfully")) {
            selenium.click("//button[@type='button']");
        }
        selenium.click("perIconExpanded");
        return true;
    }

    public int countNotifications
            () throws InterruptedException {
        int i = 1;
        gotoNotificationPage();
        Thread.sleep(1000);
//        if (selenium.isTextPresent("There are no subscriptions created at present"))
//            return 0;
//        selenium.setSpeed(NewSpeed);
        while (selenium.isElementPresent("//table[@id='subscriptionsTable']/tbody/tr[" + i + "]/td[1]"))
            i++;
        selenium.setSpeed(Curspeed);
        return i - 1;

    }

    public boolean gotoNotificationPage
            () throws InterruptedException {
        for (int second = 0; ; second++) {
            if (second >= 20) fail("timeout");
            try {
                if (selenium.isElementPresent("link=Notifications")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("link=Notifications");
        for (int second = 0; ; second++) {
            if (second >= 30) fail("timeout");
            try {
                if ("Manage Notifications".equals(selenium.getText("//div[@id='middle']/h2"))) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        return true;

    }

    public void gotoUserManagementPage
            () throws InterruptedException {
        for (int second = 0; ; second++) {
            if (second >= 20) fail("timeout");
            try {
                if (selenium.isElementPresent("link=User Management")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        Thread.sleep(1000);
        selenium.click("link=User Management");
        for (int second = 0; ; second++) {
            if (second >= 20) fail("timeout");
            try {
                if ("> User Management".equals(selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[3]")))
                    break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }

    public void gotoUsersPage
            () throws InterruptedException {
        gotoUserManagementPage();
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("link=Users")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("link=Users");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("> Users".equals(selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[4]"))) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

    }

    public void gotoRolePage
            () throws InterruptedException {
        gotoUserManagementPage();
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("link=Roles")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("link=Roles");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("> Roles".equals(selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[4]"))) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

    }

    public boolean deleteUser
            (String
                    userName) throws InterruptedException {
        int i = 1;
        boolean found = false;
        gotoUsersPage();
        while (selenium.isElementPresent("//table[@id='userTable']/tbody/tr[" + i + "]/td[1]")) {
            selenium.setSpeed(Curspeed);
            if (selenium.getText("//table[@id='userTable']/tbody/tr[" + i + "]/td[1]").equals(userName)) {
                found = true;
                Thread.sleep(1000);
                if (i == 1 || i == 2) {
                    selenium.click("link=Delete");
                } else
                    selenium.click("//a[@onclick=\"deleteUser('" + userName + "')\"]");
            }
            i++;
        }
        if (!found)
            return false;
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("messagebox-confirm")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//button[@type='button']");
        return true;
    }

    public boolean deleteRole
            (String
                    userName) throws InterruptedException {
        int i = 1;
        boolean found = false;
        gotoRolePage();
//        selenium.setSpeed(NewSpeed);
        while (selenium.isElementPresent("//table[@id='roleTable']/tbody/tr[" + i + "]/td[1]")) {
            selenium.setSpeed(Curspeed);
            if (selenium.getText("//table[@id='roleTable']/tbody/tr[" + i + "]/td[1]").equals(userName)) {
                found = true;
                if (i == 1 || i == 2)
                    selenium.click("link=Delete");
                else
                    selenium.click("//a[@onclick=\"deleteUser('" + userName + "')\"]");
            }
            i++;
        }
        if (!found)
            return false;
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("messagebox-confirm")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//button[@type='button']");
        return true;
    }

    public boolean deleteAllNotifications
            () throws InterruptedException {
        gotoNotificationPage();  //selenium.isElementPresent("//table[@id='subscriptionsTable']/tbody/tr/td[1]") &&
        while (!(selenium.isTextPresent("There are no subscriptions created at present. Please use the links below, to create new subscriptions."))) {
            for (int second = 0; ; second++) {
                if (second >= 60) fail("timeout");
                try {
                    if (selenium.isElementPresent("link=Delete")) break;
                } catch (Exception e) {
                }
                Thread.sleep(1000);
            }
            selenium.click("link=Delete");
            selenium.click("//button[@type='button']");
            for (int second = 0; ; second++) {
                if (second >= 5) break;
                try {
                    if (selenium.isElementPresent("//table[@id='subscriptionsTable']/tbody/tr/td[1]")) break;
                } catch (Exception e) {
                }
                Thread.sleep(1000);
            }
            Thread.sleep(1000);

        }
        return (selenium.isTextPresent("There are no subscriptions created at present. Please use the links below, to create new subscriptions."));
    }

    public boolean editNotification
            (String
                    path, String
                    event, String
                    notification, String
                    subsInput, String
                    newEvent, String
                    newNotification, String
                    newSubsInput) throws InterruptedException {
        int id = 0;
        String transNotific;
        gotoNotificationPage();
        if (notification.equals("SOAP") || notification.equals("HTML/Plain-Text"))
            transNotific = "Web Service";
        else
            transNotific = notification;
        id = searchNotification(path, event, transNotific, subsInput);
        if (id == -1)
            return false;
        if (id == 1)
            selenium.click("link=Edit");
        else
            selenium.click("//table[@id='subscriptionsTable']/tbody/tr[" + (id) + "]/td[4]/a[1]");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("Registry Subscription".equals(selenium.getText("//div[@id='middle']/h2"))) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        String prevEvent = event;
        String prevNotific = notification;
        String prevSubInput = subsInput;
//        System.out.println(prevEvent +"*"+ prevNotific +"*"+prevSubInput );

        if (newSubsInput.equals("") && !newNotification.equals(""))
            return false;
        if (!newEvent.equals(""))
            selenium.select("eventList", "label=" + newEvent);
        else
            selenium.select("eventList", "label=" + prevEvent);

        if (!newNotification.equals(""))
            selenium.select("notificationMethodList", "label=" + newNotification);
        else
            selenium.select("notificationMethodList", "label=" + prevNotific);

        if (!newSubsInput.equals(""))
            selenium.type("subscriptionInput", newSubsInput);
        else
            selenium.type("subscriptionInput", prevSubInput);
        String testEvent = selenium.getText("eventList");
        String testNotific = selenium.getText("notificationMethodList");
        String testSubsInput = selenium.getText("subscriptionInput");
        selenium.click("subscribeButton");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("messagebox-confirm")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//button[@type='button']");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("//div[@id='middle']/h2")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        assertTrue(searchNotification(path, testEvent, testNotific, testSubsInput) == -1);
        return true;
    }

    public int searchNotification
            (String
                    path, String
                    event, String
                    notification, String
                    subscriptionInput) throws InterruptedException {
        int i = 1;
        boolean found = false;
//        selenium.setSpeed(NewSpeed);
        Thread.sleep(1000);
        while (selenium.isElementPresent("//table[@id='subscriptionsTable']/tbody/tr[" + i + "]/td[1]")) {
//            System.out.println(selenium.getText("//table[@id='subscriptionsTable']/tbody/tr["+i+"]/td[1]") +"*"+ selenium.getText("//table[@id='subscriptionsTable']/tbody/tr["+i+"]/td[2]") +"*"+selenium.getText("//table[@id='subscriptionsTable']/tbody/tr["+i+"]/td[3]") );
            if (selenium.getText("//table[@id='subscriptionsTable']/tbody/tr[" + i + "]/td[1]").equals(path))
                if (selenium.getText("//table[@id='subscriptionsTable']/tbody/tr[" + i + "]/td[2]").equals(event))
                    if (selenium.getText("//table[@id='subscriptionsTable']/tbody/tr[" + i + "]/td[3]").equals(notification)) {
                        if (i == 1)
                            selenium.click("link=Edit");
                        else
                            selenium.click("//table[@id='subscriptionsTable']/tbody/tr[" + i + "]/td[4]/a[1]");
                        for (int second = 0; ; second++) {
                            if (second >= 60) fail("timeout");
                            try {
                                if (selenium.isElementPresent("subscriptionInput")) break;
                            } catch (Exception e) {
                            }
                            Thread.sleep(1000);
                        }
//                        System.out.println(selenium.getValue("subscriptionInput"));
                        if (selenium.getValue("subscriptionInput").equals(subscriptionInput)) {
                            selenium.setSpeed(Curspeed);
//                            selenium.goBack() ;
                            gotoNotificationPage();
                            return i;
                        } else
//                        selenium.goBack() ;
                            gotoNotificationPage();
                    }
            i++;
        }
        selenium.setSpeed(Curspeed);
        selenium.goBack();
        return -1;
    }

    public void gotoLifeCyclePage
            () throws InterruptedException {
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("link=Lifecycles")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("link=Lifecycles");
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("//div[@id='middle']/h2")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }

    public boolean viewLC
            (String
                    name) throws InterruptedException {
        gotoLifeCyclePage();
        int id;
        id = getLCId(name);

        if (id == -1)
            return false;
        else if (id == 1)
            selenium.click("link=Edit");
        else
            selenium.click("//a[@onclick=\"editLC('" + name + "')\"]");
//        selenium.waitForPageToLoad("30000");
        Thread.sleep(1000);
        if (selenium.isElementPresent("//div[@id='messagebox-error']/p")) {
            selenium.click("//button[@type='button']");
            return false;
        }
        return true;
    }

    public boolean deleteLC
            (String
                    name) throws InterruptedException {
        gotoLifeCyclePage();
        int id;
        id = getLCId(name);

        if (id == -1)
            return false;
        else if (id == 1)
            selenium.click("link=Delete");
        else
            selenium.click("//a[@onclick=\"deleteLC('" + name + "')\"]");
//        selenium.waitForPageToLoad("30000");
        Thread.sleep(1000);
        if (selenium.isElementPresent("//div[@id='messagebox-error']/p")) {
            selenium.click("//button[@type='button']");
            return false;
        }
        return true;
    }

    public int getLCId
            (String
                    name) throws InterruptedException {
        int i = 1;
        while (selenium.isElementPresent("//table[@id='lcmTable']/tbody/tr[" + (i++) + "]/td[1]"))
            if (selenium.getText("//table[@id='lcmTable']/tbody/tr[" + (i - 1) + "]/td[1]").equals(name))
                return i - 1;
        return -1;
    }

    public boolean editLC
            (String
                    name, String
                    newContent) throws InterruptedException {
        gotoLifeCyclePage();
        int id;
        id = getLCId(name);

        if (id == -1)
            return false;
        else if (id == 1)
            selenium.click("link=Edit");
        else
            selenium.click("//a[@onclick=\"editLC('" + name + "')\"]");
//        selenium.waitForPageToLoad("30000");
        Thread.sleep(1000);
        if (selenium.isElementPresent("//div[@id='messagebox-error']/p")) {
            selenium.click("//button[@type='button']");
            return false;
        }
        selenium.type("payload", newContent);
        selenium.click("//input[@value='Save']");
        Thread.sleep(1000);
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("50000");
        return true;
    }

    public boolean addNewLC
            (String
                    name, String
                    content) throws InterruptedException {
        gotoLifeCyclePage();
        selenium.click("link=Add New Lifecycle");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("Lifecycle Source".equals(selenium.getText("//div[@id='middle']/h2"))) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.type("payload", content);
        selenium.click("//input[@value='Save']");
        Thread.sleep(1000);
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("50000");
        return (getLCId(name) != -1);
    }

    public int countLCs
            () throws InterruptedException {
        gotoLifeCyclePage();
        int i = 1;
        while (selenium.isElementPresent("//table[@id='lcmTable']/tbody/tr[" + (i++) + "]/td[1]")) ;
        return i - 1;
    }

    public void deleteLCFromBrowser
            () throws InterruptedException {
        if (selenium.isElementPresent("lifecycleIconMinimized"))
            selenium.click("lifecycleIconMinimized");
        selenium.click("link=Delete");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("messagebox-confirm")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//button[@type='button']");
    }

    public boolean gotopath
            (String
                    path, String
                    res) throws InterruptedException {
        gotoBrowsePage();
        selenium.click("link=Root");
        selenium.waitForPageToLoad("100000");
        Thread.sleep(1000);
        int index = 0;
        int a, b;
        int id = 1;
        String path1 = path;
        if (!path.equals("/"))
            path = path.concat("/");

        do {
            a = path.indexOf("/", index);
            b = path.indexOf("/", a + 1);
//            System.out.println("a ->"+a);
//            System.out.println("b ->"+b);
            if (a == -1 || b == -1)
                break;
//            System.out.println(path.substring(a+1,b));
            id = getId(path.substring(a + 1, b));
            if (id == -1)
                return false;
            else {

                Thread.sleep(4000);
                selenium.click("resourceView" + id);
                selenium.waitForPageToLoad("70000");
                Thread.sleep(3000);
            }
            index = b;

        } while (a != -1 && b != -1);
        if (res.equals(""))
            return true;
        id = getId(res);
        if (id == -1)
            return false;
        selenium.click("resourceView" + id);
        selenium.waitForPageToLoad("70000");
        Thread.sleep(6000);
        return true;
    }

    public void addUserPerm
            (String
                    user, String
                    actToAuth, boolean auth) throws InterruptedException {
        selenium.click("perIconMinimized");
        selenium.select("userToAuthorize", "label=" + user);

        selenium.select("actionToAuthorize", "label=" + actToAuth);
        if (auth)
            selenium.click("permissionAllow");
        else
            selenium.click("permissionDeny");
        selenium.click("//input[@value='Add Permission']");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("Permission applied successfully".equals(selenium.getText("//div[@id='messagebox-info']/p")))
                    break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }

    public void gotoActivityPage
            () throws InterruptedException {
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("link=Activities")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }

        selenium.click("link=Activities");
        selenium.waitForPageToLoad("90000");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("Activities".equals(selenium.getText("//div[@id='middle']/h2"))) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }

    public boolean searchActivity
            (String
                    usrName, String
                    path, String
                    dateFrom, String
                    dateTo, String
                    filterBy, String
                    searchTag) throws InterruptedException {
        int i = 1;
        gotoActivityPage();
        selenium.type("user", usrName);
        selenium.type("path", path);
        if (!dateFrom.equals(""))
            selenium.type("fromDate", dateFrom);
        if (!dateTo.equals(""))
            selenium.type("toDate", dateTo);

        if (!filterBy.equals(""))
            selenium.select("filter", "label=" + filterBy);
        selenium.click("//input[@value='Search Activities']");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("link=Search Again")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        Thread.sleep(1000);
        while (selenium.isElementPresent("//div[@id='activityList']/table/tbody/tr[" + (i) + "]/td")) {
            if (selenium.getText("//div[@id='activityList']/table/tbody/tr[" + (i++) + "]/td").startsWith(searchTag))
                return true;
        }
        return false;
    }

    public boolean moveCollection
            (String
                    colectionName, String
                    sourcePath, String
                    destiPath) throws InterruptedException {
        int id;
        boolean sucess = false;
        id = getId(colectionName);
        if (id == -1)
            return false;
        selenium.click("actionLink" + id);
        if (id == 1) {
            selenium.click("link=Move");
            selenium.type("move_destination_path1", destiPath);
            selenium.click("//input[@value='Move']");
        } else {
            selenium.click("//a[@onclick=\"showHideCommon('move_panel'" + id + ");hideOthers(" + id + ",'move');\"]");
            selenium.type("move_destination_path1", destiPath);
            selenium.click("//input[@value='Move' and @type='button' and @onclick=\"this.disabled = true; moveResource('" + sourcePath + "', '" + sourcePath + colectionName + "','move_destination_path'" + id + ",'" + colectionName + "',1); this.disabled = false;\"]");
        }
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("//button[@type='button']")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        if (selenium.isElementPresent("messagebox-info"))
            sucess = true;
        else
            sucess = false;
        selenium.click("//button[@type='button']");
        return sucess;
    }

    public String convertNamespaceToFolderStructure
            (String
                    nameSpace) throws InterruptedException {
        nameSpace = nameSpace.replace("://", "/");
        nameSpace = nameSpace.replace(".", "/");
        if (!nameSpace.endsWith("/"))
            nameSpace = nameSpace.concat("/");
        return nameSpace;
    }

    public boolean copyCollecion
            (String
                    name, String
                    sourcePath, String
                    destinationPath) throws InterruptedException {
        int id;
        boolean validity = false;
        gotopath(sourcePath, "");
        id = getId(name);
        if (id == -1)
            return false;
        selenium.click("actionLink" + id);
        if (id == 1) {
            selenium.click("link=Copy");
            selenium.type("copy_destination_path1", destinationPath);
            selenium.click("//input[@value='Copy']");
        } else {
            selenium.click("//a[@onclick=\"showHideCommon('copy_panel" + id + "');hideOthers(" + id + ",'copy');\"]");
            selenium.type("copy_destination_path" + id, destinationPath);
            selenium.click("//input[@value='Copy' and @type='button' and @onclick=\"this.disabled = true; copyResource('" + sourcePath + "', '" + sourcePath + name + "','copy_destination_path" + id + "','" + name + "',1); this.disabled = false;\"]");
        }
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("OK".equals(selenium.getText("//body/div[3]/div[2]"))) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        if (!selenium.isElementPresent("//div[@id='messagebox-warning']/p"))
            validity = true;
        selenium.click("//button[@type='button']");
        return validity;
    }

    public boolean addResFromLocalFS
            (String
                    path, String
                    name) throws InterruptedException {
        selenium.click("link=Add Resource");
        selenium.type("uResourceFile", path);
        Thread.sleep(10000);
        selenium.type("uResourceName", name);
        selenium.click("//input[@value='Add' and @type='button' and @onclick='whileUpload();submitUploadContentForm();']");
        selenium.waitForPageToLoad("150000");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("//button[@type='button']")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//button[@type='button']");
        return true;
    }

    public boolean deleteResource
            (String
                    path, String
                    name) throws InterruptedException {
        gotoBrowsePage();                // this will delete the registered prfile specially or if u want u can
        // delete other folders also. eg path ="/system/uesrs/" name ="admin"
        Thread.sleep(1000);
        int index = 0;
        int a, b;
        int id = 1;
        String path1 = path;
        if (!path.equals("/"))
            path = path.concat("/");

        do {
            a = path.indexOf("/", index);
            b = path.indexOf("/", a + 1);
//            System.out.println("a ->"+a);
//            System.out.println("b ->"+b);
            if (a == -1 || b == -1)
                break;
//            System.out.println(path.substring(a+1,b));
            id = getId(path.substring(a + 1, b));
            if (id == -1)
                return false;
            else {

                Thread.sleep(1000);
                selenium.click("resourceView" + id);
                Thread.sleep(4000);
            }
            index = b;

        } while (a != -1 && b != -1);
        id = getId(name);
        if (id == -1)
            return false;

        Thread.sleep(5000);
        selenium.click("actionLink" + id);
//        System.out.println(id) ;
        Thread.sleep(3000);

        if (id == 1) {
            selenium.click("link=Delete");
        } else {
            selenium.click("//a[@onclick=\"hideOthers(" + id + ",'del');deleteResource('" + path + name + "', '" + path1 + "')\"]");
        }
        if (selenium.isTextPresent("Are you sure you want to delete ")) {
            selenium.click("//button[@type='button']");
        }
        if (selenium.isTextPresent("Successfully deleted "))
            selenium.click("//button[@type='button']");
        return true;
    }

    public boolean cleanRegistry
            () throws InterruptedException {  // if u need  anything not to delete put that
        // name to the "folder" array
        String[] folders = {"carbon", "governance", "system"};
        boolean shouldNotDelete = false;
        String name = "";
        int i = 1;
        gotoBrowsePage();
        while (selenium.isElementPresent("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[2]")) {
            name = selenium.getText("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]").replace(" ..", "").replace("..", "");
            System.out.println(name);
            for (int j = 0; j < folders.length; j++) {
                if (name.equals(folders[j]))
                    shouldNotDelete = true;
            }
            if (shouldNotDelete) {
                shouldNotDelete = false;
                i++;
                continue;
            }
            selenium.click("actionLink" + i);
            Thread.sleep(1000);
            if (selenium.isElementPresent("//a[@onclick=\"hideOthers(" + i + ",'del');deleteResource('/" + name + "', '/');\"]"))
                selenium.click("//a[@onclick=\"hideOthers(" + i + ",'del');deleteResource('/" + name + "', '/');\"]");
            else
            if (selenium.isElementPresent("//a[@onclick=\"hideOthers(" + i + ",'del');deleteResource('/" + name + "', '/')\"]"))
                selenium.click("//a[@onclick=\"hideOthers(" + i + ",'del');deleteResource('/" + name + "', '/')\"]");
            else if (selenium.isElementPresent("link=Delete"))
                selenium.click("link=Delete");
            for (int second = 0; ; second++) {
                if (second >= 60) fail("timeout");
                try {
                    if (selenium.isElementPresent("messagebox-confirm")) break;
                } catch (Exception e) {
                }
                Thread.sleep(1000);
            }
            selenium.click("//button[@type='button']");
            Thread.sleep(2000);

        }
        return false;
    }

    public void gotoDashboardPage
            () {
        selenium.click("link=Main Dashboard");
        selenium.waitForPageToLoad("30000");
    }

    public void gotoKeyStorePage
            () {
        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");
    }

    public void gotoLoggingPage
            () {
        selenium.click("link=Logging");
        selenium.waitForPageToLoad("30000");
    }

    public void gotoshutDownRestartPage
            () {
        selenium.click("link=Shutdown/Restart");
        selenium.waitForPageToLoad("30000");
    }

    public void gotomonitoredServerPage
            () {
        selenium.click("link=Monitored Servers");
        selenium.waitForPageToLoad("30000");
    }

    public void gotosystemLogsPage
            () {
        selenium.click("link=System Logs");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Search Logs"));
    }


    public boolean search
            (String
                    resName, String
                    createdFrom, String
                    createdTo, String
                    updatedFrom, String
                    updatedTo,
                                 String
                                         createdBy, String
                    updatedBy, String
                    tags, String
                    comments, String
                    propertyName, String
                    propVal, String
                    path, String
                    author) throws InterruptedException {
        boolean pathValidation = false;
        boolean authorValidation = false;
        int i = 2;
        if (selenium.isElementPresent("link=Search Again"))
            clickSearchAgain();
        selenium.type("#_resourceName", resName);
        if (!createdFrom.equals(""))
            selenium.type("cfromDate", createdFrom);
        if (!createdTo.equals(""))
            selenium.type("ctoDate", createdTo);
        if (!updatedFrom.equals(""))
            selenium.type("ufromDate", updatedFrom);
        if (!updatedTo.equals(""))
            selenium.type("utoDate", updatedTo);
        selenium.type("#_author", createdBy);
        selenium.type("#_updater", updatedBy);
        selenium.type("#_tags", tags);
        selenium.type("#_comments", comments);
        selenium.type("#_propertyName", propertyName);
        selenium.type("#_propertyValue", propVal);
        selenium.click("#_0");
        if (selenium.isElementPresent("messagebox-warning")) {
            selenium.click("//button[@type='button']");
            return false;
        }
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("link=Search Again")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
//        Thread.sleep(2000);
        if (selenium.isElementPresent("//tr[@id='1']/td[1]")) {
            if (selenium.getText("//tr[@id='1']/td[3]").equals(author))
                authorValidation = true;
            if (selenium.getText("//tr[@id='1']/td[1]").equals(path))
                pathValidation = true;
        }
        while (selenium.isElementPresent("//div[2]/table/tbody/tr[" + i + "]/td[1]")) {
            if (selenium.getText("//div[2]/table/tbody/tr[" + i + "]/td[1]").equals(author))
                pathValidation = true;
            if (selenium.getText("//tr[" + i + "]/td[3]").equals(path))
                authorValidation = true;
        }
        if (author.equals("") || path.equals(""))
            return pathValidation || authorValidation;
        else
            return pathValidation && authorValidation;
    }

    public boolean gotoSearchPage
            () throws InterruptedException {
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("link=Search")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("link=Search");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if ("Search".equals(selenium.getText("//div[@id='middle']/h2"))) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        return true;
    }

    public boolean clickSearchAgain
            () throws InterruptedException {
        selenium.click("link=Search Again");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("//form[@id='advancedSearchForm']/table/thead/tr/th")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        cleanSearchFields();
        return true;
    }

    public boolean cleanSearchFields
            () throws InterruptedException {
        selenium.type("#_resourceName", "");
        selenium.type("cfromDate", "");
        selenium.type("ctoDate", "");
        selenium.type("ufromDate", "");
        selenium.type("utoDate", "");
        selenium.type("#_author", "");
        selenium.type("#_updater", "");
        selenium.type("#_tags", "");
        selenium.type("#_comments", "");
        selenium.type("#_propertyName", "");
        selenium.type("#_propertyValue", "");
        return true;
    }

    public static String getDate
            () {
        String dateFormat = "MM/dd/yyyy";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    public static String getTomorrowDate
            () {
        String dateFormat = "MM/dd/yyyy";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        return sdf.format(cal.getTime());
    }

    public static String getYesterdayDate
            () {
        String dateFormat = "MM/dd/yyyy";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return sdf.format(cal.getTime());

    }

    public void addcomment
            (String
                    comment, String
                    path) throws InterruptedException {
        selenium.click("commentsIconMinimized");
        selenium.click("link=Add Comment");
        selenium.type("comment", comment);
        selenium.click("//input[@value='Add' and @type='button' and @onclick=\"addComment('/" + path + "');\"]");
        selenium.click("commentsIconExpanded");
    }

    public void verifyFeedResources
            (String
                    resources[]) {

        if (resources.length > 0) {
            for (int i = 0; i < resources.length; i++) {
                assertTrue(selenium.isTextPresent(resources[i]));
            }
        }
    }

    public void testCleanRegistry
            () throws InterruptedException, IOException {  // if u need  anything not to delete put that
        // name to the "folder" array
        String[] folders = {"carbon", "governance", "system"};
        boolean shouldNotDelete = false;
        String name = "";
        int i = 1;
        signOut();
        gotoBrowsePage();
        while (selenium.isElementPresent("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[2]")) {
            name = selenium.getText("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]").replace(" ..", "").replace("..", "");
            System.out.println(name);
            for (int j = 0; j < folders.length; j++) {
                if (name.equals(folders[j]))
                    shouldNotDelete = true;
            }
            if (shouldNotDelete) {
                shouldNotDelete = false;
                i++;
                continue;
            }
            selenium.click("actionLink" + i);
            Thread.sleep(1000);
            if (selenium.isElementPresent("//a[@onclick=\"hideOthers(" + i + ",'del');deleteResource('/" + name + "', '/');\"]"))
                selenium.click("//a[@onclick=\"hideOthers(" + i + ",'del');deleteResource('/" + name + "', '/');\"]");
            else
            if (selenium.isElementPresent("//a[@onclick=\"hideOthers(" + i + ",'del');deleteResource('/" + name + "', '/')\"]"))
                selenium.click("//a[@onclick=\"hideOthers(" + i + ",'del');deleteResource('/" + name + "', '/')\"]");
            else if (selenium.isElementPresent("link=Delete"))
                selenium.click("link=Delete");
            for (int second = 0; ; second++) {
                if (second >= 60) fail("timeout");
                try {
                    if (selenium.isElementPresent("messagebox-confirm")) break;
                } catch (Exception e) {
                }
                Thread.sleep(1000);
            }
            selenium.click("//button[@type='button']");
            Thread.sleep(2000);

        }
    }

    public static String getTimeStamp
            () {
        String dateFormat = "hh:mm:ssa dd.MM.yyyy";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    public boolean uploadPicShtBg
            (String
                    fileName, String
                    name, String
                    description) throws Exception {
        String path = new java.io.File(".").getCanonicalPath();
        String filePath = "/lib/shutterbug/";
        assertEquals("Upload Image", selenium.getText("//form[@id='resourceUploadForm']/table/tbody/tr[1]/td"));
        selenium.type("uResourceFile", path + filePath + fileName);
        if (!name.equals(""))
            selenium.type("uResourceName", name);
        selenium.type("description", "this a sample desc");
        selenium.click("//input[@value='Add']");
        selenium.waitForPageToLoad("100000");
        if (selenium.isElementPresent("dialog")) {
            if (selenium.isElementPresent("//a/span"))
                selenium.click("link=close");
            return false;
        }
        return true;
    }

    public String getProperty
            (String
                    propertyName) throws InterruptedException {
        int i = 0;
        selenium.click("propertiesIconMinimized");
        while (selenium.isElementPresent("//tr[@id='propViewPanel_" + i + "']/td[1]")) {
            if (selenium.getText("//tr[@id='propViewPanel_" + i + "']/td[1]/span[1]").equals(propertyName))
                return selenium.getText("//tr[@id='propViewPanel_" + i + "']/td[2]/span[1]");
        }
        return "";
    }

    public void managePopupInShutterbugBrowser
            () throws InterruptedException {

        gotoBrowsePage();
        if (selenium.isTextPresent("Successfully uploaded content."))
            selenium.click("//button[@type='button']");
        if (selenium.isElementPresent("//div[@id='messagebox-error']/p"))
            selenium.click("//button[@type='button']");
    }

    public boolean exploreThroughPaging
            (String
                    path, String
                    res) throws InterruptedException {
        int index = 0;
        boolean run = true;
        int loop = 0;
        int a, b;
        int id = 0;
        String path1 = path;
        if (!path.equals("/"))
            path = path.concat("/");
        do {
            id = 0;
            do {
                a = path.indexOf("/", index);
                b = path.indexOf("/", a + 1);
                if (a == -1 || b == -1 || b == -2) {
                    run = false;
                    break;

                }
//            System.out.println(path.substring(a+1,b));
                id = getId(path.substring(a + 1, b));
                if (id == -1) {
                    if (selenium.isElementPresent("//a[contains(text(),'Next\n            >')]")) {
                        selenium.click("//a[contains(text(),'Next\n            >')]");
                        Thread.sleep(2000);
                        continue;
                    } else

                        break;
                } else {

                    Thread.sleep(4000);
                    selenium.click("resourceView" + id);
                    selenium.waitForPageToLoad("120000");
                    Thread.sleep(3000);
                }
                index = b;

            } while (a != -1 && b != -1);
        } while (selenium.isElementPresent("//a[contains(text(),'Next\n            >')]") && run);
        while (selenium.isElementPresent("//a[contains(text(),'Next\n            >')]")) {
            if (loop != 0 && (selenium.isElementPresent("//a[contains(text(),'Next\n            >')]"))) {
                selenium.click("//a[contains(text(),'Next\n            >')]");
                Thread.sleep(2000);
            }
            loop++;
            if (res.equals(""))
                return true;
            id = getId(res);
            if (id == -1) {
                if (!selenium.isElementPresent("//a[contains(text(),'Next\n            >')]"))
                    return false;
                else
                    continue;
            }

            selenium.click("resourceView" + id);
            selenium.waitForPageToLoad("70000");
            Thread.sleep(6000);
            return true;
        }
        return false;
    }

    public boolean createCheckPoint
            () throws InterruptedException {

        boolean errors = true;
        // assertTrue(gotopath(path,resource));
        selenium.click("//a[contains(text(),'Create\n                                Checkpoint')]");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("//button[@type='button']")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        if (selenium.isTextPresent("Check point created successfully"))
            errors = !errors;
        selenium.click("//button[@type='button']");
        Thread.sleep(2000);
        return !errors;
    }

    public int getVersion
            () throws InterruptedException {
        int version = -1;
        String verString;
        clickViewVerions();
        if (selenium.isTextPresent("No versions available for this resource or collection. "))
            return version;
        verString = selenium.getText("//table[@id='versionsTable']/tbody/tr/td[1]");
        version = Integer.parseInt(verString);
        return version;
    }

    public void clickViewVerions
            () throws InterruptedException {
        selenium.click("link=View versions");
        selenium.waitForPageToLoad("120000");
    }

    public boolean checkVersionDetails
            (
                    int version, String
                    resources[]) throws InterruptedException {
        int i = 1, index = 0;
        boolean found = false;
        clickViewVerions();
        while (selenium.isElementPresent("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[1]")) {
            if (selenium.getText("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[1]").equals(version + "")) {
                if (i == 1)
                    selenium.click("link=Details");
                else
                    selenium.click("//a[@onclick=\"setAndGo('/;version:" + version + "','/')\"]");
                selenium.waitForPageToLoad("120000");
                break;
            }
            i++;
        }
        if (!selenium.isTextPresent(" (version " + version + ")")) {
            return false;
        }
        i = 1;
        while (index < resources.length) {
            found = false;
            i = 1;
            String resourceName = resources[index];
            while (selenium.isElementPresent("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]")) {
                if (selenium.getText("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]").equals(resourceName + ";version:" + version))
                    found = true;
                i++;
            }
            if (!found)
                return false;
            index++;

        }

        return true;
    }

    public boolean restoreVersion
            (
                    int version) throws InterruptedException {
        int i = 1;
        boolean found = false;
        clickViewVerions();
        while (selenium.isElementPresent("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[1]")) {
            if (selenium.getText("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[1]").equals(version + "")) {
                found = true;
                if (i == 1)
                    selenium.click("link=Restore");
                else
                    selenium.click("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[4]/a[2]");
                break;
            }
            i++;
        }
        if (!found)
            return false;
//        selenium.waitForPageToLoad("120000");
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (selenium.isElementPresent("messagebox-info")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        assertTrue(selenium.isTextPresent("Successfully restored to version :"));
        selenium.click("//button[@type='button']");
        return true;
    }

    public boolean checkVersionResourceProperties
            (
                    int version, String
                    resource, String
                    key, String
                    property) throws InterruptedException {
        int i = 1;
        boolean found = false;
        clickViewVerions();
        while (selenium.isElementPresent("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[1]")) {
            if (selenium.getText("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[1]").equals(version + "")) {
                if (i == 1)
                    selenium.click("link=Details");
                else
                    selenium.click("//a[@onclick=\"setAndGo('/;version:" + version + "','/')\"]");
                selenium.waitForPageToLoad("120000");
                break;
            }
            i++;
        }
        if (!(selenium.isTextPresent("(version " + version + ")"))) {
            return false;
        }

        found = false;
        i = 1;
        while (selenium.isElementPresent("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]")) {
            if (selenium.getText("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]").equals(resource + ";version:" + version)) {
                found = true;
                selenium.click("resourceView" + i);
                selenium.waitForPageToLoad("120000");
                break;
            }
            i++;
        }
        if (resource.equals("")) {
            if (checkProperties(key, property))
                found = true;
        }
        if (!found)
            return false;

        return true;
    }

    public void openResourceInMetaData
            (String
                    name) throws InterruptedException {
        selenium.click("link=" + name);
        selenium.waitForPageToLoad("120000");
    }

    public boolean checkVersionResourceComments
            (
                    int version, String
                    resource, String
                    comment, String
                    user) throws InterruptedException {
        int i = 1;
        boolean found = false;
        clickViewVerions();
        while (selenium.isElementPresent("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[1]")) {
            if (selenium.getText("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[1]").equals(version + "")) {
                if (i == 1)
                    selenium.click("link=Details");
                else
                    selenium.click("//a[@onclick=\"setAndGo('/;version:" + version + "','/')\"]");
                selenium.waitForPageToLoad("120000");
                break;
            }
            i++;
        }
        if (!(selenium.isTextPresent("(version " + version + ")"))) {
            return false;
        }

        found = false;
        i = 1;
        while (selenium.isElementPresent("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]")) {
            if (selenium.getText("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]").equals(resource + ";version:" + version)) {
                found = true;
                selenium.click("resourceView" + i);
                selenium.waitForPageToLoad("120000");
                break;
            }
            i++;
        }
        if (resource.equals("")) {
            if (checkComments(comment, user))
                found = true;
        }
        if (!found)
            return false;

        return true;
    }

    public boolean checkVersionResourceTags
            (
                    int version, String
                    resource, String
                    tag) throws InterruptedException {
        int i = 1;
        boolean found = false;
        clickViewVerions();
        while (selenium.isElementPresent("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[1]")) {
            if (selenium.getText("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[1]").equals(version + "")) {
                if (i == 1)
                    selenium.click("link=Details");
                else
                    selenium.click("//a[@onclick=\"setAndGo('/;version:" + version + "','/')\"]");
                selenium.waitForPageToLoad("120000");
                break;
            }
            i++;
        }
        if (!(selenium.isTextPresent("(version " + version + ")"))) {
            return false;
        }

        found = false;
        i = 1;
        while (selenium.isElementPresent("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]")) {
            if (selenium.getText("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]").equals(resource + ";version:" + version)) {
                found = true;
                selenium.click("resourceView" + i);
                selenium.waitForPageToLoad("120000");
                break;
            }
            i++;
        }
        if (resource.equals("")) {
            if (checkTags(tag))
                found = true;
        }
        if (!found)
            return false;

        return true;
    }

    public boolean checkVersionResourceRating
            (
                    int version, String
                    resource, int ratingVal,
                              double avgRating) throws InterruptedException {
        int i = 1;
        boolean found = false;
        clickViewVerions();
        while (selenium.isElementPresent("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[1]")) {
            if (selenium.getText("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[1]").equals(version + "")) {
                if (i == 1)
                    selenium.click("link=Details");
                else
                    selenium.click("//a[@onclick=\"setAndGo('/;version:" + version + "','/')\"]");
                selenium.waitForPageToLoad("120000");
                break;
            }
            i++;
        }
        if (!(selenium.isTextPresent("(version " + version + ")"))) {
            return false;
        }
        found = false;
        i = 1;
        while (selenium.isElementPresent("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]")) {
            if (selenium.getText("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]").equals(resource + ";version:" + version)) {
                found = true;
                selenium.click("resourceView" + i);
                selenium.waitForPageToLoad("120000");
                break;
            }
            i++;
        }
        if (resource.equals("")) {
            if (checkRating(ratingVal, avgRating))
                found = true;
        }
        if (!found)
            return false;

        return true;
    }

    public void editResourceContent
            (String
                    txt) throws InterruptedException {
        selenium.click("link=Edit as text");
        Thread.sleep(1000);
        selenium.type("editTextContentID", txt);
        selenium.click("saveContentButtonID");
        Thread.sleep(1000);
        selenium.click("link=Display as text");
        Thread.sleep(1000);
        assertTrue(txt.equals(selenium.getText("//div[@id='generalContentDiv']/textarea")));
    }

    public boolean checkResourceContent
            (String
                    txt) throws InterruptedException {
        selenium.click("link=Display as text");
        Thread.sleep(2000);
        if (selenium.isElementPresent("messagebox-warning")) {
            selenium.click("//button[@type='button']");
            return false;

        }

        return (txt.equals(selenium.getText("//div[@id='generalContentDiv']/textarea")));
    }

    public boolean checkVersionResourceContent
            (
                    int version, String
                    resource, String
                    txt) throws InterruptedException {
        int i = 1;
        boolean found = false;
        clickViewVerions();
        while (selenium.isElementPresent("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[1]")) {
            if (selenium.getText("//table[@id='versionsTable']/tbody/tr[" + i + "]/td[1]").equals(version + "")) {
                if (i == 1)
                    selenium.click("link=Details");
                else
                    selenium.click("//a[@onclick=\"setAndGo('/;version:" + version + "','/')\"]");
                selenium.waitForPageToLoad("120000");
                break;
            }
            i++;
        }
        if (!(selenium.isTextPresent("(version " + version + ")"))) {
            return false;
        }
        found = false;
        i = 1;
        while (selenium.isElementPresent("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]")) {
            if (selenium.getText("//td[@id='actionPaneHelper" + i + "']/table/tbody/tr/td[1]").equals(resource + ";version:" + version)) {
                found = true;
                selenium.click("resourceView" + i);
                selenium.waitForPageToLoad("120000");
                break;
            }
            i++;
        }
        if (resource.equals("")) {
            if (checkResourceContent(txt))
                found = true;
        }
        if (!found)
            return false;

        return true;
    }
}



