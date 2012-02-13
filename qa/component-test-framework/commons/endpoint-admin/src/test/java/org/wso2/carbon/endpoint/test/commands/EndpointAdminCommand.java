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

package org.wso2.carbon.endpoint.test.commands;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.endpoint.ui.types.EndpointAdminException;
import org.wso2.carbon.endpoint.ui.types.EndpointAdminStub;
import org.wso2.carbon.endpoint.ui.types.common.ConfigurationObject;
import org.wso2.carbon.endpoint.ui.types.common.to.AddressEndpointData;
import org.wso2.carbon.endpoint.ui.types.common.to.DefaultEndpointData;
import org.wso2.carbon.endpoint.ui.types.common.to.EndpointMetaData;
import org.wso2.carbon.endpoint.ui.types.common.to.LoadBalanceEndpointData;
import org.wso2.carbon.endpoint.ui.types.common.to.WSDLEndpointData;

import java.rmi.RemoteException;

public class EndpointAdminCommand extends TestCase {

    EndpointAdminStub endpointAdminStub;
    private static final Log log = LogFactory.getLog(EndpointAdminCommand.class);

    public EndpointAdminCommand(EndpointAdminStub endpointAdminStub) {
        this.endpointAdminStub = endpointAdminStub;
        log.debug("endpointAdminStub added");
    }

    public void addEndpointExecuteSuccessCase(String endpointXML) {
        log.debug("AddEndPointCommand executeSuccessCase");
        boolean epAdded = false;
        try {
            epAdded = endpointAdminStub.addEndpoint(endpointXML);
        }
        catch (Exception e) {
            fail("Unexpected exception thrown: " + e);
        }
        assertTrue(epAdded);
        log.info("AddEndPointCommand SuccessCase passed");
    }

    public void addEndpointExecuteFailureCase(String endpointXML) {
        log.debug("AddEndPointCommand executeFailureCase");
        boolean epAdded = false;
        try {
            epAdded = endpointAdminStub.addEndpoint(endpointXML);
            fail("Expected exception did not occur");
        }
        catch (RemoteException e) {
            // e.printStackTrace();
        }
        catch (EndpointAdminException e) {
            fail("Unexpected exception thrown: " + e);
        }
        assertFalse(epAdded);
        log.info("AddEndPointCommand FailureCase passed");
    }

    public void deleteEndpointExecuteSuccessCase(String endpointName) {
        log.debug("DeleteEndPointCommand executeSuccessCase");
        boolean epAdded = false;

        try {
            epAdded = endpointAdminStub.deleteEndpoint(endpointName);
        }
        catch (Exception e) {
            fail("Unexpected exception thrown: " + e);
        }

        assertTrue(epAdded);
        log.info("DeleteEndPointCommand SuccessCase passed");
    }

    public void deleteEndpointExecuteFailureCase(String endpointName) {
        log.debug("DeleteEndPointCommand executeFailureCase");
        boolean epAdded = false;
        try {
            epAdded = endpointAdminStub.deleteEndpoint(endpointName);
            fail("Expected exception did not occur");
        }
        catch (RemoteException e) {
            log.error("Error occurred: " + e);
        }
        catch (EndpointAdminException e) {
            fail("Unexpected exception thrown: " + e);
        }
        assertFalse(epAdded);
        log.info("DeleteEndPointCommand FailureCase passed");

    }

    public int getEndpointCountExecuteSuccessCase() {
        log.debug("GetEndpointCountCommand executeSuccessCase");

        int counts = -2;
        try {
            counts = endpointAdminStub.getEndpointCount();
        }
        catch (Exception e) {
            fail("Error occurred: " + e);
        }
        assertTrue(counts != -2);
        log.info("GetEndpointCountCommand SuccessCase passed");
        return counts;
    }

