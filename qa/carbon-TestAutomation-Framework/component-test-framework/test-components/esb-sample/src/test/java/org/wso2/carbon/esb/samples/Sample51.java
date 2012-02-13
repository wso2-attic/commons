/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

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

package org.wso2.carbon.esb.samples;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.common.test.utils.client.Axis2Client;

//Endpoint samples

public class Sample51 extends SampleTestTemplate {
    private static final Log log = LogFactory.getLog(Sample51.class);

    String clientLogMtom;
    String clientLogSwa;

    @Override
    void setSampleNo() {
        sampleNo = 51;
    }

    @Override
    protected void invokeClient() {
        clientLogMtom = Axis2Client.fireClient("ant optimizeclient -Dopt_mode=mtom");
        clientLogSwa = Axis2Client.fireClient("ant optimizeclient -Dopt_mode=swa");

    }

    @Override
    protected void runSuccessTest() {
        // test mtom
        Assert.assertTrue(clientLogMtom.contains("Saved response to file :"));
        Assert.assertTrue(clientLogMtom.contains("mtom-"));

        // test swa
        Assert.assertTrue(clientLogSwa.contains("Saved response to file :"));
        Assert.assertTrue(clientLogSwa.contains("swa-"));
        log.info("running sample51 success case");
        log.debug("running sample51 success case");

        clientLogMtom = "";
        clientLogSwa = "";
    }

    @Override
    protected void runFailureTest() {
        // test mtom
        Assert.assertFalse(clientLogMtom.contains("Saved response to file :"));
        Assert.assertFalse(clientLogMtom.contains("mtom-"));

        // test swa
        Assert.assertFalse(clientLogSwa.contains("Saved response to file :"));
        Assert.assertFalse(clientLogSwa.contains("swa-"));
        log.info("running sample51 failure case");
        log.debug("running sample51 failure case");
        clientLogMtom = "";
        clientLogSwa = "";
    }

    public void runFailureCase() {
        if (!isWindows()) {
            super.runFailureCase();
        }
    }
}
