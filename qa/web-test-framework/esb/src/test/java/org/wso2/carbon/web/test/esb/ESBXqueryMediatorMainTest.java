package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
	Properties properties = new Properties();

    public ESBXqueryMediatorMainTest(String text) {
        super(text);
    }

    //This method will test adding a new Fault mediator
    public void testaddXqueryMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("admin","admin");

        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("stockquoterequest","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/transform/transform_back.xslt");
        esbCommon.addSequence("sequence_xquery");
        //Adding the first XSLT mediator
        esbCommon.addRootLevelChildren("Add Child","Transform","XQuery");

        ESBXqueryMediatorUITest esbXqueryMediatorUITest = new ESBXqueryMediatorUITest(selenium);
        esbXqueryMediatorUITest.addXSLTMediator("0");

        ESBXqueryMediatorTest esbXqueryMediatorTest = new ESBXqueryMediatorTest(selenium);
        esbXqueryMediatorTest.addXqueryKey("0","stockquoterequest");
        esbXqueryMediatorTest.addTarget("//m0:add");
        //esbXqueryMediatorTest.addVariableNamespace("m0","http://services/samples");
        esbXqueryMediatorTest.addVariables("INT","symbol","10");
        esbCommon.mediatorUpdate();

        //Adding the second XSLT mediator
        esbCommon.addRootLevelChildren("Add Child","Transform","XQuery");

        esbXqueryMediatorTest = new ESBXqueryMediatorTest(selenium);
        esbXqueryMediatorTest.addXqueryKey("1","stockquoterequest");
        esbXqueryMediatorTest.addTarget("//m0:add");
        //esbXqueryMediatorTest.addVariableNamespace("m0","http://services/samples");
        esbXqueryMediatorTest.addExpressionVariable ("INT","symbol","//m0:add","stockquoterequest");
        esbXqueryMediatorTest.setVariableNsLevel();
        esbXqueryMediatorTest.addVariableNamespace("m0","http://samples/services");
        esbCommon.mediatorUpdate();

        esbCommon.sequenceSave();

        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest = new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.saveSynapseConfig();
                
        seleniumTestBase.logOutUI();
    }
}
