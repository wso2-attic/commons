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

import org.wso2.carbon.admin.service.utils.FrameworkSettings;

import java.io.File;

public class ProductConstant {

    public static final String SYSTEM_TEST_RESOURCE_LOCATION = System.getProperty("system.test.sample.location");

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

}
