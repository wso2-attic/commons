package org.wso2.carbon.web.test.esb;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

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

public class ESBStartSimpleAxis2ServerTest {
    public void startServer(String carbonHome) {
        try {

            String command1 = carbonHome + "\\samples\\axis2Server\\axis2server.bat";
            String command2 = carbonHome + "/samples/axis2Server/axis2server.sh";
            String osName = System.getProperty("os.name");

            if (osName.equals("Windows XP")) {
                Process winproc = Runtime.getRuntime().exec(command1);
                InputStream is = winproc.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                System.out.println("*****************OUTPUT**************************");
                while ((line = br.readLine()) != null)
                    System.out.println(line);
                System.out.println("*****************OUTPUT**************************>");
                int exitVal1 = winproc.waitFor();
                System.out.println("Process exitValue: " + exitVal1);

            } else if (osName.equals("Linux")) {
                Process linuxproc = Runtime.getRuntime().exec(command2);
                InputStream is = linuxproc.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                System.out.println("*****************OUTPUT**************************");
                while ((line = br.readLine()) != null)
                    System.out.println(line);
                System.out.println("*****************OUTPUT**************************>");
                int exitVal2 = linuxproc.waitFor();
                System.out.println("Process exitValue: " + exitVal2);
                System.out.println(osName);
            }

        } catch (IOException e) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void manageServerCommands(String carbonHome, String option) {
        try {

            String command1 = carbonHome + "\\samples\\axis2Server\\axis2server.bat -"+option;
            String command2 = carbonHome + "/samples/axis2Server/axis2server.sh -"+option;
            String osName = System.getProperty("os.name");

            if (osName.equals("Windows XP")) {
                Process winproc = Runtime.getRuntime().exec(command1);
                InputStream is = winproc.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                System.out.println("*****************OUTPUT**************************");
                while ((line = br.readLine()) != null)
                    System.out.println(line);
                System.out.println("*****************OUTPUT**************************>");
                int exitVal1 = winproc.waitFor();
                System.out.println("Process exitValue: " + exitVal1);

            } else if (osName.equals("Linux")) {
                Process linuxproc = Runtime.getRuntime().exec(command2);
                InputStream is = linuxproc.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                System.out.println("*****************OUTPUT**************************");
                while ((line = br.readLine()) != null)
                    System.out.println(line);
                System.out.println("*****************OUTPUT**************************>");
                int exitVal2 = linuxproc.waitFor();
                System.out.println("Process exitValue: " + exitVal2);
                System.out.println(osName);
            }

        } catch (IOException e) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void stopServer(String carbonHome) {
        try {

            String command1 = carbonHome + "\\samples\\axis2Server\\axis2server.bat -stop";
            String command2 = carbonHome + "/samples/axis2Server/axis2server.sh -stop";
            String osName = System.getProperty("os.name");

            if (osName.equals("Windows XP")) {
                Process winproc = Runtime.getRuntime().exec(command1);
                InputStream is = winproc.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                System.out.println("*****************OUTPUT**************************");
                while ((line = br.readLine()) != null)
                    System.out.println(line);
                System.out.println("*****************OUTPUT**************************>");
                int exitVal1 = winproc.waitFor();
                System.out.println("Process exitValue: " + exitVal1);

            } else if (osName.equals("Linux")) {
                Process linuxproc = Runtime.getRuntime().exec(command2);
                InputStream is = linuxproc.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                System.out.println("*****************OUTPUT**************************");
                while ((line = br.readLine()) != null)
                    System.out.println(line);
                System.out.println("*****************OUTPUT**************************>");
                int exitVal2 = linuxproc.waitFor();
                System.out.println("Process exitValue: " + exitVal2);
                System.out.println(osName);
            }

        } catch (IOException e) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }    
}
