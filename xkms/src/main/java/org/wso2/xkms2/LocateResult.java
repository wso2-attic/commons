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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;

import java.util.ArrayList;
import java.util.List;
/*
 * 
 */

public class LocateResult extends ResultType implements ElementSerializable {

    private List unverifiedKeyBindingList;

    public List getUnverifiedKeyBindingList() {
        if (unverifiedKeyBindingList == null) {
            unverifiedKeyBindingList = new ArrayList();
        }
        return unverifiedKeyBindingList;
    }

    public void setUnverifiedKeyBindingList(List unverifiedKeyBindingList) {
        this.unverifiedKeyBindingList = unverifiedKeyBindingList;
    }

    public void addUnverifiedKeyBinding(UnverifiedKeyBinding unverifiedKeyBinding) {
        if (this.unverifiedKeyBindingList == null) {
            this.unverifiedKeyBindingList = new ArrayList();
        }
        this.unverifiedKeyBindingList.add(unverifiedKeyBinding);
    }

    public OMElement serialize(OMFactory factory) throws XKMSException {
        OMElement locateResultEle = factory.createOMElement(XKMS2Constants.ELE_LOCATE_RESULT);

        super.serialize(factory, locateResultEle);

        if (unverifiedKeyBindingList != null) {
            for (int j = 0; j < unverifiedKeyBindingList.size(); j++) {
                UnverifiedKeyBinding unverifiedKeyBinding =
                        (UnverifiedKeyBinding) unverifiedKeyBindingList.get(j);
                locateResultEle.addChild(unverifiedKeyBinding.serialize(factory));

            }
        }

        return locateResultEle;


    }
}
