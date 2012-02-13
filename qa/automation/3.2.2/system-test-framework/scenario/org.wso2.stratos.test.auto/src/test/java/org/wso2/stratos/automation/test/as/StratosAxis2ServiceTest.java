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

package org.wso2.stratos.automation.test.as;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.client.AxisServiceClientUtils;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StratosAxis2ServiceTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(StratosAxis2ServiceTest.class);

    @Override
    public void init() {
        testClassName = StratosAxis2ServiceTest.class.getName();
    }

    @Override
    public void runSuccessCase() {
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("4"));
        String payload = createPayLoad().toString();
        List<String> expectedOutput = new ArrayList<String>();
        expectedOutput.add(">123<");
        String eprAxis2Service = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/services/t/" + tenantDetails.getTenantDomain() + "/Axis2Service";
        String operation = ("echoInt");
        AxisServiceClientUtils.waitForServiceDeployment(eprAxis2Service);
        AxisServiceClientUtils.sendRequest(eprAxis2Service, operation, payload, 1, expectedOutput, true);
    }

    @Override
    public void cleanup() {
    }

    private static OMElement createPayLoad() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://service.carbon.wso2.org", "ns1");
        OMElement method = fac.createOMElement("echoInt", omNs);
        OMElement value = fac.createOMElement("x", omNs);
        value.addChild(fac.createOMText(value, "123"));
        method.addChild(value);
        return method;
    }
}
