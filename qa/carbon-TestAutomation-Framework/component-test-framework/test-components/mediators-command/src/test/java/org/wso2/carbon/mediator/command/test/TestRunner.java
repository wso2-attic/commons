package org.wso2.carbon.mediator.command.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;

public class TestRunner extends TestSuite{
    public static Test suite() throws Exception {
        FrameworkSettings.getProperty();
        String frameworkPath = FrameworkSettings.getFrameworkPath();
        System.setProperty("java.util.logging.config.file", frameworkPath + "/lib/log4j.properties");
        TestSuite testSuite = new TestSuite();

        /* String testName = "";
        Properties sysProps = System.getProperties();

        for (Enumeration e = sysProps.propertyNames(); e.hasMoreElements();) {

            String key = (String) e.nextElement();

            if (key.equals("test.suite")) {
                testName = System.getProperty("test.suite");
            }
        }  */
        testSuite.addTestSuite(CmdWithPropertyTest.class);
        return testSuite;
    }
}
