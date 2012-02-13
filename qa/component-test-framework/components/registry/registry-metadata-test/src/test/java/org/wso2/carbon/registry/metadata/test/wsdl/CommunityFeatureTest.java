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
package org.wso2.carbon.registry.metadata.test.wsdl;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.governance.custom.lifecycles.checklist.test.admin.commands.CustomLifecyclesChecklistAdminCommand;
import org.wso2.carbon.governance.custom.lifecycles.checklist.test.admin.commands.InitializeCustomLifecyclesChecklistAdminCommand;
import org.wso2.carbon.governance.custom.lifecycles.checklist.ui.CustomLifecyclesChecklistAdminServiceStub;
import org.wso2.carbon.governance.custom.lifecycles.checklist.ui.beans.xsd.LifecycleBean;
import org.wso2.carbon.governance.custom.lifecycles.checklist.ui.util.xsd.LifecycleActions;
import org.wso2.carbon.registry.info.test.admin.commands.InfoAdminCommand;
import org.wso2.carbon.registry.info.test.admin.commands.InitializeInfoAdminCommand;
import org.wso2.carbon.registry.info.ui.InfoAdminServiceStub;
import org.wso2.carbon.registry.info.ui.beans.utils.xsd.Comment;
import org.wso2.carbon.registry.info.ui.beans.utils.xsd.Tag;
import org.wso2.carbon.registry.info.ui.beans.xsd.CommentBean;
import org.wso2.carbon.registry.info.ui.beans.xsd.RatingBean;
import org.wso2.carbon.registry.info.ui.beans.xsd.TagBean;
import org.wso2.carbon.registry.relations.test.admin.commands.InitializeRelationsAdminCommand;
import org.wso2.carbon.registry.relations.test.admin.commands.RelationsAdminCommand;
import org.wso2.carbon.registry.relations.ui.RelationAdminServiceStub;
import org.wso2.carbon.registry.relations.ui.beans.xsd.AssociationTreeBean;


/**
 * This class used to add WSDL files in to the governance registry using resource-admin command.
 */
