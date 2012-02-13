package org.wso2.carbon.web.test.common;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

import java.io.File;

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

public class WsdlValidator extends SeleneseTestCase {

    Selenium browser;
    SeleniumTestBase InstSeleniumTestBase;


    public WsdlValidator(Selenium _browser) {
        browser = _browser;
        InstSeleniumTestBase = new SeleniumTestBase(browser);
    }

    public void verifyUIElements() throws Exception {

		browser.click("link=WSDL Validator");
		browser.waitForPageToLoad("30000");

        verifyTrue(browser.isTextPresent("WSDL Validator"));
		verifyTrue(browser.isTextPresent("Help"));
		verifyTrue(browser.isTextPresent("Select WSDL File"));
		verifyTrue(browser.isTextPresent("WSDL File Location"));
		verifyTrue(browser.isTextPresent("Provide WSDL URL"));
		verifyTrue(browser.isTextPresent("WSDL URL"));
		verifyTrue(browser.isTextPresent("Validation Result"));
	}

    public void uploadWsdlfromFileSystem(String wsdlname) throws Exception {

         File wsdlPath = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + wsdlname);
         browser.click("link=WSDL Validator");
         browser.waitForPageToLoad("30000");
         InstSeleniumTestBase.SetFileBrowse("filedata", wsdlPath.getCanonicalPath());
         browser.click("doUpload");
         verifyTrue(browser.getValue("wsdl-msgs").matches("^exact:[\\s\\S]*[\\s\\S]*[\\s\\S]*[\\s\\S]*[\\s\\S]*valid[\\s\\S]*[\\s\\S]*[\\s\\S]*[\\s\\S]*[\\s\\S]*$"));
		 verifyTrue(browser.isTextPresent("Validation Result"));


    }

     public void uploadWsdlfromURL(String wsdlurl) throws Exception {

         browser.click("link=WSDL Validator");
         browser.waitForPageToLoad("30000");
         browser.type("url", wsdlurl);
		 browser.click("//input[@value='Validate From URL']");
         verifyTrue(browser.getValue("wsdl-msgs").matches("^exact:[\\s\\S]*[\\s\\S]*[\\s\\S]*[\\s\\S]*[\\s\\S]*valid[\\s\\S]*[\\s\\S]*[\\s\\S]*[\\s\\S]*[\\s\\S]*$"));
		 verifyTrue(browser.isTextPresent("Validation Result"));


    }

    
}
