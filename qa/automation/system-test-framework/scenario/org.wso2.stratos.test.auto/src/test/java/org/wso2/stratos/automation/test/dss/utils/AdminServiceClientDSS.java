/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.stratos.automation.test.dss.utils;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.*;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.security.mgt.stub.config.SecurityAdminServiceSecurityConfigExceptionException;
import org.wso2.carbon.admin.service.AdminServiceDataServiceFileUploader;
import org.wso2.carbon.service.mgt.stub.types.carbon.FaultyService;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaData;

import javax.activation.DataHandler;
import java.rmi.RemoteException;
import java.util.Calendar;


public class AdminServiceClientDSS {
    private static final Log log = LogFactory.getLog(AdminServiceClientDSS.class);
    protected String backEndUrl;

    public AdminServiceClientDSS(String backEndUrl) {
        this.backEndUrl = backEndUrl;
    }

    public void uploadArtifact(String sessionCookie, String fileName, DataHandler dh) {
        AdminServiceDataServiceFileUploader adminServiceDataServiceFileUploader = new AdminServiceDataServiceFileUploader(backEndUrl);
        adminServiceDataServiceFileUploader.uploadDataServiceFile(sessionCookie, fileName, dh);
    }

    public void deleteService(String sessionCookie, String[] serviceGroup) {
        AdminServiceService adminServiceService = new AdminServiceService(backEndUrl);
        adminServiceService.deleteService(sessionCookie, serviceGroup);

    }

    public void deleteFaultyService(String sessionCookie, String artifactPath) {
        AdminServiceService adminServiceService = new AdminServiceService(backEndUrl);
        adminServiceService.deleteFaultyService(sessionCookie, artifactPath);

    }

    public void activeteService(String sessionCookie, String serviceName) throws Exception, RemoteException {
        AdminServiceService adminServiceService = new AdminServiceService(backEndUrl);
        adminServiceService.startService(sessionCookie, serviceName);

    }

    public void deactivateService(String sessionCookie, String serviceName) throws RemoteException, LoginAuthenticationExceptionException, Exception {
        AdminServiceService adminServiceService = new AdminServiceService(backEndUrl);
        adminServiceService.stopService(sessionCookie, serviceName);

    }

    public void applySecurity(String sessionCookie, String serviceName, String policyId, String[] userGroups, String[] trustedKeyStoreArray, String privateStore) {
        AdminServiceSecurity adminServiceSecurity = new AdminServiceSecurity(backEndUrl);
        adminServiceSecurity.applySecurity(sessionCookie, serviceName, policyId, userGroups, trustedKeyStoreArray, privateStore);

    }

    public void applyKerberosSecurity(String sessionCookie, String serviceName, String policyId, String ServicePrincipalName, String ServicePrincipalPassword) {
        AdminServiceSecurity adminServiceSecurity = new AdminServiceSecurity(backEndUrl);
        adminServiceSecurity.applyKerberosSecurityPolicy(sessionCookie, serviceName, policyId, ServicePrincipalName, ServicePrincipalPassword);

    }

    public void disableSecurity(String sessionCookie, String serviceName) throws SecurityAdminServiceSecurityConfigExceptionException, RemoteException, LoginAuthenticationExceptionException {
        AdminServiceSecurity adminServiceSecurity = new AdminServiceSecurity(backEndUrl);
        adminServiceSecurity.disableSecurity(sessionCookie, serviceName);

    }


    public String authenticate(String userName, String passWord) {
        AdminServiceAuthentication adminServiceAuthentication = new AdminServiceAuthentication(backEndUrl);
        return adminServiceAuthentication.login(userName, passWord, "localhost");

    }

    public void logOut() {
        AdminServiceAuthentication adminServiceAuthentication = new AdminServiceAuthentication(backEndUrl);
        adminServiceAuthentication.logOut();

    }

    public void addResource(String sessionCookie, String destinationPath, String mediaType, String description, DataHandler dh) {
        AdminServiceResourceAdmin adminServiceResourceAdmin = new AdminServiceResourceAdmin(backEndUrl);
        adminServiceResourceAdmin.addResource(sessionCookie, destinationPath, mediaType, description, dh);
    }

    public ServiceMetaData getServiceData(String sessionCookie, String serviceName) {
        AdminServiceService adminServiceService = new AdminServiceService(backEndUrl);
        return adminServiceService.getServicesData(sessionCookie, serviceName);
    }

    public FaultyService getFaultyServiceData(String sessionCookie, String serviceName) {
        AdminServiceService adminServiceService = new AdminServiceService(backEndUrl);
        return adminServiceService.getFaultyData(sessionCookie, serviceName);
    }

    public boolean isServiceExist(String sessionCookie, String serviceName) {
        AdminServiceService adminServiceService = new AdminServiceService(backEndUrl);
        return adminServiceService.isServiceExists(sessionCookie, serviceName);
    }

    public boolean isServiceFaulty(String sessionCookie, String serviceName) {
        AdminServiceService adminServiceService = new AdminServiceService(backEndUrl);
        return adminServiceService.isServiceFaulty(sessionCookie, serviceName);
    }

    public void isServiceDeployed(String sessionCookie, String serviceName, int waitingTimeInMillis) {

        boolean isServiceDeployed = false;
        Calendar startTime = Calendar.getInstance();
        long time;
        while ((time = (Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis())) < waitingTimeInMillis) {
            if (isServiceExist(sessionCookie, serviceName)) {
                isServiceDeployed = true;
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
        }
        Assert.assertTrue("Service Not Found, Deployment time out ", isServiceDeployed);
        log.info("Service Deployed in " + time + " millis");


    }

    public void isServiceFaulty(String sessionCookie, String serviceName, int waitingTimeInMillis) {

        boolean isServiceDeployed = false;
        Calendar startTime = Calendar.getInstance();
        long time;
        while ((time = (Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis())) < waitingTimeInMillis) {
            if (isServiceFaulty(sessionCookie, serviceName)) {
                isServiceDeployed = true;
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
        }
        Assert.assertTrue("Service Not Found in faulty service list ", isServiceDeployed);


    }
}
