package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

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

public class ESBSetMainSequenceTest  extends TestCase {
    Selenium selenium;

    public ESBSetMainSequenceTest(Selenium _browser){
        selenium=_browser;
    }

    /*
    This method will set the main sequence with the given sequence name
     */
    public void testSetMainSequence(String sequenceName) throws Exception{
        selenium.click("link=Sequences");
        Thread.sleep(3000);
		selenium.click("//a[@onclick=\"editSequence('main')\"]");
        selenium.waitForPageToLoad("30000");
		selenium.click("link=switch to source view");
        selenium.waitForPageToLoad("30000");
		selenium.type("sequence_source", "<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"main\">\n   <syn:sequence name="+sequenceName+"/>\n</syn:sequence>");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.sequenceSave();
    }
}
