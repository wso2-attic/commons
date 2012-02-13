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

package org.wso2.carbon.eventing.eventsource.test.commands;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.eventing.eventsource.ui.EventSourceAdminServiceCallbackHandler;
import org.wso2.carbon.eventing.eventsource.ui.EventSourceAdminServiceStub;
import org.wso2.carbon.eventing.eventsource.ui.types.carbon.EventSourceDTO;

import java.rmi.RemoteException;

// Command implementation of Event source admin service

public class EventSourceAdminCommand extends TestCase {

    private static final Log log = LogFactory.getLog(EventSourceAdminCommand.class);
    EventSourceAdminServiceStub eventSourceAdminServiceStub;

    public EventSourceAdminCommand(EventSourceAdminServiceStub eventSourceAdminServiceStub) {
        this.eventSourceAdminServiceStub = eventSourceAdminServiceStub;
        log.debug("eventSourceAdminServiceStub added");
    }

    public void addEventSourceSuccessCase(EventSourceDTO eventSourceDTO) throws RemoteException {

        try {
            eventSourceAdminServiceStub.addEventSource(eventSourceDTO);
        } catch (RemoteException e) {
            Assert.fail("Remote Exception " + e);
            log.error("Remote Exception " + e.getMessage());
        }
        catch (Exception e) {
            Assert.fail("Unable to add event source " + e);
            log.error("Unable to add event source" + e.getMessage());

        }
    }

    public void addEventSourceFailureCase(EventSourceDTO eventSourceDTO) throws RemoteException {

        try {
            eventSourceAdminServiceStub.addEventSource(eventSourceDTO);
            log.error("Event sources added in non login state");
            Assert.fail("Event sources added in non login state");

        } catch (RemoteException e) {
            Assert.fail("Remote Exception " + e);
            log.error("Remote Exception " + e.getMessage());
        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception " + e);
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public EventSourceDTO getEventSourceSuccessCase(String eventSourceName) throws RemoteException {
        EventSourceDTO eventSourceDTO = new EventSourceDTO();
        try {
            eventSourceDTO = eventSourceAdminServiceStub.getEventSource(eventSourceName);
        } catch (RemoteException e) {
            Assert.fail("Remote Exception " + e);
            log.error("Remote Exception " + e.getMessage());
        }
        catch (Exception e) {
            Assert.fail("Unable to get event source " + e);
            log.error("Unable to get event source" + e.getMessage());

        }
        return eventSourceDTO;
    }

    public EventSourceDTO getEventSourceFailureCase(String eventSourceName) throws RemoteException {
        EventSourceDTO eventSourceDTO = new EventSourceDTO();
        try {
            eventSourceDTO = eventSourceAdminServiceStub.getEventSource(eventSourceName);
            if (eventSourceDTO.getName().toString() != null) {
                log.error("Get event source invoked without login session.");
                Assert.fail("Get event source invoked without login session.");
            }
        }
        catch (RemoteException e) {
            Assert.fail("Remote Exception " + e);
            log.error("Remote Exception " + e.getMessage());
        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception " + e);
            log.error("Unexpected Exception" + e.getMessage());
        }
        return eventSourceDTO;
    }

    public void removeEventSourcesSuccessCase(String eventSourceName) throws RemoteException {

        try {
            eventSourceAdminServiceStub.removeEventSource(eventSourceName);
        } catch (RemoteException e) {
            Assert.fail("Remote Exception " + e);
            log.error("Remote Exception " + e.getMessage());
        }
        catch (Exception e) {
            Assert.fail("Unable to remove event source " + e);
            log.error("Unable to get remove source" + e.getMessage());
        }
    }

    public void removeEventSourceFailureCase(String eventSourceName) throws RemoteException {
        try {
            eventSourceAdminServiceStub.removeEventSource(eventSourceName);
            log.error("Remove event source invoked without login session.");
            fail("Remove event source invoked without login session.");

        }
        catch (RemoteException e) {
            Assert.fail("Remote Exception " + e);
            log.error("Remote Exception " + e.getMessage());
        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception " + e);
            log.error("Unexpected Exception" + e.getMessage());
        }
    }


    public void saveEventSourceSuccessCase(EventSourceDTO eventSourceDTO) throws RemoteException {

        try {
            eventSourceAdminServiceStub.saveEventSource(eventSourceDTO);
        } catch (RemoteException e) {
            Assert.fail("Remote Exception " + e);
            log.error("Remote Exception " + e.getMessage());
        }
        catch (Exception e) {
            Assert.fail("Unable to add event source " + e);
            log.error("Unable to add event source" + e.getMessage());

        }
    }

    public void saveEventSourceFailureCase(EventSourceDTO eventSourceDTO) throws RemoteException {

        try {
            eventSourceAdminServiceStub.saveEventSource(eventSourceDTO);
            //     eventSourceAdminServiceStub.startgetEventSource();
            log.error("Event sources added in non login state");
            Assert.fail("Event sources added in non login state");

        } catch (RemoteException e) {
            Assert.fail("Remote Exception " + e);
            log.error("Remote Exception " + e.getMessage());
        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception " + e);
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void saveEventSourceSuccessCase(String eventSourceName19,
                                           EventSourceAdminServiceCallbackHandler callback)
            throws RemoteException {

        try {
            eventSourceAdminServiceStub.startgetEventSource(eventSourceName19, callback);
            //  eventSourceAdminServiceStub.getEventSources()
        } catch (RemoteException e) {
            Assert.fail("Remote Exception " + e);
            log.error("Remote Exception " + e.getMessage());
        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception " + e);
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void saveEventSourceFailureCase(String eventSourceName19,
                                           EventSourceAdminServiceCallbackHandler callback)
            throws RemoteException {

        try {
            eventSourceAdminServiceStub.startgetEventSource(eventSourceName19, callback);
            log.error("Event sources added in non login state");
            Assert.fail("Event sources added in non login state");

        } catch (RemoteException e) {
            Assert.fail("Remote Exception " + e);
            log.error("Remote Exception " + e.getMessage());
        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception " + e);
            log.error("Unexpected Exception" + e.getMessage());
        }
    }


}
