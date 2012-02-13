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

package org.wso2.carbon.registry.metadata.test.schema;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.registry.metadata.test.util.RegistryConsts;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;

import java.io.File;

public class SchemaAddTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(SchemaAddTest.class);
    private ResourceAdminCommand resourceAdminCommand = null;
    private String schemaPath = "/_system/governance/schemas/";


    @Override
    public void init() {
        log.info("Initializing Add Schema Resource Tests");
        log.debug("Add Add Schema Resource Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase");
        ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
        resourceAdminCommand = new ResourceAdminCommand(resourceAdminServiceStub);
        addSchema();
        addSchemafromURL();
        addSchemaFromGar();
        updateSchemaTest();
        updateSchemaFromURL();
        addSchemaMultipleImports();
    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }

    /**
     * Add schema files from fie system to registry
     */
    private void addSchema() {
        String resourceName = "calculator.xsd";

        try {
            String resource = frameworkPath + File.separator + "components" + File.separator + "registry" +
                    File.separator + "registry-metadata-test" + File.separator + "src" + File.separator +
                    "test" + File.separator + "java" + File.separator + "resources" + File.separator + resourceName;

            resourceAdminCommand.addResourceSuccessCase(schemaPath + resourceName,
                    RegistryConsts.APPLICATION_X_XSD_XML, "schemaFile", "file:///" + resource, null);

            String textContent = resourceAdminCommand.getTextContentSuccessCase(schemaPath +
                    "http/charitha/org1899988/" + resourceName);

            if (!textContent.equals(null)) {
                log.info("Resource successfully added to the registry and retrieved contents successfully");
            } else {
                log.error("Unable to get text content");
                Assert.fail("Unable to get text content");
            }

            //delete the added resource
            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/charitha/org1899988/" + resourceName);

            //check if the deleted file exists in registry
            if (!resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                    "http/charitha/org1899988/", resourceName)) {
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

    /**
     * Add schema file from URL
     */
    private void addSchemafromURL() {
        String resourceUrl = "http://ww2.wso2.org/~qa/greg/simpleXsd1.xsd";
        String resourceName = "simpleXsd1.xsd";

        try {
            resourceAdminCommand.importResourceSuccessCase(schemaPath + resourceName, resourceName,
                    RegistryConsts.APPLICATION_X_XSD_XML, "schemaFile", resourceUrl, null);

            String textContent = resourceAdminCommand.getTextContentSuccessCase(schemaPath +
                    "http/services/samples/xsd/" + resourceName);

            if (!textContent.equals(null)) {
                log.info("Resource successfully added to the registry and retrieved contents successfully");
            } else {
                log.error("Unable to get text content");
                Assert.fail("Unable to get text content");
            }

            //delete the added resource
            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/services/samples/xsd/" + resourceName);

            //check if the deleted file exists in registry
            if (!resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                    "http/services/samples/xsd/", resourceName)) {
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

    /**
     * upload a governance archive file containing four xsds
     */
    private void addSchemaFromGar() {
        String resourceName = "xsdAll.gar";

        try {
            String resource = frameworkPath + File.separator + "components" + File.separator + "registry" +
                    File.separator + "registry-metadata-test" + File.separator + "src" + File.separator +
                    "test" + File.separator + "java" + File.separator + "resources" + File.separator + resourceName;

            resourceAdminCommand.addResourceSuccessCase(schemaPath + resourceName,
                    RegistryConsts.APPLICATION_WSO2_GOVERNANCE_ARCHIVE, "schemaFile", "file:///" + resource, null);

            String textContent = resourceAdminCommand.getTextContentSuccessCase(schemaPath +
                    "http/schemas/microsoft/com/2003/10/Serialization/test2.xsd");

            if (!textContent.equals(null)) {
                log.info("Resource successfully added to the registry and retrieved contents successfully");
            } else {
                log.error("Unable to get text content");
                Assert.fail("Unable to get text content");
            }

            if (resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                    "http/schemas/microsoft/com/2003/10/Serialization", "test2.xsd") &&
                    resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                            "http/schemas/datacontract/org/2004/07/System", "test1.xsd") &&
                    resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                            "http/tempuri/org", "test3.xsd") &&
                    resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                            "http/schemas/microsoft/com/2003/10/Serialization", "test4.xsd")) {

                log.info("Resources have been uploaded to registry successfully");

            } else {
                log.error("Resources not exist in registry");
                Assert.fail("Resources not exist in registry");
            }

            //delete the added resource
            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/schemas/microsoft/com/2003/10/Serialization/test2.xsd");
            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/schemas/datacontract/org/2004/07/System/test1.xsd");
            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/tempuri/org/test3.xsd");
            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/schemas/microsoft/com/2003/10/Serialization/test4.xsd");

            //check if the deleted file exists in registry
            if (!(resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                    "http/schemas/microsoft/com/2003/10/Serialization", "test2.xsd") &&
                    resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                            "http/schemas/datacontract/org/2004/07/System", "test1.xsd") &&
                    resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                            "http/tempuri/org", "test3.xsd") &&
                    resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                            "http/schemas/microsoft/com/2003/10/Serialization", "test4.xsd"))) {

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

    private void updateSchemaTest() {
        String resourceName = "calculator.xsd";
        String updatedResourceName = "calculator-updated.xsd";

        try {
            String resource = frameworkPath + File.separator + "components" + File.separator + "registry" +
                    File.separator + "registry-metadata-test" + File.separator + "src" + File.separator +
                    "test" + File.separator + "java" + File.separator + "resources" + File.separator + resourceName;

            resourceAdminCommand.addResourceSuccessCase(schemaPath + resourceName,
                    RegistryConsts.APPLICATION_X_XSD_XML, "schemaFile", "file:///" + resource, null);

            String textContent = resourceAdminCommand.getTextContentSuccessCase(schemaPath +
                    "http/charitha/org1899988/" + resourceName);

            if (textContent.indexOf("xmlns:tns=\"http://charitha.org/\"") != -1) {
                log.info("Schema added successfully");

            } else {
                log.error("Schema content not found");
                Assert.fail("Schema content not found");
            }

            String resourceUpdated = frameworkPath + File.separator + "components" + File.separator + "registry" +
                    File.separator + "registry-metadata-test" + File.separator + "src" + File.separator +
                    "test" + File.separator + "java" + File.separator + "resources" + File.separator + updatedResourceName;

            resourceAdminCommand.addResourceSuccessCase(schemaPath + resourceName,
                    RegistryConsts.APPLICATION_X_XSD_XML, "schemaFile", "file:///" + resourceUpdated, null);

            String textContentUpdated = resourceAdminCommand.getTextContentSuccessCase(schemaPath +
                    "http/charitha/org1899988/" + resourceName);

            if (textContentUpdated.indexOf("xmlns:tns=\"http://charitha.org.updated/\"") != -1) {
                log.info("Schema Updated successfully");

            } else {
                log.error("Schema has not been updated");
                Assert.fail("Schema has not been updated");
            }

            //delete the added resource
            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/charitha/org1899988/" + resourceName);

            //check if the deleted file exists in registry
            if (!resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                    "http/charitha/org1899988/", resourceName)) {
                log.info("Resource successfully deleted from the registry");

            } else {
                log.error("Resource not deleted from registry");
                Assert.fail("Resource not deleted from registry");
            }
        }
        catch (Exception e) {
            Assert.fail("Unable to get text content " + e);
            log.error(" : " + e.getMessage());
        }
    }

    /**
     * Update schema file from URL
     */
    private void updateSchemaFromURL() {
        String resourceUrl = "http://ww2.wso2.org/~qa/greg/calculator-new.xsd";
        String resourceName = "calculator-new.xsd";
        String updatedResourceUrl = "http://ww2.wso2.org/~qa/greg/calculator-new-updated.xsd";
        String updatedResourceName = "calculator-new-updated.xsd\"";

        try {
            resourceAdminCommand.importResourceSuccessCase(schemaPath, resourceName, RegistryConsts.APPLICATION_X_XSD_XML,
                    "Import Schema from URL", resourceUrl, null);

            String textContent = resourceAdminCommand.getTextContentSuccessCase(schemaPath +
                    "http/charitha/org1/" + resourceName);

            if (textContent.indexOf("xmlns:tns=\"http://charitha.org/\"") != -1) {
                log.info("Schema added successfully");

            } else {
                log.error("Schema content not found");
                Assert.fail("Schema content not found");
            }


            resourceAdminCommand.importResourceSuccessCase(schemaPath, resourceName, RegistryConsts.APPLICATION_X_XSD_XML,
                    "Update Schema from URL", updatedResourceUrl, null);

            String textContentUpdated = resourceAdminCommand.getTextContentSuccessCase(schemaPath +
                    "http/charitha/org1/" + resourceName);

            if (textContentUpdated.indexOf("xmlns:tns=\"http://charitha.org.updated/\"") != -1) {
                log.info("Schema Updated successfully");

            } else {
                log.error("Schema has not been updated");
                Assert.fail("Schema has not been updated");
            }

            //delete the added resource
            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/charitha/org1/" + resourceName);

            //check if the deleted file exists in registry
            if (!resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                    "http/charitha/org1/", resourceName)) {
                log.info("Resource successfully deleted from the registry");

            } else {
                log.error("Resource not deleted from registry");
                Assert.fail("Resource not deleted from registry");
            }
        }
        catch (Exception e) {
            Assert.fail("Unable to get text content " + e);
            log.error(" : " + e.getMessage());
        }
    }

    /**
     * Add a schema which imports another schema
     */
    private void addSchemaMultipleImports() {
        String resourceUrl = "http://ww2.wso2.org/~qa/greg/calculator.xsd";
        String resourceName = "calculator.xsd";
        String referenceSchemaFile = "calculator-no-element-name-invalid.xsd";

        try {
            resourceAdminCommand.importResourceSuccessCase(schemaPath + resourceName, resourceName,
                    RegistryConsts.APPLICATION_X_XSD_XML, "schemaFile", resourceUrl, null);

            String textContent = resourceAdminCommand.getTextContentSuccessCase(schemaPath +
                    "http/charitha/org/" + resourceName);

            if (textContent.indexOf("xmlns:tns=\"http://charitha.org/\"") != -1) {
                log.info("Schema content found");

            } else {
                log.error("Schema content not found");
                Assert.fail("Schema content not found");
            }

            String textContentImportedSchema = resourceAdminCommand.getTextContentSuccessCase(schemaPath +
                    "http/charitha/org1/" + referenceSchemaFile);

            if (textContentImportedSchema.indexOf("xmlns:tns=\"http://charitha.org/\"") != -1) {
                log.info("Schema content found");

            } else {
                log.error("Schema content not found");
                Assert.fail("Schema content not found");
            }

            //delete the added resource
            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/charitha/org/" + resourceName);

            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/charitha/org1/" + referenceSchemaFile);

            //check if the deleted file exists in registry
            if (!resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                    "http/charitha/org/", resourceName)) {
                log.info("Resource successfully deleted from the registry");

            } else {
                log.error("Resource not deleted from the registry");
                Assert.fail("Resource not deleted from the registry");
            }

            if (!resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                    "http/charitha/org1/", referenceSchemaFile)) {
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

}
