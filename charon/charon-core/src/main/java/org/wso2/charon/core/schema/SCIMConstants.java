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
 * Place to define the constants found in SCIM-Core schema.
 */
public class SCIMConstants {

    public static final String CORE_SCHEMA_URI = "urn:scim:schemas:core:1.0";

    /*Data formats*/
    public static final String JSON = "json";
    public static final String XML = "xml";

    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_XMLA = "application/xml";

    public static String identifyFormat(String format) {
        if (format.equals("application/json")) {
            format = SCIMConstants.JSON;
        } else if (format.equals("application/xml")) {
            format = SCIMConstants.XML;
        }
        return format;
    }

    /*Constants found in core-common schema.*/

    public static class CommonSchemaConstants {
        public static final String ID = "id";
        public static final String EXTERNAL_ID = "externalId";
        public static final String META = "meta";
        public static final String CREATED = "created";
        public static final String LAST_MODIFIED = "lastModified";
        public static final String LOCATION = "location";
        public static final String VERSION = "version";
        public static final String ATTRIBUTES = "attributes";
        public static final String SCHEMAS = "schemas";

        //characteristics of multi valued attribute
        public static final String TYPE = "type";
        public static final String PRIMARY = "primary";

        public static final String DISPLAY = "display";
        public static final String OPERATION = "operation";
        public static final String VALUE = "value";

    }

    /*Constants found in core-user schema.*/

    public static class UserSchemaConstants {
        public static final String USER_NAME = "userName";
        public static final String EMAILS = "emails";
        public static final String EMAIL = "email";

        //types for multi-valued attributes like emails
        public static final String HOME = "home";
        public static final String WORK = "work";
    }

    public static class AttributeConstants {
        public static final String MULTI_VALUED_ATTRIBUTE_OPERATION = "delete";
    }

    /*Resource names as defined in SCIM Schema spec*/
    public static final String USER = "User";
    public static final String GROUP = "Group";
    public static final String COMMON = "Common";


    /*Resource and attribute descriptions according to SCIM spec.*/

    /**
     * ******Resource Descriptions********************
     */
    public static final String COMMON_DESC = "Each SCIM Resource (Users, Groups, etc.) includes the " +
                                             "common attributes of this schema. These attributes MUST be " +
                                             "included in all Resources, including any extended Resource types";

    public static final String USER_DESC = "SCIM provided schema for representing Users.";

    /**
     * ******Attributes Descriptions of the attributes found in Common Schema**************
     */
    public static final String ID_DESC = "Unique identifier for the SCIM Resource as defined by the Service Provider.";

    public static final String EXTERNAL_ID_DESC = "Unique identifier for the Resource as defined by the Service Consumer." +
                                                  "The Service Provider MUST always interpret the externalId as scoped to the Service Consumer's tenant.";


    /**
     * ******Attributes descriptions of the attributes found in User Schema************
     */
    public static final String USER_NAME_DESC = "Unique identifier for the User, typically " +
                                                "used by the user to directly authenticate to the service provider.";

    public static final String EMAILS_DESC = "E-mail addresses for the User.The value SHOULD be canonicalized by the Service Provider,";

    public static final String TYPE_DESC = "A label indicating the attribute's function; e.g., \"work\" or \"home\".";

    public static final String PRIMARY_DESC = "A Boolean value indicating the 'primary' or preferred attribute value for this attribute, " +
                                              "e.g. the preferred mailing address or primary e-mail address. " +
                                              "The primary attribute value 'true' MUST appear no more than once";

    public static final String DISPLAY_DESC = "A human readable name, primarily used for display purposes. READ-ONLY.";

    public static final String OPERATION_DESC = "The operation to perform on the multi-valued attribute during a PATCH request. " +
                                                "The only valid value is \"delete\", which signifies that this instance should be removed from the Resource.";

    public static final String VALUE_DESC = "The attribute's significant value; e.g., the e-mail address, phone number, etc. " +
                                            "Attributes that define a \"value\" sub-attribute MAY be alternately represented as a collection of primitive types";

    /*Resource endpoints relative to the base SCIM URL*/
    public static final String USER_ENDPOINT = "/Users";
}
