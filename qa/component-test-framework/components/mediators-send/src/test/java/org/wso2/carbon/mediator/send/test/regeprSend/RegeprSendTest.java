package org.wso2.carbon.mediator.send.test.regeprSend;

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

public class RegeprSendTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(RegeprSendTest.class);

    @Override
    public void init() {
        log.info("Initializing Send Mediator RegeprSend Tests");
        log.debug("Send mediator Tests Initialised");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running Send mediator SuccessCase ");

        StockQuoteClient stockQuoteClient = new StockQuoteClient();
        OMElement result = null;

        try {
            ConfigServiceAdminStub configServiceAdminStub = new
                    ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);

            String xmlPath = frameworkPath + File.separator + "components" + File.separator + "mediators-send"
                             + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "regeprSend" + File.separator + "synapse.xml";

            System.out.println(xmlPath);
            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(omElement);


            if (FrameworkSettings.STRATOS.equalsIgnoreCase("false")) {
                result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT, null, "IBM");
            } else if (FrameworkSettings.STRATOS.equalsIgnoreCase("true")) {
                result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT + "/services/" + FrameworkSettings.TENANT_NAME + "/", null, "IBM");
            }
            log.info(result);
            System.out.println(result);
            //  <ns:getQuoteResponse xmlns:ns="http://services.samples"><ns:return xmlns:ax25="http://services.samples/xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="ax25:GetQuoteResponse"><ax25:change>-2.688948938377238</ax25:change><ax25:earnings>-9.38150745007307</ax25:earnings><ax25:high>-92.87684233792116</ax25:high><ax25:last>93.53343717752745</ax25:last><ax25:lastTradeTimestamp>Mon Sep 27 10:48:40 IST 2010</ax25:lastTradeTimestamp><ax25:low>-93.442184099563</ax25:low><ax25:marketCap>3746487.897558052</ax25:marketCap><ax25:name>IBM Company</ax25:name><ax25:open>96.97815063098261</ax25:open><ax25:peRatio>23.00142771610827</ax25:peRatio><ax25:percentageChange>3.1056988945273125</ax25:percentageChange><ax25:prevClose>-86.58112166364718</ax25:prevClose><ax25:symbol>IBM</ax25:symbol><ax25:volume>16236</ax25:volume></ns:return></ns:getQuoteResponse>

            assert result != null;
            if (!result.toString().contains("IBM")) {
                Assert.fail("Send Mediator not invoked");
                log.error("Send Mediator not invoked");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Send Mediator doesn't work : " + e.getMessage());

        }

    }


    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
        ConfigServiceAdminStub configServiceAdminStub = new
                ConfigServiceAdminStubCommand().initConfigServiceAdminStub(sessionCookie);
        new ConfigServiceAdminStubCommand(configServiceAdminStub).loadDefaultConfig();

    }
}
