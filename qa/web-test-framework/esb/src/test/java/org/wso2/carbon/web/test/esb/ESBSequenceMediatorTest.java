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

public class ESBSequenceMediatorTest extends TestCase{
    Selenium selenium;

    public ESBSequenceMediatorTest(Selenium _browser){
        selenium=_browser;
    }

    public void addSequenceMediator(String refSequence) throws Exception {
        //Selecting resources from the Registry Browser
        selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('seq_ref')\"]");
        Thread.sleep(2000);

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.selectResource("Sequence",refSequence);
    }
}
