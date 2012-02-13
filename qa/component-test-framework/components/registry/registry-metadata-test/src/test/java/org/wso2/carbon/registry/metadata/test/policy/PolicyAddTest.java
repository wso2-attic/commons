/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/

package org.wso2.carbon.registry.metadata.test.policy;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.registry.metadata.test.util.RegistryConsts;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;

import java.io.File;

/**
 * Add Policy from File System and URL Functionality Tests
 */

public class PolicyAddTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(PolicyAddTest.class);
    private ResourceAdminCommand resourceAdminCommand = null;
    private String policyPath = "/_system/governance/policies/";

    @Override
    public void init() {
        log.info("Initializing Add/Update Policy Registry Tests");
        log.debug("Add/Update Policy Registry Test Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase");
        ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
        resourceAdminCommand = new ResourceAdminCommand(resourceAdminServiceStub);
        addPolicyFromFile();     // Add policy file from the file system
        updatePolicyFromFile();
        addPolicyFromURL();    // Add policy file from import URL functionality
        updatePolicyFromURL();
    }

    /**
     * Add policy file from the file system
     */
    private void addPolicyFromFile() {
        String resourceName = "sample_policy.xml";

        try {
            String resource = frameworkPath + File.separator + "components" + File.separator + "registry" +
                              File.separator + "registry-metadata-test" + File.separator + "src" + File.separator +
                              "test" + File.separator + "java" + File.separator + "resources" + File.separator + resourceName;

            resourceAdminCommand.addResourceSuccessCase(policyPath + resourceName,
                                                        RegistryConsts.POLICY_XML, "testPolicy", "file:///" + resource, null);

            String textContent = resourceAdminCommand.getTextContentSuccessCase(policyPath + resourceName);

            if (!textContent.equals(null)) {
                log.info("Resource successfully added to the registry and retrieved contents successfully");
            } else {
                log.error("Unable to get text content");
                Assert.fail("Unable to get text content");
            }
        }
        catch (Exception e) {
            Assert.fail("Unable to get file content: " + e);
            log.error("Unable to get file content: " + e.getMessage());
        }
    }

    /**
     * Add policy file from import URL functionality
     */
    private void addPolicyFromURL() {
        String resourceUrl = "http://ww2.wso2.org/~charitha/policy/RMpolicy3.xml";
        String resourceName = "RMpolicy3.xml";

        try {
            resourceAdminCommand.importResourceSuccessCase(policyPath + resourceName, resourceName,
                                                           RegistryConsts.POLICY_XML, "SamplePolicyFile", resourceUrl, null);

            String textContent = resourceAdminCommand.getTextContentSuccessCase(policyPath + resourceName);

            if (!textContent.equals(null)) {
                log.info("Policy File adding and content retrieving was successful");
            } else {
                log.error("Unable to retrieve policy file content");
                Assert.fail("Unable to retrieve policy file content");
            }
        }
        catch (Exception e) {
            Assert.fail("Unable to get file content: " + e);
            log.error("Unable to get file content : " + e.getMessage());
        }
    }

    private void updatePolicyFromFile() {

        String resourceName = "sample_policy.xml";
        String resContent = "<?xml version=\"1.0\"?>\n" +
                            "<wsp:Policy\n" +
                            "        xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\">\n" +
                            "    <wsrmp:RMAssertion\n" +
                            "            xmlns:wsrmp=\"http://docs.oasis-open.org/ws-rx/wsrmp/200702\">\n" +
                            "        <wsrmp:DeliveryAssurance>\n" +
                            "            <wsp:Policy>\n" +
                            "                <wsrmp:ExactlyOnce/>\n" +
                            "            </wsp:Policy>\n" +
                            "        </wsrmp:DeliveryAssurance>\n" +
                            "    </wsrmp:RMAssertion>\n" +
                            "</wsp:Policy>"; //to update

        try {

            /**
             *  update policy and check the content
             */
            resourceAdminCommand.updateTextContentSuccessCase(policyPath + resourceName, resContent);

            if (resourceAdminCommand.getTextContentSuccessCase(policyPath + resourceName).contains("RMAssertion")) {
                log.info("Policy file successfully updated");
            } else {
                log.error("Policy File has not been updated in the registry");
                Assert.fail("Policy File has not been updated in the registry");
            }

        }
        catch (Exception e) {
            Assert.fail("Unable to get file content: " + e);
            log.error("Unable to get file content: " + e.getMessage());
        }

    }

    private void updatePolicyFromURL() {
        String resourceName = "RMpolicy3.xml";
        String resContent = "<?xml version=\"1.0\"?>\n" +
                            "\n" +
                            "<wsp:Policy \n" +
                            "  xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\">\n" +
                            "  <wsrmp:RMAssertion \n" +
                            "    xmlns:wsrmp=\"http://docs.oasis-open.org/ws-rx/wsrmp/200702\"> \n" +
                            "    <wsrmp:DeliveryAssurance> \n" +
                            "      <wsp:Policy> \n" +
                            "        <wsrmp:ExactlyOnce /> \n" +
                            "      </wsp:Policy> \n" +
                            "    </wsrmp:DeliveryAssurance> \n" +
                            "  </wsrmp:RMAssertion> \n" +
                            "</wsp:Policy>";

        try {

            /**
             *  update policy and check the content
             */
            resourceAdminCommand.updateTextContentSuccessCase(policyPath + resourceName, resContent);

            if (!resourceAdminCommand.getTextContentSuccessCase(policyPath + resourceName).contains("InactivityTimeout")) {
                log.info("Policy file successfully updated");
            } else {
                log.error("Policy File has not been updated in the registry");
                Assert.fail("Policy File has not been updated in the registry");
            }

        } catch (
                Exception e
                )

        {
            Assert.fail("Unable to get file content: " + e);
            log.error("Unable to get file content : " + e.getMessage());
        }

    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }
}
