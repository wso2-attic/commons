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
import org.wso2.carbon.service.mgt.stub.*;
import org.wso2.carbon.service.mgt.stub.Exception;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceGroupMetaData;

import java.rmi.RemoteException;


public class AdminServiceService {
    private static final Log log = LogFactory.getLog(AdminServiceService.class);

    private final String serviceName = "ServiceAdmin";
    private ServiceAdminStub serviceAdminStub;
    private String endPoint;

    public AdminServiceService(String backEndUrl) throws AxisFault {
        this.endPoint = backEndUrl + "/services/" + serviceName;
        serviceAdminStub = new ServiceAdminStub(endPoint);
    }

    public void deleteService(String sessionCookie, String[] serviceGroup) throws RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, serviceAdminStub);
        serviceAdminStub.deleteServiceGroups(serviceGroup);

    }

    public void deleteAllNonAdminServiceGroups(String sessionCookie) throws RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, serviceAdminStub);
        serviceAdminStub.deleteAllNonAdminServiceGroups();

    }

    public ServiceGroupMetaData listServiceGroups(String sessionCookie, String serviceGroup) throws RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, serviceAdminStub);
        return serviceAdminStub.listServiceGroup(serviceGroup);

    }

    public void startService(String sessionCookie, String serviceName) throws Exception, RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, serviceAdminStub);

        serviceAdminStub.startService(serviceName);
        log.info("Service Started");

    }

    public void stopService(String sessionCookie, String serviceName) throws Exception, RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, serviceAdminStub);

        serviceAdminStub.stopService(serviceName);
        log.info("Service Stopped");

    }
}
