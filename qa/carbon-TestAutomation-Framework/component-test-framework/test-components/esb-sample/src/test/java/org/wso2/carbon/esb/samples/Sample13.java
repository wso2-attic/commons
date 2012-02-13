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
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.BACKENDSERVER_HTTP_PORT;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.HTTP_PORT;

// this is running with sample configuration 0
//Message Mediation Sample

public class Sample13 extends SampleTestTemplate {

    private static final Log log = LogFactory.getLog(Sample13.class);


    @Override
    void setSampleNo() {
        sampleNo = 0;
    }

    @Override
    protected void invokeClient() {
        // clientLog = Axis2Client.fireClient("ant stockquote -Daddurl=http://localhost:9000/services/SimpleStockQuoteService -Dtrpurl=http://localhost:8280/ -Dmode=dualquote ",40000);
        clientLog = Axis2Client.fireClient("ant stockquote -Daddurl=http://" + HOST_NAME + ":" + BACKENDSERVER_HTTP_PORT + "/services/SimpleStockQuoteService -Dtrpurl=http://" + HOST_NAME + ":" + HTTP_PORT + "/ -Dmode=dualquote", 10000);
    }

    @Override
    protected void runSuccessTest() {
        Assert.assertTrue(clientLog.contains("Standard dual channel :: Stock price"));
        log.info("running sample13 success case");
        log.debug("running sample13 success case");
    }

    @Override
    protected void runFailureTest() {
        Assert.assertFalse(clientLog.contains("Standard dual channel :: Stock price"));
        log.info("running sample13 failure case");
        log.debug("running sample13 failure case");
    }

    public void runFailureCase() {
        if (!isWindows()) {
            super.runFailureCase();
        }
    }

}
