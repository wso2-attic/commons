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

package org.wso2.stratos.automation.test.greg.app_remote_registry_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregRemoteRegistryProvider;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;

import java.util.List;


public class TestAssociation extends TestTemplate {
    private static final Log log = LogFactory.getLog(ResourceHandling.class);
    public RemoteRegistry registry;

    @Override
    public void init() {
        testClassName = CommentTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new GregRemoteRegistryProvider().getRegistry(tenantId);
        removeResource();

    }

    @Override
    public void runSuccessCase() {
        log.info("Running Registry API - TestAssociation Test ..........................");
        AddAssociationToResourceTest();
        AddAssociationToCollectionTest();
        AddAssociationToRootTest();
//        GetResourceAssociationTest();    ---do not execute
        GetCollectionAssociationTest();
        GetRootAssociationTest();
        RemoveResourceAssociationTest();
        RemoveCollectionAssociationTest();
        RemoveRootAssociationTest();
        log.info("Completed Running Registry API - TestAssociation Test ..........................");
    }


    @Override
    public void cleanup() {
    }

    private void removeResource() {
        deleteResources("/testk12");
        deleteResources("/assocol1");
        deleteResources("/getcol1");
        deleteResources("/testk1234");
        deleteResources("/assoColremove1");

    }

    public void deleteResources(String resourceName) {
        try {
            if (registry.resourceExists(resourceName)) {
                registry.delete(resourceName);
            }
        } catch (RegistryException e) {
            log.error("deleteResources RegistryException thrown:" + e.getMessage());
            Assert.fail("deleteResources RegistryException thrown:" + e.getMessage());
        }

    }

