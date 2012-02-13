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

package org.wso2.carbon.system.test.core;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.base.ServerConfigurationException;
import org.wso2.carbon.system.test.core.utils.dbUtils.DatabaseFactory;
import org.wso2.carbon.system.test.core.utils.dbUtils.DatabaseManager;
import org.wso2.carbon.system.test.core.utils.productUtils.PackageCreator;
import org.wso2.carbon.system.test.core.utils.serverUtils.ServerManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerGroupManager {
    private static final Log log = LogFactory.getLog(ServerGroupManager.class);

    private static boolean serversRunning = false;

    private static List<ServerManager> serverManagerList = new ArrayList<ServerManager>();

    public static synchronized void startServers() {
        log.info("Server starting...");

        if (!serversRunning) {
            FrameworkSettings.getFrameworkProperties();
            Assert.assertNotNull("Product List is not provided", FrameworkSettings.PRODUCT_LIST_TO_START);
            Assert.assertNotNull("Deployment Framework Home not provided", FrameworkSettings.DEPLOYMENT_FRAMEWORK_HOME);
            Assert.assertTrue("Deployment Framework Script Execution Failed", PackageCreator.createPackage());
            Assert.assertNotNull("System mount Database Name null", FrameworkSettings.SYSTEM_MOUNT_DATABASE);
            Assert.assertFalse("Invalid System Mount Database Name", FrameworkSettings.SYSTEM_MOUNT_DATABASE.equals(""));
            Assert.assertNotNull("System Mount Database Type null", FrameworkSettings.DATABASE_TYPE);
            Assert.assertFalse("Invalid System Mount Database Type", FrameworkSettings.DATABASE_TYPE.equals(""));
            ServerManager serverManager = null;
            DatabaseManager databaseManager;
            String[] productList = FrameworkSettings.PRODUCT_LIST_TO_START.split(",");


            try {
                databaseManager = DatabaseFactory.getDatabaseConnector(FrameworkSettings.DATABASE_TYPE, FrameworkSettings.JDBC_URL, FrameworkSettings.DB_USER, FrameworkSettings.DB_PASSWORD);
                databaseManager.executeUpdate("DROP DATABASE IF EXISTS " + FrameworkSettings.SYSTEM_MOUNT_DATABASE);
                databaseManager.executeUpdate("CREATE DATABASE " + FrameworkSettings.SYSTEM_MOUNT_DATABASE);
                log.info("wso2registry created");
                databaseManager.disconnect();

                for (String product : productList) {
                    serverManager = new ServerManager(ProductConstant.getCarbonHome(product));
                    serverManager.start();
                    serversRunning = true;
                    serverManagerList.add(serverManager);
                }

            } catch (ServerConfigurationException e) {
                log.error("Server configuration error " + e.getMessage());
                Assert.fail("Server configuration error " + e.getMessage());
            } catch (ClassNotFoundException e) {
                log.error("Database Driver Not Found " + e.getMessage());
                Assert.fail("Database Driver Not Found " + e.getMessage());
            } catch (SQLException e) {
                log.error("Database Server connection failed " + e.getMessage());
                Assert.fail("Database Server connection failed " + e.getMessage());
            }

            //create users in each server

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        log.info("Shutting down servers...");
                        for (ServerManager sm : serverManagerList) {
                            sm.shutdown();
                            log.info("Shutting down Server");
                        }
                        serverManagerList.clear();
                    } catch (Exception e) {
                        log.error(e);
                    }
                }
            });
        }
    }
}