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
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.util.Base64;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.Element;
import org.wso2.xkms2.*;

import javax.xml.namespace.QName;
import java.util.Iterator;
/*
 * 
 */

public abstract class MessageAbstractTypeBuilder implements ElementBuilder {
    static {
        org.apache.xml.security.Init.init();
    }

    public void buildElement(OMElement element, MessageAbstractType messageAbstractType)
            throws XKMSException {
        OMElement sigEle =
                element.getFirstChildWithName(new QName(Constants.SignatureSpecNS, "Signature"));
        if (sigEle != null) {
            Element xmlSigEle = (Element)sigEle;
            XMLSignature xmlSig;
            try {
                xmlSig = new XMLSignature(xmlSigEle, null);
            } catch (XMLSecurityException e) {
                throw new XKMSException(e);
            }
            messageAbstractType.setSignature(xmlSig);
        }

        //TODO xkms:MessageExtension fill this; not important at this point

        OMElement opaqueClientDataEle = element.getFirstChildWithName(
                new QName(XKMS2Constants.XKMS2_NS, "OpaqueClientData"));
        if (opaqueClientDataEle != null) {
            OpaqueClientData opaqueClientData = new OpaqueClientData();

            for (Iterator iterator = opaqueClientDataEle
                    .getChildrenWithName(new QName(XKMS2Constants.XKMS2_NS, "OpaqueData"));
                 iterator.hasNext();) {
                OMElement e = (OMElement) iterator.next();

                OMText binaryNode = (OMText)e.getFirstOMChild();

                OpaqueData opaqueData = new OpaqueData();

                String text = binaryNode.getText();
                opaqueData.setBase64Binary(Base64.decode(text));
                opaqueClientData.addOpaqueData(opaqueData);
                messageAbstractType.setOpaqueClientData(opaqueClientData);
            }

        }

        OMAttribute idAttr = element.getAttribute(new QName("Id"));
        if (idAttr == null) {
            throw new XKMSException("Mandatory Id attribute is not found.");
        }
        String attributeValue = idAttr.getAttributeValue();
        if (attributeValue == null) {
            throw new XKMSException("Value of Id attribute cannot be found");
        }
        messageAbstractType.setId(attributeValue);


        OMAttribute serviceAttr = element.getAttribute(new QName("Service"));
        if (serviceAttr == null) {
            throw new XKMSException("Mandatory Service attribute cannot be found");
        }
        attributeValue = serviceAttr.getAttributeValue();
        if (attributeValue == null) {
            throw new XKMSException("Value for Service attribute is not found");
        }
        messageAbstractType.setServiceURI(attributeValue);

        OMAttribute nonceAttr = element.getAttribute(new QName("Nonce"));
        if (nonceAttr != null) {
            attributeValue = nonceAttr.getAttributeValue();
            if (attributeValue != null) {
                messageAbstractType.setNonce(attributeValue.getBytes());
            }
        }


    }
}
