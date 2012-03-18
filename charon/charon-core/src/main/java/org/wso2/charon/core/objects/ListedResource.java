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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListedResource implements SCIMObject {

    /*Collection of attributes which constitute this resource.*/
    protected Map<String, Attribute> attributeList = new HashMap<String, Attribute>();

    /*List of schemas where the attributes of this resource, are defined.*/
    protected List<String> schemaList = new ArrayList<String>();

    @Override
    public Attribute getAttribute(String attributeName) throws NotFoundException {
        if (attributeList.containsKey(attributeName)) {
            return attributeList.get(attributeName);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public void deleteAttribute(String attributeName) throws NotFoundException {
        if (attributeList.containsKey(attributeName)) {
            attributeList.remove(attributeName);
        }
    }

    @Override
    public List<String> getSchemaList() {
         return schemaList;        
    }

    @Override
    public Map<String, Attribute> getAttributeList() {
        return attributeList;
    }
}
