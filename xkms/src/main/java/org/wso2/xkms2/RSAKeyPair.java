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
package org.wso2.xkms2;

import java.io.ByteArrayInputStream;
import java.security.KeyPair;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.wso2.xkms2.util.XKMSUtil;

public class RSAKeyPair implements XKMSElement{

    private byte[] modulus;

    private byte[] exponent;

    private byte[] P;

    private byte[] Q;

    private byte[] DP;

    private byte[] DQ;

    private byte[] inverseQ;

    private byte[] D;

    public RSAKeyPair() {
    }

    public byte[] getD() {
        return D;
    }

    public void setD(byte[] d) {
        D = d;
    }

    public byte[] getDP() {
        return DP;
    }

    public void setDP(byte[] dp) {
        DP = dp;
    }

    public byte[] getDQ() {
        return DQ;
    }

    public void setDQ(byte[] dq) {
        DQ = dq;
    }

    public byte[] getExponent() {
        return exponent;
    }

    public void setExponent(byte[] exponent) {
        this.exponent = exponent;
    }

    public byte[] getInverseQ() {
        return inverseQ;
    }

    public void setInverseQ(byte[] inverseQ) {
        this.inverseQ = inverseQ;
    }

    public byte[] getModulus() {
        return modulus;
    }

    public void setModulus(byte[] modulus) {
        this.modulus = modulus;
    }

    public byte[] getP() {
        return P;
    }

    public void setP(byte[] p) {
        P = p;
    }

    public byte[] getQ() {
        return Q;
    }

    public void setQ(byte[] q) {
        Q = q;
    }

    public Element build(Document doc) {

        Element rsaKeyPair = doc.createElementNS(
                XKMS2Constants.XKMS2_NS, XKMS2Constants.XKMS2_PREFIX
                        + ":" + XKMS2Constants.TAG_RSA_KEY_PAIR);
        XKMSUtil.setNamespace(rsaKeyPair, XKMS2Constants.XKMS2_NS,
                XKMS2Constants.XKMS2_PREFIX);

        Element modulus = doc.createElementNS(XKMS2Constants.XKMS2_NS,
                XKMS2Constants.XKMS2_PREFIX + ":" + "Modulus");
        appendBinaryDataAsText(this.modulus, modulus, doc);
        rsaKeyPair.appendChild(modulus);

        Element exponent = doc.createElementNS(XKMS2Constants.XKMS2_NS,
                XKMS2Constants.XKMS2_PREFIX + ":" + "Exponent");
        appendBinaryDataAsText(this.exponent, exponent, doc);
        rsaKeyPair.appendChild(exponent);

        Element P = doc.createElementNS(XKMS2Constants.XKMS2_NS,
                XKMS2Constants.XKMS2_PREFIX + ":" + "P");
        appendBinaryDataAsText(this.P, P, doc);
        rsaKeyPair.appendChild(P);

        Element Q = doc.createElementNS(XKMS2Constants.XKMS2_NS,
                XKMS2Constants.XKMS2_PREFIX + ":" + "Q");
        appendBinaryDataAsText(this.Q, Q, doc);
        rsaKeyPair.appendChild(Q);

        Element DP = doc.createElementNS(XKMS2Constants.XKMS2_NS,
                XKMS2Constants.XKMS2_PREFIX + ":" + "DP");
        appendBinaryDataAsText(this.DP, DP, doc);
        rsaKeyPair.appendChild(DP);

        Element DQ = doc.createElementNS(XKMS2Constants.XKMS2_NS,
                XKMS2Constants.XKMS2_PREFIX + ":" + "DQ");
        appendBinaryDataAsText(this.DQ, DQ, doc);
        rsaKeyPair.appendChild(DQ);

        Element inverseQ = doc.createElementNS(XKMS2Constants.XKMS2_NS,
                XKMS2Constants.XKMS2_PREFIX + ":" + "InverseQ");
        appendBinaryDataAsText(this.inverseQ, inverseQ, doc);
        rsaKeyPair.appendChild(inverseQ);

        Element D = doc.createElementNS(XKMS2Constants.XKMS2_NS,
                XKMS2Constants.XKMS2_PREFIX + ":" + "D");
        appendBinaryDataAsText(this.D, D, doc);
        rsaKeyPair.appendChild(D);

        return rsaKeyPair;
    }

    private void appendBinaryDataAsText(byte[] binaryData, Node node,
            Document doc) {
        node.appendChild(getText(encode(binaryData), doc));
    }

    private Text getText(String data, Document doc) {
        return doc.createTextNode(data);
    }

    private String encode(byte[] bytes) {
        return Base64.encode(bytes);
    }
}
