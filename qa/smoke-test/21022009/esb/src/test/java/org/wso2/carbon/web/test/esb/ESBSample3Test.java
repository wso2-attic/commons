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

public class ESBSample3Test  extends CommonSetup{

    public ESBSample3Test(String text) {
        super(text);
    }

//    <localEntry key="version">0.1</localEntry>
//    <endpoint name="simple">
//        <address uri="http://localhost:9000/services/SimpleStockQuoteService"/>
//    </endpoint>
//
//    <sequence name="stockquote">
//        <log level="custom">
//            <property name="Text" value="Sending quote request"/>
//            <property name="version" expression="get-property('version')"/>
//            <property name="direction" expression="get-property('direction')"/>
//        </log>
//        <send>
//            <endpoint key="simple"/>
//        </send>
//    </sequence>
//
//    <sequence name="main">
//        <in>
//            <property name="direction" value="incoming"/>
//            <sequence key="stockquote"/>
//        </in>
//        <out>
//            <send/>
//        </out>
//    </sequence>

     public void testSample3Config() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

         boolean login = selenium.isTextPresent("Sign-out");

         if (login){
             seleniumTestBase.logOutUI();
         }
                 
        seleniumTestBase.loginToUI("admin","admin");

        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBPropertyMediatorTest esbPropertyMediatorTest = new ESBPropertyMediatorTest(selenium);
        ESBInMediatorTest esbInMediatorTest = new ESBInMediatorTest(selenium);
        ESBOutMediatorTest esbOutMediatorTest = new ESBOutMediatorTest(selenium);
        ESBSequenceMediatorTest esbSequenceMediatorTest = new ESBSequenceMediatorTest(selenium);

        //Adding the local entry
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.testAddLocalEntry("Add In-lined Text Entry","version","0.1");

        //Adding an address endpoint
        esbCommon.testAddEndpoint();

        boolean addEprName = selenium.isTextPresent("simple");
        if (addEprName){
            //Do nothing
        } else{
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.testAddAnonAddressEndpoint();
            esbAddAddressEndpointTest.testAddAddressEprMandatoryInfo("simple","http://localhost:9000/services/SimpleStockQuoteService");
            esbAddAddressEndpointTest.testSaveAddressEndpoint();            
        }

         //Creating sequence - stockquote
        esbCommon.testAddSequence("stockquote");

        //Adding the Log mediator
        esbCommon.testAddRootLevelChildren("Add Child", "Core", "Log");
        esbLogMediatorTest.testAddLogMediator("0","Custom");
        esbLogMediatorTest.testAddLogPropeties("Text","Value","Sending","version","Expression","get-property('version')","direction","Expression","get-property('direction')");
        esbSequenceTreePopulatorTest.testClickMediator("0");

        esbCommon.testAddRootLevelChildren("Add Sibling", "Core", "Send");
        //String radioSelection, String resourceName, String endpointURI, String format, String optimize,
        //String errCode, String durSec, String maxDur, String factor,String timeoutErr, String retry,
        //String retryDelay, String action, String actDur, String wsAddEnable, String sepLister, String wsSec,
        //String secResource, String wsRm, String rmResource         
        esbSendMediatorTest.testAddSendMediator("0","epOpReg","simple",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);

        esbCommon.testSequenceSave();

        //Creating sequence - Sample_3_main
        esbCommon.testAddSequence("Sample_3_main");
        esbCommon.testAddRootLevelChildren("Add Child", "Filter", "In");
        esbInMediatorTest.testAddInMediator("0");
        esbSequenceTreePopulatorTest.testClickMediator("0"); 
        esbCommon.testAddChildMediators("0", "Core", "Property");
        esbPropertyMediatorTest.testAddPropertyMediator("0.1","direction","set","value","incoming","Axis2",null,null);
        esbSequenceTreePopulatorTest.testClickMediator("0");
        esbCommon.testAddChildMediators("0", "Core", "Sequence");
        esbSequenceMediatorTest.testAddSequenceMediator("stockquote");

        //Adding an Out mediator 
        esbSequenceTreePopulatorTest.testClickMediator("0");
        esbCommon.testAddChildMediators("0", "Filter", "Out");
        esbOutMediatorTest.testAddOutMediator("1");         
        esbSendMediatorTest.testAddSendMediator("0","epOpNone",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null); 

        esbCommon.testSequenceSave();

        ESBSetMainSequenceTest esbSetMainSequenceTest = new ESBSetMainSequenceTest(selenium);
        esbSetMainSequenceTest.testSetMainSequence("Sample_3_main");
        seleniumTestBase.logOutUI();         
     }
}
