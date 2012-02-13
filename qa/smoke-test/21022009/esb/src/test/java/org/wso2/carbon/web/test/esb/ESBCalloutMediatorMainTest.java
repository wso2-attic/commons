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


public class ESBCalloutMediatorMainTest  extends CommonSetup{

    public ESBCalloutMediatorMainTest(String text) {
        super(text);
    }

    public void testAddmediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("admin","admin");

        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.testAddLocalEntry("Add Source URL Entry","validate_schema","file:repository/samples/resources/validate/validate.xsd");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddSequence("sequence_callout");

        esbCommon.testAddRootLevelChildren("Add Child","Advanced","Callout");

        //This can contain the options sourceGroupXPath, //input[@name='sourcegroup' and @value='Key'] and targetGroupXPath, //input[@name='targetgroup' and @value='Key']
        ESBCalloutMediatorTest esbCalloutMediatorTest = new ESBCalloutMediatorTest(selenium);

        /*  Parameters that can be passed are,
         *  String svcURL, String action, String repo, String axis2XML, String sourceXpath, String target, String sourceSpecifyAs, String targetSpecifyAs,
         *  String sourceResource, String targetResource, String xpathNamespacePrefix, String xpathNamespaceURI, String targetNamespacePrefix, String targetNamespaceURI
         */
        esbCalloutMediatorTest.testAddCalloutMediator("0","http://localhost:9000/services","urn:getQuote",null,null);
        esbCalloutMediatorTest.testAddSourceInfo("//m0:add/m0:x","sourceGroupXPath","m0","http://services",null);
        esbCalloutMediatorTest.testAddTargetInfo("targetGroupXPath","m0","http://services","validate_schema");
        esbCommon.testMediatorUpdate();

        ESBCalloutMediatorUITest esbCalloutMediatorUITest = new ESBCalloutMediatorUITest(selenium);
        esbCalloutMediatorUITest.testVerifyCalloutMediator("0","sourceGroupXPath", "targetGroupsXPath");

        esbCommon.testSequenceSave();
        seleniumTestBase.logOutUI();        
    }

}
