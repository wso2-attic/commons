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

public class ESBUserManagementMainTest   extends CommonSetup{


    public ESBUserManagementMainTest(String text) {
        super(text);
    }

    public void testUserManagement() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
              
        seleniumTestBase.loginToUI("admin","admin");

        ESBUserManagementTest esbUserManagementTest = new ESBUserManagementTest(selenium);
        esbUserManagementTest.testPrepareUsers();
        esbUserManagementTest.testCheckPermission1();
        esbUserManagementTest.testCheckPermission2();
        esbUserManagementTest.testCheckPermission3();
        esbUserManagementTest.testCheckPermission4();
        esbUserManagementTest.testCheckPermission5();
        esbUserManagementTest.testCheckPermission6();

        esbUserManagementTest.testrenamePassword();
        esbUserManagementTest.testDeleteUsers();
        esbUserManagementTest.testDeleteRole();
        seleniumTestBase.logOutUI();
    }

}


