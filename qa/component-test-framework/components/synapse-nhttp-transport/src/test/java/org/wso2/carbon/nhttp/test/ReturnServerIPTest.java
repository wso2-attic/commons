package org.wso2.carbon.nhttp.test;

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

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.common.test.utils.ConfigHelper;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.common.test.utils.client.StockQuoteClient;
import org.wso2.carbon.mediation.configadmin.test.commands.ConfigServiceAdminStubCommand;
import org.wso2.carbon.mediation.configadmin.ui.ConfigServiceAdminStub;

import java.io.File;

/*checking for get-property('SERVER_IP') test returning whether the correct host ip*/

/*uncomment and add the server IP for the following line in the axis2.xml file, "<parameter name="bind-address" locked="false">127.0.0.1</parameter>,
the same value should be displayed in the log to pass this test"*/

public class ReturnServerIPTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(ReturnServerIPTest.class);
    String search_IP = "MY_MACHINE_IP = 127.0.0.1";

    @Override
    public void init() {
        log.info("Initializing Server IP Message Relay Tests");
        log.debug("Server IP Message Relay Tests Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running Server IP Message Relay SuccessCase ");
        StockQuoteClient stockQuoteClient = new StockQuoteClient();

        try {
            ConfigServiceAdminStub configServiceAdminStub = new
                    ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);

            String xmlPath = frameworkPath + File.separator + "components"
                             + File.separator + "synapse-nhttp-transport" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "serverIP.xml";
            System.out.println(xmlPath);
            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(omElement);

            String trpUrl = "http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT;
            OMElement result = stockQuoteClient.stockQuoteClientforProxy(trpUrl, null, "IBM");
            log.info(result);
            System.out.println(result);

            System.out.println(result);

        }

        catch (Exception e) {
            log.error("Message Relay for Server IP Test doesn't work : " + e.getMessage());

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
