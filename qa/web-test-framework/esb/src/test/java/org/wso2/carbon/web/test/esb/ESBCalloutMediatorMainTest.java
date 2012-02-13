package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
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


public class ESBCalloutMediatorMainTest  extends CommonSetup{

    public ESBCalloutMediatorMainTest(String text) {
        super(text);
    }

    public void testAddmediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);        
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");
        Properties properties = new Properties();

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("admin","admin");

        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("validate_schema","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/validate/validate.xsd");
        esbCommon.addSequence("sequence_callout");
        esbCommon.addRootLevelChildren("Add Child","Advanced","Callout");

        //This can contain the options sourceGroupXPath, //input[@name='sourcegroup' and @value='Key'] and targetGroupXPath, //input[@name='targetgroup' and @value='Key']
        ESBCalloutMediatorTest esbCalloutMediatorTest = new ESBCalloutMediatorTest(selenium);

        /*  Parameters that can be passed are,
         *  String svcURL, String action, String repo, String axis2XML, String sourceXpath, String target, String sourceSpecifyAs, String targetSpecifyAs,
         *  String sourceResource, String targetResource, String xpathNamespacePrefix, String xpathNamespaceURI, String targetNamespacePrefix, String targetNamespaceURI
         */
        esbCalloutMediatorTest.addCalloutMediator("0","http://localhost:9000/services","urn:getQuote",null,null);
        esbCalloutMediatorTest.addSourceInfo("//m0:add/m0:x","sourceGroupXPath","0","m0","http://services",null);
        esbCalloutMediatorTest.addTargetInfo("targetGroupXPath","m0","0","http://services","validate_schema");
        esbCommon.mediatorUpdate();

        ESBCalloutMediatorUITest esbCalloutMediatorUITest = new ESBCalloutMediatorUITest(selenium);
        esbCalloutMediatorUITest.testVerifyCalloutMediator("0","sourceGroupXPath", "targetGroupsXPath");

        esbCommon.sequenceSave();

        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest = new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.saveSynapseConfig();
                
        seleniumTestBase.logOutUI();
    }
}
