package org.wso2.carbon.mediator.property.test.addToCollection;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.common.test.utils.client.StockQuoteClient;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
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
        StockQuoteClient stockQuoteClient = new StockQuoteClient();
        OMElement result = null;

        try {
            ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);

            //add a collection to the registry
            resourceAdminServiceStub.addCollection("/_system/config/", "ResFiles", "", "contains ResFiles");

            String resource = frameworkPath + File.separator + "mediators-property" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "addToCollection" + File.separator + "synapse.xml";

            resourceAdminServiceStub.addResource("/ResFiles/synapse.xml", "application/xml", "resDesc", new DataHandler(new URL("file:///" + resource)), null);
            //todo asserting

            /*   < log
           level = "custom" >
           <property xmlns:ns2 = "http://org.apache.synapse/xsd"
           xmlns:
           ns = "http://org.apache.synapse/xsd"
           name = "MY_PROPERTY"
           expression = "get-property('registry','/myCollection/@myProperty1')" / >
           </log >*/


        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Drop mediator doesn't work : " + e.getMessage());

        }


    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {
        loadDefaultConfig();
    }
}
