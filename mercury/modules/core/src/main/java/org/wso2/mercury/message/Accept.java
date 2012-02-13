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
import org.apache.axis2.addressing.AddressingConstants;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.util.MercuryConstants;

/**
 * used to accepts the offers comes from using the offer tag
 * in create sequences
 */
public class Accept extends RMMessageElement {

    private String acceptERP;
    private String addressingNamespace;

    public Accept() {
    }

    public Accept(String rmNamespace) {
        super(rmNamespace);
    }

    public OMElement toOM() throws RMMessageBuildingException {

        OMFactory omFactory = OMAbstractFactory.getOMFactory();

        OMElement accept = omFactory.createOMElement(MercuryConstants.ACCEPT,
                rmNamespace, MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
        AcksTo acksTo = new AcksTo(this.rmNamespace);
        acksTo.setAddressingNamespace(this.addressingNamespace);
        acksTo.setEndpointAddress(acceptERP);
        accept.addChild(acksTo.toOM());
        return accept;
    }

     public static Accept fromOM(OMElement omElement) throws RMMessageBuildingException {

        String rmNamesapce = omElement.getNamespace().getNamespaceURI();
        OMElement acksTo = omElement.getFirstElement();
        if (!acksTo.getLocalName().equals(MercuryConstants.ACKS_TO)){
           throw new RMMessageBuildingException("Can not Address element in AcksTo element");
        }
        Accept accept = new Accept(rmNamesapce);
        accept.setAcceptERP(AcksTo.fromOM(acksTo).getEndpointAddress());
        return accept;
    }

    public boolean isValid(){
       return (this.acceptERP != null); 
    }

    public String getAcceptERP() {
        return acceptERP;
    }

    public void setAcceptERP(String acceptERP) {
        this.acceptERP = acceptERP;
    }

    public String getAddressingNamespace() {
        return addressingNamespace;
    }

    public void setAddressingNamespace(String addressingNamespace) {
        this.addressingNamespace = addressingNamespace;
    }
}
