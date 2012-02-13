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
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.mediation.configadmin.stub.ConfigServiceAdminStub;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import javax.servlet.ServletException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.StringReader;

public class AdminServiceConfigServiceAdmin {

    private static final Log log = LogFactory.getLog(AdminServiceConfigServiceAdmin.class);

    private final String serviceName = "ConfigServiceAdmin";
    private ConfigServiceAdminStub configServiceAdminStub;

    public AdminServiceConfigServiceAdmin(String backEndUrl, String sessionCookie) {
        String endPoint = backEndUrl + serviceName;
        try {
            configServiceAdminStub = new ConfigServiceAdminStub(endPoint);
        } catch (AxisFault axisFault) {
            log.error(": " + axisFault.getMessage());
            Assert.fail("ConfigServiceAdminStub Initialization fail: " + axisFault.getMessage());
        }
        new AuthenticateStub().authenticateStub(sessionCookie, configServiceAdminStub);
    }


    public boolean updateConfiguration(String configElement) {
        boolean updateStatus = false;
        try {
            updateStatus = configServiceAdminStub.updateConfiguration(createOMElement(configElement));
        } catch (Exception e) {
            handleException("Unable to update configuration " + e.getMessage());
        }
        return updateStatus;
    }

    private static OMElement createOMElement(String xml) throws ServletException {
        try {

            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xml));
            StAXOMBuilder builder = new StAXOMBuilder(reader);
            return builder.getDocumentElement();

        } catch (XMLStreamException e) {
            handleException("Invalid XML " + e.getMessage());
        }
        return null;
    }

    private static void handleException(String msg) {
        log.error(msg);

    }
}
