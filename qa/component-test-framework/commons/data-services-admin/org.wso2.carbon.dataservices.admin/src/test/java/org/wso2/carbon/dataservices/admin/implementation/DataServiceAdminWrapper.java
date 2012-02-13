/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

/**
 * This class have implementation methods of data service admin stub.
 */
package org.wso2.carbon.dataservices.admin.implementation;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.dataservices.admin.Exception.DataServiceAdminException;
import org.wso2.carbon.dataservices.ui.service.DataServiceAdminStub;
import org.wso2.carbon.dataservices.ui.service.admin.xsd.DBServerData;

public class DataServiceAdminWrapper extends TestCase {
    private static final Log log = LogFactory.getLog(DataServiceAdminWrapper.class);
    private DataServiceAdminStub dataServiceAdminStub;

    public DataServiceAdminWrapper(
            DataServiceAdminStub dataServiceAdminStub) {
        this.dataServiceAdminStub = dataServiceAdminStub;
        log.debug("dataServiceAdminStub added");
    }

    /**
     * getting available data service name list
     *
     * @return data services name list
     * @throws DataServiceAdminException custom exception of data service admin service wrapping module
     */
    public String[] getAvailableDS() throws DataServiceAdminException {
        String[] availableDS = null;
        try {
            availableDS = dataServiceAdminStub.getAvailableDS();
        } catch (Exception e) {
            throw new DataServiceAdminException("Exception occurred while getting available data services : " + e);
        }
        return availableDS;
    }

    /**
     * getting available database driver list
     *
     * @return database driver list
     * @throws DataServiceAdminException custom exception of data service admin service wrapping module
     */
    public DBServerData[] getDatabaseUrlDriverList() throws DataServiceAdminException {
        DBServerData[] dbServerData;
        try {
            dbServerData = dataServiceAdminStub.getDatabaseUrlDriverList();
        } catch (Exception e) {
            throw new DataServiceAdminException("Exception occurred while getting available DB driver list : " + e);
        }
        return dbServerData;
    }

    /**
     * getting available carbon data source names
     *
     * @return carbon data source names
     * @throws DataServiceAdminException custom exception of data service admin service wrapping module
     */
    public String[] getCarbonDataSourceNames() throws DataServiceAdminException {
        String[] dataSourceName = null;
        try {
            dataSourceName = dataServiceAdminStub.getCarbonDataSourceNames();
        } catch (Exception e) {
            throw new DataServiceAdminException("Exception occurred while getting available data sources list : " + e);
        }
        return dataSourceName;
    }

    /**
     * getting column names of specified query
     *
     * @param query processing query
     * @return column names
     * @throws DataServiceAdminException custom exception of data service admin service wrapping module
     */
    public String[] getColumnNames(String query) throws DataServiceAdminException {
        String[] columnNames = null;
        try {
            columnNames = dataServiceAdminStub.getColumnNames(query);
        } catch (Exception e) {
            throw new DataServiceAdminException("Exception occurred while getting column names : " + e);
        }
        return columnNames;
    }

    /**
     * Getting data service xml as a string
     *
     * @param serviceID name of the service
     * @return service content
     * @throws DataServiceAdminException custom exception of data service admin service wrapping module
     */
    public String getDataServiceContentAsString(String serviceID) throws DataServiceAdminException {
        String dataServiceContent = null;
        try {
            dataServiceContent = dataServiceAdminStub.getDataServiceContentAsString(serviceID);
        } catch (Exception e) {
            throw new DataServiceAdminException("Exception occurred while getting data service content : " + e);
        }
        return dataServiceContent;
    }

    /**
     * getting available schema list
     *
     * @param dataSourceId name of the data service
     * @return schema list
     * @throws DataServiceAdminException custom exception of data service admin service wrapping module
     */
    public String[] getSchemaList(String dataSourceId) throws DataServiceAdminException {
        String[] schemaList = null;
        try {
            schemaList = dataServiceAdminStub.getdbSchemaList(dataSourceId);
        } catch (Exception e) {
            throw new DataServiceAdminException("Exception occurred while getting data service schema list : " + e);
        }
        return schemaList;
    }

