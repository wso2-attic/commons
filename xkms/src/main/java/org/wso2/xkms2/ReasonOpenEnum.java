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
package org.wso2.xkms2;

/**
 * This is a constants class that will be used in
 * ValidReason
 * InvalidReason
 * IndeterminateReason
 */

public final class ReasonOpenEnum {
    public static final String ISSUER_TRUST =
            "http://www.w3.org/2002/03/xkms#IssuerTrust";
    public static final String REVOCATION_STATUS =
            "http://www.w3.org/2002/03/xkms#RevocationStatus";
    public static final String VALIDITY_INTERVAL =
            "http://www.w3.org/2002/03/xkms#ValidityInterval";
    public static final String SIGNATURE =
            "http://www.w3.org/2002/03/xkms#Signature";

    public static String validate(String anyURI) throws XKMSException {
        if (ISSUER_TRUST.equals(anyURI) || REVOCATION_STATUS.equals(anyURI) ||
            VALIDITY_INTERVAL.equals(anyURI) || SIGNATURE.equals(anyURI)) {
            return anyURI;

        }
        throw new XKMSException(ReasonOpenEnum.class.getName() + " validation faild.");
    }

}
