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

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the schema definitions in
 * http://www.simplecloud.info/specs/draft-scim-core-schema-00.html as ResourceSchemas and AttributeSchemas.
 * These are used when constructing SCIMObjects from the decoded payload
 */
public class SCIMSchemaDefinitions {

    //data types that an attribute can take, according to the SCIM spec.

    public static enum DataType {
        STRING, BOOLEAN, DECIMAL, INTEGER, DATE_TIME, BINARY
    }

    /*sub attribute schemas for the sub attributes defined in SCIM Schema - including the common set
    * of sub attributes in Multi-Valued Attributes.*/

    //TODO:add canonical values.
    public static final SCIMSubAttributeSchema TYPE =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(SCIMConstants.CommonSchemaConstants.TYPE,
                                                                DataType.STRING, SCIMConstants.TYPE_DESC,
                                                                false, false, false, null);
    public static final SCIMSubAttributeSchema PRIMARY =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(SCIMConstants.CommonSchemaConstants.PRIMARY,
                                                                DataType.BOOLEAN, SCIMConstants.PRIMARY_DESC,
                                                                false, false, false, null);

    public static final SCIMSubAttributeSchema DISPLAY =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(SCIMConstants.CommonSchemaConstants.DISPLAY,
                                                                DataType.STRING, SCIMConstants.DISPLAY_DESC,
                                                                true, false, false, null);

    public static final SCIMSubAttributeSchema OPERATION =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(SCIMConstants.CommonSchemaConstants.OPERATION,
                                                                DataType.STRING, SCIMConstants.OPERATION_DESC,
                                                                false, false, false, null);

    public static final SCIMSubAttributeSchema VALUE =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(SCIMConstants.CommonSchemaConstants.VALUE,
                                                                DataType.STRING, SCIMConstants.VALUE_DESC,
                                                                false, false, false, null);

    /**
     * *********SCIM defined attribute schemas***************************
     */

    //attribute schemas of the attributes defined in common schema.

    /*Unique identifier for the SCIM Resource as defined by the Service Provider*/
    public static final AttributeSchema ID =
            SCIMAttributeSchema.createSCIMAttributeSchema(SCIMConstants.CommonSchemaConstants.ID,
                                                          SCIMSchemaDefinitions.DataType.STRING, false,
                                                          null, SCIMConstants.ID_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, true, true, true, null);

    /*Unique identifier for the Resource as defined by the Service Consumer.The Service Provider
    MUST always interpret the externalId as scoped to the Service Consumer's tenant*/
    public static final AttributeSchema EXTERNAL_ID =
            SCIMAttributeSchema.createSCIMAttributeSchema(SCIMConstants.CommonSchemaConstants.EXTERNAL_ID,
                                                          SCIMSchemaDefinitions.DataType.STRING, false, null,
                                                          SCIMConstants.EXTERNAL_ID_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false, null);


    //attribute schemas of the attributes defined in user schema.

    /*Unique identifier for the User, typically used by the user to directly authenticate to the service provider.*/
    public static final AttributeSchema USER_NAME =
            SCIMAttributeSchema.createSCIMAttributeSchema(SCIMConstants.UserSchemaConstants.USER_NAME,
                                                          SCIMSchemaDefinitions.DataType.STRING, false, null,
                                                          SCIMConstants.USER_NAME_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, true, false, null);

    /*E-mail addresses for the User. The value SHOULD be canonicalized by the Service Provider*/
    //TODO:how 'work','home' and 'other' specified in emails
    //TODO:NOTE:MULTI-VALUED ATTRIBUTES HAVE SUB ATTRIBUTES - DEFINED IN SCHEMA
    public static final AttributeSchema EMAILS =
            SCIMAttributeSchema.createSCIMAttributeSchema(SCIMConstants.UserSchemaConstants.EMAILS,
                                                          SCIMSchemaDefinitions.DataType.STRING, true,
                                                          SCIMConstants.UserSchemaConstants.EMAIL,
                                                          SCIMConstants.EMAILS_DESC, SCIMConstants.CORE_SCHEMA_URI,
                                                          false, false, false, null);


    //sub attribute schemas of the attributes defined in User Schema,

    //schemas of the resources as defined in SCIM Schema spec.

    /**
     * **********SCIM defined Resource Schemas****************************
     */
    public static final SCIMResourceSchema SCIM_COMMON_SCHEMA = SCIMResourceSchema.createSCIMResourceSchema(
            SCIMConstants.COMMON, SCIMConstants.CORE_SCHEMA_URI, SCIMConstants.COMMON_DESC, null,
            SCIMSchemaDefinitions.ID, SCIMSchemaDefinitions.EXTERNAL_ID);

    public static final SCIMResourceSchema SCIM_USER_SCHEMA =
            SCIMResourceSchema.createSCIMResourceSchema(SCIMConstants.USER, SCIMConstants.CORE_SCHEMA_URI,
                                                        SCIMConstants.USER_DESC, SCIMConstants.USER_ENDPOINT,
                                                        SCIMSchemaDefinitions.USER_NAME, SCIMSchemaDefinitions.EMAILS);


}