public class CommunityFeatureTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(CommunityFeatureTest.class);
    RelationsAdminCommand relationsAdminCommand;
    InfoAdminCommand infoAdminCommand;
    CustomLifecyclesChecklistAdminCommand customLifecyclesChecklistAdminCommand;


    @Override
    public void init() {
    }

    /**
     * runSuccessCase having two different of test-cases.adding wsdl from local file system and adding wsdl from global url.
     */
    @Override
    public void runSuccessCase() {
        RelationAdminServiceStub relationAdminServiceStub = new InitializeRelationsAdminCommand().executeAdminStub(sessionCookie);
        InfoAdminServiceStub infoAdminServiceStub = new InitializeInfoAdminCommand().executeAdminStub(sessionCookie);
        CustomLifecyclesChecklistAdminServiceStub customLifecyclesChecklistAdminServiceStub = new InitializeCustomLifecyclesChecklistAdminCommand().executeAdminStub(sessionCookie);
        relationsAdminCommand = new RelationsAdminCommand(relationAdminServiceStub);
        infoAdminCommand = new InfoAdminCommand(infoAdminServiceStub);
        customLifecyclesChecklistAdminCommand = new CustomLifecyclesChecklistAdminCommand(customLifecyclesChecklistAdminServiceStub);
        associationTest();
        dependencyTest();
        tagTest();
        commentTest();
        rateTest();
        lifeCycleTest();

    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }


    private void associationTest() {
        AssociationTreeBean associationTreeBean = null;
        try {
            //check association is in position
            associationTreeBean = relationsAdminCommand.getAssociationTreeSuccessCase("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", "association");
            if (!associationTreeBean.getAssociationTree().contains("usedBy")) {
                Assert.fail("Expected association information not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while checking associations : " + e);
        }

    }

    private void dependencyTest() {
        AssociationTreeBean associationTreeBean = null;
        try {
            //check dependency information is in position
            associationTreeBean = relationsAdminCommand.getAssociationTreeSuccessCase("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", "depends");
            if (!associationTreeBean.getAssociationTree().contains("/_system/governance/endpoints/http/www/restfulwebservices/net/wcf/ep-WeatherForecastService-svc")) {
                Assert.fail("Expected dependency information not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while checking dependencies : " + e);
        }

    }

    private void commentTest() {
        try {
            infoAdminCommand.addCommentSuccessCase("this is sample comment", "/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", sessionCookie);
            infoAdminCommand.addCommentSuccessCase("this is sample comment2", "/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", sessionCookie);
            CommentBean commentBean = infoAdminCommand.getCommentsSuccessCase("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", sessionCookie);
            Comment[] comment = commentBean.getComments();
            if (!comment[0].getDescription().equalsIgnoreCase("this is sample comment")) {
                log.error("Added comment not found");
                org.junit.Assert.fail("Added comment not found");
            }
            if (!comment[1].getDescription().equalsIgnoreCase("this is sample comment2")) {
                log.error("Added comment not found");
                org.junit.Assert.fail("Added comment not found");
            }
            infoAdminCommand.removeCommentSuccessCase(comment[0].getCommentPath(), sessionCookie);
            commentBean = infoAdminCommand.getCommentsSuccessCase("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", sessionCookie);
            comment = commentBean.getComments();
            if (comment[0].getDescription().equalsIgnoreCase("this is sample comment")) {
                log.error("Comment not deleted");
                org.junit.Assert.fail("Comment not deleted");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception occured while adding/getting comment :" + e.getMessage());
            org.junit.Assert.fail("Exception occured while adding/getting comment :" + e.getMessage());
        }
    }

    private void tagTest() {
        TagBean tagBean;
        try {
            infoAdminCommand.addTagSuccessCase("SampleTag", "/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", sessionCookie);
            tagBean = infoAdminCommand.getTagsSuccessCase("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", sessionCookie);
            Tag[] tag = tagBean.getTags();
            for (int i = 0; i <= tag.length - 1; i++) {
                if (!tag[i].getTagName().equalsIgnoreCase("SampleTag")) {
                    Assert.fail("Tag not found : SampleTag");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception thrown while adding tag : " + e);
        }
    }

    private void rateTest() {
        RatingBean ratingBean;
        try {
            infoAdminCommand.rateResourceSuccessCase("2", "/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", sessionCookie);
            ratingBean = infoAdminCommand.getRatingsSuccessCase("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", sessionCookie);
            if (ratingBean.getUserRating() != 2) {
                Assert.fail("Required user rating not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while adding rate : " + e);
        }
    }

    private void lifeCycleTest() {
        String[] lifeCycleItem = {"Requirements Gathered", "Architecture Finalized", "High Level Design Completed"};
        customLifecyclesChecklistAdminCommand.addAspectSuccessCase("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", "ServiceLifeCycle");
        customLifecyclesChecklistAdminCommand.InvokeAspectSuccessCase("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", "ServiceLifeCycle", "promote", lifeCycleItem);
        LifecycleBean lifecycleBean = customLifecyclesChecklistAdminCommand.getLifecycleBeanSuccessCase("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl");
        LifecycleActions[] lifeCycleActions = lifecycleBean.getAvailableActions();
        for (int i = 0; i <= lifeCycleActions.length - 1; i++) {
            String[] actionList = lifeCycleActions[i].getActions();
            try {
                for (int j = 0; j <= actionList.length - 1; j++) {
                    if (!actionList[j].equalsIgnoreCase("demote")) {
                        Assert.fail("Life-cycle not promoted");
                    }
                }
            } catch (NullPointerException e) {
                Assert.fail("Life-cycle not promoted");
            }finally {
              customLifecyclesChecklistAdminCommand.removeAspectSuccessCase("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", "ServiceLifeCycle");
            }

        }
    }


}
