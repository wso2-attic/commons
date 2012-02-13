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


package org.wso2.carbon.mediator.send.test;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.proxyadmin.ui.ProxyServiceAdminStub;
import org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData;
import org.wso2.carbon.proxyservices.test.commands.InitializeProxyAdminCommand;
import org.wso2.carbon.proxyservices.test.commands.ProxyAdminCommand;
import org.wso2.carbon.proxyservices.test.util.ProxyReader;
import org.wso2.carbon.proxyservices.test.util.StockQuoteClient;
import org.wso2.carbon.service.mgt.test.commands.InitializeServiceAdminCommand;
import org.wso2.carbon.service.mgt.test.commands.ServiceAdminCommand;
import org.wso2.carbon.service.mgt.ui.ServiceAdminStub;
import org.wso2.carbon.service.mgt.ui.types.carbon.ServiceGroupMetaDataWrapper;

import javax.xml.namespace.QName;
import java.io.File;
import java.util.Iterator;

public class SendMediatorTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(SendMediatorTest.class);

    @Override
    public void init() {
        log.info("Initializing SendMediatorTest class ");
        log.debug("SendMediatorTest Initialised");
    }


    @Override
    public void runSuccessCase() {
        ProxyData proxyData;
        ProxyReader handler = new ProxyReader();
        log.debug("Running AddProxy SuccessCase ");
        OMElement result = null;


        try {

            String xmlPath = frameworkPath + File.separator + "mediators-send" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "SendMediatorProxy.xml";

            ProxyServiceAdminStub proxyServiceAdminStub = new InitializeProxyAdminCommand().executeAdminStub(sessionCookie);
            ServiceAdminStub serviceAdminStub = new InitializeServiceAdminCommand().executeAdminStub(sessionCookie);
            ServiceGroupMetaDataWrapper serviceGroupMetaDataWrapper = new ServiceAdminCommand(serviceAdminStub).listServiceGroupsSuccessCase("proxy","",0);

            for (int i = 0; i < serviceGroupMetaDataWrapper.getNumberOfCorrectServiceGroups(); i++) {
                String serviceName = serviceGroupMetaDataWrapper.getMetadataList()[i].getServiceGroupName();
                if (serviceName.equalsIgnoreCase("sendMediatorService")) {
                    new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("sendMediatorService");
                    log.info("sendMediatorService deleted");
                }
            }
            proxyData = handler.getProxy("SendMediatorProxy.xml", xmlPath);
            proxyData.setName("sendMediatorService");

            //Add proxy Service test
            new ProxyAdminCommand(proxyServiceAdminStub).addProxySuccessCase(proxyData);
            log.info("Proxy service added");

            StockQuoteClient stockQuoteClient = new StockQuoteClient();
            //OMElement result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT + "/services/sendMediatorService", null, "IBM");

            if(FrameworkSettings.STRATOS.equalsIgnoreCase("false")){
              result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT + "/services/sendMediatorService", null, "IBM");
            }
            else if(FrameworkSettings.STRATOS.equalsIgnoreCase("true")){
              result = stockQuoteClient.stockQuoteClientforProxy("http://"+FrameworkSettings.HOST_NAME+":"+FrameworkSettings.HTTP_PORT+"/services/" + FrameworkSettings.TENANT_NAME + "/services/sendMediatorService", null, "IBM");
            }

            boolean isFound = result.getChildren().next().toString().contains("IBM");
            log.info("Result : " + result);
            if (!isFound) {
                log.error("Required response not found.Send mediator test case failed");
                Assert.fail("Required response not found.Send mediator test case failed");
            }
            Iterator iterator = result.getFirstElement().getChildrenWithName(new QName("http://services.samples/xsd", "name"));
            while (iterator.hasNext()) {
                OMElement element = (OMElement) iterator.next();
                System.out.println("The response is received : " + element.getText());
                assertEquals("IBM Company", element.getText());
            }

            //delete proxy service test
            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("StockQuoteProxyTest");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("AddProxy success case failed : " + e.getMessage());
        }
    }

    @Override
    public void runFailureCase() {
        ProxyData proxyData;
        ProxyReader handler = new ProxyReader();
        log.debug("Running Proxy FailureCase ");
        try {
            ProxyServiceAdminStub proxyServiceAdminStub = new InitializeProxyAdminCommand().executeAdminStub(sessionCookie);
            proxyData = handler.getProxy("StockQuoteProxyTest.xml", null);

            //checking add proxy method
            new ProxyAdminCommand(proxyServiceAdminStub).addProxyFailureCase(proxyData);
            //checking delete proxy method
            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxyFailureCase("StockQuoteProxyTest");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("AddProxy failure case failed : " + e.getMessage());
        }

    }

    @Override
    public void cleanup() {
     loadDefaultConfig();
    }

}