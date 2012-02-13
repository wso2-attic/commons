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

import org.wso2.usermanager.Realm;

/**
 * RealmConfig Bean for AuthorizingRealm
 */
public class AuthorizingRealmConfig {

    /** Realm that is used to get information */
    private Realm realm = null;

    /** Authenticated user */
    private String authenticatedUserName = null;

    /** Admin role name */
    private String adminRoleName = null;

    /** Can the current user read himself */
    private boolean isCurrentUserReadable = false;

    /** Can the current user edit himself */
    private boolean isCurrentUserEditable = false;

    /** Should I enable admin behavior */
    private boolean enableAdminBehavior = false;

    public AuthorizingRealmConfig() {

    }

    public AuthorizingRealmConfig(AuthorizingRealmConfig config) {
        this.authenticatedUserName = new String(config
                .getAuthenticatedUserName());
        this.realm = config.getRealm();
        this.isCurrentUserEditable = config.isCurrentUserEditable();
        this.isCurrentUserReadable = config.isCurrentUserReadable();
        this.enableAdminBehavior = config.isEnableAdminBehavior();
        this.adminRoleName = new String(config.getAdminRoleName());
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public String getAuthenticatedUserName() {
        return authenticatedUserName;
    }

    public void setAuthenticatedUserName(String authenticatedUserName) {
        this.authenticatedUserName = authenticatedUserName;
    }

    /**
     * Users with Admin Role cannot be deleted, edited, read by other normal
     * users. If a user has the Admin Role he will be authorized to do anything.
     * i.e. isUserAuthorized() method will return true to every resource/action.
     * Admin role users can be edited/deleted and read by other admin role users
     * only.
     * 
     * @param adminRoleName
     */
    public void setAdminRoleName(String adminRoleName) {
        this.adminRoleName = adminRoleName;
    }

    public String getAdminRoleName() {
        return adminRoleName;
    }

    /**
     * Current user permission on himself when reading data
     */
    public boolean isCurrentUserReadable() {
        return isCurrentUserReadable;
    }

    /**
     * Current user permission on himself when editing data
     */
    public boolean isCurrentUserEditable() {
        return isCurrentUserEditable;
    }

    public void setCurrentUserReadable(boolean isCurrentUserReadable) {
        this.isCurrentUserReadable = isCurrentUserReadable;
    }

    public void setCurrentUserEditable(boolean isCurrentUserEditable) {
        this.isCurrentUserEditable = isCurrentUserEditable;
    }

    public boolean isEnableAdminBehavior() {
        return enableAdminBehavior;
    }

    public void setEnableAdminBehavior(boolean enableAdminBehavior) {
        this.enableAdminBehavior = enableAdminBehavior;
    }
}
