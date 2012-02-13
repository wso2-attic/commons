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
import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.system.test.core.utils.httpClient.HttpClientUtil;
import org.wso2.stratos.automation.test.dss.utils.FileManager;
import org.wso2.stratos.automation.test.dss.utils.TestTemplateRSS;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;


public class ResourcesServiceTestClient extends TestTemplateRSS {

    private static final Log log = LogFactory.getLog(ResourcesServiceTestClient.class);

    @Override
    public void setServiceMetaData() {
        serviceFileLocation = RESOURCE_LOCATION + File.separator + "dbs" + File.separator + "rdbms" + File.separator + "MySql";
        serviceFileName = "ResourcesServiceTest.dbs";
        serviceName = "ResourcesServiceTest";
        serviceGroup = "Resources";
    }

    @Override
    public void executeSql() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createArtifact() {
        Assert.assertNotNull("Initialize jdbcUrl", jdbcUrl);
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
        if(!serviceEndPoint.endsWith("/")){
            serviceEndPoint = serviceEndPoint + "/";
        }
        deleteProduct();
        addProduct();

        OMElement response;
        for (int i = 1; i < 6; i++) {
            response = getProductByCode(i + "");
            Assert.assertTrue("Expected result not found", (response.toString().indexOf("<productName>product" + i + "</productName>") > 1));
            Assert.assertTrue("Expected result not found", (response.toString().indexOf("<productLine>2</productLine>") > 1));
        }
        editProduct();
        for (int i = 1; i < 6; i++) {
            response = getProductByCode(i + "");
            Assert.assertTrue("Expected result not found", (response.toString().indexOf("<productName>product" + i + " edited</productName>") > 1));
            Assert.assertTrue("Expected result not found", (response.toString().indexOf("<buyPrice>15.0</buyPrice>") > 1));
        }

        listProduct();
        deleteProduct();
    }

    private void listProduct() {

        HttpClientUtil httpClient = new HttpClientUtil();
        OMElement result = httpClient.get(serviceEndPoint + "_getproducts");
        Assert.assertNotNull("Response null", result);
        for (int i = 1; i < 6; i++) {
            Assert.assertTrue("Expected result not found", (result.toString().indexOf("<productCode>" + i + "</productCode>") > 1));
        }

    }

    private void deleteProduct() {

        HttpClientUtil httpClient = new HttpClientUtil();
        for (int i = 1; i < 6; i++) {
            httpClient.delete(serviceEndPoint + "_deleteproduct_productcode", "productCode=" + i);

        }


    }

    private void editProduct() {

        HttpClientUtil httpClient = new HttpClientUtil();
        for (int i = 1; i < 6; i++) {

            String para = "productCode=" + i
                    + "&" + "productName=" + "product" + i + " edited"
                    + "&" + "productLine=2"
                    + "&" + "quantityInStock=200"
                    + "&" + "buyPrice=15";
            httpClient.put(serviceEndPoint + "_putproduct", para);

        }


    }

    private OMElement getProductByCode(String productId) {

        HttpClientUtil httpClient = new HttpClientUtil();

        return httpClient.get(serviceEndPoint + "_getproduct_productcode?productCode=" + productId);


    }

    private void addProduct() {

        HttpClientUtil httpClient = new HttpClientUtil();
        for (int i = 1; i < 6; i++) {

            String para = "productCode=" + i
                    + "&" + "productName=" + "product" + i
                    + "&" + "productLine=2"
                    + "&" + "quantityInStock=200"
                    + "&" + "buyPrice=10";
            httpClient.post(serviceEndPoint + "_postproduct", para);

        }
    }
}
