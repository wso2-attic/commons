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

package org.wso2.carbon.nhttp.test;

import junit.framework.Assert;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.ConfigHelper;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.common.test.utils.client.Axis2Client;
import org.wso2.carbon.mediation.configadmin.test.commands.ConfigServiceAdminStubCommand;
import org.wso2.carbon.mediation.configadmin.ui.ConfigServiceAdminStub;

import java.io.File;

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.HOST_NAME;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.HTTP_PORT;

public class URLCaseSensitivityTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(URLCaseSensitivityTest.class);
    String clientLog = "";

    @Override
    public void init() {
        log.info("Initializing URLCaseSensitivity Tests");
        log.debug("URLCaseSensitivity Tests Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running URLCaseSensitivity SuccessCase ");

        try {
            ConfigServiceAdminStub configServiceAdminStub = new
                    ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);

            String xmlPath = frameworkPath + File.separator + "components"
                    + File.separator + "synapse-nhttp-transport" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "url_case_sensitivity.xml";
            System.out.println(xmlPath);
            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(omElement);

            clientLog = Axis2Client.fireClient("ant stockquote -Dtrpurl=http://" + HOST_NAME + ":" + HTTP_PORT + "/services/SimpleStockQuoteService?symbol=IBM");

            System.out.println(clientLog);

            Assert.assertTrue(clientLog.contains("Standard :: Stock price"));
            log.info("Running URLCaseSensitivity success case");
            log.debug("Running URLCaseSensitivity success case");
            clientLog = "";
        }

        catch (Exception e) {
            Assert.fail("URLCaseSensitivity Test Failed : " + e);
            log.error("URLCaseSensitivity Test Failed : " + e.getMessage());
        }
    }


    @Override
    public void runFailureCase() {
        Assert.assertFalse(clientLog.contains("Standard :: Stock price"));
        log.info("Running URLCaseSensitivity failure case");
        log.debug("Running URLCaseSensitivity  failure case");
    }

    @Override
    public void cleanup() {
        ConfigServiceAdminStub configServiceAdminStub = new
                ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);
        new ConfigServiceAdminStubCommand(configServiceAdminStub).loadDefaultConfig();
    }
}
