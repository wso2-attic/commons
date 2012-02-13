/* This is a sample client to invoke JAXWS based service
 * Written by Charitha Kankanamge
 */

package org.wso2.wsas.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.qa.wso2.jaxws.JaxWSEchoServiceStub;


public class JaxWSClient {
	
	public static void main(String[] args)throws AxisFault{
		JaxWSEchoServiceStub stub = new JaxWSEchoServiceStub();
		JaxWSEchoServiceStub.EchoStringWebMethod request = new JaxWSEchoServiceStub.EchoStringWebMethod();
		request.setParam0("JAXWS support in WSO2WSAS");
		
		try {
			JaxWSEchoServiceStub.EchoStringWebMethodResponse response = stub.echoStringWebMethod(request);
			System.out.println(response.get_return());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

}
}
