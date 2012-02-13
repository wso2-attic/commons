package org.wso2.stratos.automation.test.greg.registry_ws_api_test;


import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.ResourceImpl;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

public class ContinuousOperations extends TestTemplate {
    private static final Log log = LogFactory.getLog(ContinuousOperations.class);
    private static WSRegistryServiceClient registry = null;

    @Override
    public void init() {
        testClassName = ContinuousOperations.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WS-API ContinuousOperations Test Cases............................ ");
        continousDelete();
        continuousUpdate();
        log.info("Completed Running WS-API ContinuousOperations Test Cases................... ");
    }

    @Override
    public void cleanup() {

    }

    private void removeResource() {
//        GregResourceRemover gregResourceRemover = new GregResourceRemover();
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

    private void continousDelete() {
        int iterations = 100;

        for (int i = 0; i < iterations; i++) {
            Resource res1 = registry.newResource();
            byte[] r1content = "R2 content".getBytes();

            try {
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
                log.error("continousDelete RegistryException thrown:" + e.getMessage());
                Assert.fail("continousDelete RegistryException thrown:" + e.getMessage());
            } catch (InterruptedException e) {
                log.error("continousDelete InterruptedException thrown:" + e.getMessage());
                Assert.fail("continousDelete InterruptedException thrown:" + e.getMessage());
            }
        }
        log.info("continousDelete - Passed");
    }

    private void continuousUpdate() {
        int iterations = 100;

        for (int i = 0; i < iterations; i++) {
            Resource res1 = registry.newResource();
            byte[] r1content = "R2 content".getBytes();

            try {
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
                log.error("continuousUpdate RegistryException thrown:" + e.getMessage());
                Assert.fail("continuousUpdate RegistryException thrown:" + e.getMessage());
            } catch (InterruptedException e) {
                log.error("continuousUpdate InterruptedException thrown:" + e.getMessage());
                Assert.fail("continuousUpdate InterruptedException thrown:" + e.getMessage());
            }
        }
        log.info("continuousUpdate - Passed");
    }
}
