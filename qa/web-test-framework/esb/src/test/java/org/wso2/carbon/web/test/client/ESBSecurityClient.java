package org.wso2.carbon.web.test.client;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.Constants;
import org.apache.rampart.RampartMessageData;
import org.apache.rampart.policy.model.RampartConfig;
import org.apache.rampart.policy.model.CryptoConfig;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.ws.security.WSPasswordCallback;
import org.wso2.carbon.web.test.esb.ESBCommon;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

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

public class ESBSecurityClient  implements CallbackHandler {
        Selenium selenium;
    public boolean runSecurityClient(String serviceName, String Namespace, String SoapAction, String operation, String param1, String param2) throws Exception {
        ESBCommon esbCommon = new ESBCommon(selenium);
        OMElement result = null;
        boolean output = false;
        boolean stockQuoteResponse = false;
        //Reading the framework.properties file

        //Reading the esb.properties file
        System.setProperty("javax.net.ssl.trustStore", esbCommon.getCarbonHome() + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

        ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(esbCommon.getCarbonHome() + File.separator + "repository", null);
        ServiceClient sc = new ServiceClient(ctx, null);
        sc.engageModule("rampart");
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

        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            System.out.println("Client Failed!!!!");
        }
        Thread.sleep(5000);


        opts.setTo(new EndpointReference("http://" + esbCommon.getHostName() + ":" + esbCommon.getNioHttpPort() + "/services/" + serviceName));
        
        opts.setAction(SoapAction);
        try {
            opts.setProperty(RampartMessageData.KEY_RAMPART_POLICY, loadPolicy(".." + File.separator + "esb" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "client_policy_3.xml"));
            //opts.setProperty(RampartMessageData.KEY_RAMPART_POLICY, loadPolicy("D:\\wsas\\wsas-3.0\\wso2wsas-3.0\\samples\\conf\\rampart\\scenario4-policy.xml"));
            //options.setSoapVersionURI(Constants.URI_SOAP12_ENV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sc.setOptions(opts);
            result = sc.sendReceive(createPayLoad(Namespace, operation, param1, param2));
            output = result.getChildren().next().toString().contains("IBM Company");
            System.out.println(result.getFirstElement().getText());
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
       esbCommon.closeFiles();
        return output;
    }


    public Policy loadPolicy(String xmlPath) throws Exception {
        StAXOMBuilder builder = new StAXOMBuilder(xmlPath);
        Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());

        RampartConfig rc = new RampartConfig();

        rc.setUser("alice");
        rc.setUserCertAlias("bob");
        rc.setEncryptionUser("bob");
        rc.setPwCbClass(ESBSecurityClient.class.getName());

        CryptoConfig sigCryptoConfig = new CryptoConfig();

        sigCryptoConfig.setProvider("org.apache.ws.security.components.crypto.Merlin");

        File file = new File(".." + File.separator + "esb" + File.separator + "lib" + File.separator + "store.jks");

        if (!file.exists()) {
            throw new Exception("Key store cannot be found");
        }

        String filepath = file.getCanonicalPath();

        Properties prop1 = new Properties();
        prop1.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
        prop1.put("org.apache.ws.security.crypto.merlin.file", filepath);
        prop1.put("org.apache.ws.security.crypto.merlin.keystore.password", "password");
        sigCryptoConfig.setProp(prop1);

        CryptoConfig encrCryptoConfig = new CryptoConfig();
        encrCryptoConfig.setProvider("org.apache.ws.security.components.crypto.Merlin");

        Properties prop2 = new Properties();

        prop2.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
        prop2.put("org.apache.ws.security.crypto.merlin.file", filepath);
        prop2.put("org.apache.ws.security.crypto.merlin.keystore.password", "password");
        encrCryptoConfig.setProp(prop2);

        rc.setSigCryptoConfig(sigCryptoConfig);
        rc.setEncrCryptoConfig(encrCryptoConfig);

        policy.addAssertion(rc);
        return policy;
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
        System.out.println(method.toString());
        return method;
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        WSPasswordCallback pwcb = (WSPasswordCallback) callbacks[0];
        String id = pwcb.getIdentifer();
        int usage = pwcb.getUsage();

        if (usage == WSPasswordCallback.USERNAME_TOKEN) {
            // Logic to get the password to build the username token
            if ("admin".equals(id)) {
                pwcb.setPassword("admin");
            }
        } else if (usage == WSPasswordCallback.SIGNATURE || usage == WSPasswordCallback.DECRYPT) {
            // Logic to get the private key password for signture or decryption
            if ("bob".equals(id)) {
                pwcb.setPassword("password");
            } else if ("alice".equals(id)){
                pwcb.setPassword("password");
            }
        }
    }
}
