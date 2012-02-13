package org.wso2.carbon.eventing.eventsource.test;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.eventing.eventsource.test.commands.EventSourceAdminCommand;
import org.wso2.carbon.eventing.eventsource.test.commands.InitializeEventSourceAdminCommand;
import org.wso2.carbon.eventing.eventsource.ui.EventSourceAdminServiceStub;
import org.wso2.carbon.eventing.eventsource.ui.types.carbon.EventSourceDTO;

public class EmbeddedRegAddRemoveTests extends TestTemplate {

    private static final Log log = LogFactory.getLog(EmbeddedRegAddRemoveTests.class);

    @Override
    public void init() {
        log.info("Initializing Embedded Registry Event Source Test class ");
        log.debug("Embedded Registry Event Source Test Initialized");
    }


    @Override
    public void runSuccessCase() {
        log.debug("Embedded Registry Running SuccessCase ");
        EventSourceDTO eventSourceDTO = new EventSourceDTO();

        try {

            EventSourceAdminServiceStub eventSourceAdminServiceStub = new InitializeEventSourceAdminCommand().executeAdminStub(sessionCookie);

            eventSourceDTO.setClassName("org.apache.synapse.eventing.managers.EmbeddedRegistryBasedSubscriptionManager");
            eventSourceDTO.setName("SampleERES");
            eventSourceDTO.setTopicHeaderName("org.wso2.carbon");
            eventSourceDTO.setType("EmbeddedRegistry");
            eventSourceDTO.setTopicHeaderNS("http://ws.apache.org/ns/synapse");

            new EventSourceAdminCommand(eventSourceAdminServiceStub).addEventSourceSuccessCase(eventSourceDTO); // adding new event source
            new EventSourceAdminCommand(eventSourceAdminServiceStub).removeEventSourcesSuccessCase("SampleERES");    //removing it

        }
        catch (Exception e) {
            log.error("Embedded Registry Event source add/remove doesn't work : " + e.getMessage());
        }
    }

    @Override
    public void runFailureCase() {


    }

    @Override
    public void cleanup() {


    }
}