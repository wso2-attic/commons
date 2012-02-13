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

public class StopAndRedeployWebAppTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(StopAndRedeployWebAppTest.class);
    private TenantDetails tenantDetails;
    private AdminServiceWebAppAdmin adminServiceWebAppAdmin;
    private String sessionCookie;
    private String webAppName = "SimpleServlet";
    private static String webappContext;

    @Override
    public void init() {
        testClassName = StopAndRedeployWebAppTest.class.getName();
        tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("7"));
        adminServiceWebAppAdmin = new AdminServiceWebAppAdmin(FrameworkSettings.APP_BACKEND_URL);
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(FrameworkSettings.APP_BACKEND_URL);
        sessionCookie = loginClient.login(tenantDetails.getTenantName(), tenantDetails.getTenantPassword(),
                FrameworkSettings.APP_BACKEND_URL);

    }

    @Override
    public void runSuccessCase() {
        webappContext = "/SimpleServlet/simple-servlet";
        WebAppUtil.waitForWebAppDeployment(getWebAppURL(webappContext), "Hello"); //wait for web app deployment
        adminServiceWebAppAdmin.stopWebapps(sessionCookie, webAppName + ".war"); //stop web app

        waitForWebappDeletion(); //wait for web app stop
        log.debug("Webapp stopped");

        deleteWebApp(sessionCookie, webAppName + ".war");//delete web app

        waitForWebappDeletion();//wait for deletion
        log.debug("Webapp deleted successfully");

        String webAppArtifactPath = ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION + File.separator + "artifacts" +
                File.separator + "AS" + File.separator + "war" + File.separator +
                webAppName + ".war";
        deployWebapp(webAppArtifactPath);
        WebAppUtil.waitForWebAppDeployment(getWebAppURL(webappContext), "Hello");
        log.info("Successfully redeployed valid webapp");
    }

    private void waitForWebappDeletion() {
        try {
            WebAppUtil.waitForWebAppUnDeployment(getWebAppURL(webappContext), "Hello");
        } catch (AssertionFailedError e) {
            log.info("Web has been stopped successfully");
            System.out.println("Web has been stopped successfully");
        }
    }

    @Override
    public void cleanup() {
    }

    private void deployWebapp(String filePath) {
        adminServiceWebAppAdmin.warFileUplaoder(sessionCookie, filePath);
    }

    private void deleteWebApp(String sessionCookie, String fileName) {
        adminServiceWebAppAdmin.deleteWebAppFile(sessionCookie, fileName);
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
