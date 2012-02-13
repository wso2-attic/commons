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
package org.wso2.carbon.registry.metadata.test.policy;

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
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;
import org.wso2.carbon.registry.resource.ui.beans.xsd.VersionPath;

public class PolicyMetadataTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(PolicyMetadataTest.class);

    private InfoAdminCommand infoAdminCommand = null;
    private CustomLifecyclesChecklistAdminCommand customLifecyclesChecklistAdminCommand = null;
    private RelationsAdminCommand relationsAdminCommand = null;
    private ResourceAdminCommand resourceAdminCommand = null;
    private String policyPath = "/_system/governance/policies/";
    private String resourceName = "sample_policy.xml";

    @Override
    public void init() {
        log.info("Initializing Tests for Community Features in Registry Policy");
        log.debug("Community Features in Registry Policy Tests Initialised");

    }

    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase");
        InfoAdminServiceStub infoAdminServiceStub = new InitializeInfoAdminCommand().executeAdminStub(sessionCookie);
        infoAdminCommand = new InfoAdminCommand(infoAdminServiceStub);
        CustomLifecyclesChecklistAdminServiceStub customLifecyclesChecklistAdminServiceStub = new InitializeCustomLifecyclesChecklistAdminCommand().executeAdminStub(sessionCookie);
        customLifecyclesChecklistAdminCommand = new CustomLifecyclesChecklistAdminCommand(customLifecyclesChecklistAdminServiceStub);
        RelationAdminServiceStub relationAdminServiceStub = new InitializeRelationsAdminCommand().executeAdminStub(sessionCookie);
        relationsAdminCommand = new RelationsAdminCommand(relationAdminServiceStub);
        ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
        resourceAdminCommand = new ResourceAdminCommand(resourceAdminServiceStub);

        addTag();
        addComment();
        addRate("2");
        addLifeCycle();
        addAssociation();
        addDependency();
        getVersion();

    }


    private void addTag() {

        try {
            infoAdminCommand.addTagSuccessCase("test tag added", policyPath + resourceName, sessionCookie);
            TagBean tagBean = infoAdminCommand.getTagsSuccessCase(policyPath + resourceName, sessionCookie);
            Tag[] tag = tagBean.getTags();
            for (int i = 0; i <= tag.length - 1; i++) {
                if (!tag[i].getTagName().equalsIgnoreCase("test tag added")) {
                    log.error("The given tag not found");
                    Assert.fail("Tag not found");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception thrown while adding tag : " + e);
        }
    }

    private void addComment() {

        try {
            //adding comment to a resource
            infoAdminCommand.addCommentSuccessCase("added a test comment", policyPath + resourceName, sessionCookie);
            CommentBean commentBean = infoAdminCommand.getCommentsSuccessCase(policyPath + resourceName, sessionCookie);
            Comment[] comment = commentBean.getComments();

            if (!comment[0].getDescription().equalsIgnoreCase("added a test comment")) {
                log.error("comment not found");
                Assert.fail("comment not found");
            }

            //removing comment from the resource
            infoAdminCommand.removeCommentSuccessCase(comment[0].getCommentPath(), sessionCookie);

            if (comment[0].getDescription().equalsIgnoreCase("added test comment")) {
                log.error("comment can not be deleted");
                Assert.fail("comment can not be deleted");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception occurred while put and get comment :" + e.getMessage());
            Assert.fail("Exception occurred while put and get comment  :" + e.getMessage());
        }
    }

    private void addRate(String rateValue) {

        try {
            infoAdminCommand.rateResourceSuccessCase(rateValue, policyPath + resourceName, sessionCookie);
            RatingBean ratingBean = infoAdminCommand.getRatingsSuccessCase(policyPath + resourceName, sessionCookie);
            if (ratingBean.getUserRating() != Integer.parseInt(rateValue)) {
                log.error("Rating value not found");
                Assert.fail("Rating value not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while adding rate : " + e);
        }
    }

    private void addLifeCycle() {

        String[] lifeCycleItem = {"Design Stage", "Implementation Stage", "Testing"};
        customLifecyclesChecklistAdminCommand.addAspectSuccessCase(policyPath + resourceName, "ServiceLifeCycle");
        customLifecyclesChecklistAdminCommand.InvokeAspectSuccessCase(policyPath + resourceName, "ServiceLifeCycle", "promote", lifeCycleItem);
        LifecycleBean lifecycleBean = customLifecyclesChecklistAdminCommand.getLifecycleBeanSuccessCase(policyPath + resourceName);
        LifecycleActions[] lifeCycleActions = lifecycleBean.getAvailableActions();

        for (int i = 0; i <= lifeCycleActions.length - 1; i++) {
            String[] actionList = lifeCycleActions[i].getActions();
            try {
                for (int j = 0; j <= actionList.length - 1; j++) {
                    if (!actionList[j].equalsIgnoreCase("demote")) {
                        log.error("Life-cycle not promoted");
                        Assert.fail("Life-cycle not promoted");
                    }
                }
            } catch (NullPointerException e) {
                Assert.fail("Life-cycle not promoted");
            } finally {
                customLifecyclesChecklistAdminCommand.removeAspectSuccessCase(policyPath + resourceName, "ServiceLifeCycle");
            }

        }
    }

    private void addAssociation() {

        AssociationTreeBean associationTreeBean = null;
        try {
            relationsAdminCommand.addAssociationSuccessCase(policyPath + resourceName, "asso", "/_system/governance/policies/", "add");

            //check for added association
            associationTreeBean = relationsAdminCommand.getAssociationTreeSuccessCase("/_system/governance/policies/", "asso");
            if (!(associationTreeBean.getAssoType().equals("asso"))) {
                log.error("Required Association Information Not Found");
                Assert.fail("Required Association Information Not Found");
            }

        } catch (Exception e) {
            throw new RuntimeException("Exception thrown while adding an association : " + e);
        }

    }

    private void addDependency() {

        AssociationTreeBean associationTreeBean = null;

        try {
            relationsAdminCommand.addAssociationSuccessCase(policyPath + resourceName, "depends", "/_system/governance/policies/", "add");

            //check for added dependencies
            associationTreeBean = relationsAdminCommand.getAssociationTreeSuccessCase("/_system/governance/policies/", "depends");
            if (!(associationTreeBean.getAssoType().equals("depends"))) {
                log.error("Required Dependency Information Not Found");
                Assert.fail("Required Dependency Information Not Found");
            }

        } catch (Exception e) {
            throw new RuntimeException("Exception thrown while adding a dependency : " + e);
        }
    }


    private void getVersion() {

        VersionPath[] versionPath = null;
        long versionNoBefore = 0L;
        long versionNoAfter = 0L;

        try {
            resourceAdminCommand.createVersionSuccessCase(policyPath + resourceName);
            versionPath = resourceAdminCommand.getVersionsBeanSuccessCase(policyPath + resourceName).getVersionPaths();
            versionNoBefore = versionPath[0].getVersionNumber();

            /**
             * update resource content and checking the version number update
             */

            updatePolicyFromFile();

            versionPath = resourceAdminCommand.getVersionsBeanSuccessCase(policyPath + resourceName).getVersionPaths();
            versionNoAfter = versionPath[0].getVersionNumber();

            if (versionNoBefore != versionNoAfter - 1) {
                Assert.fail("New Version has not been created: ");
                log.error("New Version has not been created: ");

            }

        } catch (Exception e) {
            throw new RuntimeException("Exception thrown when getting resource version : " + e);
        }

    }

    private void updatePolicyFromFile() {

        String resourceName = "sample_policy.xml";
        String resContent = "<?xml version=\"1.0\"?>\n" +
                            "\n" +
                            "<wsp:Policy xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\">\n" +
                            "  <wsp:ExactlyOne>\n" +
                            "    <wsp:All>\n" +
                            "      <wsrmp10:RMAssertion\n" +
                            "       xmlns:wsrmp10=\"http://schemas.xmlsoap.org/ws/2005/02/rm/policy\">\n" +
                            "        <!--wsrmp10:InactivityTimeout Milliseconds=\"600000\"/-->\n" +
                            "        <wsrmp10:InactivityTimeout/>\n" +
                            "        <wsrmp10:BaseRetransmissionInterval Milliseconds=\"3000\"/>\n" +
                            "        <wsrmp10:ExponentialBackoff/>\n" +
                            "        <wsrmp10:AcknowledgementInterval Milliseconds=\"200\"/>\n" +
                            "      </wsrmp10:RMAssertion>\n" +
                            "    </wsp:All>\n" +
                            "    <wsp:All>\n" +
                            "      <wsrmp:RMAssertion\n" +
                            "           xmlns:wsrmp=\"http://docs.oasis-open.org/ws-rx/wsrmp/200702\">\n" +
                            "        <wsrmp:SequenceSTR/>\n" +
                            "        <wsrmp:DeliveryAssurance>\n" +
                            "          <wsp:Policy>\n" +
                            "            <wsrmp:ExactlyOnce/>\n" +
                            "          </wsp:Policy>\n" +
                            "        </wsrmp:DeliveryAssurance>\n" +
                            "      </wsrmp:RMAssertion>\n" +
                            "    </wsp:All>\n" +
                            "  </wsp:ExactlyOne>\n" +
                            "</wsp:Policy>"; //to update

        try {

            /**
             *  update policy and check the content
             */
            resourceAdminCommand.updateTextContentSuccessCase(policyPath + resourceName, resContent);

            if (resourceAdminCommand.getTextContentSuccessCase(policyPath + resourceName).contains("InactivityTimeout")) {
                log.info("Policy file successfully updated");
            } else {
                log.error("Policy File has not been updated in the registry");
                Assert.fail("Policy File has not been updated in the registry");
            }

        }
        catch (Exception e) {
            Assert.fail("Unable to get file content: " + e);
            log.error("Unable to get file content: " + e.getMessage());
        }

    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }
}
