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

import java.util.Calendar;
/*
 * 
 */

public class UnverifiedKeyBinding extends KeyBindingAbstractType
        implements ElementSerializable {

    private ValidityInterval validityInterval;

    public void setValidityInterval(Calendar notBefore, Calendar notOnOrAfter) {
        this.validityInterval = new ValidityInterval(notBefore, notOnOrAfter);
    }

    public void setValidityInterval(ValidityInterval validityInterval) {
        this.validityInterval = validityInterval;
    }

    public ValidityInterval getValidityInterval() {
        return validityInterval;
    }

    protected void serialize(OMFactory factory, OMElement container) throws XKMSException {
        super.serialize(factory, container);
        
        if (validityInterval != null) {
            container.addChild(validityInterval.serialize(factory));
        }
    }
    
    public OMElement serialize(OMFactory factory) throws XKMSException {
        OMElement unverifiedKeyBindingEle =
                factory.createOMElement(XKMS2Constants.ELE_UNVERIFIED_KEY_BINDING);
        super.serialize(factory, unverifiedKeyBindingEle);
        return unverifiedKeyBindingEle;

    }
}
