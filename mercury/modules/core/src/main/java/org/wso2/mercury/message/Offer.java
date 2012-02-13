/*
 * Copyright 2008,2009 WSO2, Inc. http://www.wso2.org.
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
package org.wso2.mercury.message;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.util.MercuryConstants;

/**
 * used to add the offerid part to create sequence
 */
public class Offer extends RMMessageElement {

    private String identifer;

    public Offer(String identifer) {
        this.identifer = identifer;
    }

    public OMElement toOM() throws RMMessageBuildingException {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMElement offerElement = omFactory.createOMElement(
                MercuryConstants.OFFER, this.rmNamespace, MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
        OMElement identiferElement = omFactory.createOMElement(
                MercuryConstants.IDENTIFIER,this.rmNamespace,MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
        identiferElement.setText(identifer);
        offerElement.addChild(identiferElement);
        return offerElement;
    }

    public static Offer fromOM(OMElement omElement) throws RMMessageBuildingException {
        OMElement identiferElement = omElement.getFirstElement();
        Offer offer = new Offer(identiferElement.getText());
        return offer;
    }

    public String getIdentifer() {
        return identifer;
    }

    public void setIdentifer(String identifer) {
        this.identifer = identifer;
    }
}
