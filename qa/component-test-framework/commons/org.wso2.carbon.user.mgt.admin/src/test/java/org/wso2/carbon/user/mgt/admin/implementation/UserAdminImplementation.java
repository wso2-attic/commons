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
package org.wso2.carbon.user.mgt.admin.implementation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.user.mgt.admin.authenticate.UserAdminServiceInitiator;
import org.wso2.carbon.user.mgt.admin.exception.UserAdminTestException;
import org.wso2.carbon.user.mgt.ui.UserAdminExceptionException;
import org.wso2.carbon.user.mgt.ui.UserAdminStub;
import org.wso2.carbon.user.mgt.ui.types.carbon.ClaimValue;
import org.wso2.carbon.user.mgt.ui.types.carbon.FlaggedName;
import org.wso2.carbon.user.mgt.ui.types.carbon.UIPermissionNode;
import org.wso2.carbon.user.mgt.ui.types.carbon.UserStoreInfo;

import javax.activation.DataHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * UserAdmin service method implementation class
 */
public class UserAdminImplementation extends UserAdminServiceInitiator {
    private static final Log log = LogFactory.getLog(UserAdminImplementation.class);
    private UserAdminStub userAdminStub;

    public UserAdminImplementation(UserAdminStub userAdminStub) {
        this.userAdminStub = userAdminStub;
    }

    /**
     * Add new user to the server
     *
     * @param userName   User Name
     * @param password   Password
     * @param roles      Assigned role
     * @param claimValue N/A
     * @param profile    Assigned user profile
     * @throws org.wso2.carbon.user.mgt.admin.exception.UserAdminTestException
     *          UserManager test module exception
     */
    public void addNewUser(String userName, String password, String[] roles,
                           ClaimValue[] claimValue, String profile) throws UserAdminTestException {
        try {
            userAdminStub.addUser(userName, password, roles, claimValue, profile);
        } catch (RemoteException e) {
            log.error("Exception thrown while adding new user : " + e);
            throw new UserAdminTestException("Exception thrown while adding new user : " + e);
        } catch (UserAdminExceptionException e) {
            log.error("UserManager carbon component giving error while adding new user : " + e);
            throw new UserAdminTestException("UserManager carbon component giving error while adding new user : " + e);
        }
    }

    /**
     * Add new user to the server
     *
     * @param userName User Name
     * @param password Password
     * @param roles    Assigned role
     * @throws org.wso2.carbon.user.mgt.admin.exception.UserAdminTestException
     *          UserManager test module exception
     */
    public void addNewUser(String userName, String password, String[] roles)
            throws UserAdminTestException {
        try {
            userAdminStub.addUser(userName, password, roles, null, null);
        } catch (RemoteException e) {
            log.error("Exception thrown while adding new user : " + e);
            throw new UserAdminTestException("Exception thrown while adding new user : " + e);
        } catch (UserAdminExceptionException e) {
            log.error("Exception thrown while adding new user : " + e);
            throw new UserAdminTestException("UserManager carbon component giving error while adding new user : " + e);
        }
    }

    /**
     * Add new user role
     *
     * @param roleName   name of the role
     * @param userList   name of the users under this role
     * @param permission assigned permissions
     * @throws org.wso2.carbon.user.mgt.admin.exception.UserAdminTestException
     *          UserManager test module exception
     */
    public void addNewRole(String roleName, String[] userList, String[] permission)
            throws UserAdminTestException {
        try {
            this.userAdminStub.addRole(roleName, userList, permission);
        } catch (RemoteException e) {
            log.error("Exception occurred while adding new role : " + e);
            throw new UserAdminTestException("Exception occurred while adding new role : " + e);
        } catch (UserAdminExceptionException e) {
            log.error("UserManager carbon component giving error while adding new role : " + e);
            throw new UserAdminTestException("UserManager carbon component giving error while adding new role : " + e);
        }

    }

    /**
     * Delete already user
     *
     * @param userName name of the user wish to delete
     * @throws org.wso2.carbon.user.mgt.admin.exception.UserAdminTestException
     *          UserManager test module exception
     */
    public void deleteUser(String userName) throws UserAdminTestException {
        try {
            userAdminStub.deleteUser(userName);
        } catch (Exception e) {
            log.error("Exception occurred while deleting user : " + e);
            throw new UserAdminTestException("Exception occurred while deleting user : " + e);
        }
    }

    /**
     * Delete user role from the server
     *
     * @param roleName name of the role wish to delete
     * @throws org.wso2.carbon.user.mgt.admin.exception.UserAdminTestException
     *          UserManager test module exception
     */
    public void deleteRole(String roleName) throws UserAdminTestException {
        try {
            userAdminStub.deleteRole(roleName);
        } catch (Exception e) {
            log.error("Exception occurred while deleting role : " + e);
            throw new UserAdminTestException("Exception occurred while deleting role : " + e);
        }
    }

