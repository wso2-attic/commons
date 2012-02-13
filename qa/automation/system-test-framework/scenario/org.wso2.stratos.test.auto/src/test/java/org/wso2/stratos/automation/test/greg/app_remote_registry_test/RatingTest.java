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
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregRemoteRegistryProvider;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class RatingTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(RatingTest.class);
    public RemoteRegistry registry;

    @Override
    public void init() {
        testClassName = RatingTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new GregRemoteRegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running Registry API - RatingTest Test ..........................");
        AddResourceRatingTest();
        AddCollectionRatingTest();
        EditResourceRatingTest();
        RatingsPathTest();
        log.info("Completed Running Registry API - RatingTest Test ..........................");
    }


    @Override
    public void cleanup() {

    }

    private void removeResource() {
        deleteResources("/d16");
        deleteResources("/d61");
        deleteResources("/c1");

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

    public void AddResourceRatingTest() {
        String path = "/d16/d17/r1";
        Resource r1;
        try {
            r1 = registry.newResource();
            byte[] r1content = "R1 content".getBytes();
            r1.setContent(r1content);
            registry.put(path, r1);
            registry.rateResource(path, 5);
            float rating = registry.getAverageRating(path);
            assertEquals("Rating of the resource /d16/d17/r1 should be 5.", rating, (float) 5.0,
                    (float) 0.01);
            deleteResources("/d16");
            log.info("AddResourceRatingTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -AddResourceRatingTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddResourceRatingTestt RegistryException thrown :" + e.getMessage());
        }

    }

    public void AddCollectionRatingTest() {
        String path = "/d16/d18";
        Resource r1;
        try {
            r1 = registry.newCollection();
            registry.put(path, r1);
            registry.rateResource(path, 4);
            float rating = registry.getAverageRating(path);
            assertEquals("Rating of the resource /d16/d18 should be 5.", rating, (float) 4.0,
                    (float) 0.01);
            deleteResources("/d16");
            log.info("AddCollectionRatingTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -AddCollectionRatingTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -AddCollectionRatingTest RegistryException thrown :" + e.getMessage());
        }
    }

    public void EditResourceRatingTest() {
        String path = "/d61/d17/d18/r1";
        Resource r1;
        try {
            r1 = registry.newResource();
            byte[] r1content = "R1 content".getBytes();
            r1.setContent(r1content);
            registry.put(path, r1);
            registry.rateResource(path, 5);

            float rating = registry.getAverageRating(path);
            assertEquals("Rating of the resource /d61/d17/d18/r1 should be 5.", (float) 5.0, rating,
                    (float) 0.01);
            registry.rateResource(path, 3);
            float rating_edit = registry.getAverageRating(path);
            assertEquals("Rating of the resource /d61/d17/d18/r1 should be 3.", (float) 3.0, rating_edit,
                    (float) 0.01);
            deleteResources("/d61");
            log.info("EditResourceRatingTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -EditResourceRatingTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -EditResourceRatingTest RegistryException thrown :" + e.getMessage());
        }


    }

    public void RatingsPathTest() {
        Resource r5;
        try {
            r5 = registry.newResource();
            String r5Content = "this is r5 content";
            r5.setContent(r5Content.getBytes());
            r5.setDescription("production ready.");
            String r5Path = "/c1/r5";

            registry.put(r5Path, r5);
            registry.rateResource("/c1/r5", 3);
            Resource ratings = registry.get("/c1/r5;ratings");
            String[] ratingPaths = (String[]) ratings.getContent();

            Resource c1 = registry.get(ratingPaths[0]);
            InputStream stream = c1.getContentStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(stream, writer);
            String ratingString = writer.toString();
            assertEquals("Ratings are not retrieved properly as resources.", ratingString, "3");
            deleteResources("/c1");
            log.info("RatingsPathTest - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -RatingsPathTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -RatingsPathTest RegistryException thrown :" + e.getMessage());
        } catch (IOException e) {
            log.error("Registry API- RatingsPathTest RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -RatingsPathTest RegistryException thrown :" + e.getMessage());
        }

    }


}
