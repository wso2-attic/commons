package org.wso2.carbon.mediator.fault.test;

import junit.framework.Assert;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.common.test.utils.ConfigHelper;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.common.test.utils.client.StockQuoteClient;
import org.wso2.carbon.mediation.configadmin.test.commands.ConfigServiceAdminStubCommand;
import org.wso2.carbon.mediation.configadmin.ui.ConfigServiceAdminStub;

import java.io.File;

public class Soap12FCodeEncodeTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(Soap12FCodeEncodeTest.class);

    @Override
    public void init() {
        log.info("Initializing Fault Mediator SOAP12 Tests");
        log.debug("Fault Mediator SOAP12 Tests Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running CFault Mediator SOAP12 SuccessCase ");
        OMElement result = null;

        StockQuoteClient stockQuoteClient = new StockQuoteClient();

        try {
            ConfigServiceAdminStub configServiceAdminStub = new
                    ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);

            String xmlPath = frameworkPath + File.separator + "mediators-fault"
                    + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "soap12_fcode_encode.xml";

            System.out.println(xmlPath);
            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(omElement);


            if(FrameworkSettings.STRATOS.equalsIgnoreCase("false")){
              result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT, null, "IBM");
            }
            else if(FrameworkSettings.STRATOS.equalsIgnoreCase("true")){
              result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT + "/services/" + FrameworkSettings.TENANT_NAME + "/", null, "IBM");
            }
            log.info(result);
            System.out.println(result);

            assert result != null;
            if (result.toString().contains("IBM")) {
                Assert.fail("Fault Mediator SOAP12 not invoked");
                log.error("Fault Mediator SOAP12 not invoked");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Fault Mediator SOAP12 doesn't work : " + e.getMessage());

        }

    }


    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
        loadDefaultConfig();
    }
}
