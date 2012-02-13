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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.server.admin.stub.ServerAdminStub;
import org.wso2.carbon.task.stub.TaskAdminStub;
import org.wso2.carbon.task.stub.TaskManagementException;

import javax.activation.DataHandler;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.rmi.RemoteException;

public class AdminServiceTaskAdmin {
    private static final Log log = LogFactory.getLog(AdminServiceTaskAdmin.class);

    private final String serviceName = "TaskAdmin";
    private TaskAdminStub taskAdminStub;
    private String endPoint;

    public AdminServiceTaskAdmin(String backEndUrl) throws AxisFault {
        this.endPoint = backEndUrl + "/services/" + serviceName;
        taskAdminStub = new TaskAdminStub(endPoint);
    }

    public void addTask(String sessionCookie, DataHandler dh) throws TaskManagementException, IOException, XMLStreamException {
        new AuthenticateStub().authenticateStub(sessionCookie, taskAdminStub);
        XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(dh.getInputStream());
        //create the builder
        StAXOMBuilder builder = new StAXOMBuilder(parser);
        OMElement scheduleTaskElem = builder.getDocumentElement();
        taskAdminStub.addTaskDescription(scheduleTaskElem);
    }

    public void deleteTask(String sessionCookie, String name, String group) throws TaskManagementException, RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, taskAdminStub);
        taskAdminStub.deleteTaskDescription(name, group);

    }

    public OMElement getScheduleTask(String sessionCookie, String name, String group) throws TaskManagementException, RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, taskAdminStub);
        return taskAdminStub.getTaskDescription(name, group);

    }

    public static class AdminServiceCarbonServerAdmin {

        private static final Log log = LogFactory.getLog(AdminServiceCarbonServerAdmin.class);

        private final String serviceName = "ServerAdmin";
        private ServerAdminStub serverAdminStub;
        private String endPoint;

        public AdminServiceCarbonServerAdmin(String backEndUrl) throws AxisFault {
            this.endPoint = backEndUrl + "/services/" + serviceName;
            serverAdminStub = new ServerAdminStub(endPoint);
        }

        public void restartGracefully(String sessionCookie) throws Exception, RemoteException {
            new AuthenticateStub().authenticateStub(sessionCookie, serverAdminStub);
            serverAdminStub.restartGracefully();

        }

        public void restart(String sessionCookie) throws Exception, RemoteException {
            new AuthenticateStub().authenticateStub(sessionCookie, serverAdminStub);
            serverAdminStub.restart();

        }

        public void shutdown(String sessionCookie) throws Exception, RemoteException {
            new AuthenticateStub().authenticateStub(sessionCookie, serverAdminStub);
            serverAdminStub.shutdown();

        }

        public void shutdownGracefully(String sessionCookie) throws Exception, RemoteException {
            new AuthenticateStub().authenticateStub(sessionCookie, serverAdminStub);
            serverAdminStub.shutdownGracefully();

        }
    }
}
