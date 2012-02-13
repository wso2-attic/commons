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
import org.wso2.charon.core.exceptions.NotFoundException;

import java.util.Map;


/**
 * This constructs a complex attribute as defined in SCIM Core schema.
 */
public class ComplexAttribute extends AbstractAttribute {

    /*If it is a complex attribute, has a list of sub attributes.*/
    protected Map<String, Attribute> subAttributes;

    /**
     * Retrieve the map of sub attributes.
     *
     * @return
     */
    public Map<String, Attribute> getSubAttributes() {
        return subAttributes;
    }

    /**
     * Set the map of sub attributes.
     *
     * @param subAttributes
     */
    public void setSubAttributes(Map<String, Attribute> subAttributes) {
        this.subAttributes = subAttributes;
    }

    /**
     * Retrieve one attribute given the attribute name.
     *
     * @param attributeName
     * @return
     */
    public Attribute getSubAttribute(String attributeName) throws NotFoundException {
        if (subAttributes.containsKey(attributeName)) {
            return subAttributes.get(attributeName);
        } else {
            throw new NotFoundException();
        }
    }

    /**
     * Set a sub attribute on the complex attribute.
     *
     * @param subAttribute
     * @throws CharonException
     */
    public void setSubAttribute(Attribute subAttribute) throws CharonException {
        if (subAttributes.containsKey(subAttribute.getName())) {
            String errorMessage = "Attribute with the same attribute name already exist in the system";
            throw new CharonException(errorMessage);
        } else {
            subAttributes.put(subAttribute.getName(), subAttribute);
        }
    }

    public boolean isSubAttributeExist(String attributeName) {
        return subAttributes.containsKey(attributeName);
    }

    /**
     * Get the attribute value. This abstract method should be implemented in respective attribute
     * types.
     *
     * @return Value of the attribute.
     */
/*
    @Override
    public Object getValue() {
        //this is not implemented for complex attributes.
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
*/

    /**
     * Update the attribute with given value.
     *
     * @param value New value to be updated. This abstract method should be implemented in
     *              respective attribute types.
     */
/*
    @Override
    public void updateValue(Object value) {
        //this is not implemented for complex attributes.
    }
*/

    /**
     * Construct complex attribute with attribute name.
     *
     * @param attributeName
     */
    public ComplexAttribute(String attributeName) {
        super(attributeName);
    }

    /**
     * Construct complex attribute with attribute name and attribute schema.
     *
     * @param attributeName
     * @param attributeSchema
     */
    public ComplexAttribute(String attributeName, String attributeSchema) {
        super(attributeName, attributeSchema);
    }

    /**
     * Create attribute with given name, schema name,whether it is readOnly and optional.
     *
     * @param attributeName Name of the attribute
     * @param schema        schema in which the attribute is defined
     * @param readOnly      whether attribute is readOnly
     * @param optional      whether attribute is optional
     */
    public ComplexAttribute(String attributeName, String schema, boolean readOnly,
                            Map<String, Attribute> subAttributeMap,
                            boolean optional) {
        super(attributeName, schema, readOnly, optional);
        subAttributes = subAttributeMap;
    }

    /**
     * Construct complex attribute with attribute name, attribute schema and sub attribute list.
     *
     * @param attributeName
     * @param attributeSchema
     * @param subAttributeMap
     */
    public ComplexAttribute(String attributeName, String attributeSchema,
                            Map<String, Attribute> subAttributeMap) {
        super(attributeName, attributeSchema);
        this.subAttributes = subAttributeMap;
    }

    /**
     * Validate whether the attribute adheres to the SCIM schema.
     *
     * @param attribute
     * @return
     */
    public boolean validate(Attribute attribute) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
