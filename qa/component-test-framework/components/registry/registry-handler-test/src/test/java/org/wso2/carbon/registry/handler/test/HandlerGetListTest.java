/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/

package org.wso2.carbon.registry.handler.test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.registry.handler.test.admin.commands.HandlerManagementAdminCommand;
import org.wso2.carbon.registry.handler.test.admin.commands.InitializeHandlerManagementAdminCommand;
import org.wso2.carbon.registry.handler.ui.HandlerManagementServiceStub;

import java.io.File;

public class HandlerGetListTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(HandlerGetListTest.class);


    @Override
    public void init() {
        log.info("Initializing HandlerGetListTest");
        log.debug("HandlerGetListTest Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase");
        String sampleHandlerName = "sample-handler.xml";
        String handlerName = "org.wso2.carbon.registry.extensions.handlers.ServiceMediaTypeHandler";
        HandlerManagementServiceStub handlerManagementServiceStub = new InitializeHandlerManagementAdminCommand().executeAdminStub(sessionCookie);
        HandlerManagementAdminCommand handlerManagementAdminCommand = new HandlerManagementAdminCommand(handlerManagementServiceStub);

        String handlerResource = frameworkPath + File.separator + "components" + File.separator + "registry" +
                File.separator + "registry-handler-test" + File.separator + "src" + File.separator +
                "test" + File.separator + "java" + File.separator + "resources" + File.separator + sampleHandlerName;
        try {
            handlerManagementAdminCommand.createHandlerSuccessCase(HandlerAddTest.fileReader(handlerResource));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to add handler configuration " + e);
        }

        try {
            String[] handlerList = handlerManagementAdminCommand.getHandlerListSuccessCase();
            boolean match = false;
            for (String list : handlerList) {
                if (list.equalsIgnoreCase(handlerName)) {
                    log.info("handler " + handlerName + "available");
                    match = true;
                }
            }

            if (!match) {
                log.info("handler " + handlerName + "not found");
                Assert.fail("Handler not found in the list");
            }

            try {
                handlerManagementAdminCommand.deleteHandlerSuccessCase(handlerName);
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail("Failed to delete the handler" + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void runFailureCase() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cleanup() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
