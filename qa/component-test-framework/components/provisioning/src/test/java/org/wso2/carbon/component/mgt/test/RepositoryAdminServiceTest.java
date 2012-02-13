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
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.component.mgt.test.commands.InitializeRepositoryAdminCommand;
import org.wso2.carbon.component.mgt.test.commands.RepositoryAdminCommand;
import org.wso2.carbon.component.mgt.ui.RepositoryAdminServiceStub;
import org.wso2.carbon.component.mgt.ui.prov.data.Feature;
import org.wso2.carbon.component.mgt.ui.prov.data.FeatureInfo;
import org.wso2.carbon.component.mgt.ui.prov.data.RepositoryInfo;


/**
 *
 */
public class RepositoryAdminServiceTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(RepositoryAdminServiceTest.class);
    public String repoLocation;


    @Override
    public void init() {
        log.info("Initializing RepositoryAdminServiceTest class ");
        log.debug("RepositoryAdminService Test Initialised");
    }

    @Override
    public void runSuccessCase() {
        repoLocation = FrameworkSettings.P2_REPO;
        RepositoryAdminServiceStub repositoryAdminServiceStub = new InitializeRepositoryAdminCommand().executeAdminStub(sessionCookie);

        removeRepo(repositoryAdminServiceStub,repoLocation);
        addRepo(repositoryAdminServiceStub,repoLocation,"Carbon_Repo");
        getAllRepo(repositoryAdminServiceStub);
        enableRepo(repositoryAdminServiceStub);
        getRepoStatus(repositoryAdminServiceStub);
        getInstalledFeatures(repositoryAdminServiceStub);
        getInstallableFeatureInfo(repositoryAdminServiceStub);
        updateRepo(repositoryAdminServiceStub);
        removeRepo(repositoryAdminServiceStub,repoLocation);
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


    private void enableRepo(RepositoryAdminServiceStub repositoryAdminServiceStub) {
        new RepositoryAdminCommand(repositoryAdminServiceStub).enableRepositorySuccessCase(repoLocation, false);
        new RepositoryAdminCommand(repositoryAdminServiceStub).enableRepositorySuccessCase(repoLocation, true);
    }

    private void getAllRepo(RepositoryAdminServiceStub repositoryAdminServiceStub) {
        boolean methodStatus = false;
        RepositoryInfo[] repositoryInfo = new RepositoryAdminCommand(repositoryAdminServiceStub).getAllRepositoriesSuccessCase(repoLocation, true);
        for (int i = 0; i < repositoryInfo.length; i++) {
            if (!repositoryInfo[i].getNickName().equalsIgnoreCase("Carbon_Repo")) {
                methodStatus = false;
            }
            else {
                methodStatus = true;
            }
        }
        testHandler(methodStatus, "Unable to filnd repository or getAllrepositories admin method doesn't work");
        methodStatus = false;
    }

    public void addRepo(RepositoryAdminServiceStub repositoryAdminServiceStub,String repoLocation,String repoName) {
        boolean methodStatus;
        methodStatus = new RepositoryAdminCommand(repositoryAdminServiceStub).addRepositorySuccessCase(repoLocation, repoName);
        testHandler(methodStatus, "Unable to add new repository");
        methodStatus = false;
    }

    private void getRepoStatus(RepositoryAdminServiceStub repositoryAdminServiceStub) {
        try {
            boolean methodStatus = false;
            new RepositoryAdminCommand(repositoryAdminServiceStub).enableRepositorySuccessCase(repoLocation, false);
            RepositoryInfo[] repositoryInfo = new RepositoryAdminCommand(repositoryAdminServiceStub).getEnableRepositoriesSuccessCase(repoLocation, false);
            for (int i = 0; i < repositoryInfo.length; i++) {
                if (!repositoryInfo[i].getNickName().equalsIgnoreCase("Carbon_Repo")) {
                    methodStatus = false;
                }
                else {
                    methodStatus = true;
                }
            }

            testHandler(methodStatus, "Unable to find disable repository or getEnablerepositories admin method doesn't work");

            new RepositoryAdminCommand(repositoryAdminServiceStub).enableRepositorySuccessCase(repoLocation, true);
            repositoryInfo = new RepositoryAdminCommand(repositoryAdminServiceStub).getEnableRepositoriesSuccessCase(repoLocation, true);
            for (int i = 0; i < repositoryInfo.length; i++) {
                if (!repositoryInfo[i].getNickName().equalsIgnoreCase("Carbon_Repo")) {
                    methodStatus = false;
                }
                else {
                    methodStatus = true;
                }
            }
            testHandler(methodStatus, "Unable to filnd enable repository or getEnablerepositories admin method doesn't work");
            methodStatus = false;
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void getInstalledFeatures(RepositoryAdminServiceStub repositoryAdminServiceStub) {
        new RepositoryAdminCommand(repositoryAdminServiceStub).enableRepositorySuccessCase(repoLocation, true);
        Feature[] feature = new RepositoryAdminCommand(repositoryAdminServiceStub).getInstallableFeaturesSuccessCase(repoLocation, false, false, false);
        if (feature.length < 1) {
            testHandler(false, "Unable to get installable features or getInstallableFeatures admin method doesn't work");
        }
    }

    private void getInstallableFeatureInfo(RepositoryAdminServiceStub repositoryAdminServiceStub) {
        new RepositoryAdminCommand(repositoryAdminServiceStub).enableRepositorySuccessCase(repoLocation, true);
        FeatureInfo featureInfo = new RepositoryAdminCommand(repositoryAdminServiceStub).getInstallableFeatureInfoSuccessCase("org.apache.synapse.samples.feature.group", "1.4.0.wso2v2");
        if (!featureInfo.getFeatureName().contains("Synapse Samples Feature")) {
            testHandler(false, "Unable to get installabel feature name or getInstallableFeatureInfo admin method doesn't work");
        }
    }

    private void updateRepo(RepositoryAdminServiceStub repositoryAdminServiceStub) {
        new RepositoryAdminCommand(repositoryAdminServiceStub).updateRepositorySuccessCase(repoLocation, "Carbon_Repo", repoLocation, "NewCarbon_repo");
    }

    public void removeRepo(RepositoryAdminServiceStub repositoryAdminServiceStub,String repoLocation) {
        new RepositoryAdminCommand(repositoryAdminServiceStub).removeRepositorySuccessCase(repoLocation);
    }

}

