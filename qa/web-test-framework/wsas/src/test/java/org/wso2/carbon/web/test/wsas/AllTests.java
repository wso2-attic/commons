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
package org.wso2.carbon.web.test.wsas;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.textui.TestRunner;
import junit.extensions.TestSetup;

import java.util.Properties;
import java.util.Enumeration;

import org.wso2.carbon.web.test.ds.common.*;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;

public class AllTests extends TestSuite {
    public AllTests(String Name) {
        super(Name);
    }

    public static void main(String srgs[]) {
        try {
            TestRunner.run(suite());
        } catch (Exception e) {
            e.printStackTrace();
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

        if(testName.equalsIgnoreCase("StartUp")){
            suite.addTestSuite(ManageStartUpTest.class);

        }else if(testName.equalsIgnoreCase("UserManagement")){
            suite.addTestSuite(UserManagementTest.class);

        }else if(testName.equalsIgnoreCase("Axis2Service")){
            suite.addTestSuite(ManageAxis2ServicesTest.class);

        }else if(testName.equalsIgnoreCase("wsdlvalidator")){
            suite.addTestSuite(ManageWsdlValidatorTest.class);

        }else if(testName.equalsIgnoreCase("Axis1Service")){
            suite.addTestSuite(ManageAxis1ServiceTest.class);

        } else if(testName.equalsIgnoreCase("PojoService")){
            suite.addTestSuite(ManagePojoServiceTest.class);

        }else if(testName.equalsIgnoreCase("JarService")){
            suite.addTestSuite(ManageJarServiceTest.class);

        }else if(testName.equalsIgnoreCase("SpringService")){
            suite.addTestSuite(ManageSpringServiceTest.class);

        }else if(testName.equalsIgnoreCase("JaxWsService")){  //not tested
            suite.addTestSuite(ManageJaxWsServiceTest.class);

        }else if(testName.equalsIgnoreCase("EJBService")){  //not tested
            suite.addTestSuite(ManageEJBServiceTest.class);

        }else if(testName.equalsIgnoreCase("Random")){  //not tested
            suite.addTestSuite(RandomTest.class);

        }else if(testName.equalsIgnoreCase("CSVDataService")){
            suite.addTestSuite(CSVDataServiceManagementTest.class);

        }else if(testName.equalsIgnoreCase("CSVDataUploadedService")){
            suite.addTestSuite(CSVDataServiceManagementTest.class);
            suite.addTestSuite(CSVDataUploadedServiceManagement.class);

        }else if(testName.equalsIgnoreCase("EXCELDataService")){
            suite.addTestSuite(EXCELDataServiceManagementTest.class);

        }else if(testName.equalsIgnoreCase("CSVExcelMultiDataService")){
            suite.addTestSuite(CSVExcelMultiDataServiceTest .class);

        }else if(testName.equalsIgnoreCase("MySQLDataService")){
            suite.addTestSuite(RDBMSDataServiceManagementTest.class);

        }else if(testName.equalsIgnoreCase("DataSrevicesWithRoles")){
            suite.addTestSuite(DataSrevicesWithRolesTest.class);

        }else if(testName.equalsIgnoreCase("CSVMySQLMultiDataService")){
            suite.addTestSuite(CSVMySQLMultiDataServiceTest.class);

        }else if(testName.equalsIgnoreCase("StoredProcedureDataService")){
            suite.addTestSuite(StoredProcedureDataServiceManagementTest.class);

        }else if(testName.equalsIgnoreCase("RestfulDataService")){
            suite.addTestSuite( RestfulDataServiceTest.class);

        }else if(testName.equalsIgnoreCase("NestedQueryDataService")){
            suite.addTestSuite(NestedQueryDataServiceTest.class);

        }else if(testName.equalsIgnoreCase("EditDataSrevice")){
            suite.addTestSuite(EditDataSreviceTest.class);

        }else if(testName.equalsIgnoreCase("TemporaryTableDataSrevice")){
            suite.addTestSuite(TemporaryTableDataSreviceTest.class);

        }else if(testName.equalsIgnoreCase("MTOM")){
            suite.addTestSuite(MTOMTest.class);

        }else if(testName.equalsIgnoreCase("SWA")){
            suite.addTestSuite(SWATest.class);

        }else if(testName.equalsIgnoreCase("SOAPTracer")){
            suite.addTestSuite(SOAPTracerTest.class);

        }else if(testName.equalsIgnoreCase("Eventing")){
            suite.addTestSuite(EventingTest.class);

        }else if(testName.equalsIgnoreCase("STS")){
            suite.addTestSuite(STSTest.class);

        }else if(testName.equalsIgnoreCase("XKMS")){
            // suite.addTestSuite(XKMSTest.class);

        }else if(testName.equalsIgnoreCase("Monitor")){
            suite.addTestSuite(MonitorTest.class);

        }else if(testName.equalsIgnoreCase("KeyStore")){
            suite.addTestSuite(KeyStoreTest.class);

        }else if(testName.equalsIgnoreCase("Tools")){
            suite.addTestSuite(ToolsTest.class);

        }else if(testName.equalsIgnoreCase("ModuleManagement")){
            suite.addTestSuite(ModuleManagementTest.class);

        } else if(testName.equalsIgnoreCase("FaultHandling")){
            suite.addTestSuite(FaultHandlingTest.class);

        }else if(testName.equalsIgnoreCase("Transports")){
            suite.addTestSuite(TransportsTest.class);

        }else if(testName.equalsIgnoreCase("ShutdownRestartServer")){
            suite.addTestSuite(ShutdownRestartServerTest.class);

        }else if(testName.equalsIgnoreCase("PolicyPersistance")){
            suite.addTestSuite(PolicyPersistanceTest.class);

        } else if(testName.equalsIgnoreCase("RegistryBrowser")){
            suite.addTestSuite(ManageRegistryBrowserTest.class);

        }else if (testName.equalsIgnoreCase("HotUpdate"))  {
            suite.addTestSuite(HotUpdateTest.class);

        }else if (testName.equalsIgnoreCase("HotDeployment"))  {
            suite.addTestSuite( HotDeploymentTest.class);

        }else if (testName.equalsIgnoreCase("ChadSample"))  {
            suite.addTestSuite(ChadSampleTest.class);

        }else if (testName.equalsIgnoreCase("DataServiceSample"))  {
            suite.addTestSuite(DataServiceSampleTest.class);

        }

        else if(all){
            /*================================
                          StartUp Tests
            ================================*/
            suite.addTestSuite( ManageStartUpTest.class);


            /*================================
              User Management Tests
            ================================*/
            suite.addTestSuite(UserManagementTest.class);   //tested

            /*================================
            Service Management Tests. Here we test
       AAR, Pojo, Spring, jaxws and Axis1 services with QOS features
            ================================*/
            suite.addTestSuite(ManageAxis2ServicesTest.class);
            suite.addTestSuite(ManageAxis1ServiceTest.class);
            suite.addTestSuite(ManagePojoServiceTest.class);
            suite.addTestSuite(ManageJarServiceTest.class);
            suite.addTestSuite(ManageSpringServiceTest.class);
            //suite.addTestSuite(ManageJaxWsServiceTest.class);   //-- not tested
           suite.addTestSuite(RandomTest.class);

            /*================================
                           Data Service Tests
            ================================*/
            suite.addTestSuite(CSVDataServiceManagementTest.class);
            suite.addTestSuite( CSVDataUploadedServiceManagement.class);
            suite.addTestSuite(EXCELDataServiceManagementTest.class);
            suite.addTestSuite(CSVExcelMultiDataServiceTest .class);
            suite.addTestSuite(RDBMSDataServiceManagementTest.class);
            suite.addTestSuite(DataSrevicesWithRolesTest.class);
            suite.addTestSuite(CSVMySQLMultiDataServiceTest.class);
            suite.addTestSuite(StoredProcedureDataServiceManagementTest.class);
            suite.addTestSuite(RestfulDataServiceTest.class);
            suite.addTestSuite(NestedQueryDataServiceTest.class);
            suite.addTestSuite(EditDataSreviceTest.class);
            suite.addTestSuite(TemporaryTableDataSreviceTest.class);


            /*================================
                        Module Management Tests
            ================================*/
            suite.addTestSuite(ModuleManagementTest.class);

            /*================================
                        Sample Tests
                        ================================*/
             suite.addTestSuite(ChadSampleTest.class);
             suite.addTestSuite(DataServiceSampleTest.class);


            /*================================
                                  Other tests
            ================================*/
            suite.addTestSuite(MTOMTest.class);
            suite.addTestSuite(SWATest.class);
            suite.addTestSuite(SOAPTracerTest.class);
            suite.addTestSuite(EventingTest.class);
            suite.addTestSuite(STSTest.class);
            suite.addTestSuite(MonitorTest.class);
            suite.addTestSuite(KeyStoreTest.class);
            suite.addTestSuite(ToolsTest.class);
            suite.addTestSuite(TransportsTest.class);
            suite.addTestSuite(ManageRegistryBrowserTest.class);

            suite.addTestSuite(ShutdownRestartServerTest.class);   //server fails while running these 2
            suite.addTestSuite( PolicyPersistanceTest.class);
            suite.addTestSuite(XKMSTest.class);
            suite.addTestSuite(FaultHandlingTest.class);
            suite.addTestSuite(HotUpdateTest.class);
            suite.addTestSuite( HotDeploymentTest.class);

        }


         
        TestSetup wrapper = new TestSetup(suite) {

            protected void setUp() throws Exception {
                oneTimeSetUp();
            }

            private void oneTimeSetUp() throws Exception {
                //Settings.loadProperties();
                //PrepareDatabase.blowAwayDatabase();
                BrowserInitializer.initProperty();
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
            
