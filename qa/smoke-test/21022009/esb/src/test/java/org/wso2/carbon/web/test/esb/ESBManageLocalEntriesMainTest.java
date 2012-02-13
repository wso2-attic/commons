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

public class ESBManageLocalEntriesMainTest  extends CommonSetup{

    public ESBManageLocalEntriesMainTest(String text) {
        super(text);
    }

    public void testAddmediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        
        seleniumTestBase.loginToUI("admin","admin");
        Thread.sleep(2000);

        //Available options are Add Source URL Entry, Add In-lined XML Entry, Add In-lined Text Entry
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.testAddLocalEntry("Add Source URL Entry","source_url","file:repository/samples/resources/script/stockquoteTransform.js");
        esbManageLocalEntriesTest.testAddLocalEntry("Add In-lined XML Entry","inline_xml", "<test>test</test>");
        esbManageLocalEntriesTest.testAddLocalEntry("Add In-lined Text Entry","inline_text","0.1");
        seleniumTestBase.logOutUI();        

    }
}

