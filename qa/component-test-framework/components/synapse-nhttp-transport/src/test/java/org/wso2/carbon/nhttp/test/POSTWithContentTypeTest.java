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

import junit.framework.Assert;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*POST request to an endpoint which returns the response with content type application/x-www-form-urlencoded*/

public class POSTWithContentTypeTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(POSTWithContentTypeTest.class);

    @Override
    public void init() {
        log.info("Initializing POST WithContentType Tests");
        log.debug("POST WithContentType Tests Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running POST WithContentType SuccessCase ");
        StockQuoteClient stockQuoteClient = new StockQuoteClient();
        OMElement result = null;

        try {
            ConfigServiceAdminStub configServiceAdminStub = new
                    ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);

            ///home/thilini/Desktop/new/component-test-framework/components/synapse-nhttp-transport/src/test/resources
            String xmlPath = frameworkPath + File.separator + "components" + File.separator + "synapse-nhttp-transport"
                             + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "with_content_type_post.xml";
            System.out.println(xmlPath);
            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(omElement);

            String trpUrl = null;
            if (FrameworkSettings.STRATOS.equalsIgnoreCase("false")) {
                trpUrl = "http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT;
                result = stockQuoteClient.stockQuoteClientforProxy(trpUrl, null, "IBM");
            } else if (FrameworkSettings.STRATOS.equalsIgnoreCase("true")) {
                result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + "/services/" + FrameworkSettings.TENANT_NAME + "/", null, "IBM");
            }
            log.info(result);
            System.out.println(result);

            /*Test HTTP Header Contents*/

            URL url = new URL(trpUrl);
            String contentType = ((HttpURLConnection) url.openConnection())
                    .getContentType();
            System.out.println("********** " + contentType + "********** ");

            if (!contentType.contains("application/xml; charset=UTF-8")) {

                Assert.fail("POST with Content Type Test Failed");
                log.error("POST with Content Type Test Failed");
            }

        }
        catch (MalformedURLException
                e1) {
            log.error("POST WithContentType Test doesn't work : " + e1.getMessage());


        }
        catch (Exception
                e) {
            log.error("POST WithContentType Test doesn't work : " + e.getMessage());

        }

    }


    @Override
    public void runFailureCase
            () {
    }

    @Override
    public void cleanup() {
        ConfigServiceAdminStub configServiceAdminStub = new
                ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);
        new ConfigServiceAdminStubCommand(configServiceAdminStub).loadDefaultConfig();
    }
}
