package org.wso2.carbon.web.test.esb;/*
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

public class ESBAdvancedMediatorsSet1Test extends CommonSetup{

    public ESBAdvancedMediatorsSet1Test(String text) {
        super(text);
    }

    /*
    Adding the Transaction mediator
     */
    public void addTransactionMediator() throws Exception{
        ESBTransactionMediatorTest esbTransactionMediatorTest = new ESBTransactionMediatorTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.addRootLevelChildren("Add Child","Advanced","Transaction");
        esbTransactionMediatorTest.addTransactionMediator("0","Commit Transaction");
        esbCommon.mediatorUpdate();
        esbCommon.addRootLevelChildren("Add Child","Advanced","Transaction");
        esbTransactionMediatorTest.addTransactionMediator("1","Initiate new Transaction");
        esbCommon.mediatorUpdate();
        esbCommon.addRootLevelChildren("Add Child","Advanced","Transaction");
        esbTransactionMediatorTest.addTransactionMediator("2","Fault if no Transaction");
        esbCommon.mediatorUpdate();
        esbCommon.addRootLevelChildren("Add Child","Advanced","Transaction");
        esbTransactionMediatorTest.addTransactionMediator("3","Resume Transaction");
        esbCommon.mediatorUpdate();
        esbCommon.addRootLevelChildren("Add Child","Advanced","Transaction");
        esbTransactionMediatorTest.addTransactionMediator("4","Rollback Transaction");
        esbCommon.mediatorUpdate();
        esbCommon.addRootLevelChildren("Add Child","Advanced","Transaction");
        esbTransactionMediatorTest.addTransactionMediator("5","=Suspend Transaction");
        esbCommon.mediatorUpdate();
        esbCommon.addRootLevelChildren("Add Child","Advanced","Transaction");
        esbTransactionMediatorTest.addTransactionMediator("6","Use existing or Initiate Transaction");
        esbCommon.mediatorUpdate();

    }

    /*
    Adding the Cache mediator
     */
    public void addCacheMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBCacheMediatorTest esbCacheMediatorTest = new ESBCacheMediatorTest(selenium);

        esbCommon.addRootLevelChildren("Add Child","Advanced","Cache");




    }

    /*
    Adding the Entitlement mediator
     */
    public void addEntitlementMediator() throws Exception{

    }

    /*
    Adding the Callout mediator
     */
    public void addCalloutMediator() throws Exception{
        
    }
}
