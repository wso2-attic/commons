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
import org.wso2.carbon.web.test.client.ESBLoadbalanceFailoverClient;


public class ESBEndPointsInRegistryTest extends CommonSetup{
    public ESBEndPointsInRegistryTest(String text) {
        super(text);
    }

    public void createAddressEndPoint(String path,String epName) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);

        boolean addEprName = esbCommon.searchResources(path,epName);
        if (addEprName){
            System.out.println("Endpoint "+epName+" already available in registry path..!!");
        }
        else{
            //Adding an Address endpoint
            esbCommon.viewEndpoints();
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.addAnonAddressEndpoint();
            esbAddAddressEndpointTest.addAddressEprMandatoryInfo(epName,esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
            esbAddAddressEndpointTest.addAddressEprFormatOptimizeInfo("SOAP 1.1","SwA");
            esbAddAddressEndpointTest.addAddressEprSuspendInfo("101503","1000","10000","2");
            esbAddAddressEndpointTest.addAddressEprRetryInfo("101504","1000","2000");
            esbAddAddressEndpointTest.addAddressEprTimeoutInfo("Discard message","10");
            esbAddAddressEndpointTest.saveAsAddressEndpoint(path+"/"+epName);
        }
    }

    public void createWSDLEndpoint(String path,String epName) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);

        boolean addEprName = esbCommon.searchResources(path,epName);

        if (addEprName){
            System.out.println("Endpoint "+epName+" already available in registry path..!!");
        }
        else {
            //Adding a Wsdl endpoint
            esbCommon.viewEndpoints();
            ESBAddWsdlEndpointTest esbAddWsdlEndpointTest = new ESBAddWsdlEndpointTest(selenium);
            esbAddWsdlEndpointTest.addAnonWsdlEndpoint();
            esbAddWsdlEndpointTest.addUriWsdlEprMandInfo(epName,esbCommon.getServiceAddUrl("SimpleStockQuoteService")+"?wsdl","SimpleStockQuoteService","SimpleStockQuoteServiceHttpSoap11Endpoint");
            esbCommon.saveAsAddressEndpoint(path+"/"+epName);
        }
    }

    public void createFailoverGroup(String path,String epName) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        boolean addEprName = esbCommon.searchResources(path,epName);

        if (addEprName){
            System.out.println("Endpoint "+epName+" already available in registry path..!!");
        }
        else {
            //Adding a FailoverGroup
            esbCommon.viewEndpoints();
            ESBLoadbalanceFailoverTest esbLoadbalanceFailoverTest=new ESBLoadbalanceFailoverTest(selenium);
            esbLoadbalanceFailoverTest.addFailoverGroup();

            //Adding the first address endpoint to FailoverGroup
            esbLoadbalanceFailoverTest.addFailoverGroupMandatoryInfo(epName);
            esbLoadbalanceFailoverTest.addEndpointToLoadBalanceFailoverGroup("Address");
            esbLoadbalanceFailoverTest.clickAddressEndpoint("0.1");
            esbLoadbalanceFailoverTest.addAddressEprMandatoryInfo("0.1","http://10.100.1.153:9001/services/LBService1");
            esbLoadbalanceFailoverTest.addAddressEprFormatOptimizeInfo("0.1","Leave As-Is", "Leave As-Is");
            esbLoadbalanceFailoverTest.addAddressEprSuspendInfo("0.1","", "60000", "", "1.0");
            esbLoadbalanceFailoverTest.addAddressEprRetryInfo("0.1","","0", "0");
            esbLoadbalanceFailoverTest.addAddressEprTimeoutInfo("0.1","Never timeout", "");
            esbLoadbalanceFailoverTest.addAddressEprQosInfo("0.1","true", null, null, null, null, null);
            esbLoadbalanceFailoverTest.saveAddressEp("0.1");

            //Adding the second address endpoint to FailoverGroup
            esbLoadbalanceFailoverTest.addEndpointToLoadBalanceFailoverGroup("Address");
            esbLoadbalanceFailoverTest.clickAddressEndpoint("0.2");
            esbLoadbalanceFailoverTest.addAddressEprMandatoryInfo("0.2","http://10.100.1.153:9002/services/LBService1");
            esbLoadbalanceFailoverTest.addAddressEprFormatOptimizeInfo("0.2","Leave As-Is", "Leave As-Is");
            esbLoadbalanceFailoverTest.addAddressEprSuspendInfo("0.2","", "60000", "", "1.0");
            esbLoadbalanceFailoverTest.addAddressEprRetryInfo("0.2","","0", "0");
            esbLoadbalanceFailoverTest.addAddressEprTimeoutInfo("0.2","Never timeout", "");
            esbLoadbalanceFailoverTest.addAddressEprQosInfo("0.2","true", null, null, null, null, null);
            esbLoadbalanceFailoverTest.saveAddressEp("0.2");

            //Adding the third address endpoint to FailoverGroup
            esbLoadbalanceFailoverTest.addEndpointToLoadBalanceFailoverGroup("Address");
            esbLoadbalanceFailoverTest.clickAddressEndpoint("0.3");
            esbLoadbalanceFailoverTest.addAddressEprMandatoryInfo("0.3","http://10.100.1.153:9003/services/LBService1");
            esbLoadbalanceFailoverTest.addAddressEprFormatOptimizeInfo("0.2","Leave As-Is", "Leave As-Is");
            esbLoadbalanceFailoverTest.addAddressEprSuspendInfo("0.3","", "60000", "", "1.0");
            esbLoadbalanceFailoverTest.addAddressEprRetryInfo("0.3","","0", "0");
            esbLoadbalanceFailoverTest.addAddressEprTimeoutInfo("0.3","Never timeout", "");
            esbLoadbalanceFailoverTest.addAddressEprQosInfo("0.3","true", null, null, null, null, null);
            esbLoadbalanceFailoverTest.saveAddressEp("0.3");

            //save the FailoverGroup to registry
            esbCommon.saveAsAddressEndpoint(path+"/"+epName);
        }
    }

    public void createLoadBalanceGroup(String path,String epName) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        boolean addEprName = esbCommon.searchResources(path,epName);

        if (addEprName){
            System.out.println("Endpoint "+epName+" already available in registry path..!!");
        }
        else {
            //Adding a Loaad-balanceGroup
            esbCommon.viewEndpoints();
            ESBLoadbalanceFailoverTest esbLoadbalanceFailoverTest=new ESBLoadbalanceFailoverTest(selenium);
            esbLoadbalanceFailoverTest.addLoadBalance();
            esbLoadbalanceFailoverTest.addLoadBalanceMandatoryInfo("loadBlanceEp","Client ID","-1");

            //Adding the first address endpoint to Load-balanceGroup
            esbLoadbalanceFailoverTest.addEndpointToLoadBalanceFailoverGroup("Address");
            esbLoadbalanceFailoverTest.clickAddressEndpoint("0.1");
            esbLoadbalanceFailoverTest.addAddressEprMandatoryInfo("0.1","http://10.100.1.153:9001/services/LBService1");
            esbLoadbalanceFailoverTest.addAddressEprFormatOptimizeInfo("0.1","Leave As-Is", "Leave As-Is");
            esbLoadbalanceFailoverTest.addAddressEprSuspendInfo("0.1","", "", "", "1.0");
            esbLoadbalanceFailoverTest.addAddressEprRetryInfo("0.1","","0", "0");
            esbLoadbalanceFailoverTest.addAddressEprTimeoutInfo("0.1","Never timeout", "");
            esbLoadbalanceFailoverTest.addAddressEprQosInfo("0.1","true", null, null, null, null, null);
            esbLoadbalanceFailoverTest.saveAddressEp("0.1");

            //Adding the second address endpoint to Load-balanceGroup
            esbLoadbalanceFailoverTest.addEndpointToLoadBalanceFailoverGroup("Address");
            esbLoadbalanceFailoverTest.clickAddressEndpoint("0.2");
            esbLoadbalanceFailoverTest.addAddressEprMandatoryInfo("0.2","http://10.100.1.153:9002/services/LBService1");
            esbLoadbalanceFailoverTest.addAddressEprFormatOptimizeInfo("0.2","Leave As-Is", "Leave As-Is");
            esbLoadbalanceFailoverTest.addAddressEprSuspendInfo("0.2","", "", "", "1.0");
            esbLoadbalanceFailoverTest.addAddressEprRetryInfo("0.2","","0", "0");
            esbLoadbalanceFailoverTest.addAddressEprTimeoutInfo("0.2","Never timeout", "");
            esbLoadbalanceFailoverTest.addAddressEprQosInfo("0.2","true", null, null, null, null, null);
            esbLoadbalanceFailoverTest.saveAddressEp("0.2");

            //Adding the third address endpoint to Load-balanceGroup
            esbLoadbalanceFailoverTest.addEndpointToLoadBalanceFailoverGroup("Address");
            esbLoadbalanceFailoverTest.clickAddressEndpoint("0.3");
            esbLoadbalanceFailoverTest.addAddressEprMandatoryInfo("0.3","http://10.100.1.153:9003/services/LBService1");
            esbLoadbalanceFailoverTest.addAddressEprFormatOptimizeInfo("0.3","Leave As-Is", "Leave As-Is");
            esbLoadbalanceFailoverTest.addAddressEprSuspendInfo("0.3","", "", "", "1.0");
            esbLoadbalanceFailoverTest.addAddressEprRetryInfo("0.3","","0", "0");
            esbLoadbalanceFailoverTest.addAddressEprTimeoutInfo("0.3","Never timeout", "");
            esbLoadbalanceFailoverTest.addAddressEprQosInfo("0.3","true", null, null, null, null, null);
            esbLoadbalanceFailoverTest.saveAddressEp("0.3");

            //save the FailoverGroup to registry
            esbCommon.saveAsAddressEndpoint(path+"/"+epName);
        }
    }

    public void testCreateAddressEndpoint() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        createAddressEndPoint("/esbEndPoints","address_ep");
    }

    public void testCreateWsdlendPoint() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        createWSDLEndpoint("/esbEndPoints","wsdl_ep");
    }

    public void testCreateFailoverGroup() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        createFailoverGroup("/esbEndPoints","failoverEp");
    }

    public void testCreateLoadBalanceGroup() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        createLoadBalanceGroup("/esbEndPoints","loadBalanceEp");
    }

    public void testcreateProxy() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);

        esbAddProxyServiceTest.addProxyName("testEndpoints_proxyService", "Custom Proxy");
        //esbAddProxyServiceTest.transportSettings("http", "https", null, null, null);
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.setTransport("https");
        esbAddProxyServiceTest.addSourceUrlWsdl("file:repository/samples/resources/proxy/sample_proxy_2.wsdl");
        esbAddProxyServiceTest.clickNext();

        //****setting up of the In Sequence of the Proxy Service*****//
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        esbAddProxyServiceTest.selectInSequence("inSeqOpAnon", null);
        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");

        //Adding a send mediator
        esbCommon.addMediators("Add Child","0","Core","Send");
        esbSendMediatorTest.addRegistryResFromPickedPath("0.0","/esbEndPoints/address_ep");
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
    }

    public void testAddressEpFromRegistry() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        editProxySendMediatorEndpoint("/esbEndPoints/address_ep","file:repository/samples/resources/proxy/sample_proxy_1.wsdl");

        //Invoke the client
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        int before=esbCommon.checkLogFile("java.lang.NullPointerException");
        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/testEndpoints_proxyService", esbCommon.getServiceAddUrl("SimpleStockQuoteService"),"IBM");
        int after=esbCommon.checkLogFile("java.lang.NullPointerException");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else if((!stockQuoteResponse) || (after-before)>0){
            throw new MyCheckedException("Client Failed!!!! or \"NullPointerException occured when addressEndpoint pick from the registry..!!!\"");
        }
    }

    public void testWsdlEpFromRegistry() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        editProxySendMediatorEndpoint("/esbEndPoints/wsdl_ep","file:repository/samples/resources/proxy/sample_proxy_1.wsdl");

        //Invoke the client
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        int before=esbCommon.checkLogFile("java.lang.NullPointerException");
        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/testEndpoints_proxyService", esbCommon.getServiceAddUrl("SimpleStockQuoteService"),"IBM");
        int after=esbCommon.checkLogFile("java.lang.NullPointerException");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else if((!stockQuoteResponse) || (after-before)>0){
            throw new MyCheckedException("Client Failed!!!! or \"NullPointerException occured when wsdlEndpoint pick from the registry..!!!\"");
        }
    }

    public void testFailoverGroupFromRegistry() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        editProxySendMediatorEndpoint("/esbEndPoints/failoverEp","file:repository/samples/resources/proxy/sample_proxy_2.wsdl");
        ESBLoadbalanceFailoverClient esbLoadbalanceFailoverClientTest =new ESBLoadbalanceFailoverClient();
        System.out.println("***Failover***");
        int before=esbCommon.checkLogFile("java.lang.NullPointerException");
        esbLoadbalanceFailoverClientTest.sessionlessClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/testEndpoints_proxyService", null,"7" );
        int after=esbCommon.checkLogFile("java.lang.NullPointerException");
        if((after-before)>0)
            throw new MyCheckedException("NullPointerException occured when FailoverGroup pick from the registry..!!!");
    }

    public void testLoadBalanceGroupFromRegistry() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        editProxySendMediatorEndpoint("/esbEndPoints/loadBalanceEp","file:repository/samples/resources/proxy/sample_proxy_2.wsdl");
        ESBLoadbalanceFailoverClient esbLoadbalanceFailoverClientTest =new ESBLoadbalanceFailoverClient();
        System.out.println("***LoaadBalance***");
        int before=esbCommon.checkLogFile("java.lang.NullPointerException");
        esbLoadbalanceFailoverClientTest.sessionlessClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/services/testEndpoints_proxyService", null,"7" );
        int after=esbCommon.checkLogFile("java.lang.NullPointerException");
        if((after-before)>0)
            throw new MyCheckedException("NullPointerException occured when LoadBalanceGroup pick from the registry..!!!");
    }

    public void editProxySendMediatorEndpoint(String ep,String wsdlsourceUrl) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        esbCommon.logoutLogin();
        esbAddProxyServiceTest.editProxyService("testEndpoints_proxyService");
        Thread.sleep(2000);

        esbAddProxyServiceTest.addSourceUrlWsdl(wsdlsourceUrl);
        selenium.click("nextBtn");
		selenium.click("inAnonAddEdit");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Send");
        esbSendMediatorTest.addRegistryResFromPickedPath("0.0",ep);
        esbCommon.mediatorUpdate();
        selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("nextBtn");
		selenium.click("saveBtn");
		selenium.waitForPageToLoad("30000");
    }
}