    public void getEndpointCountExecuteFailureCase() {
        log.debug("GetEndpointCountCommand executeFailureCase");

        int counts = -2;
        try {
            counts = endpointAdminStub.getEndpointCount();
            fail("Expected exception did not occur");
        }
        catch (RemoteException e) {
            log.error("Remote Exception: " + e);
        }
        catch (EndpointAdminException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }

        assertTrue(counts == -2);
        log.info("GetEndpointCountCommand FailureCase passed");
    }

    public String[] getEndpointsNamesExecuteSuccessCase() {
        log.debug("GetEndPointsNamesCommand executeSuccessCase");
        String[] names = null;
        try {
            Thread.sleep(4000);
            names = endpointAdminStub.getEndPointsNames();

        }
        catch (Exception e) {
            fail("Unexpected exception thrown: " + e);
        }

        if (names == null) {
            return new String[]{};
        }
        log.info("GetEndPointsNamesCommand SuccessCase passed");
        return names;
    }


    public void getEndpointsNamesExecuteFailureCase() {
        log.debug("GetEndPointsNamesCommand executeFailureCase");

        String[] names = null;
        try {
            names = endpointAdminStub.getEndPointsNames();
            fail("Expected exception did not occur");
        }
        catch (RemoteException e) {
            log.error("Remote Exception: " + e);
        }
        catch (EndpointAdminException e) {
            fail("Endpoint Admin Exception occurred: " + e);
        } catch (Exception e) {
            log.error("error occur while getting Endpoint names: " + e);
        }

        assertNull(names);
        log.info("GetEndPointsNamesCommand FailureCase passed");
    }

    public AddressEndpointData getAddressEndpointSuccessCase(String endpointName) {
        AddressEndpointData addressEndpointData = new AddressEndpointData();
        try {
            addressEndpointData = endpointAdminStub.getAddressEndpoint(endpointName);
            log.debug("getting endpoint data");
        }
        catch (Exception e) {
            log.error("error occur while getting addressEndpointData: " + e);
        }
        return addressEndpointData;
    }

    public AddressEndpointData getAddressEndpointFailureCase(String endpointName) {
        AddressEndpointData addressEndpointData = new AddressEndpointData();
        try {
            addressEndpointData = endpointAdminStub.getAddressEndpoint(endpointName);
            log.error("Getting endpoint data without authenticating admin service");
            Assert.fail("Getting endpoint data without authenticating admin service");
        }
        catch (Exception e) {
            log.error("error occur while getting address endpoint data: " + e);
        }
        return addressEndpointData;
    }

    public LoadBalanceEndpointData getLoadBalanceEndpointDataSuccessCase(String endpointName) {
        LoadBalanceEndpointData loadBalanceEndpointData = new LoadBalanceEndpointData();
        try {
            loadBalanceEndpointData = endpointAdminStub.getLoadBalanceData(endpointName);
            log.debug("getting load-balance endpoint data");
        }
        catch (Exception e) {
            log.error("error occur while getting load-balance endpoint data: " + e);
        }
        return loadBalanceEndpointData;
    }

    public LoadBalanceEndpointData getLoadBalanceEndpointDataFailueCase(String endpointName) {
        LoadBalanceEndpointData loadBalanceEndpointData = new LoadBalanceEndpointData();
        try {
            loadBalanceEndpointData = endpointAdminStub.getLoadBalanceData(endpointName);
            log.error("Getting load-balance endpoint data without authenticating admin service");
            Assert.fail("Getting load-balance endpoint data without authenticating admin service");
        }
        catch (Exception e) {
        }
        return loadBalanceEndpointData;
    }

    public DefaultEndpointData getDefaultEndpointDataSuccessCase(String endpointName) {
        DefaultEndpointData defaultEndpointData = new DefaultEndpointData();
        try {
            defaultEndpointData = endpointAdminStub.getDefaultEndpoint(endpointName);
            log.debug("getting default endpoint data");
        }
        catch (Exception e) {
            log.error("error occur while getting default endpoint data: " + e);
        }
        return defaultEndpointData;
    }

