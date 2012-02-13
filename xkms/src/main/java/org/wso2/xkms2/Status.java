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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * This is a special class that represent the object model of Staus. This dose not inherit from
 * MessageAbstractType.
 * The StatusRequest element is used to request that the service return the status of a pending
 * request by means of a <StatusResult> element. The StatusRequest element inherits the element
 * and attributes of PendingRequestType. The RespondWith element MUST NOT be present inside a
 * StatusRequest element.
 */

public class Status implements XKMSElement,ElementSerializable {
    private List validReasonList;
    private List indeterminateReasonList;
    private List invalidReasonList;

    private StatusValue statusValue;

    public List getValidReasonList() {
        return validReasonList;
    }

    public void setValidReasonList(List validReasonList) {
        this.validReasonList = validReasonList;
    }

    public void addValidReason(ValidReason validReason) {
        if (validReasonList == null) {
            validReasonList = new ArrayList();
        }
        validReasonList.add(validReason);
    }

    public List getIndeterminateReasonList() {
        return indeterminateReasonList;
    }

    public void setIndeterminateReasonList(List indeterminateReasonList) {
        this.indeterminateReasonList = indeterminateReasonList;
    }

    public void addIndeterminateReason(IndeterminateReason indeterminateReason) {
        if (indeterminateReasonList == null) {
            indeterminateReasonList = new ArrayList();
        }
        indeterminateReasonList.add(indeterminateReason);
    }

    public List getInvalidReasonList() {
        return invalidReasonList;
    }

    public void setInvalidReasonList(List invalidReasonList) {
        this.invalidReasonList = invalidReasonList;
    }

    public void addInvalidReason(InvalidReason invalidReason) {
        if (invalidReasonList == null) {
            invalidReasonList = new ArrayList();
        }
        invalidReasonList.add(invalidReason);

    }

    public StatusValue getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(StatusValue statusValue) {
        this.statusValue = statusValue;
    }

    public OMElement serialize(OMFactory factory) throws XKMSException {

        if (statusValue == null) {
            throw new XKMSException("Required field statusValue is not available");
        }
        OMNamespace emptyNs = factory.createOMNamespace("","");
        OMElement statusEle = factory.createOMElement(XKMS2Constants.ELE_STATUS);

        statusEle.addAttribute(XKMS2Constants.ATTR_STATUS_VALUE,statusValue.getAnyURI(),emptyNs);

        if (validReasonList != null) {
            for (Iterator iterator = validReasonList.iterator(); iterator.hasNext();) {
                ValidReason validReason = (ValidReason) iterator.next();
                OMElement validReasonEle = factory.createOMElement(XKMS2Constants.ELE_VALID_REASON);
                validReasonEle.setText(validReason.getAnyURI());
                statusEle.addChild(validReasonEle);
            }
        }

        if (indeterminateReasonList != null) {
            for (Iterator iterator = indeterminateReasonList.iterator(); iterator.hasNext();) {
                IndeterminateReason indeterminateReason = (IndeterminateReason) iterator.next();
                OMElement indeterminateReasonEle =
                        factory.createOMElement(XKMS2Constants.ELE_INDETERMINATE_REASON);
                indeterminateReasonEle.setText(indeterminateReason.getAnyURI());
                statusEle.addChild(indeterminateReasonEle);
            }
        }

        if (invalidReasonList != null) {
            for (Iterator iterator = invalidReasonList.iterator(); iterator.hasNext();) {
                InvalidReason invalidReason = (InvalidReason) iterator.next();
                OMElement invalidReasonEle =
                        factory.createOMElement(XKMS2Constants.ELE_INVALID_REASON);
                invalidReasonEle.setText(invalidReason.getAnyURI());
                statusEle.addChild(invalidReasonEle);

            }
        }

        return statusEle;
    }

}
