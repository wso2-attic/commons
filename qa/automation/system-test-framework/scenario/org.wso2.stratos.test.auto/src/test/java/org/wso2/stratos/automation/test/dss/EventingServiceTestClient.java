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
package org.wso2.stratos.automation.test.dss;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.om.*;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceProxyServiceAdmin;
import org.wso2.carbon.admin.service.AdminServiceService;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaData;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClient;
import org.wso2.stratos.automation.test.dss.utils.AdminServiceClientDSS;
import org.wso2.stratos.automation.test.dss.utils.FileManager;
import org.wso2.stratos.automation.test.dss.utils.TestTemplateRSS;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Iterator;

public class EventingServiceTestClient extends TestTemplateRSS {
    private static final Log log = LogFactory.getLog(EventingServiceTestClient.class);
    private String proxyUrl = null;

    @Override
    public void setServiceMetaData() {
        serviceFileLocation = RESOURCE_LOCATION + File.separator + "dbs" + File.separator + "rdbms" + File.separator + "MySql";
        serviceFileName = "EventingTest.dbs";
        serviceName = "EventingTest";
        serviceGroup = "Eventing";
    }

    @Override
    public void executeSql() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createArtifact() {
        Assert.assertNotNull("Initialize jdbcUrl", jdbcUrl);
        createProxy();
        Assert.assertNotNull("Initialize jdbcUrl", proxyUrl);
        try {
            OMElement dbsFile = AXIOMUtil.stringToOM(FileManager.readFile(serviceFileLocation + File.separator + serviceFileName).trim());
            OMElement dbsConfig = dbsFile.getFirstChildWithName(new QName("config"));
            Iterator configElement1 = dbsConfig.getChildElements();
            while (configElement1.hasNext()) {
                OMElement property = (OMElement) configElement1.next();
                String value = property.getAttributeValue(new QName("name"));
                if ("org.wso2.ws.dataservice.protocol".equals(value)) {
                    property.setText(jdbcUrl);

                } else if ("org.wso2.ws.dataservice.user".equals(value)) {
                    property.setText(databaseUser);

                } else if ("org.wso2.ws.dataservice.password".equals(value)) {
                    property.setText(databasePassword);
                }
            }

            Iterator events = dbsFile.getChildrenWithLocalName("event-trigger");
            while (events.hasNext()) {
                OMElement event = (OMElement) events.next();
                if ("product_stock_low_trigger".equals(event.getAttributeValue(new QName("id")))) {
                    event.getFirstChildWithName(new QName("subscriptions")).getFirstChildWithName(new QName("subscription")).setText(proxyUrl);
                }
            }

            log.debug(dbsFile);
            ByteArrayDataSource dbs = new ByteArrayDataSource(dbsFile.toString().getBytes());
            serviceFile = new DataHandler(dbs);


        } catch (XMLStreamException e) {
            log.error("XMLStreamException when Reading Service File" + e.getMessage());
            Assert.fail("XMLStreamException when Reading Service File" + e.getMessage());
        } catch (IOException e) {
            log.error("IOException when Reading Service File" + e.getMessage());
            Assert.fail("IOException  when Reading Service File" + e.getMessage());
        }

    }

    @Override
    public void runSuccessCase() {

        OMElement product;
        product = getProduct(1);

        if (!product.getChildrenWithLocalName("Product").hasNext()) {
            addProduct();
        }

        editProduct(200);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Assert.fail("InterruptedException :" + e.getMessage());
        }
        product = getProduct(1);
        Assert.assertNotNull("Product Not Found ", product);
        Assert.assertEquals("Product edited failed", ((OMElement) ((OMElement) product.getChildrenWithLocalName("Product").next()).getChildrenWithLocalName("quantityInStock").next()).getText(), "200");

