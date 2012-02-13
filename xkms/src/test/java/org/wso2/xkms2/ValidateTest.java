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

import junit.framework.TestCase;
import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.impl.dom.DOOMAbstractFactory;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.content.X509Data;
import org.apache.xml.security.utils.Constants;
import org.wso2.xkms2.builder.ValidateRequestBuilder;
import org.wso2.xkms2.builder.ValidateResultBuilder;
import org.wso2.xkms2.util.XKMSUtil;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.activation.DataHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

/*
 * 
 */

public class ValidateTest extends TestCase {

    public void setUp() throws java.lang.Exception {
        org.apache.xml.security.Init.init();
    }

    public void testDocSigValidateRequest() throws Exception {
        OMFactory fac = DOOMAbstractFactory.getOMFactory();
        OMNamespace ns = fac.createOMNamespace(XKMS2Constants.XKMS2_NS, "");
        OMNamespace emptyNs = fac.createOMNamespace("", "");

        OMElement validateRequestEle = fac.createOMElement("ValidateRequest",
                ns);
        validateRequestEle.declareNamespace(Constants.SignatureSpecNS, "ds");
        validateRequestEle.declareNamespace(
                "http://www.w3.org/2001/04/xmlenc#", "xenc");
        validateRequestEle.addAttribute("Id",
                "Ie26380bfeb9d0c5bc526d5213a162d46", emptyNs);
        validateRequestEle.addAttribute("Service",
                "http://www.example.org/XKMS", emptyNs);

        OMElement opaqueClientDataEle = fac.createOMElement("OpaqueClientData",
                ns);
        validateRequestEle.addChild(opaqueClientDataEle);
        OMText omText = fac.createOMText(new DataHandler(
                new ByteArrayDataSource("junk data".getBytes())), false);
        OMElement opaqueDataEle = fac.createOMElement("OpaqueData", ns);
        opaqueDataEle.addChild(omText);
        opaqueClientDataEle.addChild(opaqueDataEle);

        OMElement respondWithEle = fac.createOMElement("RespondWith", ns);
        respondWithEle.setText(RespondWith.X_509_CERT.getAnyURI());
        validateRequestEle.addChild(respondWithEle);

        OMElement queryKeyBindingEle = fac.createOMElement("QueryKeyBinding",
                ns);
        OMElement kiEle = getKI();

        queryKeyBindingEle.addChild(kiEle);

        OMElement keyUsageEle = fac.createOMElement("KeyUsage", ns);
        keyUsageEle.setText(KeyUsage.SIGNATURE.getAnyURI());
        queryKeyBindingEle.addChild(keyUsageEle);

        OMElement useKeyWithEle = fac.createOMElement("UseKeyWith", ns);
        useKeyWithEle.addAttribute("Application", "urn:ietf:rfc:2633", emptyNs);
        useKeyWithEle.addAttribute("Identifier", "alice@example.com", emptyNs);
        queryKeyBindingEle.addChild(useKeyWithEle);
        validateRequestEle.addChild(queryKeyBindingEle);

        ValidateRequestBuilder builder = new ValidateRequestBuilder();
        ValidateRequest validateRequest = (ValidateRequest) builder
                .buildElement(validateRequestEle);

        assertNotNull(validateRequest.getId());
        assertNotNull(validateRequest.getQueryKeyBinding());
        assertNotNull(validateRequest.getQueryKeyBinding().getKeyInfo());

        fac = DOOMAbstractFactory.getOMFactory();

        OMElement ele = validateRequest.serialize(fac);

        assertNotNull(ele);
    }

