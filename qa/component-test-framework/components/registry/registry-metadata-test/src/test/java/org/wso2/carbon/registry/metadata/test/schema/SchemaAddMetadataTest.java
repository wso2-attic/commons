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

package org.wso2.carbon.registry.metadata.test.schema;

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
import org.wso2.carbon.registry.metadata.test.util.RegistryConsts;
import org.wso2.carbon.registry.relations.test.admin.commands.InitializeRelationsAdminCommand;
import org.wso2.carbon.registry.relations.test.admin.commands.RelationsAdminCommand;
import org.wso2.carbon.registry.relations.ui.RelationAdminServiceStub;
import org.wso2.carbon.registry.relations.ui.beans.xsd.AssociationTreeBean;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;

public class SchemaAddMetadataTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(SchemaValidateTest.class);
    RelationsAdminCommand relationsAdminCommand;
    InfoAdminCommand infoAdminCommand;
    private ResourceAdminCommand resourceAdminCommand = null;
    CustomLifecyclesChecklistAdminCommand customLifecyclesChecklistAdminCommand;
    private String schemaPath = "/_system/governance/schemas/";

    @Override
    public void init() {
        log.info("Initializing SchemaAddMetadata Tests");
        log.debug("SchemaAddMetadataTest Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase");
        RelationAdminServiceStub relationAdminServiceStub = new InitializeRelationsAdminCommand().executeAdminStub
                (sessionCookie);
        InfoAdminServiceStub infoAdminServiceStub = new InitializeInfoAdminCommand().executeAdminStub(sessionCookie);
        CustomLifecyclesChecklistAdminServiceStub customLifecyclesChecklistAdminServiceStub =
                new InitializeCustomLifecyclesChecklistAdminCommand().executeAdminStub(sessionCookie);
        ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);

        resourceAdminCommand = new ResourceAdminCommand(resourceAdminServiceStub);
        relationsAdminCommand = new RelationsAdminCommand(relationAdminServiceStub);
        infoAdminCommand = new InfoAdminCommand(infoAdminServiceStub);
        customLifecyclesChecklistAdminCommand = new CustomLifecyclesChecklistAdminCommand(customLifecyclesChecklistAdminServiceStub);
        addSchemaMultipleImports();

    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }

    private void addSchemaMultipleImports() {
        String resourceUrl = "http://ww2.wso2.org/~qa/greg/calculator.xsd";
        String resourceName = "calculator.xsd";
        String referenceSchemaFile = "calculator-no-element-name-invalid.xsd";

        try {
            resourceAdminCommand.importResourceSuccessCase(schemaPath + resourceName, resourceName,
                    RegistryConsts.APPLICATION_X_XSD_XML, "schemaFile", resourceUrl, null);

            String textContent = resourceAdminCommand.getTextContentSuccessCase(schemaPath +
                    "http/charitha/org/" + resourceName);

            if (textContent.indexOf("xmlns:tns=\"http://charitha.org/\"") != -1) {
                log.info("Schema content found");

            } else {
                log.error("Schema content not found");
                Assert.fail("Schema content not found");
            }

            String textContentImportedSchema = resourceAdminCommand.getTextContentSuccessCase(schemaPath +
                    "http/charitha/org1/" + referenceSchemaFile);

            if (textContentImportedSchema.indexOf("xmlns:tns=\"http://charitha.org/\"") != -1) {
                log.info("Schema content found");

            } else {
                log.error("Schema content not found");
                Assert.fail("Schema content not found");
            }

            //check dependencies
            dependencyTest(schemaPath + "http/charitha/org/" + resourceName, schemaPath +
                    "http/charitha/org1/" + referenceSchemaFile);
            //check associations
            associationTest(schemaPath +
                    "http/charitha/org1/" + referenceSchemaFile, schemaPath + "http/charitha/org/" + resourceName);

            addCommentTest(schemaPath + "http/charitha/org/" + resourceName, "This is a sample comment for main " +
                    "schema file");
            addCommentTest(schemaPath + "http/charitha/org1/" + referenceSchemaFile, "This is a sample comment for " +
                    "imported schema file");

            //add tags into each schema
            tagTest(schemaPath + "http/charitha/org/" + resourceName, "TestTag");
            tagTest(schemaPath + "http/charitha/org1/" + referenceSchemaFile, "TestTag");

            //rate schemas
            rateTest(schemaPath + "http/charitha/org/" + resourceName, "3");
            rateTest(schemaPath + "http/charitha/org1/" + referenceSchemaFile, "5");

            //add lifecycles to schemas
            lifeCycleTest(schemaPath + "http/charitha/org/" + resourceName);
            lifeCycleTest(schemaPath + "http/charitha/org1/" + referenceSchemaFile);


            //delete the added resource
            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/charitha/org/" + resourceName);

            resourceAdminCommand.deleteResourceSuccessCase(schemaPath +
                    "http/charitha/org1/" + referenceSchemaFile);

            //check if the deleted file exists in registry
            if (!resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                    "http/charitha/org/", resourceName)) {
                log.info("Resource successfully deleted from the registry");

            } else {
                log.error("Resource not deleted from the registry");
                Assert.fail("Resource not deleted from the registry");
            }

            if (!resourceAdminCommand.isResourceExist(sessionCookie, schemaPath +
                    "http/charitha/org1/", referenceSchemaFile)) {
                log.info("Resource successfully deleted from the registry");

            } else {
                log.error("Resource not deleted from the registry");
                Assert.fail("Resource not deleted from the registry");
            }
        }
        catch (Exception e) {
            Assert.fail("Unable to get text content " + e);
            log.error(" : " + e.getMessage());
        }
    }

    /**
     * Check associations of uploaded schema
     */
    private void associationTest(String path, String association) {
        AssociationTreeBean associationTreeBean = null;
        try {
            //check association is in position
            associationTreeBean = relationsAdminCommand.getAssociationTreeSuccessCase(path, "association");
            if (!associationTreeBean.getAssociationTree().contains(association)) {
                Assert.fail("Expected association information not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while checking associations : " + e);
        }

    }

    /**
     * Check dependencies of uploaded schema
     */
    private void dependencyTest(String path, String association) {
        AssociationTreeBean associationTreeBean = null;
        try {
            associationTreeBean = relationsAdminCommand.getAssociationTreeSuccessCase(path, "depends");
            if (!associationTreeBean.getAssociationTree().contains(association)) {
                Assert.fail("Expected dependency information not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while checking dependencies : " + e);
        }

    }

    private void addCommentTest(String path, String commentText) {
        try {
            //add comement
            infoAdminCommand.addCommentSuccessCase(commentText, path, sessionCookie);
            CommentBean commentBean = infoAdminCommand.getCommentsSuccessCase(path, sessionCookie);
            Comment[] comment = commentBean.getComments();

            if (!comment[0].getDescription().equalsIgnoreCase(commentText)) {
                log.error("Added comment not found");
                org.junit.Assert.fail("Added comment not found");
            }

            //remove comment
            infoAdminCommand.removeCommentSuccessCase(comment[0].getCommentPath(), sessionCookie);
            commentBean = infoAdminCommand.getCommentsSuccessCase(path, sessionCookie);
            try {
                comment = commentBean.getComments();
            } catch (NullPointerException e) {
                log.info("Comment deleted successfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception occurred while put and get comment :" + e.getMessage());
            org.junit.Assert.fail("Exception occurred while put and get comment  :" + e.getMessage());
        }

    }

    private void tagTest(String path, String tagName) {
        TagBean tagBean;
        try {
            infoAdminCommand.addTagSuccessCase(tagName, path, sessionCookie);
            tagBean = infoAdminCommand.getTagsSuccessCase(path, sessionCookie);
            Tag[] tag = tagBean.getTags();
            for (int i = 0; i <= tag.length - 1; i++) {
                if (!tag[i].getTagName().equalsIgnoreCase(tagName)) {
                    log.error("The given tag not found");
                    Assert.fail("Tag not found");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception thrown while adding tag : " + e);
        }
    }

    private void rateTest(String path, String rateValue) {
        RatingBean ratingBean;
        try {
            infoAdminCommand.rateResourceSuccessCase(rateValue, path, sessionCookie);
            ratingBean = infoAdminCommand.getRatingsSuccessCase(path, sessionCookie);
            int rateIntValue = Integer.parseInt(rateValue);
            if (ratingBean.getUserRating() != rateIntValue) {
                Assert.fail("Rating value not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while adding rate : " + e);
        }
    }

    private void lifeCycleTest(String path) {
        String[] lifeCycleItem = {"Requirements Gathered", "Architecture Finalized", "High Level Design Completed"};
        customLifecyclesChecklistAdminCommand.addAspectSuccessCase(path, "ServiceLifeCycle");
        customLifecyclesChecklistAdminCommand.InvokeAspectSuccessCase(path, "ServiceLifeCycle", "promote", lifeCycleItem);
        LifecycleBean lifecycleBean = customLifecyclesChecklistAdminCommand.getLifecycleBeanSuccessCase(path);
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
            } finally {
                customLifecyclesChecklistAdminCommand.removeAspectSuccessCase(path, "ServiceLifeCycle");
            }

        }
    }
}
