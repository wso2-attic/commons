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

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.client.Axis2Client;
import org.wso2.carbon.logging.view.LogViewerStub;
import org.wso2.carbon.logging.view.test.commands.InitializeLogViewerAdmin;
import org.wso2.carbon.logging.view.test.commands.LogViewerAdminCommand;
import org.wso2.carbon.logging.view.types.carbon.LogMessage;


/*switching transports - JMS to http/s*/
public class Sample250 extends SampleTestTemplate {
    private static final Log log = LogFactory.getLog(Sample250.class);
    String searchWord = "PropertyMediator Setting property : OUT_ONLY at scope : default to : true";

    @Override
    void setSampleNo() {
        sampleNo = 250;
    }

    @Override
    protected void invokeClient() {
        try {
            clientLog = Axis2Client.fireClient("ant jmsclient -Djms_type=pox -Djms_dest=dynamicQueues/StockQuoteProxy -Djms_payload=MSFT");

            LogViewerStub logViewerStub = new InitializeLogViewerAdmin().executeAdminStub();
            LogMessage[] logMessages = new LogViewerAdminCommand(logViewerStub).addEntrySuccessCase("PropertyMediator");
            if (!logMessages[logMessages.length - 1].getLogMessage().contains(searchWord)) {
                Assert.fail("Sample 250 doesn't work");
                log.error("Sample 250 doesn't work");
            }
        }
        catch (Exception e) {
            log.error("Sample 250 doesn't work : " + e.getMessage());
        }

    }

    @Override
    protected void runSuccessTest() {
    }

    @Override
    protected void runFailureTest() {
    }


}