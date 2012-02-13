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

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.module.mgt.stub.ModuleAdminServiceModuleMgtExceptionException;
import org.wso2.carbon.module.mgt.stub.ModuleAdminServiceStub;
import org.wso2.carbon.module.mgt.stub.types.ModuleUploadData;
import org.wso2.carbon.server.admin.stub.Exception;

import javax.activation.DataHandler;
import java.rmi.RemoteException;

public class AdminServiceModuleAdminService {
    private static final Log log = LogFactory.getLog(AdminServiceModuleAdminService.class);

    private final String serviceName = "ModuleAdminService";
    private ModuleAdminServiceStub moduleAdminServiceStub;
    private AdminServiceCarbonServerAdmin adminServiceCarbonServerAdmin;
    private String endPoint;

    public AdminServiceModuleAdminService(String backEndUrl) throws AxisFault {
        this.endPoint = backEndUrl + serviceName;
        moduleAdminServiceStub = new ModuleAdminServiceStub(endPoint);
        adminServiceCarbonServerAdmin = new AdminServiceCarbonServerAdmin(backEndUrl);
    }

    public void uploadModule(String sessionCookie, DataHandler dh) throws RemoteException, Exception {

        new AuthenticateStub().authenticateStub(sessionCookie, moduleAdminServiceStub);
        ModuleUploadData moduleUploadData = new ModuleUploadData();
        moduleUploadData.setFileName(dh.getName().substring(dh.getName().lastIndexOf('/') + 1));
        moduleUploadData.setDataHandler(dh);
        log.info((moduleAdminServiceStub.uploadModule(new ModuleUploadData[]{moduleUploadData})));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        adminServiceCarbonServerAdmin.restartGracefully(sessionCookie);
        try {
            Thread.sleep(40000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void deleteModule(String sessionCookie, String moduleId) throws ModuleAdminServiceModuleMgtExceptionException, RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, moduleAdminServiceStub);
        log.info(moduleAdminServiceStub.removeModule(moduleId));

    }


}
