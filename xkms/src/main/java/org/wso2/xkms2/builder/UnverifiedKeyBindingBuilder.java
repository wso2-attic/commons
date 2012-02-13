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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAttribute;
import org.wso2.xkms2.*;

import javax.xml.namespace.QName;

/*
 * 
 */

public class UnverifiedKeyBindingBuilder extends KeyBindingAbstractTypeBuilder {

    public UnverifiedKeyBindingBuilder() {
    }

    public XKMSElement buildElement(OMElement element) throws XKMSException {
        UnverifiedKeyBinding unverifiedKeyBinding = new UnverifiedKeyBinding();
        buildElement(element, unverifiedKeyBinding);
        return unverifiedKeyBinding;
    }

    public void buildElement(OMElement element,
            UnverifiedKeyBinding unverifiedKeyBinding) throws XKMSException {

        super.buildElement(element, unverifiedKeyBinding);

        OMElement validityIntervalEle = element
                .getFirstChildWithName(new QName(XKMS2Constants.XKMS2_NS,
                        "ValidityInterval"));

        if (validityIntervalEle != null) {
            OMAttribute notBeforeAttr = validityIntervalEle
                    .getAttribute(new QName("NotBefore"));
            OMAttribute notOnOrAfterAttr = validityIntervalEle
                    .getAttribute(new QName("NotOnOrAfter"));

            ValidityInterval validityInterval = new ValidityInterval();

            if (notBeforeAttr != null) {
                validityInterval.setNotBefore(notBeforeAttr
                        .getAttributeValue());
            }

            if (notOnOrAfterAttr != null) {
                validityInterval.setOnOrAfter(notOnOrAfterAttr
                        .getAttributeValue());
            }

            unverifiedKeyBinding.setValidityInterval(validityInterval);
        }
    }
}
