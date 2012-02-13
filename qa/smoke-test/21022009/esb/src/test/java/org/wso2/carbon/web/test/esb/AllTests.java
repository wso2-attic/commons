package org.wso2.carbon.web.test.esb;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.textui.TestRunner;
import junit.extensions.TestSetup;


public class AllTests extends TestSuite {

     public AllTests(String name) {
        super(name);
  }

  public static void main(String[] args) {
    TestRunner.run(suite());
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();

  /*
  This set of tests will verify functions which is under Home > Configure
   */
  suite.addTestSuite(ESBUserManagementMainTest.class);
  suite.addTestSuite(ESBKeyStoreManagementMainTest.class);
  suite.addTestSuite(ESBLoggingConfigMainTest.class);
//  suite.addTestSuite(ESBDatasourceManagementMainTest.class);
  suite.addTestSuite(ESBEventSourceMainTest.class);
  suite.addTestSuite(ESBScheduleTasksMainTest.class);
  suite.addTestSuite(ESBManageSynapseConfigurationMainTest.class);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /*
  This set of tests will verify functions which is under Home > Manage > Services
   */
//  suite.addTestSuite(ESBServiceManagementMainTest.class);
  suite.addTestSuite(ESBAddInlineWsdlAnonSeqProxyServiceTest.class);
  suite.addTestSuite(ESBAddInlineWsdlProxyServiceTest.class);
  suite.addTestSuite(ESBAddNonWsdlAnonSeqProxyServiceTest.class);
  suite.addTestSuite(ESBAddNonWsdlProxyServiceTest.class);
  suite.addTestSuite(ESBAddRegistrySourceWsdlProxyServiceTest.class);
  suite.addTestSuite(ESBAddSourceUrlWsdlAnonSeqProxyServiceTest.class);
  suite.addTestSuite(ESBAddSourceUrlWsdlProxyServiceTest.class);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /*
  This set of tests will verify functions which is under Home > Manage > Mediation
   */

  //Mediators
  suite.addTestSuite(ESBLogMediatorMainTest.class);
//  suite.addTestSuite(ESBFilterMediatorMainTest.class);
  suite.addTestSuite(ESBPropertyMediatorMainTest.class);
//  suite.addTestSuite(ESBScriptMediatorMainTest.class);
//  suite.addTestSuite(ESBSendMediatorMainTest.class);
//  suite.addTestSuite(ESBSpringMediatorMainTest.class);
//  suite.addTestSuite(ESBClassMediatorMainTest.class);
//  suite.addTestSuite(ESBCommandMediatorMainTest.class);
  suite.addTestSuite(ESBDropMediatorMainTest.class);
  suite.addTestSuite(ESBEventMediatorMainTest.class);
//  suite.addTestSuite(ESBAggregateMediatorMainTest.class);
  suite.addTestSuite(ESBCacheMediatorMainTest.class);
  suite.addTestSuite(ESBCalloutMediatorMainTest.class);
//  suite.addTestSuite(ESBCloneMediatorMainTest.class);
  suite.addTestSuite(ESBTransactionMediatorMainTest.class);
  suite.addTestSuite(ESBRMSequenceMediatorMainTest.class);
//  suite.addTestSuite(ESBValidateMediatorMainTest.class);
//  suite.addTestSuite(ESBRouterMediatorMainTest.class);
//  suite.addTestSuite(ESBThrottleMediatorMainTest.class);
//  suite.addTestSuite(ESBSwitchMediatorMainTest.class);
  suite.addTestSuite(ESBFaultMediatorMainTest.class);
  suite.addTestSuite(ESBHeaderMediatorMainTest.class);
  suite.addTestSuite(ESBXSLTMediatorMainTest.class);
  suite.addTestSuite(ESBXqueryMediatorMainTest.class);
  suite.addTestSuite(ESBDBReportMediatorMainTest.class);





  //Local Entries
  suite.addTestSuite(ESBManageLocalEntriesMainTest.class);

  //Endpoints
  suite.addTestSuite(ESBAddEndpointMainTest.class);

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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /*
  This set of tests will verify functions which is under Home > Monitor
   */
  suite.addTestSuite(ESBMessageMediationStatisticsMainTest.class);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /*
  This set of tests will verify the UI of the Login page and the Home page
   */
  suite.addTestSuite(ESBLoginPage.class);
  suite.addTestSuite(ESBHomePage.class);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /*
  This set of tests will create sequences which are in the sample guide
   */
//  suite.addTestSuite(ESBSample1TestClass.class);
//  suite.addTestSuite(SampleClient.class);
//  suite.addTestSuite(ESBSample2Test.class);
//  suite.addTestSuite(ESBSample3Test.class);

//  suite.addTestSuite(ESBStartSimpleAxis2ServerMainTest.class);
// return suite;


    TestSetup wrapper = new TestSetup(suite) {

    protected void setUp() {
    oneTimeSetUp();
    }

    private void oneTimeSetUp() {
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
