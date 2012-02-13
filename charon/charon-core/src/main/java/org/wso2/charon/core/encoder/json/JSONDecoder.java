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
package org.wso2.charon.core.encoder.json;

import org.wso2.charon.core.encoder.Decoder;
import org.wso2.charon.core.exceptions.AbstractCharonException;
import org.wso2.charon.core.objects.SCIMObject;

public class JSONDecoder implements Decoder {


    /**
     * Encode the given SCIM object.
     *
     * @param scimObject
     * @return the resulting string after encoding.
     */
    public String encodeSCIMObject(SCIMObject scimObject) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Encode the Exception to be sent in the SCIM - response payload.
     *
     * @param exception
     * @return the resulting string after encoding
     */
    public String encodeSCIMException(AbstractCharonException exception) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
