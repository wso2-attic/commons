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
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.exceptions.FormatNotSupportedException;
import org.wso2.charon.core.exceptions.InternalServerException;
import org.wso2.charon.core.exceptions.ResourceNotFoundException;
import org.wso2.charon.core.extensions.Storage;
import org.wso2.charon.core.extensions.UserManager;
import org.wso2.charon.core.objects.*;
import org.wso2.charon.core.protocol.ResponseCodeConstants;
import org.wso2.charon.core.protocol.SCIMResponse;

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
            encoder = AbstractResourceEndpoint.getEncoder(format);

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

                    //set error code in encoded response and return- resource not found.
                    String encodedException = encoder.encodeSCIMException(
                            new ResourceNotFoundException());
                    return buildResponse(ResponseCodeConstants.CODE_RESOURCE_NOT_FOUND,
                                         encodedException);
                }

                //convert the user into specific format.
                String encodedUser = encoder.encodeSCIMObject(user);
                return buildResponse(ResponseCodeConstants.CODE_OK, encodedUser);

            } else {
                String error = "Provided storage handler is not an implementation of UserManager";
                //log the error as well.

                //set error code in response and return- internal server error.
                String encodedException = encoder.encodeSCIMException(new InternalServerException());
                return buildResponse(ResponseCodeConstants.CODE_INTERNAL_SERVER_ERROR,
                                     encodedException);
            }

        } catch (FormatNotSupportedException e) {
            //if requested format not supported, encode exception and set it in the response.
            String encodedException = encoder.encodeSCIMException(new FormatNotSupportedException());
            return buildResponse(ResponseCodeConstants.CODE_FORMAT_NOT_SUPPORTED, encodedException);

        } catch (CharonException e) {
            String encodedException = encoder.encodeSCIMException(e);
            return buildResponse(ResponseCodeConstants.CODE_INTERNAL_SERVER_ERROR, encodedException);
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
            encoder = AbstractResourceEndpoint.getEncoder(outputFormat);
            //obtain the decoder matching the submitted format.
            decoder = AbstractResourceEndpoint.getDecoder(inputFormat);

            //decode the SCIM User object, encoded in the submitted payload.
            

            //handover the SCIM User object to the user storage provided by the SP.

            //encode the returned SCIM user object.

            //put the URI of the User object in the response header parameter.



        } catch (FormatNotSupportedException e) {
            //if the submitted format not supported, encode exception and set it in the response.
            String encodedException = encoder.encodeSCIMException(new FormatNotSupportedException());
            return buildResponse(ResponseCodeConstants.CODE_FORMAT_NOT_SUPPORTED, encodedException);

        } catch (CharonException e) {
            String encodedException = encoder.encodeSCIMException(e);
            return buildResponse(ResponseCodeConstants.CODE_INTERNAL_SERVER_ERROR, encodedException);
        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
