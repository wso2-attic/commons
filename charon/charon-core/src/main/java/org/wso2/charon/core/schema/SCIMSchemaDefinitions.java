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

    //data types that an attribute can take, according to the SCIM spec.

    public static enum DataType {
        STRING, BOOLEAN, DECIMAL, INTEGER, DATE_TIME, BINARY
    }

    /*sub attribute schemas for the sub attributes defined in SCIM Schema - including the common set
    * of sub attributes in Multi-Valued Attributes.*/


    /**
     * *******************Sub attributes found in common-schema*********************************
     */
    //TODO:add canonical values.
    public static final SCIMSubAttributeSchema TYPE =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.CommonSchemaConstants.TYPE,
                                                                DataType.STRING, SCIMConstants.TYPE_DESC,
                                                                false, false, false, null);
    public static final SCIMSubAttributeSchema PRIMARY =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.CommonSchemaConstants.PRIMARY,
                                                                DataType.BOOLEAN, SCIMConstants.PRIMARY_DESC,
                                                                false, false, false, null);

    public static final SCIMSubAttributeSchema DISPLAY =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.CommonSchemaConstants.DISPLAY,
                                                                DataType.STRING, SCIMConstants.DISPLAY_DESC,
                                                                true, false, false, null);

    public static final SCIMSubAttributeSchema OPERATION =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.CommonSchemaConstants.OPERATION,
                                                                DataType.STRING, SCIMConstants.OPERATION_DESC,
                                                                false, false, false, null);

    public static final SCIMSubAttributeSchema VALUE =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.CommonSchemaConstants.VALUE,
                                                                DataType.STRING, SCIMConstants.VALUE_DESC,
                                                                false, false, false, null);

    // sub attributes of meta and then the meta attribute
    public static final SCIMSubAttributeSchema CREATED =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.CommonSchemaConstants.CREATED,
                                                                SCIMSchemaDefinitions.DataType.DATE_TIME,
                                                                SCIMConstants.CREATED_DESC, true, false, false, null);

    public static final SCIMSubAttributeSchema LAST_MODIFIED =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.CommonSchemaConstants.LAST_MODIFIED,
                                                                SCIMSchemaDefinitions.DataType.DATE_TIME,
                                                                SCIMConstants.LAST_MODIFIED_DESC, true, false, false, null);

    public static final SCIMSubAttributeSchema LOCATION =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.CommonSchemaConstants.LOCATION,
                                                                SCIMSchemaDefinitions.DataType.STRING,
                                                                SCIMConstants.LOCATION_DESC, true, false, false, null);
    public static final SCIMSubAttributeSchema VERSION =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.CommonSchemaConstants.VERSION,
                                                                SCIMSchemaDefinitions.DataType.STRING,
                                                                SCIMConstants.VERSION_DESC, true, false, false, null);
    public static final SCIMSubAttributeSchema ATTRIBUTES =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.CommonSchemaConstants.ATTRIBUTES,
                                                                SCIMSchemaDefinitions.DataType.STRING,
                                                                SCIMConstants.ATTRIBUTES_DESC, false, false, false, null);


    /**
     * *************Sub attribute schemas for the sub attributes defined in user schema**********
     */
    //sub attributes of name
    public static final SCIMSubAttributeSchema FORMATTED =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                "formatted", DataType.STRING,
                                                                SCIMConstants.FORMATTED_NAME_DESC,
                                                                false, false, false, null);

    public static final SCIMSubAttributeSchema FAMILY_NAME =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.UserSchemaConstants.FAMILY_NAME,
                                                                DataType.STRING, SCIMConstants.FAMILY_NAME_DESC,
                                                                false, false, false, null);

    public static final SCIMSubAttributeSchema GIVEN_NAME =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.UserSchemaConstants.GIVEN_NAME,
                                                                DataType.STRING, SCIMConstants.GIVEN_NAME_DESC,
                                                                false, false, false, null);

    public static final SCIMSubAttributeSchema MIDDLE_NAME =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.UserSchemaConstants.MIDDLE_NAME,
                                                                DataType.STRING, SCIMConstants.MIDDLE_NAME_DESC,
                                                                false, false, false, null);

    public static final SCIMSubAttributeSchema HONORIFIC_PREFIX =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.UserSchemaConstants.HONORIFIC_PREFIX,
                                                                DataType.STRING, SCIMConstants.HONORIFIC_PREFIX_DESC,
                                                                false, false, false, null);

    public static final SCIMSubAttributeSchema HONORIFIC_SUFFIX =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.UserSchemaConstants.HONORIFIC_SUFFIX,
                                                                DataType.STRING, SCIMConstants.HONORIFIC_SUFFIX_DESC,
                                                                false, false, false, null);

    //sub attributes of addresses
    public static final SCIMSubAttributeSchema FORMATTED_ADDRESS =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.UserSchemaConstants.FORMATTED_ADDRESS,
                                                                DataType.STRING, SCIMConstants.FORMATTED_ADDRESS_DESC,
                                                                false, false, false, null);
    public static final SCIMSubAttributeSchema STREET_ADDRESS =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.UserSchemaConstants.STREET_ADDRESS,
                                                                DataType.STRING, SCIMConstants.STREET_ADDRESS_DESC,
                                                                false, false, false, null);

    public static final SCIMSubAttributeSchema LOCALITY =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.UserSchemaConstants.LOCALITY,
                                                                DataType.STRING, SCIMConstants.LOCALITY_DESC,
                                                                false, false, false, null);
    public static final SCIMSubAttributeSchema REGION =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.UserSchemaConstants.REGION,
                                                                DataType.STRING, SCIMConstants.REGION_DESC,
                                                                false, false, false, null);
    public static final SCIMSubAttributeSchema POSTAL_CODE =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.UserSchemaConstants.POSTAL_CODE,
                                                                DataType.STRING, SCIMConstants.POSTAL_CODE_DESC,
                                                                false, false, false, null);
    public static final SCIMSubAttributeSchema COUNTRY =
            SCIMSubAttributeSchema.createSCIMSubAttributeSchema(null,
                                                                SCIMConstants.UserSchemaConstants.COUNTRY,
                                                                DataType.STRING, SCIMConstants.COUNTRY_DESC,
                                                                false, false, false, null);

    /**
     * *********SCIM defined attribute schemas***************************
     */


    //attribute schemas of the attributes defined in common schema.

    /*Unique identifier for the SCIM Resource as defined by the Service Provider*/
    public static final SCIMAttributeSchema ID =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.CommonSchemaConstants.ID,
                                                          SCIMSchemaDefinitions.DataType.STRING, false,
                                                          null, SCIMConstants.ID_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, true, true, true, null);

    /*Unique identifier for the Resource as defined by the Service Consumer.The Service Provider
    MUST always interpret the externalId as scoped to the Service Consumer's tenant*/
    public static final SCIMAttributeSchema EXTERNAL_ID =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.CommonSchemaConstants.EXTERNAL_ID,
                                                          SCIMSchemaDefinitions.DataType.STRING, false, null,
                                                          SCIMConstants.EXTERNAL_ID_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false, null);

    /*META - A complex attribute containing resource metadata. All sub-attributes are OPTIONAL*/

    /*Since all sub attributes of META are optional, META attribute is also optional.*/
    public static final SCIMAttributeSchema META =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.CommonSchemaConstants.META,
                                                          null, false, null, SCIMConstants.META_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false,
                                                          CREATED, LAST_MODIFIED, LOCATION, VERSION, ATTRIBUTES);


    //attribute schemas of the attributes defined in user schema.

    /*Unique identifier for the User, typically used by the user to directly authenticate to the service provider.*/
    public static final SCIMAttributeSchema USER_NAME =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.USER_NAME,
                                                          SCIMSchemaDefinitions.DataType.STRING, false, null,
                                                          SCIMConstants.USER_NAME_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, true, false, null);

    /*E-mail addresses for the User. The value SHOULD be canonicalized by the Service Provider*/
    //TODO:how 'work','home' and 'other' specified in emails
    //TODO:NOTE:MULTI-VALUED ATTRIBUTES HAVE SUB ATTRIBUTES - DEFINED IN SCHEMA
    public static final SCIMAttributeSchema EMAILS =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.EMAILS,
                                                          SCIMSchemaDefinitions.DataType.STRING, true,
                                                          SCIMConstants.UserSchemaConstants.EMAIL,
                                                          SCIMConstants.EMAILS_DESC, SCIMConstants.CORE_SCHEMA_URI,
                                                          false, false, false, null);

    public static final SCIMAttributeSchema USER_DISPLAY_NAME =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.DISPLAY_NAME,
                                                          SCIMSchemaDefinitions.DataType.STRING, false, null,
                                                          SCIMConstants.USER_DISPLAY_NAME_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false,
                                                          false, false, null);

    public static final SCIMAttributeSchema NAME =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.NAME,
                                                          DataType.STRING, false, null,
                                                          SCIMConstants.NAME_DESC, SCIMConstants.CORE_SCHEMA_URI,
                                                          false, false, false,
                                                          FORMATTED, FAMILY_NAME, GIVEN_NAME, MIDDLE_NAME,
                                                          HONORIFIC_PREFIX, HONORIFIC_SUFFIX);
    public static final SCIMAttributeSchema NICK_NAME =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.NICK_NAME,
                                                          DataType.STRING, false, null, SCIMConstants.NICK_NAME_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false, null);
    public static final SCIMAttributeSchema PROFILE_URL =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.PROFILE_URL,
                                                          DataType.STRING, false, null, SCIMConstants.PROFILE_URL_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false, null);

    public static final SCIMAttributeSchema TITLE =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.TITLE,
                                                          DataType.STRING, false, null, SCIMConstants.TITLE_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false, null);

    public static final SCIMAttributeSchema USER_TYPE =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.USER_TYPE,
                                                          DataType.STRING, false, null, SCIMConstants.USER_TYPE_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false, null);

    public static final SCIMAttributeSchema PREFERRED_LANGUAGE =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.PREFERRED_LANGUAGE,
                                                          DataType.STRING, false, null,
                                                          SCIMConstants.PREFERRED_LANGUAGE_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false, null);
    public static final SCIMAttributeSchema LOCALE =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.LOCALE,
                                                          DataType.STRING, false, null, SCIMConstants.LOCALE_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false, null);
    public static final SCIMAttributeSchema TIMEZONE =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.TIME_ZONE,
                                                          DataType.STRING, false, null, SCIMConstants.TIME_ZONE_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false, null);

    public static final SCIMAttributeSchema ACTIVE =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.ACTIVE,
                                                          DataType.BOOLEAN, false, null, SCIMConstants.ACTIVE_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false, null);

    public static final SCIMAttributeSchema PASSWORD =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.PASSWORD,
                                                          DataType.STRING, false, null, SCIMConstants.PASSWORD_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false, null);

    public static final SCIMAttributeSchema PHONE_NUMBERS =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.PHONE_NUMBERS,
                                                          DataType.STRING, true,
                                                          SCIMConstants.UserSchemaConstants.PHONE_NUMBER,
                                                          SCIMConstants.PHONE_NUMBERS_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, null);

    public static final SCIMAttributeSchema IMS =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.IMS, DataType.STRING,
                                                          true, SCIMConstants.UserSchemaConstants.IM,
                                                          SCIMConstants.IMS_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false,
                                                          null);

    public static final SCIMAttributeSchema PHOTOS =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.PHOTOS,
                                                          DataType.STRING, true,
                                                          SCIMConstants.UserSchemaConstants.PHOTO,
                                                          SCIMConstants.PHOTOS_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, null);


    public static final SCIMAttributeSchema GROUPS =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.GROUPS,
                                                          DataType.STRING, true,
                                                          SCIMConstants.UserSchemaConstants.GROUP,
                                                          SCIMConstants.USER_GROUP_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, true, false, null);

    public static final SCIMAttributeSchema ADDRESSES =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.ADDRESSES,
                                                          DataType.STRING, true,
                                                          SCIMConstants.UserSchemaConstants.ADDRESS,
                                                          SCIMConstants.ADDRESSES_DESC, SCIMConstants.CORE_SCHEMA_URI,
                                                          false, false, false, FORMATTED_ADDRESS, STREET_ADDRESS,
                                                          LOCALITY, REGION, POSTAL_CODE, COUNTRY);
    public static final SCIMAttributeSchema ENTITLEMENTS =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.ENTITLEMENTS,
                                                          DataType.STRING, true, SCIMConstants.UserSchemaConstants.ENTITLEMENT,
                                                          SCIMConstants.ENTITLEMENTS_DESC, SCIMConstants.CORE_SCHEMA_URI,
                                                          false, false, false, null);
    public static final SCIMAttributeSchema ROLES =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.ROLES, DataType.STRING,
                                                          true, SCIMConstants.UserSchemaConstants.ROLE, SCIMConstants.ROLES_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false, null);
    public static final SCIMAttributeSchema X509CERTIFICATES =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.UserSchemaConstants.X509CERTIFICATES,
                                                          DataType.BINARY, true,
                                                          SCIMConstants.UserSchemaConstants.X509CERTIFICATE,
                                                          SCIMConstants.X509CERTIFICATES_DESC,
                                                          SCIMConstants.CORE_SCHEMA_URI, false, false, false, null);


    //attribute schemas of the attributes defined in group schema.
    public static final SCIMAttributeSchema DISPLAY_NAME =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.GroupSchemaConstants.DISPLAY_NAME,
                                                          SCIMSchemaDefinitions.DataType.STRING, false, null,
                                                          SCIMConstants.DISPLAY_NAME_DESC, SCIMConstants.CORE_SCHEMA_URI,
                                                          false, false, false, null);
    public static final SCIMAttributeSchema MEMBERS =
            SCIMAttributeSchema.createSCIMAttributeSchema(null,
                                                          SCIMConstants.GroupSchemaConstants.MEMBERS,
                                                          SCIMSchemaDefinitions.DataType.STRING, true,
                                                          SCIMConstants.GroupSchemaConstants.MEMBER,
                                                          SCIMConstants.MEMBERS_DESC, SCIMConstants.CORE_SCHEMA_URI,
                                                          false, true, false, null);

    /**
     * *************************Attributes defined in Enterprise User Schema***********************
     */
    //TODO


    //schemas of the resources as defined in SCIM Schema spec.


    /**
     * **********SCIM defined Resource Schemas****************************
     */
    public static final SCIMResourceSchema SCIM_COMMON_SCHEMA = SCIMResourceSchema.createSCIMResourceSchema(
            SCIMConstants.COMMON, SCIMConstants.CORE_SCHEMA_URI, SCIMConstants.COMMON_DESC, null,
            SCIMSchemaDefinitions.ID, SCIMSchemaDefinitions.EXTERNAL_ID, SCIMSchemaDefinitions.META);


    public static final SCIMResourceSchema SCIM_USER_SCHEMA =
            SCIMResourceSchema.createSCIMResourceSchema(SCIMConstants.USER, SCIMConstants.CORE_SCHEMA_URI,
                                                        SCIMConstants.USER_DESC, SCIMConstants.USER_ENDPOINT,
                                                        USER_NAME, NAME, DISPLAY_NAME, NICK_NAME, PROFILE_URL,
                                                        TITLE, USER_TYPE, PREFERRED_LANGUAGE, LOCALE,
                                                        TIMEZONE, ACTIVE, PASSWORD, EMAILS, PHONE_NUMBERS, IMS,
                                                        PHOTOS, ADDRESSES, GROUPS, ENTITLEMENTS, ROLES,
                                                        X509CERTIFICATES);


    public static final SCIMResourceSchema SCIM_GROUP_SCHEMA =
            SCIMResourceSchema.createSCIMResourceSchema(SCIMConstants.GROUP, SCIMConstants.CORE_SCHEMA_URI,
                                                        SCIMConstants.GROUP_DESC, SCIMConstants.GROUP_ENDPOINT,
                                                        DISPLAY_NAME, MEMBERS);


    /***********************Custom Defined Schemas for returning listed resources******************/
    //public static final 

    //TODO: think of a way to include canonical types included in SCIM spec for multi-valued attributes.
    //when constructing the resource schema
}
