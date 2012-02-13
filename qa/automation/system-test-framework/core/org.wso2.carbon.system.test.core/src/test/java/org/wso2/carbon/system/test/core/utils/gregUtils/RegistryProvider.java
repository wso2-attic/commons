/*
* Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.system.test.core.utils.gregUtils;

import junit.framework.Assert;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;

import java.io.File;


public class RegistryProvider {
    private static final Log log = LogFactory.getLog(RegistryProvider.class);
    private static WSRegistryServiceClient registry = null;
    private static Registry governance = null;

    public WSRegistryServiceClient getRegistry(String tenantId) {
        //Tenant details
        String userName;
        String password;
        String tenantDomain;
        ConfigurationContext configContext;
        String serverURL;

        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId));
        userName = tenantDetails.getTenantName();
        password = tenantDetails.getTenantPassword();
        tenantDomain = tenantDetails.getTenantDomain();
        serverURL = getServiceURL(tenantDomain);


        String resourcePath = ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION;
        String axis2Repo = resourcePath + File.separator + "moduleClient" + File.separator + "client";
        String axis2Conf = resourcePath + File.separator + "productConfigFiles" + File.separator + "axis2_client.xml";

        try {
            configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(axis2Repo, axis2Conf);
            registry = new WSRegistryServiceClient(serverURL, userName, password, configContext);
            log.info("WS Registry -Login Success");
        } catch (AxisFault axisFault) {
            log.error("Unable to initialize WSRegistryServiceClient :" + axisFault.getMessage());
            Assert.fail("Unable to initialize WSRegistryServiceClient :" + axisFault.getMessage());
        } catch (RegistryException e) {
            log.error("Unable to initialize WSRegistryServiceClient:" + e.getMessage());
            Assert.fail("Unable to initialize WSRegistryServiceClient:" + e.getMessage());
        }
        return registry;
    }

    public Registry getGovernance(WSRegistryServiceClient registry, String tenantId) {
        //Tenant details
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId));
        String userName = tenantDetails.getTenantName();
        setSystemProperties();

        try {
            governance = GovernanceUtils.getGovernanceUserRegistry(registry, userName);
        } catch (RegistryException e) {
            log.error("getGovernance Registry Exception thrown:" + e.getMessage());
            Assert.fail("getGovernance Registry Exception thrown:" + e.getMessage());
        }
        return governance;

    }

    private static void setSystemProperties() {
        System.setProperty("javax.net.ssl.trustStore", FrameworkSettings.getKeyStoreLocation());
        System.setProperty("javax.net.ssl.trustStorePassword", FrameworkSettings.getKeyStorePassword());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        System.setProperty("carbon.repo.write.mode", "true");
    }

    private static String getServiceURL(String tenantDomain) {
        String serverURL;
        if (FrameworkSettings.getStratosTestStatus()) {
            serverURL = "https://" + FrameworkSettings.GREG_SERVER_HOST_NAME + "/t/" + tenantDomain + File.separator + "services" + File.separator;
        } else {
            serverURL = FrameworkSettings.GREG_BACKEND_URL;
        }
        log.info("Server URL is :" + serverURL);
        return serverURL;
    }
}
