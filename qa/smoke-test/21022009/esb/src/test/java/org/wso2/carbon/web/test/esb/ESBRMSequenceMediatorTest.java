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

public class ESBRMSequenceMediatorTest extends TestCase {
    Selenium selenium;

    public ESBRMSequenceMediatorTest (Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will verify the Router mediator properties
	 */
    public void testAddRmSequenceMediator(String level, String specVersion) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(1000);

        if (specVersion.equals("v1.0")){
            selenium.click("v1.0");
        } else if (specVersion.equals("v1.1")){
            selenium.click("v1.1");
        }
    }

    /*
    This method will set the Message Sequence information
     */

    public void testSetMessageSeqInfo(String messageSeq, String xpath, String namespacePrefixSeq, String namespaceURISeq,
                                          String lastMessage, String namespacePrefixLastMsg, String namespaceURILastMsg) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (messageSeq.equals("singleRadio")){
		    selenium.click("singleRadio");
        } else if (messageSeq.equals("correlationRadio")){
            selenium.click("correlationRadio");

            selenium.type("correlation", xpath);
		    selenium.click("link=NameSpaces");
            esbCommon.testAddNamespace(namespacePrefixSeq, namespaceURISeq);

            selenium.type("last-message", lastMessage);
            selenium.click("//a[@onclick=\"showNameSpaceEditor('last-message')\"]");
            esbCommon.testAddNamespace(namespacePrefixLastMsg, namespaceURILastMsg);
        }
    }
}
