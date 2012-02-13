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
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.wso2.mercury.exception.RMMessageBuildingException;


public class SequenceAcknowledgmentTest extends TestCase {

    public void testSequenceAcknowledgment(){
        SequenceAcknowledgment sequenceAcknowledgment = new SequenceAcknowledgment("testID");
        sequenceAcknowledgment.addAcknowledgmentRange(new AcknowledgmentRange(7,5));
        sequenceAcknowledgment.addAcknowledgmentRange(new AcknowledgmentRange(4,1));
        try {
            SOAPHeaderBlock soapHeaderBlock = sequenceAcknowledgment.toSOAPHeaderBlock();
            System.out.println("SOAP Envelope ==> " + soapHeaderBlock.toString());
            SequenceAcknowledgment result = SequenceAcknowledgment.fromSOAPHeaderBlock(soapHeaderBlock);
            System.out.println("OK");
        } catch (RMMessageBuildingException e) {
            fail();
        }
    }

}
