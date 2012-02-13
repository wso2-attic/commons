package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
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

/*
 * Created by IntelliJ IDEA.
 * User: Suminda Chamara
 * Date: Apr 20, 2009
 * Time: 5:51:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManagePojoServiceTest extends TestCase
{
    Selenium browser;

    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
    }

    public void testUploadservice() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        InstSeleniumTestBase.loginToUI("admin", "admin");

       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String resourcefile = instReadXMLFileTest.ReadConfig("PojoPath","PojoService");
        String ServiceName = instReadXMLFileTest.ReadConfig("ServiceName", "PojoService");

        browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
        browser.click("link=POJO Service");
        browser.waitForPageToLoad("30000");
      //  browser.attachFile("jarZipFilename","file:///D:\\Projects\\Idea\\TestFramework\\web-test-framework\\wsas\\lib\\PojoService.class");
        InstSeleniumTestBase.SetFileBrowse("jarZipFilename", resourcefile);
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created POJO service"));

        browser.click("//button[@type='button']");
        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));

    }

    public void testAddServiceGroupModule()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.engageServiceGroupModule("PojoService","sandesha2-SNAPSHOT");
        instServiceManagement.disengageServiceGroupModule("PojoService","sandesha2-SNAPSHOT");
    }
     public void testServiceGroupParameter()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.addServiceGroupParameter("PojoService","NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("PojoService","NewParam1");
    }
    public void testCheckThrottling() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        //------ Throttle Test XML readings -------
        String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName", "PojoThrottleTest");
        String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction", "PojoThrottleTest");
        String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint", "PojoThrottleTest");
        String nameSpaceTR = instReadXMLFileTest.ReadConfig("NameSpace", "PojoThrottleTest");
        String serverIP = instReadXMLFileTest.ReadConfig("ip", "ServerConfig");
        String httpPort = instReadXMLFileTest.ReadConfig("http", "ServerConfig");

        instServiceManagement.openServiceDashboard("PojoService","axis2");
        instServiceManagement.checkThrottling("http://" + serverIP + ":" + httpPort + "/" + serviceEprTR, operationNameTR, SoapActionTR,nameSpaceTR,"Text");
    }

    public void testCheckCaching() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        instServiceManagement.accessServiceDashboard("PojoService");
        String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName", "PojoCachingTest");
        String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction", "PojoCachingTest");
        String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint", "PojoCachingTest");
        String NameSpaceTR = instReadXMLFileTest.ReadConfig("NameSpace", "PojoCachingTest");
        String serverIP = instReadXMLFileTest.ReadConfig("ip", "ServerConfig");
        String httpPort = instReadXMLFileTest.ReadConfig("http", "ServerConfig");

        instServiceManagement.checkCaching("http://" + serverIP + ":" + httpPort + "/" + serviceEprTR, operationNameTR, SoapActionTR,NameSpaceTR);
    }

    public void testCheckModuleMgt() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.accessServiceDashboard("PojoService");
        instServiceManagement.engageServiceModule("PojoService", "sandesha2-1.00");
        instServiceManagement.accessServiceDashboard("PojoService");
        instServiceManagement.disengageServiceModule("PojoService", "sandesha2-1.00");
    }
    public void testServiceParameter()throws Exception
     {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement =new ServiceManagement(browser);
        instServiceManagement.accessServiceDashboard("PojoService");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("PojoService");
        mySeleniumTestBase.deleteParameter("testparam");
     }

    public void testServiceActivation() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.serviceActivation("PojoService", "Deactivate");
        instServiceManagement.serviceActivation("PojoService", "Activate");
    }

    public void testTransport() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.checkTransport("PojoService");

    }
    public void testOperationalThrottling() throws Exception {
           ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
           ServiceManagement instServiceManagement = new ServiceManagement(browser);
           String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName", "PojoThrottleTest");
           String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction", "PojoThrottleTest");
           String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint", "PojoThrottleTest");
           String NameSpaceTR = instReadXMLFileTest.ReadConfig("NameSpace", "PojoThrottleTest");
           String serverIP = instReadXMLFileTest.ReadConfig("ip", "ServerConfig");
           String httpPort = instReadXMLFileTest.ReadConfig("http", "ServerConfig");


           instServiceManagement.accessServiceDashboard("PojoService");
           instServiceManagement.openOperationDashboard("PojoService", operationNameTR);
           instServiceManagement.checkThrottling("http://" + serverIP + ":" + httpPort + "/" + serviceEprTR, operationNameTR, SoapActionTR,NameSpaceTR,"Text");
       }

       public void testOperationalCaching() throws Exception {
           ServiceManagement instServiceManagement = new ServiceManagement(browser);
           ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
           String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName", "PojoCachingTest");
           String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction", "PojoCachingTest");
           String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint", "PojoCachingTest");
           String NameSpaceTR = instReadXMLFileTest.ReadConfig("NameSpace", "PojoCachingTest");
           String serverIP = instReadXMLFileTest.ReadConfig("ip", "ServerConfig");
           String httpPort = instReadXMLFileTest.ReadConfig("http", "ServerConfig");
           
           instServiceManagement.accessServiceDashboard("PojoService");
           instServiceManagement.openOperationDashboard("PojoService", operationNameTR);
           instServiceManagement.checkCaching("http://" + serverIP + ":" + httpPort + "/" + serviceEprTR, operationNameTR, SoapActionTR,NameSpaceTR);
       }

       public void testoperationalModuleMgt() throws Exception {
           ServiceManagement instServiceManagement = new ServiceManagement(browser);
           instServiceManagement.engageOperationModule("PojoService", "echoString", "savan-1.00");
           instServiceManagement.disengageOperationModule("PojoService", "echoString", "savan-1.00");
       }
       public void testOperationalAddParameters()throws Exception
       {
           SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
           ServiceManagement instServiceManagement =new ServiceManagement(browser);
           ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
           String operationName = instReadXMLFileTest.ReadConfig("OperationName", "CachingTest");
           instServiceManagement.openOperationDashboard("PojoService", operationName);
           mySeleniumTestBase.addNewParameter("testparam");
           instServiceManagement.openOperationDashboard("PojoService", operationName);
           mySeleniumTestBase.deleteParameter("testparam");
       }
       public void testOperationalFlows() throws Exception {
           ServiceManagement instServiceManagement = new ServiceManagement(browser);
           instServiceManagement.accessServiceDashboard("PojoService");
           instServiceManagement.openOperationDashboard("PojoService", "echoString");
           instServiceManagement.checkParamFlows();
       }

       public void testReliableMessaging() throws Exception {

           ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
           String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
           String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","PojoSecuritytest");
           String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
           String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

           ServiceManagement instServiceManagement = new ServiceManagement(browser);
           instServiceManagement.enableRM("PojoService");
           instServiceManagement.rmInvocations(serviceName,soapAction,nameSpace,operation,"s");
           instServiceManagement.disableRM("PojoService");
       }
         public void testMTOM()throws Exception
        {
            ServiceManagement insServiceManagement = new ServiceManagement(browser);

            insServiceManagement.changeOperationalMTOM("PojoService", "echoString","Optional");
            Thread.sleep(1000);
            insServiceManagement.changeOperationalMTOM("PojoService", "echoString","True");
            Thread.sleep(1000);
            insServiceManagement.changeOperationalMTOM("PojoService", "echoString","False");
            Thread.sleep(1000);
            insServiceManagement.changeServiceGroupMTOM("PojoService","True");
            Thread.sleep(1000);
            insServiceManagement.changeServiceGroupMTOM("PojoService","Optional");
            Thread.sleep(1000);
            insServiceManagement.changeServiceGroupMTOM("PojoService","False");
            Thread.sleep(1000);
            insServiceManagement.changeServiceMTOM("PojoService","Optional");
            Thread.sleep(1000);
            insServiceManagement.changeServiceMTOM("PojoService","True");
            Thread.sleep(1000);
            insServiceManagement.changeServiceMTOM("PojoService","False");

        }

    public void testAddKeyStore()throws Exception
  {
      KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
      ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
      String keystorePath = instReadXMLFileTest.ReadConfig("SecurityKeyStore","KeystoreMGT");
      String keystorePassword = instReadXMLFileTest.ReadConfig("SecurityKeyStorePwd","KeystoreMGT");
      instKeyStoreManagement.AddKeystore(keystorePath,keystorePassword);
  }
    
    public void testCheckSecurityScenario1() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignConfidentialitySecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }

    public void testCheckSecurityScenario2() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");
        
        instServiceManagement.assignUTSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }

    public void testCheckSecurityScenario3() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignIntegritySecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }

    public void testCheckSecurityScenario4() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignNonRepudiationSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }

    public void testCheckSecurityScenario5() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignSecConEncrOnlyAnonymousSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }

    public void testCheckSecurityScenario6() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignSecConSignOnlySecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }

    public void testCheckSecurityScenario7() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignSecConEncrOnlySecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }

    public void testCheckSecurityScenario8() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.accessServiceDashboard("PojoService");
        instServiceManagement.assignSecConSignOnlyAnonymousSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }

    public void testCheckSecurityScenario9() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignSecConSgnEncrSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }

    public void testCheckSecurityScenario10() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

        instServiceManagement.assignSecConSgnEncrUsernameSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }

    public void testCheckSecurityScenario11() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

        instServiceManagement.assignEncrOnlyUsernameSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }

    public void testCheckSecurityScenario12() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignSigEncrSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }

    public void testCheckSecurityScenario13() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

        instServiceManagement.assignSgnEncrUsernameSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }

    public void testCheckSecurityScenario14() throws Exception {

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.assignSgnEncrAnonymousScenario(serviceName,nameSpace,soapAction,operation,"s");
    }

    public void testCheckSecurityScenario15() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","PojoService");
        String nameSpace = instReadXMLFileTest.ReadConfig("NameSpace","PojoSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","PojoSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","PojoSecuritytest");

        instServiceManagement.assignSecConEncrUsernameSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
      public void testDeleteKeystore()throws Exception
  {
      KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
      instKeyStoreManagement.deleteKeyStore("qaserver.jks");
  }

   public void testRemoveServices() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.deleteService("PojoService");

    }

    public void logOut() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }

}
