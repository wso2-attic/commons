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

import java.sql.Driver;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.usermanager.AccessControlAdmin;
import org.wso2.usermanager.Authenticator;
import org.wso2.usermanager.Authorizer;
import org.wso2.usermanager.Realm;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.UserStoreAdmin;
import org.wso2.usermanager.UserStoreReader;
import org.wso2.usermanager.readwrite.util.DefaultStrategyImpl;

/**
 * DefaultRealm is used by developers when the need to maintain
 * users from scratch.
 * 
 * @see org.wso2.usermanager.Realm 
 */
public class DefaultRealm implements Realm {

    protected DefaultRealmConfig config = null;

    protected Driver driver = null;

    protected DefaultAuthenticator authenticator = null;

    protected DefaultAuthorizer authorizer = null;

    protected DefaultAccessControlAdmin aclAdmin = null;

    protected DefaultUserStoreAdmin usAdmin = null;

    protected DefaultUserStoreReader usReader = null;

    protected DataSource dataSource = null;

    protected DefaultStrategyImpl strategyObject = null;

    private static Log log = LogFactory.getLog(DefaultRealm.class);

    public DefaultRealm() {
    }

    public Object getRealmConfiguration() throws UserManagerException {
        DefaultRealmConfig retConfig = null;
        if (config == null) {
            retConfig = new DefaultRealmConfig();
        } else {
            retConfig = new DefaultRealmConfig(config);
        }
        return retConfig;
    }

    public void init(Object configBean) throws UserManagerException {
        if (!(configBean instanceof DefaultRealmConfig)) {
            return;
        }
        this.config = (DefaultRealmConfig) configBean;

        if (config.getDataSource() == null) {
            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setUrl(config.getConnectionURL());
            basicDataSource.setDriverClassName(config.getDriverName());
            basicDataSource.setUsername(config.getConnectionUserName());
            basicDataSource.setPassword(config.getConnectionPassword());
            dataSource = basicDataSource;
        } else {
            dataSource = config.getDataSource();
        }

        authenticator = new DefaultAuthenticator(dataSource, strategyObject);
        authorizer = new DefaultAuthorizer(dataSource, config
                .getPermissionAlgo(), strategyObject);
        aclAdmin = new DefaultAccessControlAdmin(dataSource, config
                .getPermissionAlgo(), strategyObject);
        usAdmin = new DefaultUserStoreAdmin(dataSource, strategyObject);
        usReader = new DefaultUserStoreReader(dataSource, strategyObject);

    }

    public AccessControlAdmin getAccessControlAdmin()
            throws UserManagerException {
        return aclAdmin;
    }

    public Authenticator getAuthenticator() throws UserManagerException {
        return authenticator;
    }

    public Authorizer getAuthorizer() throws UserManagerException {
        return authorizer;
    }

    public UserStoreAdmin getUserStoreAdmin() throws UserManagerException {
        return usAdmin;
    }

    public UserStoreReader getUserStoreReader() throws UserManagerException {
        return usReader;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
