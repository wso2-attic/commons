package org.wso2.mashup.client;
import java.io.ByteArrayInputStream;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

/* Purpose	:	This is a sample client to verify the handling of "file" host object of WSO2 Mashup Server.
 * Author	: 	Yumani Ranaweera
 */
 
public class clientWriteRead2 {
	public static String toEpr = "http://localhost:7762/services/samples/WriteRead2/";
	 //private static final String NEWFILE_PARAM = "VAL";	
	 private static final String WRITTINGREADING_PARAM = "Hello there! This is a test.";
	
	public static void main(String[] args) throws Exception{
		testEcho("newFile");
		testEcho1("WritingReading", WRITTINGREADING_PARAM);
	}
	
	
	public static void testEcho(String opName) throws XMLStreamException, AxisFault {
		final String param = "VAL";
		String str = "<" + opName + "Request>" + "<param>" + param + "</param>" + "</" + opName + "Request>";
		StAXOMBuilder staxOMBuilder = new StAXOMBuilder(new ByteArrayInputStream(str.getBytes()));
		OMElement payload = staxOMBuilder.getDocumentElement();

		Options options = new Options();
		options.setTo(new EndpointReference(toEpr));
		ServiceClient sender = new ServiceClient();
		sender.setOptions(options);
		
		OMElement result = sender.sendReceive(payload);
		System.out.println(result.toString());		
	}
	
	public static void testEcho1(String opName, String param) throws XMLStreamException, AxisFault {
		String str = "<" + opName + "Request>" + "<param>" + param + "</param>" + "</" + opName + "Request>";
		StAXOMBuilder staxOMBuilder = new StAXOMBuilder(new ByteArrayInputStream(str.getBytes()));
		OMElement payload = staxOMBuilder.getDocumentElement();

		Options options = new Options();
		options.setTo(new EndpointReference(toEpr));
		ServiceClient sender = new ServiceClient();
		sender.setOptions(options);
		
		OMElement result = sender.sendReceive(payload);
		System.out.println(result.toString());		
	}

}
