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

import org.wso2.charon.core.encoder.Encoder;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.exceptions.FormatNotSupportedException;
import org.wso2.charon.core.exceptions.UnauthorizedException;
import org.wso2.charon.core.extensions.UserManager;
import org.wso2.charon.core.protocol.ResponseCodeConstants;
import org.wso2.charon.core.protocol.SCIMResponse;
import org.wso2.charon.core.protocol.endpoints.AbstractResourceEndpoint;
import org.wso2.charon.core.protocol.endpoints.GroupResourceEndpoint;
import org.wso2.charon.core.schema.SCIMConstants;
import org.wso2.charon.utils.DefaultCharonManager;
import org.wso2.charon.utils.jaxrs.JAXRSResponseBuilder;

import javax.ws.rs.DELETE;
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
 * JAX-RS Service that exposes the Group Resource in SCIM Service Provider/
 */
@Path("/Groups")
public class GroupResource {
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroup(@PathParam(SCIMConstants.CommonSchemaConstants.ID) String id,
                            @HeaderParam(SCIMConstants.AUTH_HEADER_USERNAME) String userName,
                            @HeaderParam(SCIMConstants.AUTH_HEADER_PASSWORD) String password,
                            @HeaderParam(SCIMConstants.ACCEPT_HEADER) String format,
                            @HeaderParam(SCIMConstants.AUTH_HEADER_OAUTH_KEY) String authorization) {
        Encoder encoder = null;
        try {
            DefaultCharonManager defaultCharonManager = DefaultCharonManager.getInstance();

            // defaults to application/json.
            if (format == null) {
                format = SCIMConstants.APPLICATION_JSON;
            }
            //obtain the encoder at this layer in case exceptions needs to be encoded.
            encoder = defaultCharonManager.getEncoder(SCIMConstants.identifyFormat(format));
            //perform authentication
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put(SCIMConstants.AUTH_HEADER_USERNAME, userName);
            headerMap.put(SCIMConstants.AUTH_HEADER_PASSWORD, password);
            headerMap.put(SCIMConstants.AUTH_HEADER_OAUTH_KEY, authorization);
            //authenticate the request
            defaultCharonManager.handleAuthentication(headerMap);

            //obtain the user store manager
            UserManager userManager = DefaultCharonManager.getInstance().getUserManager(
                    userName);

            //create charon-SCIM Group endpoint and hand-over the request.
            GroupResourceEndpoint groupResourceEndpoint = new GroupResourceEndpoint();

            SCIMResponse scimResponse = groupResourceEndpoint.get(id, format, userManager);
            //needs to check the code of the response and return 200 0k or other error codes
            // appropriately.
            return new JAXRSResponseBuilder().buildResponse(scimResponse);

        } catch (CharonException e) {
            //create SCIM response with code as the same of exception and message as error message of the exception
            if (e.getCode() == -1) {
                e.setCode(ResponseCodeConstants.CODE_INTERNAL_SERVER_ERROR);
            }
            return new JAXRSResponseBuilder().buildResponse(
                    AbstractResourceEndpoint.encodeSCIMException(encoder, e));
        } catch (UnauthorizedException e) {
            return new JAXRSResponseBuilder().buildResponse(
                    AbstractResourceEndpoint.encodeSCIMException(encoder, e));
        } catch (FormatNotSupportedException e) {
            return new JAXRSResponseBuilder().buildResponse(
                    AbstractResourceEndpoint.encodeSCIMException(encoder, e));
        }
    }

