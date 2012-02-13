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
import org.wso2.carbon.endpoint.stub.types.EndpointAdminEndpointAdminException;
import org.wso2.carbon.endpoint.stub.types.EndpointAdminStub;

import javax.activation.DataHandler;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.rmi.RemoteException;

public class AdminServiceEndPointAdmin {
    private static final Log log = LogFactory.getLog(AdminServiceEndPointAdmin.class);

    private final String serviceName = "EndpointAdmin";
    private EndpointAdminStub endpointAdminStub;
    private String endPoint;

    public AdminServiceEndPointAdmin(String backEndUrl) throws AxisFault {
        this.endPoint = backEndUrl + "/services/" + serviceName;
        endpointAdminStub = new EndpointAdminStub(endPoint);
    }


    public void addEndPoint(String sessionCookie, DataHandler dh) throws EndpointAdminEndpointAdminException, IOException, XMLStreamException {
        new AuthenticateStub().authenticateStub(sessionCookie, endpointAdminStub);
        XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(dh.getInputStream());
        //create the builder
        StAXOMBuilder builder = new StAXOMBuilder(parser);
        OMElement endPointElem = builder.getDocumentElement();

        endpointAdminStub.addEndpoint(endPointElem.toString());
    }

    public void deleteEndPoint(String sessionCookie, String endPointName) throws EndpointAdminEndpointAdminException, RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, endpointAdminStub);
        endpointAdminStub.deleteEndpoint(endPointName);
    }

    public String getEndPoint(String sessionCookie, String endPointName) throws EndpointAdminEndpointAdminException, RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, endpointAdminStub);
        return endpointAdminStub.getEndpoint(endPointName);
    }
}
