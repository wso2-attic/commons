/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.xkms2.builder;

import java.math.BigInteger;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.Base64;
import org.wso2.xkms2.RSAKeyPair;
import org.wso2.xkms2.XKMS2Constants;
import org.wso2.xkms2.XKMSElement;
import org.wso2.xkms2.XKMSException;
import org.wso2.xkms2.util.XKMSUtil;

public class RSAKeyPairTypeBuilder implements ElementBuilder {

    private static final QName MODULUS = new QName(XKMS2Constants.XKMS2_NS,
            "Modulus");

    private static final QName EXPONENT = new QName(XKMS2Constants.XKMS2_NS,
            "Exponent");

    private static final QName P = new QName(XKMS2Constants.XKMS2_NS, "P");

    private static final QName Q = new QName(XKMS2Constants.XKMS2_NS, "Q");

    private static final QName DP = new QName(XKMS2Constants.XKMS2_NS, "DP");

    private static final QName DQ = new QName(XKMS2Constants.XKMS2_NS, "DQ");

    private static final QName INVERSE_Q = new QName(XKMS2Constants.XKMS2_NS,
            "InverseQ");

    private static final QName D = new QName(XKMS2Constants.XKMS2_NS, "D");

    public static final RSAKeyPairTypeBuilder INSTANCE = new RSAKeyPairTypeBuilder();

    public XKMSElement buildElement(OMElement element) throws XKMSException {
        RSAKeyPair rsaKeyPair = new RSAKeyPair();
        OMElement child;
        
        child = element.getFirstChildWithName(MODULUS);
        rsaKeyPair.setModulus(decodeText(child));
        
        child = element.getFirstChildWithName(EXPONENT);
        rsaKeyPair.setExponent(decodeText(child));
        
        child = element.getFirstChildWithName(P);
        rsaKeyPair.setP(decodeText(child));
        
        child = element.getFirstChildWithName(Q);
        rsaKeyPair.setQ(decodeText(child));
        
        child = element.getFirstChildWithName(DP);
        rsaKeyPair.setDP(decodeText(child));
        
        child = element.getFirstChildWithName(DQ);
        rsaKeyPair.setDQ(decodeText(child));
        
        child =element.getFirstChildWithName(INVERSE_Q);
        rsaKeyPair.setInverseQ(decodeText(child));
        
        child = element.getFirstChildWithName(D);
        rsaKeyPair.setD(decodeText(child));
        
        return rsaKeyPair;
    }

    private byte[] decodeText(OMElement e) {
        return Base64.decode(e.getText());
    }

}
