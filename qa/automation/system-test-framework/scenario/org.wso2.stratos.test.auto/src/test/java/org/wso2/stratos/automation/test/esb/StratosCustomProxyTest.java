/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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


package org.wso2.stratos.automation.test.esb;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClientUtils;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;

import java.util.ArrayList;
import java.util.List;

public class StratosCustomProxyTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(StratosCustomProxyTest.class);
    
    @Override
    public void init() {
        testClassName = StratosCustomProxyTest.class.getName();
    }

    public void runSuccessCase() {
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("1"));
        String payload = createPayLoad().toString();
        List<String> expectedOutput = new ArrayList<String>();
        expectedOutput.add(">123<");

        String eprDemoProxy = "http://" + FrameworkSettings.ESB_SERVER_HOST_NAME + ":" + FrameworkSettings.ESB_NHTTP_PORT
                + "/services/t/" + tenantDetails.getTenantDomain() + "/DemoProxy2";
        String eprAxis2service = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/services/t/" + tenantDetails.getTenantDomain() + "/Axis2Service";
        System.out.println(eprDemoProxy);
        System.out.println(eprAxis2service);
        String operation = ("echoInt");
        AxisServiceClientUtils.waitForServiceDeployment(eprAxis2service);
        AxisServiceClientUtils.waitForServiceDeployment(eprDemoProxy);
        AxisServiceClientUtils.sendRequest(eprDemoProxy, operation, payload, 1, expectedOutput, true);
    }

    @Override
    public void cleanup() {
    }

    private static OMElement createPayLoad() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://service.carbon.wso2.org", "p");
        OMElement method = fac.createOMElement("echoInt", omNs);
        OMElement value = fac.createOMElement("x", omNs);
        value.addChild(fac.createOMText(value, "123"));
        method.addChild(value);
        return method;
    }
}
