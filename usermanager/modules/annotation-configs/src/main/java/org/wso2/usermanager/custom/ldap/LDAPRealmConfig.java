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

import org.wso2.usermanager.config.RealmConfigParameterInfo;
import org.wso2.usermanager.config.UserManagerConfigException;

public class LDAPRealmConfig {

    private String connectionUrl = null;

    private String connectionName = null;

    private String connectionPass = null;

    private String attributeIds = null;

    private String userPattern = null;

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

    @RealmConfigParameterInfo(isRequired = true, getHelpText = "e.g. ldap://localhost:389")
    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl.trim();
    }

    @RealmConfigParameterInfo(isRequired = true, getHelpText = "e.g. cn=root,dc=wso2,dc=com")
    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName.trim();
    }

    @RealmConfigParameterInfo(isRequired = true, getHelpText = "Password of the connection user name")
    public void setConnectionPass(String connectionPass) {
        this.connectionPass = connectionPass.trim();
    }

    @RealmConfigParameterInfo(isRequired = true, getHelpText = "e.g. uid={0},dc=wso2,dc=com")
    public void setUserPattern(String userPattern) {
        this.userPattern = userPattern.trim();
    }

    @RealmConfigParameterInfo(isRequired = false, getHelpText = "Comma separated attribute Ids - e.g. email, age")
    public void setAttributeIds(String attributeIds) {
        this.attributeIds = attributeIds.trim();
    }

    @RealmConfigParameterInfo(isRequired = false, getHelpText = "Directory where users reside - e.g. dc=wso2,dc=com")
    public void setUserContextName(String userContextName) {
        this.userContextName = userContextName.trim();
    }

    public void validate() throws UserManagerConfigException {
        if (connectionName == null || connectionPass == null
                || connectionUrl == null || userPattern == null) {
            throw new UserManagerConfigException("ldapConfigValidation");
        }

        if (connectionName.equals("") || connectionPass.equals("")
                || connectionUrl.equals("") || userPattern.equals("")) {
            throw new UserManagerConfigException("ldapConfigValidation");
        }
    }

}