    /**
     * @param dataSourceId     data source name
     * @param dbName           data base name
     * @param schemas          schemas
     * @param tableNames       table names of data base
     * @param singleService    service status
     * @param serviceName      data service name
     * @param serviceNamespace namespace
     * @return service content
     * @throws DataServiceAdminException custom exception of data service admin service wrapping module
     */
    public String getDataServiceContentAsString(String dataSourceId, String dbName,
                                                String[] schemas, String[] tableNames,
                                                boolean singleService, String serviceName,
                                                String serviceNamespace)
            throws DataServiceAdminException {
        String dataService = null;
        try {
            dataService = dataServiceAdminStub.getDSService(dataSourceId, dbName, schemas, tableNames, singleService, serviceName, serviceNamespace);
        } catch (Exception e) {
            throw new DataServiceAdminException("Exception occurred while getting data service : " + e);
        }
        return dataService;
    }

    /**
     * @param dataSourceId     data source name
     * @param dbName           data base name
     * @param schemas          schemas
     * @param tableNames       table names of data base
     * @param singleService    service status
     * @param serviceNamespace namespace
     * @return schema list
     * @throws DataServiceAdminException custom exception of data service admin service wrapping module
     */
    public String[] getDSServiceList(String dataSourceId, String dbName,
                                     String[] schemas, String[] tableNames,
                                     boolean singleService, String serviceNamespace)
            throws DataServiceAdminException {
        String[] dsServiceList = null;
        try {
            dsServiceList = dataServiceAdminStub.getDSServiceList(dataSourceId, dbName, schemas, tableNames, singleService, serviceNamespace);
        } catch (Exception e) {
            throw new DataServiceAdminException("Exception occurred while getting data base service list : " + e);
        }
        return dsServiceList;
    }

    /**
     * getting table list
     *
     * @param dataSourceId data source name
     * @param dbName       database name
     * @param schemas      schemas
     * @return table list
     * @throws DataServiceAdminException custom exception of data service admin service wrapping module
     */

    public String[] getTableList(String dataSourceId, String dbName,
                                 String[] schemas)
            throws DataServiceAdminException {
        String[] tableList = null;
        try {
            tableList = dataServiceAdminStub.getTableList(dataSourceId, dbName, schemas);
        } catch (Exception e) {
            throw new DataServiceAdminException("Exception occurred while getting table list : " + e);
        }
        return tableList;
    }

    /**
     * save data service
     *
     * @param serviceName        data service name
     * @param dataServiceContent service content - configuration content
     * @throws DataServiceAdminException custom exception of data service admin service wrapping module
     */
    public void saveDataService(String serviceName, String dataServiceContent)
            throws DataServiceAdminException {
        try {
            dataServiceAdminStub.saveDataService(serviceName, dataServiceContent);
        } catch (Exception e) {
            throw new DataServiceAdminException("Exception occurred while saving data service : " + e);
        }
    }

    /**
     * checking GSpread connection status
     *
     * @param userName         user name
     * @param password         password
     * @param visibility       visibility of user
     * @param documentURL      doc path
     * @param protectedToken   protectedToken
     * @param passwordProvider provider of the password
     * @return connection status
     * @throws DataServiceAdminException custom exception of data service admin service wrapping module
     */
    public String testGSpreadConnection(String userName, String password, String visibility,
                                        String documentURL, String protectedToken,
                                        String passwordProvider)
            throws DataServiceAdminException {
        String testResult = null;
        try {
            testResult = dataServiceAdminStub.testGSpreadConnection(userName, password, visibility, documentURL, protectedToken, passwordProvider);
        } catch (Exception e) {
            throw new DataServiceAdminException("Exception occurred while testing GSpreadConnection : " + e);
        }
        return testResult;
    }

    /**
     * checking JDBC connection status
     *
     * @param driverClassName name of the driver class
     * @param jdbcURL         JDBC url string
     * @param userName        user name of the database
     * @param password        password of the database
     * @param protectedToken  protectedToken
     * @return connection status
     * @throws DataServiceAdminException custom exception of data service admin service wrapping module
     */
    public String testJDBCConnection(String driverClassName, String jdbcURL, String userName,
                                     String password,
                                     String protectedToken)
            throws DataServiceAdminException {
        String testResult = null;
        try {
            testResult = dataServiceAdminStub.testJDBCConnection(driverClassName, jdbcURL, userName, password, password, protectedToken);
        } catch (Exception e) {
            throw new DataServiceAdminException("Exception occurred while testing jdbc connection : " + e);
        }
        return testResult;
    }


}
