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

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.io.FileInputStream;
import java.io.File;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;
import org.apache.axis2.client.ServiceClient;

import java.util.Properties;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.util.Base64;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rahas.RahasConstants;
import org.apache.rahas.Token;
import org.apache.rahas.TokenStorage;
import org.apache.rahas.TrustUtil;
import org.apache.rahas.client.STSClient;
import org.apache.rampart.RampartMessageData;
import org.apache.rampart.policy.model.CryptoConfig;
import org.apache.rampart.policy.model.RampartConfig;
import org.apache.ws.secpolicy.Constants;
import org.opensaml.XML;

import java.io.IOException;


public class STSTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;
    static String SERVICE_EPR;
    static String STS_EPR;
    String hostname;
    String httpport;
    String contextroot;

    private static String wso2wsasHome;


    public STSTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
        hostname = property.getProperty("host.name");
        httpport = property.getProperty("http.be.port");
        contextroot = property.getProperty("context.root");
    }


    public void testSetupSecurityScenarioForSTS() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        SERVICE_EPR = "http://" + hostname + ":" + httpport + contextroot + "/services" + "/HelloService";
        STS_EPR = "http://" + hostname + ":" + httpport + contextroot  + "/services" + "/wso2carbon-sts";


        InstSeleniumTestBase.loginToUI(username, password);

        instServiceManagement.accessServiceDashboard("wso2carbon-sts");

        browser.click("link=Security");
        browser.waitForPageToLoad("30000");

        if ((browser.getSelectedValue("securityConfigAction")).equals("Yes")) {
            browser.select("securityConfigAction", "label=No");
            assertTrue(browser.isTextPresent("This will disable security from the service. Click OK to confirm"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Security disabled successfully."));
            browser.click("//button[@type='button']");
        }
        browser.select("securityConfigAction", "label=Yes");
        browser.click("//input[@name='scenarioId' and @value='scenario5']");
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@name='trustStore' and @value='wso2carbon.jks']");
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");

    }

    public void testSetServiceEndpoint() throws Exception {
        browser.click("link=Configure STS");
        browser.waitForPageToLoad("30000");
        browser.type("endpointaddrs", SERVICE_EPR);
        browser.select("alias", "label=wso2carbon");
        browser.click("//input[@value='Apply']");
        browser.waitForPageToLoad("30000");
    }

    public void testSetupSecurityScenarioForHelloService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.Login();
        instServiceManagement.accessServiceDashboard("HelloService");
        browser.click("link=Security");
        browser.waitForPageToLoad("30000");

        if ((browser.getSelectedValue("securityConfigAction")).equals("Yes")) {
            browser.select("securityConfigAction", "label=No");
            assertTrue(browser.isTextPresent("This will disable security from the service. Click OK to confirm"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Security disabled successfully."));
            browser.click("//button[@type='button']");
        }

        browser.select("securityConfigAction", "label=Yes");
        browser.click("//input[@name='scenarioId' and @value='scenario11']");
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@name='trustStore' and @value='wso2carbon.jks']");
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");
    }


    public void testImportCert() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        File certPath = new File("." + File.separator + "lib" + File.separator + "client.cert");

        instServiceManagement.Login();
        browser.click("link=Key Stores");
        browser.waitForPageToLoad("30000");
        int i = 1;
        String key = "wso2carbon.jks";
        boolean elementFound = false;

        while (!elementFound && browser.isElementPresent("//table[@id='keymgtTable']/tbody/tr[" + i + "]/td[1]")) {
            System.out.println(browser.getText("//table[@id='keymgtTable']/tbody/tr[" + i + "]/td[1]"));
            if (key.equals(browser.getText("//table[@id='keymgtTable']/tbody/tr[" + i + "]/td[1]"))) {
                elementFound = true;
                browser.click("//table[@id='keymgtTable']/tbody/tr[" + i + "]/td[3]/a[1]");
                browser.waitForPageToLoad("30000");
                instSeleniumTestBase.SetFileBrowse("browseField", certPath.getCanonicalPath());
                //browser.type("browseField", "/home/jayani/Desktop/web-test-framework/wsas/lib/client.cert");
                browser.click("//input[@value='Import']");
                browser.waitForPageToLoad("30000");
                browser.click("//button[@type='button']");

                int j = 1;
                String cert = "client.cert";
                elementFound = false;
                while (!elementFound && browser.isElementPresent("//div[@id='workArea']/table/tbody/tr[" + j + "]/td")) {

                    if (cert.equals(browser.getText("//div[@id='workArea']/table/tbody/tr[" + j + "]/td"))) {
                        elementFound = true;
                    }
                    j++;
                }

                if (!elementFound) {
                    assertTrue(cert + " not found!", false);
                }

            }
            i++;
        }


    }

    public void testInvokeService() throws Exception {
        wso2wsasHome = property.getProperty("carbon.home");
        ServiceClient client = null;
        Options options = null;
        ConfigurationContext ctx = null;
        Policy stsPolicy = null;
        STSClient stsClient = null;
        Policy servicePolicy = null;
        Token responseToken = null;
        TokenStorage store = null;
        String serviceEpr = null;
        String stsEpr = null;


        stsEpr = STS_EPR;
        serviceEpr = SERVICE_EPR;

        System.out.println(wso2wsasHome);


        ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(property.getProperty("carbon.home") + File.separator + "repository"+ File.separator + "deployment" + File.separator + "client", null);

        stsClient = new STSClient(ctx);

        stsClient.setRstTemplate(getRSTTemplate());
        stsClient.setAction(RahasConstants.WST_NS_05_02 + RahasConstants.RST_ACTION_SCT);

        stsPolicy = loadPolicy(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "sts-test" + File.separator + "sts.policy.xml");

        servicePolicy = loadPolicy(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "sts-test" + File.separator + "service.policy.xml");

        responseToken = stsClient.requestSecurityToken(servicePolicy, stsEpr, stsPolicy, serviceEpr);

        System.out.println("RECEIVED SECRET: " + Base64.encode(responseToken.getSecret()) + "\n");
        System.out.println("RECEIVED TOKEN: " + responseToken.getToken() + "\n");

        // Store token
        store = TrustUtil.getTokenStore(ctx);
        store.add(responseToken);

        client = new ServiceClient(ctx, null);
        client.engageModule("rampart");
        client.engageModule("addressing");
        options = new Options();
        options.setAction("urn:greet");
        options.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        options.setProperty(RampartMessageData.KEY_RAMPART_POLICY, servicePolicy);
        options.setProperty(RampartMessageData.SCT_ID, responseToken.getId());
        options.setTo(new EndpointReference(serviceEpr));
        client.setOptions(options);

        System.out.println(client.sendReceive(getPayload("Hello")));
        OMElement result = client.sendReceive(getPayload("Hello"));
        System.out.println(result.getFirstElement().getText());
        assertEquals("Hello World, Hello !!!", result.getFirstElement().getText());
    }

    public void testremoveSecurity() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        instServiceManagement.accessServiceDashboard("wso2carbon-sts");
        browser.click("link=Security");
        browser.waitForPageToLoad("30000");

        if ((browser.getSelectedValue("securityConfigAction")).equals("Yes")) {
            browser.select("securityConfigAction", "label=No");
            assertTrue(browser.isTextPresent("This will disable security from the service. Click OK to confirm"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Security disabled successfully."));
            browser.click("//button[@type='button']");
        }

        instServiceManagement.accessServiceDashboard("wso2carbon-sts");
        browser.click("link=Security");
        browser.waitForPageToLoad("30000");

        if ((browser.getSelectedValue("securityConfigAction")).equals("Yes")) {
            browser.select("securityConfigAction", "label=No");
            assertTrue(browser.isTextPresent("This will disable security from the service. Click OK to confirm"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Security disabled successfully."));
            browser.click("//button[@type='button']");
        }

        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }


    private static OMElement getPayload(String value) {
        OMFactory factory = null;
        OMNamespace ns = null;
        OMElement elem = null;
        OMElement childElem = null;

        factory = OMAbstractFactory.getOMFactory();
        ns = factory.createOMNamespace("http://www.wso2.org/types", "ns1");
        elem = factory.createOMElement("greet", ns);
        childElem = factory.createOMElement("name", null);
        childElem.setText(value);
        elem.addChild(childElem);

        return elem;
    }

    private static Policy loadPolicy(String xmlPath) throws Exception {

        StAXOMBuilder builder = null;
        Policy policy = null;
        RampartConfig rc = null;
        CryptoConfig sigCryptoConfig = null;
        String keystore = null;
        Properties merlinProp = null;
        CryptoConfig encrCryptoConfig = null;

        builder = new StAXOMBuilder(xmlPath);
        policy = PolicyEngine.getPolicy(builder.getDocumentElement());

        rc = new RampartConfig();

        rc.setUser("admin");
        rc.setUserCertAlias("wso2carbon");
        rc.setEncryptionUser("wso2carbon");
        rc.setPwCbClass(PWCBHandler.class.getName());

        keystore = wso2wsasHome + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks";
        merlinProp = new Properties();
        merlinProp.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
        merlinProp.put("org.apache.ws.security.crypto.merlin.file", keystore);
        merlinProp.put("org.apache.ws.security.crypto.merlin.keystore.password", "wso2carbon");

        sigCryptoConfig = new CryptoConfig();
        sigCryptoConfig.setProvider("org.apache.ws.security.components.crypto.Merlin");
        sigCryptoConfig.setProp(merlinProp);

        encrCryptoConfig = new CryptoConfig();
        encrCryptoConfig.setProvider("org.apache.ws.security.components.crypto.Merlin");
        encrCryptoConfig.setProp(merlinProp);

        rc.setSigCryptoConfig(sigCryptoConfig);
        rc.setEncrCryptoConfig(encrCryptoConfig);

        policy.addAssertion(rc);

        return policy;
    }

    private static OMElement getRSTTemplate() throws Exception {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMElement elem = fac.createOMElement(Constants.RST_TEMPLATE);
        TrustUtil.createTokenTypeElement(RahasConstants.VERSION_05_02, elem).setText(XML.SAML_NS);
        TrustUtil.createKeyTypeElement(RahasConstants.VERSION_05_02, elem,
                RahasConstants.KEY_TYPE_SYMM_KEY);
        TrustUtil.createKeySizeElement(RahasConstants.VERSION_05_02, elem, 256);
        return elem;
    }

//   
}
