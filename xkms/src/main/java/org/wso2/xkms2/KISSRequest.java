/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.xkms2;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
/*
 * 
 */

public abstract class KISSRequest extends RequestAbstractType{
    private QueryKeyBinding queryKeyBinding;

    public QueryKeyBinding getQueryKeyBinding() {
        return queryKeyBinding;
    }

    public void setQueryKeyBinding(QueryKeyBinding queryKeyBinding) {
        this.queryKeyBinding = queryKeyBinding;
    }

    protected void serialize(OMFactory factory, OMElement container) throws XKMSException {
        super.serialize(factory, container);

        if (queryKeyBinding == null) {
            throw new XKMSException("QueryKeyBinding is not available");
        }
        container.addChild(queryKeyBinding.serialize(factory));
    }
    
}
