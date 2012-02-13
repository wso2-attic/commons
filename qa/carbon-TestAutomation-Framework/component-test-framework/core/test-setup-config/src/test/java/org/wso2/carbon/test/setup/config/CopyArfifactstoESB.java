package org.wso2.carbon.test.setup.config;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.common.test.utils.TestTemplate;

import java.io.*;
import java.nio.channels.FileChannel;

public class CopyArfifactstoESB extends TestTemplate {
    private static final Log log = LogFactory.getLog(CopyArfifactstoESB.class);

    /*This class is to move test artifacts from resource directory (lib/mediator-artifacts) to relevant destination paths in ESB */

    public static final void copy(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            copyDirectory(source, destination);
        } else {
            copyFile(source, destination);
        }
    }

    public static final void copyDirectory(File source, File destination) throws IOException {
        if (!source.isDirectory()) {
            throw new IllegalArgumentException("Source (" + source.getPath() + ") must be a directory");
        }

        if (!source.exists()) {
            throw new IllegalArgumentException("Source directory (" + source.getPath() + ") does not exist");
        }

        if (destination.exists()) {
            throw new IllegalArgumentException("Destination (" + destination.getPath() + ") exists");
        }

        destination.mkdirs();
        File[] files = source.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                copyDirectory(file, new File(destination, file.getName()));
            } else {
                copyFile(file, new File(destination, file.getName()));
            }
        }
    }

    public static final void copyFile(File source, File destination) throws IOException {
        FileChannel sourceChannel = new FileInputStream(source).getChannel();
        FileChannel targetChannel = new FileOutputStream(destination).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
        sourceChannel.close();
        targetChannel.close();
    }


    @Override
    public void init() {
        try {

            frameworkPath = FrameworkSettings.getFrameworkPath();
            String srcDirPath = frameworkPath + File.separator + "lib" + File.separator + "mediator-artifacts" + File.separator;
            String destDirPath_dropins = FrameworkSettings.CARBON_HOME + File.separator + "repository" + File.separator + "components" + File.separator + "dropins" + File.separator;
            String destDirPath_ext = FrameworkSettings.CARBON_HOME + File.separator + "repository" + File.separator + "components" + File.separator + "extensions" + File.separator;
            String destDirPath_lib = FrameworkSettings.CARBON_HOME + File.separator + "repository" + File.separator + "components" + File.separator + "lib" + File.separator;
            String destDirPath_services = FrameworkSettings.CARBON_HOME + File.separator + "samples" + File.separator + "axis2Server" + File.separator + "repository" + File.separator + "services" + File.separator;


            /* Relevant to Sample 353 and Sample354*/
            File source_s353 = new File(srcDirPath + "jruby-complete-1.3.0.wso2v1.jar");
            File destination_s353 = new File(destDirPath_dropins + "jruby-complete-1.3.0.wso2v1.jar");
            copy(source_s353, destination_s353);

            /* Relevant to Sample 360, 361 and 362*/
            File source_s360_1 = new File(srcDirPath + "derby.jar");
            File destination_s360_1 = new File(destDirPath_ext + "derby.jar");
            copy(source_s360_1, destination_s360_1);

            File source_s360_2 = new File(srcDirPath + "derbyclient.jar");
            File destination_s360_2 = new File(destDirPath_ext + "derbyclient.jar");
            copy(source_s360_2, destination_s360_2);

            File source_s360_3 = new File(srcDirPath + "derbynet.jar");
            File destination_s360_3 = new File(destDirPath_ext + "derbynet.jar");
            copy(source_s360_3, destination_s360_3);
            System.out.println("**FileCopied Successfully**");

            /* Relevant to Sample 606*/
            File source_s606 = new File(srcDirPath + "rule-1.0.jar");
            File destinations606 = new File(destDirPath_lib + "rule-1.0.jar");
            copy(source_s606, destinations606);

            /* Relevant to mediators-xslt*/
            File source_xslt = new File(srcDirPath + "Echo.aar");
            File destinations_xslt = new File(destDirPath_services + "Echo.aar");
            copy(source_xslt, destinations_xslt);

            /* Relevant to mediators-xslt*/
            File source_xslt_echo_back = new File(srcDirPath + "echo_back.xslt");
            File destinations_xslt_echo_back = new File(srcDirPath + "echo_back.xslt");
            copy(source_xslt_echo_back, destinations_xslt_echo_back);

            /* Relevant to mediators-xslt*/
            File source_xslt_echo_transform = new File(srcDirPath + "echo_transform.xslt");
            File destinations_xslt_echo_transform = new File(srcDirPath + "echo_transform.xslt");
            copy(source_xslt_echo_transform, destinations_xslt_echo_transform);

            /* Relevant to mediators-class*/
            File source_class = new File(srcDirPath + "TestMediate.jar");
            File destination_class = new File(destDirPath_lib + "TestMediate.jar");
            copy(source_class, destination_class);

            /* Relevant to mediators-command*/
            File source_command = new File(srcDirPath + "TestMediateCommand.jar");
            File destination_command = new File(destDirPath_lib + "TestMediateCommand.jar");
            copy(source_command, destination_command);

            /* Relevant to mediators-spring*/
            File source_spring = new File(srcDirPath + "SpringMediatorTest.jar");
            File destination_spring = new File(destDirPath_lib + "SpringMediatorTest.jar");
            copy(source_spring, destination_spring);

            System.out.println("**Files Copied Successfully**");

        } catch (FileNotFoundException e) {
            log.error("File not copied: error occurred " + e);

        } catch (IOException e) {
            log.error("File not copied: error occurred " + e);

        } catch (Exception e) {
            log.error("File not copied: error occurred " + e);
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
}

