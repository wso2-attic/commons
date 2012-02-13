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

public class HandlerGetCollectionLocationTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(HandlerGetCollectionLocationTest.class);

    public void init() {
        log.info("Initializing HandlerGetCollectionLocationTest");
        log.debug("HandlerGetCollectionLocationTest Initialized");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase");
        String newHandlerPath = "/_system/handler/test/path/";
        String defaultHandlerPath = "/repository/components/org.wso2.carbon.governance/handlers/";
        HandlerManagementServiceStub handlerManagementServiceStub = new InitializeHandlerManagementAdminCommand().executeAdminStub(sessionCookie);
        HandlerManagementAdminCommand handlerManagementAdminCommand = new HandlerManagementAdminCommand(handlerManagementServiceStub);

        try {
            String path = handlerManagementAdminCommand.getHandlerCollectionLocationSuccessCase();
            assertTrue("Handler collection path not returned", path.equalsIgnoreCase
                    (defaultHandlerPath));

            //set handler path to new value
            handlerManagementAdminCommand.setHandlerCollectionLocationSuccessCase(newHandlerPath);
            String newPath = handlerManagementAdminCommand.getHandlerCollectionLocationSuccessCase();
            assertTrue("Updated handler collection path not returned", newPath.equalsIgnoreCase
                    (newHandlerPath));

            //set the path back to default
            handlerManagementAdminCommand.setHandlerCollectionLocationSuccessCase(defaultHandlerPath);
            String defaultPath = handlerManagementAdminCommand.getHandlerCollectionLocationSuccessCase();
            assertTrue("Updated handler collection path not returned", defaultPath.equalsIgnoreCase
                    (defaultHandlerPath));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to add handler configuration " + e);
        }
    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }
}
