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
import org.apache.axiom.om.OMNamespace;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.content.KeyName;
import org.apache.xml.security.keys.content.X509Data;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract public class KeyBindingAbstractType implements XKMSElement {

    private String id;

    private KeyInfo keyInfo;

    private List keyUsagesList = new ArrayList(3);

    private List useKeyWithList = new ArrayList();

    private PublicKey keyValue;

    private X509Certificate certValue;

    private String keyName;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setKeyValue(PublicKey keyValue) {
        this.keyValue = keyValue;
    }

    public PublicKey getKeyValue() {
        return keyValue;
    }

    public void setCertValue(X509Certificate certValue) {
        this.certValue = certValue;
    }

    public X509Certificate getCertValue() {
        return certValue;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyInfo(KeyInfo keyInfo) {
        this.keyInfo = keyInfo;
    }

    public KeyInfo getKeyInfo() {
        return keyInfo;
    }

    public void addKeyUsage(KeyUsage keyUsage) {
        keyUsagesList.add(keyUsage);
    }

    public List getKeyUsage() {
        return keyUsagesList;
    }

    public void addUseKeyWith(String application, String identifier) {
        useKeyWithList.add(new UseKeyWith(application, identifier));
    }

    public void addUseKeyWith(UseKeyWith useKeyWith) {
        useKeyWithList.add(useKeyWith);
    }

    public List getUseKeyWith() {
        return useKeyWithList;
    }

    /**
     * Types derived from this type should call this inorder to serialze the
     * object properly
     * 
     * @param factory
     * @param container
     */
    protected void serialize(OMFactory factory, OMElement container)
            throws XKMSException {

        if (keyInfo == null
                && (keyName != null || keyValue != null || certValue != null)) {
            Document doc = ((Element) container).getOwnerDocument();
            keyInfo = new KeyInfo(doc);

            if (keyName != null) {
                KeyName keyName = new KeyName(doc, this.keyName);
                keyInfo.add(keyName);
            }
            
            if (keyValue != null) {
                keyInfo.addKeyValue(keyValue);
            }
            
            if (certValue != null) {
                X509Data data = new X509Data(doc);
                try {
                    data.addCertificate(certValue);
                    keyInfo.add(data);
                } catch (XMLSecurityException ex) {
                    throw new XKMSException(ex);
                }
            }
            
            keyInfo.add(keyValue);
        }

        if (keyInfo != null) {
            Element kiElement = keyInfo.getElement();
            Element containerDOMElement = (Element) container;
            Document ownerDocument = containerDOMElement.getOwnerDocument();
            Node node = ownerDocument.importNode(kiElement, true);
            containerDOMElement.appendChild(node);
        }

        for (Iterator iterator = keyUsagesList.iterator(); iterator.hasNext();) {
            KeyUsage keyUsage = (KeyUsage) iterator.next();

            OMElement keyUsageEle = factory
                    .createOMElement(XKMS2Constants.ELE_KEY_USAGE);
            keyUsageEle.setText(keyUsage.getAnyURI());
            container.addChild(keyUsageEle);

        }

        if (useKeyWithList != null) {
            for (Iterator iterator = useKeyWithList.iterator(); iterator
                    .hasNext();) {
                UseKeyWith useKeyWith = (UseKeyWith) iterator.next();
                container.addChild(useKeyWith.serialize(factory));
            }
        }

        if (id != null) {
            OMNamespace emptyNs = factory.createOMNamespace("", "");
            container.addAttribute(XKMS2Constants.ATTR_ID, id, emptyNs);
        }

    }
}
