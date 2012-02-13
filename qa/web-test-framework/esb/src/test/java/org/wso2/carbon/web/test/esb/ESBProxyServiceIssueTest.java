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

import org.wso2.carbon.web.test.client.ESBSampleClient;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;



public class ESBProxyServiceIssueTest extends CommonSetup{

    public ESBProxyServiceIssueTest(String text) {
        super(text);
    }

    public void testCreateProxy() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        createProxy("test_proxy1");
    }

    /*
    https://support.wso2.com/jira/browse/INTMNTHCDEVSPRT-193
    */
    public void testdisableSOAP12() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.editProxyService("test_proxy1");
        Thread.sleep(2000);
        esbAddProxyServiceTest.addParameters("disableSOAP12", "true");
        selenium.click("nextBtn");
        selenium.click("nextBtn");
        selenium.click("saveBtn");
        selenium.waitForPageToLoad("50000");

        boolean soap12=checkWSDLofProxy("test_proxy1","Soap12");
        if(soap12)
           throw new MyCheckedException("SOAP12 is disabled in proxy..WSDL should not contain the SOAP12..Error!!!!");
    }

    public void testdisableSOAP11() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.editProxyService("test_proxy1");
        Thread.sleep(2000);
        esbAddProxyServiceTest.addParameters("disableSOAP11", "true");
        selenium.click("nextBtn");
        selenium.click("nextBtn");
        selenium.click("saveBtn");
        selenium.waitForPageToLoad("50000");

        boolean soap12=checkWSDLofProxy("test_proxy1","Soap11");
        if(soap12)
           throw new MyCheckedException("SOAP11 is disabled in proxy..WSDL should not contain the SOAP11..Error!!!!");
    }


    /*
    https://support.wso2.com/jira/browse/INTMNTHCDEVSPRT-194,202
    */
    public void testTurningoffHTTP() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.editProxyService("test_proxy1");
        Thread.sleep(5000);
        //delete the parameters
        esbAddProxyServiceTest.deleteParameters("disableSOAP12");
        esbAddProxyServiceTest.deleteParameters("disableSOAP11");
        //Turn off the http
        esbAddProxyServiceTest.removeTransport("http");
        Thread.sleep(2000);
        selenium.click("nextBtn");
        selenium.click("nextBtn");
        selenium.click("saveBtn");
        selenium.waitForPageToLoad("50000");

        //verify that the http has disable in wsdl
        boolean httpEp=checkWSDLofProxy("test_proxy1","HttpEndpoint");
        boolean httpBinding=checkWSDLofProxy("test_proxy1","HttpBinding");
        if(httpEp || httpBinding)
           throw new MyCheckedException("http is turn off in proxy..WSDL should not contain the HTTP..Error!!!!");
    }


    /*
    https://support.wso2.com/jira/browse/INTMNTHCDEVSPRT-195
    */
    public void testDeactivateProxy() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.editProxyService("test_proxy1");
        Thread.sleep(5000);
        //Turn on the http
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.setTransport("https");
        Thread.sleep(2000);
        selenium.click("nextBtn");
        selenium.click("nextBtn");
        selenium.click("saveBtn");
        selenium.waitForPageToLoad("50000");

        //deactivate the proxy
        esbAddProxyServiceTest.activateDeactivateProxyService("test_proxy1","deactivate");
        //Invoke the Client
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/test_proxy1",esbCommon.getServiceAddUrl("SimpleStockQuoteService"),"IBM");
        if (stockQuoteResponse){
            throw new MyCheckedException("\"test_proxy1\" proxy service is deactivated..Client invocation can not be successed..!!!!");
        }else{
            System.out.println("\"test_proxy1\" proxy service is successfully deactivated..!");
        }
        Thread.sleep(5000);

        //activate the proxy
        esbAddProxyServiceTest.activateDeactivateProxyService("test_proxy1","activate");
        //Invoke the Client
        stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/test_proxy1",esbCommon.getServiceAddUrl("SimpleStockQuoteService"),"IBM");
        if (stockQuoteResponse){
            System.out.println("\"test_proxy1\" proxy service is successfully activated..!");
        }else{
            throw new MyCheckedException("\"test_proxy1\" proxy service is activated..Client invocation can not be failed..!!!!");
        }
        Thread.sleep(5000);
    }


    /*
    https://support.wso2.com/jira/browse/INTMNTHCDEVSPRT-6
    */
    //Proxy Service erased when restarting ESB

    public void testRestartServer() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.restartSever();
        //verify in the proxy list
        esbCommon.logoutLogin();
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("test_proxy1"));

        //verify in the synapse
        assertTrue(esbCommon.verifyManageSynapseConfig("proxy name=\"test_proxy1\""));
    }


    public void createProxy(String proxyName) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.addProxyName(proxyName, "Custom Proxy");
        //esbAddProxyServiceTest.transportSettings("http", "https", null, null, null);
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.setTransport("https");
        esbAddProxyServiceTest.clickNext();

        //****setting up of the In Sequence of the Proxy Service*****//
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        esbAddProxyServiceTest.selectInSequence("inSeqOpAnon", null);
        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");

        //Adding a send mediator
        esbCommon.addMediators("Add Child","0","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("0.0");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Adding the Drop mediator
        esbCommon.addMediators("Add Sibling","0.0","Core","Drop");
        esbCommon.sequenceSave();
        esbAddProxyServiceTest.clickNext();

        //*****setting up of the Out Sequence of the Proxy Service*****//
        esbAddProxyServiceTest.selectOutSequence("outSeqOpAnon", null);
        //Adding the send mediator
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        //****saving the proxy Service*****//
        esbAddProxyServiceTest.saveProxyService();
        Thread.sleep(5000);
    }

    public boolean checkWSDLofProxy(String proxyName,String searchFor) throws Exception{
        boolean searchStatus=false;
        //verify in WSDL1.1
		selenium.click("link=List");
		selenium.waitForPageToLoad("30000");
        int row_no=1;
        if(selenium.isTextPresent(proxyName)){
            while(!(selenium.getText("//table[@id='sgTable']/tbody/tr["+row_no+"]/td[3]").equals(proxyName))){
                row_no=row_no+1;
            }
            selenium.click("//table[@id='sgTable']/tbody/tr["+row_no+"]/td[5]/a");
            Thread.sleep(5000);
            //get the new window id
            String tryitwinid =selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
            Thread.sleep(1000);
            selenium.selectWindow(tryitwinid);
            //System.out.println(selenium.getLocation());
            String strURL = selenium.getLocation().toString();
            //close the window
            selenium.close();
            selenium.selectWindow("");

            URL proxyWsdl = new URL(strURL);
            URLConnection yc = proxyWsdl.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null){
                if(inputLine.contains(searchFor))
                    searchStatus=true;
            }
            in.close();
        }
        else
            System.out.println("proxy service not found..");

        return searchStatus;
    }
}
