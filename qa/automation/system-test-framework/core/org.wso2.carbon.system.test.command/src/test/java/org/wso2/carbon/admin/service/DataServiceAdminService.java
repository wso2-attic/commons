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
package org.wso2.carbon.admin.service;

import junit.framework.Assert;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.dataservices.ui.stub.DataServiceAdminStub;

import java.rmi.RemoteException;


public class DataServiceAdminService {
    private static final Log log = LogFactory.getLog(DataServiceAdminService.class);

    private final String serviceName = "DataServiceAdmin";
    private DataServiceAdminStub dataServiceAdminStub;
    private String endPoint;

    public DataServiceAdminService(String backEndUrl) {
        this.endPoint = backEndUrl + serviceName;
        log.debug("Endpoint : " + endPoint);
        try {
            dataServiceAdminStub = new DataServiceAdminStub(endPoint);
        } catch (AxisFault axisFault) {
            log.error("Initializing DataServiceAdminStub failed : ", axisFault);
            Assert.fail("Initializing DataServiceAdminStub failed : " + axisFault.getMessage());
        }
    }

    public String[] getCarbonDataSources(String sessionCookie) {
        new AuthenticateStub().authenticateStub(sessionCookie, dataServiceAdminStub);
        String[] dataSourceList = null;
        try {
            dataSourceList = dataServiceAdminStub.getCarbonDataSourceNames();
        } catch (RemoteException e) {
            log.error("Remote Exception when listing data sources :", e);
            Assert.fail("Remote Exception when listing data sources : " + e.getMessage());
        }
        return dataSourceList;
    }
}
