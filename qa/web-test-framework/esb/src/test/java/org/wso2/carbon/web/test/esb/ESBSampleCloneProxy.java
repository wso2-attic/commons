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

public class ESBSampleCloneProxy extends CommonSetup{
        public  ESBSampleCloneProxy(String text){
        super(text);
    }


//   <proxy name="CloneProxy">
//    <target>
//     <inSequence>
//      <clone>
//         <target soapAction="urn:getQuote">
//            <sequence>
//               <validate xmlns:s11="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns2="http://org.apache.synapse/xsd" xmlns:s12="http://www.w3.org/2003/05/soap-envelope" source="s11:Body/child::*[position()=1] | s12:Body/child::*[position()=1]">
//                  <schema key="validate_schema" />
//                  <on-fail>
//                     <makefault version="soap12">
//                        <code xmlns:soap12Env="http://www.w3.org/2003/05/soap-envelope" value="soap12Env:Receiver" />
//                        <reason value="Invalid custom quote request" />
//                        <node></node>
//                        <role></role>
//                     </makefault>
//                     <property name="RESPONSE" value="true" scope="default" />
//                     <header name="To" expression="get-property('ReplyTo')" />
//                  </on-fail>
//               </validate>
//               <log level="full" separator=", ">
//                  <property name="text" value="This is the 1st message" />
//               </log>
//               <send>
//                  <endpoint key="secure_endpoint" />
//               </send>
//            </sequence>
//         </target>
//      </clone>
//   </inSequence>
//   <outSequence>
//      <header xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" name="wsse:Security" action="remove" />
//      <log level="custom" separator=", ">
//         <property name="text" value="Before aggregating the message" />
//      </log>
//      <aggregate>
//         <completeCondition>
//            <messageCount min="-1" max="-1" />
//         </completeCondition>
//         <onComplete xmlns:ns2="http://org.apache.synapse/xsd" xmlns:m0="http://services.samples/xsd" expression="//m0:getQuoteResponse">
//            <log level="full" separator=", ">
//               <property name="text" value="This is the aggregated message" />
//            </log>
//            <send />
//         </onComplete>
//      </aggregate>
//   </outSequence>
//  </target>
//</proxy>
    

    public void createProxyService(String proxyName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        ESBCloneMediatorTest esbCloneMediatorTest=new ESBCloneMediatorTest(selenium);
        ESBAggregateMediatorTest esbAggregateMediatorTest=new ESBAggregateMediatorTest(selenium);
        ESBValidateMediatorTest esbValidateMediatorTest=new ESBValidateMediatorTest(selenium);
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBPropertyMediatorTest esbPropertyMediatorTest=new ESBPropertyMediatorTest(selenium);
        ESBHeaderMediatorTest esbHeaderMediatorTest=new ESBHeaderMediatorTest(selenium);
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest=new ESBSendMediatorTest(selenium);
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);


        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("validate_schema","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/validate/validate.xsd");

        esbAddProxyServiceTest.addProxyName(proxyName, "Custom Proxy");
        //set Proxy Service Transport settings
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.setTransport("https");
        esbAddProxyServiceTest.clickNext();

        //Create InSequence
        esbAddProxyServiceTest.selectInSequence("inSeqOpAnon", null);
        //Adding the Clone mediator
        esbCommon.addRootLevelChildren("Add Child","Advanced","Clone");
        esbCloneMediatorTest.addCloneMediator("0","No");
        esbCloneMediatorTest.addCloneTarget("0");
       // esbCommon.mediatorUpdate();

        //Adding the target mediator infomation
        esbCloneMediatorTest.addTargetMediator("0.0","urn:getQuote","", "anonymous", "none", null, null);
        esbCommon.mediatorUpdate();
        selenium.click("link=Target");
        Thread.sleep(2000);
        //Adding the validate mediator
        esbCommon.addMediators("Add Child","0.0","Filter","Validate");
        esbValidateMediatorTest.addValidateMediatorSchemaKey("0.0.0", "validate_schema");
        esbValidateMediatorTest.addSource("s11:Body/child::*[position()=1] | s12:Body/child::*[position()=1]");
        esbValidateMediatorTest.addValidateNamespace("s11","http://schemas.xmlsoap.org/soap/envelope/");
        esbValidateMediatorTest.addValidateNamespace("s12","http://www.w3.org/2003/05/soap-envelope");
        esbValidateMediatorTest.addValidateNamespace("ns2","http://org.apache.synapse/xsd");
        esbCommon.mediatorUpdate();

