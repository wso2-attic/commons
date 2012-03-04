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
package org.wso2.charon.samples.user.sample02;

import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.ClientWebException;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.apache.wink.client.handlers.ClientHandler;
import org.wso2.charon.core.client.SCIMClient;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.objects.User;
import org.wso2.charon.core.schema.SCIMConstants;
import org.wso2.charon.samples.utils.CharonResponseHandler;

public class GetUserSample {

    public static final String USER_ENDPOINT = "http://localhost:8081/charonDemoApp/scim/Users/";
    public static final String USER_ID = "02e6134b-7724-499a-b1a9-daafb73ed49";

    public static void main(String[] args) {

        try {
            //create SCIM client
            SCIMClient scimClient = new SCIMClient();
            User scimUser = scimClient.createUser();
            scimUser.setUserName("hasini");
            scimUser.setExternalId("hasini@wso2.com");
            scimUser.setEmails(new String[]{"hasini@wso2.com", "hasi7786@wso2.com"});

            String encodedUser = scimClient.encodeSCIMObject(scimUser, SCIMConstants.JSON);

            ClientConfig clientConfig = new ClientConfig();
            CharonResponseHandler responseHandler = new CharonResponseHandler();
            responseHandler.setSCIMClient(scimClient);
            clientConfig.handlers(new ClientHandler[]{responseHandler});
            RestClient restClient = new RestClient(clientConfig);

            //create resource endpoint
            Resource userResource = restClient.resource(USER_ENDPOINT + USER_ID);

            //enable, disable SSL.

            String response = userResource.header("userName", "hasinig@wso2.com").header("password", "hasinig").
                    contentType("application/json").accept("application/json").get(String.class);

            //decode the response
            System.out.println(response);
        } catch (CharonException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClientWebException e) {
            System.out.println(e.getRequest().getEntity());
            System.out.println(e.getResponse().getMessage());
            e.printStackTrace();
        }

    }

}
