/**
 *  Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.test.framework.util.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.wso2.carbon.test.framework.initTestCase.*;

/**
 * Created by IntelliJ IDEA.
 * User: suho
 * Date: Jun 10, 2010
 * Time: 9:43:11 AM
 * To start and stop Sample Axis2server
 */


public class SampleAxis2Server {
    private static final Log log = LogFactory.getLog(SampleAxis2Server.class);
    static Map<String, SampleAxis2ServerManager> axis2SampleServers = new HashMap<String, SampleAxis2ServerManager>();

    public static void Start() throws Exception {
        Start("9000", "9002", "default");
    }

    //    stating Sample Axis2Server

    public static void Start(String http_port, String https_port, String server_name) throws Exception {

        //  -http 9001 -https 9005 -name MyServer1
        if (http_port != null) {
            System.setProperty("http_port", http_port);
        }
        if (https_port != null) {
            System.setProperty("https_port", https_port);
        }
        if (server_name != null) {
            System.setProperty("server_name", server_name);
        }
        log.info("Starting Sample Axis2 Server: " + server_name);

        //  -Djava.io.tmpdir=$AXIS2_HOME/../../tmp/sampleServer -Djava.endorsed.dirs=$AXIS2_ENDORSED
        System.setProperty("java.io.tmpdir", AXIS2S_HOME + "/../../tmp/sampleServer");
        System.setProperty("java.endorsed.dirs", AXIS2S_HOME + "/../../lib/endorsed");

        //     conf 1: Axis2 repository
        //          2: Axis2 configuration file (axis2.xml)
        SampleAxis2Server.replaceStringsInFile(AXIS2S_XML, ">.." + File.separator + ".." + File.separator + "resources" + File.separator + "security", ">" + ESB_HOME + "" + File.separator + "resources" + File.separator + "security");
        String[] config = {"-repo", AXIS2S_HOME + "" + File.separator + "repository", "-conf", AXIS2S_HOME + "" + File.separator + "repository" + File.separator + "conf" + File.separator + "axis2.xml"};

        SampleAxis2ServerManager.getInstance().start(config);
        axis2SampleServers.put(server_name, SampleAxis2ServerManager.getInstance());

    }

    public static void Stop() throws Exception {

        Stop("default");
    }

    public static void Stop(String serverName) throws Exception {

        log.info("Stopping Sample Axis2 Server: " + serverName);

        // SampleAxis2ServerManager.getInstance().stop();
        if (axis2SampleServers.containsKey(serverName)) {
            axis2SampleServers.get(serverName).stop();
            SampleAxis2Server.replaceStringsInFile(AXIS2S_XML, ">" + ESB_HOME + "" + File.separator + "resources" + File.separator + "security", ">.." + File.separator + ".." + File.separator + "resources" + File.separator + "security");
        } else {
            log.error("the server " + serverName + " not found, not stopped");
        }
    }

    //Building the Sample Axis2 Services

    public static void build(String service) {

        log.info("building " + service + "...");
        File wd = new File(AXIS2S_HOME + "" + File.separator + "src" + File.separator + "" + service + "" + File.separator + "");
        log.debug("Working Directory: " + wd);
        Process proc = null;

        try {
            proc = Runtime.getRuntime().exec("ant", null, wd);
            if (proc != null) {
                proc.waitFor();
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info(service + " successfully built");
    }

    public static void replaceStringsInFile(String fileName, String match, String replacingString) throws IOException {
        log.debug("String replacing started");
        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = null;
        String axis2 = null;
        File SourceFile = new File(fileName);

        if (SourceFile.exists()) {
            try {
                scanner = new Scanner(new File(fileName));
                while (scanner.hasNextLine()) {
                    text.append(scanner.nextLine()).append(NL);
                }
                axis2 = text.toString();
                axis2 = axis2.replaceAll(match, replacingString);
            } finally {
                if (scanner != null) {
                    scanner.close();
                }
            }
            log.debug("String read");
            Writer out = new OutputStreamWriter(new FileOutputStream(fileName));

            if (axis2 != null) {
                try {
                    out.write(axis2);
                } finally {
                    out.close();
                }
            }
            log.debug("String replaced");
        }
    }

}






