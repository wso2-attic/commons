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

package org.wso2.carbon.governance.services.test.admin.commands;

import junit.framework.TestCase;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.governance.services.ui.AddServicesServiceCallbackHandler;
import org.wso2.carbon.governance.services.ui.AddServicesServiceStub;
import org.wso2.carbon.governance.services.ui.RegistryExceptionException;
import org.wso2.carbon.registry.resource.ui.ExceptionException;

import javax.activation.DataHandler;
import java.net.URL;
import java.rmi.RemoteException;

public class AddServiceAdminCommand extends TestCase {
    private static final Log log = LogFactory.getLog(AddServiceAdminCommand.class);
    AddServicesServiceStub addServicesServiceStub;

    public AddServiceAdminCommand(
            AddServicesServiceStub addServicesServiceStub) {
        this.addServicesServiceStub = addServicesServiceStub;
        log.debug("addServicesServiceStub added");
    }

    /**
     * Method to add a new service to the repository.
     *
     * @param info the service details that will be added.
     * @return whether the operation was successful or not.
     */
    public boolean addServiceSuccessCase(String info) {
        boolean result = false;
        try {
            result = addServicesServiceStub.addService(info);
            System.out.println(result);
        } catch (RemoteException e) {
            e.printStackTrace();
            log.error("Add service to registry failed: " + e.getMessage());
            Assert.fail("Add service to registry failed");
        } catch (RegistryExceptionException e) {
            e.printStackTrace();
            log.error("Add service failed with registry exception: " + e.getMessage());
            Assert.fail("Add service failed with registry exception");
        }
        return result;
    }

    /**
     * Method to obtain the content of the service by the given name for editing.
     *
     * @param serviceName the name of the service to edit.
     * @return the content of the service resource.
     */
    public String editServiceSuccessCase(String serviceName) {
        String editResult = "";
        try {
            editResult = addServicesServiceStub.editService(serviceName);
        } catch (RemoteException e) {
            e.printStackTrace();
            log.error("Failed to obtain content of the service by the given name for editing: " + e.getMessage());
            Assert.fail("Failed to obtain content of the service by the given name for editing");
        } catch (RegistryExceptionException e) {
            e.printStackTrace();
            log.error("Failed to obtain content of the service with registry exception: " + e.getMessage());
            Assert.fail("Failed to obtain content of the service with registry exception");
        }
        return editResult;
    }

    /**
     * Method to determine whether the given user can make changes to a service at the given resource path.
     *
     * @param path the resource path.
     * @return true if the resource at the given path can be changed.
     */
    public boolean canChangeServiceSuccessCase(String path) {
        boolean serviceChangeStatus = false;
        try {
            addServicesServiceStub.canChange(path);
        } catch (RemoteException e) {
            e.printStackTrace();
            log.error("given user can't make changes to a service at the given resource path: " + e.getMessage());
            Assert.fail("given user can't make changes to a service at the given resource path");
        } catch (org.wso2.carbon.governance.services.ui.ExceptionException e) {
            e.printStackTrace();
            log.error("Can changed failed with governance service UI exception: " + e.getMessage());
            Assert.fail("Can changed failed with governance service UI exception");
        }
        return serviceChangeStatus;
    }

    /**
     * Method to obtain the path at which services are stored on the repository.
     *
     * @return the path at which services are stored.
     */
    public String getServiceSuccessCase() {
        String servicePath = "";
        try {
            addServicesServiceStub.getServicePath();
        } catch (RemoteException e) {
            e.printStackTrace();
            log.error("No path returned for service storage location: " + e.getMessage());
            Assert.fail("No path returned for service storage location");
        } catch (RegistryExceptionException e) {
            e.printStackTrace();
        }
        return servicePath;
    }

    /**
     * get available aspects
     *
     * @return array of all available aspects
     */
    public String[] getAvailableAspectsSuccessCase() {
        String[] aspects = null;
        try {
            aspects = addServicesServiceStub.getAvailableAspects();
        } catch (RemoteException e) {
            e.printStackTrace();
            log.error("No path returned for service storage location: " + e.getMessage());
            Assert.fail("No path returned for service storage location");

        }
        return aspects;
    }

    /**
     * Method to obtain the current service UI configuration.
     *
     * @return Method to obtain the current service UI configuration.
     */
    public String getServiceConfigurationSuccessCase() {
        String serviceConfiguration = null;
        try {
            serviceConfiguration = addServicesServiceStub.getServiceConfiguration();
        } catch (RemoteException e) {
            e.printStackTrace();
            log.error("Can not obtain current service UI configuration in XML format: " + e.getMessage());
            Assert.fail("Can not obtain current service UI configuration in XML format");

        } catch (RegistryExceptionException e) {
            log.error("Can not obtain current service UI configuration in XML format: " + e.getMessage());
            Assert.fail("Can not obtain current service UI configuration in XML format");
        }
        return serviceConfiguration;
    }
}
