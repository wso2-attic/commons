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
import org.wso2.carbon.bpel.stub.mgt.ProcessManagementException;
import org.wso2.carbon.bpel.stub.mgt.ProcessManagementServiceStub;
import org.wso2.carbon.bpel.stub.mgt.types.LimitedProcessInfoType;
import org.wso2.carbon.bpel.stub.mgt.types.PaginatedProcessInfoList;
import org.wso2.carbon.bpel.stub.mgt.types.ProcessInfoType;
import org.wso2.carbon.bpel.stub.mgt.types.ProcessStatus;

import javax.xml.namespace.QName;
import java.rmi.RemoteException;
import java.util.LinkedList;

public class AdminServiceBpelProcessManager {
    String ServiceEndPoint = null;
    String SessionCookie = null;
    private static final Log log = LogFactory.getLog(AdminServiceBpelUploader.class);

    ProcessManagementServiceStub processManagementServiceStub = null;


    public AdminServiceBpelProcessManager(String serviceEndPoint, String sessionCookie) {
        this.ServiceEndPoint = serviceEndPoint;
        this.SessionCookie = sessionCookie;
    }

    private ProcessManagementServiceStub setProcessManagementStub() {
        final String serviceMgtServiceUrl = ServiceEndPoint + "ProcessManagementService";
        AuthenticateStub authenticateStub = new AuthenticateStub();
        ProcessManagementServiceStub processManagementServiceStub = null;
        try {

            processManagementServiceStub = new ProcessManagementServiceStub(serviceMgtServiceUrl);
            authenticateStub.authenticateStub(SessionCookie, processManagementServiceStub);

        } catch (AxisFault axisFault) {
            log.error("Process management failed" + axisFault.getMessage());
            Assert.fail(axisFault.getMessage());
        }
        return processManagementServiceStub;
    }

    public void setStatus(String processID, String status) {
        processManagementServiceStub = this.setProcessManagementStub();
        try {
            if (ProcessStatus.ACTIVE.getValue().equals(status.toUpperCase())) {
                processManagementServiceStub.activateProcess(QName.valueOf(processID));
            } else if (ProcessStatus.RETIRED.getValue().equals(status.toUpperCase())) {
                processManagementServiceStub.retireProcess(QName.valueOf(processID));
            } else {
                Assert.fail("Invalid process status " + status);
            }

        } catch (ProcessManagementException e) {
            log.error("Process management failed" + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (RemoteException e) {
            log.error("Connection failed " + e.getMessage());
            Assert.fail(e.getMessage());
        }
    }

    public String getStatus(String processID) {
        String status = null;
        processManagementServiceStub = this.setProcessManagementStub();
        try {
            ProcessInfoType processInfo = processManagementServiceStub.
                    getProcessInfo(QName.valueOf(processID));
            status = processInfo.getStatus().getValue().toString();

        } catch (ProcessManagementException e) {
            log.error("Process management failed" + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (RemoteException e) {
            log.error("Connection failed " + e.getMessage());
            Assert.fail(e.getMessage());
        }
        return status;
    }

    public String getProcessId(String packageName) {
        processManagementServiceStub = this.setProcessManagementStub();
        String processId = null;
        final String processFilter = "name}}";
        final String processListOrderBy = "-deployed";
        try {
            String[] processList = processManagementServiceStub.getAllProcesses("y");
            Assert.assertFalse("Process list cannot be empty", processList.length == 0);

            boolean processFound = false;
            for (String id : processList) {
                if (id.contains(packageName + "-")) {
                    processFound = true;
                    processId = id;
                }
            }
            Assert.assertFalse("Process: " + processId + " cannot be found", !processFound);

        } catch (RemoteException e) {
            log.error("Process management failed" + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (ProcessManagementException e) {
            log.error("Process management failed" + e.getMessage());
            Assert.fail(e.getMessage());
        }

        return processId;
    }

    public PaginatedProcessInfoList getProcessInfo(String packageName) {
        processManagementServiceStub = this.setProcessManagementStub();

        PaginatedProcessInfoList filteredProcess = new PaginatedProcessInfoList();
        final String processFilter = "name}}* namespace=*";
        final String processListOrderBy = "-deployed";
        try {

            PaginatedProcessInfoList processes =
                    processManagementServiceStub.getPaginatedProcessList(processFilter,
                            processListOrderBy, 0);
            Assert.assertFalse("Process list cannot be empty", !processes.isProcessInfoSpecified() ||
                    processes.getProcessInfo().length == 0);

            for (LimitedProcessInfoType processInfo : processes.getProcessInfo()) {
                if (processInfo.getPid().contains(packageName + "-")) {
                    filteredProcess.addProcessInfo(processInfo);
                }
            }

        } catch (RemoteException e) {
            log.error("Process management failed" + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (ProcessManagementException e) {
            log.error("Process management failed" + e.getMessage());
            Assert.fail(e.getMessage());
        }
        return filteredProcess;
    }


    public LinkedList<String> getProcessInfoList(String packageName) {
        processManagementServiceStub = this.setProcessManagementStub();

        LinkedList<String> filteredProcess = new LinkedList<String>();
        try {
            String[] processList = processManagementServiceStub.getAllProcesses("y");
            Assert.assertFalse("Process list cannot be empty", processList.length == 0);

            for (String id : processList) {
                if (id.contains(packageName + "-")) {
                    filteredProcess.add(id);
                }
            }

        } catch (RemoteException e) {
            log.error("Process management failed" + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (ProcessManagementException e) {
            log.error("Process management failed" + e.getMessage());
            Assert.fail(e.getMessage());
        }
        return filteredProcess;
    }
}



