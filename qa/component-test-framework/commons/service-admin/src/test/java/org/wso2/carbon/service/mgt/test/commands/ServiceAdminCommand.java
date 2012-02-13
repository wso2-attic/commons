/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.
 
  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
 
  http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/


package org.wso2.carbon.service.mgt.test.commands;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.ServiceException;
import org.wso2.carbon.service.mgt.ui.ServiceAdminStub;
import org.wso2.carbon.service.mgt.ui.types.carbon.ServiceGroupMetaData;
import org.wso2.carbon.service.mgt.ui.types.carbon.ServiceGroupMetaDataWrapper;

import java.rmi.RemoteException;

public class ServiceAdminCommand extends TestCase {

    private static final Log log = LogFactory.getLog(ServiceAdminCommand.class);
    ServiceAdminStub serviceAdminStub;

    public ServiceAdminCommand(ServiceAdminStub serviceAdminStub) {
        this.serviceAdminStub = serviceAdminStub;
        log.debug("ServiceAdminStub added");
    }

    public ServiceGroupMetaDataWrapper listServiceGroupsSuccessCase(String serviceType,
                                                                    String serviceGroup,
                                                                    int pageNumber)
            throws Exception {
        ServiceGroupMetaDataWrapper serviceGroupMetaDataWrapper = null;
        try {
            serviceGroupMetaDataWrapper = serviceAdminStub.listServiceGroups(serviceType, serviceGroup, pageNumber);
        }
        catch (ServiceException e) {
            Assert.fail("Unable to find service in service list");
            e.printStackTrace();
            log.error("Unable to find service in service list" + e.getMessage());

        }
        catch (RemoteException e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }

        return serviceGroupMetaDataWrapper;
    }

    public ServiceGroupMetaData listServiceGroupSuccessCase(String serviceGroupName)
            throws Exception {
        ServiceGroupMetaData serviceGroupMetaData = new ServiceGroupMetaData();
        try {
            serviceGroupMetaData = serviceAdminStub.listServiceGroup(serviceGroupName);
        }
        catch (ServiceException e) {
            Assert.fail("Unable to find service group in service list");
            e.printStackTrace();
            log.error("Unable to find service in service group list" + e.getMessage());

        }

        return serviceGroupMetaData;
    }

    public void deleteNonAdminServiceGroupSuccessCase() throws Exception {
        try {
            serviceAdminStub.deleteAllNonAdminServiceGroups();
        }
        catch (ServiceException e) {
            Assert.fail("Unable to delete service group");
            e.printStackTrace();
            log.error("Unable to delete service group" + e.getMessage());

        }
    }

    //ToDo write failure case for listServiceGroupSuccessCase
}
