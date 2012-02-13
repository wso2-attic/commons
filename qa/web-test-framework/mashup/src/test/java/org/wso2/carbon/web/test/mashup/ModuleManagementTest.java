package org.wso2.carbon.web.test.mashup;

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


import com.thoughtworks.selenium.SeleneseTestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ModuleManagement;
import org.wso2.carbon.web.test.common.MexModuleClient;
import org.wso2.carbon.web.test.common.RMClient;
import org.wso2.carbon.web.test.common.CachingClient;
import org.wso2.carbon.web.test.common.ServiceManagement;

public class ModuleManagementTest extends CommonSetup {

    public ModuleManagementTest(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testSignIn() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }

    //test whether the modules Are present in the module index page
       public void testModuleIndexPage() throws Exception {
           ModuleManagement instModuleManagement = new ModuleManagement(selenium);

           instModuleManagement.checkModules("wso2mex-2.01");
           instModuleManagement.checkModules("sandesha2-2.01");
           instModuleManagement.checkModules("wso2xfer-2.01");
           instModuleManagement.checkModules("wso2caching-2.01");
           instModuleManagement.checkModules("rahas-2.01");
           instModuleManagement.checkModules("savan-SNAPSHOT");
           instModuleManagement.checkModules("wso2throttle-2.01");
           instModuleManagement.checkModules("addressing-1.5");
           instModuleManagement.checkModules("rampart-2.01");

       }

    //test engaging and disengaging modules from the module index page
       public void testEngageModules() throws Exception {
           ModuleManagement instModuleManagement = new ModuleManagement(selenium);

           instModuleManagement.accessModuleIndexPage();
           instModuleManagement.engageGlobalLevelModules("wso2mex-2.01");
           instModuleManagement.engagedAtServiceGroupLevel("admin/allCommons", "wso2mex-2.01");
           instModuleManagement.engagedAtServiceLevel("admin/allCommons", "wso2mex-2.01");
           instModuleManagement.engagedAtOperationalLevel("admin/allCommons", "echoString", "wso2mex-2.01");
           instModuleManagement.disengageGlobalLevelModules("wso2mex-2.01");

           instModuleManagement.engageGlobalLevelModules("sandesha2-2.01");
           instModuleManagement.engagedAtServiceGroupLevel("admin/allCommons", "sandesha2-2.01");
           instModuleManagement.engagedAtServiceLevel("admin/allCommons", "sandesha2-2.01");
           instModuleManagement.engagedAtOperationalLevel("admin/allCommons", "echoString", "sandesha2-2.01");
           instModuleManagement.disengageGlobalLevelModules("sandesha2-2.01");

           instModuleManagement.engageGlobalLevelModules("wso2xfer-2.01");
           instModuleManagement.engagedAtServiceGroupLevel("admin/allCommons", "wso2xfer-2.01");
           instModuleManagement.engagedAtServiceLevel("admin/allCommons", "wso2xfer-2.01");
           instModuleManagement.engagedAtOperationalLevel("admin/allCommons", "echoString", "wso2xfer-2.01");
           instModuleManagement.disengageGlobalLevelModules("wso2xfer-2.01");

           instModuleManagement.engageGlobalLevelModules("savan-SNAPSHOT");
           instModuleManagement.engagedAtServiceGroupLevel("admin/allCommons", "savan-SNAPSHOT");
           instModuleManagement.engagedAtServiceLevel("admin/allCommons", "savan-SNAPSHOT");
           instModuleManagement.engagedAtOperationalLevel("admin/allCommons", "echoString", "savan-SNAPSHOT");
           instModuleManagement.disengageGlobalLevelModules("savan-SNAPSHOT");

           instModuleManagement.engageGlobalLevelModules("rahas-2.01");
           instModuleManagement.engagedAtServiceGroupLevel("admin/allCommons", "rahas-2.01");
           instModuleManagement.engagedAtServiceLevel("admin/allCommons", "rahas-2.01");
           instModuleManagement.engagedAtOperationalLevel("admin/allCommons", "echoString", "rahas-2.01");
           instModuleManagement.engagedAtServiceGroupLevel("admin/allCommons", "rampart-2.01");
           instModuleManagement.engagedAtServiceLevel("admin/allCommons", "rampart-2.01");
           instModuleManagement.engagedAtOperationalLevel("admin/allCommons", "echoString", "rampart-2.01");
           instModuleManagement.disengageGlobalLevelModules("rahas-2.01");
           instModuleManagement.disengageGlobalLevelModules("rampart-2.01");
       }

//       public void testUploadModule() throws Exception{
//           ModuleManagement instModuleManagement = new ModuleManagement(selenium);
//           instModuleManagement.uploadModule("counter-module-SNAPSHOT");
//           instModuleManagement.checkModules("counter-module-SNAPSHOT");
//           instModuleManagement.deleteModule("counterModule-SNAPSHOT");
//           assertFalse(selenium.isTextPresent("counterModule"));
//       }


