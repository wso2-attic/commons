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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.Constants;
import org.apache.sandesha2.client.SandeshaClientConstants;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

public class GenericServiceClient {

    public static Properties loadProperties() throws IOException {
        FileInputStream freader;

        Properties properties = new Properties();
        freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        freader.close();
        return properties;
    }

    // payload with 1 input
    public OMElement CreateRequestReplyPayload(String NameSpace, String operation, String param, String input) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(NameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        OMElement value = fac.createOMElement(param, omns);
        value.addChild(fac.createOMText(value, input));
        OP1.addChild(value);
        return OP1;
    }

    //payload with 0 inputs
    public OMElement CreateRequestReplyPayload(String NameSpace, String operation) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(NameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        return OP1;
    }

    //payload  with no inputs
    public OMElement CreateOneWayPayload(String NameSpace, String operation, String param) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(NameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        OMElement value = fac.createOMElement(param, omns);
        value.addChild(fac.createOMText(value, "ping"));
        OP1.addChild(value);
        return OP1;
    }

    //sendRecive client with one input
    public OMElement twoWayAnonClient(String serviceName, String SoapAction, String NameSpace, String operation, String param, String input) throws Exception {

        ServiceClient sc = new ServiceClient();
        Options opts = new Options();
        opts.setTo(new EndpointReference("http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + serviceName));
        opts.setAction(SoapAction);
        sc.setOptions(opts);
        OMElement result = sc.sendReceive(CreateRequestReplyPayload(NameSpace, operation, param, input));
        System.out.println(result.getFirstElement().getText());
        return result;
    }

    //send recieve client with no inputs
    public OMElement twoWayAnonClient(String serviceName, String SoapAction, String NameSpace, String operation) throws Exception {

        ServiceClient sc = new ServiceClient();
        Options opts = new Options();
        opts.setTo(new EndpointReference("http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + serviceName));
        opts.setAction(SoapAction);
        sc.setOptions(opts);
        OMElement result = sc.sendReceive(CreateRequestReplyPayload(NameSpace, operation));
        System.out.println(result.getFirstElement().getText());
        return result;
    }

    //fire and forget client with no inputs
    public void onewayAnonClient(String serviceName, String SoapAction, String NameSpace, String operation, String param) throws Exception {

        ServiceClient sc = new ServiceClient();
        Options opts = new Options();
        opts.setTo(new EndpointReference("http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") +  loadProperties().getProperty("context.root") + "/services/" + serviceName));
        opts.setAction(SoapAction);
        sc.setOptions(opts);
        sc.fireAndForget(CreateOneWayPayload(NameSpace, operation, param));

    }

    // payload with one input
    public OMElement PayloadOneInput(String NameSpace, String operation, String param1, String input1) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(NameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        OMElement value = fac.createOMElement(param1, omns);
        value.addChild(fac.createOMText(value, input1));
        OP1.addChild(value);
        return OP1;
       }

    // payload with two inputs
    public OMElement PayloadTwoInputs(String NameSpace, String operation, String param1, String input1,String param2,String input2) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(NameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        OMElement value = fac.createOMElement(param1, omns);
        value.addChild(fac.createOMText(value, input1));
        OP1.addChild(value);
        value = fac.createOMElement(param2, omns);
        value.addChild(fac.createOMText(value, input2));
        OP1.addChild(value);
        return OP1;
       }

    //fire a nd forget client with one input
    public void oneWayClientOneInput(String serviceName, String SoapAction, String NameSpace, String operation, String param1, String input1) throws Exception {

        ServiceClient sc = new ServiceClient();
        Options opts = new Options();
        opts.setTo(new EndpointReference("http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + serviceName));
        opts.setAction(SoapAction);
        sc.setOptions(opts);
         sc.fireAndForget(PayloadOneInput(NameSpace, operation, param1, input1));
        //System.out.println(result.getFirstElement().getText());

    }

     //fire a nd forget client with two inputs
     public void oneWayClientTwoInputs(String serviceName, String SoapAction, String NameSpace, String operation, String param1, String input1,String param2,String input2) throws Exception {

         ServiceClient sc = new ServiceClient();
        Options opts = new Options();
        opts.setTo(new EndpointReference("http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + serviceName));
        opts.setAction(SoapAction);
        sc.setOptions(opts);
         sc.fireAndForget(PayloadTwoInputs(NameSpace, operation, param1, input1,param2,input2));
        //System.out.println(result.getFirstElement().getText());

    }

    //payload with three inputs
     public OMElement PayloadThreeInputs(String NameSpace, String operation, String param1, String input1,String param2,String input2,String param3,String input3) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(NameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        OMElement value = fac.createOMElement(param1, omns);
        value.addChild(fac.createOMText(value, input1));
        OP1.addChild(value);
        value = fac.createOMElement(param2, omns);
        value.addChild(fac.createOMText(value, input2));
        OP1.addChild(value);
        value = fac.createOMElement(param3, omns);
        value.addChild(fac.createOMText(value, input3));
        OP1.addChild(value);
        return OP1;
       }

      //fire a nd forget client with three inputs
     public void onewayClientThreeInputs(String serviceName, String SoapAction, String NameSpace, String operation, String param1, String input1,String param2,String input2,String param3,String input3) throws Exception {

         ServiceClient sc = new ServiceClient();
        Options opts = new Options();
        opts.setTo(new EndpointReference("http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + serviceName));
        opts.setAction(SoapAction);
        sc.setOptions(opts);
         sc.fireAndForget(PayloadThreeInputs(NameSpace, operation, param1, input1,param2,input2,param3,input3));
        //System.out.prim.out.println(result.getFirstElement().getText());

    }



}
