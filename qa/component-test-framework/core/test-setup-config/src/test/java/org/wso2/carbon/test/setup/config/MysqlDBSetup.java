package org.wso2.carbon.test.setup.config;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.common.test.utils.TestTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/

/*used in DSS dataservices tests: setting up the database*/

public class MysqlDBSetup extends TestTemplate {
    private static final Log log = LogFactory.getLog(MysqlDBSetup.class);
    private Connection conn = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    @Override
    public void init() {

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager
                    .getConnection("jdbc:mysql://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.MYSQL_PORT + "/employeedb", FrameworkSettings.MYSQL_USER_NAME, FrameworkSettings.MYSQL_PASSWORD);
            statement = conn.createStatement();
            System.out.println("Connection Successful");
            statement.executeUpdate("CREATE TABLE EMPLOYEE(ID VARCHAR(10) NOT NULL PRIMARY KEY, NAME VARCHAR(100), ADDRESS VARCHAR(100));");
            statement.executeUpdate("INSERT INTO EMPLOYEE VALUES('01','john','Boston')");
            statement.executeUpdate("INSERT INTO EMPLOYEE VALUES('02','Micheal','Dallas')");
            statement.executeUpdate("INSERT INTO EMPLOYEE VALUES('03','richard','Chicago')");

        } catch (MySQLSyntaxErrorException e) {
            log.error("Syntax Error: " + e);
            System.out.println(" Unknown Database: " + e);

        } catch (ClassNotFoundException e) {
            log.error("Class Not Found: " + e);
            System.out.println("Class Not Found: " + e);

        } catch (Exception e) {
            log.error("Error occurred " + e);
            System.out.println("Error occurred " + e);

        } finally {
            close();
        }
        try {
            if (resultSet != null) {
                resultSet.close();

            }
            if (statement != null) {
                statement.close();

            }
        }
        catch (Exception e) {
            log.error("Cannot Connect to the MYSQL DB Server: " + e);
            System.err.println("Cannot Connect to MYSQL DB server: " + e);

        }

        finally {
            if (conn != null) {
                try {
                    conn.close();
                    log.info("Connection Closed");
                    System.out.println("Database Connection Terminated");

                }
                catch (Exception e) {
                    log.error("Error occurred while closing the DB connection " + e);
                }
            }
        }
    }


    @Override
    public void runSuccessCase() {

    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }


    private void close() {


    }

}
