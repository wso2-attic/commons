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

public class ESBAddInlineWsdlProxyServiceTest extends CommonSetup {

    public ESBAddInlineWsdlProxyServiceTest(String text) {
        super(text);
    }


    /*
    This method will test adding of a Proxy Service that has an inline wsdl
     */
    public void testAddInlineWsdlProxyService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        String content = null;
        boolean login = selenium.isTextPresent("Sign-out");

        if (login) {
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin", "admin");


            //Adding an endpoint which will be refered through the proxy wizard
            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.testAddEndpoint();
            boolean endpoint = selenium.isTextPresent("epr1");

        if (endpoint) {
            //Do nothing
        } else {
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.testAddAnonAddressEndpoint();
            esbAddAddressEndpointTest.testAddAddressEprMandatoryInfo("epr1","http://localhost:9000/services/SimpleStockQuoteService");
            esbAddAddressEndpointTest.testSaveAddressEndpoint();
        }

        try {
            File file = new File(".." + File.separator + "esb" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "SimpleStockQuoteProxy.wsdl");
            content = FileUtils.readFileToString(file);
        } catch (
                IOException e) {
        }

        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Options which could be used are Specify in-line, Specify source URL, Pick from registry, None
        esbAddProxyServiceTest.testAddProxyService("proxy_inline_wsdl", "Specify in-line", null, content, null,"true", null);
        esbAddProxyServiceTest.testTransportSettings("http", "https", null, null, null);
        esbAddProxyServiceTest.testClickNext();

        //Available options are inSeqOpAnon, inSeqOpReg, inSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.testSelectInSequence("inSeqOpImp", "main",null,null,null);

        //Available options are epOpImp, epOpAnon, epOpReg, epOpNone
        esbAddProxyServiceTest.testSelectEndpoint("epOpImp", "epr1",null);
        esbAddProxyServiceTest.testClickNext();

        //Available options are outSeqOpAnon, outSeqOpReg, outSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.testSelectOutSequence("outSeqOpImp", "fault",null,null,null);

        //Available options are faultSeqOpAnon, faultSeqOpReg, faultSeqOpImp, faultSeqOpNone
        esbAddProxyServiceTest.testSelectFaultSequence("faultSeqOpImp", "fault",null,null,null);
        esbAddProxyServiceTest.testSaveProxyService();

        esbAddProxyServiceTest.testVerifyProxy("proxy_inline_wsdl");
        seleniumTestBase.logOutUI();        
    }
}