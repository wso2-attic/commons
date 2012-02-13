package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.*;

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

public class ESBHomePage extends CommonSetup{

    public ESBHomePage(String text) {
        super(text);
    }

    /*
	 * This method will verify the ESB Login page
	 */
    public void testVerifyHomePage() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        //Signing in
        seleniumTestBase.loginToUI("admin","admin");

        //Verify whether the ESB logo is available
        assertEquals("", selenium.getText("//div[@id='header-div']/div[2]/a/img"));
		assertTrue(selenium.isElementPresent("//div[@id='header-div']/div[2]/a/img"));

        //Verify whether the Management Console text
        assertEquals("Management Console", selenium.getText("//div[@id='header-div']/div[1]"));
		assertTrue(selenium.isElementPresent("//div[@id='header-div']/div[1]"));

        //Verify whether the About link is available and whether it is valid
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testDocLinks("About","About WSO2 Carbon","//h2[1]");

        //Verify whether the Docs link is available and whether it is valid        
        esbCommon.testDocLinks("Docs",null,null);


        //Verify whether the Sign-in text is valid
        assertEquals("admin@localhost:9443", selenium.getText("logged-user"));

        //Verify whether the Sign-out link is available and whether it is valid        
        assertEquals("Sign-out", selenium.getText("link=Sign-out"));
		assertTrue(selenium.isElementPresent("link=Sign-out"));

        seleniumTestBase.logOutUI();

		//Signing in
        seleniumTestBase.loginToUI("admin","admin");

        //Verify whether the Help link is available and whether it is valid
        esbCommon.testDocLinks("Help","WSO2 Carbon Server Home Page","//h1");

        //Verifying the home page content
        assertEquals("WSO2 ESB Home", selenium.getText("//div[@id='middle']/h2"));
		assertEquals("Welcome to the WSO2 ESB Management Console", selenium.getText("//div[@id='middle']/p[1]"));
		assertEquals("Server", selenium.getText("//table[@id='systemInfoTable']/thead/tr/th"));
		assertEquals("Host", selenium.getText("//table[@id='systemInfoTable']/tbody/tr[1]/td[1]"));
		assertEquals("Server URL", selenium.getText("//table[@id='systemInfoTable']/tbody/tr[2]/td[1]"));
		assertEquals("Server Start Time", selenium.getText("//table[@id='systemInfoTable']/tbody/tr[3]/td[1]"));
		assertEquals("System Up Time", selenium.getText("//table[@id='systemInfoTable']/tbody/tr[4]/td[1]"));
		assertEquals("Version", selenium.getText("//table[@id='systemInfoTable']/tbody/tr[5]/td[1]"));
		assertEquals("Repository Location", selenium.getText("//table[@id='systemInfoTable']/tbody/tr[6]/td[1]"));
		assertEquals("Operating System", selenium.getText("//table[@id='serverTable']/thead/tr/th"));
		assertEquals("OS Name", selenium.getText("//table[@id='serverTable']/tbody/tr[1]/td[1]"));
		assertEquals("OS Version", selenium.getText("//table[@id='serverTable']/tbody/tr[2]/td[1]"));
		assertEquals("Operating System User", selenium.getText("//table[@id='userTable']/thead/tr/th"));
		assertEquals("Country", selenium.getText("//table[@id='userTable']/tbody/tr[1]/td[1]"));
		assertEquals("Home", selenium.getText("//table[@id='userTable']/tbody/tr[2]/td[1]"));
		assertEquals("Name", selenium.getText("//table[@id='userTable']/tbody/tr[3]/td[1]"));
		assertEquals("Timezone", selenium.getText("//table[@id='userTable']/tbody/tr[4]/td[1]"));
		assertEquals("Java VM", selenium.getText("//table[@id='vmTable']/thead/tr/th"));
		assertEquals("Java Home", selenium.getText("//table[@id='vmTable']/tbody/tr[1]/td[1]"));
		assertEquals("Java Runtime Name", selenium.getText("//table[@id='vmTable']/tbody/tr[2]/td[1]"));
		assertEquals("Java Version", selenium.getText("//table[@id='vmTable']/tbody/tr[3]/td[1]"));
		assertEquals("Java Vendor", selenium.getText("//table[@id='vmTable']/tbody/tr[4]/td[1]"));
		assertEquals("Java VM Version", selenium.getText("//table[@id='vmTable']/tbody/tr[5]/td[1]"));
		assertEquals("© 2008 - 2009 WSO2 Inc. All Rights Reserved.", selenium.getText("//div[@id='footer-div']/div/div"));

