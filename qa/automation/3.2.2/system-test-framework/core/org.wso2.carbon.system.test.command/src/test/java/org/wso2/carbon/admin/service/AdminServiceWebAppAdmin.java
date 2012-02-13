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
package org.wso2.carbon.admin.service;

import junit.framework.Assert;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.webapp.mgt.stub.WebappAdminStub;
import org.wso2.carbon.webapp.mgt.stub.types.carbon.WebappUploadData;

import javax.activation.DataHandler;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

public class AdminServiceWebAppAdmin {

    private final Log log = LogFactory.getLog(AdminServiceWebAppAdmin.class);

    private final String serviceName = "WebappAdmin";
    private WebappAdminStub webappAdminStub;
    private String endPoint;
    private String filePath;

    public AdminServiceWebAppAdmin(String backendUrl, String filePath) {
        this.endPoint = backendUrl + "/" + serviceName;
        this.filePath = filePath;
        try {
            webappAdminStub = new WebappAdminStub(endPoint);
        } catch (AxisFault axisFault) {
            log.error("Initialization of webappAdminStub initialization failed: " + axisFault.getMessage());
            Assert.fail("Initialization of webappAdminStub initialization failed: " + axisFault.getMessage());
        }
    }

    public void warFileUplaoder(String sessionCookie) {
        File file = new File(filePath);
        String fileName = file.getName();
        URL url = null;
        try {
            url = new URL("file://" + filePath);
        } catch (MalformedURLException e) {
            e.printStackTrace();  //TO-DO
        }
        DataHandler dh = new DataHandler(url);
        WebappUploadData webApp;
        webApp = new WebappUploadData();
        webApp.setFileName(fileName);
        webApp.setDataHandler(dh);
        new AuthenticateStub().authenticateStub(sessionCookie, webappAdminStub);
        try {
            Assert.assertTrue("webapp upload unsuccessful", webappAdminStub.uploadWebapp(new WebappUploadData[]{webApp}));
        } catch (RemoteException e) {
            log.error("Thrown RemoteException while uploading webapp :" + e.getMessage());
            Assert.fail("Thrown RemoteException while uploading webapp :" + e.getMessage());
        }

    }

    public void deleteWebAppFile(String sessionCookie, String fileName) {
        new AuthenticateStub().authenticateStub(sessionCookie, webappAdminStub);
        try {
            webappAdminStub.deleteStartedWebapps(new String[]{fileName});
        } catch (RemoteException e) {
            log.error("Thrown RemoteException while deleting webapp :" + e.getMessage());
            Assert.fail("Thrown RemoteException while deleting webapp :" + e.getMessage());
        }
    }

}
