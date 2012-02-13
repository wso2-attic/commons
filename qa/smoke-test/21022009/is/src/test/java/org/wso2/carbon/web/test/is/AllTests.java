package org.wso2.carbon.web.test.is;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.textui.TestRunner;
import junit.extensions.TestSetup;


public class AllTests extends TestSuite {
    public AllTests(String Name) {
        super(Name);
    }

    public static void main(String srgs[]) {
        TestRunner.run(suite());

    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(DefaultUITest.class);
        suite.addTestSuite(CardIssuerTest.class);
        suite.addTestSuite(ProfileManagementTest.class);
        suite.addTestSuite(ClaimManagementTest.class);
        suite.addTestSuite(LoggingTest.class);
        //suite.addTestSuite(UserManagementTest.class);
        //suite.addTestSuite(KeyStoreTest.class);

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

            protected void tearDown() throws Exception {
                oneTimeTearDown();
            }
        };

        return wrapper;
    }
}
            