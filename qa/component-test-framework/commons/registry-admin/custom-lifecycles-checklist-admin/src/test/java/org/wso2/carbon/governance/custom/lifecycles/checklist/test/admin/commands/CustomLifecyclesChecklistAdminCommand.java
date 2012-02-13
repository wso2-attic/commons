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
package org.wso2.carbon.governance.custom.lifecycles.checklist.test.admin.commands;

import junit.framework.TestCase;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.governance.custom.lifecycles.checklist.ui.CustomLifecyclesChecklistAdminServiceStub;
import org.wso2.carbon.governance.custom.lifecycles.checklist.ui.ExceptionException;
import org.wso2.carbon.governance.custom.lifecycles.checklist.ui.beans.xsd.LifecycleBean;

import java.rmi.RemoteException;

/**
 * calling methods in CustomLifecyclesChecklistAdminService using the returned stub
 */
public class CustomLifecyclesChecklistAdminCommand extends TestCase {

    private static final Log log = LogFactory.getLog(CustomLifecyclesChecklistAdminCommand.class);
    CustomLifecyclesChecklistAdminServiceStub customLifecyclesChecklistAdminServiceStub;

    public CustomLifecyclesChecklistAdminCommand(
            CustomLifecyclesChecklistAdminServiceStub customLifecyclesChecklistAdminServiceStub) {
        this.customLifecyclesChecklistAdminServiceStub = customLifecyclesChecklistAdminServiceStub;
        log.debug("customLifecyclesChecklistAdminServiceStub added");
    }

    public void addAspectSuccessCase(String path, String aspect) {
        try {
            customLifecyclesChecklistAdminServiceStub.addAspect(path, aspect);
        } catch (RemoteException e) {
            throw new RuntimeException("Exception occurred while adding life-cycle to the resource : " + e);
        } catch (ExceptionException e) {
            throw new RuntimeException("Exception occurred while adding life-cycle to the resource : " + e);
        }
    }

    public void createLifecycleFailureCase(String path, String aspect) {
        try {
            customLifecyclesChecklistAdminServiceStub.addAspect(path, aspect);
            Assert.fail("Lifecycle can add using CustomLifecyclesChecklistAdminServiceStub without session cookie");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void removeAspectSuccessCase(String path, String aspect) {
        try {
            customLifecyclesChecklistAdminServiceStub.removeAspect(path, aspect);
        } catch (RemoteException e) {
            throw new RuntimeException("Exception occurred while removing life-cycle to the resource : " + e);
        } catch (ExceptionException e) {
            throw new RuntimeException("Exception occurred while removing life-cycle to the resource : " + e);
        }
    }

    public void removeLifecycleFailureCase(String path, String aspect) {
        try {
            customLifecyclesChecklistAdminServiceStub.removeAspect(path, aspect);
            Assert.fail("Lifecycle can remove using CustomLifecyclesChecklistAdminServiceStub without session cookie");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void InvokeAspectSuccessCase(String path, String aspect, String action, String[] items) {
        try {
            customLifecyclesChecklistAdminServiceStub.invokeAspect(path, aspect, action, items);
        } catch (RemoteException e) {
            throw new RuntimeException("Exception occurred while invoking life-cycle aspect in resource : " + e);
        } catch (ExceptionException e) {
            throw new RuntimeException("Exception occurred while invoking life-cycle aspect in resource : " + e);
        }
    }

    public void InvokeAspectFailureCase(String path, String aspect, String action, String[] items) {
        try {
            customLifecyclesChecklistAdminServiceStub.invokeAspect(path, aspect, action, items);
            Assert.fail("Lifecycle can invoke using CustomLifecyclesChecklistAdminServiceStub without session cookie");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public LifecycleBean getLifecycleBeanSuccessCase(String path) {
        LifecycleBean lifecycleBean;
        try {
            lifecycleBean = customLifecyclesChecklistAdminServiceStub.getLifecycleBean(path);
        } catch (RemoteException e) {
            throw new RuntimeException("Exception occurred while getting life-cycle bean in resource : " + e);
        } catch (ExceptionException e) {
            throw new RuntimeException("Exception occurred while getting life-cycle bean in resource : " + e);
        }
        return lifecycleBean;
    }

    public void getLifecycleBeanFailureCase(String path) {
        try {
            customLifecyclesChecklistAdminServiceStub.getLifecycleBean(path);
            Assert.fail("Lifecycle-bean can invoke using CustomLifecyclesChecklistAdminServiceStub without session cookie");
        } catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        } catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

}
