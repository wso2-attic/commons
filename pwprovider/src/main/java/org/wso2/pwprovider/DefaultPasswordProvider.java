package org.wso2.pwprovider;
/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import sun.misc.BASE64Decoder;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Properties;

/**
 * DefaultPasswordProvider DefaultPasswordProvider - This is just for demo
 */
public class DefaultPasswordProvider extends Thread implements PasswordProvider {

    private static Log log = LogFactory.getLog(DefaultPasswordProvider.class);
    private static String keyStorePassWord;
    private static String privateKeyPassWord;
    private static String keyStoreName;
    private static String keyAlias;
    private static String keyType;
    private static String carbonHome;
    private static File keyDataFile;
    private boolean stopThread = false;

    public void init(Properties properties) {

    }


    /**
     * Encrypted password value would be decrypted using primary key of carbon server
     * @param  encryptedPassword  password which has been encrypted
     * @return decryptedPassword  
     */
    public String resolve(String encryptedPassword) {

        log.info("Password is decrypted using DefaultPasswordProvider");

        final String xpathOfKeyLocation = "//KeyStore/KeyStoreName";
        final String xpathOfType = "//KeyStore/Type";
        final String xpathOfAlias = "//KeyStore/KeyAlias";
        final String xpathOfKeyStorePass = "//KeyStore/KeyStorePassword";
        final String xpathOfKeyPass = "//KeyStore/PrivateKeyPassword";

        final String xpathOfPrimaryKeyLocation = "//Server/Security/KeyStore/Location";
        final String xpathOfPrimaryKeyType = "//Server/Security/KeyStore/Type";
        final String xpathOfPrimaryKeyAlias = "//Server/Security/KeyStore/KeyAlias";

        String textFileName;
        String textFileName_tmp;
        boolean samePrivateKeyStorePass ;
        carbonHome = System.getProperty("carbon.home");
        String osName = System.getProperty("os.name");
        if(osName.toLowerCase().indexOf("win") == -1){
            textFileName = "password";
            textFileName_tmp = "password-tmp";
        } else {
            textFileName = "password.txt";
            textFileName_tmp = "password-tmp.txt";               
        }

        String keyStorePrivateKeyPass = System.getProperty("private.keyStore.password");
        if(keyStorePrivateKeyPass != null && keyStorePrivateKeyPass.trim().equals("false")) {
            samePrivateKeyStorePass = false;                            
        } else {
            samePrivateKeyStorePass = true;
        }
        keyDataFile = new File(carbonHome + File.separator +
                        "resources" + File.separator +"security" + File.separator + textFileName);
        
        DefaultPasswordProvider passwordHideThread = new DefaultPasswordProvider();
        if(keyStorePassWord == null || keyStorePassWord.equals("") ||
                privateKeyPassWord == null || privateKeyPassWord.equals("") ){
            if(keyDataFile.exists()){
                keyStorePassWord = readPassword(keyDataFile,"keyStore");
                privateKeyPassWord = readPassword(keyDataFile,"privateKey");
                if(keyStorePassWord != null && !keyStorePassWord.equals("")){
                    if (privateKeyPassWord == null || privateKeyPassWord.equals("")){
                        privateKeyPassWord = keyStorePassWord;
                    }
                }
                keyStoreName = getPrimaryKeyData(xpathOfPrimaryKeyLocation);
                keyAlias = getPrimaryKeyData(xpathOfPrimaryKeyAlias);
                keyType = getPrimaryKeyData(xpathOfPrimaryKeyType);
                keyStoreName = carbonHome + keyStoreName.
                        substring((keyStoreName.indexOf('}'))+1);
                if(! renameConfigFile(textFileName_tmp)){
                    try {
                        throw new PasswordProviderException("Can not rename Password config" +
                                " File");
                    } catch (PasswordProviderException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                keyDataFile = new File(carbonHome + File.separator+
                    "resources" + File.separator+"security" + File.separator
                        + textFileName_tmp);
                if(keyDataFile.exists()){
                    keyStorePassWord = readPassword(keyDataFile,"keyStore");
                    privateKeyPassWord = readPassword(keyDataFile,"privateKey");
                    if(keyStorePassWord != null && !keyStorePassWord.equals("")){
                        if (privateKeyPassWord == null || privateKeyPassWord.equals("")){
                            privateKeyPassWord = keyStorePassWord;
                        }
                    }
                    keyStoreName = getPrimaryKeyData(xpathOfPrimaryKeyLocation);
                    keyAlias = getPrimaryKeyData(xpathOfPrimaryKeyAlias);
                    keyType = getPrimaryKeyData(xpathOfPrimaryKeyType);
                    keyStoreName = carbonHome + keyStoreName.
                            substring((keyStoreName.indexOf('}'))+1);
                    if (! deleteConfigFile()) {
                        try {
                            throw new PasswordProviderException("Can not delete Password config " +
                                    "File");
                        } catch (PasswordProviderException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    keyDataFile = new File(carbonHome + File.separator+ "resources"
                            + File.separator + "security" + File.separator + "key-password.xml");
                    if(keyDataFile.exists()){
                        keyStorePassWord = getDataFromConfigFile(keyDataFile,
                                xpathOfKeyStorePass);
                        privateKeyPassWord = getDataFromConfigFile(keyDataFile,
                                xpathOfKeyPass);
                        keyStoreName = getDataFromConfigFile(keyDataFile, xpathOfKeyLocation);
                        keyType = getDataFromConfigFile(keyDataFile, xpathOfType);
                        keyAlias = getDataFromConfigFile(keyDataFile, xpathOfAlias);
                        if(! renameConfigFile("key-password-tmp.xml")){
                            try {
                                throw new PasswordProviderException("Can not rename Password " +
                                        "config File");
                            } catch (PasswordProviderException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        keyDataFile = new File(carbonHome + File.separator+
                            "resources"+ File.separator + "security" + File.separator
                                + "key-password-tmp.xml");
                        if(keyDataFile.exists()){
                            keyStorePassWord = getDataFromConfigFile(keyDataFile,
                                    xpathOfKeyStorePass);
                            privateKeyPassWord = getDataFromConfigFile(keyDataFile,
                                    xpathOfKeyPass);
                            keyStoreName = getDataFromConfigFile(keyDataFile,
                                    xpathOfKeyLocation);
                            keyType = getDataFromConfigFile(keyDataFile, xpathOfType);
                            keyAlias = getDataFromConfigFile(keyDataFile, xpathOfAlias);
                            if (! deleteConfigFile()) {
                                try {
                                    throw new PasswordProviderException("Can not delete Password" +
                                            " config File");
                                } catch (PasswordProviderException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            passwordHideThread.start();
                            BufferedReader input1 = new BufferedReader(new InputStreamReader(System.in));
                            if(samePrivateKeyStorePass){
                                log.info("Enter Primary KeyStore and Private Key Password of " +
                                        "Carbon Server :");
                                try {
                                    keyStorePassWord = input1.readLine();
                                    stopThread = false;
                                    privateKeyPassWord = keyStorePassWord ;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                log.info("Enter Primary KeyStore Password of Carbon Server :");
                                try {
                                    keyStorePassWord = input1.readLine();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                BufferedReader input2 = new BufferedReader(new InputStreamReader(System.in));
                                log.info("Enter Private Key Password of Carbon Server :");
                                try {
                                    privateKeyPassWord = input2.readLine();
                                    stopThread = false;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }

            }
        }
        
        if (keyStoreName == null || keyStoreName.equals("")){
            keyStoreName = getPrimaryKeyData(xpathOfPrimaryKeyLocation);
            keyStoreName = carbonHome + keyStoreName.substring((keyStoreName.indexOf('}'))+1);
        }
        if (keyAlias == null || keyAlias.equals("")){
            keyAlias = getPrimaryKeyData(xpathOfPrimaryKeyAlias);
        }
        if (keyType == null || keyType.equals("")){
            keyType = getPrimaryKeyData(xpathOfPrimaryKeyType);
        }

        byte [] decrypted = null;
        PrivateKey privateKey = null;
        Cipher cipher = null;
        FileInputStream in = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(keyType);
            in = new FileInputStream(keyStoreName);
            keyStore.load(in, keyStorePassWord.toCharArray());
            privateKey = (PrivateKey) keyStore.getKey(keyAlias,
                                                        privateKeyPassWord.toCharArray());
        } catch (Exception e) {
            handleException("Error loading key store for decrypting " +
                        "passwords in config files ", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
               handleException("Error closing input stream of keystore file");
            }
        }

        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (InvalidKeyException e) {
            handleException("Error initializing Cipher ");
        } catch (NoSuchAlgorithmException e) {
            handleException("Error initializing Cipher ");
        } catch (NoSuchPaddingException e) {
            handleException("Error initializing Cipher ");
        }

        if (encryptedPassword == null) {
            handleException("Error encrypted data can not be null ");
        }
        if (privateKey == null) {
            handleException("Error private key can not be null ");
        }

        try {
            decrypted= cipher.doFinal(new BASE64Decoder().decodeBuffer(encryptedPassword));
        } catch (Exception e) {
            handleException("Error occurred when decrypting encrypted value");
        }
        return new String(decrypted);
    }

    private String getDataFromConfigFile(File fileName, String xpath){

        String nodeValue = null;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(fileName);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath xp = xpf.newXPath();
            XPathExpression xpathEx = xp.compile(xpath);

            Node text = (Node) xpathEx.evaluate(doc.getDocumentElement(), XPathConstants.NODE);
            nodeValue = text.getTextContent();
        } catch (Exception e) {
            handleException("Error reading key store data from key-password.xml file ", e);    
        }
        return nodeValue;
    }
    
    private String getPrimaryKeyData(String xpath){

        String carbonConfigFile = carbonHome + File.separator+ "repository" +
                File.separator + "conf" + File.separator + "carbon.xml";
        String nodeValue = null;
        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(carbonConfigFile);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath xp = xpf.newXPath();
            XPathExpression xpathEx = xp.compile(xpath);

            Node text = (Node) xpathEx.evaluate(doc.getDocumentElement(), XPathConstants.NODE);
            nodeValue = text.getTextContent();
        }catch (Exception e){
            handleException("Error reading primary key store data from carbon.xml file ", e);
        }
        return nodeValue;
    }

    private String readPassword(File file, String passwordOf){

        String stringLine = null;
        FileInputStream inputStream = null;
        try{
            inputStream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            if(passwordOf.equals("keyStore")){
                stringLine = br.readLine();
            }
            if(passwordOf.equals("privateKey")){
                stringLine = br.readLine();
                stringLine = br.readLine();    
            }
        }catch (Exception e){
            handleException("Error reading password from text file ", e);
        } finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
               handleException("Error closing input stream of keystore file");
            }
        }
        return stringLine;
    }

    private boolean deleteConfigFile(){

        return keyDataFile.exists() && keyDataFile.delete();
        
    }

    private boolean renameConfigFile(String fileName){
         if (keyDataFile.exists()){
             File newConfigFile = new File(carbonHome + File.separator+
                         "resources" + File.separator + "security" + File.separator + fileName);
             return keyDataFile.renameTo(newConfigFile);
         }
         return false;
    }

    public void run () {
        stopThread = true;
        try {
            sleep(100);
            while (stopThread) {
                System.out.print("\b ");
                sleep(1);
            }   
        } catch(InterruptedException ie) {
            ie.printStackTrace();
        }

    }

    private static void handleException(String msg, Exception e) {
        log.error(msg, e);
        throw new PasswordProviderException(msg, e);
    }

    private static void handleException(String msg) {
        log.error(msg);
        throw new PasswordProviderException(msg);
    }
}
