package org.wso2.carbon.proxyservices.test;

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
import java.util.Iterator;

public class WSDLBasedTest extends TestTemplate {

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
        OMElement result = null;
        log.debug("Running AddProxy SuccessCase ");
        try {

            ProxyServiceAdminStub proxyServiceAdminStub = new InitializeProxyAdminCommand().executeAdminStub(sessionCookie);
            ServiceAdminStub serviceAdminStub = new InitializeServiceAdminCommand().executeAdminStub(sessionCookie);
            /*ServiceGroupMetaDataWrapper serviceGroupMetaDataWrapper = new ServiceAdminCommand(serviceAdminStub).listServiceGroupsSuccessCase("proxy","",0);

            for (int i = 0; i < serviceGroupMetaDataWrapper.getNumberOfCorrectServiceGroups(); i++) {
                String serviceName = serviceGroupMetaDataWrapper.getMetadataList()[i].getServiceGroupName();
                if (serviceName.equalsIgnoreCase("WSDLProxy")) {
                    new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("WSDLProxy");
                    log.info("StockQuoteProxyTest deleted");
                }
            }*/
            new ServiceAdminCommand(serviceAdminStub).deleteNonAdminServiceGroupSuccessCase();
            proxyData = handler.getProxy("wsdlproxy.xml", null);
            //Add proxy Service test
            new ProxyAdminCommand(proxyServiceAdminStub).addProxySuccessCase(proxyData);
            log.info("Proxy service added");

            StockQuoteClient stockQuoteClient = new StockQuoteClient();
            if (FrameworkSettings.STRATOS.equalsIgnoreCase("false")) {
                result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT + "/services/WSDLProxy", null, "IBM");
            }
            else if (FrameworkSettings.STRATOS.equalsIgnoreCase("true")) {
                System.out.println("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT + "/services/" + FrameworkSettings.TENANT_NAME + "/WSDLProxy");
                result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT + "/services/" + FrameworkSettings.TENANT_NAME + "/WSDLProxy", null, "IBM");
            } 
            boolean isFound = result.getChildren().next().toString().contains("IBM");
            log.info(result.toString());
            if (!isFound) {
                log.error("Required response not found");
                Assert.fail("Required response not found");
            }
            Iterator iterator = result.getFirstElement().getChildrenWithName(new QName("http://services.samples/xsd", "name"));
            while (iterator.hasNext()) {
                OMElement element = (OMElement) iterator.next();
                System.out.println("The response is received : " + element.getText());
                assertEquals("IBM Company", element.getText());
            }

            //delete proxy service test
            new ProxyAdminCommand(proxyServiceAdminStub).deleteProxySuccessCase("WSDLProxy");

        }
        catch (Exception e) {
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