    public void testDocSigValidateResult() throws Exception {
        OMFactory fac = DOOMAbstractFactory.getOMFactory();
        OMNamespace ns = fac.createOMNamespace(XKMS2Constants.XKMS2_NS, "");
        OMNamespace emptyNs = fac.createOMNamespace("", "");

        OMElement validateResultEle = fac.createOMElement("ValidateResult", ns);
        validateResultEle.declareNamespace(Constants.SignatureSpecNS, "ds");
        validateResultEle.declareNamespace("http://www.w3.org/2001/04/xmlenc#",
                "xenc");
        validateResultEle.addAttribute("Id",
                "I34ef61b96f7db2250c229d37a17edfc0", emptyNs);
        validateResultEle.addAttribute("Service",
                "http://www.example.org/XKMS", emptyNs);
        validateResultEle.addAttribute("ResultMajor", ResultMajor.SUCCESS
                .getAnyURI(), emptyNs);
        validateResultEle.addAttribute("RequestId",
                "Ie26380bfeb9d0c5bc526d5213a162d46", emptyNs);

        OMElement keyBindingEle = fac.createOMElement("KeyBinding", ns);
        validateResultEle.addChild(keyBindingEle);
        keyBindingEle.addAttribute("Id", "Icf608e9e8b07468fde1b7ee5449fe831",
                emptyNs);

        OMElement kiEle = getKI1();
        keyBindingEle.addChild(kiEle);

        OMElement keyUsageEle1 = fac.createOMElement("KeyUsage", ns);
        keyUsageEle1.setText(KeyUsage.SIGNATURE.getAnyURI());

        OMElement keyUsageEle2 = fac.createOMElement("KeyUsage", ns);
        keyUsageEle2.setText(KeyUsage.SIGNATURE.getAnyURI());

        OMElement keyUsageEle3 = fac.createOMElement("KeyUsage", ns);
        keyUsageEle3.setText(KeyUsage.SIGNATURE.getAnyURI());

        keyBindingEle.addChild(keyUsageEle1);
        keyBindingEle.addChild(keyUsageEle2);
        keyBindingEle.addChild(keyUsageEle3);

        OMElement useKeyWithEle = fac.createOMElement("UseKeyWith", ns);
        useKeyWithEle.addAttribute("Application", "urn:ietf:rfc:2633", emptyNs);
        useKeyWithEle.addAttribute("Identifier", "alice@example.com", emptyNs);

        keyBindingEle.addChild(useKeyWithEle);

        OMElement statusEle = fac.createOMElement("Status", ns);
        statusEle.addAttribute("StatusValue", StatusValue.VALID.getAnyURI(),
                emptyNs);

        OMElement validReasonEle1 = fac.createOMElement("ValidReason", ns);
        validReasonEle1.setText(ValidReason.SIGNATURE.getAnyURI());
        OMElement validReasonEle2 = fac.createOMElement("ValidReason", ns);
        validReasonEle2.setText(ValidReason.ISSUER_TRUST.getAnyURI());
        OMElement validReasonEle3 = fac.createOMElement("ValidReason", ns);
        validReasonEle3.setText(ValidReason.REVOCATION_STATUS.getAnyURI());
        OMElement validReasonEle4 = fac.createOMElement("ValidReason", ns);
        validReasonEle4.setText(ValidReason.VALIDITY_INTERVAL.getAnyURI());

        statusEle.addChild(validReasonEle1);
        statusEle.addChild(validReasonEle2);
        statusEle.addChild(validReasonEle3);
        statusEle.addChild(validReasonEle4);

        keyBindingEle.addChild(statusEle);

        ValidateResult validateResult = (ValidateResult) ValidateResultBuilder.INSTANCE
                .buildElement(validateResultEle);

        assertNotNull(validateResult.getId());
        assertNotNull(validateResult.getKeyBindingList());
        KeyBinding keyBinding = (KeyBinding) validateResult.getKeyBindingList()
                .get(0);
        assertNotNull(keyBinding);
        assertNotNull(keyBinding.getKeyUsage());
        assertNotNull(keyBinding.getKeyInfo());
        assertNotNull(keyBinding.getUseKeyWith());
        Status status = keyBinding.getStatus();
        assertNotNull(status);
        assertNotNull(status.getStatusValue());
        assertNotNull(status.getValidReasonList());

        fac = DOOMAbstractFactory.getOMFactory();

        OMElement ele = validateResult.serialize(fac);

        assertNotNull(ele);
    }

    private OMElement getKI1() throws Exception {
        KeyStore ks1 = KeyStore.getInstance("JKS");
        InputStream fis1 = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("./org/wso2/xkms/kiss/kiss-store.jks");
        ks1.load(fis1, "xkms-kiss".toCharArray());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
        org.w3c.dom.Document doc = db.newDocument();
        KeyInfo ki = new KeyInfo(doc);

        doc.appendChild(ki.getElement());

        X509Certificate cert1 = (X509Certificate) ks1.getCertificate("kiss");

        X509Data x509Data = new X509Data(doc);
        x509Data.addCertificate(cert1);

        ki.add(x509Data);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XKMSUtil.outputDOM(doc, bos);

        XMLStreamReader reader = XMLInputFactory.newInstance()
                .createXMLStreamReader(
                        new ByteArrayInputStream(bos.toByteArray()));
        StAXOMBuilder builder = new StAXOMBuilder(reader);
        OMElement kiEle = builder.getDocumentElement();
        kiEle.build();

        return kiEle;

    }

    private OMElement getKI() throws Exception {

        KeyStore ks1 = KeyStore.getInstance("JKS");
        KeyStore ks2 = KeyStore.getInstance("JKS");
        InputStream fis1 = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("./org/wso2/xkms/kiss/kiss-store.jks");
        InputStream fis2 = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("./org/wso2/xkms/kiss/kiss2-store.jks");

        ks1.load(fis1, "xkms-kiss".toCharArray());
        ks2.load(fis2, "xkms-kiss".toCharArray());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
        org.w3c.dom.Document doc = db.newDocument();
        KeyInfo ki = new KeyInfo(doc);

        doc.appendChild(ki.getElement());

        X509Certificate cert1 = (X509Certificate) ks1.getCertificate("kiss");
        X509Certificate cert2 = (X509Certificate) ks2.getCertificate("kiss2");

        X509Data x509Data = new X509Data(doc);
        x509Data.addCertificate(cert1);
        x509Data.addCertificate(cert2);
        ki.add(x509Data);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XKMSUtil.outputDOM(doc, bos);

        XMLStreamReader reader = XMLInputFactory.newInstance()
                .createXMLStreamReader(
                        new ByteArrayInputStream(bos.toByteArray()));
        StAXOMBuilder builder = new StAXOMBuilder(reader);
        OMElement kiEle = builder.getDocumentElement();
        kiEle.build();

        return kiEle;

    }
}
