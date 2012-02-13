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
package org.wso2.carbon.system.test.core;

import junit.framework.Assert;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.aarservices.stub.ExceptionException;
import org.wso2.carbon.admin.service.AdminServiceAARServiceUploader;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceCarbonAppUploader;
import org.wso2.carbon.admin.service.AdminServiceModuleAdminService;
import org.wso2.carbon.admin.service.AdminServiceWebAppAdmin;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.system.test.core.utils.*;


import javax.activation.DataHandler;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.wso2.carbon.system.test.core.utils.TenantListCsvReader.*;

public class ArtifactDeployer {

    private static final Log log = LogFactory.getLog(ArtifactDeployer.class);
    protected Map<Integer, String> productMap;
    protected MultiValueMapUtil<String, String> jarArtifactMap;
    protected MultiValueMapUtil<String, String> marArtifactMap;
    protected MultiValueMapUtil<String, String> garArtifactMap;
    protected MultiValueMapUtil<String, String> carArtifactMap;
    protected MultiValueMapUtil<String, String> aarArtifactMap;
    protected MultiValueMapUtil<String, String> xmlArtifactMap;
    protected MultiValueMapUtil<String, String> warArtifactMap;
    protected static TestConfigurationReader testConfigurationReader = null;
    protected static TestConfigurationReader.TestConfig testConfig = null;


    public void Initializer(String testClassName) {
        testConfigurationReader = TestConfigurationReader.getInstance();
        testConfig = testConfigurationReader.getTestConfig(testClassName);

        //get framework properties by checking test category - Strotos or products
        FrameworkSettings.getFrameworkProperties();


        if (testConfig != null) {
            productMap = testConfig.getProductName();
            jarArtifactMap = testConfig.getJarArtifact();
            marArtifactMap = testConfig.getMarArtifact();
            aarArtifactMap = testConfig.getAarArtifact();
            carArtifactMap = testConfig.getCarArtifact();
            xmlArtifactMap = testConfig.getXmlArtifact();
            garArtifactMap = testConfig.getGarArtifact();
            warArtifactMap = testConfig.getWarArtifact();
        }
    }

    public void artifactDeployment(String testClassName) {
        Initializer(testClassName);
        if (productMap != null) {
            productArtifactDeployment();
        }
    }

