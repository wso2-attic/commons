package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.wso2.carbon.web.test.client.ESBRMClient;/*
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

public class ESBSample101Test extends CommonSetup{

    public ESBSample101Test(String text) {
        super(text);
    }

//    <in>
//        <RMSequence single="true" version="1.0"/>
//        <send>
//           <endpoint name="reliable">
//              <address uri="http://localhost:9000/services/ReliableStockQuoteService">
//                 <enableRM/>
//                 <enableAddressing/>
//              </address>
//           </endpoint>
//        </send>
//    </in>
//    <out>
//        <header name="wsrm:SequenceAcknowledgement" action="remove"
//                xmlns:wsrm="http://schemas.xmlsoap.org/ws/2005/02/rm"/>
//        <header name="wsrm:Sequence" action="remove"
//                xmlns:wsrm="http://schemas.xmlsoap.org/ws/2005/02/rm"/>
//        <header name="wsrm:AckRequested" action="remove"
//                xmlns:wsrm="http://schemas.xmlsoap.org/ws/2005/02/rm"/>
//        <send/>
//    </out>

    /*
    This method will create a new sequence which enables RM at the ESB level and invoke using a client
     */
    public void createRmSequence(String seqName) throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBHeaderMediatorTest esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);
        ESBRMSequenceMediatorTest esbrmSequenceMediatorTest = new ESBRMSequenceMediatorTest(selenium);

        //Loging in and creating the sequence
        esbCommon.addSequence(seqName);

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding the RMSequence mediator
        esbCommon.addMediators("Add Child","0","Advanced","RMSequence");
        esbrmSequenceMediatorTest.addRmSequenceMediator("0.0","v1.0");
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","0","Core","Send");

        //Adding a send mediator with an anonymous endpoint
        esbSendMediatorTest.addAnonSendMediator("0.1");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("ReliableStockQuoteService"));
        esbSendMediatorTest.eprQosInfoSendMediator("wsAddressing",null,null,null,"wsRM",null);
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Addung the Out mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");
        esbSequenceTreePopulatorTest.clickMediator("1");

        //Adding the first Header mediator
        esbCommon.addMediators("Add Child","1","Core","Header");
        esbHeaderMediatorTest.addHeaderMediator("1.0","SequenceAcknowledgement");
        esbHeaderMediatorTest.addHeaderNameNamespace("wsrm","http://schemas.xmlsoap.org/ws/2005/02/rm");
        esbHeaderMediatorTest.removeHeader();
        esbHeaderMediatorTest.setHeaderNSVal();
        esbCommon.mediatorUpdate();

        //Adding the second Header mediator
        esbCommon.addMediators("Add Child","1","Core","Header");
        esbHeaderMediatorTest.addHeaderMediator("1.1","Sequence");
        esbHeaderMediatorTest.addHeaderNameNamespace("wsrm","http://schemas.xmlsoap.org/ws/2005/02/rm");
        esbHeaderMediatorTest.removeHeader();
        esbHeaderMediatorTest.setHeaderNSVal();        
        esbCommon.mediatorUpdate();

        //Adding the third Header mediator
        esbCommon.addMediators("Add Child","1","Core","Header");
        esbHeaderMediatorTest.addHeaderMediator("1.2","AckRequested");
        esbHeaderMediatorTest.addHeaderNameNamespace("wsrm","http://schemas.xmlsoap.org/ws/2005/02/rm");
        esbHeaderMediatorTest.removeHeader();
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","1","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("1.3");
        esbCommon.mediatorUpdate();

        //Saving the sequence
        esbCommon.sequenceSave();

        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sample_101");
    }

    /*
    This method will invoke the client
     */
    public void invokeClient() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBRMClient esbrmClient = new ESBRMClient();
        boolean requestReplyAnonResponse = esbrmClient.RMRequestReplyAnonClient("NonRMProxy","soap11","urn:getQuote","http://services.samples","getQuote","request","symbol");
//        int requestReplyAnonResponse = esbrmClient.RMRequestReplyAddressableClient("NonRMProxy","soap12","urn:getQuote","http://services.samples","getQuote","request","symbol");
//        esbrmClient.RMOnewayAnonClient("NonRMProxy","soap11","urn:Ping","http://service.esb.wso2.org","Ping","ping","pong");

        System.out.println("The response received!!!!");

        if (requestReplyAnonResponse){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
        Thread.sleep(5000);
    }

    /*
    This method will create the seqence and invoke the client
     */
    public void testSample101Config() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
         seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");
        createRmSequence("sample_101");
        seleniumTestBase.logOutUI();
        invokeClient();
    }
}
