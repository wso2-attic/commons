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
/*
 * 
 */

public class QueryKeyBindingBuilder extends KeyBindingAbstractTypeBuilder {

    private QueryKeyBinding queryKeyBinding;

    public QueryKeyBindingBuilder() {
        this.queryKeyBinding = new QueryKeyBinding();
    }

    public XKMSElement buildElement(OMElement element) throws XKMSException {
        super.buildElement(element, queryKeyBinding);

        OMElement timeInstantEle =
                element.getFirstChildWithName(new QName(XKMS2Constants.XKMS2_NS, "TimeInstant"));
        if (timeInstantEle != null) {
            TimeInstant timeInstant = new TimeInstant();
            OMAttribute timeAttr = timeInstantEle.getAttribute(new QName("Time"));
            if (timeAttr == null) {
                throw new XKMSException("Time attribute is not available");
            }

            String text = timeAttr.getAttributeValue();
            if (text == null) {
                throw new XKMSException("Value of the Time attribute is not available");
            }
            timeInstant.setDateTime(text);

            queryKeyBinding.setTimeInstance(timeInstant);

        }

        return queryKeyBinding;
    }

}
