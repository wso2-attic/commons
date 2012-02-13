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
import org.wso2.carbon.account.mgt.stub.services.BillingDataAccessServiceExceptionException;
import org.wso2.carbon.account.mgt.stub.services.BillingDataAccessServiceStub;
import org.wso2.carbon.account.mgt.stub.services.beans.xsd.Customer;
import org.wso2.carbon.account.mgt.stub.services.beans.xsd.Subscription;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;


import java.rmi.RemoteException;

public class AdminServiceBillingDataAccessService {

    private static final Log log = LogFactory.getLog(AdminServiceTenantMgtServiceAdmin.class);

    private BillingDataAccessServiceStub billingDataAccessServiceStub;

    public AdminServiceBillingDataAccessService(String backendServerURL) {
        String serviceName = "BillingDataAccessService";
        String endPoint = backendServerURL + serviceName;
        try {
            billingDataAccessServiceStub = new BillingDataAccessServiceStub(endPoint);
        } catch (AxisFault axisFault) {
            log.error("Failed to initialized billingDataAccessServiceStub : " + axisFault.getMessage());
            Assert.fail("Failed to initialized billingDataAccessServiceStub : " + axisFault.getMessage());
        }
    }

    public boolean updateUsagePlan(String sessionCookie, String tenantDomainName, String usagePlanName) {
        Customer customer;
        Boolean updateStatus;
        new AuthenticateStub().authenticateStub(sessionCookie, billingDataAccessServiceStub);
        try {
            customer = billingDataAccessServiceStub.getCustomerWithName(tenantDomainName);
            updateStatus = billingDataAccessServiceStub.changeSubscription(customer.getId(), usagePlanName);
        } catch (Exception e) {
            log.error("Unable to update usage plan : " + e.getMessage());
            Assert.fail("Unable to update usage plan : " + e.getMessage());
            return false;
        }
        return updateStatus;
    }

    public void getActiveSubscriptionOfCustomer(String sessionCookie, int customerId) {
        new AuthenticateStub().authenticateStub(sessionCookie, billingDataAccessServiceStub);
        try {
            billingDataAccessServiceStub.getActiveSubscriptionOfCustomer(customerId);
        } catch (RemoteException e) {
            log.error("Subscription update failed:" + e.getMessage());
            Assert.fail("Subscription update failed:" + e.getMessage());
        } catch (BillingDataAccessServiceExceptionException e) {
            log.error("Subscription update failed :" + e.getMessage());
            Assert.fail("Subscription update failed :" + e.getMessage());
        }
    }

    public Customer getCustomerWithName(String sessionCookie, String customerName) {
        new AuthenticateStub().authenticateStub(sessionCookie, billingDataAccessServiceStub);
        Customer customer = null;
        try {
            customer = billingDataAccessServiceStub.getCustomerWithName(customerName);
        } catch (RemoteException e) {
            log.error("Subscription update failed:" + e.getMessage());
            Assert.fail("Subscription update failed:" + e.getMessage());
        } catch (BillingDataAccessServiceExceptionException e) {
            log.error("Subscription update failed :" + e.getMessage());
            Assert.fail("Subscription update failed :" + e.getMessage());
        }
        return customer;
    }

    public Subscription getSubscription(String sessionCookie, int subscriptionId) {
        new AuthenticateStub().authenticateStub(sessionCookie, billingDataAccessServiceStub);
        Subscription subscription = null;
        try {
            subscription = billingDataAccessServiceStub.getSubscription(subscriptionId);
        } catch (RemoteException e) {
            log.error("Subscription update failed:" + e.getMessage());
            Assert.fail("Subscription update failed:" + e.getMessage());
        } catch (BillingDataAccessServiceExceptionException e) {
            log.error("Subscription update failed :" + e.getMessage());
            Assert.fail("Subscription update failed :" + e.getMessage());
        }
        return subscription;
    }

    public String getUsagePlanName(String sessionCookie, String tenantName) {
        Customer customer;
        Subscription subscription;
        new AuthenticateStub().authenticateStub(sessionCookie, billingDataAccessServiceStub);

        try {
            customer = billingDataAccessServiceStub.getCustomerWithName(tenantName);
            if (customer != null) {
                subscription = billingDataAccessServiceStub.getActiveSubscriptionOfCustomer(customer.getId());
                if (subscription != null) {
                    return subscription.getSubscriptionPlan();
                } else {
                    return new String("");
                }
            }
        } catch (Exception e) {
            String msg = "Error occurred while getting the usage place for tenant: " + tenantName;
            log.error(msg, e);
            Assert.fail(msg + e);
        }
        return new String("");
    }
}
