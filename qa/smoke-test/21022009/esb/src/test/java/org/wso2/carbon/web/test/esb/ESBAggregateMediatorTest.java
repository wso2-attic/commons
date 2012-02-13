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

public class ESBAggregateMediatorTest   extends TestCase {
    Selenium selenium;

    public ESBAggregateMediatorTest (Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add an Aggregate mediator
	 */
    public void testAddAggregateMediator(String level) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
    }

    /*
    This method will add the aggregate expression
     */
    public void testAddAggregateExpression(String aggregateExp, String namespacePrefixAgg, String namespaceURIAgg) throws Exception{
        selenium.type("aggregate_expr", aggregateExp);
		selenium.click("link=NameSpaces");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddNamespace(namespacePrefixAgg, namespaceURIAgg);
    }

    /*
    This method will add the complete timeout, completion max message and completion min message properties
     */

    public void testAddCompletionInfo(String completeTime, String completeMax, String completeMin) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        
        selenium.type("complete_time", completeTime);
		selenium.type("complete_max", completeMax);
		selenium.type("complete_min", completeMin);

    }

    /*
    This method will add onComplete mediators to the Aggregate mediator
     */
    public void testAddOnCompleteMediator(String sequenceOption, String resourceType, String resourceName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (sequenceOption.equals("sequenceOptionReference")){
            selenium.click("sequenceOptionReference");
		    selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('mediator.sequence')\"]");
            esbCommon.testSelectResource(resourceType, resourceName);
        } else if (sequenceOption.equals("sequenceOptionAnon")){
            selenium.click("sequenceOptionAnon");
        }
    }

    /*
    This method will add the corelation expression information
     */
    public void testAddCorelationExpression(String correlateExp, String namespacePrefixCorr,String namespaceURICorr) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
		selenium.type("correlate_expr", correlateExp);
		selenium.click("//a[@onclick=\"showNameSpaceEditor('correlate_expr')\"]");

        esbCommon.testAddNamespace(namespacePrefixCorr, namespaceURICorr);
    }

    /*
    This method will add child mediators to the aggregate mediator
     */
     public void testAddChildMediator (String nodeType, String mediatorCategory, String mediatorName) throws Exception{
        if (nodeType!=null){
            selenium.click("link="+nodeType);
        }

        if (mediatorCategory!=null){
            selenium.click("link="+mediatorCategory);
        }

        Thread.sleep(1000);
        selenium.click("link="+mediatorName);
		Thread.sleep(2000);
    }
}

