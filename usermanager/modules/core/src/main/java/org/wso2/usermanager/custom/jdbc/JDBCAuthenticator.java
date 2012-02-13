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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.usermanager.Authenticator;
import org.wso2.usermanager.UserManagerException;

/**
 * 
 * @see org.wso2.usermanager.Authenticator
 *
 */
public class JDBCAuthenticator implements Authenticator {

    private static Log log = LogFactory.getLog(JDBCAuthenticator.class);

    protected JDBCRealmConfig config = null;

    protected Connection dbConnection = null;

    public JDBCAuthenticator(JDBCRealmConfig config, Connection conn) {
        this.config = config;
        this.dbConnection = conn;
    }

    /**
     * @see org.wso2.usermanager.Authenticator#authenticate(String, Object)
     */
    public boolean authenticate(String userName, Object credentials)
            throws UserManagerException {
        if (credentials == null) {
            return false;
        }

        if (userName == null) {
            return false;
        }

        boolean bValue = false;

        if (!(credentials instanceof String)) {
            throw new UserManagerException(
                    "Can handle only string type credentials");
        } else {
            try {
                String password = (String) credentials;
                String stmtValue = this.constructSQLtoReadPassword(userName);

                if (log.isDebugEnabled()) {
                    log.debug(stmtValue);
                }

                Statement stmt = dbConnection.createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);

                ResultSet rs = stmt.executeQuery(stmtValue);

                if (rs.first() == true) {
                    String value = rs.getString(this.config
                            .getUserCredentialColumn());
                    if ((value != null) && (value.equals(password))) {
                        bValue = true;
                    }
                }
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                throw new UserManagerException("exceptionOnAuthenticate");
            }
        }
        return bValue;
    }

    private String constructSQLtoReadPassword(String username) {
        StringBuffer sbuff = new StringBuffer("select ");
        sbuff.append(this.config.getUserCredentialColumn());
        sbuff.append(" from ");
        sbuff.append(this.config.getUserTable());
        sbuff.append(" where ");
        sbuff.append(this.config.getUserNameColumn());
        sbuff.append(" = '");
        sbuff.append(username);
        sbuff.append("'");

        return sbuff.toString();
    }

}
