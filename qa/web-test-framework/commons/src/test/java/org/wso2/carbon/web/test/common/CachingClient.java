/*
 *  Copyright (c) 2005-2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.web.test.common;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CachingClient {

     FileInputStream freader;
    public String cachClient(String epr, String operationName, String SoapAction, String NameSpace) throws org.apache.axis2.AxisFault {
        Properties properties = new Properties();

        try {
               freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
               properties.load(freader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String globalData = "No Value";
        String carbon_home = properties.getProperty("carbon.home");
        try {

            ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(carbon_home + File.separator + "repository" + File.separator + "deployment" + File.separator + "client", null);
            ServiceClient sc = new ServiceClient(ctx, null);
            Options opts = new Options();
            opts.setTo(new EndpointReference(epr));
            opts.setAction(SoapAction);
            sc.engageModule("addressing");
            sc.setOptions(opts);

            OMElement result = sc.sendReceive(CreatePayload(operationName, NameSpace));
            System.out.println(result.getFirstElement().getText());
            globalData = result.getFirstElement().getText();
            freader.close();

        }
        catch (Exception e) {
            System.out.println(e);
        }

        return globalData;
    }

    public OMElement CreatePayload(String operationName, String Namespace) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(Namespace, "b");
        OMElement OP1 = fac.createOMElement(operationName, omns);

        return OP1;
    }
}
