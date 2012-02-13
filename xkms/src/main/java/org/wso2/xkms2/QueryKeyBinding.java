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

import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

public class QueryKeyBinding extends KeyBindingAbstractType implements ElementSerializable {

    private TimeInstant timeInstance;

    public TimeInstant getTimeInstance() {
        return timeInstance;
    }

    public void setTimeInstance(TimeInstant timeInstance) {
        this.timeInstance = timeInstance;
    }

    public OMElement serialize(OMFactory factory) throws XKMSException {

        OMElement queryKeyBindingEle = factory.createOMElement(XKMS2Constants.ELE_QUERY_KEY_BINDING);
        super.serialize(factory,queryKeyBindingEle);
        if (timeInstance != null) {
            OMElement timeInstantEle = factory.createOMElement(XKMS2Constants.ELE_TIME_INSTANCE);

            String dateTime = timeInstance.getDateTime();
            if (dateTime == null) {
                throw new XKMSException("Time is not available");
            }
            OMNamespace emptyNs = factory.createOMNamespace("","");
            timeInstantEle.addAttribute(XKMS2Constants.ATTR_TIME,dateTime,emptyNs);
            queryKeyBindingEle.addChild(timeInstantEle);

        }
        return queryKeyBindingEle;
    }


}
