/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.
 
  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
 
  http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/
package org.wso2.carbon.mediators.xslt.test;

import junit.framework.Assert;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;


public class Client {
    private static final Log log = LogFactory.getLog(Client.class);

    public OMElement InvokeClient() throws AxisFault {

        OMElement result = null;
        try {
            ServiceClient sc = new ServiceClient();
            Options opts = new Options(); // need to create soap option object for assign soap options.
            if(FrameworkSettings.STRATOS.equalsIgnoreCase("false")){
            opts.setTo(new EndpointReference("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT));
            }
            else if(FrameworkSettings.STRATOS.equalsIgnoreCase("true")){
                opts.setTo(new EndpointReference("http://" + FrameworkSettings.HOST_NAME));
            }
            opts.setAction("echoString"); // soap action field copied from the wsdl file
            sc.setOptions(opts);
            result = sc.sendReceive(CreatePayload("http://service.carbon.wso2.org", "echoString", "ns"));
        }
        catch (Exception e) {
            log.fatal("Error in invoke client : " + e.toString());
            Assert.fail("Error in invoke client : " + e.toString());
        }
        return result;
    }

    public static OMElement CreatePayload(String namespace, String operationName, String firstChild) // this function create a message to send to server
    {
        OMFactory fac = OMAbstractFactory.getOMFactory(); // Create object(fac)from OM Factory this is used to create all the elements.
        OMNamespace omns = fac.createOMNamespace(namespace, "s"); // creating namespace to assign to message, b =  namespace
        OMElement OP1 = fac.createOMElement(operationName, omns); // Operation name
        OMElement value = fac.createOMElement(firstChild, omns);
        value.addChild(fac.createOMText(value, "ECHOOOOO!!!!!!!!!!!!!!"));
        OP1.addChild(value);
        return OP1;
    }
}
