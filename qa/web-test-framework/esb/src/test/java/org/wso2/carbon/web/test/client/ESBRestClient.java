package org.wso2.carbon.web.test.client;

import java.rmi.RemoteException;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

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

public class ESBRestClient{

    public boolean stockQuoteClient(String epr) throws AxisFault {
        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null,null);
		OMElement payload = createPayLoad();
        boolean output = false;

        ServiceClient serviceclient = new ServiceClient(cc, null);

		Options opts = new Options();
        opts.setTo(new EndpointReference(epr));
		opts.setAction("urn:getQuote");
        opts.setProperty(Constants.Configuration.ENABLE_REST, Constants.VALUE_TRUE);

		serviceclient.setOptions(opts);

		try {
			OMElement res = serviceclient.sendReceive(payload);
            System.out.println(res.getChildren().next());
            output = res.getChildren().next().toString().contains("IBM Company");

        } catch (RemoteException e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        return output;
    }

	public static OMElement createPayLoad(){
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace("http://services.samples", "ns");
		OMElement method = fac.createOMElement("getQuote", omNs);
		OMElement value1 = fac.createOMElement("request", omNs);
        OMElement value2 = fac.createOMElement("symbol", omNs);

        value2.addChild(fac.createOMText(value1, "IBM"));
        value1.addChild(value2);
        method.addChild(value1);


        return method;

//        <ns:getQuote xmlns:ns="http://services.samples/xsd">
//            <ns:request>
//              <ns:symbol>IBM</ns:symbol>
//            </ns:request>
//        </ns:getQuote>
    }
}
