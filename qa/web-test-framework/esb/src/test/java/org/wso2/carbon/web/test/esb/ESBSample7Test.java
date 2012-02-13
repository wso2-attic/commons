package org.wso2.carbon.web.test.esb;

import org.apache.commons.io.FileUtils;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBSampleClient;

import java.io.File;
import java.io.IOException;

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

public class ESBSample7Test extends CommonSetup{

    public ESBSample7Test(String text) {
        super(text);
    }

//    <localEntry key="validate_schema">
//        <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
//                    xmlns="http://www.apache-synapse.org/test" elementFormDefault="qualified"
//                    attributeFormDefault="unqualified"
//                    targetNamespace="http://services.samples/xsd">
//            <xs:element name="getQuote">
//                <xs:complexType>
//                    <xs:sequence>
//                        <xs:element name="request">
//                            <xs:complexType>
//                                <xs:sequence>
//                                    <xs:element name="stocksymbol" type="xs:string"/>
//                                </xs:sequence>
//                            </xs:complexType>
//                        </xs:element>
//                    </xs:sequence>
//                </xs:complexType>
//            </xs:element>
//        </xs:schema>
//    </localEntry>
//
//    <in>
//        <validate>
//            <schema key="validate_schema"/>
//            <on-fail>
//                <makefault>
//                    <code value="tns:Receiver"
//                            xmlns:tns="http://www.w3.org/2003/05/soap-envelope"/>
//                    <reason value="Invalid custom quote request"/>
//                </makefault>
//                <property name="RESPONSE" value="true"/>
//                <header name="To" expression="get-property('ReplyTo')"/>
//            </on-fail>
//        </validate>
//    </in>
//    <send/>

    /*
    This method will create an inline local entry
     */
    public void addInlineLocalEntry() throws Exception{
        String content = null;

        try {
            File file = new File(".." + File.separator + "esb" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "validate.xsd");
            content = FileUtils.readFileToString(file);
        } catch (
                IOException e) {
        }
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addInlineXmlEntry("validate_schema",content);
    }

    /*
    This method will create the sample 7 sequence
     */
    public void createSequence(String seqName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBValidateMediatorTest esbValidateMediatorTest = new ESBValidateMediatorTest(selenium);
        ESBFaultMediatorTest esbFaultMediatorTest = new ESBFaultMediatorTest(selenium);
        ESBPropertyMediatorTest esbPropertyMediatorTest = new ESBPropertyMediatorTest(selenium);
        ESBHeaderMediatorTest esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);

        /*Creating sequence - sample_7*/
        esbCommon.addSequence(seqName);

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding a Validate mediator
        esbCommon.addMediators("Add Child","0","Filter","Validate");
        esbValidateMediatorTest.addValidateMediatorSchemaKey("0.0","validate_schema");
        esbCommon.mediatorUpdate();

        //Adding on-Error child mediators to the Validate mediator
        //Fault mediator
        esbCommon.addMediators("Add Child","0.0","Transform","Fault");
        esbFaultMediatorTest.setSoap11Fault("0.0.0","Server");
        esbFaultMediatorTest.setFaultCodeString("Invalid custom quote request");
        esbCommon.mediatorUpdate();

        //Property mediator
        esbCommon.addMediators("Add Child","0.0","Core","Property");
        esbPropertyMediatorTest.addBasicPropInfo("0.0.1","RESPONSE","set");
        esbPropertyMediatorTest.addPropertyMediator("Value","true","Synapse");
        esbCommon.mediatorUpdate();

        //Header mediator
        esbCommon.addMediators("Add Child","0.0","Transform","Header");
        esbHeaderMediatorTest.addHeaderMediator("0.0.2","To");
        esbHeaderMediatorTest.setHeaderAction("expression","get-property('ReplyTo')");
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("1");
        esbCommon.mediatorUpdate();

        //Saving the sequence                                               
        esbCommon.sequenceSave();
    }

    /*
    This method will add create the sample 7 sequence
     */
    public void testAddSample7Sequence() throws Exception{
      SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
      ESBCommon esbCommon = new ESBCommon(selenium);

     boolean login = selenium.isTextPresent("Sign-out");

     if (login){
         seleniumTestBase.logOutUI();
     }

    seleniumTestBase.loginToUI("admin","admin");
    addInlineLocalEntry();
    createSequence("sample_7");
    //Setting the created sequence to the main sequence
    esbCommon.setSequenceToSequence("main","sample_7");
    seleniumTestBase.logOutUI();
    invokeClient();
    }

    /*
    Executing client for sample 7 sequence
     */
    public void invokeClient() throws Exception{
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        ESBCommon esbCommon = new ESBCommon(selenium);
        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/",esbCommon.getServiceAddUrl("SimpleStockQuoteService"), "IBM");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();        
    }
}