package org.wso2.carbon.mediator.cache.test;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.common.test.utils.ConfigHelper;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.common.test.utils.client.StockQuoteClient;
import org.wso2.carbon.mediation.configadmin.test.commands.ConfigServiceAdminStubCommand;
import org.wso2.carbon.mediation.configadmin.ui.ConfigServiceAdminStub;

import java.io.File;
/*
* Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*  http://www.apache.org/licenses/LICENSE-2.0
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/

// Test Cache mediator without CacheID

// Testcase failed = https://wso2.org/jira/browse/CARBON-7758

public class WithoutCacheIDTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(WithoutCacheIDTest.class);

    @Override
    public void init() {
        log.info("Initializing Cache Mediator WithoutCacheIDTest class ");
        log.debug("Cache Mediators WithoutCacheIDTest Initialized");
    }


    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase ");
        StockQuoteClient stockQuoteClient = new StockQuoteClient();
        OMElement omElement, firstResult = null, cacheResult = null, timeoutResult = null;

        try {

            ConfigServiceAdminStub configServiceAdminStub = new
                    ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);

            String xmlPath = frameworkPath + File.separator + "mediators-cache"
                             + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "WithoutCacheID.xml";
            omElement = ConfigHelper.createOMElement(xmlPath);

            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(omElement);
            log.debug("With Cache ID synapse xml uploaded ");

            if (FrameworkSettings.STRATOS.equalsIgnoreCase("false")) {
                firstResult = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT, null, "IBM");
                Thread.sleep(2000);
                cacheResult = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT, null, "IBM");
                Thread.sleep(6000);
                timeoutResult = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT, null, "IBM");
            }
            if (FrameworkSettings.STRATOS.equalsIgnoreCase("true")) {
                firstResult = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT + "/services/" + FrameworkSettings.TENANT_NAME + "/", null, "IBM");
                Thread.sleep(2000);
                cacheResult = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT + "/services/" + FrameworkSettings.TENANT_NAME + "/", null, "IBM");
                Thread.sleep(6000);
                timeoutResult = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT + "/services/" + FrameworkSettings.TENANT_NAME + "/", null, "IBM");
            }


            if (firstResult.toString().equals(cacheResult.toString())) {
                if (cacheResult.toString().equals(timeoutResult.toString())) {
                    log.error("Cache Mediator WithoutCacheIDTest does not work");
                    Assert.fail("Cache Mediator WithoutCacheIDTest does not work");
                }
                log.error("Cache Mediator WithoutCacheIDTest does not work");
                Assert.fail("Cache Mediator WithoutCacheIDTest does not work");
            }
            log.debug("before cache response : " + firstResult.toString());
            log.debug("first cache response : " + cacheResult.toString());
            log.debug("After cache timeout response : " + timeoutResult.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Cache mediator WithoutCacheIDTest doesn't work : " + e.getMessage());

        }

    }

    @Override
    public void runFailureCase() {
        log.debug("Cache mediator failure case fired.");

    }

    @Override
    public void cleanup() {
        log.debug("cache mediator cleanup case fired");
        loadDefaultConfig();
    }
}
