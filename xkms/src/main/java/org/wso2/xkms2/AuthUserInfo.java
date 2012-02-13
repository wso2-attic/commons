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

import org.apache.xml.security.signature.XMLSignature;

public class AuthUserInfo {
    
    private XMLSignature proofOfPocession;
    private XMLSignature keyBindingAuth;
    private String passPhraseAuth;
    
    public void setProofOfPocession(XMLSignature proofOfPocession) {
        this.proofOfPocession = proofOfPocession;
    }
    
    public XMLSignature getProofOfPocession() {
        return proofOfPocession;
    }
    
    public void setKeyBindingAuth(XMLSignature keyBindingAuth) {
        this.keyBindingAuth = keyBindingAuth;
    }
    
    public XMLSignature getKeyBindingAuth() {
        return keyBindingAuth;
    }
    
    public void setPassPhraseAuth(String passPhraseAuth) {
        this.passPhraseAuth = passPhraseAuth;
    }
    
    public String getPassPhraseAuth() {
        return passPhraseAuth;
    }
}
