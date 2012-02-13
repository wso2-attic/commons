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

import org.mortbay.util.TestCase;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.sandesha2.client.SandeshaClientConstants;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.util.Properties;


public class RMClient {


    public OMElement CreateRequestReplyPayload(String NameSpace, String operation, String param) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(NameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        OMElement value = fac.createOMElement(param, omns);
        value.addChild(fac.createOMText(value, "RM Two way messaging!!!!"));
        OP1.addChild(value);
        return OP1;
    }


    public OMElement CreateOneWayPayload(String NameSpace, String operation, String param) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(NameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        OMElement value = fac.createOMElement(param, omns);
        value.addChild(fac.createOMText(value, "ping"));
        OP1.addChild(value);
        return OP1;
    }

    public static Properties loadProperties() throws IOException {
       Properties properties = new Properties();
        FileInputStream freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        freader.close();
        return properties;
    }

    public int RMRequestReplyAnonClient(String serviceName, String soapversion, String SoapAction, String NameSpace, String operation, String param) throws Exception {

        int count = 0;
        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(loadProperties().getProperty("carbon.home") + File.separator + "repository");
        ServiceClient sc = new ServiceClient(cc, null);
        sc.engageModule("sandesha2");
        //toDo - find a way to engage addressing without using configContext
        sc.engageModule("addressing");
        Options opts = new Options();
        opts.setTo(new EndpointReference("http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.port") +  loadProperties().getProperty("context.root")+"/services/" + serviceName));
        opts.setAction(SoapAction);
        if (soapversion.equals("soap12")) {
            opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        }
        sc.setOptions(opts);
        for (int i = 0; i < 10; i++) {
            OMElement result = sc.sendReceive(CreateRequestReplyPayload(NameSpace, operation, param));
            System.out.println(result.getFirstElement().getText());
            count++;
        }
        Thread.sleep(1000);
        //Setting the last message
        sc.getOptions().setProperty(SandeshaClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);


        OMElement result = sc.sendReceive(CreateRequestReplyPayload(NameSpace, operation, param));
        System.out.println(result.getFirstElement().getText());
        return count + 1;
    }

    public void RMOnewayAnonClient(String serviceName, String soapversion, String SoapAction, String NameSpace, String operation, String param) throws Exception {

        int count = 0;

        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(loadProperties().getProperty("carbon.home") + File.separator + "repository");
        ServiceClient sc = new ServiceClient(cc, null);
        sc.engageModule("sandesha2");
        //toDo - find a way to engage addressing without using configContext
        sc.engageModule("addressing");
        Options opts = new Options();
        opts.setTo(new EndpointReference("http://" + loadProperties().getProperty("hostname") + ":" + loadProperties().getProperty("port") + loadProperties().getProperty("context.root")+ "/services/" + serviceName));
        opts.setAction(SoapAction);
        if (soapversion.equals("soap12")) {
            opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        }
        sc.setOptions(opts);
        for (int i = 0; i < 10; i++) {
            sc.fireAndForget(CreateOneWayPayload(NameSpace, operation, param));
        }
        Thread.sleep(1000);
        //Setting the last message
        sc.getOptions().setProperty(SandeshaClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
        sc.fireAndForget(CreateOneWayPayload(NameSpace, operation, param));

    }

    public int RMRequestReplyAddressableClient(String serviceName, String soapversion, String SoapAction, String NameSpace, String operation, String param) throws Exception {

        int count = 0;
        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(loadProperties().getProperty("carbon.home") + File.separator + "repository", ".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "client-repository" + File.separator + "conf" + File.separator + "axis2.xml");
        ServiceClient sc = new ServiceClient(cc, null);
        AxisCallback Callback = new AxisCallback() {
            public void onMessage(MessageContext msgContext) {
                System.out.println("Got the message ==> " + msgContext.getEnvelope().getBody().getFirstElement());
            }

            public void onFault(MessageContext msgContext) {

            }

            public void onError(Exception e) {
                e.printStackTrace();
                System.out.println("Received an error ...");
            }

            public void onComplete() {

            }
        };
        sc.engageModule("sandesha2");
        //toDo - find a way to engage addressing without using configContext
        sc.engageModule("addressing");
        Options opts = new Options();
        opts.setTo(new EndpointReference("http://" + loadProperties().getProperty("hostname") + ":" + loadProperties().getProperty("port") + loadProperties().getProperty("context.root")+ "/services/" + serviceName));
        opts.setAction(SoapAction);
        opts.setUseSeparateListener(true);
        if (soapversion.equals("soap12")) {
            opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        }
        sc.setOptions(opts);
        for (int i = 0; i < 10; i++) {
            sc.sendReceiveNonBlocking(CreateRequestReplyPayload(NameSpace, operation, param), Callback);
            count++;
        }
        Thread.sleep(1000);
        //Setting the last message
        sc.getOptions().setProperty(SandeshaClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
        sc.sendReceiveNonBlocking(CreateRequestReplyPayload(NameSpace, operation, param), Callback);
        cc.getListenerManager().stop();
        return count + 1;
    }


}
