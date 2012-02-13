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
package org.wso2.carbon.datasource.test.admin.commands;

import junit.framework.TestCase;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.datasource.ui.stub.DataSourceAdminCallbackHandler;
import org.wso2.carbon.datasource.ui.stub.DataSourceAdminStub;
import org.wso2.carbon.datasource.ui.stub.DataSourceManagementException;

import java.rmi.RemoteException;

public class DataSourceAdminCommand extends TestCase {
    private static final Log log = LogFactory.getLog(DataSourceAdminCommand.class);
    DataSourceAdminStub dataSourceAdminStub;

    public DataSourceAdminCommand(
            DataSourceAdminStub dataSourceAdminStub) {
        this.dataSourceAdminStub = dataSourceAdminStub;
        log.debug("dataSourceAdminStub added");

    }


    public void addDataSourceInformationSuccessCase(String name51, OMElement extraElement52)
            throws RemoteException, DataSourceManagementException {

        try {
            dataSourceAdminStub.addDataSourceInformation(name51, extraElement52);
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagement Exception : " + e.getMessage());
            Assert.fail("DataSourceManagement Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }
    }

    public void addDataSourceInformationFailureCase(String name51, OMElement extraElement52)
            throws RemoteException, DataSourceManagementException {

        try {
            dataSourceAdminStub.addDataSourceInformation(name51, extraElement52);
        }
        catch (AxisFault e) {
            if (e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagement Exception : " + e.getMessage());
            Assert.fail("DataSourceManagement Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }
    }

    public void editDataSourceInformationSuccessCase(String name54, OMElement extraElement55)
            throws RemoteException {
        try {
            dataSourceAdminStub.editDataSourceInformation(name54, extraElement55);
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagement Exception : " + e.getMessage());
            Assert.fail("DataSourceManagement Exception " + e);

        }
        catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void editDataSourceInformationFailureCase(String name54, OMElement extraElement55)
            throws RemoteException {
        try {
            dataSourceAdminStub.editDataSourceInformation(name54, extraElement55);
            Assert.fail("Getting session resource path without authenticating admin service");
            log.error("Getting session resource path without authenticating admin service");
        }

        catch (AxisFault e) {
            if (e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagement Exception : " + e.getMessage());
            Assert.fail("DataSourceManagement Exception " + e);

        }
        catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public OMElement getAllDataSourceInformationSuccessCase()
            throws RemoteException, DataSourceManagementException {
        OMElement oMElement = null;
        try {
            dataSourceAdminStub.getAllDataSourceInformation();
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagement Exception : " + e.getMessage());
            Assert.fail("DataSourceManagement Exception " + e);

        }
        catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return oMElement;
    }

    public OMElement getAllDataSourceInformationFailureCase()
            throws RemoteException, DataSourceManagementException {
        OMElement oMElement = null;
        try {
            dataSourceAdminStub.getAllDataSourceInformation();
            Assert.fail("Getting session resource path without authenticating admin service");
            log.error("Getting session resource path without authenticating admin service");

        } catch (AxisFault e) {
            if (e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagement Exception : " + e.getMessage());
            Assert.fail("DataSourceManagement Exception " + e);

        }
        catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return oMElement;
    }

    public OMElement getDataSourceInformationSuccessCase(String name44)
            throws RemoteException, DataSourceManagementException {
        OMElement oMElement = null;
        try {
            oMElement = dataSourceAdminStub.getDataSourceInformation(name44);
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagement Exception : " + e.getMessage());
            Assert.fail("DataSourceManagement Exception " + e);

        }
        catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return oMElement;
    }


    public OMElement getDataSourceInformationFailureCase(String name44)
            throws RemoteException, DataSourceManagementException {
        OMElement oMElement = null;
        try {
            oMElement = dataSourceAdminStub.getDataSourceInformation(name44);
            Assert.fail("Getting session resource path without authenticating admin service");
            log.error("Getting session resource path without authenticating admin service");

        } catch (AxisFault e) {
            if (e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagement Exception : " + e.getMessage());
            Assert.fail("DataSourceManagement Exception " + e);

        }
        catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return oMElement;
    }


    public boolean isContainsSuccessCase(String name59)
            throws RemoteException, DataSourceManagementException {
        boolean isContains = false;
        try {
            isContains = dataSourceAdminStub.isContains(name59);
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagement Exception : " + e.getMessage());
            Assert.fail("DataSourceManagement Exception " + e);

        }
        catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return isContains;
    }


    public boolean isContainsFailureCase(String name59)
            throws RemoteException, DataSourceManagementException {
        boolean isContains = false;
        try {
            isContains = dataSourceAdminStub.isContains(name59);
            Assert.fail("Getting session resource path without authenticating admin service");
            log.error("Getting session resource path without authenticating admin service");

        } catch (AxisFault e) {
            if (e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagement Exception : " + e.getMessage());
            Assert.fail("DataSourceManagement Exception " + e);

        }
        catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return isContains;
    }


    public boolean testConnectionSuccessCase(String name63, OMElement extraElement64)
            throws RemoteException, DataSourceManagementException {
        boolean testConnection = false;
        try {
            testConnection = dataSourceAdminStub.testConnection(name63, extraElement64);

        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagement Exception : " + e.getMessage());
            Assert.fail("DataSourceManagement Exception " + e);

        }
        catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return testConnection;
    }

    public boolean testConnectionFailureCase(String name63, OMElement extraElement64)
            throws RemoteException, DataSourceManagementException {
        boolean testConnection = false;
        try {
            testConnection = dataSourceAdminStub.testConnection(name63, extraElement64);
            Assert.fail("Getting session resource path without authenticating admin service");
            log.error("Getting session resource path without authenticating admin service");


        } catch (AxisFault e) {
            if (e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (DataSourceManagementException e) {
            log.error("DataSourceManagement Exception : " + e.getMessage());
            Assert.fail("DataSourceManagement Exception " + e);

        }
        catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return testConnection;
    }


    public void startTestConnectionSuccessCase(String name63, OMElement extraElement64,
                                               DataSourceAdminCallbackHandler callback)
            throws RemoteException {
        try {
            dataSourceAdminStub.starttestConnection(name63, extraElement64, callback);

        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void startTestConnectionFailureCase(String name63, OMElement extraElement64,
                                               DataSourceAdminCallbackHandler callback)
            throws RemoteException {
        try {
            dataSourceAdminStub.starttestConnection(name63, extraElement64, callback);
            Assert.fail("Getting session resource path without authenticating admin service");
            log.error("Getting session resource path without authenticating admin service");

        } catch (AxisFault e) {
            if (e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }


    public void startGetDataSourceInformationSuccessCase(String name44,
                                                         DataSourceAdminCallbackHandler callback)
            throws RemoteException {

        try {
            dataSourceAdminStub.startgetDataSourceInformation(name44, callback);

        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void startGetDataSourceInformationFailureCase(String name44,
                                                         DataSourceAdminCallbackHandler callback)
            throws RemoteException {

        try {
            dataSourceAdminStub.startgetDataSourceInformation(name44, callback);
            Assert.fail("Getting session resource path without authenticating admin service");
            log.error("Getting session resource path without authenticating admin service");


        } catch (AxisFault e) {
            if (e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }


    public void startIsContainsSuccessCase(String name59, DataSourceAdminCallbackHandler callback)
            throws RemoteException {

        try {
            dataSourceAdminStub.startisContains(name59, callback);

        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void startIsContainsFailureCase(String name59, DataSourceAdminCallbackHandler callback)
            throws RemoteException {

        try {
            dataSourceAdminStub.startisContains(name59, callback);
            Assert.fail("Getting session resource path without authenticating admin service");
            log.error("Getting session resource path without authenticating admin service");

        } catch (AxisFault e) {
            if (e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void startGetAllDataSourceInformationSuccessCase(DataSourceAdminCallbackHandler callback)
            throws RemoteException {

        try {
            dataSourceAdminStub.startgetAllDataSourceInformation(callback);
            Assert.fail("Getting session resource path without authenticating admin service");
            log.error("Getting session resource path without authenticating admin service");

        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void startGetAllDataSourceInformationFailureCase(DataSourceAdminCallbackHandler callback)
            throws RemoteException {

        try {
            dataSourceAdminStub.startgetAllDataSourceInformation(callback);

        } catch (AxisFault e) {
            if (e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void removeDataSourceInformationSuccessCase(String name57) throws RemoteException {

        try {
            dataSourceAdminStub.removeDataSourceInformation(name57);

        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void removeDataSourceInformationFailureCase(String name57) throws RemoteException {

        try {
            dataSourceAdminStub.removeDataSourceInformation(name57);
            Assert.fail("Getting session resource path without authenticating admin service");
            log.error("Getting session resource path without authenticating admin service");

        } catch (AxisFault e) {
            if (e.toString().contains("AxisEngine Access Denied. Please login first")) {
                log.info("authentication exception found");
            } else {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (RemoteException e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }
}
