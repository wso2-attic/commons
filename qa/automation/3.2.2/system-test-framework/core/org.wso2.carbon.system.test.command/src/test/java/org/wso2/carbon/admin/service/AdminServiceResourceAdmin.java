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
import org.wso2.carbon.registry.resource.stub.ExceptionException;
import org.wso2.carbon.registry.resource.stub.ResourceAdminServiceStub;

import javax.activation.DataHandler;
import java.rmi.RemoteException;


public class AdminServiceResourceAdmin {
    private static final Log log = LogFactory.getLog(AdminServiceResourceAdmin.class);

    private final String serviceName = "ResourceAdminService";
    private ResourceAdminServiceStub resourceAdminServiceStub;
    private String endPoint;

    private static final String MEDIA_TYPE_WSDL = "application/wsdl+xml";
    private static final String MEDIA_TYPE_SCHEMA = "application/x-xsd+xml";
    private static final String MEDIA_TYPE_POLICY = "application/policy+xml";
    private static final String MEDIA_TYPE_GOVERNANCE_ARCHIVE = "application/vnd.wso2.governance-archive";

    public AdminServiceResourceAdmin(String backEndUrl) throws AxisFault {
        this.endPoint = backEndUrl + "/services/" + serviceName;
        resourceAdminServiceStub = new ResourceAdminServiceStub(endPoint);
    }

    public void addResource(String sessionCookie, String destinationPath, String mediaType, String description, DataHandler dh) throws RemoteException, ExceptionException {

        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        resourceAdminServiceStub.addResource(destinationPath, mediaType, description, dh, null);
        log.info("Resource Added");

    }

    public void deleteResource(String sessionCookie, String destinationPath) throws RemoteException, ExceptionException {

        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        resourceAdminServiceStub.delete(destinationPath);
        log.info("Resource Added");

    }

    public void addWSDL(String sessionCookie, String description, DataHandler dh) throws RemoteException, ExceptionException {
        String fileName;
        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        fileName = dh.getName().substring(dh.getName().lastIndexOf('/') + 1);
        resourceAdminServiceStub.addResource("/" + fileName, MEDIA_TYPE_WSDL, description, dh, null);
        log.info("Resource Added");

    }

    public void addWSDL(String sessionCookie, String resourceName, String description, String fetchURL) throws RemoteException, ExceptionException {

        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        resourceAdminServiceStub.importResource("/", resourceName, MEDIA_TYPE_WSDL, description, fetchURL, null);
        log.info("Resource Added");

    }

    public void addSchema(String sessionCookie, String description, DataHandler dh) throws RemoteException, ExceptionException {
        String fileName;
        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        fileName = dh.getName().substring(dh.getName().lastIndexOf('/') + 1);
        resourceAdminServiceStub.addResource("/" + fileName, MEDIA_TYPE_SCHEMA, description, dh, null);
        log.info("Resource Added");

    }

    public void addSchema(String sessionCookie, String resourceName, String description, String fetchURL) throws RemoteException, ExceptionException {

        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        resourceAdminServiceStub.importResource("/", resourceName, MEDIA_TYPE_SCHEMA, description, fetchURL, null);
        log.info("Resource Added");

    }

    public void addPolicy(String sessionCookie, String description, DataHandler dh) throws RemoteException, ExceptionException {
        String fileName;
        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        fileName = dh.getName().substring(dh.getName().lastIndexOf('/') + 1);
        resourceAdminServiceStub.addResource("/" + fileName, MEDIA_TYPE_POLICY, description, dh, null);
        log.info("Resource Added");

    }

    public void addPolicy(String sessionCookie, String resourceName, String description, String fetchURL) throws RemoteException, ExceptionException {

        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        resourceAdminServiceStub.importResource("/", resourceName, MEDIA_TYPE_POLICY, description, fetchURL, null);
        log.info("Resource Added");

    }

    public void uploadArtifact(String sessionCookie, String description, DataHandler dh) throws RemoteException, ExceptionException {
        String fileName;
        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        fileName = dh.getName().substring(dh.getName().lastIndexOf('/') + 1);
        resourceAdminServiceStub.addResource("/" + fileName, MEDIA_TYPE_GOVERNANCE_ARCHIVE, description, dh, null);
        log.info("Resource Added");

    }

    public void addCollection(String sessionCookie, String parentPath, String collectionName, String mediaType, String description) throws RemoteException, ExceptionException {
        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        resourceAdminServiceStub.addCollection(parentPath, collectionName, mediaType, description);
        log.info("Collection Added");
    }

}
