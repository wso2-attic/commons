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

package org.wso2.usermanager.custom.jdbc;

import org.wso2.usermanager.config.RealmConfigParameterInfo;
import org.wso2.usermanager.config.UserManagerConfigException;

public class JDBCRealmConfig {

    protected String userTable = null;

    protected String userNameColumn = null;

    protected String userCredentialColumn = null;

    protected String connectionURL = null;

    protected String driverName = null;

    protected String connectionUserName = null;

    protected String connectionPassword = null;

    protected String columnNames = null;

    public JDBCRealmConfig() {

    }

    public JDBCRealmConfig(JDBCRealmConfig config) {
        userTable = new String(config.getUserTable());
        userNameColumn = new String(config.getUserNameColumn());
        userCredentialColumn = new String(config.getUserCredentialColumn());
        connectionURL = new String(config.getConnectionURL());
        driverName = new String(config.getDriverName());
        connectionUserName = new String(config.getConnectionUserName());
        connectionPassword = new String(config.getConnectionPassword());
        columnNames = config.columnNames;

    }

    public String getColumnNames() {
        return columnNames;
    }

    public String getConnectionUserName() {
        return connectionUserName;
    }

    public String getConnectionPassword() {
        return connectionPassword;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public String getDriverName() {
        if (driverName == null || driverName.equals("")) {
            return "org.apache.derby.jdbc.EmbeddedDriver";
        } else {
            return driverName;
        }
    }

    public String getUserTable() {
        return userTable;
    }

    public String getUserCredentialColumn() {
        return userCredentialColumn;
    }

    public String getUserNameColumn() {
        return userNameColumn;
    }

    public void validate() throws UserManagerConfigException {
        if ((userTable == null) || (userNameColumn == null)
                || (userCredentialColumn == null) || connectionURL == null) {
            throw new UserManagerConfigException("jdbcConfigValidation");
        }
        if (userTable.equals("") || userNameColumn.equals("")
                || userCredentialColumn.equals("") || connectionURL.equals("")) {
            throw new UserManagerConfigException("jdbcConfigValidation");
        }
    }

    @RealmConfigParameterInfo(isRequired = true, getHelpText = "e.g. jdbc:derby:home/identity/database/USER_DB")
    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL.trim();
    }

    @RealmConfigParameterInfo(isRequired = false, getHelpText = "Username of the connection, if there is one")
    public void setConnectionUserName(String connectionName) {
        this.connectionUserName = connectionName.trim();
    }

    @RealmConfigParameterInfo(isRequired = false, getHelpText = "Password of the connection, if there is one")
    public void setConnectionPassword(String connectionPassword) {
        this.connectionPassword = connectionPassword.trim();
    }

    @RealmConfigParameterInfo(isRequired = true, getHelpText = "Name of the users table - e.g. user_table")
    public void setUserTable(String userTable) {
        this.userTable = userTable.trim();
    }

    @RealmConfigParameterInfo(isRequired = true, getHelpText = "User name column")
    public void setUserNameColumn(String userNameCol) {
        this.userNameColumn = userNameCol.trim();
    }

    @RealmConfigParameterInfo(isRequired = true, getHelpText = "Password column of user table")
    public void setUserCredentialColumn(String userCredentialCol) {
        this.userCredentialColumn = userCredentialCol.trim();
    }

    @RealmConfigParameterInfo(isRequired = false, getHelpText = "Driver name - e.g. org.apache.derby.jdbc.EmbeddedDriver")
    public void setDriverName(String driverName) {
        this.driverName = driverName.trim();
    }

    @RealmConfigParameterInfo(isRequired = false, getHelpText = "Comma separated columns in the user table - e.g. email, age")
    public void setColumnNames(String columnNames) {
        this.columnNames = columnNames.trim();
    }

}