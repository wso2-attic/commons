package org.wso2.carbon.localentry.test;

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

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.localentry.test.commands.InitializeLocalEntryAdminCommand;
import org.wso2.carbon.localentry.test.commands.LocalEntryCommand;
import org.wso2.carbon.localentry.ui.types.LocalEntryAdminServiceStub;

public class LocalEntrywithEmptyValue extends TestTemplate {

    /**
     * Normally it does not allow to create a localEntry with an empty body
     * but through an admin-service invocation, localEntry with an empty body will be created an error page from UI side therefore this test fails.
     */

    private static final Log log = LogFactory.getLog(LocalEntrywithEmptyValue.class);

    @Override
    public void init() {
        log.info("Initializing In-linedText Add LocalEntry Command Test class ");
        log.debug("Add In-linedText LocalEntry Command Test Initialized");
    }

    @Override
    public void runSuccessCase() {
        try {
            boolean methodStatus;

            LocalEntryAdminServiceStub localEntryAdminServiceStub = new InitializeLocalEntryAdminCommand().executeAdminStub(sessionCookie);
            String entryNames_before = new LocalEntryCommand(localEntryAdminServiceStub).getEntryNamesStringSuccessCase();


            // adding new local entry
            if (!(entryNames_before.matches("TestEntryText"))) {
                new LocalEntryCommand(localEntryAdminServiceStub).deleteEntryKeySuccessCase("WrongEntry");
                methodStatus = new LocalEntryCommand(localEntryAdminServiceStub).addEntrySuccessCase("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                                                                                     "<localEntry xmlns=\"http://ws.apache.org/ns/synapse\" key=\"WrongEntry\"></localEntry>");
                methodHandler(methodStatus);

            }
            String entryNames = new LocalEntryCommand(localEntryAdminServiceStub).getEntryNamesStringSuccessCase();
            System.out.println(entryNames);

            //getting localEntry names
            if ((entryNames.contains("[Entry]-WrongEntry"))) {
                log.error("LocalEntry has been added: Test Failed");
                Assert.fail("LocalEntry has been added: Test Failed");
            } else {
                log.info("Value field cannot be empty: Test passed");
                System.out.println("Value field cannot be empty: Test passed");

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
