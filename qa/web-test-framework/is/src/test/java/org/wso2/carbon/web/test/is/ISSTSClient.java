package org.wso2.carbon.web.test.is;

import java.util.Properties;
import java.io.File;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rahas.RahasConstants;
import org.apache.rahas.Token;
import org.apache.rahas.TokenStorage;
import org.apache.rahas.TrustUtil;
import org.apache.rahas.client.STSClient;
import org.apache.rampart.RampartMessageData;
import org.apache.rampart.policy.model.RampartConfig;
import org.apache.rampart.policy.model.CryptoConfig;
import org.apache.ws.secpolicy.Constants;
import org.opensaml.XML;

/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class ISSTSClient {



	final static String RELYING_PARTY_SERVICE_EPR = "http://10.100.1.120:8280/services/echo";
	final static String ESB_TRANS_EPR = "http://10.100.1.120:8280/services/test1";
	final static String STS_EPR = "https://wso2carbon:9443/services/wso2carbon-sts";


	public static OMElement STSClient() throws Exception{
		ConfigurationContext confContext = null;
		Policy stsPolicy = null;
		STSClient stsClient = null;
		Policy servicePolicy = null;
		Token responseToken = null;
		String trustStore = null;

		// You need to import the Identity Server, public certificate to this key store.
		// By default it's there - if you use wso2carbon.jks from [ESB_HOME]\resources\security
		trustStore = "." + File.separator + "src" +File.separator + "lib"+ File.separator + "is_jks" + File.separator + "wso2carbon.jks";


		// We are accessing STS over HTTPS - so need to set trustStore parameters.
		System.setProperty("javax.net.ssl.trustStore", trustStore);
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

		// Create configuration context - you will have Rampart module engaged in the
		// client.axis2.xml
		confContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem("." + File.separator + "src" +File.separator + "lib"+ File.separator+"repo","." + File.separator + "src" +File.separator + "lib"+ File.separator+"repo"+ File.separator+"conf"+ File.separator+"client.axis2.xml");

		stsClient = new STSClient(confContext);

		stsClient.setRstTemplate(getRSTTemplate());
		stsClient.setAction(RahasConstants.WST_NS_05_02 + RahasConstants.RST_ACTION_SCT);

		// This is the security policy we applied to Identity Server STS.
		// You can see it by https://[IDENTITY_SERVER]/services/wso2carbon-sts?wsdl
		stsPolicy = loadSTSPolicy("." + File.separator + "src" +File.separator + "lib"+ File.separator+"sts.policy.xml");

		// This is the security of the relying party web service.
		// This policy will accept a security token issued from Identity Server STS
		servicePolicy = loadServicePolicy("." + File.separator + "src" +File.separator + "lib"+ File.separator+"service.policy.xml");

		responseToken = stsClient.requestSecurityToken(servicePolicy, STS_EPR, stsPolicy,RELYING_PARTY_SERVICE_EPR);

		System.out.println(responseToken.getToken());

		TokenStorage store = TrustUtil.getTokenStore(confContext);
		store.add(responseToken);


		ServiceClient client = new ServiceClient(confContext, null);
		Options options = new Options();
		options.setAction("urn:echoString");
		options.setTo(new EndpointReference(RELYING_PARTY_SERVICE_EPR));
		options.setProperty(org.apache.axis2.Constants.Configuration.TRANSPORT_URL, ESB_TRANS_EPR);
		options.setProperty(RampartMessageData.KEY_RAMPART_POLICY, servicePolicy);
		options.setProperty(RampartMessageData.KEY_CUSTOM_ISSUED_TOKEN, responseToken.getId());
		options.setTimeOutInMilliSeconds(60000);
		client.setOptions(options);

		client.engageModule("addressing");
		client.engageModule("rampart");

		OMElement response = client.sendReceive(getPayload("Hello world1"));
		return response;
	}

	private static Policy loadSTSPolicy(String xmlPath) throws Exception {
		StAXOMBuilder builder = null;
		Policy policy = null;
		RampartConfig rc = null;

		builder = new StAXOMBuilder(xmlPath);
		policy = PolicyEngine.getPolicy(builder.getDocumentElement());
		rc = new RampartConfig();
		// User from the LDAP user store
		rc.setUser("ashadi");
		// You need to have password call-back class to provide the user password
		rc.setPwCbClass(PWCBHandler.class.getName());
		policy.addAssertion(rc);
		return policy;
	}

	private static Policy loadServicePolicy(String xmlPath) throws Exception {
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
		rc.setUser("wso2carbon");
		rc.setEncryptionUser("wso2carbon");
		// You need to have password call-back class to provide the user password
		rc.setPwCbClass(PWCBHandler.class.getName());

		keystore = "." + File.separator + "src" +File.separator + "lib"+ File.separator + "ebs_jks" + File.separator + "wso2carbon.jks";


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
		OMElement element = null;
		OMElement elem = fac.createOMElement(Constants.RST_TEMPLATE);
		//TrustUtil.createTokenTypeElement(RahasConstants.VERSION_05_02, elem).setText(XML.SAML_NS);
		TrustUtil.createTokenTypeElement(RahasConstants.VERSION_05_02, elem).setText(RahasConstants.TOK_TYPE_SAML_10);
		TrustUtil.createKeyTypeElement(RahasConstants.VERSION_05_02, elem,
				RahasConstants.KEY_TYPE_SYMM_KEY);
		TrustUtil.createKeySizeElement(RahasConstants.VERSION_05_02, elem, 256);
		element = TrustUtil.createClaims(RahasConstants.VERSION_05_02, elem, "http://wso2.org");
		addClaimType(element, "http://wso2.org/claims/givenname");
		return elem;
	}

	private static void addClaimType(OMElement parent, String uri) {
		OMElement element = null;
		element = parent.getOMFactory().createOMElement(
				new QName("http://schemas.xmlsoap.org/ws/2005/05/identity", "ClaimType", "wsid"),
				parent);
		element.addAttribute(parent.getOMFactory().createOMAttribute("Uri", null, uri));
	}

	private static OMElement getPayload(String value) {
		OMFactory factory = null;
		OMNamespace ns = null;
		OMElement elem = null;
		OMElement childElem = null;

		factory = OMAbstractFactory.getOMFactory();
		ns = factory.createOMNamespace("http://echo.services.core.carbon.wso2.org", "ns1");
		elem = factory.createOMElement("echoString", ns);
		childElem = factory.createOMElement("in", null);
		childElem.setText(value);
		elem.addChild(childElem);
		return elem;
	}
}