        editProduct(5);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Assert.fail("InterruptedException :" + e.getMessage());
        }
        product = getProduct(1);
        Assert.assertNotNull("Product Not Found ", product);
        Assert.assertEquals("Event Not triggered", "600", ((OMElement) ((OMElement) product.getChildrenWithLocalName("Product").next()).getChildrenWithLocalName("quantityInStock").next()).getText());

    }

    private void addProduct() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice", "ns1");
        OMElement payload = fac.createOMElement("addProduct", omNs);

        OMElement productCode = fac.createOMElement("productCode", omNs);
        productCode.setText("1");
        payload.addChild(productCode);

        OMElement productName = fac.createOMElement("productName", omNs);
        productName.setText("product");
        payload.addChild(productName);

        OMElement productLine = fac.createOMElement("productLine", omNs);
        productLine.setText("2");
        payload.addChild(productLine);

        OMElement quantityInStock = fac.createOMElement("quantityInStock", omNs);
        quantityInStock.setText("500");
        payload.addChild(quantityInStock);

        OMElement buyPrice = fac.createOMElement("buyPrice", omNs);
        buyPrice.setText("10");
        payload.addChild(buyPrice);

        new AxisServiceClient().sendRobust(payload, serviceEndPoint, "addProduct");


    }

    private void editProduct(int quantity) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice", "ns1");
        OMElement payload = fac.createOMElement("updateProductQuantity", omNs);

        OMElement productCode = fac.createOMElement("productCode", omNs);
        productCode.setText("1");
        payload.addChild(productCode);


        OMElement quantityInStock = fac.createOMElement("quantityInStock", omNs);
        quantityInStock.setText(quantity + "");
        payload.addChild(quantityInStock);


        new AxisServiceClient().sendRobust(payload, serviceEndPoint, "updateProductQuantity");


    }

    private OMElement getProduct(int productId) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice", "ns1");
        OMElement payload = fac.createOMElement("getProductByCode", omNs);

        OMElement productCode = fac.createOMElement("productCode", omNs);
        productCode.setText(productId + "");
        payload.addChild(productCode);

        return new AxisServiceClient().sendReceive(payload, serviceEndPoint, "getProductByCode");

    }

    private void createProxy() {

        AdminServiceAuthentication adminServiceAuthentication = new AdminServiceAuthentication(FrameworkSettings.ESB_BACKEND_URL);
        AdminServiceProxyServiceAdmin adminServiceProxyServiceAdmin = new AdminServiceProxyServiceAdmin(FrameworkSettings.ESB_BACKEND_URL);
        AdminServiceService adminServiceService = new AdminServiceService(FrameworkSettings.ESB_BACKEND_URL);
        String esbSessionCookie = adminServiceAuthentication.login(USER_NAME, PASSWORD, "localhost");
        final String proxyName = "eventTrigerProxy";
        ServiceMetaData serviceMetaData;
        String[] endpoints;

        if (adminServiceService.isServiceExists(esbSessionCookie, proxyName)) {
            adminServiceProxyServiceAdmin.deleteProxy(esbSessionCookie, proxyName);
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                Assert.fail("Thread InterruptedException :" + e.getMessage());
            }
        }

        try {
            OMElement proxyFile = AXIOMUtil.stringToOM(FileManager.readFile(ProductConstant.getResourceLocations(ProductConstant.DSS_SERVER_NAME) + File.separator + "resources" + "/eventTrigerProxy.xml").trim());

            OMElement target = proxyFile.getFirstElement();
            Iterator i = target.getChildrenWithName(new QName("endpoint"));
            while (i.hasNext()) {
                OMElement endpoint = (OMElement) i.next();
                OMElement address = endpoint.getFirstElement();
                OMAttribute uri = address.getAttribute(new QName("uri"));

                if (FrameworkSettings.STRATOS_TEST) {
                    uri.setAttributeValue("http://" + FrameworkSettings.DSS_SERVER_HOST_NAME + "/services/t/" + TENANT_DOMAIN + "/EventingTest/updateProductQuantity");
                } else {
                    uri.setAttributeValue("http://" + FrameworkSettings.DSS_SERVER_HOST_NAME + ":" + FrameworkSettings
                            .DSS_SERVER_HTTP_PORT + "/services/EventingTest/updateProductQuantity");
                }
            }

            ByteArrayDataSource dbs = new ByteArrayDataSource(proxyFile.toString().getBytes());
            adminServiceProxyServiceAdmin.addProxyService(esbSessionCookie, new DataHandler(dbs));

            Calendar startTime = Calendar.getInstance();
            long time;
            while ((time = (Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis())) < FrameworkSettings.SERVICE_DEPLOYMENT_DELAY) {
                if (adminServiceService.isServiceExists(esbSessionCookie, proxyName)) {
                    break;
                }
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    Assert.fail("Thread InterruptedException :" + e.getMessage());
                }

            }
            Assert.assertTrue("Proxcy deployment time out", (time < FrameworkSettings.SERVICE_DEPLOYMENT_DELAY));
            serviceMetaData = adminServiceService.getServicesData(esbSessionCookie, proxyName);
            endpoints = serviceMetaData.getEprs();
            Assert.assertNotNull("Service Endpoint object null", endpoints);
            Assert.assertTrue("No service endpoint found", (endpoints.length > 0));
            for (String epr : endpoints) {
                if (epr.startsWith("http://")) {
                    proxyUrl = epr;
                    break;
                }
            }
            log.info("Proxy Service End point :" + proxyUrl);
            Assert.assertNotNull("service endpoint null", proxyUrl);
            Assert.assertTrue("Service endpoint not contain service name", proxyUrl.contains(proxyName));

        } catch (MalformedURLException e) {
            log.error(e);
            Assert.fail("MalformedURLException : " + e.getMessage());
        } catch (IOException e) {
            log.error(e);
            Assert.fail("IOException : " + e.getMessage());
        } catch (XMLStreamException e) {
            log.error(e);
            Assert.fail("XMLStreamException : " + e.getMessage());
        }

    }

}
