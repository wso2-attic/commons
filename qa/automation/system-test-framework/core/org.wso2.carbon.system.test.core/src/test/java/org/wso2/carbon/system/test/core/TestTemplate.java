/*
* Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.system.test.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;

import java.net.MalformedURLException;


public abstract class TestTemplate extends SystemIntegrationTestCase {

    private ArtifactDeployer artifactDeployer = new ArtifactDeployer();
    private ArtifactCleaner artifactCleaner = new ArtifactCleaner();
    protected String testClassName;

    private static final Log log = LogFactory.getLog(TestTemplate.class);

    // The template method
    public void testTemplate() throws Exception {

        setFrameworkProperties();

        setKeyStore();

        init();

        artifactDeployment();

        runSuccessCase();

        artifactCleanup();

        cleanup();
    }

    private void setFrameworkProperties() {
        FrameworkSettings.getFrameworkProperties();
    }

    private void setKeyStore() {
        System.setProperty("javax.net.ssl.trustStore", FrameworkSettings.getKeyStoreLocation());
        System.setProperty("javax.net.ssl.trustStorePassword", FrameworkSettings.getKeyStorePassword());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
    }

    public abstract void init();

    public abstract void runSuccessCase();

    public abstract void cleanup();


    public void artifactDeployment() {
        artifactDeployer.artifactDeployment(testClassName);
    }

    public void artifactCleanup() throws InterruptedException, MalformedURLException {
        artifactCleaner.artifactClearner();
    }

}
