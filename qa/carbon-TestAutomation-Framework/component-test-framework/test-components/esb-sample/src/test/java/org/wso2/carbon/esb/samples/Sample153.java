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

public class Sample153 extends SampleTestTemplate {
    private static final Log log = LogFactory.getLog(Sample153.class);


    @Override
    void setSampleNo() {
        sampleNo = 153;
    }

    @Override
    protected void invokeClient() {
        clientLog = Axis2Client.fireClient("ant stockquote -Dtrpurl=http://" + HOST_NAME + ":" + HTTP_PORT + "/services/StockQuoteProxy -Dpolicy=./../../repository/samples/resources/policy/client_policy_3.xml");
        // ant stockquote -Dtrpurl=http://localhost:8280/services/StockQuoteProxy -Dpolicy=./../../repository/samples/resources/policy/client_policy_3.xml");

    }

    @Override
    protected void runSuccessTest() {
        Assert.assertTrue(clientLog.contains("Standard :: Stock price ="));
        log.info("running sample153 success case");
        log.debug("running sample153 success case");
    }

    @Override
    protected void runFailureTest() {
        Assert.assertFalse(clientLog.contains("Standard :: Stock price ="));
        log.info("running sample153 failure case");
        log.debug("running sample153 failure case");
    }


}