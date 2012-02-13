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
package org.wso2.utils.security;

import java.security.KeyStore;
import java.security.cert.Certificate;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

/**
 * A utility that allows importing a certificate from one keystore to another
 */
public class KeyImporter {
    /**
     * sourcekeystore sourceStorepass keyalias targetstore targetStorePass
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Importing certificate ...");
        if (args.length != 5) {
            throw new Exception("Incorrect number of parameters");
        }

        try {
            String sourceStorePath = args[0];
            String sourceStorePass = args[1];
            String keyAlias = args[2];
            String targetStorePath = args[3];
            String targetStorePass = args[4];

            KeyStore sourceStore = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(new File(sourceStorePath).getAbsolutePath());
            sourceStore.load(fis, sourceStorePass.toCharArray());

            Certificate cert = sourceStore.getCertificateChain(keyAlias)[0];
            KeyStore targetStore = KeyStore.getInstance("JKS");

            File targetStoreFile = new File(targetStorePath);
            if(targetStoreFile.exists()) {
                targetStore.load(new FileInputStream(targetStoreFile
                        .getAbsolutePath()), targetStorePass.toCharArray());
            } else {
                targetStore.load(null, null);
            }
            targetStore.setCertificateEntry(keyAlias, cert);
            FileOutputStream fileOutputStream =
                    new FileOutputStream(new File(targetStorePath).getAbsolutePath());
            targetStore.store(fileOutputStream, targetStorePass.toCharArray());

            fis.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            System.out.println("Importing certificate ... DONE !");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Importing of key failed");
            throw e;
        }
    }
}
