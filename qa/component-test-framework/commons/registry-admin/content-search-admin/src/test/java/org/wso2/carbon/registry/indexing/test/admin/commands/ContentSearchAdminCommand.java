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

package org.wso2.carbon.registry.indexing.test.admin.commands;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.indexing.ui.generated.ContentSearchAdminServiceCallbackHandler;
import org.wso2.carbon.registry.indexing.ui.generated.ContentSearchAdminServiceStub;
import org.wso2.carbon.registry.indexing.ui.generated.xsd.SearchResultsBean;

import java.rmi.RemoteException;

/**
 * calling methods in ContentSearchAdminService using the returned stub
 */
public class ContentSearchAdminCommand extends TestCase {
    private static final Log log = LogFactory.getLog(ContentSearchAdminCommand.class);
    ContentSearchAdminServiceStub contentSearchAdminServiceStub;

    public ContentSearchAdminCommand(
            ContentSearchAdminServiceStub contentSearchAdminServiceStub) {
        this.contentSearchAdminServiceStub = contentSearchAdminServiceStub;
        log.debug("contentSearchAdminServiceStub added");
    }

    public SearchResultsBean getContentSearchSuccessCase(String searchQuery) throws Exception {

        SearchResultsBean searchResultsBean = contentSearchAdminServiceStub.getContentSearchResults(searchQuery);

        try {
            contentSearchAdminServiceStub.getContentSearchResults(searchQuery);

        }
        catch (Exception e) {
            Assert.fail("Error occurred: " + e);
            log.error("Error occurred: " + e);
        }

        return searchResultsBean;
    }

    public SearchResultsBean getContentSearchFailureCase(String searchQuery)
            throws RemoteException {
        SearchResultsBean searchResultsBean = contentSearchAdminServiceStub.getContentSearchResults(searchQuery);
        try {
            contentSearchAdminServiceStub.getContentSearchResults(searchQuery);
            log.error("Getting search result without session cookie");
            Assert.fail("Getting search result without session cookie");
        }
        catch (Exception e) {
            Assert.fail("Error occurred: " + e);
            log.error("Error occurred: " + e);
        }
        return searchResultsBean;
    }

    public SearchResultsBean startGetContentSearchResultsSuccessCase(String searchQuery,
                                                                     ContentSearchAdminServiceCallbackHandler callback)
            throws RemoteException {
        SearchResultsBean searchResultsBean = contentSearchAdminServiceStub.getContentSearchResults(searchQuery);

        try {
            contentSearchAdminServiceStub.startgetContentSearchResults(searchQuery, callback);
            log.error("Getting search result without session cookie");
            Assert.fail("Getting search result without session cookie");
        }
        catch (Exception e) {
            Assert.fail("Error occurred: " + e);
            log.error("Error occurred: " + e);

        }
        return searchResultsBean;
    }

    public SearchResultsBean startGetContentSearchResultsFailureCase(String searchQuery,

                                                                     ContentSearchAdminServiceCallbackHandler callback)
            throws RemoteException {
        SearchResultsBean searchResultsBean = contentSearchAdminServiceStub.getContentSearchResults(searchQuery);
        try {
            contentSearchAdminServiceStub.startgetContentSearchResults(searchQuery, callback);
            log.error("Getting search result without session cookie");
            Assert.fail("Getting search result without session cookie");
        }
        catch (Exception e) {
            Assert.fail("Error occurred: " + e);
            log.error("Error occurred: " + e);

        }
        return searchResultsBean;
    }


}
