package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.wso2.carbon.web.test.client.ESBSecurityClient;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.apache.commons.io.FileUtils;
import org.apache.axiom.om.OMElement;

import java.io.*;
import java.util.Properties;
import java.util.StringTokenizer;/*
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

public class ESBSample200Test extends CommonSetup{
    Properties properties = new Properties();

    public ESBSample200Test(String text) {
        super(text);
    }

    /*
    This method will create the Local Entry
     */
    public void createLocalEntry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);        
        //Creating a local entry to store the WSDL file
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("sec_policy","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/policy/policy_3.xml");
    }

    /*
   This method will test adding of a Proxy Service with inline WSDL but with anonymous sequences
    */
    public void createProxyService() throws Exception {
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
        ESBHeaderMediatorTest esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Options which could be used are Specify in-line, Specify source URL, Pick from registry, None
        esbAddProxyServiceTest.addProxyName("sample_200_proxy", "Custom Proxy");
        esbAddProxyServiceTest.addSourceUrlWsdl("file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/proxy/sample_proxy_1.wsdl");
        esbAddProxyServiceTest.specifyStartOnload("true");
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.setTransport("https");
        esbAddProxyServiceTest.clickNext();

        //Available options are inSeqOpAnon, inSeqOpReg, inSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectInSequence("inSeqOpAnon",null);
        esbCommon.addRootLevelChildren("Add Child","Transform","Header");
        esbHeaderMediatorTest.addHeaderMediator("0","Security");
        esbHeaderMediatorTest.addHeaderNameNamespace("wsse","http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        esbHeaderMediatorTest.removeHeader();
        esbCommon.mediatorUpdate();

        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("1");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();
        Thread.sleep(2000);

        //Available options are epOpImp, epOpAnon, epOpReg, epOpNone
        esbAddProxyServiceTest.selectEndpoint("epOpNone", null);
        esbAddProxyServiceTest.clickNext();

        //Available options are outSeqOpAnon, outSeqOpReg, outSeqOpImp, inSeqOpNone
        esbAddProxyServiceTest.selectOutSequence("outSeqOpAnon", null);
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbSendMediatorTest.addNormalSendMediator("0");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        //Available options are faultSeqOpAnon, faultSeqOpReg, faultSeqOpImp, faultSeqOpNone
        esbAddProxyServiceTest.selectFaultSequence("faultSeqOpNone", null, null, null, null);
        esbAddProxyServiceTest.saveProxyService();
        esbAddProxyServiceTest.verifyProxy("sample_200_proxy");
    }



    /*
    This method will enabled security and apply the proper security to the Proxy Service
     */
    public void applySecurity() throws Exception{
        String content = null;
        try {
            File file = new File(".." + File.separator + "esb" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "policy_3.xml");
            content = FileUtils.readFileToString(file);
        } catch (
            IOException e) {
        }
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.applySecurity("sample_200_proxy",content,"scenario1");
    }

    /*
    https://support.wso2.com/jira/browse/INTMNTHCDEVSPRT-15
    Policy not saved through in-line editor in ESB
    */
    public void checkProxyPolicy(String proxyName) throws Exception{
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");

        int row_no=1;
        while(!(selenium.getText("//table[@id='sgTable']/tbody/tr["+row_no+"]/td[3]").equals(proxyName))){
            row_no=row_no+1;
        }
        selenium.click("//table[@id='sgTable']/tbody/tr["+row_no+"]/td[3]/a");
        Thread.sleep(5000);
        selenium.click("link=Policies");
        selenium.waitForPageToLoad("30000");
        selenium.click("//table[@id='binding-hierarchy-table']/tbody/tr[1]/td[2]/input");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.getValue("raw-policy")  !="<wsp:Policy xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\" />"  && selenium.getValue("raw-policy")!=null );
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        selenium.click("//table[@id='binding-hierarchy-table']/tbody/tr[4]/td[2]/input");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.getValue("raw-policy")  !="<wsp:Policy xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\" />"  && selenium.getValue("raw-policy")!=null );
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
    }

    /*
    Executing client for proxy_inline_wsdl_anon_seq Proxy Service
     */
    public void invokeClient() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSecurityClient esbSecurityClient = new ESBSecurityClient();



        boolean result=esbSecurityClient.runSecurityClient("sample_200_proxy","http://services.samples","getQuote","getQuote","request","symbol");
        if(result)
                System.out.println("Response Received!!");
        else
               System.out.println("Client Failed!!");

//        OMElement result = esbSecurityClient.runSecurityClient("scenario5", "sample_200_proxy","http://services.samples","getQuote","getQuote","request","symbol");
        //OMElement result = esbSecurityClient.runSecurityClient("client_policy_3.xml", "sample_200_proxy","http://services.samples","getQuote","getQuote","request","symbol");
//        System.out.println(result);
//        assertEquals("IBM", result.getFirstElement().getText());
//        Thread.sleep(1000);
    }

    /*
    This method will create the seqence and invoke the client
     */
    public void testSample200Config() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
         seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");
//        createProxyService();
//        applySecurity();
        checkProxyPolicy("sample_200_proxy");
//        seleniumTestBase.logOutUI();
//        invokeClient();
    }
}
