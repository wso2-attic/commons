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
package org.wso2.mercury.message;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.util.MercuryConstants;

import javax.xml.namespace.QName;

/**
 * represents an acknowledgment range
 */
public class AcknowledgmentRange extends RMMessageElement {

    private long upper;
    private long lower;


    public AcknowledgmentRange() {
        super();
    }

    public AcknowledgmentRange(String rmNamespace) {
        super(rmNamespace);
    }

    public AcknowledgmentRange(long upper, long lower) {
        this.upper = upper;
        this.lower = lower;
    }

    public OMElement toOM() throws RMMessageBuildingException {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMElement omElement = omFactory.createOMElement(MercuryConstants.ACKKNOWLEDGEMENT_RANGE,
                this.rmNamespace,
                MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
        omElement.addAttribute(MercuryConstants.UPPER, String.valueOf(upper), null);
        omElement.addAttribute(MercuryConstants.LOWER, String.valueOf(lower), null);
        return omElement;
    }

    public static AcknowledgmentRange fromOM(OMElement omElement) throws RMMessageBuildingException {
        String rmNamespace = omElement.getNamespace().getNamespaceURI();
        long upperLimit = Long.parseLong(omElement.getAttributeValue(new QName(null, MercuryConstants.UPPER)));
        long lowerLimit = Long.parseLong(omElement.getAttributeValue(new QName(null, MercuryConstants.LOWER)));
        AcknowledgmentRange acknowledgmentRange = new AcknowledgmentRange(rmNamespace);
        acknowledgmentRange.setUpper(upperLimit);
        acknowledgmentRange.setLower(lowerLimit);
        return acknowledgmentRange;
    }

    public boolean isNumberInRange(long number) {
        return (number >= lower) && (number <= upper);
    }

    public long getUpper() {
        return upper;
    }

    public void setUpper(long upper) {
        this.upper = upper;
    }

    public long getLower() {
        return lower;
    }

    public void setLower(long lower) {
        this.lower = lower;
    }

}
