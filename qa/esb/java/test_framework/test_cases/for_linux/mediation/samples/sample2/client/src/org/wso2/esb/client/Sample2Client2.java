package org.wso2.esb.client;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

public class Sample2Client2{
	
	private static String trpUrl;

	public static void main(String[] args)throws AxisFault{

        trpUrl = args[0];	
	
	Options options = new Options();
        //options.setTo(new EndpointReference(trpUrl));
        options.setProperty("TransportURL", trpUrl);

        options.setAction("urn:getQuote");
        ServiceClient sender = new ServiceClient();
        sender.setOptions(options);
        OMElement result = sender.sendReceive(getPayload());
        System.out.println(result);
	}
	
	private static OMElement getPayload() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace(
                "http://services.samples", "m0");
        OMElement method = fac.createOMElement("getQuote", omNs);
        OMElement value1 = fac.createOMElement("request", omNs);
        OMElement value2 = fac.createOMElement("symbol", omNs);
        
        value2.addChild(fac.createOMText(value2, "MSFT"));
        value1.addChild(value2);
        method.addChild(value1);

        return method;
    }

}
