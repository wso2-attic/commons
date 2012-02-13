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

public class ESBSampleSeqForRouterMediator extends CommonSetup{
    public ESBSampleSeqForRouterMediator(String text){
        super(text);
    }

    public void testLogin() throws Exception {
         ESBCommon esbCommon = new ESBCommon(selenium);
         esbCommon.logoutLogin();
    }

    public void testAll() throws Exception{
         createSequence("Sample_router_seq");
    }
//    <syn:sequence xmlns:syn="http://ws.apache.org/ns/synapse" name="router_seq">
//  <syn:in>
//     <syn:log level="full" />
//     <syn:router continueAfter="true">
//        <syn:route xmlns:ns="http://org.apache.synapse/xsd"
//xmlns:ns2="http://org.apache.synapse/xsd"
//xmlns:m0="http://services.samples"
//expression="//m0:getQuote/m0:request/m0:symbol" match="MSFT"
//breakRouter="false">
//           <syn:target
//to="http://localhost:9000/services/SimpleStockQuoteService"
//soapAction="urn:getQuote">
//              <syn:endpoint>
//                 <syn:address
//uri="http://localhost:9000/services/SimpleStockQuoteService" />
//              </syn:endpoint>
//           </syn:target>
//        </syn:route>

//        <syn:route xmlns:ns="http://org.apache.synapse/xsd"
//xmlns:ns2="http://org.apache.synapse/xsd"
//xmlns:m0="http://services.samples"
//expression="//m0:getQuote/m0:request/m0:symbol" match="SUN"
//breakRouter="false">
//           <syn:target
//to="http://localhost:9000/services/SecureStockQuoteService"
//soapAction="urn:getQuote">
//              <syn:endpoint>
//                 <syn:address
//uri="http://localhost:9000/services/SimpleStockQuoteService" />
//              </syn:endpoint>
//           </syn:target>
//        </syn:route>

//        <syn:route xmlns:ns="http://org.apache.synapse/xsd"
//xmlns:ns2="http://org.apache.synapse/xsd"
//xmlns:m0="http://services.samples"
//expression="//m0:getQuote/m0:request/m0:symbol" match="IBM">
//           <syn:target to="" soapAction="">
//              <syn:endpoint>
//                 <syn:address
//uri="http://localhost:9000/services/SimpleStockQuoteService" />
//              </syn:endpoint>
//           </syn:target>
//        </syn:route>
//     </syn:router>
    
//     <syn:send>
//        <syn:endpoint>
//           <syn:address
//uri="http://localhost:9000/services/SimpleStockQuoteService" />
//        </syn:endpoint>
//     </syn:send>
//  </syn:in>
//  <syn:out>
//     <syn:log level="custom">
//        <syn:property name="text" value="Before aggregating the message" />
//     </syn:log>
//     <syn:aggregate>
//        <syn:correlateOn xmlns:ns="http://org.apache.synapse/xsd"
//xmlns:ns2="http://org.apache.synapse/xsd"
//xmlns:m0="http://services.samples/xsd"
//expression="//m0:getQuoteResponse" />
//        <syn:completeCondition>
//           <syn:messageCount min="-1" max="-1" />
//        </syn:completeCondition>
//        <syn:onComplete xmlns:ns="http://org.apache.synapse/xsd"
//xmlns:ns2="http://org.apache.synapse/xsd"
//xmlns:m0="http://services.samples/xsd"
//expression="//m0:getQuoteResponse">
//           <syn:log level="full">
//              <syn:property name="text" value="This is the aggregated
//message" />
//           </syn:log>
//           <syn:send />
//        </syn:onComplete>
//     </syn:aggregate>
//  </syn:out>
//</syn:sequence>

    public void createSequence(String seqName) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        //ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBRouterMediatorTest esbRouterMediatorTest=new ESBRouterMediatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBAggregateMediatorTest esbAggregateMediatorTest=new ESBAggregateMediatorTest(selenium);

        //creating the sequence
        esbCommon.addSequence(seqName);

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child", "Filter", "In");

        //Adding the Log mediator
        esbCommon.addMediators("Add Child","0", "Core", "Log");
        esbLogMediatorTest.addLogMediator("0.0","Full");
        esbCommon.mediatorUpdate();

        //Adding the Router mediator
        esbCommon.addMediators("Add Child","0", "Filter", "Router");
        esbRouterMediatorTest.clickAddRoute();
        esbRouterMediatorTest.addRouterMediator("0.1","Yes");
        esbCommon.mediatorUpdate();

        //Adding Route information
        esbRouterMediatorTest.addRoute("0.1.0","No", "//m0:getQuote/m0:request/m0:symbol" );
        esbRouterMediatorTest.addRoutePattern("MSFT");
        esbRouterMediatorTest.addRouterNamespace("ns","http://org.apache.synapse/xsd");
        esbRouterMediatorTest.addRouterNamespace("ns2","http://org.apache.synapse/xsd");
        esbRouterMediatorTest.addRouterNamespace("m0","http://services.samples");
        esbCommon.mediatorUpdate();
        //Adding target information
        esbRouterMediatorTest.addTarget("0.1.0.0");
        esbRouterMediatorTest.addTargetAnonEndpoint("http://localhost:9000/services/SimpleStockQuoteService");
        Thread.sleep(2000);
        esbRouterMediatorTest.addTargetSoapAction("urn:getQuote");
        esbRouterMediatorTest.addTargetToAddress("http://localhost:9000/services/SimpleStockQuoteService");
        esbCommon.mediatorUpdate();

