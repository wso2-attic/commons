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
import org.wso2.carbon.aarservices.stub.ExceptionException;
import org.wso2.carbon.aarservices.stub.ServiceUploaderStub;
import org.wso2.carbon.aarservices.stub.types.carbon.AARServiceData;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;

import javax.activation.DataHandler;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;


public class AdminServiceAARServiceUploader {
    private static final Log log = LogFactory.getLog(AdminServiceAARServiceUploader.class);

    private final String serviceName = "ServiceUploader";
    private ServiceUploaderStub serviceUploaderStub;
    private String endPoint;

    public AdminServiceAARServiceUploader(String backEndUrl) {
        try {
            this.endPoint = backEndUrl + "/" + serviceName;
            serviceUploaderStub = new ServiceUploaderStub(endPoint);
        } catch (AxisFault axisFault) {
            log.error("Initialization of serviceUploaderStub initialization failed: " + axisFault.getMessage());
            Assert.fail("Initialization of serviceUploaderStub initialization failed: " + axisFault.getMessage());
        }

    }

    public void uploadAARFile(String sessionCookie, String fileName, String filePath, String serviceHierarchy) {
        AARServiceData aarServiceData;

        new AuthenticateStub().authenticateStub(sessionCookie, serviceUploaderStub);

        aarServiceData = new AARServiceData();
        aarServiceData.setFileName(fileName);
        aarServiceData.setDataHandler(createDataHandler(filePath));
        aarServiceData.setServiceHierarchy(serviceHierarchy);

        try {
            serviceUploaderStub.uploadService(new AARServiceData[]{aarServiceData});
        } catch (RemoteException e) {
            log.error("AAR Upload failed with RemoteException : " + e.getMessage());
            Assert.fail("AAR Upload failed with RemoteException : " + e.getMessage());
        } catch (ExceptionException e) {
            log.error("AAR Upload failed due to : " + e.getMessage());
            Assert.fail("AAR Upload failed due to  : " + e.getMessage());
        }
        log.info("Service Uploaded");
    }

    private DataHandler createDataHandler(String filePath) {
        URL url = null;
        try {
            url = new URL("file://" + filePath);
        } catch (MalformedURLException e) {
            log.error("File path URL is invalid" + e.getMessage());
            Assert.fail("File path URL is invalid" + e.getMessage());
        }
        DataHandler dh = new DataHandler(url);
        return dh;
    }
}
