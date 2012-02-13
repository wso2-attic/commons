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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.usermanager.Authenticator;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.readwrite.util.DefaultStrategyImpl;
import org.wso2.usermanager.util.Base64;

/**
 * @see org.wso2.usermanager.Authenticator
 */
public class DefaultAuthenticator implements Authenticator {

    private static Log log = LogFactory.getLog(DefaultAuthenticator.class);

    protected DataSource dataSource = null;

    protected DefaultRealm realm = null;

    protected DefaultStrategy data = null;

    public DefaultAuthenticator(DataSource dataSource) {
        this.dataSource = dataSource;
        this.data = new DefaultStrategyImpl(dataSource);
    }

    public DefaultAuthenticator(DataSource dataSource, DefaultStrategy store) {
        this.dataSource = dataSource;
        if (store != null) {
            this.data = store;
        } else {
            this.data = new DefaultStrategyImpl(dataSource);
        }
    }

    /**
     * @see org.wso2.usermanager.Authenticator#authenticate(String, Object)
     */
    public boolean authenticate(String userName, Object credentials)
            throws UserManagerException {
        boolean isAuth = false;
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (dbConnection == null) {
                throw new UserManagerException("null_connection");
            }
            dbConnection.setAutoCommit(false);
            PreparedStatement getUserStmt = dbConnection.prepareStatement(data
                    .getAuthenticatorSQL(DefaultRealmConstants.GET_USER));
            getUserStmt.setString(1, userName);
            ResultSet rs = getUserStmt.executeQuery();
            if (rs.next()) {
                if (credentials != null) {
                    MessageDigest dgst = MessageDigest.getInstance("MD5");
                    dgst.update(((String) credentials).getBytes());
                    String dbCred = rs
                            .getString(data
                                    .getColumnName(DefaultRealmConstants.COLUMN_ID_CREDENTIAL));
                    isAuth = Base64.encode(dgst.digest()).equals(dbCred);
                }
            }
            getUserStmt.close();
        } catch (SQLException e) {
            log.debug(e);
            throw new UserManagerException("errorReadingFromUserStore", e);
        } catch (NoSuchAlgorithmException e) {
            log.debug(e);
            throw new UserManagerException("errorCreatingPasswordDigest", e);
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                throw new UserManagerException("errorClosingConnection", e);
            }
        }
        return isAuth;
    }

    public DefaultStrategy getData() {
        return data;
    }

    public void setData(DefaultStrategy data) {
        this.data = data;
    }

}
