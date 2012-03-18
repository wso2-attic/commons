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
import org.wso2.charon.core.exceptions.NotFoundException;
import org.wso2.charon.core.exceptions.ResourceNotFoundException;
import org.wso2.charon.core.extensions.Storage;
import org.wso2.charon.core.extensions.UserManager;
import org.wso2.charon.core.objects.Group;
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
public class GroupResourceEndpoint extends AbstractResourceEndpoint implements ResourceEndpoint {

    /**
     * Retrieves a group resource given an unique group id. Mapped to HTTP GET request.
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
                Group group = ((UserManager) storage).getGroup(id);

                //TODO:needs a validator to see that the User returned by the custom user manager
                // adheres to SCIM spec.

                //if user not found, return an error in relevant format.
                if (group == null) {
                    String error = "Group not found in the user store.";
                    //log error.
                    //throw resource not found.
                    throw new ResourceNotFoundException();
                }
                //convert the user into specific format.
                String encodedGroup = encoder.encodeSCIMObject(group);
                //if there are any http headers to be added in the response header.
                Map<String, String> httpHeaders = new HashMap<String, String>();
                httpHeaders.put(SCIMConstants.CONTENT_TYPE_HEADER, format);
                return new SCIMResponse(ResponseCodeConstants.CODE_OK, encodedGroup, httpHeaders);

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
     * Create group in the service provider given the submitted payload that contains the SCIM group
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

            //decode the SCIM group object, encoded in the submitted payload.
            Group group = (Group) decoder.decodeResource(scimObjectString,
                                                         SCIMSchemaDefinitions.SCIM_GROUP_SCHEMA,
                                                         new Group());

            //handover the SCIM User object to the group storage provided by the SP.
            Group createdGroup;
            if (storage instanceof UserManager) {
                //need to send back the newly created group in the response payload
                createdGroup = ((UserManager) storage).createGroup(group);

            } else {
                String error = "Provided storage handler is not an implementation of UserManager";
                //log the error as well.
                //throw internal server error.
                throw new InternalServerException(error);
            }
            //encode the newly created SCIM group object and add id attribute to Location header.
            String encodedGroup;
            Map<String, String> httpHeaders = new HashMap<String, String>();
            if (createdGroup != null) {

                encodedGroup = encoder.encodeSCIMObject(createdGroup);
                //add location header
                httpHeaders.put(SCIMConstants.LOCATION_HEADER, getResourceEndpointURL(
                        SCIMConstants.GROUP_ENDPOINT) + createdGroup.getId());
                httpHeaders.put(SCIMConstants.CONTENT_TYPE_HEADER, outputFormat);

            } else {
                //TODO:log the error
                String error = "Newly created Group resource is null..";
                throw new InternalServerException(error);
            }

            //put the URI of the User object in the response header parameter.
            return new SCIMResponse(ResponseCodeConstants.CODE_CREATED, encodedGroup, httpHeaders);

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
     * @param outputFormat
     * @return
     */
    @Override
    public SCIMResponse delete(String id, Storage storage, String outputFormat) {
        Encoder encoder = null;
        try {
            //obtain the encoder matching the requested output format.
            encoder = getEncoder(SCIMConstants.identifyFormat(outputFormat));
            if (storage instanceof UserManager) {
                //delete group
                ((UserManager) storage).deleteGroup(id);

            } else {
                String error = "Provided storage handler is not an implementation of UserManager";
                //log the error as well.
                //throw internal server error.
                throw new InternalServerException(error);
            }
            //on successful deletion SCIMResponse only has 200 OK status code.
            return new SCIMResponse(ResponseCodeConstants.CODE_OK, null, null);
        } catch (InternalServerException e) {
            return AbstractResourceEndpoint.encodeSCIMException(encoder, e);
        } catch (NotFoundException e) {
            return AbstractResourceEndpoint.encodeSCIMException(encoder, e);
        } catch (FormatNotSupportedException e) {
            return AbstractResourceEndpoint.encodeSCIMException(encoder, e);
        } catch (CharonException e) {
            //we have charon exceptions also, instead of having only internal server error exceptions,
            //because inside API code throws CharonException.
            if (e.getCode() == -1) {
                e.setCode(ResponseCodeConstants.CODE_INTERNAL_SERVER_ERROR);
            }
            return AbstractResourceEndpoint.encodeSCIMException(encoder, e);
        }
    }

    /**
     * Supports list by displayName and externalID
     * @param searchAttribute
     * @return
     */
    @Override
    public SCIMResponse listByAttribute(String searchAttribute) {
        
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SCIMResponse listByFilter(String filterString) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SCIMResponse listBySort(String sortBy, String sortOrder) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SCIMResponse listWithPagination(int startIndex, int count) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
