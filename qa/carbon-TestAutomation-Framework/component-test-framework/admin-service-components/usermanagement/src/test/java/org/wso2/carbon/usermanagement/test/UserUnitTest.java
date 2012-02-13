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
import org.wso2.carbon.user.mgt.ui.Util;
import org.wso2.carbon.user.mgt.ui.types.carbon.FlaggedName;
import org.wso2.carbon.usermanagement.test.commands.UserManagementCommand;
import org.wso2.carbon.common.test.utils.TestTemplate;

import java.io.*;

/**
 * user specific test cases
 */
public class UserUnitTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(UserRoleUnitTest.class);
    UserAdminStub userAdminStub;
    private String[] roleSet1 = {UserConstants.INTERNAL_ROLE_NAME1, UserConstants.INTERNAL_ROLE_NAME2};
    private String validPassword = "!@#$QWE" ;

    @Override
    public void init() {
        log.info("Running UserRoleUnitTests ");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running UserRoleUnitTests SuccessCase ");
        userAdminStub = new UserManagementCommand().initUserAdminStubExecuteSuccessCase(sessionCookie);
        if(! UserTestInit.isReadOnly()){
            addUser();
            addInvalidUser();
            deleteInvalidUser();
            changePassword();
            changeInvalidPassword();
            bukImportUsers();
            updateRolesOfUser();
            invalidUpdateRolesOfUser();
            deleteUser();
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

    public void addUser() {
        
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
        
        new UserManagementCommand(userAdminStub).addRoleExecuteSuccessCase(
                UserConstants.INTERNAL_ROLE_NAME1, null, null);
        new UserManagementCommand(userAdminStub).addRoleExecuteSuccessCase(
                UserConstants.INTERNAL_ROLE_NAME2, null, null);

        new UserManagementCommand(userAdminStub).addUserExecuteSuccessCase(UserTestInit.getUser1(),
                validPassword, null);
        new UserManagementCommand(userAdminStub).addUserExecuteSuccessCase(UserTestInit.getUser2(),
                validPassword, roleSet1);
        assertTrue(isRoleAssigned(UserTestInit.getEveryoneRoleName(), UserTestInit.getUser1()));
        assertTrue(isRoleAssigned(UserTestInit.getEveryoneRoleName(), UserTestInit.getUser2()));
        assertTrue(isRoleAssigned(UserConstants.INTERNAL_ROLE_NAME1, UserTestInit.getUser2()));
        assertTrue(isRoleAssigned(UserConstants.INTERNAL_ROLE_NAME2, UserTestInit.getUser2()));

    }

    public void addInvalidUser() {

        new UserManagementCommand(userAdminStub).addUserExecuteFailureCase(UserTestInit.adminUserName,
                validPassword, null);
        new UserManagementCommand(userAdminStub).addUserExecuteFailureCase(UserConstants.SYSTEMUSER1,
                validPassword, null);
        new UserManagementCommand(userAdminStub).addUserExecuteFailureCase(UserConstants.SYSTEMUSER2,
                validPassword, null);
        new UserManagementCommand(userAdminStub).addUserExecuteFailureCase(UserTestInit.getUser1(),
                validPassword, null);
        new UserManagementCommand(userAdminStub).addUserExecuteFailureCase("asela@wso2.com",
                validPassword, null);
        new UserManagementCommand(userAdminStub).addUserExecuteFailureCase("o2",
                validPassword, null);
        new UserManagementCommand(userAdminStub).addUserExecuteFailureCase(
                "qwertyuiopasdfghjkzxcvbnm123456", validPassword, null);
        new UserManagementCommand(userAdminStub).addUserExecuteFailureCase("o2",
                validPassword, null);
// write test case for password length and special characters using  PasswordJavaRegEx in  user-mgt.xml Using deault value 5,30 TODO
        new UserManagementCommand(userAdminStub).addUserExecuteFailureCase(UserTestInit.getUser2(),
                null, null);
        new UserManagementCommand(userAdminStub).addUserExecuteFailureCase(UserTestInit.getUser2(),
                "pass", null);
        new UserManagementCommand(userAdminStub).addUserExecuteFailureCase(UserTestInit.getUser2(),
                "qwertyuiopasdfghjkzxcvbnm123456", null);
    }

    public void deleteUser(){

        new UserManagementCommand(userAdminStub).deleteUserExecuteSuccessCase(UserTestInit.getUser2());
        assertFalse(isRoleAssigned(UserTestInit.getEveryoneRoleName(), UserTestInit.getUser2()));
        assertFalse(isRoleAssigned(UserConstants.INTERNAL_ROLE_NAME1, UserTestInit.getUser2()));
        assertFalse(isRoleAssigned(UserConstants.INTERNAL_ROLE_NAME2, UserTestInit.getUser2()));
        new UserManagementCommand(userAdminStub).deleteUserExecuteSuccessCase(UserTestInit.getUser1());

    }

    public void deleteInvalidUser() {
        new UserManagementCommand(userAdminStub).deleteUserExecuteFailureCase(
                UserTestInit.getAdminUserName());
//        new UserManagementCommand(userAdminStub).deleteUserExecuteFailureCase(
//                UserConstants.SYSTEMUSER1);
//        new UserManagementCommand(userAdminStub).deleteUserExecuteFailureCase(
//                UserConstants.SYSTEMUSER2);
    }

    public void changePassword() {

        new UserManagementCommand(userAdminStub).changePasswordExecuteSuccessCase(
                UserTestInit.getUser1(), validPassword);
        new UserManagementCommand(userAdminStub).changePasswordByUserExecuteSuccessCase(
                UserTestInit.getAdminUserPassword(), UserTestInit.getAdminUserPassword());
    }

    public void changeInvalidPassword() {
// write test case for password length and special characters using  PasswordJavaRegEx in  user-mgt.xml Using deault value
        new UserManagementCommand(userAdminStub).changePasswordExecuteFailureCase(
                UserTestInit.getUser1(), null);
        new UserManagementCommand(userAdminStub).changePasswordExecuteFailureCase(
                UserTestInit.getUser1(), "pass");
        new UserManagementCommand(userAdminStub).changePasswordExecuteFailureCase(
                UserTestInit.getUser1(), "qwertyuiopasdfghjkzxcvbnm123456");

        new UserManagementCommand(userAdminStub).changePasswordByUserExecuteFailureCase(
                UserTestInit.adminPassword, null);
        new UserManagementCommand(userAdminStub).changePasswordByUserExecuteFailureCase(
                UserTestInit.adminPassword, "pass");
        new UserManagementCommand(userAdminStub).changePasswordByUserExecuteFailureCase(
                UserTestInit.adminPassword, "qwertyuiopasdfghjkzxcvbnm123456");
        new UserManagementCommand(userAdminStub).changePasswordByUserExecuteFailureCase("test",
                UserTestInit.getAdminUserPassword());

    }

    public void bukImportUsers() {

        String[] bulkUploadUsers = {"BulkUploadUser1","BulkUploadUser2","BulkUploadUser3",
                                    "BulkUploadUser4","BulkUploadUser5","BulkUploadUser6"};
        
        String file = null;
        try {
            file = new File(".").getCanonicalPath()+
                    File.separator+"src"+ File.separator+"test"+File.separator+"resources"+
                    File.separator+"bulkImport"+File.separator;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String[] bulkImportFiles = {file+"users1.csv",file+"users2.csv", file+"users3.csv",
                                    file+"users4.csv", file+"users5.csv"};

        new UserManagementCommand(userAdminStub).bulkImportExecuteSuccessCase(bulkImportFiles[0],
                Util.buildDataHandler(getBytesFromFile(bulkImportFiles[0])), validPassword);

        assertTrue(isRoleAssigned(UserTestInit.getEveryoneRoleName(), bulkUploadUsers[0]));
        assertTrue(isRoleAssigned(UserTestInit.getEveryoneRoleName(), bulkUploadUsers[1]));

        new UserManagementCommand(userAdminStub).updateRolesOfUserExecuteSuccessCase(
                bulkUploadUsers[0], new String[] {UserTestInit.getEveryoneRoleName(),
                        UserConstants.INTERNAL_ROLE_NAME1});

        new UserManagementCommand(userAdminStub).bulkImportExecuteFailureCase(bulkImportFiles[1],
                Util.buildDataHandler(getBytesFromFile(bulkImportFiles[1])), validPassword);
        assertTrue(isRoleAssigned(UserTestInit.getEveryoneRoleName(), bulkUploadUsers[0]));
        assertTrue(isRoleAssigned(UserTestInit.getEveryoneRoleName(), bulkUploadUsers[1]));
        assertTrue(isRoleAssigned(UserTestInit.getEveryoneRoleName(), bulkUploadUsers[2]));

        new UserManagementCommand(userAdminStub).bulkImportExecuteFailureCase(bulkImportFiles[2],
                Util.buildDataHandler(getBytesFromFile(bulkImportFiles[2])), validPassword);
        assertTrue(isRoleAssigned(UserTestInit.getEveryoneRoleName(), bulkUploadUsers[3]));

        new UserManagementCommand(userAdminStub).bulkImportExecuteFailureCase(bulkImportFiles[3],
                Util.buildDataHandler(getBytesFromFile(bulkImportFiles[3])), validPassword);
        assertTrue(isRoleAssigned(UserTestInit.getEveryoneRoleName(), bulkUploadUsers[4]));

        new UserManagementCommand(userAdminStub).bulkImportExecuteFailureCase(bulkImportFiles[4],
                Util.buildDataHandler(getBytesFromFile(bulkImportFiles[4])), "we");
        assertFalse(isRoleAssigned(UserTestInit.getEveryoneRoleName(), bulkUploadUsers[5]));

        new UserManagementCommand(userAdminStub).bulkImportExecuteFailureCase(bulkImportFiles[4],
                Util.buildDataHandler(getBytesFromFile(bulkImportFiles[4])), validPassword);
        assertTrue(isRoleAssigned(UserTestInit.getEveryoneRoleName(), bulkUploadUsers[5]));

        for(String users : bulkUploadUsers){
            new UserManagementCommand(userAdminStub).deleteUserExecuteSuccessCase(users);
        }        
    }

    public void updateRolesOfUser() {
        new UserManagementCommand(userAdminStub).updateRolesOfUserExecuteSuccessCase(
                UserTestInit.getUser1(), new String[] {UserTestInit.getEveryoneRoleName(),
                        UserConstants.INTERNAL_ROLE_NAME1});
        assertTrue(isRoleAssigned(UserConstants.INTERNAL_ROLE_NAME1, UserTestInit.getUser1()));
        new UserManagementCommand(userAdminStub).updateRolesOfUserExecuteSuccessCase(
                UserTestInit.getUser1(), new String[] {UserTestInit.getEveryoneRoleName(),
                        UserConstants.INTERNAL_ROLE_NAME2});
        assertTrue(isRoleAssigned(UserConstants.INTERNAL_ROLE_NAME2, UserTestInit.getUser1()));
        assertFalse(isRoleAssigned(UserConstants.INTERNAL_ROLE_NAME1, UserTestInit.getUser1()));
    }

    public void invalidUpdateRolesOfUser() {
        new UserManagementCommand(userAdminStub).updateRolesOfUserExecuteFailureCase(
                UserTestInit.getUser1(), new String[] {UserConstants.INTERNAL_ROLE_NAME1});
        new UserManagementCommand(userAdminStub).updateRolesOfUserExecuteFailureCase(
                UserTestInit.getAdminUserName(), new String[] {UserConstants.INTERNAL_ROLE_NAME1});
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

    private byte[] getBytesFromFile(String filePath)  {
        File file = new File(filePath);
        byte[] bytes = null;
        try{
            InputStream is = new FileInputStream(file);
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                return null;
            }
            bytes = new byte[(int)length];

            int offset = 0;
            int numRead = 0;
            while ( (offset < bytes.length)
                    &&
                    ( (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) ) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
            is.close();
        } catch(IOException e){

        }
        return bytes;
    }

}

