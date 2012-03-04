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

/**
 * This is an abstract layer for all the resource endpoint to abstract out common
 * operations to all resource endpoints.
 */
public abstract class AbstractResourceEndpoint implements ResourceEndpoint {

    /*Keeps a map of supported encoders of SCIM server side.*//*
    private static Map<String, Encoder> encoderMap = new ConcurrentHashMap<String, Encoder>();

    *//*Keeps a map of supported encoders of SCIM server side.*//*
    private static Map<String, Decoder> decoderMap = new ConcurrentHashMap<String, Decoder>();*/

    /**
     * Returns the encoder given the encoding format.
     *
     * @param format
     * @return
     * @throws FormatNotSupportedException
     */
    public Encoder getEncoder(String format)
            throws FormatNotSupportedException, CharonException {
        return null;/*org.wso2.charon.deployment.managers.DefaultCharonManager.;*/
    }

    public Decoder getDecoder(String format)
            throws FormatNotSupportedException, CharonException {

        return null;

    }

    /**
     * Register encoders to be supported by SCIM Server Side.
     *
     * @param format  - format that the registering encoder supports.
     * @param encoder
     */
    /*public static void registerEncoder(String format, Encoder encoder) throws CharonException {
        //TODO:this should be in SCIM Manager
        if (encoderMap.containsKey(format)) {
            //log the error and throw.
            String error = "Encoder for the given format is already registered.";
            throw new CharonException(error);
        } else {
            encoderMap.put(format, encoder);
        }

    }

    public static void registerDecoder(String format, Decoder decoder) throws CharonException {
        //TODO:this should be in SCIM Manager
        if (decoderMap.containsKey(format)) {
            //log the error and throw.
            String error = "Decoder for the given format is already registered.";
            throw new CharonException(error);
        } else {
            decoderMap.put(format, decoder);
        }

    }*/

    /**
     * Build SCIM Response given the response code and response message.
     * @param responseCode
     * @param responseMessage
     * @return
     */
    /*protected SCIMResponse buildResponse(String responseCode, String responseMessage) {
        return new SCIMResponse(responseCode, responseMessage);
    }*/

    /**
     * Obtain the AttributeFactory to be used in current deployment. If a custom implementation is
     * not registered, use the DefaultAttributeFactory.
     *
     * @return
     */
    /*public static AttributeFactory getAttributeFactory() {
        if (attributeFactory != null) {
            return attributeFactory;
        } else {
            attributeFactory = new DefaultAttributeFactory();
            return attributeFactory;
        }
    }*/
}
