/*
 * Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.utils.security;

import org.apache.axis2.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.wso2.utils.i18n.Messages;
import org.wso2.utils.security.CryptoException;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

/**
 * The utility class to encrypt/decrypt passwords to be stored in the
 * database.
 */
public class CryptoUtil {

    private String storeLocation;
    private String storePass;
    private String keyAlias;
    private String keyPass;
    private String storeType;


    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public CryptoUtil(String storeLocation,
                      String storePass,
                      String keyAlias,
                      String keyPass,
                      String storeType) {

        this.storeLocation = storeLocation;
        this.storePass = storePass;
        this.keyAlias = keyAlias;
        this.keyPass = keyPass;
        this.storeType = storeType;
    }

    /**
     * Encrypt a given plain text
     *
     * @param plainTextBytes The plaintext bytes to be encrypted
     * @return The cipher text bytes
     * @throws CryptoException On error during encryption
     */
    public byte[] encrypt(byte[] plainTextBytes) throws CryptoException {
        try {
            KeyStore keyStore = KeyStore.getInstance((storeType != null)
                                                     ? storeType : "PKCS12");

            FileInputStream fis = new FileInputStream(storeLocation);
            keyStore.load(fis, storePass.toCharArray());
            Certificate[] certs = keyStore.getCertificateChain(keyAlias);
            Cipher cipher = Cipher.getInstance("RSA", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, certs[0]);

            return cipher.doFinal(plainTextBytes);

        } catch (Exception e) {
            e.printStackTrace();
            throw new
                    CryptoException(Messages.getMessage("erorDuringEncryption"), e);
        }
    }

    /**
     * Encrypt the given plain text and base64 encode the encrypted content.
     *
     * @param plainText The plaintext value to be encrypted and base64
     *                  encoded
     * @return The base64 encoded cipher text
     * @throws CryptoException On error during encryption
     */
    public String encryptAndBase64Encode(byte[] plainText) throws
                                                           CryptoException {
        return Base64.encode(encrypt(plainText));
    }

    /**
     * Decrypt the given cipher text value using the WSO2 WSAS key
     *
     * @param cipherTextBytes The cipher text to be decrypted
     * @return Decrypted bytes
     * @throws CryptoException On an error during decryption
     */
    public byte[] decrypt(byte[] cipherTextBytes) throws CryptoException {
        try {
            KeyStore keyStore = KeyStore
                    .getInstance((storeType != null) ? storeType : "JKS");

            FileInputStream fis = new FileInputStream(storeLocation);
            keyStore.load(fis, storePass.toCharArray());
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias,
                                                        keyPass.toCharArray());

            Cipher cipher = Cipher.getInstance("RSA", "BC");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            return cipher.doFinal(cipherTextBytes);

        } catch (Exception e) {
            e.printStackTrace();
            throw new CryptoException("errorDuringDecryption", e);
        }
    }

    /**
     * Base64 decode the given value and decrypt using the WSO2 WSAS key
     *
     * @param base64CipherText Base64 encoded cipher text
     * @return Base64 decoded, decrypted bytes
     * @throws CryptoException On an error during decryption
     */
    public byte[] base64DecodeAndDecrypt(String base64CipherText) throws
                                                                  CryptoException {
        return decrypt(Base64.decode(base64CipherText));
    }
}

