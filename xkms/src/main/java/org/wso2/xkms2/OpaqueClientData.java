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

import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMText;

public class OpaqueClientData implements ElementSerializable {

    private List opaqueDataList;

    public List getOpaqueDataList() {
        return opaqueDataList;
    }

    public void setOpaqueDataList(List opaqueDataList) {
        this.opaqueDataList = opaqueDataList;
    }

    public void addOpaqueData(OpaqueData opaqueData) {
        if (opaqueDataList == null) {
            opaqueDataList = new ArrayList();
        }
        this.opaqueDataList.add(opaqueData);
    }

    public OMElement serialize(OMFactory factory) throws XKMSException {
        if (opaqueDataList != null) {
            OMElement opaqueClientDataEle =
                    factory.createOMElement(XKMS2Constants.ELE_OPAQUE_CLIENT_DATA);
            for (int i = 0; i < opaqueDataList.size(); i++) {
                OMElement opaqueDataEle = factory.createOMElement(XKMS2Constants.ELE_OPAQUE_DATA);
                OpaqueData opaqueData = (OpaqueData) opaqueDataList.get(i);
                DataHandler dataHandler =
                        new DataHandler(new ByteArrayDataSource(opaqueData.getBase64Binary()));

                OMText omText = factory.createOMText(dataHandler,false);
                opaqueDataEle.addChild(omText);
                opaqueClientDataEle.addChild(opaqueDataEle);
            }
            return opaqueClientDataEle;
        }
        return null;
    }
}
