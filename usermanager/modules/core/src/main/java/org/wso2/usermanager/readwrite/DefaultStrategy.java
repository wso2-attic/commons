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
package org.wso2.usermanager.readwrite;

import org.wso2.usermanager.UserManagerException;

/**
 * Method in this class dominate how the DefaultRealm behaves. This enables
 * different types of Realms to be written. For example users can be pulled out
 * from tables with different names. Sample implementation is WSASRealm.
 * 
 * Thrives to achieve loose coupling.
 */
public interface DefaultStrategy {

    /**
     * Retrives the column name given the column Id
     */
    public String getColumnName(int columnID);

    /**
     * Retrieves the authentication SQL
     */
    public String getAuthenticatorSQL(int sqlID);

    /**
     * Retrieves the SQL to perform User Store administration.
     */
    public String getUserStoreAdminSQL(int sqlID);

    /**
     * Retrieves the Common SQL
     */
    public String getCommonSQL(int sqlID);

    /**
     * Retrieves the Access Control Admin SQL
     */
    public String getAccessControlAdminSQL(int sqlID);

    /**
     * Retrieves the Authorizer SQL
     */
    public String getAuthorizerSQL(int sqlID);

    /**
     * Retrieves the User Store Reader SQL
     */
    public String getUserStoreReaderSQL(int sqlID);

    /**
     * Retrieves the User ID
     */
    public String getUserId(String userName) throws UserManagerException;

    /**
     * Retrieves the Role ID
     */
    public String getRoleId(String roleName) throws UserManagerException;

    /**
     * Retrieves the User Roles
     */
    public String[] getUserRoles(String userName) throws UserManagerException;

    
}
