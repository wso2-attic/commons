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
import org.wso2.carbon.registry.resource.stub.ExceptionException;
import org.wso2.carbon.registry.resource.stub.ResourceAdminServiceStub;
import org.wso2.carbon.registry.resource.stub.common.xsd.ResourceData;

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

    public AdminServiceResourceAdmin(String backEndUrl) {
        this.endPoint = backEndUrl + serviceName;
        log.debug("Endpoint :" + endPoint);
        try {
            resourceAdminServiceStub = new ResourceAdminServiceStub(endPoint);
        } catch (AxisFault axisFault) {
            log.error("Initializing ResourceAdminServiceStub failed : " + axisFault.getMessage());
            Assert.fail("Initializing ResourceAdminServiceStub failed : " + axisFault.getMessage());
        }
    }

    public void addResource(String sessionCookie, String destinationPath, String mediaType, String description, DataHandler dh) {

        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        log.debug("Destination Path :" + destinationPath);
        log.debug("Media Type :" + mediaType);
        try {
            Assert.assertTrue("Resource Adding Failed ", resourceAdminServiceStub.addResource(destinationPath, mediaType, description, dh, null));
            log.info("Resource Added");
        } catch (RemoteException e) {
            log.error("Resource adding failed due to RemoteException : " + e.getMessage());
            Assert.fail("Resource adding failed due to RemoteException : " + e.getMessage());
        } catch (ExceptionException e) {
            log.error("Resource adding failed due to ExceptionException : " + e.getMessage());
            Assert.fail("Resource adding failed due to ExceptionException : " + e.getMessage());
        }

    }

    public ResourceData[] getResource(String sessionCookie, String destinationPath) {
        ResourceData[] rs = null;
        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        log.debug("Destination Path :" + destinationPath);
        try {
            rs = resourceAdminServiceStub.getResourceData(new String[]{destinationPath});
            log.info("Resource Data Received");
        } catch (RemoteException e) {
            log.error("Resource getting failed due to RemoteException : " + e.getMessage());
            Assert.fail("Resource getting stub failed due to RemoteException : " + e.getMessage());
        } catch (ExceptionException e) {
            log.error("Resource getting failed due to Exception : " + e.getMessage());
            Assert.fail("Resource getting failed due to Exception : " + e.getMessage());
        }
        Assert.assertNotNull("ResourceData object null", rs);
        return rs;

    }

    public void deleteResource(String sessionCookie, String destinationPath) {

        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        log.debug("Destination Path :" + destinationPath);
        try {
            Assert.assertTrue("Resource Not Deleted", resourceAdminServiceStub.delete(destinationPath));
            log.info("Resource Deleted");
        } catch (RemoteException e) {
            log.error("Resource Deleting failed due to Exception : " + e.getMessage());
            Assert.fail("Resource Deleting failed due to Exception : " + e.getMessage());
        } catch (ExceptionException e) {
            log.error("Resource Deleting failed due to ExceptionException : " + e.getMessage());
            Assert.fail("Resource Deleting failed due to ExceptionException : " + e.getMessage());
        }


    }

    public void addWSDL(String sessionCookie, String description, DataHandler dh) {
        String fileName;
        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        fileName = dh.getName().substring(dh.getName().lastIndexOf('/') + 1);
        log.debug(fileName);
        try {
            Assert.assertTrue("WSDL Adding failed", resourceAdminServiceStub.addResource("/" + fileName, MEDIA_TYPE_WSDL, description, dh, null));
            log.info("WSDL Added");
        } catch (RemoteException e) {
            log.error("WSDL adding failed due to RemoteException : " + e.getMessage());
            Assert.fail("WSDL adding failed due to RemoteException : " + e.getMessage());
        } catch (ExceptionException e) {
            log.error("WSDL adding failed due to ExceptionException : " + e.getMessage());
            Assert.fail("WSDL adding failed due to ExceptionException : " + e.getMessage());
        }

    }

    public void addWSDL(String sessionCookie, String resourceName, String description, String fetchURL) {

        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        log.debug("fetchUrl :" + fetchURL);
        try {
            Assert.assertTrue("Importing WSDL from URL failed", resourceAdminServiceStub.importResource("/", resourceName, MEDIA_TYPE_WSDL, description, fetchURL, null));
            log.info("WSDL imported");
        } catch (RemoteException e) {
            log.error("Importing WSDL from URL failed due to RemoteException : " + e.getMessage());
            Assert.fail("Importing WSDL from URL failed due to RemoteException : " + e.getMessage());
        } catch (ExceptionException e) {
            log.error("Importing WSDL from URL failed due to ExceptionException : " + e.getMessage());
            Assert.fail("Importing WSDL from URL failed due to ExceptionException : " + e.getMessage());
        }

    }

    public void addSchema(String sessionCookie, String description, DataHandler dh) {
        String fileName;
        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        fileName = dh.getName().substring(dh.getName().lastIndexOf('/') + 1);
        log.debug("File Name: " + fileName);
        try {
            Assert.assertTrue("Schema Adding failed", resourceAdminServiceStub.addResource("/" + fileName, MEDIA_TYPE_SCHEMA, description, dh, null));
            log.info("Schema Added");
        } catch (RemoteException e) {
            log.error("Schema adding failed due to RemoteException : " + e.getMessage());
            Assert.fail("Schema adding failed due to RemoteException : " + e.getMessage());
        } catch (ExceptionException e) {
            log.error("Schema adding failed due to ExceptionException : " + e.getMessage());
            Assert.fail("Schema adding failed due to ExceptionException : " + e.getMessage());
        }


    }

    public void addSchema(String sessionCookie, String resourceName, String description, String fetchURL) {

        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        log.debug("fetchURL :" + fetchURL);
        try {
            Assert.assertTrue("Importing Schema from URL failed", resourceAdminServiceStub.importResource("/", resourceName, MEDIA_TYPE_SCHEMA, description, fetchURL, null));
            log.info("Schema Imported");
        } catch (RemoteException e) {
            log.error("Importing Schema from URL failed due to RemoteException : " + e.getMessage());
            Assert.fail("Importing Schema from URL failed due to RemoteException : " + e.getMessage());
        } catch (ExceptionException e) {
            log.error("Importing Schema from URL failed due to ExceptionException : " + e.getMessage());
            Assert.fail("Importing Schema from URL failed due to ExceptionException : " + e.getMessage());
        }

    }

    public void addPolicy(String sessionCookie, String description, DataHandler dh) {
        String fileName;
        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        fileName = dh.getName().substring(dh.getName().lastIndexOf('/') + 1);
        log.debug("file name :" + fileName);
        try {
            Assert.assertTrue("Adding Policy failed", resourceAdminServiceStub.addResource("/" + fileName, MEDIA_TYPE_POLICY, description, dh, null));
            log.info("Policy Added");
        } catch (RemoteException e) {
            log.error("Policy adding failed due to RemoteException : " + e.getMessage());
            Assert.fail("Policy adding failed due to RemoteException : " + e.getMessage());
        } catch (ExceptionException e) {
            log.error("Policy adding failed due to ExceptionException : " + e.getMessage());
            Assert.fail("Policy adding failed due to ExceptionException : " + e.getMessage());
        }

    }

    public void addPolicy(String sessionCookie, String resourceName, String description, String fetchURL) {

        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        log.debug("fetchURL :" + fetchURL);
        try {
            Assert.assertTrue("Importing Policy failed", resourceAdminServiceStub.importResource("/", resourceName, MEDIA_TYPE_POLICY, description, fetchURL, null));
            log.info("Policy imported");
        } catch (RemoteException e) {
            log.error("Importing Policy from URL failed due to ExceptionException : " + e.getMessage());
            Assert.fail("Importing Policy from URL failed due to ExceptionException : " + e.getMessage());
        } catch (ExceptionException e) {
            log.error("Importing Policy from URL failed due to ExceptionException : " + e.getMessage());
            Assert.fail("Importing Policy from URL failed due to ExceptionException : " + e.getMessage());
        }


    }

    public void uploadArtifact(String sessionCookie, String description, DataHandler dh) {
        String fileName;
        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        fileName = dh.getName().substring(dh.getName().lastIndexOf('/') + 1);
        log.debug("file name :" + fileName);
        try {
            Assert.assertTrue("uploading vnd.wso2.governance-archive failed", resourceAdminServiceStub.addResource("/" + fileName, MEDIA_TYPE_GOVERNANCE_ARCHIVE, description, dh, null));
            log.info("Artifact Uploaded");
        } catch (RemoteException e) {
            log.error("Uploading artifact vnd.wso2.governance-archive failed due to RemoteException : " + e.getMessage());
            Assert.fail("Uploading artifact vnd.wso2.governance-archive failed due to RemoteException : " + e.getMessage());
        } catch (ExceptionException e) {
            log.error("Uploading artifact vnd.wso2.governance-archive failed due to ExceptionException : " + e.getMessage());
            Assert.fail("Uploading artifact vnd.wso2.governance-archive failed due to ExceptionException : " + e.getMessage());
        }

    }

    public void addCollection(String sessionCookie, String parentPath, String collectionName, String mediaType, String description) {
        new AuthenticateStub().authenticateStub(sessionCookie, resourceAdminServiceStub);
        log.debug("collectionName :" + collectionName);
        log.debug("parentPath :" + parentPath);
        try {
            //TODO - write a proper assertion to verify output
            resourceAdminServiceStub.addCollection(parentPath, collectionName, mediaType, description);
            log.info("Collection Added");
        } catch (RemoteException e) {
            log.error("Collection adding failed due to RemoteException : " + e.getMessage());
            Assert.fail("Collection adding failed due to RemoteException : " + e.getMessage());
        } catch (ExceptionException e) {
            log.error("Collection adding failed due to ExceptionException : " + e.getMessage());
            Assert.fail("Collection adding failed due to ExceptionException : " + e.getMessage());
        }

    }

}
