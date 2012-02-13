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
package org.wso2.xkms2.builder;

import org.wso2.xkms2.*;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;

/*
 * 
 */

public class KeyBindingBuilder extends UnverifiedKeyBindingBuilder {

    public static final KeyBindingBuilder INSTANCE = new KeyBindingBuilder();

    private KeyBindingBuilder() {
    }

    /**
     * 
     */
    public XKMSElement buildElement(OMElement element) throws XKMSException {
        KeyBinding keyBinding = new KeyBinding();
        this.buildElement(element, keyBinding);
        return keyBinding;
    }

    public void buildElement(OMElement element, KeyBinding keyBinding)
            throws XKMSException {
        super.buildElement(element, keyBinding);

        OMElement statusEle = element.getFirstChildWithName(XKMS2Constants.ELE_STATUS);

        if (statusEle == null) {
            throw new XKMSException("Status element cannot be found");
        }
        
        keyBinding.setStatus((Status) StatusBuilder.INSTANCE
                .buildElement(statusEle));
    }
}