    @POST
    public Response createGroup(@HeaderParam(SCIMConstants.CONTENT_TYPE_HEADER) String inputFormat,
                               @HeaderParam(SCIMConstants.ACCEPT_HEADER) String outputFormat,
                               @HeaderParam(SCIMConstants.AUTH_HEADER_USERNAME) String userName,
                               @HeaderParam(SCIMConstants.AUTH_HEADER_PASSWORD) String password,
                               @HeaderParam(SCIMConstants.AUTH_HEADER_OAUTH_KEY) String authorization,
                               String resourceString) {
        Encoder encoder = null;
        try {
            //obtain default charon manager
            DefaultCharonManager defaultCharonManager = DefaultCharonManager.getInstance();

            //content-type header is compulsory in post request.
            if (inputFormat == null) {
                String error = SCIMConstants.CONTENT_TYPE_HEADER + " not present in the request header";
                throw new FormatNotSupportedException(error);
            }
            //set the format in which the response should be encoded, if not specified in the request,
            // defaults to application/json.
            if (outputFormat == null) {
                outputFormat = SCIMConstants.APPLICATION_JSON;
            }
            //obtain the encoder at this layer in case exceptions needs to be encoded.
            encoder = defaultCharonManager.getEncoder(SCIMConstants.identifyFormat(outputFormat));
            //perform authentication
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put(SCIMConstants.AUTH_HEADER_USERNAME, userName);
            headerMap.put(SCIMConstants.AUTH_HEADER_PASSWORD, password);
            headerMap.put(SCIMConstants.AUTH_HEADER_OAUTH_KEY, authorization);
            //authenticate the request
            defaultCharonManager.handleAuthentication(headerMap);

            //obtain the user store manager
            UserManager userManager = DefaultCharonManager.getInstance().getUserManager(
                    userName);

            //create charon-SCIM user endpoint and hand-over the request.
            GroupResourceEndpoint groupResourceEndpoint = new GroupResourceEndpoint();

            SCIMResponse response = groupResourceEndpoint.create(resourceString, inputFormat,
                                                                 outputFormat, userManager);

            return new JAXRSResponseBuilder().buildResponse(response);

        } catch (CharonException e) {
            //create SCIM response with code as the same of exception and message as error message of the exception
            if (e.getCode() == -1) {
                e.setCode(ResponseCodeConstants.CODE_INTERNAL_SERVER_ERROR);
            }
            return new JAXRSResponseBuilder().buildResponse(
                    AbstractResourceEndpoint.encodeSCIMException(encoder, e));
        } catch (UnauthorizedException e) {
            return new JAXRSResponseBuilder().buildResponse(
                    AbstractResourceEndpoint.encodeSCIMException(encoder, e));
        } catch (FormatNotSupportedException e) {
            return new JAXRSResponseBuilder().buildResponse(
                    AbstractResourceEndpoint.encodeSCIMException(encoder, e));
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteGroup(@PathParam(SCIMConstants.CommonSchemaConstants.ID) String id,
                               @HeaderParam(SCIMConstants.AUTH_HEADER_USERNAME) String userName,
                               @HeaderParam(SCIMConstants.AUTH_HEADER_PASSWORD) String password,
                               @HeaderParam(SCIMConstants.ACCEPT_HEADER) String format,
                               @HeaderParam(SCIMConstants.AUTH_HEADER_OAUTH_KEY) String authorization) {
        Encoder encoder = null;
        try {
            DefaultCharonManager defaultCharonManager = DefaultCharonManager.getInstance();

            // defaults to application/json.
            if (format == null) {
                format = SCIMConstants.APPLICATION_JSON;
            }
            //obtain the encoder at this layer in case exceptions needs to be encoded.
            encoder = defaultCharonManager.getEncoder(SCIMConstants.identifyFormat(format));
            //perform authentication
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put(SCIMConstants.AUTH_HEADER_USERNAME, userName);
            headerMap.put(SCIMConstants.AUTH_HEADER_PASSWORD, password);
            headerMap.put(SCIMConstants.AUTH_HEADER_OAUTH_KEY, authorization);
            //authenticate the request
            defaultCharonManager.handleAuthentication(headerMap);

            //obtain the user store manager
            UserManager userManager = DefaultCharonManager.getInstance().getUserManager(
                    userName);

            //create charon-SCIM user endpoint and hand-over the request.
            GroupResourceEndpoint groupResourceEndpoint = new GroupResourceEndpoint();

            SCIMResponse scimResponse = groupResourceEndpoint.delete(id, userManager, format);
            //needs to check the code of the response and return 200 0k or other error codes
            // appropriately.
            return new JAXRSResponseBuilder().buildResponse(scimResponse);

        } catch (CharonException e) {
            //create SCIM response with code as the same of exception and message as error message of the exception
            if (e.getCode() == -1) {
                e.setCode(ResponseCodeConstants.CODE_INTERNAL_SERVER_ERROR);
            }
            return new JAXRSResponseBuilder().buildResponse(
                    AbstractResourceEndpoint.encodeSCIMException(encoder, e));
        } catch (UnauthorizedException e) {
            return new JAXRSResponseBuilder().buildResponse(
                    AbstractResourceEndpoint.encodeSCIMException(encoder, e));
        } catch (FormatNotSupportedException e) {
            return new JAXRSResponseBuilder().buildResponse(
                    AbstractResourceEndpoint.encodeSCIMException(encoder, e));
        }
    }

}
