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

import org.apache.ws.security.util.XmlSchemaDateFormat;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

import java.util.Calendar;
import java.text.DateFormat;
import java.text.ParseException;

public class ValidityInterval implements ElementSerializable {

    private Calendar notBefore;
    private Calendar notOnOrAfter;

    public ValidityInterval() {
    }

    public ValidityInterval(Calendar notBefore, Calendar notOnOrAfter) {
        this.notBefore = notBefore;
        this.notOnOrAfter = notOnOrAfter;
    }

    public void setNotBefore(Calendar notBefore) {
        this.notBefore = notBefore;
    }

    public void setNotBefore(String notBefore) throws XKMSException {
        this.notBefore = Calendar.getInstance();
        DateFormat zulu = new XmlSchemaDateFormat();
        try {
            this.notBefore.setTime(zulu.parse(notBefore));
        } catch (ParseException e) {
            throw new XKMSException(e);
        }
    }

    public Calendar getNotBefore() {
        return notBefore;
    }

    public void setNotOnOrAfter(Calendar notOnOrAfter) {
        this.notOnOrAfter = notOnOrAfter;
    }

    public void setOnOrAfter(String notOnOrAfter) throws XKMSException {
        this.notOnOrAfter = Calendar.getInstance();
        DateFormat zulu = new XmlSchemaDateFormat();
        try {
            this.notOnOrAfter.setTime(zulu.parse(notOnOrAfter));
        } catch (ParseException e) {
            throw new XKMSException(e);
        }

    }

    public Calendar getOnOrAfter() {
        return notOnOrAfter;
    }

    public OMElement serialize(OMFactory factory) throws XKMSException {
        OMElement validityIntervalEle =
                factory.createOMElement(XKMS2Constants.ELE_VALIDITY_INTERVAL);
        OMNamespace emptyNs = factory.createOMNamespace("", "");
        if (notBefore != null) {
            DateFormat zulu = new XmlSchemaDateFormat();
            validityIntervalEle.addAttribute(XKMS2Constants.ATTR_NOT_BEFORE,
                                             zulu.format(notBefore.getTime()), emptyNs);

        }

        if (notOnOrAfter != null) {
            DateFormat zulu = new XmlSchemaDateFormat();
            validityIntervalEle.addAttribute(XKMS2Constants.ATTR_NOT_ON_OR_AFTER,
                                             zulu.format(notOnOrAfter.getTime()), emptyNs);

        }

        return validityIntervalEle;
    }


}
