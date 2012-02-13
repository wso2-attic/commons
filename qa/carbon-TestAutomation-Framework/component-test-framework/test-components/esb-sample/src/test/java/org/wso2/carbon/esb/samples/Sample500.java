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


public class Sample500 extends SampleTestTemplate {
    private static final Log log = LogFactory.getLog(Sample500.class);
    String clientLog1;
    String clientLog2;
    String clientLog3;
    String clientLog4;

    @Override
    void setSampleNo() {
        sampleNo = 500;
    }

    @Override
    protected void invokeClient() {

        /*Invoke the client (Subscriber)*/
        clientLog = Axis2Client.fireClient("ant eventsubscriber");

        /*Invoke the client (Sender) */
        clientLog1 = Axis2Client.fireClient("ant eventsender");

        /*Invoke the client (Un-Subscribe)*/
        clientLog2 = Axis2Client.fireClient("ant eventsubscriber -Dmode=unsubscribe -Didentifier=urn:uuid:6DFDF649A67416BFCC1228112473802909001-111373032");

        /*Invoke the client (Re-New)*/
        clientLog3 = Axis2Client.fireClient("ant eventsubscriber -Dmode=renew -Didentifier=urn:uuid:6DFDF649A67416BFCC1228112473802909001-111373032 -Dexpires=2009-12-31T21:07:00.000-08:00");

        /*Invoke the client (GetStatus) */
        clientLog4 = Axis2Client.fireClient("ant eventsubscriber -Dmode=getstatus -Didentifier=urn:uuid:6DFDF649A67416BFCC1228112473802909001-111373032");


    }

    @Override
    protected void runSuccessTest() {
        /*Invoke the client (Subscriber)*/
        Assert.assertTrue(clientLog.contains("Subscribing"));

        /*Invoke the client (Sender) */
        Assert.assertTrue(clientLog1.contains("Sending Event :"));

        /*Invoke the client (Un-Subscribe)*/
        Assert.assertTrue(clientLog2.contains("UnSubscribing"));

        /*Invoke the client (Re-New)*/
        Assert.assertTrue(clientLog3.contains("SynapseSubscription Renew"));

        /*Invoke the client (GetStatus) */
        Assert.assertTrue(clientLog4.contains("GetStatus using"));
        log.info("running sample500 success case");
        log.debug("running sample500 success case");

        clientLog = "";
        clientLog1 = "";
        clientLog2 = "";
        clientLog3 = "";
        clientLog4 = "";

    }

    @Override
    protected void runFailureTest() {


    }
}
