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

//Advanced Mediation Sample
public class Sample371 extends SampleTestTemplate {
    private static final Log log = LogFactory.getLog(Sample371.class);
    String clientLog1;
    String clientLog2;
    String clientLog3;
    String clientLog4;
    String clientLog5;

    @Override
    void setSampleNo() {
        sampleNo = 371;
    }

    @Override
    protected void invokeClient() {
//          clientLog = Axis2Client.fireClient("ant stockquote -Dsymbol=IBM -Dmode=quote -Daddurl=http://localhost:8280/");

        clientLog1 = Axis2Client.fireClient("ant stockquote -Dsymbol=IBM -Dmode=quote -Daddurl=http://" + HOST_NAME + ":" + HTTP_PORT + "/");
        clientLog2 = Axis2Client.fireClient("ant stockquote -Dsymbol=IBM -Dmode=quote -Daddurl=http://" + HOST_NAME + ":" + HTTP_PORT + "/");
        clientLog3 = Axis2Client.fireClient("ant stockquote -Dsymbol=IBM -Dmode=quote -Daddurl=http://" + HOST_NAME + ":" + HTTP_PORT + "/");
        clientLog4 = Axis2Client.fireClient("ant stockquote -Dsymbol=IBM -Dmode=quote -Daddurl=http://" + HOST_NAME + ":" + HTTP_PORT + "/");


    }

    @Override
    protected void runSuccessTest() {
        Assert.assertTrue(clientLog1.contains("Standard :: Stock price"));
        Assert.assertFalse(clientLog1.contains("axis2.AxisFault:"));
        Assert.assertTrue(clientLog2.contains("Standard :: Stock price"));
        Assert.assertFalse(clientLog2.contains("axis2.AxisFault:"));
        Assert.assertTrue(clientLog3.contains("Standard :: Stock price"));
        Assert.assertFalse(clientLog3.contains("axis2.AxisFault:"));
        Assert.assertTrue(clientLog4.contains("Standard :: Stock price"));
        Assert.assertFalse(clientLog4.contains("axis2.AxisFault:"));
        clientLog5 = Axis2Client.fireClient("ant stockquote -Dsymbol=IBM -Dmode=quote -Daddurl=http://" + HOST_NAME + ":" + HTTP_PORT + "/");
        Assert.assertTrue(clientLog5.contains("axis2.AxisFault: **Access Denied**"));
        Assert.assertFalse(clientLog5.contains("Standard :: Stock price"));
        log.info("running sample371 success case");
        log.debug("running sample371 success case");
        String clientLog1 = "";
        String clientLog2 = "";
        String clientLog3 = "";
        String clientLog4 = "";
        String clientLog5 = "";

    }

    @Override
    protected void runFailureTest() {
        Assert.assertFalse(clientLog1.contains("Standard :: Stock price"));
        Assert.assertTrue(clientLog1.contains("axis2.AxisFault:"));
        Assert.assertFalse(clientLog2.contains("Standard :: Stock price"));
        Assert.assertTrue(clientLog2.contains("axis2.AxisFault:"));
        Assert.assertFalse(clientLog3.contains("Standard :: Stock price"));
        Assert.assertTrue(clientLog3.contains("axis2.AxisFault:"));
        Assert.assertFalse(clientLog4.contains("Standard :: Stock price"));
        Assert.assertTrue(clientLog4.contains("axis2.AxisFault:"));
        clientLog5 = Axis2Client.fireClient("ant stockquote -Dsymbol=IBM -Dmode=quote -Daddurl=http://" + HOST_NAME + ":" + HTTP_PORT + "/");
        Assert.assertFalse(clientLog5.contains("axis2.AxisFault: **Access Denied**"));
        Assert.assertTrue(clientLog5.contains("axis2.AxisFault"));
        Assert.assertFalse(clientLog5.contains("Standard :: Stock price"));
        log.info("running sample371 failure case");
        log.debug("running sample371 failure case");
        String clientLog1 = "";
        String clientLog2 = "";
        String clientLog3 = "";
        String clientLog4 = "";
        String clientLog5 = "";
    }

}