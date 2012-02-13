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
import org.wso2.carbon.dataservices.ui.fileupload.stub.DataServiceFileUploaderStub;
import org.wso2.carbon.dataservices.ui.fileupload.stub.ExceptionException;

import javax.activation.DataHandler;
import java.rmi.RemoteException;

public class AdminServiceDataServiceFileUploader {
    private static final Log log = LogFactory.getLog(AdminServiceDataServiceFileUploader.class);

    private final String serviceName = "DataServiceFileUploader";
    private DataServiceFileUploaderStub dataServiceFileUploaderStub;
    private String endPoint;

    public AdminServiceDataServiceFileUploader(String backEndUrl) {
        this.endPoint = backEndUrl + serviceName;
        log.debug("EndPoint :" + endPoint);
        try {
            dataServiceFileUploaderStub = new DataServiceFileUploaderStub(endPoint);
        } catch (AxisFault axisFault) {
            log.error("Initializing DataServiceFileUploaderStub failed : " + axisFault.getMessage());
            Assert.fail("Initializing DataServiceFileUploaderStub failed : " + axisFault.getMessage());
        }
    }


    public void uploadDataServiceFile(String sessionCookie, String fileName, DataHandler dh) {
        new AuthenticateStub().authenticateStub(sessionCookie, dataServiceFileUploaderStub);
        log.debug("path to file :" + dh.getName());
        try {
            Assert.assertEquals("Expected Not Same", "successful", dataServiceFileUploaderStub.uploadService(fileName, "", dh));
            log.info("Artifact Uploaded");
        } catch (RemoteException e) {
            log.error("RemoteException: uploading failed : " + e.getMessage());
            Assert.fail("RemoteException: uploading failed : " + e.getMessage());

        } catch (ExceptionException e) {
            log.error("ExceptionException :uploading failed : " + e.getMessage());
            Assert.fail("ExceptionException :uploading failed : " + e.getMessage());
        }


    }
}
