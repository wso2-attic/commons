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

package org.wso2.carbon.admin.service.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;

import java.io.File;

public class ProductConstant {
    private static final Log log = LogFactory.getLog(ProductConstant.class);

    public static final String APP_SERVER_NAME = "AS";
    public static final String ESB_SERVER_NAME = "ESB";
    public static final String BPS_SERVER_NAME = "BPS";
    public static final String DSS_SERVER_NAME = "DSS";
    public static final String MB_SERVER_NAME = "MB";
    public static final String CEP_SERVER_NAME = "CEP";
    public static final String MS_SERVER_NAME = "MS";
    public static final String GS_SERVER_NAME = "GS";
    public static final String BRS_SERVER_NAME = "BRS";
    public static final String GREG_SERVER_NAME = "GREG";
    public static final String MULTITENANCY_FREE_PLAN = "Demo";
    public static final String MULTITENANCY_SMALL_PLAN = "SMB";
    public static final String MULTITENANCY_MEDIUM_PLAN = "Professional";
    public static final String MULTITENANCY_LARGE_PLAN = "Enterprise";
    public static final String FIREFOX_BROWSER = "firefox";
    public static final String CHROME_BROWSER = "chrome";
    public static final String IE_BROWSER = "ie";
    public static final String OPERA_BROWSER = "opera";
    public static final String HTML_UNIT_DRIVER = "htmlUnit";




    public static final String SYSTEM_TEST_RESOURCE_LOCATION = System.getProperty("system.test.sample.location");


    public static String getResourceLocations(String productName) {
        String productArtifactLocation = SYSTEM_TEST_RESOURCE_LOCATION + File.separator + "artifacts" +
                File.separator + productName;
        return productArtifactLocation;
    }


    public static String getModuleClientPath() {
        String moduleClientLocation = SYSTEM_TEST_RESOURCE_LOCATION + File.separator + "moduleClient";
        return moduleClientLocation;
    }


    public static String getSecurityScenarios() {
        String securitytLocation = SYSTEM_TEST_RESOURCE_LOCATION + File.separator + "securityScenarios";
        return securitytLocation;
    }

    public static String getKeyStoreLocation() {
        String keyStoreLocation;
        if (FrameworkSettings.getStratosTestStatus()) {
            FrameworkSettings.getStratosProperties();
            keyStoreLocation = FrameworkSettings.STRATOS_TRUSTSTORE_PATH;

        }
        keyStoreLocation = FrameworkSettings.PRODUCT_TRUSTSTORE_PATH;
        return keyStoreLocation;


    }

    public static String getCarbonHome(String product) {
        FrameworkSettings.getFrameworkProperties();
        String deploymentHome = FrameworkSettings.DEPLOYMENT_FRAMEWORK_HOME + "/SNAPSHOT/";
        File deploymentDir = new File(deploymentHome);
        String distributionPrefix = null;

        if (APP_SERVER_NAME.equalsIgnoreCase(product)) {
            distributionPrefix = "wso2as-";
        } else if (ESB_SERVER_NAME.equalsIgnoreCase(product)) {
            distributionPrefix = "wso2esb-";
        } else if (BPS_SERVER_NAME.equalsIgnoreCase(product)) {
            distributionPrefix = "wso2bps-";
        } else if (DSS_SERVER_NAME.equalsIgnoreCase(product)) {
            distributionPrefix = "wso2dataservices-";
        } else if (MB_SERVER_NAME.equalsIgnoreCase(product)) {
            distributionPrefix = "wso2mb-";
        } else if (CEP_SERVER_NAME.equalsIgnoreCase(product)) {
            distributionPrefix = "wso2cep-";
        } else if (GS_SERVER_NAME.equalsIgnoreCase(product)) {
            distributionPrefix = "wso2gs-";
        } else if (BRS_SERVER_NAME.equalsIgnoreCase(product)) {
            distributionPrefix = "wso2brs-";
        } else if (GREG_SERVER_NAME.equalsIgnoreCase(product)) {
            distributionPrefix = "wso2greg-";
        } else {
            log.warn("Invalid Product Name. Configure product.list in framework.setting correctly");

        }


        String[] folderList = deploymentDir.list();
        for (String folderName : folderList) {
            if (folderName.contains(distributionPrefix)) {
                deploymentHome = deploymentHome + folderName;
                break;
            }
        }
        log.debug("Product Distribution folder for " + product + ": " + deploymentHome);
        return deploymentHome;
    }

    public static String getBackendURL(String product) {
        FrameworkSettings.getFrameworkProperties();
        String backendURL = null;

        if (APP_SERVER_NAME.equalsIgnoreCase(product)) {
            backendURL = FrameworkSettings.APP_BACKEND_URL;
        } else if (ESB_SERVER_NAME.equalsIgnoreCase(product)) {
            backendURL = FrameworkSettings.ESB_BACKEND_URL;
        } else if (BPS_SERVER_NAME.equalsIgnoreCase(product)) {
            backendURL = FrameworkSettings.BPS_BACKEND_URL;
        } else if (DSS_SERVER_NAME.equalsIgnoreCase(product)) {
            backendURL = FrameworkSettings.DSS_BACKEND_URL;
        } else if (MB_SERVER_NAME.equalsIgnoreCase(product)) {
            backendURL = FrameworkSettings.MB_BACKEND_URL;
        } else if (CEP_SERVER_NAME.equalsIgnoreCase(product)) {
            backendURL = FrameworkSettings.CEP_BACKEND_URL;
        } else if (GS_SERVER_NAME.equalsIgnoreCase(product)) {
            backendURL = FrameworkSettings.GS_BACKEND_URL;
        } else if (BRS_SERVER_NAME.equalsIgnoreCase(product)) {
            backendURL = FrameworkSettings.BRS_BACKEND_URL;
        } else if (GREG_SERVER_NAME.equalsIgnoreCase(product)) {
            backendURL = FrameworkSettings.GREG_BACKEND_URL;
        } else {
            log.warn("Invalid Product Name. Cannot construct backend URL");
        }
        return backendURL;
    }
}