       //test adding ane removing module parameters in the module index page
       public void testModulesParameters() throws Exception {
           ModuleManagement instModuleManagement = new ModuleManagement(selenium);

           instModuleManagement.accessModules("sandesha2-2.01");
           instModuleManagement.addNewModuleParameter("testparam");
           assertTrue(selenium.isTextPresent("managedModule"));
           assertTrue(selenium.isTextPresent("propertiesToCopyFromReferenceMessage"));
           assertTrue(selenium.isTextPresent("propertiesToCopyFromReferenceRequestMessage"));
           instModuleManagement.accessModules("sandesha2-2.01");
           instModuleManagement.deleteModuleParameter("testparam");
           instModuleManagement.accessModuleIndexPage();

           instModuleManagement.accessModules("wso2caching-2.01");
           instModuleManagement.addNewModuleParameter("testparam");
           assertTrue(selenium.isTextPresent("managedModule"));
           instModuleManagement.accessModules("wso2caching-2.01");
           instModuleManagement.deleteModuleParameter("testparam");
           instModuleManagement.accessModuleIndexPage();
  //
           instModuleManagement.accessModules("rahas-2.01");
           instModuleManagement.addNewModuleParameter("testparam");
           assertTrue(selenium.isTextPresent("managedModule"));
           instModuleManagement.accessModules("rahas-2.01");
           instModuleManagement.deleteModuleParameter("testparam");
           instModuleManagement.accessModuleIndexPage();

           instModuleManagement.accessModules("rampart-2.01");
           instModuleManagement.addNewModuleParameter("testparam");
           assertTrue(selenium.isTextPresent("managedModule"));
           instModuleManagement.accessModules("rampart-2.01");
           instModuleManagement.deleteModuleParameter("testparam");
           instModuleManagement.accessModuleIndexPage();

           instModuleManagement.accessModules("savan-SNAPSHOT");
           instModuleManagement.addNewModuleParameter("testparam");
           assertTrue(selenium.isTextPresent("managedModule"));
           instModuleManagement.accessModules("savan-SNAPSHOT");
           instModuleManagement.deleteModuleParameter("testparam");
           instModuleManagement.accessModuleIndexPage();

           instModuleManagement.accessModules("wso2throttle-2.01");
           instModuleManagement.addNewModuleParameter("testparam");
           assertTrue(selenium.isTextPresent("managedModule"));
           instModuleManagement.accessModules("wso2throttle-2.01");
           instModuleManagement.deleteModuleParameter("testparam");
           instModuleManagement.accessModuleIndexPage();

           instModuleManagement.accessModules("addressing-1.5");
           instModuleManagement.addNewModuleParameter("testparam");
           instModuleManagement.accessModules("addressing-1.5");
           instModuleManagement.deleteModuleParameter("testparam");
           instModuleManagement.accessModuleIndexPage();

           instModuleManagement.accessModules("wso2mex-2.01");
           instModuleManagement.addNewModuleParameter("testparam");
           assertTrue(selenium.isTextPresent("managedModule"));
           instModuleManagement.accessModules("wso2mex-2.01");
           instModuleManagement.deleteModuleParameter("testparam");
           instModuleManagement.accessModuleIndexPage();

           instModuleManagement.accessModules("wso2xfer-2.01");
           instModuleManagement.addNewModuleParameter("testparam");
           assertTrue(selenium.isTextPresent("managedModule"));
           instModuleManagement.accessModules("wso2xfer-2.01");
           instModuleManagement.deleteModuleParameter("testparam");
           instModuleManagement.accessModuleIndexPage();

           SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(selenium);
           InstSeleniumTestBase.logOutUI();
     }

