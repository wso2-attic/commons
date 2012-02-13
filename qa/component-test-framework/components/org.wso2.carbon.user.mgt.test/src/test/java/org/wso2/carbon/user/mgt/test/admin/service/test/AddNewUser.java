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
package org.wso2.carbon.user.mgt.test.admin.service.test;

import org.apache.axis2.AxisFault;
import org.junit.Assert;
import org.wso2.carbon.authenticator.proxy.test.LoginTest;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.user.mgt.admin.implementation.UserAdminImplementation;
import org.wso2.carbon.user.mgt.admin.authenticate.UserAdminServiceInitiator;
import org.wso2.carbon.user.mgt.admin.exception.UserAdminTestException;
import org.wso2.carbon.user.mgt.test.utils.ConstantData;
import org.wso2.carbon.user.mgt.ui.UserAdminStub;
import org.wso2.carbon.user.mgt.ui.types.carbon.FlaggedName;

/**
 *  This class able to test add user,delete user,get available roles and get roles assigned to the user stub functions.
 */
public class AddNewUser extends TestTemplate {

    private UserAdminImplementation userAdminImplementation;

    @Override
    public void init() {
    }

    @Override
    public void runSuccessCase() {
        initAdmin();
        try {
            String[] availableRoles = userAdminImplementation.getAllRoleNames();
            String[] assignRoles = {availableRoles[0]};
            FlaggedName[] flaggedName;
            userAdminImplementation.addNewUser("chamara", "chamara123", assignRoles);
            boolean isFound = false;

             flaggedName = userAdminImplementation.getUsersOfRole(assignRoles[0], "*");
            for (int i = 0; i <= flaggedName.length - 1; i++) {
                if (flaggedName[i].getItemName().endsWith("chamara")) {
                    isFound = true;
                }
            }
            if (!isFound) {
                Assert.fail("Username \"chamara\" not found in " + assignRoles[0]);
            }
            LoginTest loginTest = new LoginTest();
            loginTest.loginTest("chamara","chamara123");
            isFound = false;
            userAdminImplementation.deleteUser("chamara");

            flaggedName = userAdminImplementation.getUsersOfRole(assignRoles[0], "*");
            for (int i = 0; i <= flaggedName.length - 1; i++) {
                if (flaggedName[i].getItemName().endsWith("chamara")) {
                    isFound = true;
                }
            }
            if (isFound) {
                Assert.fail("Username \"chamara\" not deleted in " + assignRoles[0]);
            }


        } catch (UserAdminTestException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void runFailureCase() {
        //TODO: Method implementation

    }

    @Override
    public void cleanup() {
        //TODO: Method implementation

    }

    private void initAdmin() {
        UserAdminServiceInitiator userAdminServiceInitiator = new UserAdminServiceInitiator();
        UserAdminStub userAdminStub;
        try {
            userAdminStub = userAdminServiceInitiator.authenticateUserAdminStub(sessionCookie, ConstantData.SERVER_URL);
        } catch (AxisFault axisFault) {
            Assert.fail("Axis2 Fault occurred while authenticating user management admin service stub");
            throw new RuntimeException("Axis2 Fault occurred while authenticating user management admin service stub" + axisFault);
        } catch (UserAdminTestException e) {
            Assert.fail("Exception occurred in framework admin module while authenticating user management admin service stub");
            throw new RuntimeException("Exception occurred in framework admin module while authenticating user management admin service stub" + e);
        }
        userAdminImplementation = new UserAdminImplementation(userAdminStub);
    }
}
