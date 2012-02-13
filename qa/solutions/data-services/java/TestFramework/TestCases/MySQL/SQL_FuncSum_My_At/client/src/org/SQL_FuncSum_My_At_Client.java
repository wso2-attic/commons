package org;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
//import org.apache.axis.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
public class SQL_FuncSum_My_At_Client {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws org.apache.axis2.AxisFault 
	{
		ServiceClient sc = new ServiceClient();
		
		Options opts = new Options(); // need to create soap option object for assign soap options.
		opts.setTo(new EndpointReference(args[0]));
		opts.setAction("urn:OP_Func_Sum"); // soap action field copied from the wsdl file
		sc.setOptions(opts);
		
		OMElement result = sc.sendReceive(CreatePayload());
		System.out.println(result);

	}
	
	public static OMElement CreatePayload() // this function create a message to send to server
	{
		OMFactory fac = OMAbstractFactory.getOMFactory(); // Create object(fac)from OM Factory this is used to create all the elements.
		OMNamespace omns = fac.createOMNamespace("http://ws.wso2.org/dataservice", "b"); // creating namespace to assign to message, b =  namespace
		OMElement OP1 = fac.createOMElement("OP_Func_Sum", omns); // Operation name
		return OP1;
	}
	
}
