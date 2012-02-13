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
package org.wso2.carbon.governance.lcm.test.admin.commands;

import junit.framework.TestCase;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.governance.lcm.ui.ExceptionException;
import org.wso2.carbon.governance.lcm.ui.LifeCycleManagementServiceStub;
import org.wso2.carbon.governance.lcm.ui.beans.xsd.LifecycleBean;

import java.rmi.RemoteException;

/**
 * calling methods in LifeCycleManagementService using the returned stub
 */
public class LifecyclesAdminCommand extends TestCase {

    private static final Log log = LogFactory.getLog(LifecyclesAdminCommand.class);
    LifeCycleManagementServiceStub lifeCycleManagementServiceStub;

    public LifecyclesAdminCommand(LifeCycleManagementServiceStub lifeCycleManagementServiceStub) {
        this.lifeCycleManagementServiceStub = lifeCycleManagementServiceStub;
        log.debug("lifecyclesAdminServiceStub added");
    }

    public void createLifecycleSuccessCase(LifecycleBean lifecycleBean) throws ExceptionException {
        try {
            lifeCycleManagementServiceStub.createLifecycle(lifecycleBean);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            log.error("Exception thrown while creating lifecycle : " + e.getMessage());
            Assert.fail("Exception thrown while creating lifecycle : " + e.getMessage());
        }
        catch (Exception e) {
            log.error("Exception thrown while creating lifecycle : " + e.getMessage());
            Assert.fail("Exception thrown while creating lifecycle : " + e.getMessage());
        }
    }

