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

public class ESBCloneMediatorTest  extends TestCase {
    Selenium selenium;

    public ESBCloneMediatorTest (Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will verify the Clone mediator
	 */
     public void testAddCloneMediator(String level, String cloneContinue) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        selenium.select("mediator.clone.continue", "label="+cloneContinue);
		selenium.click("link=Add Clone Target");
        Thread.sleep(2000);
    }

    /*
	 * This method will verify the Target mediator
	 */
    public void testAddTargetMediator(String soapAction, String toAddress, String sequenceOption, String endpointOption, String seqResourceName, String eprResourceName) throws Exception{
        selenium.click("link=Target");
        Thread.sleep(2000);		
        selenium.type("mediator.target.soapaction", soapAction);
		selenium.type("mediator.target.toaddress", toAddress);
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (sequenceOption.equals("mediator.target.seq.radio.none")){
            selenium.click("mediator.target.seq.radio.none");
        } else if (sequenceOption.equals("mediator.target.seq.radio.anon")){
            selenium.click("mediator.target.seq.radio.anon");
            /*
             * Todo - Fill in the part what should be done if anon is selected
             */
        } else if (sequenceOption.equals("mediator.target.seq.radio.reg")){
            selenium.click("mediator.target.seq.radio.reg");
            selenium.click("mediator.target.seq.reg.link");
            esbCommon.testSelectResource("Sequence", seqResourceName);
        }

        if (sequenceOption.equals("epOpNone")){
            selenium.click("epOpNone");
        } else if (sequenceOption.equals("epOpAnon")){
            selenium.click("epOpAnon");
            /*
             * Todo - Fill in the part what should be done if anon is selected
             */
        } else if (sequenceOption.equals("epOpReg")){
            selenium.click("epOpReg");
		    selenium.click("regEpLink");
            esbCommon.testSelectResource("Endpoint", eprResourceName);
        }
        esbCommon.testMediatorUpdate();
    }
}