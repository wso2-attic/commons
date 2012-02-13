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

import javax.sql.DataSource;

/**
 * Configuration Bean to be used in the DefaultRealm.
 * Contains all the data that is needed to initialize the DefaultRealm
 */
public class DefaultRealmConfig {

    /**
     * This algorithm checks users permission only when validating
     * User Authorizations
     */
    public static final String PERMISSION_USER_ONLY = "USER_ONLY";

    /**
     * This algorithm also checks role authorization when validating
     * User Authorization
     */
    public static final String PERMISSION_BLOCK_FIRST = "BLOCK_FIRST";

    /**Database connection user name*/
    private String connectionUserName = null;

    /**Database connection url*/
    private String connectionURL = null;

    /**Database connection password*/
    private String connectionPassword = null;

    /**Database driver*/
    private String driverName = null;

    /**DBCP data source*/
    private DataSource dataSource = null;

    /**The algorithm holds the permission algorithm*/
    private String permissionAlgo = PERMISSION_BLOCK_FIRST;

    /**Maximum connection count*/
    private int maxConnectionsCount = 5;

    
    public DefaultRealmConfig() {

    }

    public DefaultRealmConfig(DefaultRealmConfig config) {
        connectionUserName = new String(config.getConnectionUserName());
        connectionURL = new String(config.getConnectionURL());
        connectionPassword = new String(config.getConnectionPassword());
        driverName = new String(config.getDriverName());

    }

    public String getConnectionPassword() {
        return connectionPassword;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public String getConnectionUserName() {
        return connectionUserName;
    }

    public void setConnectionPassword(String connectionPassword) {
        this.connectionPassword = connectionPassword;
    }

    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public void setConnectionUserName(String connectionUserName) {
        this.connectionUserName = connectionUserName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getPermissionAlgo() {
        return permissionAlgo;
    }

    public void setPermissionAlgo(String permissionAlgo) {
        this.permissionAlgo = permissionAlgo;
    }

    public int getMaxConnectionsCount() {
        return maxConnectionsCount;
    }

    public void setMaxConnectionsCount(int maxConnectionsCount) {
        this.maxConnectionsCount = maxConnectionsCount;
    }

  

}
