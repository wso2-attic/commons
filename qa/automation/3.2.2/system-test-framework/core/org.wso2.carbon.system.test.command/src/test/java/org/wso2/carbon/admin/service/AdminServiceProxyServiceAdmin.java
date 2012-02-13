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
import org.wso2.carbon.proxyadmin.stub.ProxyAdminException;
import org.wso2.carbon.proxyadmin.stub.ProxyServiceAdminStub;
import org.wso2.carbon.proxyadmin.stub.types.carbon.ProxyData;
import org.wso2.carbon.proxyadmin.ui.client.ProxyServiceAdminClient;

import javax.activation.DataHandler;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Locale;


public class AdminServiceProxyServiceAdmin {
    private static final Log log = LogFactory.getLog(AdminServiceWebAppAdmin.class);

    private final String serviceName = "ProxyServiceAdmin";
    private ProxyServiceAdminStub proxyServiceAdminStub;
    private String endPoint;
    private String backEndUrl;

    public AdminServiceProxyServiceAdmin(String backEndUrl) throws AxisFault {
        this.backEndUrl = backEndUrl;
        this.endPoint = backEndUrl + "/services/" + serviceName;
        proxyServiceAdminStub = new ProxyServiceAdminStub(endPoint);
    }

    public void addProxyService(String sessionCookie, String proxyName, String wsdlURI, String serviceEndPoint) throws ProxyAdminException, RemoteException {

        new AuthenticateStub().authenticateStub(sessionCookie, proxyServiceAdminStub);

        String[] transport = {"http", "https"};
        ProxyData data = new ProxyData();
        data.setName(proxyName);
        data.setWsdlURI(wsdlURI);
        data.setTransports(transport);
        data.setStartOnLoad(true);
        //data.setEndpointKey("http://localhost:9000/services/SimpleStockQuoteService");
        data.setEndpointXML("<endpoint xmlns=\"http://ws.apache.org/ns/synapse\"><address uri=\"" + serviceEndPoint + "\" /></endpoint>");
        data.setEnableSecurity(true);

        proxyServiceAdminStub.addProxy(data);
        log.info("Proxy Added");

    }

    public void addProxyService(String sessionCookie, DataHandler dh) throws ProxyAdminException, IOException, XMLStreamException {
        ProxyServiceAdminClient adminServiceProxyServiceAdminClient = new ProxyServiceAdminClient(null, backEndUrl, sessionCookie, Locale.US);

        new AuthenticateStub().authenticateStub(sessionCookie, proxyServiceAdminStub);

        XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(dh.getInputStream());
        //create the builder
        StAXOMBuilder builder = new StAXOMBuilder(parser);
        //get the root element (in this case the envelope)
        OMElement documentElement = builder.getDocumentElement();

        ProxyData proxyData = adminServiceProxyServiceAdminClient.getDesignView(documentElement.toString());
        proxyServiceAdminStub.addProxy(proxyData);
        log.info("Proxy Added");

    }

    public void deleteProxy(String sessionCookie, String proxyName) throws ProxyAdminException, RemoteException {

        new AuthenticateStub().authenticateStub(sessionCookie, proxyServiceAdminStub);
        proxyServiceAdminStub.deleteProxyService(proxyName);
        log.info("Proxy Deleted");

    }

    public void startProxyService(String sessionCookie, String proxyName) throws ProxyAdminException, RemoteException {

        new AuthenticateStub().authenticateStub(sessionCookie, proxyServiceAdminStub);
        proxyServiceAdminStub.startProxyService(proxyName);
        log.info("Proxy Activated");
    }

    public void stopProxyService(String sessionCookie, String proxyName) throws ProxyAdminException, RemoteException {

        new AuthenticateStub().authenticateStub(sessionCookie, proxyServiceAdminStub);
        proxyServiceAdminStub.stopProxyService(proxyName);
        log.info("Proxy Deactivated");

    }

    public void reloadProxyService(String sessionCookie, String proxyName) throws ProxyAdminException, RemoteException {

        new AuthenticateStub().authenticateStub(sessionCookie, proxyServiceAdminStub);
        proxyServiceAdminStub.redeployProxyService(proxyName);
        log.info("Proxy Redeployed");

    }

    public ProxyData getProxyDetails(String sessionCookie, String proxyName) throws ProxyAdminException, RemoteException {

        new AuthenticateStub().authenticateStub(sessionCookie, proxyServiceAdminStub);
        return proxyServiceAdminStub.getProxy(proxyName);

    }

}
