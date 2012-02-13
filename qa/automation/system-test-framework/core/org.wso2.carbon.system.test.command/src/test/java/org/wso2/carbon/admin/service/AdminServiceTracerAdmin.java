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
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.tracer.stub.TracerAdminStub;
import org.wso2.carbon.tracer.stub.types.carbon.MessagePayload;
import org.wso2.carbon.tracer.stub.types.carbon.TracerServiceInfo;

import java.rmi.RemoteException;
import java.text.MessageFormat;

/*
Client class for TracerAdminStub, which implement public methods for tests.
 */
public class AdminServiceTracerAdmin {

    private final Log log = LogFactory.getLog(AdminServiceTracerAdmin.class);

    private TracerAdminStub tracerAdminStub;

    public AdminServiceTracerAdmin(String backendServerURL, String sessionCookie) {
        String serviceName = "TracerAdmin";
        String serviceURL = backendServerURL + serviceName;
        try {
            tracerAdminStub = new TracerAdminStub(serviceURL);
            new AuthenticateStub().authenticateStub(sessionCookie, tracerAdminStub);
        } catch (AxisFault axisFault) {
            log.error("TracerAdminStub initialization fail: " + axisFault.getMessage());
            Assert.fail("TracerAdminStub initialization fail: " + axisFault.getMessage());
        }
    }

    public TracerServiceInfo getMessages(int numberOfMessages, String filter) {
        try {
            TracerServiceInfo tracerServiceInfo = tracerAdminStub.getMessages(numberOfMessages, filter);
            MessagePayload message = tracerServiceInfo.getLastMessage();
            escapeHtml(message);
            return tracerServiceInfo;
        } catch (Exception e) {
            handleException("Cannot get list of tracer messages", e);
        }
        return null;
    }

    public TracerServiceInfo setMonitoring(String flag){
        try {
            return tracerAdminStub.setMonitoring(flag);
        } catch (Exception e) {
            handleException(MessageFormat.format("Cannot set tracer monitoring status",
                    flag), e);
        }
        return null;
    }

    public void clearAllSoapMessages(){
        try {
            tracerAdminStub.clearAllSoapMessages();
        } catch (RemoteException e) {
            handleException("Cannot clear all soap messages", e);
        }
    }

    public MessagePayload getMessage(String serviceName,
                                     String operationName,
                                     long messageSequence) {
        try {
            MessagePayload message = tracerAdminStub.getMessage(serviceName, operationName, messageSequence);
            escapeHtml(message);
            return message;
        } catch (Exception e) {
            handleException(MessageFormat.format("Cannot get tracer messages",
                    messageSequence, serviceName, operationName), e);
        }
        return null;
    }

    private void escapeHtml(MessagePayload message) {
        if (message != null) {
            if (message.getRequest() != null) {
                String req = StringEscapeUtils.escapeHtml(removeXmlProlog(message.getRequest()));
                message.setRequest(req);
            }
            if (message.getResponse() != null) {
                String resp = StringEscapeUtils.escapeHtml(removeXmlProlog(message.getResponse()));
                message.setResponse(resp);
            }
        }
    }

    private String removeXmlProlog(String xml) {
        xml = xml.trim();
        if (xml.indexOf("<?xml") == 0) {
            xml = xml.substring(xml.indexOf(">") + 1);
        }
        return xml;
    }

    private void handleException(String msg, Exception e) {
        log.error(msg, e);
    }
}
