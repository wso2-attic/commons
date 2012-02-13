package org.wso2.carbon.web.test.mashup;

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

import com.thoughtworks.selenium.*;

import java.awt.event.KeyEvent;
import java.io.File;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

public class EnvSetup extends CommonSetup {

    public EnvSetup(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testSignIn() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }

    //Upload Services.
    public void testUpload_Services() throws Exception {

       //Upload all services. 
       MSCommon.testServiceUpload("allCommons");
       MSCommon.testServiceUpload("EmailAllScenarios");
       MSCommon.testServiceUpload("feedReader2");
       MSCommon.testServiceUpload("IMAllScenarios");
       MSCommon.testServiceUpload("invokeSecService");
       MSCommon.testServiceUpload("raceNewsRss");
       MSCommon.testServiceUpload("request");
       MSCommon.testServiceUpload("schemaTest1");
       MSCommon.testServiceUpload("schemaTest2");
       MSCommon.testServiceUpload("schemaTest3");
       MSCommon.testServiceUpload("schemaTest4");
       MSCommon.testServiceUpload("scraper");
       //Upload faulty service.
       MSCommon.testServiceUpload("faulty");

       //Copy .txt file and .jpg file to another folder.
       File resourcePath_txtFrom = new File("." + File.separator + "lib" + File.separator + "temp.txt");
       File resourcePath_txtTo = new File(MSCommon.loadProperties().getProperty("carbon.home") + File.separator + "repository" + File.separator + "deployment" + File.separator + "server" + File.separator + "jsservices" + File.separator + "admin" + File.separator + "EmailAllScenarios.resources" + File.separator + "temp.txt");
       File resourcePath_jpgFrom = new File("." + File.separator + "lib" + File.separator + "text.jpg");
       File resourcePath_jpgTo = new File(MSCommon.loadProperties().getProperty("carbon.home") + File.separator + "repository" + File.separator + "deployment" + File.separator + "server" + File.separator + "jsservices" + File.separator + "admin" + File.separator + "EmailAllScenarios.resources" + File.separator + "text.jpg");
       MSCommon.copy(resourcePath_txtFrom,resourcePath_txtTo);
       MSCommon.copy(resourcePath_jpgFrom,resourcePath_jpgTo);

       //Copy three .jar file to another folder.
       File resourcePath_jar1From = new File("." + File.separator + "lib" + File.separator + "ymsg_network_v0_64.jar");
       File resourcePath_jar1To = new File(MSCommon.loadProperties().getProperty("carbon.home") + File.separator + "repository" + File.separator + "components" + File.separator + "lib" + File.separator + "ymsg_network_v0_64.jar");
       MSCommon.copy(resourcePath_jar1From,resourcePath_jar1To);
       File resourcePath_jar2From = new File("." + File.separator + "lib" + File.separator + "ymsg_support_v0_64.jar");
       File resourcePath_jar2To = new File(MSCommon.loadProperties().getProperty("carbon.home") + File.separator + "repository" + File.separator + "components" + File.separator + "lib" + File.separator + "ymsg_support_v0_64.jar");
       MSCommon.copy(resourcePath_jar2From,resourcePath_jar2To);
       File resourcePath_jar3From = new File("." + File.separator + "lib" + File.separator + "ymsg_test_v0_64.jar");
       File resourcePath_jar3To = new File(MSCommon.loadProperties().getProperty("carbon.home") + File.separator + "repository" + File.separator + "components" + File.separator + "lib" + File.separator + "ymsg_test_v0_64.jar");
       MSCommon.copy(resourcePath_jar3From,resourcePath_jar3To);
    }

    //Logout from Mashup Server.
    public void testSignout() throws Exception {
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.logOutUI();
    }


}
