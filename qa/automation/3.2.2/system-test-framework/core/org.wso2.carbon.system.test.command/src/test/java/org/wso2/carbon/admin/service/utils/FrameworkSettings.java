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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FrameworkSettings {

    public static String PRODUCT_TRUSTSTORE_PATH;
    public static String PRODUCT_TRUSTSTORE_PASSWORD;

    public static String LDAP_ADMIN_UN;
    public static String LDAP_ADMIN_PASSWORD;

    //APP-SERVER settings
    public static String APP_SERVER_HOST_NAME;
    public static String APP_SERVER_HTTP_PORT;
    public static String APP_SERVER_HTTPS_PORT;
    public static String APP_SERVER_WEB_CONTEXT_ROOT;

    //BPS-SERVER settings
    public static String BPS_SERVER_HTTP_PORT;
    public static String BPS_SERVER_HTTPS_PORT;
    public static String BPS_SERVER_WEB_CONTEXT_ROOT;

    //GS-SERVER settings
    public static String GS_SERVER_HTTP_PORT;
    public static String GS_SERVER_HTTPS_PORT;
    public static String GS_SERVER_WEB_CONTEXT_ROOT;

    //GREG-SERVER settings
    public static String GREG_SERVER_HTTP_PORT;
    public static String GREG_SERVER_HTTPS_PORT;
    public static String GREG_SERVER_WEB_CONTEXT_ROOT;

    //MB-SERVER settings
    public static String MB_SERVER_HTTP_PORT;
    public static String MB_SERVER_HTTPS_PORT;
    public static String MB_SERVER_WEB_CONTEXT_ROOT;

    //CEP-SERVER settings
    public static String CEP_SERVER_HTTP_PORT;
    public static String CEP_SERVER_HTTPS_PORT;
    public static String CEP_SERVER_WEB_CONTEXT_ROOT;

    //BRS-SERVER settings
    public static String BRS_SERVER_HTTP_PORT;
    public static String BRS_SERVER_HTTPS_PORT;
    public static String BRS_SERVER_WEB_CONTEXT_ROOT;

    //IS-SERVER settings
    public static String IS_SERVER_HTTP_PORT;
    public static String IS_SERVER_HTTPS_PORT;
    public static String IS_SERVER_WEB_CONTEXT_ROOT;

    //DSS-SERVER settings
    public static String DSS_SERVER_HTTP_PORT;
    public static String DSS_SERVER_HTTPS_PORT;
    public static String DSS_SERVER_WEB_CONTEXT_ROOT;

    //BAM-SERVER settings
    public static String BAB_SERVER_HTTP_PORT;
    public static String BAM_SERVER_HTTPS_PORT;
    public static String BAM_SERVER_WEB_CONTEXT_ROOT;

    //MS-SERVER settings
    public static String MS_SERVER_HTTP_PORT;
    public static String MS_SERVER_HTTPS_PORT;
    public static String MS_SERVER_WEB_CONTEXT_ROOT;

    //STRATOS-MANGER settings
    public static String MANAGER_SERVER_HTTP_PORT;
    public static String MANAGER_SERVER_HTTPS_PORT;
    public static String MANAGER_SERVER_WEB_CONTEXT_ROOT;

    //ESB-SERVER settings
    public static String ESB_SERVER_HOST_NAME;
    public static String ESB_SERVER_HTTP_PORT;
    public static String ESB_SERVER_HTTPS_PORT;
    public static String ESB_SERVER_WEB_CONTEXT_ROOT;
    public static String ESB_NHTTP_PORT;
    public static String ESB_NHTTPS_PORT;

    //Cloud service host names and ports
    public static String BPS_SERVER_HOST_NAME;
    public static String GS_SERVER_HOST_NAME;
    public static String GREG_SERVER_HOST_NAME;
    public static String MB_SERVER_HOST_NAME;
    public static String CEP_SERVER_HOST_NAME;
    public static String BRS_SERVER_HOST_NAME;
    public static String DSS_SERVER_HOST_NAME;
    public static String IS_SERVER_HOST_NAME;
    public static String MANAGER_SERVER_HOST_NAME;
    public static String BAM_SERVER_HOST_NAME;
    public static String MS_SERVER_HOST_NAME;
    public static String MB_QPID_PORT;
    public static String CEP_QPID_PORT;
    public static String STRATOS_TRUSTSTORE_PATH;
    public static String STRATOS_TRUSTSTORE_PASSWORD;

    public static String APP_BACKEND_URL;
    public static String ESB_BACKEND_URL;
    public static String BPS_BACKEND_URL;
    public static String GS_BACKEND_URL;
    public static String GREG_BACKEND_URL;
    public static String MB_BACKEND_URL;
    public static String CEP_BACKEND_URL;
    public static String BRS_BACKEND_URL;
    public static String DSS_BACKEND_URL;
    public static String IS_BACKEND_URL;
    public static String MANAGER_BACKEND_URL;
    public static String BAM_BACKEND_URL;
    public static String MS_BACKEND_URL;

    public static boolean STRATOS_TEST;

    public static Properties prop = new Properties();

    //reading framework.properties file and create input steam out of it.
    public static InputStream inputStream = FrameworkSettings.class.getResourceAsStream("/framework.properties");


    public static void getProductProperties() {
        try {


            if (inputStream != null) {
                prop.load(inputStream);
            }

            //LDAP credentials
            LDAP_ADMIN_UN = (prop.getProperty("ldap.username", "admin"));
            LDAP_ADMIN_PASSWORD = (prop.getProperty("ldap.password", "admin"));

            //initializing app-server settings.
            APP_SERVER_HOST_NAME = (prop.getProperty("as.host.name", "localhost"));
            APP_SERVER_HTTP_PORT = (prop.getProperty("as.http.port", "9767"));
            APP_SERVER_HTTPS_PORT = (prop.getProperty("as.https.port", "9447"));
            APP_SERVER_WEB_CONTEXT_ROOT = (prop.getProperty("as.webContext.root", "as"));

            //initializing ESB-server settings.
            ESB_SERVER_HOST_NAME = (prop.getProperty("esb.host.name", "localhost"));
            ESB_SERVER_HTTP_PORT = (prop.getProperty("esb.http.port", "9765"));
            ESB_SERVER_HTTPS_PORT = (prop.getProperty("esb.https.port", "9445"));
            ESB_SERVER_WEB_CONTEXT_ROOT = (prop.getProperty("esb.webContext.root", "esb"));
            ESB_NHTTP_PORT = (prop.getProperty("esb.http.port", "8280"));
            ESB_NHTTPS_PORT = (prop.getProperty("esb.nhttps.port", "8243"));


            PRODUCT_TRUSTSTORE_PATH = ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION + File.separator + "keystores" + File.separator + "products" + File.separator + "wso2carbon.jks";
            PRODUCT_TRUSTSTORE_PASSWORD = (prop.getProperty("truststore.password", "wso2carbon"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String constructBackendUrl(String webcontexRoot, String httpsPort, String ServerHostName) {
        String backendUrl;

        if (webcontexRoot != null && httpsPort != null) {
            backendUrl = "https://" + ServerHostName + ":" + httpsPort + "/" + webcontexRoot + "/" + "services";
        } else if (webcontexRoot == null && httpsPort != null) {
            backendUrl = "https://" + ServerHostName + ":" + httpsPort + "/" + "services";
        } else if (webcontexRoot == null && httpsPort == null) {
            backendUrl = "https://" + ServerHostName + "/" + "services";
        } else {
            backendUrl = "https://" + ServerHostName + "/" + webcontexRoot + "/" + "services";
        }

        return backendUrl;
    }


    public static void getStratosProperties() {

        if (inputStream != null) {
            try {
                prop.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Initializing Stratos service settings - ports, hostname and tenant details
        APP_SERVER_HOST_NAME = prop.getProperty("app.service.host.name", "appserver.stratoslive.wso2.com");
        ESB_SERVER_HOST_NAME = prop.getProperty("esb.service.host.name", "esb.stratoslive.wso2.com");
        GREG_SERVER_HOST_NAME = prop.getProperty("greg.service.host.name", "governance.stratoslive.wso2.com");
        DSS_SERVER_HOST_NAME = prop.getProperty("dss.service.host.name", "data.stratoslive.wso2.com");
        GS_SERVER_HOST_NAME = prop.getProperty("gs.service.host.name", "gadget.stratoslive.wso2.com");
        BRS_SERVER_HOST_NAME = prop.getProperty("brs.service.host.name", "rule.stratoslive.wso2.com");
        BPS_SERVER_HOST_NAME = prop.getProperty("bps.service.host.name", "process.stratoslive.wso2.com");
        MANAGER_SERVER_HOST_NAME = prop.getProperty("manager.service.host.name", "stratoslive.wso2.com");
        MS_SERVER_HOST_NAME = prop.getProperty("ms.service.host.name", "mashup.stratoslive.wso2.com");
        MB_SERVER_HOST_NAME = prop.getProperty("mb.service.host.name", "messaging.stratoslive.wso2.com");
        CEP_SERVER_HOST_NAME = prop.getProperty("cep.service.host.name", "cep.stratoslive.wso2.com");
        BAM_SERVER_HOST_NAME = prop.getProperty("bam.service.host.name", "monitor.stratoslive.wso2.com");
        IS_SERVER_HOST_NAME = prop.getProperty("is.service.host.name", "identity.stratoslive.wso2.com");

        MB_QPID_PORT = prop.getProperty("mb.qpid.port", "5672");
        CEP_QPID_PORT = prop.getProperty("cep.qpid.port", "5672");
        ESB_NHTTP_PORT = (prop.getProperty("esb.http.port", "8280"));
        ESB_NHTTPS_PORT = (prop.getProperty("esb.nhttps.port", "8243"));

        STRATOS_TRUSTSTORE_PATH = ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION + File.separator + "keystores" + File.separator + "stratos" + File.separator + "wso2carbon.jks";
        STRATOS_TRUSTSTORE_PASSWORD = (prop.getProperty("truststore.password", "wso2carbon"));
    }


    private static void setBackendUrls() {
        APP_BACKEND_URL = constructBackendUrl(APP_SERVER_WEB_CONTEXT_ROOT, APP_SERVER_HTTPS_PORT, APP_SERVER_HOST_NAME);
        ESB_BACKEND_URL = constructBackendUrl(ESB_SERVER_WEB_CONTEXT_ROOT, ESB_SERVER_HTTPS_PORT, ESB_SERVER_HOST_NAME);
        BPS_BACKEND_URL = constructBackendUrl(BPS_SERVER_WEB_CONTEXT_ROOT, BPS_SERVER_HTTPS_PORT, BPS_SERVER_HOST_NAME);
        GS_BACKEND_URL = constructBackendUrl(GS_SERVER_WEB_CONTEXT_ROOT, GS_SERVER_HTTPS_PORT, GS_SERVER_HOST_NAME);
        GREG_BACKEND_URL = constructBackendUrl(GREG_SERVER_WEB_CONTEXT_ROOT, GREG_SERVER_HTTPS_PORT, GREG_SERVER_HOST_NAME);
        MB_BACKEND_URL = constructBackendUrl(MB_SERVER_WEB_CONTEXT_ROOT, MB_SERVER_HTTPS_PORT, MB_SERVER_HOST_NAME);
        CEP_BACKEND_URL = constructBackendUrl(CEP_SERVER_WEB_CONTEXT_ROOT, CEP_SERVER_HTTPS_PORT, CEP_SERVER_HOST_NAME);
        BRS_BACKEND_URL = constructBackendUrl(BRS_SERVER_WEB_CONTEXT_ROOT, BRS_SERVER_HTTPS_PORT, BRS_SERVER_HOST_NAME);
        DSS_BACKEND_URL = constructBackendUrl(DSS_SERVER_WEB_CONTEXT_ROOT, DSS_SERVER_HTTPS_PORT, DSS_SERVER_HOST_NAME);
        IS_BACKEND_URL = constructBackendUrl(IS_SERVER_WEB_CONTEXT_ROOT, IS_SERVER_HTTPS_PORT, IS_SERVER_HOST_NAME);
        MANAGER_BACKEND_URL = constructBackendUrl(MANAGER_SERVER_WEB_CONTEXT_ROOT, MANAGER_SERVER_HTTPS_PORT, MANAGER_SERVER_HOST_NAME);
        BAM_BACKEND_URL = constructBackendUrl(BAM_SERVER_WEB_CONTEXT_ROOT, BAM_SERVER_HTTPS_PORT, BAM_SERVER_HOST_NAME);
        MS_BACKEND_URL = constructBackendUrl(MS_SERVER_WEB_CONTEXT_ROOT, MS_SERVER_HTTPS_PORT, MS_SERVER_HOST_NAME);
    }

    public static void main(String[] args) {
        getFrameworkProperties();
        setBackendUrls();

    }


    public static boolean getStratosTestStatus() {

        if (inputStream != null) {
            try {
                prop.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String Stratosvalue = (prop.getProperty("stratos.test", "false"));
        boolean StratosValueBoolean = Boolean.parseBoolean(Stratosvalue);
        STRATOS_TEST = StratosValueBoolean;
        if (StratosValueBoolean) {
            return true;
        }

        String strBoolean = "true";

        //Do the String to boolean conversion
        boolean theValue = Boolean.parseBoolean(strBoolean);

        System.out.println(theValue);

        return false;
    }

    public static String getKeyStoreLocation() {
        String keyStoreLocation;
        if (FrameworkSettings.getStratosTestStatus()) {
            FrameworkSettings.getStratosProperties();
            keyStoreLocation = FrameworkSettings.STRATOS_TRUSTSTORE_PATH;


        } else {
            FrameworkSettings.getProductProperties();
            keyStoreLocation = FrameworkSettings.PRODUCT_TRUSTSTORE_PATH;
        }
        return keyStoreLocation;
    }

    public static String getKeyStorePassword() {
        String keyStorePassword;
        if (FrameworkSettings.getStratosTestStatus()) {
            FrameworkSettings.getStratosProperties();
            keyStorePassword = FrameworkSettings.STRATOS_TRUSTSTORE_PASSWORD;


        } else {
            FrameworkSettings.getProductProperties();
            keyStorePassword = FrameworkSettings.PRODUCT_TRUSTSTORE_PASSWORD;
        }
        return keyStorePassword;
    }

    public static void getFrameworkProperties() {
        if (FrameworkSettings.getStratosTestStatus()) {
            FrameworkSettings.getStratosProperties();
            FrameworkSettings.setBackendUrls();
        } else {
            FrameworkSettings.getProductProperties();
            FrameworkSettings.setBackendUrls();
        }
    }

}
