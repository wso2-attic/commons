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
package org.wso2.carbon.registry.resource.test.commands;

import junit.framework.TestCase;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.registry.resource.ui.ExceptionException;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;
import org.wso2.carbon.registry.resource.ui.beans.xsd.CollectionContentBean;
import org.wso2.carbon.registry.resource.ui.beans.xsd.ContentBean;
import org.wso2.carbon.registry.resource.ui.beans.xsd.ContentDownloadBean;
import org.wso2.carbon.registry.resource.ui.beans.xsd.MetadataBean;
import org.wso2.carbon.registry.resource.ui.beans.xsd.PermissionBean;
import org.wso2.carbon.registry.resource.ui.beans.xsd.ResourceTreeEntryBean;
import org.wso2.carbon.registry.resource.ui.beans.xsd.VersionsBean;
import org.wso2.carbon.registry.resource.ui.common.xsd.ResourceData;

import javax.activation.DataHandler;
import java.net.URL;
import java.rmi.RemoteException;

public class ResourceAdminCommand extends TestCase {

    private static final Log log = LogFactory.getLog(ResourceAdminCommand.class);
    ResourceAdminServiceStub resourceAdminServiceStub;

    public ResourceAdminCommand(ResourceAdminServiceStub resourceAdminServiceStub) {
        this.resourceAdminServiceStub = resourceAdminServiceStub;
        log.debug("ResourceAdminStub added");
    }

    public void addResourceSuccessCase(String path, String mediaType, String description,
                                       String resourcePath, String symlinkLocation) {
        try {
            resourceAdminServiceStub.addResource(path, mediaType, description, new DataHandler(new URL(resourcePath)), symlinkLocation);
        } catch (ExceptionException e) {
            e.printStackTrace();
            log.error("Add registry resource failed : " + e.getMessage());
            Assert.fail("Add registry resource failed");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception");
        }
    }

