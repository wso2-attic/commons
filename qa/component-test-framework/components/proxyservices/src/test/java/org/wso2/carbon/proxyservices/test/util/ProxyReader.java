/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/
package org.wso2.carbon.proxyservices.test.util;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;

import org.wso2.carbon.proxyadmin.ui.client.ProxyServiceAdminClient;
import org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData;
import org.wso2.carbon.common.test.utils.ConfigHelper;
import java.util.Locale;

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.BACKENDSERVER_HOST_NAME;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.BACKENDSERVER_HTTP_PORT;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.STRATOS;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.TENANT_NAME;

public class ProxyReader {

    private static final Log log = LogFactory.getLog(ProxyReader.class);

    public ProxyData getProxy(String filePath) throws Exception {


        FrameworkSettings.getProperty();
        ProxyData proxyData = null;
        try {
            OMElement proxyElement = ConfigHelper.createOMElement(filePath);


            String fileBuffer = proxyElement.toString();
            if (STRATOS.equals("true")) {
                fileBuffer = fileBuffer.replaceAll("localhost:9000/services", BACKENDSERVER_HOST_NAME + "/services/" + TENANT_NAME); //http://appserver.cloud.private.wso2.com/services/t/testautomation.com
                //fileBuffer = fileBuffer.replaceAll(":9000","/services/"+TENANT_NAME);                  //http://localhost:9000/services/SimpleStockQuoteService
            }
            else {
                fileBuffer = fileBuffer.replaceAll("localhost", BACKENDSERVER_HOST_NAME);
                fileBuffer = fileBuffer.replaceAll("9000", BACKENDSERVER_HTTP_PORT);
            }
            proxyElement = AXIOMUtil.stringToOM(fileBuffer);
            ProxyServiceAdminClient proxyServiceAdminClient = new ProxyServiceAdminClient(null, "", null, Locale.US);
            proxyData = proxyServiceAdminClient.getDesignView(proxyElement.toString());

        }
        catch (Exception e) {
            log.error("ProxyReader class exception : " + e.toString());
        }
        return proxyData;
    }

}
