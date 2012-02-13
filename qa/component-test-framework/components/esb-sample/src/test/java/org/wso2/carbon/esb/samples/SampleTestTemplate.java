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

package org.wso2.carbon.esb.samples;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.common.test.utils.ConfigHelper;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.mediation.configadmin.test.commands.ConfigServiceAdminStubCommand;
import org.wso2.carbon.mediation.configadmin.ui.ConfigServiceAdminStub;

import java.io.File;

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.CARBON_HOME;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.TEST_FRAMEWORK_HOME;

public abstract class SampleTestTemplate extends TestTemplate {
    private static final Log log = LogFactory.getLog(SampleTestTemplate.class);

    int sampleNo;
    OMElement configOMElement;
    OMElement defaultConfigOMElement;
    String clientLog = "";

    @Override
    public void init() {
        FrameworkSettings.getProperty();
        setSampleNo();

        System.out.println("Running Sample" + sampleNo);
        log.info("Running Sample" + sampleNo);
        configOMElement = ConfigHelper.createOMElement(CARBON_HOME +
                                                       File.separator + "repository" + File.separator + "samples" +
                                                       File.separator + "synapse_sample_" + sampleNo + ".xml");

        defaultConfigOMElement =
                ConfigHelper.createOMElement(TEST_FRAMEWORK_HOME + File.separator + "components" + File.separator +
                                             "esb-sample" + File.separator + "src" + File.separator + "test" +
                                             File.separator + "resources" + File.separator + "defaultConfig.xml");

        sessionCookie = login();
        ConfigServiceAdminStub configServiceAdminStub = new
                ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);
        try {
            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(defaultConfigOMElement);
        } catch (Exception e) {
            log.error("error while reset the synapse config to default");
        }
        logout();
        sessionCookie = null;

    }

    @Override
    public void runSuccessCase() {
        ConfigServiceAdminStub configServiceAdminStub = new
                ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);
        try {
            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(configOMElement);
        } catch (Exception e) {
            log.error("error while uploading the test synapse config");
        }

        invokeClient();
        runSuccessTest();
        clientLog = "";
        try {
            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(defaultConfigOMElement);
        } catch (Exception e) {
            log.error("error while reset the synapse config to default");
        }


    }


    @Override
    public void runFailureCase() {
       /* try {
            ConfigServiceAdminStub configServiceAdminStub = new
                    ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);
            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteFailureCase(configOMElement);

            invokeClient();
            runFailureTest();
            clientLog = "";
            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteFailureCase(defaultConfigOMElement);

        }
        catch (Exception e) {
            log.info("Failure case exception" + e.getMessage());
        }
*/
    }


    @Override
    public void cleanup() {
        ConfigServiceAdminStub configServiceAdminStub = new
                ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);
        new ConfigServiceAdminStubCommand(configServiceAdminStub).loadDefaultConfig();
    }

    // set sampleNo

    abstract void setSampleNo();

    //to invoke the test client

    protected abstract void invokeClient();

    // to check whether the test has passed

    protected abstract void runSuccessTest();

    // to check whether the test has failed

    protected abstract void runFailureTest();

    protected boolean isWindows() {
        try {
            String osName = System.getProperty("os.name");
            return (osName != null && osName.startsWith("Windows"));
        } catch (Exception e) {
            System.out.println("Exception caught =" + e.getMessage());
        }
        return false;
    }


}