        //Adding the second Route information
       // esbSequenceTreePopulatorTest.clickMediator("0.1");
        selenium.click("//a[@id='mediator-0.1']");
        Thread.sleep(2000);
        esbRouterMediatorTest.clickAddRoute();
        esbRouterMediatorTest.addRoute("0.1.1","No", "//m0:getQuote/m0:request/m0:symbol" );
        esbRouterMediatorTest.addRoutePattern("SUN");
        esbRouterMediatorTest.setNsLevel();
        esbRouterMediatorTest.addRouterNamespace("ns","http://org.apache.synapse/xsd");
        esbRouterMediatorTest.addRouterNamespace("ns2","http://org.apache.synapse/xsd");
        esbRouterMediatorTest.addRouterNamespace("m0","http://services.samples");
        esbCommon.mediatorUpdate();
        //Adding target information
        esbRouterMediatorTest.addTarget("0.1.1.0");
        esbRouterMediatorTest.addTargetAnonEndpoint("http://localhost:9000/services/SimpleStockQuoteService");
        Thread.sleep(2000);
        esbRouterMediatorTest.addTargetSoapAction("urn:getQuote");
        esbRouterMediatorTest.addTargetToAddress("http://localhost:9000/services/SimpleStockQuoteService");
        esbCommon.mediatorUpdate();

        //Adding the third Route information
        selenium.click("//a[@id='mediator-0.1']");
        Thread.sleep(2000);
        esbRouterMediatorTest.clickAddRoute();
        esbRouterMediatorTest.addRoute("0.1.2","No", "//m0:getQuote/m0:request/m0:symbol" );
        esbRouterMediatorTest.addRoutePattern("IBM");
        esbRouterMediatorTest.setNsLevel();
        esbRouterMediatorTest.addRouterNamespace("ns","http://org.apache.synapse/xsd");
        esbRouterMediatorTest.addRouterNamespace("ns2","http://org.apache.synapse/xsd");
        esbRouterMediatorTest.addRouterNamespace("m0","http://services.samples");
        esbCommon.mediatorUpdate();
        //Adding target information
        esbRouterMediatorTest.addTarget("0.1.2.0");
        esbRouterMediatorTest.addTargetAnonEndpoint("http://localhost:9000/services/SimpleStockQuoteService");
        esbCommon.mediatorUpdate();

        //Adding Send mediator
        esbCommon.addMediators("Add Child","0", "Core", "Send");
        esbSendMediatorTest.addAnonSendMediator("0.2");
        esbSendMediatorTest.addMandInfoSendMediator("http://localhost:9000/services/SimpleStockQuoteService");
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Adding the out mediator
        esbCommon.addMediators("Add Sibling","0", "Filter", "Out");

        //Adding the Log mediator
        esbCommon.addMediators("Add Child","1", "Core", "Log");
        esbLogMediatorTest.addLogMediator("1.0","Custom");
        esbLogMediatorTest.addLogPropety("text","Value","Before aggregating the message");
        esbCommon.mediatorUpdate();

        //Adding the Aggregate Mediator
        esbCommon.addMediators("Add Sibling","1.0", "Advanced", "Aggregate");
        esbAggregateMediatorTest.addAggregateExpression("//m0:getQuoteResponse");
        esbCommon.setNsLevel();
        esbAggregateMediatorTest.addAggregateExpressionNameSpaces("ns","http://org.apache.synapse/xsd");
        esbAggregateMediatorTest.addAggregateExpressionNameSpaces("ns2","http://org.apache.synapse/xsd");
        esbAggregateMediatorTest.addAggregateExpressionNameSpaces("m0","http://services.samples/xsd");

        Thread.sleep(2000);
        esbAggregateMediatorTest.addCorelationExpression("//m0:getQuoteResponse");
        esbCommon.setNsLevel();
        esbAggregateMediatorTest.addCorelationExpressionNameSpaces("ns","http://org.apache.synapse/xsd");
        esbAggregateMediatorTest.addCorelationExpressionNameSpaces("ns2","http://org.apache.synapse/xsd");
        esbAggregateMediatorTest.addCorelationExpressionNameSpaces("m0","http://services.samples/xsd");
        esbCommon.mediatorUpdate();

        //Adding the Log mediator
        esbCommon.addMediators("Add Child","1.1", "Core", "Log");
        esbLogMediatorTest.addLogMediator("1.1.0","Full");
        esbLogMediatorTest.setPropNo();
        esbLogMediatorTest.addLogPropety("text","Value","This is the aggregated message");
        esbCommon.mediatorUpdate();

        //Adding the send mediator
        esbCommon.addMediators("Add Child","1.1", "Core", "Send");
        esbCommon.mediatorUpdate();

        esbCommon.sequenceSave();
    }
}
