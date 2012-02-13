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

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.Element;
import org.wso2.xkms2.*;

import javax.xml.namespace.QName;
import java.util.Iterator;
/*
 * 
 */

public abstract class KeyBindingAbstractTypeBuilder implements ElementBuilder {

    public void buildElement(OMElement element, KeyBindingAbstractType keyBindingAbstractType)
            throws XKMSException {
        OMElement keyInfoEle =
                element.getFirstChildWithName(new QName(Constants.SignatureSpecNS, "KeyInfo"));
        if (keyInfoEle != null) {
            Element kiEle = (Element)keyInfoEle;
            try {
                KeyInfo ki = new KeyInfo(kiEle, null);
                keyBindingAbstractType.setKeyInfo(ki);
            } catch (XMLSecurityException e) {
                throw new XKMSException(e);
            }

        }

        for (Iterator iterator =
                element.getChildrenWithName(new QName(XKMS2Constants.XKMS2_NS, "KeyUsage"));
             iterator.hasNext();) {
            OMElement e = (OMElement) iterator.next();
            keyBindingAbstractType.addKeyUsage(KeyUsage.valueOf(e.getText()));

        }

        for (Iterator iterator =
                element.getChildrenWithName(new QName(XKMS2Constants.XKMS2_NS, "UseKeyWith"));
             iterator.hasNext();) {
            OMElement e = (OMElement) iterator.next();
            UseKeyWith useKeyWith = new UseKeyWith();

            OMAttribute applicationAttr = e.getAttribute(new QName("Application"));
            if (applicationAttr == null) {
                throw new XKMSException("Required attribute Application is not available.");
            }

            String attributeValue = applicationAttr.getAttributeValue();
            if (attributeValue == null) {
                throw new XKMSException("Value cannot be found for Application attribute");

            }

            useKeyWith.setApplication(attributeValue);

            OMAttribute identifierAttr =  e.getAttribute(new QName("Identifier"));

            if (identifierAttr == null) {
                throw new XKMSException("Required attribute Identifier is not available");
            }
            attributeValue = identifierAttr.getAttributeValue();
            if (attributeValue == null) {
                throw new XKMSException("Value cannot be found for Identifier attribute");
            }
            useKeyWith.setIdentifier(attributeValue);

            keyBindingAbstractType.addUseKeyWith(useKeyWith);

        }

        OMAttribute idAttr = element.getAttribute(new QName("Id"));
        if (idAttr != null) {
            keyBindingAbstractType.setId(idAttr.getAttributeValue());
        }

    }
}
