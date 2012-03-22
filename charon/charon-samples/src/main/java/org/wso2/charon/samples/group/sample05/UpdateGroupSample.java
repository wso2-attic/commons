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
package org.wso2.charon.samples.group.sample05;

import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.ClientWebException;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.apache.wink.client.handlers.ClientHandler;
import org.wso2.charon.core.client.SCIMClient;
import org.wso2.charon.core.exceptions.BadRequestException;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.objects.Group;
import org.wso2.charon.core.schema.SCIMConstants;
import org.wso2.charon.samples.utils.CharonResponseHandler;
import org.wso2.charon.samples.utils.SampleConstants;
import org.wso2.charon.utils.authentication.BasicAuthHandler;
import org.wso2.charon.utils.authentication.BasicAuthInfo;

public class UpdateGroupSample {
    public static final String GROUP_ID = "e3d7fda2-2619-4ace-83e1-81842c45bc0b";
    //public static final String OLD_MEMBER = "9c9afc67-2897-4c8c-9461-5f7c7d724307";
    public static final String NEW_MEMBER = "1acee9bf-576f-4c13-afde-ff9e2ed28768";
    public static final String NEW_DISPLAY_NAME = "QA";

    public static void main(String[] args) {

        try {
            //create SCIM client
            SCIMClient scimClient = new SCIMClient();
            //create a apache wink ClientHandler to intercept and identify response messages
            CharonResponseHandler responseHandler = new CharonResponseHandler();
            responseHandler.setSCIMClient(scimClient);
            //set the handler in wink client config
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.handlers(new ClientHandler[]{responseHandler});
            //create a wink rest client with the above config
            RestClient restClient = new RestClient(clientConfig);

            BasicAuthInfo basicAuthInfo = new BasicAuthInfo();
            basicAuthInfo.setUserName(SampleConstants.CRED_USER_NAME);
            basicAuthInfo.setPassword(SampleConstants.CRED_PASSWORD);

            BasicAuthHandler basicAuthHandler = new BasicAuthHandler();
            BasicAuthInfo encodedBasicAuthInfo = (BasicAuthInfo) basicAuthHandler.getAuthenticationToken(basicAuthInfo);

            //create resource endpoint to access a known user resource.
            Resource groupResource = restClient.resource(SampleConstants.GROUP_ENDPOINT + GROUP_ID);
            String response = groupResource.
                    header(SCIMConstants.AUTHORIZATION_HEADER, encodedBasicAuthInfo.getAuthorizationHeader()).
                    contentType(SCIMConstants.APPLICATION_JSON).accept(SCIMConstants.APPLICATION_JSON)
                    .get(String.class);

            System.out.println("Retrieved group: "+response);
            //decode retrieved group
            Group decodedGroup = (Group) scimClient.decodeSCIMResponse(response, SCIMConstants.JSON, 2);

            decodedGroup.setDisplayName(NEW_DISPLAY_NAME);
            //decodedGroup.removeMember(OLD_MEMBER);
            decodedGroup.setGroupMember(NEW_MEMBER);

            String updatedGroupString = scimClient.encodeSCIMObject(decodedGroup, SCIMConstants.JSON);

            Resource updateGroupResource = restClient.resource(SampleConstants.GROUP_ENDPOINT + GROUP_ID);
            String responseUpdated = updateGroupResource.
                    header(SCIMConstants.AUTHORIZATION_HEADER, encodedBasicAuthInfo.getAuthorizationHeader()).
                    contentType(SCIMConstants.APPLICATION_JSON).accept(SCIMConstants.APPLICATION_JSON)
                    .put(String.class, updatedGroupString);
            System.out.println("Updated group: " + responseUpdated);
            //decode the response
            //System.out.println(response);
        } catch (ClientWebException e) {
            System.out.println(e.getRequest().getEntity());
            System.out.println(e.getResponse().getMessage());
            e.printStackTrace();
        } catch (BadRequestException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CharonException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
