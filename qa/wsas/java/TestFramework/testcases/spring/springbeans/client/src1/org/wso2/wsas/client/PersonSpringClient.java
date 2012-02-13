/* This is a sample client which invokes Spring bean
 * included in the Spring service
 * Written by Charitha Kankanamge
 */

package org.wso2.wsas.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.wso2.wsas.service.PersonBeanStub;

public class PersonSpringClient {
	
	public static void main(String[] args)throws AxisFault{

		PersonBeanStub stub = new PersonBeanStub();
		PersonBeanStub.SetName name = new PersonBeanStub.SetName();
		PersonBeanStub.SetAddress address = new PersonBeanStub.SetAddress();
		PersonBeanStub.SetAge age = new PersonBeanStub.SetAge();
		
		name.setName("Charitha Kankanamge");
		address.setAddress("224, Horana Road, Kottawa");
		age.setAge(30);
		
		try {
			stub.setName(name);
			stub.setAddress(address);
			stub.setAge(age);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		
		try {
			PersonBeanStub.GetNameResponse nameresponse = stub.getName();
			System.out.println(nameresponse.get_return());
			PersonBeanStub.GetAddressResponse addressresponse = stub.getAddress();
			System.out.println(addressresponse.get_return());
			PersonBeanStub.GetAgeResponse ageresponse = stub.getAge();
			System.out.println(ageresponse.get_return());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
	}

}
