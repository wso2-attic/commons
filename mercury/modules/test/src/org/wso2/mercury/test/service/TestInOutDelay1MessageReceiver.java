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
package org.wso2.mercury.test.service;

import org.apache.axis2.receivers.AbstractInOutMessageReceiver;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.AxisFault;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

public class TestInOutDelay1MessageReceiver extends AbstractInOutMessageReceiver {

    public void invokeBusinessLogic(MessageContext messageContext,
                                    MessageContext messageContext1)
            throws AxisFault {
        System.out.println("Got the soap message ==> " + messageContext.getEnvelope().getBody().getFirstElement());

        try {
            System.out.println("Sleeping ...");
            Thread.sleep(10000);
        } catch (InterruptedException e) {}
        System.out.println("Going out from the service");

        String soapNamespace = messageContext.getEnvelope().getNamespace().getNamespaceURI();
        SOAPFactory soapFactory = null;
        if (soapNamespace.equals(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI)) {
            soapFactory = OMAbstractFactory.getSOAP11Factory();
        } else {
            soapFactory = OMAbstractFactory.getSOAP12Factory();
        }
        SOAPEnvelope soapEnv = soapFactory.getDefaultEnvelope();
        messageContext1.setEnvelope(soapEnv);
        if (messageContext.getEnvelope().getBody().getFirstElement() != null) {
            String message = messageContext.getEnvelope().getBody().getFirstElement().getText();
            soapEnv.getBody().addChild(getTestOMElement(message));
        }
    }

    private OMElement getTestOMElement(String text){
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMNamespace omNamespace = omFactory.createOMNamespace("http://wso2.org/temp1","ns1");
        OMElement omElement = omFactory.createOMElement("TestElement",omNamespace);
        omElement.setText("Reply " + text);
        return omElement;
    }

}
