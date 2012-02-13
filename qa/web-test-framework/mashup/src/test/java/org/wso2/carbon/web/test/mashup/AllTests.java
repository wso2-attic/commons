package org.wso2.carbon.web.test.mashup;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */


import junit.framework.TestSuite;
import junit.framework.Test;
import junit.textui.TestRunner;
import junit.extensions.TestSetup;

import java.util.Properties;
import java.util.Enumeration;
import java.io.IOException;


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

        if(testName.equalsIgnoreCase("MSStartUp")){
            suite.addTestSuite(MSStartUpTest.class);

        }else if(testName.equalsIgnoreCase("usermanagement")){
            suite.addTestSuite(UserManagementTest.class);

        }else if(testName.equalsIgnoreCase("EnvSetup")){
            suite.addTestSuite(EnvSetup.class);

        }else if(testName.equalsIgnoreCase("DefaultUI")){
            suite.addTestSuite(MSDefaultUITest.class);

        }else if(testName.equalsIgnoreCase("AnnonUserPage")){
            suite.addTestSuite(MSAnnonUserPageTest.class);

        }else if(testName.equalsIgnoreCase("Tryit")){
            suite.addTestSuite(MSTryitTest.class);

        }else if(testName.equalsIgnoreCase("newservice")){
            suite.addTestSuite(MSNewServiceTest.class);

        }else if(testName.equalsIgnoreCase("servicemanagement")){
            suite.addTestSuite(MSServiceMnagementTest.class);

        }else if(testName.equalsIgnoreCase("keystore")){
            suite.addTestSuite(KeyStoreTest.class);

        }else if(testName.equalsIgnoreCase("logging")){
            suite.addTestSuite(LoggingTest.class);

        }else if(testName.equalsIgnoreCase("ScrapingAssistant")){
            suite.addTestSuite(ScrapingAssistantTest.class);

        }else if(testName.equalsIgnoreCase("JavaScriptStubGenerator")){
            suite.addTestSuite(JavaScriptStubGenerator.class);

        }else if(testName.equalsIgnoreCase("databinding")){
            suite.addTestSuite(MSDataBindingTest.class);

        }else if(testName.equalsIgnoreCase("sampleservices")){
            suite.addTestSuite(MSSamplesServicesTest.class);

        }else if(testName.equalsIgnoreCase("customUI")){
            suite.addTestSuite(MSCustomUITest.class);

        }else if(testName.equalsIgnoreCase("ScheduledTasks")){
            suite.addTestSuite(MSScheduledTasksTest.class);

        }else if(testName.equalsIgnoreCase("accessSecService")){
            suite.addTestSuite(MSaccessSecServiceTest.class);

        }else if(testName.equalsIgnoreCase("modulemanagement")){
            suite.addTestSuite(ModuleManagementTest.class);

        }else if(testName.equalsIgnoreCase("hostobject")){
            suite.addTestSuite(MSHostObjectsTest.class);
        
        }
        else if(all){

            /*..........................................
                   StartUp Tests  -  OK
             ...........................................*/
            suite.addTestSuite(MSStartUpTest.class);

            /*...........................................
                    Environment setup tests  - OK
            .............................................*/
            suite.addTestSuite(EnvSetup.class);

            /*...........................................
            
                    Annonymous user tests
            .............................................*/
            //suite.addTestSuite(MSDefaultUITest.class);
           // suite.addTestSuite(MSAnnonUserPageTest.class);

            /*...........................................
           Tryit tests for both annonymous user and signIn user
            .............................................*/
            //suite.addTestSuite(MSTryitTest.class);

            //suite.addTestSuite(MSNewServiceTest.class);

            /*..........................................
                   Service Management Tests
            ............................................ */
           // suite.addTestSuite(MSServiceMnagementTest.class);

            /*..........................................
                   User Management Tests
            ............................................*/
            //suite.addTestSuite(UserManagementTest.class);


           // suite.addTestSuite(KeyStoreTest.class);
           // suite.addTestSuite(LoggingTest.class);

            suite.addTestSuite(ScrapingAssistantTest.class);    // OK
           suite.addTestSuite(JavaScriptStubGenerator.class);  // OK

            /*............................................
                    DataBinding Tests     - OK
             .............................................*/
            suite.addTestSuite(MSDataBindingTest.class);

            /*............................................
                    Sample Services Tests
             .............................................*/
          //  suite.addTestSuite(MSSamplesServicesTest.class);

           suite.addTestSuite(MSCustomUITest.class);          // OK
          //  suite.addTestSuite(MSScheduledTasksTest.class);

          //  suite.addTestSuite(MSaccessSecServiceTest.class);

            /*............................................
                    Module Management Tests
             .............................................*/
           // suite.addTestSuite(ModuleManagementTest.class);

            /*............................................
                    Host Objects Tests   - OK
            ............................................. */
            suite.addTestSuite(MSHostObjectsTest.class);



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
