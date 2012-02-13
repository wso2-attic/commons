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

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.io.File;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

public class ISRelyingParty extends CommonSetup {

    public ISRelyingParty(String text) {
        super(text);
    }

    //Add relying party
    public static void addRP(String rpName) throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(selenium);

		selenium.click("link=Relying Parties");
		selenium.waitForPageToLoad("30000");
        File resourcePath = new File("." + File.separator + "src" +File.separator +"lib" + File.separator +"javarp.cer");
	    InstSeleniumTestBase.SetFileBrowse("rpcert", resourcePath.getCanonicalPath());
        Thread.sleep(2000);
		selenium.click("upload");
		selenium.waitForPageToLoad("30000");
		assertEquals("Trusted relying party added successfully", selenium.getText("messagebox-info"));
		selenium.click("//button[@type='button']");
        assertTrue(selenium.isTextPresent(rpName)); //pass the name of the trusted rp that u uploaded
    }

    //relying party ui
    public static void rpUI() throws Exception {
		selenium.click("link=Relying Parties");
		selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("InfoCard/OpenID - Trusted Relying Parties"));
        assertTrue(selenium.isTextPresent("Upload a new relying party certificate to trust"));
        assertTrue(selenium.isTextPresent("Relying party certificate"));
    }

    //delete relying party
    public static void deleteRP(String rpName) throws Exception {
        selenium.click("link=Relying Parties");
        selenium.waitForPageToLoad("30000");

        int j = 0;
        int i = 1;
        String rp = "";
        while (rp.equals(rpName)) {
            rp = selenium.getText("//div[@id='workArea']/form[2]/table/tbody/tr[" + i + "]/td[1]");
            i++;
        }
        j = i;

        selenium.click("//div[@id='workArea']/form[2]/table/tbody/tr[" + j + "]/td[2]/a/img");
        Thread.sleep(2000);
        assertTrue(selenium.getText("messagebox-confirm").matches("You are about to remove localhost\\. Do you want to proceed[\\s\\S]$"));
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
    }

    /* CSHelp */
    public static void testCSHelp() throws Exception{
        String expectedForCSHelp="https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/carbon/identity-rp/docs/userguide.html";
        selenium.click("link=Relying Parties");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Help");
        String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(helpwinid);
        Thread.sleep(10000);
        assertTrue(selenium.isTextPresent("InfoCard/OpenID - Trusted Relying Parties"));
        String actualForCSHelp = selenium.getLocation();
        if(actualForCSHelp.equals(expectedForCSHelp))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    }
}