/**
 *  Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.endpoint;

import org.junit.Assert;
import org.junit.Test;
import org.wso2.carbon.endpoint.ui.types.EndpointAdminException;
import org.wso2.carbon.endpoint.ui.types.common.to.AddressEndpointData;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: suho
 * Date: Jun 7, 2010
 * Time: 4:46:34 PM
 * Testing addition and removal of Endpoints to ESB
 */
public class EndpointTest extends EndpointTestCase {

    @Test
    public void Test() throws RemoteException, EndpointAdminException {

        String addressEp = "<endpoint xmlns=\"http://ws.apache.org/ns/synapse\" name=\"SimpleStockQuoteService\">\n" +
                "   <address uri=\"http://localhost:9000/services/SimpleStockQuoteService\" >\n" +
                "      <suspendOnFailure>\n" +
                "         <progressionFactor>1.0</progressionFactor>\n" +
                "      </suspendOnFailure>\n" +
                "      <markForSuspension>\n" +
                "         <retriesBeforeSuspension>0</retriesBeforeSuspension>\n" +
                "         <retryDelay>0</retryDelay>\n" +
                "      </markForSuspension>\n" +
                "   </address>\n" +
                "</endpoint>";
        if (endpointAdminStub.getEndPointsNames() != null) {
            List epList = Arrays.asList(endpointAdminStub.getEndPointsNames());
            if (epList.contains("SimpleStockQuoteService")) {
                endpointAdminStub.deleteEndpoint("SimpleStockQuoteService");
            }
        }
        endpointAdminStub.addEndpoint(addressEp);
        Assert.assertEquals(endpointAdminStub.getEndpointCount(),1);

        if (endpointAdminStub.getEndpointCount() == 1) {
            AddressEndpointData addressEndpointData = endpointAdminStub.getAddressEndpoint("SimpleStockQuoteService");

            Assert.assertEquals(addressEndpointData.getAddress(),"http://localhost:9000/services/SimpleStockQuoteService");
            endpointAdminStub.deleteEndpoint("SimpleStockQuoteService");
            Assert.assertEquals(endpointAdminStub.getEndpointCount(),0);
        }
    }


}
