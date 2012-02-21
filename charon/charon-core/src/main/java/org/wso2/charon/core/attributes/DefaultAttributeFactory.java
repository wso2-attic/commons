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
package org.wso2.charon.core.attributes;

import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.schema.AttributeSchema;
import org.wso2.charon.core.schema.SCIMAttributeSchema;
import org.wso2.charon.core.schema.SCIMSubAttributeSchema;

/**
 * Default implementation of AttributeFactory according to SCIM Schema spec V1.
 */
public class DefaultAttributeFactory /*implements AttributeFactory*/ {

    public Attribute createSimpleAttribute(String attributeId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Attribute createComplexAttribute(String attributeId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Attribute createMultiValuedAttribute(String attributeId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Create the attribute given the attribute schema and the attribute object - may be with
     * attribute value set.
     *
     * @param attributeSchema
     * @param attribute
     * @return
     */
    public static Attribute createAttribute(AttributeSchema attributeSchema, Attribute attribute)
            throws CharonException {
        //Default attribute factory knows about SCIMAttribute schema
        if (attributeSchema instanceof SCIMAttributeSchema) {
            return createSCIMAttribute((SCIMAttributeSchema) attributeSchema, attribute);
        } else if (attributeSchema instanceof SCIMSubAttributeSchema) {
            return createSCIMSubAttribute((SCIMSubAttributeSchema) attributeSchema, attribute);
        }
        String error = "Unknown attribute schema..";
        //log error
        throw new CharonException(error);
    }

    public static Attribute createSCIMAttribute(SCIMAttributeSchema attributeSchema,
                                                Attribute attribute) {
        //things like set the attribute properties according to the schema
        //if multivalued, check if it is simple-multivalued or complex multivalued..
        //if complex-multi-valued, ignore the names of complex attributes. Consider only the names of
        //sub attributes of the complex attribute.
        return attribute;
    }

    public static Attribute createSCIMSubAttribute(SCIMSubAttributeSchema attributeSchema,
                                                   Attribute attribute) {
        return null;
    }
}
