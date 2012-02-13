package org.wso2.carbon.test.setup.config;

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

/*Before running DBLookup/DBReport samples (sample 360, 361, 362) need to setup db using this class. */

public class DerbySetup extends TestTemplate {
    private static final Log log = LogFactory.getLog(DerbySetup.class);
    private Connection conn = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    @Override
    public void init() {


        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            conn = DriverManager
                    .getConnection("jdbc:derby://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.DERBY_PORT + "/esbdb;user=esb;password=esb;create=true");

            statement = conn.createStatement();

            statement.executeUpdate("CREATE table company(name varchar(10), id varchar(10), price double)");

            statement.executeUpdate("INSERT into company values ('IBM','c1',0.0)");
            statement.executeUpdate("INSERT into company values ('SUN','c2',0.0)");
            statement.executeUpdate("INSERT into company values ('MSFT','c3',0.0)");

        } catch (Exception e) {
            log.error("Driver not loaded " + e);
            System.out.println("Driver not loaded " + e);


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
            log.error("cannot connect to the db server " + e);
            System.err.println(e + "Cannot connect to database server");
        }

        finally {
            if (conn != null) {
                try {
                    conn.close();
                    log.info("connection closed");
                    System.out.println("Database connection terminated");

                }
                catch (Exception e) {
                    log.error("Error occurred while closing the db connection " + e);
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
