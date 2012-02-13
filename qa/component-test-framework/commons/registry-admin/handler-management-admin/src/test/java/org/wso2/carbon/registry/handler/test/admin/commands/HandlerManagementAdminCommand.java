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
package org.wso2.carbon.registry.handler.test.admin.commands;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.handler.ui.HandlerManagementServiceStub;
import org.wso2.carbon.registry.handler.ui.beans.xsd.SimulationRequest;
import org.wso2.carbon.registry.handler.ui.beans.xsd.SimulationResponse;

/**
 * calling methods in HandlerManagementService using the returned stub
 */
public class HandlerManagementAdminCommand extends TestCase {

    private static final Log log = LogFactory.getLog(HandlerManagementAdminCommand.class);
    HandlerManagementServiceStub handlerManagementServiceStub;

    public HandlerManagementAdminCommand(
            HandlerManagementServiceStub handlerManagementServiceStub) {
        this.handlerManagementServiceStub = handlerManagementServiceStub;
        log.debug("handlerManagementServiceStub added");
    }

    /**
     * Method to create a handler using the provided configuration.
     *
     * @param payload
     * @return the handler configuration.
     * @throws java.lang.Exception - if an error occurred.
     */
    public boolean createHandlerSuccessCase(String payload) throws Exception {
        boolean value = handlerManagementServiceStub.createHandler(payload);
        return value;
    }

    public void createHandlerFailureCase(String payload) {
        try {
            handlerManagementServiceStub.createHandler(payload);
            log.error("Handler created without session cookie");
            Assert.fail("Handler created without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    /**
     * Method to delete the configuration of a named handler.
     *
     * @param handlerName the name of the handler.
     * @return whether the operation was successful.
     * @throws java.lang.Exception - if an error occurred.
     */
    public boolean deleteHandlerSuccessCase(String handlerName) throws Exception {
        boolean status;
        status = handlerManagementServiceStub.deleteHandler(handlerName);
        return status;
    }

    public void deleteHandlerFailureCase(String handlerName) {
        try {
            handlerManagementServiceStub.deleteHandler(handlerName);
            log.error("Handler deleted without session cookie");
            Assert.fail("Handler deleted without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    /**
     * Method to obtain the location at which the handler collection is stored on the registry.
     *
     * @return the resource path of the handler collection.
     * @throws java.lang.Exception - if the operation failed.
     */
    public String getHandlerCollectionLocationSuccessCase() throws Exception {

        String handlerCollectionLoc = handlerManagementServiceStub.getHandlerCollectionLocation();
        return handlerCollectionLoc;
    }

    public void getHandlerCollectionLocationFailureCase() {
        try {
            handlerManagementServiceStub.getHandlerCollectionLocation();
            log.error("Getting handler collection location without session cookie");
            Assert.fail("Getting handler collection location without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    /**
     * Method to obtain the configuration of a named handler.
     *
     * @param handlerName the name of the handler.
     * @return the handler configuration.
     * @throws java.lang.Exception - if the operation failed.
     */
    public String getHandlerConfigurationSuccessCase(String handlerName) throws Exception {

        String handlerConfiguration = handlerManagementServiceStub.getHandlerConfiguration(handlerName);
        return handlerConfiguration;
    }

    public void getHandlerConfigurationFailureCase(String handlerName) {
        try {
            handlerManagementServiceStub.getHandlerConfiguration(handlerName);
            log.error("Getting handler configurations without session cookie");
            Assert.fail("Getting handler configurations without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    /**
     * Method to obtain the list of handlers that have been configured through the Handler Administration API
     *
     * @return the list of handlers
     * @throws java.lang.Exception - if the operation failed.
     */
    public String[] getHandlerListSuccessCase() throws Exception {

        String[] handlerList = handlerManagementServiceStub.getHandlerList();
        return handlerList;
    }

    public void getHandlerListFailureCase() {
        try {
            handlerManagementServiceStub.getHandlerList();
            log.error("Getting handler list without session cookie");
            Assert.fail("Getting handler list without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    /**
     * Method to set the location at which the handler collection is stored on the registry.
     *
     * @param location - the resource path of the handler collection.
     * @throws java.lang.Exception - if the operation failed.
     */
    public void setHandlerCollectionLocationSuccessCase(String location) throws Exception {
        handlerManagementServiceStub.setHandlerCollectionLocation(location);
    }

    public void setHandlerCollectionLocationFailureCase(String location) {
        try {
            handlerManagementServiceStub.setHandlerCollectionLocation(location);
            log.error("Setting handler location without session cookie");
            Assert.fail("Setting handler location without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    /**
     * Method to simulate handlers on a registry.
     *
     * @param simulationRequest the simulation request object.
     * @return the response containing the status of the handler execution.
     * @throws java.lang.Exception - if the operation failed.
     */
    public SimulationResponse simulateSuccessCase(SimulationRequest simulationRequest)
            throws Exception {
        SimulationResponse simulationResponse = handlerManagementServiceStub.simulate(simulationRequest);
        return simulationResponse;
    }

    public void simulateFailureCase(SimulationRequest simulationRequest) {
        try {
            handlerManagementServiceStub.simulate(simulationRequest);
            log.error("Simulating without session cookie");
            Assert.fail("Simulating without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    /**
     * Method to update the configuration of a named handler, using the provided configuration
     *
     * @param oldName the name of the handler.
     * @param payload the handler configuration.
     * @throws java.lang.Exception - if an error occurred.
     * @returns whether the operation was successful.
     */
    public boolean updateHandlerSuccessCase(String oldName, String payload) throws Exception {
        boolean status;
        status = handlerManagementServiceStub.updateHandler(oldName, payload);
        return status;
    }

    public void updateHandlerFailureCase(String oldName, String payload) {
        try {
            handlerManagementServiceStub.updateHandler(oldName, payload);
            log.error("Handler updated without session cookie");
            Assert.fail("Handler updated without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }
}
