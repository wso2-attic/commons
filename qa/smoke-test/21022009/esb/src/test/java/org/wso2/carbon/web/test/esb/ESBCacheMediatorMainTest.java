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

public class ESBCacheMediatorMainTest  extends CommonSetup{

    public ESBCacheMediatorMainTest(String text) {
        super(text);
    }

    public void testAddmediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }        
        seleniumTestBase.loginToUI("admin","admin");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddSequence("sequence_cache");

        esbCommon.testAddRootLevelChildren("Add Child","Advanced","Cache");

        /*
        The options available are Per-Host, Per-Mediator && Collector, Finder && sequenceOptionAnon, sequenceOptionReference
         */
        ESBCacheMediatorTest esbCacheMediatorTest = new ESBCacheMediatorTest(selenium);

        /*
        Parameters that can be passed are,
        String cacheId, String cacheScope, String cacheType, String cacheTimeout, String maxMsgSize, String maxSize, String onCacheHit, String resourceName,
         */
        esbCacheMediatorTest.testAddCacheMediatorMainInfo("0","A","Per-Host","Finder","10","100");
        esbCacheMediatorTest.testAddCacheImplementationInfo("In-Memory","2");
        esbCacheMediatorTest.testAddOnCacheHitInfo("sequenceOptionReference", "main");
        esbCommon.testMediatorUpdate();

        ESBCacheMediatorUITest esbCacheMediatorUITest = new ESBCacheMediatorUITest(selenium);
        esbCacheMediatorUITest.testVerifyCacheMediator("0","sequenceOptionReference");

        esbCommon.testSequenceSave();
        seleniumTestBase.logOutUI();        
    }

}
