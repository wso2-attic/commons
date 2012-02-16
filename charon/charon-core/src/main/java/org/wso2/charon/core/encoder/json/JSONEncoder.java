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
package org.wso2.charon.core.encoder.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.json.
import org.wso2.charon.core.attributes.Attribute;
import org.wso2.charon.core.attributes.ComplexAttribute;
import org.wso2.charon.core.attributes.MultiValuedAttribute;
import org.wso2.charon.core.attributes.SimpleAttribute;
import org.wso2.charon.core.encoder.Encoder;
import org.wso2.charon.core.exceptions.AbstractCharonException;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.objects.Common;
import org.wso2.charon.core.protocol.ResponseCodeConstants;
import org.wso2.charon.core.schema.SCIMSchemaConstants;

import java.util.List;
import java.util.Map;

public class JSONEncoder implements Encoder {

    /**
     * Encode the given SCIM object.
     *
     * @param abstractSCIMObject
     * @return the resulting string after encoding.
     */
    public String encodeSCIMObject(Common abstractSCIMObject) throws CharonException {
        //root json object containing the encoded SCIM Object.
        JSONObject rootObject = new JSONObject();
        try {
            //encode schemas
            this.encodeArrayOfValues(SCIMSchemaConstants.SCIMCommonSchemaConstants.SCHEMAS,
                                     (abstractSCIMObject.getSchemaList()).toArray(), rootObject);
            //encode attribute list
            Map<String, Attribute> attributes = abstractSCIMObject.getAttributeList();


            for (Attribute attribute : attributes.values()) {
                if (attribute instanceof SimpleAttribute) {
                    encodeSimpleAttribute((SimpleAttribute) attribute, rootObject);

                } else if (attribute instanceof ComplexAttribute) {
                    encodeComplexAttribute((ComplexAttribute) attribute, rootObject);

                } else if (attribute instanceof MultiValuedAttribute) {
                    encodeMultiValuedAttribute((MultiValuedAttribute) attribute, rootObject);
                }
            }
            
        } catch (JSONException e) {
            String errorMessage = "Error in encoding resource..";
            //log the error
            throw new CharonException(errorMessage);
        }                                       
        return rootObject.toString();
    }

    /**
     * Encode the Exception to be sent in the SCIM - response payload.
     *
     * @param exception
     * @return the resulting string after encoding
     */
    public String encodeSCIMException(AbstractCharonException exception) {
        //outer most json object
        JSONObject rootErrorObject = new JSONObject();
        //if multiple errors present, we send them in an array.
        JSONArray arrayOfErrors = new JSONArray();

        //JSON Object containing the error code and error message
        JSONObject errorObject = new JSONObject();
        try {
            //construct error object with details in the exception
            errorObject.put(ResponseCodeConstants.DESCRIPTION, exception.getDescription());
            errorObject.put(ResponseCodeConstants.CODE, exception.getCode());
            //TODO:for the moment it is expected that an exception only contains one error.
            arrayOfErrors.put(errorObject);
            //construct the full json obj.
            rootErrorObject.put(ResponseCodeConstants.ERRORS, arrayOfErrors);

        } catch (JSONException e) {
            //usually errors occur rarely when encoding exceptions. and no need to pass them to clients.
            //sufficient to log them in server side back end.
            //TODO:log the error
            e.printStackTrace();
        }
        //return json string
        return rootErrorObject.toString();

    }

    protected void encodeArrayOfValues(String arrayName, Object[] arrayValues,
                                       JSONObject rootObject) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Object arrayValue : arrayValues) {
            jsonArray.put(arrayValue);
        }
        rootObject.put(arrayName, jsonArray);
    }

    /**
     * Encode the simple attribute and include it in root json object to be returned.
     *
     * @param attribute
     * @param rootObject
     */
    protected void encodeSimpleAttribute(SimpleAttribute attribute, JSONObject rootObject)
            throws JSONException {
        rootObject.put(attribute.getName(), attribute.getValue());
    }

    /**
     * When an attribute value becomes a simple attribute itself, encode it and put it in json array.
     *
     * @param attributeValue
     * @param jsonArray
     * @throws JSONException
     */
    protected void encodeSimpleAttributeValue(SimpleAttribute attributeValue, JSONArray jsonArray)
            throws JSONException {
        JSONObject attributeValueObject = new JSONObject();
        attributeValueObject.put(attributeValue.getName(), attributeValue.getValue());
        jsonArray.put(attributeValueObject);
    }


    /**
     * Encode the complex attribute and include it in root json object to be returned.
     *
     * @param attribute
     * @param rootObject
     */
    protected void encodeComplexAttribute(ComplexAttribute attribute, JSONObject rootObject)
            throws JSONException {
        JSONObject subObject = new JSONObject();
        Map<String, Attribute> subAttributes = attribute.getSubAttributes();
        for (Attribute attributeValue : subAttributes.values()) {
            if (attributeValue instanceof SimpleAttribute) {
                encodeSimpleAttribute((SimpleAttribute) attributeValue, subObject);

            } else if (attributeValue instanceof ComplexAttribute) {
                encodeComplexAttribute((ComplexAttribute) attributeValue, subObject);

            } else if (attributeValue instanceof MultiValuedAttribute) {
                encodeMultiValuedAttribute((MultiValuedAttribute) attributeValue, subObject);
            }
            rootObject.put(attribute.getName(), subObject);
        }

    }

    /**
     * When an attribute value becomes a complex attribute, use this method to encode it.
     *
     * @param attributeValue
     * @param jsonArray
     */
    protected void encodeComplexAttributeValue(ComplexAttribute attributeValue,
                                               JSONArray jsonArray) throws JSONException {
        JSONObject subObject = new JSONObject();
        Map<String, Attribute> subAttributes = attributeValue.getSubAttributes();
        for (Attribute value : subAttributes.values()) {
            if (value instanceof SimpleAttribute) {
                encodeSimpleAttribute((SimpleAttribute) value, subObject);

            } else if (value instanceof ComplexAttribute) {
                encodeComplexAttribute((ComplexAttribute) value, subObject);

            } else if (value instanceof MultiValuedAttribute) {
                encodeMultiValuedAttribute((MultiValuedAttribute) value, subObject);
            }
            jsonArray.put(subObject);
        }
    }

    /**
     * Encode the simple attribute and include it in root json object to be returned.
     *
     * @param multiValuedAttribute
     * @param jsonObject
     */
    protected void encodeMultiValuedAttribute(MultiValuedAttribute multiValuedAttribute,
                                              JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        List<Attribute> attributeValues = multiValuedAttribute.getValuesAsSubAttributes();
        for (Attribute attributeValue : attributeValues) {
            if (attributeValue instanceof SimpleAttribute) {
                encodeSimpleAttributeValue((SimpleAttribute) attributeValue, jsonArray);

            } else if (attributeValue instanceof ComplexAttribute) {

                encodeComplexAttributeValue((ComplexAttribute) attributeValue, jsonArray);
            }
        }
        jsonObject.put(multiValuedAttribute.getName(), jsonArray);

    }


}
