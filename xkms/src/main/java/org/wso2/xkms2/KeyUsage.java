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

public class KeyUsage {

    private String anyURI;

    public static final KeyUsage ENCRYPTION = new KeyUsage(
            "http://www.w3.org/2002/03/xkms#Encryption");

    public static final KeyUsage SIGNATURE = new KeyUsage(
            "http://www.w3.org/2002/03/xkms#Signature");

    public static final KeyUsage EXCHANGE = new KeyUsage(
            "http://www.w3.org/2002/03/xkms#Exchange");

    private KeyUsage(String anyURI) {
        this.anyURI = anyURI;
    }

    public String getAnyURI() {
        return anyURI;
    }

    public static KeyUsage valueOf(String anyURI) throws XKMSException {
        if (ENCRYPTION.anyURI.equals(anyURI)) {
            return ENCRYPTION;

        } else if (SIGNATURE.anyURI.equals(anyURI)) {
            return SIGNATURE;

        } else if (EXCHANGE.anyURI.equals(anyURI)) {
            return EXCHANGE;
        }

        throw new XKMSException(KeyUsage.class.getName() + " validation failed");
    }

}
