package cep;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.util.StAXUtils;
import cep.JNDIContext;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;

public class AllStockQuotesPublisher {
    private static InitialContext initContext = null;
    private static TopicConnectionFactory topicConnectionFactory = null;
    
    public static void main(String[] args) throws XMLStreamException {
 
        String xmlElement1 = "<quotedata:AllStockQuoteStream xmlns:quotedata=\"http://ws.cdyne.com/\">\n" +
                " <quotedata:StockQuoteEvent>\n" +
                " <quotedata:StockSymbol>MSFT</quotedata:StockSymbol>\n" +
                " <quotedata:LastTradeAmount>126.36 </quotedata:LastTradeAmount>\n" +
                " <quotedata:StockChange>0.05</quotedata:StockChange>\n" +
                " <quotedata:OpenAmount>25.05</quotedata:OpenAmount>\n" +
                " <quotedata:DayHigh>25.46</quotedata:DayHigh>\n" +
                " <quotedata:DayLow>25.01</quotedata:DayLow>\n" +
                " <quotedata:StockVolume>20452658</quotedata:StockVolume>\n" +
                " <quotedata:PrevCls>25.31</quotedata:PrevCls>\n" +
                " <quotedata:ChangePercent>0.20</quotedata:ChangePercent>\n" +
                " <quotedata:FiftyTwoWeekRange>22.73 - 31.58</quotedata:FiftyTwoWeekRange>\n" +
                " <quotedata:EarnPerShare>2.326</quotedata:EarnPerShare>\n" +
                " <quotedata:PE>10.88</quotedata:PE>\n" +
                " <quotedata:CompanyName>Microsoft Corpora</quotedata:CompanyName>\n" +
                " <quotedata:QuoteError>false</quotedata:QuoteError>\n" +
                " </quotedata:StockQuoteEvent>\n" +
                " </quotedata:AllStockQuoteStream>";
        String xmlElement2 = "<quotedata:AllStockQuoteStream xmlns:quotedata=\"http://ws.cdyne.com/\">\n" +
                " <quotedata:StockQuoteEvent>\n" +
                " <quotedata:StockSymbol>MSFT</quotedata:StockSymbol>\n" +
                " <quotedata:LastTradeAmount>36.36</quotedata:LastTradeAmount>\n" +
                " <quotedata:StockChange>0.05</quotedata:StockChange>\n" +
                " <quotedata:OpenAmount>25.05</quotedata:OpenAmount>\n" +
                " <quotedata:DayHigh>25.46</quotedata:DayHigh>\n" +
                " <quotedata:DayLow>25.01</quotedata:DayLow>\n" +
                " <quotedata:StockVolume>20452658</quotedata:StockVolume>\n" +
                " <quotedata:PrevCls>25.31</quotedata:PrevCls>\n" +
                " <quotedata:ChangePercent>0.20</quotedata:ChangePercent>\n" +
                " <quotedata:FiftyTwoWeekRange>22.73 - 31.58</quotedata:FiftyTwoWeekRange>\n" +
                " <quotedata:EarnPerShare>2.326</quotedata:EarnPerShare>\n" +
                " <quotedata:PE>10.88</quotedata:PE>\n" +
                " <quotedata:CompanyName>Microsoft Corpora</quotedata:CompanyName>\n" +
                " <quotedata:QuoteError>false</quotedata:QuoteError>\n" +
                " </quotedata:StockQuoteEvent>\n" +
                " </quotedata:AllStockQuoteStream>";
        String xmlElement3 = "<quotedata:AllStockQuoteStream xmlns:quotedata=\"http://ws.cdyne.com/\">\n" +
                " <quotedata:StockQuoteEvent>\n" +
                " <quotedata:StockSymbol>MSFT</quotedata:StockSymbol>\n" +
                " <quotedata:LastTradeAmount>6.36</quotedata:LastTradeAmount>\n" +
                " <quotedata:StockChange>0.05</quotedata:StockChange>\n" +
                " <quotedata:OpenAmount>25.05</quotedata:OpenAmount>\n" +
                " <quotedata:DayHigh>25.46</quotedata:DayHigh>\n" +
                " <quotedata:DayLow>25.01</quotedata:DayLow>\n" +
                " <quotedata:StockVolume>20452658</quotedata:StockVolume>\n" +
                " <quotedata:PrevCls>25.31</quotedata:PrevCls>\n" +
                " <quotedata:ChangePercent>0.20</quotedata:ChangePercent>\n" +
                " <quotedata:FiftyTwoWeekRange>22.73 - 31.58</quotedata:FiftyTwoWeekRange>\n" +
                " <quotedata:EarnPerShare>2.326</quotedata:EarnPerShare>\n" +
                " <quotedata:PE>10.88</quotedata:PE>\n" +
                " <quotedata:CompanyName>Microsoft Corpora</quotedata:CompanyName>\n" +
                " <quotedata:QuoteError>false</quotedata:QuoteError>\n" +
                " </quotedata:StockQuoteEvent>\n" +
                " </quotedata:AllStockQuoteStream>";
 
        initContext = JNDIContext.getInstance().getInitContext();
        topicConnectionFactory = JNDIContext.getInstance().getTopicConnectionFactory();
        AllStockQuotesPublisher publisher = new AllStockQuotesPublisher();
        XMLStreamReader reader1 = StAXUtils.createXMLStreamReader(new ByteArrayInputStream(
                xmlElement1.getBytes()));
        StAXOMBuilder builder1 = new StAXOMBuilder(reader1);
        OMElement OMMessage1 = builder1.getDocumentElement();
        publisher.publish("AllStockQuotes", OMMessage1);
        XMLStreamReader reader2 = StAXUtils.createXMLStreamReader(new ByteArrayInputStream(
                xmlElement2.getBytes()));
        StAXOMBuilder builder2 = new StAXOMBuilder(reader2);
        OMElement OMMessage2 = builder2.getDocumentElement();
        publisher.publish("AllStockQuotes", OMMessage2);
        XMLStreamReader reader3 = StAXUtils.createXMLStreamReader(new ByteArrayInputStream(
                xmlElement3.getBytes()));
        StAXOMBuilder builder3 = new StAXOMBuilder(reader3);
        OMElement OMMessage3 = builder3.getDocumentElement();
        publisher.publish("AllStockQuotes", OMMessage3);
    }
    
    /**
     * Publish message to given topic
     *
     * @param topicName - topic name to publish messages
     * @param message   - message to send
     */
 
      public void publish(String topicName, OMElement message) {
        // create topic connection
        TopicConnection topicConnection = null;
        try {
            topicConnection = topicConnectionFactory.createTopicConnection();
            topicConnection.start();
        } catch (JMSException e) {
            System.out.println("Can not create topic connection." + e);
            return;
        }
        // create session, producer, message and send message to given destination(topic)
        // OMElement message text is published here.
        Session session = null;
        try {
            session = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageProducer producer = session.createProducer(topic);
            TextMessage jmsMessage = session.createTextMessage(message.toString());
            producer.send(jmsMessage);
            producer.close();
            session.close();
            topicConnection.stop();
            topicConnection.close();
        } catch (JMSException e) {
            System.out.println("Can not subscribe." + e);
        }
    }
}
