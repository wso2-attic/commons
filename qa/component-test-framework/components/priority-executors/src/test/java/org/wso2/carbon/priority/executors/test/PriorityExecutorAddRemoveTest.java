package org.wso2.carbon.priority.executors.test;

import junit.framework.Assert;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.priority.executors.ui.PriorityMediationAdminStub;
import org.wso2.carbon.priority.test.commands.InitializePriorityExecutorsAdminCommand;
import org.wso2.carbon.priority.test.commands.PriorityExecutorsAdminCommand;

/*
 * Copyright 2004,2005 The Apache Software Foundation.                         
 *                                                                             
 * Licensed under the Apache License, Version 2.0 (the "License");             
 * you may not use this file except in compliance with the License.            
 * You may obtain a copy of the License at                                     
 *                                                                             
 *      http://www.apache.org/licenses/LICENSE-2.0                             
 *                                                                             
 * Unless required by applicable law or agreed to in writing, software         
 * distributed under the License is distributed on an "AS IS" BASIS,           
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and         
 * limitations under the License.                                              
 */

public class PriorityExecutorAddRemoveTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(PriorityExecutorAddRemoveTest.class);

    @Override
    public void init() {
        log.info("Initializing Add Priority Executor Tests");
        log.debug("Add Priority Executor Test Initialised");

    }

    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase");

        try {

            PriorityMediationAdminStub priorityMediationAdminStub = new InitializePriorityExecutorsAdminCommand().executeAdminStub(sessionCookie);

            String priorityExConfig = "<priority-executor xmlns=\"http://ws.apache.org/ns/synapse\" name=\"ex\">\n" +
                                      "   <queues>\n" +
                                      "      <queue size=\"34\" priority=\"2\" />\n" +
                                      "      <queue size=\"23\" priority=\"1\" />\n" +
                                      "   </queues>\n" +
                                      "   <threads max=\"100\" core=\"20\" keep-alive=\"5\" />\n" +
                                      "</priority-executor>";

            OMElement omElement = AXIOMUtil.stringToOM(priorityExConfig);
            new PriorityExecutorsAdminCommand(priorityMediationAdminStub).addSuccessCase("TestExecutor", omElement);

            OMElement result = new PriorityExecutorsAdminCommand(priorityMediationAdminStub).getExecutorSuccessCase("TestExecutor");

            if (result != null) {
                log.info("Priority Executor Added Successfully");
                System.out.println("Priority Executor Added Successfully: " + result);
            } else {
                Assert.fail("Priority Executor Adding Failed: cannot find priority executor with the name - TestExecutor");
                log.error("Priority Executor Adding Failed: cannot find priority executor with the name - TestExecutor");

            }
            new PriorityExecutorsAdminCommand(priorityMediationAdminStub).removeSuccessCase("TestExecutor");
        }
        catch (Exception e) {
            Assert.fail("Unable to Add or Remove Priority Executors: " + e);
            log.error(" : " + e.getMessage());

        }


    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }
}
