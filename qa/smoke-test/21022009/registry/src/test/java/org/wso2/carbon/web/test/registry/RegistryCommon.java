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

package org.wso2.carbon.web.test.registry;

import java.awt.event.KeyEvent;


public class RegistryCommon extends CommonSetup {
    public RegistryCommon(String text) {
        super(text);
    }

    public static boolean rateResource(String resourcePath, int ratingValue, double expectedAverageRating)
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

    public static void addTextResourceToRoot(String resourcePath) throws InterruptedException {

        boolean value = false;

        selenium.click("link=Resources");
        selenium.waitForPageToLoad("30000");
        String resourceName = removeChar(resourcePath, '/');
        assertTrue(selenium.isTextPresent("Resources"));
        selenium.click("link=Add Resource");
        assertTrue(selenium.isTextPresent("Add Resource"));
        Thread.sleep(1000);
        selenium.select("addMethodSelector", "label=Text content");
        selenium.click("//option[@value='text']");
        selenium.type("trFileName", resourceName);
        selenium.type("trDescription", "resourceNameDesc");
        selenium.click("//input[@value='Add' and @type='button' and @onclick='whileUpload();submitTextContentForm();']");
        Thread.sleep(1000);
        assertTrue("resource doesn't exists at the registry", resourceExists(resourceName));
    }


    public static boolean deleteResource(String resourcePath, int ActionLinkId) throws InterruptedException {

        boolean value = false;
        selenium.click("link=Resources");
        selenium.waitForPageToLoad("30000");
        clickRootIcon();
        String resourceName = removeChar(resourcePath, '/');
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent(resourceName));
        Thread.sleep(1000);
        selenium.click("actionLink" + ActionLinkId);
        Thread.sleep(1000);
        selenium.click("//a[@onclick=\"hideOthers(" + ActionLinkId + ",'del');deleteResource(\'" +
                resourcePath + "\', '/')\"]");
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

