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

public class ESBSampleLogSequence extends CommonSetup{
    public ESBSampleLogSequence(String text) {
        super(text);
    }

//    <sequence name="test">
//    <in>
//        <send>
//            <endpoint>
//                <address uri="http://localhost:9000/services/SimpleStockQuoteService"/>
//            </endpoint>
//        </send>
//    </in>
//    <out>
//        <log level="full"/>
//        <send/>
//    </out>
//</sequence>

    public void testLogin() throws Exception {
         ESBCommon esbCommon=new ESBCommon(selenium);
         esbCommon.logoutLogin();
    }

    public void testAll() throws Exception{
         createSequence("Log_seq");
    }

    public void createSequence(String seqName) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBSendMediatorTest esbSendMediatorTest=new ESBSendMediatorTest(selenium);

        //Loging in and creating the sequence
        esbCommon.addSequence(seqName);

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","0","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("0.0");
        esbSendMediatorTest.addMandInfoSendMediator("http://localhost:9000/services/SimpleStockQuoteService");
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Adding the out mediator
        esbCommon.addMediators("Add Sibling","0","Filter","Out");

        //Adding log mediator
        esbCommon.addMediators("Add Child","1","Core","Log");
        esbCommon.mediatorUpdate();

        //Adding Send Mediator
        esbCommon.addMediators("Add Child","1","Core","Send");
        esbCommon.mediatorUpdate();

        //Saving the sequence
        esbCommon.sequenceSave();
    }
}

