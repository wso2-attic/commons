package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.client.ESBSampleClient;
import org.wso2.carbon.web.test.client.ThrottleClient;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

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

public class ESBThrottleMediatorMainTest  extends CommonSetup{
	Properties properties = new Properties();

    public ESBThrottleMediatorMainTest(String text) {
        super(text);
    }


    public void testAddThrottleMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_throttle_mediator");
        //Add a Throttle mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Advanced","Throttle");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Throttle"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        assertEquals("Throttle", selenium.getText("//a[@id='mediator-0']"));
        assertEquals("OnAccept", selenium.getText("//a[@id='mediator-0.0']"));
        assertEquals("OnReject", selenium.getText("//a[@id='mediator-0.1']"));


        //Click on the Throttle mediator
        assertEquals("Throttle Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table[1]/tbody/tr[1]/td/h2"));
        assertEquals("on", selenium.getValue("policygroupValueId"));
        assertEquals("on", selenium.getValue("onacceptgroup"));
        assertEquals("on", selenium.getValue("onrejectgroup"));

        //Click on 'Add Sibling' of the Throttle mediator
        esbCommon.verifyClickAddSibling();

        //Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Throttle");

        //Click on the 'Delete' icon of the 'Throttle mediator'
        esbCommon.delMediator("0");
        assertEquals("Root Add Child", selenium.getText("treePane"));
    }

    public void testCreateThrottlingProxyService() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        //Options which could be used are Specify in-line, Specify source URL, Pick from registry, None
        esbAddProxyServiceTest.addProxyName("throttling_proxy_service", "Custom Proxy");
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.setTransport("https");
        esbAddProxyServiceTest.clickNext();

        //****setting up of the In Sequence of the Proxy Service*****//
        ESBThrottleMediatorTest esbThrottleMediatorTest=new ESBThrottleMediatorTest(selenium);
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBPropertyMediatorTest esbPropertyMediatorTest=new ESBPropertyMediatorTest(selenium);
        ESBHeaderMediatorTest esbHeaderMediatorTest=new ESBHeaderMediatorTest(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);

        esbAddProxyServiceTest.selectInSequence("inSeqOpAnon", null);
        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding the Throttle mediator
        esbCommon.addMediators("Add Child","0","Advanced","Throttle");
        //Define throttle policy
        esbThrottleMediatorTest.addThrottleMediator("0.0", "A" );
        esbThrottleMediatorTest.addThrottlePolicy("0.0", "policygroupInlineId", null,null,null,"IP","Control","10","100000","10000");
        esbCommon.mediatorUpdate();

        //Throttle onAccept
        esbThrottleMediatorTest.onAcceptInfo("onacceptgroup",null);

        //Adding a  Log mediator
        esbCommon.addMediators("Add Child","0.0.0","Core","Log");
        esbLogMediatorTest.addLogMediator("0.0.0.0","Custom");
        esbLogMediatorTest.addLogPropety("text","Value","**Access Accept**");
        esbCommon.mediatorUpdate();

        //Adding a send mediator
        esbCommon.addMediators("Add Sibling","0.0.0.0","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("0.0.0.1");
        esbSendMediatorTest.addMandInfoSendMediator("http://localhost:8280/services/echo");
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Throttle onReject
        //esbThrottleMediatorTest.onRejectInfo("0.0","onrejectgroup",null);
        //Adding the Log mediator
        esbCommon.addMediators("Add Child","0.0.1","Core","Log");
        esbLogMediatorTest.setPropNo();
        esbLogMediatorTest.addLogMediator("0.0.1.0","Custom");
        esbLogMediatorTest.addLogPropety("text","Value","**Access Denied**");
        esbCommon.mediatorUpdate();

        //Adding the Fault mediator
        esbCommon.addMediators("Add Sibling","0.0.1.0","Transform","Fault");
        esbFaultMediatorTest.setSoap12Fault("0.0.1.1","Receiver");
        esbFaultMediatorTest.setFaultCodeString("**Access Denied**");
        esbCommon.mediatorUpdate();

        //Adding the property mediator
        esbCommon.addMediators("Add Sibling","0.0.1.1","Core","Property");
        esbPropertyMediatorTest.addBasicPropInfo("0.0.1.2","RESPONSE","true");
        esbCommon.mediatorUpdate();

        //Adding the Header mediator
        esbCommon.addMediators("Add Sibling","0.0.1.2","Transform","Header");
        esbHeaderMediatorTest.addHeaderMediator("0.0.1.3","To");
        esbHeaderMediatorTest.removeHeader();
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Sibling","0.0.1.3","Core","Send");
        esbCommon.mediatorUpdate();

        //Adding the Drop mediator
        esbCommon.addMediators("Add Sibling","0.0.1.4","Core","Drop");
        esbCommon.sequenceSave();
        esbAddProxyServiceTest.clickNext();

        //*****setting up of the Out Sequence of the Proxy Service*****//
        esbAddProxyServiceTest.selectOutSequence("outSeqOpAnon", null);
        //Adding the out mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");
        //esbCommon.mediatorUpdate();

        //Adding the trottle mediator
        esbCommon.addMediators("Add Child","0","Advanced","Throttle");
        esbThrottleMediatorTest.addThrottleMediator("0.0", "A");
        //esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Sibling","0.0","Core","Send");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        //****saving the proxy Service*****//
        esbAddProxyServiceTest.saveProxyService();
    }

