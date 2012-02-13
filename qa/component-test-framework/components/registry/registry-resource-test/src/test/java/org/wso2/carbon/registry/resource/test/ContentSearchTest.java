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

package org.wso2.carbon.registry.resource.test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.registry.indexing.test.admin.commands.ContentSearchAdminCommand;
import org.wso2.carbon.registry.indexing.test.admin.commands.InitializeContentSearchAdminCommand;
import org.wso2.carbon.registry.indexing.ui.generated.ContentSearchAdminServiceStub;
import org.wso2.carbon.registry.indexing.ui.generated.xsd.ResourceData;
import org.wso2.carbon.registry.indexing.ui.generated.xsd.SearchResultsBean;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;

import java.io.File;

public class ContentSearchTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(ContentSearchTest.class);
    private ResourceAdminCommand resourceAdminCommand = null;
    private ContentSearchAdminCommand contentSearchAdminCommand = null;
    private String wsdlPath = "/_system/governance/wsdls/http/footballpool/dataaccess/eu/";
    private String resourceName = "sample.wsdl";

    @Override
    public void init() {
        log.info("Initializing Tests for Community Feature in Registry Policy");
        log.debug("Community Feature in Registry Policy Test Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase");
        ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
        ContentSearchAdminServiceStub contentSearchAdminServiceStub = new InitializeContentSearchAdminCommand().executeAdminStub(sessionCookie);
        resourceAdminCommand = new ResourceAdminCommand(resourceAdminServiceStub);
        contentSearchAdminCommand = new ContentSearchAdminCommand(contentSearchAdminServiceStub);

        addResource();
        searchContent();
    }

    private void searchContent() {
        SearchResultsBean bean = null;

        try {
            bean = contentSearchAdminCommand.getContentSearchSuccessCase("ArrayOftCountrySelectedTopScorer");
            if (bean.getResourceDataList() != null) {
                bean.setResourceDataList(new ResourceData[0]);

            } else {

                Assert.fail("Content search failed");
                log.error("Content search failed");
            }
            resourceAdminCommand.deleteResourceSuccessCase(wsdlPath + resourceName);

        } catch (Exception e) {
            Assert.fail("Content search failed: " + e);
            log.error("Content search failed: " + e.getMessage());
            String msg = "Failed to get search results from the search service. " +
                    e.getMessage();
            log.error(msg, e);
        }
    }

    private void addResource() {

        try {
            String resource = frameworkPath + File.separator + "components" + File.separator + "registry" +
                    File.separator + "registry-resource-test" + File.separator + "src" + File.separator +
                    "test" + File.separator + "java" + File.separator + "resources" + File.separator + resourceName;

            resourceAdminCommand.addResourceSuccessCase(wsdlPath + resourceName,
                    "application/wsdl+xml", "test resource", "file:///" + resource, null);

        } catch (Exception e) {
            Assert.fail("Unable to get file content: " + e);
            log.error("Unable to get file content: " + e.getMessage());
        }
    }


    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {

    }

}
