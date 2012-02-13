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
package org.wso2.stratos.automation.test.as;

import junit.framework.AssertionFailedError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceWebAppAdmin;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.webAppUtils.WebAppUtil;

import java.io.File;

/*
Deploy Axis2 service with the schema of the wsdl is stored in governance service of the tenant
 */
public class FaultyWebAppDeploymentTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(FaultyWebAppDeploymentTest.class);
    private TenantDetails tenantDetails;

    @Override
    public void init() {
        testClassName = FaultyWebAppDeploymentTest.class.getName();
        tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("12"));
    }

    @Override
    public void runSuccessCase() {
        String webappContext = "/SimpleServlet-faulty/simple-servlet";
        try {
            WebAppUtil.waitForWebAppDeployment(getWebAppURL(webappContext), "Hello");
        } catch (AssertionFailedError e) {
            log.info("Faulty webapp not deployed");
            System.out.println("Fail to deploy faulty webapp");
        }

        //Redeploy correct web app
        log.info("Redeploy valid webapp");
        String webAppArtifactPath = ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION + File.separator + "artifacts" +
                File.separator + "AS" + File.separator + "war" + File.separator + "duplicateWar" + File.separator +
                "SimpleServlet-faulty.war";

        deployWebapp(webAppArtifactPath);
        WebAppUtil.waitForWebAppDeployment(getWebAppURL(webappContext), "Hello");
        log.info("Successfully redeployed valid webapp");
    }

    @Override
    public void cleanup() {
    }

    private void deployWebapp(String filePath) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(FrameworkSettings.APP_BACKEND_URL);
        String sessionCookies = loginClient.login(tenantDetails.getTenantName(), tenantDetails.getTenantPassword(),
                FrameworkSettings.APP_BACKEND_URL);
        AdminServiceWebAppAdmin AdminServiceWebAppAdmin = new AdminServiceWebAppAdmin(FrameworkSettings.APP_BACKEND_URL);
        AdminServiceWebAppAdmin.warFileUplaoder(sessionCookies, filePath);
    }

    private String getWebAppURL(String webappContext) {
        String webAppURL;
        if (FrameworkSettings.STRATOS_TEST) {
            webAppURL = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/t/" + tenantDetails.getTenantDomain() +
                    "/webapps" + webappContext;
        } else {
            webAppURL = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + ":" + FrameworkSettings.APP_SERVER_HTTP_PORT + webappContext;
        }
        return webAppURL;
    }

}