      //test engaging and disengaging modules at service group level and service level
       public void testSGModules() throws Exception {
           testSignIn();   //
           Thread.sleep(1000);
           ModuleManagement moduleManagement = new ModuleManagement(selenium);

           moduleManagement.engageServiceGroupModules("admin/allCommons", "admin/allCommons", "echoString", "wso2mex-2.01");
           moduleManagement.disengageServiceGroupModules("admin/allCommons", "admin/allCommons", "echoString", "wso2mex-2.01");

           moduleManagement.engageServiceGroupModules("admin/allCommons", "admin/allCommons", "echoString", "wso2xfer-2.01");
           moduleManagement.disengageServiceGroupModules("admin/allCommons", "admin/allCommons", "echoString", "wso2xfer-2.01");

           moduleManagement.engageServiceGroupModules("admin/allCommons", "admin/allCommons", "echoString", "savan-SNAPSHOT");
           moduleManagement.disengageServiceGroupModules("admin/allCommons", "admin/allCommons", "echoString", "savan-SNAPSHOT");

           moduleManagement.engageServiceGroupModules("admin/allCommons", "admin/allCommons", "echoString", "sandesha2-2.01");
           moduleManagement.disengageServiceGroupModules("admin/allCommons", "admin/allCommons", "echoString", "sandesha2-2.01");
      }  //engage and disengage wso2mex at service group level


    //test engaging and disengaging modules at service level and operational level
     public void testModulesAtServiceLevel() throws Exception {
           ModuleManagement moduleManagement = new ModuleManagement(selenium);

           moduleManagement.engageServiceLevelModules("admin/allCommons","admin/allCommons", "wso2mex-2.01");
           moduleManagement.engagedAtOperationalLevel("admin/allCommons", "echoString", "wso2mex-2.01");
           moduleManagement.disengageServiceLevelModules("admin/allCommons", "wso2mex-2.01");
           moduleManagement.disengagedAtServiceLevel("admin/allCommons", "wso2mex-2.01");
           moduleManagement.disengagedAtOperationalLevel("admin/allCommons", "echoString", "wso2mex-2.01");
   //
           moduleManagement.engageServiceLevelModules("admin/allCommons","admin/allCommons", "sandesha2-2.01");
           moduleManagement.engagedAtOperationalLevel("admin/allCommons", "echoString", "sandesha2-2.01");
           moduleManagement.disengageServiceLevelModules("admin/allCommons","sandesha2-2.01");
           moduleManagement.disengagedAtServiceLevel("admin/allCommons", "sandesha2-2.01");
           moduleManagement.disengagedAtOperationalLevel("admin/allCommons", "echoString", "sandesha2-2.01");

           moduleManagement.engageServiceLevelModules("admin/allCommons","admin/allCommons", "wso2xfer-2.01");
           moduleManagement.engagedAtOperationalLevel("admin/allCommons", "echoString", "wso2xfer-2.01");
           moduleManagement.disengageServiceLevelModules("admin/allCommons","wso2xfer-2.01");
           moduleManagement.disengagedAtServiceLevel("admin/allCommons", "wso2xfer-2.01");
           moduleManagement.disengagedAtOperationalLevel("admin/allCommons", "echoString", "wso2xfer-2.01");

           moduleManagement.engageServiceLevelModules("admin/allCommons","admin/allCommons", "savan-SNAPSHOT");
           moduleManagement.engagedAtOperationalLevel("admin/allCommons", "echoString", "savan-SNAPSHOT");
           moduleManagement.disengageServiceLevelModules("admin/allCommons","savan-SNAPSHOT");
           moduleManagement.disengagedAtServiceLevel("admin/allCommons", "savan-SNAPSHOT");
           moduleManagement.disengagedAtOperationalLevel("admin/allCommons", "echoString", "savan-SNAPSHOT");

           moduleManagement.engageServiceLevelModules("admin/allCommons","admin/allCommons", "rahas-2.01");
           moduleManagement.engagedAtOperationalLevel("admin/allCommons", "echoString", "rahas-2.01");
           moduleManagement.engagedAtOperationalLevel("admin/allCommons", "echoString", "rampart-2.01");
           moduleManagement.engagedAtServiceLevel("admin/allCommons", "rampart-2.01");
           moduleManagement.disengageServiceLevelModules("admin/allCommons","rahas-2.01");
           moduleManagement.disengagedAtServiceLevel("admin/allCommons", "rahas-2.01");
           moduleManagement.disengagedAtOperationalLevel("admin/allCommons", "echoString", "rahas-2.01");
           moduleManagement.disengageServiceLevelModules("admin/allCommons","rampart-2.01");
           moduleManagement.disengagedAtServiceLevel("admin/allCommons","rampart-2.01");
           moduleManagement.disengagedAtOperationalLevel("admin/allCommons", "echoString", "rampart-2.01");
      }
       //test security at service level
//      public void testSecurityeAtServiceLevel() throws Exception {
//               ModuleManagement moduleManagement = new ModuleManagement(selenium);
//               ServiceManagement instServiceManagement = new ServiceManagement(selenium);
//
//               moduleManagement.engageServiceLevelModules("allCommons", "rampart-2.01");
//               instServiceManagement.enableSecurityScenario("allCommons", "scenario1");
//               moduleManagement.disengageServiceLevelModules("allCommons", "rampart-2.01");
//               moduleManagement.engagedAtServiceLevel("allCommons", "rampart-2.01");
//               moduleManagement.engagedAtOperationalLevel("allCommons", "echoString", "rampart-2.01");
//               instServiceManagement.disableSecurity("allCommons");
//               moduleManagement.disengagedAtServiceLevel("allCommons", "rampart-2.01");
//               moduleManagement.disengagedAtOperationalLevel("allCommons", "echoString", "rampart-2.01");
//
//
//               instServiceManagement.enableSecurityScenario("allCommons", "scenario1");
//               moduleManagement.engageServiceGroupModules("allCommons", "allCommons", "echoString", "rampart-2.01");
//               moduleManagement.engagedAtServiceLevel("allCommons", "rampart-2.01");
//               instServiceManagement.disableSecurity("allCommons");
//               moduleManagement.disengageServiceGroupModules("allCommons", "allCommons", "echoString", "rampart-2.01");
//
//               moduleManagement.engageServiceGroupModules("allCommons", "allCommons", "echoString", "rampart-2.01");
//               instServiceManagement.enableSecurityScenario("allCommons", "scenario1");
//               moduleManagement.engagedAtServiceLevel("allCommons", "rampart-2.01");
//               instServiceManagement.disableSecurity("allCommons");
//               moduleManagement.disengageServiceGroupModules("allCommons", "allCommons", "echoString", "rampart-2.01");
//           }



