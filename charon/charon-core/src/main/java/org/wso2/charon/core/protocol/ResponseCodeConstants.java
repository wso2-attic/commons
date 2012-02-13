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
package org.wso2.charon.core.protocol;

/**
 * SCIM Protocol uses the response status codes defined in HTTP to indicate
 * operation success or failure. This class includes those code and relevant description as constants.
 */
public class ResponseCodeConstants {

    //when errors returned in response, this goes as the heading of the body:
    public static final String ERRORS = "Errors";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";

    public static final String CODE_FORMAT_NOT_SUPPORTED = "406";
    public static final String DESC_FORMAT_NOT_SUPPORTED = "Requested format is not supported.";

    public static final String CODE_INTERNAL_SERVER_ERROR = "500";
    public static final String DESC_INTERNAL_SERVER_ERROR =
            "The server encountered an unexpected condition which prevented it from fulfilling the request";

    public static final String CODE_RESOURCE_NOT_FOUND = "404";
    public static final String DESC_RESOURCE_NOT_FOUND = "Specified resource does not exist.";

    public static final String CODE_NOT_FOUND = "404";
    public static final String DESC_NOT_FOUND = "Requested resource is not found.";

    public static final String CODE_OK = "200";


    //Other common error messages thrown by the API
    public static final String MISMATCH_IN_REQUESTED_DATATYPE = "Requested datatype doesn't match " +
                                                                "the datatype of the attribute value";
    public static final String ATTRIBUTE_ALREADY_EXIST = "Attribute with the same attribute name " +
                                                         "already exist.";
    public static final String ATTRIBUTE_READ_ONLY = "Attribute is read only. Hence can not set again.";


}
