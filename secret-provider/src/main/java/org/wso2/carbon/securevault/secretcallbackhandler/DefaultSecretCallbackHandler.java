/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.securevault.secretcallbackhandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.securevault.secret.AbstractSecretCallbackHandler;
import org.apache.synapse.securevault.secret.SingleSecretCallback;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This is the default secret call back handler class, shipped with carbon distribution which extends the
 * AbstractSecretCallbackHandler class in synapse secure vault
 *
 * This class retrieves the password of key store and private key as command line input when carbon
 * server is start-up.
 *
 * When server is started as a daemon, this class searches for a simple test file called "password" and read
 * the password from there and delete it.
 *
 * By default, this class assumes that private and key store passwords are same. By passing the
 * key.password=true as a system property in server start-up, default behaviour can be changed
 */
public class DefaultSecretCallbackHandler extends AbstractSecretCallbackHandler {
    
    private static Log log = LogFactory.getLog(DefaultSecretCallbackHandler.class);

    private static final String KEY_PASSWORD = "key.password";
    private static final String CARBON_HOME = "carbon.home";
    private static String keyStorePassWord;
    private static String privateKeyPassWord;
    private static File keyDataFile;

    public void handleSingleSecretCallback(SingleSecretCallback singleSecretCallback) {

        if(keyStorePassWord == null && privateKeyPassWord == null){

            String textFileName;
            String textFileName_tmp;
            boolean sameKeyAndKeyStorePass = true;

            String carbonHome = System.getProperty(CARBON_HOME);

            String osName = System.getProperty("os.name");
            if(osName.toLowerCase().indexOf("win") == -1){
                textFileName = "password";
                textFileName_tmp = "password-tmp";
            } else {
                textFileName = "password.txt";
                textFileName_tmp = "password-tmp.txt";
            }

            String keyPassword = System.getProperty(KEY_PASSWORD);
            if(keyPassword != null && keyPassword.trim().equals("true")){
                sameKeyAndKeyStorePass = false;
            }

            keyDataFile = new File(carbonHome + File.separator + textFileName);

            if(keyDataFile.exists()){
                keyStorePassWord = readPassword(keyDataFile, sameKeyAndKeyStorePass);
                if(sameKeyAndKeyStorePass){
                    privateKeyPassWord = keyStorePassWord;
                } else {
                    privateKeyPassWord = readPassword(keyDataFile, sameKeyAndKeyStorePass);
                }
                if(! renameConfigFile(textFileName_tmp)){
                    handleException("Error renaming Password config File");
                }

            } else {
                keyDataFile = new File(carbonHome + File.separator + textFileName_tmp);
                if(keyDataFile.exists()){
                    keyStorePassWord = readPassword(keyDataFile, sameKeyAndKeyStorePass);
                    if(sameKeyAndKeyStorePass){
                        privateKeyPassWord = keyStorePassWord;
                    } else {
                        privateKeyPassWord = readPassword(keyDataFile, sameKeyAndKeyStorePass);
                    }
                    if (!deleteConfigFile()) {
                        handleException("Error deleting Password config File");
                    }
                } else {
                    Console console;
                    char[] password;
                    if(sameKeyAndKeyStorePass){
                        if ((console = System.console()) != null && (password = console.readPassword("[%s]",
                                        "Enter KeyStore and Private Key Password :")) != null) {
                            keyStorePassWord = String.valueOf(password);
                            privateKeyPassWord= keyStorePassWord;
                        }
                    } else {
                        if ((console = System.console()) != null &&
                            (password = console.readPassword("[%s]",
                                                        "Enter KeyStore Password :")) != null) {
                            keyStorePassWord = String.valueOf(password);
                        }
                        if ((console = System.console()) != null &&
                            (password = console.readPassword("[%s]",
                                                        "Enter Private Key Password : ")) != null) {
                            privateKeyPassWord = String.valueOf(password);
                        }
                    }
                }
            }
        }
        if(singleSecretCallback.getId().equals("identity.key.password")){
            singleSecretCallback.setSecret(privateKeyPassWord);
        } else {
            singleSecretCallback.setSecret(keyStorePassWord);
        }
    }


    private String readPassword(File file, boolean sameKeyAndKeyStorePass){

        String stringLine = null;
        FileInputStream inputStream = null;
        try{
            inputStream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            if(sameKeyAndKeyStorePass){
                stringLine = br.readLine();
            } else {
                stringLine = br.readLine();
                stringLine = br.readLine();
            }
            br.close();
        }catch (Exception e){
            handleException("Error reading password from text file ", e);
        } finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
               handleException("Error closing input stream of keyStore file");
            }
        }
        return stringLine;
    }

    private boolean deleteConfigFile(){

        return keyDataFile.exists() && keyDataFile.delete();
    }

    private boolean renameConfigFile(String fileName){
         if (keyDataFile.exists()){
             File newConfigFile = new File(System.getProperty(CARBON_HOME) + File.separator + fileName);
             return keyDataFile.renameTo(newConfigFile);
         }
         return false;
    }

    private static void handleException(String msg, Exception e) {
        log.error(msg, e);
        throw new SecretCallbackHandlerException(msg, e);
    }

    private static void handleException(String msg) {
        log.error(msg);
        throw new SecretCallbackHandlerException(msg);
    }
}
