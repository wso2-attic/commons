/*
 * Copyright 2008,2009 WSO2, Inc. http://www.wso2.org.
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
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.util.MercuryConstants;

import java.util.*;


public class SequenceAcknowledgment extends RMMessageHeader {

    private String identifier;
    private List acknowledgmentRanges;

    public SequenceAcknowledgment() {
        super();
        this.acknowledgmentRanges = new ArrayList();
    }

    public SequenceAcknowledgment(String identifier) {
        this.identifier = identifier;
        this.acknowledgmentRanges = new ArrayList();
    }

    public void addAcknowledgmentRange(AcknowledgmentRange range) {
        this.acknowledgmentRanges.add(range);
    }

    public void populateAcknowledgmentRanges(Set acknowlegments) {
        // first sort the array
        List numbers = new ArrayList(acknowlegments);
        if (numbers.size() > 0) {
            Collections.sort(numbers);
            long currentNumber;
            long lowerlimit = getNumber(numbers, 0);
            long upperlimit = getNumber(numbers, 0);

            for (int i = 1; i < numbers.size(); i++) {
                currentNumber = getNumber(numbers, i);
                if (currentNumber > (upperlimit + 1)) {
                    // this means we have a new range.
                    this.acknowledgmentRanges.add(new AcknowledgmentRange(upperlimit, lowerlimit));
                    lowerlimit = currentNumber;
                    upperlimit = currentNumber;
                } else {
                    // advance the upper limit
                    upperlimit = currentNumber;
                }
            }

            // add the final range
            this.acknowledgmentRanges.add(new AcknowledgmentRange(upperlimit, lowerlimit));
        }

    }

    private long getNumber(List numbers, int position) {
        return ((Long) numbers.get(position)).longValue();
    }

    public SOAPHeaderBlock toSOAPHeaderBlock() throws RMMessageBuildingException {
        SOAPFactory soapFactory = getSoapFactory();
        OMNamespace omNamespace = soapFactory.createOMNamespace(
                MercuryConstants.RM_1_0_NAMESPACE, MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
        SOAPHeaderBlock sequenceAcknowledgment =
                soapFactory.createSOAPHeaderBlock(MercuryConstants.SEQUENCE_ACKNOWLEDGMENT, omNamespace);
        OMElement identifierElement = soapFactory.createOMElement(
                MercuryConstants.IDENTIFIER,
                this.rmNamespace,
                MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
        identifierElement.setText(this.identifier);
        sequenceAcknowledgment.addChild(identifierElement);

        AcknowledgmentRange acknowledgmentRange = null;
        for (Iterator iter = this.acknowledgmentRanges.iterator(); iter.hasNext();) {
            acknowledgmentRange = (AcknowledgmentRange) iter.next();
            sequenceAcknowledgment.addChild(acknowledgmentRange.toOM());
        }

        return sequenceAcknowledgment;
    }

    public static SequenceAcknowledgment fromSOAPHeaderBlock(SOAPHeaderBlock soapHeaderBlock)
            throws RMMessageBuildingException {
        soapHeaderBlock.setProcessed();
        String soapNamespace = soapHeaderBlock.getQName().getNamespaceURI();
        SequenceAcknowledgment sequenceAcknowledgment = new SequenceAcknowledgment();
        sequenceAcknowledgment.setSoapNamesapce(soapNamespace);

        String rmNamesapce = soapHeaderBlock.getNamespace().getNamespaceURI();
        sequenceAcknowledgment.setRmNamespace(rmNamesapce);
        Iterator elementIter = soapHeaderBlock.getChildElements();
        OMElement childElement;
        for (; elementIter.hasNext();) {
            childElement = (OMElement) elementIter.next();
            if (childElement.getLocalName().equals(MercuryConstants.IDENTIFIER)) {
                sequenceAcknowledgment.setIdentifier(childElement.getText());
            } else if (childElement.getLocalName().equals(MercuryConstants.ACKKNOWLEDGEMENT_RANGE)) {
                sequenceAcknowledgment.addAcknowledgmentRange(AcknowledgmentRange.fromOM(childElement));
            }
        }
        return sequenceAcknowledgment;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List getAcknowledgmentRanges() {
        return acknowledgmentRanges;
    }

    public void setAcknowledgmentRanges(List acknowledgmentRanges) {
        this.acknowledgmentRanges = acknowledgmentRanges;
    }
}
