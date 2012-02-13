package org.wso2.carbon.mediator.clazz.test;

/*testcase for class mediator test without class peroperty*/

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

public class WithPropertyTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(WithPropertyTest.class);

    @Override
    public void init() {
        log.info("Initializing class Mediator Test class ");
        log.debug("Class Mediators Test Initialized");
    }


    @Override
    public void runSuccessCase() {
        log.debug("Running SuccessCase ");
        StockQuoteClient stockQuoteClient = new StockQuoteClient();

        try {

            ConfigServiceAdminStub configServiceAdminStub = new
                    ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);

            String xmlPath = frameworkPath + File.separator + "mediators-class"
                    + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "WithPropertySource.xml";
            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(omElement);


            OMElement result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT, null, "IBM");

            System.out.println(result);

            log.info(result);
            if (!result.toString().contains("MSFT")) {

                Assert.fail("Class mediator doesn't work");
                log.error("Class mediator doesn't work");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("class mediator doesn't work : " + e.getMessage());

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
