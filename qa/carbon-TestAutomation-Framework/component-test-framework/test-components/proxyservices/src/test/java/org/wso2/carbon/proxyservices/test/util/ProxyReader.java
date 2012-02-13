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
import org.apache.axiom.om.impl.builder.StAXOMBuilder;


import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;

import org.wso2.carbon.proxyadmin.ui.client.ProxyServiceAdminClient;
import org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.BACKENDSERVER_HOST_NAME;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.BACKENDSERVER_HTTP_PORT;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.STRATOS;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.TENANT_NAME;

public class ProxyReader {

    private static final Log log = LogFactory.getLog(ProxyReader.class);

    public OMElement createOMElement(String fileName, String fileLocation) throws Exception {

        //if file location =null it taking from the test data directory
        OMElement documentElement = null;
        FileInputStream inputStream;
        try {
            if (fileLocation == null) {

                File filePath = new File("./");
                String relativePath = filePath.getCanonicalPath();
                File findFile = new File(relativePath + File.separator + "config" + File.separator + "framework.properties");
                if (!findFile.isFile()) {
                    filePath = new File("./../");
                    relativePath = filePath.getCanonicalPath();
                }


                inputStream = new FileInputStream(relativePath + File.separator + "proxyservices" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "testdata" + File.separator + fileName);
            }
            else {
                inputStream = new FileInputStream(fileLocation);

            }
            XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
            //create the builder
            StAXOMBuilder builder = new StAXOMBuilder(parser);
            //get the root element (in this case the envelope)
            documentElement = builder.getDocumentElement();


        }
        catch (Exception e) {
            log.fatal(e.toString());
        }
        return documentElement;

    }

    public ProxyData getProxy(String fileName, String fileLocation) throws Exception {


        FrameworkSettings.getProperty();
        ProxyReader handler = new ProxyReader();
        ProxyData proxyData = null;
        try {
            OMElement proxyElement = handler.createOMElement(fileName, fileLocation);

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
