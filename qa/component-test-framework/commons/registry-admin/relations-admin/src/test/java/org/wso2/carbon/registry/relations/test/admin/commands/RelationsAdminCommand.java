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
package org.wso2.carbon.registry.relations.test.admin.commands;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.relations.ui.RelationAdminServiceStub;
import org.wso2.carbon.registry.relations.ui.beans.xsd.AssociationTreeBean;
import org.wso2.carbon.registry.relations.ui.beans.xsd.DependenciesBean;

/**
 * giving the access to operations in ActivityAdminService
 */
public class RelationsAdminCommand extends TestCase {
    private static final Log log = LogFactory.getLog(RelationsAdminCommand.class);
    RelationAdminServiceStub relationAdminServiceStub;

    public RelationsAdminCommand(
            RelationAdminServiceStub relationAdminServiceStub) {
        this.relationAdminServiceStub = relationAdminServiceStub;
        log.debug("relationAdminServiceStub added");
    }

    public void addAssociationSuccessCase(String path, String type, String associationPath,
                                          String toDo) throws Exception {
        relationAdminServiceStub.addAssociation(path, type, associationPath, toDo);
    }

    public void addAssociationFailureCase(String path, String type, String associationPath,
                                          String toDo) {
        try {
            relationAdminServiceStub.addAssociation(path, type, associationPath, toDo);
            log.error("Adding association without session cookie");
            Assert.fail("Adding association without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public AssociationTreeBean getAssociationTreeSuccessCase(String path, String type)
            throws Exception {
        AssociationTreeBean associationTreeBean = relationAdminServiceStub.getAssociationTree(path, type);
        return associationTreeBean;
    }

    public void getAssociationTreeFailureCase(String path, String type) {
        try {
            relationAdminServiceStub.getAssociationTree(path, type);
            log.error("Getting associationTreeBean without session cookie");
            Assert.fail("Getting associationTreeBean without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public DependenciesBean getDependenciesSuccessCase(String path)
            throws Exception {
        DependenciesBean dependenciesBean = relationAdminServiceStub.getDependencies(path);
        return dependenciesBean;
    }

    public void getDependenciesFailureCase(String path) {
        try {
            relationAdminServiceStub.getDependencies(path);
            log.error("Getting dependenciesBean without session cookie");
            Assert.fail("Getting dependenciesBean without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }
}
