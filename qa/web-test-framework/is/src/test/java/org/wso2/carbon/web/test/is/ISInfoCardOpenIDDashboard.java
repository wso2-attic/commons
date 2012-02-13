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


public class ISInfoCardOpenIDDashboard extends CommonSetup{

    public ISInfoCardOpenIDDashboard(String text) {
        super(text);
    }

    //Test InfoCard/OpenID UI.
    public static void infoCardOpenIDUITest(String userName) throws Exception{
        String openIDUrl="https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/openid/"+userName;
        selenium.click("link=InfoCard/OpenID");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("InfoCard/OpenID Dashboard"));
		assertTrue(selenium.isTextPresent(openIDUrl));

        assertTrue(selenium.isTextPresent("Download Information Card"));
		assertEquals("Download", selenium.getText("link=Download"));
		assertTrue(selenium.isTextPresent("Download OpenID Information Card"));
		assertEquals("Download", selenium.getText("//div[@id='workArea']/table[3]/tbody/tr[4]/td/a"));
    }

    //Test openID URL.
    public static void openIDUrlTest(String userName) throws Exception{
        String openIDUrl="https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/openid/"+userName;
        selenium.click("link=InfoCard/OpenID");
		selenium.waitForPageToLoad("30000");

        selenium.click("link=exact:"+openIDUrl);
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("This is the OpenID Url of user, "+userName));
		assertTrue(selenium.isTextPresent("Management Console"));
        selenium.goBack();

        selenium.click("link=Delete");
		assertTrue(selenium.isTextPresent("You cannot remove the OpenID provided by WSO2 Identity Server"));
		selenium.click("//button[@type='button']");
    }

    //CSHelp test
    public static void csHelpTest() throws Exception{
        String expectedForCSHelp="https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/carbon/identity-provider/docs/userguide.html";

        assertEquals("Help", selenium.getText("link=Help"));
        selenium.click("link=Help");
        String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(helpwinid);
        Thread.sleep(10000);
		assertTrue(selenium.isTextPresent("InfoCard / OpenID Dashboard"));
        String actualForCSHelp = selenium.getLocation();
        if(actualForCSHelp.equals(expectedForCSHelp))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    }
}