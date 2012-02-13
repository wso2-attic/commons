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
package org.wso2.carbon.usermanagement.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.user.mgt.ui.UserAdminStub;
import org.wso2.carbon.user.mgt.ui.types.carbon.FlaggedName;
import org.wso2.carbon.user.mgt.ui.types.carbon.UIPermissionNode;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.usermanagement.test.commands.UserManagementCommand;


/**
 * Role specfic tests 
 */
public class UserRoleUnitTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(UserRoleUnitTest.class);
    UserAdminStub userAdminStub;
    private String[] userSet1 = {UserTestInit.getUser1() ,UserTestInit.getUser2()} ;
    private String[] userSet2 = {UserTestInit.getUser1() ,UserTestInit.getUser3()} ;
    private String[] permissionSet1 = {UserConstants.PERMISSION1 ,UserConstants.PERMISSION2} ;
    private String[] permissionSet2 = {UserConstants.PERMISSION1 ,UserConstants.PERMISSION3} ;
    boolean isAuthorized = false ;
    @Override
    public void init() {
        log.info("Running UserRoleUnitTests ");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running UserRoleUnitTests SuccessCase ");
        userAdminStub = new UserManagementCommand().initUserAdminStubExecuteSuccessCase(sessionCookie);
        createRole();
        createInvalidRole();
        assignPermission();
        assignInvalidPermission();
        addRemoveUsersFromRoles();
        invalidAddRemoveUsersFromRoles();
        deleteRole();
        deleteInvalidRole();
        renameRole();
        renameInvalidRole();
        
        if(UserTestInit.getExternalRole1() != null){

        }
        log.info("EndpointUnitTests SuccessCase Passed");
    }

    @Override
    public void runFailureCase() {
        log.debug("Running EndpointUnitTests FailureCase ");
        //runSuccessCase();
        log.info("EndpointUnitTests FailureCase Passed");
    }

    @Override
    public void cleanup() {

    }


    public void createRole() {

        FlaggedName[] roleList = new UserManagementCommand(userAdminStub).getAllRoleNamesExecuteSuccessCase();
        for(FlaggedName role : roleList){
            if(role.getItemName().equals(UserConstants.INTERNAL_ROLE_NAME1) ||
                    role.getItemName().equals(UserConstants.INTERNAL_ROLE_NAME2) ||
                    role.getItemName().equals(UserConstants.INTERNAL_ROLE_NAME3)){
                new UserManagementCommand(userAdminStub).deleteRoleExecuteSuccessCase(role.
                        getItemName());
            }
        }
        String[] externalUserList = new UserManagementCommand(userAdminStub).listUsersExecuteSuccessCase();
        for(String user : externalUserList){
            if(user.equals(UserTestInit.getUser1()) || user.equals(UserTestInit.getUser2()) ||
                    user.equals(UserTestInit.getUser3())){
                 new UserManagementCommand(userAdminStub).deleteUserExecuteSuccessCase(user);
            }
        }

        String password = "mypass";
        new UserManagementCommand(userAdminStub).addUserExecuteSuccessCase(UserTestInit.getUser1(),
                password, null);
        new UserManagementCommand(userAdminStub).addUserExecuteSuccessCase(UserTestInit.getUser2(),
                password, null);
        new UserManagementCommand(userAdminStub).addUserExecuteSuccessCase(UserTestInit.getUser3(),
                password, null);

        new UserManagementCommand(userAdminStub).addRoleExecuteSuccessCase(
                UserConstants.INTERNAL_ROLE_NAME1, null, null);
        new UserManagementCommand(userAdminStub).addRoleExecuteSuccessCase(
                UserConstants.INTERNAL_ROLE_NAME2, userSet1, null);
        new UserManagementCommand(userAdminStub).addRoleExecuteSuccessCase(
                UserConstants.INTERNAL_ROLE_NAME3, userSet2, permissionSet1);
        
        assertTrue(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME2, UserTestInit.getUser1()));
        assertTrue(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME2, UserTestInit.getUser2()));
        assertTrue(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME3, UserTestInit.getUser1()));
        assertTrue(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME3, UserTestInit.getUser3()));

        assertTrue(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME3, UserConstants.PERMISSION1));
        assertTrue(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME3, UserConstants.PERMISSION2));
    }

    public void createInvalidRole(){

        new UserManagementCommand(userAdminStub).addRoleExecuteFailureCase(
                UserConstants.INTERNAL_ROLE_NAME1, null, null);
        new UserManagementCommand(userAdminStub).addRoleExecuteFailureCase(
                UserTestInit.adminRoleName, null, null);
        new UserManagementCommand(userAdminStub).addRoleExecuteFailureCase(
                UserTestInit.everyOneRoleName, null, null);
        new UserManagementCommand(userAdminStub).addRoleExecuteFailureCase(
                UserConstants.SYSTEMROLE, null, null);
    }

    public void assignPermission() {

        new UserManagementCommand(userAdminStub).setRolePermissionExecuteSuccessCase(
                UserConstants.INTERNAL_ROLE_NAME1, permissionSet1);
        assertTrue(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME1, UserConstants.PERMISSION1));
        assertTrue(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME1, UserConstants.PERMISSION2));
        new UserManagementCommand(userAdminStub).setRolePermissionExecuteSuccessCase(
                UserConstants.INTERNAL_ROLE_NAME2, permissionSet1);
        assertTrue(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME2, UserConstants.PERMISSION1));
        assertTrue(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME2, UserConstants.PERMISSION2));
        new UserManagementCommand(userAdminStub).setRolePermissionExecuteSuccessCase(
                UserConstants.INTERNAL_ROLE_NAME2, permissionSet2);
        assertTrue(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME2, UserConstants.PERMISSION1));
        assertTrue(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME2, UserConstants.PERMISSION3));
        assertFalse(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME2, UserConstants.PERMISSION2));
    }

    public void assignInvalidPermission (){

        new UserManagementCommand(userAdminStub).setRolePermissionExecuteFailureCase(
                UserTestInit.adminRoleName, permissionSet2);
//        new UserManagementCommand(userAdminStub).setRolePermissionUserExecuteFailureCase(
//                UserConstants.SYSTEMROLE, permissionSet2);
    }

    public void addRemoveUsersFromRoles() {

        FlaggedName flaggedName1 = new FlaggedName();
        FlaggedName flaggedName2 = new FlaggedName();
        flaggedName1.setItemName(UserTestInit.getUser1()); flaggedName1.setSelected(true);
        flaggedName2.setItemName(UserTestInit.getUser2()); flaggedName2.setSelected(true);

        new UserManagementCommand(userAdminStub).updateUsersOfRoleExecuteSuccessCase(
                UserConstants.INTERNAL_ROLE_NAME1, new FlaggedName[] {flaggedName1, flaggedName2});
        assertTrue(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME1, UserTestInit.getUser1()));
        assertTrue(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME1, UserTestInit.getUser2()));
        flaggedName2.setSelected(false);
        new UserManagementCommand(userAdminStub).updateUsersOfRoleExecuteSuccessCase(
                UserConstants.INTERNAL_ROLE_NAME1, new FlaggedName[] {flaggedName1, flaggedName2});
        assertTrue(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME1, UserTestInit.getUser1()));
        assertFalse(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME1, UserTestInit.getUser2()));

    }

    public void  invalidAddRemoveUsersFromRoles() {

        FlaggedName flaggedName1 = new FlaggedName();
        FlaggedName flaggedName2 = new FlaggedName();
        flaggedName1.setItemName(UserTestInit.getUser1()); flaggedName1.setSelected(false);
        flaggedName2.setItemName(UserTestInit.adminUserName); flaggedName2.setSelected(false);

        new UserManagementCommand(userAdminStub).updateUsersOfRoleExecuteFailureCase(
                UserTestInit.adminRoleName, new FlaggedName[] {flaggedName2});
        new UserManagementCommand(userAdminStub).updateUsersOfRoleExecuteFailureCase(
                UserTestInit.everyOneRoleName, new FlaggedName[] {flaggedName2});
        new UserManagementCommand(userAdminStub).updateUsersOfRoleExecuteFailureCase(
                UserTestInit.everyOneRoleName, new FlaggedName[] {flaggedName1});
    }

    public void deleteRole() {

        new UserManagementCommand(userAdminStub).deleteRoleExecuteSuccessCase(
                UserConstants.INTERNAL_ROLE_NAME2);

        assertFalse(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME2, UserTestInit.getUser1()));
        assertFalse(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME2, UserTestInit.getUser2()));
        assertFalse(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME2, UserConstants.PERMISSION1));
        assertFalse(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME2, UserConstants.PERMISSION2));
        new UserManagementCommand(userAdminStub).deleteRoleExecuteSuccessCase(
                UserConstants.INTERNAL_ROLE_NAME1);
        new UserManagementCommand(userAdminStub).addRoleExecuteSuccessCase(
                UserConstants.INTERNAL_ROLE_NAME1, null, null);
        assertFalse(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME1, UserTestInit.getUser1()));
        assertFalse(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME1, UserTestInit.getUser2()));
        assertFalse(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME1, UserConstants.PERMISSION1));
        assertFalse(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME1, UserConstants.PERMISSION2));

    }

    public void deleteInvalidRole() {

        new UserManagementCommand(userAdminStub).deleteRoleExecuteFailureCase(
                UserTestInit.adminRoleName);
        new UserManagementCommand(userAdminStub).deleteRoleExecuteFailureCase(
                UserTestInit.everyOneRoleName);
//        new UserManagementCommand(userAdminStub).deleteRoleExecuteFailureCase(
//                UserConstants.SYSTEMROLE);

    }

    public void renameRole() {

        new UserManagementCommand(userAdminStub).renameRoleExecuteSuccessCase(
                UserConstants.INTERNAL_ROLE_NAME3, UserConstants.INTERNAL_ROLE_NAME2);
        assertTrue(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME2, UserConstants.PERMISSION1));
        assertTrue(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME2, UserConstants.PERMISSION2));
        assertFalse(isRoleAuthorized(UserConstants.INTERNAL_ROLE_NAME2, UserConstants.PERMISSION3));
        assertTrue(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME2, UserTestInit.getUser1()));
        assertTrue(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME2, UserTestInit.getUser3()));
        assertFalse(isUserAssigned(UserConstants.INTERNAL_ROLE_NAME2, UserTestInit.getUser2()));
    }

    public void renameInvalidRole() {

        new UserManagementCommand(userAdminStub).renameRoleExecuteFailureCase(
                UserConstants.INTERNAL_ROLE_NAME2, UserConstants.INTERNAL_ROLE_NAME1);
        new UserManagementCommand(userAdminStub).renameRoleExecuteFailureCase(
                UserTestInit.adminRoleName, "renamedAdminRole");
        new UserManagementCommand(userAdminStub).renameRoleExecuteFailureCase(
                UserTestInit.everyOneRoleName, "renamedEveryoneRole");
//        new UserManagementCommand(userAdminStub).renameRoleExecuteFailureCase(
//                UserConstants.SYSTEMROLE, "renamedSystemRole");
    }

    public void externalRoles() {
        new UserManagementCommand(userAdminStub).setRolePermissionExecuteSuccessCase(
                UserTestInit.getExternalRole1(), permissionSet1);
        assertTrue(isRoleAuthorized(UserTestInit.getExternalRole1(), UserConstants.PERMISSION1));
        assertTrue(isRoleAuthorized(UserTestInit.getExternalRole1(), UserConstants.PERMISSION2));
        new UserManagementCommand(userAdminStub).setRolePermissionExecuteSuccessCase(
                UserTestInit.getExternalRole1(), permissionSet2);
        assertTrue(isRoleAuthorized(UserTestInit.getExternalRole1(), UserConstants.PERMISSION1));
        assertTrue(isRoleAuthorized(UserTestInit.getExternalRole1(), UserConstants.PERMISSION3));
        assertFalse(isRoleAuthorized(UserTestInit.getExternalRole1(), UserConstants.PERMISSION2));        
    }

    public void extrnalInvalidRoles() {

        new UserManagementCommand(userAdminStub).addRoleExecuteFailureCase(
                UserTestInit.getExternalRole1(), null, null);
        new UserManagementCommand(userAdminStub).renameRoleExecuteFailureCase(
                UserConstants.INTERNAL_ROLE_NAME1, UserTestInit.getExternalRole1());
    }

    private boolean isUserAssigned(String roleName, String userName){
        userAdminStub = new UserManagementCommand().initUserAdminStubExecuteSuccessCase(sessionCookie);
        boolean isAssigned = false;
        FlaggedName[] userNames;
        userNames = new UserManagementCommand(userAdminStub).getUsersOfRoleExecuteSuccessCase(roleName);
        for(FlaggedName user : userNames){
             if(user.getItemName().equals(userName)){
                 isAssigned = user.getSelected();
             }
        }
        return isAssigned;
    }

  public boolean isRoleAuthorized(String role, String permission) {
        isAuthorized = false;
        UIPermissionNode node = new UserManagementCommand(userAdminStub).
                getRolePermissionExecuteSuccessCase(role);
        roleAuthorization(node, permission);
        return isAuthorized;
    }

    public boolean isRoleAssigned(String roleName, String userName) {
        FlaggedName[] roles;
        boolean isAssigned = false;;
        roles = new UserManagementCommand(userAdminStub).getRolesOfUserExecuteSuccessCase(userName);
        for(FlaggedName role : roles){
            if(role.getItemName().equals(roleName)){
                isAssigned = role.getSelected();
            }
        }
        return isAssigned;
    }

    private void roleAuthorization(UIPermissionNode node  , String permission) {

        UIPermissionNode[] children = node.getNodeList();
        String path = node.getResourcePath();
        if(node.getSelected() && permission.equals(node.getResourcePath())){
            isAuthorized=true;
        }

        for (UIPermissionNode child : children) {
            if (child.getNodeList()== null){
                if (child.getSelected() && permission.equals(child.getResourcePath())){
                    isAuthorized = true;
                }
            } else{
                roleAuthorization(child, permission);
            }
        }
    }    

}