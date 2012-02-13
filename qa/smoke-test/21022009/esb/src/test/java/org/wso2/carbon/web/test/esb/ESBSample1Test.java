package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
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

public class ESBSample1Test  extends TestCase {
    Selenium selenium;

    public ESBSample1Test(Selenium _browser){
        selenium = _browser;
    }

//    <filter source="get-property('To')" regex=".*/StockQuote.*">
//        <send>
//            <endpoint>
//                <address uri="http://localhost:9000/services/SimpleStockQuoteService"/>
//            </endpoint>
//        </send>
//        <drop/>
//    </filter>
//    <send/>

    public void testNew() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBInMediatorTest esbInMediatorTest = new ESBInMediatorTest(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBOutMediatorTest esbOutMediatorTest = new ESBOutMediatorTest(selenium);

        ESBFilterMediatorTest esbFilterMediatorTest = new ESBFilterMediatorTest(selenium);

        //Loging in and creating the sequence
        esbCommon.testAddSequence("SampleSequence1");

        //Adding the Filter mediator
        esbCommon.testAddRootLevelChildren("Add Child", "Filter", "Filter");
        esbSequenceTreePopulatorTest.testClickMediator("0");

        //String xPath, String src, String regex, String namespacePrefix, String namespaceURI, String selectionType
        //Selection type xpath, xpathRex
        esbFilterMediatorTest.testAddFilterMediator("0",null, "get-property('To')",".*/StockQuote.*",null,null,"xpathRex");
        esbFilterMediatorTest.testAddThenChildMediators("Core", "Log");
        esbLogMediatorTest.testAddLogMediator("0","Simple");
        esbFilterMediatorTest.testAddThenChildMediators("Core", "Send");
        esbSendMediatorTest.testAddSendMediator("0","epOpAnon","", "http://localhost:9000/services/SimpleStockQuoteService","Leave As-Is", "Leave As-Is","","","","","","","","Never timeout","",null,null,null,null,null,null);
        esbFilterMediatorTest.testAddThenChildMediators("Core", "Drop");


        //Adding the Filter > Send mediator
        esbCommon.testAddRootLevelChildren("Add Sibling", "Core", "Send");

        //Saving the sequence
        esbCommon.testSequenceSave();
        }
}

