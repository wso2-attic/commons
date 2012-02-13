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

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.xml.security.encryption.XMLCipher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wso2.xkms2.builder.RSAKeyPairTypeBuilder;
import org.wso2.xkms2.util.XKMSUtil;

public class PrivateKey implements XKMSElement, ElementSerializable {

    private Element encryptedData;

    private RSAKeyPair rsaKeyPair;

    private Key key;

    public void setRSAKeyPair(KeyPair keyPair) {
        java.security.PublicKey public1 = keyPair.getPublic();
        java.security.PrivateKey private1 = keyPair.getPrivate();

        PublicKey publicKey = public1;
        if (!(publicKey instanceof RSAPublicKey && private1 instanceof RSAPrivateCrtKey)) {
            throw new IllegalArgumentException(
                    "KeyPair contains invalid key types. RSAPublicKey and RSAPrivateCrtKey are expected instead of "
                            + publicKey.getClass().getName()
                            + " and "
                            + private1.getClass().getName());
        }

        RSAPublicKey crtPubKey = (RSAPublicKey) public1;
        RSAPrivateCrtKey crtPriKey = (RSAPrivateCrtKey) private1;

        rsaKeyPair = new RSAKeyPair();

        rsaKeyPair.setModulus(crtPubKey.getModulus().toByteArray());
  
        rsaKeyPair.setExponent(crtPriKey.getPublicExponent().toByteArray());
        rsaKeyPair.setP(crtPriKey.getPrimeP().toByteArray());
        rsaKeyPair.setQ(crtPriKey.getPrimeQ().toByteArray());
        rsaKeyPair.setDP(crtPriKey.getPrimeExponentP().toByteArray());
        rsaKeyPair.setDQ(crtPriKey.getPrimeExponentQ().toByteArray());
        rsaKeyPair.setInverseQ(crtPriKey.getCrtCoefficient().toByteArray());
        rsaKeyPair.setD(crtPriKey.getPrivateExponent().toByteArray());
    }

    public KeyPair getRSAKeyPair() {
        if (rsaKeyPair == null) {
            if (encryptedData != null && key != null) {
                try {
                    decryptData(key, encryptedData.getOwnerDocument());
                } catch (XKMSException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            } else {
                return null;
            }
        }
        
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(
                new BigInteger(rsaKeyPair.getModulus()), new BigInteger(
                        rsaKeyPair.getExponent()));
        
        RSAPrivateCrtKeySpec rsaPrivateCrtKeySpec = new RSAPrivateCrtKeySpec(
                new BigInteger(rsaKeyPair.getModulus()), 
                new BigInteger(rsaKeyPair.getExponent()),
                new BigInteger(rsaKeyPair.getD()),
                new BigInteger(rsaKeyPair.getP()),
                new BigInteger(rsaKeyPair.getQ()),
                new BigInteger(rsaKeyPair.getDP()),
                new BigInteger(rsaKeyPair.getDQ()),
                new BigInteger(rsaKeyPair.getInverseQ()));

        KeyFactory factory;

        try {
            factory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        java.security.PublicKey public1;
        java.security.PrivateKey private1;
        try {
            public1 = factory.generatePublic(rsaPublicKeySpec);
            private1 = factory.generatePrivate(rsaPrivateCrtKeySpec);
            return new KeyPair(public1, private1);

        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public Element getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(Element encryptedData) {
        this.encryptedData = encryptedData;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    public void createEncryptedData(String algorithm, Key key, Element element)
            throws XKMSException {
        try {

            Document doc = element.getOwnerDocument();
            XMLCipher xmlCipher = XMLCipher.getInstance(algorithm);
            xmlCipher.init(XMLCipher.ENCRYPT_MODE, key);
            xmlCipher.doFinal(doc, element, true);

        } catch (Exception e) {
            e.printStackTrace();
            throw new XKMSException(e);
        }
    }

    public void decryptData(Key key, Document doc) throws XKMSException {
        try {
            XMLCipher xmlCipher = XMLCipher.getInstance();
            xmlCipher.init(XMLCipher.DECRYPT_MODE, key);
            xmlCipher.doFinal(doc, encryptedData, true);

            OMElement rsaKeyPairElem = ((OMElement) encryptedData)
                    .getFirstChildWithName(XKMS2Constants.Q_ELEM_RSA_KEY_PAIR);
            rsaKeyPair = (RSAKeyPair) RSAKeyPairTypeBuilder.INSTANCE
                    .buildElement(rsaKeyPairElem);
            encryptedData = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new XKMSException(ex);
        }
    }

    public OMElement serialize(OMFactory factory) throws XKMSException {
        OMElement privateKeyElement = factory
                .createOMElement(XKMS2Constants.Q_ELEM_PRIVATE_KEY);

        if (key == null || rsaKeyPair == null) {
            throw new XKMSException(
                    "Either Encryption Key or RSAKeyPair is null");
        }

        Document doc = ((Element) privateKeyElement).getOwnerDocument();
        Element rsaKeyPairElem = rsaKeyPair.build(doc);
        ((Element) privateKeyElement).appendChild(rsaKeyPairElem);

        createEncryptedData(XMLCipher.TRIPLEDES, key,
                (Element) privateKeyElement);

        return privateKeyElement;
    }
}