    public DefaultEndpointData getDefaultEndpointDataFailureCase(String endpointName) {
        DefaultEndpointData defaultEndpointData = new DefaultEndpointData();
        try {
            defaultEndpointData = endpointAdminStub.getDefaultEndpoint(endpointName);
            log.error("Getting default endpoint data without authenticating admin service");
            Assert.fail("Getting default endpoint data without authenticating admin service");
        }
        catch (Exception e) {
        }
        return defaultEndpointData;
    }

    public ConfigurationObject[] getDependentSuccessCase(String endpointName) {
        ConfigurationObject[] configurationObjects = null;
        try {
            configurationObjects = endpointAdminStub.getDependents(endpointName);
            log.debug("getting dependent data");
        }
        catch (Exception e) {
            log.error("error occur while getting default endpoint data: " + e);
        }
        return configurationObjects;
    }

    public ConfigurationObject[] getDependentFailureCase(String endpointName) {
        ConfigurationObject[] configurationObjects = null;
        try {
            configurationObjects = endpointAdminStub.getDependents(endpointName);
            log.error("Getting dependent data endpoint data without authenticating admin service");
            Assert.fail("Getting dependent data endpoint data without authenticating admin service");
        }
        catch (Exception e) {
        }
        return configurationObjects;
    }

    public boolean addDynamicEndpointSuccessCase(String key, String endpointName) {
        boolean methodStatus = false;
        try {
            methodStatus = endpointAdminStub.addDynamicEndpoint(key, endpointName);
            log.debug("Dynamic endpoint added");
        }
        catch (Exception e) {
            log.error("error occur while adding dynamic endpoint: " + e);
        }
        return methodStatus;
    }

    public boolean addDynamicEndpointFailureCase(String key, String endpointName) {
        boolean methodStatus = false;
        try {
            methodStatus = endpointAdminStub.addDynamicEndpoint(key, endpointName);
            log.error("Dynamic endpoint added without authenticating admin service");
            Assert.fail("Dynamic endpoint added without authenticating admin service");
        }
        catch (Exception e) {
        }
        return methodStatus;
    }

    public OMElement convertEpDataSuccessCase(OMElement epData) {
        OMElement omElement = null;
        try {
            omElement = endpointAdminStub.convertToEndpointData(epData);
            log.debug("Endpoint data converted");
        }
        catch (Exception e) {
            log.error("error occur while converting endpoint data: " + e);
        }
        return omElement;
    }

    public void convertEpDataFailureCase(OMElement epData) {
        try {
            endpointAdminStub.convertToEndpointData(epData);
            log.error("Converting endpoint data without authenticating admin service");
            Assert.fail("Converting endpoint data without authenticating admin service");
        }
        catch (Exception e) {
        }
    }

    public boolean deleteDynamicEpSuccessCase(String key) {
        boolean methodStatus = false;
        try {
            methodStatus = endpointAdminStub.deleteDynamicEndpoint(key);
            log.debug("Dynamic endpoint deleted");
        }
        catch (Exception e) {
            log.error("error occur while deleting dynamic endpoint: " + e);
        }
        return methodStatus;
    }

    public void deleteDynamicEpFailureCase(String key) {
        try {
            endpointAdminStub.deleteDynamicEndpoint(key);
            log.error("deleting dynamic endpoint without authenticating admin service");
            Assert.fail("deleting dynamic endpoint without authenticating admin service");
        }
        catch (Exception e) {
        }
    }

    public void enableStatSuccessCase(String epName) {
        try {
            endpointAdminStub.enableStatistics(epName);
            log.debug("Statistics enabled");
        }
        catch (Exception e) {
            log.error("error occur while enabling statistics: " + e);
        }
    }

    public void enableStatFailureCase(String epName) {
        try {
            endpointAdminStub.enableStatistics(epName);
            log.error("Stat enabled without authenticating admin service");
            Assert.fail("stat enabled without authenticating admin service");
        }
        catch (Exception e) {
            log.error("error occur while enabling statistics: " + e);
        }
    }

