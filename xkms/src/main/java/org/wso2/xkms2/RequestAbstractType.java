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

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * The RequestAbstractType abstract class is the class from which all XKMS request element classes
 * are derived. The RequestAbstractType abstract class inherits the element and attributes of
 * the MessageAbstractType abstract class and in addition contains the following elements
 * and attributes.
 */

public abstract class RequestAbstractType extends MessageAbstractType implements ElementSerializable {

    private List responseMechanismList;
    private List respondWithList;
    private PendingNotification pendingNotification;
    private String originalRequestId;
    private int responseLimit = -1;

    public void addResponseMechanism(ResponseMechanism responseMechanism) {
        if (responseMechanismList == null) {
            responseMechanismList = new ArrayList();
        }
        responseMechanismList.add(responseMechanism);
    }

    public List getResponseMechanism() {
        return responseMechanismList;
    }

    public void addRespondWith(RespondWith respondWith) {
        if (respondWithList == null) {
            respondWithList = new ArrayList();
        }
        respondWithList.add(respondWith);
    }

    public List getRespondWith() {
        return respondWithList;
    }

    public void setRespondWithList(List respondWithList) {
        this.respondWithList = respondWithList;
    }

    public void setPendingNotification(PendingNotification pendingNotification) {
        this.pendingNotification = pendingNotification;
    }

    public PendingNotification getPendingNotification() {
        return pendingNotification;
    }

    public void setOriginalRequestId(String originalRequestId) {
        this.originalRequestId = originalRequestId;
    }

    public String getOriginalRequestId() {
        return originalRequestId;
    }

    public void setResponseLimit(int responseLimit) {
        this.responseLimit = responseLimit;
    }

    public int getResponseLimit() {
        return responseLimit;
    }

    public void setResponseMechanismList(List responseMechanismList) {
        this.responseMechanismList = responseMechanismList;
    }

    protected void serialize(OMFactory factory, OMElement container) throws XKMSException {
        super.serialize(factory, container);

        if (responseMechanismList != null) {
            for (Iterator iterator = responseMechanismList.iterator();iterator.hasNext();) {
                ResponseMechanism responseMechanism = (ResponseMechanism) iterator.next();
                OMElement responseMechanismEle =
                        factory.createOMElement(XKMS2Constants.ELE_RESPONSE_MECHANISM);
                responseMechanismEle.setText(responseMechanism.getAnyURI());
                container.addChild(responseMechanismEle);
            }

        }

        if (respondWithList != null) {
            for (Iterator iterator = respondWithList.iterator();iterator.hasNext();) {
                RespondWith respondWith = (RespondWith) iterator.next();
                OMElement respondWithEle = factory.createOMElement(XKMS2Constants.ELE_RESPOND_WITH);
                respondWithEle.setText(respondWith.getAnyURI());
                container.addChild(respondWithEle);
            }
        }

        if (pendingNotification != null) {
            container.addChild(pendingNotification.serialize(factory));

        }
        OMNamespace emptyNs = factory.createOMNamespace("", "");
        if (originalRequestId != null) {
            container.addAttribute(XKMS2Constants.ATTR_ORIGINAL_REQUEST_ID, originalRequestId,
                                   emptyNs);

        }
        if (responseLimit != -1) {
            container.addAttribute(XKMS2Constants.ATTR_RESPONSE_LIMIT,
                                   Integer.toBinaryString(responseLimit), emptyNs);
        }

    }

}
