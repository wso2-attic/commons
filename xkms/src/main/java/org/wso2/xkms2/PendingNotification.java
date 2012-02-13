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

public class PendingNotification implements ElementSerializable {

    /*From PendingNotificationType. Shold the values be URIs*/

    private String identifier;
    private String mechanism;

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setMechanism(String mechanism) {
        this.mechanism = mechanism;
    }

    public String getMechanism() {
        return mechanism;
    }

    public OMElement serialize(OMFactory factory) throws XKMSException {
        if (identifier != null && mechanism != null) {
            OMElement pendingNotificationEle = factory.createOMElement(XKMS2Constants.ELE_PENDING_NOTIFICATION);
            OMNamespace emptyNs = factory.createOMNamespace("","");
            pendingNotificationEle.addAttribute(XKMS2Constants.ATTR_IDENTIFIER,identifier,emptyNs);
            pendingNotificationEle.addAttribute(XKMS2Constants.ATTR_MECHANISM,mechanism,emptyNs);

            return pendingNotificationEle;
        } else {
            throw new XKMSException(
                    "PendingNotification couldn't built due to required fields are not found");
        }
    }
}
