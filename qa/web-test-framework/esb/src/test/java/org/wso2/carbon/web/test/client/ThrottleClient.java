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

package org.wso2.carbon.web.test.client;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAP12Constants;
//import org.apache.axis.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.Constants;
import org.apache.axis2.AxisFault;
import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

public class ThrottleClient extends TestCase{
   Selenium selenium;

   public ThrottleClient(Selenium _browser) {
        selenium = _browser;
   }

    /*
    This method is used to create the message payload for 'echo' service
     */
    public OMElement createPayload_echo(String operationName,String namespace, String firstChild) throws Exception// this function create a message to send to server
    {
//         <p:echoInt xmlns:p="http://echo.services.core.carbon.wso2.org">
//                  <in>3</in>
//         </p:echoInt>

        OMFactory fac = OMAbstractFactory.getOMFactory(); // Create object(fac)from OM Factory this is used to create all the elements.
        OMNamespace omNs = fac.createOMNamespace(namespace, "p"); // creating namespace to assign to message, b =  namespace
        OMElement OP1 = fac.createOMElement(operationName, omNs);   // Operation name
        OMElement value = fac.createOMElement(firstChild,null);
        value.addChild(fac.createOMText(value, "3"));
        OP1.addChild(value);
        return OP1;
    }


    public int throttleClient(String trpUrl,String epr, String operationName, String SoapAction,String namespace, String firstChild) throws Exception {
        int cCounter = 0;
        try {
            ServiceClient sc = new ServiceClient();
            Options opts = new Options(); // need to create soap option object for assign soap options.

            if (trpUrl != null && !"null".equals(trpUrl)) {
            opts.setProperty(Constants.Configuration.TRANSPORT_URL, trpUrl);
            }
            opts.setTo(new EndpointReference(epr));
            opts.setAction(SoapAction); // soap action field copied from the wsdl file
            opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            sc.setOptions(opts);

            //sc.engageModule("addressing");


            OMElement payload = createPayload_echo(operationName,namespace, firstChild);
            System.out.println(payload);

            //OMElement result = sc.sendReceive(payload);
            for (int i = 0; i < 15; i++) {
                OMElement result = sc.sendReceive(payload);
                System.out.println(result);
                cCounter = cCounter + 1;
            }

        }
        catch (AxisFault e) {
            System.out.println("AxisFault Reason== "+e.getReason());
            //OnReject sequence contains a  Fault mediator with Fault String set as "**Access Denied**".
            //So that if the client fails because of Throttle rejection then fault reason should be "**Access Denied**"
            assertTrue(e.getReason().equals("**Access Denied**"));
            e.printStackTrace();
        }
        return cCounter;
    }
}
