/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.system.test.core.utils.dbUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;

public class DatabaseFactory {
    private static final Log log = LogFactory.getLog(DatabaseFactory.class);

    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String H2_DRIVER = "org.h2.Driver";

    public static DatabaseManager getDatabaseConnector(String databaseDriver, String jdbcUrl, String userName, String passWord) throws ClassNotFoundException, SQLException {

        if (MYSQL_DRIVER.equalsIgnoreCase(databaseDriver)) {
            return new MySqlDatabaseManager(jdbcUrl, userName, passWord);

        } else if (ORACLE_DRIVER.equalsIgnoreCase(databaseDriver)) {
            return new OracleDatabaseManager(jdbcUrl, userName, passWord);

        } else {
            log.warn("No implementation for " + databaseDriver + " Database Connection");
            return null;
        }

    }
}
