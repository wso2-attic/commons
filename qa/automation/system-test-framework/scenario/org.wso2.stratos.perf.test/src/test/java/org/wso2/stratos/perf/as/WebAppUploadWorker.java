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
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceWebAppAdmin;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;


public class WebAppUploadWorker extends Thread {

    private final Log log = LogFactory.getLog(WebAppUploadWorker.class);
    private String userName;
    private String password;
    private String filePath;

    public WebAppUploadWorker(String userName, String password, String filePath) {
        this.userName = userName;
        this.password = password;
        this.filePath = filePath;
        FrameworkSettings.getFrameworkProperties();
    }

    public void run() {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(FrameworkSettings.APP_BACKEND_URL);
        String sessionCookies = loginClient.login(userName, password, FrameworkSettings.APP_BACKEND_URL);
        AdminServiceWebAppAdmin AdminServiceWebAppAdmin = new AdminServiceWebAppAdmin(FrameworkSettings.APP_BACKEND_URL);
        AdminServiceWebAppAdmin.warFileUplaoder(sessionCookies, filePath);
    }

    public void deleteWebApp(String fileName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(FrameworkSettings.APP_BACKEND_URL);
        String sessionCookies = loginClient.login(userName, password, FrameworkSettings.APP_BACKEND_URL);
        AdminServiceWebAppAdmin AdminServiceWebAppAdmin = new AdminServiceWebAppAdmin(FrameworkSettings.APP_BACKEND_URL);
        AdminServiceWebAppAdmin.deleteWebAppFile(sessionCookies, fileName);
    }
}

