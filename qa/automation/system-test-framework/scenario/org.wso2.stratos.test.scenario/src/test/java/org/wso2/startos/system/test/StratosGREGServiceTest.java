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

package org.wso2.startos.system.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.Comment;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.startos.system.test.stratosUtils.ServiceLoginClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StratosGREGServiceTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(StratosGREGServiceTest.class);
    private static RemoteRegistry registry = null;
    private static String HTTP_GOVERNANCE_URL;

    @Override
    public void init() {
        log.info("Running GREG regression test");
        testClassName = StratosGREGServiceTest.class.getName();
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("4"));
        HTTP_GOVERNANCE_URL = "http://" + FrameworkSettings.GREG_SERVER_HOST_NAME + "/t/" + tenantDetails.getTenantDomain();
    }

    @Override
    public void runSuccessCase() {
        String gregServerHostName = FrameworkSettings.GREG_SERVER_HOST_NAME;
        ServiceLoginClient.loginChecker(gregServerHostName);
        remoteRegistryClientTest();
    }

    @Override
    public void cleanup() {
    }

    public static void remoteRegistryClientTest() {

        String path = "/_system/local/registry.txt";


        boolean getValue = false;
        boolean putValue = false;
        boolean deleteValue = false;

        try {
            registry = new RemoteRegistry(new URL(HTTP_GOVERNANCE_URL + "/registry"),
                    "admin123@manualQA0001.org", "admin123");
        } catch (RegistryException e) {
            log.error("Failed to initiate remote registry instance :" + e.getMessage());
            fail("Failed to initiate remote registry instance  :" + e.getMessage());
        } catch (MalformedURLException e) {
            log.error("Malformed remote registry URL :" + e.getMessage());
            fail("Malformed remote registry URL :" + e.getMessage());
        }

        /*put resource */

        try {
            Resource r1 = registry.newResource();
            r1.setContent("test content".getBytes());
            r1.setMediaType("text/plain");
            String pathValue = registry.put(path, r1);

            if (pathValue.equalsIgnoreCase(path)) {
                log.info("Resource successfully uploaded to registry");
                putValue = true;
            }
            assertTrue("Failed to upload resource to registry", putValue);
        } catch (RegistryException e) {
            log.error("Failed to upload resource to registry :" + e.getMessage());
            fail("Failed to upload resource to registry :" + e.getMessage());
        }

        /*get resource */
        try {
            Resource r2 = registry.get(path);

            if (r2.getMediaType().equalsIgnoreCase("text/plain")) {
                getValue = true;
            }
            assertTrue("Failed to get resource media type", getValue);
        } catch (RegistryException e) {
            log.error("Failed to get resource :" + e.getMessage());
            fail("Failed to get resource :" + e.getMessage());
        }

        /*Delete resource */
        try {
            registry.delete(path);

            if (!registry.resourceExists(path)) {
                deleteValue = true;
            }
            assertTrue("Failed to delete resource", deleteValue);

        } catch (RegistryException e) {
            log.error("Failed to delete resource :" + e.getMessage());
            fail("Failed to delete resource :" + e.getMessage());
        }
    }

}