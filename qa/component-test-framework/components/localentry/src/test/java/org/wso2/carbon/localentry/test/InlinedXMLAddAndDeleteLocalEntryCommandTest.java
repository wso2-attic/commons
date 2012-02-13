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


package org.wso2.carbon.localentry.test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.localentry.test.commands.InitializeLocalEntryAdminCommand;
import org.wso2.carbon.localentry.test.commands.LocalEntryCommand;
import org.wso2.carbon.localentry.ui.types.LocalEntryAdminServiceStub;

public class InlinedXMLAddAndDeleteLocalEntryCommandTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(InlinedXMLAddAndDeleteLocalEntryCommandTest.class);
    int entryCount_before, entryCount_after = 0;

    @Override
    public void init() {
        log.info("Initializing In-linedXML Add LocalEntry Command Test class ");
        log.debug("In-linedXML Add LocalEntry Command Test Initialized");
    }

    @Override
    public void runSuccessCase() {
        try {
            boolean methodStatus;

            LocalEntryAdminServiceStub localEntryAdminServiceStub = new InitializeLocalEntryAdminCommand().executeAdminStub(sessionCookie);
            String entryNames_before = new LocalEntryCommand(localEntryAdminServiceStub).getEntryNamesStringSuccessCase();

            // adding new local entry
            if (!(entryNames_before.matches("XMLTestLocalEntry"))) {
                new LocalEntryCommand(localEntryAdminServiceStub).deleteEntryKeySuccessCase("XMLTestLocalEntry");
                entryCount_before = new LocalEntryCommand(localEntryAdminServiceStub).getEntryDataCountSuccessCase();
                methodStatus = new LocalEntryCommand(localEntryAdminServiceStub).addEntrySuccessCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                                                                                     "<localEntry xmlns=\"http://ws.apache.org/ns/synapse\" key=\"XMLTestLocalEntry\">\n" +
                                                                                                     "    <endpoint name=\"SimpleStockQuoteService\">\n" +
                                                                                                     "        <address uri=\"http://localhost:9000/services/SimpleStockQuoteService\">\n" +
                                                                                                     "            <suspendOnFailure>\n" +
                                                                                                     "                <progressionFactor>1.0</progressionFactor>\n" +
                                                                                                     "            </suspendOnFailure>\n" +
                                                                                                     "            <markForSuspension>\n" +
                                                                                                     "                <retriesBeforeSuspension>0</retriesBeforeSuspension>\n" +
                                                                                                     "                <retryDelay>0</retryDelay>\n" +
                                                                                                     "            </markForSuspension>\n" +
                                                                                                     "        </address>\n" +
                                                                                                     "    </endpoint>\n" +
                                                                                                     "</localEntry>");
                entryCount_after = new LocalEntryCommand(localEntryAdminServiceStub).getEntryDataCountSuccessCase();
                methodHandler(methodStatus);

            }

            //getting number of local entries available
            if (!(entryCount_after - entryCount_before == 1)) {
                log.error("In-linedXML LocalEntry has not been added successfully");
                Assert.fail("In-linedXML LocalEntry has not been added successfully");
            } else {
                log.info("LocalEntry added successfully");
                System.out.println("In-linedXML LocalEntry added successfully");
                new LocalEntryCommand(localEntryAdminServiceStub).deleteEntryKeySuccessCase("XMLTestLocalEntry");
                entryCount_after = new LocalEntryCommand(localEntryAdminServiceStub).getEntryDataCountSuccessCase();

                if (entryCount_after == entryCount_before) {
                    System.out.println("In-linedXML LocalEntry deleted successfully");
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Exception thrown " + e);
        }
    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }

    private void methodHandler(boolean methodStatus) {
        if (!methodStatus) {
            log.error("admin service failed");
        }
    }
}
