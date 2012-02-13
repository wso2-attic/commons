package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

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

public class ESBDBReportMediatorMainTest extends CommonSetup {

    public ESBDBReportMediatorMainTest(String text) {
        super(text);
    }

    //This method will test adding a new Fault mediator
    public void testaddXSLTMediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("admin","admin");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.addSequence("sequence_dbreport");

        //Adding the first DBreport mediator - pool connection
        esbCommon.addRootLevelChildren("Add Child","Transform","DBReport");

        ESBDBReportMediatorUITest esbdbReportMediatorUITest = new ESBDBReportMediatorUITest(selenium);
        //esbdbReportMediatorUITest.testVerifyDBreportMediator("0");

        ESBDBReportMediatorTest esbdbReportMediatorTest = new ESBDBReportMediatorTest(selenium);
        esbdbReportMediatorTest.addDBreportMediatorPoolInfo("0","org.apache.derby.jdbc.ClientDriver","jdbc:derby://localhost:1527/esbdb;create=false","esb","esb");
        esbdbReportMediatorTest.addDBreportMediatorProperties("autocommit","true");
        esbdbReportMediatorTest.addDBreportMediatorSQLStatement("select * from company;");
        esbdbReportMediatorTest.addDBLookupMediatorParameters("CHAR","Expression","0" );
        esbdbReportMediatorTest.addDBReportParameterNamespase("m0","http://services/samples");
        esbCommon.mediatorUpdate();

        //Adding the second DBreport mediator - data source connection
        esbCommon.addRootLevelChildren("Add Child","Transform","DBReport");

        esbdbReportMediatorTest = new ESBDBReportMediatorTest(selenium);
        esbdbReportMediatorTest.addDBreportMediatorDSInfo("1","sourceTypeInline","com.sun.jndi.rmi.registry.RegistryContextFactory","lookupdb","rmi://localhost:2199","esb","esb");
        esbdbReportMediatorTest.addDBreportMediatorProperties("autocommit","true");
        esbdbReportMediatorTest.addDBreportMediatorSQLStatement("select * from company;");
        esbdbReportMediatorTest.addDBLookupMediatorParameters("CHAR","Expression","0" );
        esbdbReportMediatorTest.addDBReportParameterNamespase("m0","http://services/samples");
        
        esbCommon.mediatorUpdate();

//        esbCommon.sequenceSave();
//
//        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest = new ESBManageSynapseConfigurationTest(selenium);
//        esbManageSynapseConfigurationTest.saveSynapseConfig();
                
        seleniumTestBase.logOutUI();
    }
}
