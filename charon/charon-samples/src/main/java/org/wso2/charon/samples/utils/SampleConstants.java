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
package org.wso2.charon.samples.utils;

/**
 * Change constants common to all the samples in one place - like credentials
 */
public class SampleConstants {
    //credentials for accessing API
    public static final String CRED_USER_NAME = "charonAdmin";
    public static final String CRED_PASSWORD = "charonAdmin";

    public static final String CRED_TENANT_DOMAIN = "wso2.edu";

    public static final String USER_ENDPOINT = "http://localhost:8081/charonDemoApp/scim/Users/";
    public static final String GROUP_ENDPOINT = "http://localhost:8081/charonDemoApp/scim/Groups/";
    public static final String REG_SERVICE_ENDPOINT = "http://localhost:8080/charonDemoApp/scim/RegistrationService";
    /*public static final String USER_ENDPOINT = "http://appserver.stratoslive.wso2.com/t/charon.com/webapps/charonDemoApp/scim/Users/";
    //public static final String USER_ENDPOINT = "http://localhost:8080/t/charon.com/webapps/charonDemoApp/scim/Users/";
    public static final String GROUP_ENDPOINT = "http://appserver.stratoslive.wso2.com/t/charon.com/webapps/charonDemoApp/scim/Groups/";
    public static final String REG_SERVICE_ENDPOINT = "http://appserver.stratoslive.wso2.com/t/charon.com/webapps/charonDemoApp/scim/RegistrationService";
    //public static final String REG_SERVICE_ENDPOINT = "http://localhost:8080/t/charon.com/webapps/charonDemoApp/scim/RegistrationService";
    //http://appserver.stratoslive.wso2.com/t/charon.org/webapps/charonDemoApp/*/

    /*public static final String USER_ENDPOINT = "http://people.wso2.com:8080/charonDemoApp/scim/Users/";
    public static final String GROUP_ENDPOINT = "http://people.wso2.com:8080/charonDemoApp/scim/Groups/";
    public static final String REG_SERVICE_ENDPOINT = "http://people.wso2.com:8080/charonDemoApp/scim/RegistrationService";*/
}
