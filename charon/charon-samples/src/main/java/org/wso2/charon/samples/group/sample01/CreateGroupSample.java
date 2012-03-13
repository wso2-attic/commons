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
package org.wso2.charon.samples.group.sample01;

import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.ClientWebException;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.apache.wink.client.handlers.ClientHandler;
import org.wso2.charon.core.client.SCIMClient;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.objects.Group;
import org.wso2.charon.core.schema.SCIMConstants;
import org.wso2.charon.samples.utils.CharonResponseHandler;
import org.wso2.charon.samples.utils.SampleConstants;

public class CreateGroupSample {
    //user details
    public static final String DISPLAY_NAME = "marketingsnjk";
    public static final String EXTERNAL_ID = "marketingsnjk";
    //replace following IDs with already existing member IDs.
    public static final String[] MEMBERS = {"2e54c490-c510-4d70-a95d-6eb3b21ee941",
                                            "30e99938-3bd3-412f-a29d-8531b940c4a9"};

    public static void main(String[] args) {

        try {
            //create SCIM client
            SCIMClient scimClient = new SCIMClient();
            //create a group according to SCIM Group Schema
            Group scimGroup = scimClient.createGroup();
            scimGroup.setExternalId(EXTERNAL_ID);
            scimGroup.setDisplayName(DISPLAY_NAME);
            //set group members
            for (String member : MEMBERS) {
                scimGroup.setMember(member);
            }
            //encode the group in JSON format
            String encodedGroup = scimClient.encodeSCIMObject(scimGroup, SCIMConstants.JSON);
            //create a apache wink ClientHandler to intercept and identify response messages
            CharonResponseHandler responseHandler = new CharonResponseHandler();
            responseHandler.setSCIMClient(scimClient);
            //set the handler in wink client config
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.handlers(new ClientHandler[]{responseHandler});
            //create a wink rest client with the above config
            RestClient restClient = new RestClient(clientConfig);
            //create resource endpoint to access User resource
            Resource groupResource = restClient.resource(SampleConstants.GROUP_ENDPOINT);

            //TODO:enable, disable SSL. For the demo purpose, we make the calls over http
            //send previously registered SCIM consumer credentials in http headers.
            String response = groupResource.
                    header(SCIMConstants.AUTH_HEADER_USERNAME, SampleConstants.CRED_USER_NAME).
                    header(SCIMConstants.AUTH_HEADER_PASSWORD, SampleConstants.CRED_PASSWORD).
                    contentType(SCIMConstants.APPLICATION_JSON).accept(SCIMConstants.APPLICATION_JSON).
                    post(String.class, encodedGroup);

            //decode the response
            System.out.println(response);

        } catch (CharonException e) {
            e.printStackTrace();
        } catch (ClientWebException e) {
            System.out.println(e.getRequest().getEntity());
            System.out.println(e.getResponse().getMessage());
            e.printStackTrace();
        }
    }
}
