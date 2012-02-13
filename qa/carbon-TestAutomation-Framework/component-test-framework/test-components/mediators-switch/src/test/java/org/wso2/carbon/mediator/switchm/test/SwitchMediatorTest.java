package org.wso2.carbon.mediator.switchm.test;

/*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

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

import junit.framework.Assert;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.common.test.utils.ConfigHelper;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.common.test.utils.client.StockQuoteClient;
import org.wso2.carbon.logging.view.LogViewerStub;
import org.wso2.carbon.logging.view.test.commands.InitializeLogViewerAdmin;
import org.wso2.carbon.logging.view.test.commands.LogViewerAdminCommand;
import org.wso2.carbon.logging.view.types.carbon.LogMessage;
import org.wso2.carbon.mediation.configadmin.test.commands.ConfigServiceAdminStubCommand;
import org.wso2.carbon.mediation.configadmin.ui.ConfigServiceAdminStub;

import java.io.File;
import java.util.Calendar;

public class SwitchMediatorTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(SwitchMediatorTest.class);
    String searchWord = "<ns:symbol>IBM</ns:symbol>";

    @Override
    public void init() {
        log.info("Initializing switch mediator Tests");
        log.debug("switch mediator Tests Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running switch mediator SuccessCase ");
        Calendar cal = Calendar.getInstance();


        StockQuoteClient stockQuoteClient = new StockQuoteClient();
        OMElement result = null;
        LogViewerStub logViewerStub = new InitializeLogViewerAdmin().executeAdminStub();
        try {
            ConfigServiceAdminStub configServiceAdminStub = new
                    ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);


            String xmlPath = frameworkPath + File.separator + "mediators-switch"
                             + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "switch.xml";
            System.out.println(xmlPath);
            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(omElement);


            if (FrameworkSettings.STRATOS.equalsIgnoreCase("false")) {
                result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT, null, "IBM");
            } else if (FrameworkSettings.STRATOS.equalsIgnoreCase("true")) {
                result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT + "/services/" + FrameworkSettings.TENANT_NAME + "/", null, "IBM");
            }
            log.info(result);

        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("switch mediator doesn't work : " + e.getMessage());

        }


        try {
            LogMessage[] logMessages = new LogViewerAdminCommand(logViewerStub).addEntrySuccessCase("mediator");
            if (!logMessages[logMessages.length - 1].getLogMessage().contains(searchWord)) {
                log.error("Switch Mediator doesn't work");
                Assert.fail("Switch Mediator doesn't work");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
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
