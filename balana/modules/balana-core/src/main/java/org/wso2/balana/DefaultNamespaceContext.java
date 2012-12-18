/*
*  Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.balana;

import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

/**
 * implementation of <code>NamespaceContext</code>
 */
public class DefaultNamespaceContext implements NamespaceContext {

    private String prefix;

    private String namespaceURI;

    public DefaultNamespaceContext(String prefix, String namespaceURI) {
        this.prefix = prefix;
        this.namespaceURI = namespaceURI;
    }

    public String getNamespaceURI(String prefix) {
        if(prefix != null && prefix.equals(this.prefix)){
            return namespaceURI;
        } else {
            return null;
        }
    }

    public String getPrefix(String namespaceURI) {
        if(namespaceURI != null && namespaceURI.equals(this.namespaceURI)){
            return prefix;
        } else {
            return null;
        }
    }

    public Iterator getPrefixes(String namespaceURI) {
        return null;
    }

}
