package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.apache.commons.io.FileUtils;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;/*
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

public class ESBExtensionMediatorsTest extends CommonSetup{
    Properties properties = new Properties();

    public ESBExtensionMediatorsTest(String text) {
        super(text);
    }

    /*
    Creating the local entry for the context.xml
     */
    public void addLocalEntry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);        
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("context.xml","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/spring/context.xml");
        esbManageLocalEntriesTest.addSourceUrlEntry("stockquoteScript","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/script/stockquoteTransform.js");
    }

    /*
    Adding the Spring mediator
     */
    public void addSpringMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSpringMediatorTest esbSpringMediatorTest = new ESBSpringMediatorTest(selenium);
        esbCommon.addRootLevelChildren("Add Child","Extension","Spring");
        esbSpringMediatorTest.addSpringMediator("0","SimpleMathBean","context.xml");
        esbCommon.mediatorUpdate();
    }

    /*
    Adding the Script mediator
     */
    public void addScriptMediator() throws Exception{
        ESBScriptMediatorTest esbScriptMediatorTest = new ESBScriptMediatorTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        String requestContent = null;
        
        try {
            File file = new File(".." + File.separator + "esb" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "requestJavaScript.js");
            requestContent = FileUtils.readFileToString(file);
        } catch (
                IOException e) {
        }

        //Adding the Registry type script mediator
        esbCommon.addRootLevelChildren("Add Child","Extension","Script");
        esbScriptMediatorTest.addRegistryScripts("1","Javascript","transformRequest","stockquoteScript");
        esbCommon.mediatorUpdate();

        //Adding the inline script mediator
        esbCommon.addRootLevelChildren("Add Child","Extension","Script");
        esbScriptMediatorTest.addInlineScripts ("2","Javascript",requestContent);
        esbScriptMediatorTest.addIncludeKeys("stockquoteScript");
        esbCommon.mediatorUpdate();
    }

    /*
    Adding the Class mediator
     */
    public void addClassMediator() throws Exception{

    }

    /*
    Adding the Command mediator
     */
    public void addCommandMediator() throws Exception{

    }

    /*
    This method will add all the mediators which are under the 'Extension' category to a sequence and update the Synapse confoguration
     */
    public void testAddExtensionMediators() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");

        addLocalEntry();
        esbCommon.addSequence("sequence_extension");
        addSpringMediator();
        addScriptMediator();
        addClassMediator();
        esbCommon.sequenceSave();
        seleniumTestBase.logOutUI();
    }

}
