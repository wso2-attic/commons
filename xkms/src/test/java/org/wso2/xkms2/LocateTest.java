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
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.impl.dom.DOOMAbstractFactory;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.utils.Constants;
import org.wso2.xkms2.builder.LocateRequestBuilder;
import org.wso2.xkms2.builder.LocateResultBuilder;
import org.wso2.xkms2.util.Messages;
import org.wso2.xkms2.util.XKMSUtil;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
/*
 * 
 */

public class LocateTest extends TestCase {

    public void setUp() throws java.lang.Exception {
        org.apache.xml.security.Init.init();
    }

    public void testDocSigLocateRequest() throws Exception {

        OMElement locateRequestEle = Messages.createDocSigLocateRequest();

        XKMSUtil.DOOMElementMetadata doomMD = XKMSUtil.getDOOMElement(locateRequestEle);
        locateRequestEle = doomMD.getElement();

        LocateRequestBuilder builder = new LocateRequestBuilder();
        LocateRequest locateRequest = (LocateRequest)builder.buildElement(locateRequestEle);


        assertNotNull(locateRequest.getId());
        assertNotNull(locateRequest.getQueryKeyBinding());
        assertNotNull(locateRequest.getQueryKeyBinding().getKeyInfo());
        assertNotNull(locateRequest.getQueryKeyBinding().getKeyUsage());

        /*serializing the envelop */

        OMFactory fac = DOOMAbstractFactory.getOMFactory();

        OMElement ele = locateRequest.serialize(fac);


        assertNotNull(ele);


    }

    public void testDocSigLocateResult() throws Exception {
        OMFactory fac = DOOMAbstractFactory.getOMFactory();
        OMNamespace ns = fac.createOMNamespace(XKMS2Constants.XKMS2_NS, "");
        OMNamespace emptyNs = fac.createOMNamespace("", "");

        OMElement locateResultEle = fac.createOMElement("LocateResult",ns);
        locateResultEle.declareNamespace(Constants.SignatureSpecNS,"ds");
        locateResultEle.declareNamespace("http://www.w3.org/2001/04/xmlenc#","xenc");
        locateResultEle.addAttribute("Id","I04cd4f17d0656413d744f55488369264",emptyNs);
        locateResultEle.addAttribute("Service","http://www.example.org/XKMS",emptyNs);
        locateResultEle.addAttribute("ResultMajor",ResultMajor.SUCCESS.getAnyURI(),emptyNs);
        locateResultEle.addAttribute("RequestId","I045c66f6c525a9bf3842ecd3466cd422",emptyNs);

        OMElement unverifiedKeyBindingEle = fac.createOMElement("UnverifiedKeyBinding",ns);
        locateResultEle.addChild(unverifiedKeyBindingEle);
        OMElement kiEle = getKIResultElement();
        unverifiedKeyBindingEle.addChild(kiEle);

        OMElement keyUsageEle1 = fac.createOMElement("KeyUsage",ns);
        keyUsageEle1.setText(KeyUsage.SIGNATURE.getAnyURI());
        OMElement keyUsageEle2 = fac.createOMElement("KeyUsage",ns);
        keyUsageEle2.setText(KeyUsage.ENCRYPTION.getAnyURI());
        OMElement keyUsageEle3 = fac.createOMElement("KeyUsage",ns);
        keyUsageEle3.setText(KeyUsage.EXCHANGE.getAnyURI());

        unverifiedKeyBindingEle.addChild(keyUsageEle1);
        unverifiedKeyBindingEle.addChild(keyUsageEle2);
        unverifiedKeyBindingEle.addChild(keyUsageEle3);


        LocateResult locateResult = (LocateResult) LocateResultBuilder.INSTANCE.buildElement(locateResultEle);

        assertNotNull(locateResult.getId());
        assertNotNull(locateResult.getUnverifiedKeyBindingList());

        fac = DOOMAbstractFactory.getOMFactory();

        OMElement ele = locateResult.serialize(fac);

        assertNotNull(ele);



    }

    private OMElement getKIResultElement() throws Exception{
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
        ki.addKeyValue(cert.getPublicKey());

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
