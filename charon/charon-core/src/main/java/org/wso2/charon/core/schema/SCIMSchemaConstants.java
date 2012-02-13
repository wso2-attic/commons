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
public class SCIMSchemaConstants {

    public static final String coreSchemaURI = "urn:scim:schemas:core:1.0";

    /*Data formats*/
    public static final String JSON = "json";
    public static final String XML = "xml";

    /*Constants found in core-common schema.*/

    public static class SCIMCommonSchemaConstants {
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

    public static class SCIMUserSchemaConstants {
        public static final String USER_NAME = "userName";
        public static final String EMAILS = "emails";

        //types for multi-valued attributes like emails
        public static final String HOME = "home";
        public static final String WORK = "work";
    }

    public static class AttributeConstants {
        public static final String MULTI_VALUED_ATTRIBUTE_OPERATION = "delete";
    }
}
