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

package org.wso2.carbon.system.test;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.client.AxisServiceClientUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ScenarioTest1 extends TestTemplate {

    private static final Log log = LogFactory.getLog(ScenarioTest1.class);

    @Override
    public void init() {
        testClassName = ScenarioTest1.class.getName();
    }

    @Override
    public void runSuccessCase() {

        String payload = createPayLoad().toString();
        List<String> expectedOutput = new ArrayList<String>();
        expectedOutput.add(">3<");

        String eprDemoProxy = "http://" + FrameworkSettings.ESB_SERVER_HOST_NAME + ":" + FrameworkSettings.ESB_NHTTP_PORT
                + File.separator + "services/" + "DemoProxy2";
        String eprCalculator = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + ":" + FrameworkSettings.APP_SERVER_HTTP_PORT
                + File.separator + "services/" + "Calculator";
        System.out.println(eprDemoProxy);
        System.out.println(eprCalculator);
        String operation = ("add");
        AxisServiceClientUtils.waitForServiceDeployment(eprCalculator);
        AxisServiceClientUtils.waitForServiceDeployment(eprDemoProxy);
        AxisServiceClientUtils.sendRequest(eprDemoProxy, operation, payload, 1, expectedOutput, true);
    }

    @Override
    public void cleanup() {
    }

    public static OMElement createPayLoad() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://test.com", "ns1");
        OMElement method = fac.createOMElement("add", omNs);
        OMElement value1 = fac.createOMElement("a", omNs);
        OMElement value2 = fac.createOMElement("b", omNs);
        value1.addChild(fac.createOMText(value1, "1"));
        value2.addChild(fac.createOMText(value1, "2"));
        method.addChild(value1);
        method.addChild(value2);
        return method;
    }


}
