package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;

/*
 * Created by IntelliJ IDEA.
 * User: Suminda Chamara
 * Date: Mar 30, 2009
 * Time: 11:16:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class ManageAxis2ServicesTest extends TestCase
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

        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String file = instReadXMLFileTest.ReadConfig("FilePath","Axis2Service");
        String ServiceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
 
        browser.open("/carbon/service-mgt/index.jsp?pageNumber=0");
		browser.click("link=Axis2 Service");
		browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("aarFilename",file);
		browser.click("upload");
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created Axis2 service"));
		browser.click("//button[@type='button']");
        Thread.sleep(12000);
		browser.click("link=List");
		browser.waitForPageToLoad("120000");
		assertTrue(browser.isTextPresent(ServiceName));

  }

   public void testAddServiceGroupModule()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.engageServiceGroupModule("Axis2Service","sandesha2-SNAPSHOT");
        instServiceManagement.disengageServiceGroupModule("Axis2Service","sandesha2-SNAPSHOT");
    }
   public void testCheckThrottling()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
          //------ Throttle Test XML readings -------
        String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName","Axis2ThrottleTest");
        String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction","Axis2ThrottleTest");
        String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint","Axis2ThrottleTest");
        String NameSpaceTR = instReadXMLFileTest.ReadConfig("NameSpace","Axis2ThrottleTest");
        String serverIP = instReadXMLFileTest.ReadConfig("ip","ServerConfig");
        String httpPort = instReadXMLFileTest.ReadConfig("http","ServerConfig");

        instServiceManagement.openServiceDashboard("Axis2Service","axis2");
        instServiceManagement.checkThrottling("http://"+serverIP+":"+httpPort+"/"+serviceEprTR,operationNameTR,SoapActionTR,NameSpaceTR,"Text");
    }

    public void testCheckCaching()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        instServiceManagement.accessServiceDashboard("Axis2Service");
        String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName","Axis2CachingTest");
        String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction","Axis2CachingTest");
        String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint","Axis2CachingTest");
        String NameSpaceTR = instReadXMLFileTest.ReadConfig("NameSpace","Axis2CachingTest");
        String serverIP = instReadXMLFileTest.ReadConfig("ip","ServerConfig");
        String httpPort = instReadXMLFileTest.ReadConfig("http","ServerConfig");

        instServiceManagement.checkCaching("http://"+serverIP+":"+httpPort+"/"+serviceEprTR,operationNameTR,SoapActionTR,NameSpaceTR);
    }

   public void testServiceModuleMgt()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.accessServiceDashboard("Axis2Service");
        instServiceManagement.engageServiceModule("Axis2Service","sandesha2-1.00");
        instServiceManagement.accessServiceDashboard("Axis2Service");
        instServiceManagement.disengageServiceModule("Axis2Service","sandesha2-1.00");
    }

    public void testServiceActivation()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.serviceActivation("Axis2Service","Deactivate");
        instServiceManagement.serviceActivation("Axis2Service","Activate");
    }
 
   public void testTransport()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.checkTransport("Axis2Service");
    }

  public void testServiceParameter()throws Exception
     {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement =new ServiceManagement(browser);
        instServiceManagement.accessServiceDashboard("Axis2Service");
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.accessServiceDashboard("Axis2Service");
        mySeleniumTestBase.deleteParameter("testparam");
     }

      public void testServiceGroupParameter()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.addServiceGroupParameter("Axis2Service","NewParam1");
        Thread.sleep(1000);
        instServiceManagement.deleteServiceGroupParameter("Axis2Service","NewParam1");
    }

  /*   public void testOperationalThrottling()throws Exception
   {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName","Axis2ThrottleTest");
       String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction","Axis2ThrottleTest");
       String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint","Axis2ThrottleTest");
       String NameSpaceTR = instReadXMLFileTest.ReadConfig("NameSpace","Axis2ThrottleTest");
       String serverIP = instReadXMLFileTest.ReadConfig("ip","ServerConfig");
        String httpPort = instReadXMLFileTest.ReadConfig("http","ServerConfig");

       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.openOperationDashboard("Axis2Service",operationNameTR);
       instServiceManagement.checkThrottling("http://"+serverIP+":"+httpPort+"/"+serviceEprTR,operationNameTR,SoapActionTR,NameSpaceTR,"Text");
   }
  public void testOperationalCaching()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String operationNameTR = instReadXMLFileTest.ReadConfig("OperationName","Axis2CachingTest");
        String SoapActionTR = instReadXMLFileTest.ReadConfig("SoapAction","Axis2CachingTest");
        String serviceEprTR = instReadXMLFileTest.ReadConfig("ServiceEndpoint","Axis2CachingTest");
        String NameSpaceTR = instReadXMLFileTest.ReadConfig("NameSpace","Axis2CachingTest");
        String serverIP = instReadXMLFileTest.ReadConfig("ip","ServerConfig");
        String httpPort = instReadXMLFileTest.ReadConfig("http","ServerConfig");

        instServiceManagement.accessServiceDashboard("Axis2Service");
        instServiceManagement.openOperationDashboard("Axis2Service",operationNameTR);
        instServiceManagement.checkCaching("http://"+serverIP+":"+httpPort+"/"+serviceEprTR,operationNameTR,SoapActionTR,NameSpaceTR);
    }*/
    public void testOperationalModuleMgt()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.engageOperationModule("Axis2Service","echoString","savan-1.00");
        instServiceManagement.disengageOperationModule("Axis2Service","echoString","savan-1.00");
    }
      public void testOperationalAddParameters()throws Exception
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement =new ServiceManagement(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String operationName = instReadXMLFileTest.ReadConfig("OperationName", "Axis2CachingTest");
        instServiceManagement.openOperationDashboard("Axis2Service", operationName);
        mySeleniumTestBase.addNewParameter("testparam");
        instServiceManagement.openOperationDashboard("Axis2Service", operationName);
        mySeleniumTestBase.deleteParameter("testparam");
    }
   public void testOperationalFlows()throws Exception
    {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.accessServiceDashboard("Axis2Service");
        instServiceManagement.openOperationDashboard("Axis2Service","echoString" );
        instServiceManagement.checkParamFlows();
    }

    public void testReliableMessaging()throws Exception
    {
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
        String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
        String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
        String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        instServiceManagement.enableRM("Axis2Service");
        instServiceManagement.rmInvocations(serviceName,soapAction,nameSpace,operation,"s");
        instServiceManagement.disableRM("Axis2Service");
    }

  /*   public void testMTOM()throws Exception
     {
         ServiceManagement insServiceManagement = new ServiceManagement(browser);

         insServiceManagement.changeOperationalMTOM("Axis2Service", "echoString","True");
         Thread.sleep(1000);
         insServiceManagement.changeOperationalMTOM("Axis2Service", "echoString","Optional");
         Thread.sleep(1000);
         insServiceManagement.changeOperationalMTOM("Axis2Service", "echoString","False");
         Thread.sleep(1000);
         insServiceManagement.changeServiceGroupMTOM("Axis2Service","True");
         Thread.sleep(1000);
         insServiceManagement.changeServiceGroupMTOM("Axis2Service","Optional");
         Thread.sleep(1000);
         insServiceManagement.changeServiceGroupMTOM("Axis2Service","False");
         Thread.sleep(1000);
         insServiceManagement.changeServiceMTOM("Axis2Service","Optional");
         Thread.sleep(1000);
         insServiceManagement.changeServiceMTOM("Axis2Service","True");
         Thread.sleep(1000);
         insServiceManagement.changeServiceMTOM("Axis2Service","False");


     }
*/
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
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignConfidentialitySecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
  /*  public void testCheckSecurityScenario2()throws Exception
    {
       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignUTSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario3()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignIntegritySecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario4()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignNonRepudiationSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario5()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

        ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignSecConEncrOnlyAnonymousSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario6()throws Exception
            
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignSecConSignOnlySecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario7()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignSecConEncrOnlySecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario8()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignSecConSignOnlyAnonymousSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario9()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignSecConSgnEncrSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario10()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignSecConSgnEncrUsernameSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario11()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignEncrOnlyUsernameSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario12()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignSigEncrSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario13()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignSgnEncrUsernameSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario14()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignSgnEncrAnonymousScenario(serviceName,nameSpace,soapAction,operation,"s");
    }
    public void testCheckSecurityScenario15()throws Exception
    {
       ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
       String serviceName = instReadXMLFileTest.ReadConfig("ServiceName","Axis2Service");
       String nameSpace = instReadXMLFileTest.ReadConfig("nameSpace","Axis2Securitytest");
       String soapAction = instReadXMLFileTest.ReadConfig("SoapAction","Axis2Securitytest");
       String operation = instReadXMLFileTest.ReadConfig("OperationName","Axis2Securitytest");

       ServiceManagement instServiceManagement = new ServiceManagement(browser);
       instServiceManagement.accessServiceDashboard("Axis2Service");
       instServiceManagement.assignSecConEncrUsernameSecurityScenario(serviceName,nameSpace,soapAction,operation,"s");
    }*/
    public void testDeleteKeystore()throws Exception
  {
      KeyStoreManagement instKeyStoreManagement = new KeyStoreManagement(browser);
      instKeyStoreManagement.deleteKeyStore("qaserver.jks");
  }

    public void testRemoveServices()throws Exception
    {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.deleteService("Axis2Service");
    }
   
     public void logOut()throws Exception
     {
         SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
         instSeleniumTestBase.logOutUI();
     }
}
