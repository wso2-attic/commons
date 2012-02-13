/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/
package org.wso2.carbon.usermanagement.test.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.AxisFault;
import org.wso2.carbon.user.mgt.ui.UserAdminStub;
import org.wso2.carbon.user.mgt.ui.UserAdminExceptionException;
import org.wso2.carbon.user.mgt.ui.types.carbon.UIPermissionNode;
import org.wso2.carbon.user.mgt.ui.types.carbon.FlaggedName;
import org.wso2.carbon.common.test.utils.FrameworkSettings;
import static org.wso2.carbon.common.test.utils.FrameworkSettings.HOST_NAME;
import static org.wso2.carbon.common.test.utils.FrameworkSettings.HTTPS_PORT;

import java.rmi.RemoteException;
import junit.framework.TestCase;
import javax.activation.DataHandler;

/**
 * Implementation of Methods in UserAdmin service as success & failure cases 
 */
public class UserManagementCommand extends TestCase {

    UserAdminStub userAdminStub;
    private static final Log log = LogFactory.getLog(UserManagementCommand.class);

    public UserManagementCommand(UserAdminStub userAdminStub) {
        this.userAdminStub = userAdminStub;
        log.debug("UserAdminStub added");
    }

    public UserManagementCommand() {
        log.debug("UserAdminStub added");
    }

/*
*        Test for the success and failure cases  for  add Role
*/
    public void addRoleExecuteSuccessCase(String roleName, String[] userSet, String[] permissionSet) {
        try {
            userAdminStub.addRole(roleName, userSet, permissionSet);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
    }

    public void addRoleExecuteFailureCase(String roleName, String[] userSet, String[] permissionSet) {
        try {
            userAdminStub.addRole(roleName, userSet, permissionSet);
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
       }
    }

/*
*        Test for the success and failure cases  for  add user
*/
    public void addUserExecuteSuccessCase(String userName, String password, String[] roleSet) {
        try {
            userAdminStub.addUser(userName, password, roleSet, null, null);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
    }

    public void addUserExecuteFailureCase(String userName, String password, String[] roleSet) {
        try {
            userAdminStub.addUser(userName, password, roleSet, null, null);
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            //e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
    }

/*
*        Test for the success and failure cases  for  delete user
*/
    public void deleteUserExecuteSuccessCase(String userName) {
        try {
            userAdminStub.deleteUser(userName);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
    }

    public void deleteUserExecuteFailureCase(String userName) {
        try {
            userAdminStub.deleteUser(userName);
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
    }
/*
*        Test for the success and failure cases  for  delete role
*/
    public void deleteRoleExecuteSuccessCase(String roleName) {
        try {
            userAdminStub.deleteRole(roleName);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
    }

    public void deleteRoleExecuteFailureCase(String roleName) {
        try {
            userAdminStub.deleteRole(roleName);
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
    }
/*
*        Test for the success and failure cases  for  get permissions
*/
    public UIPermissionNode getRolePermissionExecuteSuccessCase(String roleName) {
        UIPermissionNode permission = null;
        try {
            permission = userAdminStub.getRolePermissions(roleName);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
        return permission;
    }

    public void getRolePermissionExecuteFailureCase(String roleName) {
        try {
            userAdminStub.getRolePermissions(roleName);
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
    }

/*
*        Test for the success and failure cases  for  get roles of users
*/
    public FlaggedName[] getRolesOfUserExecuteSuccessCase(String userName) {
        FlaggedName[] roles = null;
        try {
            roles = userAdminStub.getRolesOfUser(userName);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
        return roles;
    }

    public void getRolesOfUserExecuteFailureCase(String userName) {
        try {
            userAdminStub.getRolesOfUser(userName);
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
    }

/*
*        Test for the success and failure cases  for  get users of roles
*/
    public FlaggedName[] getUsersOfRoleExecuteSuccessCase(String roleName) {
        FlaggedName[] users = null;
        try {
            users = userAdminStub.getUsersOfRole(roleName, "*");
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
        return users;
    }

    public void getUsersOfRoleExecuteFailureCase(String roleName) {
        try {
            userAdminStub.getUsersOfRole(roleName, "*");
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
    }
/*
*        Test for the success and failure cases  for  list all users
*/
    public String[] listUsersExecuteSuccessCase() {
        String[] users = null;
        try {
            users = userAdminStub.listUsers("*");
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
        return users;
    }

    public void listUserExecuteFailureCase() {
        try {
            userAdminStub.listUsers("*");
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
    }
    
/*
*        Test for the success and failure cases  for  set role permissions
*/
    public void setRolePermissionExecuteSuccessCase(String roleName, String[] permission) {
        try {
            userAdminStub.setRoleUIPermission(roleName, permission);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
    }

    public void setRolePermissionExecuteFailureCase(String roleName, String[] permission) {
        try {
            userAdminStub.setRoleUIPermission(roleName, permission);
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
    }

/*
*        Test for the success and failure cases  for  update users od roles
*/
    public void updateUsersOfRoleExecuteSuccessCase(String roleName, FlaggedName[] users) {
        try {
            userAdminStub.updateUsersOfRole(roleName, users);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
    }

    public void updateUsersOfRoleExecuteFailureCase(String roleName, FlaggedName[] users) {
        try {
            userAdminStub.updateUsersOfRole(roleName, users);
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
    }

/*
*        Test for the success and failure cases  for  rename role
*/
    public void renameRoleExecuteSuccessCase(String roleName, String newRoleName) {
        try {
            userAdminStub.updateRoleName(roleName, newRoleName);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
    }

    public void renameRoleExecuteFailureCase(String roleName, String newRoleName) {
        try {
            userAdminStub.updateRoleName(roleName, newRoleName);
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
    }

/*
*        Test for the success and failure cases  for  bulk import
*/
    public void bulkImportExecuteSuccessCase(String file, DataHandler inputData, String password) {
        try {
            userAdminStub.bulkImportUsers(file, inputData, password);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
    }

    public void bulkImportExecuteFailureCase(String file, DataHandler inputData, String password) {
        try {
            userAdminStub.bulkImportUsers(file, inputData, password);
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
    }


/*
*        Test for the success and failure cases  for  update roles of users
*/
    public void updateRolesOfUserExecuteSuccessCase(String userName, String[] roleNames) {
        try {
            userAdminStub.updateRolesOfUser(userName, roleNames);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
    }

    public void updateRolesOfUserExecuteFailureCase(String userName, String[] roleNames) {
        try {
            userAdminStub.updateRolesOfUser(userName, roleNames);
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
    }



/*
*        Test for the success and failure cases  for  change password
*/
    public void changePasswordExecuteSuccessCase(String userName, String password) {
        try {
            userAdminStub.changePassword(userName, password);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
    }

    public void changePasswordExecuteFailureCase(String userName, String password) {
        try {
            userAdminStub.changePassword(userName, password);
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
    }

/*
*        Test for the success and failure cases  for  change password
*/
    public void changePasswordByUserExecuteSuccessCase(String userName, String password) {
        try {
            userAdminStub.changePasswordByUser(userName, password);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
    }

    public void changePasswordByUserExecuteFailureCase(String userName, String password) {
        try {
            userAdminStub.changePasswordByUser(userName, password);
            fail("Expected exception did not occur");
        } catch (Exception e) {
            //e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
    }

/*
*        Test for the success and failure cases  for  get all role names
*/
    public FlaggedName[] getAllRoleNamesExecuteSuccessCase() {
        FlaggedName[] roles = null;
        try {
            roles = userAdminStub.getAllRolesNames();
        } catch (Exception e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
        return roles;
    }

    public FlaggedName[] getAllRoleNamesExecuteFailureCase() {
        FlaggedName[] roles = null;
        try {
            roles = userAdminStub.getAllRolesNames();
            fail("Expected exception did not occur");
        } catch (Exception e) {
           // e.printStackTrace();
//        } catch (UserAdminExceptionException e) {
//            e.printStackTrace();
//            fail("Unexpected exception thrown");
        }
        return roles;
    }

    public UserAdminStub initUserAdminStubExecuteSuccessCase(String sessionCookie) {
        log.debug("sessionCookie:" + sessionCookie);
        FrameworkSettings.getProperty();
        String serviceURL = "https://" + HOST_NAME + ":" + HTTPS_PORT + "/services/UserAdmin";

        UserAdminStub endpointAdminStub = null;
        try {
            endpointAdminStub = new UserAdminStub(serviceURL);

            ServiceClient client = endpointAdminStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("endpointAdminStub created");
        return endpointAdminStub;
    }
}
