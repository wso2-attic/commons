package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBRestClient;
import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;/*
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

public class ESBWSDLEndpointTest extends CommonSetup{

    public ESBWSDLEndpointTest(String text) {
        super(text);
    }

    /*
    This method will add a new address endpoint, assign it to a sequence and invoke using a client
     */
//    <in>
//        <validate>
//            <schema key="validate_schema"/>
//            <on-fail>
//                <!-- if the request does not validate againt schema throw a fault -->
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


    public void testVerifyWSDLEndpoints() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        String content = null;
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");

        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBValidateMediatorTest esbValidateMediatorTest = new ESBValidateMediatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBSampleClient sampleClient = new ESBSampleClient();
        ESBRestClient esbRestClient = new ESBRestClient();
        ESBFaultMediatorTest esbFaultMediatorTest = new ESBFaultMediatorTest(selenium);
        ESBPropertyMediatorTest esbPropertyMediatorTest = new ESBPropertyMediatorTest(selenium);
        ESBHeaderMediatorTest esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);

        try {
            File file = new File(".." + File.separator + "esb" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "validate.xsd");
            content = FileUtils.readFileToString(file);
        } catch (
                IOException e) {
        }

        //Adding the schema file
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addInlineXmlEntry("validate_schema",content);

        //Adding an endpoint which will be refered through the proxy wizard
        esbCommon = new ESBCommon(selenium);
        esbCommon.viewEndpoints();
        boolean endpoint = selenium.isTextPresent("wsdl_epr1");
        System.out.println(endpoint);

        if (endpoint) {
            //Do nothing
        } else {
            ESBAddWsdlEndpointTest esbAddWsdlEndpointTest = new ESBAddWsdlEndpointTest(selenium);
            esbAddWsdlEndpointTest.addAnonWsdlEndpoint();
            esbAddWsdlEndpointTest.addUriWsdlEprMandInfo("wsdl_epr1",esbCommon.getServiceAddUrl("SimpleStockQuoteService")+"?wsdl","SimpleStockQuoteService","SimpleStockQuoteServiceHttpSoap11Endpoint");
            esbAddWsdlEndpointTest.saveWsdlEndpoint();
        }

        //Creating a sequence and setting up mediators

        //Loging in and creating the sequence
        esbCommon.addSequence("wsdl_epr_test_seq");

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding the Validate mediator
        esbCommon.addMediators("Add Child","0","Filter","Validate");
        esbValidateMediatorTest.addValidateMediatorSchemaKey("0.0","validate_schema");
        esbCommon.mediatorUpdate();

        //Adding the onFail mediators
        esbSequenceTreePopulatorTest.setFocusMediator("0.0");

        //Adding the Fault mediator
        esbCommon.addMediators("Add Child","0.0","Transform","Fault");
        esbSequenceTreePopulatorTest.setFocusMediator("0.0.0");
        esbFaultMediatorTest.setSoap11Fault("0.0.0","Server");
        esbFaultMediatorTest.setFaultCodeString("Invalid custom quote request");
        esbCommon.mediatorUpdate();

        //Adding the Property mediator
        esbCommon.addMediators("Add Child","0.0","Core","Property");
        //esbSequenceTreePopulatorTest.setFocusMediator("0.0.1");
        esbPropertyMediatorTest.addBasicPropInfo("0.0.1","RESPONSE","set");
        esbPropertyMediatorTest.addPropertyMediator("value","true","Synapse");
        esbCommon.mediatorUpdate();

        //Adding the Header mediator
        esbCommon.addMediators("Add Child","0.0","Transform","Header");
        //esbSequenceTreePopulatorTest.setFocusMediator("0.0.2");
        esbHeaderMediatorTest.addHeaderMediator("0.0.2","To");
        esbHeaderMediatorTest.setHeaderAction("expression","get-property('ReplyTo')");
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","0","Core","Send");
        esbSendMediatorTest.addRegSendMediator("0.1","wsdl_epr1");

        //Adding the Out mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");
        esbSequenceTreePopulatorTest.clickMediator("1");

        //Adding the Send mediator
        esbCommon.addMediators("Add Child","1","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("1.0");
        esbCommon.sequenceSave();

        //Assigning the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","wsdl_epr_test_seq");

        //Loging out from the console
        seleniumTestBase.logOutUI();

        //Invoking the client
        boolean stockQuoteResponse =esbRestClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/StockQuote");

        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            System.out.println("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();
    }

    public void testWsdlEndPointInfo() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        getWsdlEndPointInfo("test_wsdl_info",esbCommon.getServiceAddUrl("SimpleStockQuoteService")+"?wsdl","SimpleStockQuoteService","SimpleStockQuoteServiceHttpSoap11Endpoint"
                            ,"101503", "1000", "2000", "1.0"
                            ,"101503","1", "1000"
                            ,"Execute fault sequence", "2000"
                            ,"on","on", null, null, null, null);
    }


    public void getWsdlEndPointInfo(String endpointName, String uriWSDLVal, String wsdlendpointService, String wsdlendpointPort
                                    ,String suspendErrorCode, String suspendDuration, String suspendMaxDuration, String factor
                                    ,String retryErroCode,String retryTimeOut, String retryDelay
                                    ,String actionSelect, String actionDuration
                                    ,String wsAddressing, String sepListener, String wsSecurity, String secResource, String wsRM, String rmResource) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Adding an endpoint which will be refered through the proxy wizard
        esbCommon.viewEndpoints();
        boolean endpoint = selenium.isTextPresent(endpointName);

        if (endpoint) {
            //Do nothing
        } else {
            ESBAddWsdlEndpointTest esbAddWsdlEndpointTest = new ESBAddWsdlEndpointTest(selenium);
            esbAddWsdlEndpointTest.addAnonWsdlEndpoint();
            esbAddWsdlEndpointTest.addUriWsdlEprMandInfo(endpointName, uriWSDLVal, wsdlendpointService, wsdlendpointPort);
            //Add endpoint suspend information
            esbAddWsdlEndpointTest.addWsdlEprSuspendInfo(suspendErrorCode, suspendDuration, suspendMaxDuration, factor);
            //Add endpoint retry information
            esbAddWsdlEndpointTest.addWsdlEprRetryInfo(retryErroCode,retryTimeOut, retryDelay);
            //Add endpoint timeout information
            esbAddWsdlEndpointTest.addWsdlEprTimeoutInfo(actionSelect, actionDuration);
            //Add endpoint QoS information
            esbAddWsdlEndpointTest.addWsdlEprQosInfo(wsAddressing, sepListener, wsSecurity, secResource, wsRM, rmResource);

            esbAddWsdlEndpointTest.saveWsdlEndpoint();
        }

        esbCommon.viewEndpoints();
        editEndPoint(endpointName);
        Thread.sleep(5000);
        assertEquals(endpointName, selenium.getValue("endpointName"));
        assertEquals("on", selenium.getValue("uriWSDL"));
        assertEquals(uriWSDLVal, selenium.getValue("uriWSDLVal"));
        assertEquals(wsdlendpointService, selenium.getValue("wsdlendpointService"));
        assertEquals(wsdlendpointPort, selenium.getValue("wsdlendpointPort"));
        assertEquals(suspendErrorCode, selenium.getValue("suspendErrorCode"));
        assertEquals(suspendDuration, selenium.getValue("suspendDuration"));
        assertEquals(suspendMaxDuration, selenium.getValue("suspendMaxDuration"));
        assertEquals(factor, selenium.getValue("factor"));
        assertEquals(retryErroCode, selenium.getValue("retryErroCode"));
        assertEquals(retryTimeOut, selenium.getValue("retryTimeOut"));
        assertEquals(retryDelay, selenium.getValue("retryDelay"));
        assertTrue(selenium.getSelectedValue("actionSelect").equals(actionSelect));
        assertEquals(actionDuration, selenium.getValue("actionDuration"));
        if (wsAddressing !=null)
            assertEquals("on", selenium.getValue("wsAddressing"));
        if (sepListener != null)
             assertEquals("on", selenium.getValue("sepListener"));
        if (wsSecurity != null)
             assertEquals("on", selenium.getValue("wsSecurity"));
        if (wsRM != null)
             assertEquals("on", selenium.getValue("wsRM"));
    }

    public void editEndPoint(String epName) throws Exception{
        int row_no=1;
        String ep_name="";

        if(epName!=null){
             while(!epName.equals(ep_name)){
                 ep_name=selenium.getText("//table[@id='endpointListTable']/tbody/tr["+row_no+"]/td[1]");
                 row_no=row_no+1;
             }
             row_no=row_no-1;
             if(row_no==1){
                 selenium.click("link=Edit");
                 selenium.waitForPageToLoad("30000");
             }
             else{
                 selenium.click("//a[@onclick=\"editEndpoint('"+epName+"','1')\"]");
                 selenium.waitForPageToLoad("30000");
             } 
        }
    }
}
