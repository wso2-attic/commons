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

// Provisioning admin stub P2 based command implemantation class

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.component.mgt.ui.ProvisioningAdminServiceStub;
import org.wso2.carbon.component.mgt.ui.RepositoryAdminServiceStub;
import org.wso2.carbon.component.mgt.ui.prov.data.Feature;
import org.wso2.carbon.component.mgt.ui.prov.data.FeatureInfo;
import org.wso2.carbon.component.mgt.ui.prov.data.LicenseInfo;
import org.wso2.carbon.component.mgt.ui.prov.data.ProfileHistory;
import org.wso2.carbon.component.mgt.ui.prov.data.ProvisioningActionInfo;
import org.wso2.carbon.component.mgt.ui.prov.data.ProvisioningActionResultInfo;
import org.wso2.carbon.component.mgt.ui.prov.data.RepositoryInfo;


/**
 *
 */
public class ProvisioningAdminCommand extends TestCase {
    private static final Log log = LogFactory.getLog(ProvisioningAdminCommand.class);
    ProvisioningAdminServiceStub provisioningAdminServiceStub;

    public ProvisioningAdminCommand(ProvisioningAdminServiceStub provisioningAdminServiceStub) {
        this.provisioningAdminServiceStub = provisioningAdminServiceStub;
        log.debug("provisioningAdminServiceStub");
    }

    public Feature[] getAllInstalledFeatureSuccessCase() {
        Feature[] features = null;
        try {
            features = provisioningAdminServiceStub.getAllInstalledFeatures();
            log.debug("Getting all installed features");
        }
        catch (Exception e) {
            log.error("Unable to get installed features" + e.getMessage());
            Assert.fail("Unable to get installed features");
        }
        return features;
    }

