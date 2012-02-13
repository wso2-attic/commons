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

package org.wso2.carbon.web.test.common;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;

public class CachingClient {


    public String cachClient(String epr, String operationName, String SoapAction,String NameSpace) throws org.apache.axis2.AxisFault {
        String globalData = "No Value";
        try {
            System.out.println(epr);

            ServiceClient sc = new ServiceClient();
            Options opts = new Options(); // need to create soap option object for assign soap options.
            opts.setTo(new EndpointReference(epr));
            opts.setAction(SoapAction); // soap action field copied from the wsdl file
            sc.setOptions(opts);

            OMElement result = sc.sendReceive(CreatePayload(operationName,NameSpace));
            System.out.println(result);
            globalData = result.toString();


        }
        catch (Exception e) {
            System.out.println(e);
        }
        return globalData;
    }

    public OMElement CreatePayload(String operationName,String Namespace) throws Exception// this function create a message to send to server
    {

        OMFactory fac = OMAbstractFactory.getOMFactory(); // Create object(fac)from OM Factory this is used to create all the elements.
        OMNamespace omns = fac.createOMNamespace(Namespace, "b"); // creating namespace to assign to message, b =  namespace
        OMElement OP1 = fac.createOMElement(operationName, omns);   // Operation name

        return OP1;
    }
}