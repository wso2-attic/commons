package org.wso2.carbon.common.test.utils.server;

/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;

import java.io.*;

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.*;

public class Axis2Server {
    static boolean build = false;

    static void buildAxis2Server() {
        if (!build) {
            //server build
            FrameworkSettings.getProperty();
            Runtime rt = Runtime.getRuntime();
            String[] services = new String[]{
                    "SimpleStockQuoteService",
                    "FastStockQuoteService",
                    "LoadbalanceFailoverService",
                    "MTOMSwASampleService",
                    "ReliableStockQuoteService",
                    "SecureStockQuoteService",

            };
            for (String service : services) {
                File serviceAar = new File(CARBON_HOME + File.separator + "samples" + File.separator + "axis2Server" + File.separator + "repository" + File.separator + "services" + File.separator + service + ".aar");
                if (!serviceAar.isFile()) {
                    File wd = new File(CARBON_HOME + File.separator + "samples" + File.separator + "axis2Server" + File.separator + "src" + File.separator + service);

                    Process proc = null;
                    try {
                        proc = rt.exec("ant", null, wd);
                        System.out.println("Built " + service + " : " + proc.waitFor());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                build = true;

            }
        }

    }


    public static void startAxis2Server(String testName) {
        startAxis2Server(testName, null, null, null);
    }

    public static void startAxis2Server(String testName, String http, String https, String serverName) {
        buildAxis2Server();
        try {
            FrameworkSettings.getProperty();
            Runtime rt = Runtime.getRuntime();

            //     server shell start
            File filePath = new File("./");
            String relativePath = filePath.getCanonicalPath();
            File findFile = new File(relativePath + File.separator + "common" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "server" + File.separator + "axis2server-stop.sh");
            if (!findFile.isFile()) {
                filePath = new File("./../");
                relativePath = filePath.getCanonicalPath();
            }

            File wd = new File(relativePath + File.separator + "common" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "server");
            Process proc = rt.exec("chmod 777 axis2server-start.sh", null, wd);
            System.out.println("Process make executable: " + proc.waitFor());
            Process axis2serverProc;
            if (http != null && https != null && serverName != null) {
                axis2serverProc = rt.exec("./axis2server-start.sh " + testName + " " + CARBON_HOME + " " + serverName + " " + http + " " + https + " ", null, wd);
            } else {

                axis2serverProc = rt.exec("./axis2server-start.sh " + testName + " " + CARBON_HOME + " default", null, wd);
                serverName = "default";
            }

            System.out.println("Process exitValue: " + axis2serverProc.waitFor());
            Thread.sleep(3000);
            File file = new File(CARBON_HOME + "/samples/axis2Server/repository/logs/testLogs/" + testName + "/axis2server-" + serverName + ".log");


            try {
                FileInputStream inputstream = new FileInputStream(file);

                // Here BufferedInputStream is added for fast reading.
                InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
                BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

                // dis.available() returns 0 if the file does not have more lines.
                String line;
                while ((line = bufferedreader.readLine()) != null) {

                    if (line.contains("INFO - HttpCoreNIOListener HTTP Listener started on port :")) {
                        break;
                    }

                }

                // dispose all the resources after using them.
                inputstream.close();
                inputstreamreader.close();
                bufferedreader.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static void stopAxis2Server(String testName) {
        stopAxis2Server(testName, "default");
    }

    public static void stopAxis2Server(String testName, String serverName) {
        try {
            FrameworkSettings.getProperty();
            Runtime rt = Runtime.getRuntime();
            File filePath = new File("./");
            String relativePath = filePath.getCanonicalPath();
            File findFile = new File(relativePath + File.separator + "common" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "server" + File.separator + "axis2server-stop.sh");
            if (!findFile.isFile()) {
                filePath = new File("./../");
                relativePath = filePath.getCanonicalPath();
            }

            File wd = new File(relativePath + File.separator + "common" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "server");
            Process proc = rt.exec("chmod 777 axis2server-stop.sh", null, wd);

            System.out.println("Process make executable: " + proc.waitFor());
            proc = rt.exec("chmod 777 kill-process-tree.sh", null, wd);
            proc.waitFor();
            Process axis2serverProc = rt.exec("./axis2server-stop.sh " + testName + " " + CARBON_HOME + " " + serverName, null, wd);

            //     int exitVal = proc.exitValue();
            System.out.println("Process exitValue: " + axis2serverProc.waitFor());

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }
}
