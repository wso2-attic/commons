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
import org.wso2.carbon.account.mgt.stub.beans.xsd.AccountInfoBean;
import org.wso2.carbon.account.mgt.stub.services.*;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;

import java.rmi.RemoteException;


public class AdminServiceStratosAccountMgt {

    private static final Log log = LogFactory.getLog(AdminServiceStratosAccountMgt.class);

    private AccountMgtServiceStub accountMgtServiceStub;

    public AdminServiceStratosAccountMgt(String backEndUrl) {
        String serviceName = "AccountMgtService";
        String endPoint = backEndUrl + serviceName;
        log.debug("EndPoint :" + endPoint);
        try {
            accountMgtServiceStub = new AccountMgtServiceStub(endPoint);
        } catch (AxisFault axisFault) {
            log.error("Initializing AccountMgtServiceStub failed : " + axisFault.getMessage());
            Assert.fail("Initializing AccountMgtServiceStub failed : " + axisFault.getMessage());
        }
    }

    public void deactivateTenant(String sessionCookie) {
        new AuthenticateStub().authenticateStub(sessionCookie, accountMgtServiceStub);
        try {
            accountMgtServiceStub.deactivate();
        } catch (RemoteException e) {
            log.error("Tenant deactivation fail due to remote exception " + e.getMessage());
            Assert.fail("Tenant deactivation fail due to remote exception " + e.getMessage());
        } catch (DeactivateExceptionException e) {
            log.error("Tenant deactivation fail " + e.getMessage());
            Assert.fail("Tenant deactivation fail " + e.getMessage());
        }
    }

    public void updateTenantContact(String sessionCookie, String email) {
        new AuthenticateStub().authenticateStub(sessionCookie, accountMgtServiceStub);
        try {
            accountMgtServiceStub.updateContact(email);
        } catch (RemoteException e) {
            log.error("Tenant contact info update fail " + e.getMessage());
            Assert.fail("Tenant contact info update fail " + e.getMessage());
        } catch (UpdateContactExceptionException e) {
            log.error("Tenant contact info update fail " + e.getMessage());
            Assert.fail("Tenant contact info update fail " + e.getMessage());
        }
    }

    public void updateTenantFullName(String sessionCookie, AccountInfoBean accountInfoBean) {
        new AuthenticateStub().authenticateStub(sessionCookie, accountMgtServiceStub);
        try {
            accountMgtServiceStub.updateFullname(accountInfoBean);
        } catch (RemoteException e) {
            log.error("Tenant full name update fail " + e.getMessage());
            Assert.fail("Tenant full name update fail " + e.getMessage());
        } catch (UpdateFullnameExceptionException e) {
            log.error("Tenant full name update fail " + e.getMessage());
            Assert.fail("Tenant full name update fail " + e.getMessage());
        }
    }

    public String getTenantcontact(String sessionCookie) {
        new AuthenticateStub().authenticateStub(sessionCookie, accountMgtServiceStub);
        String contactInfo = null;
        try {
            contactInfo = accountMgtServiceStub.getContact();
        } catch (RemoteException e) {
            log.error("Cannot retrieve tenant contact info " + e.getMessage());
            Assert.fail("Cannot retrieve tenant contact info " + e.getMessage());

        } catch (GetContactExceptionException e) {
            log.error("Cannot retrieve tenant contact info " + e.getMessage());
            Assert.fail("Cannot retrieve tenant contact info " + e.getMessage());
        }
        return contactInfo;
    }

    public AccountInfoBean getTenantFullName(String sessionCookie) {
        new AuthenticateStub().authenticateStub(sessionCookie, accountMgtServiceStub);
        AccountInfoBean accountInfoBean = null;
        try {
            accountInfoBean = accountMgtServiceStub.getFullname();
        } catch (RemoteException e) {
            log.error("Cannot retrieve tenant full name " + e.getMessage());
            Assert.fail("Cannot retrieveenant full name " + e.getMessage());
        } catch (GetFullnameExceptionException e) {
            log.error("Cannot retrieve tenant full name " + e.getMessage());
            Assert.fail("Cannot retrieve tenant full name " + e.getMessage());
        }
        return accountInfoBean;
    }
}
