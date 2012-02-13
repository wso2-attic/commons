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

public class ResponseMechanism {

    private String anyURI;

    public static final ResponseMechanism PENDING =
            new ResponseMechanism("http://www.w3.org/2002/03/xkms#Pending");
    public static final ResponseMechanism REPRESENT =
            new ResponseMechanism("http://www.w3.org/2002/03/xkms#Represent");
    public static final ResponseMechanism REQUEST_SIGNATURE_VALUE =
            new ResponseMechanism("http://www.w3.org/2002/03/xkms#RequestSignatureValue");

    private ResponseMechanism(String anyURI) {
        this.anyURI = anyURI;
    }

    public String getAnyURI() {
        return anyURI;
    }

    public static ResponseMechanism validate(String anyURI) throws XKMSException {
        if (anyURI.equals(PENDING.getAnyURI()) || anyURI.equals(REPRESENT.getAnyURI()) ||
            anyURI.equals(REQUEST_SIGNATURE_VALUE.getAnyURI())){
            return new ResponseMechanism(anyURI);
        }
        throw new XKMSException(ResponseMechanism.class.getName() + " validation failed");
    }
}
