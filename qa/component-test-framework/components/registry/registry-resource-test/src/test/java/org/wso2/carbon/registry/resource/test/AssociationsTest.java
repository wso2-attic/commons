/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.
 
  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
 
  http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/


package org.wso2.carbon.registry.resource.test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.registry.relations.test.admin.commands.InitializeRelationsAdminCommand;
import org.wso2.carbon.registry.relations.test.admin.commands.RelationsAdminCommand;
import org.wso2.carbon.registry.relations.ui.RelationAdminServiceStub;
import org.wso2.carbon.registry.relations.ui.beans.xsd.AssociationTreeBean;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;
import org.wso2.carbon.registry.resource.ui.beans.xsd.CollectionContentBean;

import java.io.File;

public class AssociationsTest extends TestTemplate {
    /**
     * @goal testing associations feature in registry
     */

    private static final Log log = LogFactory.getLog(AssociationsTest.class);

    @Override
    public void init() {

    }

    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase");

        try {
            ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
            RelationAdminServiceStub relationAdminServiceStub = new InitializeRelationsAdminCommand().executeAdminStub(sessionCookie);
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
            collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/TestAutomation", "AssociationTest", "", "");
            log.info("collection added to " + collectionPath);
            collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/TestAutomation/AssociationTest", "TestCollection1", "", "");
            collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/TestAutomation/AssociationTest", "TestCollection2", "", "");
            String resource = frameworkPath + File.separator + "components" + File.separator + "registry" + File.separator + "registry-resource-test" + File.separator + "src" + File.separator + "test" + File.separator + "java" + File.separator + "resources" + File.separator + "sampleText.txt";
            new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/TestAutomation/AssociationTest/TestCollection1/sampleText.txt", "text/html", "txtDesc", "file:///" + resource, null);
            new ResourceAdminCommand(resourceAdminServiceStub).getTextContentSuccessCase("/TestAutomation/AssociationTest/TestCollection1/sampleText.txt");
            addAssociation(relationAdminServiceStub);
            deleteAssociation(relationAdminServiceStub);
            addInvalidAssociation(relationAdminServiceStub);

            new ResourceAdminCommand(resourceAdminServiceStub).deleteResourceSuccessCase("/TestAutomation");
        }
        catch (Exception e) {
            Assert.fail("error occured while running association test " + e);
            log.error(" error occured while running association test " + e.getMessage());

        }
    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }

    private void addAssociation(RelationAdminServiceStub relationAdminServiceStub) {
        try {
            new RelationsAdminCommand(relationAdminServiceStub).addAssociationSuccessCase("/TestAutomation/AssociationTest/TestCollection2", "association", "/TestAutomation/AssociationTest/TestCollection1/sampleText.txt", "add");
            AssociationTreeBean associationTreeBean = new RelationsAdminCommand(relationAdminServiceStub).getAssociationTreeSuccessCase("/TestAutomation/AssociationTest/TestCollection2", "association");
            if (!associationTreeBean.getAssociationTree().contains("/TestAutomation/AssociationTest/TestCollection1/sampleText.txt")) {
                log.error("Added association not found in /TestAutomation/AssociationTest/TestCollection2");
                Assert.fail("Added association not found in /TestAutomation/AssociationTest/TestCollection2");
            }
            log.debug("associationTreeBean : " + associationTreeBean.getAssociationTree());
        }
        catch (Exception e) {
            log.error("Unable to add association in /TestAutomation/AssociationTest/TestCollection2");
            Assert.fail("Unable to add association in /TestAutomation/AssociationTest/TestCollection2");

        }
    }


    private void deleteAssociation(RelationAdminServiceStub relationAdminServiceStub) {
        try {
            new RelationsAdminCommand(relationAdminServiceStub).addAssociationSuccessCase("/TestAutomation/AssociationTest/TestCollection2", "association", "/TestAutomation/AssociationTest/TestCollection1/sampleText.txt", "remove");
            log.debug("Association removed in : /TestAutomation/AssociationTest/TestCollection2");
        }
        catch (Exception e) {
            log.error("Unable to remove association in /TestAutomation/AssociationTest/TestCollection2");
            Assert.fail("Unable to remove association in /TestAutomation/AssociationTest/TestCollection2");
        }
    }

    public void addInvalidAssociation(RelationAdminServiceStub relationAdminServiceStub) {
        // current release havin bug = CARBON-8272
        try {
            new RelationsAdminCommand(relationAdminServiceStub).addAssociationSuccessCase("TestAutomation/AssociationTest/TestCollection2", "association", "TestAutomation/AssociationTest/TestCollection2/invalid", "add");
            log.error("Error: Expected exception not thrown while adding invalid association path");
            Assert.fail("Error: Expected exception not thrown while adding invalid association path");
        }
        catch (Exception e) {
            //toDo exception need to check here
        }
    }
}
