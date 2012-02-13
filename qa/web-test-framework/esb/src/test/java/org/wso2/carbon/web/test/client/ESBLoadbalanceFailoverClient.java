package org.wso2.carbon.web.test.client;

/*
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


import com.thoughtworks.selenium.Selenium;

import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.wsdl.WSDLConstants;

import javax.xml.namespace.QName;
import java.util.*;
import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;


public class ESBLoadbalanceFailoverClient {
    Selenium selenium;

    public String sessionlessClient(String trpUrl, String addUrl,String pIterations ) throws AxisFault {

        int iterations = 100;
        boolean infinite = true;

        if (pIterations != null) {
            try {
                iterations = Integer.parseInt(pIterations);
                if (iterations != -1) {
                    infinite = false;
                }
            } catch (NumberFormatException e) {
                // run with default values
            }
        }

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMElement value = fac.createOMElement("Value", null);
        value.setText("Sample string");

        Options options = new Options();
         if (addUrl != null) {
             options.setTo(new EndpointReference(addUrl));
         }

        options.setAction("urn:sampleOperation");
        ConfigurationContext configContext = ConfigurationContextFactory.
                    createConfigurationContextFromFileSystem("." + File.separator + "lib"+ File.separator+"client_repo", null);

        ServiceClient client = new ServiceClient(configContext, null);
        long timeout = 60000;
        System.out.println("timeout=" + timeout);
        options.setTimeOutInMilliSeconds(timeout);

        // set addressing, transport
        if (addUrl != null && !"null".equals(addUrl)) {
            client.engageModule("addressing");
            options.setTo(new EndpointReference(addUrl));
        }
        if (trpUrl != null && !"null".equals(trpUrl)) {
            options.setProperty(Constants.Configuration.TRANSPORT_URL, trpUrl);
        } else {
            client.engageModule("addressing");
        }
        client.setOptions(options);
        String testString = "";

        long i = 0;
        while (i < iterations || infinite) {
            client.getOptions().setManageSession(true);
            OMElement responseElement = client.sendReceive(value);
            String response = responseElement.getText();

            i++;
            System.out.println("Request: " + i + " ==> " + response);
            testString += (":" + i + ">" + response + ":");
        }
        return testString;
    }
}
