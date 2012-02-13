package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement; /*
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

/**
 * Created by IntelliJ IDEA.
 * User: Suminda Chamara
 * Date: Apr 21, 2009
 * Time: 5:23:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManageJaxWsServiceTest extends TestCase
{
    Selenium browser;

   public void setUp() throws Exception
    {
		browser = BrowserInitializer.getBrowser();
	}
   public void testUploadservice()throws Exception
   {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        InstSeleniumTestBase.loginToUI("admin","admin");

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("JaxWsService.XML");
        String file = instReadXMLFileTest.ReadConfig("filePath","JaxWsService");
        String ServiceName = instReadXMLFileTest.ReadConfig("ServiceName","JaxWsService");

        browser.open("/carbon/service-mgt/index.jsp?pageNumber=0");
		browser.click("link=JAX-WS Service");
		browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("filename",file);
		browser.click("upload");
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to seethe status of the created JAXWS service"));
		browser.click("//button[@type='button']");
        Thread.sleep(12000);
		browser.click("link=List");
		browser.waitForPageToLoad("120000");
		assertTrue(browser.isTextPresent(ServiceName));

  }


     public void testAddServiceGroupModule()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.engageServiceGroupModule("JaxWSService.jar","savan-1.00");
        instServiceManagement.disengageServiceGroupModule("JaxWSService.jar","savan-1.00");
    }
   public void testCheckThrottling()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("JaxWsService.XML");
          //------ Throttle Test XML readings -------
        String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName","ThrottleTest");
        String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction","ThrottleTest");
        String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint","ThrottleTest");
        String NameSpaceTR = instReadXMLFileTest.ReadConfig("NameSpace","ThrottleTest");

        instServiceManagement.openServiceDashboard("JaxWSTestService.JaxWSServicePort","jaxws");
        instServiceManagement.checkThrottling(serviceEprTR,operationNameTR,SoapActionTR,NameSpaceTR,"Text");
    }


    public void testCheckCaching()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("JaxWsService.XML");
        instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
        String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName","CachingTest");
        String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction","CachingTest");
        String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint","CachingTest");
        String NameSpaceTR = instReadXMLFileTest.ReadConfig("NameSpace","CachingTest");

        instServiceManagement.checkCaching(serviceEprTR,operationNameTR,SoapActionTR,NameSpaceTR);
    }

   public void testServiceModuleMgt()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
        instServiceManagement.engageServiceModule("JaxWSTestService.JaxWSServicePort","sandesha2-1.00");
        instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
        instServiceManagement.disengageServiceModule("JaxWSTestService.JaxWSServicePort","sandesha2-1.00");
    }

    public void testServiceActivation()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.serviceActivation("JaxWSTestService.JaxWSServicePort","Deactivate");
        instServiceManagement.serviceActivation("JaxWSTestService.JaxWSServicePort","Activate");
    }

   public void testTransport()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.checkTransport("JaxWSTestService.JaxWSServicePort");
    }

  /* public void testServiceParameter()throws Exception
     {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement =new ServiceManagement(browser);
        instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
        mySeleniumTestBase.deleteParameter("testparam");
     }

      public void testServiceGroupParameter()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.addServiceGroupParameter("JaxWSService.jar","NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("JaxWSService.jar","NewParam1");
    }*/
     public void testAddKeyStore()throws Exception
  {
      KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
      ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
      String keystorePath = instReadXMLFileTest.ReadConfig("SecurityKeyStore","KeystoreMGT");
      String keystorePassword = instReadXMLFileTest.ReadConfig("SecurityKeyStorePwd","KeystoreMGT");
      instKeyStoreManagement.AddKeystore(keystorePath,keystorePassword);
  }
    
    public void testCheckSecurityScenario1()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","JaxWsService");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","jaxwsSecuritytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","jaxwsSecuritytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","jaxwsSecuritytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignConfidentialitySecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    /*public void testCheckSecurityScenario2()throws Exception
    {
       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignUTSecurityScenario("JaxWSTestService.JaxWSServicePort");
    }*/
    public void testCheckSecurityScenario3()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","JaxWsService");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","jaxwsSecuritytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","jaxwsSecuritytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","jaxwsSecuritytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignIntegritySecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario4()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","JaxWsService");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","jaxwsSecuritytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","jaxwsSecuritytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","jaxwsSecuritytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignNonRepudiationSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario5()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","JaxWsService");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","jaxwsSecuritytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","jaxwsSecuritytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","jaxwsSecuritytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignSecConEncrOnlyAnonymousSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario6()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","JaxWsService");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","jaxwsSecuritytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","jaxwsSecuritytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","jaxwsSecuritytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignSecConSignOnlySecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario7()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","JaxWsService");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","jaxwsSecuritytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","jaxwsSecuritytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","jaxwsSecuritytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignSecConEncrOnlySecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario8()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","JaxWsService");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","jaxwsSecuritytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","jaxwsSecuritytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","jaxwsSecuritytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignSecConSignOnlyAnonymousSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario9()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","JaxWsService");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","jaxwsSecuritytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","jaxwsSecuritytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","jaxwsSecuritytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignSecConSgnEncrSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
   /* public void testCheckSecurityScenario10()throws Exception
    {
       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignSecConSgnEncrUsernameSecurityScenario("JaxWSTestService.JaxWSServicePort");
    }
    public void testCheckSecurityScenario11()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","JaxWsService");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","jaxwsSecuritytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","jaxwsSecuritytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","jaxwsSecuritytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignEncrOnlyUsernameSecurityScenario("JaxWSTestService.JaxWSServicePort");
    }*/
    public void testCheckSecurityScenario12()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","JaxWsService");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","jaxwsSecuritytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","jaxwsSecuritytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","jaxwsSecuritytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignSigEncrSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
   /* public void testCheckSecurityScenario13()throws Exception
    {
       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignSgnEncrUsernameSecurityScenario("JaxWSTestService.JaxWSServicePort");
    }*/
    public void testCheckSecurityScenario14()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","JaxWsService");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","jaxwsSecuritytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","jaxwsSecuritytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","jaxwsSecuritytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignSgnEncrAnonymousScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
   /* public void testCheckSecurityScenario15()throws Exception
    {
       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.assignSecConEncrUsernameSecurityScenario("JaxWSTestService.JaxWSServicePort");
    }*/
      public void testDeleteKeystore()throws Exception
  {
      KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
      instKeyStoreManagement.deleteKeyStore("qaserver.jks");
  }

   public void testParamThrottling()throws Exception
   {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("JaxWsService.XML");
       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName","ThrottleTest");
       String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction","ThrottleTest");
       String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint","ThrottleTest");
       String NameSpaceTR = instReadXMLFileTest.ReadConfig("NameSpace","ThrottleTest");
       instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
       instServiceManagement.openOperationDashboard("JaxWSTestService.JaxWSServicePort",operationNameTR);
       instServiceManagement.checkThrottling(serviceEprTR,operationNameTR,SoapActionTR,NameSpaceTR,"Text");
   }
  public void testParamCaching()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("JaxWsService.XML");
        String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName","CachingTest");
        String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction","CachingTest");
        String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint","CachingTest");
        String NameSpaceTR = instReadXMLFileTest.ReadConfig("NameSpace","CachingTest");

        instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
        instServiceManagement.openOperationDashboard("JaxWSTestService.JaxWSServicePort",operationNameTR);
        instServiceManagement.checkCaching(serviceEprTR,operationNameTR,SoapActionTR,NameSpaceTR);
    }
    public void testParamModuleMgt()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.engageOperationModule("JaxWSTestService.JaxWSServicePort","echoString","savan-1.00");
        instServiceManagement.disengageOperationModule("JaxWSTestService.JaxWSServicePort","echoString","savan-1.00");
    }
  /*    public void testOperationalAddParameters()throws Exception
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement =new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("JaxWsService.XML");
        String operationName = instReadXMLFileTest.ReadConfig("OperationName", "CachingTest");
        instServiceManagement.openOperationDashboard("JaxWSTestService.JaxWSServicePort", operationName);
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.openOperationDashboard("JaxWSTestService.JaxWSServicePort", operationName);
        mySeleniumTestBase.deleteParameter("testparam");
    }*/
   public void testParamFlows()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.accessServiceDashboard("JaxWSTestService.JaxWSServicePort");
        instServiceManagement.openOperationDashboard("JaxWSTestService.JaxWSServicePort","echoString" );
        instServiceManagement.checkParamFlows();
    }

    public void testReliableMessaging()throws Exception
    {
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","JaxWsService");
        String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","jaxwsSecuritytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","jaxwsSecuritytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","jaxwsSecuritytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.enableRM("JaxWSTestService.JaxWSServicePort");
        instServiceManagement.rmInvocations(serviceName,soapAction,nameSpace,operation,"s");
        instServiceManagement.disableRM("JaxWSTestService.JaxWSServicePort");
    }

     public void testMTOM()throws Exception
     {
         ServiceManagement insServiceManagement = new ServiceManagement(browser);
         insServiceManagement.changeServiceGroupMTOM("JaxWSService.jar","True");
         Thread.sleep(1000);
         insServiceManagement.changeServiceGroupMTOM("JaxWSService.jar","Optional");
         Thread.sleep(1000);
         insServiceManagement.changeServiceGroupMTOM("JaxWSService.jar","False");
         Thread.sleep(1000);
         insServiceManagement.changeServiceMTOM("JaxWSService.jar","Optional");
         Thread.sleep(1000);
         insServiceManagement.changeServiceMTOM("JaxWSService.jar","True");
         Thread.sleep(1000);
         insServiceManagement.changeServiceMTOM("JaxWSService.jar","False");
         Thread.sleep(1000);
         insServiceManagement.changeOperationalMTOM("JaxWSService.jar", "echoString","Optional");
         Thread.sleep(1000);
         insServiceManagement.changeOperationalMTOM("JaxWSService.jar", "echoString","True");
         Thread.sleep(1000);
         insServiceManagement.changeOperationalMTOM("JaxWSService.jar", "echoString","False");
     }
    public void testRemoveServices()throws Exception
    {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.deleteService("JaxWSService.jar");
    }

     public void logOut()throws Exception
     {
         SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
         instSeleniumTestBase.logOutUI();
     } 
}
