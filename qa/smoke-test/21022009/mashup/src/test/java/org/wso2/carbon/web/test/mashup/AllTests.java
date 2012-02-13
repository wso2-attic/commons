package org.wso2.carbon.web.test.mashup;

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
        //  suite.addTestSuite(UserManagement.class);
        //   suite.addTestSuite(ServiceUpload.class);
        suite.addTestSuite(NewService.class);

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
            