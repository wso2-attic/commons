/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.charon.samples.registration;

import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.apache.wink.client.handlers.ClientHandler;
import org.wso2.charon.core.client.SCIMClient;
import org.wso2.charon.core.schema.SCIMConstants;
import org.wso2.charon.samples.utils.CharonResponseHandler;

public class RegisterTenantSample {

    public static final String REG_SERVICE_ENDPOINT = "http://localhost:8081/charonDemoApp/scim/RegistrationService";

    public static void main(String[] args) {

        SCIMClient scimClient = new SCIMClient();
        ClientConfig clientConfig = new ClientConfig();
        CharonResponseHandler responseHandler = new CharonResponseHandler();
        responseHandler.setSCIMClient(scimClient);
        clientConfig.handlers(new ClientHandler[]{responseHandler});
        RestClient restClient = new RestClient(clientConfig);

        //create resource endpoint
        Resource registrationService = restClient.resource(REG_SERVICE_ENDPOINT);

        //enable, disable SSL.

        registrationService.header("tenantAdminUserName", "hasinig@wso2.com").
                header("tenantAdminPassword", "hasinig").header("tenantDomain", "wso2.com").
                header("authMechanism", SCIMConstants.AUTH_TYPE_BASIC).post(String.class, "");

    }

}
