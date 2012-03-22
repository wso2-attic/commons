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
package org.wso2.charon.samples.user.sample01;

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
import org.wso2.charon.samples.utils.SampleConstants;
import org.wso2.charon.utils.authentication.BasicAuthHandler;
import org.wso2.charon.utils.authentication.BasicAuthInfo;

public class CreateUserSample {

    //user details
    public static final String USER_NAME = "hasinig";
    public static final String EXTERNAL_ID = "hasini@gmail.com";
    public static final String[] EMAILS = {"hasini@gmail.com", "hasini@wso2.com"};
    public static final String DISPLAY_NAME = "Hasini Gunasinghe";
    public static final String PASSWORD = "dummyPW";
    public static final String LANGUAGE = "Sinhala";
    public static final String PHONE_NUMBER = "9077657623";

    public static void main(String[] args) {

        try {
            //create SCIM client
            SCIMClient scimClient = new SCIMClient();
            //create a user according to SCIM User Schema
            User scimUser = scimClient.createUser();
            scimUser.setUserName(USER_NAME);
            scimUser.setExternalId(EXTERNAL_ID);
            scimUser.setEmails(EMAILS);
            scimUser.setDisplayName(DISPLAY_NAME);
            scimUser.setPassword(PASSWORD);
            scimUser.setPreferredLanguage(LANGUAGE);
            scimUser.setPhoneNumber(PHONE_NUMBER, null, false);
            //encode the user in JSON format
            String encodedUser = scimClient.encodeSCIMObject(scimUser, SCIMConstants.JSON);
            //create a apache wink ClientHandler to intercept and identify response messages
            CharonResponseHandler responseHandler = new CharonResponseHandler();
            responseHandler.setSCIMClient(scimClient);
            //set the handler in wink client config
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.handlers(new ClientHandler[]{responseHandler});
            //create a wink rest client with the above config
            RestClient restClient = new RestClient(clientConfig);
            //create resource endpoint to access User resource
            Resource userResource = restClient.resource(SampleConstants.USER_ENDPOINT);

            BasicAuthInfo basicAuthInfo = new BasicAuthInfo();
            basicAuthInfo.setUserName(SampleConstants.CRED_USER_NAME);
            basicAuthInfo.setPassword(SampleConstants.CRED_PASSWORD);

            BasicAuthHandler basicAuthHandler = new BasicAuthHandler();
            BasicAuthInfo encodedBasicAuthInfo = (BasicAuthInfo) basicAuthHandler.getAuthenticationToken(basicAuthInfo);


            //TODO:enable, disable SSL. For the demo purpose, we make the calls over http
            //send previously registered SCIM consumer credentials in http headers.
            String response = userResource.
                    header(SCIMConstants.AUTHORIZATION_HEADER, encodedBasicAuthInfo.getAuthorizationHeader()).
                    contentType(SCIMConstants.APPLICATION_JSON).accept(SCIMConstants.APPLICATION_JSON).
                    post(String.class, encodedUser);

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
