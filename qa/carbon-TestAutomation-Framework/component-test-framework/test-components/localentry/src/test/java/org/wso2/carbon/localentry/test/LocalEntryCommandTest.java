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
import org.wso2.carbon.localentry.ui.types.ConfigurationObject;
import org.wso2.carbon.localentry.ui.types.LocalEntryAdminServiceStub;

public class LocalEntryCommandTest extends TestTemplate{

    private static final Log log = LogFactory.getLog(LocalEntryCommandTest.class);

    @Override
    public void init() {
        log.info("Initializing LocalEntryCommandTest class ");
        log.debug("LocalEntryCommand Test Initialized");
    }
    @Override
    public void runSuccessCase() {
        try{
            boolean methodStatus;
            LocalEntryAdminServiceStub localEntryAdminServiceStub = new InitializeLocalEntryAdminCommand().executeAdminStub(sessionCookie);
            // adding new local entry
            methodStatus = new LocalEntryCommand(localEntryAdminServiceStub).addEntrySuccessCase("<localEntry xmlns=\"http://ws.apache.org/ns/synapse\" key=\"TestEntry\">This is test entry for check admin service</localEntry>");
            methodHandler(methodStatus);
            // 
            //getting number of local entries available
            int entryCount  = new LocalEntryCommand(localEntryAdminServiceStub).getEntryDataCountSuccessCase();
            if(entryCount == 0){
              log.error("EntryDataCount didn't work");
              Assert.fail("EntryDataCount didn't work");
            }

            ConfigurationObject[] configurationObject = new LocalEntryCommand(localEntryAdminServiceStub).getDependantSuccessCase("TestEntry");
            String [] entryName = new LocalEntryCommand(localEntryAdminServiceStub).getEntryNameSuccessCase();

           }
        catch (Exception e)
        {
           e.printStackTrace();
        }
    }

     @Override
    public void runFailureCase() {
         
     }
     @Override
    public void cleanup() {
     loadDefaultConfig();
    }

    private void methodHandler(boolean methodStatus)
    {
        if(!methodStatus){
            log.error("admin service failed");
        }
    }
}
