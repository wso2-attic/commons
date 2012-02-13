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

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
/*
 * 
 */

public class ValidateResult extends ResultType implements ElementSerializable {

    private List keyBindingList;

    public List getKeyBindingList() {
        return keyBindingList;
    }

    public void setKeyBindingList(List keyBindingList) {
        this.keyBindingList = keyBindingList;
    }

    public void addKeyBinding(KeyBinding keyBinding) {
        if (keyBindingList == null) {
            keyBindingList = new ArrayList();
        }
        keyBindingList.add(keyBinding);
    }

    public OMElement serialize(OMFactory factory) throws XKMSException {

        OMElement validateResultEle = factory.createOMElement(XKMS2Constants.ELE_VALIDATE_RESULT);

        super.serialize(factory,validateResultEle);

        if (keyBindingList != null) {
            for (Iterator iterator = keyBindingList.iterator();iterator.hasNext();) {
                KeyBinding keyBinding = (KeyBinding)iterator.next();
                validateResultEle.addChild(keyBinding.serialize(factory));
            }
        }
        return validateResultEle;
    }


}