    public void disableStatSuccessCase(String epName) {
        try {
            endpointAdminStub.disableStatistics(epName);
            log.debug("Statistics disabled");
        }
        catch (Exception e) {
            log.error("error occur while disable statistics: " + e);
        }
    }

    public void disableStatFailureCase(String epName) {
        try {
            endpointAdminStub.disableStatistics(epName);
            log.error("Stat disabled without authenticating admin service");
            Assert.fail("stat disabled without authenticating admin service");
        }
        catch (Exception e) {
            log.error("error occur while disable statistics: " + e);
        }
    }

    public WSDLEndpointData getDlEndpointSuccessCase(String epName) {
        WSDLEndpointData wsdlEndpointData = new WSDLEndpointData();
        try {
            wsdlEndpointData = endpointAdminStub.getdlEndpoint(epName);
            log.debug("getting wsdl endpoint data");
        }
        catch (Exception e) {
            log.error("error occur while getting wsdl endpoint data: " + e);
        }
        return wsdlEndpointData;
    }

    public void getDlEndpointFailureCase(String epName) {
        WSDLEndpointData wsdlEndpointData = new WSDLEndpointData();
        try {
            wsdlEndpointData = endpointAdminStub.getdlEndpoint(epName);
            if (wsdlEndpointData != null) {
                log.error("getting wsdl endpoint data without authenticating admin service");
                Assert.fail("getting wsdl endpoint data without authenticating admin service");
            }
        }
        catch (Exception e) {
            log.error("error occur while getting wsdl endpoint data: " + e);
        }
    }

    public String getDynamicEndpointSuccessCase(String key) {
        String dynamicEndpoint = null;
        try {
            dynamicEndpoint = endpointAdminStub.getDynamicEndpoint(key);
            log.debug("getting dynamic endpoint");
        }
        catch (Exception e) {
            log.error("error occur while getting dynamic endpoint: " + e);
        }
        return dynamicEndpoint;
    }

    public void getDynamicEndpointFailureCase(String key) {
        String dynamicEndpoint = null;
        try {
            dynamicEndpoint = endpointAdminStub.getDynamicEndpoint(key);
            if (dynamicEndpoint != null) {
                log.error("getting dynamic endpoint without authenticating admin service");
                Assert.fail("getting dynamic endpoint without authenticating admin service");
            }
        }
        catch (Exception e) {
            log.error("error occur while getting dynamic endpoint: " + e);
        }
    }

    public int getDynamicEndpointCountSuccessCase() {
        int dynamicEpCount = -1;
        try {
            dynamicEpCount = endpointAdminStub.getDynamicEndpointCount();
            log.debug("getting dynamic endpoint count");
        }
        catch (Exception e) {
            log.error("error occur while getting dynamic endpoint count: " + e);
        }
        return dynamicEpCount;
    }

    public void getDynamicEndpointCountFailureCase() {
        try {
            endpointAdminStub.getDynamicEndpointCount();
            log.error("getting dynamic endpoint without authenticating admin service");
            Assert.fail("getting dynamic endpoint without authenticating admin service");
        }
        catch (Exception e) {
            log.error("error occur while getting dynamic endpoint count: " + e);
        }
    }

    public String[] getDynamicEndpointsSuccessCase(int pageNumber, int endpointsPerPage) {
        String[] dynamicEndpoints = null;
        try {
            dynamicEndpoints = endpointAdminStub.getDynamicEndpoints(pageNumber, endpointsPerPage);
            log.debug("getting dynamic endpoints");
        }
        catch (Exception e) {
            log.error("error occur while getting dynamic endpoints: " + e);
        }
        return dynamicEndpoints;
    }

    public void getDynamicEndpointsFailureCase(int pageNumber, int endpointsPerPage) {
        String[] dynamicEndpoint = null;
        try {
            dynamicEndpoint = endpointAdminStub.getDynamicEndpoints(pageNumber, endpointsPerPage);
            if (dynamicEndpoint[0] != null) {
                log.error("getting dynamic endpoints without authenticating admin service");
                Assert.fail("getting dynamic endpoints without authenticating admin service");
            }
        }
        catch (Exception e) {
            log.error("error occur while getting dynamic endpoints: " + e);
        }
    }

