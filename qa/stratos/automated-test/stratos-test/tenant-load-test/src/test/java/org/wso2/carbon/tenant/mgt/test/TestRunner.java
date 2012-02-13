package org.wso2.carbon.tenant.mgt.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 */
public class TestRunner extends TestSuite{

    public static Test suite(){
        TestSuite testSuite = new TestSuite();
        testSuite.addTestSuite(TenantLoadTest.class);
        return testSuite;
    }

}
