package org.wso2.carbon.mediator.property.test.addToCollection;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.mediation.configadmin.test.commands.ConfigServiceAdminStubCommand;
import org.wso2.carbon.mediation.configadmin.ui.ConfigServiceAdminStub;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;

import javax.activation.DataHandler;
import java.io.File;
import java.net.URL;
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

public class AddToCollectionTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(AddToCollectionTest.class);

    @Override
    public void init() {
        log.info("Initializing Add to Collection Test class ");
        log.debug("Add to Collection Test Initialised");

    }

    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase");

        try {

            ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
            //add a collection to the registry
            String collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/_system/config/", "ResFiles", "", "contains ResFiles");
            log.info("collection added to " + collectionPath);
            // Changing media type
            collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/_system/config/", "ResFiles", "application/vnd.wso2.esb", "application/vnd.wso2.esb media type collection");
            String resource = frameworkPath + File.separator + "components" + File.separator + "mediators-property" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "addToCollection" + File.separator + "synapse.xml";

            new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/ResFiles/synapse.xml", "application/xml", "resDesc", "file:///" + resource, null);

            String textContent = new ResourceAdminCommand(resourceAdminServiceStub).getTextContentSuccessCase("/ResFiles/synapse.xml");

            if (textContent.equals(null)) {
                log.error("Unable to get text content");
                Assert.fail("Unable to get text content");
            } else {
                System.out.println("Resource successfully added to the registry and retrieved contents successfully");
            }
            new ResourceAdminCommand(resourceAdminServiceStub).deleteResourceSuccessCase("/ResFiles/synapse.xml");

            if (!textContent.equals(null)) {
                System.out.println("Resource successfully deleted from the registry");

            } else {
                log.error("Unable to delete the resource from the registry");
                Assert.fail("Unable to delete the resource from the registry");
            }

        }
        catch (Exception e) {
            Assert.fail("Unable to get text content " + e);
            log.error(" : " + e.getMessage());

        }


    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {
        ConfigServiceAdminStub configServiceAdminStub = new
                ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);
        new ConfigServiceAdminStubCommand(configServiceAdminStub).loadDefaultConfig();

    }
}
