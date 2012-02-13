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

import java.util.Map;

public class SCIMResponse {

    protected String responseCode;
    protected String responseMessage;
    //If there are any HTTP header parameters to be set in response other than response code,

    protected Map<String, String> headerParamMap;

    public Map<String, String> getHeaderParamMap() {
        return headerParamMap;
    }

    public void setHeaderParamMap(Map<String, String> headerParamMap) {
        this.headerParamMap = headerParamMap;
    }

    public void setHTTPResponseHeader(String headerParam, String paramValue) {
        if (this.headerParamMap.containsKey(headerParam)) {
            //TODO:print a warning and override the value.
            headerParamMap.remove(headerParam);
            headerParamMap.put(headerParam, paramValue);

        } else {
            headerParamMap.put(headerParam,paramValue);

        }
    }

    public SCIMResponse(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

}
