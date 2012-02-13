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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.UserStoreReader;

/**
 * 
 * @see org.wso2.usermanager.UserStoreReader
 *
 */
public class JDBCUserStoreReader implements UserStoreReader {

    private static Log log = LogFactory.getLog(JDBCUserStoreReader.class);

    protected JDBCRealmConfig config = null;

    protected Connection dbConnection = null;

    public JDBCUserStoreReader(JDBCRealmConfig config, Connection conn) {
        this.config = config;
        this.dbConnection = conn;
    }

    /**
     * Gets all user properties
     */
    public Map getUserProperties(String userName) throws UserManagerException {
        return readProperties(userName, config.getUserNameColumn(), config
                .getUserTable());
    }

    /**
     * Gets all property names
     */
    public String[] getUserPropertyNames() throws UserManagerException {
        String colNames = config.getColumnNames();
        String[] attrIds = colNames.split(",");
        return attrIds;
    }

    /**
     * Gets all usernames
     */
    public String[] getAllUserNames() throws UserManagerException {
        try {
            String stmtValue = constructSQLtoGetAllUsers();
            Statement stmt = dbConnection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery(stmtValue);

            List lst = new ArrayList();
            String name = null;
            while (rs.next() == true) {
                name = rs.getString(config.getUserNameColumn());
                lst.add(name);
            }
            return (String[]) lst.toArray(new String[lst.size()]);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new UserManagerException("exceptionOnAuthenticate");
        }
    }
    
    public Map getUserProfileProperties(String username, String profileName)
        throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }
    
    public boolean isExistingUser(String userName) throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }
    
    public boolean isExistingRole(String roleName) throws UserManagerException{
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public String[] getUserNamesWithPropertyValue(String propertyName,
            String propetyValue) throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public Map getRoleProperties(String roleName) throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public String[] getAllRoleNames() throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public String[] getUserRoles(String userName) throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public String[] getUsersInRole(String roleName) throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    //  *************************************************************************************

    protected String[] selectNames(String entityNameCol, String entityTable)
            throws UserManagerException {
        // SELECT ColumnName FROM TableName
        String[] names = new String[0];
        StringBuffer sbuff = new StringBuffer();
        sbuff.append("Select ");
        sbuff.append(entityNameCol);
        sbuff.append(" from ");
        sbuff.append(entityTable);

        String strStmt = sbuff.toString();
        try {
            log.debug(strStmt);
            Statement stmt = dbConnection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery(strStmt);
            if (rs.first() == false) {
                return names;
            }

            List lst = new ArrayList();
            while (rs.next()) {
                String name = rs.getString(entityNameCol);
                lst.add(name);
            }
            names = (String[]) lst.toArray(new String[lst.size()]);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return names;
    }

    protected Map readProperties(String entityName, String entityNameCol,
            String entityTable) throws UserManagerException {
        Map props = new HashMap();
        try {

            String strStmt = constructSQLToReadAttributes(entityName,
                    entityNameCol, entityTable);
            log.debug(strStmt);
            Statement stmt = dbConnection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery(strStmt);

            if (rs.first() == false) {
                return props;
            }

            String colNames = config.getColumnNames();
            String[] attrIds = colNames.split(",");
            ResultSetMetaData mf = rs.getMetaData();
            for (int i = 0; i < attrIds.length; i++) {

                if (attrIds[i].equals(this.config.getUserCredentialColumn())) {
                    continue;
                }

                int index = i + 1; // rs.findColumn(attrIds[i]);
                String colName = mf.getColumnName(index);
                int iType = mf.getColumnType(index);

                switch (iType) {
                case Types.INTEGER: {
                    int value = rs.getInt(index);
                    props.put(colName, Integer.toString(value));
                }
                    break;
                case Types.DOUBLE: {
                    double value = rs.getDouble(index);
                    props.put(colName, Double.toString(value));
                }
                    break;
                case Types.FLOAT: {
                    float value = rs.getFloat(index);
                    props.put(colName, Float.toString(value));
                }
                    break;
                case Types.BOOLEAN: {
                    boolean value = rs.getBoolean(index);
                    props.put(colName, Boolean.toString(value));
                }
                case Types.VARCHAR: {
                    String value = rs.getString(index);
                    props.put(colName, value);
                }
                    break;
                default: {
                    // assert false : "Unsupported type";
                }
                }

            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }

        return props;

    }

    private String constructSQLToReadAttributes(String entityName,
            String entityNameCol, String entityTable)
            throws UserManagerException {
        StringBuffer sbuff = new StringBuffer("select ");
        String colNames = config.getColumnNames();
        String[] attrIds = colNames.split(",");
        for (int i = 0; i < attrIds.length; i++) {
            if (attrIds[i].equals(this.config.getUserCredentialColumn())) {
                continue;
            }
            sbuff.append(attrIds[i]);
            sbuff.append(", ");
        }
        sbuff.deleteCharAt(sbuff.length() - 1);
        sbuff.deleteCharAt(sbuff.length() - 1);
        sbuff.append(" from ");
        sbuff.append(entityTable);
        sbuff.append(" where ");
        sbuff.append(entityNameCol);
        sbuff.append(" = '");
        sbuff.append(entityName);
        sbuff.append("'");

        return sbuff.toString();
    }

    private String constructSQLtoGetAllUsers() {
        StringBuffer sbuff = new StringBuffer("select ");
        sbuff.append(this.config.getUserNameColumn());
        sbuff.append(" from ");
        sbuff.append(this.config.getUserTable());
        return sbuff.toString();
    }

}
