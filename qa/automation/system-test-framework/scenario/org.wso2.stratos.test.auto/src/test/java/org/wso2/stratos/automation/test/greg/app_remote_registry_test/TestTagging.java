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
import org.wso2.carbon.registry.core.*;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregRemoteRegistryProvider;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;

public class TestTagging extends TestTemplate {
    private static final Log log = LogFactory.getLog(TestTagging.class);
    public RemoteRegistry registry;

    @Override
    public void init() {
        testClassName = TestTagging.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new GregRemoteRegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running Registry API - TestTaggingt Test ..........................");
        AddTaggingTest();
        DuplicateTaggingTest();
        AddTaggingCollectionTest();
//        EditTaggingTest();
        RemoveResourceTaggingTest();
        RemoveCollectionTaggingTest();
        TaggingTest();
        log.info("Completed Running Registry API - TestTaggingt Test .....................");
    }

    @Override
    public void cleanup() {

    }

    private void removeResource() {
        deleteResources("/d11");
        deleteResources("/d12");
        deleteResources("/d13");
        deleteResources("/d15");
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

    public void AddTaggingTest() {
        // add a resource
        Resource r1;
        try {
            r1 = registry.newResource();
            byte[] r1content = "q1 content".getBytes();
            r1.setContent(r1content);
            registry.put("/d11/r1", r1);

            Resource r2 = registry.newResource();
            byte[] r2content = "q2 content".getBytes();
            r2.setContent(r2content);
            registry.put("/d11/r2", r2);

            Resource r3 = registry.newResource();
            byte[] r3content = "q3 content".getBytes();
            r3.setContent(r3content);
            registry.put("/d11/r3", r3);

            registry.applyTag("/d11/r1", "jsp");
            registry.applyTag("/d11/r2", "jsp");
            registry.applyTag("/d11/r3", "java long tag");

            TaggedResourcePath[] paths = registry.getResourcePathsWithTag("jsp");
            boolean artifactFound = false;
            for (TaggedResourcePath path : paths) {


                if (path.getResourcePath().equals("/d11/r1")) {
                    assertEquals("Path are not matching", "/d11/r1", path.getResourcePath());
                    artifactFound = true;
                }
            }
            assertTrue("/d11/r1 is not tagged with the tag \"jsp\"", artifactFound);

            Tag[] tags = null;
            try {
                tags = registry.getTags("/d11/r1");
            }
            catch (RegistryException e) {
                fail("Failed to get tags for the resource /d11/r1");
            }

            boolean tagFound = false;
            for (Tag tag : tags) {
                if (tag.getTagName().equals("jsp")) {
                    tagFound = true;
                    break;
                }
            }
            assertTrue("tag 'jsp' is not associated with the artifact /d11/r1", tagFound);
            registry.getResourcePathsWithTag("jsp");
            deleteResources("/d11");
            log.info("AddTaggingTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -AddTaggingTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddTaggingTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void DuplicateTaggingTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            byte[] r1content = "q1 content".getBytes();
            r1.setContent(r1content);
            registry.put("/d12/r1", r1);

            registry.applyTag("/d12/r1", "tag1");
            registry.applyTag("/d12/r1", "tag2");

            Tag[] tags = registry.getTags("/d12/r1");

            boolean tagFound = false;
            for (Tag tag : tags) {
                if (tag.getTagName().equals("tag1")) {
                    tagFound = true;
                }
            }
            assertTrue("tag 'tag1' is not associated with the artifact /d12/r1", tagFound);
            deleteResources("/d12");
            log.info("DuplicateTaggingTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -DuplicateTaggingTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -DuplicateTaggingTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void AddTaggingCollectionTest() {
        Collection r1;
        try {
            r1 = registry.newCollection();
            registry.put("/d13/d14", r1);
            registry.applyTag("/d13/d14", "col_tag1");

            Tag[] tags = registry.getTags("/d13/d14");

            boolean tagFound = false;
            for (Tag tag : tags) {
                if (tag.getTagName().equals("col_tag1")) {
                    tagFound = true;
                    break;
                }
            }
            assertTrue("tag 'col_tag1' is not associated with the artifact /d13/d14", tagFound);
            deleteResources("/d13");
            log.info("AddTaggingCollectionTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -AddTaggingCollectionTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddTaggingCollectionTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void EditTaggingTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            byte[] r1content = "q1 content".getBytes();
            r1.setContent(r1content);
            registry.put("/d14/d13/r1", r1);

            registry.applyTag("/d14/d13/r1", "tag1");
            registry.applyTag("/d14/d13/r1", "tag2");

            Tag[] tags = registry.getTags("/d14/d13/r1");

            boolean tagFound = false;
            for (Tag tag : tags) {
                if (tag.getTagName().equals("tag1")) {
                    tagFound = true;
                    assertEquals("Tag names are not equals", "tag1", tag.getTagName());
                    assertEquals("Tag category not equals", 1, tag.getCategory());
                    assertEquals("Tag count not equals", 1, (int) (tag.getTagCount()));
                    assertEquals("Tag length not equals", 2, tags.length);
                    tag.setTagName("tag1_updated");
                    break;
                }
            }

            TaggedResourcePath[] paths = null;
            try

            {
                paths = registry.getResourcePathsWithTag("tag1");

            } catch (RegistryException e) {
                fail("Failed to get resources with tag 'tag1'");
            }

            boolean artifactFound = false;
            for (
                    TaggedResourcePath path
                    : paths)

            {
                if (path.getResourcePath().equals("/d14/d13/r1")) {
                    assertEquals("Path are not matching", "/d14/d13/r1", path.getResourcePath());
                    assertEquals("Tag count not equals", 0, (int) (paths[0].getTagCount()));
                    artifactFound = true;
                }
            }
            assertTrue("/d11/r1 is not tagged with the tag \"jsp\"", artifactFound);
            assertTrue("tag 'col_tag1' is not associated with the artifact /d14/d13/r1", tagFound);
            deleteResources("/d14");
            log.info("EditTaggingTest");
        } catch (RegistryException e) {
            log.error("Registry API -EditTaggingTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -EditTaggingTest RegistryException thrown :" + e.getMessage());
        }
    }


    public void RemoveResourceTaggingTest() {
        Resource r1;
        try {
            r1 = registry.newResource();
            byte[] r1content = "q1 content".getBytes();
            r1.setContent(r1content);
            registry.put("/d15/d14/r1", r1);

            registry.applyTag("/d15/d14/r1", "tag1");
            registry.applyTag("/d15/d14/r1", "tag2");

            Tag[] tags = registry.getTags("/d15/d14/r1");

            boolean tagFound = false;
            for (Tag tag : tags) {
                if (tag.getTagName().equals("tag1")) {
                    tagFound = true;
                }
            }

            assertTrue("tag 'tag1' is not associated with the artifact /d15/d14/r1", tagFound);

            /*remove tag goes here*/
            registry.removeTag("/d15/d14/r1", "tag1");

            TaggedResourcePath[] paths = registry.getResourcePathsWithTag("tag1");
            boolean artifactFound = false;
            for (TaggedResourcePath path : paths) {
                if (path.getResourcePath().equals("/d15/d14/r1")) {
                    artifactFound = true;
                }
            }
            assertFalse("/d15/d14/r1 is not tagged with the tag \"tag1\"", artifactFound);
            assertTrue("tag 'tag1' is not associated with the artifact /d15/d14/r1", tagFound);
            deleteResources("/d15");
            log.info("RemoveResourceTaggingTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -RemoveResourceTaggingTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -RemoveResourceTaggingTest RegistryException thrown :" + e.getMessage());
        }

    }

    public void RemoveCollectionTaggingTest() {
        CollectionImpl r1 = new CollectionImpl();
        r1.setAuthorUserName("Author q1 remove");
        try {
            registry.put("/d15/d14/d13/d12", r1);
            registry.applyTag("/d15/d14/d13", "tag1");
            registry.applyTag("/d15/d14/d13", "tag2");
            registry.applyTag("/d15/d14/d13", "tag3");

            Tag[] tags = registry.getTags("/d15/d14/d13");

            boolean tagFound = false;
            for (Tag tag : tags) {
                if (tag.getTagName().equals("tag1")) {
                    tagFound = true;
                }
            }

            assertTrue("tag 'tag1' is not associated with the artifact /d15/d14/d13", tagFound);

            /*remove tag goes here*/

            registry.removeTag("/d15/d14/d13", "tag1");

            TaggedResourcePath[] paths = registry.getResourcePathsWithTag("tag1");

            boolean artifactFound = false;
            for (TaggedResourcePath path : paths) {
                if (path.getResourcePath().equals("/d15/d14/d13")) {
                    artifactFound = true;
                }
            }
            assertFalse("/d15/d14/d13 is not tagged with the tag \"tag1\"", artifactFound);
            assertTrue("tag 'tag1' is not associated with the artifact /d15/d14/d13", tagFound);
            deleteResources("/d15");
            log.info("RemoveCollectionTaggingTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -RemoveCollectionTaggingTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -RemoveCollectionTaggingTest RegistryException thrown :" + e.getMessage());
        }


    }

    public void TaggingTest() {
        // add a resource
        Resource r1;
        try {
            r1 = registry.newResource();
            byte[] r1content = "R1 content".getBytes();
            r1.setContent(r1content);
            registry.put("/d11/r1", r1);

            Resource r2 = registry.newResource();
            byte[] r2content = "R2 content".getBytes();
            r2.setContent(r2content);
            registry.put("/d11/r2", r2);

            Resource r3 = registry.newResource();
            byte[] r3content = "R3 content".getBytes();
            r3.setContent(r3content);
            registry.put("/d11/r3", r3);

            registry.applyTag("/d11/r1", "JSP");
            registry.applyTag("/d11/r2", "jsp");
            registry.applyTag("/d11/r3", "jaVa");

            registry.applyTag("/d11/r1", "jsp");
            Tag[] r11Tags = registry.getTags("/d11/r1");
            assertEquals(1, r11Tags.length);

            TaggedResourcePath[] paths = registry.getResourcePathsWithTag("jsp");
            boolean artifactFound = false;
            for (TaggedResourcePath path : paths) {
                if (path.getResourcePath().equals("/d11/r1")) {
                    artifactFound = true;
                    break;
                }
            }
            assertTrue("/d11/r1 is not tagged with the tag \"jsp\"", artifactFound);

            Tag[] tags = registry.getTags("/d11/r1");

            boolean tagFound = false;
            for (Tag tag : tags) {
                if (tag.getTagName().equalsIgnoreCase("jsp")) {
                    tagFound = true;
                    break;
                }
            }
            assertTrue("tag 'jsp' is not associated with the artifact /d11/r1", tagFound);
            deleteResources("/d11");
            log.info("TaggingTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -TaggingTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -TaggingTest RegistryException thrown :" + e.getMessage());
        }
    }
}
