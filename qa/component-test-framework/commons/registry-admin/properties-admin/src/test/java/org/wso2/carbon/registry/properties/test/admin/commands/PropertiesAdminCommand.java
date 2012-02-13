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
package org.wso2.carbon.registry.properties.test.admin.commands;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.registry.properties.ui.PropertiesAdminServiceStub;
import org.wso2.carbon.registry.properties.ui.beans.xsd.PropertiesBean;

/**
 * calling methods in PropertiesAdminService using the returned stub
 */
public class PropertiesAdminCommand extends TestCase {

    private static final Log log = LogFactory.getLog(PropertiesAdminCommand.class);
    PropertiesAdminServiceStub propertiesAdminServiceStub;

    public PropertiesAdminCommand(PropertiesAdminServiceStub propertiesAdminServiceStub) {
        this.propertiesAdminServiceStub = propertiesAdminServiceStub;
        log.debug("propertiesAdminServiceStub added");
    }

    public PropertiesBean getPropertiesSuccessCase(String path, String viewProps) throws Exception {
        PropertiesBean propertiesBean = propertiesAdminServiceStub.getProperties(path, viewProps);
        return propertiesBean;
    }

    public void getPropertiesFailureCase(String path, String viewProps) {
        try {
            propertiesAdminServiceStub.getProperties(path, viewProps);
            log.error("Property added without session cookie");
            Assert.fail("Property added without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public void removePropertiesSuccessCase(String path, String name) throws Exception {
        propertiesAdminServiceStub.removeProperty(path, name);
    }

    public void removePropertyFailureCase(String path, String name) {
        try {
            propertiesAdminServiceStub.removeProperty(path, name);
            log.error("Property removed without session cookie");
            Assert.fail("Property removed without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public void setPropertySuccessCase(String path, String name, String value) throws Exception {
        propertiesAdminServiceStub.setProperty(path, name, value);
    }

    public void setPropertyFailureCase(String path, String name, String value) {
        try {
            propertiesAdminServiceStub.setProperty(path, name, value);
            log.error("Setting property without session cookie");
            Assert.fail("Setting property without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public void updatePropertySuccessCase(String path, String name, String value, String oldName)
            throws Exception {
        propertiesAdminServiceStub.updateProperty(path, name, value, oldName);
    }

    public void updatePropertyFailureCase(String path, String name, String value, String oldName) {
        try {
            propertiesAdminServiceStub.updateProperty(path, name, value, oldName);
            log.error("Property updated without session cookie");
            Assert.fail("Property updated without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }
}
