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
    public void addRmSequenceMediator(String level, String specVersion) throws Exception{
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

    public void setMessageSeqInfo(String messageSeq) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (messageSeq.equals("singleRadio")){
		    selenium.click("singleRadio");
        } else if (messageSeq.equals("correlationRadio")){
            selenium.click("correlationRadio");
        }
    }

    /*
    This method will the correlation expression
     */
    public void addCorrXpath(String xpath, String nsLevel, String namespacePrefixSeq, String namespaceURISeq) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.type("correlation", xpath);
        esbCommon.clickNamespaceLink();
        esbCommon.addNamespace(namespacePrefixSeq, namespaceURISeq);
    }

    /*
    This method will the last-message expression
     */
    public void addLastMessage(String lastMessage, String nsLevel, String namespacePrefixLastMsg, String namespaceURILastMsg) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.type("last-message", lastMessage);
        selenium.click("//a[@onclick=\"showNameSpaceEditor('last-message')\"]");
        esbCommon.addNamespace(namespacePrefixLastMsg, namespaceURILastMsg);
    }

}
