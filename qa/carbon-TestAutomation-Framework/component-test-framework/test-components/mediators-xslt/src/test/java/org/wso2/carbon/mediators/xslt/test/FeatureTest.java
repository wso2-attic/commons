/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/

package org.wso2.carbon.mediators.xslt.test;

import junit.framework.Assert;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.ConfigHelper;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.common.test.utils.client.StockQuoteClient;
import org.wso2.carbon.mediation.configadmin.test.commands.ConfigServiceAdminStubCommand;
import org.wso2.carbon.mediation.configadmin.ui.ConfigServiceAdminStub;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;

import javax.activation.DataHandler;
import java.io.File;
import java.net.URL;
import java.util.Calendar;

public class FeatureTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(DefaultXpathTest.class);

    @Override
    public void init() {
        log.info("Initializing XSLT FeatureTest class ");
        log.debug("XSLT FeatureTest Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase ");
               StockQuoteClient stockQuoteClient = new StockQuoteClient();
               try {

                   ConfigServiceAdminStub configServiceAdminStub = new
                           ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);

                   String xmlPath = frameworkPath + File.separator + "mediators-xslt"
                           + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "feature.xml";
                   OMElement omElement = ConfigHelper.createOMElement(xmlPath);

                   uploadResources();

                   new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(omElement);

                   Client client = new Client();
                   OMElement responseElement = client.InvokeClient();
                   if (!responseElement.toString().contains("Hi-Response")) {
                       log.error("Transformed response not found.");
                       Assert.fail("Transformed response not found.");
                   } else {
                       log.info(responseElement.toString());
                   }
               }
               catch (Exception e) {
                   e.printStackTrace();
                   log.error("XSLT mediator doesn't work : " + e.getMessage());

               }
       
    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {
     loadDefaultConfig();
    }

    private void uploadResources() throws Exception {
        ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
        new ResourceAdminCommand(resourceAdminServiceStub).deleteCollectionSuccessCase("/_system/config/XSLTFiles");
        new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/_system/config/", "XSLTFiles", "", "contain test XSLT files");


        String xsltFile = frameworkPath + File.separator + "mediators-xslt" + File.separator + "src" + File.separator + "test" + File.separator + "resources";

        new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/_system/config/XSLTFiles/echo_back.xslt", "application/xml", "xslt files", new DataHandler(new URL("file:///" + xsltFile + File.separator + "echo_back.xslt")), null);
        Thread.sleep(1000);
        new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/_system/config/XSLTFiles/echo_transform.xslt", "application/xml", "xslt files", new DataHandler(new URL("file:///" + xsltFile + File.separator + "echo_transform.xslt")), null);


    }
}
