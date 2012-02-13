package org.wso2.esb.client;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;


public class PoxToSoap12Client {


	private static String toEpr;

	public static void main(String[] args)throws AxisFault{
		
	toEpr = args[0];
		
	Options options = new Options();
        options.setTo(new EndpointReference(toEpr));
        options.setAction("urn:add");
        options.setProperty(Constants.Configuration.ENABLE_REST , Constants.VALUE_TRUE);
        ServiceClient sender = new ServiceClient();
        sender.setOptions(options);
        OMElement result = sender.sendReceive(getPayload());
        System.out.println(result);
	}
	
	private static OMElement getPayload() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace(
                "http://service.esb.wso2.org", "ns");
        OMElement method = fac.createOMElement("add", omNs);
        OMElement value1 = fac.createOMElement("x", omNs);
        OMElement value2 = fac.createOMElement("y", omNs);
        
        value1.addChild(fac.createOMText(value1, "10"));
        value2.addChild(fac.createOMText(value2, "10"));
        method.addChild(value1);
        method.addChild(value2);

        return method;
    }

}
