/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/

package org.wso2.carbon.registry.metadata.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.registry.metadata.test.policy.PolicyAddTest;
import org.wso2.carbon.registry.metadata.test.policy.PolicyDeleteTest;
import org.wso2.carbon.registry.metadata.test.policy.PolicyMetadataTest;
import org.wso2.carbon.registry.metadata.test.schema.SchemaAddMetadataTest;
import org.wso2.carbon.registry.metadata.test.schema.SchemaAddTest;
import org.wso2.carbon.registry.metadata.test.schema.SchemaValidateTest;
import org.wso2.carbon.registry.metadata.test.service.ServiceAddTest;
import org.wso2.carbon.registry.metadata.test.service.ServiceEditTest;
import org.wso2.carbon.registry.metadata.test.service.ServiceGetConfigurationTest;
import org.wso2.carbon.registry.metadata.test.wsdl.CommunityFeatureTest;
import org.wso2.carbon.registry.metadata.test.wsdl.WSDLAdd;
import org.wso2.carbon.registry.metadata.test.wsdl.WSDLValidation;

public class TestRunner extends TestSuite {

    public static Test suite() throws Exception {
        FrameworkSettings.getProperty();
        String frameworkPath = FrameworkSettings.getFrameworkPath();
        System.setProperty("java.util.logging.config.file", frameworkPath + "/lib/log4j.properties");
        TestSuite testSuite = new TestSuite();

        /**
         * Schema tests
         */
        testSuite.addTestSuite(SchemaAddTest.class);
        testSuite.addTestSuite(SchemaValidateTest.class);
        testSuite.addTestSuite(SchemaAddMetadataTest.class);

        /**
         * Policy tests
         */
        testSuite.addTestSuite(PolicyAddTest.class);
        testSuite.addTestSuite(PolicyMetadataTest.class);
        testSuite.addTestSuite(PolicyDeleteTest.class);

        /**
         * WSDL tests
         */
        testSuite.addTestSuite(WSDLAdd.class);
        testSuite.addTestSuite(WSDLValidation.class);
        testSuite.addTestSuite(CommunityFeatureTest.class);

        /**
         * Service tests
         */
        testSuite.addTestSuite(ServiceAddTest.class);
        testSuite.addTestSuite(ServiceEditTest.class);
        testSuite.addTestSuite(ServiceGetConfigurationTest.class);

        return testSuite;
    }
}
