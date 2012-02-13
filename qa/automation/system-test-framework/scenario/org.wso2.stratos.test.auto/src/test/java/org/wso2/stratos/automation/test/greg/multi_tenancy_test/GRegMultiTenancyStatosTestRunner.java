package org.wso2.stratos.automation.test.greg.multi_tenancy_test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;


public class GRegMultiTenancyStatosTestRunner extends TestSuite {

    public static Test suite() {
        TestSuite testSuite = new TestSuite();
        if (FrameworkSettings.getStratosTestStatus()) {
            testSuite.addTestSuite(GarfileUploadServiceTestClient.class);
            testSuite.addTestSuite(LifeCycleComparisonServiceTestClient.class);
            testSuite.addTestSuite(PolicyUploadServiceTestClient.class);
            testSuite.addTestSuite(ResourceHandlingServiceTestClient.class);
            testSuite.addTestSuite(SchemaUploadServiceTestClient.class);
            testSuite.addTestSuite(WsdlImportServiceTestClient.class);
        }
        return testSuite;
    }
}
