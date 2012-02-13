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
package org.wso2.xkms2.util;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;

public class XKMSKeyUtil {
    static {
        Security
                .addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    private static byte[] KEY1 = new byte[] { 1 };

    private static byte[] KEY2 = new byte[] { 2 };

    private static byte[] KEY3 = new byte[] { 3 };

    private static byte[] KEY4 = new byte[] { 4 };

    public static Key getAuthenticationKey(String phrase) {
        byte[] binaryData = phrase.getBytes();
        return getAuthenticationKey(binaryData);
    }

    public static Key getPrivateKey(String phrase, String algorithm) {
        byte[] binaryData = phrase.getBytes();
        return getPivateKey(binaryData, algorithm);
    }

    private static Key getPivateKey(byte[] binaryData, String algorithm) {
        if ("DESede".equals(algorithm)) {
            return bytesToDESKey(privateKeyTransform(binaryData, 24));
        }

        throw new IllegalArgumentException("Invalid algorithm " + algorithm);
    }

    /**
     * 
     * @param binaryData
     * @return
     */
    private static Key getAuthenticationKey(byte[] binaryData) {
        return (bytesToKey(authenticationTransform(binaryData)));
    }

    /**
     * 
     * @param passcode
     * @return
     */
    private static byte[] authenticationTransform(byte[] passcode) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(bytesToKey(KEY1));
            mac.update(passcode);
            return mac.doFinal();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Can't generate Authentication Key", e);

        } catch (InvalidKeyException e) {
            throw new RuntimeException("Can't generate Authentication Key", e);
        }
    }

    private static Key bytesToKey(byte[] data) {
        return new SecretKeySpec(data, "RAW");
    }

    private static Key bytesToDESKey(byte[] data) {
        return new SecretKeySpec(data, "DESede");
    }

    public static X509Certificate getX509Certificate(String identifier,
            BigInteger serialNumber, Date startDate, Date expiryDate,
            PublicKey key, X509Certificate caCert, PrivateKey caKey) {
        return getX509Certificate(identifier, serialNumber, startDate,
                expiryDate, true, true, key, caCert, caKey);

    }

    public static X509Certificate getX509Certificate(String identifier,
            BigInteger serialNumber, Date startDate, Date expiryDate,
            boolean degitalSigning, boolean dataEncryption, PublicKey key,
            X509Certificate caCert, PrivateKey caKey) {

        try {

            X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
            X500Principal principal = new X500Principal(identifier);

//            certGen.setIssuerDN(PrincipalUtil.getSubjectX509Principal(caCert));
            certGen.setIssuerDN(new X509Name(PrincipalUtil.getSubjectX509Principal(caCert).getName()));
            certGen.setSerialNumber(serialNumber);
            certGen.setNotBefore(startDate);
            certGen.setNotAfter(expiryDate);

//            certGen.setSubjectDN(principal);
            certGen.setSubjectDN(new X509Name(principal.getName()));
            certGen.setPublicKey(key);
            certGen.setSignatureAlgorithm("SHA1withRSA");

            certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false,
                    new AuthorityKeyIdentifierStructure(caCert));
            certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false,
                    new SubjectKeyIdentifierStructure(key));

            // this are default values which will be supported.
            KeyUsage ku = buildKeyUsage(degitalSigning, dataEncryption);
            certGen.addExtension(X509Extensions.KeyUsage, false, ku);

            return certGen.generateX509Certificate(caKey, "BC");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static KeyPair generateRSAKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
            keyGen.initialize(1024);
            return keyGen.genKeyPair();
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] privateKeyTransform(byte[] passcode,
            int keysizeInBytes) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(bytesToKey(KEY4));
            mac.update(passcode);

            byte[] block_0 = mac.doFinal();
            byte[] output = null;

            if (block_0.length < keysizeInBytes) {

                byte[] block_n_1 = block_0;
                output = (byte[]) block_0.clone();

                byte[] block_n;

                while (output.length < keysizeInBytes) {

                    byte[] key = (byte[]) block_n_1.clone();
                    key[0] = (byte) (key[0] & (0xff ^ KEY4[0]));

                    mac.init(bytesToKey(key));
                    mac.update(passcode);

                    block_n = mac.doFinal();

                    byte[] output2 = new byte[output.length + block_n.length];

                    System.arraycopy(output, 0, output2, 0, output.length);
                    System.arraycopy(block_n, 0, output2, output.length,
                            block_n.length);

                    block_n_1 = block_n;
                    output = output2;
                }
            }

            byte[] result = new byte[keysizeInBytes];
            if (block_0.length > keysizeInBytes) {
                System.arraycopy(block_0, 0, result, 0, keysizeInBytes);
            } else {
                System.arraycopy(output, 0, result, 0, keysizeInBytes);
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyUsage buildKeyUsage(boolean digitalSigning,
            boolean dataEncryption) {

        int usage = 0;

        if (digitalSigning) {
            usage = KeyUsage.digitalSignature;
        }
        if (dataEncryption) {
            usage = usage | KeyUsage.dataEncipherment;
        }

        return new KeyUsage(usage);
    }

    public static void listKeyStoreInfo(KeyStore ks) {
        try {
            Enumeration aliases = ks.aliases();
            for (; aliases.hasMoreElements();) {
                String aliase = (String) aliases.nextElement();
                System.out.println(aliase
                        + (ks.isKeyEntry(aliase) ? " ->Key Entry "
                                : " -> Certificate Entry"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
