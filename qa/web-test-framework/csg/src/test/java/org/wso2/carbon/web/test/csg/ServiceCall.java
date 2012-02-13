package org.wso2.carbon.web.test.csg;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
//import org.apache.axis.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import com.sun.deploy.net.proxy.StaticProxyManager;

public class ServiceCall {

	public String ServiceClient(String epr,String soapAction,String namespace,String operationName)throws org.apache.axis2.AxisFault
	{
		ServiceClient sc = new ServiceClient();
        String outResult = "";
		Options opts = new Options(); // need to create soap option object for assign soap options.
		opts.setTo(new EndpointReference(epr));
		opts.setAction(soapAction); // soap action field copied from the wsdl file
		sc.setOptions(opts);
        for(int i=0;i<=20;i++)
        {
		OMElement result = sc.sendReceive(CreatePayload(namespace,operationName));
		System.out.println(result);
        outResult = result.toString();
        }
         return outResult;

	}

	public OMElement CreatePayload(String namespace,String operationName) // this function create a message to send to server
	{
		OMFactory fac = OMAbstractFactory.getOMFactory(); // Create object(fac)from OM Factory this is used to create all the elements.
		OMNamespace omns = fac.createOMNamespace(namespace, "b"); // creating namespace to assign to message, b =  namespace
		OMElement OP1 = fac.createOMElement(operationName, omns); // Operation name
		return OP1;
	}


}