    public void AddAssociationToResourceTest() {
        Resource r2;
        try {
            r2 = registry.newResource();
            String path = "/testk12/testa/testbsp/test.txt";
            r2.setContent(new String("this is the content").getBytes());
            r2.setDescription("this is test desc");
            r2.setMediaType("plain/text");
            r2.setProperty("test2", "value2");

            registry.put(path, r2);
            registry.addAssociation(path, "/vtr2121/test", "testasstype1");
            registry.addAssociation(path, "/vtr2122/test", "testasstype2");
            registry.addAssociation(path, "/vtr2123/test", "testasstype3");
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2121/test"));
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2122/test"));
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2123/test"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype1"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype2"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype3"));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            deleteResources("/testk12");
            log.info("AddAssociationToResourceTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -AddAssociationToResourceTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddAssociationToResourceTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void AddAssociationToCollectionTest() {
        Resource r2;
        try {
            r2 = registry.newCollection();
            String path = "/assocol1/assocol2/assoclo3";
            r2.setDescription("this is test desc");
            r2.setMediaType("plain/text");
            r2.setProperty("test2", "value2");

            registry.put(path, r2);
            registry.addAssociation(path, "/vtr2121/test", "testasstype1");
            registry.addAssociation(path, "/vtr2122/test", "testasstype2");
            registry.addAssociation(path, "/vtr2123/test", "testasstype3");
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2121/test"));
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2122/test"));
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2123/test"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype1"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype2"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype3"));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            deleteResources("/assocol1");
            log.info("AddAssociationToCollectionTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -AddAssociationToCollectionTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddAssociationToCollectionTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void AddAssociationToRootTest() {
        try {
            String path = "/";
            registry.addAssociation(path, "/vtr2121/test", "testasstype1");
            registry.addAssociation(path, "/vtr2122/test", "testasstype2");
            registry.addAssociation(path, "/vtr2123/test", "testasstype3");
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2121/test"));
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2122/test"));
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2123/test"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype1"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype2"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype3"));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            registry.removeAssociation(path, "/vtr2121/test", "testasstype1");
            registry.removeAssociation(path, "/vtr2122/test", "testasstype2");
            registry.removeAssociation(path, "/vtr2123/test", "testasstype3");
            log.info("AddAssociationToRootTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -AddAssociationToRootTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddAssociationToRootTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void GetResourceAssociationTest() {
        Resource r2;
        try {
            r2 = registry.newResource();
            String path = "/testk1234/testa/testbsp/test.txt";
            r2.setContent(new String("this is the content").getBytes());
            r2.setDescription("this is test desc");
            r2.setMediaType("plain/text");
            r2.setProperty("test2", "value2");

            registry.addAssociation(path, "/vtr2121/test", "testasstype1");
            registry.addAssociation(path, "/vtr2122/test", "testasstype2");
            registry.addAssociation(path, "/vtr2123/test", "testasstype3");
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype1", "/vtr2121/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype2", "/vtr2122/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype3", "/vtr2123/test"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype1"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype2"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype3"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype1"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype2"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype3"));
            deleteResources("/testk1234");
            log.info("GetResourceAssociationTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -GetResourceAssociationTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -GetResourceAssociationTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void GetCollectionAssociationTest() {
        Resource r2;
        try {
            r2 = registry.newCollection();
            String path = "/getcol1/getcol2/getcol3";
            r2.setDescription("this is test desc");
            r2.setProperty("test2", "value2");

            registry.put(path, r2);
            registry.addAssociation(path, "/vtr2121/test", "testasstype1");
            registry.addAssociation(path, "/vtr2122/test", "testasstype2");
            registry.addAssociation(path, "/vtr2123/test", "testasstype3");
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype1", "/vtr2121/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype2", "/vtr2122/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype3", "/vtr2123/test"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype1"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype2"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype3"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype1"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype2"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype3"));
            deleteResources("/getcol1");
            log.info("GetCollectionAssociationTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -GetCollectionAssociationTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -GetCollectionAssociationTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void GetRootAssociationTest() {
        Resource r2;
        try {
            r2 = registry.newCollection();
            String path = "/";
            r2.setDescription("this is test desc");
            r2.setProperty("test2", "value2");

            registry.put(path, r2);
            registry.addAssociation(path, "/vtr21211/test", "testasstype1");
            registry.addAssociation(path, "/vtr21221/test", "testasstype2");
            registry.addAssociation(path, "/vtr21231/test", "testasstype3");
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype1", "/vtr21211/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype2", "/vtr21221/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype3", "/vtr21231/test"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype1"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype2"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype3"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype1"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype2"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype3"));
            registry.removeAssociation(path, "/vtr21211/test", "testasstype1");
            registry.removeAssociation(path, "/vtr21221/test", "testasstype2");
            registry.removeAssociation(path, "/vtr21231/test", "testasstype3");
            log.info("GetRootAssociationTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -GetRootAssociationTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -GetRootAssociationTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void RemoveResourceAssociationTest() {
        Resource r2;
        try {
            r2 = registry.newResource();
            String path = "/testk1234/testa/testbsp/test.txt";
            r2.setContent(new String("this is the content").getBytes());
            r2.setDescription("this is test desc");
            r2.setMediaType("plain/text");
            r2.setProperty("test2", "value2");

            registry.put(path, r2);
            registry.addAssociation(path, "/vtr2121/test", "testasstype1");
            registry.addAssociation(path, "/vtr2122/test", "testasstype2");
            registry.addAssociation(path, "/vtr2123/test", "testasstype3");
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype1", "/vtr2121/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype2", "/vtr2122/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype3", "/vtr2123/test"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype1"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype2"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype3"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype1"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype2"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype3"));
            registry.removeAssociation(path, "/vtr2121/test", "testasstype1");
            registry.removeAssociation(path, "/vtr2122/test", "testasstype2");
            registry.removeAssociation(path, "/vtr2123/test", "testasstype3");
            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype1", "/vtr2121/test"));
            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype2", "/vtr2122/test"));
            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype3", "/vtr2123/test"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype1"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype2"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype3"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype1"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype2"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype3"));
            deleteResources("/testk1234/");
            log.info("RemoveResourceAssociationTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -RemoveResourceAssociationTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -RemoveResourceAssociationTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void RemoveCollectionAssociationTest() {
        Resource r2;
        try {
            r2 = registry.newCollection();
            String path = "/assoColremove1/assoColremove2/assoColremove3";
            r2.setDescription("this is test desc");
            r2.setMediaType("plain/text");
            r2.setProperty("test2", "value2");

            registry.put(path, r2);
            registry.addAssociation(path, "/vtr2121/test", "testasstype1");
            registry.addAssociation(path, "/vtr2122/test", "testasstype2");
            registry.addAssociation(path, "/vtr2123/test", "testasstype3");

            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype1", "/vtr2121/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype2", "/vtr2122/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype3", "/vtr2123/test"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype1"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype2"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype3"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype1"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype2"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype3"));

            registry.removeAssociation(path, "/vtr2121/test", "testasstype1");
            registry.removeAssociation(path, "/vtr2122/test", "testasstype2");
            registry.removeAssociation(path, "/vtr2123/test", "testasstype3");

            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype1", "/vtr2121/test"));
            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype2", "/vtr2122/test"));
            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype3", "/vtr2123/test"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype1"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype2"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype3"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype1"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype2"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype3"));
            deleteResources("/assoColremove1");
            log.info("RemoveCollectionAssociationTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -RemoveCollectionAssociationTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -RemoveCollectionAssociationTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void RemoveRootAssociationTest() {
        Resource r2;
        try {
            r2 = registry.newCollection();
            String path = "/";
            r2.setDescription("this is test desc");
            r2.setMediaType("plain/text");
            r2.setProperty("test2", "value2");

            registry.put(path, r2);
            registry.addAssociation(path, "/vtr21212/test", "testasstype11");
            registry.addAssociation(path, "/vtr21222/test", "testasstype21");
            registry.addAssociation(path, "/vtr21232/test", "testasstype31");


            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype11", "/vtr21212/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype21", "/vtr21222/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype31", "/vtr21232/test"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype11"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype21"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype31"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype11"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype21"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype31"));

            registry.removeAssociation(path, "/vtr21212/test", "testasstype11");
            registry.removeAssociation(path, "/vtr21222/test", "testasstype21");
            registry.removeAssociation(path, "/vtr21232/test", "testasstype31");

            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype11", "/vtr21212/test"));
            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype21", "/vtr21222/test"));
            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype31", "/vtr21232/test"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype11"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype21"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype31"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype11"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype21"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype31"));
            log.info("RemoveRootAssociationTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -RemoveRootAssociationTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -RemoveRootAssociationTest RegistryException thrown :" + e.getMessage());
        }
    }


    public static boolean resourceExists(RemoteRegistry registry, String fileName)
            throws RegistryException {
        boolean value = registry.resourceExists(fileName);
        return value;
    }

    public boolean associationPathExists(String path, String assoPath)
            throws RegistryException {
        Association association[] = registry.getAllAssociations(path);
        boolean value = false;

        for (int i = 0; i < association.length; i++) {
            //System.out.println(association[i].getDestinationPath());
            if (assoPath.equals(association[i].getDestinationPath())) {
                value = true;
            }
        }


        return value;
    }

    public boolean associationTypeExists(String path, String assoType) throws RegistryException {
        Association association[] = registry.getAllAssociations(path);
        boolean value = false;

        for (int i = 0; i < association.length; i++) {
            association[i].getAssociationType();
            if (assoType.equals(association[i].getAssociationType())) {
                value = true;
            }
        }


        return value;
    }

    public boolean associationSourcepathExists(String path, String sourcePath)
            throws RegistryException {
        Association association[] = registry.getAllAssociations(path);
        boolean value = false;

        for (int i = 0; i < association.length; i++) {
            association[i].getAssociationType();
            if (sourcePath.equals(association[i].getSourcePath())) {
                value = true;
            }
        }

        return value;
    }

    public boolean getAssocitionbyType(String path, String type) throws RegistryException {

        Association[] asso;
        asso = registry.getAssociations(path, type);

        boolean assoFound = false;

        for (Association a2 : asso) {

            if (a2.getAssociationType().equals(type)) {
                assoFound = true;
                break;
            }
        }
        return assoFound;
    }

    public boolean getAssocitionbySourceByType(String path, String type) throws RegistryException {

        Association[] asso;
        asso = registry.getAssociations(path, type);

        boolean assoFound = false;

        for (Association a2 : asso) {

            if (a2.getSourcePath().equals(path)) {
                assoFound = true;
                break;
            }
        }
        return assoFound;
    }

    public boolean getAssocitionbyDestinationByType(String path, String type,
                                                    String destinationPath)
            throws RegistryException {

        Association[] asso;
        asso = registry.getAssociations(path, type);


        boolean assoFound = false;

        for (Association a2 : asso) {

            if (a2.getDestinationPath().equals(destinationPath)) {
                assoFound = true;
                break;
            }
        }
        return assoFound;
    }

    public boolean associationNotExists(String path) throws RegistryException {
        Association association[] = registry.getAllAssociations(path);
        boolean value = true;
        if (association.length > 0) {
            value = false;
        }
        return value;
    }

    public boolean getProperty(String path, String key, String value) throws RegistryException {
        Resource r3 = registry.newResource();
        try {
            r3 = registry.get(path);
        }
        catch (RegistryException e) {
            fail((new StringBuilder()).append("Couldn't get file from the path :").append(path).toString());
        }
        List propertyValues = r3.getPropertyValues(key);
        Object valueName[] = propertyValues.toArray();
        boolean propertystatus = containsString(valueName, value);
        return propertystatus;
    }

    private boolean containsString(Object[] array, String value) {
        boolean found = false;
        for (Object anArray : array) {
            String s = anArray.toString();
            if (s.startsWith(value)) {
                found = true;
                break;
            }
        }
        return found;
    }

}
