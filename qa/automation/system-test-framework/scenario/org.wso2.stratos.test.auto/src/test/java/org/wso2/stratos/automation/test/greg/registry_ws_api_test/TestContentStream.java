package org.wso2.stratos.automation.test.greg.registry_ws_api_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

import java.io.*;

public class TestContentStream extends TestTemplate {
    private static final Log log = LogFactory.getLog(TestContentStream.class);
    private static WSRegistryServiceClient registry = null;
    String resourcePath;

    @Override
    public void init() {
        testClassName = TestContentStream.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        resourcePath = ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION;
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WS-API TestContentStream Test Cases............................ ");
        putResourceasStreamXML();
        contentStreaming();
        setContainStreamXML();
        log.info("Completed Running WS-API TestContentStream Test Cases....................");
    }

    @Override
    public void cleanup() {

    }

    private void removeResource() {
//        GregResourceRemover gregResourceRemover = new GregResourceRemover();
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

       private void putResourceasStreamXML() {
        final String description = "testPutXMLResourceAsBytes";
        final String mediaType = "application/xml";
        // Establish where we are putting the resource in registry
        final String registryPath = "/wso2registry/conf/pom.xml";

        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(getTestResourcePath() +
                    "pom.xml"));
            String st = null;
            try {
                st = slurp(is);
            } catch (IOException e) {
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
            log.error("putResourceasStreamXML FileNotFoundException thrown :" + e.getMessage());
            Assert.fail("putResourceasStreamXML FileNotFoundException thrown :" + e.getMessage());
        } catch (RegistryException e) {
            log.error("putResourceasStreamXML RegistryException thrown :" + e.getMessage());
            Assert.fail("putResourceasStreamXML RegistryException thrown :" + e.getMessage());
        }
    }

    private String getTestResourcePath() {
        String repo_path = resourcePath + File.separator + "artifacts" + File.separator + "GREG" + File.separator;
        return repo_path;
    }

    private void contentStreaming() {
        Resource r3 = registry.newResource();
        String path = "/content/stream/content.txt";

        try {
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
            log.info("contentStreaming - Passed");
        } catch (RegistryException e) {
            log.error("contentStreaming RegistryException thrown :" + e.getMessage());
            Assert.fail("contentStreaming RegistryException thrown :" + e.getMessage());
        }
    }

    private void setContainStreamXML() {
        final String description = "testPutXMLResourceAsBytes";
        final String mediaType = "application/xml";
        // Establish where we are putting the resource in registry
        final String registryPath = "/wso2registry/contentstream/conf/pom.xml";
        InputStream is = null;
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
            log.error("setContainStreamXML FileNotFoundException thrown :" + e.getMessage());
            Assert.fail("setContainStreamXML FileNotFoundException thrown :" + e.getMessage());
        } catch (RegistryException e) {
            log.error("setContainStreamXML RegistryException thrown :" + e.getMessage());
            Assert.fail("setContainStreamXML RegistryException thrown :" + e.getMessage());
        }
    }

//    The below methods are used in the above tests. No need to add them to the test suit.

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    private static String slurp(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }
}
