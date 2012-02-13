/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.xkms2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;

public class KRSSResult extends ResultType {
    
    private ArrayList keyBindingList = new ArrayList();
    
    public void addKeyBinding(KeyBinding keyBinding) {
        this.keyBindingList.add(keyBinding);
    }

    public List getKeyBindings() {
        return keyBindingList;
    }
    
    protected void serialize(OMFactory factory, OMElement container) throws XKMSException {
        super.serialize(factory, container);
        KeyBinding keyBinding;
        
        for (Iterator iterator = keyBindingList.iterator(); iterator.hasNext();) {
            keyBinding = (KeyBinding) iterator.next();
            container.addChild(keyBinding.serialize(factory));
        }
    }
}
