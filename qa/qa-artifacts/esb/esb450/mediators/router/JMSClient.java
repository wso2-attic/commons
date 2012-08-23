/**
 * Created with IntelliJ IDEA.
 * User: evanthika
 * Date: 7/2/12
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

import java.rmi.RemoteException;

public class JMSClient {
    public static void main(String[] args) throws RemoteException {
        OMElement payload = createonewayPayload();
        ServiceClient serviceclient;

        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("/home/evanthika/WSO2/CARBON/CARBON4/ESB/19th-Aug-2012/wso2esb-4.5.0/repository/deployment/client/modules/", "/home/evanthika/WSO2/CARBON/CARBON4/ESB/19th-Aug-2012/wso2esb-4.5.0/repository/conf/axis2/axis2.xml");
        Options opt = new Options();
        opt.setTo(new EndpointReference ("jms:/RouterProxy?transport.jms.DestinationType=queue&transport.jms.ContentTypeProperty=Content-Type&java.naming.provider.url=tcp://localhost:61616&java.naming.factory.initial=org.apache.activemq.jndi.ActiveMQInitialContextFactory&transport.jms.ConnectionFactoryJNDIName=QueueConnectionFactory"));

        opt.setAction("urn:placeOrder");
        serviceclient = new ServiceClient(cc, null);
        serviceclient.setOptions(opt);
        serviceclient.sendReceive(payload);


    }

    public static OMElement createonewayPayload(){

       /* For placeOrder   */

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://services.samples", "ns");
        OMElement method = fac.createOMElement("placeOrder", omNs);
        OMElement order = fac.createOMElement("order", omNs);
        OMElement price = fac.createOMElement("price", omNs);
        OMElement quantity = fac.createOMElement("quantity", omNs);
        OMElement symbol = fac.createOMElement("symbol", omNs);

        symbol.addChild(fac.createOMText("MSFT"));
        quantity.addChild(fac.createOMText("100"));
        price.addChild(fac.createOMText("100.00"));

        order.addChild(symbol);
        order.addChild(quantity);
        order.addChild(price);

        method.addChild(order);

        return method;

    }

}

