package org.wso2.carbon.eventing.eventsource.test.commands;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.eventing.eventsource.ui.types.carbon.EventSourceDTO;
import org.wso2.carbon.proxyadmin.ui.ProxyAdminException;
import org.wso2.carbon.proxyadmin.ui.ProxyServiceAdminStub;
import org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData;
import org.wso2.carbon.eventing.eventsource.ui.EventSourceAdminServiceStub;

import java.rmi.RemoteException;/*
* Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*  http://www.apache.org/licenses/LICENSE-2.0
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/


// Command implemantation of Event source admin service

public class EventSourceAdminCommand extends TestCase {

    private static final Log log = LogFactory.getLog(EventSourceAdminCommand.class);
    EventSourceAdminServiceStub eventSourceAdminServiceStub;

    public EventSourceAdminCommand(EventSourceAdminServiceStub eventSourceAdminServiceStub) {
        this.eventSourceAdminServiceStub = eventSourceAdminServiceStub;
    }

    public boolean addEventSourceSuccessCase(EventSourceDTO eventSourceDTO) throws RemoteException {
        boolean result = false;
        try {
            eventSourceAdminServiceStub.addEventSource(eventSourceDTO);
            result = true;
        }
        catch (Exception e) {
            Assert.fail("Unable to add event source");
            e.printStackTrace();
            log.error("Unable to add event source" + e.getMessage());

        }
        return result;
    }

    public boolean addEventSourceFailureCase(EventSourceDTO eventSourceDTO) throws RemoteException {
        boolean result = false;
        try {
            eventSourceAdminServiceStub.addEventSource(eventSourceDTO);
            result = false;
            log.error("Event sources added in non login state");
            Assert.fail("Event sources added in non login state");


        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            } else {
                result = true;
            }

        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return result;

    }

    public EventSourceDTO getEventSourceSuccessCase(String eventSourceName) throws RemoteException {
        EventSourceDTO eventSourceDTO = new EventSourceDTO();
        try {
            eventSourceDTO = eventSourceAdminServiceStub.getEventSource(eventSourceName);
        }
        catch (Exception e) {
            Assert.fail("Unable to get event source");
            e.printStackTrace();
            log.error("Unable to get event source" + e.getMessage());

        }
        return eventSourceDTO;
    }

    public boolean getEventSourceFailureCase(String eventSourceName) throws RemoteException {
        boolean result = false;
        EventSourceDTO eventSourceDTO = new EventSourceDTO();
        try {
            eventSourceDTO = eventSourceAdminServiceStub.getEventSource(eventSourceName);
            if (eventSourceDTO.getName().toString() != null) {
                log.error("Get event source invoked without login session.");
                Assert.fail("Get event source invoked without login session.");
            }
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            } else {
                result = true;
            }

        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return result;

    }

    public boolean removeEventSourcesSuccessCase(String eventSourceName) throws RemoteException {
        boolean result = false;
        try {
            eventSourceAdminServiceStub.removeEventSource(eventSourceName);
            result = true;
        }
        catch (Exception e) {
            Assert.fail("Unable to remove event source");
            e.printStackTrace();
            log.error("Unable to get remove source" + e.getMessage());
            result = false;
        }
        return result;
    }

    public boolean removeEventSourceFailureCase(String eventSourceName) throws RemoteException {
        boolean result = false;
        try {
            eventSourceAdminServiceStub.removeEventSource(eventSourceName);
            log.error("Remove event source invoked without login session.");
            Assert.fail("Remove event source invoked without login session.");

        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            } else {
                result = true;
            }

        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return result;

    }


}
