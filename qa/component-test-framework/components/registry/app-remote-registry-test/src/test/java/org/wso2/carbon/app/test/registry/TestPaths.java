/*
 * Copyright 2004,2005 The Apache Software Foundation.
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

package org.wso2.carbon.app.test.registry;

import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

public class TestPaths extends TestTemplate {
     public RemoteRegistry registry;

    @Override
    public void init() {
        InitializeAPI initializeAPI = new InitializeAPI();
        registry = initializeAPI.getRegistry(FrameworkSettings.CARBON_HOME,FrameworkSettings.HTTPS_PORT,FrameworkSettings.HTTP_PORT);
    }

    @Override
    public void runSuccessCase() {
        try {
            GetOnPathsTest();
            PutOnPathsTest();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }

    public void GetOnPathsTest() throws RegistryException {

        Resource r1 = registry.newResource();
        // r1.setContent("some content");
        registry.put("/testkrishantha/paths/r1", r1);

        assertTrue("Resource not found.", registry.resourceExists("/testkrishantha"));
        assertTrue("Resource not found.", registry.resourceExists("/testkrishantha/"));
        assertTrue("Resource not found.", registry.resourceExists("/testkrishantha/paths/r1"));
        assertTrue("Resource not found.", registry.resourceExists("/testkrishantha/paths/r1/"));

        registry.get("/testkrishantha");
        registry.get("/testkrishantha/");
        registry.get("/testkrishantha/paths/r1");
        registry.get("/testkrishantha/paths/r1/");

    }

    public void PutOnPathsTest() throws RegistryException {

        Resource r1 = registry.newResource();
        r1.setContent("some content");
        registry.put("/testkrishantha1/paths2/r1", r1);

        Resource r2 = registry.newResource();
        r2.setContent("another content");
        registry.put("/testkrishantha1/paths2/r2", r2);

        Collection c1 = registry.newCollection();
        registry.put("/testkrishantha1/paths2/c1", c1);

        Collection c2 = registry.newCollection();
        registry.put("/testkrishantha1/paths2/c2", c2);

        assertTrue("Resource not found.", registry.resourceExists("/testkrishantha1/paths2/r1"));
        assertTrue("Resource not found.", registry.resourceExists("/testkrishantha1/paths2/r2"));
        assertTrue("Resource not found.", registry.resourceExists("/testkrishantha1/paths2/c1"));
        assertTrue("Resource not found.", registry.resourceExists("/testkrishantha1/paths2/c2"));
    }
}
