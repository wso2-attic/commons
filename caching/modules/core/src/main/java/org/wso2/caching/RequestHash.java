/*
*  Copyright (c) 2005-2011, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.caching;

import java.io.Serializable;

/**
 * Represents a SOAP Request Hash
 */
public class RequestHash implements Serializable {

    public String requestHash;

    public RequestHash(String requestHash) {
        this.requestHash = requestHash;
    }

    public String getRequestHash() {
        return requestHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestHash that = (RequestHash) o;
        return requestHash.equals(that.requestHash);
    }

    @Override
    public int hashCode() {
        return requestHash.hashCode();
    }
}
