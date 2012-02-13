package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBAggregateClient;

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

public class ESBAggregateMediatorMainTest  extends CommonSetup{

    public ESBAggregateMediatorMainTest(String text) {
        super(text);
    }

    public void testAddAggregateMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_aggregate_mediator");
        //Add a Aggregate mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Advanced","Aggregate");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Aggregate"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //Click on the Aggregate mediator
        selenium.click("//a[@id='mediator-0']");
        assertEquals("Aggregate Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));
        assertEquals("on", selenium.getValue("sequenceOptionAnon"));
        assertTrue(selenium.isTextPresent("exact:Aggregation Expression * NameSpaces"));
        assertTrue(selenium.isTextPresent("Correlation Expression NameSpaces"));


        //Click on 'Add Sibling' of the Aggregate mediator
        esbCommon.verifyClickAddSibling();

        ////Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Aggregate");

        //Click on the 'Delete' icon of the 'Aggregate mediator'
        esbCommon.delMediator("0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Aggregate"));
    }

    //Select the On Complete option as 'Pick From Registry '
    public void testOnCompleteOption() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        String localRegList=esbCommon.getLocalRegItems();
        String embeddedRegItem[]=new String[100];
        embeddedRegItem=esbCommon.getEmbeddedRegItems();

        esbCommon.addSequence("test_aggregate_mediator");
        esbCommon.addRootLevelChildren("Add Child","Advanced","Aggregate");

        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        selenium.click("sequenceOptionReference");
        selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('mediator.sequence')\"]");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Resource Paths \n Local Registry Select A Value"+localRegList+" \n \n Picked Path : \n /"));

        //verify embedded registry items
        /*
         If the  entry name has more than 13 characters this will not work!
        */
         selenium.click("plus_root");
         int pickedPath_no=0;
         while(selenium.isElementPresent("father_root_"+pickedPath_no)){
               assertEquals(embeddedRegItem[pickedPath_no], selenium.getText("father_root_"+pickedPath_no));
               pickedPath_no=pickedPath_no+1;
         }
         selenium.click("link=X");
    }

    public void testCreateProxy() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBSample400Test esbSample400Test=new ESBSample400Test("");
        esbSample400Test.createProxyService("proxy_aggregate");
    }


    public void testMaxMessages() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.editProxyService("proxy_aggregate");
        selenium.click("nextBtn");
		selenium.click("nextBtn");
		selenium.click("outAnonAddEdit");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Aggregate");
        Thread.sleep(2000);
		selenium.type("complete_max", "20");
		selenium.click("//input[@value='Update']");
		selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("saveBtn");
		selenium.waitForPageToLoad("30000");

        //Invoke the client
        ESBAggregateClient esbAggregateClient=new ESBAggregateClient();
        int responseCount=esbAggregateClient.aggregateClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/proxy_aggregate","http://localhost:9000/services/SimpleStockQuoteService",30);
        System.out.println("Count== "+responseCount);
        if(responseCount>20)
            throw new MyCheckedException("Aggregate mediator,Maximum number of messages that can exist in an aggregation does not work..!");
    }


    public void testMinMessages() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.editProxyService("proxy_aggregate");
        selenium.click("nextBtn");
		selenium.click("nextBtn");
		selenium.click("outAnonAddEdit");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Aggregate");
        Thread.sleep(2000);
		selenium.type("complete_min", "10");
		selenium.click("//input[@value='Update']");
		selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("saveBtn");
		selenium.waitForPageToLoad("30000");

        //Invoke the client
        ESBAggregateClient esbAggregateClient=new ESBAggregateClient();
        int responseCount=esbAggregateClient.aggregateClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/proxy_aggregate","http://localhost:9000/services/SimpleStockQuoteService",5);
        System.out.println("Count== "+responseCount);
        if(responseCount>0)
            throw new MyCheckedException("Aggregate mediator ,Minimum number of messages required for the aggregation to complete does not work..!");

    }

    public void testTimeOut() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.editProxyService("proxy_aggregate");
        selenium.click("nextBtn");
		selenium.click("nextBtn");
		selenium.click("outAnonAddEdit");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Aggregate");
        Thread.sleep(2000);
		selenium.type("complete_time", "5000");
		selenium.click("//input[@value='Update']");
		selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("saveBtn");
		selenium.waitForPageToLoad("30000");

        //Invoke the client
        ESBAggregateClient esbAggregateClient=new ESBAggregateClient();
        int responseCount=esbAggregateClient.aggregateClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/proxy_aggregate","http://localhost:9000/services/SimpleStockQuoteService",50);
        System.out.println("Count== "+responseCount);
        if(responseCount==50)
            throw new MyCheckedException("Aggregate mediator ,\"Completion Timeout\" does not work..!");

    }
    

