package org.wso2.carbon.web.test.client;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.Constants;
import org.apache.sandesha2.client.SandeshaClientConstants;
import org.wso2.carbon.web.test.esb.ESBCommon;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

import com.thoughtworks.selenium.Selenium;/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

public class ESBRMClient {
    Selenium selenium;



    public OMElement CreateRequestReplyPayload(String NameSpace, String operation, String param) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(NameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        OMElement value = fac.createOMElement(param, omns);
        value.addChild(fac.createOMText(value, "RM Two way messaging!!!!"));
        OP1.addChild(value);
        return OP1;
    }

    /*
    This method created the message payload
     */
	public static OMElement createPayLoad(String NameSpace, String operation, String param1, String param2){
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace(NameSpace, "ns");
		OMElement method = fac.createOMElement(operation, omNs);
		OMElement value1 = fac.createOMElement(param1, omNs);
        OMElement value2 = fac.createOMElement(param2, omNs);

        value2.addChild(fac.createOMText(value1, "IBM"));
        value1.addChild(value2);
        method.addChild(value1);

        return method;
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

   /*
    This method will handle RM disabled messages which are being sent to RM enabled services
     */
    public boolean NonRMRequestReplyAnonClient(String serviceName, String soapversion, String SoapAction, String NameSpace, String operation, String param1, String param2) throws Exception {
        ESBCommon esbCommon = new ESBCommon(selenium);
        int count = 0;
        //ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(esbCommon.getCarbonHome() + File.separator + "repository");
       ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("." + File.separator + "lib"+ File.separator+"client_repo", null);
       ServiceClient sc = new ServiceClient(cc,null);
       sc.engageModule("sandesha2");
        //toDo - find a way to engage addressing without using configContext
        sc.engageModule("addressing");
        Options opts = new Options();
        if (esbCommon.getContextRoot().equals(null))
        {
            opts.setTo(new EndpointReference("http://" + esbCommon.getHostName() + ":" + esbCommon.getNioHttpPort() + "/services/" + serviceName));
        }
        else
        {
            opts.setTo(new EndpointReference("http://" + esbCommon.getHostName() + ":" + esbCommon.getNioHttpPort() + "/" + esbCommon.getContextRoot()+ "/services/" + serviceName));
        }
        opts.setAction(SoapAction);
        if (soapversion.equals("soap12")) {
            opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        }
        sc.setOptions(opts);
        for (int i = 0; i < 10; i++) {
            OMElement result = sc.sendReceive(createPayLoad(NameSpace, operation, param1, param2));
            System.out.println(result.getFirstElement().getText());
            count++;
        }
        Thread.sleep(1000);
        //Setting the last message
        sc.getOptions().setProperty(SandeshaClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);


        OMElement result = sc.sendReceive(createPayLoad(NameSpace, operation, param1, param2));
        System.out.println(result.getFirstElement().getText());
        boolean output;
        output = result.getChildren().next().toString().contains("IBM Company");
//        return count + 1;
       esbCommon.closeFiles();
        return output;
    }

    /*
    This method will handle RM disabled messages which are being sent to RM enabled services
     */
    public int NonRMRequestReplyAddressableClient(String serviceName, String soapversion, String SoapAction, String NameSpace, String operation, String param1, String param2) throws Exception {
        ESBCommon esbCommon = new ESBCommon(selenium);
        int count = 0;
        boolean response;
        //ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(esbCommon.getCarbonHome() + File.separator + "repository", ".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "client-repository" + File.separator + "conf" + File.separator + "axis2.xml");
        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("." + File.separator + "lib"+ File.separator+"client_repo", null);
        ServiceClient sc = new ServiceClient(cc, null);
        AxisCallback Callback = new AxisCallback() {
            public void onMessage(MessageContext msgContext) {
                System.out.println("Got the message ==> " + msgContext.getEnvelope().getBody().getFirstElement());
                System.out.println(msgContext.getEnvelope().getBody().getChildren().next().toString().contains("IBM Company"));
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
        //toDo - find a way to engage addressing without using configContext
        sc.engageModule("addressing");
        Options opts = new Options();
        if (esbCommon.getContextRoot().equals(null))
        {
            opts.setTo(new EndpointReference("http://" + esbCommon.getHostName() + ":" + esbCommon.getNioHttpPort() + "/services/" + serviceName));
        }
        else
        {
            opts.setTo(new EndpointReference("http://" + esbCommon.getHostName() + ":" + esbCommon.getNioHttpPort() + "/" + esbCommon.getContextRoot()+ "/services/" + serviceName));
        }
        opts.setAction(SoapAction);
        if (soapversion.equals("soap12")) {
            opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        }
        sc.setOptions(opts);
        for (int i = 0; i < 10; i++) {
            sc.sendReceiveNonBlocking(createPayLoad(NameSpace, operation, param1, param2), Callback);
            count++;
        }
        Thread.sleep(1000);
        //Setting the last message
        sc.getOptions().setProperty(SandeshaClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
        sc.sendReceiveNonBlocking(createPayLoad(NameSpace, operation, param1, param2), Callback);
        cc.getListenerManager().stop();
       esbCommon.closeFiles();
        return count + 1;
    }

    /*
    This method will handle RM disabled messages which are being sent to RM enabled services
     */    
    public void NonRMOnewayAnonClient(String serviceName, String soapversion, String SoapAction, String NameSpace, String operation, String param1, String param2) throws Exception {
        ESBCommon esbCommon = new ESBCommon(selenium);
        int count = 0;

        //ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(esbCommon.getCarbonHome() + File.separator + "repository");
        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("." + File.separator + "lib"+ File.separator+"client_repo", null);
        ServiceClient sc = new ServiceClient(cc, null);
        //toDo - find a way to engage addressing without using configContext
        //sc.engageModule("addressing");
        Options opts = new Options();
        if (esbCommon.getContextRoot().equals(null))
        {
            opts.setTo(new EndpointReference("http://" + esbCommon.getHostName() + ":" + esbCommon.getNioHttpPort() + "/services/" + serviceName));
        }
        else
        {
            opts.setTo(new EndpointReference("http://" + esbCommon.getHostName() + ":" + esbCommon.getNioHttpPort() + "/" + esbCommon.getContextRoot()+ "/services/" + serviceName));
        }        opts.setAction(SoapAction);
        if (soapversion.equals("soap12")) {
            opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        }
        sc.setOptions(opts);
        for (int i = 0; i < 10; i++) {
            sc.fireAndForget(createPayLoad(NameSpace, operation, param1, param2));
        }
        Thread.sleep(1000);
        //Setting the last message
        sc.getOptions().setProperty(SandeshaClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
        sc.fireAndForget(createPayLoad(NameSpace, operation, param1, param2));
       esbCommon.closeFiles();
    }

    /*
    This method will handle RM enabled messages which are being sent to RM disabled services
     */
    public boolean RMRequestReplyAnonClient(String serviceName, String soapversion, String SoapAction, String NameSpace, String operation, String param1, String param2) throws Exception {
        ESBCommon esbCommon = new ESBCommon(selenium);
        int count = 0;
        //ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(esbCommon.getCarbonHome() + File.separator + "repository");
        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("." + File.separator + "lib"+ File.separator+"client_repo", null);
        ServiceClient sc = new ServiceClient(cc, null);
        sc.engageModule("sandesha2");
        //toDo - find a way to engage addressing without using configContext
        sc.engageModule("addressing");
        Options opts = new Options();
        if (esbCommon.getContextRoot().equals(null))
        {
            opts.setTo(new EndpointReference("http://" + esbCommon.getHostName() + ":" + esbCommon.getNioHttpPort() + "/services/" + serviceName));
        }
        else
        {
            opts.setTo(new EndpointReference("http://" + esbCommon.getHostName() + ":" + esbCommon.getNioHttpPort() + "/" + esbCommon.getContextRoot()+ "/services/" + serviceName));
        }        opts.setAction(SoapAction);
        if (soapversion.equals("soap12")) {
            opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        }
        sc.setOptions(opts);
        for (int i = 0; i < 10; i++) {
            OMElement result = sc.sendReceive(createPayLoad(NameSpace, operation, param1, param2));
            System.out.println(result.getFirstElement().getText());
            count++;
        }
        Thread.sleep(1000);
        //Setting the last message
        sc.getOptions().setProperty(SandeshaClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);


        OMElement result = sc.sendReceive(createPayLoad(NameSpace, operation, param1, param2));
        System.out.println(result.getFirstElement().getText());
        boolean output;
        output = result.getChildren().next().toString().contains("IBM Company");
       esbCommon.closeFiles();
        return output;
    }

    /*
    This method will handle RM enabled messages which are being sent to RM disabled services
     */
    public int RMRequestReplyAddressableClient(String serviceName, String soapversion, String SoapAction, String NameSpace, String operation, String param1, String param2) throws Exception {
        ESBCommon esbCommon = new ESBCommon(selenium);
        int count = 0;
        boolean response;
        //ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(esbCommon.getCarbonHome() + File.separator + "repository", ".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "client-repository" + File.separator + "conf" + File.separator + "axis2.xml");
        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("." + File.separator + "lib"+ File.separator+"client_repo", null);
        ServiceClient sc = new ServiceClient(cc, null);
        AxisCallback Callback = new AxisCallback() {
            public void onMessage(MessageContext msgContext) {
                System.out.println("Got the message ==> " + msgContext.getEnvelope().getBody().getFirstElement());
                System.out.println(msgContext.getEnvelope().getBody().getChildren().next().toString().contains("IBM Company"));
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
        if (esbCommon.getContextRoot().equals(null))
        {
            opts.setTo(new EndpointReference("http://" + esbCommon.getHostName() + ":" + esbCommon.getNioHttpPort() + "/services/" + serviceName));
        }
        else
        {
            opts.setTo(new EndpointReference("http://" + esbCommon.getHostName() + ":" + esbCommon.getNioHttpPort() + "/" + esbCommon.getContextRoot()+ "/services/" + serviceName));
        }
        opts.setAction(SoapAction);
        if (soapversion.equals("soap12")) {
            opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        }
        sc.setOptions(opts);
        for (int i = 0; i < 10; i++) {
            sc.sendReceiveNonBlocking(createPayLoad(NameSpace, operation, param1, param2), Callback);
            count++;
        }
        Thread.sleep(1000);
        //Setting the last message
        sc.getOptions().setProperty(SandeshaClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
        sc.sendReceiveNonBlocking(createPayLoad(NameSpace, operation, param1, param2), Callback);
        cc.getListenerManager().stop();
       esbCommon.closeFiles();
        return count + 1;
    }

    /*
    This method will handle RM enabled messages which are being sent to RM disabled services
     */
    public void RMOnewayAnonClient(String serviceName, String soapversion, String SoapAction, String NameSpace, String operation, String param1, String param2) throws Exception {
        ESBCommon esbCommon = new ESBCommon(selenium);
        int count = 0;
        //ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(esbCommon.getCarbonHome() + File.separator + "repository");
        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("." + File.separator + "lib"+ File.separator+"client_repo", null);
        ServiceClient sc = new ServiceClient(cc, null);
        sc.engageModule("sandesha2");
        //toDo - find a way to engage addressing without using configContext
        sc.engageModule("addressing");
        Options opts = new Options();

        if (esbCommon.getContextRoot().equals(null))
        {
            opts.setTo(new EndpointReference("http://" + esbCommon.getHostName() + ":" + esbCommon.getNioHttpPort() + "/services/" + serviceName));
        }
        else
        {
            opts.setTo(new EndpointReference("http://" + esbCommon.getHostName() + ":" + esbCommon.getNioHttpPort() + "/" + esbCommon.getContextRoot()+ "/services/" + serviceName));
        }

        opts.setAction(SoapAction);
        if (soapversion.equals("soap12")) {
            opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        }
        sc.setOptions(opts);
        for (int i = 0; i < 10; i++) {
            sc.fireAndForget(createPayLoad(NameSpace, operation, param1, param2));
        }
        Thread.sleep(1000);
        //Setting the last message
        sc.getOptions().setProperty(SandeshaClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
        sc.fireAndForget(createPayLoad(NameSpace, operation, param1, param2));
       esbCommon.closeFiles();
    }
    
}