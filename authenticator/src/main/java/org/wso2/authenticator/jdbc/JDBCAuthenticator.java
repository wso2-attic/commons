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
package org.wso2.authenticator.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.authenticator.Authenticator;
import org.wso2.authenticator.AuthenticatorException;
import org.wso2.authenticator.help.UserHelpInfo;


/**
 * 
 * @see org.wso2.usermanager.Authenticator
 *
 */
public class JDBCAuthenticator implements Authenticator {

    private static Log log = LogFactory.getLog(JDBCAuthenticator.class);

    protected Connection dbConnection = null;
    
    protected Driver driver = null;
    
    private String userCredentialColumn = null;

    private String userTable = null;

    private String userNameColumn = null;
    
    private String connectionUserName = null;
    
    private String connectionPassword = null;
    
    private String connectionURL = null;
    
    private String driverName = null;

    public JDBCAuthenticator() {
        
    }

    /**
     * @see org.wso2.usermanager.Authenticator#authenticate(String, Object)
     */
    public boolean authenticate(String userName, Object credentials)
            throws AuthenticatorException {
        
        
        if (credentials == null) {
            return false;
        }

        if (userName == null) {
            return false;
        }

        boolean bValue = false;

        if (!(credentials instanceof String)) {
            throw new AuthenticatorException(
                    "Can handle only string type credentials");
        } else {
            try {
                this.open();
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
                    String value = rs.getString(userCredentialColumn);
                    if ((value != null) && (value.equals(password))) {
                        bValue = true;
                    }
                }
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                throw new AuthenticatorException("exceptionOnAuthenticate");
            }
        }
        return bValue;
    }

    private String constructSQLtoReadPassword(String username) {
        StringBuffer sbuff = new StringBuffer("select ");
        sbuff.append(userCredentialColumn);
        sbuff.append(" from ");
        sbuff.append(userTable);
        sbuff.append(" where ");
        sbuff.append(userNameColumn);
        sbuff.append(" = '");
        sbuff.append(username);
        sbuff.append("'");

        return sbuff.toString();
    }
    
    /**
     * Open (if necessary) and return a database connection for use by this
     * Realm.
     * 
     * @exception SQLException
     *                if a database error occurs
     */
    public Connection open() throws AuthenticatorException, SQLException {

        if (dbConnection != null)
            return (dbConnection);

        if (connectionURL == null) {
            throw new AuthenticatorException("Connection URL is null");
        }

        if (driver == null) {
            try {
                Class clazz = Class.forName(driverName);
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
                throw new AuthenticatorException("exceptionOnConnectionOpen", e);
            }
        }

            Properties props = new Properties();
            if (connectionUserName != null)
                props.put("user", connectionUserName);
            if (connectionPassword != null)
                props.put("password", connectionPassword);
            dbConnection = driver.connect(connectionURL, props);
            dbConnection.setAutoCommit(false);
            return (dbConnection);
   
    }
    @UserHelpInfo(isRequired = true, getHelpText = "Password column of user table",
                   getInputType = "text", getLabel= "Password Column")
    public void setUserCredentialColumn(String userCredentialColumn) {
        this.userCredentialColumn = userCredentialColumn;
    }

//all the setters expected to be set by a user has a description    
    @UserHelpInfo(isRequired = true, getHelpText = "Name of the users table - e.g. user_table",
            getInputType = "text", getLabel= "User Table")
    public void setUserTable(String userTable) {
        this.userTable = userTable;
    }
    @UserHelpInfo(isRequired = true, getHelpText = "User name column",
            getInputType = "text", getLabel= "User Name Column")
    public void setUserNameColumn(String userNameColumn) {
        this.userNameColumn = userNameColumn;
    }
    @UserHelpInfo(isRequired = false, getHelpText = "Username of the connection, if there is one",
        getInputType = "text", getLabel= "Connection User Name")
    public void setConnectionUserName(String connectionUserName) {
        this.connectionUserName = connectionUserName;
    }
    @UserHelpInfo(isRequired = false, getHelpText = "Password of the connection, if there is one",
            getInputType = "password", getLabel= "Connection Password")
    public void setConnectionPassword(String connectionPassword) {
        this.connectionPassword = connectionPassword;
    }
    
    @UserHelpInfo(isRequired = true, getHelpText = "e.g. jdbc:derby:home/identity/database/USER_DB",
            getInputType = "text", getLabel= "Connection URL")
    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }
    @UserHelpInfo(isRequired = true, getHelpText = "Driver name - e.g. org.apache.derby.jdbc.EmbeddedDriver",
            getInputType = "text", getLabel= "Driver class")
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }


    
    
}
