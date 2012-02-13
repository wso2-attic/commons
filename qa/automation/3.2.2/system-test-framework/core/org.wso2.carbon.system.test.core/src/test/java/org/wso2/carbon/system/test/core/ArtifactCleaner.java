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
import org.wso2.carbon.admin.service.AdminServiceService;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminExceptionException;
import org.wso2.carbon.application.mgt.stub.ApplicationAdminStub;
import org.wso2.carbon.admin.service.utils.ProductConstant;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Iterator;

public class ArtifactCleaner extends ArtifactDeployer {

    private static final Log log = LogFactory.getLog(ArtifactCleaner.class);
    private static String SERVICE_URL = null;

    public void ArtifactClearner(String testClassName) throws InterruptedException, MalformedURLException {
        Initializer(testClassName);
        if (productMap != null) {
            productArtifactClearner(testClassName);
        }

    }

    public void productArtifactClearner(String testClassName) throws InterruptedException, MalformedURLException {
        Initializer(testClassName);

        for (int i = 1; i <= productMap.size(); i++) {

            if (productMap.get(i).contains("AS")) {
                String sessionCookie = null;
                try {
//                    sessionCookie = serverLogin.login("AS");
                } catch (Exception e) {
                    log.error("Login to App Server failed " + e.getMessage());
                }
                String artifactLocation = ProductConstant.getResourceLocations("AS");
//                testSetting = frameworkSettings.getServiceSettings("AS");
//                SERVICE_URL = testSetting.getServiceURL();

                if (!carArtifactMap.getValues("AS").isEmpty()) {
                    for (Iterator itrCarMap = carArtifactMap.getValues("AS").iterator(); itrCarMap.hasNext();) {
                        String artifactName = itrCarMap.next().toString();

                        URL url = new URL("file:///" + artifactLocation + File.separator + "car" + File.separator + artifactName);
                        deleteAllCarArtifacts(SERVICE_URL, sessionCookie);
                    }
                }

                if (!aarArtifactMap.getValues("AS").isEmpty()) {
                    for (Iterator itrAarMap = aarArtifactMap.getValues("AS").iterator(); itrAarMap.hasNext();) {
                        String artifactName = itrAarMap.next().toString();
                        deleteAllNotAdminServices(sessionCookie, SERVICE_URL);
                    }
                }
            }

            if (productMap.get(i).contains("ESB")) {
                String sessionCookie = null;
                try {
//                    sessionCookie = serverLogin.login("ESB");
                } catch (Exception e) {
                    log.error("Login to ESB Server failed");
                }
                String artifactLocation = ProductConstant.getResourceLocations("ESB");
//                testSetting = frameworkSettings.getServiceSettings("ESB");
//                SERVICE_URL = testSetting.getServiceURL();

                if (!carArtifactMap.getValues("ESB").isEmpty()) {
                    for (Iterator itrCarMap = carArtifactMap.getValues("ESB").iterator(); itrCarMap.hasNext();) {
                        String artifactName = itrCarMap.next().toString();

                        URL url = new URL("file:///" + artifactLocation + File.separator + "car" + File.separator + artifactName);
                        deleteAllCarArtifacts(SERVICE_URL, sessionCookie);
                    }
                }

                if (!carArtifactMap.getValues("ESB").isEmpty()) {
                    for (Iterator itrCarMap = carArtifactMap.getValues("ESB").iterator(); itrCarMap.hasNext();) {
                        String artifactName = itrCarMap.next().toString();

                        URL url = new URL("file:///" + artifactLocation + File.separator + "car" + File.separator + artifactName);
                        deleteAllCarArtifacts(SERVICE_URL, sessionCookie);
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
        try {
            adminServiceService = new AdminServiceService(serviceURL);
            adminServiceService.deleteAllNonAdminServiceGroups(sessionCookie);

        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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

    public static void main(String[] args) {
        ArtifactCleaner ac = new ArtifactCleaner();
        try {
            ac.ArtifactClearner("org.wso2.carbon.system.test.ScenarioTest1");
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

