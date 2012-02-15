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
package org.wso2.carbon.admin.service;

import junit.framework.Assert;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.security.mgt.stub.config.*;

import java.rmi.RemoteException;

public class AdminServiceSecurity {
    private static final Log log = LogFactory.getLog(AdminServiceSecurity.class);

    private final String securityServiceName = "SecurityAdminService";
    private SecurityAdminServiceStub securityAdminServiceStub;
    private String endPoint;

    public AdminServiceSecurity(String backEndUrl) {
        this.endPoint = backEndUrl + securityServiceName;
        log.debug("Endpoint : " + this.endPoint);
        try {
            securityAdminServiceStub = new SecurityAdminServiceStub(endPoint);
        } catch (AxisFault axisFault) {
            log.error("Initialization of SecurityAdminServiceStub initialization failed: " + axisFault.getMessage());
            Assert.fail("Initialization of SecurityAdminServiceStub initialization failed: " + axisFault.getMessage());
        }
    }

    public void applySecurity(String sessionCookie, String serviceName, String policyId, String[] userGroups, String[] trustedKeyStoreArray, String privateStore) {

        new AuthenticateStub().authenticateStub(sessionCookie, securityAdminServiceStub);
        ApplySecurity applySecurity;
        applySecurity = new ApplySecurity();
        applySecurity.setServiceName(serviceName);
        applySecurity.setPolicyId("scenario" + policyId);
        applySecurity.setTrustedStores(trustedKeyStoreArray);
        applySecurity.setPrivateStore(privateStore);
        applySecurity.setUserGroupNames(userGroups);

        try {
            securityAdminServiceStub.applySecurity(applySecurity);
            log.info("Security Applied");
        } catch (RemoteException e) {
            log.error("RemoteException when applying security " + applySecurity.getPolicyId() + " :" + e.getMessage());
            Assert.fail("RemoteException when applying security " + applySecurity.getPolicyId() + " :" + e.getMessage());
        } catch (SecurityAdminServiceSecurityConfigExceptionException e) {
            log.error("SecurityAdminServiceSecurityConfigExceptionException when applying security " + applySecurity.getPolicyId() + " :" + e.getMessage());
            Assert.fail("SecurityAdminServiceSecurityConfigExceptionException when applying security " + applySecurity.getPolicyId() + " :" + e.getMessage());
        }

    }

    public void applyKerberosSecurityPolicy(String sessionCookie, String serviceName, String policyId, String ServicePrincipalName, String ServicePrincipalPassword) {

        new AuthenticateStub().authenticateStub(sessionCookie, securityAdminServiceStub);
        ApplyKerberosSecurityPolicy applySecurity;
        applySecurity = new ApplyKerberosSecurityPolicy();
        applySecurity.setServiceName(serviceName);
        applySecurity.setPolicyId("scenario" + policyId);
        applySecurity.setServicePrincipalName(ServicePrincipalName);
        applySecurity.setServicePrincipalPassword(ServicePrincipalPassword);

        try {
            securityAdminServiceStub.applyKerberosSecurityPolicy(applySecurity);
            log.info("Security Applied");
        } catch (RemoteException e) {
            log.error("RemoteException when applying Kerberos security " + applySecurity.getPolicyId() + " :" + e.getMessage());
            Assert.fail("RemoteException when applying Kerberos security " + applySecurity.getPolicyId() + " :" + e.getMessage());
        } catch (SecurityAdminServiceSecurityConfigExceptionException e) {
            log.error("SecurityAdminServiceSecurityConfigExceptionException when applying Kerberos security " + applySecurity.getPolicyId() + " :" + e.getMessage());
            Assert.fail("SecurityAdminServiceSecurityConfigExceptionException when applying Kerberos security " + applySecurity.getPolicyId() + " :" + e.getMessage());
        }

    }

    public void disableSecurity(String sessionCookie, String serviceName) {

        DisableSecurityOnService disableRequest = new DisableSecurityOnService();
        disableRequest.setServiceName(serviceName);

        new AuthenticateStub().authenticateStub(sessionCookie, securityAdminServiceStub);

        try {
            securityAdminServiceStub.disableSecurityOnService(disableRequest);
            log.info("Security Disabled");
        } catch (RemoteException e) {
            log.error("RemoteException when disabling security: " + e.getMessage());
            Assert.fail("RemoteException when disabling security: " + e.getMessage());
        } catch (SecurityAdminServiceSecurityConfigExceptionException e) {
            log.error("SecurityAdminServiceSecurityConfigExceptionException when disabling security: " + e.getMessage());
            Assert.fail("SecurityAdminServiceSecurityConfigExceptionException when disabling security: " + e.getMessage());

        }


    }

}
