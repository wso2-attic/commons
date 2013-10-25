package wso2.test;

//import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
//import org.apache.axiom.om.OMFactory;
//import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.engine.AxisServer;
import org.wso2.carbon.event.client.broker.BrokerClient;
import org.wso2.carbon.event.client.broker.BrokerClientException;
import org.wso2.carbon.event.client.stub.generated.authentication.AuthenticationExceptionException;
import java.rmi.RemoteException;

import javax.xml.stream.XMLStreamException;
public class PubSubClient {
    private AxisServer axisServer;
    private BrokerClient brokerClient;
    public void start() {
        try {
            KeyStoreUtil.setTrustStoreParams();
            this.axisServer = new AxisServer();
            //this.axisServer.deployService(EventSinkService.class.getName());
            this.brokerClient = new BrokerClient("https://localhost:9444/services/EventBrokerService", "admin", "admin");
            // give time to start the simple http server
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        } catch (AxisFault axisFault) {
            System.out.println("Can not start the server");
        } catch (AuthenticationExceptionException e) {
            e.printStackTrace();
        }
    }
    public String subscribe() {
        // set the properties for ssl
        try {
            return this.brokerClient.subscribe("foo/bar" , "http://localhost:6060/axis2/services/EventSinkService/receive");
        } catch (BrokerClientException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void publish(){
        try {
            this.brokerClient.publish("wso2mb_inputService", getOMElementToSend());
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }
    public void unsubscribe(String subscriptionID){
        try {
            this.brokerClient.unsubscribe(subscriptionID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void stop(){
        try {
            this.axisServer.stop();
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }
    public static void main(String[] args) {
        PubSubClient pubSubClient = new PubSubClient();
        pubSubClient.start();
        String subscriptionId = pubSubClient.subscribe();
        pubSubClient.publish();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {}
        pubSubClient.unsubscribe(subscriptionId);
        pubSubClient.stop();
    }
    private OMElement getOMElementToSend() {
       // OMFactory omFactory = OMAbstractFactory.getOMFactory();
       // OMNamespace omNamespace = omFactory.createOMNamespace("http://ws.cdyne.com/", "quotedata");
       //  OMElement receiveElement = omFactory.createOMElement("receive", omNamespace);
       //  OMElement messageElement = omFactory.createOMElement("message", omNamespace);
       
     
      
        String xmlElement1 = "<quotedata:AllStockQuoteStream xmlns:quotedata=\"http://ws.cdyne.com/\">\n"
                + "         <quotedata:StockQuoteEvent>\n"
                + "              <quotedata:StockSymbol>MSFT</quotedata:StockSymbol>\n"
                + "              <quotedata:LastTradeAmount>26.36</quotedata:LastTradeAmount>\n"
                + "              <quotedata:StockChange>0.05</quotedata:StockChange>\n"
                + "              <quotedata:OpenAmount>25.05</quotedata:OpenAmount>\n"
                + "              <quotedata:DayHigh>25.46</quotedata:DayHigh>\n"
                + "              <quotedata:DayLow>25.01</quotedata:DayLow>\n"
                + "              <quotedata:StockVolume>20452658</quotedata:StockVolume>\n"
                + "              <quotedata:PrevCls>25.31</quotedata:PrevCls>\n"
                + "              <quotedata:ChangePercent>0.20</quotedata:ChangePercent>\n"
                + "              <quotedata:FiftyTwoWeekRange>22.73 - 31.58</quotedata:FiftyTwoWeekRange>\n"
                + "              <quotedata:EarnPerShare>2.326</quotedata:EarnPerShare>\n"
                + "              <quotedata:PE>10.88</quotedata:PE>\n"
                + "              <quotedata:CompanyName>Microsoft Corpora</quotedata:CompanyName>\n"
                + "              <quotedata:QuoteError>false</quotedata:QuoteError>\n"
                + "        </quotedata:StockQuoteEvent>\n"
                + "</quotedata:AllStockQuoteStream>";
      
        try {
            return AXIOMUtil.stringToOM(xmlElement1);
           
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            

       //  messageElement.setText(xmlElement1);
       //  receiveElement.addChild(messageElement);
        return null;
       
    }
}