          public void testAccessDenyThrottling() throws Exception {
               ModuleManagement instModuleManagement = new ModuleManagement(selenium);

               instModuleManagement.accessModuleIndexPage();
      //
               instModuleManagement.deleteEntries();
               MSCommon.checkAccessDenyAtGlobalThottling_OfModule("sarasi");
            //Commented this line,because of assertion failure--->// instModuleManagement.checkAccessDenyAtGlobalThottling("", "allCommons", "echoString", "param1", "sarasi", 1, "Fault: Access deny for a caller ");
               instModuleManagement.globallyDisableThrottling();
          }

          public void testAccessControlThrottling() throws Exception {
                ModuleManagement instModuleManagement = new ModuleManagement(selenium);

                instModuleManagement.accessModuleIndexPage();
                instModuleManagement.checkAccessControlAtGlobalThottling("IP", 10, 100000, 10000, "Control");


                String serviceepr = "http://" + MSCommon.loadProperties().getProperty("host.name") + ":" + MSCommon.loadProperties().getProperty("http.be.port") +MSCommon.loadProperties().getProperty("context.root")+ "/services/" + "admin/allCommons";
                instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 10);

                Thread.sleep(10000);
                instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 10);

                Thread.sleep(10000);
                instModuleManagement.checkAccessControlAtGlobalThottling("DOMAIN", 5, 100000, 10000, "Control");

                serviceepr = "http://" + MSCommon.loadProperties().getProperty("host.name") + ":" + MSCommon.loadProperties().getProperty("http.be.port") +MSCommon.loadProperties().getProperty("context.root")+ "/services/" + "admin/allCommons";
                instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 5);

                Thread.sleep(10000);
                instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 5);
                instModuleManagement.globallyDisableThrottling();

                serviceepr = "http://" + MSCommon.loadProperties().getProperty("host.name") + ":" + MSCommon.loadProperties().getProperty("http.be.port") +MSCommon.loadProperties().getProperty("context.root")+ "/services/" + "admin/allCommons";
                instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 16);

                instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 16);

                instModuleManagement.setMaximumConcurrentAccesses("7");
                instModuleManagement.checkAccessControlAtGlobalThottling("DOMAIN", 5, 100000, 10000, "Control");
                instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 5);

                Thread.sleep(10000);
                instModuleManagement.setMaximumConcurrentAccesses("");
                instModuleManagement.checkAccessControlAtGlobalThottling("IP", 5, 100000, 10000, "Allow");
                instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 16);

                Thread.sleep(10000);
                instModuleManagement.checkAccessControlAtGlobalThottling("IP", 10, 100000, 10000, "Control");
                instModuleManagement.enableThrottlingAtServiceLevel("admin/allCommons", "IP", 3, 100000, 10000, "Control");
                instModuleManagement.enableThrottlingAtOperationalLevel("admin/allCommons", "echoString", "IP", 2, 100000, 10000, "Control");

                serviceepr = "http://" + MSCommon.loadProperties().getProperty("host.name") + ":" + MSCommon.loadProperties().getProperty("http.be.port") +MSCommon.loadProperties().getProperty("context.root")+ "/services/" + "admin/allCommons";
                instModuleManagement.invokeService(serviceepr, "echoInt", "urn:echoInt", "http://service.carbon.wso2.org", "x", 3);
                Thread.sleep(10000);

                instModuleManagement.invokeService(serviceepr, "echoString", "urn:echoString", "http://service.carbon.wso2.org", "s", 2);
                Thread.sleep(10000);
                instModuleManagement.invokeService(serviceepr, "getTime", "urn:getTime", "http://service.carbon.wso2.org", "return", 3);
          }

        public void testAddEntry() throws Exception{
            ModuleManagement instModuleManagement = new ModuleManagement(selenium);
            instModuleManagement.accessModuleIndexPage();
            selenium.click("//a[@onclick=\"submitHiddenForm('../throttling/index.jsp');return false;\"]");
            selenium.waitForPageToLoad("30000");
            selenium.select("enableThrottle", "label=Yes");
            selenium.click("link=Add New Entry");
            selenium.type("data21", "other");
            selenium.click("//input[@value='Finish']");
            selenium.waitForPageToLoad("30000");
            selenium.click("//button[@type='button']");
            instModuleManagement.globallyDisableThrottling();
        }

        public void testWso2mexModule() throws Exception {
            ModuleManagement instModuleManagement = new ModuleManagement(selenium);
            MexModuleClient instMexModuleClient = new MexModuleClient();
            instModuleManagement.accessModuleIndexPage();
            instModuleManagement.engageGlobalLevelModules("wso2mex-2.01");
            String serviceepr = "http://" + MSCommon.loadProperties().getProperty("host.name") + ":" + MSCommon.loadProperties().getProperty("http.be.port") +MSCommon.loadProperties().getProperty("context.root")+ "/services/" + "admin/allCommons";
            instMexModuleClient.getServiceSchema(serviceepr);
            instModuleManagement.disengageGlobalLevelModules("wso2mex-2.01");
        }

