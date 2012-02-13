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

package org.wso2.carbon.priority.test.commands;

import junit.framework.TestCase;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.priority.executors.ui.PriorityMediationAdminCallbackHandler;
import org.wso2.carbon.priority.executors.ui.PriorityMediationAdminStub;

import java.rmi.RemoteException;

public class PriorityExecutorsAdminCommand extends TestCase {

    private static final Log log = LogFactory.getLog(PriorityExecutorsAdminCommand.class);
    PriorityMediationAdminStub priorityMediationAdminStub;


    public PriorityExecutorsAdminCommand(
            PriorityMediationAdminStub priorityMediationAdminStub) {
        this.priorityMediationAdminStub = priorityMediationAdminStub;
        log.debug("PriorityMediationAdmin Stub Added");

    }

    public String[] getExecutorListSuccessCase() throws RemoteException {
        String[] executorList = null;
        try {
            executorList = priorityMediationAdminStub.getExecutorList();
        }

        catch (RemoteException e) {
            log.error("Remote Exception : " + e.getMessage());
            Assert.fail("Remote Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }
        return executorList;
    }

    public String[] getExecutorListFailureCase() throws RemoteException {
        String[] executorList = null;
        try {
            executorList = priorityMediationAdminStub.getExecutorList();
            Assert.fail("Priority Mediation without authenticating admin service");
            log.error("Priority Mediation without authenticating admin service");
        }

        catch (RemoteException e) {
            log.error("Remote Exception : " + e.getMessage());
            Assert.fail("Remote Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }
        return executorList;
    }

    public void addSuccessCase(String name22, OMElement extraElement23) throws RemoteException {

        try {
            priorityMediationAdminStub.add(name22, extraElement23);
        }

        catch (RemoteException e) {
            log.error("Remote Exception : " + e.getMessage());
            Assert.fail("Remote Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }

    }

    public void addFailureCase(String name22, OMElement extraElement23) throws RemoteException {

        try {
            priorityMediationAdminStub.add(name22, extraElement23);
            Assert.fail("Priority Mediation without authenticating admin service");
            log.error("Priority Mediation without authenticating admin service");
        }

        catch (RemoteException e) {
            log.error("Remote Exception : " + e.getMessage());
            Assert.fail("Remote Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }

    }

    public OMElement getExecutorSuccessCase(String name33) throws RemoteException {
        OMElement oMElement = null;
        try {

            oMElement = priorityMediationAdminStub.getExecutor(name33);
        }

        catch (RemoteException e) {
            log.error("Remote Exception : " + e.getMessage());
            Assert.fail("Remote Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }
        return oMElement;
    }

    public OMElement getExecutorFailureCase(String name33) throws RemoteException {
        OMElement oMElement = null;
        try {

            oMElement = priorityMediationAdminStub.getExecutor(name33);
            Assert.fail("Priority Mediation without authenticating admin service");
            log.error("Priority Mediation without authenticating admin service");
        }

        catch (RemoteException e) {
            log.error("Remote Exception : " + e.getMessage());
            Assert.fail("Remote Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }
        return oMElement;
    }

    public void removeSuccessCase(String name28) throws RemoteException {
        try {
            priorityMediationAdminStub.remove(name28);
        }

        catch (RemoteException e) {
            log.error("Remote Exception : " + e.getMessage());
            Assert.fail("Remote Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }

    }

    public void removeFailureCase(String name28) throws RemoteException {
        try {
            priorityMediationAdminStub.remove(name28);
            Assert.fail("Priority Mediation without authenticating admin service");
            log.error("Priority Mediation without authenticating admin service");
        }

        catch (RemoteException e) {
            log.error("Remote Exception : " + e.getMessage());
            Assert.fail("Remote Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }

    }

    public void startGetExecutorSuccessCase(String name33,
                                            PriorityMediationAdminCallbackHandler callback)
            throws RemoteException {

        try {
            priorityMediationAdminStub.startgetExecutor(name33, callback);
        }
        catch (RemoteException e) {
            log.error("Remote Exception : " + e.getMessage());
            Assert.fail("Remote Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }

    }

    public void startGetExecutorFailureCase(String name33,
                                            PriorityMediationAdminCallbackHandler callback)
            throws RemoteException {

        try {
            priorityMediationAdminStub.startgetExecutor(name33, callback);
            Assert.fail("Priority Mediation without authenticating admin service");
            log.error("Priority Mediation without authenticating admin service");
        }
        catch (RemoteException e) {
            log.error("Remote Exception : " + e.getMessage());
            Assert.fail("Remote Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }

    }

    public void startGetExecutorListSuccessCase(PriorityMediationAdminCallbackHandler callback)
            throws RemoteException {

        try {
            priorityMediationAdminStub.startgetExecutorList(callback);

        }

        catch (RemoteException e) {
            log.error("Remote Exception : " + e.getMessage());
            Assert.fail("Remote Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }

    }

    public void startGetExecutorListFailureCase(PriorityMediationAdminCallbackHandler callback)
            throws RemoteException {

        try {
            priorityMediationAdminStub.startgetExecutorList(callback);
            Assert.fail("Priority Mediation without authenticating admin service");
            log.error("Priority Mediation without authenticating admin service");

        }

        catch (RemoteException e) {
            log.error("Remote Exception : " + e.getMessage());
            Assert.fail("Remote Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }

    }

    public void updateSuccessCase(String name25, OMElement extraElement26) throws RemoteException {

        try {
            priorityMediationAdminStub.update(name25, extraElement26);

        }
        catch (RemoteException e) {
            log.error("Remote Exception : " + e.getMessage());
            Assert.fail("Remote Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }
    }

    public void updateFailureCase(String name25, OMElement extraElement26) throws RemoteException {

        try {
            priorityMediationAdminStub.update(name25, extraElement26);
            Assert.fail("Priority Mediation without authenticating admin service");
            log.error("Priority Mediation without authenticating admin service");

        }
        catch (RemoteException e) {
            log.error("Remote Exception : " + e.getMessage());
            Assert.fail("Remote Exception " + e);

        } catch (Exception e) {
            log.error("Unknown Exception : " + e.getMessage());
            Assert.fail("Unknown Exception " + e);

        }
    }
}