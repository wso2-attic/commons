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
package org.wso2.carbon.component.mgt.test.commands;

// Repository admin stub P2 based command implemantation class

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.component.mgt.ui.ProvisioningAdminServiceStub;
import org.wso2.carbon.component.mgt.ui.RepositoryAdminServiceStub;
import org.wso2.carbon.component.mgt.ui.prov.data.Feature;
import org.wso2.carbon.component.mgt.ui.prov.data.FeatureInfo;
import org.wso2.carbon.component.mgt.ui.prov.data.RepositoryInfo;


/**
 *
 */
public class RepositoryAdminCommand extends TestCase {
    private static final Log log = LogFactory.getLog(RepositoryAdminCommand.class);
    RepositoryAdminServiceStub repositoryAdminServiceStub;
    private boolean methodStatus;

    public RepositoryAdminCommand(RepositoryAdminServiceStub repositoryAdminServiceStub) {
        this.repositoryAdminServiceStub = repositoryAdminServiceStub;
        log.debug("RepositoryAdminServiceStub added");
    }

    public boolean addRepositorySuccessCase(String location, String repoName) {
        try {
            methodStatus = repositoryAdminServiceStub.addRepository(location, repoName);
            log.debug("New repository added");
        }
        catch (Exception e) {
            log.error("Unable to add new repository" + e.getMessage());
            Assert.fail("Unable to add new repository");
        }
        return methodStatus;
    }

    public boolean addRepositoryFailureCase(String location, String repoName)
            throws Exception {
        try {
            methodStatus = repositoryAdminServiceStub.addRepository(location, repoName);
            log.error("New repository added without session cookie");
            Assert.fail("New repository added without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());

        }
        return methodStatus;
    }

    public void enableRepositorySuccessCase(String location, boolean isEnabled) {
        try {
            repositoryAdminServiceStub.enableRepository(location, isEnabled);
            log.debug("repository enabled");
        }
        catch (Exception e) {
            log.error("repository enable failed" + e.getMessage());
            Assert.fail("repository enable failed");
        }
    }

    public void enableRepositoryFailureCase(String location, boolean isEnabled)
            throws Exception {
        try {
            repositoryAdminServiceStub.enableRepository(location, isEnabled);
            log.error("repository enabled without session cookie");
            Assert.fail("repository enabled without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());

        }
    }

    public RepositoryInfo[] getAllRepositoriesSuccessCase(String location, boolean isEnabled) {
        RepositoryInfo[] repositoryInfo = null;
        try {
            repositoryInfo = repositoryAdminServiceStub.getAllRepositories();
            log.debug("Getting all repositories");
        }
        catch (Exception e) {
            log.error("Getting repository failed" + e.getMessage());
            Assert.fail("Getting repository failed");
        }
        return repositoryInfo;
    }

    public RepositoryInfo[] getAllRepositoriesFailureCase(String location, boolean isEnabled)
            throws Exception {
        RepositoryInfo[] repositoryInfo = null;
        try {
            repositoryInfo = repositoryAdminServiceStub.getAllRepositories();
            log.error("getting repository without session cookie");
            Assert.fail("getting repository without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());
        }
        return repositoryInfo;
    }

    public RepositoryInfo[] getEnableRepositoriesSuccessCase(String location, boolean isEnabled) {
        RepositoryInfo[] repositoryInfo = null;
        try {
            repositoryInfo = repositoryAdminServiceStub.getEnabledRepositories();
            log.debug("Getting enabled repository");
        }
        catch (Exception e) {
            log.error("Getting enabled repository failed" + e.getMessage());
            Assert.fail("Getting enabled repository failed");
        }
        return repositoryInfo;
    }

    public RepositoryInfo[] getEnableRepositoriesFailureCase(String location, boolean isEnabled)
            throws Exception {
        RepositoryInfo[] repositoryInfo = null;
        try {
            repositoryInfo = repositoryAdminServiceStub.getEnabledRepositories();
            log.error("Getting enabled repository without session cookie");
            Assert.fail("Getting enabled repository without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());
        }
        return repositoryInfo;
    }

    public void removeRepositorySuccessCase(String location) {
        try {
            repositoryAdminServiceStub.removeRepository(location);
            log.debug("repository removed from " + location);
        }
        catch (Exception e) {
            log.error("repository remove failed" + e.getMessage());
            Assert.fail("repository remove failed");
        }
    }

    public void removeRepositoryFailureCase(String location)
            throws Exception {
        try {
            repositoryAdminServiceStub.removeRepository(location);
            log.error("repository removed without session cookie");
            Assert.fail("repository removed without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());

        }
    }

    public void updateRepositorySuccessCase(String previousLocation, String previousName,
                                            String newLocation, String newName) {
        try {
            repositoryAdminServiceStub.updateRepository(previousLocation, previousName, newLocation, newName);
            log.debug("repository updated");
        }
        catch (Exception e) {
            log.error("repository update failed" + e.getMessage());
            Assert.fail("repository update failed");
        }
    }

    public void updateRepositoryFailureCase(String previousLocation, String previousName,
                                            String newLocation, String newName)
            throws Exception {
        try {
            repositoryAdminServiceStub.updateRepository(previousLocation, previousName, newLocation, newName);
            log.error("repository update without session cookie");
            Assert.fail("repository update without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());

        }
    }

    public FeatureInfo getInstallableFeatureInfoSuccessCase(String featureId, String featureVersion) {
        FeatureInfo featureInfo = null;
        try {
            featureInfo = repositoryAdminServiceStub.getInstallableFeatureInfo(featureId, featureVersion);
            log.debug("Getting installable features");
        }
        catch (Exception e) {
            log.error("Getting installable features info failed" + e.getMessage());
            Assert.fail("Getting installable features info failed");
        }
        return featureInfo;
    }

    public FeatureInfo getInstallableFeatureInfoFailureCase(String featureId, String featureVersion)
            throws Exception {
        FeatureInfo featureInfo = null;
        try {
            featureInfo = repositoryAdminServiceStub.getInstallableFeatureInfo(featureId, featureVersion);
            log.error("Getting installable features info without session cookie");
            Assert.fail("Getting installable features info session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());

        }
        return featureInfo;
    }

    public Feature[] getInstallableFeaturesSuccessCase(String location, boolean groupCatagory,
                                                      boolean hideFeature, boolean showFeature) {
        Feature[] feature = null;
        try {
            feature = repositoryAdminServiceStub.getInstallableFeatures(location, groupCatagory,
                                                                        hideFeature, showFeature);
            log.debug("Getting installable features");
        }
        catch (Exception e) {
            log.error("Getting installable features failed" + e.getMessage());
            Assert.fail("Getting installable features failed");
        }
        return feature;
    }

    public Feature[] getInstallableFeaturesFailureCase(String location, boolean groupCatagory,
                                                      boolean hideFeature, boolean showFeature)
            throws Exception {
        Feature[] feature = null;
        try {
            feature = repositoryAdminServiceStub.getInstallableFeatures(location, groupCatagory,
                                                                        hideFeature, showFeature);
            log.error("Getting installable features without session cookie");
            Assert.fail("Getting installable features session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());

        }
        return feature;
    }


}