    public static boolean deleteCollection(String resourcePath, int ActionLinkId) throws InterruptedException {

        boolean value = false;
        selenium.click("link=Resources");
        selenium.waitForPageToLoad("30000");
        String resourceName = removeChar(resourcePath, '/');
        resourceName = "/" + resourceName;
        System.out.println(resourceName);

        clickRootIcon();

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

    public static boolean clickRootIcon() {

        boolean value = false;

        selenium.click("//div[@id='workArea']/div/table/tbody/tr/td[1]/a/img");
        selenium.waitForPageToLoad("30000");
        if ((selenium.isTextPresent("root"))) {
            value = true;
            return value;
        }
        return value;
    }

    public static boolean addProperty(String key, String protValue) {

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

    public static boolean editProperty(String key, String protValue, int panelPlace) throws InterruptedException {

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

    public static boolean deleteProperty(String protValue, String key) throws InterruptedException {

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

    public static boolean addCollectionToRoot(String collectionName, String mediaType, String colDescription) throws InterruptedException {

        boolean value;

        selenium.click("link=Add Collection");
        selenium.type("collectionName", collectionName);
        selenium.type("mediaType", mediaType);
        selenium.type("colDesc", colDescription);
        Thread.sleep(1000);
        selenium.click("//input[@value='Add' and @type='button' and @onclick='submitCollectionAddForm()']");
        Thread.sleep(1000);
        if (resourceExists(collectionName)) {
            value = false;
        } else {
            value = true;
            return value;
        }
        return value;
    }

    public static boolean verifyPropertyCount(int count, String label) throws InterruptedException {

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

    public static boolean gotoResourcePage() {
        boolean value;

        selenium.click("link=Resources");
        selenium.waitForPageToLoad("30000");

        if (selenium.isTextPresent("root") && (selenium.isTextPresent("Resources"))) {
            value = false;
        } else {
            value = true;
            return value;
        }
        return value;
    }

    public static boolean searchProproties(String key, String value, String resourcePath, String userName)
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

    public static boolean addTags(String tagName, String tagId) throws InterruptedException {

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

    public static boolean tagLinkNavigation(String tagName, int tagCount, String resourceName) throws InterruptedException {

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

    public static boolean basicTagSearch(String resourcePath, String tagValue) throws InterruptedException {

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

    public static boolean addComment(String commentValue, String commentedByUser, String resourceName) throws InterruptedException {

        boolean value = false;

        selenium.click("commentsIconMinimized");
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

    public static boolean searchSimpleCommet(String path, String comment) throws Exception {

        boolean value = false;
        selenium.click("link=Search");
        selenium.waitForPageToLoad("30000");
        selenium.type("#_comments", comment);
        selenium.click("#_0");
        Thread.sleep(1000);

        if ((selenium.isTextPresent("Search Results") && selenium.isTextPresent(path) && selenium.isTextPresent("Search Again"))) {
            value = true;
        }

        return value;
    }

    public static boolean searchSimpleTag(String path, String tag) throws Exception {

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

    public static void addtResourceFromFileSystem(String path, String resourceName, int rowCount) throws Exception {

        selenium.click("link=Add Resource");
        selenium.type("uResourceFile", path);
        Thread.sleep(1000);
        selenium.click("//input[@value='Add' and @type='button' and @onclick='whileUpload();submitUploadContentForm();']");
        selenium.waitForPageToLoad("30000");
        assertTrue("resource doesn't exists at the registry", resourceExists(resourceName));
    }

    public static boolean addtResourceFromUrl(String resourceUrl, String resourceName, int rowCount) throws Exception {

        boolean value = false;

        selenium.click("link=Add Resource");
        selenium.select("addMethodSelector", "label=Import content from URL");
        selenium.type("irFetchURL", resourceUrl);
        selenium.type("irDescription", "test");
        selenium.click("//input[@value='Add' and @type='button' and @onclick='whileUpload();submitImportContentForm();']");


        String readResource = "";

        int i = 1;

        while (!readResource.equals(resourceName)) {

            Thread.sleep(1000);
            readResource = selenium.getTable("//td[@id='actionPaneHelper" + i + "']/table.0.0");
            readResource = removeTruncateCharacter(readResource);
            int index1 = resourceName.indexOf(readResource);

            if (index1 != -1) {
                value = true;
                break;
            }

            i = i + 1;

            if (i == rowCount + 1) {
                assertTrue("Resource can not be found in registry browser", value);
                break;
            }
        }
        return value;
    }

    public static boolean searchResources(String path) throws Exception {

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

    public static void importWsldMexTool(String mediatype, String wsdlUrl, String wsdlName, String owner,
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

    public static String removeChar(String s, char c) {

        String r = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) r += s.charAt(i);
        }
        return r;
    }

    public static String removeTruncateCharacter(String s) {

        int index1 = s.indexOf("..");
        String r = s;
        if (index1 != -1) {
            int beginChar = s.length() - 2;
            r = s.substring(0, beginChar);
        }
        return r;
    }


    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);

    }

    public static boolean resourceExists(String resourceName) throws InterruptedException {

        boolean value = false;

        String readResource = "";

        int i = 1;

        while (!readResource.equals(resourceName)) {

            Thread.sleep(1000);
            readResource = selenium.getTable("//td[@id='actionPaneHelper" + i + "']/table.0.0");
            readResource = removeTruncateCharacter(readResource);
            System.out.println(readResource);
            System.out.println(resourceName);
            int index1 = resourceName.indexOf(readResource);

            if (index1 != -1) {
                value = true;
                break;
            }

            int a = i + 1;
            if (selenium.getTable("//td[@id='actionPaneHelper" + a + "']/table.0.0") != null) {
                i = i + 1;
            } else {
                assertTrue("Resource can not be found in registry browser", value);
                break;
            }

//            if (i == rowCount + 1) {
//                assertTrue("Resource can not be found in registry browser", value);
//                break;
//            }
        }
        return value;
    }

    public static int getActionID(String resourceName) throws InterruptedException {

        int actionID = 0;
        boolean value = false;

        String readResource = "";

        int i = 1;

        while (!readResource.equals(resourceName)) {

            Thread.sleep(1000);
            readResource = selenium.getTable("//td[@id='actionPaneHelper" + i + "']/table.0.0");
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
            if (selenium.getTable("//td[@id='actionPaneHelper" + a + "']/table.0.0") != null) {
                i = i + 1;
            } else {
                actionID = 0;
                assertTrue("Resource can not be found in registry browser", value);
                break;
            }

        }
        return actionID;
    }

    public static void gotoResource(String resourceName) throws InterruptedException {

        int actionID = getActionID(resourceName);
        selenium.click("resourceView" + actionID);
        selenium.waitForPageToLoad("30000");
        assertTrue("Resource doesn't exists", selenium.isTextPresent(resourceName));
    }

    public static void createCheckpoint(String resourceName) throws InterruptedException {

        selenium.click("//a[contains(text(),'Create\n                                Checkpoint')]");
        Thread.sleep(1000);
        assertTrue(selenium.isTextPresent("Check point created successfully"));
        selenium.click("//button[@type='button']");
    }

    public static void checkResourceVersions(String resourceName, String userName) {

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

    public static void checkCollectionVersions(String resourceName, String userName) {

        selenium.click("link=View versions");
        selenium.waitForPageToLoad("30000");

        assertTrue(selenium.isTextPresent(resourceName));
        assertTrue(selenium.isTextPresent("No versions available for this resource or collection."));

        String actionscolumn = selenium.getText("//table[@id='versionsTable']/thead/tr/th[4]");
        System.out.println("action Column: " + actionscolumn);
    }

    public static void signOut() {

        if (selenium.isTextPresent("Sign-out") || selenium.isTextPresent("@")) {
            selenium.click("link=Sign-out");
            selenium.waitForPageToLoad("30000");
        }
    }

    public static void copyResource(int actionId, String resourcePath, String destinationPath) throws InterruptedException {

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
            selenium.click("//input[@value='Copy' and @type='button' and @onclick=\"copyResource('/', '" + resourcePath + "','copy_destination_path" + actionId + "','"+resourceNewPath+"',1)\"]");
            Thread.sleep(1000);
        }
    }
}
