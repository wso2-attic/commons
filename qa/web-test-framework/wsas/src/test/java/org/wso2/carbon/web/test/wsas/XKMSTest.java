/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;
import java.util.List;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.Key;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;
import org.wso2.xkms2.builder.RegisterResultBuilder;
import org.wso2.xkms2.util.XKMSUtil;
import org.wso2.xkms2.util.XKMSKeyUtil;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.dom.DOOMAbstractFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.wso2.xkms2.*;
import org.apache.xml.security.keys.KeyInfo;
import javax.xml.stream.XMLInputFactory;
import org.apache.xml.security.keys.content.KeyName;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;

public class XKMSTest extends TestCase {
     Selenium browser;
    Properties property;
    String username;
    String password;
    static String XKMS_SERVICE_URL;
    static String PASS_PHRASE = "secret";

    public XKMSTest(String s){
        super(s);
    }


    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
    }



    public void testSetParametersForXKMS() throws Exception{
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        XKMS_SERVICE_URL="http://" +property.getProperty("host.name") + ":" + property.getProperty("http.be.port") + property.getProperty("context.root")+ "/services/XKMS";
        File keyStorePath = new File("." + File.separator + "lib" + File.separator + "keystore.jks");

        InstSeleniumTestBase.loginToUI( username, password );
        instServiceManagement.accessServiceDashboard("XKMS");

        browser.click("link=Parameters");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Service Parameters (Service: XKMS)"));
        browser.type("org.wso2.xkms2.service.crypto.persistence.enabled", "false");
        browser.type("org.wso2.xkms2.service.crypto.authen.code", "secret");
        browser.type("org.wso2.xkms2.service.crypto.keystore.password", "password");
        browser.type("org.wso2.xkms2.service.crypto.default.expriy.interval", "365");
        browser.type("org.wso2.xkms2.service.crypto.server.key.password", "password");
        browser.type("org.wso2.xkms2.service.crypto.issuer.key.password", "password");
        browser.type("org.wso2.xkms2.service.crypto.default.private.key.password", "password");
        browser.type("org.wso2.xkms2.service.crypto.keystore.location",keyStorePath.getCanonicalPath() );
        browser.type("org.wso2.xkms2.service.crypto.issuer.cert.aliase", "alice");
        browser.type("org.wso2.xkms2.service.crypto.server.cert.aliase", "bob");
        browser.click("updateBtn");
        browser.waitForPageToLoad("30000");
    }

    private static OMElement getAsOMElement(RegisterRequest registerRequest)
            throws Exception {
        return registerRequest.serialize(DOOMAbstractFactory.getOMFactory());
    }

    private static RegisterResult getRegisterResult(OMElement registerResult)
            throws Exception {
        return (RegisterResult) RegisterResultBuilder.INSTANCE
                .buildElement(registerResult);
    }

    public void testXKMSRegistrationServcie() throws Exception{
        org.apache.xml.security.Init.init();

        KeyPair keyPair = generateRSAKeyPair();
        RegisterRequest request=createRegisterRequest();
        request.setServiceURI(XKMS_SERVICE_URL);

        // setting the authentication info
        Authentication authentication = createAuthenticate();
        Key authKey = XKMSKeyUtil.getAuthenticationKey(PASS_PHRASE);
        authentication.setKeyBindingAuthenticationKey(authKey);
        request.setAuthentication(authentication);

        // setting the Keybinding info
        PrototypeKeyBinding keyBinding = createPrototypeKeyBinding();

        // setting the public key to which an X509Certificate should be issued.
        keyBinding.setKeyValue(keyPair.getPublic());

        // setting usages of the key
        keyBinding.addKeyUsage(KeyUsage.SIGNATURE);
        keyBinding.addUseKeyWith(UseKeyWith.PKIX,
                "C=US, ST=NY, L=NYC, O=Yahoo, OU=XKMS, CN=Motukuru");
        request.setPrototypeKeyBinding(keyBinding);

        // setting private key as proof that the client pocess both public and private keys
        request.setProofOfPocessionKey(keyPair.getPrivate());

        request.addRespondWith(RespondWith.KEY_NAME);
        request.addRespondWith(RespondWith.KEY_VALUE);
        request.addRespondWith(RespondWith.X_509_CERT);

        OMElement element = getAsOMElement(request);
        OMElement result =sendReceive(element, XKMS_SERVICE_URL);
        result = buildElement(result);

        RegisterResult registerResult = getRegisterResult(result);
        List keybindings = registerResult.getKeyBindings();
        KeyBinding keybinding = (KeyBinding) keybindings.get(0);

        KeyInfo keyInfo = keybinding.getKeyInfo();
        KeyName keyName = keyInfo.itemKeyName(0);

        if (keyName != null) {
            System.out.println("KeyName : " + keyName.getKeyName());
        }else{
            assertTrue("KeyName Not Found ",false);
        }

        X509Certificate cert = keyInfo.getX509Certificate();
        if (cert != null) {
            System.out.println("X509Certificate : ");
            printCert(cert);
        }else{
            assertTrue("Cert Not Found ",false);
        }
    }

    public void testlogOutXKMSTest() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }



    public static KeyPair generateRSAKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            return keyGen.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static RegisterRequest createRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setId(XKMSUtil.getRamdomId());
        return request;
    }

    public static Authentication createAuthenticate() {
        Authentication authentication = new Authentication();
        return authentication;
    }

    public static PrototypeKeyBinding createPrototypeKeyBinding() {
        PrototypeKeyBinding keyBinding = new PrototypeKeyBinding();
        keyBinding.setId(XKMSUtil.getRamdomId());
        return keyBinding;
    }

    public static OMElement sendReceive(OMElement element, String serviceURL)
            throws AxisFault {

        ServiceClient client = new ServiceClient();

        Options options = client.getOptions();
        EndpointReference epr = new EndpointReference(serviceURL);
        options.setTo(epr);

        options.setProperty(HTTPConstants.CHUNKED, Boolean.FALSE);
        options.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

        OMElement result = client.sendReceive(element);
        return result;
    }

    public static OMElement buildElement(OMElement element) throws Exception {
        String str = element.toString();
        ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes());

        StAXOMBuilder builder = new StAXOMBuilder(DOOMAbstractFactory
                .getOMFactory(), XMLInputFactory.newInstance()
                .createXMLStreamReader(bais));

        return builder.getDocumentElement();
    }

    public static void printCert(X509Certificate cert) {
        System.out
                .println("--------------- Cert Info ----------------------------");
        X500Principal subjectX500Principal = cert.getSubjectX500Principal();
        System.out.println("SubjectDN : " + subjectX500Principal.getName());
        System.out.println("Not After : " + cert.getNotAfter());
        System.out.println("Not Before : " + cert.getNotBefore());
        System.out.println("Public Key :\n" + cert.getPublicKey());
        X500Principal issuerX500Principal = cert.getIssuerX500Principal();
        System.out.println("IssuerDN : " + issuerX500Principal.getName());
        System.out.println("IssuerSerial : " + cert.getSerialNumber());
        System.out
                .println("--------------- End Cert Info -------------------------");

    }

}
