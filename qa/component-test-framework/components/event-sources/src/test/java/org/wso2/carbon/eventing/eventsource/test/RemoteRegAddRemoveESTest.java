package org.wso2.carbon.eventing.eventsource.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.eventing.eventsource.test.commands.EventSourceAdminCommand;
import org.wso2.carbon.eventing.eventsource.test.commands.InitializeEventSourceAdminCommand;
import org.wso2.carbon.eventing.eventsource.ui.EventSourceAdminServiceStub;
import org.wso2.carbon.eventing.eventsource.ui.types.carbon.EventSourceDTO;


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
public class RemoteRegAddRemoveESTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(RemoteRegAddRemoveESTest.class);

    @Override
    public void init() {
        log.info("Initializing Remote Registry Event Source Test class ");
        log.debug("Remote Registry Event Source Test Initialized");
    }


    @Override
    public void runSuccessCase() {
        log.debug("Running Remote Registry SuccessCase ");
        EventSourceDTO eventSourceDTO = new EventSourceDTO();

        try {

            EventSourceAdminServiceStub eventSourceAdminServiceStub = new InitializeEventSourceAdminCommand().executeAdminStub(sessionCookie);

            eventSourceDTO.setClassName("org.wso2.carbon.mediation.registry.WSO2Registry");
            eventSourceDTO.setName("SampleRRES");
            eventSourceDTO.setTopicHeaderName("org.wso2.carbon");
            eventSourceDTO.setType("RemoteRegistry");
            eventSourceDTO.setTopicHeaderNS("http://ws.apache.org/ns/synapse");
            eventSourceDTO.setRegistryUrl("/_system/config");
            eventSourceDTO.setUsername("admin");
            eventSourceDTO.setPassword("admin");

            new EventSourceAdminCommand(eventSourceAdminServiceStub).addEventSourceSuccessCase(eventSourceDTO); // adding new event source
            new EventSourceAdminCommand(eventSourceAdminServiceStub).removeEventSourcesSuccessCase("SampleRRES");    //removing it

        }
        catch (Exception e) {
            log.error("Remote Registry Event source add/remove doesn't work : " + e.getMessage());
        }
    }

    @Override
    public void runFailureCase() {


    }

    @Override
    public void cleanup() {


    }
}