//    public void testCorrelationExpression() throws Exception{
//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.logoutLogin();
//
//        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
//        esbAddProxyServiceTest.editProxyService("proxy_aggregate");
//        selenium.click("nextBtn");
//		selenium.click("nextBtn");
//		selenium.click("outAnonAddEdit");
//		selenium.waitForPageToLoad("30000");
//		selenium.click("link=Aggregate");
//        Thread.sleep(2000);
//        selenium.type("complete_time", "10000");
//		selenium.type("correlate_expr", "//m0:getQuoteResponse");
//		selenium.click("//input[@value='Update']");
//		selenium.click("saveButton");
//		selenium.waitForPageToLoad("30000");
//		selenium.click("saveBtn");
//		selenium.waitForPageToLoad("30000");
//
//        //Invoke the client
//        ESBAggregateClient esbAggregateClient=new ESBAggregateClient();
//        int responseCount=esbAggregateClient.aggregateClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/proxy_aggregate","http://localhost:9001/services/SimpleStockQuoteService",10);
//        System.out.println("Count== "+responseCount);
//
////        ESBThrottleMediatorMainTest esbThrottleMediatorMainTest=new ESBThrottleMediatorMainTest("");
////        esbThrottleMediatorMainTest.checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/proxy_aggregate","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",5);
//    }
//
    public void testMediatorInfomation() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBAggregateMediatorTest esbAggregateMediatorTest = new ESBAggregateMediatorTest(selenium);
        esbCommon.addSequence("sequence_aggregate");
        esbCommon.addRootLevelChildren("Add Child","Advanced","Aggregate");

        esbAggregateMediatorTest.addAggregateMediator("0");
        esbAggregateMediatorTest.addAggregateExpression("//m0:add");
        esbAggregateMediatorTest.addAggregateExpressionNameSpaces("m0","http://services/samples");
        esbAggregateMediatorTest.addCompletionInfo("1000","2","2");
        esbAggregateMediatorTest.addCorelationExpression("//m0:div");
        esbAggregateMediatorTest.addCorelationExpressionNameSpaces("m0","http://samples/services");
        esbAggregateMediatorTest.addOnCompleteMediator("sequenceOptionReference","Sequence","fault");
        esbCommon.mediatorUpdate();

        selenium.click("//a[@id='mediator-0']");
		esbCommon.clickMediatorSource("0");
        assertTrue(selenium.isTextPresent("<syn:aggregate xmlns:syn=\"http://ws.apache.org/ns/synapse\"> <syn:correlateOn xmlns:m0=\"http://samples/services\" expression=\"//m0:div\" /> <syn:completeCondition timeout=\"1\"> <syn:messageCount min=\"2\" max=\"2\" /> </syn:completeCondition> <syn:onComplete xmlns:m0=\"http://services/samples\" expression=\"//m0:add\" sequence=\"fault\" /> </syn:aggregate>"));
        selenium.click("link=switch to design view");

        //Switch back to the design view
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        assertEquals("//m0:add", selenium.getValue("aggregate_expr"));
        selenium.click("link=NameSpaces");
		assertEquals("m0", selenium.getValue("prefix0"));
		assertEquals("http://services/samples", selenium.getValue("uri0"));
		selenium.click("link=X");
		assertEquals("1000", selenium.getValue("complete_time"));
		assertEquals("2", selenium.getValue("complete_max"));
		assertEquals("2", selenium.getValue("complete_min"));
		assertEquals("//m0:div", selenium.getValue("correlate_expr"));
        selenium.click("//a[@onclick=\"showNameSpaceEditor('correlate_expr')\"]");
		assertEquals("m0", selenium.getValue("prefix0"));
		assertEquals("http://samples/services", selenium.getValue("uri0"));
		selenium.click("link=X");
		assertEquals("on", selenium.getValue("sequenceOptionReference"));
    }
}
