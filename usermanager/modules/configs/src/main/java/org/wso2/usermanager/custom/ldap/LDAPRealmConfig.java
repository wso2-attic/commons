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

package org.wso2.usermanager.custom.ldap;

/**
 * Configuration Bean to be used in the AcegiRealmConfig Contains all the data
 * that is needed to initialize the AcegiRealmConfig
 */
public class LDAPRealmConfig {

    /** LDAP Connection URL */
    private String connectionUrl = null;

    /** LDAP Connection Name */
    private String connectionName = null;

    /** LDAP Connection Password */
    private String connectionPass = null;

    /** LDAP attribute Ids */
    private String attributeIds = null;

    /** LDAP User search pattern */
    private String userPattern = null;

    /** LDAP User Context name */
    private String userContextName = null;

    public LDAPRealmConfig() {

    }

    public LDAPRealmConfig(LDAPRealmConfig config) {
        connectionUrl = new String(config.getConnectionUrl());
        connectionName = new String(config.getConnectionName());
        connectionPass = new String(config.getConnectionPass());
        userPattern = new String(config.getUserPattern());
        attributeIds = config.getAttributeIds();

    }

    public String getAttributeIds() {
        return attributeIds;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public String getConnectionPass() {
        return connectionPass;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public String getUserPattern() {
        return userPattern;
    }

    public String getUserContextName() {
        return userContextName;
    }

    public void setAttributeIds(String attributeIds) {
        this.attributeIds = attributeIds;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public void setConnectionPass(String connectionPass) {
        this.connectionPass = connectionPass;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public void setUserPattern(String userPattern) {
        this.userPattern = userPattern;
    }

    public void setUserContextName(String userContextName) {
        this.userContextName = userContextName;
    }

}
