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
import org.apache.axiom.om.OMElement;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.util.MercuryConstants;

public class AcknowledgmentRangeTest extends TestCase {

    public void testAcknowledgmentRange(){
        AcknowledgmentRange acknowledgmentRange = new AcknowledgmentRange(7,3);
        try {
            OMElement omElement = acknowledgmentRange.toOM();
            System.out.println("OM Element ==> " + omElement.toString());
            AcknowledgmentRange result = AcknowledgmentRange.fromOM(omElement);
            assertEquals(result.getRmNamespace(), MercuryConstants.RM_1_0_NAMESPACE);
            assertEquals(result.getUpper(),7);
            assertEquals(result.getLower(),3);
        } catch (RMMessageBuildingException e) {
            fail();
        }
    }
}
