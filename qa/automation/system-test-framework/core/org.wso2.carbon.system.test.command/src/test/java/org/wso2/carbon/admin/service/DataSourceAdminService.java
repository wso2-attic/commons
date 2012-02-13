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
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.commons.datasource.DataSourceInformation;
import org.apache.synapse.commons.datasource.factory.DataSourceInformationFactory;
import org.apache.synapse.commons.datasource.serializer.DataSourceInformationSerializer;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.datasource.ui.stub.DataSourceAdminStub;
import org.wso2.carbon.datasource.ui.stub.DataSourceManagementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.rmi.RemoteException;
import java.util.Properties;

public class DataSourceAdminService {

    private static final Log log = LogFactory.getLog(DataSourceAdminService.class);

    private final String serviceName = "DataSourceAdmin";
    private DataSourceAdminStub dataSourceAdminStub;
    private String endPoint;

    public DataSourceAdminService(String backEndUrl) {
        this.endPoint = backEndUrl + serviceName;
        log.debug("EndPoint : " + endPoint);
        try {
            dataSourceAdminStub = new DataSourceAdminStub(endPoint);
        } catch (AxisFault axisFault) {
            log.error("Initializing DataSourceAdminStub failed : ", axisFault);
            Assert.fail("Initializing DataSourceAdminStub failed : " + axisFault.getMessage());
        }
    }

    public void addDataSourceInformation(String sessionCookie, String dataSourceName, DataSourceInformation dataSourceInfo) {
        new AuthenticateStub().authenticateStub(sessionCookie, dataSourceAdminStub);
        OMElement dataSourceInfoElement;
        try {
            Properties properties = DataSourceInformationSerializer.serialize(dataSourceInfo);
            if (properties.isEmpty()) {
                Assert.fail("No DataSource Information found");
            }
            dataSourceInfoElement = createOMElement(properties);
            Assert.assertNotNull("DataSource Information null", dataSourceInfoElement);
            dataSourceAdminStub.addDataSourceInformation(dataSourceName, dataSourceInfoElement);
            log.debug("Data Source Added");
        } catch (RemoteException e) {
            log.error("Remote Exception when adding data sources :", e);
            Assert.fail("Remote Exception when adding data sources : " + e.getMessage());
        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagementException when adding data sources :", e);
            Assert.fail("DataSourceManagementException when adding data sources : " + e.getMessage());
        }
    }

    public void editCarbonDataSources(String sessionCookie, String name, DataSourceInformation dataSourceInformation) {
        new AuthenticateStub().authenticateStub(sessionCookie, dataSourceAdminStub);
        try {
            Properties properties = DataSourceInformationSerializer.serialize(dataSourceInformation);
            if (properties.isEmpty()) {
                Assert.fail("No DataSource Information found");
            }
            OMElement dataSourceElement = createOMElement(properties);
            Assert.assertNotNull("DataSource Information null", dataSourceElement);
            dataSourceAdminStub.editDataSourceInformation(name, dataSourceElement);
        } catch (RemoteException e) {
            log.error("Remote Exception when editing data sources :", e);
            Assert.fail("Remote Exception when editing data sources : " + e.getMessage());
        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagementException  when editing data sources :", e);
            Assert.fail("DataSourceManagementException  when editing data sources : " + e.getMessage());
        }

    }

    public DataSourceInformation getCarbonDataSources(String sessionCookie, String name) {
        new AuthenticateStub().authenticateStub(sessionCookie, dataSourceAdminStub);
        OMElement dataSource;
        DataSourceInformation dataSourceInformation = null;
        try {
            dataSource = dataSourceAdminStub.getDataSourceInformation(name);
            Assert.assertNotNull("DataSource Information Not Found", dataSource);
            dataSourceInformation = validateAndCreate(name, dataSource.getFirstElement());
        } catch (RemoteException e) {
            log.error("Remote Exception when getting data sources :", e);
            Assert.fail("Remote Exception when getting data sources : " + e.getMessage());
        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagementException  when getting data sources :", e);
            Assert.fail("DataSourceManagementException  when getting data sources : " + e.getMessage());
        }
        Assert.assertNotNull("DataSource Information null", dataSourceInformation);
        return dataSourceInformation;
    }

    public void removeCarbonDataSources(String sessionCookie, String name) {
        new AuthenticateStub().authenticateStub(sessionCookie, dataSourceAdminStub);
        try {
            dataSourceAdminStub.removeDataSourceInformation(name);
        } catch (RemoteException e) {
            log.error("Remote Exception when removing data sources :", e);
            Assert.fail("Remote Exception when removing data sources : " + e.getMessage());
        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagementException  when removing data sources :", e);
            Assert.fail("DataSourceManagementException  when removing data sources : " + e.getMessage());
        }
    }

    private static DataSourceInformation validateAndCreate(String name, OMElement element) {

        Properties properties = loadProperties(element);
        if (properties.isEmpty()) {
            Assert.fail("DataSource Information Not Found. properties Empty");
        }

        DataSourceInformation information = DataSourceInformationFactory.createDataSourceInformation(name, properties);

        return information;
    }

    private static Properties loadProperties(OMElement element) {

        log.debug("Loading properties from : " + element);
        String xml = "<!DOCTYPE properties   [\n" +
                "\n" +
                "<!ELEMENT properties ( comment?, entry* ) >\n" +
                "\n" +
                "<!ATTLIST properties version CDATA #FIXED \"1.0\">\n" +
                "\n" +
                "<!ELEMENT comment (#PCDATA) >\n" +
                "\n" +
                "<!ELEMENT entry (#PCDATA) >\n" +
                "\n" +
                "<!ATTLIST entry key CDATA #REQUIRED>\n" +
                "]>" + element.toString();
        final Properties properties = new Properties();
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(xml.getBytes());
            properties.loadFromXML(in);
            return properties;
        } catch (IOException e) {
            Assert.fail("IOException while reading DataSource information" + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException inored) {
                }
            }

        }
        return properties;
    }

    private static OMElement createOMElement(Properties properties) {

        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            properties.storeToXML(baos, "");
            String propertyS = new String(baos.toByteArray());
            String correctedS = propertyS.substring(propertyS.indexOf("<properties>"),
                    propertyS.length());
            String inLined = "<!DOCTYPE properties   [\n" +
                    "\n" +
                    "<!ELEMENT properties ( comment?, entry* ) >\n" +
                    "\n" +
                    "<!ATTLIST properties version CDATA #FIXED \"1.0\">\n" +
                    "\n" +
                    "<!ELEMENT comment (#PCDATA) >\n" +
                    "\n" +
                    "<!ELEMENT entry (#PCDATA) >\n" +
                    "\n" +
                    "<!ATTLIST entry key CDATA #REQUIRED>\n" +
                    "]>";
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(
                    new StringReader(inLined + correctedS));
            StAXOMBuilder builder = new StAXOMBuilder(reader);
            return builder.getDocumentElement();

        } catch (XMLStreamException e) {
            Assert.fail("Error Creating a OMElement from properties : " + properties + ":" + e.getMessage());
        } catch (IOException e) {
            Assert.fail("Error Creating a OMElement from properties : " + properties + ":" + e.getMessage());
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException ignored) {
                }

            }
        }
        return null;
    }
}
