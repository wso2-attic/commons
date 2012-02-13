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

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis2.addressing.AddressingConstants;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.util.MercuryConstants;

/**
 * this class is used to generate and parse
 */
public class AcksTo extends RMMessageElement{

    private String addressingNamespace;
    private String endpointAddress;

    public AcksTo(String rmNamespace) {
        this.rmNamespace = rmNamespace;
    }

    public String getRMNamespaceValue() {
        return rmNamespace;
    }

    public OMElement toOM() throws RMMessageBuildingException {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        
        OMElement acksTo = omFactory.createOMElement(MercuryConstants.ACKS_TO,
                rmNamespace, MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
        if (addressingNamespace == null){
            addressingNamespace = AddressingConstants.Final.WSA_NAMESPACE;
        }
        OMElement address = omFactory.createOMElement(MercuryConstants.ADDRESS,
                addressingNamespace, AddressingConstants.WSA_DEFAULT_PREFIX);
        address.setText(endpointAddress);
        acksTo.addChild(address);
        return acksTo;
    }

    public static AcksTo fromOM(OMElement omElement) throws RMMessageBuildingException {
        String rmNamesapce = omElement.getNamespace().getNamespaceURI();
        OMElement address = omElement.getFirstElement();
        if (!address.getLocalName().equals(MercuryConstants.ADDRESS)){
           throw new RMMessageBuildingException("Can not Address element in AcksTo element");
        }

        AcksTo acksTo = new AcksTo(rmNamesapce);
        acksTo.setAddressingNamespace(address.getNamespace().getNamespaceURI());
        acksTo.setEndpointAddress(address.getText());
        return acksTo;
    }

    public String getAddressingNamespace() {
        return addressingNamespace;
    }

    public void setAddressingNamespace(String addressingNamespace) {
        this.addressingNamespace = addressingNamespace;
    }

    public String getEndpointAddress() {
        return endpointAddress;
    }

    public void setEndpointAddress(String endpointAddress) {
        this.endpointAddress = endpointAddress;
    }
}
