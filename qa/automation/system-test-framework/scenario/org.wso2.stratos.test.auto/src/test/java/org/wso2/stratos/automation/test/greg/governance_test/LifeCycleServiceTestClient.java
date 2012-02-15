package org.wso2.stratos.automation.test.greg.governance_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.wsdls.WsdlManager;
import org.wso2.carbon.governance.api.wsdls.dataobjects.Wsdl;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

public class LifeCycleServiceTestClient extends TestTemplate {
    private static final Log log = LogFactory.getLog(PolicyImportServiceTestClient.class);
    private static WSRegistryServiceClient registry = null;
    private static Registry governance = null;
    private static final String StateProperty = "registry.lifecycle.ServiceLifeCycle.state";
    String wsdl_path = "/_system/governance/trunk/wsdls/com/foo/BizService.wsdl";


    @Override
    public void init() {
        testClassName = LifeCycleServiceTestClient.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        governance = new RegistryProvider().getGovernance(registry, tenantId);
        //Delete wsdl
        deleteWSDL();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running LifeCycleServiceTestClient Test Cases............................ ");
        addWSDL();
        checkLifeCycle();
        log.info("Completed Running LifeCycleServiceTestClient Test Cases...................");
    }

    @Override
    public void cleanup() {
    }


    public void addWSDL() {
        String wsdl_url = "http://people.wso2.com/~evanthika/wsdls/BizService.wsdl";
        WsdlManager wsdlManager = new WsdlManager(governance);
        Wsdl wsdl = null;
        try {
            wsdl = wsdlManager.newWsdl(wsdl_url);
            wsdl.addAttribute("creator2", "it is me");
            wsdl.addAttribute("version2", "0.01");
            wsdlManager.addWsdl(wsdl);
            log.info("LifeCycleServiceTestClient -Add wsdl");
        } catch (GovernanceException e) {
            log.error("addWSDL GovernanceException thrown:" + e.getMessage());
            Assert.fail("addWSDL GovernanceException thrown thrown:" + e.getMessage());
        }
    }

    public void deleteWSDL() {
        try {
            if (registry.resourceExists(wsdl_path)) {
                registry.delete(wsdl_path);
            }
        } catch (RegistryException e) {
            log.error("deleteWSDL RegistryException thrown :" + e.getMessage());
            Assert.fail("deleteWSDL RegistryException thrown :" + e.getMessage());
        }
    }

    public void checkLifeCycle() {
        String testStageState;

        if (FrameworkSettings.getStratosTestStatus()) {
            testStageState = "Tested";
        } else {
            testStageState = "Testing";
        }


        try {
            registry.associateAspect(wsdl_path, "ServiceLifeCycle");
//                  System.out.println(registry.get(wsdl_path).getProperty(StateProperty));
            assertEquals("WSDL Property - WSI creator", registry.get(wsdl_path).getProperty(StateProperty), "Development");
            Thread.sleep(3000);

            //Promote Life cycle to Tested State
            registry.invokeAspect(wsdl_path, "ServiceLifeCycle", "Promote");
//                  System.out.println(registry.get(wsdl_path).getProperty(StateProperty));
            assertEquals("WSDL Property - WSI creator", registry.get(wsdl_path).getProperty(StateProperty), testStageState);
            Thread.sleep(3000);

            //Promote Life cycle to Production State
            registry.invokeAspect(wsdl_path, "ServiceLifeCycle", "Promote");
//                   System.out.println(registry.get(wsdl_path).getProperty(StateProperty));
            assertEquals("WSDL Property - WSI creator", registry.get(wsdl_path).getProperty(StateProperty), "Production");
            Thread.sleep(3000);

            //Demote Life cycle to Tested State
            registry.invokeAspect(wsdl_path, "ServiceLifeCycle", "Demote");
//                  System.out.println(registry.get(wsdl_path).getProperty(StateProperty));
            assertEquals("WSDL Property - WSI creator", registry.get(wsdl_path).getProperty(StateProperty), testStageState);
            Thread.sleep(3000);

            //Demote Life cycle to Development State
            registry.invokeAspect(wsdl_path, "ServiceLifeCycle", "Demote");
//                  System.out.println(registry.get(wsdl_path).getProperty(StateProperty));
            assertEquals("WSDL Property - WSI creator", registry.get(wsdl_path).getProperty(StateProperty), "Development");
            Thread.sleep(3000);
            //Delete wsdl
            deleteWSDL();
        } catch (RegistryException e1) {
            log.error("checkLifeCycle RegistryException thrown :" + e1.getMessage());
            Assert.fail("checkLifeCycle RegistryException thrown :" + e1.getMessage());
        } catch (InterruptedException e) {
            log.error("checkLifeCycle InterruptedException thrown :" + e.getMessage());
            Assert.fail("checkLifeCycle InterruptedException thrown :" + e.getMessage());
        }
    }
}