    public String getEndpointSuccessCase(String epName) {
        String endpoint = null;
        try {
            endpoint = endpointAdminStub.getEndpoint(epName);
            log.debug("getting endpoint");
        }
        catch (Exception e) {
            log.error("error occur while getting endpoint: " + e);
        }
        return endpoint;
    }

    public void getEndpointFailureCase(String epName) {
        try {
            endpointAdminStub.getEndpoint(epName);
            log.error("getting endpoint without authenticating admin service");
            Assert.fail("getting endpoint without authenticating admin service");
        }
        catch (Exception e) {
            log.error("error occur while getting endpoint: " + e);
        }
    }

    public boolean saveDynamicEndpointSuccessCase(String key, String epName) {
        boolean isSaved = false;
        try {
            isSaved = endpointAdminStub.saveDynamicEndpoint(key, epName);
            log.debug("Dynamic endpoint saved");
        }
        catch (Exception e) {
            log.error("error occur while saving dynamic endpoint: " + e);
        }
        return isSaved;
    }

    public void saveDynamicEndpointFailureCase(String key, String epName) {
        try {
            endpointAdminStub.saveDynamicEndpoint(key, epName);
            log.error("saving dynamic endpoint without authenticating admin service");
            Assert.fail("saving dynamic endpoint without authenticating admin service");
        }
        catch (Exception e) {
            log.error("error occur while saving dynamic endpoint: " + e);
        }
    }

    public boolean saveEndpointSuccessCase(String epName) {
        boolean isSaved = false;
        try {
            isSaved = endpointAdminStub.saveEndpoint(epName);
            log.debug("endpoint saved");
        }
        catch (Exception e) {
            log.error("error occur while saving endpoint: " + e);
        }
        return isSaved;
    }

    public void saveEndpointFailureCase(String epName) {
        try {
            endpointAdminStub.saveEndpoint(epName);
            log.error("saving endpoint without authenticating admin service");
            Assert.fail("saving endpoint without authenticating admin service");
        }
        catch (Exception e) {
            log.error("error occur while saving endpoint: " + e);
        }
    }

    public boolean updateDynamicEndpointSuccessCase(String key, String epName) {
        boolean isUpdate = false;
        try {
            isUpdate = endpointAdminStub.updateDynamicEndpoint(key, epName);
            log.debug("Dynamic endpoint updated");
        }
        catch (Exception e) {
            log.error("error occur while updating dynamic endpoint: " + e);
        }
        return isUpdate;
    }

    public void updateDynamicEndpointFailureCase(String key, String epName) {
        try {
            endpointAdminStub.updateDynamicEndpoint(key, epName);
            log.error("updating dynamic endpoint without authenticating admin service");
            Assert.fail("updating dynamic endpoint without authenticating admin service");
        }
        catch (Exception e) {
            log.error("error occur while updating dynamic endpoint: " + e);
        }
    }

    public EndpointMetaData[] getEndpointMetaDataSuccessCase(int pageNumber, int endpointsPerPage) {
        EndpointMetaData[] endpointMetaData = null;
        try {
            endpointMetaData = endpointAdminStub.endpointData(pageNumber, endpointsPerPage);
            log.debug("Getting Endpoint meta data");
        }
        catch (Exception e) {
            log.error("error occur while getting endpoint meta data: " + e);
        }
        return endpointMetaData;
    }

    public void getEndpointMetaDataFailureCase(int pageNumber, int endpointsPerPage) {
        try {
            endpointAdminStub.endpointData(pageNumber, endpointsPerPage);
            log.error("getting endpoint meta data without authenticating admin service");
            Assert.fail("getting endpoint meta data without authenticating admin service");
        }
        catch (Exception e) {
            log.error("error occur while getting endpoint meta data: " + e);

        }
    }

}





