package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

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

public class ESBXqueryMediatorMainTest  extends CommonSetup {

    public ESBXqueryMediatorMainTest(String text) {
        super(text);
    }

    //This method will test adding a new Fault mediator
    public void testAddXqueryMediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("admin","admin");

        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.testAddLocalEntry("Add Source URL Entry","stockquoterequest","file:repository/samples/resources/transform/transform_back.xslt");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddSequence("sequence_xquery");

        //Adding the first XSLT mediator
        esbCommon.testAddRootLevelChildren("Add Child","Transform","XQuery");

        ESBXqueryMediatorUITest esbXqueryMediatorUITest = new ESBXqueryMediatorUITest(selenium);
        esbXqueryMediatorUITest.testAddXSLTMediator("0");

        ESBXqueryMediatorTest esbXqueryMediatorTest = new ESBXqueryMediatorTest(selenium);
        esbXqueryMediatorTest.testAddXqueryMediator("0","stockquoterequest","//m0:add","m0","http://services/samples");
        esbXqueryMediatorTest.testAddVariables("INT","symbol","Value","10", null,null,null);

        //Adding the second XSLT mediator
        esbCommon.testAddRootLevelChildren("Add Child","Transform","XQuery");

        esbXqueryMediatorTest = new ESBXqueryMediatorTest(selenium);
        esbXqueryMediatorTest.testAddXqueryMediator("1","stockquoterequest","//m0:add","m0","http://services/samples");
        esbXqueryMediatorTest.testAddVariables("INT","symbol","Expression","//m0:add","stockquoterequest","m0","http://samples/services");

        esbCommon.testMediatorUpdate();
        esbCommon.testSequenceSave();
        seleniumTestBase.logOutUI();
    }
}
