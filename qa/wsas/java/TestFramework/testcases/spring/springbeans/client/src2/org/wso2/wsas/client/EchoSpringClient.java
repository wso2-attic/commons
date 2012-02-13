/* This is a sample client which invokes EchoSpring service
 * included in the spring service.
 * Written by Charitha Kankanamge
 */
package org.wso2.wsas.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.wso2.wsas.service.EchoBeanStub;

public class EchoSpringClient {
	
	public static void main(String[] args)throws AxisFault{

		EchoBeanStub stub = new EchoBeanStub();
		EchoBeanStub.EchoString request = new EchoBeanStub.EchoString();
		request.setS("Welcome to the world of Spring + WSAS");
		
		try {
			EchoBeanStub.EchoStringResponse response = stub.echoString(request);
			System.out.println(response.get_return());
		} catch (RemoteException e) {
		}
		
		
	}

}
