package org.wso2.carbon.eventing.eventsource.test;

import junit.framework.Assert;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.ConfigHelper;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.eventing.eventsource.test.commands.EventSourceAdminCommand;
import org.wso2.carbon.eventing.eventsource.test.commands.InitializeEventSourceAdminCommand;
import org.wso2.carbon.eventing.eventsource.ui.EventSourceAdminServiceStub;
import org.wso2.carbon.eventing.eventsource.ui.types.carbon.EventSourceDTO;
import org.wso2.carbon.mediation.configadmin.test.commands.ConfigServiceAdminStubCommand;
import org.wso2.carbon.mediation.configadmin.ui.ConfigServiceAdminStub;

import java.io.File;
/* Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*  http://www.apache.org/licenses/LICENSE-2.0
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/

public class DeleteEventSourceTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(CreateEventSourceTest.class);

    @Override
    public void init() {
        log.info("Initializing Event Source Test class ");
        log.debug("Event Source Test Initialized");
    }


    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase ");
        EventSourceDTO eventSourceDTO = new EventSourceDTO();

        try {

            EventSourceAdminServiceStub eventSourceAdminServiceStub = new InitializeEventSourceAdminCommand().executeAdminStub(sessionCookie);
            ConfigServiceAdminStub configServiceAdminStub = new
                    ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);

            String xmlPath = frameworkPath + File.separator + "event-sources"
                    + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "CreateSource.xml";
            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(omElement);

            EventSourceAdminCommand eventSourceAdminCommand = new EventSourceAdminCommand(eventSourceAdminServiceStub);

            eventSourceDTO.setClassName("org.apache.synapse,eventing.managers.DefaultInMemorySubscriptionManager");
            eventSourceDTO.setName("TestEventSource");
            eventSourceDTO.setTopicHeaderName("org.wso2.carbon");
            eventSourceDTO.setType("DefaultInMemory");

            boolean result = eventSourceAdminCommand.addEventSourceSuccessCase(eventSourceDTO); // adding new event source
            if(!result){
             Assert.fail("Add event source failed");
             log.error("Add event source failed");
            }
            result = eventSourceAdminCommand.removeEventSourcesSuccessCase("TestEventSource");
            if(!result){
             Assert.fail("Remove event source failed");
             log.error("Remove event source failed");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Event source remove doesn't work : " + e.getMessage());
        }
    }

    @Override
    public void runFailureCase() {


    }

    @Override
    public void cleanup() {
      loadDefaultConfig();
    }
}
