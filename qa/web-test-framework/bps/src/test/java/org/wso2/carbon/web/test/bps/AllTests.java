package org.wso2.carbon.web.test.bps;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.textui.TestRunner;
import junit.extensions.TestSetup;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Charitha K
 * Date: Mar 10, 2009
 * Time: 12:28:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class AllTests extends TestCase {

    public AllTests(String name) {
        super(name);
    }

    public static void main(String[] args) {
        try {
            TestRunner.run(suite());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static Test suite() throws Exception {
        // TestSuite suite = new AllTests("BPS Tests");
        TestSuite suite = new TestSuite();
//<<<<<<< .mine
        suite.addTestSuite(BPSUserManagementTest.class);
        //suite.addTestSuite(BPSLoginPageTest.class);
         
        suite.addTestSuite(BPSFileUploadTest.class);
        suite.addTestSuite(BPSInstanceCreationTest.class);


        suite.addTestSuite(BPSProcessManagementTest.class);
        suite.addTestSuite(BPSInstanceManagementTest.class);
//=======
        //suite.addTestSuite(BPSUserManagementTest.class);
      // suite.addTestSuite(BPSLoginPageTest.class);
       //suite.addTestSuite(BPSFileUploadTest.class);
//        Thread.sleep(50000);
       //suite.addTest(new BPSInstanceCreationTest("testHelloWorldInstanceCreation"));
       //suite.addTestSuite(BPSProcessManagementTest.class);
       //suite.addTestSuite(BPSInstanceManagementTest.class);
       //suite.addTest(new BPSInstanceCreationTest("testCounterInstanceCreation"));
       //suite.addTestSuite(BPSInstanceManagementTest.class);
       //suite.addTestSuite(BPSTryitTest.class);
//
//>>>>>>> .r42235
        suite.addTestSuite(BPSServiceManagementTest.class);
//<<<<<<< .mine
        suite.addTestSuite(BPSPackageManagementTest.class);
//=======
  // suite.addTestSuite(BPSKeystoreManagement.class);
        //suite.addTestSuite(BPSDataSourceManagement.class);
       // suite.addTestSuite(BPSServerAdministration.class);
  //suite.addTestSuite(BPSPackageManagementTest.class);
//>>>>>>> .r42235


        //return suite;

        TestSetup wrapper = new TestSetup(suite) {

            protected void setUp() throws IOException {
                oneTimeSetUp();
            }

            private void oneTimeSetUp() throws IOException {
                //Settings.loadProperties();
                //PrepareDatabase.blowAwayDatabase();
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
