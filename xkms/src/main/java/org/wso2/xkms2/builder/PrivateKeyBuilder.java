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

import org.apache.axiom.om.OMElement;
import org.w3c.dom.Element;
import org.wso2.xkms2.PrivateKey;
import org.wso2.xkms2.XKMS2Constants;
import org.wso2.xkms2.XKMSElement;
import org.wso2.xkms2.XKMSException;

public class PrivateKeyBuilder implements ElementBuilder {
    
    public static final PrivateKeyBuilder INSTANCE = new PrivateKeyBuilder();
    
    private PrivateKeyBuilder() {
    }

    public XKMSElement buildElement(OMElement element) throws XKMSException {
        PrivateKey privateKey = new PrivateKey();
        OMElement encryptedData = element
                .getFirstChildWithName(XKMS2Constants.Q_ELEM_XML_ENC_DATA);
        privateKey.setEncryptedData((Element) element);
        return privateKey;
    }
}
