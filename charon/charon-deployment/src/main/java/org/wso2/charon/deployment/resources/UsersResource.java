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
package org.wso2.charon.deployment.resources;

import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.exceptions.UnauthorizedException;
import org.wso2.charon.core.extensions.UserManager;
import org.wso2.charon.core.protocol.ResponseCodeConstants;
import org.wso2.charon.core.protocol.SCIMResponse;
import org.wso2.charon.core.protocol.endpoints.UserResourceEndpoint;
import org.wso2.charon.core.schema.SCIMConstants;
import org.wso2.charon.utils.DefaultCharonManager;
import org.wso2.charon.utils.builders.JAXRSResponseBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * JAX-RS Service that exposes the Users Resource in SCIM Service Provider/
 */
@Path("/Users")
public class UsersResource {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam(SCIMConstants.CommonSchemaConstants.ID) String id,
                            @HeaderParam(SCIMConstants.AUTH_HEADER_USERNAME) String userName,
                            @HeaderParam(SCIMConstants.AUTH_HEADER_PASSWORD) String password,
                            @HeaderParam(SCIMConstants.ACCEPT_HEADER) String format,
                            @HeaderParam(SCIMConstants.AUTH_HEADER_OAUTH_KEY) String authorization) {

        try {
            DefaultCharonManager defaultCharonManager = DefaultCharonManager.getInstance();
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put(SCIMConstants.AUTH_HEADER_USERNAME, userName);
            headerMap.put(SCIMConstants.AUTH_HEADER_PASSWORD, password);
            headerMap.put(SCIMConstants.AUTH_HEADER_OAUTH_KEY, authorization);
            //authenticate the request
            defaultCharonManager.handleAuthentication(headerMap);

            // defaults to application/json.
            if (format == null) {
                format = SCIMConstants.JSON;
            }

            //obtain the user store manager
            UserManager userManager = DefaultCharonManager.getInstance().getUserManager(
                    userName);

            //create charon-SCIM user endpoint and hand-over the request.
            UserResourceEndpoint userResourceEndpoint = new UserResourceEndpoint();

            SCIMResponse scimResponse = userResourceEndpoint.get(id, format, userManager);
            //needs to check the code of the response and return 200 0k or other error codes
            // appropriately.
            return new JAXRSResponseBuilder().buildResponse(scimResponse);

        } catch (CharonException e) {
            //create SCIM response with code as the same of exception and message as error message of the exception
            if (e.getCode() == -1) {
                e.setCode(ResponseCodeConstants.CODE_INTERNAL_SERVER_ERROR);
            }
            SCIMResponse scimResponse = new SCIMResponse(e.getCode(), e.getDescription());
            return new JAXRSResponseBuilder().buildResponse(scimResponse);
        } catch (UnauthorizedException e) {
            SCIMResponse scimResponse = new SCIMResponse(e.getCode(), e.getDescription());
            return new JAXRSResponseBuilder().buildResponse(scimResponse);
        }
    }

    @POST
    public Response createUser(@HeaderParam(SCIMConstants.CONTENT_TYPE_HEADER) String inputFormat,
                               @HeaderParam(SCIMConstants.ACCEPT_HEADER) String outputFormat,
                               @HeaderParam(SCIMConstants.AUTH_HEADER_USERNAME) String userName,
                               @HeaderParam(SCIMConstants.AUTH_HEADER_PASSWORD) String password,
                               @HeaderParam(SCIMConstants.AUTH_HEADER_OAUTH_KEY) String authorization,
                               String resourceString) {
        try {
            DefaultCharonManager defaultCharonManager = DefaultCharonManager.getInstance();
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put(SCIMConstants.AUTH_HEADER_USERNAME, userName);
            headerMap.put(SCIMConstants.AUTH_HEADER_PASSWORD, password);
            headerMap.put(SCIMConstants.AUTH_HEADER_OAUTH_KEY, authorization);
            //authenticate the request
            defaultCharonManager.handleAuthentication(headerMap);

            //set the format in which the response should be encoded, if not specified in the request,
            // defaults to application/json.
            if (outputFormat == null) {
                outputFormat = SCIMConstants.APPLICATION_JSON;
            }

            //obtain the user store manager
            UserManager userManager = DefaultCharonManager.getInstance().getUserManager(
                    userName);

            //create charon-SCIM user endpoint and hand-over the request.
            UserResourceEndpoint userResourceEndpoint = new UserResourceEndpoint();

            SCIMResponse response = (userResourceEndpoint.create(resourceString, inputFormat, outputFormat, userManager));

            return new JAXRSResponseBuilder().buildResponse(response);

        } catch (CharonException e) {
            //create SCIM response with code as the same of exception and message as error message of the exception
            if (e.getCode() == -1) {
                e.setCode(ResponseCodeConstants.CODE_INTERNAL_SERVER_ERROR);
            }
            //TODO:encode exception
            SCIMResponse scimResponse = new SCIMResponse(e.getCode(), e.getDescription());
            return new JAXRSResponseBuilder().buildResponse(scimResponse);
        } catch (UnauthorizedException e) {
            SCIMResponse scimResponse = new SCIMResponse(e.getCode(), e.getDescription());
            return new JAXRSResponseBuilder().buildResponse(scimResponse);
        }
    }
}
