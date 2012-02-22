package org.test;

import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rampart.RampartMessageData;
import org.apache.rampart.policy.model.CryptoConfig;
import org.apache.rampart.policy.model.RampartConfig;
import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.Properties;

public class SecureClient implements CallbackHandler {

    public static void main(String[] args) throws Exception {


    ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("/home/charitha/products/wsas/client-repo/repository/", null);
                    SecureServiceStub stub = new SecureServiceStub(cc, "http://appserver.stratoslive.wso2.com/services/t/superqa.com/SecureService") ;

                    ServiceClient sc = stub._getServiceClient();

                    sc.engageModule("rampart");
                    sc.engageModule("addressing");

              Options opts = sc.getOptions();
              //opts.setAction("");

              opts.setProperty(RampartMessageData.KEY_RAMPART_POLICY, loadPolicy("scenario2-policy.xml"));
               sc.setOptions(opts);

              System.out.println(stub.add(3,4));

                 }


            public static Policy loadPolicy(String xmlPath) throws Exception {
                StAXOMBuilder builder = new StAXOMBuilder(xmlPath);
                Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());
               RampartConfig rc = new RampartConfig();

                      rc.setUserCertAlias("client");
                      rc.setEncryptionUser("service");
                      rc.setPwCbClass(SecureClient.class.getName());

                      CryptoConfig sigCryptoConfig = new CryptoConfig();

                      sigCryptoConfig.setProvider("org.apache.ws.security.components.crypto.Merlin");

                      Properties prop1 = new Properties();
                      prop1.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
                      prop1.put("org.apache.ws.security.crypto.merlin.file", "client.jks");
                      prop1.put("org.apache.ws.security.crypto.merlin.keystore.password", "testing");
                      sigCryptoConfig.setProp(prop1);

                      CryptoConfig encrCryptoConfig = new CryptoConfig();
                      encrCryptoConfig.setProvider("org.apache.ws.security.components.crypto.Merlin");

                      Properties prop2 = new Properties();

                      prop2.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
                      prop2.put("org.apache.ws.security.crypto.merlin.file", "client.jks");
                      prop2.put("org.apache.ws.security.crypto.merlin.keystore.password", "testing");
                      encrCryptoConfig.setProp(prop2);

                      rc.setSigCryptoConfig(sigCryptoConfig);
                      rc.setEncrCryptoConfig(encrCryptoConfig);


                      policy.addAssertion(rc);
                      return policy;



           }



                 public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

                     WSPasswordCallback pwcb = (WSPasswordCallback) callbacks[0];
                     String id = pwcb.getIdentifer();
                     if ("client".equals(id)) {
                         pwcb.setPassword("testing");
                     }
                 }

}
