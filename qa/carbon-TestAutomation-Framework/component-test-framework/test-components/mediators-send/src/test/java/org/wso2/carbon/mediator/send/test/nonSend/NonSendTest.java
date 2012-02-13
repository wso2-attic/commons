package org.wso2.carbon.mediator.send.test.nonSend;

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
// Test synapse xml wrong


public class NonSendTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(NonSendTest.class);

    @Override
    public void init() {
        log.info("Initializing Send Mediator NonSendTest Tests");
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

            String xmlPath = frameworkPath + File.separator + "mediators-send"
                    + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "nonSend" + File.separator + "synapse.xml";

            System.out.println(xmlPath);
            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            new ConfigServiceAdminStubCommand(configServiceAdminStub).updateConfigurationExecuteSuccessCase(omElement);


            if(FrameworkSettings.STRATOS.equalsIgnoreCase("false")){
              result = stockQuoteClient.stockQuoteClientforProxy("http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT, null, "IBM");
            }
            else if(FrameworkSettings.STRATOS.equalsIgnoreCase("true")){
              result = stockQuoteClient.stockQuoteClientforProxy("http://"+FrameworkSettings.HOST_NAME+":"+FrameworkSettings.HTTP_PORT+"/services/" + FrameworkSettings.TENANT_NAME + "/", null, "IBM");
            }
            log.info(result);
            System.out.println(result);
//            <ns:getQuoteResponse xmlns:ns="http://services.samples"><ns:return xmlns:ax25="http://services.samples/xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="ax25:GetQuoteResponse"><ax25:change>3.8707744966448914</ax25:change><ax25:earnings>-9.110413456146665</ax25:earnings><ax25:high>-78.58241199349489</ax25:high><ax25:last>79.71137482286048</ax25:last><ax25:lastTradeTimestamp>Sun Oct 03 22:25:53 IST 2010</ax25:lastTradeTimestamp><ax25:low>82.81973526900181</ax25:low><ax25:marketCap>5.787670793968044E7</ax25:marketCap><ax25:name>IBM Company</ax25:name><ax25:open>81.75499495994777</ax25:open><ax25:peRatio>-19.63375809610714</ax25:peRatio><ax25:percentageChange>4.380048244102419</ax25:percentageChange><ax25:prevClose>88.37287356039407</ax25:prevClose><ax25:symbol>IBM</ax25:symbol><ax25:volume>8120</ax25:volume></ns:return></ns:getQuoteResponse>

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
        loadDefaultConfig();
    }
}
