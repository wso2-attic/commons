package org.wso2.carbon.web.test.bps;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.textui.TestRunner;
import junit.extensions.TestSetup;

/**
 * Created by IntelliJ IDEA.
 * User: Charitha K
 * Date: Mar 10, 2009
 * Time: 12:28:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class AllTests extends TestSuite {

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
        //suite.addTestSuite(BPSUserManagementTest.class);
        suite.addTestSuite(BPSLoginPageTest.class);
        suite.addTestSuite(BPSFileUploadTest.class);
        Thread.sleep(50000);
        suite.addTestSuite(BPSInstanceCreationTest.class);
        suite.addTestSuite(BPSProcessManagementTest.class);
        suite.addTestSuite(BPSInstanceManagementTest.class);
        suite.addTestSuite(BPSServiceManagementTest.class);


        //return suite;

        TestSetup wrapper = new TestSetup(suite) {

            protected void setUp() {
                oneTimeSetUp();
            }

            private void oneTimeSetUp() {
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
