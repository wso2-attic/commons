/**
 *  Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.mediation.configadmin.test.commands;

import junit.framework.TestCase;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.common.test.utils.ConfigHelper;
import org.wso2.carbon.mediation.configadmin.ui.ConfigAdminException;
import org.wso2.carbon.mediation.configadmin.ui.ConfigServiceAdminStub;

import java.io.File;
import java.rmi.RemoteException;

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.*;

/*
*ConfigServiceAdmin methods will be called using its returned stub
*/
public class ConfigServiceAdminStubCommand extends TestCase {
    private static final Log log = LogFactory.getLog(ConfigServiceAdminStub.class);

    protected String sessionCookie = null;
    protected String frameworkPath;
    ConfigServiceAdminStub configServiceAdminStub;

    public ConfigServiceAdminStubCommand(ConfigServiceAdminStub configServiceAdminStub) {
        this.configServiceAdminStub = configServiceAdminStub;

    }

    public ConfigServiceAdminStubCommand() {

    }

    public ConfigServiceAdminStub initConfigServiceAdminStub(String sessionCookie) {
        log.debug("sessionCookie:" + sessionCookie);
        FrameworkSettings.getProperty();
        String serviceURL = SERVICE_URL + "ConfigServiceAdmin";

        ConfigServiceAdminStub configServiceAdminStub = null;
        try {
            configServiceAdminStub = new ConfigServiceAdminStub(serviceURL);

            ServiceClient client = configServiceAdminStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
        } catch (AxisFault axisFault) {
            fail("Unexpected exception thrown: " + axisFault);
        }
        log.info("endpointAdminStub created");
        return configServiceAdminStub;

    }


    public void updateConfigurationExecuteSuccessCase(OMElement omElement) throws Exception {
        log.debug("Update synapse configuration");
        String fileBuffer = omElement.toString();
        if (STRATOS.equals("true")) {
            fileBuffer = fileBuffer.replaceAll("localhost:9000/services", BACKENDSERVER_HOST_NAME + "/services/" + TENANT_NAME); //http://appserver.cloud.private.wso2.com/services/t/testautomation.com
            //fileBuffer = fileBuffer.replaceAll(":9000","/services/"+TENANT_NAME);                  //http://localhost:9000/services/SimpleStockQuoteService
        } else {
            fileBuffer = fileBuffer.replaceAll("localhost", BACKENDSERVER_HOST_NAME);
            fileBuffer = fileBuffer.replaceAll("9000", BACKENDSERVER_HTTP_PORT);
        }
        omElement = AXIOMUtil.stringToOM(fileBuffer);

        boolean configurationUpdated = false;
        configServiceAdminStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(600000);
        try {
            configurationUpdated = (configServiceAdminStub.updateConfiguration(omElement));
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e);
        }
        assertTrue(configurationUpdated);
    }

    public void updateConfigurationExecuteFailureCase(OMElement omElement) {
        log.debug("DeleteEndPointCommand executeFailureCase");
        boolean epAdded = false;
        try {
            epAdded = configServiceAdminStub.updateConfiguration(omElement);
            fail("Expected exception did not occur");
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("Remote exception thrown");
        } catch (ConfigAdminException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        assertFalse(epAdded);
        log.info("DeleteEndPointCommand FailureCase passed");

    }

    public void loadDefaultConfig() {
        log.debug("loading default synapse configuration to the esb");

        try {
            FrameworkSettings.getProperty();
            String relativePath = null;
            File filePath = new File("./");
            relativePath = filePath.getCanonicalPath();
            File findFile = new File(relativePath + File.separator + "config" + File.separator + "framework.properties");
            if (!findFile.isFile()) {
                filePath = new File("./../../");
                relativePath = filePath.getCanonicalPath();
            }
            frameworkPath = relativePath;
            OMElement omElement = ConfigHelper.createOMElement(frameworkPath + File.separator + "commons" + File.separator + "synapse-config-admin"
                                                               + File.separator + "src" + File.separator + "test" + File.separator + "resources"
                                                               + File.separator + "default.xml");

            new ConfigServiceAdminStubCommand(configServiceAdminStub).
                    updateConfigurationExecuteSuccessCase(omElement);

        } catch (Exception e) {
            log.debug("unable to load default configuration to the esb");
            log.error("error occurred while resetting the synapse config " + e);
        }
    }


}
