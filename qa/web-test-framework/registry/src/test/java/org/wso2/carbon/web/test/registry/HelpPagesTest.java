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

package org.wso2.carbon.web.test.registry;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.util.Properties;


public class HelpPagesTest extends TestCase {

    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        selenium = BrowserInitializer.getBrowser();
        registryCommon = new RegistryCommon(selenium);
        UmCommon = new SeleniumTestBase(selenium);
        adminUserName = property.getProperty("admin.username");
        adminPassword = property.getProperty("admin.password");
    }

    public HelpPagesTest(String text) {
        super(text);
    }

    public void testHomePageHelpLinks() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        UmCommon.logOutUI();
        assertTrue(selenium.isTextPresent("WSO2 Carbon user guide."));
        assertTrue(selenium.isTextPresent("Sign-in"));
        assertEquals("Help", selenium.getText("link=Help"));
        assertEquals("Management Console\n \n Sign-in | Docs | About", selenium.getTable("main-table.0.0"));
        assertTrue(selenium.isTextPresent("Management Console"));
        selenium.click("link=Help");

        String helpWinId = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(helpWinId);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("WSO2 Carbon Server Home Page"));
        assertTrue(selenium.isTextPresent("Operating System"));
        assertTrue(selenium.isTextPresent("Server"));
        assertTrue(selenium.isTextPresent("Registry"));
        assertTrue(selenium.isTextPresent("Java VM"));
        assertTrue(selenium.isTextPresent("Operating System User"));

        selenium.close();

        String[] win;
        String winTitle;
        win = selenium.getAllWindowTitles();
        winTitle = win[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        selenium.windowFocus();
        assertEquals("Sign-in Help", selenium.getText("link=Sign-in Help"));
        selenium.click("link=Sign-in Help");

        String signInHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(signInHelp);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("Sign-In"));
        assertEquals("", selenium.getText("//img[@alt='Sign-In Panel']"));
        assertTrue(selenium.isTextPresent("To access this page, in the navigator, under Configure, click User Store"));
        assertTrue(selenium.isTextPresent("Sign-In"));
        assertTrue(selenium.isTextPresent("In the Sign-In panel, provide the user name and the password of your account" +
                " and the server you want to sign in. Your user name and password will be authenticated, and you will " +
                "authorized to perform functions according to your role"));


        selenium.windowFocus();
        selenium.close();

        String[] winSignIn;
        winSignIn = selenium.getAllWindowTitles();

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winSignIn[0]);
        }

        assertEquals("Management Console\n \n Sign-in | Docs | About", selenium.getTable("main-table.0.0"));
        assertEquals("Help", selenium.getText("link=Help"));
        assertEquals("About", selenium.getText("link=About"));
        assertEquals("Docs", selenium.getText("link=Docs"));
        assertEquals("Sign-in Help", selenium.getText("link=Sign-in Help"));

        selenium.click("link=About");

        String aboutWin = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(aboutWin);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("About WSO2 Carbon"));
        assertTrue(selenium.isTextPresent("WSO2 Carbon is a component based Enterprise SOA platform. The design of" +
                " WSO2 Carbon focuses on separating the key functionality of the SOA platform into separate pluggable" +
                " Carbon components that can be mixed and matched, like customizable building blocks. This allows you " +
                "to add only the functionality you need to start up, and continue to add product capabilities as your " +
                "requirements grow. This helps a business to quickly adapt to changes"));
        assertTrue(selenium.isTextPresent("Need more help?"));

        selenium.close();

        String[] winFocus;
        String winTitle2;
        winFocus = selenium.getAllWindowTitles();
        winTitle2 = winFocus[0];

        if (winTitle2.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle2);
        }
    }

    public void testHomePageHelpLinksAfterLogin() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        assertTrue(selenium.isTextPresent("WSO2 Governance Registry Home"));
        assertTrue(selenium.isTextPresent("Welcome to the WSO2 Governance Registry Management Console"));
        assertTrue(selenium.isTextPresent("Operating System"));
        assertTrue(selenium.isTextPresent("Server"));
        assertTrue(selenium.isTextPresent("Registry"));
        assertTrue(selenium.isTextPresent("Java VM"));
        assertTrue(selenium.isTextPresent("Operating System User"));

        assertTrue(selenium.isTextPresent("Management Console"));
        selenium.click("link=Help");

        String helpWinId = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(helpWinId);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("WSO2 Carbon Server Home Page"));
        assertTrue(selenium.isTextPresent("Operating System"));
        assertTrue(selenium.isTextPresent("Server"));
        assertTrue(selenium.isTextPresent("Registry"));
        assertTrue(selenium.isTextPresent("Java VM"));
        assertTrue(selenium.isTextPresent("Operating System User"));

        selenium.close();

        String[] win;
        String winTitle;
        win = selenium.getAllWindowTitles();
        winTitle = win[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        assertEquals("Sign-out", selenium.getText("link=Sign-out"));
        assertTrue(selenium.isTextPresent("Signed-in as:"));
        assertEquals("Help", selenium.getText("link=Help"));
        assertEquals("About", selenium.getText("link=About"));
        assertEquals("Docs", selenium.getText("link=Docs"));

        selenium.click("link=About");

        String aboutWin = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(aboutWin);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("About WSO2 Carbon"));
        assertTrue(selenium.isTextPresent("WSO2 Carbon is a component based Enterprise SOA platform. The design " +
                "of WSO2 Carbon focuses on separating the key functionality of the SOA platform into separate " +
                "pluggable Carbon components that can be mixed and matched, like customizable building blocks. " +
                "This allows you to add only the functionality you need to start up, and continue to add product " +
                "capabilities as your requirements grow. This helps a business to quickly adapt to changes"));
        assertTrue(selenium.isTextPresent("Need more help?"));

        selenium.close();

        String[] winFocus;
        String winTitle2;
        winFocus = selenium.getAllWindowTitles();
        winTitle2 = winFocus[0];

        if (winTitle2.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle2);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }


    public void testUserManagementHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        Thread.sleep (1000);
        registryCommon.gotoUserManagementPage();
        Thread.sleep (1000);

        assertTrue(selenium.isTextPresent("User Management"));
        assertTrue(selenium.isTextPresent("System User Store"));
        assertTrue(selenium.isTextPresent("External User Store"));

        assertEquals("Users", selenium.getText("link=Users"));
        assertEquals("Roles", selenium.getText("link=Roles"));
        assertTrue(selenium.isTextPresent("Add External User Store"));

        selenium.click("link=Help");

        String umHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(umHelp);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("User Management"));
        assertTrue(selenium.isTextPresent("The User Management component of the WSO2 Carbon facilitates the management " +
                "and control of user accounts and user roles at different levels. The key functionalities of this component include:"));

        assertTrue(selenium.isTextPresent("Managing User Accounts"));
        assertTrue(selenium.isElementPresent("//img"));
        assertTrue(selenium.isTextPresent("Managing User Roles"));
        assertTrue(selenium.isElementPresent("//p[1]/img"));

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testKeyStoreHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoKeyStorePage();

        assertTrue(selenium.isTextPresent("Key Store Management"));
        assertTrue(selenium.isTextPresent("wso2carbon.jks"));
        assertEquals("Add New Key store", selenium.getText("link=Add New Key store"));
        assertTrue(selenium.isTextPresent("Key Store Management"));

        selenium.click("link=Help");

        String keySotreHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(keySotreHelp);

        selenium.windowFocus();

        assertTrue(selenium.isElementPresent("//img"));
        assertTrue(selenium.isTextPresent("Adding a New Keystore"));
        assertTrue(selenium.isTextPresent("WSO2 Carbon keystore management provides the " +
                "facility to manage multiple keystores. It supports two types of Keystores."));
        assertTrue(selenium.isElementPresent("//p[7]/img"));
        assertTrue(selenium.isTextPresent("Note: Keystore management does not let you import an existing " +
                "private key for which you already have a certificate."));

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testLoggingHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoLoggingPage();

        assertTrue(selenium.isTextPresent("Logging Configuration"));
        assertTrue(selenium.isTextPresent("Persist All Configuration Changes"));
        assertTrue(selenium.isTextPresent("Configure Log4J Appenders"));
        assertTrue(selenium.isTextPresent("Configure Log4J Loggers"));
        assertTrue(selenium.isTextPresent("Logger"));

        selenium.click("link=Help");

        String loggingHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(loggingHelp);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("Configure Logging"));
        assertTrue(selenium.isTextPresent("This page shows the existing Log4j configuration. And it also allows you " +
                "to modify existing configuration. You can either modify the global Log4j configuration, an Appender or " +
                "a Logger. If you select Persist all Configurations Changes check box, all the modifications will be " +
                "persited and they will be available even after a server restart."));
        assertTrue(selenium.isTextPresent("Configuring Log4J Loggers"));
        assertTrue(selenium.isElementPresent("//div[@id='middle']/ol/p[2]/img"));
        assertTrue(selenium.isTextPresent("There are two ways of configuring Log4j in WSO2 Carbon. Either by manually " +
                "editing the log4j.properties file or through the management console. Changes you made to Log4j " +
                "configuration through the management console are persisted in WSO2 Registry, therefore those " +
                "changes will be available after server restarts. There is also an option to restore the original " +
                "Log4j configuration from the log4j.properties file using the management console. But if you modify " +
                "the log4j.properties file and restart the server, you will see the changes you've made. That means " +
                "the earlier log4j configuration persisted in the registry is overwritten"));

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testserverManagementHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoshutDownRestartPage();

        assertTrue(selenium.isTextPresent("Shutdown/Restart Server"));
        assertTrue(selenium.isTextPresent("Shutdown"));
        assertTrue(selenium.isTextPresent("Restart"));
        assertTrue(selenium.isTextPresent("Graceful Shutdown"));
        assertTrue(selenium.isTextPresent("Stop accepting new requests, continue to process already received requests, " +
                "and then shutdown the server."));
        assertTrue(selenium.isTextPresent("Forced Shutdown"));
        assertTrue(selenium.isTextPresent("Discard any requests currently being processed and immediately shutdown the " +
                "server."));
        assertTrue(selenium.isTextPresent("Forced Restart"));
        assertTrue(selenium.isTextPresent("Discard any requests currently being processed and immediately restart the " +
                "server."));

        selenium.click("link=Help");

        String serverHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(serverHelp);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("Server Administration"));
        assertTrue(selenium.isTextPresent("You can use the Shutdown/Restart feature to shutdown and restart the server." +
                " The machine can be shutdown gracefully or forcefully."));
        assertTrue(selenium.isTextPresent("Each option is described on the Shutdown/Restart page."));
        assertTrue(selenium.isElementPresent("//img[@alt='Server Admin']"));

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }


    public void testAddServiceOperationHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoServicePage();

        assertTrue(selenium.isTextPresent("Service Operations"));

        selenium.click("link=Help");

        String serviceOperationsHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(serviceOperationsHelp);

        selenium.windowFocus();

        metaDataAddHelpPageTest();

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAddPolicyHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoPolicyPage();

        assertTrue(selenium.isTextPresent("Policy Operations"));

        selenium.click("link=Help");

        String policyOperationsHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(policyOperationsHelp);

        selenium.windowFocus();

        metaDataAddHelpPageTest();

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAddWsdlHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoWSDLPage();

        assertTrue(selenium.isTextPresent("WSDL Operations"));

        selenium.click("link=Help");

        String wsdlOperationsHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(wsdlOperationsHelp);

        selenium.windowFocus();

        metaDataAddHelpPageTest();

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAddSchemaHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoSchemaPage();

        assertTrue(selenium.isTextPresent("Schema Operations"));

        selenium.click("link=Help");

        String schemaOperationsHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(schemaOperationsHelp);

        selenium.windowFocus();

        metaDataAddHelpPageTest();

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAddServiceListHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoServicesPage();

        assertTrue(selenium.isTextPresent("Service List"));

        selenium.click("link=Help");

        String serviceListHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(serviceListHelp);

        selenium.windowFocus();

        metaDataListHelpPageTest();

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAddPolicyListHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoPoliciesPage();

        assertTrue(selenium.isTextPresent("Policy List"));

        selenium.click("link=Help");

        String policyOperationListsHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(policyOperationListsHelp);

        selenium.windowFocus();

        metaDataListHelpPageTest();

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAddWsdlListHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoWSDLsPage();

        assertTrue(selenium.isTextPresent("WSDL List"));

        selenium.click("link=Help");

        String wsdlOperationsListHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(wsdlOperationsListHelp);

        selenium.windowFocus();

        metaDataListHelpPageTest();

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testAddSchemaListHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoSchemasPage();

        assertTrue(selenium.isTextPresent("Schema List"));

        selenium.click("link=Help");

        String schemaOperationsListHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(schemaOperationsListHelp);

        selenium.windowFocus();

        metaDataListHelpPageTest();

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testBrowseHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoResourcePage();

        assertTrue(selenium.isTextPresent("Browse"));

        selenium.click("link=Help");

        String browseHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(browseHelp);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("Registry Resources"));
        assertTrue(selenium.isTextPresent("The WSO2 Registry is a repository that stores resources in a structured way. " +
                "Here are some of the features provided by the registry interface."));
        assertTrue(selenium.isTextPresent("Registry User Interface"));
        assertTrue(selenium.isElementPresent("//img"));
        assertTrue(selenium.isTextPresent("Content Panel"));
        assertTrue(selenium.isTextPresent("Adding a subscription involves selecting the event type and the notification " +
                "method. The event types supported by default are as follows:"));
        assertTrue(selenium.isTextPresent("Based on the notification method you select, you may be required to provide " +
                "additional information, such as the e-mail address to use of the name of the user of whom the profile " +
                "will be used. Having done this step, you will have to click on the Subscribe button to add a subscription " +
                "(see Figure above)."));

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testActivitiesHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoActivityPage();

        assertTrue(selenium.isTextPresent("Activities"));
        assertTrue(selenium.isTextPresent("Search Activities"));

        selenium.click("link=Help");

        String activitiesHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(activitiesHelp);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("Registry Activities"));
        assertTrue(selenium.isTextPresent("All registry related activities can be searched through this interface. " +
                "Your search can be refined by providing optional fields, username, resource path and date range."));
        assertTrue(selenium.isElementPresent("//p[5]/img"));
        assertTrue(selenium.isTextPresent("Figure 3: Filter option."));

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testSearchHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoSearchPage();

        assertTrue(selenium.isTextPresent("Search"));
        assertTrue(selenium.isTextPresent("Search for Resources"));
        assertTrue(selenium.isTextPresent("Resource Name"));

        selenium.click("link=Help");

        String searchHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(searchHelp);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("Registry Search"));
        assertTrue(selenium.isTextPresent("All resouces found in the registry, can be searched through this interface. " +
                "Search could be refined by optionally providing, resource name, created date range, updated date range, " +
                "tags, comments, property name, property value and the content."));
        assertTrue(selenium.isElementPresent("//img"));
        assertTrue(selenium.isTextPresent("Created or Updated dates can be either entered in the format of MM/DD/YYYY or " +
                "used the date picker user interface provided."));
        assertTrue(selenium.isElementPresent("//p[5]/img"));
        assertTrue(selenium.isTextPresent("Figure 2: Date picker user interface"));

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testNotificationsHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoNotificationPage();

        assertTrue(selenium.isTextPresent("Manage Notifications"));

        selenium.click("link=Help");

        String notificationsHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(notificationsHelp);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("Notifications"));
        assertTrue(selenium.isTextPresent("The Notifications feature can be used to generate notifications for events " +
                "that occur as a result of performing operations. In order to receive notifications, users will have to " +
                "create a subscription to a particular event along with a specified notification method (which can be an " +
                "e-mail alert or a SOAP message). Each subscription is associated with a resource or collection on the " +
                "registry. The Manage Notifications page can be used to create, edit, or delete subscriptions to " +
                "notifications on various operations performed on resources and collections stored in the registry."));
        assertTrue(selenium.isElementPresent("//img"));
        assertTrue(selenium.isElementPresent("//h2[3]"));
        assertTrue(selenium.isElementPresent("//p[18]"));
        assertTrue(selenium.isElementPresent("//p[17]/img"));

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testLifeCycleHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoLifeCyclePage();

        assertTrue(selenium.isTextPresent("Lifecycles"));

        selenium.click("link=Help");

        String lifecyclesHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(lifecyclesHelp);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("View existing lifecycle configurations"));
        assertTrue(selenium.isTextPresent("Adding a lifecycle configuration"));
        assertTrue(selenium.isTextPresent("Step by Step guide to configuration"));
        assertTrue(selenium.isElementPresent("//p[9]/img"));

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testSystemLogsHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotosystemLogsPage();

        assertTrue(selenium.isTextPresent("Search Logs"));

        selenium.click("link=Help");

        String systemLogsHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(systemLogsHelp);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("Monitor Logs"));
        assertTrue(selenium.isTextPresent("This page displays all the system logs. You can also search for a particular " +
                "log using the Search Logs feature."));
        assertTrue(selenium.isTextPresent("The log messages, displayed in this page are obtained from a memory appender. " +
                "Hence, the severity(log level) of the displayed log messages are equal or higher than the threshold of " +
                "the memory appender. For more information on appenders, loggers and their log levels, please visit here."));
        assertTrue(selenium.isTextPresent("Log files on disk"));
        assertTrue(selenium.isTextPresent("The location of the log files on disk is specified in the log4j.configuration " +
                "file."));

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void testMyProfileHelpPage() throws Exception {

        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);

        registryCommon.gotoMyprofile();

        assertTrue(selenium.isTextPresent("My Profiles"));

        selenium.click("link=Help");

        String myprofileHelp = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(myprofileHelp);

        selenium.windowFocus();

        assertTrue(selenium.isTextPresent("User Profile Management"));
        assertTrue(selenium.isTextPresent("The User Profile Management component of the WSO2 Carbon facilitates the " +
                "management and control of user's details. The key functionalities of this component include:"));
        assertTrue(selenium.isTextPresent("Use the Add New Profile link to add new user accounts. To delete profiles " +
                "use Delete link. The default profile of a user cannot be deleted. Click on the profile name to add/edit " +
                "details of the profile."));
        assertTrue(selenium.isElementPresent("//img"));
        assertTrue(selenium.isElementPresent("//p[6]/img"));

        selenium.windowFocus();
        selenium.close();

        String[] winFocus;
        String winTitle;
        winFocus = selenium.getAllWindowTitles();
        winTitle = winFocus[0];

        if (winTitle.equalsIgnoreCase("WSO2 Management Console")) {
            selenium.selectWindow(winTitle);
        }

        Thread.sleep(1000);
        UmCommon.logOutUI();
    }

    public void metaDataAddHelpPageTest() {

        assertTrue(selenium.isTextPresent("Governance Registry Service Metadata"));
        assertTrue(selenium.isTextPresent("WSO2 Governance Registry is providing complete Service Metadata Management " +
                "features with Release 3.0.0 to do a better governance in your SOA system. In the new left panel there" +
                " are set of operations supported in order to manage service metadata efficiently."));
        assertTrue(selenium.isTextPresent("Add Service"));
        assertTrue(selenium.isTextPresent("This service import method is preferred when user doesn't have a proper " +
                "description about the service with a WSDL but we provide a field to import WSDL in to Governance " +
                "Registry. When user click on Add Service link user will be given a form to fill. Important thing in " +
                "this form is user can simply reconfigure the default configuration."));
        assertTrue(selenium.isElementPresent("//img"));
        assertTrue(selenium.isTextPresent("Add Schema"));
        assertTrue(selenium.isTextPresent("Like WSDL importing Governance Registry allow users to add Schema in to " +
                "registry using add Schema UI. User have to give the schema location in to Schema URL and Schema" +
                " name will be filling very similar in Add WSDL and Add Policy forms. Successful schema import" +
                " will redirect in to currently available imported schema listing page."));
        assertTrue(selenium.isTextPresent("Similar to WSDLs, WSO2 Governance Registry performs Schema validations " +
                "on the wsdl provided once importing is done. The result is displayed under \"properties\" section of" +
                " the imported Schema resource."));
    }

    public void metaDataListHelpPageTest() {

        assertTrue(selenium.isTextPresent("Governance Registry Service Metadata Management"));
        assertTrue(selenium.isTextPresent("With metadata management we are allowing users to manage the imported " +
                "metadata as resources management in the Governance Registry since all the information are stored as " +
                "resource information inside the Governance Registry. In the Metadata list part users can list imported " +
                "resources, WSDLs, Schemas and Policies"));
        assertTrue(selenium.isTextPresent("List Service"));
        assertTrue(selenium.isTextPresent("In the previous figure you can see two services one imported using " +
                "add-Service and other imported using Add-WSDL. User can click the service imported using WSDL " +
                "and go to edit Service UI like following figure."));
        assertTrue(selenium.isElementPresent("//p[6]/img"));
        assertTrue(selenium.isTextPresent("List other metadata"));
        assertTrue(selenium.isElementPresent("//p[10]/img"));
        assertTrue(selenium.isTextPresent("Listing other metadata like Policies, Schemas and WSDL's are " +
                "similar to listing services instead it contains a link to view the dependencies between " +
                "resources. As example there's dependency between WSDL resource and it's service resource " +
                "and WSDL resource will be having dependencies with some schema resources. Those dependencies " +
                "can be viewed if user click view dependencies. And user can traverse to those dependencies by " +
                "clicking on dependencies links."));
    }

}
