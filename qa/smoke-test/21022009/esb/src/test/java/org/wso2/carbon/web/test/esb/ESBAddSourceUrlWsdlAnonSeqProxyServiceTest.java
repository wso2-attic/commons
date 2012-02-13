package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
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

public class ESBAddSourceUrlWsdlAnonSeqProxyServiceTest extends CommonSetup {

    public ESBAddSourceUrlWsdlAnonSeqProxyServiceTest(String text) {
        super(text);
    }

    /*
   This method will test adding of a Proxy Service with source url WSDL but with anonymous sequences
    */
    public void testAddSourceUrleWsdlAnonSeqProxyService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        String content = null;
        boolean login = selenium.isTextPresent("Sign-out");

        if (login) {
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin", "admin");

        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Options which could be used are Specify in-line, Specify source URL, Pick from registry, None
        esbAddProxyServiceTest.testAddProxyService("proxy_sourceurl_wsdl_anon_seq", "Specify source URL", "file:repository/samples/resources/proxy/sample_proxy_1.wsdl", null, null,"true", null);
        esbAddProxyServiceTest.testTransportSettings("http", "https", null, null, null);
        esbAddProxyServiceTest.testClickNext();


        //Available options are inSeqOpAnon, inSeqOpReg, inSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.testSelectInSequence("inSeqOpAnon", null,"Add Child","Core","Log");

        //Available options are epOpImp, epOpAnon, epOpReg, epOpNone
        esbAddProxyServiceTest.testSelectEndpoint("epOpAnon", null,"http://localhost:9000/SimpleStockQuoteService");
        esbAddProxyServiceTest.testClickNext();

        //Available options are outSeqOpAnon, outSeqOpReg, outSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.testSelectOutSequence("outSeqOpAnon", null,"Add Child","Core","Send");

        //Available options are faultSeqOpAnon, faultSeqOpReg, faultSeqOpImp, faultSeqOpNone
        esbAddProxyServiceTest.testSelectFaultSequence("faultSeqOpAnon", null,"Add Child","Core","Drop");
        esbAddProxyServiceTest.testSaveProxyService();

        esbAddProxyServiceTest.testVerifyProxy("proxy_inline_wsdl_anon_seq");
        seleniumTestBase.logOutUI();        
    }
}
