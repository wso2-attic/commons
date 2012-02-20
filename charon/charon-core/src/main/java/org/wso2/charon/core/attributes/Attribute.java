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

/**
 * Interface to represent Attribute defined in SCIM schema spec.
 */
public interface Attribute {

    /**
     * Get the name of the attribute.
     *
     * @return Name of the attribute.
     */
    public String getName();

    /**
     * Get the attribute value.
     * @return Value of the attribute.
     */
    //public Object getValue();

    /**
     * Update the attribute with given value.
     * @param Value New value to be updated.
     */
    //public void updateValue(Object Value);

    /**
     * Get the schema where the attribute is defined.
     */
    public String getSchema();

    /**
     * Set the schema of the attribute.
     *
     * @param schema
     */
    public void setSchema(String schema);

    /**
     * Validate whether the attribute adheres to the SCIM schema.
     *
     * @param attribute
     * @return
     */
    public boolean validate(Attribute attribute);

}