    public void productArtifactDeployment() {

        for (int i = 1; i <= productMap.size(); i++) {

            if (productMap.get(i).contains(ProductConstant.APP_SERVER_NAME)) {

                //get app server artifact location
                String artifactLocation = ProductConstant.getResourceLocations(ProductConstant.APP_SERVER_NAME);


                if (!carArtifactMap.getKey().contains(ProductConstant.APP_SERVER_NAME)) {
                    for (Object carArtifactKey : carArtifactMap.getKey()) {
                        String key = carArtifactKey.toString();
                        if (key.contains(ProductConstant.APP_SERVER_NAME)) {
                            String backendUrl = FrameworkSettings.APP_BACKEND_URL;
                            for (Object carArtifactkey : carArtifactMap.getValues(key)) {
                                String artifactName = getArtifactNameString(carArtifactkey.toString());
                                int tenantId = getTenantId(key);
                                TenantDetails tenantDetails = getTenantDetails(tenantId);
                                try {
                                    URL url = new URL("file:///" + artifactLocation + File.separator + "car" + File.separator + artifactName);
                                    String sessionCookie = login(tenantDetails.getTenantName(), tenantDetails.getTenantPassword(), backendUrl);
                                    carFileUploder(sessionCookie, backendUrl, url, artifactName);
                                    log.info("car file: " + artifactName + " deployed successfully ");
                                    System.out.println("car file: " + artifactName + "deployed successfully");
                                } catch (MalformedURLException e) {
                                    log.error("MalformedURL exception thrown while initializing car artifact url" + e.getMessage());
                                    Assert.fail("MalformedURL exception thrown while initializing car artifact url" + e.getMessage());
                                }
                            }
                        }
                    }
                }

                if (!warArtifactMap.getKey().contains(ProductConstant.APP_SERVER_NAME)) {
                    for (Object warArtifactKey : warArtifactMap.getKey()) {
                        String key = warArtifactKey.toString();
                        if (key.contains(ProductConstant.APP_SERVER_NAME)) {
                            String backendUrl = FrameworkSettings.APP_BACKEND_URL;
                            for (Object warArtifactName : warArtifactMap.getValues(key)) {
                                String artifactName = getArtifactNameString(warArtifactName.toString());
                                String filePath = artifactLocation + File.separator + "war" + File.separator + artifactName;
                                int tenantId = getTenantId(key);
                                TenantDetails tenantDetails = getTenantDetails(tenantId);
                                String sessionCookie = login(tenantDetails.getTenantName(), tenantDetails.getTenantPassword(), backendUrl);
                                warFileUploder(sessionCookie, backendUrl, filePath);
                            }
                        }
                    }
                }

                if (!aarArtifactMap.getKey().contains(ProductConstant.APP_SERVER_NAME)) {
                    for (Object aarArtifactKey : aarArtifactMap.getKey()) {
                        String key = aarArtifactKey.toString();
                        if (key.contains(ProductConstant.APP_SERVER_NAME)) {
                            String backendUrl = FrameworkSettings.APP_BACKEND_URL;
                            for (Object aarArtifactName : aarArtifactMap.getValues(key)) {
                                String artifactName = getArtifactNameString(aarArtifactName.toString());
                                String filePath = artifactLocation + File.separator + "aar" + File.separator + artifactName;
                                int tenantId = getTenantId(key);
                                TenantDetails tenantDetails = getTenantDetails(tenantId);
                                String sessionCookie = login(tenantDetails.getTenantName(), tenantDetails.getTenantPassword(), backendUrl);
                                aarFileUploder(sessionCookie, backendUrl, artifactName, filePath);
                            }
                        }
                    }
                }
            }


            if (productMap.get(i).contains(ProductConstant.ESB_SERVER_NAME)) {
                String artifactLocationESB = ProductConstant.getResourceLocations(ProductConstant.ESB_SERVER_NAME);


                if (!carArtifactMap.getKey().contains(ProductConstant.ESB_SERVER_NAME)) {
                    for (Object carArtifactKeys : carArtifactMap.getKey()) {
                        String key = carArtifactKeys.toString();
                        if (key.contains(ProductConstant.ESB_SERVER_NAME)) {
                            String backendUrl = FrameworkSettings.ESB_BACKEND_URL;
                            for (Object carArtifactName : carArtifactMap.getValues(key)) {
                                String artifactName = getArtifactNameString(carArtifactName.toString());
                                int tenantId = getTenantId(key);
                                TenantDetails tenantDetails = getTenantDetails(tenantId);
                                try {
                                    URL url = new URL("file:///" + artifactLocationESB + File.separator + "car" + File.separator + artifactName);
                                    String sessionCookie = login(tenantDetails.getTenantName(), tenantDetails.getTenantPassword(), backendUrl);
                                    carFileUploder(sessionCookie, backendUrl, url, artifactName);
                                } catch (MalformedURLException e) {
                                    log.error("MalformedURL exception thrown while initializing car artifact url" + e.getMessage());
                                    Assert.fail("MalformedURL exception thrown while initializing car artifact url" + e.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private static void carFileUploder(String sessionCookie, String hostName, URL url, String artifactName) {
        DataHandler dh = new DataHandler(url);
        AdminServiceCarbonAppUploader adminServiceCarbonAppUploader = new AdminServiceCarbonAppUploader(hostName);
        adminServiceCarbonAppUploader.uploadCarbonAppArtifact(sessionCookie, artifactName, dh);
    }

    private static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }

    private static void warFileUploder(String sessionCookie, String hostName, String filePath) {
        AdminServiceWebAppAdmin AdminServiceWebAppAdmin = new AdminServiceWebAppAdmin(hostName, filePath);
        AdminServiceWebAppAdmin.warFileUplaoder(sessionCookie);
    }

    private static void aarFileUploder(String sessionCookie, String backEndUrl, String artifactName, String filePath) {
        AdminServiceAARServiceUploader adminServiceAARServiceUploader = new AdminServiceAARServiceUploader(backEndUrl);
        adminServiceAARServiceUploader.uploadAARFile(sessionCookie, artifactName, filePath, "");
    }

    private static String getArtifactNameString(String str) {
        return str.replaceAll("[\\]\\[]", "");
    }

    public static void main(String[] args) {
        ArtifactDeployer ad = new ArtifactDeployer();
        ad.artifactDeployment("org.wso2.stratos.automation.test.as.StratosAxis2ServiceTest");

    }
}

