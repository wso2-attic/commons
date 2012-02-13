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
package org.wso2.usermanager;

/**
 * The parent interface
 * 
 */
public interface Realm {

    /**
     * Returns the realm configuration bean.
     * 
     * @throws UserManagerException
     */
    public Object getRealmConfiguration() throws UserManagerException;

    /**
     * Initializes the realm using the properties from the configuration bean
     * passed in.
     * 
     * @throws UserManagerException
     */
    public void init(Object configBean) throws UserManagerException;

    /**
     * The returned object can perform authentication checks
     * 
     * @throws UserManagerException
     */
    public Authenticator getAuthenticator() throws UserManagerException;

    /**
     * The returned object can perform authorization checks
     * 
     * @throws UserManagerException
     */
    public Authorizer getAuthorizer() throws UserManagerException;

    /**
     * The returned object can read data from user store
     */
    public UserStoreReader getUserStoreReader() throws UserManagerException;

    /**
     * The returned object can manipulate the user store
     * 
     * @throws UserManagerException
     */
    public UserStoreAdmin getUserStoreAdmin() throws UserManagerException;

    /**
     * The returned object can manipulate access controls
     * 
     * @throws UserManagerException
     */
    public AccessControlAdmin getAccessControlAdmin()
            throws UserManagerException;

}
