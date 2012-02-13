/**
 *  Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.esb.samples;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.common.test.utils.client.Axis2Client;

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.HOST_NAME;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.HTTP_PORT;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.NIO_TRANSPORT_HTTPS;

public class Sample152 extends SampleTestTemplate {
    private static final Log log = LogFactory.getLog(Sample152.class);
    String clientLogOnHTTP = "";
    String clientLogOnHTTPS = "";

    @Override
    void setSampleNo() {
        sampleNo = 152;
    }

    @Override
    protected void invokeClient() {
        clientLogOnHTTP = Axis2Client.fireClient("ant stockquote -Dtrpurl=http://" + HOST_NAME + ":" + HTTP_PORT + "/services/StockQuoteProxy");
        clientLogOnHTTPS = Axis2Client.fireClient("ant stockquote -Dtrpurl=https://" + HOST_NAME + ":" + NIO_TRANSPORT_HTTPS + "/services/StockQuoteProxy");


    }

    @Override
    protected void runSuccessTest() {
        Assert.assertTrue(clientLogOnHTTP.contains("org.apache.axis2.AxisFault: The service cannot be found for the endpoint reference (EPR) /services/StockQuoteProxy"));
        Assert.assertTrue(clientLogOnHTTPS.contains("Standard :: Stock price ="));
        log.info("running sample152 success case");
        log.debug("running sample152 success case");
        clientLogOnHTTP = "";
        clientLogOnHTTPS = "";
    }

    @Override
    protected void runFailureTest() {
        Assert.assertFalse(clientLogOnHTTP.contains("org.apache.axis2.AxisFault: The service cannot be found for the endpoint reference (EPR) /services/StockQuoteProxy"));
        Assert.assertFalse(clientLogOnHTTPS.contains("Standard :: Stock price ="));
        log.info("running sample152 failure case");
        log.debug("running sample152 failure case");
        clientLogOnHTTP = "";
        clientLogOnHTTPS = "";
    }


}