//    public void testProxyInfoInEditMode() throws Exception{
//        //sequence with the Throttle Policy specified as an In-Lined Policy. Save the sequence and view through the edit mode
//        //sequence with the On Acceptance specified as an In-Lined Sequence. Save the sequence and view through the edit mode
//        //sequence with the On Rejection specified as an In-Lined Sequence. Save the sequence and view through the edit mode
//
//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.logoutLogin();
//
//        selenium.click("link=List");
//		selenium.waitForPageToLoad("30000");
//		if(selenium.isTextPresent("throttling_proxy_service")){
//            ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
//            esbAddProxyServiceTest.editProxyService("throttling_proxy_service");
//
//            esbCommon.clickSequenceSource(null);
//            Thread.sleep(2000);
//            assertTrue(selenium.isTextPresent("exact:<syn:inSequence xmlns:syn=\"http://ws.apache.org/ns/synapse\"> <syn:in> <syn:throttle id=\"A\"> <syn:policy> <wsp:Policy xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\" xmlns:throttle=\"http://www.wso2.org/products/wso2commons/throttle\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"WSO2MediatorThrottlingPolicy\"> <throttle:MediatorThrottleAssertion> <wsp:Policy> <throttle:ID throttle:type=\"IP\">other</throttle:ID> <wsp:Policy> <throttle:Control> <wsp:Policy> <throttle:MaximumCount>10</throttle:MaximumCount> <throttle:UnitTime>100000</throttle:UnitTime> <throttle:ProhibitTimePeriod wsp:Optional=\"true\">10000</throttle:ProhibitTimePeriod> </wsp:Policy> </throttle:Control> </wsp:Policy> </wsp:Policy> </throttle:MediatorThrottleAssertion> </wsp:Policy> </syn:policy> <syn:onReject> <syn:log level=\"custom\"> <syn:property name=\"text\" value=\"**Access Denied**\" /> </syn:log> <syn:makefault version=\"soap12\"> <syn:code xmlns:soap12Env=\"http://www.w3.org/2003/05/soap-envelope\" value=\"soap12Env:Receiver\" /> <syn:reason value=\"**Access Denied**\" /> <syn:node></syn:node> <syn:role></syn:role> </syn:makefault> <syn:property name=\"RESPONSE\" scope=\"default\" action=\"remove\" /> <syn:header name=\"To\" action=\"remove\" /> <syn:send /> <syn:drop /> </syn:onReject> <syn:onAccept> <syn:log level=\"custom\"> <syn:property name=\"text\" value=\"**Access Accept**\" /> </syn:log> <syn:send> <syn:endpoint> <syn:address uri=\"http://localhost:8280/services/echo\" /> </syn:endpoint> </syn:send> </syn:onAccept> </syn:throttle> </syn:in> </syn:inSequence>"));
//            esbCommon.clickDesignView();
//        }
//        else
//            System.out.println("Proxy \"throttling_proxy_service\" not found.");
//    }


    public void testAccessDenyThrottlingWithTypeIp() throws Exception{
    //    - Remove DOMAIN type row
    // - Select “Deny” for access level and save the configuration
    // - Invoke the echoInt operation

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.editProxyService("throttling_proxy_service");

        selenium.click("nextBtn");
        selenium.click("inAnonAddEdit");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Throttle");
        Thread.sleep(2000);
        selenium.click("link=Throttle Policy Editor");
        Thread.sleep(2000);
        if(selenium.isElementPresent("data21")){
            selenium.click("//table[@id='dataTable']/tbody/tr[2]/td[7]/a");
        }
        selenium.type("data11", "other");
        selenium.select("data16", "label=IP");
        selenium.select("data15", "label=Deny");
        selenium.click("//input[@value='Finish']");
        selenium.click("//input[@value='Update']");
        selenium.click("saveButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("//button[@type='button']");
        selenium.click("nextBtn");
        selenium.click("saveBtn");
        selenium.waitForPageToLoad("30000");

        boolean throttling_status=false;
        if (esbCommon.getContextRoot()==null){
                throttling_status=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
        }
        else{
                throttling_status=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
        }
        if(throttling_status){
            //Throttling should fail
            //'Access denied' error should be returned
            throw new  MyCheckedException("Throttling should fail..Access denied' error should be returned..!");
        }
    }

    public void testAccessDenyThrottlingWithTypeDomain() throws Exception{
//         - Remove DOMAIN type row
//         - Select “Deny” for access level and save the configuration
//         - Change the type to “DOMAIN”
//         - Invoke the echoInt operation

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
        ESBThrottleMediatorTest esbThrottleMediatorTest=new ESBThrottleMediatorTest(selenium);
        esbAddProxyServiceTest.editProxyService("throttling_proxy_service");

        selenium.click("nextBtn");
        selenium.click("inAnonAddEdit");
        selenium.waitForPageToLoad("30000");
        esbThrottleMediatorTest.addThrottlePolicy("0.0", "policygroupInlineId", null,null,"other","DOMAIN","Deny",null,null,null);
        selenium.click("//input[@value='Update']");
        selenium.click("saveButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("//button[@type='button']");
        selenium.click("nextBtn");
        selenium.click("saveBtn");
        selenium.waitForPageToLoad("30000");

        boolean throttling_status=false;
        if (esbCommon.getContextRoot()==null){
                throttling_status=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
        }
        else{
                throttling_status=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
        }
        if(throttling_status){
            //Throttling should fail
            //'Access denied' error should be returned
            throw new  MyCheckedException("Throttling should fail..Access denied' error should be returned..!");
        }
    }

    public void testAccessControlInputs() throws Exception{
//     - Remove DOMAIN type row
//     - Select “Control” for access level and save the configuration without specifying any inputs
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_throttling_policy");
        //Add a Throttle mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Advanced","Throttle");

        //Define throttle policy
        ESBThrottleMediatorTest esbThrottleMediatorTest=new ESBThrottleMediatorTest(selenium);
        esbThrottleMediatorTest.addThrottleMediator("0", "A" );
        esbThrottleMediatorTest.addThrottlePolicy("0", "policygroupInlineId", null,null,null,"IP","Control",null,null,null);
        assertEquals("When the Access Level is Control, Max Request Count and Unit time MUST be specified", selenium.getText("messagebox-info"));
		selenium.click("//button[@type='button']");

        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();

    }

     public void testAccessControlThrottlingWithTypeIp() throws Exception{
    // - Remove DOMAIN type row
    // - Select “Control” for access level and specify the following inputs
    //   Max Request Count = 10, UnitTime = 100000ms, Prohibit Time period = 10000ms
    // - Change the type to “IP”
    // - Invoke the echoInt operation

    //     Max no. of allowed requests will be 10 within 100 seconds. After 100 seconds, another set of requests will be allowed.
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
        ESBThrottleMediatorTest esbThrottleMediatorTest=new ESBThrottleMediatorTest(selenium);
        esbAddProxyServiceTest.editProxyService("throttling_proxy_service");


        selenium.click("nextBtn");
        selenium.click("inAnonAddEdit");
        selenium.waitForPageToLoad("30000");
        esbThrottleMediatorTest.addThrottlePolicy("0.0", "policygroupInlineId", null,null,"other","IP","Control","10","100000","10000");
        selenium.click("//input[@value='Update']");
        selenium.click("saveButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("//button[@type='button']");
        selenium.click("nextBtn");
        selenium.click("saveBtn");
        selenium.waitForPageToLoad("30000");

        boolean throttling_status1=false;
        boolean throttling_status2=false;
        if (esbCommon.getContextRoot()==null){
            checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
        }
        else{
            checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
        }

        Thread.sleep(5000);
        System.out.println("\n\n****System waits for 5S..Trottling prohibit time has set to 10S..So throttling should fail...!****\n");
        if (esbCommon.getContextRoot()==null){
            throttling_status1=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
        }
        else{
            throttling_status1=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
        }
        if(throttling_status1)
            throw new MyCheckedException("Client requests are not within the throttling valid period..Throttling should fail..Error in throttling...!! ");


         Thread.sleep(100000);
         System.out.println("\n\n****System waits for 100S..Trottling has set to 100S..So throttling should pass...!****\n\n");
         if (esbCommon.getContextRoot()==null){
             throttling_status2=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
         }
         else{
             throttling_status2=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
         }
         if(!throttling_status2)
            throw new MyCheckedException("Client requests are within the throttling valid period..Throttling should done..Error in throttling...!! ");
         esbCommon.closeFiles();
    }

    public void testSpecifyIPrange() throws Exception{
//         - Remove DOMAIN type row
//         - Specify a valid IP range
//         - Select “Control” for access level and specify the following inputs
//           Max Request Count = 10, UnitTime = 100000ms, Prohibit Time period = 10000ms
//         - Change the type to “IP”
//         - Invoke the  echoInt operation using an IP given in the above rage

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();
        String ip=selenium.getText("//table[@id='systemInfoTable']/tbody/tr[1]/td[2]");
        int p=0,q=0;
        String[] arr=null;
        arr=ip.split("\\.");
        int x=Integer.parseInt(arr[3]);
        if(x>0 && x!=255) {
            p=x-1;
            q=x+1;
        }
        String ip_range=arr[0]+"."+arr[1]+"."+arr[2]+"."+p+"-"+arr[0]+"."+arr[1]+"."+arr[2]+"."+q;

        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
        ESBThrottleMediatorTest esbThrottleMediatorTest=new ESBThrottleMediatorTest(selenium);
        esbAddProxyServiceTest.editProxyService("throttling_proxy_service");

        selenium.click("nextBtn");
		selenium.click("inAnonAddEdit");
		selenium.waitForPageToLoad("30000");
        esbThrottleMediatorTest.addThrottlePolicy("0.0", "policygroupInlineId", null,null,ip_range,"IP","Control","10","100000","10000");
		selenium.click("//input[@value='Update']");
		selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("//button[@type='button']");
        selenium.click("nextBtn");
		selenium.click("saveBtn");
		selenium.waitForPageToLoad("30000");

        boolean throttling_status1=false;
        if (esbCommon.getContextRoot()==null){
            throttling_status1=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/throttling_proxy_service","http://"+ip+":8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
        }
        else{
            throttling_status1=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://"+ip+":8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
        }
        if(!throttling_status1)
            throw new MyCheckedException("Client requests are not within the  valid Ip range..Throttling should done..Error in throttling...!! ");
    }

    public void testAccessControlThrottlingWithTypeDomain() throws Exception{
    //- Add a throttle configuration entry as follows.
    //  Range = specify a valid domain, Type = DOMAIN, Max Request Count = 5, Unit Time = 100000, Prohibit Time period = 10000, Access = Control
    //- Invoke the echoInt operation using a client located in the above domain

    //Max no. of allowed requests will be 5 within 100 seconds. After 100 seconds, another set of requests will be allowed.
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
        ESBThrottleMediatorTest esbThrottleMediatorTest=new ESBThrottleMediatorTest(selenium);
        esbAddProxyServiceTest.editProxyService("throttling_proxy_service");

        selenium.click("nextBtn");
		selenium.click("inAnonAddEdit");
		selenium.waitForPageToLoad("30000");
        esbThrottleMediatorTest.addThrottlePolicy("0.0", "policygroupInlineId", null,null,"other","DOMAIN","Control","5","100000","10000");
		selenium.click("//input[@value='Update']");
		selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("//button[@type='button']");
		selenium.click("nextBtn");
		selenium.click("saveBtn");
		selenium.waitForPageToLoad("30000");

        boolean throttling_status1=false;
        boolean throttling_status2=false;
        if (esbCommon.getContextRoot()==null){
            checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",5);
        }
        else{
            checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",5);
        }


        Thread.sleep(5000);
        System.out.println("\n\n****System waits for 5S..Trottling prohibit time has set to 10S..So throttling should fail...!****\n");
        if (esbCommon.getContextRoot()==null){
            throttling_status1=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",5);
        }
        else{
            throttling_status1=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",5);
        }
        if(throttling_status1)
            throw new MyCheckedException("Client requests are not within the throttling valid period..Throttling should fail..Error in throttling...!! ");


         Thread.sleep(10000);
         System.out.println("\n\n****System waits for 100S..Trottling has set to 100S..So throttling should pass...!****\n\n");
         if (esbCommon.getContextRoot()==null){
             throttling_status2=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",5);
         }
         else{
             throttling_status2=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",5);
         }
         if(!throttling_status2)
            throw new MyCheckedException("Client requests are within the throttling valid period..Throttling should done..Error in throttling...!! ");
        esbCommon.closeFiles();
    }


        public void testAddTwoThrottleEntries() throws Exception{
        // - Add two throttle config entries as follows.
        // 1. Range = range1 (specify a valid IP range), Type = IP, Max Request Count = 5, Unit Time = 100000, Prohibit Time period = 10000, Access = Control
        // 2. Range = range2 (specify a valid IP range), Type = IP, Max Request Count = 10, Unit Time = 100000, Prohibit Time period = 10000, Access = Control
        //- Invoke the echoInt operation using an IP which is in the range1
            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.logoutLogin();

            String ip=selenium.getText("//table[@id='systemInfoTable']/tbody/tr[1]/td[2]");
            int p=0;
            String[] arr=null;
            arr=ip.split("\\.");
            int x=Integer.parseInt(arr[3]);
            if(x>0) {
                p=x-1;
            }
            String ip_range=arr[0]+"."+arr[1]+"."+arr[2]+"."+p+"-"+arr[0]+"."+arr[1]+"."+arr[2]+"."+"254";


            ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
            ESBThrottleMediatorTest esbThrottleMediatorTest=new ESBThrottleMediatorTest(selenium);
            esbAddProxyServiceTest.editProxyService("throttling_proxy_service");

            selenium.click("nextBtn");
            selenium.click("inAnonAddEdit");
            selenium.waitForPageToLoad("30000");
            esbThrottleMediatorTest.addThrottlePolicy("0.0", "policygroupInlineId", null,null,ip_range,"IP","Control","5","100000","10000");
            esbCommon.mediatorUpdate();
            selenium.click("link=Throttle");
            Thread.sleep(2000);
            selenium.click("link=Throttle Policy Editor");
            selenium.click("link=Add New Entry");
            selenium.type("data21", "10.100.1.200-10.100.220");
            selenium.select("data25", "label=Control");
            selenium.type("data22", "10");
            selenium.type("data23", "100000");
            selenium.type("data24", "10000");
            selenium.click("//input[@value='Finish']");
            selenium.click("//input[@value='Update']");
            selenium.click("saveButton");
            selenium.waitForPageToLoad("30000");
            selenium.click("//button[@type='button']");
            selenium.click("nextBtn");
            selenium.click("saveBtn");
            selenium.waitForPageToLoad("30000");

            boolean throttling_status1=false;
            boolean throttling_status2=false;
            System.out.println("Throttling should done..Request in the specified IP range..");
            if (esbCommon.getContextRoot()==null){
                checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/throttling_proxy_service","http://"+ip+":8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",5);
            }
            else{
                checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://"+ip+":8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",5);
            }


             Thread.sleep(10000);
             System.out.println("\n\nThrottling should failed..Request not in the specified IP range..");
             if (esbCommon.getContextRoot()==null){
                 throttling_status2=checkThrottleClient("http://"+ip+":8280"+"/services/throttling_proxy_service","http://"+ip+":8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",5);
             }
             else{
                 throttling_status2=checkThrottleClient("http://"+ip+":8280"+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://"+ip+":8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",5);
             }
             if(!throttling_status2)
                throw new MyCheckedException("Client requests are within the throttling valid period..Throttling should done..Error in throttling...!! ");
            esbCommon.closeFiles();
        }

    //Create a complete sequence with On Acceptance sequence set as an referred sequence
    public void testOnAcceptReferingSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //create the on acceptance refering seqquence
        createOnAcceptReferingSequence();

        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
        ESBThrottleMediatorTest esbThrottleMediatorTest=new ESBThrottleMediatorTest(selenium);
        esbAddProxyServiceTest.editProxyService("throttling_proxy_service");

        selenium.click("nextBtn");
        selenium.click("inAnonAddEdit");
        selenium.waitForPageToLoad("30000");
        esbThrottleMediatorTest.addThrottlePolicy("0.0", "policygroupInlineId", null,null,"other","IP","Control","10","100000","10000");
        selenium.click("//input[@value='Update']");
        selenium.click("link=Throttle");
        Thread.sleep(2000);
        esbThrottleMediatorTest.onAcceptInfo("onAcceptSequenceKey","onAccept_sequence");
        Thread.sleep(2000);
        selenium.click("//input[@value='Update']");
		selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("nextBtn");
		selenium.click("saveBtn");
		selenium.waitForPageToLoad("30000");

        //sequence with the On Acceptance specified as an Referring Sequence. Save the sequence and view through the edit mode
        esbAddProxyServiceTest.editProxyService("throttling_proxy_service");
        Thread.sleep(2000);
        esbCommon.clickSequenceSource(null);
        assertTrue(selenium.isTextPresent("exact:<syn:proxy xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"throttling_proxy_service\" transports=\"https,http\" statistics=\"disable\" trace=\"disable\" startOnLoad=\"true\"> <syn:target> <syn:inSequence> <syn:in> <syn:throttle id=\"A\" onAccept=\"onAccept_sequence\"> <syn:policy> <wsp:Policy xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\" xmlns:throttle=\"http://www.wso2.org/products/wso2commons/throttle\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"WSO2MediatorThrottlingPolicy\"> <throttle:MediatorThrottleAssertion> <wsp:Policy> <throttle:ID throttle:type=\"IP\">other</throttle:ID> <wsp:Policy> <throttle:Control> <wsp:Policy> <throttle:MaximumCount>10</throttle:MaximumCount> <throttle:UnitTime>100000</throttle:UnitTime> <throttle:ProhibitTimePeriod wsp:Optional=\"true\">10000</throttle:ProhibitTimePeriod> </wsp:Policy> </throttle:Control> </wsp:Policy> </wsp:Policy> </throttle:MediatorThrottleAssertion> </wsp:Policy> </syn:policy> <syn:onReject> <syn:log level=\"custom\"> <syn:property name=\"text\" value=\"**Access Denied**\" /> </syn:log> <syn:makefault version=\"soap12\"> <syn:code xmlns:soap12Env=\"http://www.w3.org/2003/05/soap-envelope\" value=\"soap12Env:Receiver\" /> <syn:reason value=\"**Access Denied**\" /> <syn:node /> <syn:role /> </syn:makefault> <syn:property name=\"RESPONSE\" scope=\"default\" action=\"remove\" /> <syn:header name=\"To\" action=\"remove\" /> <syn:send /> <syn:drop /> </syn:onReject> </syn:throttle> </syn:in> </syn:inSequence> <syn:outSequence> <syn:out> <syn:throttle id=\"A\"> <syn:onReject /> <syn:onAccept /> </syn:throttle> <syn:send /> </syn:out> </syn:outSequence> </syn:target> </syn:proxy>"));
        esbCommon.clickDesignView();

        if (esbCommon.getContextRoot()==null){
            checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
        }
        else{
            checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
        }
    }

////    public void testOnRejectReferingSequence() throws Exception{
////        ESBCommon esbCommon = new ESBCommon(selenium);
////        esbCommon.logoutLogin();
////
////        //create the on reject refering seqquence
////        createOnRejectReferingSequence();
////
////        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
////        ESBThrottleMediatorTest esbThrottleMediatorTest=new ESBThrottleMediatorTest(selenium);
////        esbAddProxyServiceTest.editProxyService("throttling_proxy_service");
////
////        selenium.click("nextBtn");
////        selenium.click("inAnonAddEdit");
////        selenium.waitForPageToLoad("30000");
////        selenium.click("link=Throttle");
////        Thread.sleep(2000);
////        esbThrottleMediatorTest.onRejectInfo("0.0","onRejectSequenceKey","onReject_sequence");
////        Thread.sleep(2000);
////        selenium.click("//input[@value='Update']");
////		selenium.click("saveButton");
////		selenium.waitForPageToLoad("30000");
////		selenium.click("nextBtn");
////		selenium.click("saveBtn");
////		selenium.waitForPageToLoad("30000");
////
////        if (esbCommon.getContextRoot()==null){
////            checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
////        }
////        else{
////            checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",10);
////        }
////    }
//
    public void testRefferingThrottlingPolicy() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //create throttling policy
        createThrottlingPolicy();

        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
        ESBThrottleMediatorTest esbThrottleMediatorTest=new ESBThrottleMediatorTest(selenium);
        esbAddProxyServiceTest.editProxyService("throttling_proxy_service");

        selenium.click("nextBtn");
        selenium.click("inAnonAddEdit");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Throttle");
        Thread.sleep(2000);
        esbThrottleMediatorTest.addThrottlePolicy("0.0", "policygroupValueId", "throttle_policy.xml",null,null,null,null,null,null,null);
        Thread.sleep(2000);
        selenium.click("//input[@value='Update']");
		selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("nextBtn");
		selenium.click("saveBtn");
		selenium.waitForPageToLoad("30000");

        boolean throttling_status=false;
        if (esbCommon.getContextRoot()==null){
            throttling_status=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",4);
        }
        else{
            throttling_status=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",4);
        }
        if(!throttling_status)
            throw new MyCheckedException("Refering throttling policy does not working...!");

    }

    //Create a complete sequence with a different Throttle ID for the incoming and outgoing messages
    public void testDifferentThrottleId() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBAddProxyServiceTest esbAddProxyServiceTest=new ESBAddProxyServiceTest(selenium);
        ESBThrottleMediatorTest esbThrottleMediatorTest=new ESBThrottleMediatorTest(selenium);
        esbAddProxyServiceTest.editProxyService("throttling_proxy_service");

        selenium.click("nextBtn");
		selenium.click("nextBtn");
		selenium.click("outAnonAddEdit");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Throttle");
		selenium.type("throttle_id1", "B");
		selenium.click("//input[@value='Update']");
		selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("saveBtn");
		selenium.waitForPageToLoad("30000");

        boolean throttling_status=false;
        if (esbCommon.getContextRoot()==null){
            throttling_status=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",4);
        }
        else{
            throttling_status=checkThrottleClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/throttling_proxy_service","http://localhost:8280/services/echo", "echoInt", "urn:echoInt", "http://echo.services.core.carbon.wso2.org", "in",4);
        }
        if(throttling_status)
            throw new MyCheckedException("Sequence uses  different Throttle ID for the incoming and outgoing messages..The invocation can NOT be successful..!! ");
    }

    public void createOnAcceptReferingSequence() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);

        esbCommon.viewSequences();
        if(!selenium.isTextPresent("onAccept_sequence")){
            esbCommon.addSequence("onAccept_sequence");
            //Adding a  Log mediator
            esbCommon.addRootLevelChildren("Add Child","Core","Log");
            esbLogMediatorTest.addLogMediator("0","Custom");
            esbLogMediatorTest.addLogPropety("text","Value","**Access Accept**");
            esbCommon.mediatorUpdate();

            //Adding a send mediator
            esbCommon.addMediators("Add Sibling","0","Core","Send");
            esbSendMediatorTest.addAnonSendMediator("1");
            esbSendMediatorTest.addMandInfoSendMediator("http://localhost:8280/services/echo");
            esbSendMediatorTest.saveEndpoint();
            esbCommon.mediatorUpdate();

            esbCommon.sequenceSave();
        }
    }

    public void createOnRejectReferingSequence() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBPropertyMediatorTest esbPropertyMediatorTest=new ESBPropertyMediatorTest(selenium);
        ESBHeaderMediatorTest esbHeaderMediatorTest=new ESBHeaderMediatorTest(selenium);

        esbCommon.viewSequences();
        if(!selenium.isTextPresent("onReject_sequence")){
            esbCommon.addSequence("onReject_sequence");
            //Adding the Log mediator
            esbCommon.addRootLevelChildren("Add Child","Core","Log");
            esbLogMediatorTest.setPropNo();
            esbLogMediatorTest.addLogMediator("0","Custom");
            esbLogMediatorTest.addLogPropety("text","Value","**Access Accept**");
            esbCommon.mediatorUpdate();

            //Adding the Fault mediator
            esbCommon.addMediators("Add Sibling","0","Transform","Fault");
            esbFaultMediatorTest.setSoap12Fault("1","Receiver");
            esbFaultMediatorTest.setFaultCodeString("**Access Denied**");
            esbCommon.mediatorUpdate();

            //Adding the property mediator
            esbCommon.addMediators("Add Sibling","1","Core","Property");
            esbPropertyMediatorTest.addBasicPropInfo("2","RESPONSE","true");
            esbCommon.mediatorUpdate();

            //Adding the Header mediator
            esbCommon.addMediators("Add Sibling","2","Transform","Header");
            esbHeaderMediatorTest.addHeaderMediator("3","To");
            esbHeaderMediatorTest.removeHeader();
            esbCommon.mediatorUpdate();

            //Adding the Send mediator
            esbCommon.addMediators("Add Sibling","3","Core","Send");
            esbCommon.mediatorUpdate();

            //Adding the Drop mediator
            esbCommon.addMediators("Add Sibling","4","Core","Drop");
            esbCommon.sequenceSave();
            }
    }

    public void createThrottlingPolicy() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        //Adding a local entry
       selenium.click("link=Local Entries");
		Thread.sleep(2000);

        if (selenium.isTextPresent("throttle_policy.xml")){
         //do nothing
        } else {
            ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
            esbManageLocalEntriesTest.addSourceUrlEntry("throttle_policy.xml","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/policy/throttle_policy.xml");
        }
    }

    public boolean checkThrottleClient(String trpUrl,String ServiceEpr, String operationName, String SoapAction, String namespace, String firstChild,int count) throws Exception{
        ThrottleClient tClient=new ThrottleClient(selenium);
        ESBCommon esbCommon=new ESBCommon(selenium);
        boolean throttling_status=false;

        int iThrottle = tClient.throttleClient(trpUrl,ServiceEpr, operationName, SoapAction, namespace, firstChild);
        System.out.println("iThrottle "+iThrottle);
        Thread.sleep(1000);
        if (iThrottle == count) {
            System.out.println("Throttling done.");
            throttling_status=true;
        } else {
            System.out.println("Throtlling failed.        Response count - " + iThrottle);
            throttling_status=false;
        }

        return throttling_status;
    }

    public String getLocalIP() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        String ip=selenium.getText("//table[@id='systemInfoTable']/tbody/tr[1]/td[2]");
        return ip;
    }

//    public String getLocalIp() throws Exception{
//        InetAddress localMachine = InetAddress.getLocalHost();
//        System.out.println("Hostname of local machine: " + localMachine.getHostName());
//
//        InetAddress inet = InetAddress.getByName(localMachine.getHostName()+".local");
//        String inet_ip=inet.getHostAddress();
//        System.out.println ("IP  : " + inet_ip);
//
//        return inet_ip;
//    }
}