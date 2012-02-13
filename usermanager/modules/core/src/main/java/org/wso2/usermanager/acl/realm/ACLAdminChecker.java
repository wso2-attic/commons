/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.usermanager.acl.realm;

import java.util.ArrayList;
import java.util.List;

import org.wso2.usermanager.Realm;
import org.wso2.usermanager.UserManagerException;

/**
 * Checks whether a specific user is an admin. The list is Loaded once and read
 * my times. This is loaded at initialization.
 */
public class ACLAdminChecker {

    /** Holds the list of Admins */
    private static List adminUsers = new ArrayList();

    /**
     * Loads the admin users
     * 
     * @param adminRole Admin role
     * @param realm 
     * @throws UserManagerException
     */
    static void loadAdminUsers(String adminRole, Realm realm)
            throws UserManagerException {
        String[] users = realm.getUserStoreReader().getUsersInRole(adminRole);
        for (int i = 0; i < users.length; i++) {
            adminUsers.add(users[i]);
        }
    }

    /**
     * @param username
     * @return ture if the user is an Admin
     */
    static boolean isAdminUser(String username) {
        return adminUsers.contains(username);
    }
}
