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

/*
 * 
 */

public class StatusValue {

    private String anyURI;

    public static final StatusValue VALID = new StatusValue(
            "http://www.w3.org/2002/03/xkms#Valid");

    public static final StatusValue INVALID = new StatusValue(
            "http://www.w3.org/2002/03/xkms#Invalid");

    public static final StatusValue INDETERMINATE = new StatusValue(
            "http://www.w3.org/2002/03/xkms#Indeterminate");

    private StatusValue(String anyURI) {
        this.anyURI = anyURI;
    }

    public String getAnyURI() {
        return anyURI;
    }

    public static StatusValue valueOf(String anyURI) throws XKMSException {
        if (VALID.anyURI.equals(anyURI)) {
            return VALID;

        } else if (INVALID.anyURI.equals(anyURI)) {
            return INVALID;

        } else if (INDETERMINATE.anyURI.equals(anyURI)) {
            return INDETERMINATE;
        }

        throw new XKMSException(StatusValue.class.getName()
                + " validation faild.");
    }
}