        //Verifying the Home link of the left menu
        assertEquals("Home", selenium.getText("link=Home"));
		assertTrue(selenium.isElementPresent("link=Home"));
        selenium.click("link=Home");
        selenium.waitForPageToLoad("30000");
        assertEquals("WSO2 ESB Home", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Configure link of the left menu
        assertEquals("Configure", selenium.getText("//div[@id='menu']/ul/li[2]"));

        //Verifying the Configure > User Management link of the left menu        
        assertEquals("User Management", selenium.getText("link=User Management"));
		selenium.click("link=User Management");
		selenium.waitForPageToLoad("30000");
		assertEquals("> User Management", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[3]"));
		assertEquals("User Management", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Configure > Key Stores link of the left menu
		assertEquals("Key Stores", selenium.getText("link=Key Stores"));
		selenium.click("link=Key Stores");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Key Stores", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[3]"));
		assertEquals("Key store Management", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Configure > Logging link of the left menu
        assertEquals("Logging", selenium.getText("link=Logging"));
		selenium.click("link=Logging");
		selenium.waitForPageToLoad("30000");
		assertEquals("Home", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[1]/a"));
		assertEquals("Logging Configuration", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Configure > Data sources link of the left menu
        assertEquals("Data Sources", selenium.getText("link=Data Sources"));
		selenium.click("link=Data Sources");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Configure", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));
		assertEquals("Data Sources", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Configure > Event Stores link of the left menu
        assertEquals("Event Sources", selenium.getText("link=Event Sources"));
		selenium.click("link=Event Sources");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Configure", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));
		assertEquals("Event Sources", selenium.getText("eventsources"));

        //Verifying the Configure > Scheduled Tasks link of the left menu
        assertEquals("Scheduled Tasks", selenium.getText("link=Scheduled Tasks"));
		selenium.click("link=Scheduled Tasks");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Configure", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));
		assertEquals("Scheduled Tasks", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Configure > Synapse link of the left menu
        assertEquals("Synapse", selenium.getText("link=Synapse"));
		selenium.click("link=Synapse");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Configure", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));
		assertEquals("Manage Synapse Configuration", selenium.getText("//div[@id='middle']/h2"));
		assertTrue(selenium.isTextPresent("Manage"));

        //Verifying the Manage > Service link of the left menu
        assertEquals("Service", selenium.getText("//div[@id='menu']/ul/li[5]/ul/li[1]"));
		assertEquals("List", selenium.getText("link=List"));
		selenium.click("link=List");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Manage", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));
		assertEquals("Deployed Services", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Manage > Service > Add link of the left menu
		assertEquals("Add", selenium.getText("//div[@id='menu']/ul/li[5]/ul/li[2]/ul/li[2]"));

        //Verifying the Manage > Proxy Service link of the left menu
        assertEquals("Proxy Service", selenium.getText("link=Proxy Service"));
		selenium.click("link=Proxy Service");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Manage", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));
		assertEquals("Add Proxy Service", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Manage > Mediation link of the left menu        
        assertEquals("Mediation", selenium.getText("//div[@id='menu']/ul/li[5]/ul/li[3]"));

        //Verifying the Manage > Mediation > Sequences link of the left menu
        assertEquals("Sequences", selenium.getText("link=Sequences"));
		selenium.click("link=Sequences");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Manage", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));
		assertEquals("Mediation Sequences", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Manage > Mediation > Endpoints link of the left menu
        assertEquals("Endpoints", selenium.getText("link=Endpoints"));
		selenium.click("link=Endpoints");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Manage", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));
		assertEquals("Manage Endpoints", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Manage > Mediation > Local Entries link of the left menu
        assertEquals("Local Entries", selenium.getText("link=Local Entries"));
		selenium.click("link=Local Entries");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Manage", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));
		assertEquals("Manage Local Registry Entries", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Manage > Modules link of the left menu
        assertEquals("Modules", selenium.getText("//div[@id='menu']/ul/li[5]/ul/li[5]"));
		assertEquals("List", selenium.getText("//div[@id='menu']/ul/li[5]/ul/li[6]/ul/li[1]/a"));
		selenium.click("//div[@id='menu']/ul/li[5]/ul/li[6]/ul/li[1]/a");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Manage", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));
		assertEquals("Deployed Modules", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Manage > Module > Add link of the left menu
        assertEquals("Add", selenium.getText("link=Add"));
		selenium.click("link=Add");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Modules", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[3]"));
		assertEquals("Add a module", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Manage > Transports link of the left menu
        assertEquals("Transports", selenium.getText("link=Transports"));
		selenium.click("link=Transports");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Manage", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));
		assertEquals("Transport Management", selenium.getText("listTransport"));

        //Verifying the Manage > Shutdown/Restart link of the left menu
        assertEquals("Shutdown/Restart", selenium.getText("link=Shutdown/Restart"));
		selenium.click("link=Shutdown/Restart");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Shutdown/Restart", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[3]"));
		assertEquals("Shutdown/Restart Server", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Registry > Resources link of the left menu
		assertEquals("Registry", selenium.getText("//tr[3]/td/div/ul/li[1]"));
		assertEquals("Resources", selenium.getText("link=Resources"));
		selenium.click("link=Resources");
		Thread.sleep(20000);
		assertEquals("> Registry", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));

        //Verifying the Registry > Search link of the left menu
        assertEquals("Resources", selenium.getText("//div[@id='middle']/h2"));
		assertEquals("Search", selenium.getText("link=Search"));
		selenium.click("link=Search");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Registry", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));

        //Verifying the Search link of the left menu        
        assertEquals("Search", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Monitor link of the left menu
        assertEquals("Monitor", selenium.getText("//tr[4]/td/div/ul/li[1]"));

        //Verifying the Monitor > System Statistics link of the left menu
		assertEquals("System Statistics", selenium.getText("link=System Statistics"));
		selenium.click("link=System Statistics");
		selenium.waitForPageToLoad("30000");
		assertEquals("> System Statistics", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[3]"));
		assertEquals("System Statistics", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Monitor > Mediation Statistics link of the left menu
        assertEquals("Mediation Statistics", selenium.getText("link=Mediation Statistics"));
		selenium.click("link=Mediation Statistics");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Mediation Statistics", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[3]"));
		assertEquals("Mediation Statistics", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Monitor > System Logs link of the left menu
		assertEquals("System Logs", selenium.getText("link=System Logs"));
		selenium.click("link=System Logs");
		selenium.waitForPageToLoad("30000");

        //Verifying the Monitor > Monitor link of the left menu
		assertEquals("> Monitor", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));

        //Verifying the Monitor > System Logs link of the left menu
        assertEquals("System Logs", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Monitor > SOAP Tracer link of the left menu
        assertEquals("SOAP Tracer", selenium.getText("link=SOAP Tracer"));
		selenium.click("link=SOAP Tracer");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Monitor", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));

        //Verifying the Monitor > SOAP Message Tracer link of the left menu
		assertEquals("SOAP Message Tracer", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Monitor > Mediation Tracer link of the left menu
        assertEquals("Mediation Tracer", selenium.getText("link=Mediation Tracer"));
		selenium.click("link=Mediation Tracer");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Monitor", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[2]"));
		assertEquals("Mediation Message Tracer", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Monitor > Message Flows link of the left menu
        assertEquals("Message Flows", selenium.getText("link=Message Flows"));
		selenium.click("link=Message Flows");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Message Flows", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[3]"));
		assertEquals("Message Flows (Graphical View)", selenium.getText("//div[@id='middle']/h2"));

        //Verifying the Monitor > Tools link of the left menu
		assertEquals("Tools", selenium.getText("//tr[5]/td/div/ul/li[1]"));

        //Verifying the Monitor > Try It link of the left menu
        assertEquals("Try It", selenium.getText("link=Try It"));
		selenium.click("link=Try It");
		selenium.waitForPageToLoad("30000");
		assertEquals("> Try It", selenium.getText("//div[@id='breadcrumb-div']/table/tbody/tr/td[3]"));
		assertEquals("Try It", selenium.getText("//div[@id='middle']/h2"));

        //Signing out
        seleniumTestBase.logOutUI();
    }
}
