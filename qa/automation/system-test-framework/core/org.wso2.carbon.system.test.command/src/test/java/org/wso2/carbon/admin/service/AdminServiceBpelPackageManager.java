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
import org.wso2.carbon.bpel.stub.mgt.BPELPackageManagementServiceStub;
import org.wso2.carbon.bpel.stub.mgt.PackageManagementException;
import org.wso2.carbon.bpel.stub.mgt.types.DeployedPackagesPaginated;
import org.wso2.carbon.bpel.stub.mgt.types.Package_type0;
import org.wso2.carbon.bpel.stub.upload.types.UploadedFileItem;

import javax.activation.DataHandler;
import java.rmi.RemoteException;

public class AdminServiceBpelPackageManager {
    String ServiceEndPoint = null;
    String SessionCookie = null;
    private static final Log log = LogFactory.getLog(AdminServiceBpelUploader.class);
    BPELPackageManagementServiceStub bpelPackageManagementServiceStub;

    public AdminServiceBpelPackageManager(String serviceEndPoint, String sessionCookie) {
        this.ServiceEndPoint = serviceEndPoint;
        this.SessionCookie = sessionCookie;
    }

    private BPELPackageManagementServiceStub setPackageManagementStub() {
        final String packageMgtServiceUrl = ServiceEndPoint + "BPELPackageManagementService";
        AuthenticateStub authenticateStub = new AuthenticateStub();
        BPELPackageManagementServiceStub packageManagementServiceStub = null;
        try {

            packageManagementServiceStub = new BPELPackageManagementServiceStub(packageMgtServiceUrl);
            authenticateStub.authenticateStub(SessionCookie, packageManagementServiceStub);

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            Assert.fail(axisFault.getMessage());
        }
        return packageManagementServiceStub;
    }

    private UploadedFileItem getUploadedFileItem(DataHandler dataHandler, String fileName, String fileType) {
        UploadedFileItem uploadedFileItem = new UploadedFileItem();
        uploadedFileItem.setDataHandler(dataHandler);
        uploadedFileItem.setFileName(fileName);
        uploadedFileItem.setFileType(fileType);

        return uploadedFileItem;
    }

    public void undeployBPEL(String packageName) {
        try {
            bpelPackageManagementServiceStub = this.setPackageManagementStub();
            bpelPackageManagementServiceStub.undeployBPELPackage(packageName);
            Thread.sleep(10000);
            DeployedPackagesPaginated deployedPackages = bpelPackageManagementServiceStub.
                    listDeployedPackagesPaginated(0);
            boolean packageUndeployed = true;
            try {
                for (Package_type0 bpelPackage : deployedPackages.get_package()) {
                    if (bpelPackage.getName().equals(packageName)) {

                        packageUndeployed = false;
                        Assert.fail("Service is not undeployed");
                        log.error("Service stilll exists, Undeployment failed");
                    }
                }
            } catch (NullPointerException e) {
                System.out.println(packageName + " has undeployed successfully");
            }
            Assert.assertFalse(packageName + " undeplyment failed", !packageUndeployed);
        } catch (AxisFault axisFault) {
            log.error("Axis fault" + axisFault.getMessage());
            Assert.fail(axisFault.getMessage());
        } catch (RemoteException e) {
            log.error("Connection failed " + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (InterruptedException e) {
            log.error("Deployment Interuppted " + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (PackageManagementException e) {
            log.error("Package management failed" + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    public boolean checkProcessDeployment(String packageName) {
        boolean packageDeployed = false;
        bpelPackageManagementServiceStub = this.setPackageManagementStub();
        try {
            DeployedPackagesPaginated deployedPackages = bpelPackageManagementServiceStub.
                    listDeployedPackagesPaginated(0);
            packageDeployed = false;
            for (Package_type0 bpelPackage : deployedPackages.get_package()) {
                if (bpelPackage.getName().equals(packageName)) {
                    System.out.println(packageName + " has deployed successfully");
                    packageDeployed = true;
                }
            }
            Assert.assertFalse(packageName + " deployment failed", !packageDeployed);
        } catch (RemoteException e) {
            log.error("Connection failed " + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (PackageManagementException e) {
            log.error("Package management failed" + e.getMessage());
            Assert.fail(e.getMessage());
        }
        return packageDeployed;
    }

}


