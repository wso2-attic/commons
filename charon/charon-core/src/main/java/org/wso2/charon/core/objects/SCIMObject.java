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
package org.wso2.charon.core.objects;

import org.wso2.charon.core.attributes.Attribute;
import org.wso2.charon.core.exceptions.NotFoundException;

/**
 * SCIM Object is a collection of attributes that may come from different schemas.
 * This interface supports that concept, which will be implemented by SCIM objects.
 * <p/>
 * In server-side, there should be a way to map the storage to these attributes.
 */
public interface SCIMObject {

    public void setAttribute(Attribute newAttribute);

    public Attribute getAttribute(String attributeName) throws NotFoundException;

    public void deleteAttribute(String attributeName) throws NotFoundException;

    /**
     * Validates whether the given SCIM object adheres to the SCIM schema.
     * @param scimObject
     * @return
     */
    public boolean validate(SCIMObject scimObject);

}