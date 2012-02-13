package org.wso2.carbon.web.test.esb;

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

import org.wso2.carbon.web.test.common.SeleniumTestBase;

public class ESBEntitlementMainTest extends CommonSetup{
      public ESBEntitlementMainTest(String text) {
        super(text);
    }

    public void testAddMediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("admin","admin");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.addSequence("sequence_entilement");

        esbCommon.addRootLevelChildren("Add Child","Advanced","Entitlement");

        ESBEntitlementMediatorTest esbEntitlementMediatorTest=new ESBEntitlementMediatorTest(selenium);
        esbEntitlementMediatorTest.addMediator("teste05","teste05","teste05");

        ESBEntitlementMediatorUITest esbEntitlementMediatorUITest=new ESBEntitlementMediatorUITest(selenium);
        esbEntitlementMediatorUITest.testverifyEntitlementMediator();

        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest = new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.saveSynapseConfig();

        seleniumTestBase.logOutUI();

    }
}
