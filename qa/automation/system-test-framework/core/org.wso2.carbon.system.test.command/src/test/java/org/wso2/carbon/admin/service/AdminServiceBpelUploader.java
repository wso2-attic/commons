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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.bpel.stub.mgt.BPELPackageManagementServiceStub;
import org.wso2.carbon.bpel.stub.upload.BPELUploaderStub;
import org.wso2.carbon.bpel.stub.upload.types.UploadedFileItem;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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


    public boolean deployBPEL(String packageName, String sessionCookie) throws RemoteException, MalformedURLException, InterruptedException {

        final String uploaderServiceURL = ServiceEndPoint + "BPELUploader";
        AdminServiceBpelPackageManager manager = new AdminServiceBpelPackageManager(ServiceEndPoint, sessionCookie);

        boolean success = true;
        AuthenticateStub authenticateStub = new AuthenticateStub();
        BPELUploaderStub bpelUploaderStub = new BPELUploaderStub(uploaderServiceURL);
        authenticateStub.authenticateStub(sessionCookie, bpelUploaderStub);
        deployPackage(packageName, bpelUploaderStub);
        Thread.sleep(10000);
        success = manager.checkProcessDeployment(packageName);
        return success;
    }

    public boolean deployBPEL(String packageName, String dirPath, String sessionCookie) throws RemoteException, InterruptedException {

        final String uploaderServiceURL = ServiceEndPoint + "BPELUploader";
        AdminServiceBpelPackageManager manager = new AdminServiceBpelPackageManager(ServiceEndPoint, sessionCookie);
        boolean success = false;
        AuthenticateStub authenticateStub = new AuthenticateStub();
        BPELUploaderStub bpelUploaderStub = new BPELUploaderStub(uploaderServiceURL);
        authenticateStub.authenticateStub(sessionCookie, bpelUploaderStub);
        deployPackage(packageName, dirPath, bpelUploaderStub);
        Thread.sleep(10000);
        success = manager.checkProcessDeployment(packageName);
        Assert.assertTrue("Service did not deployed successfully", success);


        return success;
    }

    private UploadedFileItem getUploadedFileItem(DataHandler dataHandler, String fileName, String fileType) {
        UploadedFileItem uploadedFileItem = new UploadedFileItem();
        uploadedFileItem.setDataHandler(dataHandler);
        uploadedFileItem.setFileName(fileName);
        uploadedFileItem.setFileType(fileType);

        return uploadedFileItem;
    }

    public void deployPackage(String packageName,
                              BPELUploaderStub bpelUploaderStub) throws MalformedURLException, RemoteException, InterruptedException {
        String sampleArchiveName = packageName + ".zip";
        File bpelZipArchive = new File(System.getProperty("system.test.sample.location") + File.separator +
                "artifacts" + File.separator + "BPS" + File.separator + "bpel" + File.separator + sampleArchiveName);
        UploadedFileItem[] uploadedFileItems = new UploadedFileItem[1];
        uploadedFileItems[0] = getUploadedFileItem(new DataHandler(bpelZipArchive.toURI().toURL()),
                sampleArchiveName,
                "zip");
        System.out.println("Deploying " + sampleArchiveName);
        bpelUploaderStub.uploadService(uploadedFileItems);
        Thread.sleep(10000);
    }

    public void deployPackage(String packageName, String resourceDir,
                              BPELUploaderStub bpelUploaderStub) throws RemoteException, InterruptedException {

        String sampleArchiveName = packageName + ".zip";
        System.out.println(resourceDir + File.separator + sampleArchiveName);
        DataSource bpelDataSource = new FileDataSource(resourceDir + File.separator + sampleArchiveName);
        UploadedFileItem[] uploadedFileItems = new UploadedFileItem[1];
        uploadedFileItems[0] = getUploadedFileItem(new DataHandler(bpelDataSource),
                sampleArchiveName,
                "zip");
        System.out.println("Deploying " + sampleArchiveName);
        bpelUploaderStub.uploadService(uploadedFileItems);
        Thread.sleep(10000);
    }
}
