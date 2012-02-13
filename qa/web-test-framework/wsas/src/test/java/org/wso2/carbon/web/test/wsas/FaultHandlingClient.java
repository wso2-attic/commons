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

package org.wso2.carbon.web.test.wsas;

import org.apache.ws.security.WSPasswordCallback;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.rampart.RampartMessageData;
import org.apache.rampart.policy.model.RampartConfig;
import org.apache.rampart.policy.model.CryptoConfig;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.wso2.carbon.web.test.common.SecurityClient;


import javax.xml.namespace.QName;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.util.Properties;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class FaultHandlingClient extends TestCase implements CallbackHandler {


    public static Properties loadProperties() throws IOException {
        FileInputStream freader;

        Properties properties = new Properties();
        freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        freader.close();
        return properties;
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
            if ("qaclient".equals(id)) {
                pwcb.setPassword("qaclient");
            }
        }
    }

    public OMElement twoWayPayloadwithTwoInputs(String NameSpace, String operation, String param1,String param2, String input1,String input2) throws Exception {

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

      public OMElement twoWayAnonClientwithTwoInputs(String serviceName, String SoapAction, String NameSpace, String operation, String param1,String param2, String input1,String input2) throws Exception {

          ServiceClient sc = new ServiceClient();
          Options opts = new Options();
          opts.setTo(new EndpointReference("http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + serviceName));
          opts.setAction(SoapAction);
          sc.setOptions(opts);
          OMElement result = sc.sendReceive(twoWayPayloadwithTwoInputs(NameSpace, operation, param1,param2, input1,input2));
          System.out.println(result.getFirstElement().getText());
          return result;
      }

    public OMElement twoWayClientWithHandledExceptions(String serviceName, String SoapAction, String NameSpace, String operation, String param1,String param2, String input1,String input2) throws Exception {

        ServiceClient sc = new ServiceClient();
        Options opts = new Options();
        OMElement result=null;
        opts.setTo(new EndpointReference("http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.be.port") + loadProperties().getProperty("context.root") + "/services/" + serviceName));
        opts.setAction(SoapAction);
        sc.setOptions(opts);
        try{
            result = sc.sendReceive(twoWayPayloadwithTwoInputs(NameSpace, operation, param1,param2, input1,input2));
            System.out.println(result.getFirstElement().getText());
        }catch(AxisFault e){
            String msg=e.getMessage();
            System.out.println(e);
            if((msg.equals("Account does not exist!")&& !(input1.equals("88"))) || msg.equals("Insufficient funds")&& 400<Integer.parseInt(input2) ){

                System.out.println("service invoked successfully!!");
            }else{
                System.out.println("service failed!!!");
                assertTrue("service failed!!!",false);
            }
        }
        return result;
    }

     public OMElement runSecurityClient(String scenarioid, String serviceName, String Namespace, String SoapAction, String operation, String param1,String param2, String input1,String input2) throws Exception {

        Properties properties = new Properties();
        OMElement result = null;
        FileInputStream freader=new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);

        String carbon_home = properties.getProperty("carbon.home");
        String host_name = properties.getProperty("host.name");
        String http_port = properties.getProperty("http.be.port");
        String https_port = properties.getProperty("https.be.port");
        String context_root = properties.getProperty("context.root");
        System.setProperty("javax.net.ssl.trustStore", carbon_home + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        freader.close();

        /*        to desable InfaultFlow security by code
        ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem( carbon_home + File.separator + "repository", null);
         Iterator itr = ctx.getAxisConfiguration().getInFaultFlowPhases().iterator();
         int j=0,i = 0;
         while(itr.hasNext()){
           String s =  itr.next().toString();
             System.out.println(s);

          if(s.equalsIgnoreCase("security")){
             break;
          }
             i++;
         }

          ctx.getAxisConfiguration().getInFaultFlowPhases().remove(i);  */

        //if disabling security by axis2.xml
        //ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(carbon_home + File.separator + "repository", ".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "client-repository" + File.separator + "conf" + File.separator + "axis2.xml");
         ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(carbon_home + File.separator + "repository", null);

        ServiceClient sc = new ServiceClient(ctx, null);
        sc.engageModule("rampart");
        sc.engageModule("addressing");


        Options opts = new Options();
        opts.setTo(new EndpointReference("http://" + host_name + ":" + http_port + context_root + "/services/" + serviceName));


        opts.setAction(SoapAction);

        try {
            opts.setProperty(RampartMessageData.KEY_RAMPART_POLICY, loadPolicy(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "policy" + File.separator + scenarioid + "-policy.xml"));

        } catch (Exception e) {
            e.printStackTrace();
        }
         try {
             sc.setOptions(opts);

             result = sc.sendReceive(twoWayPayloadwithTwoInputs(Namespace, operation, param1,param2, input1,input2));
             System.out.println(result.getFirstElement().getText());
             String msg=result.getFirstElement().getText();
                     
          }catch(AxisFault e){
            String msg=e.getMessage();
            System.out.println(e);
            if((msg.equals("Account does not exist!")&& !(input1.equals("88"))) || msg.equals("Insufficient funds")&& 400<Integer.parseInt(input2) ){

                System.out.println("service invoked successfully!!");
            }else{
                System.out.println("service failed!!!");
                assertTrue("service failed!!!",false);
            }
         }

        return result;
    }

    


    public Policy loadPolicy(String xmlPath) throws Exception {
        StAXOMBuilder builder = new StAXOMBuilder(xmlPath);
        Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());

        RampartConfig rc = new RampartConfig();

        rc.setUser("admin");
        rc.setUserCertAlias("qaclient");
        rc.setEncryptionUser("qaserver");
        rc.setPwCbClass(SecurityClient.class.getName());

        CryptoConfig sigCryptoConfig = new CryptoConfig();

        sigCryptoConfig.setProvider("org.apache.ws.security.components.crypto.Merlin");

        File file = new File(".." + File.separator + "commons" + File.separator + "lib" + File.separator + "qaclient.jks");

        if (!file.exists()) {
            throw new Exception("Key store cannot be found");
        }

        String filepath = file.getCanonicalPath();

        Properties prop1 = new Properties();
        prop1.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
        prop1.put("org.apache.ws.security.crypto.merlin.file", filepath);
        prop1.put("org.apache.ws.security.crypto.merlin.keystore.password", "qaclient");
        sigCryptoConfig.setProp(prop1);

        CryptoConfig encrCryptoConfig = new CryptoConfig();
        encrCryptoConfig.setProvider("org.apache.ws.security.components.crypto.Merlin");

        Properties prop2 = new Properties();

        prop2.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
        prop2.put("org.apache.ws.security.crypto.merlin.file", filepath);
        prop2.put("org.apache.ws.security.crypto.merlin.keystore.password", "qaclient");
        encrCryptoConfig.setProp(prop2);

        rc.setSigCryptoConfig(sigCryptoConfig);
        rc.setEncrCryptoConfig(encrCryptoConfig);

        policy.addAssertion(rc);
        return policy;
    }



}
