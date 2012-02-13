package org.wso2.carbon.web.test.esb;

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

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;


public class ESBIterateMediatorTest extends TestCase {
    Selenium selenium;

    public ESBIterateMediatorTest (Selenium _browser){
		selenium = _browser;
    }

    public void addMediator(String toContinueParent,String toPreservePayload,String iterateExpression,String attachPath) throws Exception{

        selenium.select("continueParent", "label="+toContinueParent);
        selenium.select("preservePayload", "label="+toPreservePayload);

        if(iterateExpression!=null)
            selenium.type("itr_expression", iterateExpression);
        if(attachPath!=null)
            selenium.type("attach_path", attachPath);
   }
    
    int iterateExpressionNsCount=0;
    //verify the NameSpaces for Iterate Expression
    public void testAddNameSpacesToIterateExpression(String prefix,String url) throws Exception {
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.click("link=Namespaces");
        esbCommon.addNamespace(prefix,url);
        esbCommon.nsLevel=iterateExpressionNsCount;
        iterateExpressionNsCount=iterateExpressionNsCount+1;
    }

    public void setIterateExpressionNsCount() throws Exception{
        iterateExpressionNsCount=0;
    }

    int attachPathNsCount=0;
    //verify the NameSpaces for Attach path
    public void addNameSpacesToAttachPath(String prefix,String url) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.click("//a[@onclick=\"showNameSpaceEditor('attach_path')\"]");
        esbCommon.addNamespace(prefix,url);
        esbCommon.nsLevel=attachPathNsCount;
        attachPathNsCount=attachPathNsCount+1;
    }

    public void setAttachPathNsCount() throws Exception{
        attachPathNsCount=0;
    }


    /*
	 * This method will verify the Target mediator
	 */
    public void addTargetMediator(String soapAction, String toAddress, String sequenceOption, String endpointOption, String seqResourceName, String eprResourceName) throws Exception{
        selenium.click("link=Target");
        Thread.sleep(2000);
        if(soapAction!=null)
            selenium.type("mediator.target.soapaction", soapAction);
        if(toAddress!=null)
		    selenium.type("mediator.target.toaddress", toAddress);
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (sequenceOption.equalsIgnoreCase("none")){
            selenium.click("mediator.target.seq.radio.none");
        } else if (sequenceOption.equalsIgnoreCase("anonymous")){
            selenium.click("mediator.target.seq.radio.anon");
            /*
             * Todo - Fill in the part what should be done if anon is selected
             */
        } else if (sequenceOption.equalsIgnoreCase("Pick From Registry")){
            selenium.click("mediator.target.seq.radio.reg");
            selenium.click("mediator.target.seq.reg.link");
            esbCommon.selectResource("Sequence", seqResourceName);
        }

        if (endpointOption.equalsIgnoreCase("none")){
            selenium.click("epOpNone");
        } else if (endpointOption.equalsIgnoreCase("anonymous")){
            selenium.click("epOpAnon");
            /*
             * Todo - Fill in the part what should be done if anon is selected
             */
        } else if (endpointOption.equalsIgnoreCase("Pick From Registry")){
            selenium.click("epOpReg");
		    selenium.click("regEpLink");
            esbCommon.selectResource("Enpoint", eprResourceName);
        }
    }
    
}
