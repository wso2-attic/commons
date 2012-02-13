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

import org.apache.ws.security.util.XmlSchemaDateFormat;

import java.util.Calendar;
import java.text.DateFormat;
import java.text.ParseException;
/*
 * 
 */

public class TimeInstant {

    private Calendar dateTime;

    public String getDateTime() throws XKMSException {
        if (dateTime == null) {
            throw new XKMSException("dateTime is not available");
        }
        DateFormat zulu = new XmlSchemaDateFormat();
        return zulu.format(dateTime.getTime());
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    public void setDateTime(String dateTime) throws XKMSException {
        this.dateTime = Calendar.getInstance();

        DateFormat zulu = new XmlSchemaDateFormat();
        try {
            this.dateTime.setTime(zulu.parse(dateTime));
        } catch (ParseException e) {
            throw new XKMSException(e);
        }
    }
}
