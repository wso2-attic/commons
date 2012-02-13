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
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.bpel.stub.mgt.BPELPackageManagementServiceStub;
import org.wso2.carbon.bpel.stub.upload.BPELUploaderStub;
import org.wso2.carbon.bpel.stub.upload.types.UploadedFileItem;

import javax.activation.DataHandler;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class AdminServiceBpelUploader {
    String ServiceEndPoint = null;
    private static final Log log = LogFactory.getLog(AdminServiceBpelUploader.class);
    BPELPackageManagementServiceStub bpelPackageManagementServiceStub;

    public AdminServiceBpelUploader(String serviceEndPoint) {
        this.ServiceEndPoint = serviceEndPoint;
    }

    public boolean deployBPEL(String packageName, String serviceName, String sessionCookie) {

        final String uploaderServiceURL = ServiceEndPoint + "BPELUploader";
        AdminServiceBpelPackageManager manager = new AdminServiceBpelPackageManager(ServiceEndPoint, sessionCookie);


        boolean success = false;
        AuthenticateStub authenticateStub = new AuthenticateStub();
        try {
            BPELUploaderStub bpelUploaderStub = new BPELUploaderStub(uploaderServiceURL);
            authenticateStub.authenticateStub(sessionCookie, bpelUploaderStub);
            deployPackage(packageName, serviceName, bpelUploaderStub);
            Thread.sleep(5000);
            success = manager.checkProcessDeployment(packageName);
            Assert.assertTrue("Service did not deployed successfully", success);

        } catch (AxisFault axisFault) {
            log.error("Axis fault" + axisFault.getMessage());
            Assert.fail(axisFault.getMessage());
        } catch (InterruptedException e) {
            log.error("Deployment Interuppted " + e.getMessage());
            Assert.fail(e.getMessage());
        }
        return success;
    }
    public boolean deployBPEL(String packageName,String dirPath, String serviceName, String sessionCookie) {

        final String uploaderServiceURL = ServiceEndPoint + "BPELUploader";
        AdminServiceBpelPackageManager manager = new AdminServiceBpelPackageManager(ServiceEndPoint, sessionCookie);


        boolean success = false;
        AuthenticateStub authenticateStub = new AuthenticateStub();
        try {
            BPELUploaderStub bpelUploaderStub = new BPELUploaderStub(uploaderServiceURL);
            authenticateStub.authenticateStub(sessionCookie, bpelUploaderStub);
            deployPackage(packageName, serviceName,dirPath, bpelUploaderStub);
            Thread.sleep(5000);
            success = manager.checkProcessDeployment(packageName);
            Assert.assertTrue("Service did not deployed successfully", success);

        } catch (AxisFault axisFault) {
            log.error("Axis fault" + axisFault.getMessage());
            Assert.fail(axisFault.getMessage());
        } catch (InterruptedException e) {
            log.error("Deployment Interuppted " + e.getMessage());
            Assert.fail(e.getMessage());
        }
        return success;
    }

    private UploadedFileItem getUploadedFileItem(DataHandler dataHandler, String fileName, String fileType) {
        UploadedFileItem uploadedFileItem = new UploadedFileItem();
        uploadedFileItem.setDataHandler(dataHandler);
        uploadedFileItem.setFileName(fileName);
        uploadedFileItem.setFileType(fileType);

        return uploadedFileItem;
    }

    public void deployPackage(String packageName, String serviceName,
                              BPELUploaderStub bpelUploaderStub) {
        try {
            String sampleArchiveName = packageName + ".zip";
            File bpelZipArchive = new File(ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION + File.separator + 
                    "artifacts" + File.separator + "BPS" + File.separator + "bpel" + File.separator + sampleArchiveName);
            UploadedFileItem[] uploadedFileItems = new UploadedFileItem[1];
            uploadedFileItems[0] = getUploadedFileItem(new DataHandler(bpelZipArchive.toURI().toURL()),
                    sampleArchiveName,
                    "zip");
            System.out.println("Deploying " + sampleArchiveName);

            bpelUploaderStub.uploadService(uploadedFileItems);

            Thread.sleep(5000);
        } catch (RemoteException e) {
            log.error("Connection Failed" + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (MalformedURLException e) {
            log.error("Connection Failed" + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (InterruptedException e) {
            log.error("Deployment Interuppted " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }
         public void deployPackage(String packageName, String serviceName,String resourceDir,
                              BPELUploaderStub bpelUploaderStub) {
        try {
            String sampleArchiveName = packageName + ".zip";
            System.out.println(System.getProperty("bps.sample.location"));
            File bpelZipArchive = new File(System.getProperty("bps.sample.location") + File.separatorChar+resourceDir+File.separatorChar + sampleArchiveName);
            UploadedFileItem[] uploadedFileItems = new UploadedFileItem[1];
            uploadedFileItems[0] = getUploadedFileItem(new DataHandler(bpelZipArchive.toURI().toURL()),
                    sampleArchiveName,
                    "zip");
            System.out.println("Deploying " + sampleArchiveName);

            bpelUploaderStub.uploadService(uploadedFileItems);

            Thread.sleep(5000);
        } catch (RemoteException e) {
            log.error("Connection Failed" + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (MalformedURLException e) {
            log.error("Connection Failed" + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (InterruptedException e) {
            log.error("Deployment Interuppted " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }
}
