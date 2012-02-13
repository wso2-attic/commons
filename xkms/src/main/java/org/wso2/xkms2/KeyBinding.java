/*
 * Copyright 2001-2004 The Apache Software Foundation.
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

public class KeyBinding extends UnverifiedKeyBinding {

    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public OMElement serialize(OMFactory factory) throws XKMSException {
        OMElement keyBindingEle = factory.createOMElement(XKMS2Constants.ELE_KEY_BINDING);
        this.serialize(factory,keyBindingEle);
        return keyBindingEle;
    }
    
    public void serialize(OMFactory factory, OMElement container) throws XKMSException {
        super.serialize(factory, container);
        
        if (status == null) {
            throw new XKMSException("Status element not found");
        }
        container.addChild(status.serialize(factory));
    }
}
