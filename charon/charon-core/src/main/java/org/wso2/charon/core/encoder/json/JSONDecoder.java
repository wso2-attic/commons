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
import org.json.JSONTokener;
import org.wso2.charon.core.attributes.Attribute;
import org.wso2.charon.core.attributes.ComplexAttribute;
import org.wso2.charon.core.attributes.DefaultAttributeFactory;
import org.wso2.charon.core.attributes.MultiValuedAttribute;
import org.wso2.charon.core.attributes.SimpleAttribute;
import org.wso2.charon.core.encoder.Decoder;
import org.wso2.charon.core.exceptions.AbstractCharonException;
import org.wso2.charon.core.exceptions.BadRequestException;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.objects.AbstractSCIMObject;
import org.wso2.charon.core.objects.DefaultResourceFactory;
import org.wso2.charon.core.objects.SCIMObject;
import org.wso2.charon.core.protocol.ResponseCodeConstants;
import org.wso2.charon.core.schema.AttributeSchema;
import org.wso2.charon.core.schema.ResourceSchema;
import org.wso2.charon.core.schema.SCIMAttributeSchema;
import org.wso2.charon.core.schema.SCIMSchemaDefinitions;
import org.wso2.charon.core.schema.SCIMSubAttributeSchema;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONDecoder implements Decoder {


    /**
     * Decode the resource string sent in the SCIM request/response payload.
     *
     * @param scimResourceString
     * @param resourceSchema
     * @param scimObject         @return
     */
    public SCIMObject decodeResource(String scimResourceString,
                                     ResourceSchema resourceSchema, AbstractSCIMObject scimObject)
            throws BadRequestException, CharonException {
        try {
            //decode the string into json representation
            JSONObject decodedJsonObj = new JSONObject(new JSONTokener(scimResourceString));

            //get the attribute schemas list from the schema that defines the given resource
            List<AttributeSchema> attributeSchemas = resourceSchema.getAttributesList();
            //iterate through the schema and extract the attributes.
            for (AttributeSchema attributeSchema : attributeSchemas) {

                Object attributeValObj = decodedJsonObj.opt(attributeSchema.getName());

                if (attributeValObj instanceof String) {
                    //if the corresponding json value object is String, it is a SimpleAttribute.
                    scimObject.setAttribute(buildSimpleAttribute(attributeSchema, attributeValObj));
                    /*DefaultResourceFactory.setAttribute(scimObject, buildSimpleAttribute
                            (attributeSchema, attributeValObj));*/

                } else if (attributeValObj instanceof JSONArray) {
                    //if the corresponding json value object is JSONArray, it is a MultiValuedAttribute.
                    scimObject.setAttribute(
                            buildMultiValuedAttribute(attributeSchema, (JSONArray) attributeValObj));
                    /*DefaultResourceFactory.setAttribute(
                            scimObject, buildMultiValuedAttribute(attributeSchema, (JSONArray) attributeValObj));*/

                } else if (attributeValObj instanceof JSONObject) {
                    //if the corresponding json value object is JSONObject, it is a ComplexAttribute.
                    scimObject.setAttribute(buildComplexAttribute(attributeSchema,
                                                                  (JSONObject) attributeValObj));
                    /*DefaultResourceFactory.setAttribute(
                            scimObject, buildComplexAttribute(attributeSchema, (JSONObject) attributeValObj));*/
                }
            }
            //return DefaultResourceFactory.createSCIMObject(resourceSchema, scimObject);
            return scimObject;

        } catch (JSONException e) {
            //log error
            String error = "JSON string could not be decoded properly.";
            throw new BadRequestException();
        } catch (CharonException e) {
            //log error
            String error = "Error in building resource from the JSON representation";
            throw new CharonException(error);
        }
    }

    /**
     * Decode the string sent in the SCIM response payload, which is an exception.
     * JSON encoded exception is usually like:
     * {
     * "Errors":[
     * {
     * "description":"Resource 2819c223-7f76-453a-919d-413861904646 not found",
     * "code":"404"
     * }
     * ]
     * }
     *
     * @param scimExceptionString
     * @return
     */
    public AbstractCharonException decodeException(String scimExceptionString)
            throws CharonException {
        //decode the string into json representation
        try {
            JSONObject decodedJsonObj = new JSONObject(new JSONTokener(scimExceptionString));
            Object jsonError = decodedJsonObj.opt(ResponseCodeConstants.ERRORS);
            List<JSONObject> jsonErrorValues = new ArrayList<JSONObject>();
            //according to the SCIM spec, Error details returned as multivalued attribute.
            //so we assume the same
            if (jsonError instanceof JSONArray) {
                //according to the spec, ERRORS are composed as complex attributes with
                //"description" & "code" as the sub attributes. Since we return only one exception,
                //only the first error is read.
                JSONObject errorObject = (JSONObject) ((JSONArray) jsonError).get(0);
                //decode the details of the error
                String errorCode = (String) errorObject.opt(ResponseCodeConstants.CODE);
                String errorDescription = (String) errorObject.opt(ResponseCodeConstants.DESCRIPTION);
                return new AbstractCharonException(Integer.parseInt(errorCode), errorDescription);
            }
        } catch (JSONException e) {
            //log error
            String error = "Error in building exception from the JSON representation";
            throw new CharonException(error);
        }
        return null;
    }

    /**
     * Decode the attribute, given that it is identified as a Simple Attribute.
     *
     * @param attributeSchema
     * @param attributeValue
     * @return
     * @throws CharonException
     */
    private SimpleAttribute buildSimpleAttribute(AttributeSchema attributeSchema,
                                                 Object attributeValue) throws CharonException {
        //if expecting to decode an attribute of date time type, parse it to Date type.
        if (attributeSchema.getType() == SCIMSchemaDefinitions.DataType.DATE_TIME) {
            attributeValue = parseDateTime((String) attributeValue);
        }
        SimpleAttribute simpleAttribute = new SimpleAttribute(attributeSchema.getName(), attributeValue);
//      simpleAttribute.setValue(attributeValue);
        return (SimpleAttribute) DefaultAttributeFactory.createAttribute(attributeSchema,
                                                                         simpleAttribute);
    }

    /**
     * Decode the attribute, given that it is identified as a MultiValued Attribute.
     *
     * @param attributeSchema
     * @param attributeValues
     * @return
     * @throws CharonException
     */
    private MultiValuedAttribute buildMultiValuedAttribute(AttributeSchema attributeSchema,
                                                           JSONArray attributeValues)
            throws CharonException {
        try {
            if (attributeValues.get(0) instanceof String) {
                //attribute is a simple multi-valued attribute. we assume the type of values of the
                //multivalued attribute is String.
                return buildSimpleMultiValuedAttribute(attributeSchema, attributeValues);
            } else if (attributeValues.get(0) instanceof JSONObject) {
                //attribute is a complex multi-valued attribute
                return buildComplexMultiValuedAttribute(attributeSchema, attributeValues);
            } else {
                //TODO:log the error.
                String error = "Unknown JSON representation for the MultiValued attribute Value..";
                throw new CharonException(error);
            }
        } catch (JSONException e) {
            //TODO:log the error
            String error = "Error in accessing JSON value of multivalues attribute";
            throw new CharonException(error);
        }
    }

    /**
     * Decode the attribute, given that it is identified as a simple multi valued attribute.
     *
     * @param attributeSchema
     * @param attributeValues
     * @return
     */
    private MultiValuedAttribute buildSimpleMultiValuedAttribute(AttributeSchema attributeSchema,
                                                                 JSONArray attributeValues)
            throws CharonException {
        try {
            MultiValuedAttribute multiValuedAttribute = new MultiValuedAttribute(attributeSchema.getName());
            List<String> simpleAttributeValues = new ArrayList<String>();

            //iterate through JSONArray and create the list of string values.
            for (int i = 0; i < attributeValues.length(); i++) {
                simpleAttributeValues.add((String) attributeValues.get(i));
            }
            multiValuedAttribute.setValuesAsStrings(simpleAttributeValues);

            return (MultiValuedAttribute) DefaultAttributeFactory.createAttribute(attributeSchema,
                                                                                  multiValuedAttribute);
        } catch (JSONException e) {
            String error = "Error in accessing the value of multivalued attribute.";
            //log the error
            throw new CharonException(error);
        }

    }

    /**
     * Decode the attribute, given that it is identified as a complex multi-valued attribute.
     *
     * @param attributeSchema
     * @param attributeValues
     * @return
     */
    private MultiValuedAttribute buildComplexMultiValuedAttribute(AttributeSchema attributeSchema,
                                                                  JSONArray attributeValues)
            throws CharonException {
        try {
            MultiValuedAttribute multiValuedAttribute = new MultiValuedAttribute(attributeSchema.getName());
            List<Attribute> complexAttributeValues = new ArrayList<Attribute>();
            //iterate through JSONArray and create the list of values as complex attributes..
            for (int i = 0; i < attributeValues.length(); i++) {
                JSONObject complexAttributeValue = (JSONObject) attributeValues.get(i);
                complexAttributeValues.add(buildComplexValue(attributeSchema, complexAttributeValue));
            }
            //set values as complex attributes
            multiValuedAttribute.setValuesAsSubAttributes(complexAttributeValues);
            //canonicalize before storing.
            //see primary is not set twice.
            return (MultiValuedAttribute) DefaultAttributeFactory.createAttribute(attributeSchema,
                                                                                  multiValuedAttribute);
        } catch (JSONException e) {
            String error = "Error in accessing the value of multivalued attribute.";
            //log the error
            throw new CharonException(error);
        }
    }

    /**
     * Decode the attribute, given that it is identified as a complex attribute.
     *
     * @param attributeSchema
     * @param jsonObject
     * @return
     */
    private ComplexAttribute buildComplexAttribute(AttributeSchema attributeSchema,
                                                   JSONObject jsonObject) throws CharonException {

        ComplexAttribute complexAttribute = new ComplexAttribute(attributeSchema.getName());
        //Map<String, Attribute> subAttributesMap = new HashMap<String, Attribute>();
        List<SCIMSubAttributeSchema> subAttributeSchemas =
                ((SCIMAttributeSchema) attributeSchema).getSubAttributes();

        for (SCIMSubAttributeSchema subAttributeSchema : subAttributeSchemas) {
            Object subAttributeValue = jsonObject.opt(subAttributeSchema.getName());
            if (subAttributeValue instanceof String) {
                SimpleAttribute simpleAttribute =
                        buildSimpleAttribute(subAttributeSchema, subAttributeValue);
                //let the attribute factory to set the sub attribute of a complex attribute to detect schema violations.
                //DefaultAttributeFactory.setSubAttribute(complexAttribute, simpleAttribute);
                //subAttributesMap.put(subAttributeSchema.getName(), simpleAttribute);
            } else if (subAttributeValue instanceof JSONArray) {
                //there can be sub attributes which are multivalued: such as: Meta->attributes
/*
                DefaultAttributeFactory.setSubAttribute(
                        complexAttribute, buildMultiValuedAttribute(subAttributeSchema,
                                                                    (JSONArray) subAttributeValue));
*/
            }
        }
        //complexAttribute.setSubAttributes(subAttributesMap);
        return (ComplexAttribute) DefaultAttributeFactory.createAttribute(attributeSchema,
                                                                          complexAttribute);
    }

    /**
     * To build a complex type value of a Multi Valued Attribute.
     *
     * @param attributeSchema
     * @param jsonObject
     * @return
     */
    private ComplexAttribute buildComplexValue(AttributeSchema attributeSchema,
                                               JSONObject jsonObject) throws CharonException {
        ComplexAttribute complexAttribute = new ComplexAttribute();
        //Map<String, Attribute> subAttributesMap = new HashMap<String, Attribute>();
        List<SCIMSubAttributeSchema> subAttributeSchemas =
                ((SCIMAttributeSchema) attributeSchema).getSubAttributes();

        for (SCIMSubAttributeSchema subAttributeSchema : subAttributeSchemas) {

            Object subAttributeValue = jsonObject.opt(subAttributeSchema.getName());
            if (subAttributeValue instanceof String) {
                SimpleAttribute simpleAttribute =
                        buildSimpleAttribute(subAttributeSchema, subAttributeValue);
                //let the attribute factory to set the sub attribute of a complex attribute to detect schema violations.
                DefaultAttributeFactory.setSubAttribute(complexAttribute, simpleAttribute);
                //subAttributesMap.put(subAttributeSchema.getName(), simpleAttribute);
            } else if (subAttributeValue instanceof JSONArray) {
                //there can be sub attributes which are multivalued: such as: Meta->attributes
                DefaultAttributeFactory.setSubAttribute(
                        complexAttribute, buildMultiValuedAttribute(subAttributeSchema,
                                                                    (JSONArray) subAttributeValue));
            }
        }
        //complexAttribute.setSubAttributes(subAttributesMap);
        return (ComplexAttribute) DefaultAttributeFactory.createAttribute(attributeSchema,
                                                                          complexAttribute);
    }

    /**
     * SCIM spec requires date time to be adhered to XML Schema Datatypes Specification
     *
     * @param dateTimeString
     * @return
     * @throws ParseException
     */
    private Date parseDateTime(String dateTimeString) throws CharonException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            return sdf.parse(dateTimeString);
        } catch (ParseException e) {
            throw new CharonException("Error in parsing date time. " +
                                      "Date time should adhere to the format: yyyy-MM-dd'T'HH:mm:ss");
        }
    }
}
