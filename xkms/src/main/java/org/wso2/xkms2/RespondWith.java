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

public class RespondWith {

    private String anyURI;

    public static final RespondWith KEY_NAME =
            new RespondWith("http://www.w3.org/2002/03/xkms#KeyName");
    public static final RespondWith KEY_VALUE =
            new RespondWith("http://www.w3.org/2002/03/xkms#KeyValue");
    public static final RespondWith X_509_CERT =
            new RespondWith("http://www.w3.org/2002/03/xkms#X509Cert");
    public static final RespondWith X_509_CHAIN =
            new RespondWith("http://www.w3.org/2002/03/xkms#X509Chain");
    public static final RespondWith X_509_CRL =
            new RespondWith("http://www.w3.org/2002/03/xkms#X509CRL");
    public static final RespondWith RETRIEVAL_METHOD =
            new RespondWith("http://www.w3.org/2002/03/xkms#RetrievalMethod");
    public static final RespondWith PGP =
            new RespondWith("http://www.w3.org/2002/03/xkms#PGP");
    public static final RespondWith PGP_WEB =
            new RespondWith("http://www.w3.org/2002/03/xkms#PGPWeb");
    public static final RespondWith SPKI =
            new RespondWith("http://www.w3.org/2002/03/xkms#SPKI");
    public static final RespondWith PRIVATE_KEY =
            new RespondWith("http://www.w3.org/2002/03/xkms#PrivateKey");

    private RespondWith(String anyURI) {
        this.anyURI = anyURI;
    }

    public String getAnyURI() {
        return anyURI;
    }

    public static RespondWith valueOf(String anyURI) throws IllegalArgumentException {
        
        if (KEY_NAME.anyURI.equals(anyURI)) {
            return KEY_NAME;
        } else if (KEY_VALUE.anyURI.equals(anyURI)) {
            return KEY_VALUE;
        } else if (X_509_CERT.anyURI.equals(anyURI)) {
            return X_509_CERT;
        } else if (X_509_CHAIN.anyURI.equals(anyURI)) {
            return X_509_CHAIN;
        } else if (X_509_CRL.anyURI.equals(anyURI)) {
            return X_509_CRL;
        } else if (RETRIEVAL_METHOD.anyURI.equals(anyURI)) {
            return RETRIEVAL_METHOD;
        } else if (PGP.anyURI.equals(anyURI)) {
            return PGP;
        } else if (PGP_WEB.anyURI.equals(anyURI)) {
            return PGP_WEB;
        } else if (SPKI.anyURI.equals(anyURI)) {
            return SPKI;
        } else if (PRIVATE_KEY.anyURI.equals(anyURI)) {
            return PRIVATE_KEY;
        } else {
           throw new IllegalArgumentException("Invalid argument " + anyURI);
        }
        
    }
}
