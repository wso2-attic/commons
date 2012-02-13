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
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregRemoteRegistryProvider;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;

import java.util.List;

public class TestResources extends TestTemplate {
    private static final Log log = LogFactory.getLog(TestResources.class);
    public RemoteRegistry registry;
    String username;

    @Override
    public void init() {
        testClassName = TestResources.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new GregRemoteRegistryProvider().getRegistry(tenantId);
        //Tenant Details
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId));
        username = tenantDetails.getTenantName();
        removeResource();

    }

    @Override
    public void runSuccessCase() {
        log.info("Running Registry API - TestResources Test ..........................");
        HierachicalResourceTest();
        UpdateResouceContentTest();
        AddAnotherResourceTest();
        SetResourceDetailsTest();
        SetCollectionDetailsTest();
        CollectionDetailsTest();
        DeleteCollectionTest();
        DeleteResourceTest();
        AddSpacesinResNameTest();
//        AddResourceFromURLTest();
        RenameResourceTest();
        DeleteandUpdateResourceTest();
        ResourcemultiplePropertiesTest();
        CollectionmultiplePropertiesTest();
        log.info("Completed Running Registry API - TestResources Test ..........................");

    }


    @Override
    public void cleanup() {
    }

    private void removeResource() {
        deleteResources("/d1");
        deleteResources("/c11");
        deleteResources("/c1");
        deleteResources("/c20");
        deleteResources("/c11");
        deleteResources("/d30");
        deleteResources("/d33");
        deleteResources("/d40");
        deleteResources("/m11");
        deleteResources("/m15");
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

    public void HierachicalResourceTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            String content = "this is my content1";
            r1.setContent(content.getBytes());
            r1.setDescription("This is r1 file description");

            String path = "/d1/d2/d3/r1";
            try {
                registry.put(path, r1);
            }
            catch (RegistryException e) {
                fail("Couldn't put content to path /d1/d2/d3/r1");
            }

            Resource r1_actual = registry.newResource();
            try {
                r1_actual = registry.get("/d1/d2/d3/r1");
            }
            catch (RegistryException e) {
                fail("Couldn't get content from path /d1/d2/d3/r1");
            }

            assertEquals("Content is not equal.", new String((byte[]) r1.getContent()),
                    new String((byte[]) r1_actual.getContent()));
            assertEquals("LastUpdatedUser is not Equal", username, r1_actual.getLastUpdaterUserName());
            assertEquals("Can not get Resource path", path, r1_actual.getPath());
            assertEquals("Can not get Resource parent path", "/d1/d2/d3", r1_actual.getParentPath());
            assertEquals("Resource description is not equal", r1.getDescription(),
                    r1_actual.getDescription());
            assertEquals("Resource description is not equal", username, r1_actual.getAuthorUserName());
            deleteResources("/d1");
            log.info("HierachicalResourceTest- Passed");
        } catch (RegistryException e) {
            log.error("Registry API -HierachicalResourceTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -HierachicalResourceTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void UpdateResouceContentTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            String content = "this is my content1";
            r1.setContent(content.getBytes());
            r1.setDescription("This is r1 file description");
            r1.setProperty("key1", "value1");
            r1.setProperty("key2", "value2");

            String path = "/d1/d2/d3/d4/r1";
            try {
                registry.put(path, r1);
            }
            catch (RegistryException e) {
                fail("Couldn't put content to path /d1/d2/d3/d4/r1");
            }

            Resource r1_actual = registry.get("/d1/d2/d3/d4/r1");

            assertEquals("Content is not equal.", new String((byte[]) r1.getContent()),
                    new String((byte[]) r1_actual.getContent()));
            assertEquals("LastUpdatedUser is not Equal", username, r1_actual.getLastUpdaterUserName());
            assertEquals("Can not get Resource path", "/d1/d2/d3/d4/r1", r1_actual.getPath());
            assertEquals("Can not get Resource parent path", "/d1/d2/d3/d4", r1_actual.getParentPath());
            assertEquals("Resource description is not equal", r1.getDescription(),
                    r1_actual.getDescription());
            assertEquals("Author is not equal", username, r1_actual.getAuthorUserName());
            assertEquals("Resource properties are equal", r1.getProperty("key1"),
                    r1_actual.getProperty("key1"));
            assertEquals("Resource properties are equal", r1.getProperty("key2"),
                    r1_actual.getProperty("key2"));
            assertEquals("Resource properties are equal", r1.getProperty("key3_update"),
                    r1_actual.getProperty("key3_update"));

            String contentUpdated = "this is my content updated";
            r1.setContent(contentUpdated.getBytes());
            r1.setDescription("This is r1 file description updated");
            r1.setProperty("key1", "value1_update");
            r1.setProperty("key2", "value2_update");
            r1.setProperty("key3_update", "value3_update");

            registry.put(path, r1);
            Resource r2_actual = registry.get("/d1/d2/d3/d4/r1");

            assertEquals("Content is not equal.", new String((byte[]) r1.getContent()),
                    new String((byte[]) r2_actual.getContent()));
            assertEquals("LastUpdatedUser is not Equal", username, r2_actual.getLastUpdaterUserName());
            assertEquals("Can not get Resource path", "/d1/d2/d3/d4/r1", r2_actual.getPath());
            assertEquals("Can not get Resource parent path", "/d1/d2/d3/d4", r2_actual.getParentPath());
            assertEquals("Resource description is not equal", r1.getDescription(),
                    r2_actual.getDescription());
            assertEquals("Author is not equal", username, r2_actual.getAuthorUserName());
            assertEquals("Resource properties are equal", r1.getProperty("key1"),
                    r2_actual.getProperty("key1"));
            assertEquals("Resource properties are equal", r1.getProperty("key2"),
                    r2_actual.getProperty("key2"));
            assertEquals("Resource properties are equal", r1.getProperty("key3_update"),
                    r2_actual.getProperty("key3_update"));
            deleteResources("/d1");
            log.info("UpdateResouceContentTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -UpdateResouceContentTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -UpdateResouceContentTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void AddAnotherResourceTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            String content = "this is my content2";
            r1.setContent(content.getBytes());
            r1.setDescription("r2 file description");
            String path = "/d1/d2/r2";

            r1.setProperty("key1", "value1");
            r1.setProperty("key2", "value2");
            try {
                registry.put(path, r1);
            }
            catch (RegistryException e) {
                fail("Couldn't put content to path /d1/d2/r2");
            }

            Resource r1_actual = registry.newResource();
            try {
                r1_actual = registry.get("/d1/d2/r2");
            }
            catch (RegistryException e) {
                fail("Couldn't get content from path /d1/d2/r2");
            }

            assertEquals("Content is not equal.", new String((byte[]) r1.getContent()),
                    new String((byte[]) r1_actual.getContent()));
            assertEquals("LastUpdatedUser is not Equal", username, r1_actual.getLastUpdaterUserName());
            assertEquals("Can not get Resource path", "/d1/d2/r2", r1_actual.getPath());
            assertEquals("Can not get Resource parent path", "/d1/d2", r1_actual.getParentPath());
            assertEquals("Resource description is not equal", r1.getDescription(),
                    r1_actual.getDescription());
            assertEquals("Author is not equal", username, r1_actual.getAuthorUserName());
            assertEquals("Resource properties are equal", r1.getProperty("key1"),
                    r1_actual.getProperty("key1"));
            assertEquals("Resource properties are equal", r1.getProperty("key2"),
                    r1_actual.getProperty("key2"));
            deleteResources("/d1");
            log.info("AddAnotherResourceTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API AddAnotherResourceTestt RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddAnotherResourceTest RegistryException thrown :" + e.getMessage());
        }


    }

    public void SetResourceDetailsTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setDescription("R4 collection description");
            r1.setMediaType("jpg/image");
            r1.setContent(new byte[]{(byte) 0xDE, (byte) 0xDE, (byte) 0xDE, (byte) 0xDE});
            r1.setProperty("key1", "value5");
            r1.setProperty("key2", "value3");

            String path_collection = "/c11/c12/c13/c14/r4";
            try {
                registry.put(path_collection, r1);
            }
            catch (RegistryException e) {
                fail("Couldn't put content to path /c11/c12/c13/c14/r4");
            }

            Resource r1_actual = null;
            try {
                r1_actual = registry.get("/c11/c12/c13/c14/r4");
            }
            catch (RegistryException e) {
                fail("Couldn't get content from path /c11/c12/c13/c14/r4");
            }

            assertEquals("LastUpdatedUser is not Equal", username, r1_actual.getLastUpdaterUserName());
            assertEquals("Can not get Resource path", path_collection, r1_actual.getPath());
            assertEquals("Can not get Resource parent path", "/c11/c12/c13/c14",
                    r1_actual.getParentPath());
            assertEquals("Resource description is not equal", r1.getDescription(),
                    r1_actual.getDescription());
            assertEquals("Author is not equal", username, r1_actual.getAuthorUserName());
            assertEquals("Resource properties are not equal", r1.getProperty("key1"),
                    r1_actual.getProperty("key1"));
            assertEquals("Resource properties are not equal", r1.getProperty("key2"),
                    r1_actual.getProperty("key2"));
            assertEquals("Media Types are not equal", r1.getMediaType(), r1_actual.getMediaType());
            deleteResources("/c11");
            log.info("SetResourceDetailsTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -SetResourceDetailsTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -SetResourceDetailsTest RegistryException thrown :" + e.getMessage());
        }


    }

    public void CollectionDetailsTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            String content = "this is my content4";
            r1.setContent(content.getBytes());
            r1.setDescription("r3 file description");
            String path = "/c1/c2/c3/c4/r3";

            try {
                registry.put(path, r1);
            }
            catch (RegistryException e) {
                fail("Couldn't put Collection to path /c1/c2/c3/c4/r3");
            }

            try {
                registry.get("/c1/c2/c3");
            }
            catch (RegistryException e) {
                fail("Couldn't get content from path /c1/c2/c3");
            }

            String path_delete = "/c1/c2/c3";
            try {
                registry.delete(path_delete);
            }
            catch (RegistryException e) {
                fail("Couldn't delete content resource " + path_delete);
            }

            boolean failed = false;
            try {
                registry.get(path);
            }
            catch (RegistryException e) {
                failed = true;
            }

            assertTrue("Deleted resource /r1 is returned on get.", failed);
            deleteResources("/c1");
            log.info("SetResourceDetailsTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -SetResourceDetailsTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -SetResourceDetailsTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void SetCollectionDetailsTest() {
        Collection r1;
        try {
            r1 = registry.newCollection();
            r1.setDescription("C3 collection description");
            r1.setProperty("key1", "value5");
            r1.setProperty("key2", "value3");

            String path_collection = "/c1/c2/c3";

            registry.put(path_collection, r1);

            Resource r1_actual = registry.get("/c1/c2/c3");

            assertTrue(r1_actual instanceof Collection);
            assertEquals("LastUpdatedUser is not Equal", username, r1_actual.getLastUpdaterUserName());
            assertEquals("Can not get Resource path", path_collection, r1_actual.getPath());
            assertEquals("Can not get Resource parent path", "/c1/c2", r1_actual.getParentPath());
            assertEquals("Resource description is not equal", r1.getDescription(),
                    r1_actual.getDescription());
            assertEquals("Authour is not equal", username, r1_actual.getAuthorUserName());
            assertEquals("Resource properties are not equal", r1.getProperty("key1"),
                    r1_actual.getProperty("key1"));
            assertEquals("Resource properties are not equal", r1.getProperty("key2"),
                    r1_actual.getProperty("key2"));
            deleteResources("/c1");
            log.info("SetCollectionDetailsTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -SetCollectionDetailsTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -SetCollectionDetailsTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void DeleteResourceTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setContent("this is file for deleting");
            r1.setDescription("this is the description of deleted file");
            r1.setMediaType("text/plain");
            r1.setProperty("key1", "value1");
            r1.setProperty("key2", "value2");

            String path = "/c11/c12/c13/r4";

            registry.put(path, r1);

            String path_delete = "/c11/c12/c13/r4";

            registry.delete(path_delete);

            boolean failed = false;
            try {
                registry.get("/c11/c12/c13/r4");
            }
            catch (RegistryException e) {
                failed = true;
            }

            assertTrue("Deleted resource /c11/c12/c13/r4 is returned on get.", failed);

            /*Add deleted resource again in to same path*/
            Resource r2 = registry.newResource();
            r2.setContent("This is new contenet added after deleting");
            r2.setDescription("this is desc for new resource");
            r2.setMediaType("text/plain");
            r2.setProperty("key1", "value5");
            r2.setProperty("key2", "value3");

            String path_new = "/c11/c12/c13/r4";
            try {
                registry.put(path_new, r2);
            }
            catch (RegistryException e) {
                fail("Couldn't put content to path /c11/c12/c13/r4");
            }

            Resource r1_actual = null;
            try {
                r1_actual = registry.get(path_new);
            }
            catch (RegistryException e) {
                fail("Couldn't get content of path /c11/c12/c13/r4");
            }
            assertEquals("LastUpdatedUser is not Equal", username, r1_actual.getLastUpdaterUserName());
            assertEquals("Can not get Resource path", path_new, r1_actual.getPath());
            assertEquals("Can not get Resource parent path", "/c11/c12/c13", r1_actual.getParentPath());
            assertEquals("Resource description is not equal", r2.getDescription(),
                    r1_actual.getDescription());
            assertEquals("Authour is not equal", username, r1_actual.getAuthorUserName());
            assertEquals("Resource properties are equal", r2.getProperty("key1"),
                    r1_actual.getProperty("key1"));
            assertEquals("Resource properties are equal", r2.getProperty("key2"),
                    r1_actual.getProperty("key2"));
            assertEquals("Media Types is not equal", r2.getMediaType(), r1_actual.getMediaType());
            deleteResources("/c11");
            log.info("DeleteResourceTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -DeleteResourceTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -DeleteResourceTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void DeleteCollectionTest() {
        Resource r1;
        try {
            r1 = registry.newCollection();
            r1.setDescription("this is a collection for deleting");
            r1.setMediaType("text/plain");
            r1.setProperty("key1", "value1");
            r1.setProperty("key2", "value2");
            String path = "/c20/c21/c22";

            registry.put(path, r1);
            String path_delete = "/c20/c21/c22";

            registry.delete(path_delete);

            boolean failed = false;
            try {
                registry.get("/c20/c21/c22");
            }
            catch (RegistryException e) {
                failed = true;
            }

            assertTrue("Deleted collection /c20/c21/c22 is returned on get.", failed);

            /*Add deleted resource again in to same path*/

            Resource r2 = registry.newCollection();
            r2.setDescription("this is desc for new collection");
            r2.setProperty("key1", "value5");
            r2.setProperty("key2", "value3");
            String path_new = "/c20/c21/c22";

            try {
                registry.put(path_new, r2);
            }
            catch (RegistryException e) {
                fail("Couldn't put content to path /c20/c21/c22");
            }

            Resource r1_actual = registry.newCollection();
            try {
                r1_actual = registry.get(path_new);
            }
            catch (RegistryException e) {
                fail("Couldn't get content of path /c20/c21/c22");
            }
            assertEquals("LastUpdatedUser is not Equal", username, r1_actual.getLastUpdaterUserName());
            assertEquals("Can not get Resource path", path_new, r1_actual.getPath());
            assertEquals("Can not get Resource parent path", "/c20/c21", r1_actual.getParentPath());
            assertEquals("Resource description is not equal", r2.getDescription(),
                    r1_actual.getDescription());
            assertEquals("Authour is not equal", username, r1_actual.getAuthorUserName());
            assertEquals("Resource properties are equal", r2.getProperty("key1"),
                    r1_actual.getProperty("key1"));
            assertEquals("Resource properties are equal", r2.getProperty("key2"),
                    r1_actual.getProperty("key2"));
            deleteResources("/c20");
            log.info("DeleteCollectionTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -DeleteCollectionTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -DeleteCollectionTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void AddSpacesinResNameTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setContent("this is file file content");
            r1.setDescription("this is a file name with spaces");
            r1.setMediaType("text/plain");
            r1.setProperty("key1", "value5");
            r1.setProperty("key2", "value3");

            String path = "/d11/d12/d13/r1 space";
            String actualPath = null;
            try {
                actualPath = registry.put(path, r1);
            } catch (RegistryException e) {
                fail("Couldn't put content to path /d11/d12/d13/r1 space");
            }

            Resource r1_actual = null;
            try {
                r1_actual = registry.get(actualPath);
            } catch (RegistryException e) {
                fail("Couldn't get content of path /d11/d12/d13/r1 space");
            }

            assertEquals("LastUpdatedUser is not Equal", username, r1_actual.getLastUpdaterUserName());
            assertEquals("Can not get Resource path", path, r1_actual.getPath());
            assertEquals("Can not get Resource parent path", "/d11/d12/d13", r1_actual.getParentPath());
            assertEquals("Resource description is not equal", r1.getDescription(),
                    r1_actual.getDescription());
            assertEquals("Authour is not equal", username, r1_actual.getAuthorUserName());
            assertEquals("Resource properties are not equal", r1.getProperty("key1"),
                    r1_actual.getProperty("key1"));
            assertEquals("Resource properties are not equal", r1.getProperty("key2"),
                    r1_actual.getProperty("key2"));
            assertEquals("Media Types are not equal", r1.getMediaType(), r1_actual.getMediaType());
            deleteResources("/d11");
            log.info("AddSpacesinResNameTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -AddSpacesinResNameTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddSpacesinResNameTest RegistryException thrown :" + e.getMessage());
        }
    }


    public void AddResourceFromURLTest() {
        String path = "/d25/d21/d23/d24/r1";
        String url = "http://shortwaveapp.com/waves.txt";

        Resource r1;
        try {
            r1 = registry.newResource();
            r1.setDescription("this is a file imported from url");
            r1.setMediaType("java");
            r1.setProperty("key1", "value5");
            r1.setProperty("key2", "value3");

            try {
                registry.importResource(path, url, r1);
            }
            catch (RegistryException e) {
                fail("Couldn't import content to path:" + path);
            }

            Resource r1_actual = registry.newResource();
            try {
                r1_actual = registry.get(path);
            }
            catch (RegistryException e) {
                fail("Couldn't get content from path" + path);
            }

            boolean content = true;
            if (r1_actual == null) {
                content = false;
            }

            assertTrue("Imported file is empty", content);
            assertEquals("LastUpdatedUser is not Equal", username, r1_actual.getLastUpdaterUserName());
            assertEquals("Can not get Resource path", path, r1_actual.getPath());
            assertEquals("Can not get Resource parent path", "/d25/d21/d23/d24",
                    r1_actual.getParentPath());
            assertEquals("Authour is not equal", username, r1_actual.getAuthorUserName());
            deleteResources("/d25");
            log.info("AddResourceFromURLTest -Passed");
        } catch (RegistryException e) {
            log.error("Registry API -AddResourceFromURLTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddResourceFromURLTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void RenameResourceTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            String content = "this is my content";
            r1.setContent(content.getBytes());
            r1.setDescription("This is r1 file description");
            String path = "/d30/d31/r1";

            try {
                registry.put(path, r1);
            }
            catch (RegistryException e) {
                fail("Couldn't put content to path" + path);
            }

            Resource r1_actual = registry.newResource();
            try {
                r1_actual = registry.get(path);
            }
            catch (RegistryException e) {
                fail("Couldn't get content from path" + path);
            }

            assertEquals("Content is not equal.", new String((byte[]) r1.getContent()),
                    new String((byte[]) r1_actual.getContent()));

            /*rename the resource*/

            String new_path = "/d33/d34/r1";

            try {
                registry.rename(path, new_path);
            }
            catch (RegistryException e) {
                fail("Can not rename the path from" + path + "to" + new_path);
            }

            Resource r2_actual = registry.newResource();
            try {
                r2_actual = registry.get(new_path);
            }
            catch (RegistryException e) {
                fail("Couldn't get content from path" + new_path);
            }
            assertEquals("LastUpdatedUser is not Equal", username, r2_actual.getLastUpdaterUserName());
            assertEquals("Can not get Resource path", new_path, r2_actual.getPath());
            assertEquals("Can not get Resource parent path", "/d33/d34", r2_actual.getParentPath());
            assertEquals("Resource description is not equal", r1.getDescription(),
                    r2_actual.getDescription());
            assertEquals("Authour is not equal", username, r2_actual.getAuthorUserName());
            assertEquals("Resource properties are equal", r1.getProperty("key1"),
                    r2_actual.getProperty("key1"));
            assertEquals("Resource properties are equal", r1.getProperty("key2"),
                    r2_actual.getProperty("key2"));
            deleteResources("/d30");
            deleteResources("/d33");
            log.info("RenameResourceTest = Passed");
        } catch (RegistryException e) {
            log.error("Registry API -RenameResourceTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -RenameResourceTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void DeleteandUpdateResourceTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            String content = "this is my content";
            r1.setContent(content.getBytes());
            r1.setDescription("This is r1 file description");
            String path = "/d40/d43/r1";

            try {
                registry.put(path, r1);
            }
            catch (RegistryException e) {
                fail("Couldn't put content to path" + path);
            }

            Resource r1_actual = registry.newResource();
            try {
                r1_actual = registry.get(path);
            }
            catch (RegistryException e) {
                fail("Couldn't get content from path" + path);
            }

            assertEquals("Content is not equal.", new String((byte[]) r1.getContent()),
                    new String((byte[]) r1_actual.getContent()));

            boolean deleted = true;
            try {
                registry.delete(path);
            }
            catch (RegistryException e) {
                fail("Couldn't delete the resource from path" + path);
                deleted = false;
            }

            assertTrue("Resource not deleted", deleted);

            /*add the same resource again*/
            Resource r2 = registry.newResource();
            String content2 = "this is my content updated";
            r2.setContent(content2.getBytes());
            r2.setDescription("This is r1 file description");

            String path_new = "/d40/d43/r1";
            try {
                registry.put(path_new, r2);
            }
            catch (RegistryException e) {
                fail("Couldn't put content to path" + path_new);
            }

            Resource r1_actual2 = registry.newResource();
            try {
                r1_actual2 = registry.get(path_new);
            }
            catch (RegistryException e) {
                fail("Couldn't get content from path" + path_new);
            }
            assertEquals("Content is not equal.", new String((byte[]) r2.getContent()),
                    new String((byte[]) r1_actual2.getContent()));
            registry.delete("/d40");
            log.info("DeleteandUpdateResourceTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -DeleteandUpdateResourceTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -DeleteandUpdateResourceTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void ResourcemultiplePropertiesTest() {

        try {
            String path = "/m11/m12/r1";
            Resource r1 = registry.newResource();
            String content = "this is my content";
            r1.setContent(content.getBytes());
            r1.setDescription("This is r1 file description");
            r1.addProperty("key1", "value1");
            r1.addProperty("key1", "value2");
            r1.addProperty("key1", "value3");
            r1.addProperty("key2", "value1");
            r1.addProperty("key2", "value2");

            registry.put(path, r1);

            Resource r1_actual2 = registry.get(path);

            assertEquals("Content is not equal.", new String((byte[]) r1.getContent()),
                    new String((byte[]) r1_actual2.getContent()));

            List propertyValues = r1_actual2.getPropertyValues("key1");
            Object[] valueName = propertyValues.toArray();

            List propertyValuesKey2 = r1_actual2.getPropertyValues("key2");
            Object[] valueNameKey2 = propertyValuesKey2.toArray();
            assertTrue("value1 is not associated with key1", containsString(valueName, "value1"));
            assertTrue("value2 is not associated with key1", containsString(valueName, "value2"));
            assertTrue("value3 is not associated with key1", containsString(valueName, "value3"));
            assertTrue("value1 is not associated with key2",
                    containsString(valueNameKey2, "value1"));
            assertTrue("value2 is not associated with key2",
                    containsString(valueNameKey2, "value2"));
            deleteResources("/m11");
            log.info("ResourcemultiplePropertiesTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -ResourcemultiplePropertiesTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -ResourcemultiplePropertiesTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void CollectionmultiplePropertiesTest() {

        try {
            String path = "/m15/m16/m17";
            Resource r1 = registry.newCollection();

            r1.setDescription("This m17 description");
            r1.addProperty("key1", "value1");
            r1.addProperty("key1", "value2");
            r1.addProperty("key1", "value3");
            r1.addProperty("key2", "value1");
            r1.addProperty("key2", "value2");

            registry.put(path, r1);

            Resource r1_actual2 = registry.get(path);

            List propertyValues = r1_actual2.getPropertyValues("key1");
            Object[] valueName = propertyValues.toArray();

            List propertyValuesKey2 = r1_actual2.getPropertyValues("key2");
            Object[] valueNameKey2 = propertyValuesKey2.toArray();

            assertTrue("value1 is not associated with key1", containsString(valueName, "value1"));
            assertTrue("value2 is not associated with key1", containsString(valueName, "value2"));
            assertTrue("value3 is not associated with key1", containsString(valueName, "value3"));
            assertTrue("value1 is not associated with key2",
                    containsString(valueNameKey2, "value1"));
            assertTrue("value2 is not associated with key2",
                    containsString(valueNameKey2, "value2"));
            deleteResources("/m15");
            log.info("CollectionmultiplePropertiesTest - Passed");
        }
        catch (RegistryException e) {
            log.error("Registry API -CollectionmultiplePropertiesTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -CollectionmultiplePropertiesTest RegistryException thrown :" + e.getMessage());
        }

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
