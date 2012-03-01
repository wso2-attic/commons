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
package org.wso2.charon.core.client;

import org.wso2.charon.core.encoder.Decoder;
import org.wso2.charon.core.encoder.Encoder;
import org.wso2.charon.core.encoder.json.JSONDecoder;
import org.wso2.charon.core.encoder.json.JSONEncoder;
import org.wso2.charon.core.exceptions.AbstractCharonException;
import org.wso2.charon.core.exceptions.BadRequestException;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.objects.AbstractSCIMObject;
import org.wso2.charon.core.objects.SCIMObject;
import org.wso2.charon.core.objects.User;
import org.wso2.charon.core.protocol.ResponseCodeConstants;
import org.wso2.charon.core.schema.ResourceSchema;
import org.wso2.charon.core.schema.SCIMConstants;
import org.wso2.charon.core.schema.SCIMSchemaDefinitions;

/**
 * SCIM Client API
 */
public class SCIMClient {

    /**
     * JSON encoder, decoder
     */
    private Encoder jsonEncoder;
    private Decoder jsonDecoder;

    private Encoder xmlEncoder;
    private Decoder xmlDecoder;

    public static final int USER = 1;
    public static final int GROUP = 2;

    public SCIMClient() {
        //no config provided. Hence, default encoder/decoder in Charon-Core is used.
        jsonEncoder = new JSONEncoder();
        jsonDecoder = new JSONDecoder();
    }

    public SCIMClient(CharonClientConfig clientConfig) {
        //to allow to set extensions in the SCIM Clientside like: encoder/decoders, AuthHandlers etc.
        //TODO:decide how to register schema extension in client side - i.e: If an extended schema
        //is registered for User, how to access that user, when "createUser" method is called.
        //we can have user, group objects of parent type and initialize relevant type through config constructor.
    }

    /**
     * Return a SCIMUser object as defined in SCIM schema
     *
     * @return
     */
    public User createUser() {
        return new User();
    }

    /**
     * Encode the SCIM object, in the given format.
     *
     * @param scimObject
     * @param format
     * @return
     * @throws CharonException
     */
    public String encodeSCIMObject(AbstractSCIMObject scimObject, String format)
            throws CharonException {
        if ((format.equals(SCIMConstants.JSON)) && (jsonEncoder != null)) {
            return jsonEncoder.encodeSCIMObject(scimObject);
        } else if ((format.equals(SCIMConstants.XML)) && (xmlEncoder != null)) {
            return xmlEncoder.encodeSCIMObject(scimObject);
        } else {
            throw new CharonException("Encoder in the given format is not properly initialized..");
        }
    }

    /**
     * Decode the SCIMResponse, given the format and the resource type.
     * Here we assume the resource type is of an existing SCIMObject type.
     * Int type is given as parameter rather than AbstractSCIMObject - so that API user can
     * select out of existing type without worrying about which extended type to pass.
     *
     * @param scimResponse
     * @param format
     * @return
     */
    public SCIMObject decodeSCIMResponse(String scimResponse, String format, int resourceType)
            throws BadRequestException, CharonException {
        if ((format.equals(SCIMConstants.JSON)) && (jsonDecoder != null)) {
            return decodeSCIMResponse(scimResponse, jsonDecoder, resourceType);

        } else if ((format.equals(SCIMConstants.XML)) && (xmlEncoder != null)) {
            return decodeSCIMResponse(scimResponse, xmlDecoder, resourceType);

        } else {
            throw new CharonException("Encoder in the given format is not properly initialized..");
        }
    }

    private SCIMObject decodeSCIMResponse(String scimResponse, Decoder decoder,
                                          int resourceType)
            throws CharonException, BadRequestException {
        switch (resourceType) {
            case 1:
                return decoder.decodeResource(scimResponse,
                                              SCIMSchemaDefinitions.SCIM_USER_SCHEMA, new User());
            case 2:
                return decoder.decodeResource(scimResponse,
                                              SCIMSchemaDefinitions.SCIM_USER_SCHEMA, new User());
            default:
                throw new CharonException("Resource type didn't match any existing types.");
        }
    }

    /**
     * Decode the SCIMResponse which contains a custom SCIMObject which is based on an extended
     * Schema.
     *
     * @param scimResponse
     * @param format
     * @param resourceSchema
     * @return
     */
    public SCIMObject decodeSCIMResponse(String scimResponse, String format,
                                         ResourceSchema resourceSchema,
                                         AbstractSCIMObject scimObject)
            throws CharonException, BadRequestException {
        if ((format.equals(SCIMConstants.JSON)) && (jsonDecoder != null)) {
            return jsonDecoder.decodeResource(scimResponse, resourceSchema, scimObject);

        } else if ((format.equals(SCIMConstants.XML)) && (xmlEncoder != null)) {
            return xmlDecoder.decodeResource(scimResponse, resourceSchema, scimObject);

        } else {
            throw new CharonException("Encoder in the given format is not properly initialized..");
        }

    }

    /**
     * Once the response is identified as containing exception, decode the relevant e
     *
     * @param scimResponse
     * @param format
     * @return
     * @throws CharonException
     */
    public AbstractCharonException decodeSCIMException(String scimResponse, String format)
            throws CharonException {
        if ((format.equals(SCIMConstants.JSON)) && (jsonDecoder != null)) {
            return jsonDecoder.decodeException(scimResponse);

        } else if ((format.equals(SCIMConstants.XML)) && (xmlEncoder != null)) {
            return xmlDecoder.decodeException(scimResponse);

        } else {
            throw new CharonException("Encoder in the given format is not properly initialized..");
        }
    }

    /**
     * Identify whether the response includes a success response or failure response according to the
     * response status code.
     *
     * @param statusCode
     * @return
     */
    public boolean evaluateResponseStatus(int statusCode) {
        switch (statusCode) {
            //ok
            case ResponseCodeConstants.CODE_OK:
                return true;
            case ResponseCodeConstants.CODE_CREATED:
                return true;

            case ResponseCodeConstants.CODE_NO_CONTENT:
                return true;

            case ResponseCodeConstants.CODE_UNAUTHORIZED:
                return false;

            case ResponseCodeConstants.CODE_FORMAT_NOT_SUPPORTED:
                return false;

            case ResponseCodeConstants.CODE_INTERNAL_SERVER_ERROR:
                return false;

            case ResponseCodeConstants.CODE_RESOURCE_NOT_FOUND:
                return false;

            case ResponseCodeConstants.CODE_BAD_REQUEST:
                return false;

            default:
                return false;
        }
    }
}
