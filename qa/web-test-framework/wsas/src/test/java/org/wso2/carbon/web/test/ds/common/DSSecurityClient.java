package org.wso2.carbon.web.test.ds.common;

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
import org.apache.rampart.RampartMessageData;
import org.apache.rampart.policy.model.RampartConfig;
import org.apache.rampart.policy.model.CryptoConfig;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.callback.CallbackHandler;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Dec 10, 2009
 * Time: 2:06:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class DSSecurityClient implements CallbackHandler {

        public OMElement runSecurityClient(String scenarioid, String serviceName, String Namespace, String SoapAction, String operation, String param,String user) throws Exception {

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
        ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(carbon_home + File.separator + "repository", null);
        ServiceClient sc = new ServiceClient(ctx, null);
        sc.engageModule("rampart");
        sc.engageModule("addressing");

        Options opts = new Options();
        if (context_root.equals(null)) {
            if (scenarioid.equals("scenario1")) {
                opts.setTo(new EndpointReference("https://" + host_name + ":" + https_port + "/services/" + serviceName));
            } else {
                opts.setTo(new EndpointReference("http://" + host_name + ":" + http_port + "/services/" + serviceName));
            }
        } else {
            if (scenarioid.equals("scenario1")) {
                opts.setTo(new EndpointReference("https://" + host_name + ":" + https_port + context_root + "/services/" + serviceName));
            } else {
                opts.setTo(new EndpointReference("http://" + host_name + ":" + http_port + context_root + "/services/" + serviceName));
            }
        }
        opts.setAction(SoapAction);
//        if(serviceName.equals("JaxWSTestService.JaxWSServicePort")){
//        opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//        }
        try {
            opts.setProperty(RampartMessageData.KEY_RAMPART_POLICY, loadPolicy(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "policy" + File.separator + scenarioid + "-policy.xml",user));

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sc.setOptions(opts);
            if (param.equals(null)) {
                result = sc.sendReceive(CreateRequestReplynoInputsPayload(Namespace, operation));
                System.out.println(result.getFirstElement().getText());
                return result;
            } else {
                result = sc.sendReceive(CreateRequestReplyPayload(Namespace, operation, param));
                System.out.println(result.getFirstElement().getText());
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }



    public Policy loadPolicy(String xmlPath,String user) throws Exception {
        StAXOMBuilder builder = new StAXOMBuilder(xmlPath);
        Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());

        RampartConfig rc = new RampartConfig();

        rc.setUser(user);
        rc.setUserCertAlias("qaclient");
        rc.setEncryptionUser("qaserver");
        rc.setPwCbClass(DSSecurityClient.class.getName());

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

    public OMElement CreateRequestReplyPayload(String NameSpace, String operation, String param) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(NameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        OMElement value = fac.createOMElement(param, omns);
        value.addChild(fac.createOMText(value, "Invoking Security Scenario!!!!"));
        OP1.addChild(value);
        return OP1;
    }

    public OMElement CreateRequestReplynoInputsPayload(String NameSpace, String operation) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(NameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        return OP1;

    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        WSPasswordCallback pwcb = (WSPasswordCallback) callbacks[0];
        String id = pwcb.getIdentifer();
        int usage = pwcb.getUsage();

        if (usage == WSPasswordCallback.USERNAME_TOKEN) {
            // Logic to get the password to build the username token
            if ("user1".equals(id)) {
                pwcb.setPassword("userone");
            }
            if ("user2".equals(id)) {
                pwcb.setPassword("usertwo");
            }
        } else if (usage == WSPasswordCallback.SIGNATURE || usage == WSPasswordCallback.DECRYPT) {
            // Logic to get the private key password for signture or decryption
            if ("qaclient".equals(id)) {
                pwcb.setPassword("qaclient");
            }
        }
    }
}
