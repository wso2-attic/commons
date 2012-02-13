package org.wso2.stratos.automation.test.greg.registry_ws_api_test;


import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.core.*;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

public class TestTagging extends TestTemplate {
    private static final Log log = LogFactory.getLog(TestTagging.class);
    private static WSRegistryServiceClient registry = null;

    @Override
    public void init() {
        testClassName = TestTagging.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WS-API TestTagging Test Cases............................ ");
        addTagging();
        duplicateTagging();
        addTaggingCollection();
        editTagging();
        removeResourceTagging();
        removeCollectionTagging();
        tagging();
        log.info("Completed Running WS-API TestTagging Test Cases....................");
    }

    @Override
    public void cleanup() {

    }

    private void removeResource() {
//        GregResourceRemover gregResourceRemover = new GregResourceRemover();
        deleteResources("/d11");
        deleteResources("/d12");
        deleteResources("/d13");
        deleteResources("/d14");
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


    private void addTagging() {
        // add a resource
        Resource r1 = registry.newResource();
        byte[] r1content = "q1 content".getBytes();

        try {
            r1.setContent(r1content);
            registry.put("/d11/r1", r1);

            Resource r2 = registry.newResource();
            byte[] r2content = "q2 content".getBytes();
            r2.setContent(r2content);
            registry.put("/d11/r2", r2);

//        RemoteRegistry q3Registry = new RemoteRegistry(baseURL, "q3", "");
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
            } catch (Exception e) {
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
            log.info("addTagging - Passed");
        } catch (RegistryException e) {
            log.error("addTagging Registry Exception thrown:" + e.getMessage());
            Assert.fail("addTagging Registry Exception thrown:" + e.getMessage());
        }
    }

    private void duplicateTagging() {
        Resource r1 = registry.newResource();
        byte[] r1content = "q1 content".getBytes();

        try {
            r1.setContent(r1content);
            registry.put("/d12/r1", r1);
            registry.applyTag("/d12/r1", "tag1");
            registry.applyTag("/d12/r1", "tag2");

            Tag[] tags = registry.getTags("/d12/r1");

            boolean tagFound = false;

            for (Tag tag : tags) {
                if (tag.getTagName().equals("tag1")) {
                    tagFound = true;


                    break;

                }
            }
            assertTrue("tag 'tag1' is not associated with the artifact /d12/r1", tagFound);

            deleteResources("/d12");
            log.info("duplicateTagging - Passed");
        } catch (RegistryException e) {
            log.error("duplicateTagging Registry Exception thrown:" + e.getMessage());
            Assert.fail("duplicateTagging Registry Exception thrown:" + e.getMessage());
        }
    }

    private void addTaggingCollection() {
        Collection r1 = registry.newCollection();

        try {
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
            log.info("addTaggingCollection - Passed");
        } catch (RegistryException e) {
            log.error("addTaggingCollection Registry Exception thrown:" + e.getMessage());
            Assert.fail("addTaggingCollection Registry Exception thrown:" + e.getMessage());
        }
    }

    private void editTagging() {
        Resource r1 = registry.newResource();
        byte[] r1content = "q1 content".getBytes();

        try {
            r1.setContent(r1content);
            registry.put("/d14/d13/r1", r1);
            registry.applyTag("/d14/d13/r1", "tag1");
            registry.applyTag("/d14/d13/r1", "tag2");

            Tag[] tags = registry.getTags("/d14/d13/r1");

            boolean tagFound = false;
            for (Tag tag : tags) {
                if (tag.getTagName().equals("tag1")) {
                    tagFound = true;
                    //System.out.println(tag.getTagName());
                    assertEquals("Tag names are not equals", "tag1", tag.getTagName());
                    //System.out.println(tag.getCategory());
                    assertEquals("Tag category not equals", 1, tag.getCategory());
                    //System.out.println(tag.getTagCount());
                    assertEquals("Tag count not equals", 1, (int) (tag.getTagCount()));
                    //System.out.println(tags.length);
                    assertEquals("Tag length not equals", 2, tags.length);

                    tag.setTagName("tag1_updated");
                    break;

                }
            }

            TaggedResourcePath[] paths = null;
            try {
                paths = registry.getResourcePathsWithTag("tag1");
            } catch (Exception e) {
                fail("Failed to get resources with tag 'tag1'");
            }
            boolean artifactFound = false;
            for (TaggedResourcePath path : paths) {
                if (path.getResourcePath().equals("/d14/d13/r1")) {
                    // System.out.println(paths[1].getResourcePath());
                    assertEquals("Path are not matching", "/d14/d13/r1", path.getResourcePath());
                    //System.out.println(paths[1].getTagCount());
                    assertEquals("Tag count not equals", 1, (int) (paths[0].getTagCount()));
//                System.out.println(paths[1].getTagCounts());
//                assertEquals("Tag count not equals",0,(paths[0].getTagCounts()));
                    artifactFound = true;
                    //break;
                }
            }
            assertTrue("/d11/r1 is not tagged with the tag \"jsp\"", artifactFound);
            assertTrue("tag 'col_tag1' is not associated with the artifact /d14/d13/r1", tagFound);
            deleteResources("/d14");
            log.info("editTagging - Passed");
        } catch (RegistryException e) {
            log.error("editTagging Registry Exception thrown:" + e.getMessage());
            Assert.fail("editTagging Registry Exception thrown:" + e.getMessage());
        }
    }

    private void removeResourceTagging() {
        Resource r1 = registry.newResource();
        byte[] r1content = "q1 content".getBytes();

        try {
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
//
            boolean artifactFound = false;

            if (paths != null) {

                for (TaggedResourcePath path : paths) {

                    if (path.getResourcePath().equals("/d15/d14/r1")) {

                        artifactFound = true;

                    }
                }
            }
            assertFalse("/d15/d14/r1 is not tagged with the tag \"tag1\"", artifactFound);
            //assertTrue("/d15/d14/r1 is not tagged with the tag \"tag1\"", artifactFound);
            assertTrue("tag 'tag1' is not associated with the artifact /d15/d14/r1", tagFound);
            deleteResources("/d15");
            log.info("removeResourceTagging - Passed");
        } catch (RegistryException e) {
            log.error("removeResourceTagging Registry Exception thrown:" + e.getMessage());
            Assert.fail("removeResourceTagging Registry Exception thrown:" + e.getMessage());
        }


    }

    private void removeCollectionTagging() {
        CollectionImpl r1 = new CollectionImpl();
        r1.setAuthorUserName("Author q1 remove");

        try {
            registry.put("/d15/d14/d13/d12", r1);
            registry.applyTag("/d15/d14/d13", "tag1");
            registry.applyTag("/d15/d14/d13", "tag2");
            registry.applyTag("/d15/d14/d13", "tag3");

            Tag[] tags = registry.getTags("/d15/d14/d13");
            //System.out.println("getTagCount:" + tags[0].getTagCount());

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

            //System.out.println("Path tag counts:" + paths.length);
            boolean artifactFound = false;

            if (paths != null) {
                for (TaggedResourcePath path : paths) {


                    if (path.getResourcePath().equals("/d15/d14/d13")) {

                        artifactFound = true;

                    }
                }
            }
            assertFalse("/d15/d14/d13 is not tagged with the tag \"tag1\"", artifactFound);
            //assertTrue("/d15/d14/r1 is not tagged with the tag \"tag1\"", artifactFound);
            assertTrue("tag 'tag1' is not associated with the artifact /d15/d14/d13", tagFound);

            deleteResources("/d15");
            log.info("removeCollectionTagging - Passed");
        } catch (RegistryException e) {
            log.error("removeCollectionTagging Registry Exception thrown:" + e.getMessage());
            Assert.fail("removeCollectionTagging Registry Exception thrown:" + e.getMessage());
        }


    }

    private void tagging() {
        // add a resource
        Resource r1 = registry.newResource();
        byte[] r1content = "R1 content".getBytes();

        try {

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

            TaggedResourcePath[] paths2 = registry.getResourcePathsWithTag("jsp");

            assertEquals("Tag based search should not return paths of deleted resources.",
                    paths2, null);

            deleteResources("/d11");
            log.info("tagging - Passed");
        } catch (RegistryException e) {
            log.error("tagging Registry Exception thrown:" + e.getMessage());
            Assert.fail("tagging Registry Exception thrown:" + e.getMessage());
        }
    }
}
