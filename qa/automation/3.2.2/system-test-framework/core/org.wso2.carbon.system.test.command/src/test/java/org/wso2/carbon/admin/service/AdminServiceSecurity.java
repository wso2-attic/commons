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

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.security.mgt.stub.config.ApplySecurity;
import org.wso2.carbon.security.mgt.stub.config.DisableSecurityOnService;
import org.wso2.carbon.security.mgt.stub.config.SecurityAdminServiceSecurityConfigExceptionException;
import org.wso2.carbon.security.mgt.stub.config.SecurityAdminServiceStub;

import java.rmi.RemoteException;

public class AdminServiceSecurity {
    private static final Log log = LogFactory.getLog(AdminServiceSecurity.class);

    private final String securityServiceName = "SecurityAdminService";
    private SecurityAdminServiceStub securityAdminServiceStub;
    private String endPoint;

    public AdminServiceSecurity(String hostName) throws AxisFault {
        this.endPoint = "https://" + hostName + "/services/" + securityServiceName;
        System.out.println(endPoint);
        securityAdminServiceStub = new SecurityAdminServiceStub(endPoint);
    }

    public void applySecurity(String sessionCookie, String serviceName, String policyId, String[] userGroups, String[] trustedKeyStoreArray, String StrPrivateStore) throws RemoteException, SecurityAdminServiceSecurityConfigExceptionException {
        String[] trustedKeyStore = trustedKeyStoreArray;
        String privateStore = StrPrivateStore;

        new AuthenticateStub().authenticateStub(sessionCookie, securityAdminServiceStub);
        ApplySecurity applySecurity;
        applySecurity = new ApplySecurity();
        applySecurity.setServiceName(serviceName);
        applySecurity.setPolicyId("scenario" + policyId);
        applySecurity.setTrustedStores(trustedKeyStore);
        applySecurity.setPrivateStore(privateStore);
        applySecurity.setUserGroupNames(userGroups);

        securityAdminServiceStub.applySecurity(applySecurity);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        log.info("Security Applied");


    }

    public void disableSecurity(String sessionCookie, String serviceName) throws SecurityAdminServiceSecurityConfigExceptionException, RemoteException {

        DisableSecurityOnService disableRequest = new DisableSecurityOnService();
        disableRequest.setServiceName(serviceName);

        new AuthenticateStub().authenticateStub(sessionCookie, securityAdminServiceStub);

        securityAdminServiceStub.disableSecurityOnService(disableRequest);
        log.info("Security Disabled");


    }

}
