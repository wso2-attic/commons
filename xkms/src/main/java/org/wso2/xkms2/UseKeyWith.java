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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

public class UseKeyWith implements ElementSerializable {

    /*
    Application  	Application URI  	Identifier  	Type
        XKMS 	http://www.w3.org/2002/03/xkms# 	URL identifying SOAP role 	URI
        XKMS/profile 	http://www.w3.org/2002/03/xkms#profile 	URL identifying SOAP role 	URI
        S/MIME 	urn:ietf:rfc:2633 	SMTP email address of subject 	RFC822 addr-spec
        PGP 	urn:ietf:rfc:2440 	SMTP email address of subject 	RFC822 addr-spec
        TLS 	urn:ietf:rfc:2246 	URI identifying certificate subject 	URI
        TLS/HTTPS 	urn:ietf:rfc:2818 	DNS address of http server 	DNS Address
        TLS/SMTP 	urn:ietf:rfc:2487 	DNS address of mail server 	DNS Address
        IPSEC 	urn:ietf:rfc:2401 	IP address of network resource 	IP Address
        PKIX 	urn:ietf:rfc:2459 	Certificate Subject Name 	X.509 Distinguished Name

    */

    public static final String S_MIME = "urn:ietf:rfc:2633";
    public static final String PGP = "urn:ietf:rfc:2440";
    public static final String TLS = "urn:ietf:rfc:2246";
    public static final String TLS_HTTPS = "urn:ietf:rfc:2818";
    public static final String TLS_SMTP = "urn:ietf:rfc:2487";
    public static final String IPSEC = "urn:ietf:rfc:2401";
    public static final String PKIX = "urn:ietf:rfc:2459";
    public static final String SKI = "urn:ietf:rfc:3080";

    private String application;

    private String identifier;

    public UseKeyWith() {
    }

    public UseKeyWith(String application, String identifier) {
        this.application = application;
        this.identifier = identifier;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getApplication() {
        return application;
    }

    public String getIdentifier() {
        return identifier;
    }

    public OMElement serialize(OMFactory factory) throws XKMSException {
        if (application != null && identifier != null) {
            OMElement useKeyWithEle = factory.createOMElement(XKMS2Constants.ELE_USE_KEY_WITH);
            OMNamespace emptyNs = factory.createOMNamespace("", "");
            useKeyWithEle.addAttribute(XKMS2Constants.ATTR_APPLICATION, application, emptyNs);
            useKeyWithEle.addAttribute(XKMS2Constants.ATTR_IDENTIFIER, identifier, emptyNs);

            return useKeyWithEle;

        } else {
            throw new XKMSException("Required attributes are not available");
        }


    }


}
