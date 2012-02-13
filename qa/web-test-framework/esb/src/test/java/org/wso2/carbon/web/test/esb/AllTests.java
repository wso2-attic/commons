package org.wso2.carbon.web.test.esb;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.textui.TestRunner;
import junit.extensions.TestSetup;

import java.util.Enumeration;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class AllTests extends TestSuite {

     public AllTests(String name) {
        super(name);
  }

  public static void main(String[] args) {
    try {
        TestRunner.run(suite());
    } catch (Exception e) {
        e.printStackTrace();
    }
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();

    String testName="";
    Properties sysprops = System .getProperties();

    for ( Enumeration e = sysprops.propertyNames(); e.hasMoreElements(); ){

        String key = (String)e.nextElement();

        if(key.equals("test.suite")){
             testName=System.getProperty("test.suite");
        }
    }

    /*
      This set of tests will verify functions which is under Home > Configure
    */
    if(testName.equalsIgnoreCase("UserManagementTest") || testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Configure")){
        suite.addTestSuite(ESBUserManagementTest.class);
    }
    if(testName.equalsIgnoreCase("ESBKeyStoreManagementTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Configure")){
           suite.addTestSuite(ESBKeyStoreManagementTest.class);
    }
    if(testName.equalsIgnoreCase("ESBLoggingConfigTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Configure")){
           suite.addTestSuite(ESBLoggingConfigTest.class);
    }
    if(testName.equalsIgnoreCase("ESBEventSourceTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Configure")){
           suite.addTestSuite(ESBEventSourceTest.class);
    }
    if(testName.equalsIgnoreCase("ESBManageSynapseConfigurationMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Configure")){
           suite.addTestSuite(ESBManageSynapseConfigurationMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBScheduleTasksMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Configure")){
           suite.addTestSuite(ESBScheduleTasksMainTest.class);
    }
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
      This set of tests will verify functions which is under Home > Manage > Services
    */
      if(testName.equalsIgnoreCase("ESBAddInlineWsdlAnonSeqProxyServiceTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Services")){
            suite.addTestSuite(ESBAddInlineWsdlAnonSeqProxyServiceTest.class);
      }
      if(testName.equalsIgnoreCase("ESBAddInlineWsdlProxyServiceTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Services")){
            suite.addTestSuite(ESBAddInlineWsdlProxyServiceTest.class);
      }
      if(testName.equalsIgnoreCase("ESBAddNonWsdlAnonSeqProxyServiceTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Services")){
            suite.addTestSuite(ESBAddNonWsdlAnonSeqProxyServiceTest.class);
      }
      if(testName.equalsIgnoreCase("ESBAddNonWsdlProxyServiceTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Services")){
            suite.addTestSuite(ESBAddNonWsdlProxyServiceTest.class);
      }
      if(testName.equalsIgnoreCase("ESBAddRegistrySourceWsdlProxyServiceTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Services")){
            suite.addTestSuite(ESBAddRegistrySourceWsdlProxyServiceTest.class);
      }
      if(testName.equalsIgnoreCase("ESBAddSourceUrlWsdlAnonSeqProxyServiceTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Services")){
            suite.addTestSuite(ESBAddSourceUrlWsdlAnonSeqProxyServiceTest.class);
      }
      if(testName.equalsIgnoreCase("ESBAddSourceUrlWsdlProxyServiceTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Services")){
            suite.addTestSuite(ESBAddSourceUrlWsdlProxyServiceTest.class);
      }
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

      /*
        This set of tests will create sequences which are in the sample guide
      */
      if(testName.equalsIgnoreCase("ESBSample1Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample1Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample2Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample2Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample3Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample3Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample4Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample4Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample5Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample5Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample6Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample6Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample7Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample7Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample8Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample8Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample12Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample12Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample50Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample50Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample56Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample56Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample100Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample100Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample101Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample101Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample151Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample151Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample152Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample152Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample153Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample153Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample200Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample200Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample201Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample201Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample250Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample250Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample251Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample251Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample300Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample300Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample350Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample350Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample351Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample351Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample353Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample353Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample354Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample354Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample362Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample362Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample380st")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample380Test.class);
      }
      if(testName.equalsIgnoreCase("ESBSample420Test")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("samples")){
            suite.addTestSuite(ESBSample420Test.class);
      }


  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


     //Local Entries
     if(testName.equalsIgnoreCase("ESBManageLocalEntriesMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("LocalEntries")){
          suite.addTestSuite(ESBManageLocalEntriesMainTest.class);
    }
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Endpoints
    if(testName.equalsIgnoreCase("ESBAddEndpointMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Endpoints")){
          suite.addTestSuite(ESBAddEndpointMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBGeneralAddressEndpointTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Endpoints")){
          suite.addTestSuite(ESBGeneralAddressEndpointTest.class);
    }
    if(testName.equalsIgnoreCase("ESBSecuredAddressEndpointTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Endpoints")){
          suite.addTestSuite(ESBSecuredAddressEndpointTest.class);
    }
    if(testName.equalsIgnoreCase("ESBWSDLEndpointTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Endpoints")){
          suite.addTestSuite(ESBWSDLEndpointTest.class);
    }
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Sequences
    if(testName.equalsIgnoreCase("ESBGenerarlSequenceScenariosTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Sequences")){
          suite.addTestSuite(ESBGenerarlSequenceScenariosTest.class);
    }
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
        This set of tests will verify functions which is under Home > Manage > Modules
         */

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
        This set of tests will verify functions which is under Home > Manage
         */

 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
        This set of tests will verify functions which is under Home > Registry
         */

 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
        This set of tests will verify functions which is under Home > Monitor
    */
    if(testName.equalsIgnoreCase("ESBMessageMediationStatisticsMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Monitor")){
          suite.addTestSuite(ESBMessageMediationStatisticsMainTest.class);
    }
 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
        This set of tests will verify the UI of the Login page and the Home page
    */
    if(testName.equalsIgnoreCase("ESBLoginPage")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Login_Home")){
          suite.addTestSuite(ESBLoginPage.class);
    }
    if(testName.equalsIgnoreCase("ESBHomePage")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Login_Home")){
          suite.addTestSuite(ESBHomePage.class);
    }
 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
      This set of tests will verify functions which is under Home > Manage > Mediation
    */
    //Mediators
    if(testName.equalsIgnoreCase("ESBCoreMediatorTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBCoreMediatorTest.class);
    }
    if(testName.equalsIgnoreCase("ESBTransformMediatorsTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBTransformMediatorsTest.class);
    }
    if(testName.equalsIgnoreCase("ESBExtensionMediatorsTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBExtensionMediatorsTest.class);
    }
    if(testName.equalsIgnoreCase("ESBFilterMediatorsTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBFilterMediatorsTest.class);
    }
    if(testName.equalsIgnoreCase("ESBInMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBInMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBFilterMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBFilterMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBOutMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBOutMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBScriptMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBScriptMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBSpringMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBSpringMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBClassMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBClassMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBSendMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBSendMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBDropMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBDropMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBPropertyMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBPropertyMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBLogMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBLogMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBSequenceMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBSequenceMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBEventMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBEventMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBAggregateMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBAggregateMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBCacheMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBCacheMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBIterateMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
            suite.addTestSuite(ESBIterateMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBCalloutMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBCalloutMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBCloneMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBCloneMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBTransactionMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBTransactionMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBRMSequenceMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBRMSequenceMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBValidateMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBValidateMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBRouterMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBRouterMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBThrottleMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBThrottleMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBSwitchMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBSwitchMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBFaultMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBFaultMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBHeaderMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBHeaderMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBXSLTMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBXSLTMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBXqueryMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBXqueryMediatorMainTest.class);
    }
    if(testName.equalsIgnoreCase("ESBDBReportMediatorMainTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("Mediators")){
          suite.addTestSuite(ESBDBReportMediatorMainTest.class);
    }

      /*
      Jira Issues
       */
    if(testName.equalsIgnoreCase("ESBEndPointsInRegistryTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("CustomerIssues")){
        suite.addTestSuite(ESBEndPointsInRegistryTest.class);
    }
    if(testName.equalsIgnoreCase("ESBLocalEndpointsTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("CustomerIssues")){
        suite.addTestSuite(ESBLocalEndpointsTest.class);
    }
    if(testName.equalsIgnoreCase("ESBProxyServiceIssueTest")|| testName.equalsIgnoreCase("all") || testName.equalsIgnoreCase("CustomerIssues")){
        suite.addTestSuite(ESBProxyServiceIssueTest.class);
    }


    TestSetup wrapper = new TestSetup(suite) {

    protected void setUp() throws Exception {
    oneTimeSetUp();
    }

    private void oneTimeSetUp() throws Exception {
        BrowserInitializer.initBrowser();
    }

    private void oneTimeTearDown() {
    BrowserInitializer.stopBrowser();
    }

    protected void tearDown() {
    oneTimeTearDown();
    }
    };

    return wrapper;
    }
    }
