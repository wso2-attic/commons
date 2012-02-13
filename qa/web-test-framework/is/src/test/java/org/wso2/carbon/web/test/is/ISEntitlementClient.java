package org.wso2.carbon.web.test.is;
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

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rampart.RampartMessageData;

import java.io.File;

public class ISEntitlementClient {

	public static OMElement EntitlementClient() throws Exception {
		ServiceClient client = null;
		Options options = null;
		OMElement response = null;
		ConfigurationContext context = null;
		String trustStore = null;


		trustStore = "." + File.separator + "src" +File.separator + "lib"+ File.separator + "ebs_jks" + File.separator + "wso2carbon.jks";


		System.setProperty("javax.net.ssl.trustStore", trustStore);
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

		String addUrl = null;
		String tranUrl = null;

//		if (args.length > 1) {
//			addUrl = args[0];
//			tranUrl = args[1];
//		} else {
			addUrl = "http://10.100.1.68:8280/services/echo";
			tranUrl = "https://10.100.1.68:8243/services/test";
//		}

		StAXOMBuilder builder = new StAXOMBuilder("." + File.separator + "src" +File.separator + "lib"+ File.separator+"policy.xml");
		Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());

		context = ConfigurationContextFactory.createConfigurationContextFromFileSystem("." + File.separator + "src" +File.separator + "lib"+ File.separator+"repo","." + File.separator + "src" +File.separator + "lib"+ File.separator+"repo"+ File.separator+"conf"+ File.separator+"client.axis2.xml");
		client = new ServiceClient(context, null);
		options = new Options();
		options.setAction("urn:echoString");
		// This is the addressing URL
		options.setTo(new EndpointReference(addUrl));
		// To the ESB, the proxy service
		options.setUserName("admin");
		options.setPassword("admin");
		options.setProperty(Constants.Configuration.TRANSPORT_URL, tranUrl);
		options.setProperty(RampartMessageData.KEY_RAMPART_POLICY, policy);
		client.setOptions(options);
		client.engageModule("addressing");
		client.engageModule("rampart");

		response = client.sendReceive(getPayload("Hello world"));

        return response;
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