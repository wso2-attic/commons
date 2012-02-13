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

public class StatusBuilder implements ElementBuilder {

    public static final StatusBuilder INSTANCE = new StatusBuilder();

    // private Status status;

    private StatusBuilder() {
        // status = new Status();
    }

    public XKMSElement buildElement(OMElement element) throws XKMSException {
        Status status = new Status();

        for (Iterator iterator = element.getChildrenWithName(new QName(
                XKMS2Constants.XKMS2_NS, "ValidReason")); iterator.hasNext();) {

            OMElement e = (OMElement) iterator.next();

            String text = e.getText();
            if (text == null) {
                throw new XKMSException(
                        "ValidReason elements test value is not available");
            }
            status.addValidReason(ValidReason.validate(text));
        }

        for (Iterator iterator = element.getChildrenWithName(new QName(
                XKMS2Constants.XKMS2_NS, "IndeterminateReason")); iterator
                .hasNext();) {

            OMElement e = (OMElement) iterator.next();

            String text = e.getText();
            if (text == null) {
                throw new XKMSException(
                        "IndeterminateReason elements text is not available");
            }
            status.addIndeterminateReason(IndeterminateReason.validate(text));

        }

        for (Iterator iterator = element.getChildrenWithName(new QName(
                XKMS2Constants.XKMS2_NS, "InvalidReason")); iterator.hasNext();) {

            OMElement e = (OMElement) iterator.next();

            String text = e.getText();
            if (text == null) {
                throw new XKMSException(
                        "InvalidReason elements text is not available");
            }
            status.addInvalidReason(InvalidReason.validate(text));

        }

        OMAttribute statusValueAtt = element.getAttribute(new QName(
                "StatusValue"));

        if (statusValueAtt == null) {
            throw new XKMSException("StatusValue attribute is not available");
        }

        String statusValue = statusValueAtt.getAttributeValue();

        if (statusValue == null) {
            throw new XKMSException(
                    "Value of StatusValue attribute is not available");
        }

        status.setStatusValue(StatusValue.valueOf(statusValue));

        return status;
    }
}