    public Feature[] getAllInstalledFeatureFailureCase()
            throws Exception {
        Feature[] features = null;
        try {
            features = provisioningAdminServiceStub.getAllInstalledFeatures();
            log.error("getting all installed features without session cookie");
            Assert.fail("getting all installed features without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());

        }
        return features;
    }

    public FeatureInfo getInstalledFeatureInfoSuccessCase(String featureId, String featureVersion) {
        FeatureInfo featureInfo = null;
        try {
            featureInfo = provisioningAdminServiceStub.getInstalledFeatureInfo(featureId, featureVersion);
            log.debug("Getting installed feature info");
        }
        catch (Exception e) {
            log.error("Unable to get installed feature info" + e.getMessage());
            Assert.fail("Unable to get installed feature info");
        }
        return featureInfo;
    }

    public FeatureInfo getInstalledFeatureInfoFailureCase(String featureId, String featureVersion)
            throws Exception {
        FeatureInfo featureInfo = null;
        try {
            featureInfo = provisioningAdminServiceStub.getInstalledFeatureInfo(featureId, featureVersion);
            log.error("getting installed feature info without session cookie");
            Assert.fail("getting installed feature info without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());
        }
        return featureInfo;
    }

    public FeatureInfo[] getInstalledFeaturePropertySuccessCase(String key, String value) {
        FeatureInfo[] featureInfo = null;
        try {
            featureInfo = provisioningAdminServiceStub.getInstalledFeaturesWithProperty(key, value);
            log.debug("Getting properties of installed feature");
        }
        catch (Exception e) {
            log.error("Unable to get properties of installed feature" + e.getMessage());
            Assert.fail("Unable to get properties of installed feature");
        }
        return featureInfo;
    }

    public FeatureInfo[] getAllInstalledFeaturePropertyFailureCase(String key, String value)
            throws Exception {
        FeatureInfo[] featureInfo = null;
        try {
            featureInfo = provisioningAdminServiceStub.getInstalledFeaturesWithProperty(key, value);
            log.error("getting properties of installed feature without session cookie");
            Assert.fail("getting properties of installed feature without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());
        }
        return featureInfo;
    }

    public LicenseInfo[] getLicensingInfoSuccessCase() {
        LicenseInfo[] licenseInfo = null;
        try {
            licenseInfo = provisioningAdminServiceStub.getLicensingInformation();
            log.debug("Getting licensing infomation");
        }
        catch (Exception e) {
            log.error("Unable to get licensing infomation" + e.getMessage());
            Assert.fail("Unable to get licensing infomation");
        }
        return licenseInfo;
    }

    public LicenseInfo[] getLicensingInfoFailureCase()
            throws Exception {
        LicenseInfo[] licenseInfo = null;
        try {
            licenseInfo = provisioningAdminServiceStub.getLicensingInformation();
            log.error("getting license infomation without session cookie");
            Assert.fail("getting license infomation without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());
        }
        return licenseInfo;
    }

    public ProfileHistory[] getProfileHistorySuccessCase() {
        ProfileHistory[] profileHistories = null;
        try {
            profileHistories = provisioningAdminServiceStub.getProfileHistory();
            log.debug("Getting profile histories");
        }
        catch (Exception e) {
            log.error("Unable to get profile histories" + e.getMessage());
            Assert.fail("Unable to get profile histories");
        }
        return profileHistories;
    }

    public ProfileHistory[] getProfileHistoryFailureCase()
            throws Exception {
        ProfileHistory[] profileHistories = null;
        try {
            profileHistories = provisioningAdminServiceStub.getProfileHistory();
            log.error("getting profile histories without session cookie");
            Assert.fail("getting profile histories without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());
        }
        return profileHistories;
    }

    public void performProvisioningActionSuccessCase(String actionType) {
        try {
            provisioningAdminServiceStub.performProvisioningAction(actionType);
            log.debug("Provisioning action performed");
        }
        catch (Exception e) {
            log.error("Unable to perform provisioning action : " + e.getMessage());
            Assert.fail("Unable to perform provisioning action");
        }
    }

    public void performProvisioningActionFailureCase(String actionType)
            throws Exception {
        try {
            provisioningAdminServiceStub.performProvisioningAction(actionType);
            log.error("Performing provisioning actions without session cookie");
            Assert.fail("Performing provisioning actions without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());
        }
    }

    public void removeAllConsoleFeaturesSuccessCase() {
        try {
            provisioningAdminServiceStub.removeAllConsoleFeatures();
            log.debug("all console features removed");
        }
        catch (Exception e) {
            log.error("Unable to remove all console features" + e.getMessage());
            Assert.fail("Unable to remove all consaole features");
        }
    }

    public void removeAllConsoleFeaturesFailureCase()
            throws Exception {
        try {
            provisioningAdminServiceStub.removeAllConsoleFeatures();
            log.error("remove all console features without session cookie");
            Assert.fail("remove all console features without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());
        }
    }

    public void removeAllServerFeaturesSuccessCase() {
        try {
            provisioningAdminServiceStub.removeAllServerFeatures();
            log.debug("all server features removed");
        }
        catch (Exception e) {
            log.error("Unable to remove all server features" + e.getMessage());
            Assert.fail("Unable to remove all server features");
        }
    }

    public void removeAllServerFeaturesFailureCase()
            throws Exception {
        try {
            provisioningAdminServiceStub.removeAllServerFeatures();
            log.error("remove all server features without session cookie");
            Assert.fail("remove all server features without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());
        }
    }

    public ProvisioningActionResultInfo reviewProvisioningActionSuccessCase(
            ProvisioningActionInfo provisioningActionInfo) {
        ProvisioningActionResultInfo provisioningActionResultInfo = null;
        try {
            provisioningActionResultInfo = provisioningAdminServiceStub.reviewProvisioningAction(provisioningActionInfo);
            log.debug("Review provisioning action info performed");
        }
        catch (Exception e) {
            log.error("Unable to review provisioning action info" + e.getMessage());
            Assert.fail("Unable to review provisioning action info");
        }
        return provisioningActionResultInfo;
    }

    public ProvisioningActionResultInfo reviewProvisioningActionFailureCase(
            ProvisioningActionInfo provisioningActionInfo)
            throws Exception {
        ProvisioningActionResultInfo provisioningActionResultInfo = null;
        try {
            provisioningActionResultInfo = provisioningAdminServiceStub.reviewProvisioningAction(provisioningActionInfo);
            log.error("Review provisioning action info without session cookie");
            Assert.fail("Review provisioning action info without session cookie");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());
        }
        return provisioningActionResultInfo;
    }
}
