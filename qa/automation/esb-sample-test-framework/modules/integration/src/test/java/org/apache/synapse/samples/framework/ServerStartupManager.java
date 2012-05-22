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
package org.apache.synapse.samples.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerStartupManager {
    private static final Log log = LogFactory.getLog(ServerStartupManager.class);

    private Process process;
    private Thread consoleLogPrinter;
    private String originalUserDir = null;

    private static String carbonHome;

    public ServerStartupManager() {
        PropertyLoader.getProperties();
        this.carbonHome = PropertyLoader.CARBON_HOME;
    }

    private final static String SERVER_STARTUP_MESSAGE = "WSO2 Carbon started in";
    private final String CARBON_HOME = carbonHome;
    private final static String SERVER_SHUTDOWN_MESSAGE = "Halting JVM";
    private final static long DEFAULT_START_STOP_WAIT_MS = 1000 * 60 * 4;

    public synchronized void start(int sampleNumber) throws Exception {
        int sampleNo = sampleNumber;
        System.out.println("/n");
        System.out.println("STARTING SAMPLE NUMBER : " + sampleNumber);
        System.out.println("/n");

        //ToDO comment it
        if (sampleNo == 12) {
            sampleNo = 0;
        }
        if (sampleNo == 13) {
            sampleNo = 0;
        }
        String CARBON_HOME = PropertyLoader.CARBON_HOME;
        if (process != null) { // An instance of the server is running
            return;
        }
        Process tempProcess;
        try {
//            instrumentJarsForEmma(carbonHome);
            System.setProperty(CARBON_HOME, carbonHome);
            originalUserDir = System.getProperty("user.dir");
            System.setProperty("user.dir", carbonHome);
//            log.info("Importing Code Coverage Details...");
//            ServerManager.importEmmaCoverage();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ignored) {
//            }
//            log.info("Imported Code Coverage Details.");
            String temp;
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {

                tempProcess = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/C", "bin\\wso2server.bat", "-Dsetup"},
                        null, new File(carbonHome));
            } else {
                //when starting wso2greg it crate all tables in registry

                tempProcess = Runtime.getRuntime().exec(new String[]{"sh", "bin/wso2esb-samples.sh", "-sn " + sampleNo + " -Dsetup"},
                        null, new File(carbonHome));
            }
//            Runtime.getRuntime().addShutdownHook(new Thread() {
//                public void run() {
//                    try {
//                        log.info("Shutting down server...");
//                        shutdown();
//
//                    } catch (Exception e) {
//                        log.error(e);
//                    }
//                }
//            });
            final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(tempProcess.getInputStream()));
            long time = System.currentTimeMillis() + DEFAULT_START_STOP_WAIT_MS;
            while ((temp = reader.readLine()) != null && System.currentTimeMillis() < time) {
                log.info(temp);
                if (temp.contains(SERVER_STARTUP_MESSAGE)) {
                    consoleLogPrinter = new Thread() {
                        public void run() {
                            try {
                                String temp;
                                while ((temp = reader.readLine()) != null) {
                                    log.info(temp);
                                }
                            } catch (Exception ignore) {
                                log.error(ignore);
                            }
                        }
                    };
                    consoleLogPrinter.start();
                    break;
                }
            }
        } catch (IOException e) {
            log.error(e);
            throw new RuntimeException("Unable to start server", e);
        }
        process = tempProcess;
        log.info("Successfully started Carbon server. Returning...");
    }

//    public synchronized static void startServerUsingCarbonZip(String carbonServerZipFile)
//            throws ServerConfigurationException, IOException {
//        if (process != null) { // An instance of the server is running
//            return;
//        }
//        String carbonHome = setUpCarbonHome(carbonServerZipFile);
////        instrumentJarsForEmma(carbonHome);
//        startServerUsingCarbonHome();
//    }

//    public synchronized void shutdown() throws Exception {
//        if (process != null) {
//            process.destroy();
//            try {
//                String temp;
//                BufferedReader reader = new BufferedReader(
//                        new InputStreamReader(process.getInputStream()));
//                long time = System.currentTimeMillis() + DEFAULT_START_STOP_WAIT_MS;
//                while ((temp = reader.readLine()) != null && System.currentTimeMillis() < time) {
//                    System.out.println(SERVER_SHUTDOWN_MESSAGE + ": " + temp);
//                    if (temp.contains(SERVER_SHUTDOWN_MESSAGE)) {
//                        System.out.println("ShutDown Complete.......");
//                        break;
//                    }
//                    System.out.println("shutting down ... ");
//                }
//
//            } catch (IOException e) {
//
//            }
//            try {
//                consoleLogPrinter.interrupt();
//            } catch (Exception e) {
//                log.error(e);
//            }
//            consoleLogPrinter = null;
//            process = null;
////            log.info("Saving Code Coverage Details...");
//            //ServerManager.exportEmmaCoverage();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ignored) {
//                log.error(ignored);
//            }
////            log.info("Completed Saving Code Coverage Details.");
//            System.clearProperty(CARBON_HOME);
//            System.setProperty("user.dir", originalUserDir);
//        }
//    }
}

