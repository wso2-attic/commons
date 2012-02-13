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

import junit.framework.TestCase;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.addressing.AddressingConstants;


public class CreateSequenceMessageTest extends TestCase {

    public void testCreateSequenceMessage1(){
        CreateSequenceMessage createSequenceMessage =
                new CreateSequenceMessage(MercuryConstants.RM_1_0_NAMESPACE,
                        SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        createSequenceMessage.setAcksToAddress("http://org.org");
        createSequenceMessage.setAddressingNamespace(AddressingConstants.Final.WSA_NAMESPACE);
        try {
            SOAPEnvelope soapEnvelope = createSequenceMessage.toSOAPEnvelope();
            System.out.println("SOAP Envelop ==> " + soapEnvelope.toString());
            CreateSequenceMessage result = CreateSequenceMessage.fromSOAPEnvelope(soapEnvelope);
            assertEquals(result.getAcksToAddress(),"http://org.org");
            assertEquals(result.getAddressingNamespace(),AddressingConstants.Final.WSA_NAMESPACE);
            assertEquals(result.getRmNamespace(),MercuryConstants.RM_1_0_NAMESPACE);
            assertEquals(result.getSoapNamesapce(),SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        } catch (RMMessageBuildingException e) {
            fail();
        }
    }

    public void testCreateSequenceMessage2(){
        CreateSequenceMessage createSequenceMessage =
                new CreateSequenceMessage(MercuryConstants.RM_1_0_NAMESPACE,
                        SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        createSequenceMessage.setAcksToAddress("http://org.org");
        createSequenceMessage.setAddressingNamespace(AddressingConstants.Final.WSA_NAMESPACE);
        createSequenceMessage.setOfferIdentifier("testID");
        try {
            SOAPEnvelope soapEnvelope = createSequenceMessage.toSOAPEnvelope();
            System.out.println("SOAP Envelop ==> " + soapEnvelope.toString());
            CreateSequenceMessage result = CreateSequenceMessage.fromSOAPEnvelope(soapEnvelope);
            assertEquals(result.getAcksToAddress(),"http://org.org");
            assertEquals(result.getAddressingNamespace(),AddressingConstants.Final.WSA_NAMESPACE);
            assertEquals(result.getRmNamespace(),MercuryConstants.RM_1_0_NAMESPACE);
            assertEquals(result.getSoapNamesapce(),SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            assertEquals(result.getOfferIdentifier(),"testID");
        } catch (RMMessageBuildingException e) {
            fail();
        }
    }
}
