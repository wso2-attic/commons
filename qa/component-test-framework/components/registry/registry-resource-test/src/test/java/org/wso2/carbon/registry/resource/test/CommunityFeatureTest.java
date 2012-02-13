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
package org.wso2.carbon.registry.resource.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.registry.info.test.admin.commands.InfoAdminCommand;
import org.wso2.carbon.registry.info.test.admin.commands.InitializeInfoAdminCommand;
import org.wso2.carbon.registry.info.ui.InfoAdminServiceStub;
import org.wso2.carbon.registry.info.ui.beans.utils.xsd.Comment;
import org.wso2.carbon.registry.info.ui.beans.xsd.CommentBean;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;
import org.wso2.carbon.registry.resource.ui.beans.xsd.CollectionContentBean;

/**
 *
 */
public class CommunityFeatureTest extends TestTemplate {
    /**
     * @goal testing comment feature in registry
     */

    private static final Log log = LogFactory.getLog(DependencyTest.class);

    @Override
    public void init() {

    }

    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase");

        try {

            ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
            InfoAdminServiceStub infoAdminServiceStub = new InitializeInfoAdminCommand().executeAdminStub(sessionCookie);
            CollectionContentBean collectionContentBean = new CollectionContentBean();
            collectionContentBean = new ResourceAdminCommand(resourceAdminServiceStub).getCollectionContentSuccessCase("/");
            if (collectionContentBean.getChildCount() > 0) {
                String[] childPath = collectionContentBean.getChildPaths();
                for (int i = 0; i <= childPath.length - 1; i++) {
                    if (childPath[i].equalsIgnoreCase("/TestAutomation")) {
                        new ResourceAdminCommand(resourceAdminServiceStub).deleteResourceSuccessCase("/TestAutomation");
                    }
                }
            }
            String collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/", "TestAutomation", "", "");
            log.info("collection added to " + collectionPath);
            collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/TestAutomation", "InfoAdminTest", "", "");
            log.info("collection added to " + collectionPath);
            CommentTest(infoAdminServiceStub);

        }
        catch (Exception e) {
        }
    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }

    public void CommentTest(InfoAdminServiceStub infoAdminServiceStub) {
        try {
            InfoAdminCommand infoAdminCommand = new InfoAdminCommand(infoAdminServiceStub);
            infoAdminCommand.addCommentSuccessCase("this is sample comment", "/TestAutomation/InfoAdminTest", sessionCookie);
            infoAdminCommand.addCommentSuccessCase("this is sample comment2", "/TestAutomation/InfoAdminTest", sessionCookie);
            CommentBean commentBean = infoAdminCommand.getCommentsSuccessCase("/TestAutomation/InfoAdminTest", sessionCookie);
            Comment[] comment = commentBean.getComments();
            if (!comment[0].getDescription().equalsIgnoreCase("this is sample comment")) {
                log.error("Added comment not found");
                Assert.fail("Added comment not found");
            }
            if (!comment[1].getDescription().equalsIgnoreCase("this is sample comment2")) {
                log.error("Added comment not found");
                Assert.fail("Added comment not found");
            }
            infoAdminCommand.removeCommentSuccessCase(comment[0].getCommentPath(), sessionCookie);
            commentBean = infoAdminCommand.getCommentsSuccessCase("/TestAutomation/InfoAdminTest", sessionCookie);
            comment = commentBean.getComments();
            if (comment[0].getDescription().equalsIgnoreCase("this is sample comment")) {
                log.error("Comment not deleted");
                Assert.fail("Comment not deleted");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Exception occured while adding/getting comment :" + e.getMessage());
            Assert.fail("Exception occured while adding/getting comment :" + e.getMessage());
        }
    }
}
