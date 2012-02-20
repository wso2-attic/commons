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
package org.wso2.charon.core.objects;

import org.wso2.charon.core.schema.AttributeSchema;
import org.wso2.charon.core.schema.ResourceSchema;

/**
 * This resource factory class is to handle factory aspect of constructing  a resource given a
 * SCIMObject and the corresponding resource schema.
 */
public class DefaultResourceFactory {

    public static SCIMObject createSCIMObject(ResourceSchema resourceSchema,
                                              SCIMObject scimObject) {
        //perform and actions related to constructing the SCIMObject
        //TODO: Validate the constructed SCIMObject against the schema
        //for the moment return the SCIMObject
        return scimObject;
    }

}
