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
package org.wso2.carbon.system.test.core.utils.serverUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.ServerConfigurationException;
import org.wso2.carbon.utils.ArchiveManipulator;
import org.wso2.carbon.utils.FileManipulator;
import org.wso2.carbon.utils.ServerConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerManager {
    private static final Log log = LogFactory.getLog(ServerManager.class);

    private Process process;
    private Thread consoleLogPrinter;
    private String originalUserDir = null;

    private String carbonHome;

    public ServerManager(String carbonHome) {
        this.carbonHome = carbonHome;
    }

    private final static String SERVER_STARTUP_MESSAGE = "WSO2 Carbon started in";
    private final static String SERVER_SHUTDOWN_MESSAGE = "Halting JVM";
    private final static long DEFAULT_START_STOP_WAIT_MS = 1000 * 60 * 4;

    public synchronized void start() throws ServerConfigurationException {
        if (process != null) { // An instance of the server is running
            return;
        }
        Process tempProcess;
        try {
//            instrumentJarsForEmma(carbonHome);
            System.setProperty(ServerConstants.CARBON_HOME, carbonHome);
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
                tempProcess = Runtime.getRuntime().exec(new String[]{"bat", "bin/wso2server.bat"},
                        null, new File(carbonHome));
            } else {
                //when starting wso2greg it crate all tables in registry
                if (carbonHome.contains("wso2greg")) {
                    tempProcess = Runtime.getRuntime().exec(new String[]{"sh", "bin/wso2server.sh", "-Dsetup test"},
                            null, new File(carbonHome));
                } else {
                    tempProcess = Runtime.getRuntime().exec(new String[]{"sh", "bin/wso2server.sh", "test"},
                            null, new File(carbonHome));
                }

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
                                while ((temp = reader.readLine()) != null && temp.equalsIgnoreCase(SERVER_SHUTDOWN_MESSAGE)) {
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

    public synchronized String setUpCarbonHome(String carbonServerZipFile)
            throws IOException {
        if (process != null) { // An instance of the server is running
            return null;
        }
        int indexOfZip = carbonServerZipFile.lastIndexOf(".zip");
        if (indexOfZip == -1) {
            throw new IllegalArgumentException(carbonServerZipFile + " is not a zip file");
        }
        String fileSeparator = (File.separator.equals("\\")) ? "\\" : "/";
        String extractedCarbonDir =
                carbonServerZipFile.substring(carbonServerZipFile.lastIndexOf(fileSeparator) + 1,
                        indexOfZip);
        FileManipulator.deleteDir(extractedCarbonDir);
        new ArchiveManipulator().extract(carbonServerZipFile, "carbontmp");
        return new File(".").getAbsolutePath() + File.separator + "carbontmp" +
                File.separator + extractedCarbonDir;
    }

    public synchronized static void exportEmmaCoverage() {
        if (System.getProperty("emma.home") == null) {
            return;
        }
        String carbonHome = System.getProperty(ServerConstants.CARBON_HOME);
        String emmaOutput = System.getProperty("emma.output");
        File current = new File(carbonHome + File.separator + "coverage.ec");
        File saved = new File(emmaOutput + File.separator + "coverage.ec");
        if (current.exists() && new File(emmaOutput).exists()) {
            try {
                FileUtils.copyFile(current, saved);
            } catch (IOException ignored) {
                log.error(ignored);
            }
        }
    }

    public synchronized static void importEmmaCoverage() {
        if (System.getProperty("emma.home") == null) {
            return;
        }
        String carbonHome = System.getProperty(ServerConstants.CARBON_HOME);
        String emmaOutput = System.getProperty("emma.output");
        File current = new File(carbonHome + File.separator + "coverage.ec");
        File saved = new File(emmaOutput + File.separator + "coverage.ec");
        if (saved.exists() && new File(carbonHome).exists()) {
            try {
                FileUtils.copyFile(saved, current);
            } catch (IOException ignored) {
                log.error(ignored);
            }
        }
    }

    public synchronized static void instrumentJarsForEmma(String carbonHome) throws IOException {
        String workingDir = System.getProperty("user.dir");
        try {
            System.setProperty("user.dir", carbonHome);
            String emmaHome = System.getProperty("emma.home");
            if (emmaHome == null) {
                return;
            } else if (!emmaHome.endsWith(File.separator)) {
                emmaHome += File.separator;
            }
            File emmaOutput = new File(System.getProperty("emma.output"));
            if (!emmaOutput.exists()) {
                FileUtils.forceMkdir(emmaOutput);
            }
            String emmaJarName = null;
            for (File file : new File(emmaHome).listFiles()) {
                if (file.getName().startsWith("org.wso2.carbon.integration.core")) {
                    ArchiveManipulator archiveManipulator = new ArchiveManipulator();
                    archiveManipulator.extract(file.getAbsolutePath(), emmaHome);
                } else if (file.getName().startsWith("emma")) {
                    emmaJarName = file.getName();
                }
            }

            if (emmaJarName == null) {
                return;
            }

            String jarList = System.getProperty("jar.list");


            FileUtils.copyFile(new File(emmaHome + emmaJarName),
                    new File(carbonHome + File.separator + "repository" +
                            File.separator + "components" + File.separator + "plugins" +
                            File.separator + "emma.jar"));
            FileUtils.copyFile(new File(emmaHome + emmaJarName),
                    new File(carbonHome + File.separator + "repository" +
                            File.separator + "components" + File.separator + "lib" +
                            File.separator + "emma.jar"));
            FileUtils.copyFile(new File(emmaHome + emmaJarName),
                    new File(carbonHome + File.separator + "lib" +
                            File.separator + "emma.jar"));
            for (File file : new File[]{new File(carbonHome), emmaOutput}) {
                FileUtils.copyFileToDirectory(new File(carbonHome + File.separator + "lib" +
                        File.separator + "emma.jar"), file);
                FileUtils.copyFileToDirectory(new File(emmaHome + "gen_emma_coverage.rb"), file);
                FileUtils.copyFileToDirectory(new File(jarList), file);
            }

            String temp;
            Process process = Runtime.getRuntime().exec(new String[]{"ruby",
                    "gen_emma_coverage.rb", "instrument", System.getenv("JAVA_HOME")}, null,
                    new File(carbonHome));
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            try {
                while ((temp = reader.readLine()) != null) {
                    log.info(temp);
                }
            } catch (IOException ignored) {
                log.error(ignored);
            }
            FileUtils.copyFileToDirectory(new File(carbonHome + File.separator + "coverage.em"),
                    emmaOutput);
        } finally {
            System.setProperty("user.dir", workingDir);
        }
    }

    public synchronized void shutdown() throws Exception {
        if (process != null) {
            process.destroy();
            try {
                String temp;
                process.destroy();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                long time = System.currentTimeMillis() + DEFAULT_START_STOP_WAIT_MS;
                while ((temp = reader.readLine()) != null && System.currentTimeMillis() < time) {
                    if (temp.contains(SERVER_SHUTDOWN_MESSAGE)) {
                        break;
                    }
                }

            } catch (IOException ignored) {
            }
            try {
                consoleLogPrinter.interrupt();
            } catch (Exception e) {
                log.error(e);
            }
            consoleLogPrinter = null;
            process = null;
//            log.info("Saving Code Coverage Details...");
            //ServerManager.exportEmmaCoverage();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
                log.error(ignored);
            }
//            log.info("Completed Saving Code Coverage Details.");
            System.clearProperty(ServerConstants.CARBON_HOME);
            System.setProperty("user.dir", originalUserDir);
        }
    }

}
