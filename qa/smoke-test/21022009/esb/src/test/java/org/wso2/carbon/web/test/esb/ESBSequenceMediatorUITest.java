package org.wso2.carbon.web.test.esb;

import com.thoughtworks.selenium.*;
import junit.framework.TestCase;
        
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

public class ESBSequenceMediatorUITest extends TestCase{
    Selenium selenium;

    public ESBSequenceMediatorUITest(Selenium _browser){
        selenium=_browser;
    }

    /*
     * This method will be called to verify the mediator properties of the Sequence mediator
     */
    public void testVerifySequenceMediator() throws Exception{    
        //Verifying the properties of the Sequence mediator
        assertTrue(selenium.isTextPresent("Sequence"));
		assertTrue(selenium.isTextPresent("Sequence Mediator"));
		assertTrue(selenium.isTextPresent("Referring sequence*"));
        assertTrue(selenium.isTextPresent("Registry Browser"));
    
        //Verifying the Help link on the mediator and its content
//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.testMediatorHelp("Sequence Mediator");
    }
}
