package org.wso2.stratos.automation.test.greg.governance_test;

import junit.framework.Assert;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.governance.api.wsdls.WsdlManager;
import org.wso2.carbon.governance.api.wsdls.dataobjects.Wsdl;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

import javax.xml.namespace.QName;
import java.util.List;


public class WsdlUpadateContentServiceTestClient extends TestTemplate {
    private static final Log log = LogFactory.getLog(WsdlUpadateContentServiceTestClient.class);
    private static WSRegistryServiceClient registry = null;
    private static Registry governance = null;
    String wsdl_path = "/_system/governance/trunk/wsdls/com/foo/BizService.wsdl";


    @Override
    public void init() {
        testClassName = GarFileImportServiceTestClient.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        governance = new RegistryProvider().getGovernance(registry, tenantId);
        //Delete WSDL which exists
        deleteWSDL();
    }


    @Override
    public void runSuccessCase() {
        log.info("Running WsdlUpadateContentServiceTestClient Test Cases............................ ");
        updateWsdlContent();
        log.info("Completed Running WsdlUpadateContentServiceTestClient Test Cases...................");
    }

    @Override
    public void cleanup() {
    }

    public void deleteWSDL() {
        try {
            if (registry.resourceExists("/_system/governance/trunk/wsdls")) {
                registry.delete("/_system/governance/trunk/wsdls");
            }
        } catch (RegistryException e) {
            log.error("deleteWSDL Exception thrown:" + e.getMessage());
        }
    }

    private void updateWsdlContent() {
        String wsdl_url = "http://people.wso2.com/~evanthika/wsdls/BizService.wsdl";
        WsdlManager wsdlManager = new WsdlManager(governance);
        Wsdl wsdl = null;
        try {
            wsdl = wsdlManager.newWsdl(wsdl_url);
            wsdl.addAttribute("creator", "Aaaa");
            wsdl.addAttribute("version", "0.01");
            wsdlManager.addWsdl(wsdl);
            log.info("wsdl was added");

            Resource r1 = registry.newResource();
            r1 = registry.get(wsdl_path);
            String content = new String((byte[]) r1.getContent());
            //Assert Content location
            assertTrue("Assert Content -location :", content.indexOf("http://people.wso2.com:9763/services/BizService") > 0);

            //Edit wsdl content
            OMElement contentElement = wsdl.getWsdlElement();
            OMElement addressElement = evaluateXPathToElement("//soap:address", contentElement);
            addressElement.addAttribute("location", "http://my-custom-endpoint/hoooo", null);
            wsdl.setWsdlElement(contentElement);

            // now do an update.
            wsdlManager.updateWsdl(wsdl);

            OMElement contentElement2 = wsdl.getWsdlElement();
            OMElement addressElement2 = evaluateXPathToElement("//soap:address", contentElement2);

            //create new resource
            Resource r2 = registry.newResource();
            r2 = registry.get(wsdl_path);
            String content2 = new String((byte[]) r2.getContent());

            //Assert initial value has been updated properly
            assertFalse("Assert Content wsdl file - key word 1", content2.indexOf("http://people.wso2.com:9763/services/BizService") > 0);
            assertEquals("http://my-custom-endpoint/hoooo", addressElement2.getAttributeValue(new QName("location")));

            //delete resource
            registry.delete(wsdl_path);

            //Assert resource does not exists any more
            assertFalse("WSDL Deleted", registry.resourceExists(wsdl_path));
        } catch (GovernanceException e) {
            log.error("updateWsdlContent GovernanceException  thrown:" + e.getMessage());
            Assert.fail("updateWsdlContent GovernanceException  thrown:" + e.getMessage());
        } catch (RegistryException e) {
            log.error("updateWsdlContent RegistryException  thrown:" + e.getMessage());
            Assert.fail("updateWsdlContent RegistryException  thrown:" + e.getMessage());
        } catch (org.wso2.carbon.registry.api.RegistryException e) {
            log.error("updateWsdlContent RegistryException  thrown:" + e.getMessage());
            Assert.fail("updateWsdlContent RegistryException  thrown:" + e.getMessage());
        } catch (Exception e) {
            log.error("updateWsdlContent Exception  thrown:" + e.getMessage());
            Assert.fail("updateWsdlContent Exception  thrown:" + e.getMessage());
        }
    }

    private static OMElement evaluateXPathToElement(String expression,
                                                    OMElement root) throws Exception {
        List<OMElement> nodes = GovernanceUtils.evaluateXPathToElements(expression, root);
        if (nodes == null || nodes.size() == 0) {
            return null;
        }
        return nodes.get(0);
    }
}
