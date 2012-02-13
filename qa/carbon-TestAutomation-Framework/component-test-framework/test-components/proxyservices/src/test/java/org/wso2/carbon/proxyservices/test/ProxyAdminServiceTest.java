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


package org.wso2.carbon.proxyservices.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.proxyadmin.ui.ProxyServiceAdminStub;
import org.wso2.carbon.proxyadmin.ui.types.carbon.MetaData;
import org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData;
import org.wso2.carbon.proxyservices.test.commands.InitializeProxyAdminCommand;
import org.wso2.carbon.proxyservices.test.commands.ProxyAdminCommand;
import org.wso2.carbon.proxyservices.test.util.ProxyReader;
import org.wso2.carbon.service.mgt.ui.ServiceAdminStub;
import org.wso2.carbon.service.mgt.test.commands.*;
import org.wso2.carbon.service.mgt.ui.types.carbon.ServiceGroupMetaDataWrapper;


public class ProxyAdminServiceTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(ProxyAdminServiceTest.class);

    @Override
    public void init() {
        log.info("Initializing ProxyAdminServiceTest ");
        log.debug("Test Initialised");
    }

    @Override
    public void runSuccessCase() {
        ProxyData proxyData;
        ProxyReader handler = new ProxyReader();
        log.debug("Running AddProxy SuccessCase ");
        try {

            ProxyServiceAdminStub proxyServiceAdminStub = new InitializeProxyAdminCommand().executeAdminStub(sessionCookie);
            ServiceAdminStub serviceAdminStub = new InitializeServiceAdminCommand().executeAdminStub(sessionCookie);
            //   EndpointAdminStub endpointAdminStub = new EndpointCommand(endpointAdminStub).InitializeEndpointAdminStubExecuteSuccessCase(sessionCookie);

            /*ServiceGroupMetaDataWrapper serviceGroupMetaDataWrapper = new ServiceAdminCommand(serviceAdminStub).listServiceGroupsSuccessCase("proxy","",0);

            for (int i = 0; i < serviceGroupMetaDataWrapper.getNumberOfCorrectServiceGroups(); i++) {
                String serviceName = serviceGroupMetaDataWrapper.getMetadataList()[i].getServiceGroupName();
                if (serviceName.equalsIgnoreCase("StockQuoteProxyTest")) {
                    new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("StockQuoteProxyTest");
                    log.info("StockQuoteProxyTest deleted");
                }
            }*/
            new ServiceAdminCommand(serviceAdminStub).deleteNonAdminServiceGroupSuccessCase();
            proxyData = handler.getProxy("StockQuoteProxyTest.xml", null);

            //Add proxy Service test
            new ProxyAdminCommand(proxyServiceAdminStub).addProxySuccessCase(proxyData);
            log.info("Proxy service added");

            //Get proxy data test
            proxyData = new ProxyAdminCommand(proxyServiceAdminStub).getProxySuccessCase("StockQuoteProxyTest");
            if (!proxyData.getName().equalsIgnoreCase("StockQuoteProxyTest")) {
                log.error("Proxy data not matched");
                Assert.fail("Proxy data not matched");
            }

            //get Transport test
            String[] transport = new ProxyAdminCommand(proxyServiceAdminStub).getTransportSuccessCase();
            for (String aTransport : transport) {
                if ((!aTransport.equalsIgnoreCase("http")) && (!aTransport.equalsIgnoreCase("https"))) {
                    log.error("Get transport not working");
                    junit.framework.Assert.fail("Get transport not working");
                }
            }

            //get Sequences test
            String[] sequences = new ProxyAdminCommand(proxyServiceAdminStub).getSequencesSuccessCase();
            for (String sequence : sequences) {
                if ((!sequence.equalsIgnoreCase("fault")) && (!sequence.equalsIgnoreCase("main"))) {
                    log.error("Get sequences not working");
                    Assert.fail("Get sequences not working");
                }
            }

            //get Metadata test
            MetaData metaData = new ProxyAdminCommand(proxyServiceAdminStub).getMetaDataSuccessCase();


            /*//Get available endpoint test
            // Getting config
            String endpointXML = ConfigHelper.getXMLConfig(File.separator + "config"+File.separator+"TestSuites"+File.separator+"Endpoints"+File.separator+"ESBEndpointAddAndRemoveTest"+File.separator+"endpoint.xml");
            String endpointName = ConfigHelper.readXML("name", endpointXML);
            // Adding config
            if (new EndpointCommand(endpointAdminStub).getEndpointsNamesExecuteSuccessCase() != null) {
                String [] endpointNames = new EndpointCommand(endpointAdminStub).getEndpointsNamesExecuteSuccessCase();
                for (String aEndpoint : endpointNames) {
                if (aEndpoint.equalsIgnoreCase(endpointName)){
                    new EndpointCommand(endpointAdminStub).deleteEndpointExecuteSuccessCase(endpointName);
                }

            }
             new EndpointCommand(endpointAdminStub).addEndpointExecuteSuccessCase(endpointXML);
             String [] endpoint = new ProxyAdminCommand(proxyServiceAdminStub).getAvailableEndpointSuccessCase();
                for (String anEndpoint : endpoint) {
                if (anEndpoint.isEmpty()) {
                    log.error("Get Endpoint not work");
                    Assert.fail("Get Endpoint not work");
                }
            }*/


            //Enable stat test
            new ProxyAdminCommand(proxyServiceAdminStub).enableStatSuccessCase("StockQuoteProxyTest");
            log.info("Stat enabled");

            //Disable stat test
            new ProxyAdminCommand(proxyServiceAdminStub).disableStatSuccessCase("StockQuoteProxyTest");
            log.info("Stat disabled");

            //Enable tracing test
            new ProxyAdminCommand(proxyServiceAdminStub).enableTracingSuccessCase("StockQuoteProxyTest");
            log.info("tracing enabled");

            //Disable tracing test
            new ProxyAdminCommand(proxyServiceAdminStub).disableTracingSuccessCase("StockQuoteProxyTest");
            log.info("tracing disabled");

            //Redeploy Service test
            new ProxyAdminCommand(proxyServiceAdminStub).redeployProxySuccessCase("StockQuoteProxyTest");
            log.info("Service Redeployed");

            //Stop proxy service test
            new ProxyAdminCommand(proxyServiceAdminStub).stopProxySuccessCase("StockQuoteProxyTest");
            log.info("Proxy Service Stopped");

            //Start proxy service test
            new ProxyAdminCommand(proxyServiceAdminStub).startProxySuccessCase("StockQuoteProxyTest");
            log.info("Proxy Service Stopped");

            //delete proxy service test
            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("StockQuoteProxyTest");

        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
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

            //Get proxy test
            new ProxyAdminCommand(proxyServiceAdminStub).getProxyFailureCase("StockQuoteProxyTest");

            //Get transport test
            new ProxyAdminCommand(proxyServiceAdminStub).getTransportFailureCase();

            //Get sequences test
            new ProxyAdminCommand(proxyServiceAdminStub).getSequencesFailureCase();

            //Get Metadata test
            new ProxyAdminCommand(proxyServiceAdminStub).getMetaDataFailureCase();

            //Enable stat test
            new ProxyAdminCommand(proxyServiceAdminStub).enableStatFailureCase("StockQuoteProxyTest");

            //Disable stat test
            new ProxyAdminCommand(proxyServiceAdminStub).disableStatFailureCase("StockQuoteProxyTest");

            //Enable tracing test
            new ProxyAdminCommand(proxyServiceAdminStub).enableTracingFailureCase("StockQuoteProxyTest");

            //Disable tracing test
            new ProxyAdminCommand(proxyServiceAdminStub).disableTracingFailureCase("StockQuoteProxyTest");

            //Redeploy service test
            new ProxyAdminCommand(proxyServiceAdminStub).redeployProxyFailureCase("StockQuoteProxyTest");

            //Stop proxy service test
            new ProxyAdminCommand(proxyServiceAdminStub).stopProxyFailureCase("StockQuoteProxyTest");

            //Start proxy service test
            new ProxyAdminCommand(proxyServiceAdminStub).startProxyFailureCase("StockQuoteProxyTest");

            //checking delete proxy method
            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxyFailureCase("StockQuoteProxyTest");

        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("AddProxy failure case failed : " + e.getMessage());
        }

    }

    @Override
    public void cleanup() {
     loadDefaultConfig();
    }

}