        //Adding the Fault mediator
        esbCommon.addMediators("Add Child","0.0.0","Transform","Fault");
        esbFaultMediatorTest.setSoap12Fault("0.0.0.0","Receiver");
        esbFaultMediatorTest.setFaultCodeString("Invalid custom quote request");
        esbCommon.mediatorUpdate();

        //Adding the Property mediator
        esbCommon.addMediators("Add Sibling","0.0.0.0","Core","Property");
        esbPropertyMediatorTest.addBasicPropInfo("0.0.0.1","RESPONSE","Set");
        esbPropertyMediatorTest.addPropertyMediator("value", "true", "Synapse");
        esbCommon.mediatorUpdate();

        //Adding the Header mediator
        esbCommon.addMediators("Add Sibling","0.0.0.1","Transform","Header");
        esbHeaderMediatorTest.addHeaderMediator("0.0.0.2", "To");
        esbHeaderMediatorTest.setHeaderAction("Expression", "get-property('ReplyTo')");
        esbHeaderMediatorTest.addHeaderExpNamespace("ns2","http://org.apache.synapse/xsd");
        esbHeaderMediatorTest.addHeaderExpNamespace("s11","http://schemas.xmlsoap.org/soap/envelope/");
        esbHeaderMediatorTest.addHeaderExpNamespace("s12","http://www.w3.org/2003/05/soap-envelope");
        esbCommon.mediatorUpdate();

        //Adding the Log mediator
        esbCommon.addMediators("Add Sibling","0.0.0","Core","Log");
        esbLogMediatorTest.addLogMediator("0.0.1","Full");
        esbLogMediatorTest.addLogPropety("text","Value","This is the 1st message");
        esbCommon.mediatorUpdate();

        //Adding the send mediator
        esbCommon.addMediators("Add Sibling","0.0.1","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("0.0.2");
        esbAddAddressEndpointTest.addAddressEprMandatoryInfo(null,esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbAddAddressEndpointTest.saveAddressEndpoint();
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        //create out sequence
        selenium.click("nextBtn");
        esbAddProxyServiceTest.selectOutSequence("outSeqOpAnon", null);
        //Adding the Header mediator
        esbCommon.addRootLevelChildren("Add Child","Transform","Header");
        esbHeaderMediatorTest.addHeaderMediator("0", "Security");
        esbHeaderMediatorTest.addHeaderNameNamespace("wsse", "http://docs.oasis-open.org/wss/2004/01");
        esbHeaderMediatorTest.removeHeader();
        esbCommon.mediatorUpdate();

        //Adding the Log mediaor
        esbLogMediatorTest.setPropNo();
        esbCommon.addMediators("Add Sibling","0","Core","Log");
        esbLogMediatorTest.addLogMediator("1","Custom");
        esbLogMediatorTest.addLogPropety("text","Value","Before aggregating the message");
        esbCommon.mediatorUpdate();

        //Adding the Aggregate mediator
        esbCommon.addMediators("Add Sibling","1","Advanced","Aggregate");
        Thread.sleep(2000);
        esbAggregateMediatorTest.addAggregateExpression("//m0:getQuoteResponse");
        esbAggregateMediatorTest.addAggregateExpressionNameSpaces("m0","http://services.samples");
        esbCommon.mediatorUpdate();

        //Adding the Log mediator
        esbLogMediatorTest.setPropNo();
        esbCommon.addMediators("Add Child","2","Core","Log");
        esbLogMediatorTest.addLogMediator("2.0","Full");
        esbLogMediatorTest.addLogPropety("text","Value","This is the aggregated message");
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","2","Core","Send");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        Thread.sleep(2000);
        selenium.click("saveBtn");
        selenium.waitForPageToLoad("50000");
    }

    public void testCreateProxy() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        createProxyService("test_clone_proxy");
        
    }
}
