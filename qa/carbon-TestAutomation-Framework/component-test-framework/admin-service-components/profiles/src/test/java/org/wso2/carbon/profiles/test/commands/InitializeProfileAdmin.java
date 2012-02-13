/*                                                                             
 * Copyright 2004,2005 The Apache Software Foundation.                         
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
package org.wso2.carbon.profiles.test.commands;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.registry.profiles.ui.ProfilesAdminServiceStub;

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.HOST_NAME;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.HTTPS_PORT;


/**
 * ProfileAdmin Service Initialization
 */
public class InitializeProfileAdmin {
    private static final Log log = LogFactory.getLog(InitializeProfileAdmin.class);

    public ProfilesAdminServiceStub executeAdminStub(String sessionCookie) {
        log.debug("sessionCookie:" + sessionCookie);
        FrameworkSettings.getProperty();
        String serviceURL = null;
        if (FrameworkSettings.STRATOS.equalsIgnoreCase("false")) {
            serviceURL = "https://" + HOST_NAME + ":" + HTTPS_PORT + "/services/ProfilesAdminService";
        } else if (FrameworkSettings.STRATOS.equalsIgnoreCase("true")) {
            serviceURL = "https://" + HOST_NAME + "/services/ProfilesAdminService";
        }

        ProfilesAdminServiceStub profilesAdminServiceStub = null;

        try {
            profilesAdminServiceStub = new ProfilesAdminServiceStub(serviceURL);

            ServiceClient client = profilesAdminServiceStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);

        }
        catch (AxisFault axisFault) {
            log.error("Unexpected exception thrown");
            axisFault.printStackTrace();
        }

        log.info("ProfilesAdminServiceStub created");
        return profilesAdminServiceStub;

    }

}