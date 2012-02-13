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
package org.wso2.xkms2.builder;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAttribute;
import org.wso2.xkms2.*;

import javax.xml.namespace.QName;
import java.util.Iterator;
/*
 * 
 */

public abstract class RequestAbstractTypeBuilder extends MessageAbstractTypeBuilder {

    private RequestAbstractType requestAbstractType;

    public RequestAbstractType getRequestAbstractType() {
        return requestAbstractType;
    }

    public void buildElement(OMElement element, MessageAbstractType messageAbstractType)
            throws XKMSException {
        super.buildElement(element, messageAbstractType);

        requestAbstractType = (RequestAbstractType) messageAbstractType;

        for (Iterator iterator = element.getChildrenWithName(
                new QName(XKMS2Constants.XKMS2_NS, "ResponseMechanism")); iterator.hasNext();) {
            OMElement e = (OMElement) iterator.next();
            requestAbstractType.addResponseMechanism(ResponseMechanism.validate(e.getText()));

        }

        for (Iterator iterator =
                element.getChildrenWithName(new QName(XKMS2Constants.XKMS2_NS, "RespondWith"));
             iterator.hasNext();) {
            OMElement e = (OMElement) iterator.next();
            requestAbstractType.addRespondWith(RespondWith.valueOf(e.getText()));

        }

        OMElement pendingNotificationEle = element.getFirstChildWithName(
                new QName(XKMS2Constants.XKMS2_NS, "PendingNotification"));
        if (pendingNotificationEle != null) {

            PendingNotification pendingNotification = new PendingNotification();

            OMAttribute mechanismAttr = pendingNotificationEle.getAttribute(new QName("Mechanism"));
            if (mechanismAttr == null) {
                throw new XKMSException("Mechanism attribute is not found");
            }
            String text = mechanismAttr.getAttributeValue();
            if (text == null) {
                throw new XKMSException("Value of the Mechanism attribute is not found");
            }
            pendingNotification.setMechanism(text);


            OMAttribute identifierAttr = pendingNotificationEle.getAttribute(new QName("Identifier"));
            if (identifierAttr == null) {
                throw new XKMSException("Identifier attribute is not found");
            }
            text = identifierAttr.getAttributeValue();
            if (text == null) {
                throw new XKMSException("Value of the Identifier attribute is not found");
            }
            pendingNotification.setMechanism(text);
            requestAbstractType.setPendingNotification(pendingNotification);
        }

        OMElement originalRequestIdEle = element.getFirstChildWithName(
                new QName(XKMS2Constants.XKMS2_NS, "OriginalRequestId"));
        if (originalRequestIdEle != null) {
            requestAbstractType.setOriginalRequestId(originalRequestIdEle.getText());
        }

        OMElement responseLimitEle =
                element.getFirstChildWithName(new QName(XKMS2Constants.XKMS2_NS, "ResponseLimit"));
        if (responseLimitEle != null) {
            requestAbstractType.setResponseLimit(Integer.parseInt(responseLimitEle.getText()));
        }


    }
}
