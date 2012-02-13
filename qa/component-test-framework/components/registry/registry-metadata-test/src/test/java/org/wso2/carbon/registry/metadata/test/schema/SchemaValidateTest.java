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

package org.wso2.carbon.registry.metadata.test.schema;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.registry.metadata.test.util.RegistryConsts;
import org.wso2.carbon.registry.properties.test.admin.commands.InitializePropertiesAdminCommand;
import org.wso2.carbon.registry.properties.test.admin.commands.PropertiesAdminCommand;
import org.wso2.carbon.registry.properties.ui.PropertiesAdminServiceStub;
import org.wso2.carbon.registry.properties.ui.beans.xsd.PropertiesBean;
import org.wso2.carbon.registry.properties.ui.utils.xsd.Property;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;

import java.io.File;


public class SchemaValidateTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(SchemaValidateTest.class);
    private ResourceAdminCommand resourceAdminCommand = null;
    private PropertiesAdminCommand propertiesAdminCommand;
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
        PropertiesAdminServiceStub propertiesAdminServiceStub = new InitializePropertiesAdminCommand().executeAdminStub(sessionCookie);
        resourceAdminCommand = new ResourceAdminCommand(resourceAdminServiceStub);
        propertiesAdminCommand = new PropertiesAdminCommand(propertiesAdminServiceStub);
        addValidSchemaTest();
        addInvalidSchemaTest();
        addCompressSchemaFile();
    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }

    /**
     * Check schema validation status for correctness.
     */
    private void addValidSchemaTest() {
        String resourceUrl = "http://ww2.wso2.org/~qa/greg/Patient.xsd";
        String resourceName = "Patient.xsd";

        try {
            resourceAdminCommand.importResourceSuccessCase(schemaPath + resourceName, resourceName,
                    RegistryConsts.APPLICATION_X_XSD_XML, "schemaFile", resourceUrl, null);

            assertTrue("Schema validation status incorrect", validateProperties(schemaPath + "http/ihc/org/xsd?patient/" + resourceName, "Schema Validation", "Valid"));
            assertTrue("Target namespace not found", validateProperties(schemaPath + "http/ihc/org/xsd?patient/" + resourceName, "targetNamespace", "http://ihc.org/xsd?patient"));

            String textContent = resourceAdminCommand.getTextContentSuccessCase(schemaPath +
                    "http/ihc/org/xsd?patient/" + resourceName);

            if (!textContent.equals(null)) {
                log.info("Resource successfully added to the registry and retrieved contents successfully");
            } else {
                log.error("Unable to get text content");
                Assert.fail("Unable to get text content");
            }

            //delete the added resource
            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/ihc/org/xsd?patient/" + resourceName);

            //check if the deleted file exists in registry
            if (!resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                    "http/ihc/org/xsd?patient/", resourceName)) {
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
     * Check schema properties to find out whether the schema validation status is valid or invalid
     */
    private void addInvalidSchemaTest() {
        String resourceUrl = "http://ww2.wso2.org/~charitha/xsds/calculator-no-element-name-invalid.xsd";
        String resourceName = "calculator-no-element-name-invalid.xsd";

        try {
            resourceAdminCommand.importResourceSuccessCase(schemaPath + resourceName, resourceName,
                    RegistryConsts.APPLICATION_X_XSD_XML, "schemaFile", resourceUrl, null);

            assertTrue("Schema validation status incorrect", validateProperties(schemaPath + "http/charitha/org/" +
                    resourceName, "Schema Validation", "Invalid"));
            assertTrue("Target namespace not found", validateProperties(schemaPath + "http/charitha/org/" +
                    resourceName, "targetNamespace", "http://charitha.org/"));
            assertTrue("Schema validation error not found", validateProperties(schemaPath + "http/charitha/org/" +
                    resourceName, "Schema Validation Message 1", "Error: s4s-att-must-appear: Attribute 'name' must " +
                    "appear in element 'element'."));

            String textContent = resourceAdminCommand.getTextContentSuccessCase(schemaPath +
                    "http/charitha/org/" + resourceName);

            if (!textContent.equals(null)) {
                log.info("Resource successfully added to the registry and retrieved contents successfully");
            } else {
                log.error("Unable to get text content");
                Assert.fail("Unable to get text content");
            }

            //delete the added resource
            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/charitha/org/" + resourceName);

            //check if the deleted file exists in registry
            if (!resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                    "http/charitha/org/", resourceName)) {
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
     * Add Schemas to registry using a zip file, validation status of all uploaded files checked.
     */
    private void addCompressSchemaFile() {
        String resourceName = "registry-new.zip";

        try {
            String resource = frameworkPath + File.separator + "components" + File.separator + "registry" +
                    File.separator + "registry-metadata-test" + File.separator + "src" + File.separator +
                    "test" + File.separator + "java" + File.separator + "resources" + File.separator + resourceName;

            resourceAdminCommand.addResourceSuccessCase(schemaPath + resourceName,
                     RegistryConsts.APPLICATION_WSO2_GOVERNANCE_ARCHIVE, "schemaFile", "file:///" + resource, null);
//
            assertTrue("Schema validation status incorrect", validateProperties(schemaPath +
                    "http/www/dr/dk/namespaces/schemas/application/mas/whatson/production/production.xsd",
                    "Schema Validation", "Valid"));
            assertTrue("Target namespace not found", validateProperties(schemaPath +
                    "http/www/dr/dk/namespaces/schemas/application/mas/whatson/production/production.xsd",
                    "targetNamespace", "http://www.dr.dk/namespaces/schemas/application/mas/whatson/production"));
            assertTrue("Schema validation status incorrect", validateProperties(schemaPath +
                    "http/www/dr/dk/namespaces/schemas/common/types/types.xsd",
                    "Schema Validation", "Valid"));
            assertTrue("Target namespace not found", validateProperties(schemaPath +
                    "http/www/dr/dk/namespaces/schemas/common/types/types.xsd",
                    "targetNamespace", "http://www.dr.dk/namespaces/schemas/application/mas/whatson/production"));

            String textContent = resourceAdminCommand.getTextContentSuccessCase(schemaPath +
                    "http/www/dr/dk/namespaces/schemas/common/types/types.xsd");


            if (!textContent.equals(null)) {
                log.info("Resource successfully added to the registry and retrieved contents successfully");
            } else {
                log.error("Unable to get text content");
                Assert.fail("Unable to get text content");
            }

            if (resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                    "http/www/dr/dk/namespaces/schemas/common/types", "types.xsd") &&
                    resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                            "http/www/dr/dk/namespaces/schemas/application/mas/whatson/production", "production.xsd")) {

                log.info("Resources have been uploaded to registry successfully");

            } else {
                log.error("Resources not exist in registry");
                Assert.fail("Resources not exist in registry");
            }

            //delete the added resource
            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/www/dr/dk/namespaces/schemas/common/types/types.xsd");
            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/www/dr/dk/namespaces/schemas/application/mas/whatson/production/production.xsd");

            //check if the deleted file exists in registry
            if (!(resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                    "http/www/dr/dk/namespaces/schemas/common/types", "types.xsd") &&
                    resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                            "http/www/dr/dk/namespaces/schemas/application/mas/whatson/production", "production.xsd"))) {

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


    public boolean validateProperties(String resourcePath, String key, String value) {
        boolean validationState = false;
        try {
            PropertiesBean propertiesBean = propertiesAdminCommand.getPropertiesSuccessCase(resourcePath, "yes");
            Property[] property = propertiesBean.getProperties();
            for (int i = 0; i <= property.length - 1; i++) {
                if (property[i].getKey().equalsIgnoreCase(key) && property[i].getValue().equalsIgnoreCase(value)) {
                    validationState = true;
                    log.info("Property key and value found");
                }
            }
        } catch (Exception e) {
            log.error("Error on finding resource properties : " + e);
            throw new RuntimeException("Error on finding resource properties : " + e);
        }
        return validationState;
    }
}

