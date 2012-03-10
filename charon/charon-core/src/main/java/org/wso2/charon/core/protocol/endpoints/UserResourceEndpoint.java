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
package org.wso2.charon.core.protocol.endpoints;

import org.wso2.charon.core.encoder.Decoder;
import org.wso2.charon.core.encoder.Encoder;
import org.wso2.charon.core.exceptions.BadRequestException;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.exceptions.FormatNotSupportedException;
import org.wso2.charon.core.exceptions.InternalServerException;
import org.wso2.charon.core.exceptions.ResourceNotFoundException;
import org.wso2.charon.core.extensions.Storage;
import org.wso2.charon.core.extensions.UserManager;
import org.wso2.charon.core.objects.*;
import org.wso2.charon.core.protocol.ResponseCodeConstants;
import org.wso2.charon.core.protocol.SCIMResponse;
import org.wso2.charon.core.schema.SCIMConstants;
import org.wso2.charon.core.schema.SCIMSchemaDefinitions;

import java.util.HashMap;
import java.util.Map;

/**
 * REST API exposed by Charon-Core to perform operations on UserResource.
 * Any SCIM service provider can call this API perform relevant CRUD operations on USER ,
 * based on the HTTP requests received by SCIM Client.
 */
public class UserResourceEndpoint extends AbstractResourceEndpoint implements ResourceEndpoint {

    /**
     * Retrieves a user resource given an unique user id. Mapped to HTTP GET request.
     *
     * @param id      - unique resource id
     * @param format  - requested format of the response.
     * @param storage - handler to UserManager implementation that should be passed by the API user.
     * @return SCIM response to be returned.
     */
    public SCIMResponse get(String id, String format, Storage storage) {

        Encoder encoder = null;
        try {
            //obtain the correct encoder according to the format requested.
            encoder = getEncoder(SCIMConstants.identifyFormat(format));

            //API user should pass a UserManager storage to UserResourceEndpoint.
            if (storage instanceof UserManager) {
                //retrieve the user from the provided storage.
                User user = ((UserManager) storage).getUser(id);

                //TODO:needs a validator to see that the User returned by the custom user manager
                // adheres to SCIM spec.

                //if user not found, return an error in relevant format.
                if (user == null) {
                    String error = "User not found in the user store.";
                    //log error.
                    //throw resource not found.
                    throw new ResourceNotFoundException();
                }
                //convert the user into specific format.
                String encodedUser = encoder.encodeSCIMObject(user);
                //if there are any http headers to be added in the response header.
                Map<String, String> httpHeaders = new HashMap<String, String>();
                httpHeaders.put(SCIMConstants.CONTENT_TYPE_HEADER, format);
                return new SCIMResponse(ResponseCodeConstants.CODE_OK, encodedUser, httpHeaders);

            } else {
                String error = "Provided storage handler is not an implementation of UserManager";
                //log the error as well.
                //throw internal server error.
                throw new InternalServerException(error);
            }

        } catch (FormatNotSupportedException e) {
            //if requested format not supported, encode exception and set it in the response.
            return AbstractResourceEndpoint.encodeSCIMException(encoder, e);
        } catch (CharonException e) {
            //we have charon exceptions also, instead of having only internal server error exceptions,
            //because inside API code throws CharonException.
            if (e.getCode() == -1) {
                e.setCode(ResponseCodeConstants.CODE_INTERNAL_SERVER_ERROR);
            }
            return AbstractResourceEndpoint.encodeSCIMException(encoder, e);
        } catch (InternalServerException e) {
            return AbstractResourceEndpoint.encodeSCIMException(encoder, e);
        } catch (ResourceNotFoundException e) {
            return AbstractResourceEndpoint.encodeSCIMException(encoder, e);
        }
    }

    /**
     * Create User in the service provider given the submitted payload that contains the SCIM user
     * resource, format and the handler to storage.
     *
     * @param scimObjectString - Payload of HTTP request, which contains the SCIM object.
     * @param inputFormat      - format of the submitted content
     * @param outputFormat     - format mentioned in HTTP Accept header.
     * @param storage          - handler to storage that should be passed by the API user.
     * @return
     */
    public SCIMResponse create(String scimObjectString, String inputFormat, String outputFormat,
                               Storage storage) {

        //needs to validate the incoming object. eg: id can not be set by the consumer.

        Encoder encoder = null;
        Decoder decoder = null;

        try {
            //obtain the encoder matching the requested output format.
            encoder = getEncoder(SCIMConstants.identifyFormat(outputFormat));
            //obtain the decoder matching the submitted format.
            decoder = getDecoder(SCIMConstants.identifyFormat(inputFormat));

            //decode the SCIM User object, encoded in the submitted payload.
            User user = (User) decoder.decodeResource(scimObjectString,
                                                      SCIMSchemaDefinitions.SCIM_USER_SCHEMA, new User());

            //handover the SCIM User object to the user storage provided by the SP.
            User createdUser;
            if (storage instanceof UserManager) {
                //need to send back the newly created user in the response payload
                createdUser = ((UserManager) storage).createUser(user);

            } else {
                String error = "Provided storage handler is not an implementation of UserManager";
                //log the error as well.
                //throw internal server error.
                throw new InternalServerException(error);
            }
            //encode the newly created SCIM user object and add id attribute to Location header.
            String encodedUser;
            Map<String, String> httpHeaders = new HashMap<String, String>();
            if (createdUser != null) {

                encodedUser = encoder.encodeSCIMObject(createdUser);
                //add location header
                httpHeaders.put(SCIMConstants.LOCATION_HEADER, getResourceEndpointURL(
                        SCIMConstants.USER_ENDPOINT) + createdUser.getId());
                httpHeaders.put(SCIMConstants.CONTENT_TYPE_HEADER, outputFormat);

            } else {
                //TODO:log the error
                String error = "Newly created User resource is null..";
                throw new InternalServerException(error);
            }

            //put the URI of the User object in the response header parameter.
            return new SCIMResponse(ResponseCodeConstants.CODE_CREATED, encodedUser, httpHeaders);

        } catch (FormatNotSupportedException e) {
            //if the submitted format not supported, encode exception and set it in the response.
            return AbstractResourceEndpoint.encodeSCIMException(encoder, e);
        } catch (CharonException e) {
            //we have charon exceptions also, instead of having only internal server error exceptions,
            //because inside API code throws CharonException.
            if (e.getCode() == -1) {
                e.setCode(ResponseCodeConstants.CODE_INTERNAL_SERVER_ERROR);
            }
            return AbstractResourceEndpoint.encodeSCIMException(encoder, e);
        } catch (BadRequestException e) {
            return AbstractResourceEndpoint.encodeSCIMException(encoder, e);
        } catch (InternalServerException e) {
            return AbstractResourceEndpoint.encodeSCIMException(encoder, e);
        }

    }

    /**
     * Method of the ResourceEndpoint that is mapped to HTTP Delete method..
     *
     * @param id
     * @param storage
     * @return
     */
    @Override
    public SCIMResponse delete(String id, Storage storage) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
