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
package org.wso2.carbon.admin.service;

import junit.framework.Assert;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.bpel.stub.mgt.InstanceManagementServiceStub;
import org.wso2.carbon.bpel.stub.mgt.types.*;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.fail;

public class AdminServiceBpelInstanceManager {
    String ServiceEndPoint = null;
    String SessionCookie = null;
    private static final Log log = LogFactory.getLog(AdminServiceBpelInstanceManager.class);

    InstanceManagementServiceStub instanceManagementServiceStub = null;

    public AdminServiceBpelInstanceManager(String serviceEndPoint, String sessionCookie) {
        this.ServiceEndPoint = serviceEndPoint;
        this.SessionCookie = sessionCookie;
    }

    private InstanceManagementServiceStub setInstanceManagementStub() {
        final String packageMgtServiceUrl = ServiceEndPoint + "InstanceManagementService";
        AuthenticateStub authenticateStub = new AuthenticateStub();
        InstanceManagementServiceStub instanceManagementStub = null;
        try {

            instanceManagementStub = new InstanceManagementServiceStub(packageMgtServiceUrl);
            authenticateStub.authenticateStub(SessionCookie, instanceManagementStub);

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            Assert.fail(axisFault.getMessage());
        }
        return instanceManagementStub;
    }

    public PaginatedInstanceList listAllInstances() {

        instanceManagementServiceStub = this.setInstanceManagementStub();

        PaginatedInstanceList paginatedInstanceList = null;
        try {
            /** The filter set on the is not filtering appropriate services as the filter is ment . it is require to filer the service manually**/
            paginatedInstanceList = instanceManagementServiceStub.getPaginatedInstanceList("", "", 300, 0);

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            fail(axisFault.getMessage());
        } catch (Exception e) {
            log.error("ExceptionOccoured : " + e.getMessage());
            Assert.fail("ExceptionOccoured : " + e.getMessage());
        }

        return paginatedInstanceList;
    }

    public PaginatedInstanceList filterPageInstances(String processId) {

        instanceManagementServiceStub = this.setInstanceManagementStub();

        PaginatedInstanceList paginatedInstanceList = null;
        PaginatedInstanceList filteredInstanceList = null;
        try {
            /** The filter set on the is not filtering appropriate services as the filter is ment . it is require to filer the service manually**/
            paginatedInstanceList = instanceManagementServiceStub.getPaginatedInstanceList("", "", 300, 0);
            filteredInstanceList = new PaginatedInstanceList();
            if (paginatedInstanceList.isInstanceSpecified()) {
                for (LimitedInstanceInfoType instance : paginatedInstanceList.getInstance()) {
                    if (instance.getPid().toString().contains(processId)) {
                        filteredInstanceList.addInstance(instance);
                    }
                }
            }
        } catch (AxisFault axisFault) {
            log.error("Axis fault" + axisFault.getMessage());
            Assert.fail(axisFault.getMessage());
        } catch (Exception e) {
            log.error("ExceptionOccoured : " + e.getMessage());
            Assert.fail("ExceptionOccoured : " + e.getMessage());
        }
        return filteredInstanceList;
    }


    public List<String> listInstances(String processId) {
        List<String> instanceIds = new ArrayList<String>();

        instanceManagementServiceStub = this.setInstanceManagementStub();

        try {
            /** The filter set on the is not filtering appropriate services as the filter is ment . it is require to filer the service manually**/
            PaginatedInstanceList paginatedInstanceList = instanceManagementServiceStub.getPaginatedInstanceList("", "", 300, 0);
            if (paginatedInstanceList.isInstanceSpecified()) {
                for (LimitedInstanceInfoType instance : paginatedInstanceList.getInstance()) {
                    instanceIds.add(instance.getIid());
                    log.info("ProcessId: " + instance.getPid() +
                            "\nInstanceId: " + instance.getIid() +
                            "\nStarted: " + instance.getDateStarted().getTime() +
                            "\nState: " + instance.getStatus() +
                            "\nLast-Active: " + instance.getDateLastActive().getTime() + "\n");
                }
            }
        } catch (AxisFault axisFault) {
            log.error("Axis fault" + axisFault.getMessage());
            Assert.fail(axisFault.getMessage());
        } catch (Exception e) {
            log.error("ExceptionOccoured : " + e.getMessage());
            Assert.fail("ExceptionOccoured : " + e.getMessage());
        }

        return instanceIds;
    }

    public void deleteInstance(String instanceId) {
        String instanceFilter = "IID="+instanceId;
        instanceManagementServiceStub = this.setInstanceManagementStub();
        try {
            log.info("Deleting all the instances");
            int instanceCount = instanceManagementServiceStub.
                    deleteInstances(instanceFilter, true);
            assertFalse("Instance deletion failed!", instanceCount == 0);

            assertFalse("Instance not deleted !",listInstances(instanceId).contains(instanceId));
        } catch (AxisFault axisFault) {
            log.error("Axis fault" + axisFault.getMessage());
            Assert.fail(axisFault.getMessage());
        } catch (Exception e) {
            log.error("ExceptionOccoured : " + e.getMessage());
            Assert.fail("ExceptionOccoured : " + e.getMessage());
        }
    }


