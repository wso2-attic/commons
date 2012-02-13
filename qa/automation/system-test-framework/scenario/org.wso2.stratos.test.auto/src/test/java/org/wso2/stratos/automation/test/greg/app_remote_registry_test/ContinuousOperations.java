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
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.ResourceImpl;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregRemoteRegistryProvider;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;

public class ContinuousOperations extends TestTemplate {
    private static final Log log = LogFactory.getLog(ContinuousOperations.class);
    public RemoteRegistry registry;

    @Override
    public void init() {
        testClassName = ContinuousOperations.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new GregRemoteRegistryProvider().getRegistry(tenantId);
        removeResource();

    }

    @Override
    public void runSuccessCase() {
        log.info("Running Registry API - ContinuousOperations Test ..........................");
        ContinousDelete();
        ContinuousUpdate();
        log.info("Completed Running Registry API - ContinuousOperations Test ..........................");
    }

    private void removeResource() {
        deleteResources("/con-delete");

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

    @Override
    public void cleanup() {
    }

    public void ContinousDelete() {
        int iterations = 100;

        for (int i = 0; i < iterations; i++) {
            Resource res1;
            try {
                res1 = registry.newResource();
                byte[] r1content = "R2 content".getBytes();
                res1.setContent(r1content);
                String path = "/con-delete/test/" + i + 1;
                registry.put(path, res1);

                Resource resource1 = registry.get(path);
                assertEquals("File content is not matching", new String((byte[]) resource1.getContent()),
                        new String((byte[]) res1.getContent()));
                registry.delete(path);
                boolean value = false;
                if (registry.resourceExists(path)) {
                    value = true;
                }

                assertFalse("Resoruce not found at the path", value);
                res1.discard();
                resource1.discard();
                Thread.sleep(100);
                deleteResources("/con-delete");
            } catch (RegistryException e) {
                log.error("Registry API -ContinousDelete RegistryException thrown :" + e.getMessage());
                Assert.fail("Registry API -ContinousDelete RegistryException thrown :" + e.getMessage());
            } catch (InterruptedException e) {
                log.error("Registry API -ContinousDelete InterruptedException thrown :" + e.getMessage());
                Assert.fail("Registry API -ContinousDelete InterruptedException thrown :" + e.getMessage());
            }
        }
        log.info("ContinousDelete -Passed");
    }

    public void ContinuousUpdate() {
        int iterations = 100;

        for (int i = 0; i < iterations; i++) {
            Resource res1;
            try {
                res1 = registry.newResource();
                byte[] r1content = "R2 content".getBytes();
                res1.setContent(r1content);
                String path = "/con-delete/test-update/" + i + 1;

                registry.put(path, res1);
                Resource resource1 = registry.get(path);
                assertEquals("File content is not matching", new String((byte[]) resource1.getContent()),
                        new String((byte[]) res1.getContent()));

                Resource resource = new ResourceImpl();
                byte[] r1content1 = "R2 content updated".getBytes();
                resource.setContent(r1content1);
                resource.setProperty("abc", "abc");
                registry.put(path, resource);
                Resource resource2 = registry.get(path);
                assertEquals("File content is not matching", new String((byte[]) resource.getContent()),
                        new String((byte[]) resource2.getContent()));
                resource.discard();
                res1.discard();
                resource1.discard();
                resource2.discard();
                Thread.sleep(100);
                deleteResources("/con-delete");
            } catch (RegistryException e) {
                log.error("Registry API -ContinuousUpdate RegistryException thrown :" + e.getMessage());
                Assert.fail("Registry API -ContinuousUpdate RegistryException thrown :" + e.getMessage());
            } catch (InterruptedException e) {
                log.error("Registry API -ContinuousUpdate InterruptedException thrown :" + e.getMessage());
                Assert.fail("Registry API -ContinuousUpdate InterruptedException thrown :" + e.getMessage());
            }

        }
        log.info("ContinuousUpdate -Passed");
    }
}
