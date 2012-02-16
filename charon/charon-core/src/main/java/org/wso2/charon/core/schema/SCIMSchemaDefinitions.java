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

/**
 * This class contains the schema definitions in
 * http://www.simplecloud.info/specs/draft-scim-core-schema-00.html as ResourceSchemas and AttributeSchemas.
 * These are used when constructing SCIMObjects from the decoded payload
 */
public class SCIMSchemaDefinitions {
    /**
     * **********SCIM defined Resource Schemas****************************
     */
    public static final SCIMResourceSchema SCIM_COMMON_SCHEMA = new SCIMResourceSchema(
            SCIMConstants.COMMON, SCIMConstants.CORE_SCHEMA_URI, SCIMConstants.COMMON_DESC, null,
            SCIMSchemaDefinitions.ID, SCIMSchemaDefinitions.EXTRNAL_ID);

    public static final SCIMResourceSchema SCIM_USER_SCHEMA =
            new SCIMResourceSchema(SCIMConstants.COMMON, SCIMConstants.CORE_SCHEMA_URI,
                                   SCIMConstants.USER_DESC, null, SCIMSchemaDefinitions.USER_NAME,
                                   SCIMSchemaDefinitions.EMAILS);
    /**
     * *********SCIM defined attribute schemas***************************
     */

    //attribute schemas of the attributes defined in common schema.
    public static final AttributeSchema ID = new SCIMAttributeSchema();

    public static final AttributeSchema EXTRNAL_ID = new SCIMAttributeSchema();


    //attribute schemas of the attributes defined in user schema.
    public static final AttributeSchema USER_NAME = new SCIMAttributeSchema();

    public static final AttributeSchema EMAILS = new SCIMAttributeSchema();
}
