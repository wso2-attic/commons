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

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.om.OMAbstractFactory;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.util.MercuryConstants;

/**
 * each RM specific message class should extends from this class
 * in addition to toSOAPEnvelope those classes should have
 * static fromSOAPEnvelope to parse the data.
 */
public abstract class RMMessage {

    protected String rmNamespace;
    protected String soapNamesapce;

    protected RMMessage() {
        this.rmNamespace = MercuryConstants.RM_1_0_NAMESPACE;
        this.soapNamesapce = SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI;
    }

    protected RMMessage(String rmNamespace, String soapNamesapce) {
        this.rmNamespace = rmNamespace;
        this.soapNamesapce = soapNamesapce;
    }

    protected SOAPFactory getSoapFactory() {
        SOAPFactory soapFactory = null;
        if ((soapNamesapce != null) &&
                soapNamesapce.equals(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI)) {
            soapFactory = OMAbstractFactory.getSOAP12Factory();
        } else {
            // default is taken as soap 1.1
            soapFactory = OMAbstractFactory.getSOAP11Factory();
        }
        return soapFactory;
    }

    // all the RMMessages are supposed to return the correct soap envelop
    // to serialize them
    public abstract SOAPEnvelope toSOAPEnvelope() throws RMMessageBuildingException;

    public String getRmNamespace() {
        return rmNamespace;
    }

    public void setRmNamespace(String rmNamespace) {
        this.rmNamespace = rmNamespace;
    }

    public String getSoapNamesapce() {
        return soapNamesapce;
    }

    public void setSoapNamesapce(String soapNamesapce) {
        this.soapNamesapce = soapNamesapce;
    }
}
