/**
 *  Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.esb.samples;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;

import java.util.Enumeration;
import java.util.Properties;

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.BACKENDSERVER_RUNNING;

public class TestRunner extends TestSuite {

    public static Test suite() throws Exception {
        FrameworkSettings.getProperty();
        TestSuite testSuite = new TestSuite();

        String testName = "";
        Properties sysProps = System.getProperties();

        for (Enumeration e = sysProps.propertyNames(); e.hasMoreElements();) {

            String key = (String) e.nextElement();

            if (key.equals("test.suite")) {
                testName = System.getProperty("test.suite");
            }
        }
        System.out.println("Axis2 server Started : " + BACKENDSERVER_RUNNING);
        System.out.println("Running all ESB tests.");

        if (BACKENDSERVER_RUNNING) {

            testSuite.addTestSuite(Sample0.class);
            testSuite.addTestSuite(Sample1.class);
            testSuite.addTestSuite(Sample2.class);
            testSuite.addTestSuite(Sample3.class);
            testSuite.addTestSuite(Sample4.class);      //not fully tested (ESB log not checked)
            testSuite.addTestSuite(Sample5.class);
            testSuite.addTestSuite(Sample6.class);
            testSuite.addTestSuite(Sample7.class);
            testSuite.addTestSuite(Sample8.class);
            testSuite.addTestSuite(Sample9.class);
            testSuite.addTestSuite(Sample10.class);
            //testSuite.addTestSuite(Sample11.class);     //fails  for unknown reason
            //testSuite.addTestSuite(Sample12.class);     //see the results on server
            testSuite.addTestSuite(Sample13.class);   //running with sample configuration 0 & Fail case is not tested for windows
            testSuite.addTestSuite(Sample50.class);
            testSuite.addTestSuite(Sample51.class);   // Fail case is not tested for windows
            testSuite.addTestSuite(Sample56.class);
            testSuite.addTestSuite(Sample100.class);
            testSuite.addTestSuite(Sample101.class);
            testSuite.addTestSuite(Sample102.class);
            testSuite.addTestSuite(Sample150.class);
            testSuite.addTestSuite(Sample151.class);
            testSuite.addTestSuite(Sample152.class);
            testSuite.addTestSuite(Sample153.class);
            testSuite.addTestSuite(Sample155.class);
            //testSuite.addTestSuite(Sample200.class);  //this fails known issue
            testSuite.addTestSuite(Sample201.class);
            testSuite.addTestSuite(Sample202.class);
            testSuite.addTestSuite(Sample350.class);
            testSuite.addTestSuite(Sample351.class);
            testSuite.addTestSuite(Sample352.class);
            testSuite.addTestSuite(Sample353.class);
            testSuite.addTestSuite(Sample354.class);
            testSuite.addTestSuite(Sample370.class);
            testSuite.addTestSuite(Sample371.class);
            testSuite.addTestSuite(Sample372.class);
            testSuite.addTestSuite(Sample380.class);
            testSuite.addTestSuite(Sample390.class);
            testSuite.addTestSuite(Sample391.class);
            testSuite.addTestSuite(Sample400.class);
            //   testSuite.addTestSuite(Sample420.class);// Need to log SimpleStockQuote S.O.P results and check the axis2Server log
            testSuite.addTestSuite(Sample430.class);
            testSuite.addTestSuite(Sample500.class);
            testSuite.addTestSuite(Sample501.class);
            testSuite.addTestSuite(Sample502.class);
            testSuite.addTestSuite(Sample600.class);
            testSuite.addTestSuite(Sample601.class);
            testSuite.addTestSuite(Sample602.class);
            testSuite.addTestSuite(Sample603.class);
            testSuite.addTestSuite(Sample604.class);
            testSuite.addTestSuite(Sample605.class);
            testSuite.addTestSuite(Sample606.class);    //works only if the rule jar is added
            testSuite.addTestSuite(Sample650.class);    //running with sample configuration 600
        }
        return testSuite;
    }
}
