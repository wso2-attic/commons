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
package org.wso2.charon.core.protocol.endpoints;

import org.wso2.charon.core.extensions.Storage;
import org.wso2.charon.core.objects.SCIMObject;
import org.wso2.charon.core.protocol.SCIMResponse;

/**
 * Interface for SCIM resource endpoints.
 */
public interface ResourceEndpoint {

    /**
     * Method of resource endpoint which is mapped to HTTP GET request. 
     * @param id - unique resource id
     * @param format - format mentioned in HTTP Content-Type header.
     * @param storage - handler to storage that should be passed by the API user.
     * @return SCIMResponse 
     */
    public SCIMResponse get(String id, String format, Storage storage);

    /**
     * Method of resource endpoint which is mapped to HTTP POST request.
     * @param scimObjectString - Payload of HTTP request, which contains the SCIM object.
     * @param inputFormat - format mentioned in HTTP Content-Type header.
     * @param outputFormat - format mentioned in HTTP Accept header.
     * @param storage - handler to storage that should be passed by the API user.
     * @return SCIMResponse -
     * From Spec: {Since the server is free to alter and/or ignore POSTed content,
     * returning the full representation can be useful to the client, enabling it to correlate the
     * client and server views of the new Resource. When a Resource is created, its URI must be returned
     * in the response Location header.}
     */
    public SCIMResponse create(String scimObjectString, String inputFormat, String outputFormat,
                               Storage storage);
}
