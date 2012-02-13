/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.stratos.perf.test.scenarios.webappConcurrency.webAppUtils;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WebAppUtil {

    private static final Log log = LogFactory.getLog(WebAppUtil.class);

    protected static boolean webappTest(String url, String content) {
        URL webAppURL;
        BufferedReader in;
        boolean webappStatus = false;

        try {
            log.info("Invoking webapp on app server service");
            webAppURL = new URL(url);
            URLConnection yc;
            yc = webAppURL.openConnection();

            in = new BufferedReader(
                    new InputStreamReader(
                            yc.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.indexOf(content) > 1) {
                    webappStatus = true;
                    break;
                }
            }
            in.close();
        } catch (IOException e) {
            log.error("Web app invocation failed: IO Exception" + e.getMessage());
        }
        return webappStatus;
    }

    public static void waitForWebAppDeployment(String serviceUrl, String content) {
        int serviceTimeOut = 0;
        while (!webappTest(serviceUrl, content)) {
            if (serviceTimeOut == 0) {
            } else if (serviceTimeOut > 100) { //Check for the service for 100 seconds
                // if Service not available assertfalse;
                Assert.fail(serviceUrl + " webapp is not found");
                break;
            }
            try {
                Thread.sleep(500);
                serviceTimeOut++;
            } catch (InterruptedException e) {
                Assert.fail(e.getMessage());
            }
        }
    }

    public static void waitForWebAppUnDeployment(String serviceUrl, String content) {
        int serviceTimeOut = 0;
        while (webappTest(serviceUrl, content)) {
            if (serviceTimeOut == 0) {
            } else if (serviceTimeOut > 60) { //Check for the service for 100 seconds
                Assert.fail("webapp undeployment failed");
                break;
            }
            try {
                Thread.sleep(500);
                serviceTimeOut++;
            } catch (InterruptedException e) {
                Assert.fail(e.getMessage());
            }
        }
    }
}
