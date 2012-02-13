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
package org.wso2.carbon.automation.ravana.test.ravanaUtils;

import junit.framework.Assert;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceConfigServiceAdmin;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.dbUtils.MySqlDatabaseManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.sql.SQLException;

public abstract class RavanaTestTemplate extends TestTemplate {
    private static final Log logRavanaTemplate = LogFactory.getLog(RavanaTestTemplate.class);
    public static MySqlDatabaseManager mysqlDBMgt = null;

    public abstract void executeSql();

    public abstract void createProcessBuilder();

    @Override
    public void init() {
        updateSynapseConfiguration();
        createConnection();
        createProcessBuilder();
        executeSql();
    }

    @Override
    public void cleanup() {
        try {
            mysqlDBMgt.disconnect();
        } catch (SQLException e) {
            logRavanaTemplate.error("SQL error " + e.getMessage());
            Assert.fail("SQL error " + e.getMessage());
        }
    }

    private void createConnection() {
        String dbInstanceName = "ravana";
        String connectionURL = FrameworkSettings.RAVANA_JDBC_URL + "/" + dbInstanceName;
        String password = FrameworkSettings.RAVANA_DB_PASSWORD;
        String userName = FrameworkSettings.RAVANA_DB_USER;
        try {
            mysqlDBMgt = new MySqlDatabaseManager(connectionURL, userName, password);
        } catch (SQLException e) {
            logRavanaTemplate.error("SQL error " + e.getMessage());
            Assert.fail("SQL error " + e.getMessage());
        } catch (ClassNotFoundException e) {
            logRavanaTemplate.error("Driver classes not found " + e.getMessage());
            Assert.fail("Driver classes not found " + e.getMessage());
        }
    }

    public static void updateSynapseConfiguration() {
        String backendURL = FrameworkSettings.ESB_BACKEND_URL;
        String synapseFileLocation = FrameworkSettings.RAVANA_FRAMEWORK_PATH + File.separator + "scenario" +
                File.separator + "wso2" + File.separator + "synapse.xml";
        logRavanaTemplate.debug("Synapse file content" + convertXMLFileToString(synapseFileLocation));

        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("0"));
        String sessionCookie = login(tenantDetails.getTenantName(), tenantDetails.getTenantPassword(), backendURL);
        AdminServiceConfigServiceAdmin configAdmin = new AdminServiceConfigServiceAdmin(backendURL, sessionCookie);
        assertTrue("Synapse configuration update failed",
                configAdmin.updateConfiguration(convertXMLFileToString(synapseFileLocation)));
        logRavanaTemplate.info("synapse configuration updated");
        try {
            Thread.sleep(1000 * 30);
        } catch (InterruptedException e) {
            logRavanaTemplate.error("Thread interrupted " + e.getMessage());
        }
    }

    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }

    private static String convertXMLFileToString(String fileName) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            InputStream inputStream = new FileInputStream(new File(fileName));
            org.w3c.dom.Document doc = documentBuilderFactory.newDocumentBuilder().parse(inputStream);
            StringWriter stw = new StringWriter();
            Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.transform(new DOMSource(doc), new StreamResult(stw));
            return stw.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
