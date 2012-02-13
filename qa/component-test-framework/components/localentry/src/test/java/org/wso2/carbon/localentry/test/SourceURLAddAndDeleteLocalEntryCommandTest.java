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

public class SourceURLAddAndDeleteLocalEntryCommandTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(SourceURLAddAndDeleteLocalEntryCommandTest.class);
    int entryCount_before, entryCount_after = 0;

    @Override
    public void init() {
        log.info("Initializing SourceURL Add LocalEntry Command Test class ");
        log.debug("SourceURL Add LocalEntry Command Test Initialized");
    }

    @Override
    public void runSuccessCase() {
        try {
            boolean methodStatus;

            LocalEntryAdminServiceStub localEntryAdminServiceStub = new InitializeLocalEntryAdminCommand().executeAdminStub(sessionCookie);
            String entryNames_before = new LocalEntryCommand(localEntryAdminServiceStub).getEntryNamesStringSuccessCase();

            // adding new local entry
            if (!((entryNames_before.toString().matches("SourceURLTestEntry")) || (entryNames_before.toString().matches("SampleTestEntry")))) {
                methodStatus = new LocalEntryCommand(localEntryAdminServiceStub).deleteEntryKeySuccessCase("SourceURLTestEntry");
                methodStatus = new LocalEntryCommand(localEntryAdminServiceStub).deleteEntryKeySuccessCase("SampleTestEntry");
                entryCount_before = new LocalEntryCommand(localEntryAdminServiceStub).getEntryDataCountSuccessCase();
                methodStatus = new LocalEntryCommand(localEntryAdminServiceStub).addEntrySuccessCase("<localEntry xmlns=\"http://ws.apache.org/ns/synapse\" key=\"SampleTestEntry\">This is test entry</localEntry>");

                /* This is valid for esb 3.1.0 and later releases as it has been changed the source url path to "repository/deployment/server/synapse-configs/default/local-entries"*/
                methodStatus = new LocalEntryCommand(localEntryAdminServiceStub).addEntrySuccessCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                                                                                     "<localEntry xmlns=\"http://ws.apache.org/ns/synapse\" key=\"SourceURLTestEntry\" src=\"file:repository/deployment/server/synapse-configs/default/local-entries/SampleTestEntry.xml\"/>");
                methodHandler(methodStatus);
                entryCount_after = new LocalEntryCommand(localEntryAdminServiceStub).getEntryDataCountSuccessCase();


            }

            //getting number of local entries available
            if (!(entryCount_after - entryCount_before == 2)) {
                log.error("SourceURL LocalEntry has not been added successfully");
                Assert.fail("SourceURL LocalEntry has not been added successfully");
            } else {
                log.info("SourceURL LocalEntry added successfully");
                System.out.println("SourceURL LocalEntry added successfully");

            }

            methodStatus = new LocalEntryCommand(localEntryAdminServiceStub).deleteEntryKeySuccessCase("SourceURLTestEntry");
            entryCount_after = new LocalEntryCommand(localEntryAdminServiceStub).getEntryDataCountSuccessCase();

            if (entryCount_after == entryCount_before + 1) {
                System.out.println("SourceURL LocalEntry deleted successfully");
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
