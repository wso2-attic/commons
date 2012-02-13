package org;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
//import org.apache.axis.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
public class SQL_Update_My_Client {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws org.apache.axis2.AxisFault 
	{
		ServiceClient sc = new ServiceClient();
		
		Options opts = new Options(); // need to create soap option object for assign soap options.
		opts.setTo(new EndpointReference(args[0]));
		
		opts.setAction("urn:Select_Data");
		sc.setOptions(opts);
		OMElement Result0 = sc.sendReceive(CreatePayload2());
		System.out.println(Result0);
		String ResultString = Result0.toString();
		System.out.println(ResultString);
	/*	if ( false == ResultString.contains("\"http://ws.wso2.org/dataservice\"/>"))  
				{
					if (true == ResultString.contains("EmployeeID>75"))
					{}
			
		}
		*/
		opts.setAction("urn:DeleteRow");
		sc.setOptions(opts);
		OMElement Result5 = sc.sendReceive(CreatePayload4());
		System.out.println(Result5);
		
		opts.setAction("urn:OP_insertData"); // soap action field copied from the wsdl file
		sc.setOptions(opts);
		OMElement result = sc.sendReceive(CreatePayload());
		System.out.println(result);
		
		opts.setAction("urn:Select_Data");
		sc.setOptions(opts);
		OMElement Result2 = sc.sendReceive(CreatePayload2());
		System.out.println(Result2);
		
		opts.setAction("urn:Update_Data");
		sc.setOptions(opts);
		OMElement Result3 = sc.sendReceive(CreatePayload3());
		System.out.println(Result3);
		
		opts.setAction("urn:Select_Data");
		sc.setOptions(opts);
		OMElement Result4 = sc.sendReceive(CreatePayload2());
		System.out.println(Result4);

	}
	
	public static OMElement CreatePayload() // this function create a message to send to server
	{
		OMFactory fac = OMAbstractFactory.getOMFactory(); // Create object(fac)from OM Factory this is used to create all the elements.
		OMNamespace omns = fac.createOMNamespace("http://ws.wso2.org/dataservice", "b"); // creating namespace to assign to message, b =  namespace
		OMElement OP1 = fac.createOMElement("OP_insertData", omns); // Operation name
		return OP1;
	}
	
	public static OMElement CreatePayload2() 
	{
		OMFactory MyFac = OMAbstractFactory.getOMFactory();
		OMNamespace MyName = MyFac.createOMNamespace("http://ws.wso2.org/dataservice", "e");
		OMElement OP2 = MyFac.createOMElement("Select_Data", MyName);
		return OP2;
		
	}
	
	public static OMElement CreatePayload3() 
	{
		OMFactory MyFac = OMAbstractFactory.getOMFactory();
		OMNamespace MyName = MyFac.createOMNamespace("http://ws.wso2.org/dataservice", "e");
		OMElement OP3 = MyFac.createOMElement("Update_Data", MyName);
		return OP3;
		
	}
	
	public static OMElement CreatePayload4() 
	{
		OMFactory MyFac = OMAbstractFactory.getOMFactory();
		OMNamespace MyName = MyFac.createOMNamespace("http://ws.wso2.org/dataservice", "e");
		OMElement OP4 = MyFac.createOMElement("DeleteRow", MyName);
		return OP4;
		
	}
}
