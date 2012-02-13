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

package org.wso2.carbon.registry.metadata.test.service;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.governance.services.test.admin.commands.AddServiceAdminCommand;
import org.wso2.carbon.governance.services.test.admin.commands.InitializeAddServiceAdminCommand;
import org.wso2.carbon.governance.services.ui.AddServicesServiceStub;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;

import java.io.File;

public class ServiceEditTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(ServiceEditTest.class);
    private AddServiceAdminCommand addServiceAdminCommand = null;
    private ResourceAdminCommand resourceAdminCommand = null;
    private String servicePath = "/_system/governance/services/";
    private String wsdlPath = "/_system/governance/wsdls/";
    private String schemaPath = "/_system/governance/schemas/";

    @Override
    public void init() {
        log.info("Initializing Edit Service Resource Tests");
        log.debug("Add Service Resource Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase");
        AddServicesServiceStub addServicesServiceStub = new InitializeAddServiceAdminCommand().executeAdminStub(sessionCookie);
        addServiceAdminCommand = new AddServiceAdminCommand(addServicesServiceStub);
        ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
        resourceAdminCommand = new ResourceAdminCommand(resourceAdminServiceStub);

        String resourceName = "SimpleStockQuote.xml";
        String resourceNameUpdated = "SimpleStockQuote-updated.xml";
        String serviceName = "SimpleStockQuoteService";
        String wsdlName = serviceName + ".wsdl";
        String wsdlNamespacePath = "http/services/samples/";

        String resource = frameworkPath + File.separator + "components" + File.separator + "registry" +
                File.separator + "registry-metadata-test" + File.separator + "src" + File.separator +
                "test" + File.separator + "java" + File.separator + "resources" + File.separator + resourceName;

        addServiceAdminCommand.addServiceSuccessCase(ServiceAddTest.fileReader(resource));

        try {

            String textContent = resourceAdminCommand.getTextContentSuccessCase(servicePath +
                    wsdlNamespacePath + serviceName);

            if (textContent.indexOf("http://services.samples") != -1) {
                log.info("service content found");

            } else {
                log.error("service content not found");
                Assert.fail("service content not found");
            }

            String resourceUpdated = frameworkPath + File.separator + "components" + File.separator + "registry" +
                    File.separator + "registry-metadata-test" + File.separator + "src" + File.separator +
                    "test" + File.separator + "java" + File.separator + "resources" + File.separator + resourceNameUpdated;

            addServiceAdminCommand.addServiceSuccessCase(ServiceAddTest.fileReader(resourceUpdated));

            String textContentUpdated = resourceAdminCommand.getTextContentSuccessCase(servicePath +
                    wsdlNamespacePath + serviceName);

            if (textContentUpdated.indexOf("SimpleStockQuoteService Description Updated") != -1) {
                log.info("service content found");

            } else {
                log.error("service content not found");
                Assert.fail("service content not found");
            }

            //delete the added resource
            resourceAdminCommand.deleteResourceSuccessCase(servicePath +
                    wsdlNamespacePath + serviceName);

            //check if the deleted file exists in registry
            if (!resourceAdminCommand.isResourceExist(sessionCookie, servicePath +
                    wsdlNamespacePath, serviceName)) {
                log.info("Resource successfully deleted from the registry");

            } else {
                log.error("Resource not deleted from the registry");
                Assert.fail("Resource not deleted from the registry");
            }
        }
        catch (Exception e) {
            Assert.fail("Unable to get text content " + e);
            log.error(" : " + e.getMessage());
        }
    }

    @Override
    public void runFailureCase() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cleanup() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
