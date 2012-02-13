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

// Admin service operatrions test

package org.wso2.carbon.component.mgt.test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.component.mgt.test.commands.InitializeProvisioningAdminCommand;
import org.wso2.carbon.component.mgt.test.commands.InitializeRepositoryAdminCommand;
import org.wso2.carbon.component.mgt.test.commands.ProvisioningAdminCommand;
import org.wso2.carbon.component.mgt.ui.ProvisioningAdminServiceStub;
import org.wso2.carbon.component.mgt.ui.RepositoryAdminServiceStub;
import org.wso2.carbon.component.mgt.ui.prov.data.Feature;
import org.wso2.carbon.component.mgt.ui.prov.data.FeatureInfo;
import org.wso2.carbon.component.mgt.ui.prov.data.LicenseInfo;
import org.wso2.carbon.component.mgt.ui.prov.data.ProvisioningActionInfo;
import org.wso2.carbon.component.mgt.ui.prov.data.ProvisioningActionResultInfo;


/**
 *
 */
public class ProvisioningAdminServiceTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(ProvisioningAdminServiceTest.class);
    private String repoLocation = "http://builder.wso2.org/~carbon/releases/carbon/3.0.1/RC3/p2-repo/";


    @Override
    public void init() {
        log.info("Initializing ProvisioningAdminServiceTest class ");
        log.debug("ProvisioningAdminService Test Initialised");
    }

    @Override
    public void runSuccessCase() {
        /*
          ActionTypes = org.wso2.carbon.prov.action.install
                        org.wso2.carbon.prov.action.uninstall
                        org.wso2.carbon.prov.action.revert
         */

        ProvisioningAdminServiceStub provisioningAdminServiceStub = new InitializeProvisioningAdminCommand().executeAdminStub(sessionCookie);
        RepositoryAdminServiceStub repositoryAdminServiceStub = new InitializeRepositoryAdminCommand().executeAdminStub(sessionCookie);

        RepositoryAdminServiceTest repositoryAdminServiceTest = new RepositoryAdminServiceTest();
        repositoryAdminServiceTest.removeRepo(repositoryAdminServiceStub, repoLocation);
        log.info("repo removed");
        repositoryAdminServiceTest.addRepo(repositoryAdminServiceStub, repoLocation, "ProvisioningRepo");
        log.info("repo added");
        LicenseInfo[] licenseInfo = new ProvisioningAdminCommand(provisioningAdminServiceStub).getLicensingInfoSuccessCase();
        // new ProvisioningAdminCommand(provisioningAdminServiceStub).performProvisioningActionSuccessCase("org.wso2.carbon.prov.action.install");
        ProvisioningActionInfo provisioningActionInfo = new ProvisioningActionInfo();
        Feature feature = new Feature();

        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setFeatureID("org.wso2.carbon.bpel.feature.group");
        featureInfo.setFeatureName("WSO2 Carbon - BPEL Feature");
        featureInfo.setFeatureVersion("3.0.1");


        provisioningActionInfo.addFeaturesToInstall(featureInfo);
        provisioningActionInfo.setActionType("org.wso2.carbon.prov.action.install");


        ProvisioningActionResultInfo provisioningActionResultInfo = new ProvisioningAdminCommand(provisioningAdminServiceStub).reviewProvisioningActionSuccessCase(provisioningActionInfo);
        new ProvisioningAdminCommand(provisioningAdminServiceStub).performProvisioningActionSuccessCase("org.wso2.carbon.prov.action.install");
         


    }

    @Override
    public void runFailureCase() {
    }

    public void cleanup() {
    }

    private void testHandler(boolean methodStatus, String assertMassage) {
        if (methodStatus == false) {
            Assert.fail(assertMassage);
            log.error(assertMassage);
        }
    }


}

