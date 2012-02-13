/*
 *  Copyright (c) 2005-2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.web.test.common;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
//import org.apache.axis.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

public class throttleClient {
    // if max request count more than 7 additional request occur.
    public int throttleClient(String epr, String operationName, String SoapAction,String namespace, String firstChild) throws org.apache.axis2.AxisFault {
        int cCounter = 0;
        try {
            System.out.println(epr);

                ServiceClient sc = new ServiceClient();
                Options opts = new Options(); // need to create soap option object for assign soap options.
                opts.setTo(new EndpointReference(epr));
                opts.setAction(SoapAction); // soap action field copied from the wsdl file
                sc.setOptions(opts);
          for (int i = 0; i <= 15; i++) {
                OMElement result = sc.sendReceive(CreatePayload(operationName,namespace, firstChild));
                System.out.println(result);
                cCounter = cCounter + 1;
            }

        }
        catch (Exception e) {
            System.out.println(e);
        }
        return cCounter;
    }

    public OMElement CreatePayload(String operationName,String namespace, String firstChild) throws Exception// this function create a message to send to server
    {

        OMFactory fac = OMAbstractFactory.getOMFactory(); // Create object(fac)from OM Factory this is used to create all the elements.
        OMNamespace omns = fac.createOMNamespace(namespace, "b"); // creating namespace to assign to message, b =  namespace
        OMElement OP1 = fac.createOMElement(operationName, omns);   // Operation name
        OMElement value = fac.createOMElement(firstChild, omns);
        value.addChild(fac.createOMText(value, "WSO2 QA ..."));
        OP1.addChild(value);
        return OP1;
    }
}