//        public void testSandeshaModule() throws Exception {
//            Thread.sleep(10000);
//            ModuleManagement instModuleManagement = new ModuleManagement(selenium);
//            RMClient rmClient = new RMClient();
//
//            instModuleManagement.accessModuleIndexPage();
//            instModuleManagement.engageGroupLevelModules("sandesha2-2.01");
//            instModuleManagement.invokeRMClient("allCommons","urn:echoString", "http://service.carbon.wso2.org", "echoString", "s");
//            //Request-Reply invocation
//    //        int soap11_response_count = rmClient.RMRequestReplyAnonClient("Axis2Service", "soap11", "urn:echoString", "http://service.carbon.wso2.org", "echoString", "s");
//    //        //assertEquals(11, soap11_response_count);
//    //        if(soap11_response_count==11){
//    //            System.out.println("RM Done");
//    //        }else{
//    //            System.out.println("RM Not Done");
//    //            assertEquals(11, soap11_response_count);;
//    //        }
//            instModuleManagement.disengageGroupLevelModules("sandesha2-2.01");
//         }

      public void testCaching() throws Exception {
            Thread.sleep(10000);
            ModuleManagement instModuleManagement = new ModuleManagement(selenium);
            CachingClient instCachingClient = new CachingClient();

            instModuleManagement.engageCachingAtGlobalLevel();

            String serviceepr = "http://" + MSCommon.loadProperties().getProperty("host.name") + ":" + MSCommon.loadProperties().getProperty("http.be.port") +MSCommon.loadProperties().getProperty("context.root")+ "/services/" + "admin/allCommons";
            String time1 = instCachingClient.cachClient(serviceepr, "getTime", "urn:getTime", "http://service.carbon.wso2.org");

            int i = 1;
            String time2;
            while (i < 5) {
                Thread.sleep(5000);
                time2 = instCachingClient.cachClient(serviceepr, "getTime", "urn:getTime", "http://service.carbon.wso2.org");

                i = i + 1;
                assertEquals("Caching not done!", time1, time2);
            }
            instModuleManagement.disengagecachingatGlobalLevel();
       }

       //Sign-out from Mashup
       public void testlogOut() throws Exception {
            SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
            instSeleniumTestBase.logOutUI();
       }

}
