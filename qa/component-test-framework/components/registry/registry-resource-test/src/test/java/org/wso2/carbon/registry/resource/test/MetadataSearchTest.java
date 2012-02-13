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
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;
import org.wso2.carbon.registry.search.test.admin.commands.InitializeSearchAdminCommand;
import org.wso2.carbon.registry.search.test.admin.commands.SearchAdminCommand;
import org.wso2.carbon.registry.search.ui.SearchAdminServiceStub;
import org.wso2.carbon.registry.search.ui.beans.xsd.AdvancedSearchResultsBean;
import org.wso2.carbon.registry.search.ui.common.xsd.ResourceData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MetadataSearchTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(MetadataSearchTest.class);

    private SearchAdminCommand searchAdminCommand = null;
    private ResourceAdminCommand resourceAdminCommand = null;
    private String wsdlPath = "/_system/governance/wsdls/http/footballpool/dataaccess/eu/";
    private String resourceName = "sample.wsdl";


    @Override
    public void init() {
        log.info("Initializing Tests for Meta-data Search");
        log.debug("Meta-data Search Test Initialised");

    }

    @Override
    public void runSuccessCase() {

        log.debug("Running SuccessCase");
        SearchAdminServiceStub searchAdminServiceStub = new InitializeSearchAdminCommand().executeAdminStub(sessionCookie);
        searchAdminCommand = new SearchAdminCommand(searchAdminServiceStub);

        ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
        resourceAdminCommand = new ResourceAdminCommand(resourceAdminServiceStub);

        addResource();
        searchMetadata();
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

    private void searchMetadata() {
        AdvancedSearchResultsBean bean = null;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {

            // searchAdminCommand.getSearchResultsSuccessCase("sample.wsdl", "admin", dateFormat.format(calendar.getTime()), dateFormat.format(calendar.getTime()));
            //advanced search
            bean = searchAdminCommand.getAdvancedSearchResultsSuccessCase("sample.wsdl", "admin", "", "", "", "", "", "", "", "", "", "ArrayOftCountrySelectedTopScorer");

            if (bean.getResourceDataList() != null) {
                bean.setResourceDataList(new ResourceData[0]);

            } else {
                Assert.fail("Failed to get search results from the search service");
                log.error("Failed to get search results from the search service");
            }

        } catch (Exception e) {
            Assert.fail("Failed to get search results from the search service: " + e);
            log.error("Failed to get search results from the search service: " + e);

        }


    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }
}
