package org.wso2.carbon.proxyservices;

import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.axiom.om.OMElement;
import org.apache.rampart.policy.model.RampartConfig;
import org.apache.rampart.policy.model.CryptoConfig;
import org.apache.rampart.RampartMessageData;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.callback.CallbackHandler;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;

public class SecurityClient implements CallbackHandler {

    public static void main(String srgs[]) {

        SecurityClient securityCl = new SecurityClient();
        OMElement result = null;
        try {
            result = securityCl.runSecurityClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result.toString());

    }

    public OMElement runSecurityClient() throws Exception {

        Properties properties = new Properties();
        FileInputStream freader = new FileInputStream("/chamara/project/esb/compoments-test-framework/proxyservices/src/test/resources/client.properties");
        properties.load(freader);
        String clientRepo = "/chamara/project/esb/wso2esb-3.0.0/samples/axis2Client/client_repo";
        String endpointHttpS = "https://localhost:8243/services/echo";
        String endpointHttp = "http://localhost:8280/services/echo";
        int securityScenario = Integer.parseInt("15");
        String clientKey = "/chamara/project/esb/wso2esb-3.0.0/resources/security/wso2carbon.jks";
        String SoapAction = "urn:echoString";
        String body = "<p:echoString xmlns:p=\"http://echo.services.core.carbon.wso2.org\"> <in>Test</in> </p:echoString>";
        String trustStore = "/chamara/project/esb/wso2esb-3.0.0/resources/security/wso2carbon.jks";
        String securityPolicy = "/chamara/project/esb/compoments-test-framework/proxyservices/src/test/resources";

        OMElement result = null;


        System.setProperty("javax.net.ssl.trustStore", trustStore);
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

        ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(clientRepo, null);
        ServiceClient sc = new ServiceClient(ctx, null);
        sc.engageModule("rampart");
        sc.engageModule("addressing");

        Options opts = new Options();

        if (securityScenario == 1) {
            opts.setTo(new EndpointReference(endpointHttpS));
        } else {
            opts.setTo(new EndpointReference(endpointHttp));
        }

        opts.setAction(SoapAction);

        if (securityScenario != 0) {
            try {
                String securityPolicyPath = securityPolicy + File.separator + "scenario" + securityScenario + "-policy.xml";
                opts.setProperty(RampartMessageData.KEY_RAMPART_POLICY, loadPolicy(securityPolicyPath, clientKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sc.setOptions(opts);
        result = sc.sendReceive(AXIOMUtil.stringToOM(body));
        System.out.println(result.getFirstElement().getText());
        return result;
    }

    public Policy loadPolicy(String xmlPath, String clientKey) throws Exception {

        StAXOMBuilder builder = new StAXOMBuilder(xmlPath);
        Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());

        RampartConfig rc = new RampartConfig();

        rc.setUser("admin");
        rc.setUserCertAlias("wso2carbon");
        rc.setEncryptionUser("wso2carbon");
        rc.setPwCbClass(SecurityClient.class.getName());

        CryptoConfig sigCryptoConfig = new CryptoConfig();
        sigCryptoConfig.setProvider("org.apache.ws.security.components.crypto.Merlin");

        Properties prop1 = new Properties();
        prop1.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
        prop1.put("org.apache.ws.security.crypto.merlin.file", clientKey);
        prop1.put("org.apache.ws.security.crypto.merlin.keystore.password", "wso2carbon");
        sigCryptoConfig.setProp(prop1);

        CryptoConfig encrCryptoConfig = new CryptoConfig();
        encrCryptoConfig.setProvider("org.apache.ws.security.components.crypto.Merlin");

        Properties prop2 = new Properties();
        prop2.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
        prop2.put("org.apache.ws.security.crypto.merlin.file", clientKey);
        prop2.put("org.apache.ws.security.crypto.merlin.keystore.password", "wso2carbon");
        encrCryptoConfig.setProp(prop2);

        rc.setSigCryptoConfig(sigCryptoConfig);
        rc.setEncrCryptoConfig(encrCryptoConfig);

        policy.addAssertion(rc);
        return policy;
    }


    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        WSPasswordCallback pwcb = (WSPasswordCallback) callbacks[0];
        String id = pwcb.getIdentifer();
        int usage = pwcb.getUsage();

        if (usage == WSPasswordCallback.USERNAME_TOKEN) {

            if ("admin".equals(id)) {
                pwcb.setPassword("admin");
            }

        } else if (usage == WSPasswordCallback.SIGNATURE || usage == WSPasswordCallback.DECRYPT) {

            if ("wso2carbon".equals(id)) {
                pwcb.setPassword("wso2carbon");
            }
        }
    }
}
