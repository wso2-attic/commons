package org.wso2.carbon.web.test.is;

/*
 * Copyright 2004,2005 The Apache Software Foundation.
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

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.textui.TestRunner;
import junit.extensions.TestSetup;

import java.io.IOException;
import java.util.Properties;
import java.util.Enumeration;


public class AllTests extends TestSuite {
    public AllTests(String Name) {
        super(Name);
    }

    public static void main(String[] args) {
        try {
            TestRunner.run(suite());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();


        String testName="";
        Properties sysprops = System .getProperties();
        boolean all=true;
        for ( Enumeration e = sysprops.propertyNames(); e.hasMoreElements(); )
        {
            String key = (String)e.nextElement();

            if(key.equals("test.suite")){
                testName=System.getProperty("test.suite");
            }
            all = testName == null || testName.equalsIgnoreCase("all")  || testName.equals("${test.suite}");
        }

        System.out.println("System property "+testName);
        if (!all) {
            System.out.println("Running Test: " + testName);
        } else {
            System.out.println("Running all tests.");
        }


        if (testName.equalsIgnoreCase("ISStartUp")) {
           suite.addTestSuite(ISStartUpTest.class);

        } else if (testName.equalsIgnoreCase("usermanagement")) {
           suite.addTestSuite(ISUserManagementTest.class);

        } else if (testName.equalsIgnoreCase("CardIssuer")) {
            suite.addTestSuite(ISCardIssuerTest.class);

        } else if (testName.equalsIgnoreCase("MultifactorAuthentication")) {
            suite.addTestSuite(ISMultifactorAuthenticationTest.class);

        } else if (testName.equalsIgnoreCase("InfoCardOpenID")) {
            suite.addTestSuite(ISInfoCardOpenIDDashboardTest.class);

        } else if (testName.equalsIgnoreCase("DefaultUI")) {
            suite.addTestSuite(ISDefaultUITest.class);

        }else if (testName.equalsIgnoreCase("OpenIDUrl")) {
            suite.addTestSuite(ISOpenIDUrlTest.class);

        } else if (testName.equalsIgnoreCase("keystore")) {
            suite.addTestSuite(ISKeyStoreTest.class);

        } else if (testName.equalsIgnoreCase("logging")) {
            suite.addTestSuite(ISLoggingTest.class);

        } else if (testName.equalsIgnoreCase("RelyingParty")) {
            suite.addTestSuite(ISRelyingPartyTest.class);

        } else if (testName.equalsIgnoreCase("EntitlementPolicies")) {
            suite.addTestSuite(ISEntitlementPoliciesTest.class);

        } else if (testName.equalsIgnoreCase("MyProfile")) {
            suite.addTestSuite(ISMyProfileTest.class);

        } else if (testName.equalsIgnoreCase("Signup")) {
            suite.addTestSuite(ISSignupTest.class);

        } else if (testName.equalsIgnoreCase("ExternalUserStore")) {
            suite.addTestSuite(ExternalUserStoreTest.class);

        } else if (testName.equalsIgnoreCase("FineGrainedAuth")) {
            suite.addTestSuite(ISFineGrainedAuthTest.class);

        } else if (testName.equalsIgnoreCase("STSScenario")) {
            suite.addTestSuite(ISSTSScenario.class);

        } else if (testName.equalsIgnoreCase("ProfileManagement")) {
            suite.addTestSuite(ISProfileManagementTest.class);

        } else if (testName.equalsIgnoreCase("ClaimManagement")) {
            suite.addTestSuite(ISClaimManagementTest.class);

        } else if (all) {

            /*....................................
                     StartUp Test
            ......................................*/
            suite.addTestSuite(ISStartUpTest.class);

            /*...................................
                    User Management Test
            .................................... */
            suite.addTestSuite(ISUserManagementTest.class);

            suite.addTestSuite(ISCardIssuerTest.class);

            suite.addTestSuite(ISMultifactorAuthenticationTest.class);
            suite.addTestSuite(ISInfoCardOpenIDDashboardTest.class);

            suite.addTestSuite(ISDefaultUITest.class);


            /*....................................
                    Open ID url test
            ...................................... */
            suite.addTestSuite(ISOpenIDUrlTest.class);

            suite.addTestSuite(ISLoggingTest.class);
            suite.addTestSuite(ISKeyStoreTest.class);

            suite.addTestSuite(ISRelyingPartyTest.class);

            suite.addTestSuite(ISEntitlementPoliciesTest.class);

            suite.addTestSuite(ISMyProfileTest.class);

            suite.addTestSuite(ISSignupTest.class);

            /*....................................
                     Entitlement Client Test
            ...................................... */
            suite.addTestSuite(ISFineGrainedAuthTest.class);

            /*......................................
                    STS Client Test
            ........................................ */
            suite.addTestSuite(ISSTSScenario.class);

            /*......................................
                    External User Store Test
            ........................................ */
            suite.addTestSuite(ExternalUserStoreTest.class);

             /*......................................
                    Profile Management Test
            ........................................ */
            suite.addTestSuite(ISProfileManagementTest.class);

             /*......................................
                    Claim Management Test
            ........................................ */
            suite.addTestSuite(ISClaimManagementTest.class);

        }


        TestSetup wrapper = new TestSetup(suite) {

            protected void setUp() throws IOException {
                oneTimeSetUp();
            }

            private void oneTimeSetUp() throws IOException {
                BrowserInitializer.initbrowser();
            }

            private void oneTimeTearDown() {
                BrowserInitializer.stopbrowser();
            }

            protected void tearDown() throws Exception {
                oneTimeTearDown();
            }
        };

        return wrapper;
    }
}