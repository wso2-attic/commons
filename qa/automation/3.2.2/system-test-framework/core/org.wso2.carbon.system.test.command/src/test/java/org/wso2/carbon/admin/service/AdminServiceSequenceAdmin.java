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
import org.wso2.carbon.sequences.stub.types.SequenceAdminServiceStub;
import org.wso2.carbon.sequences.stub.types.SequenceEditorException;

import javax.activation.DataHandler;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.rmi.RemoteException;

public class AdminServiceSequenceAdmin {
    private static final Log log = LogFactory.getLog(AdminServiceSequenceAdmin.class);

    private final String serviceName = "SequenceAdminService";
    private SequenceAdminServiceStub sequenceAdminServiceStub;
    private String endPoint;

    public AdminServiceSequenceAdmin(String backEndUrl) throws AxisFault {
        this.endPoint = backEndUrl + "/services/" + serviceName;
        sequenceAdminServiceStub = new SequenceAdminServiceStub(endPoint);
    }


    public void addSequence(String sessionCookie, DataHandler dh) throws SequenceEditorException, IOException, XMLStreamException {
        new AuthenticateStub().authenticateStub(sessionCookie, sequenceAdminServiceStub);
        XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(dh.getInputStream());
        //create the builder
        StAXOMBuilder builder = new StAXOMBuilder(parser);
        OMElement sequenceElem = builder.getDocumentElement();
        sequenceAdminServiceStub.addSequence(sequenceElem);

    }

    public void addDynamicSequence(String sessionCookie, String key, OMElement omElement) throws SequenceEditorException, RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, sequenceAdminServiceStub);
        sequenceAdminServiceStub.addDynamicSequence(key, omElement);

    }

    public OMElement getSequence(String sessionCookie, String sequenceName) throws SequenceEditorException, RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, sequenceAdminServiceStub);
        return sequenceAdminServiceStub.getSequence(sequenceName);

    }

    public void deleteSequence(String sessionCookie, String sequenceName) throws SequenceEditorException, RemoteException {
        new AuthenticateStub().authenticateStub(sessionCookie, sequenceAdminServiceStub);
        sequenceAdminServiceStub.deleteSequence(sequenceName);

    }
}
