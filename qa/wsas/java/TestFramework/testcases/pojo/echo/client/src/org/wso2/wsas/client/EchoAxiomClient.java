package org.wso2.wsas.client;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;


/* This is a sample client which invokes an echo service  
 * using Axis2 client API 
 * Written by Charitha Kankanamge 
 */

public class EchoAxiomClient {
	private static String toEpr = "http://localhost:9763/services/EchoService";
	public static void main(String[] args)throws AxisFault{
		
		Options options = new Options();
        options.setTo(new EndpointReference(toEpr));
        options.setAction("urn:echoString");
        ServiceClient sender = new ServiceClient();
        sender.setOptions(options);
        OMElement result = sender.sendReceive(getPayload());
        //System.out.println(result);

        try {
            XMLStreamWriter writer = XMLOutputFactory.newInstance()
                    .createXMLStreamWriter(System.out);
            result.serialize(writer);
            writer.flush();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        }
    	
	}
	
	private static OMElement getPayload() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace(
                "http://service.wsas.wso2.org", "wsasns");
        OMElement method = fac.createOMElement("echo", omNs);
        OMElement value = fac.createOMElement("Text", omNs);
        value.addChild(fac.createOMText(value, "Invoking echo service using Axis2 client API"));
        method.addChild(value);

        return method;
    }

}
