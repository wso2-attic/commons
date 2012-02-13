package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

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

public class ESBAddRegistrySourceWsdlProxyServiceTest extends CommonSetup {

    public ESBAddRegistrySourceWsdlProxyServiceTest(String text) {
        super(text);
    }


    /*
   This method will test adding of a Proxy Service that has proxy wsdl refered from the registry
    */
    public void testAddRegistrySourceProxyService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        String content = null;
        boolean login = selenium.isTextPresent("Sign-out");

        if (login) {
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin", "admin");

        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Creating a local entry to store the WSDL file
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.testAddLocalEntry("Add Source URL Entry","wsdl_file","file:repository/samples/resources/proxy/sample_proxy_1.wsdl");

        //Options which could be used are Specify in-line, Specify source URL, Pick from registry, None
        esbAddProxyServiceTest.testAddProxyService("proxy_registry_wsdl", "Pick from registry", null, null,"wsdl_file", "true", null);
        esbAddProxyServiceTest.testAddParams("param1", "value1", "true");
        esbAddProxyServiceTest.testAddParams("param2", "value2", "false");
        esbAddProxyServiceTest.testTransportSettings("http", "https", null, null, null);
        esbAddProxyServiceTest.testClickNext();

        //Available options are inSeqOpAnon, inSeqOpReg, inSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.testSelectInSequence("inSeqOpImp", "main",null,null,null);

        //Available options are epOpImp, epOpAnon, epOpReg, epOpNone
        esbAddProxyServiceTest.testSelectEndpoint("epOpNone", null,null);
        esbAddProxyServiceTest.testClickNext();

        //Available options are outSeqOpAnon, outSeqOpReg, outSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.testSelectOutSequence("outSeqOpImp", "fault",null,null,null);

        //Available options are faultSeqOpAnon, faultSeqOpReg, faultSeqOpImp, faultSeqOpNone
        esbAddProxyServiceTest.testSelectFaultSequence("faultSeqOpImp", "fault",null,null,null);
        esbAddProxyServiceTest.testSaveProxyService();

        esbAddProxyServiceTest.testVerifyProxy("proxy_registry_wsdl");
        seleniumTestBase.logOutUI();        
    }

}
