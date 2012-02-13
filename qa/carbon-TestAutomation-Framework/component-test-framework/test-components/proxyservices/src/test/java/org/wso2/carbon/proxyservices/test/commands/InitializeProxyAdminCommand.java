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


package org.wso2.carbon.proxyservices.test.commands;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.proxyadmin.ui.ProxyServiceAdminStub;

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.HOST_NAME;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.HTTPS_PORT;

public class InitializeProxyAdminCommand {

    private static final Log log = LogFactory.getLog(InitializeProxyAdminCommand.class);

    public ProxyServiceAdminStub executeAdminStub(String sessionCookie) {
        log.debug("sessionCookie:" + sessionCookie);
        FrameworkSettings.getProperty();
        String serviceURL = null;
        if(FrameworkSettings.STRATOS.equalsIgnoreCase("false")){
            serviceURL = "https://" + HOST_NAME + ":" + HTTPS_PORT + "/services/ProxyServiceAdmin";
        }
        else if(FrameworkSettings.STRATOS.equalsIgnoreCase("true")){
             serviceURL = "https://" + HOST_NAME + "/services/ProxyServiceAdmin";
        }

        ProxyServiceAdminStub proxyServiceAdminStub = null;
        try {
            proxyServiceAdminStub = new ProxyServiceAdminStub(serviceURL);

            ServiceClient client = proxyServiceAdminStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
            proxyServiceAdminStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(600000);
        }
        catch (AxisFault axisFault) {
            log.error("AxisFault in InitializeProxyAdminCommand class : " + axisFault.toString());
            Assert.fail("Unexpected exception thrown");
            axisFault.printStackTrace();
        }
        log.info("InitializeProxyAdminCommand : ProxyAdminStub created with session " + sessionCookie);
        return proxyServiceAdminStub;

    }


}




