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

package org.wso2.startos.system.test;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceTenantMgtServiceAdmin;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.tenant.mgt.stub.ActivateTenantExceptionException;
import org.wso2.carbon.tenant.mgt.stub.AddTenantExceptionException;
import org.wso2.carbon.tenant.mgt.stub.GetTenantExceptionException;
import org.wso2.startos.system.test.stratosUtils.ServiceLoginClient;

import java.rmi.RemoteException;

public class StratosManagerServiceTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(StratosManagerServiceTest.class);

    @Override
    public void init() {
        testClassName = StratosManagerServiceTest.class.getName();
    }

    @Override
    public void runSuccessCase() {
        String managerServerHostName = FrameworkSettings.MANAGER_SERVER_HOST_NAME;
        String sessionCookie = ServiceLoginClient.loginChecker(managerServerHostName);
    }

    @Override
    public void cleanup() {
    }


}
