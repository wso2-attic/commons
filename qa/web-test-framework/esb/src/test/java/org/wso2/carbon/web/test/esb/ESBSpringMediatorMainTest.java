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

public class ESBSpringMediatorMainTest  extends CommonSetup{
	Properties properties = new Properties();

    public ESBSpringMediatorMainTest(String text) {
        super(text);
    }

    public void addMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        properties.load(new FileInputStream(".."+ File.separator+"esb"+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"esb.properties"));
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
               
        seleniumTestBase.loginToUI("admin","admin");
        Thread.sleep(2000);

        //Adding a local entry
        //Paramters which could be passed are String entryType, String name, String value
        // Options available for paramter entryType=Add Source URL Entry,Add In-lined XML Entry,Add In-lined Text Entry
        selenium.click("link=Local Entries");
		Thread.sleep(2000);

        boolean sourceUrl = selenium.isTextPresent("context.xml");

        if (sourceUrl){
         //do nothing
        } else {
            ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
            esbManageLocalEntriesTest.addSourceUrlEntry("context.xml","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/spring/context.xml");
        }
        esbCommon.addSequence("sequence_spring");

        esbCommon.addRootLevelChildren("Add Child","Extension","Spring");

        ESBSpringMediatorTest esbSpringMediatorTest = new ESBSpringMediatorTest(selenium);
        esbSpringMediatorTest.addSpringMediator("0","com.test.SimpleMath","context.xml");

        ESBSpringMediatorUITest esbSpringMediatorUITest = new ESBSpringMediatorUITest(selenium);
        esbSpringMediatorUITest.testVerifySpringMediatorUI("0");

        esbCommon.sequenceSave();

        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest = new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.saveSynapseConfig();
                
        seleniumTestBase.logOutUI();
    }
}