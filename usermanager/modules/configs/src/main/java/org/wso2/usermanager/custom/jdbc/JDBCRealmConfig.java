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

/**
 * Configuration Bean to be used in the JDBCRealmConfig. Contains all the data
 * that is needed to initialize the JDBCRealmConfig
 */
public class JDBCRealmConfig {

    /** User table name */
    protected String userTable = null;

    /** User name column */
    protected String userNameColumn = null;

    /** User credential column */
    protected String userCredentialColumn = null;

    /** User connection URL */
    protected String connectionURL = null;

    /** Driver name */
    protected String driverName = null;

    /** Connection User Name */
    protected String connectionUserName = null;

    /** Connection Password */
    protected String connectionPassword = null;

    /** Column name */
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
        return driverName;
    }

    public String getUserCredentialColumn() {
        return userCredentialColumn;
    }

    public String getUserNameColumn() {
        return userNameColumn;
    }

    public String getUserTable() {
        return userTable;
    }

    public void setColumnNames(String columnNames) {
        this.columnNames = columnNames;
    }

    public void setConnectionUserName(String connectionName) {
        this.connectionUserName = connectionName;
    }

    public void setConnectionPassword(String connectionPassword) {
        this.connectionPassword = connectionPassword;
    }

    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setUserCredentialColumn(String userCredentialCol) {
        this.userCredentialColumn = userCredentialCol;
    }

    public void setUserNameColumn(String userNameCol) {
        this.userNameColumn = userNameCol;
    }

    public void setUserTable(String userTable) {
        this.userTable = userTable;
    }

}