   public void clearInstancesOfProcess(String processId)
   {
    PaginatedInstanceList instanceList = filterPageInstances(processId);
       for(LimitedInstanceInfoType instanceInfo :instanceList.getInstance())
       {
           deleteInstance(instanceInfo.getIid());
       }
      Assert.assertTrue("Instance on process "+processId+" not cleared",filterPageInstances(processId).getInstance()==null);
   }

    public InstanceInfoType getInstanceInfo(String instanceId) {
        instanceManagementServiceStub = this.setInstanceManagementStub();
        InstanceInfoType instanceInfo = null;
        try {
            instanceInfo = instanceManagementServiceStub.
                    getInstanceInfo(Long.parseLong(instanceId));

                assertFalse("Empty Instance Found: ", instanceInfo.getIid().isEmpty());

        } catch (AxisFault axisFault) {
            log.error("Axis fault" + axisFault.getMessage());
            Assert.fail(axisFault.getMessage());
        } catch (Exception e) {
            log.error("ExceptionOccoured : " + e.getMessage());
            Assert.fail("ExceptionOccoured : " + e.getMessage());
        }
        return instanceInfo;
    }

    public boolean assertInstanceInfo(String status, String variableName, String expectedVarValue,
                                       List<String> instanceIds) {
        instanceManagementServiceStub = this.setInstanceManagementStub();
        boolean variableFound = false;
        try {
            for (String iid : instanceIds) {
                InstanceInfoType instanceInfo = instanceManagementServiceStub.
                        getInstanceInfo(Long.parseLong(iid));
                if (status != null) {
                    log.info("Validating instance status, expected: " + status +
                            " actual: " + instanceInfo.getStatus());
                    assertFalse("Status of instance " + iid + " is not equal to " + status +
                            " but " + instanceInfo.getStatus().getValue(),
                            !instanceInfo.getStatus().getValue().equals(status.toUpperCase()));
                }
                if (variableName == null) {
                    variableFound = true;
                } else {
                    Variables_type0 variables = instanceInfo.getRootScope().getVariables();
                    VariableInfoType[] variableList = variables.getVariableInfo();
                    for (VariableInfoType variable : variableList) {
                        String varName = variable.getSelf().getName();
                        String varValue = null;
                        for (OMElement varElement : variable.getValue().getExtraElement()) {
                            if (varValue == null) {
                                varValue = varElement.toString();
                            } else {
                                varValue += varElement.toString();
                            }

                            if (variableName != null && expectedVarValue != null) {
                                if (varName.equals(variableName)) {
                                    if (varValue.contains(expectedVarValue)) {
                                        variableFound = true;
                                    } else {
                                        fail("Incorrect Test Result: " + varValue +
                                                " Expected" + expectedVarValue + "in the result");
                                    }
                                }
                            } else {
                                variableFound = true;
                            }
                            log.info("Variable name: " + varName + "\nVariable Value: " +
                                    varValue);
                        }
                    }
                }
            }

            assertFalse(variableName + " variable not found", !variableFound);
        } catch (AxisFault axisFault) {
            log.error("Axis fault" + axisFault.getMessage());
            Assert.fail(axisFault.getMessage());
        } catch (Exception e) {
            log.error("ExceptionOccoured : " + e.getMessage());
            Assert.fail("ExceptionOccoured : " + e.getMessage());
        }
        return variableFound;
    }


    public void performAction(String instanceId, InstanceOperation operation) {
        instanceManagementServiceStub = this.setInstanceManagementStub();
        switch (operation) {
            case SUSPEND:
                try {
                    instanceManagementServiceStub.suspendInstance(Long.parseLong(instanceId));
                } catch (Exception e) {
                    log.error("ExceptionOccoured : " + e.getMessage());
                    Assert.fail("ExceptionOccoured : " + e.getMessage());
                }
                break;
            case RESUME:
                try {
                    instanceManagementServiceStub.resumeInstance(Long.parseLong(instanceId));
                } catch (Exception e) {
                    log.error("ExceptionOccoured : " + e.getMessage());
                    Assert.fail("ExceptionOccoured : " + e.getMessage());
                }
                break;
            case TERMINATE:
                try {
                    instanceManagementServiceStub.terminateInstance(Long.parseLong(instanceId));
                } catch (Exception e) {
                    log.error("ExceptionOccoured : " + e.getMessage());
                    Assert.fail("ExceptionOccoured : " + e.getMessage());
                }
                break;
        }
    }

    public static enum InstanceOperation {
        SUSPEND,
        RESUME,
        TERMINATE
    }

}
