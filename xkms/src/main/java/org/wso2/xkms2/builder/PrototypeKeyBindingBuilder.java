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
package org.wso2.xkms2.builder;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

import org.apache.axiom.om.OMElement;
import org.apache.ws.security.util.XmlSchemaDateFormat;
import org.wso2.xkms2.PrototypeKeyBinding;
import org.wso2.xkms2.ValidityInterval;
import org.wso2.xkms2.XKMS2Constants;
import org.wso2.xkms2.XKMSElement;
import org.wso2.xkms2.XKMSException;

public class PrototypeKeyBindingBuilder extends KeyBindingAbstractTypeBuilder
        implements ElementBuilder {

    public static final PrototypeKeyBindingBuilder INSTANCE = new PrototypeKeyBindingBuilder();

    public static PrototypeKeyBindingBuilder getInstance() {
        return INSTANCE;
    }

    private PrototypeKeyBindingBuilder() {
    }

    private PrototypeKeyBinding prototypeKeyBinding;

    public XKMSElement buildElement(OMElement element) throws XKMSException {
        prototypeKeyBinding = new PrototypeKeyBinding();
        super.buildElement(element, prototypeKeyBinding);

        OMElement validityIntervalEle = element
                .getFirstChildWithName(XKMS2Constants.ELE_VALIDITY_INTERVAL);

        if (validityIntervalEle != null) {
            processValidityInterval(validityIntervalEle);
        }

        OMElement revokCodeIdentifierElem = element
                .getFirstChildWithName(XKMS2Constants.ELEM_REVOCATION_CODE_IDENTIFIER);

        if (revokCodeIdentifierElem != null) {
            processRevocationCodeIdeintifer(revokCodeIdentifierElem);
        }

        return prototypeKeyBinding;
    }

    private void processValidityInterval(OMElement validityIntervalElem)
            throws XKMSException {
        ValidityInterval validityInterval = new ValidityInterval();

        String strNotBefore = validityIntervalElem
                .getAttributeValue(XKMS2Constants.Q_ATTR_NOT_BEFORE);
        String strNotOnOrAfter = validityIntervalElem
                .getAttributeValue(XKMS2Constants.Q_ATTR_NOT_ON_OR_AFTER);

        DateFormat zulu = new XmlSchemaDateFormat();

        try {
            if (strNotBefore != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(zulu.parse(strNotBefore));
                validityInterval.setNotBefore(calendar);
            }

            if (strNotOnOrAfter != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(zulu.parse(strNotOnOrAfter));
                validityInterval.setNotOnOrAfter(calendar);
            }

        } catch (ParseException xkmse) {
            throw new XKMSException(xkmse);
        }

        prototypeKeyBinding.setValidityInterval(validityInterval);
    }

    private void processRevocationCodeIdeintifer(
            OMElement revocationCodeIdentifierElem) {
        String encoded = revocationCodeIdentifierElem.getText();
        prototypeKeyBinding.setRevocationCodeIdentifier(encoded.getBytes());
    }
}
