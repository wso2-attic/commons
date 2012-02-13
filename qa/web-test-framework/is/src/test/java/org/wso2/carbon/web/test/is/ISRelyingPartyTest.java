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

import org.wso2.carbon.web.test.common.SeleniumTestBase;

public class ISRelyingPartyTest extends CommonSetup {

    public ISRelyingPartyTest(String text) {
        super(text);
    }


    //Login to admin console and test Logging.
    public void testSignin() throws Exception{
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }

    //Test Relying Party UI.
    public void testRpUI() throws Exception{
         ISRelyingParty.rpUI();
         ISRelyingParty.testCSHelp();
    }

    //Test Basic Relying Party tests
    public void testRpBasics() throws Exception {
        ISRelyingParty.addRP("localhost");
        ISRelyingParty.deleteRP("localhost");
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.logOutUI();
    }

}
