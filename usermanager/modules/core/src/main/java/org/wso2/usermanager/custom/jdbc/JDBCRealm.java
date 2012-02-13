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

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.usermanager.AccessControlAdmin;
import org.wso2.usermanager.Authenticator;
import org.wso2.usermanager.Authorizer;
import org.wso2.usermanager.Realm;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.UserStoreAdmin;
import org.wso2.usermanager.UserStoreReader;

/**
 * @see org.wso2.usermanager.Realm 
 */
public class JDBCRealm implements Realm {

    private static Log log = LogFactory.getLog(JDBCRealm.class);

    protected JDBCRealmConfig config = null;

    protected Driver driver = null;

    protected Connection dbConnection = null;

    public JDBCRealm() {

    }

    public Object getRealmConfiguration() throws UserManagerException {
        JDBCRealmConfig retConfig = null;
        if (config == null) {
            retConfig = new JDBCRealmConfig();
        } else {
            retConfig = new JDBCRealmConfig(config);
        }
        return retConfig;
    }

    public void init(Object configBean) throws UserManagerException {
        if (!(configBean instanceof JDBCRealmConfig)) {
            return;
        }
        this.config = (JDBCRealmConfig) configBean;
        open();
    }

    /**
     * Open (if necessary) and return a database connection for use by this
     * Realm.
     * 
     * @exception SQLException
     *                if a database error occurs
     */
    private Connection open() throws UserManagerException {

        if (dbConnection != null)
            return (dbConnection);

        if (this.config.getConnectionURL() == null) {
            throw new UserManagerException("Connection URL is null");
        }

        if (driver == null) {
            try {
                Class clazz = Class.forName(config.getDriverName());
                driver = (Driver) clazz.newInstance();
            } catch (Throwable e) {
                log.debug(e.getMessage(), e);
                e.printStackTrace();
                SQLException exc = new SQLException();
                if (exc.getNextException() != null) {
                    Exception nextExecption = exc.getNextException();
                    log.debug("exceptionOnConnectionOpen",
                            nextExecption);
                }
                throw new UserManagerException("exceptionOnConnectionOpen", e);
            }
        }

        try {
            Properties props = new Properties();
            if (config.getConnectionUserName() != null)
                props.put("user", config.getConnectionUserName());
            if (config.getConnectionPassword() != null)
                props.put("password", config.getConnectionPassword());
            dbConnection = driver.connect(config.getConnectionURL(), props);
            dbConnection.setAutoCommit(false);
            return (dbConnection);
        } catch (SQLException e) {
            log.debug(e.getMessage(), e);
            throw new UserManagerException("exceptionOnConnectionOpen", e);
        }

    }

    public Authenticator getAuthenticator() throws UserManagerException {
        JDBCAuthenticator authenticator = new JDBCAuthenticator(config,
                dbConnection);
        return authenticator;
    }

    public UserStoreReader getUserStoreReader() throws UserManagerException {
        JDBCUserStoreReader reader = new JDBCUserStoreReader(config,
                dbConnection);
        return reader;
    }

    public UserStoreAdmin getUserStoreAdmin() throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public AccessControlAdmin getAccessControlAdmin()
            throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public Authorizer getAuthorizer() throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

}
