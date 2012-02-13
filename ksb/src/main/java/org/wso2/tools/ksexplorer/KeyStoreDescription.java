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

package org.wso2.tools.ksexplorer;

import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

public class KeyStoreDescription {

    private KeyStore keyStore;
    
    private String name;

    private String uuid;
    
    public KeyStoreDescription(KeyStore store, String name) {
        this.keyStore = store;
        this.name = name;
        this.uuid = UUID.randomUUID().toString();
    }
    
    public String getStoreType() {
        return this.keyStore.getType();
    }
    
    public List getPrivateKeys() throws Exception {
        ArrayList list = new ArrayList();
        
        Enumeration aliases = this.keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = (String) aliases.nextElement();
            if(this.keyStore.isKeyEntry(alias)) {
                list.add(new Cert(alias, (X509Certificate) this.keyStore
                        .getCertificate(alias)));
            }
        }
        
        return list;
    }

    public List getCertificates() throws Exception {
        ArrayList list = new ArrayList();
        
        Enumeration aliases = this.keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = (String) aliases.nextElement();
            if(this.keyStore.isCertificateEntry(alias)) {
                list.add(new Cert(alias, (X509Certificate) this.keyStore
                        .getCertificate(alias)));
            }
        }
        
        return list;
    }
    
    public String getAlias(Certificate cert) {
        return this.getAlias(cert);
    }
    
    public String getName() {
        return this.name;
    }
    
    class Cert {
        
        private String alias;
        private X509Certificate cert;
        
        public Cert(String alias, X509Certificate cert) {
            this.alias = alias;
            this.cert = cert;
        }

        public String getAlias() {
            return alias;
        }

        public X509Certificate getCert() {
            return cert;
        }
        
    }

    public String getUuid() {
        return uuid;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }
    
}
