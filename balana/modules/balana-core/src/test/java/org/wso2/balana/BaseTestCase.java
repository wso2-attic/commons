/*
 * Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.wso2.balana;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.wso2.balana.basic.BasicTestV3;
import org.wso2.balana.conformance.ConformanceTestV2;

/**
 * Test suite for Balana 
 */
public class BaseTestCase extends TestSuite {

    public static Test suite() throws Exception {

        TestSuite testSuite = new TestSuite();
        // basic test of XACML version 3.0
//        testSuite.addTestSuite(BasicTestV3.class);
        // conformance test for XACML 2.0
        testSuite.addTestSuite(ConformanceTestV2.class);
        return testSuite;
    }
}
