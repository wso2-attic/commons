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

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.*;

//dblookup mediator sample
public class Sample360 extends SampleTestTemplate {
    private static final Log log = LogFactory.getLog(Sample360.class);
    String clientLog1 = "";
    String clientLog2 = "";

    @Override
    void setSampleNo() {
        sampleNo = 360;
    }

    @Override
    protected void invokeClient() {
        //When the IBM stock quote is requested,
        clientLog = Axis2Client.fireClient("ant stockquote -Daddurl=http://" + HOST_NAME + ":" + BACKENDSERVER_HTTP_PORT + "/services/SimpleStockQuoteService -Dtrpurl=http://" + HOST_NAME + ":" + HTTP_PORT + "/ -Dsymbol=IBM");

        // For the SUN stock quote,
        clientLog1 = Axis2Client.fireClient("ant stockquote -Daddurl=http://" + HOST_NAME + ":" + BACKENDSERVER_HTTP_PORT + "/services/SimpleStockQuoteService -Dtrpurl=http://" + HOST_NAME + ":" + HTTP_PORT + "/ -Dsymbol=SUN");

        //For the MSFT stock quote,
        clientLog2 = Axis2Client.fireClient("ant stockquote -Daddurl=http://" + HOST_NAME + ":" + BACKENDSERVER_HTTP_PORT + "/services/SimpleStockQuoteService -Dtrpurl=http://" + HOST_NAME + ":" + HTTP_PORT + "/ -Dsymbol=MSFT");

    }

    @Override
    protected void runSuccessTest() {
        Assert.assertTrue(clientLog.contains("Standard :: Stock price"));
        Assert.assertTrue(clientLog1.contains("Standard :: Stock price"));
        Assert.assertTrue(clientLog2.contains("Standard :: Stock price"));
        log.info("running sample360 success case");
        log.debug("running sample360 success case");
        clientLog1 = "";
        clientLog2 = "";
    }

    @Override
    protected void runFailureTest() {
        Assert.assertFalse(clientLog.contains("Standard :: Stock price"));
        Assert.assertFalse(clientLog1.contains("Standard :: Stock price"));
        Assert.assertFalse(clientLog2.contains("Standard :: Stock price"));
        log.info("running sample360 failure case");
        log.debug("running sample360 failure case");
        clientLog1 = "";
        clientLog2 = "";
    }
}
