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
import org.wso2.charon.core.objects.AbstractSCIMObject;

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
    @Override
    public void validateSCIMObject(AbstractSCIMObject scimObject, ResourceSchema resourceSchema) {
        //get attributes from schema.
        //get attribute list from scim object.
        //compare everything fine.
        //To change body of implemented methods use File | Settings | File Templates.
        //check if required attributes are there.
    }

    public void validateAttribute(AbstractAttribute attribute, AttributeSchema attributeSchema) {
        
    }
}
