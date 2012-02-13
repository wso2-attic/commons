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

package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;
import org.wso2.carbon.web.test.common.WsdlValidator;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;

public class ManageWsdlValidatorTest extends TestCase {

Selenium browser;
    Properties property;
    String username;
    String password;
    String moduleVersion ;
    String hostname;
    String httpport;
    String contextroot;


    public ManageWsdlValidatorTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");

    }

    public void testLogin() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        InstSeleniumTestBase.loginToUI(username, password);

    }

    public void testverifyUIElements() throws Exception {
        WsdlValidator wsdlValidator = new WsdlValidator(browser);
        wsdlValidator.verifyUIElements();
    }

    public void testuploadWsdlfromFileSystem() throws Exception {
        String wsdlname = "AreaService.wsdl";
        WsdlValidator wsdlValidator = new WsdlValidator(browser);
        wsdlValidator.uploadWsdlfromFileSystem(wsdlname);
    }

    public void testuploadWsdlfromURL() throws Exception {
       String wsdlurl = "http://www50.brinkster.com/vbfacileinpt/np.asmx?wsdl";
       WsdlValidator wsdlValidator = new WsdlValidator(browser);
        wsdlValidator.uploadWsdlfromURL(wsdlurl);
    }

     public void testlogOutWsdlValidator() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }
}