    public void createLifecycleFailureCase(LifecycleBean lifecycleBean) {
        try {
            lifeCycleManagementServiceStub.createLifecycle(lifecycleBean);
            log.error("Lifecycle created without session cookie");
            Assert.fail("Lifecycle created without session cookie");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void deleteLifecycleSuccessCase(String lcName) throws ExceptionException {
        try {
            lifeCycleManagementServiceStub.deleteLifecycle(lcName);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            log.error("Exception thrown while deleting lifecycle : " + e.getMessage());
            Assert.fail("Exception thrown while deleting lifecycle : " + e.getMessage());
        }
        catch (Exception e) {
            log.error("Exception thrown while deleting lifecycle : " + e.getMessage());
            Assert.fail("Exception thrown while deleting lifecycle : " + e.getMessage());
        }
    }

    public void deleteLifecycleFailureCase(String lcName) {
        try {
            lifeCycleManagementServiceStub.deleteLifecycle(lcName);
            log.error("Lifecycle deleted without session cookie");
            Assert.fail("Lifecycle deleted without session cookie");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            junit.framework.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public LifecycleBean getLifecycleBeanSuccessCase(String lcName) throws ExceptionException {
        LifecycleBean lifecycleBean = new LifecycleBean();
        try {

            lifecycleBean = lifeCycleManagementServiceStub.getLifecycleBean(lcName);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            log.error("Exception thrown while getting lifecycle bean : " + e.getMessage());
            Assert.fail("Exception thrown while getting lifecycle bean : " + e.getMessage());
        }
        catch (Exception e) {
            log.error("Exception thrown while getting lifecycle bean : " + e.getMessage());
            Assert.fail("Exception thrown while getting lifecycle bean : " + e.getMessage());
        }
        return lifecycleBean;
    }

    public void getLifecycleBeanFailureCase(String lcName) {
        LifecycleBean lifecycleBean = new LifecycleBean();
        try {
            lifecycleBean = lifeCycleManagementServiceStub.getLifecycleBean(lcName);
            log.error("getting lifecycle bean without session cookie");
            Assert.fail("getting lifecycle bean without session cookie");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public String getLifecycleConfigSuccessCase(String lcName) throws ExceptionException {
        String lcConfig = null;
        try {

            lcConfig = lifeCycleManagementServiceStub.getLifecycleConfiguration(lcName);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            log.error("Exception thrown while getting lifecycle configurations : " + e.getMessage());
            Assert.fail("Exception thrown while getting lifecycle configurations : " + e.getMessage());
        }
        catch (Exception e) {
            log.error("Exception thrown while getting lifecycle configurations : " + e.getMessage());
            Assert.fail("Exception thrown while getting lifecycle configurations : " + e.getMessage());
        }
        return lcConfig;
    }

    public void getLifecycleConfigFailureCase(String lcName) {
        String lcConfig = null;
        try {
            lcConfig = lifeCycleManagementServiceStub.getLifecycleConfiguration(lcName);
            if (!lcConfig.equals(null)) {
                log.error("getting lifecycle configuration without session cookie");
                Assert.fail("getting lifecycle configuration without session cookie");
            }
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public String[] getLifecycleListSuccessCase() throws ExceptionException {
        String[] lcList = null;
        try {

            lcList = lifeCycleManagementServiceStub.getLifecycleList();
        }
        catch (RemoteException e) {
            e.printStackTrace();
            log.error("Exception thrown while getting lifecycle list : " + e.getMessage());
            Assert.fail("Exception thrown while getting lifecycle list : " + e.getMessage());
        }
        catch (Exception e) {
            log.error("Exception thrown while getting lifecycle list : " + e.getMessage());
            Assert.fail("Exception thrown while getting lifecycle list : " + e.getMessage());
        }
        return lcList;
    }

    public void getLifecycleListFailureCase(String lcName) {
        String[] lcList = null;
        try {
            lcList = lifeCycleManagementServiceStub.getLifecycleList();
            if (!lcList.equals(null)) {
                log.error("getting lifecycle list without session cookie");
                Assert.fail("getting lifecycle list without session cookie");
            }
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public String getLifecycleCollecitonLocationSuccessCase() throws ExceptionException {
        String lcCollectionLocation = null;
        try {

            lcCollectionLocation = lifeCycleManagementServiceStub.getLifecyclesCollectionLocation();
        }
        catch (RemoteException e) {
            e.printStackTrace();
            log.error("Exception thrown while getting lifecycle collection location : " + e.getMessage());
            Assert.fail("Exception thrown while getting lifecycle collection location : " + e.getMessage());
        }
        catch (Exception e) {
            log.error("Exception thrown while getting lifecycle collection location : " + e.getMessage());
            Assert.fail("Exception thrown while getting lifecycle collection location : " + e.getMessage());
        }
        return lcCollectionLocation;
    }

    public void getLifecycleCollecitonLocationFailureCase(String lcName) {
        String lcCollectionLocation = null;
        try {
            lcCollectionLocation = lifeCycleManagementServiceStub.getLifecyclesCollectionLocation();
            if (!lcCollectionLocation.equals(null)) {
                log.error("getting lifecycle collection location without session cookie");
                Assert.fail("getting lifecycle collection location without session cookie");
            }
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public boolean isLifecycleNameInUseSuccessCase(String lcName) throws ExceptionException {
        boolean isUsed = false;
        try {

            isUsed = lifeCycleManagementServiceStub.isLifecycleNameInUse(lcName);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            log.error("Exception thrown while checking lifecycle name usage : " + e.getMessage());
            Assert.fail("Exception thrown while checking lifecycle name usage : " + e.getMessage());
        }
        catch (Exception e) {
            log.error("Exception thrown while checking lifecycle name usage : " + e.getMessage());
            Assert.fail("Exception thrown while checking lifecycle name usage : " + e.getMessage());
        }
        return isUsed;
    }

    public void isLifecycleNameInUseFailureCase(String lcName) {
        boolean isUsed = false;
        try {
            isUsed = lifeCycleManagementServiceStub.isLifecycleNameInUse(lcName);
            if (isUsed == true) {
                log.error("checking lifecycle name usage without session cookie");
                Assert.fail("checking lifecycle name usage without session cookie");
            }
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public LifecycleBean parseConfigurationSuccessCase(String configuration)
            throws ExceptionException {
        LifecycleBean lifecycleBean = new LifecycleBean();
        try {

            lifecycleBean = lifeCycleManagementServiceStub.parseConfiguration(configuration);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            log.error("Exception thrown while parsing configurations : " + e.getMessage());
            Assert.fail("Exception thrown while parsing configurations : " + e.getMessage());
        }
        catch (Exception e) {
            log.error("Exception thrown while parsing configurations : " + e.getMessage());
            Assert.fail("Exception thrown while parsing configurations : " + e.getMessage());
        }
        return lifecycleBean;
    }

    public void parseConfigurationFailureCase(String configuration) {
        LifecycleBean lifecycleBean = new LifecycleBean();
        try {
            lifecycleBean = lifeCycleManagementServiceStub.parseConfiguration(configuration);
            if (!lifecycleBean.equals(null)) {
                log.error("parsing configurations without session cookie");
                Assert.fail("parsing configurations without session cookie");
            }
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void setLifecycleCollecitonLocationSuccessCase(String location)
            throws ExceptionException {
        try {
            lifeCycleManagementServiceStub.setLifecyclesCollectionLocation(location);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            log.error("Exception thrown while setting lifecycle collection location : " + e.getMessage());
            Assert.fail("Exception thrown while setting lifecycle collection location : " + e.getMessage());
        }
        catch (Exception e) {
            log.error("Exception thrown while setting lifecycle collection location : " + e.getMessage());
            Assert.fail("Exception thrown while setting lifecycle collection location : " + e.getMessage());
        }
    }

    public void setLifecycleCollecitonLocationFailureCase(String location) {
        try {
            lifeCycleManagementServiceStub.setLifecyclesCollectionLocation(location);
            log.error("setting lifecycle collection location without session cookie");
            Assert.fail("setting lifecycle collection location without session cookie");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void updateLifeCycleSuccessCase(String oldName, LifecycleBean lifecycleBean)
            throws ExceptionException {
        try {
            lifeCycleManagementServiceStub.updateLifecycle(oldName, lifecycleBean);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            log.error("Exception thrown while updating lifecycle : " + e.getMessage());
            Assert.fail("Exception thrown while updating lifecycle : " + e.getMessage());
        }
        catch (Exception e) {
            log.error("Exception thrown while updating lifecycle : " + e.getMessage());
            Assert.fail("Exception thrown while updating lifecycle : " + e.getMessage());
        }
    }

    public void updateLifeCycleFailureCase(String oldName, LifecycleBean lifecycleBean) {
        try {
            lifeCycleManagementServiceStub.updateLifecycle(oldName, lifecycleBean);
            log.error("updating lifecycle without session cookie");
            Assert.fail("updating lifecycle without session cookie");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                junit.framework.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }
}