    public void addResourceFailureCase(String path, String mediaType, String description,
                                       DataHandler content, String symlinkLocation)
            throws RemoteException, ExceptionException {
        try {
            resourceAdminServiceStub.addResource(path, mediaType, description, content, symlinkLocation);
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public String addCollectionSuccessCase(String parentPath, String collectionName,
                                           String mediaType,
                                           String description)
            throws Exception {
        String result = null;
        try {
            result = resourceAdminServiceStub.addCollection(parentPath, collectionName, mediaType, description);
        } catch (ExceptionException e) {
            log.error("Add registry collection failed : " + e.getMessage());
            Assert.fail("Add registry collection failed");
            e.printStackTrace();
        }
        return result;
    }

    public void addCollectionFailureCase(String parentPath, String collectionName, String mediaType,
                                         String description)
            throws ExceptionException, RemoteException {
        try {
            resourceAdminServiceStub.addCollection(parentPath, collectionName, mediaType, description);
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void deleteResourceSuccessCase(String collectionPath)
            throws ExceptionException, RemoteException {
        try {
            resourceAdminServiceStub.delete(collectionPath);
        } catch (ExceptionException e) {
            log.error("Collection delete failed : " + e.getMessage());
            Assert.fail("Collection delete failed");
            e.printStackTrace();
        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception");
            e.printStackTrace();
        }
    }

    public void deleteResourceFailureCase(String collectionPath)
            throws ExceptionException, RemoteException {
        try {
            resourceAdminServiceStub.delete(collectionPath);
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void addRemoteLinkSuccessCase(String parentPath, String name, String instance,
                                         String targetPath) {
        try {
            resourceAdminServiceStub.addRemoteLink(parentPath, name, instance, targetPath);
        } catch (ExceptionException e) {
            log.error("Add remote link failed : " + e.getMessage());
            Assert.fail("Add remote link failed");
            e.printStackTrace();
        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception");
            e.printStackTrace();
        }
    }

    public void addRemoteLinkFailureCase(String parentPath, String name, String instance,
                                         String targetPath) {
        try {
            resourceAdminServiceStub.addRemoteLink(parentPath, name, instance, targetPath);
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void addRolePermissionSuccessCase(String pathToAuthorize, String roleToAuthorize,
                                             String actionToAuthorize,
                                             String permissoinType) {
        try {
            resourceAdminServiceStub.addRolePermission(pathToAuthorize, roleToAuthorize, actionToAuthorize, permissoinType);
        } catch (Exception e) {
            log.error("Unable to add role permission : " + e.getMessage());
            Assert.fail("Unable to add role permission");
        }
    }

    public void addRolePermissionFailureCase(String pathToAuthorize, String roleToAuthorize,
                                             String actionToAuthorize,
                                             String permissoinType) {
        try {
            resourceAdminServiceStub.addRolePermission(pathToAuthorize, roleToAuthorize, actionToAuthorize, permissoinType);
            Assert.fail("Role permission added without authenticating admin service");
            log.error("Role permission added without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void addSymbolicLinkSuccessCase(String parentPath, String name,
                                           String targetPath) {
        try {
            resourceAdminServiceStub.addSymbolicLink(parentPath, name, targetPath);
        } catch (Exception e) {
            log.error("Unable to add symbolic link : " + e.getMessage());
            Assert.fail("Unable to add symbolic link");
        }
    }

    public void addSymbolicLinkFailureCase(String parentPath, String name,
                                           String targetPath) {
        try {
            resourceAdminServiceStub.addSymbolicLink(parentPath, name, targetPath);
            Assert.fail("Symbolic link added without authenticating admin service");
            log.error("Symbolic link added without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void addTextResourceSuccessCase(String parentPath, String fileName,
                                           String mediaType, String description, String content)
            throws Exception {
        try {
            resourceAdminServiceStub.addTextResource(parentPath, fileName, mediaType, description, content);
        } catch (ExceptionException e) {
            log.error("Unable to add text resource : " + e.getMessage());
            Assert.fail("Unable to add text resource");
        }
    }

    public void addTextResourceFailureCase(String parentPath, String fileName,
                                           String mediaType, String description, String content) {
        try {
            resourceAdminServiceStub.addTextResource(parentPath, fileName, mediaType, description, content);
            Assert.fail("Text resource added without authenticating admin service");
            log.error("Text resource added without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void addUserPermissionSuccessCase(String pathToAuthorize, String userToAutorize,
                                             String sectionToAuthorize, String permissionType) {
        try {
            resourceAdminServiceStub.addUserPermission(pathToAuthorize, userToAutorize, sectionToAuthorize, permissionType);
        } catch (Exception e) {
            log.error("Unable to add user permission : " + e.getMessage());
            Assert.fail("Unable to add user permission");
        }
    }

    public void addUserPermissionFailureCase(String pathToAuthorize, String userToAutorize,
                                             String sectionToAuthorize, String permissionType) {
        try {
            resourceAdminServiceStub.addUserPermission(pathToAuthorize, userToAutorize, sectionToAuthorize, permissionType);
            Assert.fail("User permission added without authenticating admin service");
            log.error("User permissoin added without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void changeUserPermissionSuccessCase(String resourcePath, String permissionInput) {
        try {
            resourceAdminServiceStub.changeUserPermissions(resourcePath, permissionInput);
        } catch (Exception e) {
            log.error("Unable to change user permission : " + e.getMessage());
            Assert.fail("Unable to change user permission");
        }
    }

    public void changeUserPermissionFailureCase(String resourcePath, String permissionInput) {
        try {
            resourceAdminServiceStub.changeUserPermissions(resourcePath, permissionInput);
            Assert.fail("User permission changed without authenticating admin service");
            log.error("User permissoin changed without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void changeRolePermissionSuccessCase(String resourcePath, String permissionInput) {
        try {
            resourceAdminServiceStub.changeUserPermissions(resourcePath, permissionInput);
        } catch (Exception e) {
            log.error("Unable to change role permission : " + e.getMessage());
            Assert.fail("Unable to change role permission");
        }
    }

    public void changeRolePermissionFailureCase(String resourcePath, String permissionInput) {
        try {
            resourceAdminServiceStub.changeRolePermissions(resourcePath, permissionInput);
            Assert.fail("Role permission changed without authenticating admin service");
            log.error("Role permissoin changed without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void copyResourceSuccessCase(String parentPath, String resourcePath,
                                        String destinationPath, String resourceName) {
        try {
            resourceAdminServiceStub.copyResource(parentPath, resourcePath, destinationPath, resourceName);
        } catch (Exception e) {
            log.error("Unable to copy resource : " + e.getMessage());
            Assert.fail("Unable to copy resource");
        }
    }

    public void copyResourceFailureCase(String parentPath, String resourcePath,
                                        String destinationPath, String resourceName) {
        try {
            resourceAdminServiceStub.copyResource(parentPath, resourcePath, destinationPath, resourceName);
            Assert.fail("Resource copied without authenticating admin service");
            log.error("Resource copied without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void createVersionSuccessCase(String resourcePath) {
        try {
            resourceAdminServiceStub.createVersion(resourcePath);
        } catch (Exception e) {
            log.error("Unable to create version : " + e.getMessage());
            Assert.fail("Unable to create version");
        }
    }

    public void createVersionFailureCase(String resourcePath) {
        try {
            resourceAdminServiceStub.createVersion(resourcePath);
            Assert.fail("Version created without authenticating admin service");
            log.error("Version created without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public CollectionContentBean getCollectionContentSuccessCase(String path) {
        CollectionContentBean collectionContentBean = new CollectionContentBean();
        try {
            collectionContentBean = resourceAdminServiceStub.getCollectionContent(path);
        } catch (Exception e) {
            log.error("Unable to get collection content : " + e.getMessage());
            Assert.fail("Unable to get collection content");
        }
        return collectionContentBean;
    }

    public void getCollectionContentFailureCase(String path) {
        try {
            resourceAdminServiceStub.getCollectionContent(path);
            Assert.fail("Getting collection content without authenticating admin service");
            log.error("Getting collection content without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public String getCollectionMediaTypeDefinitionSuccessCase() {
        String mediaTypeDefCollection = null;
        try {
            mediaTypeDefCollection = resourceAdminServiceStub.getCollectionMediatypeDefinitions();
        } catch (Exception e) {
            log.error("Unable to get collection media type definition : " + e.getMessage());
            Assert.fail("Unable to get collection media type definition");
        }
        return mediaTypeDefCollection;
    }

    public void getCollectionMediaTypeDefinitionFailureCase() {
        try {
            resourceAdminServiceStub.getCollectionMediatypeDefinitions();
            Assert.fail("Getting collection media type definition without authenticating admin service");
            log.error("Getting collection media type definition without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public ContentBean getContentBeanSuccessCase(String path) {
        ContentBean contentBean = new ContentBean();
        try {
            contentBean = resourceAdminServiceStub.getContentBean(path);
        } catch (Exception e) {
            log.error("Unable to get content bean : " + e.getMessage());
            Assert.fail("Unable to get content bean");
        }
        return contentBean;
    }

    public void getContentBeanFailureCase(String path) {
        try {
            resourceAdminServiceStub.getContentBean(path);
            Assert.fail("Getting content bean without authenticating admin service");
            log.error("Getting content bean without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public ContentDownloadBean getContentDownloadBeanSuccessCase(String path) {
        ContentDownloadBean contentDownloadBean = new ContentDownloadBean();
        try {
            contentDownloadBean = resourceAdminServiceStub.getContentDownloadBean(path);
        } catch (Exception e) {
            log.error("Unable to get content download bean : " + e.getMessage());
            Assert.fail("Unable to get content download bean");
        }
        return contentDownloadBean;
    }

    public void getContentDownloadBeanFailureCase(String path) {
        try {
            resourceAdminServiceStub.getContentDownloadBean(path);
            Assert.fail("Getting content download bean without authenticating admin service");
            log.error("Getting content download bean without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public String getCustomUIMediatypeDefinitionsSuccessCase() {
        String customUIMediatypeDefinitions = null;
        try {
            customUIMediatypeDefinitions = resourceAdminServiceStub.getCustomUIMediatypeDefinitions();
        } catch (Exception e) {
            log.error("Unable to get customUIMediatypeDefinitions : " + e.getMessage());
            Assert.fail("Unable to get customUIMediatypeDefinitions");
        }
        return customUIMediatypeDefinitions;
    }

    public void getCustomUIMediatypeDefinitionsFailureCase() {
        try {
            resourceAdminServiceStub.getCustomUIMediatypeDefinitions();
            Assert.fail("Getting CustomUIMediatypeDefinitions without authenticating admin service");
            log.error("Getting CustomUIMediatypeDefinitions without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }


    public String getMediatypeDefinitionsSuccessCase() {
        String mediatypeDefinition = null;
        try {
            mediatypeDefinition = resourceAdminServiceStub.getMediatypeDefinitions();
        } catch (Exception e) {
            log.error("Unable to get mediatypeDefinition : " + e.getMessage());
            Assert.fail("Unable to get mediatypeDefinition");
        }
        return mediatypeDefinition;
    }

    public void getMediatypeDefinitionsFailureCase() {
        try {
            resourceAdminServiceStub.getMediatypeDefinitions();
            Assert.fail("Getting mediatypeDefinition without authenticating admin service");
            log.error("Getting mediatypeDefinition without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public MetadataBean getMetadataSuccessCase(String path) {
        MetadataBean MetadataBean = new MetadataBean();
        try {
            MetadataBean = resourceAdminServiceStub.getMetadata(path);
        } catch (Exception e) {
            log.error("Unable to get metadata : " + e.getMessage());
            Assert.fail("Unable to get metadata");
        }
        return MetadataBean;
    }

    public void getMetadataFailureCase(String path) {
        try {
            resourceAdminServiceStub.getMetadata(path);
            Assert.fail("Getting metadata without authenticating admin service");
            log.error("Getting metadata without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public PermissionBean getPermissionsSuccessCase(String path) {
        PermissionBean permissionBean = new PermissionBean();
        try {
            permissionBean = resourceAdminServiceStub.getPermissions(path);
        } catch (Exception e) {
            log.error("Unable to get permissions : " + e.getMessage());
            Assert.fail("Unable to get permissions");
        }
        return permissionBean;
    }

    public void getPermissionsFailureCase(String path) {
        try {
            resourceAdminServiceStub.getPermissions(path);
            Assert.fail("Getting permissions without authenticating admin service");
            log.error("Getting permissions without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public String getPropertySuccessCase(String resourcePath, String key) {
        String property = null;
        try {
            property = resourceAdminServiceStub.getProperty(resourcePath, key);
        } catch (Exception e) {
            log.error("Unable to get property : " + e.getMessage());
            Assert.fail("Unable to get property");
        }
        return property;
    }

    public void getPropertyFailureCase(String resourcePath, String key) {
        try {
            resourceAdminServiceStub.getProperty(resourcePath, key);
            Assert.fail("Getting property without authenticating admin service");
            log.error("Getting property without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public ResourceData[] getResourceDataSuccessCase(String[] path) {
        ResourceData[] resourceData = null;
        try {
            resourceData = resourceAdminServiceStub.getResourceData(path);
        } catch (Exception e) {
            log.error("Unable to get resourcedata : " + e.getMessage());
            Assert.fail("Unable to get resourcedata");
        }
        return resourceData;
    }

    public void getResourceDataFailureCase(String[] path) {
        try {
            resourceAdminServiceStub.getResourceData(path);
            Assert.fail("Getting resource data without authenticating admin service");
            log.error("Getting resource data without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public ResourceTreeEntryBean getResourceTreeEntrySuccessCase(String resourcePath) {
        ResourceTreeEntryBean resourceTreeEntryBean = new ResourceTreeEntryBean();
        try {
            resourceTreeEntryBean = resourceAdminServiceStub.getResourceTreeEntry(resourcePath);
        } catch (Exception e) {
            log.error("Unable to get resource tree entry bean : " + e.getMessage());
            Assert.fail("Unable to get resource tree entry bean");
        }
        return resourceTreeEntryBean;
    }

    public void getResourceTreeEntryFailureCase(String resourcePath) {
        try {
            resourceAdminServiceStub.getResourceTreeEntry(resourcePath);
            Assert.fail("Getting resource tree entry bean without authenticating admin service");
            log.error("Getting resource tree entry bean without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public String getSessionResourcePathSuccessCase() {
        String SessionResourcePath = null;
        try {
            SessionResourcePath = resourceAdminServiceStub.getSessionResourcePath();
        } catch (Exception e) {
            log.error("Unable to get session resource path : " + e.getMessage());
            Assert.fail("Unable to get session resource path");
        }
        return SessionResourcePath;
    }

    public void getSessionResourcePathFailureCase(String resourcePath) {
        try {
            resourceAdminServiceStub.getSessionResourcePath();
            Assert.fail("Getting session resource path without authenticating admin service");
            log.error("Getting session resource path without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }

    }

    public String getTextContentSuccessCase(String path) {
        String textContent = null;
        try {
            textContent = resourceAdminServiceStub.getTextContent(path);
        } catch (Exception e) {
            log.error("Unable to get text content : " + e.getMessage());
            Assert.fail("Unable to get text content");
        }
        return textContent;
    }

    public void getTextContentFailureCase(String path) {
        try {
            resourceAdminServiceStub.getTextContent(path);
            Assert.fail("Getting text content without authenticating admin service");
            log.error("Getting text content without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }

    }

    public VersionsBean getVersionsBeanSuccessCase(String path) {
        VersionsBean versionsBean = new VersionsBean();
        try {
            versionsBean = resourceAdminServiceStub.getVersionsBean(path);
        } catch (Exception e) {
            log.error("Unable to get version bean : " + e.getMessage());
            Assert.fail("Unable to get version bean");
        }
        return versionsBean;
    }

    public void getVersionsBeanFailureCase(String path) {
        try {
            resourceAdminServiceStub.getVersionsBean(path);
            Assert.fail("Getting version bean without authenticating admin service");
            log.error("Getting version bean without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }

    }

    public void importResourceSuccessCase(String parentPath, String resourceName, String mediaType,
                                          String desc, String fetchURL, String symlinkLocation) {
        try {
            resourceAdminServiceStub.importResource(parentPath, resourceName, mediaType, desc, fetchURL, symlinkLocation);
        } catch (Exception e) {
            log.error("Unable to import resource : " + e.getMessage());
            Assert.fail("Unable to import resource");
        }
    }

    public void importResourceFailureCase(String parentPath, String resourceName, String mediaType,
                                          String desc, String fetchURL, String symlinkLocation) {
        try {
            resourceAdminServiceStub.importResource(parentPath, resourceName, mediaType, desc, fetchURL, symlinkLocation);
            Assert.fail("Importing resource without authenticating admin service");
            log.error("Importing resource without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }

    }

    public void moveResourceSuccessCase(String parentPath, String oldResourcePath,
                                        String destinationPath,
                                        String resourceName) {
        try {
            resourceAdminServiceStub.moveResource(parentPath, oldResourcePath, destinationPath, resourceName);
        } catch (Exception e) {
            log.error("Unable to move resource : " + e.getMessage());
            Assert.fail("Unable to move resource");
        }
    }

    public void moveResourceFailureCase(String parentPath, String oldResourcePath,
                                        String destinationPath,
                                        String resourceName) {
        try {
            resourceAdminServiceStub.moveResource(parentPath, oldResourcePath, destinationPath, resourceName);
            Assert.fail("Resource moving without authenticating admin service");
            log.error("Resource moving without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }

    }


    public void renameResourceSuccessCase(String parentPath, String oldResourcePath,
                                          String newResourceNmae) {
        try {
            resourceAdminServiceStub.renameResource(parentPath, oldResourcePath, newResourceNmae);
        } catch (Exception e) {
            log.error("Unable to rename resource : " + e.getMessage());
            Assert.fail("Unable to rename resource");
        }
    }

    public void renameResourceFailureCase(String parentPath, String oldResourcePath,
                                          String newResourceName) {
        try {
            resourceAdminServiceStub.renameResource(parentPath, oldResourcePath, newResourceName);
            Assert.fail("Resource renaming without authenticating admin service");
            log.error("Resource renaming without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }

    }

    public void restoreVersionSuccessCase(String versionPath) {
        try {
            resourceAdminServiceStub.restoreVersion(versionPath);
        } catch (Exception e) {
            log.error("Unable to restore version : " + e.getMessage());
            Assert.fail("Unable to restore version");
        }
    }

    public void restoreVersionFailureCase(String versionPath) {
        try {
            resourceAdminServiceStub.restoreVersion(versionPath);
            Assert.fail("Version restoring without authenticating admin service");
            log.error("Version restoring without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }

    }

    public void setDescriptionSuccessCase(String path, String description) {
        try {
            resourceAdminServiceStub.setDescription(path, description);
        } catch (Exception e) {
            log.error("Unable to set description : " + e.getMessage());
            Assert.fail("Unable to set description");
        }
    }

    public void setDescriptionFailureCase(String path, String description) {
        try {
            resourceAdminServiceStub.setDescription(path, description);
            Assert.fail("Setting description without authenticating admin service");
            log.error("Setting description without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void setSessionResourcePathSuccessCase(String resourcePath) {
        try {
            resourceAdminServiceStub.setSessionResourcePath(resourcePath);
        } catch (Exception e) {
            log.error("Unable to set session resource path : " + e.getMessage());
            Assert.fail("Unable to set session resource path");
        }
    }

    public void setSessionResourcePathFailureCase(String resourcePath) {
        try {
            resourceAdminServiceStub.setSessionResourcePath(resourcePath);
            Assert.fail("Setting session resource path without authenticating admin service");
            log.error("Setting session resource path without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void updateTextContentSuccessCase(String resourcePath, String contentText) {
        try {
            resourceAdminServiceStub.updateTextContent(resourcePath, contentText);
        } catch (Exception e) {
            log.error("Unable to update text content : " + e.getMessage());
            Assert.fail("Unable to update text content");
        }
    }

    public void updateTextContentFailureCase(String resourcePath, String contentText) {
        try {
            resourceAdminServiceStub.updateTextContent(resourcePath, contentText);
            Assert.fail("Updating text content without authenticating admin service");
            log.error("Updating text content without authenticating admin service");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public boolean isResourceExist(String sessionCookie, String resourcePath, String resourceName) {
        boolean isResourceExist = false;
        ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
        CollectionContentBean collectionContentBean = new CollectionContentBean();
        collectionContentBean = new ResourceAdminCommand(resourceAdminServiceStub).getCollectionContentSuccessCase(resourcePath);
        if (collectionContentBean.getChildCount() > 0) {
            String[] childPath = collectionContentBean.getChildPaths();
            for (int i = 0; i <= childPath.length - 1; i++) {
                if (childPath[i].equalsIgnoreCase(resourcePath + "/" + resourceName)) {
                    ;
                }
                isResourceExist = true;
            }
        }
        return isResourceExist;
    }

}
