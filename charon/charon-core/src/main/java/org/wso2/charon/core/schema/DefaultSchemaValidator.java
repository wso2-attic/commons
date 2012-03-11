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
package org.wso2.charon.core.schema;

import org.wso2.charon.core.attributes.AbstractAttribute;
import org.wso2.charon.core.attributes.Attribute;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.objects.AbstractSCIMObject;

import java.util.List;
import java.util.Map;

/**
 * Contains common functionality to any SCIM object schema validator.
 * TODO:If all required attributes are there, if additional attributes not there
 * according to the schema,and do this after creating the resource by decoder and usermanager
 * and client.
 */
public class DefaultSchemaValidator implements SchemaValidator {

    /**
     * Validate SCIMObject given the object and the corresponding schema.
     *
     * @param scimObject
     * @param resourceSchema
     */
    public static void validateSCIMObject(AbstractSCIMObject scimObject,
                                          ResourceSchema resourceSchema)
            throws CharonException {
        //get attributes from schema.
        List<AttributeSchema> attributeSchemaList = resourceSchema.getAttributesList();
        //get attribute list from scim object.
        Map<String, Attribute> attributeList = scimObject.getAttributeList();
        for (AttributeSchema attributeSchema : attributeSchemaList) {
            if (attributeSchema.getRequired()) {
                if (!attributeList.containsKey(attributeSchema.getName())) {
                    String error = "Required attribute is missing in the SCIM Object.";
                    throw new CharonException(error);
                }
            }
        }
        //check if required attributes are there.
    }

    public static void validateAttribute(AbstractAttribute attribute,
                                         AttributeSchema attributeSchema) {

    }
}
