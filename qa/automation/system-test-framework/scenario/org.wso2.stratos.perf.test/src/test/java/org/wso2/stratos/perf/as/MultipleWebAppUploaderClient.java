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

package org.wso2.stratos.perf.as;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.webAppUtils.TestExceptionHandler;
import org.wso2.carbon.system.test.core.utils.webAppUtils.WebAppUtil;

import java.io.File;

public class MultipleWebAppUploaderClient extends TestTemplate {

    private static final Log log = LogFactory.getLog(MultipleWebAppUploaderClient.class);
    private static final String resourcePath = null;
    private TenantDetails tenantDetails_obj1;
    private TenantDetails tenantDetails_obj2;
    private TenantDetails tenantDetails_obj3;
    private static String HTTP_APPSERVER_URL;

    @Override
    public void init() {
        testClassName = MultipleWebAppUploaderClient.class.getName();
        tenantDetails_obj1 = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("3"));
        tenantDetails_obj2 = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("1"));
        tenantDetails_obj3 = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("2"));
        HTTP_APPSERVER_URL = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME;
    }

    public void runSuccessCase() {
        String resourcePath = ProductConstant.getResourceLocations(ProductConstant.APP_SERVER_NAME);

        Thread t1 = new Thread(new WebAppUploadWorker(tenantDetails_obj1.getTenantName(), tenantDetails_obj1.getTenantPassword(), resourcePath + File.separator + "war" + File.separator + "Calendar.war"));
        TestExceptionHandler exHandler = new TestExceptionHandler();
        t1.setUncaughtExceptionHandler(exHandler);
        t1.start();

        Thread t2 = new Thread(new WebAppUploadWorker(tenantDetails_obj2.getTenantName(), tenantDetails_obj2.getTenantPassword(), resourcePath + File.separator + "war" + File.separator + "myServletWAR.war"));
        t2.setUncaughtExceptionHandler(exHandler);
        t2.start();

        Thread t3 = new Thread(new WebAppUploadWorker(tenantDetails_obj3.getTenantName(), tenantDetails_obj3.getTenantPassword(), resourcePath + File.separator + "war" + File.separator + "sample.war"));
        t3.setUncaughtExceptionHandler(exHandler);
        t3.start();

        //wait for thread to finish
        try {
            t1.join();
            t2.join();
            t3.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (exHandler.throwable != null) {
            exHandler.throwable.printStackTrace();
            fail(exHandler.throwable.getMessage());
        }

        WebAppUtil.waitForWebAppDeployment(HTTP_APPSERVER_URL + "/t/" + tenantDetails_obj1.getTenantDomain() + "/webapps/Calendar/Calendar.html", "GWT Calendar");
        WebAppUtil.waitForWebAppDeployment(HTTP_APPSERVER_URL + "/t/" + tenantDetails_obj2.getTenantDomain() + "/webapps/myServletWAR/hello", "from HelloServlet");
        WebAppUtil.waitForWebAppDeployment(HTTP_APPSERVER_URL + "/t/" + tenantDetails_obj3.getTenantDomain() + "/webapps/sample/hello", "Sample Application Servlet");
    }

    @Override
    public void cleanup() {

        new WebAppUploadWorker("admin123@wso2manualQA0006.org", "admin123", resourcePath + File.separator + "war" + File.separator + "Calendar.war").deleteWebApp("Calendar.war");
        new WebAppUploadWorker("admin123@wso2manualQA0002.org", "admin123", resourcePath + File.separator + "war" + File.separator + "myServletWAR.war").deleteWebApp("myServletWAR.war");
        new WebAppUploadWorker("admin123@wso2manualQA0004.org", "admin123", resourcePath + File.separator + "war" + File.separator + "sample.war").deleteWebApp("sample.war");

        WebAppUtil.waitForWebAppUnDeployment(HTTP_APPSERVER_URL + "/t/" + tenantDetails_obj1.getTenantDomain() + "/webapps/Calendar/Calendar.html", "GWT Calendar");
        WebAppUtil.waitForWebAppUnDeployment(HTTP_APPSERVER_URL + "/t/" + tenantDetails_obj2.getTenantDomain() + "/webapps/myServletWAR/hello", "from HelloServlet");
        WebAppUtil.waitForWebAppUnDeployment(HTTP_APPSERVER_URL + "/t/" + tenantDetails_obj3.getTenantDomain() + "/webapps/sample/hello", "Sample Application Servlet");
    }
}
