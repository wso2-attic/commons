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
import org.wso2.carbon.admin.service.AdminServiceApplicationAdmin;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceService;
import org.wso2.carbon.admin.service.AdminServiceWebAppAdmin;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminExceptionException;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.system.test.core.utils.TenantDetails;

import static org.wso2.carbon.system.test.core.utils.TenantListCsvReader.*;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

public class ArtifactCleaner extends ArtifactDeployer {

    private static final Log log = LogFactory.getLog(ArtifactCleaner.class);

    public void artifactClearner() {
        if (productMap != null) {
            try {
                productArtifactClearner();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public void productArtifactClearner() throws InterruptedException, MalformedURLException {

        for (int i = 1; i <= productMap.size(); i++) {

            if (productMap.get(i).contains(ProductConstant.APP_SERVER_NAME)) {
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
                                    deleteAllCarArtifacts(backendUrl, sessionCookie);
                                    log.info("car file: " + artifactName + " undeployed successfully ");

                                } catch (MalformedURLException e) {
                                    log.error("MalformedURL exception thrown while initializing car artifact url" + e.getMessage());
                                    Assert.fail("MalformedURL exception thrown while initializing car artifact url" + e.getMessage());
                                }
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
                                deleteAllNotAdminServices(sessionCookie, backendUrl);
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
                                int tenantId = getTenantId(key);
                                TenantDetails tenantDetails = getTenantDetails(tenantId);
                                String sessionCookie = login(tenantDetails.getTenantName(), tenantDetails.getTenantPassword(), backendUrl);
                                deleteWebApp(sessionCookie, artifactName);
                            }
                        }
                    }
                }
            }
        }
    }


    public void deleteAllCarArtifacts(String serviceURL, String sessionCookie) {

        String carAppList[] = getAllCarArtifactList(sessionCookie, serviceURL);
        AdminServiceApplicationAdmin adminServiceApplicationAdmin = null;

        try {
            adminServiceApplicationAdmin = new AdminServiceApplicationAdmin(serviceURL);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (carAppList != null) {
            for (int i = 0; i < carAppList.length; i++) {
                try {
                    adminServiceApplicationAdmin.deleteApplication(sessionCookie, carAppList[i]);
                } catch (ApplicationAdminExceptionException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (RemoteException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        waitForCarUndeployment(sessionCookie, serviceURL);
    }

    public static String[] getAllCarArtifactList(String sessionCookie, String serviceURL) {
        String[] appList = null;
        AdminServiceApplicationAdmin adminServiceApplicationAdmin = null;
        try {
            adminServiceApplicationAdmin = new AdminServiceApplicationAdmin(serviceURL);
            appList = adminServiceApplicationAdmin.listAllApplications(sessionCookie);

        } catch (ApplicationAdminExceptionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return appList;

    }

    public static String[] deleteAllNotAdminServices(String sessionCookie, String serviceURL) {
        String[] appList = null;
        AdminServiceService adminServiceService = null;
        adminServiceService = new AdminServiceService(serviceURL);
        adminServiceService.deleteAllNonAdminServiceGroups(sessionCookie);

        return appList;

    }


    public static void waitForCarUndeployment(String sessionCookie, String ServiceURL) {
        int serviceTimeOut = 0;
        while (getAllCarArtifactList(sessionCookie, ServiceURL) != null) {
            if (serviceTimeOut == 0) {
            } else if (serviceTimeOut > 200) { //Check for the applist for 100 seconds
                // if Service not available assertfalse;
                Assert.fail("Car artifact clearance failed");
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

    public void deleteCarArtifact(String serviceURL, String sessionCookie, String carFileName) {

        String carbonAppUnLoaderURL = serviceURL + "ApplicationAdmin";
        AuthenticateStub authenticateStub = new AuthenticateStub();

        ApplicationAdminStub applicationAdminStub = null;
        try {
            applicationAdminStub = new ApplicationAdminStub(carbonAppUnLoaderURL);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
        authenticateStub.authenticateStub(sessionCookie, applicationAdminStub);

        // String[] appList;
        try {
            //appList = applicationAdminStub.listAllApplications();

            //for (int i = 0; i < appList.length; i++) {
            applicationAdminStub.deleteApplication(carFileName);
            //}
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ApplicationAdminExceptionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void deleteWebApp(String sessionCookie, String fileName) {
        AdminServiceWebAppAdmin AdminServiceWebAppAdmin = new AdminServiceWebAppAdmin(FrameworkSettings.APP_BACKEND_URL);
        AdminServiceWebAppAdmin.deleteWebAppFile(sessionCookie, fileName);
    }


}

