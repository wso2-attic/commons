/* This is a sample client to test Access throttling 
 * Written by Charitha Kankanamge
 */
package org.wso2.wsas.client;
import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.wso2.wsas.service.Access_throttling_serviceStub;

public class Access_throttling_client{

	public static void main(String args[])throws AxisFault{

		Access_throttling_serviceStub stub = new Access_throttling_serviceStub();
		Access_throttling_serviceStub.EchoString request = new Access_throttling_serviceStub.EchoString();

		request.setS("Verifying access throttling");
		for (int i = 0; i < 11; i++) {
			try {
				Access_throttling_serviceStub.EchoStringResponse response = stub.echoString(request);
				System.out.println(response.get_return());
				
				
				} catch (RemoteException e) {
				if (e.getMessage().indexOf("quota")>0){
					System.out.println("###################################################");
					System.out.println("Throttling successful!!!");
					System.out.println("###################################################");
				}
				//e.printStackTrace();
			}
		}
		
	}
}