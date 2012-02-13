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

import java.io.*;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregRemoteRegistryProvider;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;


public class TestContentStream extends TestTemplate {
    private static final Log log = LogFactory.getLog(TestTagging.class);
    public RemoteRegistry registry;
    String resourcePath;

    @Override
    public void init() {
        testClassName = CommentTest.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new GregRemoteRegistryProvider().getRegistry(tenantId);
        resourcePath = ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION;
        removeResource();
    }


    @Override
    public void runSuccessCase() {
        log.info("Running Registry API - TestContentStream Test ..........................");
        putResourceasStreamXML();
        ContentStreaming();
        setContainStreamXML();
        log.info("Completed Running Registry API - TestContentStream Test ..........................");
    }


    @Override
    public void cleanup() {

    }

    private void removeResource() {
        deleteResources("/wso2registry");
        deleteResources("/content");
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

    public void putResourceasStreamXML() {
        final String description = "testPutXMLResourceAsBytes";
        final String mediaType = "application/xml";

        final String registryPath = "/wso2registry/conf/pom.xml";

        InputStream is;
        try {
            is = new BufferedInputStream(new FileInputStream(getTestResourcePath() +
                    "pom.xml"));
            String st = null;
            try {
                st = slurp(is);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            Resource resource = registry.newResource();
            resource.setContent(st.getBytes());
            resource.setDescription(description);
            resource.setMediaType(mediaType);
            registry.put(registryPath, resource);

            Resource r2 = registry.get(registryPath);
            assertEquals("File content is not matching", new String((byte[]) resource.getContent()),
                    new String((byte[]) r2.getContent()));
            deleteResources("/wso2registry");
            log.info("putResourceasStreamXML - Passed");
        } catch (FileNotFoundException e) {
            log.error("Registry API -putResourceasStreamXML FileNotFoundExceptionthrown :" + e.getMessage());
            Assert.fail("Registry API -putResourceasStreamXML FileNotFoundException thrown :" + e.getMessage());
        } catch (RegistryException e) {
            log.error("Registry API -putResourceasStreamXML RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -putResourceasStreamXML RegistryException thrown :" + e.getMessage());
        }
    }

    private String getTestResourcePath() {
        String repo_path = resourcePath + File.separator + "artifacts" + File.separator + "GREG" + File.separator;
        return repo_path;
    }

    public void ContentStreaming() {
        Resource r3;
        try {
            r3 = registry.newResource();
            String path = "/content/stream/content.txt";
            r3.setContent(new String("this is the content").getBytes());
            r3.setDescription("this is test desc");
            r3.setMediaType("plain/text");
            r3.setProperty("test2", "value2");
            r3.setProperty("test1", "value1");
            registry.put(path, r3);

            Resource r4 = registry.get("/content/stream/content.txt");

            assertEquals("Content is not equal.", new String((byte[]) r3.getContent()),
                    new String((byte[]) r4.getContent()));

            InputStream isTest = r4.getContentStream();
            assertEquals("Content stream is not equal.", new String((byte[]) r3.getContent()),
                    convertStreamToString(isTest));

            r3.discard();
            deleteResources("/content");
            log.info("ContentStreaming - Passed");
        } catch (RegistryException e) {
            log.error("Registry API -ContentStreaming RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -ContentStreaming RegistryException thrown :" + e.getMessage());
        }
    }

    public void setContainStreamXML() {
        final String description = "testPutXMLResourceAsBytes";
        final String mediaType = "application/xml";
        final String registryPath = "/wso2registry/contentstream/conf/pom.xml";

        InputStream is;
        try {
            is = new BufferedInputStream(new FileInputStream(getTestResourcePath() +
                    "pom.xml"));
            Resource resource = registry.newResource();

            resource.setContentStream(is);
            resource.setDescription(description);
            resource.setMediaType(mediaType);
            registry.put(registryPath, resource);
            Resource r2 = registry.get(registryPath);
            assertEquals("File content is not matching", new String((byte[]) resource.getContent()),
                    new String((byte[]) r2.getContent()));
            deleteResources("/wso2registry");
            log.info("setContainStreamXML - Passed");
        } catch (FileNotFoundException e) {
            log.error("Registry API -setContainStreamXML FileNotFoundExceptionthrown :" + e.getMessage());
            Assert.fail("Registry API -setContainStreamXML FileNotFoundException thrown :" + e.getMessage());
        } catch (RegistryException e) {
            log.error("Registry API -setContainStreamXML RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API -setContainStreamXML RegistryException thrown :" + e.getMessage());
        }
    }

    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public static String slurp(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }
}
