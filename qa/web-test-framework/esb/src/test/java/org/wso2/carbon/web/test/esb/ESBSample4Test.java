package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBSampleClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;/*
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

public class ESBSample4Test extends CommonSetup{

    public ESBSample4Test(String text) {
        super(text);
    }

//        <sequence name="fault">
//            <log level="custom">
//                <property name="text" value="An unexpected error occured"/>
//                <property name="message" expression="get-property('ERROR_MESSAGE')"/>
//            </log>
//            <drop/>
//        </sequence>
//
//        <sequence name="sunErrorHandler">
//            <log level="custom">
//                <property name="text" value="An unexpected error occured for stock SUN"/>
//                <property name="message" expression="get-property('ERROR_MESSAGE')"/>
//            </log>
//            <drop/>
//        </sequence>
//
//        <!-- default message handling sequence used by Synapse - named 'main' -->
//        <sequence name="main">
//            <in>
//                <switch source="//m0:getQuote/m0:request/m0:symbol" xmlns:m0="http://services.samples/xsd">
//                    <case regex="IBM">
//                        <send>
//                            <endpoint><address uri="http://localhost:9000/services/SimpleStockQuoteService"/></endpoint>
//                        </send>
//                    </case>
//                    <case regex="MSFT">
//                        <send>
//                            <endpoint key="bogus"/>
//                        </send>
//                    </case>
//                    <case regex="SUN">
//                        <sequence key="sunSequence"/>
//                    </case>
//                </switch>
//                <drop/>
//            </in>
//
//            <out>
//                <send/>
//            </out>
//        </sequence>
//
//        <sequence name="sunSequence" onError="sunErrorHandler">
//            <send>
//                <endpoint key="sunPort"/>
//            </send>
//        </sequence>

    /*
    This method will create the stockquote sequence
     */
    public void addSampleFault() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);

        /*Creating sequence - stockquote*/
        esbCommon.addSequence("sample_4_fault");

        //Adding the Log mediator
        esbCommon.addRootLevelChildren("Add Child", "Core", "Log");
        esbLogMediatorTest.addLogMediator("0","Custom");
        esbLogMediatorTest.addLogPropety("text","Value","An unexpected error occured");
        esbLogMediatorTest.addLogPropety("message","Expression","get-property('ERROR_MESSAGE')");
        esbCommon.mediatorUpdate();

        esbCommon.addRootLevelChildren("Add Child", "Core", "Drop");
        esbCommon.sequenceSave();
    }

    /*
    This method will create the sunErrorHandler sequence
     */
    public void addSunErrorHandler() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);        
        /*Creating sequence - sunErrorHandler*/
        esbCommon.addSequence("sunErrorHandler");

        //Adding the Log mediator
        esbCommon.addRootLevelChildren("Add Child", "Core", "Log");
        esbLogMediatorTest.addLogMediator("0","Custom");
        esbLogMediatorTest.setPropNo();
        esbLogMediatorTest.addLogPropety("text","Value","An unexpected error occured");
        esbLogMediatorTest.addLogPropety("message","Expression","get-property('ERROR_MESSAGE')");
        esbCommon.mediatorUpdate();

        esbCommon.addRootLevelChildren("Add Child", "Core", "Drop");
        esbCommon.sequenceSave();
    }

    /*
    This method will create the sunSequence sequence
     */
    public void addSunSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        
        /*Creating sequence - sunSequence*/
        esbCommon.addSequence("sunSequence");

        //Adding the Send mediator
        esbCommon.addRootLevelChildren("Add Child", "Core", "Send");
        esbSendMediatorTest.addRegSendMediator("0","sunPort");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();
    }

    /*
    This method will create the  sample_4
     */
    public void addSequence(String seqName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBSwitchMediatorTest esbSwitchMediatorTest = new ESBSwitchMediatorTest(selenium);
        ESBSequenceMediatorTest esbSequenceMediatorTest = new ESBSequenceMediatorTest(selenium);

        /*Creating sequence - sample_4*/
        esbCommon.addSequence(seqName);
        esbCommon.setOnErrorSeq("sunErrorHandler");

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding a Switch mediator
        esbCommon.addMediators("Add Child","0","Filter","Switch");
        esbSwitchMediatorTest.addSwitchMediator("0.0","//m0:getQuote/m0:request/m0:symbol");
        esbSwitchMediatorTest.addSwitchNamespace("m0","http://services.samples");
        esbCommon.mediatorUpdate();

        //Adding the first level Case mediator
        esbSwitchMediatorTest.addCase("0.0","IBM","0.0.0");
        esbCommon.mediatorUpdate();
        esbSwitchMediatorTest.addCaseChildMediator("0.0.0", "Core","Send");
        esbSendMediatorTest.addAnonSendMediator("0.0.0.0");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Adding the second level Case mediator
        esbSwitchMediatorTest.addCase("0.0","MSFT","0.0.1");
        esbCommon.mediatorUpdate();
        esbSwitchMediatorTest.addCaseChildMediator("0.0.1", "Core","Send");
        esbSequenceTreePopulatorTest.clickMediator("0.0.1");
        esbSendMediatorTest.addRegSendMediator("0.0.1.0","bogus");
        esbCommon.mediatorUpdate();

        //Adding the third level Case mediator
        esbSwitchMediatorTest.addCase("0.0","SUN","0.0.2");
        esbCommon.mediatorUpdate();
        esbSwitchMediatorTest.addCaseChildMediator("0.0.2", "Core","Sequence");
        esbSequenceTreePopulatorTest.clickMediator("0.0.2");
        esbSequenceMediatorTest.addSequenceMediator("sunSequence");
        esbCommon.mediatorUpdate();

        //Adding the Drop mediator
        esbCommon.addMediators("Add Child","0","Core","Drop");

        //Adding an Out mediator
        esbCommon.addRootLevelChildren("Add Child", "Filter", "Out");
        esbSequenceTreePopulatorTest.clickMediator("1");

        //Adding a Send mediator        
        esbCommon.addMediators("Add Child","1", "Core", "Send");
        esbSendMediatorTest.addNormalSendMediator("1.0");
        esbCommon.sequenceSave();
    }

    /*
    This method will add the bogus endpoint
     */
    public void addEndpoints() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        
        //Adding an address endpoint - bogus
        esbCommon.viewEndpoints();

        boolean addbogusEprName = selenium.isTextPresent("bogus");
        if (addbogusEprName){
            //Do nothing
        } else{
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.addAnonAddressEndpoint();
            esbAddAddressEndpointTest.addAddressEprMandatoryInfo("bogus","http://localhost:9000/bogus");
            esbAddAddressEndpointTest.saveAddressEndpoint();
        }

        //Adding an address endpoint - sunPort
        esbCommon.viewEndpoints();

        boolean addsunPortEpr = selenium.isTextPresent("sunPort");
        if (addsunPortEpr){
            //Do nothing
        } else{
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.addAnonAddressEndpoint();
            esbAddAddressEndpointTest.addAddressEprMandatoryInfo("sunPort","http://localhost:9000/sunPort");
            esbAddAddressEndpointTest.saveAddressEndpoint();
        }
    }

    /*
    Executing client for invokeMsftClient Proxy Service
     */
    public void invokeMsftClient() throws Exception{
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        ESBCommon esbCommon = new ESBCommon(selenium);

        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/",null, "IBM");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();        
    }

    /*
    Executing client for invokeSunClient Proxy Service
     */
    public void invokeSunClient() throws Exception{
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        ESBCommon esbCommon = new ESBCommon(selenium);
        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/",null, "IBM");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            System.out.println("Client Failed!!!!");
        }
        Thread.sleep(5000);
    }

    public void testSample4Config() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        
         boolean login = selenium.isTextPresent("Sign-out");

         if (login){
             seleniumTestBase.logOutUI();
         }

        seleniumTestBase.loginToUI("admin","admin");
        addEndpoints();
        addSampleFault();
        addSunErrorHandler();
        addSunSequence();
        addSequence("sample_4");

        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sample_4");
        esbCommon.deleteEndpoint("bogus");
        esbCommon.deleteEndpoint("sunPort");
        seleniumTestBase.logOutUI();
        invokeMsftClient();
        invokeSunClient();
    }
}
