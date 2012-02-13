package org.wso2.startos.system.test.stratosUtils.asUtils;

import junit.framework.Assert;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rampart.RampartMessageData;
import org.apache.rampart.policy.model.CryptoConfig;
import org.apache.rampart.policy.model.RampartConfig;
import org.apache.ws.security.WSPasswordCallback;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
//import org.opensaml.saml2.core.Conditions;

public class asSecurityUtils implements CallbackHandler {

    public static OMElement runSecurityClient(int scenarioNo, String endpoint,
                                              String soapAction, String payload, String keyStroreName, String KeystorePasswprd) throws Exception {
        final Log log = LogFactory.getLog(asSecurityUtils.class);
        OMElement result;
        String endpoint_https = endpoint;

        int security_scenario_no = scenarioNo;
        String key_path = FrameworkSettings.getKeyStoreLocation();
        String security_policy_path = ProductConstant.getSecurityScenarios() + File.separator + "policyFiles" + File.separator + "scenario" + security_scenario_no + "-policy.xml";
        System.setProperty("javax.net.ssl.trustStore", key_path);
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem("/home/krishantha/svn/system-test-framework/core/org.wso2.carbon.system.test.core/src/test/resources/moduleClient/client/", null);
        ServiceClient sc = new ServiceClient(ctx, null);
        sc.engageModule("addressing");
        sc.engageModule("rampart");
        Options opts = new Options();
        opts.setTo(new EndpointReference(endpoint_https));
        opts.setAction(soapAction);
        result = null;
        try {
            opts.setProperty(RampartMessageData.KEY_RAMPART_POLICY, loadPolicy(security_policy_path, key_path));
            sc.setOptions(opts);
            result = sc.sendReceive(org.apache.axiom.om.impl.llom.util.AXIOMUtil.stringToOM(payload));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Loading policy failed:" + e.getMessage());
            Assert.fail("Loading policy failed" + e.getMessage());
        }
        return result;

    }


    private static Policy loadPolicy(String xmlPath, String clientKey) throws Exception {

        StAXOMBuilder builder = new StAXOMBuilder(xmlPath);
        org.apache.neethi.Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());
        RampartConfig rc = new RampartConfig();
        rc.setUser("admin123");
        rc.setUserCertAlias("wso2carbon");
        rc.setEncryptionUser("wso2carbon");
        rc.setPwCbClass(asSecurityUtils.class.getName());
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

            if ("admin123".equals(id)) {
                pwcb.setPassword("admin123");
            }

        } else if (usage == WSPasswordCallback.SIGNATURE || usage == WSPasswordCallback.DECRYPT) {

            if ("wso2carbon".equals(id)) {
                pwcb.setPassword("wso2carbon");
            }
        }
    }
}

