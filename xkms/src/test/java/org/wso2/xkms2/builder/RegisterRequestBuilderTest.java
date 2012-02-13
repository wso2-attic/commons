package org.wso2.xkms2.builder;

import java.security.Key;
import java.security.PublicKey;

import org.apache.axiom.om.OMElement;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.wso2.xkms2.Authentication;
import org.wso2.xkms2.PrototypeKeyBinding;
import org.wso2.xkms2.RegisterRequest;
import org.wso2.xkms2.XKMSException;
import org.wso2.xkms2.XKRRSTestCase;
import org.wso2.xkms2.util.XKMSUtil;

public class RegisterRequestBuilderTest extends XKRRSTestCase {

    private Key authKey = XKMSUtil.getAuthenticationKey("secret");

    public RegisterRequestBuilderTest() {
        super("RegisterRequestBuilderTest");
    }

    public void testBuilder() throws XKMSException, XMLSignatureException,
            KeyResolverException {
        OMElement registerRequestElem = getResourceAsElement("T1_RegisterRequest-http.xml");
        RegisterRequest registerRequest = (RegisterRequest) RegisterRequestBuilder.INSTANCE
                .buildElement(registerRequestElem);

        Authentication authentication = registerRequest.getAuthentication();
        XMLSignature keyBindingAuthentication = authentication
                .getKeyBindingAuthentication();
        assertTrue(keyBindingAuthentication.checkSignatureValue(authKey));

        PrototypeKeyBinding prototypeKeyBinding = registerRequest
                .getPrototypeKeyBinding();
        KeyInfo keyInfo = prototypeKeyBinding.getKeyInfo();
        PublicKey publicKey = keyInfo.getPublicKey();
        XMLSignature proofOfPossession = registerRequest.getProofOfPossession();
        assertTrue(proofOfPossession.checkSignatureValue(publicKey));
    }
}
