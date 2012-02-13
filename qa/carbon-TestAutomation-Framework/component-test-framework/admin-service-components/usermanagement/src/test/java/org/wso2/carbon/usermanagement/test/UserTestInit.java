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

import org.wso2.carbon.user.mgt.ui.UserAdminExceptionException;
import org.wso2.carbon.user.mgt.ui.UserAdminStub;
import org.wso2.carbon.user.mgt.ui.types.carbon.FlaggedName;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.usermanagement.test.commands.UserManagementCommand;

import java.io.IOException;
import java.util.Map;

/**
 * initalized user store param to use for tests
 */
public class UserTestInit extends TestTemplate {


    UserAdminStub userAdminStub;
    private static String[] users = {"Wso2TestUser1","Wso2TestUser2","Wso2TestUser3"};
    private static String[] externalUsers = {null, null, null};
    private static String[] externalRoles = {null, null, null};
    protected static String userStoreClass ;
    protected static String adminRoleName ;
    protected static String adminUserName ;
    protected static String adminPassword ;      //admin password must be read from config file TODO
    protected static String everyOneRoleName ;
    protected Map<String, String> userStoreProperties;
    protected static boolean isReadOnly = false;   // read this property from user-mgt.xm, not applies to 3.0.0  TODO

    @Override
    public void init() {

    }

    @Override
    public void runSuccessCase() {
        userAdminStub = new UserManagementCommand().initUserAdminStubExecuteSuccessCase(sessionCookie);
        buildUserStoreConstants();
    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }

    public void buildUserStoreConstants()  {
        new UserConfigInit();
        if (! userStoreClass.equals(UserConstants.JDBC_CLASS)){

            String[] externalUserList = new UserManagementCommand().listUsersExecuteSuccessCase();
            int i = 0;
            for(String user : externalUserList){
                if (!(user.equals(adminUserName) || user.equals("admin"))){
                    if(isReadOnly){
                        users[i] = user;
                    }else{
                        externalUsers[i] = user;
                    }
                    i++;
                    if(i == 3){break;}
                }
            }
            ////// must read External user store attribute TODO
            i = 0;
            FlaggedName[] roleList = new UserManagementCommand().getAllRoleNamesExecuteSuccessCase();
            for(FlaggedName role : roleList){
                if (!(role.getItemName().equals(everyOneRoleName) || role.getItemName().
                        equals(adminRoleName) || role.getItemName().equals(UserConstants.SYSTEMROLE))){
                    externalRoles[i] = role.getItemName();
                    i++;
                    if(i == 3){break;}                                                                                                   }
            }
        }
   }

    public static String getUserStoreClass() {
        return   userStoreClass;
    }

    public static String getAdminUserName() {
        return   adminUserName;
    }

    public static String getAdminUserPassword() {
        return   adminPassword;                                
    }

    public static String getAdminRoleName() {
        return   adminRoleName;
    }

    public static String getEveryoneRoleName() {
        return   everyOneRoleName;
    }

    public static String getAdminPassWord() {
        return   adminPassword;
    }

    public Map<String, String> getUserStoreProperties() {
        return  userStoreProperties;
    }

    public static String getUser1() {
        return users[0];
    }

    public static String getUser2() {
        return users[1];
    }

    public static String getUser3() {
        return users[2];
    }

    public static String getExternalRole1() {
        return externalRoles[0];
    }

    public static String getExternalRole2() {
        return externalRoles[1];
    }

    public static String getExternalRole3() {
        return externalRoles[2];
    }

    public static boolean isReadOnly() {
        return isReadOnly;
    }
}
