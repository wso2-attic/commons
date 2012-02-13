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

package org.wso2.carbon.mediator.log.test;

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

public class LogMediatorFullLevelTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(LogMediatorFullLevelTest.class);

    String search_l1 = "request# inComing = ***Incoming Message***#";
    String search_l2 = "inExpression = Echo String - urn:getQuote";
    String search_l3 = "response# outgoing = ***Outgoing Message***#";

    @Override
    public void init() {
        log.info("Initializing Full Level Log Mediator Tests");
        log.debug("Log Mediator Tests Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running Full Level Log Mediator SuccessCase ");
        OMElement result = null;
        boolean isFound_search_l1 = false;
        boolean isFound_search_l2 = false;
        boolean isFound_search_l3 = false;


        StockQuoteClient stockQuoteClient = new StockQuoteClient();

        try {
            ConfigServiceAdminStub configServiceAdminStub = new
                    ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);
            LogViewerStub logViewerStub = new InitializeLogViewerAdmin().executeAdminStub();

            String xmlPath = frameworkPath + File.separator + "components" + File.separator + "mediators-log"
                             + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "full" + File.separator + "synapse.xml";
            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(omElement);

            /*Sending a StockQuoteClient request*/
            if (FrameworkSettings.STRATOS.equalsIgnoreCase("false")) {
                result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT, null, "IBM");
            } else if (FrameworkSettings.STRATOS.equalsIgnoreCase("true")) {
                result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT + "/services/" + FrameworkSettings.TENANT_NAME + "/", null, "IBM");
            }

            log.info(result);

            if (!result.toString().contains("IBM")) {
                Assert.fail();
                log.error("Full Level Log Mediation Failed");

            }
            LogMessage[] logMessages = new LogViewerAdminCommand(logViewerStub).addEntrySuccessCase("LogMediator");
            int removeMessageLength = 0;
            if (logMessages.length > 4) {
                removeMessageLength = logMessages.length - 3;
            } else {
                removeMessageLength = 0;
            }
            for (int i = removeMessageLength; i <= logMessages.length - 1; i++) {
                System.out.println(logMessages[i].getLogMessage());
                if (logMessages[i].getLogMessage().contains(search_l1)) {
                    isFound_search_l1 = true;
                }
                if (logMessages[i].getLogMessage().contains(search_l2)) {
                    isFound_search_l2 = true;
                }
                if (logMessages[i].getLogMessage().contains(search_l3)) {
                    isFound_search_l3 = true;
                }
            }
            assertTrue("request$ inComing = ***Incoming Message***# not found in log message", isFound_search_l1);
            assertTrue("inExpression = Echo String - urn:getQuote not found in log message", isFound_search_l2);
            assertTrue("response$ outgoing = ***Outgoing Message*** not found in log message", isFound_search_l3);

        }
        catch (Exception e) {
            log.error("Full Level Log Mediator doesn't work : " + e.getMessage());

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
