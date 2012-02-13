package org.wso2.startos.system.test.stratosUtils.cepUtils;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.ServiceClient;

import javax.xml.stream.XMLStreamException;

public class cepRequest {
    public void send() throws AxisFault, XMLStreamException {
        ServiceClient serviceClient = null;
        try {
            serviceClient = new ServiceClient();
            serviceClient.setTargetEPR(new EndpointReference("http://cep.stratoslive.wso2.com/services/a/manualQA0001.org/localBrokerService/ConditionSatisfyingStockQuotes"));
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
        if (serviceClient != null) {
            String xmlElement1 = "<quotedata:AllStockQuoteStream xmlns:quotedata=\"http://ws.cdyne.com/\">\n" +
                    "                    <quotedata:StockQuoteEvent>\n" +
                    "              <quotedata:StockSymbol>MSFT</quotedata:StockSymbol>\n" +
                    "              <quotedata:LastTradeAmount>99.55</quotedata:LastTradeAmount>\n" +
                    "              <quotedata:StockChange>0.05</quotedata:StockChange>\n" +
                    "              <quotedata:OpenAmount>25.05</quotedata:OpenAmount>\n" +
                    "              <quotedata:DayHigh>25.46</quotedata:DayHigh>\n" +
                    "              <quotedata:DayLow>25.01</quotedata:DayLow>\n" +
                    "              <quotedata:StockVolume>20452658</quotedata:StockVolume>\n" +
                    "              <quotedata:PrevCls>25.31</quotedata:PrevCls>\n" +
                    "              <quotedata:ChangePercent>0.20</quotedata:ChangePercent>\n" +
                    "              <quotedata:FiftyTwoWeekRange>22.73 - 31.58</quotedata:FiftyTwoWeekRange>\n" +
                    "              <quotedata:EarnPerShare>2.326</quotedata:EarnPerShare>\n" +
                    "              <quotedata:PE>10.88</quotedata:PE>\n" +
                    "              <quotedata:CompanyName>Microsoft Corpora</quotedata:CompanyName>\n" +
                    "              <quotedata:QuoteError>false</quotedata:QuoteError>\n" +
                    "                    </quotedata:StockQuoteEvent>\n" +
                    "                </quotedata:AllStockQuoteStream>";

            String xmlElement2 = "<quotedata:AllStockQuoteStream xmlns:quotedata=\"http://ws.cdyne.com/\">\n" +
                    "                    <quotedata:StockQuoteEvent>\n" +
                    "              <quotedata:StockSymbol>MSFT</quotedata:StockSymbol>\n" +
                    "              <quotedata:LastTradeAmount>101.36</quotedata:LastTradeAmount>\n" +
                    "              <quotedata:StockChange>0.05</quotedata:StockChange>\n" +
                    "              <quotedata:OpenAmount>25.05</quotedata:OpenAmount>\n" +
                    "              <quotedata:DayHigh>25.46</quotedata:DayHigh>\n" +
                    "              <quotedata:DayLow>25.01</quotedata:DayLow>\n" +
                    "              <quotedata:StockVolume>20452658</quotedata:StockVolume>\n" +
                    "              <quotedata:PrevCls>25.31</quotedata:PrevCls>\n" +
                    "              <quotedata:ChangePercent>0.20</quotedata:ChangePercent>\n" +
                    "              <quotedata:FiftyTwoWeekRange>22.73 - 31.58</quotedata:FiftyTwoWeekRange>\n" +
                    "              <quotedata:EarnPerShare>2.326</quotedata:EarnPerShare>\n" +
                    "              <quotedata:PE>10.88</quotedata:PE>\n" +
                    "              <quotedata:CompanyName>Microsoft Corpora</quotedata:CompanyName>\n" +
                    "              <quotedata:QuoteError>false</quotedata:QuoteError>\n" +
                    "                    </quotedata:StockQuoteEvent>\n" +
                    "                </quotedata:AllStockQuoteStream>";

            String xmlElement3 = "<quotedata:AllStockQuoteStream xmlns:quotedata=\"http://ws.cdyne.com/\">\n" +
                    "                    <quotedata:StockQuoteEvent>\n" +
                    "              <quotedata:StockSymbol>MSFT</quotedata:StockSymbol>\n" +
                    "              <quotedata:LastTradeAmount>99.98</quotedata:LastTradeAmount>\n" +
                    "              <quotedata:StockChange>0.05</quotedata:StockChange>\n" +
                    "              <quotedata:OpenAmount>25.05</quotedata:OpenAmount>\n" +
                    "              <quotedata:DayHigh>25.46</quotedata:DayHigh>\n" +
                    "              <quotedata:DayLow>25.01</quotedata:DayLow>\n" +
                    "              <quotedata:StockVolume>20452658</quotedata:StockVolume>\n" +
                    "              <quotedata:PrevCls>25.31</quotedata:PrevCls>\n" +
                    "              <quotedata:ChangePercent>0.20</quotedata:ChangePercent>\n" +
                    "              <quotedata:FiftyTwoWeekRange>22.73 - 31.58</quotedata:FiftyTwoWeekRange>\n" +
                    "              <quotedata:EarnPerShare>2.326</quotedata:EarnPerShare>\n" +
                    "              <quotedata:PE>10.88</quotedata:PE>\n" +
                    "              <quotedata:CompanyName>Microsoft Corpora</quotedata:CompanyName>\n" +
                    "              <quotedata:QuoteError>false</quotedata:QuoteError>\n" +
                    "                    </quotedata:StockQuoteEvent>\n" +
                    "                </quotedata:AllStockQuoteStream>";


            OMElement omElement1 = null;
            OMElement omElement2 = null;
            OMElement omElement3 = null;
            try {
                omElement1 = AXIOMUtil.stringToOM(xmlElement1);
                omElement2 = AXIOMUtil.stringToOM(xmlElement2);
                omElement3 = AXIOMUtil.stringToOM(xmlElement3);
                serviceClient.fireAndForget(omElement1);
                serviceClient.fireAndForget(omElement2);
                serviceClient.fireAndForget(omElement3);
            } catch (XMLStreamException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (AxisFault axisFault) {
                axisFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }
}

