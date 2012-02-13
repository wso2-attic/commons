/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.performance.util;

import java.io.FileReader;
import java.io.BufferedReader;

/**
 * Simple JavaBench output to CSV converter
 *
 * @author asankha perera (asankha AT wso2 DOT com)
 *
 * 20 June 2008
 */
public class JBToCSV {

    private static final String DOC_PATH   = "Document Path:";
    private static final String DOC_LEN    = "Document Length:";
    private static final String CONC_LEV   = "Concurrency Level:";
    private static final String COMPLETED  = "Complete requests:";
    private static final String FAILED     = "Failed requests:";
    private static final String WRITE_ERR  = "Write errors:";
    private static final String TPS        = "Requests per second:";
    private static final String REQ_TIME   = "Time per request:";
    private static final String END_SEG    = "kb/s total";

    Result currentResult = new Result();

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java -jar jbtocsv.jar <JavaBenchOutputFile>");
        } else {
            new JBToCSV().readFile(args[0]);
        }
    }

    private void readFile(String fileName) throws Exception {

        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str;
        while ((str = in.readLine()) != null) {
            process(str);
        }
        in.close();
    }

    private void process(String line) {
        if (line.startsWith(DOC_PATH)) {
            // loose trailing "/" if present
            if (line.endsWith("/")) {
                line = line.substring(0, line.length()-1);
            }
            int pos = line.lastIndexOf("/");
            currentResult.serviceName = line.substring(pos+1);

        } else if (line.startsWith(DOC_LEN)) {
            line = line.substring(DOC_LEN.length()+1).trim();
            int size = Integer.parseInt(line.split(" ")[0]);
            size = size / 1024;
            currentResult.size = size;

        } else if (line.startsWith(CONC_LEV)) {
            line = line.substring(CONC_LEV.length()+1).trim();
            currentResult.concurrency = Integer.parseInt(line);

        } else if (line.startsWith(COMPLETED)) {
            line = line.substring(COMPLETED.length()+1).trim();
            currentResult.completed = Integer.parseInt(line);

        } else if (line.startsWith(FAILED)) {
            line = line.substring(FAILED.length()+1).trim();
            currentResult.failed = Integer.parseInt(line);

        } else if (line.startsWith(WRITE_ERR)) {
            line = line.substring(WRITE_ERR.length()+1).trim();
            currentResult.writeErrors = Integer.parseInt(line);

        } else if (line.startsWith(TPS)) {
            line = line.substring(TPS.length()+1).trim().replaceAll(",", "");
            currentResult.tps = Float.parseFloat(line.split(" ")[0]);

        } else if (line.startsWith(REQ_TIME) && currentResult.reqTime == 0) {
            line = line.substring(REQ_TIME.length()+1).trim().replaceAll(",", "");
            currentResult.reqTime = Float.parseFloat(line.split(" ")[0]);

        } else if (line.endsWith(END_SEG)) {
            System.out.println(currentResult);
            currentResult = new Result();
        }
    }

    private class Result {
        String serviceName;
        int size;
        int concurrency;
        int completed;
        int failed;
        int writeErrors;
        float tps;
        float reqTime;


        public String toString() {
            return serviceName + ',' + size + "," + concurrency + "," +
                completed + "," + failed + "," + writeErrors + "," + tps + "," + reqTime;
        }
    }
}
