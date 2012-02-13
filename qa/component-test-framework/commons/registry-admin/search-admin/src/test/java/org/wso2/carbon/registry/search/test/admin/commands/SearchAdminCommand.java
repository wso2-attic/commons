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
package org.wso2.carbon.registry.search.test.admin.commands;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.registry.search.ui.SearchAdminServiceStub;
import org.wso2.carbon.registry.search.ui.beans.xsd.AdvancedSearchResultsBean;
import org.wso2.carbon.registry.search.ui.beans.xsd.SearchResultsBean;

/**
 * giving the access to operations in SearchAdminService
 */
public class SearchAdminCommand extends TestCase {
    private static final Log log = LogFactory.getLog(SearchAdminCommand.class);
    SearchAdminServiceStub searchAdminServiceStub;

    public SearchAdminCommand(SearchAdminServiceStub searchAdminServiceStub) {
        this.searchAdminServiceStub = searchAdminServiceStub;
        log.debug("searchAdminServiceStub added");
    }

    public AdvancedSearchResultsBean getAdvancedSearchResultsSuccessCase(String resourceName,
                                                                         String authorName,
                                                                         String updaterName,
                                                                         String createdAfter,
                                                                         String createdBefore,
                                                                         String updatedAfter,
                                                                         String updatedBefore,
                                                                         String tags,
                                                                         String commentWords,
                                                                         String propertyName,
                                                                         String propertyValue,
                                                                         String content)
            throws Exception {
        AdvancedSearchResultsBean advancedSearchResultsBean = searchAdminServiceStub.getAdvancedSearchResults(resourceName, authorName, updaterName, createdAfter, createdBefore, updatedAfter, updatedBefore, tags, commentWords, propertyName, propertyValue, content);
        return advancedSearchResultsBean;
    }

    public void getAdvancedSearchResultsFailureCase(String resourceName,
                                                    String authorName,
                                                    String updaterName,
                                                    String createdAfter,
                                                    String createdBefore,
                                                    String updatedAfter,
                                                    String updatedBefore,
                                                    String tags,
                                                    String commentWords,
                                                    String propertyName,
                                                    String propertyValue,
                                                    String content) {
        try {
            searchAdminServiceStub.getAdvancedSearchResults(resourceName, authorName, updaterName, createdAfter, createdBefore, updatedAfter, updatedBefore, tags, commentWords, propertyName, propertyValue, content);
            log.error("Getting advance search result without session cookie");
            Assert.fail("Getting advance search result without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public SearchResultsBean getSearchResultsSuccessCase(String type, String criteria)
            throws Exception {
        SearchResultsBean searchResultsBean = searchAdminServiceStub.getSearchResults(type, criteria);
        return searchResultsBean;
    }

    public void getSearchResultsFailureCase(String type, String criteria) {
        try {
            searchAdminServiceStub.getSearchResults(type, criteria);
            log.error("Getting search result without session cookie");
            Assert.fail("Getting search result without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }
}
