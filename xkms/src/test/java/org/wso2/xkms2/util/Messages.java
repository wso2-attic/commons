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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.content.X509Data;
import org.wso2.xkms2.XKMS2Constants;
import org.wso2.xkms2.RespondWith;
import org.wso2.xkms2.KeyUsage;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
/*
 * 
 */

public class Messages {

    public static OMElement createDocSigLocateRequest() throws Exception {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace ns = fac.createOMNamespace(XKMS2Constants.XKMS2_NS, "");
        OMNamespace emptyNs = fac.createOMNamespace("", "");

        OMElement locateRequestEle = fac.createOMElement("LocateRequest", ns);
        locateRequestEle.declareNamespace(Constants.SignatureSpecNS, "ds");
        locateRequestEle.declareNamespace("http://www.w3.org/2001/04/xmlenc#", "xenc");
        locateRequestEle.addAttribute("Id", "I045c66f6c525a9bf3842ecd3466cd422", emptyNs);
        locateRequestEle.addAttribute("Service", "http://www.example.org/XKMS", emptyNs);

        OMElement respondWithEle = fac.createOMElement("RespondWith", ns);
        respondWithEle.setText(RespondWith.KEY_VALUE.getAnyURI());
        locateRequestEle.addChild(respondWithEle);

        OMElement queryBindingEle = fac.createOMElement("QueryKeyBinding", ns);

        locateRequestEle.addChild(queryBindingEle);

        OMElement kiEle = getKIElement();
        queryBindingEle.addChild(kiEle);

        OMElement keyUsageEle = fac.createOMElement("KeyUsage", ns);
        keyUsageEle.setText(KeyUsage.SIGNATURE.getAnyURI());
        queryBindingEle.addChild(keyUsageEle);

        return locateRequestEle;
    }

    private static OMElement getKIElement() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        InputStream fis = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("./org/wso2/xkms/kiss/kiss-store.jks");

        ks.load(fis, "xkms-kiss".toCharArray());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);


        javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
        org.w3c.dom.Document doc = db.newDocument();
        KeyInfo ki = new KeyInfo(doc);

        doc.appendChild(ki.getElement());

        X509Certificate cert = (X509Certificate) ks.getCertificate("kiss");
        X509Data x509Data = new X509Data(doc);
        x509Data.addCertificate(cert);
        ki.add(x509Data);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();


        XKMSUtil.outputDOM(doc, bos);

        XMLStreamReader reader = XMLInputFactory.newInstance()
                .createXMLStreamReader(new ByteArrayInputStream(bos.toByteArray()));
        StAXOMBuilder builder = new StAXOMBuilder(reader);
        OMElement kiEle = builder.getDocumentElement();
        kiEle.build();

        return kiEle;

    }
}
