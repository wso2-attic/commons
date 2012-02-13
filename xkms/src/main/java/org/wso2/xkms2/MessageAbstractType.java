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
import org.apache.xml.security.signature.XMLSignature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.security.Key;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * The MessageAbstractType abstract class is the class from which all XKMS message
 * element classes are derived
 */

public class MessageAbstractType implements XKMSElement{


    private XMLSignature signature;
    private List messageExtensionList;
    private OpaqueClientData opaqueClientData;

    private String id;
    private String serviceURI;
    private byte[] nonce;
    
    private Key signKey;
    private X509Certificate signCert;

    public XMLSignature getSignature() {
        return signature;
    }

    public void setSignature(XMLSignature signature) {
        this.signature = signature;
    }
    
    public void setSignKey(Key signKey) {
        this.signKey = signKey;
    }
    
    public Key getSignKey() {
        return signKey;
    }
    
    public void setSignCert(X509Certificate signCert) {
        this.signCert = signCert;
    }
    
    public X509Certificate getSignCert() {
        return signCert;
    }

    public List getMessageExtensionList() {
        return messageExtensionList;
    }

    public void setMessageExtensionList(List messageExtensionList) {
        this.messageExtensionList = messageExtensionList;
    }

    public void addMessageExtension(MessageExtension messageExtension) {
        this.messageExtensionList.add(messageExtension);
    }

    public OpaqueClientData getOpaqueClientData() {
        return opaqueClientData;
    }

    public void setOpaqueClientData(OpaqueClientData opaqueClientData) {
        this.opaqueClientData = opaqueClientData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceURI() {
        return serviceURI;
    }

    public void setServiceURI(String serviceURI) {
        this.serviceURI = serviceURI;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void setNonce(byte[] nonce) {
        this.nonce = nonce;
    }

    /**
     * Types derived from this type should call this inorder to serialze the object properly
     *
     * @param factory
     * @param container
     */
    protected void serialize(OMFactory factory, OMElement container) throws XKMSException {

        if (signature != null) {
            Element sigElement = signature.getElement();
            Element containerDOMElement = (Element)container;
            Document ownerDocument = containerDOMElement.getOwnerDocument();
            Node node = ownerDocument.importNode(sigElement, true);
            containerDOMElement.appendChild(node);
        }

        //TODO left out the MessageExtension for the moment. Will implement this once the interop has started.

        if (opaqueClientData != null) {
            OMElement ele = opaqueClientData.serialize(factory);
            if (ele != null) {
                container.addChild(ele);
            }

        }

        if (id == null) {
            throw new XKMSException("Required element Id is not set");
        }
        OMNamespace emptyNs = factory.createOMNamespace("", "");
        container.addAttribute(XKMS2Constants.ATTR_ID, id, emptyNs);

        if (serviceURI == null) {
            throw new XKMSException("Required element Service is not set");
        }
        container.addAttribute(XKMS2Constants.ATTR_SERVICE, serviceURI, emptyNs);

        if (nonce != null) {
            container.addAttribute(XKMS2Constants.ATTR_NONCE, new String(nonce), emptyNs);
        }

    }

}
