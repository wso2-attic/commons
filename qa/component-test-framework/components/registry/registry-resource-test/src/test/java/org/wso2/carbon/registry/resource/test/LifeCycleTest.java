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
import org.wso2.carbon.common.test.utils.ConfigHelper;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.governance.lcm.test.admin.commands.InitializeLifeCycleManagementAdminCommand;
import org.wso2.carbon.governance.lcm.test.admin.commands.LifecyclesAdminCommand;
import org.wso2.carbon.governance.lcm.ui.ExceptionException;
import org.wso2.carbon.governance.lcm.ui.LifeCycleManagementServiceStub;
import org.wso2.carbon.governance.lcm.ui.beans.xsd.LifecycleBean;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;
import org.wso2.carbon.registry.resource.ui.beans.xsd.CollectionContentBean;

import java.io.File;

/**
 *
 */
public class LifeCycleTest extends TestTemplate {
    /**
     * @goal testing dependency feature in registry
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
            LifeCycleManagementServiceStub lifeCycleManagementServiceStub = new InitializeLifeCycleManagementAdminCommand().executeAdminStub(sessionCookie);
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
            collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/TestAutomation", "LifeCycleTest", "", "");
            log.info("collection added to " + collectionPath);
            addDefaultLC(lifeCycleManagementServiceStub);
            updateLifecycle(lifeCycleManagementServiceStub);
            deleteLifecycle(lifeCycleManagementServiceStub);
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

    private void addDefaultLC(LifeCycleManagementServiceStub lifeCycleManagementServiceStub) {
        try {
            LifecycleBean lifecycleBean = new LifecycleBean();
            String xmlConfig = ConfigHelper.getXMLConfig(frameworkPath + File.separator + "components" + File.separator + "registry" + File.separator + "registry-resource-test" + File.separator + "src" + File.separator + "test" + File.separator + "java" + File.separator + "resources" + File.separator + "custom_lifecycle.xml");
            LifecyclesAdminCommand lifecyclesAdminCommand = new LifecyclesAdminCommand(lifeCycleManagementServiceStub);
            lifecycleBean = lifecyclesAdminCommand.parseConfigurationSuccessCase(xmlConfig);
            new LifecyclesAdminCommand(lifeCycleManagementServiceStub).createLifecycleSuccessCase(lifecycleBean);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Error occured while adding lifecycle :" + e.getMessage());
            Assert.fail("Error occured while adding lifecycle :" + e.getMessage());
        }
    }

    private void updateLifecycle(LifeCycleManagementServiceStub lifeCycleManagementServiceStub) {
        try {
            LifecycleBean lifecycleBean = new LifecycleBean();
            lifecycleBean = new LifecyclesAdminCommand(lifeCycleManagementServiceStub).getLifecycleBeanSuccessCase("CustomLifeCycle");
            if (lifecycleBean.getName().equalsIgnoreCase("CustomLifeCycle")) {
                lifecycleBean.setName("CustomLifeCycleChanged");
            } else {
                log.error("Lifecycle update failed. CustomLifeCycle not found in lifecycle list");
                Assert.fail("Lifecycle update failed. CustomLifeCycle not found in lifecycle list");
            }
            new LifecyclesAdminCommand(lifeCycleManagementServiceStub).updateLifeCycleSuccessCase("CustomLifeCycle", lifecycleBean);
            lifecycleBean = new LifecyclesAdminCommand(lifeCycleManagementServiceStub).getLifecycleBeanSuccessCase("CustomLifeCycleChanged");
            if (!lifecycleBean.getName().equalsIgnoreCase("CustomLifeCycleChanged")) {
                log.error("Lifecycle not updated");
                Assert.fail("Lifecycle not updated");
            }
        }
        catch (ExceptionException e) {
            e.printStackTrace();
            log.error("Exception occured while updating lifecycle : " + e);
            Assert.fail("Exception occured while updating lifecycle : " + e);
        }
    }

    private void deleteLifecycle(LifeCycleManagementServiceStub lifeCycleManagementServiceStub) {
        try {
            new LifecyclesAdminCommand(lifeCycleManagementServiceStub).deleteLifecycleSuccessCase("CustomLifeCycleChanged");
            log.info("Lifecycle \"CustomLifeCycleChanged\" deleted.");
        }
        catch (ExceptionException e) {
            e.printStackTrace();
            log.error("Error orrcured while deleting lifecycle - CustomLifeCycleChanged ");
            Assert.fail("Error orrcured while deleting lifecycle - CustomLifeCycleChanged ");
        }
    }
}
