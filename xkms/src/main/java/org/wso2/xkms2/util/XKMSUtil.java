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
package org.wso2.xkms2.util;

/*
 * 
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.Date;

import javax.activation.DataHandler;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.impl.dom.DOOMAbstractFactory;
import org.apache.axiom.om.impl.dom.factory.OMDOMFactory;
import org.apache.axiom.om.impl.llom.OMTextImpl;
import org.apache.axiom.om.util.UUIDGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.util.XmlSchemaDateFormat;
import org.apache.xml.security.algorithms.MessageDigestAlgorithm;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.IdResolver;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.wso2.xkms2.KeyBinding;
import org.wso2.xkms2.LocateResult;
import org.wso2.xkms2.RecoverResult;
import org.wso2.xkms2.RegisterResult;
import org.wso2.xkms2.ReissueResult;
import org.wso2.xkms2.ValidateRequest;
import org.wso2.xkms2.ValidateResult;
import org.wso2.xkms2.XKMSException;
import org.xml.sax.SAXException;

public class XKMSUtil {

    private static Log log = LogFactory.getLog(XKMSUtil.class);

    /**
     * Gets an llom element and returns a doom element
     * 
     * @param body
     * @return OMElement
     */
    public static DOOMElementMetadata getDOOMElement(OMElement body) {
        body.build();
        XMLStreamReader xmlStreamReader = body.getXMLStreamReader();
        OMFactory fac = DOOMAbstractFactory.getOMFactory();
        StAXOMBuilder builder = new StAXOMBuilder(fac, xmlStreamReader);

        return new DOOMElementMetadata(builder.getDocumentElement(),
                (OMDOMFactory) fac);
    }

    /**
     * Get llom element from doom element
     * 
     * @param doomElement
     * @return OMElement
     */
    public static OMElement getOMElement(OMElement doomElement) {
        StAXOMBuilder builder = new StAXOMBuilder(OMAbstractFactory
                .getOMFactory(), doomElement.getXMLStreamReader());
        return builder.getDocumentElement();
    }

    /**
     * convert OMElement to org.w3c.dom.Element
     * 
     * @param element
     * @return
     * @throws XKMSException
     */
    public static Element toDOM(OMElement element) throws XKMSException {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            factory.setNamespaceAware(true);
            element.serialize(baos);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos
                    .toByteArray());
            return factory.newDocumentBuilder().parse(bais)
                    .getDocumentElement();

        } catch (XMLStreamException e) {
            log.error(e);
            throw new XKMSException(e);
        } catch (IOException e) {
            log.error(e);
            throw new XKMSException(e);
        } catch (ParserConfigurationException e) {
            log.error(e);
            throw new XKMSException(e);
        } catch (SAXException e) {
            log.error(e);
            throw new XKMSException(e);
        }

    }

    /**
     * Converts org.w3c.dom.Element to OMElement
     * 
     * @param element
     * @return OMElement
     */
    public static OMElement toOM(Element element) throws XKMSException {
        // TODO find a better way to conver this Ruchith ??
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        StAXOMBuilder buider;
        try {
            bos.write(Canonicalizer.getInstance(
                    Canonicalizer.ALGO_ID_C14N_WITH_COMMENTS)
                    .canonicalizeSubtree(element));
            buider = new StAXOMBuilder(new ByteArrayInputStream(bos
                    .toByteArray()));
            return buider.getDocumentElement();
        } catch (IOException e) {
            log.error(e);
            throw new XKMSException(e);
        } catch (CanonicalizationException e) {
            log.error(e);
            throw new XKMSException(e);
        } catch (InvalidCanonicalizerException e) {
            log.error(e);
            throw new XKMSException(e);
        } catch (XMLStreamException e) {
            log.error(e);
            throw new XKMSException(e);
        }
    }

    public static void outputDOM(Node contextNode, OutputStream os,
            boolean addPreamble) throws XKMSException {

        try {
            if (addPreamble) {
                os.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        .getBytes());
            }

            os.write(Canonicalizer.getInstance(
                    Canonicalizer.ALGO_ID_C14N_WITH_COMMENTS)
                    .canonicalizeSubtree(contextNode));

        } catch (InvalidCanonicalizerException ex) {
            log.error(ex);
            throw new XKMSException(ex);
        } catch (CanonicalizationException ex) {
            log.error(ex);
            throw new XKMSException(ex);
        } catch (IOException e) {
            log.error(e);
            throw new XKMSException(e);
        }
    }

    /**
     * Outputs a DOM tree to an {@link OutputStream}.
     * 
     * @param contextNode
     *            root node of the DOM tree
     * @param os
     *            the {@link OutputStream}
     */
    public static void outputDOM(Node contextNode, OutputStream os)
            throws XKMSException {
        outputDOM(contextNode, os, false);
    }

    /**
     * @param child
     * @param parent
     */
    public static void appendChild(Element child, OMElement parent) {
        Document ownerDocument = ((Element) parent).getOwnerDocument();
        Node importNode = ownerDocument.importNode(child, true);
        parent.addChild((OMNode) importNode);
    }

    public static void appendAsTest(byte[] base64Binary, OMElement element) {
        ByteArrayDataSource bads = new ByteArrayDataSource(base64Binary);
        DataHandler dataHandler = new DataHandler(bads);
        OMText text = new OMTextImpl(dataHandler, element.getOMFactory());
        element.addChild(text);
    }

    public static String getTimeDate(Date date) {
        DateFormat zulu = new XmlSchemaDateFormat();
        return zulu.format(date);
    }

    public static class DOOMElementMetadata {
        private OMElement element;

        private OMDOMFactory factory;

        public DOOMElementMetadata(OMElement element, OMDOMFactory factory) {
            this.element = element;
            this.factory = factory;
        }

        public OMElement getElement() {
            return element;
        }

        public OMDOMFactory getFactory() {
            return factory;
        }
    }

    public static Key getAuthenticationKey(String authenticationCode) {
        byte[] keyMetirials = authenticationCode.getBytes();
        byte[] key = (new BigInteger("1", 16).toByteArray());
        Key result = null;

        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            result = new SecretKeySpec(mac.doFinal(keyMetirials), "HmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static XMLSignature sign(Key signKey, Element signElement)
            throws XKMSException {

        String xmlSignatureAlgorithm = null;

        if (signKey instanceof SecretKeySpec) {
            xmlSignatureAlgorithm = XMLSignature.ALGO_ID_MAC_HMAC_SHA1;

        } else if (signKey instanceof PrivateKey) {
            xmlSignatureAlgorithm = XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1;

        } else {
            throw new XKMSException("Invalid signing key type : "
                    + signKey.getClass().getName());
        }

        String elementId = signElement.getAttribute("Id");
        if (elementId == null) {
            throw new XKMSException("Id of the signing element is not set");
        }

        String elementRefId = "#" + elementId;
        IdResolver.registerElementById(signElement, elementId);

        try {
            XMLSignature signature = new XMLSignature(signElement
                    .getOwnerDocument(), "", xmlSignatureAlgorithm,
                    Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

            Transforms transforms = new Transforms(signElement
                    .getOwnerDocument());
            transforms
                    .addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
            signature.addDocument(elementRefId, transforms,
                    MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA1);

            signature.sign(signKey);
            return signature;

        } catch (XMLSecurityException se) {
            throw new XKMSException(se);
        }
    }

    public static void sign(Key signKey, X509Certificate signCert,
            Element signElement) throws XKMSException {

        String elementId = signElement.getAttribute("Id");
        if (elementId == null) {
            throw new XKMSException("Id of the signing element is not set");
        }

        String elementRefId = "#" + elementId;
        IdResolver.registerElementById(signElement, elementId);

        try {
            XMLSignature signature = new XMLSignature(signElement
                    .getOwnerDocument(), elementRefId,
                    XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1,
                    Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

            signElement.appendChild(signature.getElement());

            Transforms transforms = new Transforms(signElement
                    .getOwnerDocument());

            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
            transforms
                    .addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);

            signature.addDocument(elementRefId, transforms,
                    MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA1);

            signature.addKeyInfo(signCert);
            signature.addKeyInfo(signCert.getPublicKey());

            signature.sign(signKey);

        } catch (XMLSecurityException xmse) {
            throw new XKMSException(xmse);
        }
    }

    public static String getRamdomId() {
        return UUIDGenerator.getUUID();
    }

    public static Document getDOOMDocument() {
        return (Document) DOOMAbstractFactory.getOMFactory().createOMDocument();
    }

    /**
     * set the namespace if it is not set already. <p/>
     * 
     * @param element
     * @param namespace
     * @param prefix
     * @return TODO
     */
    public static String setNamespace(Element element, String namespace,
            String prefix) {
        String pre = getPrefixNS(namespace, element);
        if (pre != null) {
            return pre;
        }
        element.setAttributeNS(WSConstants.XMLNS_NS, "xmlns:" + prefix,
                namespace);
        return prefix;
    }

    /*
     * ** The following methods were copied over from aixs.utils.XMLUtils and
     * adapted
     */

    public static String getPrefixNS(String uri, Node e) {
        while (e != null && (e.getNodeType() == Element.ELEMENT_NODE)) {
            NamedNodeMap attrs = e.getAttributes();
            for (int n = 0; n < attrs.getLength(); n++) {
                Attr a = (Attr) attrs.item(n);
                String name;
                if ((name = a.getName()).startsWith("xmlns:")
                        && a.getNodeValue().equals(uri)) {
                    return name.substring(6);
                }
            }
            e = e.getParentNode();
        }
        return null;
    }

    public static void print(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i]);
        }
    }

    public static RegisterResult createRegisterResult() {
        RegisterResult result = new RegisterResult();
        result.setId(getRamdomId());
        return result;
    }
    
    public static KeyBinding createKeyBinding() {
        KeyBinding keyBinding = new KeyBinding();
        keyBinding.setId(getRamdomId());
        return keyBinding;
    }
    
    public static RecoverResult createRecoverResult() {
        RecoverResult recoverResult = new RecoverResult();
        recoverResult.setId(getRamdomId());
        return recoverResult;
    }
    
    public static ReissueResult creatReissueResult() {
        ReissueResult reissueResult = new ReissueResult();
        reissueResult.setId(getRamdomId());
        return reissueResult;
    }
    
    public static LocateResult createLocateResult() {
        LocateResult locateResult = new LocateResult();
        locateResult.setId(getRamdomId());
        return locateResult;
    }
    
    public static ValidateResult createValidateResult() {
        ValidateResult validateResult = new ValidateResult();
        validateResult.setId(getRamdomId());
        return validateResult;
    }
}