    /**
     * Getting available role names
     *
     * @return String array of role names
     * @throws org.wso2.carbon.user.mgt.admin.exception.UserAdminTestException
     *          UserManager test module exception
     */
    public String[] getAllRoleNames() throws UserAdminTestException {
        String[] roleList = null;
        try {
            FlaggedName[] flaggedName = userAdminStub.getAllRolesNames();
            roleList = new String[flaggedName.length];
            for (int i = 0; i <= flaggedName.length - 1; i++) {
                roleList[i] = flaggedName[i].getItemName();
            }
        } catch (RemoteException e) {
            log.error("Exception occurred while getting role list : " + e);
            throw new UserAdminTestException("Exception occurred while getting role list : " + e);
        } catch (UserAdminExceptionException e) {
            log.error("Exception occurred in user management carbon component while getting role list : " + e);
            throw new UserAdminTestException("Exception occurred in user management carbon component while getting role list : " + e);
        }
        return roleList;
    }

    /**
     * Returning assigned role list of selected user
     *
     * @param userName name of the user wish to get role list
     * @return list of roles assigned to the user
     * @throws org.wso2.carbon.user.mgt.admin.exception.UserAdminTestException
     *          UserManager test module exception
     */
    public String[] getRoleOfUser(String userName) throws UserAdminTestException {
        String[] roleList = null;
        try {
            FlaggedName[] flaggedName = userAdminStub.getRolesOfUser(userName);
            roleList = new String[flaggedName.length];
            for (int i = 0; i <= flaggedName.length - 1; i++) {
                roleList[i] = flaggedName[i].getItemName();
            }
        } catch (RemoteException e) {
            log.error("Exception occurred while getting assigned roles of user : " + userName + " : " + e);
            throw new UserAdminTestException("Exception occurred while getting assigned roles of user : " + userName + " : " + e);
        } catch (UserAdminExceptionException e) {
            log.error("Exception occurred in user management carbon component while " +
                      "getting getting assigned roles of user : " + userName + " : " + e);
            throw new UserAdminTestException("Exception occurred in user management carbon component while " +
                                             "getting getting assigned roles of user : " + userName + " : " + e);
        }
        return roleList;
    }

    /**
     * Change password in specific user
     *
     * @param userName    user name
     * @param newPassword new password of user
     * @throws UserAdminTestException UserManager test module exception
     */
    public void changePassword(String userName, String newPassword) throws UserAdminTestException {
        try {
            this.userAdminStub.changePassword(userName, newPassword);
        } catch (RemoteException e) {
            throw new UserAdminTestException("Exception found while changing the password" + e);
        } catch (UserAdminExceptionException e) {
            throw new UserAdminTestException("UserManagement carbon component giving exception while changing the password" + e);
        }
    }

    /**
     * Method used to change password of current user
     *
     * @param oldPassword current password
     * @param newPassword new password
     * @throws UserAdminTestException user management admin service test component exception
     */
    public void changePasswordCurrentUser(String oldPassword, String newPassword)
            throws UserAdminTestException {
        try {
            this.userAdminStub.changePasswordByUser(oldPassword, newPassword);
        } catch (RemoteException e) {
            throw new UserAdminTestException("Exception found while changing the password of current user" + e);
        } catch (UserAdminExceptionException e) {
            throw new UserAdminTestException("Exception found in user manager carbon component " +
                                             "while changing the password of current user" + e);
        }
    }

    /**
     * Method used to change role name
     *
     * @param oldRoleName existing role name
     * @param newRoleName new role name
     * @throws UserAdminTestException org.wso2.carbon.user.mgt.test.admin.exception.UserAdminTestException
     *                                UserManager test module exception
     */
    public void updateRoleName(String oldRoleName, String newRoleName)
            throws UserAdminTestException {
        try {
            userAdminStub.updateRoleName(oldRoleName, newRoleName);
        } catch (RemoteException e) {
            throw new UserAdminTestException("Exception found while changing the role name" + e);
        } catch (UserAdminExceptionException e) {
            throw new UserAdminTestException("Exception found in user management carbon component " +
                                             "while changing the role name" + e);
        }
    }

    /**
     * Method used to update existing role engagement of selected user
     *
     * @param userName    user name
     * @param newUserLIst role list
     * @throws UserAdminTestException org.wso2.carbon.user.mgt.test.admin.exception.UserAdminTestException
     *                                UserManager test module exception
     */
    public void updateRolesOfUser(String userName, String[] newUserLIst)
            throws UserAdminTestException {
        try {
            userAdminStub.updateRolesOfUser(userName, newUserLIst);
        } catch (RemoteException e) {
            throw new UserAdminTestException("Exception found while updating roles of user" + e);
        } catch (UserAdminExceptionException e) {
            throw new UserAdminTestException("Exception found in user management carbon component " +
                                             "while updating roles of user" + e);
        }
    }

