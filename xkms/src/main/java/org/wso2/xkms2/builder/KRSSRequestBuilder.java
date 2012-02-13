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
package org.wso2.xkms2.builder;

import org.apache.axiom.om.OMElement;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.w3c.dom.Element;
import org.wso2.xkms2.Authentication;
import org.wso2.xkms2.KRSSRequest;
import org.wso2.xkms2.XKMS2Constants;
import org.wso2.xkms2.XKMSException;

public abstract class KRSSRequestBuilder extends RequestAbstractTypeBuilder
        implements ElementBuilder {
    
    public KRSSRequest buildElement(OMElement element, KRSSRequest request)
            throws XKMSException {
        super.buildElement(element, request);
        
        OMElement authenticationElem = element
                .getFirstChildWithName(XKMS2Constants.Q_ELEM_AUTHENTICATION);
        
        if (authenticationElem != null) {
            
            Authentication authentication = new Authentication();
            
            OMElement keyBindingAuthElem = authenticationElem
                    .getFirstChildWithName(XKMS2Constants.Q_ELEM_KEY_BINDING_AUTH);

            if (keyBindingAuthElem != null) {
                processKeyBindingAuthenticationElement(keyBindingAuthElem, authentication);
            }

            request.setAuthentication(authentication);
        }
        
        return request;
    }

    private void processKeyBindingAuthenticationElement(
            OMElement keyBindingAuthElem, Authentication authentication) throws XKMSException {
        OMElement signElement = keyBindingAuthElem
                .getFirstChildWithName(XKMS2Constants.Q_ELEM_XML_SIG);
        try {
            XMLSignature signature = new XMLSignature((Element) signElement, "");
            authentication.setKeyBindingAuthentication(signature);

        } catch (XMLSecurityException se) {
            throw new XKMSException(se);
        }
    }
}