    /**
     * Update user list of specific role
     *
     * @param roleName role name
     * @param userList user list of role
     * @throws UserAdminTestException org.wso2.carbon.user.mgt.test.admin.exception.UserAdminTestException
     *                                UserManager test module exception
     */
    public void updateUsersOfRole(String roleName, String[] userList)
            throws UserAdminTestException {
        FlaggedName[] flaggedName = new FlaggedName[userList.length];
        try {
            for (int i = 0; i <= userList.length - 1; i++) {
                flaggedName[i].setItemName(userList[i]);
            }
            userAdminStub.updateUsersOfRole(roleName, flaggedName);
        } catch (RemoteException e) {
            throw new UserAdminTestException("Exception found while updating roles of user" + e);
        } catch (UserAdminExceptionException e) {
            throw new UserAdminTestException("Exception found in user management carbon component " +
                                             "while updating roles of user" + e);
        }
    }

    /**
     * List available users
     *
     * @param filterString search string
     * @return result user list
     * @throws UserAdminTestException org.wso2.carbon.user.mgt.test.admin.exception.UserAdminTestException
     *                                UserManager test module exception
     */
    public String[] listUsers(String filterString) throws UserAdminTestException {
        String[] userList;
        try {
            userList = userAdminStub.listUsers(filterString);
        } catch (RemoteException e) {
            throw new UserAdminTestException("Exception found while getting user list" + e);
        } catch (UserAdminExceptionException e) {
            throw new UserAdminTestException("Exception found in user management carbon component while getting user list" + e);
        }
        return userList;
    }

    /**
     * getting user list in specific role
     *
     * @param roleName     name of the role wish to search user list
     * @param searchString search string
     * @throws UserAdminTestException org.wso2.carbon.user.mgt.test.admin.exception.UserAdminTestException
     *                                UserManager test module exception
     */

    public FlaggedName[] getUsersOfRole(String roleName, String searchString)
            throws UserAdminTestException {
        String[] userList;
        FlaggedName[] flaggedName;
        try {
            flaggedName = userAdminStub.getUsersOfRole(roleName, searchString);
            userList = new String[flaggedName.length];
            for (int i = 0; i <= flaggedName.length - 1; i++) {
                userList[i] = flaggedName[i].getItemName();
            }
        } catch (RemoteException e) {
            throw new UserAdminTestException("Exception found while getting user list in role :" + roleName + " : " + e);
        } catch (UserAdminExceptionException e) {
            throw new UserAdminTestException("Exception found in user management component while getting user list in role :" + roleName + " : " + e);
        }
        return flaggedName;
    }

    /**
     * Getting available user store information
     *
     * @return User Store infomation object
     */
    public UserStoreInfo getUserStoreInfo() {
        UserStoreInfo userStoreInfo = new UserStoreInfo();
        try {
            userStoreInfo = userAdminStub.getUserStoreInfo();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (UserAdminExceptionException e) {
            e.printStackTrace();
        }
        return userStoreInfo;
    }

    /**
     * This method used to import multiple users information from file
     *
     * @param fileName        file name
     * @param fileLocation    location of the file
     * @param defaultPassword password
     */
    public void bulkImportUsers(String fileName, String fileLocation, String defaultPassword) {
        try {
            userAdminStub.bulkImportUsers(fileName, new DataHandler(new URL(fileLocation)), defaultPassword);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (UserAdminExceptionException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public UIPermissionNode getAllUiPermissions() throws UserAdminTestException {
        UIPermissionNode uiPermissionNode = new UIPermissionNode();
        try {
            uiPermissionNode = userAdminStub.getAllUIPermissions();
        } catch (RemoteException e) {
            throw new UserAdminTestException("Exception occurred while getting available user permissions" + e);
        } catch (UserAdminExceptionException e) {
            throw new UserAdminTestException("Exception occurred in user management carbon component " +
                                             "while getting available user permissions" + e);
        }
        return uiPermissionNode;
    }

    public UIPermissionNode getRolePermissions(String roleName) throws UserAdminTestException {
        UIPermissionNode uiPermissionNode = new UIPermissionNode();
        try {
            uiPermissionNode = userAdminStub.getRolePermissions(roleName);
        } catch (RemoteException e) {
            throw new UserAdminTestException("Exception occurred while getting " + roleName + " role permissions" + e);
        } catch (UserAdminExceptionException e) {
            throw new UserAdminTestException("Exception occurred in user management carbon component " +
                                             "while getting " + roleName + " role permissions" + e);
        }
        return uiPermissionNode;
    }

    public void setUIPermissions(String roleName, String[] rawResources)
            throws UserAdminTestException {
        try {
            userAdminStub.setRoleUIPermission(roleName, rawResources);
        } catch (RemoteException e) {
            throw new UserAdminTestException("Exception occurred while setting permissions to role : " + roleName + e);
        } catch (UserAdminExceptionException e) {
            throw new UserAdminTestException("Exception occurred in user management carbon component " +
                                             "while setting permissions to role : " + roleName + e);
        }
    }


}

